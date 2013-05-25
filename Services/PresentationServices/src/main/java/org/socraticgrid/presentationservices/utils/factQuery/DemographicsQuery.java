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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.utils.factQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.util.CommonUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Scanner;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.xml.XMLSerializer;
import org.socraticgrid.kmrolib.KMROPatientTriples;
import org.socraticgrid.kmrolib.MDWSQueryUtil;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.utils.factModels.DetailTabs;
import org.socraticgrid.presentationservices.utils.factModels.Facts;
import org.socraticgrid.presentationservices.utils.factModels.GetPatientDataFactObject;
import org.socraticgrid.presentationservices.utils.factModels.GetPatientDemogObject;
import org.socraticgrid.presentationservices.utils.factModels.ListTabs;
import org.socraticgrid.presentationservices.utils.factModels.PatientDataFact;
import org.socraticgrid.presentationservices.utils.factModels.PatientDemogFact;

/**
 *
 * @author nhin
 */
public class DemographicsQuery extends BaseQuery {

    private String JENAPROP = "JENAService";
    private String sparqlEP = null;

    private String classname = "\n"+this.getClass().getName()+":";
    private static DemographicsQuery instance = null;

    private String personId = null;

    public static DemographicsQuery getInstance() {
        synchronized (DemographicsQuery.class) {
            if (instance == null) {
                instance = new DemographicsQuery();
            }
        }
        return instance;
    }

    /**
     *
     * @param dataSourceURL
     * @param requestType  - "details", "list", "ecs", "raw"
     * @param args
     *
           "domain"       --> patient;allergy;problem;vital;lab;med;xray;consult;
                              procedure;surgery;document;encounter;factor

           "userId"       --> MDWS: incoming user; FMQL: n/a
           "token"        --> MDWS: incoming pwd;  FMQL: n/a
           "patientId"    -->
           "requestType"  -->  list, details, ecs, raw
           "itemId"       -->
           "dataURL"      --> Endpoint to data
           "dataSource"   --> FMQL, MDWS
     *
     * @param useStubbedData
     * @return
     * @throws FileNotFoundException
     * @throws Exception
     */
    public String createResponse(Map args, boolean useStubbedData) throws FileNotFoundException, Exception
    {
        this.sparqlEP = PropertyHelper.getPropertyHelper().getProperty(this.JENAPROP);

        String jsonResp = null;

        String resp = null;

        String domain =      (String) args.get("domain");
        String patientId =   (String) args.get("patientId");
        String userId =      (String) args.get("userId");
        String responseType = (String) args.get("responseType");
        String itemId =      (String) args.get("itemId");
        // ----- specific for VistA -----------------
        String dsURL =       (String) args.get("dataURL");
        String dsSource =    (String) args.get("dataSource");
        // ----- specific for CDS -----------------
        String sectionId =      (String) args.get("sectionId");


        // When patientId is not given, the user is asking for his/her own info.
        if (CommonUtil.strNullorEmpty(patientId)) {
            personId = userId;
        } else {
            personId = patientId;
        }

        // ----------
        // PROCESSING
        // ----------
        try {
            if (useStubbedData) {
                if (responseType.equalsIgnoreCase(BaseResource.RESPONSTYPE_DETAIL))
                    jsonResp = this.getStubbedXMLDetailData(itemId, personId);
                else
                    jsonResp = this.getStubbedXMLListData(personId);

            } else {

                if (responseType.equalsIgnoreCase("detail")) {  //pull from ???
                    jsonResp = this.mapPatientDetail(itemId,
                                        sectionId, domain, responseType);

                } else if (responseType.equalsIgnoreCase("list")) { //pull from MDWS
                    jsonResp = this.mapPatientList(patientId,
                                        sectionId, domain, responseType);

                }

//                if (requestType.equalsIgnoreCase(BaseResource.RESPONSTYPE_DETAIL))
//                    resp = this.mapFactDetail(itemId);
//
//                else if (requestType.equalsIgnoreCase(BaseResource.RESPONSTYPE_LIST))
//                    resp = this.mapFactList(personId);
//
//                else if (requestType.equalsIgnoreCase(BaseResource.RESPONSTYPE_RAW))
//                    resp = this.getRawData(personId, dsURL, dsSource);
//                else
//                    resp = null;
            }

        } catch (Exception e) {
            String statusMsg = e.getMessage();
            jsonResp = createEmptyPatientDataFact(responseType, domain,
                                                  false, statusMsg, sectionId);

            System.out.println(statusMsg);
            e.printStackTrace();
        }
        return jsonResp;

    }

