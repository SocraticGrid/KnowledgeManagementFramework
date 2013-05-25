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

import org.socraticgrid.displaycalendarlib.SetCalendarRequest;
import org.socraticgrid.displaycalendarlib.SogoCalendar;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;

/**
 *
 * @author nhin
 */
public class SetCalendarResource extends BaseResource {

    private String token;
    private String userId;
    private String patientId;
    private String providerId;
    private String calendarId;
    private String eventId;
    private String title;
    private String startDate;
    private String endDate;
    private String action;
    private Form form;

    public SetCalendarResource(Context context, Request request, Response response) {
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
        try {
            if (SessionUtilities.verifyToken(token)) {
                return;
            }

            String failures = validateFields();

            if (failures.length() > 1) {
                String errorMessage = "setCalendar: "
                        + failures + "is a missing required field";
                ErrorResponse err = new ErrorResponse(errorMessage, "setCalendarFact");
                String ret = err.generateError();
                return;
            }

            SogoCalendar sogo = new SogoCalendar();
            SetCalendarRequest request = new SetCalendarRequest();
            String response = sogo.setCalendarEvent(request);
            StringBuilder sb = new StringBuilder("{ \"calendarsFact\": {");
            sb.append("\"response\"");
            sb.append(response);
            sb.append(" } }");
        } catch (Exception ex) {
            Logger.getLogger(SetCalendarResource.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    private void setParameters(Form form) {

        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }

        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("modelId").getValue();
        }

        if (getRequestParameter(form, "providerId")) {
            providerId = form.getFirst("action").getValue();
        }

        if (getRequestParameter(form, "calendarId")) {
            calendarId = form.getFirst("calendarId").getValue();
        }

        if (getRequestParameter(form, "eventId")) {
            eventId = form.getFirst("eventId").getValue();
        }

        if (getRequestParameter(form, "title")) {
            title = form.getFirst("title").getValue();
        }

        if (getRequestParameter(form, "start")) {
            startDate = form.getFirst("start").getValue();
        }

        if (getRequestParameter(form, "end")) {
            endDate = form.getFirst("end").getValue();
        }

        if (getRequestParameter(form, "action")) {
            action = form.getFirst("action").getValue();
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
        fields.put("calendarId", calendarId);
        fields.put("eventId", eventId);
        fields.put("title", title);
        fields.put("start", startDate);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }
}
