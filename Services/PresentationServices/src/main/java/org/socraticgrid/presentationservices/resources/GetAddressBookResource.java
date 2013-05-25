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

import org.socraticgrid.presentationservices.resources.messages.GetMessageDetailResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.aggregator.DisplayDataAggregator;
import org.socraticgrid.aggregator.DisplayDataAggregatorPortType;
import org.socraticgrid.common.dda.GetAddressBookRequestType;
import org.socraticgrid.common.dda.GetAddressBookResponseType;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.BindingProvider;
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
 * @author jharby
 */
public class GetAddressBookResource extends BaseResource {

    String userId;
    String token;
    final Logger logger = Logger.getLogger(GetMessageDetailResource.class.getName());

    public GetAddressBookResource(Context context, Request request, Response response) {
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
            this.token =
                    request.getResourceRef().getQueryAsForm().getFirstValue("token");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }


        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        SessionUtilities.setCORSHeaders(this);
        context.getClientDispatcher();
        init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String result = getAddressBook();
        Representation representation =
                new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String getAddressBook() {
        DisplayDataAggregator service = new org.socraticgrid.aggregator.DisplayDataAggregator();
        DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));
        GetAddressBookRequestType request = new GetAddressBookRequestType();
        GetAddressBookResponseType response = new GetAddressBookResponseType();

        try {
            request.setUserId(userId);
            response = port.getAddressBook(request);
        } catch (Exception e) {
            logger.info("Exception in getMessageDetail()");
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        StringBuilder sb = new StringBuilder("{\n\"addressBookFact\": {\n");
        AddressFact fact = new AddressFact();
        boolean first = true;
        int i = 1;
        
        // Crate empty
        if (CommonUtil.listNullorEmpty(response.getGetAddressResponse())) {
            System.out.println("GETADDRESSBOOK: No contacts found for userId="+ request.getUserId());
            
            sb.append("\"successStatus\": true \n");
            sb.append("\n}\n}");
            //return sb.toString();
            
        } else {
            for (GetAddressBookResponseType.GetAddressResponse gResponse : response.getGetAddressResponse()) {
                
                //PREP an failed Response msg and pass back to user.
                if (!gResponse.isSuccessStatus()) {
                    
                    sb.append("\"successStatus\": " + gResponse.isSuccessStatus() + " ,\n");
                    sb.append("\"statusMessage\": " + gResponse.getStatusMessage() + " ,\n");
                    //sb.append("\"contacts\": [\n");
                    sb.append("\n}\n}");
                    //return sb.toString();
                }

                //PREP the
                if (first) {
                    sb.append("\"successStatus\": " + gResponse.isSuccessStatus() + " ,\n");
                    sb.append("\"statusMessage\": " + gResponse.getStatusMessage() + " ,\n");
                    sb.append("\"contacts\": [\n");
                    first = false;
                }
                fact.setContactId(gResponse.getContactId());
                fact.setName(gResponse.getName());
                fact.setAddress1(gResponse.getAddress1());
                fact.setAddress2(gResponse.getAddress2());
                fact.setCity(gResponse.getCity());
                fact.setState(gResponse.getState());
                fact.setZipCode(gResponse.getZipCode());
                String[] phones = new String[gResponse.getPhones().size()];
                phones = gResponse.getPhones().toArray(phones);
                fact.setPhones(phones);
                fact.setEmail(gResponse.getEmail());
                if (i < response.getGetAddressResponse().size()) {
                    sb.append(gson.toJson(fact) + ",");
                } else {
                    sb.append(gson.toJson(fact));
                }
                ++i;
            }
            sb.append("\n]\n}\n}");
        }
        
        return sb.toString();
    }

    class AddressFact {

        String contactId;
        String name;
        String address1;
        String address2;
        String city;
        String state;
        String zipCode;
        String[] phones;
        String email;

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getContactId() {
            return contactId;
        }

        public void setContactId(String contactId) {
            this.contactId = contactId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getPhones() {
            return phones;
        }

        public void setPhones(String[] phones) {
            this.phones = phones;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }
    }
}
