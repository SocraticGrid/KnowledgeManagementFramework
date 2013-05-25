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
package org.socraticgrid.adapter.factservice.client;

import org.socraticgrid.adapter.fact.AllergyFactType;
import org.socraticgrid.adapter.fact.CareRecordPayloadType;
import org.socraticgrid.adapter.fact.CodeLabelPair;
import org.socraticgrid.adapter.fact.EncounterSearchPayloadType;
import org.socraticgrid.adapter.fact.FactQueryRequestType;
import org.socraticgrid.adapter.fact.FactQueryResponseType;
import org.socraticgrid.adapter.fact.FactType;
import org.socraticgrid.adapter.fact.ProblemFactType;
import org.socraticgrid.adapter.factservice.AdapterFactService;
import org.socraticgrid.adapter.factservice.AdapterFactService_Service;
import org.socraticgrid.adapter.factservice.FaultMessage;
import org.socraticgrid.util.CommonUtil;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class AdapterFactServiceClient {

   private static Log log = LogFactory.getLog(AdapterFactServiceClient.class);
   public static final String FACT_SERVICE_QNAME = "urn:gov:hhs:fha:nhinc:adapter:factservice";
   /**  Handle to Common Data Access Layer web services  */
   private AdapterFactService_Service factService = null;
   /**  Facts web services URL  */
   private String serviceEndpoint = null;
   /** CDA date formater  */
   private static DateFormat cdaDateFormat = null;

   public AdapterFactServiceClient(String serviceEndpoint) {
      initService(serviceEndpoint);
      cdaDateFormat = new SimpleDateFormat("yyyyMMddHHmmssZ"); // YYYYMMDDHHmmss-0000
   }

   private void initService(String serviceEndpoint) {
      if (serviceEndpoint != null && !serviceEndpoint.isEmpty()) {
         this.serviceEndpoint = serviceEndpoint;
         URL baseUrl = org.socraticgrid.adapter.factservice.AdapterFactService_Service.class.getResource(".");
         try {
            URL url = new URL(baseUrl, serviceEndpoint);
            factService = new AdapterFactService_Service(url, new QName(FACT_SERVICE_QNAME, "AdapterFactService"));
         } catch (MalformedURLException e) {
            log.error("Failed to create URL for the wsdl Location: " + serviceEndpoint);
         }
      } else {
         log.error("Failed to create URL for the wsdl Location: " + serviceEndpoint);
      }
   }

   public List<FactType> getDemographicsFact(String patientId) {
      log.debug("getDemographicsFact: params patientId[=" + patientId + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);
            //request.setRoot(homeCommunityId);
            //request.setAssigningAuthorityName(homeCommunityName);

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getDemographicsFact(createCareRecordQuery("PRPA_IN201307UV02", "PRPA_TE201307UV02", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }

      return new ArrayList<FactType>();
   }

   public List<FactType> getAllergyFacts(String patientId, String startDate, String endDate) {
      log.debug("getAllergyFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("INTOLIST");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getAllergyFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   public List<FactType> getMedicationFacts(String patientId, String startDate, String endDate) {
      log.debug("getMedicationFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("HISTMEDLIST");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getMedicationFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   /**
    * Retrieve problems of a patient within a specific date ranges.
    * @param patientId
    * @param startDate
    * @param endDate
    * @return
    */
   public List<FactType> getProblemFacts(String patientId, String startDate, String endDate) {
      log.debug("getProblemFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("PROBLIST");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getProblemFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   public List<FactType> getTestResultFacts(String patientId, String startDate, String endDate) {
      log.debug("getTestResultFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("LABCAT");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getTestResultFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (FaultMessage fm) {
            log.error("getTestResultFacts: fault message=[" + fm.getMessage() + "]");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   /**
    * Retrieve procedures of a patient within a specific date ranges.
    * @param patientId
    * @param startDate
    * @param endDate
    * @return
    */
   public List<FactType> getProcedureFacts(String patientId, String startDate, String endDate) {
      log.debug("getProcedureFacts: params patientId=[" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("PSVCCAT");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getProcedureFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (FaultMessage fm) {
            log.error("getProcedureFacts: fault message=[" + fm.getMessage() + "]");
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   /**
    * Retrieve vital signs data of a patient.
    * @param patientId
    * @param startDate
    * @param endDate
    * @return
    */
   public List<FactType> getVitalFacts(String patientId, String startDate, String endDate) {
      log.debug("getVitalFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("COBSCAT");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getVitalsFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   public List<FactType> getImmunizationFacts(String patientId, String startDate, String endDate) {
      log.debug("getImmunizationFacts: params patientId[=" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            CareRecordPayloadType payload = new CareRecordPayloadType();
            payload.setPatientId(patientId);

            // set care provision code
            payload.setCareProvisionCode("IMMUCAT");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setCareRecordStartTimePeriod(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setCareRecordEndTimePeriod(endDate);
            } else {
               payload.setCareRecordEndTimePeriod(this.convertToCDATime(new Date()));
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getImmunizationFacts(createCareRecordQuery("QUPC_IN043100UV", "QUPC_TE043100UV01", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   /**
    * Rerieve appointment facts of a patient.
    * @param patientId
    * @param startDate
    * @param endDate
    * @param statusList
    * @return
    */
   public List<FactType> getAppointmentFacts(String patientId, String startDate, String endDate, List<String> statusList) {
      log.debug("getAppointmentFacts: params patientId=[" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            EncounterSearchPayloadType payload = new EncounterSearchPayloadType();

            // set patient
            payload.setPatientId(patientId);

            // set type of encounter to outpatient
            payload.setTypeOfEncounter("AMB");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setEncounterStartTimeFrame(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setEncounterEndTimeFrame(endDate);
            } else {
               payload.setEncounterEndTimeFrame(this.convertToCDATime(new Date()));
            }

            // set status: "active", "cancelled", "completed", "new"
            if (statusList != null && !statusList.isEmpty()) {
               for (String status : statusList) {
                  payload.getEncounterStatus().add(status);
               }
            } else {
               payload.getEncounterStatus().add("active");
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getAppointmentFacts(createEncounterSearchQuery("REPC_IN040100UV", "REPC_TE040100UV", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   /**
    * Rerieve appointment facts of a patient.
    * @param patientId
    * @param startDate
    * @param endDate
    * @param statusList
    * @return
    */
   public List<FactType> getAdmissionFacts(String patientId, String startDate, String endDate, List<String> statusList) {
      log.debug("getAdmissionFacts: params patientId=[" + patientId + "] startDate=[" +
              startDate + "] endDate=[" + endDate + "]");

      if (patientId != null && !patientId.isEmpty()) {
         try {
            // build payload
            EncounterSearchPayloadType payload = new EncounterSearchPayloadType();

            // set patient
            payload.setPatientId(patientId);

            // set type of encounter to outpatient
            payload.setTypeOfEncounter("IMP");

            // set start and end date
            if (startDate != null && !startDate.isEmpty()) {
               payload.setEncounterStartTimeFrame(startDate);
            }

            if (endDate != null && !endDate.isEmpty()) {
               payload.setEncounterEndTimeFrame(endDate);
            } else {
               payload.setEncounterEndTimeFrame(this.convertToCDATime(new Date()));
            }

            // set status: "active", "cancelled", "completed", "new"
            if (statusList != null && !statusList.isEmpty()) {
               for (String status : statusList) {
                  payload.getEncounterStatus().add(status);
               }
            } else {
               payload.getEncounterStatus().add("active");
            }

            // call web services
            if (factService == null) {
               initService(serviceEndpoint);
            }

            AdapterFactService port = factService.getCommonDataLayerFactPort();
            FactQueryResponseType resp =
                    port.getAdmissionFacts(createEncounterSearchQuery("REPC_IN040100UV", "REPC_TE040100UV", payload));

            return resp.getProblemFactOrMedicationFactOrAllergyFact();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return new ArrayList<FactType>();
   }

   private FactQueryRequestType createCareRecordQuery(String interaction, String triggerEvent, CareRecordPayloadType payload) {
      FactQueryRequestType request = new FactQueryRequestType();
      request.setQueryId(CommonUtil.generateId());
      request.setSenderId(this.getClass().getName());
      request.setInteractionId(interaction);
      request.setTriggerEventCode(triggerEvent);
      request.setCareRecordPayload(payload);

      return request;
   }

   /**
    * Construct an encounter search query request.
    * @param interaction
    * @param triggerEvent
    * @param payload
    * @return
    */
   private FactQueryRequestType createEncounterSearchQuery(String interaction, String triggerEvent, EncounterSearchPayloadType payload) {
      FactQueryRequestType request = new FactQueryRequestType();
      request.setQueryId(CommonUtil.generateId());
      request.setSenderId(this.getClass().getName());
      request.setInteractionId(interaction);
      request.setTriggerEventCode(triggerEvent);
      request.setEncounterSearchPayload(payload);

      return request;
   }

   private String convertToCDATime(Date date) {
      return cdaDateFormat.format(date);
   }

   public static String toString(FactType obj) {
      StringBuffer str = new StringBuffer();

      if (obj instanceof org.socraticgrid.adapter.fact.AllergyFactType) {
         AllergyFactType fact = (AllergyFactType) obj;
         str.append("type[AllergyFact] id=[" + (!fact.getId().isEmpty() ? fact.getId().get(0).getValue() : "") + "] patientId=[" +
                 fact.getPatientId().getValue() + "] adverseEventDate=[" + fact.getAdverseEventDate() + "]adverseEventType=[" +
                 (fact.getAdverseEventType() != null ? (fact.getAdverseEventType().getCode() + "|" + fact.getAdverseEventType().getLabel()) : "") +
                 "] codedProduct=[" + (fact.getCodedProduct() != null ? (fact.getCodedProduct().getCode() + "|" + fact.getCodedProduct().getLabel()) : "") +
                 "] freeTextProduct=[" + fact.getFreeTextProduct() + "] #reactions=[" + fact.getReaction().size() + "]");
         for (int i = 0; i < fact.getReaction().size(); i++) {
            CodeLabelPair reaction = fact.getReaction().get(i).getCodedReaction();
            str.append(" reaction " + i + "=[" + (reaction != null ? reaction.getCode() : fact.getReaction().get(i).getFreeTextReaction()) + "] ");
         }
      } else if (obj instanceof org.socraticgrid.adapter.fact.ProblemFactType) {
         ProblemFactType fact = (ProblemFactType) obj;
         str.append("type[ProblemFact] id=[" + (!fact.getId().isEmpty() ? fact.getId().get(0).getValue() : "") + "] patientId=[" +
                 fact.getPatientId().getValue() + "] problemType=[" +
                 (fact.getProblemType() != null ? (fact.getProblemType().getCode() + "|" + fact.getProblemType().getLabel()) : "") +
                 "] problemDate=[" + fact.getProblemDate() + ",resolutionDate=" + fact.getResolutionDate() + "] codedProblem=[" +
                 (fact.getCodedProblem() != null ? (fact.getCodedProblem().getCode() + "|" + fact.getCodedProblem().getLabel()) : "") +
                 "] freeTextProblem=[" + fact.getFreeTextProblem() + "] codedProblemStatus=[" +
                 (fact.getCodedProblemStatus() != null ? fact.getCodedProblemStatus().getCode() : "") + "] #treatingProvider=[" +
                 fact.getTreatingProvider().size() + "]" + System.getProperty("line.separator") + "\t");
         for (int i = 0; i < fact.getTreatingProvider().size(); i++) {
            str.append("provider=[" + fact.getTreatingProvider().get(i).getPrefix() +
                    fact.getTreatingProvider().get(i).getFirstName() + fact.getTreatingProvider().get(i).getMiddleName() +
                    fact.getTreatingProvider().get(i).getFamilyName() + fact.getTreatingProvider().get(i).getSuffix() + "] ");
         }
         str.append(System.getProperty("line.separator"));
      }

      return str.toString();
   }
}
