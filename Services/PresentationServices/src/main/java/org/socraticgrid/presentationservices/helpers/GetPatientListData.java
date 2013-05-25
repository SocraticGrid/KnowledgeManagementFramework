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
import org.socraticgrid.adapter.fact.AllergyFactType;
import org.socraticgrid.adapter.fact.EncounterFactType;
import org.socraticgrid.adapter.fact.FactType;
import org.socraticgrid.adapter.fact.ImmunizationFactType;
import org.socraticgrid.adapter.fact.LabOrderFactType;
import org.socraticgrid.adapter.fact.MedicationFactType;
import org.socraticgrid.adapter.fact.NameFactType;
import org.socraticgrid.adapter.fact.OrderFactType;
import org.socraticgrid.adapter.fact.PersonFactType;
import org.socraticgrid.adapter.fact.ProblemFactType;
import org.socraticgrid.adapter.fact.ProcedureFactType;
import org.socraticgrid.adapter.fact.ReactionFactType;
import org.socraticgrid.adapter.fact.ResultFactType;
import org.socraticgrid.adapter.fact.ValueType;
import org.socraticgrid.adapter.fact.VitalFactType;
import org.socraticgrid.adapter.factservice.client.AdapterFactServiceClient;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import org.socraticgrid.presentationservices.resources.GetPatientDataResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sushma
 */
public class GetPatientListData {

    private String domain = "";
    private String endPoint = "";
    private String userId = "";
    private String toDate = "";
    private String fromDate = "";
    private List vitalCodes;
    private String DSS = "dss";
    private String ADAPTER_FACT_SERVICE = "ADAPTER_FACT_SERVICE";

    public GetPatientListData(String domain, String endPoint, String userId, String fromDate, String toDate, List vitalCodeSystems) {
        this.domain = domain;
        this.endPoint = endPoint;
        this.userId = userId;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.vitalCodes = vitalCodeSystems;
    }

