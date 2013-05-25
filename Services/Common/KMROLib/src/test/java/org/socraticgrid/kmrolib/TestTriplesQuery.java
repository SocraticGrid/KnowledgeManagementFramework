/*
 * ****************************************************************************************************************
 *  *
 *  * Copyright (C) 2012 by Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is
 *  * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and limitations under the License.
 *  *
 *  ****************************************************************************************************************
 *
 * ****************************************************************************************************************
 *  * Socratic Grid contains components to which third party terms apply. To comply with these terms, the following
 *  * notice is provided:
 *  *
 *  * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 *  * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 *  * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 *  * the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *  *     following disclaimer.
 *  * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *  *     following disclaimer in the documentation and/or other materials provided with the distribution.
 *  * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or
 *  *     promote products derived from this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 *  * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  * END OF TERMS AND CONDITIONS
 *  *
 *  ****************************************************************************************************************
 */

package org.socraticgrid.kmrolib;


import java.util.Map;
import com.google.gson.JsonObject;
import gov.va.medora.mdws.emrsvc.TaggedTextArray;
import gov.va.medora.mdws.emrsvc.UserTO;
import gov.va.medora.mdws.emrsvc.EmrSvc;
import gov.va.medora.mdws.emrsvc.DataSourceTO;
import gov.va.medora.mdws.emrsvc.ArrayOfDataSourceTO;
import gov.va.medora.mdws.emrsvc.DataSourceArray;
import gov.va.medora.mdws.emrsvc.ArrayOfSiteTO;
import gov.va.medora.mdws.emrsvc.SiteTO;
import gov.va.medora.mdws.emrsvc.SiteArray;
import gov.va.medora.mdws.emrsvc.RegionTO;
import java.util.List;
import gov.va.medora.mdws.emrsvc.ArrayOfRegionTO;
import gov.va.medora.mdws.emrsvc.RegionArray;
import gov.va.medora.mdws.emrsvc.EmrSvcSoap;
import java.io.StringReader;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;

//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;



/**
 *
 * @author nhin
 */
public class TestTriplesQuery {

