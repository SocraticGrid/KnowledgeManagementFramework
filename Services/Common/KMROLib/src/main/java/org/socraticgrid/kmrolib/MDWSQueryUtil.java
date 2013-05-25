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

import java.net.*;
import java.io.*;
import java.util.*;
import gov.va.medora.mdws.emrsvc.ArrayOfDataSourceTO;
import gov.va.medora.mdws.emrsvc.ArrayOfTaggedText;
import gov.va.medora.mdws.emrsvc.DataSourceArray;
import gov.va.medora.mdws.emrsvc.DataSourceTO;
import gov.va.medora.mdws.emrsvc.EmrSvc;
import gov.va.medora.mdws.emrsvc.EmrSvcSoap;
import gov.va.medora.mdws.emrsvc.PatientTO;
import gov.va.medora.mdws.emrsvc.TaggedText;
import gov.va.medora.mdws.emrsvc.TaggedTextArray;
import gov.va.medora.mdws.emrsvc.UserTO;


import javax.xml.ws.BindingProvider;
import org.socraticgrid.documenttransformer.Transformer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;


/*
 * A Utility for accessing a KMRO-conformant triple store.
 *
 * To use:
 * - down gson JAR and make sure it is in your classpath
 * - crude invocation if JAR for gson is in this file's directory
 *   - javac -classpath 'gson-VERSION.jar' KMROPatientTriples.java
 *   - java -classpath '.:gson-VERSION.jar' KMROPatientTriples
 * Look at invocation code in 'main' and copy in your own code.
 *
 * Background:
 * KMR has three triple stores. Two (KMRO, KMRO10) are accessible through
 * this utility.
 * - DANNOCOPY_EP: Copy of Danno (VS scheme)
 * - KMRO_EP: Danno patients in KMRO
 * - KMRO10_EP: best of KMRO Danno patients. Hand done until end.
 *
 * To process JSON, this uses gson:
 * - https://sites.google.com/site/gson/gson-user-guide
 *
 * SVN Ontology: http://184.191.173.238:90/svn/socraticgrid/trunk/projects/KMR/Ontology/KMR_Ontology.ttl
 *
 */

public class MDWSQueryUtil {

    //--------------------------------------------------------------------
    // FMQL endpoint:  http://172.31.5.104:8080/fmqlEP?
    //      i.e. http://172.31.5.104:8080/fmqlEP?fmql=DESCRIBE 2-9
    //
    // MDWS endpoint:  http://172.31.5.104/mdws2/EmrSvc.asmx?
    //      i.e. You have to issue a SELECT of patient DFN first, before
    //      executing any other requests.
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/select?DFN=100022
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllOrders
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllMeds
    //
    // JENA endpoint:  http://172.31.5.104/ds/sparql?query=
    //      i.e.   http://172.31.5.104/ds/sparql?query=SELECT * {?s ?p ?o}
    //--------------------------------------------------------------------

    Boolean trace = false;

    private String mdwsEP;

    private String user = null;
    private String pwd =  null;

    private static ApplicationContext ctx;
    private static Transformer tx;

    //--------------------------------------------------------------------
    // CONSTRUCTORs
    //--------------------------------------------------------------------
//    public MDWSQueryUtil() {
//    }

    public MDWSQueryUtil(String serviceEndpoint, String user, String password) {
        this.mdwsEP = serviceEndpoint;
        this.user = user;
        this.pwd = password;


        MDWSQueryUtil.ctx = new FileSystemXmlApplicationContext("file:/home/nhin/Properties/MDWSDocumentTransformer.xml");
        MDWSQueryUtil.tx = (Transformer) ctx.getBean("Xform");
    }

    //--------------------------------------------------------------------
    // PROCESS
    //--------------------------------------------------------------------

    private boolean loginToSite(EmrSvcSoap webService, String siteId) throws Exception {

        // -------
        // CONNECT
        // -------
        DataSourceArray dataSourceArray = webService.connect(siteId);

//        ArrayOfDataSourceTO dataSourceArrayItems = dataSourceArray.getItems();
//        List<DataSourceTO> dataSourceTOList = dataSourceArrayItems.getDataSourceTO();
//            for (DataSourceTO dataSourceTO : dataSourceTOList) {
//                System.out.println(dataSourceTO.getProvider()+":"+dataSourceTO.getPort());
//                System.out.println(dataSourceTO.getWelcomeMessage());
//            }
        if (dataSourceArray.getFault() == null) {
             System.out.println("MDWS CONNECT successful");
        } else {
             System.out.println("MDWS CONNECT failed with: "+ dataSourceArray.getFault().getStackTrace());
             return false;
        }

        // -------
        // LOGIN
        // -------
        UserTO userTO = webService.login(this.user, this.pwd, "");

        System.out.println("userTO.getName(): "+ userTO.getName());

        if (userTO.getFault() == null) {
            
            System.out.println("Login successful");

        } else {
            System.out.println("MDWS LOGIN failed with: "+ userTO.getFault().getInnerMessage());
            return false;
        }
        return true;
    }

