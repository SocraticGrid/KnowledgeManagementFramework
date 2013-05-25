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
package org.socraticgrid.kmrolib;

import org.socraticgrid.ecsmanager.model.Configuration;
import org.socraticgrid.ecsmanager.model.ConfigQueryParams;
import org.socraticgrid.ecsmanager.service.EcsService;
import org.junit.After;
import org.junit.Before;
import org.socraticgrid.kmrolib.utils.DateUtil;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import org.xml.sax.SAXException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import org.socraticgrid.kmrolib.KMROPatientTriples;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;




/**
 *
 * @author nhin
 */
public class TestFactRequest {

    public TestFactRequest() {
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

//    private String prepMedsJsonList(String patientId,
//                                   List<Map<String, String>>medicationOrderList)
//    {
//        StringBuilder sb = null;
//
//        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
//        String domain = "VitalSign";
//
////DBG ONLY - REMOVE
//patientId = "http://patients.kmr.org/danno/2-224";
//
//        //-----------------------------------------
//        // BUILD patientDataFact json parent elems
//        //-----------------------------------------
//        PatientDataFact pdf = new PatientDataFact();
//        pdf.setFactType("Medications");
//        pdf.setTrxnType("list");
//        pdf.setVisibleGridHeaders(true);
//        pdf.setMaxColumns(12);
//        pdf.setSuccessStatus(true);
//
//        //-----------------------------------------
//        // BUILD detailTabs
//        //-----------------------------------------
//        //FACT DETAIL SECTION
//        DetailTabs detailTab = new DetailTabs();
//        detailTab.setLabel("Dispensations");
//        detailTab.setVisible(true);
//        detailTab.setType("grid");
//        detailTab.setResponseType("detail");
//        detailTab.setSectionId("dispensations");
//        detailTab.getFilters().add("code");
//        detailTab.getFilters().add("codeSystemCode");
//        pdf.getDetailTabs().add(detailTab);
//        //---------------
//        //ECS SECTION(s)
//        //---------------
//        // 1) Get a list of all required ecs sections from db
//        //---------------
//        EcsService ecsConfig = new EcsService();
//        ConfigQueryParams ecsFilters = new ConfigQueryParams();
//        ecsFilters.setDomainType("Medications");
//        ecsFilters.setActiveFlag(Boolean.TRUE);
//        List<Configuration> ecsSections = ecsConfig.queryConfiguration(ecsFilters);
//        //---------------
//        // 2) and create detailTab for each.
//        //---------------
//        Iterator ecsSectionsIter = ecsSections.iterator();
//        int i = 1;
//        while (ecsSectionsIter.hasNext()) {
//            Configuration aSection = (Configuration) ecsSectionsIter.next();
//
//            DetailTabs dt = new DetailTabs();
//            dt.setLabel(aSection.getLabel());
//            dt.setVisible(aSection.getActiveFlag());
//            dt.setType("text");
//            dt.setResponseType("detail");
//            dt.setSectionId(aSection.getSectionId().toString());
//            pdf.getDetailTabs().add(dt);
//        }
//        //------------------
//        // BUILD gridHeaders
//        //------------------
//        GridHeaders gh = new GridHeaders();
//        gh.setColumnId("orderDate");
//        gh.setValue("Order D/T");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("medication");
//        gh.setValue("Medication");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("sig");
//        gh.setValue("Sig");
//        gh.setWidth(4);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("duration");
//        gh.setValue("Duration");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("quantity");
//        gh.setValue("Quantity");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("refills");
//        gh.setValue("Refills");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("prescriber");
//        gh.setValue("Prescriber");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("orderExpireDate");
//        gh.setValue("Order Expir.");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("source");
//        gh.setValue("Source");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//
//        //------------------
//        //Begin building the Response Json String.
//        //------------------
//        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//        sb = new StringBuilder("{\n\"patientDataFact\": \n");
//
////        sb.deleteCharAt(sb.length()-1);
////        sb.append(",\"facts\": [\n");
//
//        //---------------------------------------------
//        // Build Medicaton Fact Array
//        // KMRO MAPPED DATA:
//        /*  {statusCode=73425007,
//         * medicationCode=198211,
//         * dateTimeOfOrder=2010-02-27T09:03:43Z,
//         * s=http://patients.kmr.org/danno/100-28050,
//         * medicationTerminologyCode=2.16.840.1.113883.6.88,
//         * medicationLabel=Simvastatin 40 MG Oral Tablet,
//         * medicationTerminologyLabel=RXNORM,
//         * numberOfRefillsAllowed=3,
//         * dose=90,
//         * statusTerminologyCode=2.16.840.1.113883.6.96,
//         * sig=TAKE ONE TABLET PO QPM}
//        */
//        //---------------------------------------------
//        Iterator<Map<String, String>> medsIter = medicationOrderList.iterator();
//        while(medsIter.hasNext()) {
//            Map<String, String> med = medsIter.next();
//
//            Facts f = new Facts();
//            f.setOrderDate(med.get("dateTimeOfOrder"));
//            f.setMedication(med.get("medicationLabel"));
//            f.setCode(med.get("medicationCode"));
//            f.setCodeSystemName(med.get("medicationTerminologyLabel"));
//            f.setCodeSystemCode(med.get("medicationTerminologyCode"));
//            f.setSig(med.get("sig"));
//            f.setDuration("TBD");
//            f.setQuantity("TBD");
//            f.setRefills(med.get("numberOfRefillsAllowed"));
//            f.setPrescriber("TBD");
//            f.setSource("TBD");
//            f.setItemId(med.get("s"));
//            f.getHoverTexts().add("");
//            f.getHoverTexts().add(med.get("medicationCode")
//                             +" ("+ med.get("medicationTerminologyLabel")+ ")");
//            f.setStatus("TBD");
//
//            pdf.getFacts().add(f);
//        }
//
//
//        sb.append(gson.toJson(pdf));
////        sb.append("]\n");
//       sb.append("\n}");
//
//        System.out.println("====>\n" + sb);
//
//        return sb.toString();
//    }

//    private String prepMedsJsonDetail(List<Map<String, String>> medsDisp) {
//
//        StringBuilder sb = null;
//
//        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
//        String domain = "VitalSign";
//
////DBG ONLY - REMOVE
////patientId = "http://patients.kmr.org/danno/2-224";
//
//        //-----------------------------------------
//        // BUILD patientDataFact json parent elems
//        //-----------------------------------------
//        PatientDataFact pdf = new PatientDataFact();
//        pdf.setFactType("Medications");
//        pdf.setTrxnType("list");
//        pdf.setVisibleGridHeaders(true);
//        pdf.setMaxColumns(12);
//        pdf.setSuccessStatus(true);
//
//
//        //------------------
//        // BUILD gridHeaders
//        //------------------
//        GridHeaders gh = new GridHeaders();
//        gh.setColumnId("fillDt");
//        gh.setValue("Fill D/T");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("dispenseDt");
//        gh.setValue("Dispense D/T");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("drugDispensed");
//        gh.setValue("Drug Dispensed");
//        gh.setWidth(4);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("dispAmount");
//        gh.setValue("Disp Amount");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("expirationDt");
//        gh.setValue("Expiration Date");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("location");
//        gh.setValue("Location");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        gh = new GridHeaders();
//        gh.setColumnId("pharmacist");
//        gh.setValue("Pharmacist");
//        gh.setWidth(10);
//        pdf.getGridHeaders().add(gh);
//
//        //------------------
//        //Begin building the Response Json String.
//        //------------------
//        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//        sb = new StringBuilder("{\n\"patientDataFact\": \n");
//
//        //---------------------------------------------
//        // Build Medicaton Fact Array
//        // KMRO MAPPED DATA:
//        /*  [{medicationCode=199083,
//         * dateTimeOrderFilled=1999-08-25T00:00:00Z,
//         * hasFillQuantity=90,
//         * dateTimeOrderDispensed=1999-08-25T00:00:00Z,
//         * medicationTerminologyCode=2.16.840.1.113883.6.88,
//         * medicationLabel=Ascorbic Acid 250 MG Oral Tablet,
//         * medicationTerminologyLabel=RXNORM,
//         * dateTimeOfExpiration=2000-08-25T00:00:00Z}
//        */
//        //---------------------------------------------
//        Iterator<Map<String, String>> medsIter = medsDisp.iterator();
//        while(medsIter.hasNext()) {
//            Map<String, String> med = medsIter.next();
//
//            Facts f = new Facts();
//            f.setOrderDate(med.get("dateTimeOfOrder"));
//            f.setMedication(med.get("medicationLabel"));
//            f.setCode(med.get("medicationCode"));
//            f.setCodeSystemName(med.get("medicationTerminologyLabel"));
//            f.setCodeSystemCode(med.get("medicationTerminologyCode"));
//            f.setSig(med.get("sig"));
//            f.setDuration("TBD");
//            f.setQuantity("TBD");
//            f.setRefills(med.get("numberOfRefillsAllowed"));
//            f.setPrescriber("TBD");
//            f.setSource("TBD");
//            f.setItemId(med.get("s"));
//            f.getHoverTexts().add("");
//            f.getHoverTexts().add(med.get("medicationCode")
//                             +" ("+ med.get("medicationTerminologyLabel")+ ")");
//            f.setStatus("TBD");
//
//            pdf.getFacts().add(f);
//        }
//
//
//        sb.append(gson.toJson(pdf));
////        sb.append("]\n");
//       sb.append("\n}");
//
//        System.out.println("====>\n" + sb);
//
//        return sb.toString();
//    }

//    @Test
//    public void testGetMeds() throws Exception {
//        System.out.println("\n======================");
//        System.out.println("testGetMeds");
//        System.out.println("======================");
//
//        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
//
//        String patientId = null;
//        String domain = null;
//        List<Map<String, String>> conceptDefinitionList = null;
//        List<Map<String, String>> conceptValueList = null;
//        Iterator<Map<String, String>> itr = null;
//        String conceptCode = null;
//
//        //----------------------------------
//        // use patient 224 for examples
//        // if going to KMRO; use 1 for KMRO10
//        //----------------------------------
//        patientId = "http://patients.kmr.org/danno/2-1";
//
//        if (KMROPatientTriples.sparqlEP.equals(KMROPatientTriples.KMRO_EP))
//            patientId = "http://patients.kmr.org/danno/2-224";
//
//
//        //----------------------------------------
//        // GETTING A LIST OF ALL MEDICATION ORDERS
//        //----------------------------------------
//        System.out.println("\n\n=== Get medication orders for a patient, ordered by date ===\n");
//        List<Map<String, String>> medicationOrderList = KMROPatientTriples.getMedicationOrdersByPatient(patientId);
//        System.out.println(medicationOrderList);
//
////        prepMedsJsonList(patientId, medicationOrderList);
//
//        //----------------------------------------
//        // GETTING A DETAIL OF ALL MEDICATION ORDERS
//        //----------------------------------------
////        System.out.println("\n\n=== For all med orders, get dispensations ===\n");
////        itr = medicationOrderList.iterator();
////        while(itr.hasNext()) {
////            Map<String, String> medicationOrder = itr.next();
////            String orderId = medicationOrder.get("s");
////            List<Map<String, String>> diss = KMROPatientTriples.getDispensationsOfMedicationOrder(orderId);
////            System.out.println("\n\tOrder:" + orderId + ":" + "\n\t\t" + diss);
////        }
//
//        //------------------------------------------
//        //GETTING A SPECIFIC MEDICATION ORDER DETAIL by orderId
//        //------------------------------------------
//        String orderId2 = "http://patients.kmr.org/danno/100-10405";
//        System.out.println("GETTING SPEICFIC ORDER: "+ orderId2);
//        List<Map<String, String>> diss2 = KMROPatientTriples.getDispensationsOfMedicationOrder(orderId2);
//        System.out.println("\n\tOrder2:" + orderId2 + ":" + "\n\t\t" + diss2);
//
//
//        //prepMedsJsonDetail(diss2);
//
//        //----------------------------------
//        // PROCESS - Davide
//        //----------------------------------
////        // Get Medication Orders
////        String xmlReply = null;
////        domain = "MedicationOrder";
////        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
////        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
////        System.out.println(xmlReply + "\n==========\n");
////
////        // Get Dispensations - will refer back to orders
////        domain = "Dispensation";
////        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
////        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
////        System.out.println(xmlReply + "\n==========\n");
//
//    }
//
////    @Test
////    public void testGetLabs() throws Exception {
////
////        //.....................
////        PatientDataFact pdf = new PatientDataFact();
////        pdf.setFactType("Labs");
////        pdf.setTrxnType("list");
////        pdf.setVisibleGridHeaders(true);
////        pdf.setMaxColumns(12);
////        pdf.setSuccessStatus(true);
////
////
////        DetailTabs detailTab = new DetailTabs();
////        detailTab.setLabel("Detail");
////        detailTab.setVisible(false);
////        detailTab.setType("graph");
////        detailTab.setResponseType("detail");
////        pdf.getDetailTabs().add(detailTab);
////
////        GridHeaders gh = new GridHeaders();
////        gh.setColumnId("description");
////        gh.setValue("Description");
////        gh.setWidth(20);
////        pdf.getGridHeaders().add(gh);
////
////        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
////
////        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\": \n");
////        //.....................
////
////
////        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
////
////        String patientId = null;
////        String domain = null;
////        List<Map<String, String>> conceptDefinitionList = null;
////        List<Map<String, String>> conceptValueList = null;
////        Iterator<Map<String, String>> itr = null;
////        String conceptCode = null;
////
////        //----------------------------------
////        // use patient 224 for examples
////        // if going to KMRO; use 1 for KMRO10
////        //----------------------------------
////        patientId = "http://patients.kmr.org/danno/2-1";
////
////        if (KMROPatientTriples.sparqlEP.equals(KMROPatientTriples.KMRO_EP))
////            patientId = "http://patients.kmr.org/danno/2-224";
////
////
////
////        //----------------------------------
////        // PROCESS
////        //----------------------------------
////        String xmlReply = null;
////
//////        domain = "LabTestResult";
//////        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
//////        xmlReply = KMROPatientTriples.getShallowFactList(patientId, domain, 10, 0);
//////        System.out.println(xmlReply + "\n==========\n");
//////
//////        domain = "LabTestResult";
//////        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
//////        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
//////        System.out.println(xmlReply + "\n==========\n");
//////
//////        // Panels contain results - deep == get both
//////        domain = "LabPanel";
//////        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
//////        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
//////        System.out.println(xmlReply + "\n==========\n");
//////
////
////        //----------------------------------
////        // PROCESS
////        //----------------------------------
////        System.out.println("\n\n=== Get distinct Lab concepts asserted for a patient ===\n");
////        domain = "LabTestResult";
////
////        conceptDefinitionList = KMROPatientTriples.getConceptsOfPatientByDomain(patientId, domain);
////        System.out.println(conceptDefinitionList);
////
////        System.out.println("\n\n=== For all LOINC labs of a patient, get values, ordered by date ===\n");
////        itr = conceptDefinitionList.iterator();
////        while(itr.hasNext()) {
////            Map<String, String> conceptDefinition = itr.next();
////            String terminologySystemLabel = conceptDefinition.get("terminologySystemLabel");
////            // only get LOINC
////            if (!terminologySystemLabel.equals("LOINC"))
////                continue;
////            conceptCode = conceptDefinition.get("conceptCode");
////            String conceptLabel = conceptDefinition.get("conceptLabel");
////            List<Map<String, String>> cvl = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
////            System.out.println("\n\tLOINC:" + conceptLabel + ":" + conceptCode + "\n\t\t" + cvl);
////        }
////    }
////
////    @Test
////    public void testGetVitals() throws Exception {
////
////        //.....................
////        PatientDataFact pdf = new PatientDataFact();
////        pdf.setFactType("Vitals");
////        pdf.setTrxnType("list");
////        pdf.setVisibleGridHeaders(true);
////        pdf.setMaxColumns(12);
////        pdf.setSuccessStatus(true);
////
////        DetailTabs detailTab = new DetailTabs();
////        detailTab.setLabel("Detail");
////        detailTab.setVisible(false);
////        detailTab.setType("graph");
////        detailTab.setResponseType("detail");
////        pdf.getDetailTabs().add(detailTab);
////
////        GridHeaders gh = new GridHeaders();
////        gh.setColumnId("description");
////        gh.setValue("Description");
////        gh.setWidth(20);
////        pdf.getGridHeaders().add(gh);
////
////        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
////
////        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\": \n");
////        //.....................
////
////        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
////
////        String patientId;
////        String domain;
////
////        patientId = "http://patients.kmr.org/danno/2-1";
////        // use patient 224 for examples if going to KMRO; use 1 for 10
////        if (KMROPatientTriples.sparqlEP.equalsIgnoreCase(KMROPatientTriples.KMRO_EP))
////        {
////            patientId = "http://patients.kmr.org/danno/2-224";
////        }
////
////        System.out.println("\n\n=== Get distinct Vital Sign concepts asserted for a patient ===\n");
////        domain = "VitalSign";
////        List<Map<String, String>> conceptDefinitionList = KMROPatientTriples.getConceptsOfPatientByDomain(patientId, domain);
////        System.out.println(conceptDefinitionList);
////
////        System.out.println("\n\n=== Get distinct dates, in order, on which any vital was asserted for a patient ===\n");
////        List<String> datesOfDomain = KMROPatientTriples.getDatesOfPatientByDomain(patientId, domain);
////        System.out.println(datesOfDomain);
////
////        //------------------
////        // BUILD gridHeaders
////        //------------------
////        Iterator datesIter = datesOfDomain.iterator();
////        int i = 1;
////        while (datesIter.hasNext()) {
////            String aDate = (String) datesIter.next();
////
////            GridHeaders dateGH = new GridHeaders();
////            dateGH.setColumnId("column" + i++);
////            dateGH.setValue(DateUtil.parse(aDate));
////            dateGH.setWidth(10);
////            pdf.getGridHeaders().add(dateGH);
////        }
////
////
////        System.out.println("\n\n=== Get values of particular concept for a patient, ordered by date ===\n");
////        String conceptCode = "4500638"; // VA for Temperature.s
////        List<Map<String, String>> conceptValueList = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
////        System.out.println(conceptValueList);
////
////
////        System.out.println("\n\n=== For all LOINC vital types of a patient, get values, ordered by date ===\n");
////        Iterator<Map<String, String>> itr = conceptDefinitionList.iterator();
////
////        sb.append(gson.toJson(pdf));
////        sb.deleteCharAt(sb.length()-1);
////        sb.append(",\"facts\": [\n");
////
////        int factCount = 0;
////        //iterating through each row/concept
////        while (itr.hasNext()) {
////            factCount++;
////            Map<String, String> conceptDefinition = itr.next();
////            String terminologySystemLabel = conceptDefinition.get("terminologySystemLabel");
////            // only get LOINC
////            if (!terminologySystemLabel.equals("LOINC")) {
////                continue;
////            }
////            conceptCode = conceptDefinition.get("conceptCode");
////            String conceptLabel = conceptDefinition.get("conceptLabel");
////            List<Map<String, String>> cvl = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
////            //System.out.println("\n\tLOINC:" + conceptLabel + ":" + conceptCode + "\n\t\t" + cvl);
////
////            if (factCount > 1) {
////                sb.append(",");
////            }
////            sb.append("{\"description\": \"" + conceptLabel + "(" + cvl.get(1).get("unit") + ")\",\n");
////            sb.append("\"code\": \"" + conceptCode + "\",\n");
////            sb.append("\"codeSystemName\": \"" + terminologySystemLabel + "\",\n");
////            sb.append("\"codeSystemCode\": \"" + "NEED-LOINC-CODE" + "\",\n");
////
////            //loop through all cvls and get val
////            Iterator<Map<String, String>> valueIter = cvl.iterator();
////            int j = 0;
////            while (valueIter.hasNext()) {
////                Map<String, String> valueDef = valueIter.next();
////                 if (j >= 1) {
////                    sb.append(",");
////                }
////                sb.append("\"column"+(++j)+"\": \""+ valueDef.get("value") +"\"\n");
////
////
////            }
////            //hovertext
////            sb.append(",\"hoverTexts\": [\""+ conceptCode +" ("+ terminologySystemLabel + ")\"]\n");
////
////            //itemId - don't need for Vitals
////            //status - don't need for Vitals
////
////            sb.append("\n}");
////
////        }
////
////        sb.append("]\n");
////
////        sb.append("\n}\n}");
////        System.out.println("====>\n" + sb);
////
////
////    }













    /**
     * @throws Exception
     */
    //@Test
    public void testGetVitalsFromKRMO() throws Exception {

        System.out.println("\n======================");
        System.out.println("testGetVitalsFromKRMO");
        System.out.println("======================");


        String text = null;

        KMROPatientTriples triples = new KMROPatientTriples();

        String patientId;
        String domain;
        String factId;
        String xmlReply = null;
        String inXSD = "/home/nhin/Properties/schemas/kmr2.xsd";

        //--------------------------------------
        // GET list of all Vitals for patientId
        //--------------------------------------
        patientId = "http://patients.kmr.org/danno/2-1";
        domain = "VitalSign";
        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + "> ===\n");
 //TMN       xmlReply = triples.getShallowFactList(patientId, domain, 10, 0, "dateTimeReported");
        System.out.println(xmlReply + "\n==========\n");

        //--------------------------------------
        // VALIDATE KRMO XML
        //--------------------------------------
        // 1. Lookup a factory for the W3C XML Schema language
        SchemaFactory factory =
            SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");

        // 2. Compile the schema.
        Source schemaFile = new StreamSource(new File(inXSD));
        Schema schema = factory.newSchema(schemaFile);

        // 3. Get a validator from the schema.
        Validator validator = schema.newValidator();

        // 4. Parse the document you want to check.
        Source source = new StreamSource(xmlReply);

        // 5. Check the document
        try {
            validator.validate(source);
            System.out.println("VALID.");
            assertTrue(true);
        }
        catch (SAXException ex) {
            System.out.println("INVALID because ");
            System.out.println(ex.getMessage());
            assertFalse(false);
        }

        //--------------------------------------
        // TRANSFORM to GUI XML
        //--------------------------------------
        String inXML = xmlReply;
        String inXSL = "test.xsl";
        String outTXT = "test.xml";

        try {
            transform(inXML, inXSL, outTXT);
        } catch (TransformerConfigurationException e) {
            System.err.println("Invalid factory configuration");
            System.err.println(e);
        } catch (TransformerException e) {
            System.err.println("Error during transformation");
            System.err.println(e);
        }


        //--------------------------------------
        // PRINT OUT XML
        //--------------------------------------
//        XMLSerializer xmlser = new XMLSerializer();
//        xmlser.setTypeHintsEnabled(true);
//
//        JSON jsonOut = xmlser.read(text);
//        String jsonRsp = jsonOut.toString(2);
//
//        System.out.println("\nJSON=\n" + jsonRsp);
//

        //--------------------------------------
        // TRANSFORM TO GUI XML
        //--------------------------------------

        assertTrue(true);
    }


