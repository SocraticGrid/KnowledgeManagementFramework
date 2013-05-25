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


import org.socraticgrid.presentationservices.helpers.JSONHelper;
import org.json.JSONArray;
import org.json.JSONObject;
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
 *
 * @author markpitman
 */
public class LocationScheduleResource extends BaseResource
{

    //we had a default user/patient coded for development - but commenting that out
    private String locationId = "";
    private String fromDate = "";
    private String toDate ="";

    private String jsonRequestString = "";
    private String apiKey;

    public LocationScheduleResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();
//        System.out.println("query: "+query);
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

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));

        //store passed parms
        apiKey = request.getResourceRef().getQueryAsForm().getFirstValue(
                "apiKey");

        fromDate=this.getParameter(request, "fromDate", "");
        toDate=this.getParameter(request, "toDate", "");
        locationId=this.getParameter(request, "locationId", "");

    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

         Representation representation = null;
        try
        {
            JSONObject jo = new JSONObject();
            jo.put("locationId", locationId);
            //TODO Fix
            jo.put("locationName","Clinic"+locationId);
            jo.put("fromDate", fromDate);
            jo.put("toDate", toDate);

            JSONArray array = new JSONArray();
            JSONArray apptArray;
            JSONObject appt;
            //TODO: Replace Dummy data with real patients
            JSONObject pat;
            pat = JSONHelper.getPatient("6", "LABORATORY", "PATIENT","Male","50" );
            apptArray = new JSONArray();
            appt = new JSONObject();
            appt.put("appointmentTime","2011-02-03T15:00:00.000-08:00");
            apptArray.put(appt);
            pat.put("appointments", apptArray);
            array.put(pat);

            pat = JSONHelper.getPatient("8", "PEDIATRIC", "PATIENT","Male","7" );
            apptArray = new JSONArray();
            appt = new JSONObject();
            appt.put("appointmentTime","2011-02-12T09:45:00.000-08:00");
            apptArray.put(appt);
            appt = new JSONObject();
            appt.put("appointmentTime","2011-02-19T09:45:00.000-08:00");
            apptArray.put(appt);
            pat.put("appointments", apptArray);
            
            array.put(pat);
            jo.put("patients", array);

            representation = new StringRepresentation(jo.toString(), MediaType.APPLICATION_JSON);
        }
        catch(Exception e)
        {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }
        return representation;
        //}
        //}
        //return new StringRepresentation("APIKey invalid", MediaType.APPLICATION_JSON);

    }



}
