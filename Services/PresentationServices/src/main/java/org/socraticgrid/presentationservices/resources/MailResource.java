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

import org.socraticgrid.common.dda.SummaryData;
import org.socraticgrid.ura.EntityType;
import org.socraticgrid.ura.IdAddressBean;
import org.socraticgrid.ura.UniversalResourceAddressBean;
import org.socraticgrid.ura.UniversalResourceAddressBeanFactory;
import org.socraticgrid.presentationservices.helpers.ActionHelper;
import org.socraticgrid.presentationservices.helpers.CallStatus;
import org.socraticgrid.presentationservices.helpers.JSONHelper;

import java.net.URISyntaxException;
import java.net.URLDecoder;

import java.util.Iterator;

import javax.xml.ws.BindingProvider;

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
 * @author  markpitman
 */
public class MailResource extends BaseResource
{

    /** DOCUMENT ME! */
    private String action = "";

    /** DOCUMENT ME! */
    private String dataSource = "";

    /** DOCUMENT ME! */
    private String groupId = "";

    /** DOCUMENT ME! */
    private String itemId = "";

    /** DOCUMENT ME! */
    private String locationId = "";

    /** DOCUMENT ME! */
    private String message = "";

    /** DOCUMENT ME! */
    private String onlyNew = "";

    /** DOCUMENT ME! */
    private String patientId = "";

    /** DOCUMENT ME! */
    private String userId = "";

    /** DOCUMENT ME! */
    private String providerId = "";

    /** DOCUMENT ME! */
    private String ticket = "";

    /** DOCUMENT ME! */
    private String status = "";

    /** DOCUMENT ME! */
    private String includeUpdateHistory = "";

    /**
     * Creates a new MailResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public MailResource(Context context, Request request, Response response)
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
            action = this.getParameter(request, "action", "");
            dataSource = URLDecoder.decode(this.getParameter(request,
                        "dataSource", ""), "UTF-8");
            providerId = this.getParameter(request, "providerId", "");
            patientId = this.getParameter(request, "patientId", "");
            userId = this.getParameter(request, "userId", "");
            groupId = this.getParameter(request, "groupId", "");
            locationId = this.getParameter(request, "locationId", "");
            itemId = this.getParameter(request, "itemId", "");
            onlyNew = this.getParameter(request, "onlyNew", "");
            ticket = URLDecoder.decode(this.getParameter(request, "ticket", ""),
                    "UTF-8");
            message = this.getParameter(request, "message", "");
            status = this.getParameter(request, "status", "");
            includeUpdateHistory = this.getParameter(request,
                    "includeUpdateHistory", "No");
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

        CallStatus clStat = null;

        String user = "";

        if (providerId.length() > 0)
        {
            user = providerId;
        }
        else if (patientId.length() > 0)
        {
            user = patientId;
        }

        if (itemId.length() == 0)
        {
            itemId = ticket;
        }

        if (!userId.isEmpty())
        {
            user = userId;
        }

        clStat = ActionHelper.getActionHelper().updateAction(dataSource, itemId,
                action, status, message, user, patientId);


        getResponse().setEntity(JSONHelper.getCallStatus(clStat).toString(),
            MediaType.APPLICATION_JSON);

        if (clStat.isError())
        {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
                clStat.getStatusDetail());
        }
        else
        {
            getResponse().setStatus(Status.SUCCESS_OK);
        }

    }


    /**
     * Handle PUT requests. replace or update resource
     */
    @Override public void storeRepresentation(Representation entity)
        throws ResourceException
    {

        CallStatus clStat = null;

        String user = "";

        if (providerId.length() > 0)
        {
            user = providerId;
        }
        else if (patientId.length() > 0)
        {
            user = patientId;
        }

        if (itemId.length() == 0)
        {
            itemId = ticket;
        }

        if (!userId.isEmpty())
        {
            user = userId;
        }

        clStat = ActionHelper.getActionHelper().updateAction(dataSource, itemId,
                action, status, message, user, patientId);

        getResponse().setEntity(JSONHelper.getCallStatus(clStat).toString(),
            MediaType.APPLICATION_JSON);

        if (clStat.isError())
        {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL,
                clStat.getStatusDetail());
        }
        else
        {
            getResponse().setStatus(Status.SUCCESS_OK);
        }
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
     * DOCUMENT ME!
     *
     * @param   ds      DOCUMENT ME!
     * @param   itemId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getNewDetailData(String ds, String itemId)
    {
        String ret = "";


        try // Call Web Service Operation
        {
            org.socraticgrid.aggregator.DisplayDataAggregator service =
                new org.socraticgrid.aggregator.DisplayDataAggregator();

            org.socraticgrid.aggregator.DisplayDataAggregatorPortType port =
                service.getDisplayDataAggregatorPortSoap11();

            //Bind call to Endpoint
            ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));


            org.socraticgrid.common.dda.GetDetailDataRequestType getDetailDataRequest =
                new org.socraticgrid.common.dda.GetDetailDataRequestType();

            getDetailDataRequest.setDataSource(ds);
            getDetailDataRequest.setItemId(itemId);

            org.socraticgrid.common.dda.GetDetailDataResponseType result =
                port.getDetailData(getDetailDataRequest);

