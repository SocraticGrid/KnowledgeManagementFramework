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
package org.socraticgrid.presentationservices.resources.simulator;

import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import java.util.HashMap;
import java.util.Map;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import org.socraticgrid.presentationservices.resources.simulator.model.Configuration;
import java.util.LinkedHashMap;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.mas.helpers.SyncDialogueHelper;

/**
 *
 * @author nhin
 */
public class RequestResultOperationResource extends BaseResource {

    public Form form;
    private String resultId;
    private String userId;
    private String operationType;
    private String token;

    public RequestResultOperationResource(Context context, Request request, Response response) {
        super(context, request, response);
        init(context, request, response);
        setModifiable(true);
        setAvailable(true);
        Representation rep = request.getEntity();

    }

    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    @Override
    public boolean allowPut() {
        return true;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * PUT handler
     * 
     * @param entity
     * @throws ResourceException 
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        Form f = new Form(entity);
        setParameters(f);
        
        if (!SessionUtilities.verifyToken(token)) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, "Session token validation failed.");
            return;
        }

        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "requestResultOperation: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "requestResultOperationFact");
            String ret = err.generateError();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, ret);
            return;
        }

        String callResponse = requestResultOperation();

        System.out.println("COMMAND CONFIGURATION RETURNING: " + callResponse);

        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(callResponse, MediaType.APPLICATION_JSON);
        // TODO handle error case returning json for successStatus
    }

    private void setParameters(Form form) {

        if (getRequestParameter(form, "resultId")) {
            resultId = form.getFirst("resultId").getValue();
        }

        if (getRequestParameter(form, "operationType")) {
            operationType = form.getFirstValue("operationType", operationType);
        }
        
        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("userId", userId);
        fields.put("resultId", resultId);
        fields.put("operationType", operationType);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }


    private String requestResultOperation() {
        //throw new UnsupportedOperationException("Not yet implemented");

        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SyncDialogueHelper helper =
                SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();

        args.put("userId", this.userId);
        args.put("resultId", this.resultId);
        args.put("operationType", this.operationType);

        String root = "{\n\"simulationProcess\": {";
        String jsonRoot = null;
        String jsonResp = null;
        try {
            helper.invokeRequest("requestResultOperation", args);
            String xml = (String) helper.getReturn(false);

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
}
