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

import java.io.StringWriter;
import java.io.Writer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
// add this import if you need soapaction
//import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;

import org.restlet.Context;

import org.restlet.data.MediaType;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import org.restlet.Client;

import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;


/**
 * @author  markpitman
 */
public class SchedulingResource extends BaseResource
{

    /** DOCUMENT ME! */
    private String CalDavHost = "";

    /** DOCUMENT ME! */
    private String changeType = "";

    /** DOCUMENT ME! */
    private String method = "";

    /** DOCUMENT ME! */
    private String password = "turnkey";

    /** DOCUMENT ME! */
    private String provider = "";

    /** DOCUMENT ME! */
    private String scheduleHost = "/Calendar?frmt=ics&authToken=";

    /** DOCUMENT ME! */
    private String uid = "";

    /** DOCUMENT ME! */
    private String username = "admin@example.com";

    /** DOCUMENT ME! */
    private String values = "";

    /**
     * Creates a new SchedulingResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public SchedulingResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();
            System.out.println("SCHEDULING : " + query);

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

        String u = request.getResourceRef().getQueryAsForm().getFirstValue(
                "username");
        String p = request.getResourceRef().getQueryAsForm().getFirstValue(
                "password");
        String ui = request.getResourceRef().getQueryAsForm().getFirstValue(
                "uid");
        String ct = request.getResourceRef().getQueryAsForm().getFirstValue(
                "changeType");
        String pr = request.getResourceRef().getQueryAsForm().getFirstValue(
                "provider");

        if (p != null)
        {
            this.password = p;
        }

        if (u != null)
        {
            this.username = u;
        }

        if (ui != null)
        {
            this.uid = ui;
        }

        if (ct != null)
        {
            this.changeType = ct;
        }

        if (pr != null)
        {
            this.provider = pr;
        }

        CalDavHost = this.getProperty("CalDavHost");

        // This representation has only one type of representation.
        //getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
    }

    //POST
    /*
     * NOT - flex can't do a straight PUT via HTTP (flex shortcoming) - so it does a POST with a method=PUT parameter
     */
    /**
     * @see  org.restlet.resource.Resource#acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override public void acceptRepresentation(Representation entity)
        throws ResourceException
    {

        try
        {
            Form form = new Form(entity);
            String u = form.getFirstValue("username");
            String p = form.getFirstValue("password");
            String m = form.getFirstValue("method");
            String v = form.getFirstValue("values");

            if (p != null)
            {
                this.password = p;
            }

            if (u != null)
            {
                this.username = u;
            }

            if (m != null)
            {
                this.method = m;
            }

            if (v != null)
            {
                this.values = v;
            }

            if (this.method.equals("PUT"))
            {
                this.storeRepresentation(entity);
            }
            else
            {

                //System.out.println("in POST: "+ entity.getText());
                try
                {
                    Client client = new Client(Protocol.HTTP);
                    client.setConnectTimeout(10);

                    Request request = new Request(Method.POST,
                            new Reference(
                                CalDavHost + "/home/" + username +
                                "@socraticgrid.org" + scheduleHost +
                                authenticate()),
                            new StringRepresentation(entity.getText(),
                                MediaType.TEXT_XML));
                    Response response = client.handle(request);

                    if (response.getStatus().isSuccess())
                    {
                        getResponse().setStatus(Status.SUCCESS_CREATED);
                    }
                    else
                    {
                        getResponse().setStatus(
                            Status.CLIENT_ERROR_UNPROCESSABLE_ENTITY);
                    }
                }
                catch (Exception e)
                {
                    Logger.getLogger(SchedulingResource.class.getName()).log(
                        Level.SEVERE, null, e);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @see  org.restlet.resource.Resource#allowDelete()
     */
    @Override public boolean allowDelete()
    {
        return true;
    }


    //allow post - otherwise a http error code 405 will happen
    /**
     * @see  org.restlet.resource.Resource#allowPost()
     */
    @Override public boolean allowPost()
    {
        return true;
    }

