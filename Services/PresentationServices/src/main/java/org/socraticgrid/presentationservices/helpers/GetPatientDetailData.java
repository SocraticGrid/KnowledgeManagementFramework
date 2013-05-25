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
package org.socraticgrid.presentationservices.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.adapter.fact.FactType;
import org.socraticgrid.adapter.fact.ImmunizationFactType;
import org.socraticgrid.adapter.fact.MedicationFactType;
import org.socraticgrid.adapter.fact.PersonFactType;
import org.socraticgrid.adapter.fact.ProblemFactType;
import org.socraticgrid.adapter.fact.ProcedureFactType;
import org.socraticgrid.adapter.fact.ResultFactType;
import org.socraticgrid.adapter.factservice.client.AdapterFactServiceClient;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import org.socraticgrid.presentationservices.resources.GetPatientDataResource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sushma
 */
public class GetPatientDetailData {

    private String domain = "";
    private String userId = "";
    private String toDate = "";
    private String fromDate = "";
    private String itemId = "";
    private String communityId = "";
    private String DSS = "dss";
    private String ADAPTER_FACT_SERVICE = "ADAPTER_FACT_SERVICE";
    private boolean found = false;

    public GetPatientDetailData(String domain, String userId, String fromDate, String toDate, String itemId, String communityId) {
        this.domain = domain;
        this.userId = userId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.itemId = itemId;
        this.communityId = communityId;
    }

    public String getPatientDetailData() throws Exception {
        StringBuilder sb = new StringBuilder();
        List<FactType> res = null;
        AdapterFactServiceClient factClient = null;
        List<FactHelper> list = new ArrayList();
        PatientList plist = new PatientList();

        try {
            PropertyAccessor.forceRefresh(DSS);
            factClient = new AdapterFactServiceClient(PropertyAccessor.getProperty(DSS, ADAPTER_FACT_SERVICE));
        } catch (PropertyAccessException ex) {
            Logger.getLogger(GetPatientDetailData.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (domain.equalsIgnoreCase(GetPatientDataResource.DEMOGRAPHICS)) {
            res = factClient.getDemographicsFact(userId);

            PersonFactType person = new PersonFactType();
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    person = (PersonFactType) res.get(i);
                    if (person.getId().get(0).getValue().equalsIgnoreCase(itemId) && 
                            person.getId().get(0).getCodeSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");

                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(person) + "}" + "}");
                } else {
                    return ("");
                }
            }
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.LABS)) {
            res = factClient.getTestResultFacts(userId, fromDate, toDate);

            ResultFactType lab = null;
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    lab = (ResultFactType) res.get(i);
                    if (lab.getId().get(0).getValue().equalsIgnoreCase(itemId) && 
                            lab.getSourceSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");

                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(lab) + "}" + "}");
                } else {
                    return "";
                }
            }

        } else if (domain.equalsIgnoreCase(GetPatientDataResource.IMMUNIZATIONS)) {
            res = factClient.getImmunizationFacts(userId, fromDate, toDate);

            ImmunizationFactType immunization = null;
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    immunization = (ImmunizationFactType) res.get(i);
                    if (immunization.getId().get(0).getValue().equalsIgnoreCase(itemId) && immunization.getSourceSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(immunization) + "}" + "}");
                } else {
                    return "";
                }
            }

        } else if (domain.equalsIgnoreCase(GetPatientDataResource.MEDICATIONS)) {
            res = factClient.getMedicationFacts(userId, fromDate, toDate);

            MedicationFactType medication = null;
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    medication = (MedicationFactType) res.get(i);
                    if (medication.getId().get(0).getValue().equalsIgnoreCase(itemId) && 
                            medication.getSourceSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(medication) + "}" + "}");
                } else {
                    return "";
                }
            }

        }
        else if (domain.equalsIgnoreCase(GetPatientDataResource.PROCEDURES)) {
            res = factClient.getProcedureFacts(userId, fromDate, toDate);

            ProcedureFactType procedure = null;
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    procedure = (ProcedureFactType) res.get(i);
                    if (procedure.getId().get(0).getValue().equalsIgnoreCase(itemId) && procedure.getSourceSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(procedure) + "}" + "}");
                } else {
                    return "";
                }
            }
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.PROBLEMS)) {
            res = factClient.getProblemFacts(userId, fromDate, toDate);

            ProblemFactType problem = null;
            if (res != null && !res.isEmpty()) {
                for (int i = 0; i < res.size(); i++) {
                    problem = (ProblemFactType) res.get(i);
                    if (problem.getId().get(0).getValue().equalsIgnoreCase(itemId) && problem.getSourceSystem().equalsIgnoreCase(communityId)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    sb = new StringBuilder("{\n\"patientDataFact\" : { \n");
                    sb.append("\"successStatus\": " + true + " ,\n");
                    sb.append("\"statusMessage\": " + "\"Successful retrieval of the detail fact object\"" + " ,\n");
                    sb.append("\"fact\": \n");
                    Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
                    sb.append(gson.toJson(problem) + "}" + "}");
                } else {
                    return "";
                }
            }
        }
        return sb.toString();
    }
}
