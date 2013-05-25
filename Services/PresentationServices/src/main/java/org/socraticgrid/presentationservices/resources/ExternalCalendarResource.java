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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.restlet.Client;
import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
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
public class ExternalCalendarResource extends BaseResource
{

    /** DOCUMENT ME! */
    private String action = "";

    /** DOCUMENT ME! */
    private String dataSource = "";

    /** DOCUMENT ME! */
    private String groupId = "";

    /** DOCUMENT ME! */
    private String locationId = "";

    /** DOCUMENT ME! */
    private String patientId = "";

    /** DOCUMENT ME! */
    private String providerId = "";

    /** DOCUMENT ME! */
    private String calendarName = "";

    /**
     * Creates a new MailResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public ExternalCalendarResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        // This representation has only one type of representation.
        getVariants().clear();

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.TEXT_CALENDAR));

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
            providerId = this.getParameter(request, "providerId", "");
            patientId = this.getParameter(request, "patientId", "");
            groupId = this.getParameter(request, "groupId", "");
            locationId = this.getParameter(request, "locationId", "");
            calendarName = this.getParameter(request, "calendarName", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    //handle POST requests.  create resource
    /**
     * @see  org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override public void acceptRepresentation(Representation entity)
        throws ResourceException
    {


        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
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

        //String endPoint = "http://www.google.com/calendar/ical/wrightbase%40gmail.com/public/basic.ics";
        Representation representation = null;

        try
        {
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);

            CalendarInfo cal = this.findCalendarInfo(patientId, calendarName);

            if (cal != null)
            {


                Request request = new Request(Method.GET,
                        new Reference(cal.url));

                Response response = client.handle(request);

                if (response.getStatus().isSuccess())
                {
                    representation = new StringRepresentation(
                            response.getEntity().getText(),
                            MediaType.TEXT_CALENDAR);

                }
                else
                {
                    throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                        "Error during call to JSONPojoFactServiceEndpoint, Status = " +
                        response.getStatus().getDescription());

                }
            }
            else
            {
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST,
                    "No such external calendar " + this.calendarName);

            }
        }
        catch (ResourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            Logger.getLogger(AppointmentsResource.class.getName()).log(
                Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }


        return representation;

    }

    private CalendarInfo findCalendarInfo(String patientId, String calName)
    {
        CalendarInfo out = null;
        List<CalendarInfo> calList = getCalendars(patientId);
        Iterator<CalendarInfo> itr = calList.iterator();

        while (itr.hasNext())
        {
            CalendarInfo ci = itr.next();

            if (ci.name.compareTo(calName) == 0)
            {
                out = ci;

                break;
            }
        }

        //out.url="http://www.google.com/calendar/ical/wrightbase%40gmail.com/public/basic.ics";
        return out;
    }

    private List<CalendarInfo> getCalendars(String patientId)
    {
        LinkedList<CalendarInfo> out = new LinkedList<CalendarInfo>();

        try
        {
            String patPref = PreferencesHelper.getPreferencesHelper()
                .getPreferencesAsJSON(patientId);
            JSONObject jo = new JSONObject(patPref);
            JSONObject jo1 = jo.getJSONObject("preferences");
            JSONArray joa = jo1.optJSONArray("externalCalendar");

            if (joa != null)
            {

                for (int i = 0; i < joa.length(); i++)
                {
                    JSONObject joe = joa.getJSONObject(i);
                    out.add(new CalendarInfo(joe));
                }
            }
        }
        catch (JSONException exp)
        {
            getLogger().log(Level.SEVERE,
                "Error parsing preferences for patient " + patientId, exp);
        }


        return out;
    }

    public class CalendarInfo
    {
        public String url;
        public String name;
        public String type;
        public String username;
        public String password;
        public boolean authenicate;
        public String eventColor;

        CalendarInfo()
        {

        }

        CalendarInfo(JSONObject jo)
        {
            name = jo.optString("name");
            url = jo.optString("url");
            type = jo.optString("type");
            username = jo.optString("username");
            password = jo.optString("password");
            authenicate = jo.optBoolean("authenicate");
            eventColor = jo.optString("eventColor");

        }

    }


}
