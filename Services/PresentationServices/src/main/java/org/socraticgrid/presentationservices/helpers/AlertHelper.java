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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.socraticgrid.presentationservices.helpers;
import java.io.StringWriter;
import java.io.Writer;


import org.socraticgrid.alertmanager.*;
import org.socraticgrid.common.task.*;

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
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;






/**
 *
 * @author Jerry Goodnough
 */
public class AlertHelper {

    private static AlertHelper singleton = new AlertHelper();

    public static AlertHelper getAlertHelper()
    {
        return singleton;
    }
    
    
    /*
    UpdateAlertRequestType(java.lang.String ticket, java.lang.String action, java.lang.String userID, java.lang.String userName, boolean userProvider, java.lang.String message, gov.hhs.fha.nhinc.client.alertmanagerservice.NameValuePair[] actionParamaters) {
    */
    /**
     * DOCUMENT ME!
     *
     * @param   ticket     DOCUMENT ME!
     * @param   action     DOCUMENT ME!
     * @param   message    DOCUMENT ME!
     * @param   patientId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String OLDupdateAlert(String ticket, String action, String message,
        String patientId)
    {
        String ret = "";
        System.out.println("ticket: " + ticket);

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
            env.addNamespaceDeclaration("urn",
                "urn:gov:hhs:fha:nhinc:common:task");


            SOAPHeader header = env.getHeader();

            SOAPBody body = env.getBody();

            // Populate body
            SOAPElement be = body.addChildElement("UpdateAlertRequest", "urn");
            be.addChildElement("ticket", "urn").addTextNode(ticket);
            be.addChildElement("action", "urn").addTextNode(action);
            be.addChildElement("message", "urn").addTextNode(message);
            be.addChildElement("userID", "urn").addTextNode(patientId);

            // Save message
            msg.saveChanges();

            // View input
            //System.out.println("\n Soap request:\n");
            //msg.writeTo(System.out);

            // Send
            String urlval = PropertyHelper.getPropertyHelper().getProperty("AlertManagerService");
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
            ret = xmlSource;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return ret;
    }


    public CallStatus updateAlert(String ticket, String action, String message,
        String userId, boolean userProvider)
    {
        CallStatus out = new CallStatus();

        try { // Call Web Service Operation
            AlertManager service = new AlertManager();
            AlertManagerPortType port = service.getAlertManagerPortSoap11();
            // TODO initialize WS operation arguments here
            String endpoint = PropertyHelper.getPropertyHelper().getProperty("AlertManagerService");
            //Use the BOS Endpoint
            if ((endpoint != null)&&(!endpoint.isEmpty()))
            {
                    ((BindingProvider) port).getRequestContext()
                .put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    endpoint);
            }
            UpdateAlertRequestType updateAlertRequest = new UpdateAlertRequestType();
            updateAlertRequest.setAction(action);
            updateAlertRequest.setUserID(userId);
            //Update to handle different user types here
            updateAlertRequest.setUserProvider(userProvider);
            updateAlertRequest.setMessage(message);
            updateAlertRequest.setTicket(ticket);
            updateAlertRequest.setUserName("");
              
            UpdateAlertResponseType result = port.updateAlert(updateAlertRequest);


            out.setStatusCode(result.getStatusCode());
            out.setStatusDetail(result.getStatusDetail());
            out.setError(false);
        } catch (Exception ex) {
            // TODO handle custom exceptions here
            out.setError(true);
            out.setStatusCode(999);
            out.setStatusDetail(ex.getMessage());
        }
        return out;
    }


}
