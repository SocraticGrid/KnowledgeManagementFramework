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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import java.net.URL;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import javax.xml.ws.BindingProvider;
import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import org.socraticgrid.aggregator.*;
import org.socraticgrid.common.dda.*;


/**
 * Returns NHIN document having gone through C32 transform.
 *
 * @author  chrisjan
 */
public class DocumentsResource extends BaseResource
{

    /** User id making request. */
    private String userId;

    /** Document id being requested. */
    private String itemId;

    /**
     * Creates a new DocumentResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public DocumentsResource(Context context, Request request, Response response)
    {
        super(context, request, response);


        try
        {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
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
            userId = this.getParameter(request, "userId", null);
            itemId = this.getParameter(request, "itemId", null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    /**
     * @see  org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override public void acceptRepresentation(Representation entity)
        throws ResourceException
    {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }


    /**
     * Handle PUT requests. replace or update resource
     */
    @Override public void storeRepresentation(Representation entity)
        throws ResourceException
    {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource#allowPut()
     */
    @Override public boolean allowPut()
    {
        return false;
    }

    @Override public boolean allowPost()
    {
        return false;
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
        String result = getDocument(userId, itemId);

        Representation representation = new StringRepresentation(result, MediaType.TEXT_HTML);

        return representation;
    }

    /**
     * Fetch the document and perform C32 transform.
     *
     * @param   userId  user id making request
     * @param   itemId  document id
     *
     * @return  document
     */
    public String getDocument(String userId, String itemId)
    {
        String ret = "";

        try { // Call Web Service Operation
            DisplayDataAggregator service = new DisplayDataAggregator();
            DisplayDataAggregatorPortType port = service.getDisplayDataAggregatorPortSoap11();
            ((BindingProvider) port).getRequestContext().put(
                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                getProperty("DisplayDataAggregatorService"));

            GetDetailDataForUserRequestType request = new GetDetailDataForUserRequestType();
            request.setDataSource("NHIN Documents");
            request.setUserId(userId);
            request.setItemId(itemId);
            GetDetailDataResponseType result = port.getDetailDataForUser(request);

            //C32 transform
            if ((result.getDetailObject() != null) && (result.getDetailObject().getData() != null)) {
                URL url = new URL(getProperty("staticResources") + "/CCD.xsl");
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringWriter outWriter = new StringWriter();
                Result outResult = new StreamResult(outWriter);
                TransformerFactory tFactory = TransformerFactory.newInstance();
                Transformer transformer = tFactory.newTransformer(new StreamSource(reader));
                transformer.transform(new StreamSource(new StringReader(result.getDetailObject().getData())), outResult);

                ret = outWriter.toString();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            ret = "<html><body>" + e.getMessage() + "</body></html>";
        }

        return ret;
    }

}
