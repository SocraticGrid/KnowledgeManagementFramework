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

package org.socraticgrid.presentationservices.resources.facts;

import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.utils.factQuery.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

public class GetPatientDataResource extends BaseResource {

    /* 
     * fieldMap expected incoming attributes:
     * 
     *  userId
     *  token
     *  domain
     *  responseType     list, detail, ecs, raw, codeDetial
     *  code             optional
     *  codeSystemCode   optional
     *  sectionId        optional
     *                   (responseType==codeDetail) drugUtil, triplesSPL, origSPL
     * 
     *  fromDate         optional
     *  toDate           optional
     *  returnCount      optional
     *
     * SAMPLE gui call:
     *   http://<host>:9763/PresentationServices/getPatientData?domain=medications&responseType=list&userId=99990070&token=
     *   http://<host>:9763/PresentationServices/getPatientData?itemId=10&responseType=detail&code=0093-4160-78&codeSystemCode=ndc&domain=medications&userId=99990070&token=6C
     *   http://<host>:9763/PresentationServices/getPatientData?itemId=10&responseType=ecs&code=0093-4160-78&codeSystemCode=ndc&sectionId=4&userId=99990070&token=6C
     */
    private Map fieldMap = new HashMap();
    
    String patientFactsRepo = null;
    String patientFactsInterface = null;

    static final Logger logger = Logger.getLogger(GetPatientDataResource.class.getName());