    /**
     * Handle DELETE requests.
     */
    @Override public void removeRepresentations() throws ResourceException
    {
        System.out.println("in delete");

        try
        {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();

            // Create message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();


            // Object for message parts
            SOAPPart sp = msg.getSOAPPart();

            SOAPEnvelope env = sp.getEnvelope();
            env.addNamespaceDeclaration("soap",
                "http://schemas.xmlsoap.org/soap/envelop/");

            SOAPHeader header = env.getHeader();
            SOAPElement he = header.addChildElement(env.createName("context",
                        "soap", "urn:zimbra"));
            he.addChildElement("authToken", "soap").addTextNode(authenticate());

            SOAPBody body = env.getBody();

            // Populate body

            //neither seem to work - oh bother

            SOAPElement be = body.addChildElement(env.createName(
                        "MsgActionRequest", "soap", "urn:zimbraAccount"));
            be.addChildElement("action", "soap").addAttribute(new QName("uid"),
                "e6e8bfc3-26d7-4445-9e37-46463776bdb7").addAttribute(new QName(
                    "op"), "delete");
//            SOAPElement be = body.addChildElement(env.createName("DeleteIdentityRequest", "soap", "urn:zimbraAccount"));
//            be.addChildElement("action", "soap").addAttribute(new QName("uid"), "e6e8bfc3-26d7-4445-9e37-46463776bdb7");

            // Save message
            msg.saveChanges();

            // View input
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);

            // Send
            String urlval = CalDavHost + "/service/soap";

            //String urlval = getProperty("SOAPEndpoint");
            SOAPMessage reply = conn.call(msg, urlval);


            // View the output
            //  System.out.println("\nXML response\n");


            // Create transformer
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            // Get reply content
            Source source = reply.getSOAPPart().getContent();
            Writer outWriter = new StringWriter();

            // Set output transformation
            //StreamResult result = new StreamResult(System.out);
            StreamResult result = new StreamResult(outWriter);


            //transfrom to get the xml
            tf.transform(source, result);

            //close connection
            conn.close();

            String xmlSource = outWriter.toString();

            //token = xmlSource.substring(xmlSource.indexOf("<authToken>")+11, xmlSource.indexOf("</authToken>"));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * GET Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {
        System.out.println("getting schedule ");

        String result = "";
        Representation representation = null;

        if (getRequest().getResourceRef().getRemainingPart().indexOf("Update") >
                -1)
        {

            try
            {
                username = provider;

                //get schedule for uid
                Client client = new Client(Protocol.HTTP);
                client.setConnectTimeout(10);

                Request request = new Request(Method.GET,
                        new Reference(
                            CalDavHost + "/home/" + provider +
                            "@socraticgrid.org" + scheduleHost +
                            authenticate()));
                Response response = client.handle(request);

                if (response.getStatus().isSuccess())
                {

                }

            }
            catch (Exception e)
            {
            }

            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
        }
        else
        {

            try
            {
                Client client = new Client(Protocol.HTTP);
                client.setConnectTimeout(10);

                Request request = new Request(Method.GET,
                        new Reference(
                            CalDavHost + "/home/" + username +
                            "@socraticgrid.org" + scheduleHost +
                            authenticate()));
                Response response = client.handle(request);

                if (response.getStatus().isSuccess())
                {
                    result = response.getEntity().getText();
                    System.out.println("get schedule response: " + result);
                }
            }
            catch (Exception e)
            {
                Logger.getLogger(SchedulingResource.class.getName()).log(
                    Level.SEVERE, null, e);
            }

            representation = new StringRepresentation(result,
                    MediaType.TEXT_PLAIN);

        }

        return representation;
    }

    //PUT
    /**
     * @see  org.restlet.resource.Resource#storeRepresentation(org.restlet.resource.Representation)
     */
    @Override public void storeRepresentation(Representation entity)
    {

        try
        {
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);

            Request request = new Request(Method.PUT,
                    new Reference(
                        CalDavHost + "/home/" + username + "@socraticgrid.org" +
                        scheduleHost + authenticate()),
                    new StringRepresentation(values, MediaType.TEXT_XML));
            Response response = client.handle(request);

        }
        catch (Exception e)
        {
            Logger.getLogger(SchedulingResource.class.getName()).log(
                Level.SEVERE, null, e);
        }

    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String authenticate()
    {
        String token = "";

        try
        {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();

            // Create message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();


            // Object for message parts
            SOAPPart sp = msg.getSOAPPart();

            SOAPEnvelope env = sp.getEnvelope();
            env.addNamespaceDeclaration("soap",
                "http://schemas.xmlsoap.org/soap/envelop/");

            SOAPHeader header = env.getHeader();
            SOAPElement he = header.addChildElement(env.createName("content",
                        "soap", "urn:zimbra"));

            SOAPBody body = env.getBody();

            // Populate body

            SOAPElement be = body.addChildElement(env.createName("AuthRequest",
                        "", "urn:zimbraAccount"));
            be.addChildElement("account", "soap").addTextNode(username);
            be.addChildElement("password", "soap").addTextNode(password);


            // Save message
            msg.saveChanges();

            // View input
            //System.out.println("\n Soap request:\n");
            //msg.writeTo(System.out);

            // Send
            String urlval = CalDavHost + "/service/soap";

            //String urlval = getProperty("SOAPEndpoint");
            SOAPMessage reply = conn.call(msg, urlval);


            // View the output
            //  System.out.println("\nXML response\n");


            // Create transformer
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            // Get reply content
            Source source = reply.getSOAPPart().getContent();
            Writer outWriter = new StringWriter();

            // Set output transformation
            //StreamResult result = new StreamResult(System.out);
            StreamResult result = new StreamResult(outWriter);


            //transfrom to get the xml
            tf.transform(source, result);

            //close connection
            conn.close();

            String xmlSource = outWriter.toString();

            //System.out.println("schedule auth: "+xmlSource);
            token = xmlSource.substring(xmlSource.indexOf("<authToken>") + 11,
                    xmlSource.indexOf("</authToken>"));


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return token;
    }

}
