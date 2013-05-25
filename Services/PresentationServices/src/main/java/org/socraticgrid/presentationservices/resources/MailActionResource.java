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

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
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
 * NOTE:  MailAction refers to the embedded actions in a CDS Recommendation (will obtain | arrange it | disregard )
 */
public class MailActionResource extends BaseResource {

    //providerAndPatientEmailSender?fromUser=fu&fromUserProvider=fup&toUser=tu&toUserProvider=tup&subject=s&message=m
    private String userId="";
    private String action="";
    private String fromUser="";
    private String fromUserProvider="";
    private String toUser="";
    private String toUserProvider="";
    private String subject="";
    private String message="";
    public final static String EMAIL_TASK_ID = "17";
    public final static String TRUE = "true";
    public final static String FALSE = "false";
    public final static int PATIENT_SENDS_TO_PROVIDER=0;
    public final static int PROVIDER_SENDS_TO_PATIENT=1;


    public MailActionResource(Context context, Request request, Response response) {
        super(context, request, response);

        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
    //      System.out.println("query: "+query);
            if (checkApiCaller(query)!=true){
               getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
               return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        setModifiable(true);
        setAvailable(true);
        allowPut();

        final Method httpMethod = request.getMethod();
        Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, "httpMethod: "+ httpMethod);

        try {
            userId = request.getResourceRef().getQueryAsForm().getFirstValue("userId");
            action = request.getResourceRef().getQueryAsForm().getFirstValue("action");
            Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, userId);
            Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, action);




//            fromUser=request.getResourceRef().getQueryAsForm().getFirstValue("fromUser");
//            fromUserProvider=request.getResourceRef().getQueryAsForm().getFirstValue("fromUserProvider");
//            toUser=request.getResourceRef().getQueryAsForm().getFirstValue("toUser");
//            toUserProvider=request.getResourceRef().getQueryAsForm().getFirstValue("toUserProvider");
//            subject=request.getResourceRef().getQueryAsForm().getFirstValue("subject");
//            message=request.getResourceRef().getQueryAsForm().getFirstValue("message");
        } catch (Exception e) {
            //don't care
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }

    /**
     * handle GET Returns a full representation for a given variant.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String result = "{\"mail\":\"status\":\"GET not implemented yet\"}";
        Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, "GET on /Mail");

        Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;

    }

    /**
     * Handle PUT requests.  replace or update resource
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {

        Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, "PUT on /Mail");
        MediaType mediaType = entity.getMediaType();
        Form form = new Form(entity);
       //  item.setDescription(form.getFirstValue("description"));

            fromUser=(String) form.getFirstValue("fromUser");
            fromUserProvider=(String) form.getFirstValue("fromUserProvider");
            toUser=(String) form.getFirstValue("toUser");
            toUserProvider=(String) form.getFirstValue("toUserProvider");
            subject=(String) form.getFirstValue("subject");
            message=(String) form.getFirstValue("message");

        //implement code for PUT of mail messages
//        System.out.println("in mailAction Put");
//        StringBuffer sb = new StringBuffer();
//        sb.append("fromUser:"+fromUser+"|");
//        sb.append("fromUserProvider:"+fromUserProvider+"|");
//        sb.append("toUser:"+toUser+"|");
//        sb.append("toUserProvider:"+toUserProvider+"|");
//        sb.append("subject:"+subject+"|");
//        sb.append("message:"+message);
//        System.out.println("values: "+sb.toString());

        // The PUT request updates or creates the resource.
        if (patientAndProviderEmailSend(fromUser, fromUserProvider, toUser, toUserProvider, subject, message))
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
        else
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_ACCEPTABLE);
    }

    //handle POST requests.  create resource
    @Override
    public void acceptRepresentation(Representation  entity) throws ResourceException{
        Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, "POST on /Mail");

        //implement code for POST of mail messages

        // Set the response's status and entity
        getResponse().setStatus(Status.SUCCESS_CREATED);
    }

    /**
     * Handle DELETE requests.  remove/delete resource
     */
    @Override
    public void removeRepresentations() throws ResourceException {
        Logger.getLogger(MailActionResource.class.getName()).log(Level.INFO, "DELETE on /Mail");

        //implement code for DELETE of mail messages

        // Tells the client that the request has been successfully fulfilled.
        getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
    }
    public boolean patientAndProviderEmailSend(String fromUser, String fromUserProvider, String toUser, String toUserProvider, String subject, String message){
        String ret="";
       // System.out.println("ticket: "+ticket);
        try {
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection conn = scf.createConnection();

            // Create message
            MessageFactory mf = MessageFactory.newInstance();
            SOAPMessage msg = mf.createMessage();

            // Object for message parts
            SOAPPart sp = msg.getSOAPPart();

            SOAPEnvelope env = sp.getEnvelope();
            env.addNamespaceDeclaration("soap", "http://schemas.xmlsoap.org/soap/envelop/");
            env.addNamespaceDeclaration("urn", "urn:gov:hhs:fha:nhinc:common:task");


            SOAPHeader header = env.getHeader();

            SOAPBody body = env.getBody();

            // Populate body
            SOAPElement be = body.addChildElement("SendMailTaskRequest","urn");
            be.addChildElement("taskID", "urn").addTextNode(EMAIL_TASK_ID);
            be.addChildElement("fromUser", "urn").addTextNode(fromUser);
            be.addChildElement("fromUserProvider", "urn").addTextNode(fromUserProvider);
            be.addChildElement("toUser", "urn").addTextNode(toUser);
            be.addChildElement("toUserProvider", "urn").addTextNode(toUserProvider);
            be.addChildElement("subject", "urn").addTextNode(subject);
            be.addChildElement("message", "urn").addTextNode(message);
//            be.addChildElement("ticket", "urn").addTextNode(ticket);
//            be.addChildElement("action", "urn").addTextNode(action);
//            be.addChildElement("message", "urn").addTextNode(message);
//            be.addChildElement("patientID", "urn").addTextNode(patientId);

            // Save message
            msg.saveChanges();

            // View input
            //System.out.println("\n Soap request:\n");
            //msg.writeTo(System.out);

            // Send
            // String urlval = "http://208.75.163.61:8080/TaskManager/TaskManagerService";
            String urlval = getProperty("EmailSendEndpoint");
            SOAPMessage reply = conn.call(msg, urlval);


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
            ret=xmlSource;
            //System.out.println("mail test: "+ret);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
      //assertTrue(ret.indexOf("<detail>Success</detail>")>0);
      if (ret.indexOf("<detail>Success</detail>")>0)
          return true;
      return false;
    }


}
