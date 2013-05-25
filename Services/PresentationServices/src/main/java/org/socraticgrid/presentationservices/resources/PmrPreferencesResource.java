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

package org.socraticgrid.presentationservices.resources;

import org.socraticgrid.presentationservices.helpers.PreferencesHelper;

import java.io.IOException;

import java.util.logging.Level;
// add this import if you need soapaction
//import javax.xml.soap.MimeHeaders;

import org.restlet.Context;

import org.restlet.data.Form;
import org.restlet.data.MediaType;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;


/**
 *
 * @author markpitman
 */
public class PmrPreferencesResource extends BaseResource
{

    private String patientId = "";
    private String username = "";
    private String password = "";

    public PmrPreferencesResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        if (getResponse().getStatus() == Status.CLIENT_ERROR_UNAUTHORIZED)
        {
            return;
        }

        getLogger().log(Level.INFO,
            "in getPrefs, method is: " + request.getMethod());

        //password = getParameter(request, "password", "turnkey");
        patientId = getParameter(request, "patientId", null);
        username = getParameter(request, "username", null);
        password = getParameter(request, "password", null);


        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }


    //allow post - otherwise a http error code 405 will happen
    @Override public boolean allowPost()
    {
        return true;
    }
//    @Override
//    public boolean allowPut() {
//        return true;
//    }

    /**
     * GET
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        if (!variant.getMediaType().isCompatible(MediaType.APPLICATION_JSON))
        {
            getLogger().info("Preferences Media type not JSON compatible, " +
                variant.getMediaType().toString());
        }

        getLogger().log(Level.INFO,
            "getting preferences for patient " + patientId);

        String out = PreferencesHelper.getPreferencesHelper()
            .getPreferencesAsJSON(patientId);
        Representation representation = new StringRepresentation(out,
                MediaType.APPLICATION_JSON);

        getLogger().log(Level.INFO, "returning: " + out);

        return representation;
    }

    //POST
    /*
     * NOTE - flex can't do a straight PUT via HTTP (flex shortcoming) - so it does a POST with a method=PUT parameter
     *        not that that matters much here - we are getting a string with preferences - just write file
     */
    @Override public void acceptRepresentation(Representation entity)
        throws ResourceException
    {


        if (entity.getMediaType().isCompatible(MediaType.APPLICATION_JSON))
        {

            getLogger().log(Level.INFO, "in update preferences - JSON");

            try
            {

                if (PreferencesHelper.getPreferencesHelper()
                        .savePreferencesAsJSON(patientId, entity.getText()))
                {
                    getResponse().setEntity("{\"Status\":\"OK\"}",
                        MediaType.APPLICATION_JSON);
                    getResponse().setStatus(Status.SUCCESS_OK);
                }
                else
                {
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

                }
            }
            catch (IOException e)
            {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            }
        }
        else if (entity.getMediaType().isCompatible(
                    MediaType.APPLICATION_WWW_FORM))
        {
            getLogger().log(Level.INFO, "in update preferences - FORM");

            Form form = new Form(entity);
            String u = form.getFirstValue("username");
            String p = form.getFirstValue("password");

            //String m = form.getFirstValue("method");
            String v = form.getFirstValue("values");

            if (u != null)
            {
                this.patientId = u;
            }


            if (PreferencesHelper.getPreferencesHelper().savePreferencesAsJSON(
                        patientId, v))
            {
                getResponse().setEntity("{\"Status\":\"OK\"}",
                    MediaType.APPLICATION_JSON);
                getResponse().setStatus(Status.SUCCESS_OK);
            }
            else
            {
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            }
        }
        else
        {
            getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
        }
    }


}
