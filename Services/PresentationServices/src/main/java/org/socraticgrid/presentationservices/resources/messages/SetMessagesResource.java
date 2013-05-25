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

import org.socraticgrid.aggregator.DisplayDataAggregator;
import org.socraticgrid.aggregator.DisplayDataAggregatorPortType;
import org.socraticgrid.common.dda.MessageDataRequestType;
import org.socraticgrid.common.dda.SetMessageDataRequestType;
import org.socraticgrid.common.dda.SetMessageDataResponseType;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
//mport org.socraticgrid.ldapaccess.ContactDAO;
//import org.socraticgrid.ldapaccess.ContactDTO;
//import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.JSONHelper;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 *
 * @author Sushma
 */
public class SetMessagesResource extends BaseResource {

    public Form form;
    private String userId = "";
    private String token = "";
    private String patientId;
    private List messageIds = new ArrayList();
    /** action can be update, new, delete etc */
    private String action = "";
    private String subject;
    private List contactTo = new ArrayList();
    private List contactCC = new ArrayList();
    private List contactBCC = new ArrayList();
    private String body;
    private String document = "";
    /** Folders which represent flagged or not */
    private List labels = new ArrayList();

//    private List<String> locations = new ArrayList();
    private String location = "";

    /** Status which represents archived, unread etc */
    private String attachment = "";
    private List messageTypes = new ArrayList();


    //final Logger logger = Logger.getLogger(SetMessagesResource.class.getName());
    private static Log logger = LogFactory.getLog(SetMessagesResource.class);

    SetMessageDataResponseType output = new SetMessageDataResponseType();
    private String LABEL = "Label";
    private String ALERT = "Alert";
    private String EMAIL = "EMail";
    private String ARCHIVE = "Archive";
    private String DOCUMENT = "Document";

    /**
     * Creates a new SetMessagesResource object.
     *
     * @param  context
     * @param  request
     * @param  response
     */
    public SetMessagesResource(Context context, Request request, Response response) {
        super(context, request, response);
        init(context, request, response);
        setModifiable(true);
        Representation rep = request.getEntity();
        form = new Form(rep);
        setParameters(form);   //GET ALL INPUT PARAMS
    }
    
    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    /**
     * Handle PUT requests. replace or update resource
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource#allowPut()
     */
    @Override
    public boolean allowPut() {
        return false;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * Handle DELETE requests. remove/delete resource
     */
    @Override
    public void removeRepresentations() throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource #acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {

        if (!SessionUtilities.verifyToken(token)) {
            return;
        }
        String failures = validateFields();

        if (failures.length() > 1) {
            String errorMessage = "setMessages: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getMessagesFact");
            String ret = err.generateError();
            return;
        }

        //PROCESSING....
        output = this.setMessages(userId, messageIds, messageTypes, labels);

        if (!output.isSuccessStatus()) {
            generateError("SetMessages is not successful");
            return;
        }

        output.setSuccessStatus(true);
        output.setMessage("Set Messages successful");
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                MediaType.APPLICATION_JSON);
    }

