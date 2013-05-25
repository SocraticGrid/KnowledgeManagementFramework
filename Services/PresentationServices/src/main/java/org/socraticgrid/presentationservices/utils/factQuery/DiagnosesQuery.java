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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.socraticgrid.documenttransformer.Transformer;
import org.socraticgrid.kmrolib.MDWSQueryUtil;
import org.socraticgrid.kmrolib.SPARQLQueryUtil;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.utils.factModels.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author nhin
 */
public class DiagnosesQuery extends BaseQuery {

    private String JENAPROP = "JENAService";
    private String sparqlEP = null;

    private String classname = "\n"+this.getClass().getName()+":";
    private static DiagnosesQuery instance = null;

    public static DiagnosesQuery getInstance() {
        synchronized (DiagnosesQuery.class) {
            if (instance == null) {
                instance = new DiagnosesQuery();
            }
        }
        return instance;
    }

    public String createResponse(Map args, boolean useStubbedData) throws FileNotFoundException, Exception
    {
        this.sparqlEP = PropertyHelper.getPropertyHelper().getProperty(this.JENAPROP);

        String jsonResp = null;

        // EXTRACT out args to filter with
        String domain =         (String) args.get("domain");
        String patientId =      (String) args.get("patientId");
        String userId =         (String) args.get("userId");
        String responseType =   (String) args.get("responseType");
        String itemId =         (String) args.get("itemId");
        // ----- specific for KMR2 -----------------
        String code =           (String) args.get("code");
        String codeSystemCode = (String) args.get("codeSystemCode");
        String fromDate =       (String) args.get("fromDate");
        // ----- specific for CDS -----------------
        String searchStr =      (String) args.get("searchPattern");
        String sectionId =      (String) args.get("sectionId");

        // ----------
        // PROCESSING
        // ----------
        try {
            if (useStubbedData) {
                if (responseType.equalsIgnoreCase("detail"))
                    jsonResp = this.getStubbedXMLDetailData(itemId);
                else
                    jsonResp = this.getStubbedXMLListData();

            } else {
                if (responseType.equalsIgnoreCase("detail")) {  //to pull patient specific details
                    jsonResp = this.mapPatientDetail(itemId);

                } else if (responseType.equalsIgnoreCase("list")) { //pull from MDWS
                    jsonResp = this.mapPatientList(patientId,
                                        sectionId, domain, responseType);

                } else if (responseType.equalsIgnoreCase("codeList")) { //pull from JENA
                    jsonResp = this.mapCodeList(searchStr,
                                        sectionId, domain, responseType);

                } else if (responseType.equalsIgnoreCase("codeDetail")) { //pull from JENA
                    jsonResp = this.mapCodeDetail(code, codeSystemCode, 
                                        sectionId, domain, responseType);
                }
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
    private String mapPatientDetail(String itemId) {
        return this.classname+"KMR Patient fact details mapping TBD";
    }

    /**
     * Pulling from MDWS Vista server the list of Problems belonging to a
     * specific patientid.
     * 
     * Note that MDWS returns code in vuid. Will need to transform into RXNORM
     * and then send that RXNORM code via SPARQL to JENA to get back the final
     * list of codes for display.
     *
     * @param patientId
     * @return
     */
    private static String ACTIVE_ITEMS = "Active";
    private String mapPatientList(String patientId,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET PATIENT PROBLEMS LIST (pattern="+patientId+") ======================");

//        //-----------------------------------------
//        // CHECK FOR VALID SUPPORTED SECTIONID
//        //-----------------------------------------
//        if (   !CommonUtil.strNullorEmpty(sectionId)
//            && !sectionId.equalsIgnoreCase("Active"))
//        {
//            String statusMsg = "SectionId "+sectionId+" not yet supported.";
//            String jsonResp = createEmptyPatientDataFact(domain, responseType,
//                                                         false, statusMsg, sectionId);
//            System.out.println(statusMsg);
//            return jsonResp;
//        }

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
        // QUERY - get the Patient Problems
        // (VUID pulled from MDWS and transformed to RXNORM)
        //-----------------------------------------
        String probsJson = null;

        MDWSQueryUtil request = new MDWSQueryUtil(dataURL, mdwsUser, mdwsPwd);
        probsJson = request.getTransformedData(patientId, mdwsSite, "problem");


        System.out.println("====> probsJson=\n"+probsJson+ "\n");

        //-----------------------------------------
        // PUSH string of json into a class object.
        // str --> bufferedReader --> object
        //-----------------------------------------
        // use gson to push into matching class object
        Gson gsonMDWS = new Gson();
        GetPatientDataFactObject pdfParent = gsonMDWS.fromJson(probsJson, GetPatientDataFactObject.class);

        //-----------------------------------------
        // FILTER out for only request STATUS (sectionId)
        // FactStatusCompareValue == the status value
        // of a Fact that we are looking for, for the requested SECTIONID.
        //
        // mdwsParent ==> parent object of data from MDWS
        // pdfParent ==> final parent object
        //-----------------------------------------
        //GetPatientDataFactObject pdfParent = new GetPatientDataFactObject();

        String FactStatusCompareValue = null;
        if (sectionId.equalsIgnoreCase(ACTIVE_ITEMS)) {
            FactStatusCompareValue = "active";
        } else {
            FactStatusCompareValue = "completed";
        }
        Iterator<Facts> factIter = pdfParent.getpatientDataFact().getFacts().iterator();
        while (factIter.hasNext()) {
            Facts aFact = factIter.next();

//            //-----------------------------------------
//            // if the fact status is correct for this SECTIONID,
//            // then save it to the final parent object.
//            //-----------------------------------------
//            if (!aFact.getStatus().equalsIgnoreCase(FactStatusCompareValue)) {
//                factIter.remove();
//            }

            if (  (sectionId.equalsIgnoreCase(ACTIVE_ITEMS) && !aFact.getStatus().equalsIgnoreCase(ACTIVE_ITEMS))
                ||(!sectionId.equalsIgnoreCase(ACTIVE_ITEMS) && aFact.getStatus().equalsIgnoreCase(ACTIVE_ITEMS)) )
            {
                    factIter.remove();
                    System.out.println("REMOVING fact with Status= "+ aFact.getStatus()) ;
            }
        }

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = pdfParent.getpatientDataFact();
        pdf.setFactType("Problems");
        pdf.setTrxnType("list");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(10);
        pdf.setSectionId(sectionId);

        ListTabs listTab = new ListTabs();
        listTab.setDomain("problems");
        listTab.setLabel("Active");
        listTab.setVisible(true);
        listTab.setDisable(false);
        listTab.setSectionId("active");
        pdf.getListTabs().add(listTab);

        listTab = new ListTabs();
        listTab.setDomain("problems");
        listTab.setLabel("Inactive");
        listTab.setVisible(true);
        listTab.setDisable(false);
        listTab.setSectionId("inactive");
        pdf.getListTabs().add(listTab);

        //-----------------------------------------
        // BUILD GridHeader
        //-----------------------------------------
        pdf.getGridHeaders().add(createGH("problem", "Problem", null, 10));
        pdf.getGridHeaders().add(createGH("displayCode", "DisplayCode", null, 10));
        pdf.getGridHeaders().add(createGH("onset", "Onset", null, 10));
//        pdf.getGridHeaders().add(createGH("route", "Route", null, 10));
        pdf.getGridHeaders().add(createGH("lastRecordedDate", "Updated", null, 10));
//        pdf.getGridHeaders().add(createGH("prescriber", "Provider", null, 10));
        pdf.getGridHeaders().add(createGH("acuity", "Acuity", null, 10));
//        pdf.getGridHeaders().add(createGH("facility", "Facility", null, 10));

        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

// PRESCRIBER and FACILITY not being given back to me by Jerry, yet.

//-----------------------------------------
// FAKE IN RXNORM return CODE FOR TESTING
// fields to fill out:
//      {
//        "code": "272_4",
//        "codeSystemName": "ICD9 not translated",
//        "codeSystemCode": "ICD9",
//        "itemId": "866",
//        "status": "ACTIVE",
//        "lastRecordedDate": "2000-01-01",
//        "problem": "Hyperlipidemia",
//        "onset": "2007-04-10",
//        "acuity": "CHRONIC",
//        "age": 0
//      },

//-----------------------------------------
//Facts f = new Facts();
//f.setCode("780_52");
//f.setCodeSystemCode( "ICD9 not translated");
//f.setCodeSystemName("ICD9");
//f.setItemId("9999");
//f.setStatus("ACTIVE");
//f.setLastRecordedDate( "2000-01-01");
//f.setProblem("UNK");
//f.setOnset("2007-04-10");
//f.setAcuity("CHRONIC");
//pdf.getFacts().add(f);


        pdf.setSuccessStatus(true);

        //-----------------------------------------
        // CONVERT dot in code to be underline,
        // cause query can't handle dot.
        //-----------------------------------------
        Iterator<Facts> iter = pdf.getFacts().iterator();
        int i = 0;
        while(iter.hasNext()) {

            Facts aFact = iter.next();

            aFact.setDisplayCode(aFact.getCode()); //Set the "dot" cversion to dispayCode

            String codeUnderscore = aFact.getCode().replace(".", "_");
            aFact.setCode(codeUnderscore);
        }


        //-------------------------------------------------------
        // Convert objects to JSON and string-i-fied for return
        //-------------------------------------------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();

        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(pdfParent));

        return sb.toString();
    }

    /**
     * Pulling list of medical codes from Jena SPARQL server
     * containing various code graphs.
     * @param searchStr
     * @param sectionId
     * @param domain
     * @param responseType
     * @return
     * @throws Exception
     */
    private String mapCodeList(String searchStr,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET PROBLEMS LIST (pattern="+searchStr+") ======================");

        //-----------------------------------------
        // CHECK FOR VALID SUPPORTED SECTIONID
        //-----------------------------------------
        if (   !CommonUtil.strNullorEmpty(sectionId)
            && !sectionId.equalsIgnoreCase("Active"))
        {
            String statusMsg = "SectionId "+sectionId+" not yet supported.";
            String jsonResp = createEmptyPatientDataFact(domain, responseType,
                                                         false, statusMsg, sectionId);
            System.out.println(statusMsg);
            return jsonResp;
        }

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Problems");
        pdf.setTrxnType("codeList");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(7);
        pdf.setSectionId(sectionId);

        //-----------------------------------------
        // BUILD GridHeader
        //-----------------------------------------
        pdf.getGridHeaders().add(createGH("diagnosis", "Diagnosis", null, 10));
        pdf.getGridHeaders().add(createGH("displayCode", "ICD9 Code", null, 10));
        //pdf.getGridHeaders().add(createGH("description", "Description", null, 10));
        
        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

        //-----------------------------------------
        // BUILD facts
        // Note:  This requires the knowledge of the
        // mapping of attributes between sparql return and gui objects.
        // JENA RESPONSE SNIPPET
        //      {
        //        "label": { "type": "literal" , "value": "ACCIDENT IN HOME" } ,
        //        "icd9": { "type": "uri" , "value": "http://datasets.caregraf.org/icd9cm/E849_0" } ,
        //        "icd9Code": { "type": "literal" , "value": "E849_0" }
        //      } ,
        //-----------------------------------------
        SPARQLQueryUtil jena = new SPARQLQueryUtil(this.sparqlEP);
        List<Map<String, String>> drugList = jena.getNewDiagnosesAsMap(searchStr);

        if (drugList.size() == 0) {

            Facts f = new Facts();
            f.setDiagnosis("");
            f.setDisplayCode("");
            f.setCode("");
            f.setCodeSystemCode("");
            f.setCodeSystemName("");
            f.setDescription("");
            f.setItemId("0");
            pdf.getFacts().add(f);

        } else {
            int counter = 1;
            Iterator<Map<String, String>> iter = drugList.iterator();
            while(iter.hasNext()) {
                Map<String, String> d = iter.next();

                Facts f = new Facts();
                f.setDiagnosis(d.get("label"));
                f.setDisplayCode(d.get("icd9Code"));
                f.setCode(d.get("icd9Code"));
                f.setCodeSystemCode("icd9cm"); //TBD: PARSE d.get("icd9")
                f.setCodeSystemName(d.get("icd9"));

                //NO VALUE COMING FROM SPARQL AS YET so not quite sure what the element name is.
    //            if (CommonUtil.strNullorEmpty(d.get("description")))
                    f.setDescription("");  //NO VALUE COMING FROM SPARQL AS YET
    //            else
    //                f.setDescription(d.get("description"));

                f.setItemId(Integer.toString(counter++)); //not used by non-patient detail filter.

                pdf.getFacts().add(f);
            }
        }
        pdf.setSuccessStatus(true);



        //-----------------------------------------
        // CONVERT underline in code to be dot .. for display value only.
        //-----------------------------------------
        Iterator<Facts> iter = pdf.getFacts().iterator();
        int i = 0;
        while(iter.hasNext()) {

            Facts aFact = iter.next();

            String codeDot = aFact.getCode().replace("_", ".");
            aFact.setDisplayCode(codeDot); //Set the "dot" cversion to displayCode
        }

        //-----------------------------------------
        // BUILD parent json element
        //-----------------------------------------
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

    /**
     * Pulling details for a given medical code from Jena SPARQL server
     * containing various code graphs.
     * @param code
     * @param codeSystemCode
     * @param sectionId
     * @param domain
     * @param responseType
     * @return
     * @throws Exception
     */
    private String mapCodeDetail(String code, String codeSystemCode,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET PROBLEMS CODEDETAIL (code="+code+") ======================");

        //-----------------------------------------
        // CHECK FOR VALID SUPPORTED SECTIONID
        //-----------------------------------------
        if (   !CommonUtil.strNullorEmpty(sectionId)
            && !sectionId.equalsIgnoreCase("drugUtil"))
        {
            String statusMsg = "SectionId "+sectionId+" not yet supported.";
            String jsonResp = createEmptyPatientDataFact(domain, responseType,
                                                         false, statusMsg, sectionId);

            System.out.println(statusMsg);
            return jsonResp;
        }

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Problems");
        pdf.setTrxnType("codeDetail");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(7);
        pdf.setSectionId(sectionId);

        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

        //-----------------------------------------
        // BUILD GridHeader
        //-----------------------------------------
        pdf.getGridHeaders().add(createGH("drugClass", "Drug Class", null, 25));
        pdf.getGridHeaders().add(createGH("example", "Example", null, 20));
        pdf.getGridHeaders().add(createGH("utilization", "Utilization @ Dx", null, 7));
        pdf.getGridHeaders().add(createGH("d90Days", "@ 90 Days Post-Dx", null, 7));
        pdf.getGridHeaders().add(createGH("d180Days", "@ 180 Days Post-Dx", null, 7));
        pdf.getGridHeaders().add(createGH("d270Days", "@ 270 Days Post-Dx", null, 7));

        //-----------------------------------------
        // BUILD facts
        // Note:  This requires the knowledge of the
        // mapping of attributes between sparql return and gui objects.
        //-----------------------------------------
        SPARQLQueryUtil jena = new SPARQLQueryUtil(this.sparqlEP);
        List<Map<String, String>> drugDetails = jena.getDrugsAsMap(code);

        int counter = 1;
        Iterator<Map<String, String>> detailsIter = drugDetails.iterator();
        while(detailsIter.hasNext()) {
            Map<String, String> d = detailsIter.next();

            Facts f = new Facts();
            f.setDrugClass(d.get("drugClass"));
            f.setExample(d.get("exampleDrug"));
            f.setUtilization(d.get("rr_initial"));
            f.setD90Days(d.get("rr_p1q"));
            f.setD180Days(d.get("rr_p2q"));
            f.setD270Days(d.get("rr_p3q"));

            f.setItemId(Integer.toString(counter++));

            pdf.getFacts().add(f);
        }
        pdf.setSuccessStatus(true);
        
        //-----------------------------------------
        // BUILD parent json element
        //-----------------------------------------
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


    private Collection<DetailTabs> getDetailTabs() {
        Collection<DetailTabs> coll = new ArrayList();

        DetailTabs dh = new DetailTabs();
        dh.setLabel("Summary Information");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("summary");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("Diagnoses");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("diagnoses");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("Treatment");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("treatment");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("Pubmed");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("pubmed");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("Podcasts");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("podcasts");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("Videos");
        dh.setType("text");
        dh.setResponseType("ecs");
        dh.setSectionId("videos");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("VA Drug Utilization");
        dh.setType("grid");
        dh.setResponseType("detail");
        dh.setSectionId("drugUtil");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(false);
        coll.add(dh);

        return coll;
    }

    
    public String getStubbedXMLDetailData(String itemId) throws FileNotFoundException {

        String id = itemId;
        if (CommonUtil.strNullorEmpty(itemId)) {
System.out.println(classname+"No itemId given. DEFAULTING to 10");
            id = "10";
        }

        String filename = "/home/nhin/Properties/facts/data/getPatientData-Diagnoses_Detail_"+id+".json";
        System.out.println("PULLING detial STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }

    public String getStubbedXMLListData() throws FileNotFoundException {
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Diagnoses_List.json";
        System.out.println("PULLING list STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }





}