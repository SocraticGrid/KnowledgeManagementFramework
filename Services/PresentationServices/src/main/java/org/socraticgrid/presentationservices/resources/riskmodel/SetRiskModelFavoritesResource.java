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
package org.socraticgrid.presentationservices.resources.riskmodel;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 * @author nhin
 */
public class SetRiskModelFavoritesResource extends BaseResource {

    private String userId;
    private String patientId;
    private String token;
    private String modelIds;
    private String displayThresholds;
    public Form form;

    public SetRiskModelFavoritesResource(Context context, Request request, Response response) {
        super(context, request, response);
        init(context, request, response);
        setModifiable(true);
        Representation rep = request.getEntity();
        form = new Form(rep);
        setParameters(form);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    @Override
    public boolean allowPut() {
        return false;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {

        if (!SessionUtilities.verifyToken(token)) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, "Session token validation failed.");
            return;
        }

        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "SetRiskModelFavorites: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "setRiskModelFavoritesFact");
            String ret = err.generateError();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ret);
            return;
        }

        List<String> modelList = Arrays.asList(modelIds.split(","));
        List<String> thresholdList = Arrays.asList(displayThresholds.split(","));

	String response = "";
        String callResponse = setRiskModelFavorites(modelList, thresholdList);
        if (callResponse.equals("success")) {
             response = "{\n"
			+ "    \"riskModels\": {\n"
			+ "        \"successStatus\": true \n"
			+ "    }\n" + "}";
        }
        else {
            response = "{\n"
			+ "    \"riskModels\": {\n"
			+ "        \"successStatus\": false,\n"
			+ "        \"statusMessage\": \"Error in setRiskModelFavorites\" \n"
			+ "    }\n" + "}";
        }

        System.out.println("SET RISK MODELS RETURNING: " + callResponse);
        System.out.println(response);

        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(response, MediaType.APPLICATION_JSON);
        // TODO handle error case returning json for successStatus
    }

    private void setParameters(Form form) {

        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }

        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("patientId").getValue();
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }

        if (getRequestParameter(form, "modelIds")) {
            modelIds = form.getFirst("modelIds").getValue();
        }

        if (getRequestParameter(form, "displayThresholds")) {
            displayThresholds = form.getFirst("displayThresholds").getValue();
        }
    }

    private boolean getRequestParameter(Form form, String param) {
        if (form.getFirst(param) != null) {
            return true;
        }
        return false;
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("userId", userId);
        fields.put("patientId", patientId);
        fields.put("modelIds", modelIds);
        fields.put("displayThresholds", displayThresholds);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }

    private String setRiskModelFavorites(List<String> modelIds,
                                         List<String> displayThresholds)
    {
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();

        int i = 0;
        for (String modelId : modelIds) {
            
            RiskModelFavorite fav = new RiskModelFavorite();

            //-----------------------------------------------
            // Alternate key is userId, patientId and modelId
            // Get records for this model and see if there is a match
            // with the incoming data so update is done instead of insert
            //-----------------------------------------------
            List modelList = util.getRMFByModelId(modelId);
            fav.setRiskmodelfavoriteid(null);

            for (Object o : modelList) {
                RiskModelFavorite rmf = (RiskModelFavorite) o;
                if (   rmf.getProviderId().equals(userId)
                    && rmf.getPatientId().equals(patientId))
                {
                    fav.setRiskmodelfavoriteid(rmf.getRiskmodelfavoriteid());
                }
            }
            
            fav.setDisplayThreshold(Long.parseLong(displayThresholds.get(i)));
            fav.setModelId(modelId);
            fav.setPatientId(patientId);
            fav.setProviderId(userId);
            util.saveRiskModelFavorite(fav);
            fav = null;
            ++i;
        }
        return "success";
    }
}
