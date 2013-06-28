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

import org.socraticgrid.docmgr.repository.model.Document;
import org.socraticgrid.docmgr.repository.service.DocumentService;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.ldapaccess.ContactAcctDTO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 *
 * @author Sushma
 */
public class ValidateAccountResource extends BaseResource {

    private static final String nhin = PropertyHelper.getPropertyHelper().getProperty("SSOEndpoint");
    private String username;
    private String password;
    private String facility;
    private String CAC;
    final Logger logger = Logger.getLogger(ValidateAccountResource.class.getName());

    public ValidateAccountResource(Context context, Request request, Response response) {
        super(context, request, response);
        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            System.out.println("query: " + query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {
            this.username = request.getResourceRef().getQueryAsForm().getFirstValue("userName");
            this.password = request.getResourceRef().getQueryAsForm().getFirstValue("password");
            this.facility = request.getResourceRef().getQueryAsForm().getFirstValue("facility");
            this.CAC = request.getResourceRef().getQueryAsForm().getFirstValue("CAC");

        } catch (Exception e) {
            e.printStackTrace();
        }
        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        init(context, request, response);
    }

    @Override
    public final void init(Context context, Request request, Response response) {
        super.init(context, request, response);
        SessionUtilities.setCORSHeaders(this);
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String result = "";
        result = authenticate();
        Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
//        SessionUtilities.setCORSHeaders(this);
        return representation;
    }

    private String authenticate() {
        String retVal = "{\"validateAccountFact\": {\"statusMessage\" : \"Invalid User or System down\" , \"successStatus\" : false }}";
        List<ContactDTO> contacts = LdapService.getContactDAO().findContact("cn,="+username);
        if (contacts == null || contacts.isEmpty()){
            //invalid
            return retVal;
        }
        
        List<ContactAcctDTO> accts = LdapService.getContactDAO().findContactAcct(username, "Mail.Account");
        if (accts == null || accts.isEmpty()){
            //invalid
            return retVal;
        }
        
        ContactAcctDTO acct = accts.get(0);
        
        if (!password.equals(acct.getClearPassword())){
            //invalid
            return retVal;
        }
        
        ContactDTO contact = contacts.get(0);
        
        String uid = contact.getUid();
        String providerId = contact.getEmployeeNumber();
        String securityToken = UUID.randomUUID().toString();
        String role = null;
        
        if (contact.getEmployeeNumber() == null){
            //It is a Patient
            role = "patient";
        }else{
            role = "provider";
        }

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("{\"validateAccountFact\" : {\"statusMessage\" : \"valid\", \"successStatus\" : true");
        
        //token
        responseBuilder.append(",\"token\":\"");
        responseBuilder.append(securityToken);
        responseBuilder.append("\"");
        
        //role
        responseBuilder.append(",\"role\":\"");
        responseBuilder.append(role);
        responseBuilder.append("\"");
        
        //userId
        responseBuilder.append(",\"userId\":\"");
        responseBuilder.append(uid);
        responseBuilder.append("\"");

        //providerId
        if (providerId != null){
            responseBuilder.append(",\"providerId\":\"");
            responseBuilder.append(providerId);
            responseBuilder.append("\"");
        }
        
        responseBuilder.append("}}");

        SessionUtilities.storeTokenData(username, providerId, uid, securityToken);

        //NEED TO DBG .... REMOVE FOR NOW
        //removeDocs();

        return responseBuilder.toString();
    }
    
    private String authenticateDEPRECATED() {
        String retVal = "{\"validateAccountFact\": {\"statusMessage\" : \"Invalid User or System down\" , \"successStatus\" : false }}";

        String errorMessage = "";
        try {

            //PREP RESTLET CLIENT call to SSO to get token per username/pwd.
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(1000);
            String openSsoUrl = nhin + "/opensso/identity/authenticate?username="
                    + username + "&password=" + password;
            Request request = new Request(Method.PUT, new Reference(openSsoUrl));
            logger.info("Making FIRST SSO request to URL: " + openSsoUrl);
            Response response = null;
            try {
                response = client.handle(request);
            } catch (Throwable e) {
                e.printStackTrace();
            }

            logger.log(Level.INFO, request.getAttributes().toString());

            if (response.getStatus().isSuccess())
            {
                String ret = response.getEntity().getText();
                ret = ret.substring(0, ret.length() - 1);
                logger.log(Level.INFO, ret);
            
                //BEGIN READYING json response for GUI.
                retVal = "{\"validateAccountFact\": {\"status\" : \"" + ret + "\", \"successStatus\" : true }}";

                //CHECK if SSO authentication req returned a "token.id=xxx".
                if (ret.indexOf("token.id=") > -1)
                {
                    String[] ay = ret.split("token.id=");
                    System.out.println("token: " + ay[1]);

                    //CLEANUP blanks and special chars.
                    String enc = URLEncoder.encode(ay[1], "ISO-8859-1");
                    System.out.println("enc: " + enc);

                    //CREATE another client req call to SSO.
                    Client client1 = new Client(Protocol.HTTP);
                    client1.setConnectTimeout(1000);

                    String ssoUrl2 = nhin + "/opensso/identity/attributes?subjectid=" + enc;
                    logger.info("Making SECOND SSO request to URL: " + ssoUrl2);
                    Request request1 = new Request(Method.POST, new Reference(ssoUrl2));

                    Response response1 = client1.handle(request1);

                    if (response1.getStatus().isSuccess()) {
                        //Map map = new HashMap();
                        String key = "";
                        String value = "";
                        
                        String[] attribs = response1.getEntity().getText().split("\n");
                        String jsonHeader = "{\"validateAccountFact\" : {\"statusMessage\" : \"valid\", \"successStatus\" : true, ";
                        String jsonFooter = "}}";
                        String jsonBody = "";
                        String encryptedToken = "";
                        String patientId = "";
                        String providerId = "";
                        String role = "";
                        Map<String, String> attrMap = new HashMap<String, String>();
                        for (int i = 0; i < attribs.length; ++i) {
                            String[] item = attribs[i].split("=");
                            attrMap.put(item[0] + i, item[1]);
                        }
                        for (int i = 0; i < attribs.length; i++) {
                            String string = attribs[i];
                            System.out.println(string);
                            if (string.startsWith("userdetails.token.id")) {
                                encryptedToken = SessionUtilities.encryptToken(string.substring(21));
                                jsonBody = "\"token\":\"" + encryptedToken + "\",";
                            }


                            if (string.startsWith("userdetails.attribute.name=")) {
                                key = string.replaceFirst("userdetails.attribute.name=", "");
                                if (key.equalsIgnoreCase("uid") || key.equalsIgnoreCase("employeenumber")) {
                                    if (key.equalsIgnoreCase("uid")) {
                                        key = "userId";
                                    }
                                    if (key.equalsIgnoreCase("employeenumber")) {
                                        key = "providerId";
                                    }
                                }
                            } else if (string.startsWith("userdetails.attribute.value=")
                                    && ((key.equalsIgnoreCase("userId") || (key.equalsIgnoreCase("providerId"))))) {
                                value = string.replaceFirst("userdetails.attribute.value=", "");

                            }

                            if (!key.equals("") && !value.equals("")
                                    && ((key.equalsIgnoreCase("userId") || (key.equalsIgnoreCase("providerId"))))) {

                                if (key.equalsIgnoreCase("providerId")) {
                                    role = "provider";
                                    jsonBody = jsonBody + "\"role\":" + "\"" + role + "\",";
                                    jsonBody = jsonBody + "\"" + key + "\":" + "\"" + value + "\",";
                                    providerId = value;
                                }
                                if (key.equalsIgnoreCase("userId")) {
                                    if (!attrMap.containsValue("employeenumber")) {
                                        role = "patient";
                                        jsonBody = jsonBody + "\"role\":" + "\"" + role + "\",";
                                     }
                                        jsonBody = jsonBody + "\"" + key + "\":" + "\"" + value + "\",";
                                        patientId = value;
                                }

                                key = "";
                                value = "";
                            }
                        }

                        retVal = jsonHeader + jsonBody.substring(0, jsonBody.length() - 1) + jsonFooter;
                        SessionUtilities.storeTokenData(username, providerId, patientId, encryptedToken);
                    }
                } else {
                    response.setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
                    errorMessage = response.getStatus().toString();
                    logger.log(Level.SEVERE, errorMessage);
                    return retVal = "{\"validateAccountFact\": {\"statusMessage\" : \" " + errorMessage + "\" , \"successStatus\" : false }}";
                }
            }
        } catch (Exception e) {
            errorMessage = e.toString();
            e.printStackTrace();
            logger.log(Level.SEVERE, errorMessage);
            retVal = "{\"validateAccountFact\": {\"statusMessage\" : \" " + errorMessage + "\" , \"successStatus\" : false }}";
        } finally {
            removeDocs();
        }
        
        return retVal;
    }

    /**
     * Removes existing documents... for demo purposes.
     */
    private void removeDocs() {
        try {
            //Clear NHIN documents for demos
            boolean demo = PropertyHelper.getPropertyHelper().getPropertyAsBoolean("demo");
            if (demo) {
                logger.log(Level.WARNING, "Demo mode: removing existing documents.");
                DocumentService docService = new DocumentService();
                for (Document doc : docService.getAllDocuments()) {
                    docService.deleteDocument(doc);
                }
            }
        } catch (Exception e) {
            //ignore
        }
    }

    class JsonResponse {

        private String statusMessage;
        private String token;
        private boolean successStatus;
        private String userId;

        public String getProviderId() {
            return providerId;
        }

        public void setProviderId(String providerId) {
            this.providerId = providerId;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }

        public boolean isSuccessStatus() {
            return successStatus;
        }

        public void setSuccessStatus(boolean successStatus) {
            this.successStatus = successStatus;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
        private String providerId;
    }
}
