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

import org.socraticgrid.common.docmgr.GetNHINDocumentsRequestType;
import org.socraticgrid.common.docmgr.GetNHINDocumentsResponseType;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.resources.BaseResource;
import ihe.iti.xds_b._2007.DocumentManagerPortType;
import ihe.iti.xds_b._2007.DocumentManagerService;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeFactory;
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
public class GetDocumentsResource extends BaseResource {

    private String patientId;
    private String userId;
    private String action;
    private String token;
    private final static Logger logger =
            Logger.getLogger(GetDocumentsResource.class.getName());

    /** LDAP attribute for provider's user id. */
    public static final String LDAP_PROVIDER_ID_ATT = "employeeNumber";

    /** LDAP attribute for patient's user id. */
    public static final String LDAP_PATIENT_ID_ATT = "uid";


    public GetDocumentsResource(Context context, Request request, Response response) {
        super(context, request, response);

        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            logger.log(Level.INFO, "getDocuments query: " + query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
                return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }

        try {
            patientId = getParm(request, "patientId");
            userId = getParm(request, "userId");
            action = getParm(request, "action");
            token = getParm(request, "token");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

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

        Map<String, String> fields = new HashMap<String, String>();
        fields.put("userId", userId);
        ParameterValidator validator = new ParameterValidator(fields);
        String failures = validator.validateMissingOrEmpty();

        if (failures.length() > 1) {
            String errorMessage = "getDocuments: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getDocumentsFact");
            String ret = err.generateError();
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        //If patientId is not passed, then use userId as patient
        if ((patientId == null) || patientId.isEmpty()) {
            patientId = userId;
        }

        //Get further info from ldap
        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> userContacts = contactDAO.findContact(
            LDAP_PROVIDER_ID_ATT + "=" + userId);
        List<ContactDTO> ptContacts = contactDAO.findContact(
            LDAP_PATIENT_ID_ATT + "=" + patientId);
        ContactDTO userContact;
        ContactDTO ptContact;

        //If user not found, find as patient
        if (userContacts.isEmpty()) {
            userContacts = contactDAO.findContact(
                LDAP_PATIENT_ID_ATT + "=" + userId);
        }

        //If user still not found, return error
        if (userContacts.isEmpty()) {
            String errorMessage = "getDocuments: userId is not found in ldap";
            ErrorResponse err = new ErrorResponse(errorMessage, "getDocumentsFact");
            String ret = err.generateError();
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        //If patient not found, return error
        if (ptContacts.isEmpty()) {
            String errorMessage = "getDocuments: patientId is not found in ldap";
            ErrorResponse err = new ErrorResponse(errorMessage, "getDocumentsFact");
            String ret = err.generateError();
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        userContact = userContacts.get(0);
        ptContact = ptContacts.get(0);

        DocumentManagerService service = new ihe.iti.xds_b._2007.DocumentManagerService();
        DocumentManagerPortType port = service.getDocumentManagerPortSoap();
        ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("XDSManagerWeb"));

        GetNHINDocumentsResponseType response = new GetNHINDocumentsResponseType();
        GetNHINDocumentsRequestType request = new org.socraticgrid.common.docmgr.GetNHINDocumentsRequestType();
        request.setPatientId(patientId);
        request.setUserId(userId);
        request.setAction(action);
        try {
            //Patient dob is faked until we can get the real value
            request.setPatientDOB(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
        }
        catch (Exception e) {
            //ignore
        }
        request.setPatientFirstName(ptContact.getGivenName());
        request.setPatientMiddleName("");
        request.setPatientLastName(ptContact.getSurname());
        request.setUserFirstName(userContact.getGivenName());
        request.setUserMiddleName("");
        request.setUserLastName(userContact.getSurname());

        output = getDocuments(request, response, port);

        Representation representation =
                new StringRepresentation(output, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String getDocuments(GetNHINDocumentsRequestType request,
        GetNHINDocumentsResponseType response, DocumentManagerPortType port) {

        try {
            response = port.getNHINDocuments(request);
        } catch (Exception e) {
            logger.info("Exception in getDocuments()");
        }

        StringBuilder sb = new StringBuilder("{\n\"documentsFact\": {\n");
        sb.append("\"successStatus\": " + response.isSuccessStatus() + " ,\n");

        if (!response.isSuccessStatus()) {
            sb.append("\"statusMessage\": \"" + response.getStatusMessage() + "\",\n");
        }

        sb.append("\"processState\": \"" + response.getProcessState() + "\"\n");

        sb.append("}\n}");
        String s = sb.toString();
        return s;
    }

    private String getParm(Request request, String paramName) {
        return request.getResourceRef().getQueryAsForm().getFirstValue(paramName);
    }
}
