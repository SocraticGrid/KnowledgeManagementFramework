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
public class ProvidersResource extends BaseResource
{

    /** DOCUMENT ME! */
    private String apiKey;

    //we had a default user/patient coded for development - but commenting that out
    /** DOCUMENT ME! */
    private String patientId = "";

    /** DOCUMENT ME! */
    private String providerFamilyName = "";

    /** DOCUMENT ME! */
    private String providerFirstName = "";

    /** DOCUMENT ME! */
    private String providerId = "";
    /** Only version 2 + */
    private int version = 2;

    /**
     * Creates a new ProvidesrResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public ProvidersResource(Context context, Request request, Response response)
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
        providerId = this.getParameter(request, "providerId", "");
        providerFirstName = this.getParameter(request, "providerFirstName", "");

        providerFamilyName = this.getParameter(request, "providerFamilyName",
                "");


    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        // if (checkApiKey(apiKey)) {
//            String result = makeSOAPCall();

//            Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
//            return representation;
        if (providerId.isEmpty() && providerFamilyName.isEmpty() &&
                providerFirstName.isEmpty() && !patientId.isEmpty())
        {
            providerId = getProviderForPatient(patientId);
        }

        if (providerId.isEmpty() && providerFamilyName.isEmpty())
        {
            throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE,
                "No providerId supplied and providerFamilyName not supplied");

        }

        //TODO: Add add nameSupport

        String jsonRequestString;

        if (providerId.isEmpty())
        {

            //Query by Name
            //Query by Id only
            jsonRequestString =
                "{\"getProviderFacts\":{\"criteria\":{\"queryId\":\"6858a017-39c1-4153-bbd4-eaedac72a0e7\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPM_IN306010UV01\",\"triggerEventCode\":\"PRPM_TE306010UV01\",\"providerSearchPayload\":{\"name\":\"{\"familyName\":\"" +
                providerFamilyName + "\",\"firstName\":\"" + providerFirstName +
                "\"}}}}}";
        }
        else
        {

            //Query by Id only
            jsonRequestString =
                "{\"getProviderFacts\":{\"criteria\":{\"queryId\":\"6858a017-39c1-4153-bbd4-eaedac72a0e7\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPM_IN306010UV01\",\"triggerEventCode\":\"PRPM_TE306010UV01\",\"providerSearchPayload\":{\"id\":\"" +
                providerId + "\"}}}}";

            String x =
                "{\"getProviderFacts\":{\"criteria\":{\"queryId\":\"6858a017-39c1-4153-bbd4-eaedac72a0e7\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPM_IN306010UV01\",\"triggerEventCode\":\"PRPM_TE306010UV01\",\"providerSearchPayload\":{\"id\":\"" +
                providerId + "\"}}}}";

            if (x.compareTo(jsonRequestString) == 0)
            {
                Logger.getLogger(ProvidersResource.class.getName()).log(
                    Level.INFO, "Equivalent Strings!");
            }
        }

        Representation representation = null;

        try
        {
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);

            Request request = new Request(Method.POST,
                    new Reference(
                        this.getProperty("JSONPojoFactServiceEndpointV2")),
                    new StringRepresentation(jsonRequestString));
            Response response = client.handle(request);

            if (response.getStatus().isSuccess())
            {
                representation = new StringRepresentation(response.getEntity()
                        .getText(), MediaType.APPLICATION_JSON);

            }
            else
            {
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint, Status = "+response.getStatus().getDescription());
            }
        }
        catch (ResourceException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            Logger.getLogger(ProvidersResource.class.getName()).log(Level.SEVERE,
                null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }

        return representation;

        //}
        //return new StringRepresentation("APIKey invalid", MediaType.APPLICATION_JSON);
    }

    String getProviderForPatient(String patientId)
    {

        //TODO:
        //TEMP: Now useing Joe Provider from Janus
        return "10000000210";
    }


}