    public String getPatientData() throws Exception {
        StringBuilder sb = new StringBuilder();
        List<FactType> res = null;
        List<FactType> vlist = new ArrayList();
        AdapterFactServiceClient factClient = null;
        List<FactHelper> list = new ArrayList();
        PatientList plist = new PatientList();

        try {
            PropertyAccessor.forceRefresh(DSS);
            factClient = new AdapterFactServiceClient(PropertyAccessor.getProperty(DSS, ADAPTER_FACT_SERVICE));
        } catch (PropertyAccessException ex) {
            Logger.getLogger(GetPatientListData.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (domain.equalsIgnoreCase(GetPatientDataResource.ALLERGIES)) {
            res = factClient.getAllergyFacts(userId, toDate, toDate);
            if (res != null && !res.isEmpty()) {
                plist.setFactCount(res.size());
                Logger.getLogger("Number of AllergiesFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    AllergyFactType allergy = (AllergyFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setAllergyName("");
                    fh.setAllergyType("");
                    fh.setCode("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setFactNo(i + 1);
                    fh.setItemId("");
                    fh.setOnsetDate("");
                    fh.setCommunityId("");

                    //if(null != allergy.getId)
                    // fh.setItemId();
                    List id = allergy.getId();
                    if (null != id) {
                        ValueType value = (ValueType) id.get(0);
                        fh.setItemId(value.getValue());
                        if (allergy.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                    }
                    if (!allergy.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(allergy.getSourceSystem());
                    }
                    if (null != allergy.getCodedProduct()) {
                        fh.setAllergyName(allergy.getCodedProduct().getLabel());
                    }
                    if (null != allergy.getAdverseEventDate()) {
                        fh.setOnsetDate(allergy.getAdverseEventDate().toString());
                    }
                    if (null != allergy.getAdverseEventType()) {
                        fh.setCode(allergy.getAdverseEventType().getCode());
                        fh.setCodeSystem(allergy.getAdverseEventType().getCodeSystem());
                        fh.setAllergyType(allergy.getAdverseEventType().getLabel());
                        fh.setCodeSystemName(allergy.getAdverseEventType().getCodeSystemName());
                    }
                    if (null != allergy.getReaction()) {
                        for (ReactionFactType reaction : allergy.getReaction()) {
                            Map map = new HashMap();
                            List rlist = new ArrayList();
                            if (null != reaction.getSeverity() && null != reaction.getSeverity().getCodedSeverity()) {
                                map.put("reactionSeverity", reaction.getSeverity().getCodedSeverity().getLabel());
                            }
                            if (null != reaction.getCodedReaction()) {
                                map.put("reactionName", reaction.getCodedReaction().getLabel());
                            }
                            //TODO: Need to implement when reactionDate is returning back from CAL
                            map.put("reportDate", "");
                            map.put("adverseEventDate", "");
                            rlist.add(map);
                            fh.setReactions(rlist);
                        }
                    }

                    list.add(fh);
                }
            }

            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Allergies Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.DEMOGRAPHICS)) {
            res = factClient.getDemographicsFact(userId);
            if (res != null && !res.isEmpty()) {
                plist.setFactCount(1);
                Logger.getLogger("Number of PatientDemographicsFactType fact objects :" + 1);
                for (int i = 0; i < res.size(); i++) {
                    PersonFactType person = (PersonFactType) res.get(0);
                    FactHelper fh = new FactHelper();
                    fh.setFactNo(i + 1);
                    fh.setCommunityId("");
                    fh.setFirstName("");
                    fh.setLastName("");
                    fh.setMiddleName("");
                    fh.setSuffix("");
                    fh.setDateOfBirth("");
                    fh.setPatientId(userId);
                    fh.setAge("");
                    fh.setItemId("");
                    fh.setSex("");
                    fh.setEmergencyContact("");
                    fh.setRelationship("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setCode("");

                    if (null != person.getLegalName()) {
                        fh.setFirstName(person.getLegalName().getFirstName());
                        fh.setLastName(person.getLegalName().getFamilyName());
                        fh.setMiddleName(person.getLegalName().getMiddleName());
                        if (null != person.getLegalName().getSuffix()) {
                            fh.setSuffix(person.getLegalName().getSuffix());
                        }
                    }

                    if ((null != person.getSourceSystem()) && !person.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(person.getSourceSystem());
                    }
                    if (null != person.getDateOfBirth()) {
                        fh.setDateOfBirth(person.getDateOfBirth().toString());
                    }
                    if (null != person.getAge()) {
                        fh.setAge(person.getAge().getValue());
                    }
                    if (null != person.getId()) {
                        //for(int j =0; j<person.getId().size(); j++){
                        ValueType value = (ValueType) person.getId().get(0);
                        fh.setItemId(value.getValue());
                        fh.setCode(value.getValue());
                        fh.setCodeSystem(value.getCodeSystem());
                        fh.setCodeSystemName(value.getCodeSystemName());
                        if (null == person.getSourceSystem() || (null != person.getSourceSystem() && person.getSourceSystem().isEmpty())) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                        //}
                    }
                    if (null != person.getGender()) {
                        fh.setSex(person.getGender().getLabel());
                    }

                    //TODO Need to add the Emergency Contact and the relationship
                    list.add(fh);
                }
            }

            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Demographics Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.LABS)) {
            res = factClient.getTestResultFacts(userId, fromDate, toDate);
            if (res != null && !res.isEmpty()) {
                plist.setFactCount(res.size());
                Logger.getLogger("Number of LabsFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    ResultFactType lab = (ResultFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setFactNo(i + 1);
                    fh.setCommunityId("");
                    fh.setResultDate("");
                    fh.setSpecimenDate("");
                    fh.setResultStatus("");
                    fh.setOrderingProvider("");
                    fh.setItemId("");
                    fh.setCode("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setLabel("");
                    fh.setType("");
                    fh.setSource("");
                    fh.setAccessionDate("");
                    fh.setPanelStatus("");

                    if (lab.getCodedTestType() != null) {
                        fh.setLabel(lab.getCodedTestType().getLabel());
                    }
                    if (null != lab.getResultDate()) {
                        fh.setResultDate(lab.getResultDate().toString());
                    }
                    if (null != lab.getSpecimenDate()) {
                        fh.setSpecimenDate(lab.getSpecimenDate().toString());
                    }
                    if (null != lab.getSpecimenDate()) {
                        fh.setAccessionDate(lab.getSpecimenDate().toString());
                    }
                    if (null != lab.getSpecimen()) {
                        fh.setSource(lab.getSpecimen().toString());
                    }
                    if (null != lab.getResultStatus()) {
                        fh.setResultStatus(lab.getResultStatus());
                    }
                    if (!lab.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(lab.getSourceSystem());
                    }
                    String name;
                    if (null != lab.getOrderingProvider()) {
                        name = lab.getOrderingProvider().getFirstName() + lab.getOrderingProvider().getFamilyName();
                        fh.setOrderingProvider(name);
                    }

                    List id = lab.getId();
                    if (null != id) {
                        ValueType value = (ValueType) id.get(0);
                        fh.setItemId(value.getValue());
                        if (lab.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                    }
                    if (null != lab.getCodedPanelType()) {
                        fh.setCodeSystem(lab.getCodedPanelType().getCodeSystem());
                        fh.setCode(lab.getCodedPanelType().getCode());
                        fh.setCodeSystemName(lab.getCodedPanelType().getCodeSystemName());
                        fh.setType(lab.getCodedPanelType().getLabel());
                    }

                    list.add(fh);

                }
            }

            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Test Results Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.VITALS)) {
            res = factClient.getVitalFacts(userId, fromDate, toDate);
            if (res != null && !res.isEmpty()) {
                if (null == vitalCodes) {
                    return getVitalsfromCode(plist, res, list);
                } else if (!vitalCodes.isEmpty()) {
                    for (int i = 0; i < res.size(); i++) {
                        for (Object vitalCode : vitalCodes) {
                            String code = vitalCode.toString();
                            VitalFactType vital = (VitalFactType) res.get(i);
                            if (vital.getCodedResultType().getCode().equalsIgnoreCase(code)) {
                                vlist.add(res.get(i));
                            }
                        }
                    }
                    return getVitalsfromCode(plist, vlist, list);
                }
            }
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.IMMUNIZATIONS)) {
            res = factClient.getImmunizationFacts(userId, fromDate, toDate);

            if (res != null && !res.isEmpty()) {
                Logger.getLogger("Number of ImmunizationFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    plist.setFactCount(res.size());
                    ImmunizationFactType immunization = (ImmunizationFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setItemId("");
                    fh.setFactNo(i + 1);
                    fh.setCommunityId("");
                    fh.setCode("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setLabel("");
                    fh.setAdministeredDate("");

                    fh.setFactNo(i + 1);
                    if (immunization.getCodedBrandName() != null) {
                        fh.setLabel(immunization.getCodedBrandName().getLabel());
                        fh.setCode(immunization.getCodedBrandName().getCode());
                        fh.setCodeSystem(immunization.getCodedBrandName().getCodeSystem());
                        fh.setCodeSystemName(immunization.getCodedBrandName().getCodeSystemName());

                    }
                    if (null != immunization.getAdministeredDate()) {
                        fh.setAdministeredDate(immunization.getAdministeredDate().toString());
                    }
                    if (!immunization.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(immunization.getSourceSystem());
                    }
                    if (null != immunization.getId()) {
                        ValueType value = (ValueType) immunization.getId().get(0);
                        fh.setItemId(value.getValue());
                        if (immunization.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                    }
                    list.add(fh);
                }
            }
            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Imunizations Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.MEDICATIONS)) {
            res = factClient.getMedicationFacts(userId, fromDate, toDate);
            if (res != null && !res.isEmpty()) {
                Logger.getLogger("Number of MedicationFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    plist.setFactCount(res.size());
                    MedicationFactType medication = (MedicationFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setFactNo(i + 1);
                    fh.setCommunityId("");
                    fh.setItemId("");
                    fh.setLabel("");
                    fh.setSigFreeText("");
                    fh.setCodeSigRouteLabel("");
                    fh.setSigDoseValue("");
                    fh.setSigDoseUnit("");
                    fh.setSigFrequencyUnit("");
                    fh.setSigFrequencyValue("");
                    fh.setOrderExpirationDate("");
                    fh.setOrderingProvider("");
                    fh.setSigDuration("");
                    fh.setCodedAdministrationUnitCodeLabel("");

                    if (null != medication.getId()) {
                        ValueType value = (ValueType) medication.getId().get(0);
                        fh.setItemId(value.getValue());
                    }
                    if (medication.getCodedProductName() != null) {
                        fh.setLabel(medication.getCodedProductName().getLabel());
                    }
                    if (!medication.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(medication.getSourceSystem());
                    }
                    if (null != medication.getOrder()) {
                        if (null != medication.getOrder().getOrderExpirationDateTime()) {
                            fh.setOrderExpirationDate(medication.getOrder().getOrderExpirationDateTime().toString());
                        }
                        if (null != medication.getOrder().getOrderingProvider()) {
                            fh.setOrderingProvider(medication.getOrder().getOrderingProvider().getFirstName() + " " + medication.getOrder().getOrderingProvider().getFamilyName());
                        }
                    }
                    if (null != medication.getSigFreeText()) {
                        fh.setSigFreeText(medication.getSigFreeText());
                    }
                    if (null != medication.getCodedSigRoute()) {
                        fh.setCodeSigRouteLabel(medication.getCodedSigRoute().getLabel());
                    }
                    if (null != medication.getSigDose()) {
                        if (!medication.getSigDose().getUnit().isEmpty()) {
                            fh.setSigDoseUnit(medication.getSigDose().getUnit());
                        }
                        if (!medication.getSigDose().getValue().isEmpty()) {
                            fh.setSigDoseValue(medication.getSigDose().getValue());
                        }
                    }
                    if (null != medication.getSigFrequency()) {
                        if (!medication.getSigFrequency().getUnit().isEmpty()) {
                            fh.setSigFrequencyUnit(medication.getSigFrequency().getUnit());
                        }
                        if (!medication.getSigFrequency().getValue().isEmpty()) {
                            fh.setSigFrequencyValue(medication.getSigFrequency().getValue());
                        }
                    }
                    if (null != medication.getId()) {
                        ValueType value = (ValueType) medication.getId().get(0);
                        fh.setItemId(value.getValue());
                        if (medication.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                    }
                    //TODO Add the SigDuration and CodedAdministrationUnitCode

                    list.add(fh);
                }
            }
            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Medications Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.PROBLEMS)) {
            res = factClient.getProblemFacts(userId, fromDate, toDate);
            if (res != null && !res.isEmpty()) {
                plist.setFactCount(res.size());
                Logger.getLogger("Number of ProblemFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    ProblemFactType problem = (ProblemFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setFactNo(i + 1);
                    fh.setCode("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setItemId("");
                    fh.setLabel("");
                    fh.setProblemDate("");
                    fh.setResolutionDate("");
                    fh.setProblemType("");
                    fh.setCodedProblemStatus("");
                    fh.setTreatingProviders("");
                    fh.setCommunityId("");

                    if (problem.getCodedProblem() != null) {
                        fh.setLabel(problem.getCodedProblem().getLabel());
                        fh.setCode(problem.getCodedProblem().getCode());
                        fh.setCodeSystem(problem.getCodedProblem().getCodeSystem());
                        fh.setCodeSystemName(problem.getCodedProblem().getCodeSystemName());
                    }
                    if (!problem.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(problem.getSourceSystem());
                    }
                    if (null != problem.getProblemDate()) {
                        fh.setProblemDate(problem.getProblemDate().toString());
                    }
                    if (null != problem.getResolutionDate()) {
                        fh.setResolutionDate(problem.getResolutionDate().toString());
                    }
                    if (null != problem.getCodedProblemStatus()) {
                        fh.setCodedProblemStatus(problem.getCodedProblemStatus().getCode());
                    }
                    if (null != problem.getProblemType()) {
                        fh.setProblemType(problem.getProblemType().getLabel());
                    }
                    if (null != problem.getTreatingProvider()) {
                        String treatingProviders = "";
                        for (int k = 0; k < problem.getTreatingProvider().size(); k++) {
                            NameFactType name = problem.getTreatingProvider().get(k);
                            if (!treatingProviders.isEmpty()) {
                                treatingProviders = treatingProviders + "," + name.getFirstName() + " " + name.getFamilyName();
                            } else {
                                treatingProviders = name.getFirstName() + " " + name.getFamilyName();
                            }
                        }
                        fh.setTreatingProviders(treatingProviders);
                    }
                    if (null != problem.getId()) {
                        ValueType value = (ValueType) problem.getId().get(0);
                        fh.setItemId(value.getValue());
                        if (problem.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }

                    }
                    list.add(fh);

                }
            }

            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Problems Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
        } else if (domain.equalsIgnoreCase(GetPatientDataResource.PROCEDURES)) {
            res = factClient.getProcedureFacts(userId, fromDate, toDate);
            if (res != null && !res.isEmpty()) {
                plist.setFactCount(res.size());
                Logger.getLogger("Number of ProcedureFactType fact objects :" + res.size());
                for (int i = 0; i < res.size(); i++) {
                    ProcedureFactType procedure = (ProcedureFactType) res.get(i);
                    FactHelper fh = new FactHelper();
                    fh.setFactNo(i + 1);
                    fh.setItemId("");
                    fh.setCommunityId("");
                    fh.setCode("");
                    fh.setCodeSystem("");
                    fh.setCodeSystemName("");
                    fh.setLabel("");
                    fh.setProcedureDate("");
                    fh.setTreatingProviders("");

                    if (procedure.getProcedureType() != null) {
                        fh.setLabel(procedure.getProcedureType().getLabel());
                        fh.setCode(procedure.getProcedureType().getCode());
                        fh.setCodeSystem(procedure.getProcedureType().getCodeSystem());
                        fh.setCodeSystemName(procedure.getProcedureType().getCodeSystemName());

                    }
                    if (!procedure.getSourceSystem().isEmpty()) {
                        fh.setCommunityId(procedure.getSourceSystem());
                    }
                    if (null != procedure.getProcedureDate()) {
                        fh.setProcedureDate(procedure.getProcedureDate().toString());
                    }
                    if (null != procedure.getTreatingProvider()) {
                        String treatingProviders = "";
                        for (int k = 0; k < procedure.getTreatingProvider().size(); k++) {
                            NameFactType name = procedure.getTreatingProvider().get(k);
                            if (!treatingProviders.isEmpty()) {
                                treatingProviders = treatingProviders + "," + name.getFirstName() + " " + name.getFamilyName();
                            } else {
                                treatingProviders = name.getFirstName() + " " + name.getFamilyName();
                            }
                        }
                        fh.setTreatingProviders(treatingProviders);
                    }

                    if (null != procedure.getId()) {
                        ValueType value = (ValueType) procedure.getId().get(0);
                        fh.setItemId(value.getValue());
                        if (procedure.getSourceSystem().isEmpty()) {
                            fh.setCommunityId(value.getCodeSystem());
                        }
                    }

                    list.add(fh);

                }
            }

            plist.setFacts(list);
            plist.setStatusMessage("Successful retrieval of Procedures Fact List Info");
            plist.setSuccessStatus(true);

            sb = new StringBuilder("{\n\"patientDataFact\": ");
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            sb.append(gson.toJson(plist) + "}");
            return sb.toString();
          } 
//else if (domain.equalsIgnoreCase(GetPatientDataResource.RELATIONSHIPS)) {
//            res = factClient.(userId, fromDate, toDate);
//            if (res != null && !res.isEmpty()) {
//                plist.setFactCount(res.size());
//                Logger.getLogger("Number of ProcedureFactType fact objects :" + res.size());
//                for (int i = 0; i < res.size(); i++) {
//                    ProcedureFactType procedure = (ProcedureFactType) res.get(i);
//                    FactHelper fh = new FactHelper();
//                    fh.setFactNo(i + 1);
//                    fh.setItemId("");
//                    fh.setCommunityId("");
//                    fh.setCode("");
//                    fh.setCodeSystem("");
//                    fh.setCodeSystemName("");
//                    fh.setLabel("");
//                    fh.setProcedureDate("");
//                    fh.setTreatingProviders("");
//
//                    if (procedure.getProcedureType() != null) {
//                        fh.setLabel(procedure.getProcedureType().getLabel());
//                        fh.setCode(procedure.getProcedureType().getCode());
//                        fh.setCodeSystem(procedure.getProcedureType().getCodeSystem());
//                        fh.setCodeSystemName(procedure.getProcedureType().getCodeSystemName());
//
//                    }
//                    if (!procedure.getSourceSystem().isEmpty()) {
//                        fh.setCommunityId(procedure.getSourceSystem());
//                    }
//                    if (null != procedure.getProcedureDate()) {
//                        fh.setProcedureDate(procedure.getProcedureDate().toString());
//                    }
//                    if (null != procedure.getTreatingProvider()) {
//                        String treatingProviders = "";
//                        for (int k = 0; k < procedure.getTreatingProvider().size(); k++) {
//                            NameFactType name = procedure.getTreatingProvider().get(k);
//                            if (!treatingProviders.isEmpty()) {
//                                treatingProviders = treatingProviders + "," + name.getFirstName() + " " + name.getFamilyName();
//                            } else {
//                                treatingProviders = name.getFirstName() + " " + name.getFamilyName();
//                            }
//                        }
//                        fh.setTreatingProviders(treatingProviders);
//                    }
//
//                    if (null != procedure.getId()) {
//                        ValueType value = (ValueType) procedure.getId().get(0);
//                        fh.setItemId(value.getValue());
//                        if (procedure.getSourceSystem().isEmpty()) {
//                            fh.setCommunityId(value.getCodeSystem());
//                        }
//                    }
//
//                    list.add(fh);
//
//                }
//            }
//
//            plist.setFacts(list);
//            plist.setStatusMessage("Successful retrieval of Procedures Fact List Info");
//            plist.setSuccessStatus(true);
//
//            sb = new StringBuilder("{\n\"patientDataFact\": ");
//            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//            sb.append(gson.toJson(plist) + "}");
//            return sb.toString();
//        } 
        //else if (domain.equalsIgnoreCase(GetPatientDataResource.ENCOUNTERS)) {
        //            res = factClient.getEncounterFacts(userId, fromDate, toDate);
        //            if (res != null && !res.isEmpty()) {
        //                plist.setFactCount(res.size());
        //                Logger.getLogger("Number of EncounterFactType fact objects :" + res.size());
        //                for (int i = 0; i < res.size(); i++) {
        //                    EncounterFactType procedure = (EncounterFactType) res.get(i);
        //                    if (procedure.getProcedureType() != null) {
        //                        fh.setFactLabelName(procedure.getProcedureType().getLabel());
        //                        if (null != procedure.getProcedureDate()) {
        //                            fh.setFactDate(procedure.getProcedureDate().toString());
        //                        }
        //                        list.add(fh);
        //                    }
        //                }
        //            }
        //
        //            plist.setFacts(list);
        //            plist.setStatusMessage("Successful retrieval of Procedures Fact List Info");
        //            plist.setSuccessStatus(true);
        //
        //            sb = new StringBuilder("{\n\"patientDataFact\": ");
        //            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        //            sb.append(gson.toJson(plist) + "}");
        //            return sb.toString();
        //  }
//        else if (domain.equalsIgnoreCase(GetPatientDataResource.ORDERS)) {
//            res = factClient.getOrderFacts(userId, toDate, toDate);
//            if (res != null && !res.isEmpty()) {
//                plist.setFactCount(res.size());
//                Logger.getLogger("Number of OrderFactType fact objects :" + res.size());
//                for (int i = 0; i < res.size(); i++) {
//                    LabOrderFactType order = (LabOrderFactType) res.get(i);
//                    FactHelper fh = new FactHelper();
//                    if (order.getOrderDateTime() != null) {
//                        fh.setFactDate(order.getOrderDateTime().toString());
////                        if (null != procedure.getProcedureDate()) {
////                            fh.setFactDate(procedure.getProcedureDate().toString());
////                        }
//                        list.add(fh);
//                    }
//                }
//            }
//
//            plist.setFacts(list);
//            plist.setStatusMessage("Successful retrieval of Procedures Fact List Info");
//            plist.setSuccessStatus(true);
//
//            sb = new StringBuilder("{\n\"patientDataFact\": ");
//            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
//            sb.append(gson.toJson(plist) + "}");
//            return sb.toString();
//        }

        return sb.toString();
    }

    public String generateError(String errorMessage) {
        String error = "";
        ErrorResponse err = new ErrorResponse(errorMessage, "patientDataFact");
        error = err.generateError();
        return error;
    }

    public String getVitalsfromCode(PatientList plist, List<FactType> res, List<FactHelper> list) {
        StringBuilder sb;
        plist.setFactCount(res.size());
        Logger.getLogger("Number of VitalsFactType fact objects :" + res.size());
        for (int i = 0; i < res.size(); i++) {
            VitalFactType vital = (VitalFactType) res.get(i);
            FactHelper fh = new FactHelper();
            fh.setFactNo(i + 1);
            fh.setResultStatus("");
            fh.setResultDate("");
            fh.setResultValue("");
            fh.setResultValueUnit("");
            fh.setCode("");
            fh.setCodeSystem("");
            fh.setCodeSystemName("");
            fh.setLabel("");

            if (vital.getCodedResultType() != null) {
                fh.setLabel(vital.getCodedResultType().getLabel());
                fh.setCode(vital.getCodedResultType().getCode());
                fh.setCodeSystem(vital.getCodedResultType().getCodeSystem());
                fh.setCodeSystemName(vital.getCodedResultType().getCodeSystemName());
            }
            if (null != vital.getResultDate()) {
                fh.setResultDate(vital.getResultDate().toString());
            }
            if (null != vital.getId()) {
                ValueType value = (ValueType) vital.getId().get(0);
                fh.setItemId(value.getValue());
            }
            if (null != vital.getSourceSystem()) {
                fh.setCommunityId(vital.getSourceSystem());
            }
            if (null != vital.getResultValue()) {
                fh.setResultValue(vital.getResultValue().getValue());
                fh.setResultValueUnit(vital.getResultValue().getUnit());
            }
            if (null != vital.getResultStatus()) {
                fh.setResultStatus(vital.getResultStatus());
            }

            list.add(fh);
        }



        plist.setFacts(list);
        plist.setStatusMessage("Successful retrieval of Vitals Fact List Info");
        plist.setSuccessStatus(true);
        sb = new StringBuilder("{\n\"patientDataFact\": ");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        sb.append(gson.toJson(plist) + "}");
        return sb.toString();
    }
}