    //@Test
    public void testTransform() throws Exception {

        String inXML = "/home/nhin/test/deep_Vitals.xml";
        String inXSL = "/home/nhin/test/toKMR2Format.xslt";
        String outTXT = "/home/nhin/test/output.xml";

//        String inXML = "/home/nhin/simpletest/cd.xml";
//        String inXSL = "/home/nhin/simpletest/cd.xslt";
//        String outTXT = "/home/nhin/simpletest/output.xml";

        try {
            transform(inXML, inXSL, outTXT);
        } catch (TransformerConfigurationException e) {
            System.err.println("Invalid factory configuration");
            System.err.println(e);
        } catch (TransformerException e) {
            System.err.println("Error during transformation");
            System.err.println(e);
        }

    }


    public void transform (String inXML,String inXSL,String outTXT)
    throws TransformerConfigurationException,
           TransformerException
    {
    	TransformerFactory factory = TransformerFactory.newInstance();

        	StreamSource xslStream = new StreamSource(inXSL);
        	Transformer transformer = factory.newTransformer(xslStream);
        	transformer.setErrorListener(new MyErrorListener());

        	StreamSource in = new StreamSource(inXML);
        	StreamResult out = new StreamResult(outTXT);
        	transformer.transform(in,out);
        	System.out.println("The generated XML file is:" + outTXT);

    }

    private String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
class MyErrorListener implements ErrorListener {
    public void warning(TransformerException e)
                throws TransformerException {
        show("Warning",e);
        throw(e);
    }
    public void error(TransformerException e)
                throws TransformerException {
        show("Error",e);
        throw(e);
    }
    public void fatalError(TransformerException e)
                throws TransformerException {
        show("Fatal Error",e);
        throw(e);
    }
    private void show(String type,TransformerException e) {
        System.out.println(type + ": " + e.getMessage());
        if(e.getLocationAsString() != null)
            System.out.println(e.getLocationAsString());

    }
}