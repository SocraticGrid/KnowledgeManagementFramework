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

package org.socraticgrid.presentationservices.resources.dsa;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.resources.facts.GetPatientDataResource;
import org.socraticgrid.taps.dsanotificationlib.DSANotification;

import org.socraticgrid.util.SessionUtilities;

        
/**
 *
 * @author tnguyen
 */
public class NotifyDSA extends BaseResource {

    static final Logger logger = Logger.getLogger(GetPatientDataResource.class.getName());
    
    private Map fieldMap = new HashMap();

    public NotifyDSA(Context context, Request request, Response response) {
        
        super(context, request, response);

        String token = null;
        
        setModifiable(true);
        
        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            NotifyDSA.logger.log(Level.INFO, "query: {0}", query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }
        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {

            System.out.println("\n===>NotifyDSA REQ: " + request.getResourceRef().getQueryAsForm().toString());

            //creating a Map of all params.
            fieldMap.putAll(request.getResourceRef().getQueryAsForm().getValuesMap());
            
            token = (String) fieldMap.get("token");

        } catch (Exception e) {
            NotifyDSA.logger.log(Level.SEVERE, e.getMessage());
        }

        //VERIFY THAT TOKEN BEING USED IS STILL VALID.
        if ((token == null) || (!SessionUtilities.verifyToken(token))) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

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
    public Representation represent(Variant variant) throws ResourceException {

        //---------------------------------
        // VALIDATING INPUT PARAMS
        //---------------------------------
        String ret = handleParamValidation();
        
        if (!ret.equals("")) {
            System.out.println("\n==>ERROR: "+ret);
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        //---------------------------------
        // PROCESSING
        //---------------------------------
        String resp = "";
        try {
            String patientId = (String) fieldMap.get("patientId");
            String userId    = (String) fieldMap.get("userId");
            
            System.out.println("===> LOAD PATIENT ID " + patientId + "):" + resp);

            DSANotification dsa = new DSANotification(getProperty("ActionAgentService"));
            dsa.sendPatientIdToDSA(patientId, userId);
            
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
        
        
        Representation representation = new StringRepresentation(resp, MediaType.APPLICATION_JSON);

        return representation;
    }

    private String handleParamValidation() {
        String ret = null;
        
        return ret;
    }
}
