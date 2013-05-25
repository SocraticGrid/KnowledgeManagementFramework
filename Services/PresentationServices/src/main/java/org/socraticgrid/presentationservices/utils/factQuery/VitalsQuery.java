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

import org.socraticgrid.kmrolib.KMROPatientTriples;
import org.socraticgrid.kmrolib.utils.DateUtil;
import org.socraticgrid.presentationsservices.facttypes.DetailTabs;
import org.socraticgrid.presentationsservices.facttypes.GridHeaders;
import org.socraticgrid.presentationsservices.facttypes.PatientDataFact;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author nhin
 */
public class VitalsQuery {
    
    private String classname = "\n"+this.getClass().getName()+":";
    private static VitalsQuery instance = null;
    
    public static VitalsQuery getInstance() {
        synchronized (VitalsQuery.class) {
            if (instance == null) {
                instance = new VitalsQuery();
            }
        }
        return instance;
    }
    
    public String createResponse(Map args, boolean useStubbedData) throws FileNotFoundException, Exception{

        String jsonResp = null;

        String domain =         (String) args.get("domain");
        String patientId =      (String) args.get("patientId");
        String userId =         (String) args.get("userId");
        String responseType =   (String) args.get("responseType");
        String itemId =         (String) args.get("itemId");
        String code =           (String) args.get("code");
        String codeSystemCode = (String) args.get("codeSystemCode");
        String fromDate =       (String) args.get("fromDate");

        if (useStubbedData) {
            jsonResp = this.getStubbedXMLListData();
        } else {
            jsonResp = this.mapFact(patientId);
        }

        return jsonResp;
    }
        

    private String mapFact(String patientId) throws Exception {

        KMROPatientTriples KMROPatientTriples = new KMROPatientTriples();
        String domain = "VitalSign";

//DBG ONLY - REMOVE
patientId = "http://patients.kmr.org/danno/2-224";

        //-----------------------------------------
        // BUILD patientDataFact json parent elems
        //-----------------------------------------
        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType("Vitals");
        pdf.setTrxnType("list");
        pdf.setVisibleGridHeaders(true);
        pdf.setMaxColumns(12);
        pdf.setSuccessStatus(true);

        DetailTabs detailTab = new DetailTabs();
        detailTab.setLabel("Detail");
        detailTab.setVisible(false);
        detailTab.setType("graph");
        detailTab.setResponseType("detail");
        pdf.getDetailTabs().add(detailTab);

        GridHeaders gh = new GridHeaders();
        gh.setColumnId("description");
        gh.setValue("Description");
        gh.setWidth(20);
        pdf.getGridHeaders().add(gh);

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\": \n");

        
        //---------------------------------------------
        // GETTING list of distinct Vital Sign concepts
        //---------------------------------------------
        List<Map<String, String>> conceptDefinitionList = KMROPatientTriples.getConceptsOfPatientByDomain(patientId, domain);

        System.out.println("\n===> Get distinct Vital Sign concepts asserted for a patientID="+ patientId+"\n");
        System.out.println(conceptDefinitionList);

        //---------------------------------------------
        // GETTING list of distinct Vital Sign dates
        //---------------------------------------------
        List<String> datesOfDomain = KMROPatientTriples.getDatesOfPatientByDomain(patientId, domain);

        System.out.println("\n\n=== Get distinct dates, in order, on which any vital was asserted for a patient ===\n");
        System.out.println(datesOfDomain);

        //------------------
        // BUILD gridHeaders
        //------------------
        Iterator datesIter = datesOfDomain.iterator();
        int i = 1;
        while (datesIter.hasNext()) {
            String aDate = (String) datesIter.next();

            GridHeaders dateGH = new GridHeaders();
            dateGH.setColumnId("column" + i++);
            dateGH.setValue(DateUtil.parse(aDate));
            dateGH.setWidth(10);
            pdf.getGridHeaders().add(dateGH);
        }
        
//DEBUG ONLY - THIS IS SAMPLE PULL OF A single VITAL CODE.....
//        System.out.println("\n\n=== Get values of particular concept for a patient, ordered by date ===\n");
//        String conceptCode = "4500638"; // VA for Temperature.s
//        List<Map<String, String>> conceptValueList = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
//        System.out.println(conceptValueList);

        String conceptCode = null;
        System.out.println("\n\n=== For all LOINC vital types of a patient, get values, ordered by date ===\n");
        Iterator<Map<String, String>> itr = conceptDefinitionList.iterator();

        sb.append(gson.toJson(pdf));
        sb.deleteCharAt(sb.length()-1);
        sb.append(",\"facts\": [\n");

        int factCount = 0;
        //iterating through each row/concept
        while (itr.hasNext()) {
            factCount++;
            Map<String, String> conceptDefinition = itr.next();
            String terminologySystemLabel = conceptDefinition.get("terminologySystemLabel");
            // only get LOINC
            if (!terminologySystemLabel.equals("LOINC")) {
                continue;
            }
            conceptCode = conceptDefinition.get("conceptCode");
            String conceptLabel = conceptDefinition.get("conceptLabel");
            List<Map<String, String>> cvl = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
            //System.out.println("\n\tLOINC:" + conceptLabel + ":" + conceptCode + "\n\t\t" + cvl);

            if (factCount > 1) {
                sb.append(",");
            }
            sb.append("{\"description\": \"" + conceptLabel + "(" + cvl.get(1).get("unit") + ")\",\n");
            sb.append("\"code\": \"" + conceptCode + "\",\n");
            sb.append("\"codeSystemName\": \"" + terminologySystemLabel + "\",\n");
            sb.append("\"codeSystemCode\": \"" + "NEED-LOINC-CODE" + "\",\n");

            //loop through all cvls and get val
            Iterator<Map<String, String>> valueIter = cvl.iterator();
            int j = 0;
            while (valueIter.hasNext()) {
                Map<String, String> valueDef = valueIter.next();
                 if (j >= 1) {
                    sb.append(",");
                }
                sb.append("\"column"+(++j)+"\": \""+ valueDef.get("value") +"\"\n");


            }
            //hovertext
            sb.append(",\"hoverTexts\": [\""+ conceptCode +" ("+ terminologySystemLabel + ")\"]\n");

            //itemId - don't need for Vitals
            //status - don't need for Vitals

            sb.append("\n}");

        }

        sb.append("]\n");
        sb.append("\n}\n}");
        System.out.println("====>\n" + sb);
        
        return sb.toString();

    }
    
    public String getStubbedXMLListData() throws FileNotFoundException {
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Vitals_List.json";
        System.out.println("PULLING list STATIC DATA:  "+filename);

        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }
    
    
}