//TMN
    /**
     *
     * @param messageId
     * @param userId
     * @param messageIdWithType
     * @param j
     * @param labels
     * @return MessageDataRequestType object containing all needed request params.
     */
    private MessageDataRequestType createMsgRequest( String messageId
                                                    ,String userId
                                                    ,Map messageIdWithType
                                                    ,int j
                                                    ,List labels)
    {
        MessageDataRequestType setMessageRequest = new MessageDataRequestType();

        if (!CommonUtil.strNullorEmpty(messageId)) {
            setMessageRequest.setMessageId(messageId.toString());
        }
        setMessageRequest.setUserId(userId);

        if (!CommonUtil.strNullorEmpty(patientId)) {
            setMessageRequest.setPatientId(patientId);
        }
        setMessageRequest.setAction(action);

        setMessageRequest.setLocation(this.location);
        
        if (!CommonUtil.mapNullorEmpty(messageIdWithType)) {
            setMessageRequest.setDataSource(messageIdWithType.get(messageId).toString());
        }
        else {
            setMessageRequest.setDataSource("Email");
        }

        setMessageRequest.setSubject(subject);
        setMessageRequest.setBody(body);

        if (!CommonUtil.listNullorEmpty(contactTo)) setMessageRequest.getContactTo().addAll(contactTo);
        if (!CommonUtil.listNullorEmpty(contactCC)) setMessageRequest.getContactCC().addAll(contactCC);
        if (!CommonUtil.listNullorEmpty(contactBCC)) setMessageRequest.getContactBCC().addAll(contactBCC);

        //
        if (labels != null && !labels.isEmpty()) {
            setMessageRequest.getLabels().addAll(labels);
        }

        System.out.println("===> createMsgRequest:  msgId="+ setMessageRequest.getMessageId());

        return setMessageRequest;
    }


    private void createSaveRequest(SetMessageDataRequestType setMessageRequest,
                                   String userId, List labels) {

        MessageDataRequestType aMsgReq = new MessageDataRequestType();
        aMsgReq.setUserId(userId);
        aMsgReq.setAction(action);
        aMsgReq.setLocation("Draft");
        aMsgReq.setDataSource("Email");
        aMsgReq.setSubject(subject);
        if (!CommonUtil.strNullorEmpty(body)) {
            aMsgReq.setBody(body);
        }
        else {
            aMsgReq.setBody(" ");
        }
        aMsgReq.getContactTo().addAll(contactTo);
        aMsgReq.getContactCC().addAll(contactCC);
        aMsgReq.getContactBCC().addAll(contactBCC);
        if (labels != null && !labels.isEmpty()) {
            aMsgReq.getLabels().addAll(labels);
        }
        setMessageRequest.getRequestMessage().add(aMsgReq);
    }

    private String validateFields() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("action", action);
        fields.put("userId", userId);
        if (!action.equals("Send")) {
            fields.put("messageIds", messageIds);
        }
        //fields.put("messageTypes", messageTypes);
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();
        return failures;
    }

    private void generateError(String errorMsg) {
        output.setSuccessStatus(false);
        output.setMessage(errorMsg);
        getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(), MediaType.APPLICATION_JSON);
        logger.error(errorMsg);
    }


    /**
     * ONLY call DDA.setMessage for
     * @param userId
     * @param messageIds
     * @param messageTypes
     * @param labels
     * @return
     */
    //Exception DisallowedSetAction = new Exception();
    private SetMessageDataResponseType setMessages( String userId
                                                   ,List<String> messageIds
                                                   ,List<String> messageTypes
                                                   ,List<String> labels) 
    {
        DisplayDataAggregator service = new org.socraticgrid.aggregator.DisplayDataAggregator();
        DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
        
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));

        SetMessageDataRequestType setMessageRequest = new SetMessageDataRequestType();
        SetMessageDataResponseType setMessageResponse = new SetMessageDataResponseType();

        try {
            int j = 0; //indicates how many msgs needs to be process.
            Map messageIdWithType = new HashMap();


            //------------------------------------------------
            // WHEN msg already exists
            //------------------------------------------------
            if (!CommonUtil.listNullorEmpty(messageIds)) {

                //-----------------------------------------------
                // On preexisting msgs, when coming from EMR
                // where (patientId <> null) AND (user logged in is a provider),
                // disallow the following SET actions:
                // Update, Read, Archive, Unarchive, Delete, Undelete, DeleteForever
                //
                //  !! This logic still not working correctly yet!!
                //-----------------------------------------------
//                ContactDAO contactDAO = LdapService.getContactDAO();
//                ContactDTO contact = null;
//                contact = contactDAO.findContact("uid=" + this.userId).get(0);

//                if (   !CommonUtil.strNullorEmpty(this.patientId)
//                    && !CommonUtil.strNullorEmpty(contact.getEmployeeNumber())
//                   )
//                {
//
//                    String errmsg = "Doing a Set action when logged in as userId="+ this.userId
//                            +" and message has context of patientId="+ this.patientId
//                            +" "+ this.action + " action disallowed on this request.  ";
//
//                    System.out.println(errmsg);
//
//                    setMessageResponse.setSuccessStatus(false);
//                    setMessageResponse.setMessage(errmsg);
//
//                    getResponse().setStatus(Status.SUCCESS_OK);
//                    getResponse().setEntity(JSONHelper.getResponse(setMessageResponse).toString(),
//                    MediaType.APPLICATION_JSON);
//
//                } else {

                    //-----------------------------------------------
                    // Create a Map of msgId:<Email|Alert|Document>
                    // so that DDA can correct redirect the msg to be processed later
                    //-----------------------------------------------
                    for (int i = 0; i < messageIds.size(); i++) {
                        messageIdWithType.put(messageIds.get(i), messageTypes.get(i));
                    }

                    //-----------------------------------------------
                    // CHANGE:
                    // Instead of sending separate DDA ws call for each messageId,
                    // prep a list and do one call.  Let DDA take care of iterating
                    // through the list as it makes its library processing call.
                    //-----------------------------------------------
                    for (String messageId : messageIds) {
                        MessageDataRequestType aMsgReq = new MessageDataRequestType();
                        aMsgReq = createMsgRequest(messageId, userId, messageIdWithType, j, labels);
                        ++j;
                        setMessageRequest.getRequestMessage().add(aMsgReq);
                    }
//                }
            }
            //------------------------------------------------
            // WHEN msg is new and user wants to do a SAVE
            //------------------------------------------------
            else if (action.equalsIgnoreCase("Save")) {
                createSaveRequest(setMessageRequest, userId, labels);
            }
            //------------------------------------------------
            // WHEN msg is new and user wants to do a SEND
            //------------------------------------------------
            else if (action.equalsIgnoreCase("Send")) {
                MessageDataRequestType aMsgReq = new MessageDataRequestType();
                aMsgReq = createMsgRequest(null, userId, null, 0, labels);
                setMessageRequest.getRequestMessage().add(aMsgReq);
            }
            
            //Send to DDA to process.
            setMessageResponse = port.setMessage(setMessageRequest);

        } catch (Exception e) {
            logger.error(e.getMessage());
            setMessageResponse.setSuccessStatus(false);
            setMessageResponse.setMessage(e.toString());

            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(setMessageResponse).toString(),
                    MediaType.APPLICATION_JSON);
            e.printStackTrace();
        }

        return setMessageResponse;
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param form
     */
    private void setParameters(Form form) {

        for (Parameter parameter : form) {
            System.out.print("===> SetMessageResource: parameter= " + parameter.getName());
            System.out.println("/" + parameter.getValue());
        }


        if (getRequestParameter(form, "body")) {
            body = form.getFirst("body").getValue();
        }
        if (getRequestParameter(form, "action")) {
            action = form.getFirst("action").getValue();
        }
        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }
        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("patientId").getValue();
        }
        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }
        if (getRequestParameter(form, "subject")) {
            subject = form.getFirst("subject").getValue();
        }
        if (getRequestParameter(form, "messageIds")) {         
            messageIds = SessionUtilities.getParameterValues(form, "messageIds");
        }

        if (getRequestParameter(form, "location")) {
            location = form.getFirst("location").getValue();
        }
