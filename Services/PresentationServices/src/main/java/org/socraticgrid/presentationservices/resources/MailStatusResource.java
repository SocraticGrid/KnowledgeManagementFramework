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

import org.socraticgrid.presentationservices.helpers.ActionHelper;
import org.socraticgrid.presentationservices.helpers.CallStatus;

import org.socraticgrid.presentationservices.helpers.JSONHelper;
import java.net.URLDecoder;

import java.util.HashMap;


import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;


/**
 * @author  markpitman
 */
public class MailStatusResource extends BaseResource
{

    private static HashMap<String, String> alertDataSources;
    private static HashMap<String, String> alertAlternateActions;
    private static HashMap<String, String> mailAlternateActions;
    private static HashMap<String, String> docAlternateActions;

    static
    {
        alertDataSources = new HashMap<String, String>();

        alertDataSources.put("MedAlerts", "");
        alertDataSources.put("MedAlerts - mobile", "");
        alertDataSources.put("Patient Alerts", "");
        alertDataSources.put("Messages", "");

        alertAlternateActions = new HashMap<String, String>();
        alertAlternateActions.put("SEEN", "Read");

        mailAlternateActions = new HashMap<String, String>();
        mailAlternateActions.put("Read", "SEEN");

        docAlternateActions = new HashMap<String, String>();
    }

    /** DOCUMENT ME! */
    //rivate String action = "";

    /** DOCUMENT ME! */
    private String dataSource = "";


    /** DOCUMENT ME! */
    private String itemId = "";

    /** DOCUMENT ME! */
    private String patientId = "";

    /** DOCUMENT ME! */
    private String userId = "";

    /** DOCUMENT ME! */
    private String status = ""; //True or False
    private String flag = ""; //Comma separated  list of flag names ANSWERED, DELETED, DRAFT, FLAGGED, RECENT, SEEN
    private String message = "";

    /**
     * Creates a new MailResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public MailStatusResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);


        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();
            System.out.println("query: " + query);

            if (checkApiCaller(query) != true)
            {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        }
        catch (Exception e)
        {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }

        setModifiable(true);
        setAvailable(true);


        try
        {
            dataSource = URLDecoder.decode(this.getParameter(request,
                        "dataSource", ""), "UTF-8");
            itemId = this.getParameter(request, "itemId", "");
            flag = this.getParameter(request, "flag", "");
            status = this.getParameter(request, "status", "true");
            patientId = this.getParameter(request, "patientId", "");
            message = URLDecoder.decode(this.getParameter(request, "message",
                        ""), "UTF-8");
            userId = this.getParameter(request, "userId", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    //handle POST requests.  create resource
    /**
     * @see  org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override public void acceptRepresentation(Representation entity)
        throws ResourceException
    {

        getLogger().entering("MailStatusResource","acceptRepresentation");


        CallStatus result = ActionHelper.getActionHelper().updateAction(dataSource, itemId, flag, status, message, userId, patientId);


        getResponse().setEntity(JSONHelper.getCallStatus(result).toString(), MediaType.APPLICATION_JSON);

        if (result.isError())
        {
            getResponse().setStatus(Status.SUCCESS_OK,result.getStatusDetail());
        }
        else
        {
            getResponse().setStatus(Status.SUCCESS_OK);
        }

        getLogger().exiting("MailStatusResource","acceptRepresentation");

    }


    /**
     * Handle PUT requests. replace or update resource
     */
    @Override public void storeRepresentation(Representation entity)
        throws ResourceException
    {

    }

    /**
     * @see  org.restlet.resource.Resource#allowPut()
     */
    @Override public boolean allowPut()
    {
        return true;
    }

    @Override public boolean allowPost()
    {
        return true;
    }

    /**
     * Handle DELETE requests. remove/delete resource
     */
    @Override public void removeRepresentations() throws ResourceException
    {

        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * handle GET Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        getLogger().entering("MailStatusResource","represent");

        CallStatus result = new CallStatus();
        result.setError(true);
        result.setStatusDetail("Mike - The Mail/Status Fetch Not yet implement ed - Called as a Get");
        Representation representation = new StringRepresentation(
                JSONHelper.getCallStatus(result).toString(), MediaType.APPLICATION_JSON);

        getLogger().exiting("MailStatusResource","represent");

        return representation;

    }


}
