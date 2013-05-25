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

package org.socraticgrid.util;

import org.socraticgrid.account.model.UserSession;
import org.socraticgrid.account.service.AccountService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;

/**
 * This is a class that contains common refactored utility methods for session
 * oriented requirements.
 *
 * @author jharby
 */
public class SessionUtilities {

    public static final Logger logger = Logger.getLogger(SessionUtilities.class.getName());

    /**
     * This method sets the response headers for Cross-Origin Resource Sharing in the
     * resource response headers.
     * 
     * @param resource
     */
    public static void setCORSHeaders(Resource resource) {
        logger.log(Level.INFO, "Setting CORS headers for " + resource.getClass().getName());
        Form responseHeaders = (Form) resource.getResponse().getAttributes().get("org.restlet.http.headers");
        if (responseHeaders == null) {
            responseHeaders = new Form();
            resource.getResponse().getAttributes().put("org.restlet.http.headers", responseHeaders);
        }
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Allow-Control-Allow-Methods", "OPTIONS, GET, POST, PUT, DELETE");
        responseHeaders.add("Access-Control-Allow-Headers", "Content-Type, X-Requested-With");
    }

    public static boolean verifyToken(String token) {
        AccountService service = new AccountService();
        UserSession session = service.getUserSession(token);

        if (session == null) {
            return false;
        } else {
            session.setLastUpdateTime(new Date());
            service.saveUserSession(session);
            return true;
        }

    }

    public static void storeTokenData(String username, String providerId, String patientId, String tokenValue) {
        AccountService accountService = new AccountService();
        UserSession session = new UserSession();
        session.setToken(tokenValue);
        session.setProviderId(providerId);
        session.setUserId(username);
        accountService.saveUserSession(session);
    }

    public static String encryptToken(String tokenValue) throws IOException, GeneralSecurityException {
        String encryptedToken;
        AESEncryption aesEncryption = new AESEncryption();
        encryptedToken = AESEncryption.toHexString(aesEncryption.aesEncrypt(tokenValue));
        return encryptedToken;
    }

    public static void generateErrorRepresentation(String errorMessage,
            String errorCode, Response response) {
        // This is an error
        response.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        StringRepresentation representation =
                new StringRepresentation(errorMessage, MediaType.APPLICATION_JSON);
        response.setEntity(representation);

    }

    /*
     * This Method pulls the ArrayList from the request Parameters
     * where parameter is the name of the request parameter (for POST)
     *
     */
    public static List getParameterValues(Form inForm, String parameter) {
        Set<String> names = inForm.getNames();
        boolean foundParameter = false;
        List params = null;
        for (String name : names) {
            if (name.equalsIgnoreCase(parameter)) {
                foundParameter = true;
            }
        }

        if (foundParameter) {
System.out.println("===> ARRAYparam: parameter="+ parameter);
            if (null != inForm.getFirstValue(parameter))
            {
                params = new ArrayList();
                String formValues = inForm.getFirstValue(parameter);
                Collections.addAll(params, formValues.split("\\,"));
System.out.println("===> ARRAYparam: formValues="+ formValues);
            }
            else if (null == inForm.getFirstValue(parameter)) {
System.out.println("===> ARRAYparam: formValues=NONE FOUND");
                params = new ArrayList();
                params.add("");
            }
        }

        return params;
    }
}