    //TBD
    private String mapPatientDetail(String patientId,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= TBD: GET PATIENT MEDICATION DETAIL "+patientId+") ======================");
        return null;
    }

    private String mapPatientList(String patientId,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET PATIENT Demographics LIST "+patientId+") ======================");

        //-----------------------------------------
        // CHECK FOR VALID SUPPORTED SECTIONID
        //-----------------------------------------
        if (   !CommonUtil.strNullorEmpty(sectionId)
            && !sectionId.equalsIgnoreCase("Main"))
        {
            String statusMsg = "SectionId "+sectionId+" not yet supported.";
            String jsonResp = createEmptyPatientDataFact(domain, responseType,
                                                         false, statusMsg, sectionId);
            System.out.println(statusMsg);
            return jsonResp;
        }

        //-----------------------------------------
        // BUILD facts
        // Note:  This requires the knowledge of the
        // mapping of attributes between sparql return and gui objects.
        //-----------------------------------------
        //-----------------------------------------
        // GET LIST of PATIENT PROBLEMS from MDWS
        //-----------------------------------------
        String propsService = "MDWSService";
        String propsUser = "MDWSUser";
        String propsPwd = "MDWSPwd";
        String propsSite = "MDWSSite";

        String dataURL = PropertyHelper.getPropertyHelper().getProperty(propsService);
        String mdwsUser = PropertyHelper.getPropertyHelper().getProperty(propsUser);
        String mdwsPwd = PropertyHelper.getPropertyHelper().getProperty(propsPwd);
        String mdwsSite = PropertyHelper.getPropertyHelper().getProperty(propsSite);

        System.out.println("PROPS input: MDWSService= "+ dataURL);

        //-----------------------------------------
        // QUERY - get the Patient Medications
        // (VUID pulled from MDWS and transformed to RXNORM)
        //-----------------------------------------
        String demogJson = null;

        MDWSQueryUtil request = new MDWSQueryUtil(dataURL, mdwsUser, mdwsPwd);
        demogJson = request.getTransformedData(patientId, mdwsSite, "patient");

        System.out.println("====> demogJson=\n"+demogJson+ "\n");

        //-----------------------------------------
        // BUILD parent elements
        //-----------------------------------------
        //-----------------------------------------
        // PUSH string of json into a class object.
        // str --> bufferedReader --> object
        //-----------------------------------------
        // use gson to push into matching class object
        Gson gsonMDWS = new Gson();
        GetPatientDemogObject demogParent = gsonMDWS.fromJson(demogJson, GetPatientDemogObject.class);

//        //-----------------------------------------
//        // BUILD patientDataFact json parent elems
//        //-----------------------------------------
//        PatientDemogFact pdf = pdfParent.getpatientDataFact();
//        pdf.setFactType("Demographics");
//        pdf.setTrxnType("list");
//        pdf.setVisibleGridHeaders(true);
//        pdf.setMaxColumns(10);
//        pdf.setSectionId(sectionId);
//
//        pdf.setSuccessStatus(true);

        //-----------------------------------------
        // REMAP to GUI patientDataFact object (with Facts array)
        //-----------------------------------------
        Facts f2 = new Facts();
        f2 = demogParent.getpatientDataFact().getFacts();
        f2.setPatientId(patientId);

        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Demographics");
        pdf.setTrxnType("list");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(10);
        pdf.setSectionId(sectionId);
        pdf.getFacts().add(f2);

        // NOTE:  no ListTabs
        // NOTE:  no GridHeader
        // NOTE:  no DetailTabs

        pdf.setSuccessStatus(true);

        GetPatientDataFactObject pdfParent = new GetPatientDataFactObject();
        pdfParent.setpatientDataFact(pdf);

        //-------------------------------------------------------
        // Convert objects to JSON and string-i-fied for return
        //-------------------------------------------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();

        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(pdfParent));

        return sb.toString();
    }

//    private Collection<DetailTabs> getDetailTabs() {
//        Collection<DetailTabs> coll = new ArrayList();
//
//        DetailTabs dh = new DetailTabs();
//        dh.setLabel("Original SPL");
//        dh.setType("grid");
//        dh.setResponseType("codeDetail");
//        dh.setSectionId("origSPL");
//        dh.getFilters().add("code");
//        dh.getFilters().add("codeSystemCode");
//        coll.add(dh);
//
//        dh = new DetailTabs();
//        dh.setLabel("SPL Triples");
//        dh.setType("grid");
//        dh.setResponseType("codeDetail");
//        dh.setSectionId("triplesSPL");
//        dh.getFilters().add("code");
//        dh.getFilters().add("codeSystemCode");
//        coll.add(dh);
//
//        return coll;
//    }

    /**
     *
     * @param patientId
     * @param dsURL
     * @param dsSource - "FMQL" or "MDWS"
     * @return
     * @throws Exception
     */
    private String getRawData(String patientId, String dsURL, String dsSource) throws Exception
    {
//FROM MDWS LOGIC
//                KMROPatientTriples triples = new KMROPatientTriples(dataURL, request.getDataSource());
//
//                System.out.println("\n============================");
//                result = triples.getMDWSData(request.getPatientId(),
//                                             request.getDomain(),
//                                             mUser, mPwd, mSite);
//
//                System.out.println("\n"+result + "\n============================\n");

        //------------ QUERY REPOSITORY ---------------
        KMROPatientTriples triples = new KMROPatientTriples(dsURL, dsSource);

        String reply = triples.getDemographicsByPatient(patientId);

        System.out.println(reply + "\n===== XML XML XML =====");


//        //------------ TRANSFORM to XML ---------------
        XMLSerializer serializer = new XMLSerializer();
        JSON json = JSONSerializer.toJSON(reply);
        //serializer.setRootName("Demographics");
        serializer.setTypeHintsEnabled(false);
        String xmlResponse = serializer.write(json);

        System.out.println(xmlResponse + "\n=====XML XML XML=====\n");

        return xmlResponse;
    }

    public String getStubbedXMLDetailData(String itemId, String personId) throws FileNotFoundException {

        String id = itemId;
        if (CommonUtil.strNullorEmpty(itemId)) {
System.out.println(classname+"No itemId given. DEFAULTING to 10");
            id = "10";
        }

        String filename = "/home/nhin/Properties/facts/data/getPatientData-Demographics_Detail_"
                + id +"_"+ personId +".json";

        System.out.println("PULLING detail STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }

    public String getStubbedXMLListData(String personId) throws FileNotFoundException {
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Demographics_List"
                +"_"+ personId +".json";

        System.out.println("PULLING list STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }





}