//        if (getRequestParameter(form, "locations")) {
//            locations = SessionUtilities.getParameterValues(form, "locations");
//            logger.info("locations:" + locations);
//        }

        if (getRequestParameter(form, "labels")) {
            labels = SessionUtilities.getParameterValues(form, "labels");
        }
        if (getRequestParameter(form, "types")) {
            messageTypes = SessionUtilities.getParameterValues(form, "types");
        }
        if (getRequestParameter(form, "sendTo")) {
            contactTo = SessionUtilities.getParameterValues(form, "sendTo");
//            if (CommonUtil.listNullorEmpty(contactTo)) {
//                contactTo = null;
//            }
        }
        if (getRequestParameter(form, "CCTo")) {
            contactCC = SessionUtilities.getParameterValues(form, "CCTo");
////            if (CommonUtil.listNullorEmpty(contactCC)) {
////                contactCC = null;
////            }
        }
        if (getRequestParameter(form, "BCCTo")) {
            contactBCC = SessionUtilities.getParameterValues(form, "BCCTo");
//            if (CommonUtil.listNullorEmpty(contactBCC)) {
//                contactBCC = null;
//            }
        }
    }

    private boolean isvalidMessageType(String messageType) {
        return (messageType.equalsIgnoreCase(ALERT) || messageType.equalsIgnoreCase(EMAIL)
                || messageType.equalsIgnoreCase(ARCHIVE) || messageType.equalsIgnoreCase(DOCUMENT));
    }

}
