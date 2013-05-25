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
 *
 * @author markpitman
 */
public class PatientDemographicsResource extends BaseResource
{

    //we had a default user/patient coded for development - but commenting that out
    private String patientId = "";
    private int version = 2;
    private String jsonRequestString = "";
    private String apiKey;

    public PatientDemographicsResource(Context context, Request request,
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


        patientId = this.getParameter(request, "patientId", "");
        //TODO; Add version support

    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {
        String endPoint="";
        if (version ==1)
        {
            jsonRequestString =
            "{\"getDemographicsFact\":{\"criteria\":{\"queryId\":\"20100411011010\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPA_IN201307UV02\",\"triggerEventCode\":\"PRPA_TE201307UV02\",\"patientId\":\"" +
            patientId +
            "\",\"careProvisionCode\":\"\",\"careRecordStartTimePeriod\":\"\",\"careRecordEndTimePeriod\":\"\"}}}";
            endPoint = this.getProperty("JSONPojoFactServiceEndpoint");
        }
        else if (version ==2)
        {
            jsonRequestString =
            "{\"getDemographicsFact\":{\"criteria\":{\"queryId\":\"20100411011010\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPA_IN201307UV02\",\"triggerEventCode\":\"PRPA_TE201307UV02\","+
            "\"careRecordPayload\":{"+
            "\"patientId\":\"" +
            patientId +
            "\",\"careProvisionCode\":\"\",\"careRecordStartTimePeriod\":\"\",\"careRecordEndTimePeriod\":\"\"}}}}";

            endPoint = this.getProperty("JSONPojoFactServiceEndpointV2");

        }
        // if (checkApiKey(apiKey)) {
//            String result = makeSOAPCall();

//            Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
//            return representation;
        try
        {
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);

            Request request = new Request(Method.POST,
                    new Reference(
                        endPoint),
                    new StringRepresentation(jsonRequestString));
            Response response = client.handle(request);

            if (response.getStatus().isSuccess())
            {
                String result=response.getEntity().getText();
                result = addMobileNumberIfNeeded(result);
                Representation representation = new StringRepresentation(
                        result,
                        MediaType.APPLICATION_JSON);

                return representation;
            }
        }
        catch (ResourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            Logger.getLogger(PatientDemographicsResource.class.getName()).log(
                Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }

        return null;

        //}
        //return new StringRepresentation("APIKey invalid", MediaType.APPLICATION_JSON);
    }

    private String addMobileNumberIfNeeded(String text) throws ResourceException
    {
        if (text.isEmpty())
            return text;

        JSONObject jo;
        try
        {
            jo = new JSONObject(text);
            //Grab the telcom segment
            //Now we are looking for personFact.telcom[].value.type
            //grab personFact
            JSONObject pf = jo.getJSONObject("personFact");

            //Make sure telcom os and array
            Object otelcom = pf.opt("telecom");
            JSONArray telcom=null;
            if (otelcom == null)
            {
                telcom = new JSONArray();
                pf.put("telecom", telcom);

            }
            else if (!(otelcom instanceof JSONArray))
            {
                JSONObject val = (JSONObject) otelcom;
                telcom = new JSONArray();

                telcom.put(val);
                pf.remove("telecom");
                pf.put("telecom", telcom);
            }
            else
            {
                telcom = (JSONArray) otelcom;
            }

            boolean fnd = false;
            for (int i=0; i<telcom.length();i++)
            {
                JSONObject info = telcom.getJSONObject(i);
                JSONObject telcomType =info.getJSONObject("telecomType");
                if (telcomType.getString("code").compareTo("MC")==0)
                {
                    fnd=true;
                    break;
                }

            }
            if (!fnd)
            {
                /*
            "telecomType":{
            "codeSystem":"",
            "codeSystemName":"",
            "code":"HP",
            "label":""
                 */
                JSONObject phn = new JSONObject();
                JSONObject type = new JSONObject();
                phn.put("telcomType", type);
                type.put("code", "MC");
                type.put("codeSystem","");
                type.put("codeSystemName","");
                type.put("label","");
                String num;
                if (patientId.compareTo("6")==0)
                {
                    num = "1-619-203-0130";
                }
                else
                {
                    num = "1-858-395-7317";
                }
                phn.put("value",num);
                String test = phn.toString();
                telcom.put(phn);

            }
        }
        catch (JSONException e)
        {
            Logger.getLogger(PatientDemographicsResource.class.getName()).log(
            Level.SEVERE, "Unable to add telcom to JSON String: "+text, e);
            return text;
            // throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
            //    "JSON Translation", e);
        }
        return jo.toString();
    }

}