            boolean includeItemUpdates = Boolean.getBoolean(
                    includeUpdateHistory);

            JSONObject jo = new JSONObject();

            JSONArray joe = new JSONArray(result.getErrorList().toArray());
            jo.put("errorList", joe);
            jo.put("detailObject",
                JSONHelper.getDetaitData(result.getDetailObject(),
                    includeItemUpdates));

            JSONObject joOut = new JSONObject();
            joOut.put("GetDetailDataResponse", jo);
            ret = joOut.toString();


        }
        catch (Exception ex)
        {

            // TODO handle custom exceptions here
            ex.printStackTrace();
            ret = ex.getMessage();
        }


        return ret;
    }


    public String getNewSummaryData(String ds, String provId, String patId,
        String grpId, String locId, String newOnly)
    {
        String ret = "";

        try // Call Web Service Operation
        {
            org.socraticgrid.aggregator.DisplayDataAggregator service =
                new org.socraticgrid.aggregator.DisplayDataAggregator();
            org.socraticgrid.aggregator.DisplayDataAggregatorPortType port =
                service.getDisplayDataAggregatorPortSoap11();

            //Bind call to Endpoint
            ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));


            // TODO initialize WS operation arguments here
            org.socraticgrid.common.dda.GetSummaryDataForUserRequestType getSummaryDataForUserRequest =
                new org.socraticgrid.common.dda.GetSummaryDataForUserRequestType();

            String[] dss = ds.split(",");

            for (int i = 0; i < dss.length; i++)
            {
                getSummaryDataForUserRequest.getDataSources().add(dss[i]);
            }

            //Assign the provider Id
            getSummaryDataForUserRequest.setProviderId(provId);

            UniversalResourceAddressBean uid=null;

            try
            {

                if (userId.length() > 0)
                {

                    if (UniversalResourceAddressBeanFactory.isAddressBean(
                                userId))
                    {
                        uid = UniversalResourceAddressBeanFactory.getInstance()
                            .createIdAddressBean(userId);
                    }
                    else
                    {

                        if ((!provId.isEmpty())&&(userId.compareTo(patientId) == 0))
                        {

                            uid = new IdAddressBean(EntityType.PATIENT, patientId, "");
                        }
                        else if ((!provId.isEmpty())&&(userId.compareTo(provId) == 0))
                        {
                            uid = new IdAddressBean(EntityType.PROVIDER, provId,
                                    "");
                        }
                    }
                }
            }
            catch (URISyntaxException e)
            {
                //Log the error
            }

            if (uid == null)
            {
                if (!provId.isEmpty())
                {
                    uid = new IdAddressBean(EntityType.PROVIDER, provId, "");
                }
                else if (!patientId.isEmpty())
                {
                    uid = new IdAddressBean(EntityType.PATIENT, patientId, "");
                }
                else
                {
                    uid = new IdAddressBean(EntityType.UNKNOWN, "", "");
                }

            }
            getSummaryDataForUserRequest.setUserId(uid.toString());

            //Assign the only new tag
            getSummaryDataForUserRequest.setOnlyNew(Boolean.getBoolean(
                    newOnly));

            getSummaryDataForUserRequest.setGroupId(grpId);

            getSummaryDataForUserRequest.setPatientId(patId);

            getSummaryDataForUserRequest.setLocationId(locId);

            // TODO process result here
            org.socraticgrid.common.dda.GetSummaryDataResponseType result =
                port.getSummaryDataForUser(getSummaryDataForUserRequest);


            JSONObject jo = new JSONObject();

            JSONArray jos = new JSONArray();
            Iterator<SummaryData> itr = result.getSummaryObjects().iterator();

            boolean includeItemUpdates = Boolean.getBoolean(
                    includeUpdateHistory);

            while (itr.hasNext())
            {
                SummaryData data = itr.next();

                if (data != null)
                {
                    jos.put(JSONHelper.getSummaryData(data,
                            includeItemUpdates));
                }

            }

            jo.put("summaryObjects", jos);

            JSONArray joe = new JSONArray(result.getErrorList().toArray());
            jo.put("errorList", joe);

            JSONObject joOut = new JSONObject();
            joOut.put("GetSummaryDataForUserResponse", jo);
            ret = joOut.toString();

        }
        catch (Exception ex)
        {

            // TODO handle custom exceptions here
            ex.printStackTrace();
            ret = ex.getMessage();
        }


        return ret;
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
        String result = "";

        if (action.equalsIgnoreCase("summary"))
        {

            //result = transformToJson(this.getSummaryData(dataSource, providerId,
            //            patientId, groupId, locationId, onlyNew));
            result = getNewSummaryData(dataSource, providerId, patientId,
                    groupId, locationId, onlyNew);
        }
        else if (action.equalsIgnoreCase("detail"))
        {

            //result = transformToJson(this.getDetailData(dataSource, itemId));
            result = this.getNewDetailData(dataSource, itemId);
        }
        else
        {
            result = "{\"mail\":\"status\":\"Action = "+action+"not implemented\"}";
        }

        Representation representation = new StringRepresentation(result,
                MediaType.APPLICATION_JSON);

        return representation;

    }


}
