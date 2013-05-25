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
package org.socraticgrid.presentationservices.resources.diagnostics;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import org.socraticgrid.presentationservices.resources.BaseResource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.mas.helpers.SyncDialogueHelper;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.Variant;

/**
 *
 * @author nhin
 */
public class SetDiagnosticActionStatusResource extends BaseResource {

    private String dxProcessId;
    private String actionId;
    private String status;
    private String token;
    private String patientId;
    private String userId;
    final Logger logger = Logger.getLogger(GetDGProcessStatusResource.class.getName());
    private static final String[] statusValArr =
            new String[]{"Not Started", "Started", "Commited", "Completed"};
    private static final List<String> statusValues = Arrays.asList(statusValArr);
    private Form form;

    public SetDiagnosticActionStatusResource(Context context, Request request, Response response) {
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

    /**
     * @see  org.restlet.resource.Resource #acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override
    public void acceptRepresentation(Representation entity) {

        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "setDiagnosticsActionStatus: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "setDiagnosticActionStatusFact");
            String ret = err.generateError();
            return;
        }

        if (!SessionUtilities.verifyToken(token)) {
            ErrorResponse err =
                    new ErrorResponse("Token validation failed", "setDiagnosticActionStatusFact");
            String ret = err.generateError();
            return;
        }

        String jsonReturn = setDiagActionStatus(userId, dxProcessId, actionId, status, patientId);
//        jsonReturn = jsonReturn.replaceAll("\"true\"", "true");
//        jsonReturn = jsonReturn.replaceAll("\"false\"", "false");
        System.out.println("SET DG ACTION STATUS RETURNING: " + jsonReturn);
        getResponse().setEntity(jsonReturn, MediaType.APPLICATION_JSON);
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("dxProcessId", dxProcessId);
        fields.put("patientId", patientId);
        fields.put("actionId", actionId);
        fields.put("status", status);
        fields.put("userId", userId);
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();
        return failures;
    }

    private String setDiagActionStatus(String userId, String dxProcessId, String actionId,
            String status, String patientId) {

        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SyncDialogueHelper helper = SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("userId", userId);
        args.put("patientId", patientId);
        args.put("dxProcessId", dxProcessId);
        args.put("actionId", actionId);
        args.put("status", status);

        String root = "{\n\"diagnosticGuideProcess\": {";
        String jsonRoot = null;
        String jsonResp = null;
        try {
            helper.invokeRequest("setDiagnosticActionStatus", args);
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

    private void setParameters(Form form) {

        if (getRequestParameter(form, "dxProcessId")) {
            dxProcessId = form.getFirst("dxProcessId").getValue();
            logger.log(Level.INFO, "dxProcessId: " + dxProcessId);
        }
        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("patientId").getValue();
            logger.log(Level.INFO, "patientId:", patientId);
        }
        if (getRequestParameter(form, "actionId")) {
            actionId = form.getFirst("actionId").getValue();
            logger.log(Level.INFO, "actionId: " + actionId);
        }
        if (getRequestParameter(form, "status")) {
            status = form.getFirst("status").getValue();
            logger.log(Level.INFO, "status:", status);
        }
        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
            logger.log(Level.INFO, "userId:", userId);
        }
        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
            logger.log(Level.INFO, "token:" + token);
        }
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }
}

class GuideForm {

    private String nodeId;
    private Boolean chosen;
    private List<String> formFields;
}

class Action {

    private String nodeId;
    private String type;
    private String descr;
    private String utility;
    private String status;
    private String statusUpdate;
    private GuideForm guideForm;
}

class Decision {
    // Decision

    private String nodeId;
    private String stage;
    private String current;
    private String descr;
    private String diseaseProbability;
    private String response;
    private List<Action> children;
}