    public GetPatientDataResource(Context context, Request request, Response response) {
        
        super(context, request, response);

        String token = null;
        
        setModifiable(true);
        
        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            GetPatientDataResource.logger.log(Level.INFO, "query: {0}", query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }
        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {

            System.out.println("\n===>REQ: " + request.getResourceRef().getQueryAsForm().toString());

            //creating a Map of all params.
            //Note: should not be used if an attrib is an array.
            fieldMap.putAll(request.getResourceRef().getQueryAsForm().getValuesMap());
            
            // When patientId is not given, use the userId as the patient Id.
//            if (CommonUtil.strNullorEmpty( (String)fieldMap.get("patientId")) ) {
//                fieldMap.put("patientId", fieldMap.get("userId"));
//            }

            token = (String) fieldMap.get("token");

        } catch (Exception e) {
            GetPatientDataResource.logger.log(Level.SEVERE, e.getMessage());
        }

        //VERIFY THAT TOKEN BEIGN USED IS STILL VALID.
        if ((token == null) || (!SessionUtilities.verifyToken(token))) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        SessionUtilities.setCORSHeaders(this);
        init(context, request, response);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) throws ResourceException {

        //---------------------------------
        // VALIDATING INPUT PARAMS
        //---------------------------------
        String ret = handleParamValidation();
        
        if (!ret.equals("")) {
            System.out.println("\n==>ERROR: "+ret);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        //---------------------------------
        // PROCESSING
        //---------------------------------
        String resp = "";
        try {
            GetDataUtil gp = new GetDataUtil();
            resp = gp.getData(this.fieldMap);
            
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
        
        //System.out.println("===>GET PATIENT DATA (" + (String) fieldMap.get("domain") + "):" + resp);
        Representation representation = new StringRepresentation(resp, MediaType.APPLICATION_JSON);

        return representation;
    }

    /**
     * handleParamValidation - Check for required incoming parameters from request.
     * @return String, null if no errors.  Else, an relevant error messages will be returned.
     */
    private String handleParamValidation() {
        String ret = "";
        String responseType = (String)this.fieldMap.get("responseType");
        
        //PREP Map to check required detail attributes.
        Map requiredFieldMap = this.fieldMap;
        
        requiredFieldMap.put("userId", this.fieldMap.get("userId"));
        requiredFieldMap.put("responseType", this.fieldMap.get("responseType"));
        
        //INCLUDE required attributes as needed too.
        if (//responseType.equalsIgnoreCase("detail")||
            responseType.equalsIgnoreCase("ecs")) 
        {
            requiredFieldMap.put("code", (String)this.fieldMap.get("code"));
            requiredFieldMap.put("codeSystemCode", (String)this.fieldMap.get("codeSystemCode"));        

        } else {
            requiredFieldMap.put("domain", this.fieldMap.get("domain"));
        }

        ParameterValidator validator = new ParameterValidator(requiredFieldMap);
        String failures = validator.validateMissingOrEmpty();
        
        if (failures.length() > 1) {
            String errorMessage = "GetPatientData: " + failures + "is a missing required field";

            ErrorResponse err = new ErrorResponse(errorMessage, "GetPatientData");
            ret = err.generateError();
            return ret;
        }
        
        return ret;
    }

    
/*
    private String getPatientData() throws Exception {
        String resp = "";

        JSON jsonOut = null;
        String jsonRsp = null;
        

        String domain = (String)this.fieldMap.get("domain");
        String responseType = (String)this.fieldMap.get("responseType");
        
        //GET data for codeDetail, detail, list, or ecs
        if (responseType.equalsIgnoreCase("ecs"))
        {
            jsonRsp = EcsQuery.getInstance().createResponse(fieldMap, useStubbedEcs);
            
        } else {
            
            if (domain.equalsIgnoreCase("Medications")) {
                jsonRsp = MedicationsQuery.getInstance().createResponse(fieldMap, useStubbedMedications);

            } else if (domain.equalsIgnoreCase("Immunizations")) {
                jsonRsp = ImmunizationsQuery.getInstance().createResponse(fieldMap, useStubbedImmunizations);

            } else if (domain.equalsIgnoreCase("Labs")) {
                jsonRsp = LabsQuery.getInstance().createResponse(fieldMap, useStubbedLabs);
            
            } else if (domain.equalsIgnoreCase("Admissions")) {
                jsonRsp = AdmissionsQuery.getInstance().createResponse(fieldMap, useStubbedAdmissions);

            } else if (domain.equalsIgnoreCase("Allergies")) {
                jsonRsp = AllergiesQuery.getInstance().createResponse(fieldMap, useStubbedAllergies);

            } else if (domain.equalsIgnoreCase("Demographics")) {
                jsonRsp = DemographicsQuery.getInstance().createResponse(fieldMap, useStubbedDemographics);

            } else if (domain.equalsIgnoreCase("Problems")) { //Diagnoses
                jsonRsp = DiagnosesQuery.getInstance().createResponse(fieldMap, useStubbedDiagnoses);

            } else if (domain.equalsIgnoreCase("DocInpatient")) {
                jsonRsp = DocInpatientQuery.getInstance().createResponse(fieldMap, useStubbedDocInpatient);


//
//TMN - DOCUMENT is currently being used for OUTPATIENT
//        domain = "Documentation" is too general.
//        domain should be:  ClinicNotes, Directives
//        and need to have relative instance class.
//
//
//            } else if (domain.equalsIgnoreCase("DocDirectives")) {
//                xml = DocDirectives.getInstance().createResponse(fieldMap, useStubbedFacts);
//
             
            } else if (domain.equalsIgnoreCase("ClinicNotes")) {
                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, useStubbedClinicNotes);

            } else if (domain.equalsIgnoreCase("Documentation")) {
                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, useStubbedDocumentation);

            
            } else if (domain.equalsIgnoreCase("Equipment")) {
                jsonRsp = EquipmentsQuery.getInstance().createResponse(fieldMap, useStubbedEquipment);

            } else if (domain.equalsIgnoreCase("Imaging")) {
                jsonRsp = ImagingQuery.getInstance().createResponse(fieldMap, useStubbedImaging);

            } else if (domain.equalsIgnoreCase("Procedures")) {
                jsonRsp = ProceduresQuery.getInstance().createResponse(fieldMap, useStubbedProcedures);

            } else if (domain.equalsIgnoreCase("Vitals")) {
                jsonRsp = VitalsQuery.getInstance().createResponse(fieldMap, useStubbedVitals);

            } else {
                System.out.println("\n===>ERROR: GetPatientDataResource: Domain " + domain + " not supported.\n");
                //TBD - Need better error msg return.
            }


            //TRANSFORM xml TO json for return.
            if (CommonUtil.strNullorEmpty(jsonRsp)) {
                System.out.println("\n===>ERROR: GetPatientData: No response for domain=" + domain
                        + " responseType=" + responseType
                        + "query.\n");

                //TBD - Need better error msg return.
            }

            //DBG ONLY
            //System.out.println("==> PS: jsonResp=\n"+jsonRsp);

        }

        return jsonRsp;
    }
    */
}