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

import org.socraticgrid.properties.DateProperty;
import org.socraticgrid.properties.DatePropertyAccessor;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;

import java.io.File;

import java.util.logging.Level;
import java.util.logging.Logger;
// add this import if you need soapaction
//import javax.xml.soap.MimeHeaders;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;

import org.restlet.resource.Resource;

import org.apache.commons.codec.digest.DigestUtils;

import java.net.URLDecoder;

import org.restlet.data.Status;

import org.restlet.resource.Variant;


/**
 *
 * @author markpitman
 */
public class BaseResource extends Resource
{


    public static final String MEDICATIONS = "medications";
    public static final String DEMOGRAPHICS = "medications";
    private static final String DATE_PROPERTIES_FILES =
        "PresentationServiceDates";

    //-------------------------------------------------------------------------
    // These are used to inidicate whether the PS request should return
    // a minimal list of items, or full detail of a specific item,
    // or the ecs content of an item, or the raw detail return of all items.
    //-------------------------------------------------------------------------
    public static final String RESPONSTYPE_LIST = "list";
    public static final String RESPONSTYPE_DETAIL = "detail";
    public static final String RESPONSTYPE_ECS = "ecs";
    public static final String RESPONSTYPE_RAW = "raw";


//    protected String apiKey;

    public BaseResource(Context context, Request request, Response response)
    {
        super(context, request, response);

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));

        //store passed parms
//        apiKey = request.getResourceRef().getQueryAsForm().getFirstValue(
//                "apiKey");

        if (PropertyHelper.getPropertyHelper().getPropertyAsBoolean(
                    "EnableSecurity"))
        {

            try
            {
                String query = request.getResourceRef().getQueryAsForm()
                    .getQueryString();

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
        }


    }

    /**
     * Returns a full representation for a given variant.
     */
//    @Override
//    public Representation represent(Variant variant) throws ResourceException {
//
//        if (checkApiKey(apiKey)){
//            String result = makeSOAPCall();
//
//            Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
//            return representation;
//        }
//        return new StringRepresentation("APIKey invalid", MediaType.APPLICATION_JSON);
//    }

    //TODO:  implement apk check when LDAP exists
    protected boolean checkApiKey(String key)
    {
        return true;
    }

    protected boolean checkApiCaller(String requestStr)
    {
        Logger.getLogger(BaseResource.class.getName()).setLevel(Level.ALL);

        getLogger().info("chekcAPI - req: " + requestStr);

        String strippedRequest = "";

        try
        {
            requestStr = URLDecoder.decode(requestStr, "UTF-8");

            if (getProperty("checkApiRequests").equalsIgnoreCase("true"))
            {
                //actually check the api against the sha1 salt
                //first step it to bust up the queryString in the request.

                //fail if queryString is not right
                //else bust the incomming string into fields split on the ampersand
                //if it's checksum= field save off the actual checksum and toss the field away
                //else put the querystring back minus the checksum= pair
                if ((requestStr == null) ||
                        (requestStr.indexOf("checksum") < 0))
                    return false;
                else
                {
                    String checksum = "";
                    String[] fields = requestStr.split("&");

                    for (int i = 0; i < fields.length; i++)
                    {
                        String string = fields[i];
                        System.out.println("fileds" + i + "--> " + string);

                        if (string.indexOf("checksum=") > -1)
                            checksum = string.substring(8);
                        else
                        {

                            if (strippedRequest.equals(""))
                                strippedRequest = string;
                            else
                                strippedRequest = strippedRequest + "&" +
                                    string;
                        }

                    }

                    //remove the leading equalsign
                    //if (checksum.startsWith("="))
                    checksum = checksum.substring(1);

                    Logger.getLogger(BaseResource.class.getName()).log(
                        Level.FINEST, "requestChecksum: " + checksum);
                    Logger.getLogger(BaseResource.class.getName()).log(
                        Level.FINEST, "strippedRequest: " + strippedRequest);


                    String cs = getHash(strippedRequest,
                            getProperty("apiSalt"));
                    Logger.getLogger(BaseResource.class.getName()).log(
                        Level.FINEST, "cs: " + cs);

                    if ((cs == null) || (cs.equals(checksum) == false))
                    {
                        Logger.getLogger(BaseResource.class.getName()).log(
                            Level.INFO,
                            "checksumError:  checksum did not pass security check, request will be rejected: " +
                            strippedRequest);

                        return false;
                    }

                    Logger.getLogger(BaseResource.class.getName()).log(
                        Level.INFO,
                        "checksum ok:  passed security check: " +
                        strippedRequest);

                    return true;
                }

            }
            else
                return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    protected String getHash(String qs, String salt)
    {
        return DigestUtils.shaHex(qs + salt);
    }

    //TODO:  module that replaces nodes
    protected String replaceNodes(String xml, String resource)
    {
        return xml;
    }

    protected String getProperty(String property)
    {
        // Read properties file.

        return PropertyHelper.getPropertyHelper().getProperty(property);
    }

    protected File getXslFile()
    {

        //InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("patientServicesJson.xsl");
        //InputStream is =  this.getClass().getClassLoader().getResourceAsStream("patientServicesJson.xsl");
        //System.out.println("xslinputStream: "+ getProperty("xslFile"));
        return new File(getProperty("xslFile"));


    }

    protected String getParameter(Request request, String parameter,
        String defaultValue)
    {
        String param = request.getResourceRef().getQueryAsForm().getFirstValue(
                parameter);

        if (param != null)
        {
            return param;
        }
        else
        {
            return defaultValue;
        }
    }

    protected String getDateParameter(Request request, String parameter,
        String subject, String domain, String qualifer)
    {
        String param = request.getResourceRef().getQueryAsForm().getFirstValue(
                parameter);

        if (param != null)
        {
            return param;
        }
        else
        {
            String out;

            try
            {
                DateProperty dp = DatePropertyAccessor.getDateProperty(
                        DATE_PROPERTIES_FILES, subject, domain, qualifer);
                out = dp.getCDATime();
            }
            catch (PropertyAccessException e)
            {
                out = "";
            }

            return out;
        }
    }
}
