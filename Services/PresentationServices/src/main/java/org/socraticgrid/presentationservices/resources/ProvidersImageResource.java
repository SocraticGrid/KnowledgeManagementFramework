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

import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.presentationservices.representations.JPEGRepresentation;

import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.Variant;


/**
 * @author  markpitman
 */
public class ProvidersImageResource extends BaseResource
{

    private static String PROVIDER_IMAGEFILE = "/ProviderMissingImage.jpg";

    /** DOCUMENT ME! */
    private String apiKey;

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
    public ProvidersImageResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        // This representation does not support JSON
        getVariants().clear();
        getVariants().add(new Variant(MediaType.IMAGE_JPEG));


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

        providerId = this.getParameter(request, "providerId", "");

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

        if (providerId.isEmpty())
        {
            throw new ResourceException(Status.SERVER_ERROR_SERVICE_UNAVAILABLE,
                "No providerId supplied");

        }


        Representation representation = null;

        try
        {

            //"employeeNumber="+id
            List<ContactDTO> provList = LdapService.getContactDAO().findContact(
                    "employeeNumber=" + providerId);

            BufferedImage image = null;

            if (!provList.isEmpty())
            {
                ContactDTO provObj = provList.get(0);

                if ((provObj.getJpegPhoto() != null) &&
                        (provObj.getJpegPhoto().length > 0))
                {
                    ByteArrayInputStream bai = new ByteArrayInputStream(
                            provObj.getJpegPhoto());
                    image = ImageIO.read(bai);

                }

            }

            if (image == null)
            {
                image = this.getDefaultImage();
            }

            representation = new JPEGRepresentation(image);
        }
        catch (Exception e)
        {
            Logger.getLogger(ProvidersImageResource.class.getName()).log(
                Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to LDAP: " + e.getMessage(), e);
        }

        return representation;
    }


    private BufferedImage getDefaultImage() throws IOException
    {
        BufferedImage image;
        InputStream is;

        is = this.getClass().getResourceAsStream(PROVIDER_IMAGEFILE);

 

        image = ImageIO.read(is);

        return image;
    }

    String getProviderForPatient(String patientId)
    {

        //TODO:
        return "10000000210";
    }


}
