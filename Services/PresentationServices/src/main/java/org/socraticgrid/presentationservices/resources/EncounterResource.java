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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
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
public class EncounterResource extends BaseResource
{

    public EncounterResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();

            //      System.out.println("query: "+query);
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
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {
        String result = makeSOAPCall();

        //String result = "Unimplemented Resource";
        Representation representation = new StringRepresentation(result,
                MediaType.APPLICATION_JSON);

        return representation;
    }

    private String makeSOAPCall() throws ResourceException
    {

        String retVal = "";

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
            env.addNamespaceDeclaration("SOAP-ENV",
                "http://schemas.xmlsoap.org/soap/envelop/");
            env.addNamespaceDeclaration("SOAP-ENC",
                "http://schemas.xmlsoap.org/soap/encoding/");
            env.addNamespaceDeclaration("xsd",
                "http://www.w3.org/2001/XMLSchema");
            env.addNamespaceDeclaration("xsi",
                "http://www.w3.org/2001/XMLSchema-instance");

            SOAPBody body = env.getBody();

            // Populate body

            SOAPElement be = body.addChildElement(env.createName(
                        "FindEncounters_PRPA_IN900300UV02Request", "v3",
                        "urn:hl7-org:v3"));
            be.addChildElement("localDeviceId", "v3").addTextNode("1.1");
            be.addChildElement("senderOID", "v3").addTextNode("1.1");
            be.addChildElement("receiverOID", "v3").addTextNode(
                "2.16.840.1.113883.3.198");

            SOAPElement query = be.addChildElement("query", "v3");
            query.addChildElement("id", "v3").addAttribute(new QName(
                    "extension"), "6858a017-39c1-4153-bbd4-eaedac72a0e7");
            query.addChildElement("creationTime", "v3").addAttribute(new QName(
                    "value"), "20091221011010.005");
            query.addChildElement("interactionId", "v3").addAttribute(new QName(
                    "extension"), "PRPA_IN900300UV02");
            query.addChildElement("processingCode", "v3").addAttribute(
                new QName("code"), "P");
            query.addChildElement("processingModeCode", "v3").addAttribute(
                new QName("code"), "R");
            query.addChildElement("acceptAckCode", "v3").addAttribute(new QName(
                    "code"), "AL");

            SOAPElement receiver = query.addChildElement("receiver", "v3")
                .addAttribute(new QName("typeCode"), "RCV");

            SOAPElement receiverDevice = receiver.addChildElement("device",
                    "v3").addAttribute(new QName("determinerCode"), "INSTANCE");
            receiverDevice.addChildElement("id", "v3").addAttribute(new QName(
                    "root"), "2.16.840.1.113883.3.200");
            receiverDevice.addChildElement("telecom", "v3").addAttribute(
                new QName("value"),
                "http://localhost:8080/NhinConnect/CommonDataLayerService");

            SOAPElement sender = query.addChildElement("sender", "v3")
                .addAttribute(new QName("typeCode"), "SND");

            SOAPElement senderDevice = sender.addChildElement("device", "v3")
                .addAttribute(new QName("determinerCode"), "INSTANCE");
            senderDevice.addChildElement("id", "v3").addAttribute(new QName(
                    "root"), "2.16.840.1.113883.3.200");

            SOAPElement controlActProcess = query.addChildElement(
                    "controlActProcess", "v3").addAttribute(new QName(
                        "classCode"), "CACT").addAttribute(new QName(
                        "moodCode"), "EVN");
            controlActProcess.addChildElement("code", "v3").addAttribute(
                new QName("code"), "PRPA_TE900300UV02");
            //controlActProcess.addChildElement("priorityCode", "v3").addAttribute(new QName("code"), "R");

            SOAPElement queryByParameter = controlActProcess.addChildElement(
                    "queryByParameterPayload", "v3");
            queryByParameter.addChildElement("queryId", "v3").addAttribute(
                new QName("extension"), "2c748172-7cc2-4902-8af0-23A105c4401b");
            queryByParameter.addChildElement("statusCode", "v3").addAttribute(
                new QName("code"), "new");

            SOAPElement encounterStatus = queryByParameter.addChildElement(
                    "encounterStatus", "v3");
            encounterStatus.addChildElement("value", "v3").addAttribute(
                new QName("code"), "active");

            SOAPElement encounterTimeframe = queryByParameter.addChildElement(
                    "encounterTimeframe", "v3");
            SOAPElement value = encounterTimeframe.addChildElement("value",
                    "v3");
            value.addChildElement("low", "v3").addAttribute(new QName("value"),
                "20091001");
            value.addChildElement("high", "v3").addAttribute(new QName("value"),
                "20091030");

            SOAPElement patientId = queryByParameter.addChildElement(
                    "patientId", "v3");
            patientId.addChildElement("value", "v3").addAttribute(new QName(
                    "extension"), "D1234");

            SOAPElement responsibleOrganization =
                queryByParameter.addChildElement("responsibleOrganization",
                    "v3");
            responsibleOrganization.addChildElement("value", "v3").addAttribute(
                new QName("extension"), "CLINIC-A");

            SOAPElement typeOfEncounter = queryByParameter.addChildElement(
                    "typeOfEncounter", "v3");
            typeOfEncounter.addChildElement("value", "v3").addAttribute(
                new QName("extension"), "AMB");


            // Save message
            msg.saveChanges();

            // View input
            //  System.out.println("\n Soap request:\n");
            //  msg.writeTo(System.out);

            // Send
            //String urlval = "http://nhindev06.asu.edu:8080/DataAccessService/DataAccessService";
            String urlval = getProperty("SOAPEndpoint");
            SOAPMessage reply = conn.call(msg, urlval);


            // View the output
            //  System.out.println("\nXML response\n");

            // Create transformer
            TransformerFactory tff = TransformerFactory.newInstance();
            Transformer tf = tff.newTransformer();

            // Get reply content
            Source sc = reply.getSOAPPart().getContent();
            Writer outWriter = new StringWriter();

            // Set output transformation
            //StreamResult result = new StreamResult(System.out);
            StreamResult result = new StreamResult(outWriter);

            //transfrom to get the xml
            tf.transform(sc, result);

            //close connection
            conn.close();

            String xmlSource = outWriter.toString();

//            xmlSource = xmlSource.replace("<telecom use=\"HP\" value=\"tel:+1-999-999-9999\"/>", "<telecom><homePhone>tel:+1-999-999-9999</homePhone></telecom>");

            //transform into json
//            if (sc==null)
//                System.out.println("context null");
//
//             URL u = super.getClass().getClassLoader().getResource("/WEB-INF/patientServicesJson.xsl");
//              //URL u = sc.getClass().getResource("/WEB-INF/patientServicesJson.xsl");
//            if (u==null)
//                System.out.println("u null");
//
//              System.out.println("u: "+u.toExternalForm());
//            InputStream is  = super.getClass().getClassLoader().getResourceAsStream("/WEB-INF/patientServicesJson.xsl");
            //URL u = sc.getClass().getClassLoader().getResource("patientServicesJson.xsl");
            //System.out.println("u: "+ u.toString());
            //File file = new File(u.getFile());
            //System.out.println(sc.getClass().getResource(urlval) +"/WEB-INF/patientServicesJson.xsl");
//            if (!file.isFile())
//                System.out.println("file not good: "+ file.getPath());
            Templates template = tff.newTemplates(new StreamSource(
                        new FileInputStream(
                            new File(
                                "/home/nhin/KMR/patientServicesJson.xsl"))));
            tf = template.newTransformer();

            StreamSource streamSource = new StreamSource(new StringReader(
                        xmlSource));

            Writer xml = new StringWriter();
            StreamResult json = new StreamResult(xml);

            // Apply the xsl file to the source file and write the result to the output file
            tf.transform(streamSource, json);


            retVal = xml.toString();


        }

//        catch (FileNotFoundException e) { retVal = "fileNotFound \n" + e.getMessage();
//                    }
        catch (TransformerConfigurationException e)
        {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"transformerConfigEx: " + e.getMessage(),e);
        }
        catch (TransformerException e)
        {

            // An error occurred while applying the XSL file
            // Get location of error in input file
            retVal = "transformerException \n" + e.getMessage();

            SourceLocator locator = e.getLocator();
            int col = locator.getColumnNumber();
            int line = locator.getLineNumber();
            String publicId = locator.getPublicId();
            String systemId = locator.getSystemId();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"TransformerException: " + e.getMessage(),e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //retVal = e.getMessage();
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,"Error during SOAP call",e);
        }

        return retVal;


    }

    //TODO: refactor abobe calll into one that makes call and one that trnasforms
//    private String transformToJSON(String s){
//
//    }
}