    /**
     * Get Patient domain data from MDWS as a JSON string (with VUID codes),
     * send to Transform to convert to RXNORM, then return resulting json string.
     * 
     * MDWS DOMAINS:  patient;allergy;problem;vital;lab;med;xray;consult;procedure;surgery;document;encounter;factor
     * 
     * @param patientId
     * @param siteId
     * @param domain
     * @return Stirng of Json formatted patient medication.
     * @throws Exception
     */
    public String getTransformedData (String patientId, String siteId, String domain)
    throws Exception
    {
        //-----------------------------------------
        // REMAP MDWS domain name to DcumentTransformer domain name
        //-----------------------------------------
        Map<String, String> domainMap = new HashMap<String, String>();
        domainMap.put("med", "Medications");
        domainMap.put("problem", "Problems");
        domainMap.put("patient", "Demographics");
        // .. ADD MORE DOMAIN LATER AS KNOWN...

        //-----------------------------------------
        // GET PATIENT MDWS DATA (VUID codes)
        //-----------------------------------------
        String mdwsXML = getData(patientId, siteId, domain);
        
        System.out.println("MDWS-VUID=\n"+mdwsXML);

        System.out.println("====> TRANSFORMING MDWS:"+domain+" --> TRSFRM:"+domainMap.get(domain));
        //-----------------------------------------
        // TRANSFORM json list of VUID codes to RXNORM codes
        //-----------------------------------------
        // Convert VUID to RXNORM
//        ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:MDWSDocumentTransformer.xml");
//        Transformer tx = (Transformer) ctx.getBean("Xform");
        Resource res = new ByteArrayResource(mdwsXML.getBytes());

        String result = "";
        try
        {
            result = MDWSQueryUtil.tx.transform(domainMap.get(domain), res.getInputStream());
        }
        catch (IOException ex)
        {
            throw ex;
            //Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println("TRANSFORM=\n"+result);

        return result;
    }

    public String getData (String patientId, String siteId, String domain) throws Exception
    {
        String result = null;

        EmrSvc emrService = new EmrSvc();

        EmrSvcSoap webService = emrService.getEmrSvcSoap();
        ((BindingProvider) webService).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY, this.mdwsEP);

        System.out.println("EP= " + ((BindingProvider) webService).getEndpointReference().toString());

        // -------------------------------------
        // CONNECT/LOGIN and if successful,
        // then SELECT patient and if successful
        // then select data
        // -------------------------------------
        if (loginToSite(webService, siteId)) {

            PatientTO selectedPatient = webService.select(patientId);

            if (selectedPatient.getFault() != null) {
                result = null;
                System.out.println("MDWS SELECT: PatientId "+ patientId + " not found.");
                System.out.println(selectedPatient.getFault().getMessage());

            } else {
                System.out.println("FOundPatient= "+ selectedPatient.getPatientName());

                TaggedTextArray foundData = webService.getNhinData(domain);
                System.out.println("TOTAL foundData= "+ foundData.getCount());


                ArrayOfTaggedText foundDataArray = foundData.getResults();
                List<TaggedText> foundDataList = foundDataArray.getTaggedText();

                System.out.println("TOTAL getresult= "+ foundData.getResults().getTaggedText().size());

//                StringBuilder sb = new StringBuilder();
//                for (TaggedText tag : foundDataList) {
//                    sb.append(tag.getText());
//                }


                StringBuilder sb = new StringBuilder();
                sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                sb.append("<TaggedTextArray xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://mdws.medora.va.gov/EmrSvc\">");
                sb.append("<count>");
                sb.append(Integer.toString(foundData.getCount()));
                sb.append("</count><results>");
                sb.append("<TaggedText><tag>");
                sb.append(siteId);
                sb.append("</tag><text>");
                for (TaggedText tag : foundDataList) {
                    sb.append(tag.getText());
                }
                sb.append("</text></TaggedText></results></TaggedTextArray>");

                result = sb.toString(); //foundData.getResults().getTaggedText().get(0).getText();
            }
            
            webService.disconnect();
            
        } else {
            result = null;
        }

        return result;

    }

    /**
     * This will send the FMQL format query to the interface, and get a
     * response as a BufferedReader object.
     * 
     * @param query
     * @return
     * @throws Exception
     */
    public BufferedReader singleRequest(String query) throws Exception {

        System.out.println("query= "+ query);

//        String enc = URLEncoder.encode(query, "UTF-8");
//        System.out.println("enc= "+ enc);

        String mdwsCall = this.mdwsEP + query; //URLEncoder.encode(query, "UTF-8");
                
        System.out.println("MDWS+query= "+ mdwsCall);

        URL wsURL = new URL(mdwsCall);
        // 1. Make the query
        URLConnection wsConnection = wsURL.openConnection();
        // 2. Read the Response
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                wsConnection.getInputStream()));
        return in;
    }



    private void printBuffer(BufferedReader in) {
        try {
            String inputLine = null;
            System.out.println("---------- BUFFER ----------\n");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            System.out.println("---------- BUFFER ----------\n");
            
        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }
}
