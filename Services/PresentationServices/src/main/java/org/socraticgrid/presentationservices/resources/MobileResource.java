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

import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;

import javax.xml.soap.SOAPHeader;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import javax.xml.transform.Templates;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
public class MobileResource extends BaseResource {

    private String action="";
    private String dataSource="";
    private String providerId="";
    private String patientId="";
    private String groupId="";
    private String locationId="";
    private String onlyNew="";
    private String itemId="";
    private String ticket="";
    private String message="";

    private String staticResponse="[{\"itemId\":\"ID:6-127.0.0.1(a2:f3:38:99:5e:e1)-1-1277309586497\",\"dataSource\":\"MedAlerts\",\"author\":\"1\",\"patient\":\"BARBARA FAYE SMITH\",\"description\":\"UC#2: PLT GT 40 and LE 80\",\"dateCreated\":\"2010-06-23T09:13:06.000-07:00\",\"Priority\":\"true\"},{\"itemId\":\"ID:11-127.0.0.1(a2:f3:38:99:5e:e1)-1-1277310816792\",\"dataSource\":\"MedAlerts\",\"author\":\"1\",\"patient\":\"BARBARA FAYE SMITH\",\"description\":\"UC#4: PLT LE 80 percent of 5pt average\",\"dateCreated\":\"2010-06-23T09:33:37.000-07:00\",\"Priority\":\"true\"}]";

    public MobileResource(Context context, Request request, Response response) {
        super(context, request, response);


       try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            System.out.println("query: "+query);
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


        try {
            String a = request.getResourceRef().getQueryAsForm().getFirstValue("action");
            String d = request.getResourceRef().getQueryAsForm().getFirstValue("dataSource");
            //if (dataSource!=null) dataSource = URLDecoder.decode(dataSource, "UTF-8");
            String pro= request.getResourceRef().getQueryAsForm().getFirstValue("providerId");
            String pat = request.getResourceRef().getQueryAsForm().getFirstValue("patientId");
            String g = request.getResourceRef().getQueryAsForm().getFirstValue("groupId");
            String l = request.getResourceRef().getQueryAsForm().getFirstValue("locationId");
            String i = request.getResourceRef().getQueryAsForm().getFirstValue("itemId");
            //if (action!=null) itemId = URLDecoder.decode(itemId, "UTF-8");
            String onN = request.getResourceRef().getQueryAsForm().getFirstValue("onlyNew");
            String t =request.getResourceRef().getQueryAsForm().getFirstValue("ticket");
            String m =request.getResourceRef().getQueryAsForm().getFirstValue("mesage");
            if (a!=null) action=a;
            if (d!=null) dataSource=URLDecoder.decode(d, "UTF-8");
            if (pro!=null) providerId=pro;
            if (pat!=null) patientId=pat;
            if (onN!=null) onlyNew=onN;
            if (g!=null) groupId=g;
            if (l!=null) locationId=l;
            if (i!=null) itemId=i;
            if (t!=null) ticket=URLDecoder.decode(t, "UTF-8");
            if (m!=null) message=m;
            //if (a!=null) action=a;

        } catch (Exception e) {
            e.printStackTrace();
        }

        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
    }
    @Override
    public boolean allowPut() {
        return true;
    }
    /**
     * handle GET Returns a full representation for a given variant.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String result = "";
        if (action.equalsIgnoreCase("summary")){
            result = this.getSummaryData(dataSource, groupId, patientId, groupId, groupId, result);
        }
        else if (action.equalsIgnoreCase("detail")){
            result = transformToJson(this.getDetailData(dataSource, itemId));
        }
        else if (action.equalsIgnoreCase("patientAlerts")){
            result = transformToJson(this.getPatientAlerts(patientId));
        }
        else if (message!=null){
            result = transformToJson(this.updateAlert(ticket, action, message, patientId));
        }
        else
            result = "{\"mail\":\"status\":\"not implemented\"}";

        Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;

    }

    /**
     * Handle PUT requests.  replace or update resource
     */
    @Override
    public void storeRepresentation(Representation entity) throws ResourceException {
        String  result = this.updateAlert(ticket, action, message, patientId);

        if (result.indexOf("Success")>-1)
            getResponse().setStatus(Status.SUCCESS_NO_CONTENT);
        else
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
    }

