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
package org.socraticgrid.presentationservices.resources;

import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.GetPatientDetailData;
import org.socraticgrid.presentationservices.helpers.GetPatientListData;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.json.JSONException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import java.util.logging.Logger;

import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 *
 * @author Sushma
 */
public class GetPatientDataResource extends BaseResource {

    /** create or update action */
    private String domain = "";
    private String fromDate = "";
    private String toDate = "";
    private String userId = "";
    private String token = "";
    private String responseType = "";
    private int returnCount;
    private String factCodeSystem = "";
    private String factCode = "";
    private String itemId = "";
    private String communityId = "";
    private List vitalCodes;
    final Logger logger = Logger.getLogger(GetPatientDataResource.class.getName());
    private String endPoint = "";
    public static String DEMOGRAPHICS = "Demographics";
    public static String ALLERGIES = "Allergies";
    public static String LABS = "TestResults";
    public static String VITALS = "Vitals";
    public static String IMMUNIZATIONS = "Immunizations";
    public static String MEDICATIONS = "Medications";
    public static String PROBLEMS = "Problems";
    public static String PROCEDURES = "Procedures";
    public static String ENCOUNTERS = "Encounters";
    public static String DIAGNOSTICIMAGING = "DiagnosticImaging";
    public static String ORDERS = "Orders";
    private String LIST = "List";
    private String DETAIL = "Detail";
    private String codes = "";
    String ret = "";

    /**
     * Creates a new GetPatientDataResource object.
     *
     * @param  context   context
     * @param  request   request
     * @param  response  response
     */
    public GetPatientDataResource(Context context, Request request, Response response) {
        super(context, request, response);

        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            logger.log(Level.INFO, "query: " + query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {
            userId = getParam(request, "userId");
            domain = getParam(request, "domain");
            fromDate = getParam(request, "fromDate");
            toDate = getParam(request, "toDate");
            token = getParam(request, "token");
            responseType = getParam(request, "responseType");
            communityId = getParam(request, "communityId");
            itemId = getParam(request, "itemId");
            codes = getParam(request, "vitalCodes");
            vitalCodes = getListFromCodes(codes);
            returnCount = Integer.parseInt(getParam(request, "returnCount"));
            factCode = getParam(request, "factCode");
            factCodeSystem = getParam(request, "factCodeSystem");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());

        }
        if (domain.equalsIgnoreCase(VITALS)) {
            fromDate = this.getDateParameter(request, "fromDate", "Patient", "Vitals", "Begin");
            toDate = this.getDateParameter(request, "toDate", "Patient", "Vitals", "End");
        }
        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "The token was not found, the session may have timed out. Token is " + token;
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        init(context, request, response);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) {
        String output = "";

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("userId", userId);
        fields.put("domain", domain);
        fields.put("token", token);
        fields.put("responseType", responseType);
        if(responseType.equalsIgnoreCase(DETAIL)){
            fields.put("itemId", itemId);
            fields.put("communityId", communityId);
        }
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();
        String errorMessage = "";
        endPoint = this.getProperty("JSONPojoFactServiceEndpointV2");
        GetPatientListData gpld = new GetPatientListData(domain, endPoint, userId, fromDate, toDate, vitalCodes);
        GetPatientDetailData gpdd = new GetPatientDetailData(domain, userId, fromDate, toDate, itemId, communityId);
        if (failures.length() > 1) {
            errorMessage = "patientData: "
                    + failures + "is a missing required field";
            ret = gpld.generateError(errorMessage);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        } else if ((null != domain) && !(domain.equalsIgnoreCase(ALLERGIES) || 
                domain.equalsIgnoreCase(LABS) || (domain.equalsIgnoreCase(DEMOGRAPHICS))
                || domain.equalsIgnoreCase(VITALS) || domain.equalsIgnoreCase(IMMUNIZATIONS)
                || domain.equalsIgnoreCase(MEDICATIONS) || domain.equalsIgnoreCase(PROBLEMS)
                || domain.equalsIgnoreCase(PROCEDURES) || domain.equalsIgnoreCase(ORDERS)
                || domain.equalsIgnoreCase(DIAGNOSTICIMAGING) || domain.equalsIgnoreCase(ENCOUNTERS))) {
            errorMessage = "Domain value is not valid";
            ret = gpld.generateError(errorMessage);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        } else if ((null != responseType) && !(responseType.equalsIgnoreCase(LIST) || responseType.equalsIgnoreCase(DETAIL))) {
            errorMessage = "Response Type is not valid";
            ret = gpld.generateError(errorMessage);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }
        
        if (responseType.equalsIgnoreCase(LIST)) {
            try {
                try {
                    output = gpld.getPatientData();
                } catch (JSONException ex) {
                    Logger.getLogger(GetPatientDataResource.class.getName()).log(Level.SEVERE, null, ex);
                    ret = gpld.generateError(ex.getMessage());
                    return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
                } catch (ResourceException ex) {
                    Logger.getLogger(GetPatientDataResource.class.getName()).log(Level.SEVERE, null, ex);
                    ret = gpld.generateError(ex.getMessage());
                    return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
                }
            } catch (Exception ex) {
                Logger.getLogger(GetPatientDataResource.class.getName()).log(Level.SEVERE, null, ex);
                ret = gpld.generateError(ex.getMessage());
                return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
            }
        } else if (responseType.equalsIgnoreCase(DETAIL)) {

            try {
                output = gpdd.getPatientDetailData();
                if (output.isEmpty()) {
                    Logger.getLogger(GetPatientDataResource.class.getName()).log(Level.INFO, " Detail object returned is null");
                    ret = gpld.generateError("There is no matching Fact Object found for the itemId and communityId provided.");
                    return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
                }
            } catch (Exception ex) {
                Logger.getLogger(GetPatientDataResource.class.getName()).log(Level.SEVERE, null, ex);
                ret = gpld.generateError(ex.getMessage());
                return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
            }
        }
        Representation representation =
                new StringRepresentation(output, MediaType.APPLICATION_JSON);
        return representation;

    }

    private String getParam(Request request, String paramName) {
        return request.getResourceRef().getQueryAsForm().getFirstValue(paramName);
    }

    private List getListFromCodes(String codes) {
        List<String> vitalCodes = Arrays.asList(codes.split(","));
        
        return vitalCodes;
    }  
}
