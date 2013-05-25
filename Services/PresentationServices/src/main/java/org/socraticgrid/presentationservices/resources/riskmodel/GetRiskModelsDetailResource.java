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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.resources.GetCalendarResource;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.mas.helpers.SyncDialogueHelper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 *
 * @author nhin
 */
public class GetRiskModelsDetailResource extends BaseResource {

    private String userId;
    private String patientId;
    private String token;
    private String modelIds;
    final Logger logger = Logger.getLogger(GetCalendarResource.class.getName());

    public GetRiskModelsDetailResource(Context context, Request request, Response response) {
        super(context, request, response);

        setModifiable(true);

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
            this.userId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("userId");
            this.patientId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("patientId");
            this.modelIds =
                    request.getResourceRef().getQueryAsForm().getFirstValue("modelIds");
            this.token =
                    request.getResourceRef().getQueryAsForm().getFirstValue("token");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        // This representation has only one type of representation.
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
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String ret = handleParamValidation();
        if (!ret.equals("")) {
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        String[] mldArr = modelIds.split(",");

        //List<GetRiskModelsDetailResponse> result = getRiskModelsDetail(userId, patientId, modelIdList);
        //Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        // String toReturn = gson.toJson(result);
        String resp = getRiskModelsDetail(userId, patientId, mldArr);
        System.out.println("GET RISK MODELS DETAIL RETURNING: " + resp);
        Representation representation =
                new StringRepresentation(resp, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String handleParamValidation() {
        String ret = "";
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("userId", userId);
        fieldMap.put("patientId", patientId);
        fieldMap.put("modelIds", modelIds);
        ParameterValidator validator = new ParameterValidator(fieldMap);
        String failures = validator.validateMissingOrEmpty();
        if (failures.length() > 1) {
            String errorMessage = "riskModelsDetailFact: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "riskModelsDetailFact");
            ret = err.generateError();
            return ret;
        }
        return ret;
    }

    private String getRiskModelsDetail(String userId, String patientId,
                                        String[] modelIdList)
    {
        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SyncDialogueHelper helper =
                SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("userId", userId);
        args.put("patientId", patientId);
        args.put("modelIds", modelIdList);

        String root = "{\n\"riskModels\": {";
        String jsonRoot = null;
        String jsonResp = null;
        try {
            helper.invokeRequest("getRiskModelsDetail", args);
            String xml = (String) helper.getReturn(true);
            XMLSerializer xmlser = new XMLSerializer();
            JSON jsonOut = xmlser.read(xml);

            jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
            jsonResp = jsonRoot + ","+ jsonOut.toString(2).substring(1) + "}";

        }
        catch (Exception e) {
            jsonResp = SynchronousRequestHelperFactory.prepErrorResponse(root, e.getMessage());

            e.printStackTrace();
        }
        return jsonResp;
    }

    
//TODO: Need to realign to new response structure.
//    private String getStubData() {
//        return "{\n"
//                + "    \"riskModelsDetailFact\" : {\n"
//                + "        \"successStatus\":true,\n"
//                + "        \"statusMessage\":\"This is a message to the user if the request was unsuccessful\",\n"
//                + "        \"riskModels\":[\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId2\",\n"
//                + "                \"position\":1,\n"
//                + "                \"severity\":80,\n"
//                + "                \"title\":\"Aesthma\",\n"
//                + "                \"disease\":\"Aesthma description\",\n"
//                + "                \"relativeRisk\":60,\n"
//                + "                \"relativeRiskRange\":10,\n"
//                + "                \"displayThreshold\":50,\n"
//                + "                \"surveyId\":\"UNIQUESURVEYID2\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId1\",\n"
//                + "                \"position\":0,\n"
//                + "                \"severity\":30,\n"
//                + "                \"title\":\"PTSD\",\n"
//                + "                \"disease\":\"PTSD description\",\n"
//                + "                \"relativeRisk\":30,\n"
//                + "                \"relativeRiskRange\":25,\n"
//                + "                \"displayThreshold\":40,\n"
//                + "                \"surveyId\":\"UNIQUESURVEYID1\",\n"
//                + "                \"dxProcessId\":\"DIAG7310\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId3\",\n"
//                + "                \"position\":2,\n"
//                + "                \"severity\":0,\n"
//                + "                \"title\":\"Diabetes\",\n"
//                + "                \"disease\":\"Diabetes description\",\n"
//                + "                \"relativeRisk\":5,\n"
//                + "                \"relativeRiskRange\":25,\n"
//                + "                \"displayThreshold\":80,\n"
//                + "                \"dxProcessId\":\"DIAG73456\"\n"
//                + "            }\n"
//                + "        ]\n"
//                + "    }\n"
//                + "}";
//    }
}