    //handle POST requests.  create resource
    @Override
    public void acceptRepresentation(Representation  entity) throws ResourceException{

        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * Handle DELETE requests.  remove/delete resource
     */
    @Override
    public void removeRepresentations() throws ResourceException {

        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    public String getSummaryData(String ds,String provId, String patId, String grpId, String locId, String newOnly){
        String ret="";
        ret="Alert 1:Drug Interaction:P1|Alert 2:Appointment Confirmed:I1";
/*
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
            env.addNamespaceDeclaration("urn", "urn:gov:hhs:fha:nhinc:common:dda");

            SOAPHeader header = env.getHeader();

            SOAPBody body = env.getBody();

            // Populate body
            SOAPElement be = body.addChildElement("GetSummaryDataForUserRequest","urn");
            be.addChildElement("dataSources", "urn").addTextNode(ds);
            if (provId!=null && provId.length()>0){
                be.addChildElement("providerId", "urn").addTextNode(provId);
                be.addChildElement("userId", "urn").addTextNode(provId);
            }
            if (patId!=null && patId.length()>0)
                be.addChildElement("patientId", "urn").addTextNode(patId);
            if (grpId!=null && grpId.length()>0)
                be.addChildElement("groupId", "urn").addTextNode(grpId);
            if (locId!=null && locId.length()>0)
                be.addChildElement("locationId", "urn").addTextNode(locId);

            //added new or all switch for us
            be.addChildElement("onlyNew", "urn").addTextNode(newOnly);

            // Save message
            msg.saveChanges();
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);

            // Send
            //String urlval = "http://208.75.163.61:8080/DisplayDataAggregator/DisplayDataAggregatorService";
            String urlval = getProperty("DisplayDataAggregatorService");
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

            //String xmlSource = outWriter.toString();

            //ret=xmlSource;
            String json = this.transformToJson(outWriter.toString());
            return json;

        }
        catch (Exception e) {
            e.printStackTrace();
            ret =e.getMessage();
        }
*/
        return ret;
    }

    public String getDetailData(String ds,String itemId){
        String ret="";
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
            env.addNamespaceDeclaration("urn", "urn:gov:hhs:fha:nhinc:common:dda");


            SOAPHeader header = env.getHeader();

            SOAPBody body = env.getBody();

            // Populate body
            SOAPElement be = body.addChildElement("GetDetailDataRequest","urn");
            be.addChildElement("dataSource", "urn").addTextNode(ds);
            be.addChildElement("itemId", "urn").addTextNode(itemId);

            // Save message
            msg.saveChanges();

            // View input
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);

            // Send
            String urlval = getProperty("DisplayDataAggregatorService");
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

        }
        catch (Exception e) {
            e.printStackTrace();
            ret =e.getMessage();
        }
        return ret;
    }
    public String updateAlert(String ticket,String action, String message, String patientId){
        String ret="";
        System.out.println("ticket: "+ticket);
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
            SOAPElement be = body.addChildElement("UpdateAlertRequest","urn");
            be.addChildElement("ticket", "urn").addTextNode(ticket);
            be.addChildElement("action", "urn").addTextNode(action);
            be.addChildElement("message", "urn").addTextNode(message);
            be.addChildElement("patientID", "urn").addTextNode(patientId);

            // Save message
            msg.saveChanges();

            // View input
            //System.out.println("\n Soap request:\n");
            //msg.writeTo(System.out);

            // Send
             String urlval = getProperty("AlertManagerService");
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

        }
        catch (Exception e) {
            e.printStackTrace();
        }
       return ret;
    }
    private String transformToJson(String s){
        if (s==null || s.length()<1)
            return s;

        //transform
        try {
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            Templates template = tff.newTemplates(new StreamSource(new FileInputStream(new File("/home/nhin/KMR/patientServicesJson.xsl"))));
            tf = template.newTransformer();

            StreamSource streamSource = new StreamSource(new StringReader(s));

            Writer xml = new StringWriter();
            StreamResult json = new StreamResult(xml);

            // Apply the xsl file to the source file and write the result to the output file
            tf.transform(streamSource, json);
            return xml.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }
    public String getPatientAlerts(String patientId){
        String ret="";
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
            env.addNamespaceDeclaration("urn", "urn:gov:hhs:fha:nhinc:common:dda");


            SOAPHeader header = env.getHeader();

            SOAPBody body = env.getBody();

            // Populate body
            SOAPElement be = body.addChildElement("GetSummaryDataRequest","urn");
            be.addChildElement("dataSources", "urn").addTextNode("Patient Alerts");
            //be.addChildElement("dataSources", "urn").addTextNode("MedAlerts - mobile");
            be.addChildElement("providerId", "urn").addTextNode("");
            be.addChildElement("patientId", "urn").addTextNode(patientId);
            be.addChildElement("groupId", "urn").addTextNode("");
            be.addChildElement("locationId", "urn").addTextNode("");
            // Save message
            msg.saveChanges();

            // View input
            System.out.println("\n Soap request:\n");
            msg.writeTo(System.out);

            // Send
            String urlval = getProperty("DisplayDataAggregatorService");
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

        }
        catch (Exception e) {
            e.printStackTrace();
            ret =e.getMessage();
        }
        return ret;
    }

}
