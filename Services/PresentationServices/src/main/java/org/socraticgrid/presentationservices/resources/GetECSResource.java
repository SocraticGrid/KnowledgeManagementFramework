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

import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
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
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/*******************************************************************
 *
 *  GetECSResource DEPRECATED --> .facts.GetPatientDataResource.java
 * 
 *******************************************************************/


/**
 *
 * @author nhin
 */
public class GetECSResource extends BaseResource {

    private String code;
    private String codeScheme;
    private String section;
    private String token;
    private static final String ECS_URL = "http://stormwoods.info:8080/WexContentService/resources/codeinfo/";
    final Logger logger = Logger.getLogger(GetECSResource.class.getName());

    public GetECSResource(Context context, Request request, Response response) {
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
            this.code =
                    request.getResourceRef().getQueryAsForm().getFirstValue("code");
            this.codeScheme =
                    request.getResourceRef().getQueryAsForm().getFirstValue("codeScheme");
            this.section =
                    request.getResourceRef().getQueryAsForm().getFirstValue("section");
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
        String result = getECS(codeScheme, code, section);
        Representation representation =
                new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String handleParamValidation() {
        String ret = "";
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("codeScheme", codeScheme);
        ParameterValidator validator = new ParameterValidator(fieldMap);
        String failures = validator.validateMissingOrEmpty();
        if (failures.length() > 1) {
            String errorMessage = "getECSFact: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getECSFact");
            ret = err.generateError();
            return ret;
        }
        return ret;
    }

    private String getECS(String codeScheme, String code, String section) {
        try {
            StringBuilder urlString = new StringBuilder(ECS_URL).append(codeScheme);
            if (!CommonUtil.strNullorEmpty(code)) {
                urlString.append("/").append(code);
            }
            if (!CommonUtil.strNullorEmpty(section)) {
                urlString.append("/").append(section);
            }
            
            logger.info("ECS URL request: " + urlString);
            
            URL url = new URL(urlString.toString());
            String ecsResp = getURLContent(url);
            
            StringBuilder jsonResp = new StringBuilder("{\"getECSFact\": \n");
            jsonResp.append(ecsResp);
            jsonResp.append("}");
            
            return jsonResp.toString();
        } catch (IOException ex) {
            Logger.getLogger(GetECSResource.class.getName()).log(Level.SEVERE, null, ex);
            return "{ getECSFact: { successStatus: false, statusMessage: \"Error fetching content from ECS URL\"}}";
        }
    }

    private String getURLContent(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                // Process each line.
                sb.append(inputLine);
            }
        } finally {
            in.close();
        }
        return sb.toString();
    }
}
