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


import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
public class VitalsResource extends BaseResource
{

    private String patientId;
    private String fromDate = "20090101";
    private String toDate = "20110630";

    private int version = 2;

    private String apiKey;
    private String jsonRequestString;

    public VitalsResource(Context context, Request request, Response response)
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

        apiKey = request.getResourceRef().getQueryAsForm().getFirstValue(
                "apiKey");
        patientId = this.getParameter(request, "patientId", "");
        fromDate = this.getDateParameter(request, "fromDate", "Patient", "Vitals", "Begin");
        toDate = this.getDateParameter(request, "toDate", "Patient", "Vitals", "End");

    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        if (version > 1)
        {
            jsonRequestString =
                "{\"getVitalsFacts\":{\"criteria\":{\"queryId\":\"20100411011010\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"QUPC_IN043100UV\",\"triggerEventCode\":\"QUPC_TE043100UV01\",\"careRecordPayload\":{\"patientId\":\"" +
                patientId +
                "\",\"careProvisionCode\":\"COBSCAT\",\"careRecordStartTimePeriod\":\"" +
                fromDate + "\",\"careRecordEndTimePeriod\":\"" + toDate +
                "\"}}}}";

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
                    Representation representation = new StringRepresentation(
                            response.getEntity().getText(),
                            MediaType.APPLICATION_JSON);

                    return representation;
                }
                else
                {
                    Logger.getLogger(ProcedureResource.class.getName()).log(
                        Level.SEVERE, null,
                        response.getStatus().getDescription());
                    throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                        "Error during call to JSONPojoFactServiceEndpoint: " +
                        response.getStatus().getDescription());
                }
            }
            catch (ResourceException e )
            {
                throw e;
            }
            catch (Exception e)
            {
                Logger.getLogger(ProcedureResource.class.getName()).log(
                    Level.SEVERE, null, e);
                throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                    "Error during call to JSONPojoFactServiceEndpoint", e);
            }
        }
        else
        {
            //Old code

            if (checkApiKey(apiKey))
            {
                String result = makeSOAPCall();

                Representation representation = new StringRepresentation(result,
                        MediaType.APPLICATION_JSON);

                return representation;
            }

            return new StringRepresentation("APIKey invalid",
                    MediaType.APPLICATION_JSON);
        }
    }

    private String makeSOAPCall()
    {

        String retVal = "";

        try
        {

            // Create the connection
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
                        "CareRecord_QUPC_IN043100UV01VitalsRequest", "m",
                        "urn:hl7-org:v3"));
            be.addChildElement("localDeviceId", "m").addTextNode("1.1");
            be.addChildElement("senderOID", "m").addTextNode("1.1");
            be.addChildElement("receiverOID", "m").addTextNode(
                "2.16.840.1.113883.3.198");

            SOAPElement query = be.addChildElement("query", "m");
            query.addChildElement("id", "m").addAttribute(new QName(
                    "extension"), "20090920011010.005");
            query.addChildElement("creationTime", "m").addAttribute(new QName(
                    "value"), "20090920011010.005");
            query.addChildElement("interactionId", "m").addAttribute(new QName(
                    "extension"), "QUPC_IN043100UV").addAttribute(new QName(
                    "root"), "2.16.840.1.113883.5");
            query.addChildElement("processingCode", "m");
            query.addChildElement("processingModeCode", "m");
            query.addChildElement("acceptAckCode", "m");

            SOAPElement receiver = query.addChildElement("receiver", "m")
                .addAttribute(new QName("typeCode"), "RCV");

            SOAPElement receiverDevice = receiver.addChildElement("device", "m")
                .addAttribute(new QName("classCode"), "DEV").addAttribute(
                    new QName("determinerCode"), "INSTANCE");
            receiverDevice.addChildElement("id", "m").addAttribute(new QName(
                    "extension"), "Common Data Layer Service");

            SOAPElement sender = query.addChildElement("sender", "m")
                .addAttribute(new QName("typeCode"), "SND");

            SOAPElement senderDevice = sender.addChildElement("device", "m")
                .addAttribute(new QName("classCode"), "DEV").addAttribute(
                    new QName("determinerCode"), "INSTANCE");
            senderDevice.addChildElement("id", "m").addAttribute(new QName(
                    "extension"), "Adapter Assembly Service");

            SOAPElement controlActProcess = query.addChildElement(
                    "controlActProcess", "m").addAttribute(new QName(
                        "classCode"), "CACT").addAttribute(new QName(
                        "moodCode"), "EVN");
            controlActProcess.addChildElement("code", "m").addAttribute(
                new QName("code"), "QUPC_TE043100UV01");
            controlActProcess.addChildElement("priorityCode", "m").addAttribute(
                new QName("code"), "R");

            SOAPElement queryByParameter = controlActProcess.addChildElement(
                    "queryByParameter", "m");
            queryByParameter.addChildElement("queryId", "m").addAttribute(
                new QName("extension"), "20090920010010");
            queryByParameter.addChildElement("statusCode", "m").addAttribute(
                new QName("code"), "new");
            queryByParameter.addChildElement("responsePriorityCode", "m")
                .addAttribute(new QName("code"), "I");

            SOAPElement parameterList = queryByParameter.addChildElement(
                    "parameterList", "m");
            SOAPElement careProvisionCode = parameterList.addChildElement(
                    "careProvisionCode", "m");
            careProvisionCode.addChildElement("value", "m").addAttribute(
                new QName("code"), "CONSCAT").addAttribute(new QName(
                    "codeSystem"), "1.3.5.1.4.1.19376.1.5.3.2").addAttribute(
                new QName("codeSystemName"), "IHEActCode");

            SOAPElement careRecordTimePeriod = parameterList.addChildElement(
                    "careRecordTimePeriod", "m");
            SOAPElement careRecordTimePeriodValue =
                careRecordTimePeriod.addChildElement("value", "m");
            careRecordTimePeriodValue.addChildElement("low", "m").addAttribute(
                new QName("value"), "20090101");
            careRecordTimePeriodValue.addChildElement("high", "m").addAttribute(
                new QName("value"), "20090630");

            SOAPElement patientIdentification = parameterList.addChildElement(
                    "patientId", "m");
            patientIdentification.addChildElement("value", "m").addAttribute(
                new QName("root"), "2.16.840.1.113883.3.198").addAttribute(
                new QName("extension"), "8237363");


            // Save message
            msg.saveChanges();

            // View input
//              System.out.println("\n START Soap request:\n");
//              msg.writeTo(System.out);
//                System.out.println("\n END Soap request:\n");

            // Send
            //String urlval = getProperty("SOAPEndpoint");
            String urlval =
                "http://nhinint03.asu.edu:8080/CommonDataLayerService/AdapterCommonDataLayer";
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

            String xmlSource = replaceNodes(outWriter.toString(), MEDICATIONS);
            //System.out.println(xmlSource);

            //transform into json
            Templates template = tff.newTemplates(new StreamSource(
                        getXslFile()));
            tf = template.newTransformer();

            StreamSource streamSource = new StreamSource(new StringReader(
                        xmlSource));

            Writer xml = new StringWriter();
            StreamResult json = new StreamResult(xml);

            // Apply the xsl file to the source file and write the result to the output file
            tf.transform(streamSource, json);


            retVal = xml.toString();


        }
        catch (TransformerConfigurationException e)
        {
            retVal = "transformerConfigEx \n" + e.getMessage();
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
            retVal = e.getMessage();
        }

        return retVal;
    }
}