    public TestTriplesQuery() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }



    private void webServiceTest(EmrSvcSoap webService) {

        RegionArray regionArray = webService.getVHA();
        ArrayOfRegionTO regions = regionArray.getRegions();
        List<RegionTO> regionTOList = regions.getRegionTO();

        for (RegionTO region : regionTOList) {
            System.out.println("Region: " + region.getName());
            SiteArray regionSites = region.getSites();
            ArrayOfSiteTO sites = regionSites.getSites();

            for (SiteTO site : sites.getSiteTO()) {

                System.out.println(site.getName());

                DataSourceArray siteDataSources = site.getDataSources();
                ArrayOfDataSourceTO siteDataSourcesItems = siteDataSources.getItems();
                List<DataSourceTO> dataSourceTO = siteDataSourcesItems.getDataSourceTO();

                for (DataSourceTO dataSource : dataSourceTO) {
                    System.out.println(dataSource.getProvider() + ":" + dataSource.getPort());
                }
            }
        }
    }

    private boolean loginToSite(EmrSvcSoap webService,
            String site, String user, String pwd, String context) throws Exception {

        // -------
        // CONNECT
        // -------
        DataSourceArray dataSourceArray = webService.connect(site);

        ArrayOfDataSourceTO dataSourceArrayItems = dataSourceArray.getItems();

        List<DataSourceTO> dataSourceTOList = dataSourceArrayItems.getDataSourceTO();

        for (DataSourceTO dataSourceTO : dataSourceTOList) {
            System.out.println(dataSourceTO.getProvider()+":"+dataSourceTO.getPort());
//            System.out.println(dataSourceTO.getWelcomeMessage());
        }
        if (dataSourceArray.getFault() == null) {
             System.out.println("MDWS CONNECT successful");
        } else {
             System.out.println("MDWS CONNECT failed with: "+ dataSourceArray.getFault().getStackTrace());
             return false;
        }

        // -------
        // LOGIN
        // -------
        UserTO userTO = webService.login(user, pwd, context);

        System.out.println("userTO.getName(): "+ userTO.getName());

        if (userTO.getFault() == null) {

            System.out.println("Login successful");

        } else {
            System.out.println("Login failed with: "+ userTO.getFault().getStackTrace());
            return false;
        }
        return true;
    }


    private TaggedTextArray getNhinData(EmrSvcSoap webService, String domain) {
        return webService.getNhinData(domain);
    }

    @Test
    public void testMDWS_getMedsByPatient() throws Exception{

        System.out.println("\n======================");
        System.out.println("testMDWS_getMedsByPatient");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.104//mdws2/EmrSvc.asmx";
        String dataSourceType = "MDWS";
        String patientId = "100013"; //100022";
        String user = "1programmer";
        String password = "programmer1";
        String siteId = "102";
        String context = "";

        String domain = "med";

//        //=================================
//        // GET A SERVICE
//        //=================================
//        EmrSvc emrService = new EmrSvc();
//        EmrSvcSoap webService = emrService.getEmrSvcSoap();

        //=================================
        //=================================
//        KMROPatientTriples triples = new KMROPatientTriples(dataSourceURL, dataSourceType);
//        String response = triples.getMDWSData(patientId, domain, user, password, siteId);
//        System.out.println(response + "\n==========\n");

        //=================================
        //=================================
        MDWSQueryUtil request = new MDWSQueryUtil(dataSourceURL, user, password);
        String response = request.getTransformedData(patientId, siteId, domain);
        
        System.out.println("\n==========\nRESPONSE=\n"+response);

        //=================================
        // UPPER module will
        // COnvert Json string to Class so can add more attributes.
        // Then convert back to string.
        //=================================

        System.out.println("\n==========\n");

    }


    public void printXpathResult(Object result){
        NodeList nodes = (NodeList) result;
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println("NODE: "+ nodes.item(i).getNodeValue());
        }
    }

    private static Document convertStringToDocument(String xmlStr) {

        //Create DocumentBuilderFactory for reading xml file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) );
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testMDWS_getProblemsByPatient() throws Exception{

        System.out.println("\n======================");
        System.out.println("testMDWS_getProblemsByPatient");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.104/mdws2/EmrSvc.asmx";
        String dataSourceType = "MDWS";
        String patientId = "100013";
        String user = "1programmer";
        String password = "programmer1";
        String siteId = "102";
        String context = "";

        String domain = "problem";
        String response = null;

        
//        //=================================
//        KMROPatientTriples triples = new KMROPatientTriples(dataSourceURL, dataSourceType);
//        response = triples.getMDWSData(patientId, domain, user, password, siteId);
//
//        System.out.println(response + "\n==========\n");

        //=================================
        MDWSQueryUtil request = new MDWSQueryUtil(dataSourceURL, user, password);
        response = request.getTransformedData(patientId, siteId, domain);
        System.out.println(response + "\n==========\n");
    }

    @Test
    public void testMDWS_getLabsByPatient() throws Exception{

        System.out.println("\n======================");
        System.out.println("testMDWS_getLabsByPatient");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.48/mdws2/EmrSvc.asmx";
        String dataSourceType = "MDWS";
        String patientId = "100022";
        String user = "1programmer";
        String password = "programmer1";
        String siteId = "102";
        String context = "";

        String domain = "lab";

        //=================================
        // GET A SERVICE
        //=================================
        EmrSvc emrService = new EmrSvc();
        EmrSvcSoap webService = emrService.getEmrSvcSoap();
//
//        //=================================
//        // Test SITE pull
//        //=================================
//        webServiceTest(webService);  // sample Testing mdws ws connection
//
//        //=================================
//        // Test CONNECT/LOGIN
//        //=================================
//        loginToSite(webService, siteId, user, password, context);
//
//        //=================================
//        // Test SELECT patient
//        //=================================
//        webService.select(patientId);
//
//        //=================================
//        // Test get labs
//        //=================================
//        TaggedTextArray labs = webService.getNhinData("lab");
//
//        System.out.println("TOTAL LABS= "+ labs.getCount());
//
//        ArrayOfTaggedText labsArray = labs.getResults();
//
//        webService.disconnect();

        //=================================
        //=================================
        KMROPatientTriples triples = new KMROPatientTriples(dataSourceURL, dataSourceType);
        String response = triples.getMDWSData(patientId, domain, user, password, siteId);

        System.out.println(response + "\n==========\n");
    }

    @Test
    public void testFMQL_getDemographics() throws Exception{

        System.out.println("\n======================");
        System.out.println("testFMQL_getDemographics");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.254:3030/data/sparql?query=";
        String dataSourceType = "FMQL";
//        String dataRetFormat = "xml";

        String factId = "2-8";

        KMROPatientTriples triples = new KMROPatientTriples(dataSourceURL, dataSourceType);

        String jsonReply = triples.getDemographicsByPatient(factId);

        System.out.println(jsonReply + "\n==========\n");

    }

    @Test
    public void testJENA_getRXNORM() throws Exception {

        String JENA_EP = "http://192.168.1.111:3030/data/sparql";
        SPARQLQueryUtil jena = new SPARQLQueryUtil(JENA_EP);

        String jsonResp = null;

        System.out.println("=======================");
        jsonResp = jena.getRXNORM("4008592", "json");
        System.out.println(jsonResp);

//        System.out.println("=======================");
//        jsonResp = jena.getRXNORM("4008159", "xml");
//        System.out.println(jsonResp);
//
//        System.out.println("=======================");
//        jsonResp = jena.getRXNORM("4008159", null);
//        System.out.println(jsonResp);

//Amlodipine

    }

    @Test
    public void testJENA_getDrugs() throws Exception {

        String JENA_EP = "http://192.168.1.111:3030/data/sparql?query=";
        SPARQLQueryUtil jena = new SPARQLQueryUtil(JENA_EP);

        System.out.println("=======================");
        JsonObject jsonResp = jena.getDrugsAsJson("780_52");

        List<Map<String, String>> map = jena.processListReply(jsonResp);


        System.out.println("=======================");
        List<Map<String, String>> drugsMap = jena.getDrugsAsMap("780_52");


        System.out.println("=======================");
        List<Map<String, String>> newdrugsMap = jena.getNewDiagnosesAsMap("asth");


        for (Map<String, String> e : newdrugsMap) {
            System.out.println( e.values().toString());
        }

        System.out.println("=======================");
    }

    @Test
    public void testJENA_getAssociatedTriples() throws Exception {

        String JENA_EP = "http://192.168.1.111:3030/data/sparql?query=";
        SPARQLQueryUtil jena = new SPARQLQueryUtil(JENA_EP);

        System.out.println("=======================");
        List<Map<String, String>> triples = jena.getMedAssociationsAsMap("197853");


        System.out.println("=======================");
    }


    /*
     * MDWS RETURN (domain=patient)
     
    <?xml version="1.0" encoding="UTF-8"?>
    <TaggedTextArray>
       <count>1</count>
       <results>
          <TaggedText>
             <tag>102</tag>
             <text>
                <results version="1.1" timeZone="-0500">
                   <demographics total="1">
                      <patient>
                         <bid value="B0008" />
                         <dob value="2350407" />
                         <facilities>
                            <facility code="500" name="CAMP MASTER" latestDate="3020415" domain="VEHU.FO-ALBANY.MED.VA.GOV" />
                         </facilities>
                         <familyName value="BCMA" />
                         <fullName value="BCMA,EIGHT" />
                         <gender value="M" />
                         <givenNames value="EIGHT" />
                         <id value="100022" />
                         <lrdfn value="418" />
                         <sc value="0" />
                         <ssn value="666330008" />
                         <veteran value="1" />
                      </patient>
                   </demographics>
                </results>
             </text>
          </TaggedText>
       </results>
    </TaggedTextArray>

    */
    @Test
    public void testMDWS_getDemographicsByPatient() throws Exception{

        System.out.println("\n======================");
        System.out.println("testMDWS_getDemographicsByPatient");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.104/mdws2/EmrSvc.asmx";
        String dataSourceType = "MDWS";
        String patientId = "100013";
        String user = "1programmer";
        String password = "programmer1";
        String siteId = "102";
        String context = "";

        String domain = "patient";

        String response = null;

//        //=================================
//        // TRY using direct call
//        //=================================
//        KMROPatientTriples triples = new KMROPatientTriples(dataSourceURL, dataSourceType);
//        response = triples.getMDWSData(patientId, domain, user, password, siteId);
//
//        System.out.println(response + "\n==========\n");

        //=================================
        // TRY using transform wrapped call
        //=================================
        MDWSQueryUtil request = new MDWSQueryUtil(dataSourceURL, user, password);
        response = request.getTransformedData(patientId, siteId, domain);

        System.out.println("\n==========\nRESPONSE=\n"+response);

    }

    @Test
    public void testMDWS_getDataByPatient() throws Exception{

        System.out.println("\n======================");
        System.out.println("testMDWS_getDataByPatient");
        System.out.println("======================");

        String dataSourceURL = "http://172.31.5.104//mdws2/EmrSvc.asmx";
        String dataSourceType = "MDWS";
        String patientId ="100013";
        String user = "1programmer";
        String password = "programmer1";
        String siteId = "102";
        String context = "";

        // patient;allergy;problem;vital;lab;med;xray;consult;procedure;surgery;document;encounter;factor
        
        String domain = "encounter";

        //=================================
        MDWSQueryUtil request = new MDWSQueryUtil(dataSourceURL, user, password);
        String response = request.getData(patientId, siteId, domain);

        System.out.println("\n==========\nRESPONSE=\n"+response);



    }
}
