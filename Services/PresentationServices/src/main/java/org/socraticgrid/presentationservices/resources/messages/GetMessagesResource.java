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
package org.socraticgrid.presentationservices.resources.messages;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.aggregator.DisplayDataAggregator;
import org.socraticgrid.aggregator.DisplayDataAggregatorPortType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.GetMessagesResponseType.GetMessageResponse;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.MessageResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.BindingProvider;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * SAMPLE CALLS:
 *
 * GETTING Alerts for a user (via Desktop):
 * GET /PresentationServices/getMessages?type=Alert&userId=1&token=
 * GET /PresentationServices/getMessageDetail?messageId=39&type=Alert&location=INBOX&userId=1&token=
 * GET /PresentationServices/getSurvey?patientId=99990070&surveyId=86a5f20c-de18-4507-a023-2dbf4d9ef2ff&userId=1&token=
 *
 * GETTING Alerts for a patient (via EMR):
 * GET /PresentationServices/getMessages?type=Alert&patientId=99990070&userId=1&token=
 *
 * GETTING Archived Msgs for a user (via Desktop, cause EMR should not allow getting Archived msgs):
 * GET /PresentationServices/getMessages?location=Archived&userId=1&token=
 * GET /PresentationServices/getMessageDetail?messageId=2&type=Email&location=Archive&userId=1&token=
 *
 *
 *
 * @author jharby
 */
public class GetMessagesResource extends BaseResource {

    private String patientId;
    private String userId;
    private String type;
    private String token;
    private String location;
    private final static Logger logger =
            Logger.getLogger(GetMessagesResource.class.getName());

    public GetMessagesResource(Context context, Request request, Response response) {
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

            //DBG
            for (Parameter parameter : request.getResourceRef().getQueryAsForm()) {
                System.out.print("===> GetMessagesResource: parameter= " + parameter.getName());
                System.out.println("/" + parameter.getValue());
            }
            
            patientId = getParm(request, "patientId");
            userId = getParm(request, "userId");
            type = getParm(request, "type");
            location = getParm(request, "location");
            token = getParm(request, "token");

        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
//TMN??? WHY IS THIS COMMENTED OUT?
//        if (token == null || !SessionUtilities.verifyToken(token)) {
//            String errorMsg = "The token was not found, the session may have timed out. Token is " + token;
//            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
//        }

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
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String output = "";
        if (type == null || type.equals("")) {
            type = "all";
        }

        if (location == null || location.isEmpty() || location.equalsIgnoreCase("INBOX")) {
            location = "INBOX";
        }

        if (location.equalsIgnoreCase("Sent")) {
            location = "Sent";
        }

        if (location.contains("Archive")) {
            location = "Archive";
        }


        GetMessagesResponseType responses = new GetMessagesResponseType();

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("userId", userId);
        // fields.put("location", location);
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();

        if (failures.length() > 1) {
            String errorMessage = "getMessages: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getMessagesFact");
            String ret = err.generateError();
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }


        DisplayDataAggregator service = new org.socraticgrid.aggregator.DisplayDataAggregator();
        DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));
        GetMessagesRequestType request = new GetMessagesRequestType();

        request.setPatientId(patientId);
        request.setUserId(userId);
        request.setMessageType(type);
        request.setLocation(location);
        output = getMessages(request, responses, port);
        Representation representation =
                new StringRepresentation(output, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String getMessages(GetMessagesRequestType request,
            GetMessagesResponseType response, DisplayDataAggregatorPortType port) {

        try {
            response = port.getMessages(request);
        } catch (Exception e) {
            logger.info("Exception in getMessages():"+ e.getStackTrace());
            
        }
//        Configuration configuration = new Configuration();
//        CacheManager cacheManager = new CacheManager(configuration);

        StringBuilder jsonResponse = new StringBuilder();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        StringBuilder sb = new StringBuilder("{\n\"messagesFact\": {\n");
        int i = 1;
        List<MessageResponse> msgRespList = new ArrayList<MessageResponse>();
        for (GetMessagesResponseType.GetMessageResponse msgResp : response.getGetMessageResponse()) {

            if (i == 1) {
                sb.append("\"successStatus\": " + msgResp.isSuccessStatus() + " ,\n");
                if (!msgResp.isSuccessStatus()) {
                    sb.append("\"statusMessage\": " + msgResp.getStatusMessage() + " ,\n");
                }
                sb.append("\"messageObjects\": [\n");
                if (msgResp.isSuccessStatus() == false) {
                    break;
                }
            }

            if (msgResp.getMessageType() == null || msgResp.getMessageType().equals("")) {
                continue;
            }

            MessageResponse msg = new MessageResponse();
            if (!CommonUtil.strNullorEmpty(msgResp.getMessageId())) {
                msg.setMessageId(msgResp.getMessageId());
            }
            if (CommonUtil.strNullorEmpty(location)) {
                msg.setLocation("INBOX");
            } else {
                msg.setLocation(location);
            }

            if (msgResp.getLabels().contains("Starred")
                    || msgResp.getLabels().contains("starred")) {
                msg.getLabels().add("Starred");
            }

            if (msgResp.getMessageDate() != null) {
                List<String> dateTime =
                        CommonUtil.getDateTime(msgResp.getMessageDate());
                msg.setMessageDate(dateTime.get(0));
                msg.setMessageTime(dateTime.get(1));
            }

            msg.setMessageType(msgResp.getMessageType());
            msg.setDescription(msgResp.getDescription());
            String mailFrom = msgResp.getFrom();
            if (mailFrom != null) {
                msg.setFrom(mailFrom.replaceAll("\"", ""));
            }
            msg.setTitle(msgResp.getTitle());
            msg.setStatus(msgResp.getMessageStatus());
            msg.setPriority(msgResp.getPriority());
            msg.setTasksCount(msgResp.getTasksCount());
            msg.setTasksComplete(msgResp.getTasksComplete());
            msgRespList.add(msg);
            ++i;
        }

        Collections.sort(msgRespList);
        Collections.reverse(msgRespList);

        int j = 1;
        for (MessageResponse msgResp : msgRespList) {
//            if (i < msgRespList.size()) {
//                sb.append(gson.toJson(msgResp) + ",");
//            } else {
//                sb.append(gson.toJson(msgResp));
//            }
            if (j < msgRespList.size()) {
                sb.append(gson.toJson(msgResp) + ",");
            } else {
                sb.append(gson.toJson(msgResp));
            }
            ++j;
        }
        sb.append("\n]\n}\n}");
        String s = sb.toString();
        return s;
    }

    private String getParm(Request request, String paramName) {
        return request.getResourceRef().getQueryAsForm().getFirstValue(paramName);
    }
}
