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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.socraticgrid.kmrolib.MDWSQueryUtil;
import org.socraticgrid.kmrolib.SPARQLQueryUtil;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.utils.factModels.*;

/**
 *
 * @author nhin
 */
public class MedicationsQuery extends BaseQuery {

    private String JENAPROP = "JENAService";
    private String sparqlEP = null;

    private String classname = "\n"+this.getClass().getName()+":";
    private static MedicationsQuery instance = null;

    public static MedicationsQuery getInstance() {
        synchronized (MedicationsQuery.class) {
            if (instance == null) {
                instance = new MedicationsQuery();
            }
        }
        return instance;
    }

    public String createResponse(Map args, boolean useStubbedData)
    throws FileNotFoundException, Exception
    {
        this.sparqlEP = PropertyHelper.getPropertyHelper().getProperty(this.JENAPROP);
        
        String jsonResp = null;

        // EXTRACT out args to filter with
        String domain =         (String) args.get("domain");
        String userId =         (String) args.get("userId");
        String patientId =      (String) args.get("patientId");
        String responseType =   (String) args.get("responseType");
        String itemId =         (String) args.get("itemId");
        // ----- specific for KMR2 -----------------
        String code =           (String) args.get("code");
        String codeSystemCode = (String) args.get("codeSystemCode");
        String fromDate =       (String) args.get("fromDate");
        // ----- specific for VistA -----------------
        String dsURL =       (String) args.get("dataURL");
        String dsSource =    (String) args.get("dataSource");
        // ----- specific for CDS -----------------
        String searchStr =      (String) args.get("searchPattern");
        String sectionId =      (String) args.get("sectionId");

        // ----------
        // PROCESSING
        // ----------
        try {
            if (useStubbedData) {
                if (responseType.equalsIgnoreCase(BaseResource.RESPONSTYPE_DETAIL))
                    jsonResp = this.getStubbedXMLDetailData(itemId);
                else
                    jsonResp = this.getStubbedXMLListData();

            } else {

                if (responseType.equalsIgnoreCase("detail")) {  //pull from ???
                    jsonResp = this.mapPatientDetail(itemId,
                                        sectionId, domain, responseType);

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
    private String mapPatientDetail(String patientId,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= TBD: GET PATIENT MEDICATION DETAIL "+patientId+") ======================");
        return null;


    }

    private static String ACTIVE_ITEMS = "Active";
    private String mapPatientList(String patientId,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET PATIENT MEDICATION LIST "+patientId+") ======================");

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
        String medsJson = null;

        MDWSQueryUtil request = new MDWSQueryUtil(dataURL, mdwsUser, mdwsPwd);
        medsJson = request.getTransformedData(patientId, mdwsSite, "med");

        System.out.println("====> medsJson=\n"+medsJson+ "\n");

        //-----------------------------------------
        // BUILD parent elements
        //-----------------------------------------
        //-----------------------------------------
        // PUSH string of json into a class object.
        // str --> bufferedReader --> object
        //-----------------------------------------
        // use gson to push into matching class object
        Gson gsonMDWS = new Gson();
        GetPatientDataFactObject pdfParent = gsonMDWS.fromJson(medsJson, GetPatientDataFactObject.class);


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
            FactStatusCompareValue = "expired";
        }
        Iterator<Facts> factIter = pdfParent.getpatientDataFact().getFacts().iterator();
        while (factIter.hasNext()) {
            Facts aFact = factIter.next();

            //-----------------------------------------
            // if SECTIONID == "active" then remove all item.status != "active"
            // OR if SECTIONID != "active" then remove all item.status == "active"
            //-----------------------------------------
//            System.out.println("fact Status= "+ aFact.getStatus()) ;

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
        pdf.setFactType("Medications");
        pdf.setTrxnType("list");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(8);
        pdf.setSectionId(sectionId);

        ListTabs listTab = new ListTabs();
        listTab.setDomain("medications");
        listTab.setLabel("Active");
        listTab.setVisible(true);
        listTab.setDisable(false);
        listTab.setSectionId("active");
        pdf.getListTabs().add(listTab);

        listTab = new ListTabs();
        listTab.setDomain("medications");
        listTab.setLabel("Expired");
        listTab.setVisible(true);
        listTab.setDisable(false);
        listTab.setSectionId("expired");
        pdf.getListTabs().add(listTab);
        
        //-----------------------------------------
        // BUILD GridHeader
        //-----------------------------------------
        pdf.getGridHeaders().add(createGH("medication", "Medication", null, 20));
        pdf.getGridHeaders().add(createGH("formulation", "Formulation", null, 10));
        pdf.getGridHeaders().add(createGH("dose", "Dose", null, 10));
        pdf.getGridHeaders().add(createGH("route", "Route", null, 6));
        pdf.getGridHeaders().add(createGH("schedule", "Schedule", null, 10));
//        pdf.getGridHeaders().add(createGH("duration", "Duration", null, 10));
        pdf.getGridHeaders().add(createGH("orderDate", "OrderDate", null, 10));
        pdf.getGridHeaders().add(createGH("drugClass", "Drug Class", null, 30));
        pdf.getGridHeaders().add(createGH("prescriber", "Prescriber", null, 10));

        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

//-----------------------------------------
// FAKE IN RXNORM return CODE FOR TESTING
// fields to fill out:
//
//      {
//        "code": "1191",
//        "codeSystemName": "RxNorm not translated",
//        "codeSystemCode": "RxNorm",
//        "itemId": "17702",
//        "status": "active",
//        "drugClass": "NON-OPIOID ANALGESICS",
//        "orderDate": "2007-04-10",
//        "medication": "ASPIRIN",
//        "sig": "TAKE ONE TABLET BY MOUTH EVERY MORNING",
//        "formulation": "TAB,EC",
//        "unit": "MG",
//        "dose": "81",
//        "route": "PO",
//        "frequency": "TDB",
//        "duration": "TDB",
//        "prescriber": "VEHU,ONEHUNDRED",
//        "drugClassCode": "CN103",
//        "drugClassCodeSystem": "drugclass"
//      },
//MEDS
//  RXNORM code  
//   207772        
//   198036        
        
//-----------------------------------------
//String istat = null;
//if (sectionId.equalsIgnoreCase(ACTIVE_ITEMS)) istat = "active";
//else istat = "completed";
//
//if (patientId.equalsIgnoreCase("100013")) {
//    Facts f = new Facts();
//    f.setCode("207772");     //put in RXNORM code
//    f.setCodeSystemCode("RxNorm");
//    f.setCodeSystemName("RxNorm not translated");
//    f.setItemId("99998");
//    f.setStatus(istat);
//    f.setDrugClass("(*)");
//    f.setSchedule("QPM");
//    f.setOrderDate("2007-04-10");
//    f.setMedication("Nifedipine");
//    f.setFormulation("TAB,EC");
//    f.setUnit("MG");
//    f.setDose("30 MG");
//    f.setRoute("PO");
//    f.setFrequency("TDB");
//    f.setDuration("TDB");
//    f.setPrescriber("VEHU,ONEHUNDRED");
//    f.setDrugClass("TBD");
//    pdf.getFacts().add(f);
//
//} else {
//    Facts f = new Facts();
//    f.setCode("198036");     //put in VUID code
//    f.setCodeSystemCode("RxNorm");
//    f.setCodeSystemName("RxNorm not translated");
//    f.setItemId("99999");
//    f.setStatus(istat);
//    f.setDrugClass("(*)");
//    f.setOrderDate("2007-04-10");
//    f.setMedication("Nifedipine");
//    f.setFormulation("TAB,EC");
//    f.setSchedule("BID");
//    f.setUnit("MG");
//    f.setDose("90 MG");
//    f.setRoute("PO");
//    f.setFrequency("TDB");
//    f.setDuration("TDB");
//    f.setPrescriber("VEHU,ONEHUNDRED");
//    f.setDrugClass("TBD");
//    pdf.getFacts().add(f);
//}

        //-----------------------------------------
        //-----------------------------------------
        pdf.setSuccessStatus(true);

        //-------------------------------------------------------
        // Convert objects to JSON and string-i-fied for return
        //-------------------------------------------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();

        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(pdfParent));

        return sb.toString();
    }

    private String mapCodeList(String searchStr,
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET MEDICATIONS LIST (pattern="+searchStr+") ======================");

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
        pdf.setFactType("Medications");
        pdf.setTrxnType("codeList");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(7);
        pdf.setSectionId(sectionId);

        //-----------------------------------------
        // BUILD GridHeader
        //-----------------------------------------
        GridHeaders grh = new GridHeaders();

        grh.setColumnId("medication");
        grh.setValue("Medication");
        grh.setDescription("A column header.");
        grh.setWidth(10);
        pdf.getGridHeaders().add(grh);

        grh = new GridHeaders();
        grh.setColumnId("newCode");
        grh.setValue("RxNORM Code");
        grh.setDescription("A column header.");
        grh.setWidth(10);
        pdf.getGridHeaders().add(grh);

//        grh = new GridHeaders();
//        grh.setColumnId("description");
//        grh.setValue("Description");
//        grh.setDescription("A column header.");
//        grh.setWidth(10);
//        pdf.getGridHeaders().add(grh);
        
        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

        //-----------------------------------------
        // BUILD facts
        // Note:  This requires the knowledge of the
        // mapping of attributes between sparql return and gui objects.

//      {
//        "label": { "type": "literal" , "value": "0.1 ML Influenza A virus vaccine, A-California-7-2009 (H1N1)-like virus 0.09 MG/ML / Influenza A virus vaccine, A-Victoria-361-2011 (H3N2)-like virus 0.09 MG/ML / Influenza B virus vaccine, B-Wisconsin-1-2010-like virus 0.09 MG/ML Prefilled Syringe" } ,
//        "rxnorm": { "type": "uri" , "value": "http://datasets.caregraf.org/rxnorm/1304311" } ,
//        "rxnormCode": { "type": "literal" , "value": "1304311" }
//      } ,
        //-----------------------------------------
        SPARQLQueryUtil jena = new SPARQLQueryUtil(this.sparqlEP);
        List<Map<String, String>> drugList = jena.getNewMedicationsAsMap(searchStr);

        if (drugList.size() == 0) {
            Facts f = new Facts();
            f.setMedication("");
            f.setNewCode("");
            f.setCode("");
            f.setCodeSystemCode(""); 
            f.setCodeSystemName("");
            //f.setDescription("");
            f.setItemId("0");
            pdf.getFacts().add(f);

        } else {
            int counter = 1;
            Iterator<Map<String, String>> iter = drugList.iterator();
            while(iter.hasNext()) {
                Map<String, String> d = iter.next();

                Facts f = new Facts();
                f.setMedication(d.get("label"));
                f.setNewCode(d.get("rxnormCode"));
                f.setCode(d.get("rxnormCode"));
                f.setCodeSystemCode("rxnorm"); //TBD: PARSE d.get("rxnorm")
                f.setCodeSystemName(d.get("rxnorm"));

                //NO VALUE COMING FROM SPARQL AS YET so not quite sure what the element name is.
    //            if (CommonUtil.strNullorEmpty(d.get("description")))
                    f.setDescription("");
    //            else
    //                f.setDescription(d.get("description"));

                f.setItemId(Integer.toString(counter++)); //not used by non-patient detail filter.

                pdf.getFacts().add(f);
            }
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

    private String mapCodeDetail(String code, String codeSystemCode, 
                           String sectionId, String domain, String responseType)
            throws Exception
    {
        System.out.println("\n========= GET MEDICATIONS CODEDETAIL (code="+code+") ======================");

        //-----------------------------------------
        // CHECK FOR VALID SUPPORTED SECTIONID
        //-----------------------------------------
        if (   !CommonUtil.strNullorEmpty(sectionId)
            && !sectionId.equalsIgnoreCase("triplesSPL"))
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
        pdf.setFactType("Medications");
        pdf.setTrxnType("codeDetail");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(7);
        pdf.setSectionId(sectionId);

        //-----------------------------------------
        // BUILD DetailHeaders
        //-----------------------------------------
        pdf.getDetailTabs().addAll(getDetailTabs());

        //-----------------------------------------
        // BUILD facts
        // Note:  This requires the knowledge of the
        // mapping of attributes between sparql return and gui objects.
        //
        // OUTPUT FROM JENA (snippet of each Fact type items):
        //      {
        //        "indication": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/78048006" } ,
        //        "indicationLabel": { "type": "literal" , "value": "Candidiasis" }
        //      } ,
        //
        //      {
        //        "monitoring": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/365786009" } ,
        //        "monitoringLabel": { "type": "literal" , "value": "Bilirubin level - finding" }
        //      } ,
        //
        //      {
        //        "boxWarning": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/25569003" } ,
        //        "boxWarningLabel": { "type": "literal" , "value": "Ventricular tachycardia" }
        //      } ,
        //
        //      {
        //        "sideEffect": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/404640003" } ,
        //        "sideEffectLabel": { "type": "literal" , "value": "Dizziness" }
        //      } ,
        //      {
        //        "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/not_HAS_INDICATION" } ,
        //        "associationTypeLabel": { "type": "literal" , "value": "not_HAS_INDICATION" } ,
        //        "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/24321005" } ,
        //        "additionalAssociationLabel": { "type": "literal" , "value": "Fungal meningitis" }
        //      }

        //-----------------------------------------
        SPARQLQueryUtil jena = new SPARQLQueryUtil(this.sparqlEP);
        
        //-----------------
        // INDICATIONS
        //-----------------
        Facts f = new Facts();
        f.setTitle("Indications");

        List<Map<String, String>> medFacts = jena.getMedIndicationsAsMap(code);
        Iterator<Map<String, String>> medItems = medFacts.iterator();
        while(medItems.hasNext()) {
            Map<String, String> d = medItems.next();
            f.getItems().add(d.get("indicationLabel"));
            //f.getHoverTexts().add(d.get("indication"));  //DON'T WANT TO DISPLAY
        }
        pdf.getFacts().add(f);
        //-----------------
        // MONITORING
        //-----------------
        f = new Facts();
        f.setTitle("Monitoring");

        medFacts = jena.getMedMonitoringAsMap(code);
        medItems = medFacts.iterator();
        while(medItems.hasNext()) {
            Map<String, String> d = medItems.next();
            f.getItems().add(d.get("monitoringLabel"));
            //f.getHoverTexts().add(d.get("monitoring"));  //DON'T WANT TO DISPLAY
        }
        pdf.getFacts().add(f);
        //-----------------
        // BOXWARNING
        //-----------------
        f = new Facts();
        f.setTitle("Box Warning");

        medFacts = jena.getMedBoxWarningAsMap(code);
        medItems = medFacts.iterator();
        while(medItems.hasNext()) {
            Map<String, String> d = medItems.next();
            f.getItems().add(d.get("boxWarningLabel"));
            //f.getHoverTexts().add(d.get("boxWarning"));  //DON'T WANT TO DISPLAY
        }
        pdf.getFacts().add(f);
        //-----------------
        // SIDE EFFECTS
        //-----------------
        f = new Facts();
        f.setTitle("Side Effects");

        medFacts = jena.getMedSideEffectAsMap(code);
        medItems = medFacts.iterator();
        while(medItems.hasNext()) {
            Map<String, String> d = medItems.next();
            f.getItems().add(d.get("sideEffectLabel"));
            //f.getHoverTexts().add(d.get("sideEffect"));  //DON'T WANT TO DISPLAY

        }
        pdf.getFacts().add(f);
        //-----------------
        // ASSOCIATED TRIPLES
        // have to loop through all titles that are the same.
        // title = associationTypeLabel
        // item = additionalAssociationLabel
        //
        // SAMPLE JENA RESPONSE of ASSOCIATED TRIPLES
        //      {
        //        "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/IS_ASSESSED_USING" } ,
        //        "associationTypeLabel": { "type": "literal" , "value": "IS_ASSESSED_USING" } ,
        //        "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/33688009" } ,
        //        "additionalAssociationLabel": { "type": "literal" , "value": "Cholestasis" }
        //      } ,
        //      {
        //        "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/IS_ASSESSED_USING" } ,
        //        "associationTypeLabel": { "type": "literal" , "value": "IS_ASSESSED_USING" } ,
        //        "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/61261009" } ,
        //        "additionalAssociationLabel": { "type": "literal" , "value": "Hemolytic anemia" }
        //      } ,        //      {
        //        "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/IS_ASSESSED_USING" } ,
        //        "associationTypeLabel": { "type": "literal" , "value": "IS_NOT_ASSESSED_USING" } ,
        //        "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/61261009" } ,
        //        "additionalAssociationLabel": { "type": "literal" , "value": "Lorem ipsem" }
        //      } ,
        //-----------------
        String newTitle = null;
        String currTitle = null;
        int counter = 1;

        Facts fs = null;
        medFacts = jena.getMedAssociationsAsMap(code);

        
        medItems = medFacts.iterator();
        while(medItems.hasNext()) {
            Map<String, String> d = medItems.next();

            newTitle = d.get("associationTypeLabel");

            //----------------------------
            // CREATE a new FACT
            //      when it's the first record
            //   OR when newTitle is diff from last title.
            //
            //----------------------------
            if ((currTitle == null) || !newTitle.equalsIgnoreCase(currTitle))
            {
                // SAVE the previously processed fs ONLY when it is NOT NULL (trying to avoid first time in)
                if (fs != null) pdf.getFacts().add(fs);

                fs = new Facts();
                fs.setTitle(newTitle);
                currTitle = newTitle; //logging that newTitle has already been saved.
            }
            
            fs.getItems().add(d.get("additionalAssociationLabel"));
            fs.setItemId(Integer.toString(counter++));
        }
        // MAKING sure to save the last fs coming out, ONLY when it is  NOT NULL
        if (fs != null) pdf.getFacts().add(fs);

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
        dh.setLabel("FDA Special Product Label (SPL)");
        dh.setType("grid");
        dh.setResponseType("codeDetail");
        dh.setSectionId("origSPL");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(true);
        coll.add(dh);

        dh = new DetailTabs();
        dh.setLabel("SPL \"Facts\"");
        dh.setType("grid");
        dh.setResponseType("codeDetail");
        dh.setSectionId("triplesSPL");
        dh.getFilters().add("code");
        dh.getFilters().add("codeSystemCode");
        dh.setVisible(true);
        dh.setDisable(false);
        coll.add(dh);

        return coll;
    }


    //=====================================================


/*
    private String mapFactDetail(String orderId) throws Exception {
        String jsonResp = null;
System.out.append("===> mapFactDetail: itemId="+ orderId);

        String domain = "Medications";
        KMROPatientTriples KMROPatientTriples =
                new KMROPatientTriples(this.dsURL, this.dsInterface);

        List<Map<String, String>> conceptValueList = null;
        Iterator<Map<String, String>> itr = null;
        String conceptCode = null;


        //------------------------------------------
        //GETTING A SPECIFIC MEDICATION ORDER DETAIL by orderId
        //------------------------------------------

        System.out.println("GETTING SPECIFIC ORDER: "+ orderId);
        List<Map<String, String>> medDispense = KMROPatientTriples.getDispensationsOfMedicationOrder(orderId);
        System.out.println("\n\tOrderId:" + orderId + ":" + "\n\t\t" + medDispense);


        return prepMedDetailJsonResp(medDispense);
    }



    private String prepMedDetailJsonResp(
                                   List<Map<String, String>>medicationOrderList)
    {
        StringBuilder sb = null;

        //KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Medications");
        pdf.setTrxnType(BaseResource.RESPONSTYPE_DETAIL);
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(12);
        pdf.setSuccessStatus(true);




        return sb.toString();
    }

    private String mapFactList(String patientId) throws Exception {

System.out.append("===> mapFactList: patientId="+ patientId);

        String domain = "Medications";
        KMROPatientTriples KMROPatientTriples =
                new KMROPatientTriples(this.dsURL,this.dsInterface);

        List<Map<String, String>> conceptDefinitionList = null;
        List<Map<String, String>> conceptValueList = null;
        Iterator<Map<String, String>> itr = null;
        String conceptCode = null;

        //----------------------------------------
        // GETTING A LIST OF ALL MEDICATION ORDERS
        //----------------------------------------
        System.out.println("\n\n=== Get medication orders for a patient, ordered by date ===\n");
        List<Map<String, String>> medicationOrderList = KMROPatientTriples.getMedicationOrdersByPatient(patientId);
        System.out.println(medicationOrderList);

        return prepMedsJsonResp(patientId, medicationOrderList);
    }


    private String prepMedsJsonResp(String patientId,
                                   List<Map<String, String>>medicationOrderList)
    {
        StringBuilder sb = null;

        //KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Medications");
        pdf.setTrxnType("list");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(12);
        pdf.setSuccessStatus(true);

        //-----------------------------------------
        // BUILD detailTabs
        //-----------------------------------------
        //FACT DETAIL SECTION
        DetailTabs detailTab = new DetailTabs();
        detailTab.setLabel("Dispensations");
        detailTab.setVisible(true);
        detailTab.setType("grid");
        detailTab.setResponseType(BaseResource.RESPONSTYPE_DETAIL);
        detailTab.setSectionId("dispensations");
        detailTab.getFilters().add("code");
        detailTab.getFilters().add("codeSystemCode");
        pdf.getDetailTabs().add(detailTab);
        //---------------
        //ECS SECTION(s)
        //---------------
        // 1) Get a list of all required ecs sections from db
        //---------------
        EcsService ecsConfig = new EcsService();
        ConfigQueryParams ecsFilters = new ConfigQueryParams();
        ecsFilters.setDomainType("Medications");
        ecsFilters.setActiveFlag(Boolean.TRUE);
        List<Configuration> ecsSections = ecsConfig.queryConfiguration(ecsFilters);
        //---------------
        // 2) and create detailTab for each.
        //---------------
        Iterator ecsSectionsIter = ecsSections.iterator();
        int i = 1;
        while (ecsSectionsIter.hasNext()) {
            Configuration aSection = (Configuration) ecsSectionsIter.next();

            DetailTabs dt = new DetailTabs();
            dt.setLabel(aSection.getLabel());
            dt.setVisible(aSection.getActiveFlag());
            dt.setType("text");
            dt.setResponseType(BaseResource.RESPONSTYPE_DETAIL);
            dt.setSectionId(aSection.getSectionId().toString());
            pdf.getDetailTabs().add(dt);
        }
        //------------------
        // BUILD gridHeaders
        //------------------
        GridHeaders gh = new GridHeaders();
        gh.setColumnId("orderDate");
        gh.setValue("Order D/T");
        gh.setWidth(10);
        gh.setFormatter("factDateFormatter");
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("medication");
        gh.setValue("Medication");
        gh.setWidth(20);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("sig");
        gh.setValue("Sig");
        gh.setWidth(30);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("duration");
        gh.setValue("Duration");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("quantity");
        gh.setValue("Quantity");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("refills");
        gh.setValue("Refills");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("prescriber");
        gh.setValue("Prescriber");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("orderExpireDate");
        gh.setValue("Order Expir.");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        gh = new GridHeaders();
        gh.setColumnId("source");
        gh.setValue("Source");
        gh.setWidth(10);
        pdf.getGridHeaders().add(gh);

        //------------------
        //Begin building the Response Json String.
        //------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        sb = new StringBuilder("{\n\"patientDataFact\": \n");

        //---------------------------------------------
        // Build Medicaton Fact Array
        // KMRO MAPPED DATA:
//          {statusCode=73425007,
//          medicationCode=198211,
//          dateTimeOfOrder=2010-02-27T09:03:43Z,
//          s=http://patients.kmr.org/danno/100-28050,
//          medicationTerminologyCode=2.16.840.1.113883.6.88,
//          medicationLabel=Simvastatin 40 MG Oral Tablet,
//          medicationTerminologyLabel=RXNORM,
//          numberOfRefillsAllowed=3,
//          dose=90,
//          statusTerminologyCode=2.16.840.1.113883.6.96,
//          sig=TAKE ONE TABLET PO QPM}
        
        //---------------------------------------------
        Iterator<Map<String, String>> medsIter = medicationOrderList.iterator();
        while(medsIter.hasNext()) {
            Map<String, String> med = medsIter.next();

            Facts f = new Facts();
            f.setOrderDate(med.get("dateTimeOfOrder"));
            f.setMedication(med.get("medicationLabel"));
            f.setCode(med.get("medicationCode"));
            f.setCodeSystemName(med.get("medicationTerminologyLabel"));
            f.setCodeSystemCode(med.get("medicationTerminologyCode"));
            f.setSig(med.get("sig"));
            f.setDuration("TBD");
            f.setQuantity("TBD");
            f.setRefills(med.get("numberOfRefillsAllowed"));
            f.setPrescriber("TBD");
            f.setSource("TBD");
            f.setItemId(med.get("s"));
            f.getHoverTexts().add("");
            f.getHoverTexts().add(med.get("medicationCode")
                             +" ("+ med.get("medicationTerminologyLabel")+ ")");
            f.setStatus("TBD");

            pdf.getFacts().add(f);
        }

        sb.append(gson.toJson(pdf));
        sb.append("\n}");

        System.out.println("====> MEDS LIST:\n" + sb);

        return sb.toString();
    }
*/

    public String getStubbedXMLDetailData(String itemId)
            throws FileNotFoundException
    {
        String id = itemId;
        if (CommonUtil.strNullorEmpty(itemId)) {
            System.out.println(classname+"No itemId given. DEFAULTING to 10");
            id = "10";
        }

        //String filename = "/home/nhin/Properties/facts/data/"+patientId+"/"+domain+"/med_"+codeSystemCode+"_"+code+"_"+fromDate;
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Medications_Detail_"+id+".json";
        System.out.println("==>PULLING detail STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }

    public String getStubbedXMLListData() throws FileNotFoundException {

        //String filename = "/home/nhin/Properties/facts/data/"+patientId+"/"+domain+"/getPatientData-Medications_List.xml";
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Medications_List.json";
        System.out.println("PULLING list STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }





}
