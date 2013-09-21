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
package org.socraticgrid.taskmanager;

import org.socraticgrid.displayalert.DeliverMsgUtil;
import org.socraticgrid.dsa.DeliverMessageRequestType;
import org.socraticgrid.dsa.DeliverMessageResponseType;
import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.service.TaskService;
import java.util.List;
import javax.xml.ws.BindingProvider;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;

/**
 *
 * @author nhin
 */
public class DeliverMsgHandler {

    public static final Long SERVICE_TASK_ID = 3L;

    public DeliverMsgHandler() {
    }

    public org.socraticgrid.dsa.DeliverMessageResponseType deliverMessage(DeliverMessageRequestType request) {

        DeliverMessageResponseType response = new DeliverMessageResponseType();

        for (Object o : request.getType()) {
            String type = (String) o;
            
            if (type.contains("ALERT")) {
                response = new DeliverMsgUtil().deliverMsg(request, "/home/nhin/Properties");
                
                //---------------------------------------------
                // FOR PUSH support
                // WHEN Alert creation has been successful, 
                // CALL Inbox push messaging service with 
                //     patient ID ==> patientId=request.getSubject().get(0)
                
                // the msging service will push all the Alerts to 
                // any client sessions regitstered with that patientId.
                //---------------------------------------------
                if (response.getStatus().equalsIgnoreCase("success")) {
                    
                    //----------------------------------------
                    // USE TOMCAT SOAP WS TO PUSH ALERTS
                    //
                    //----------------------------------------
                    
                    String patientId = request.getSubject().get(0);
                    
                    System.out.println("===> TaskExecHandler.deliverMsg(): New Alert created for patientId="
                            + patientId
                            + " . PUSHING all Alerts to all clients, registered with this patient");
                    
                    //----------------------------------------
                    // PREP and SEND REQUEST to get Alerts as JSON
                    //----------------------------------------
                    GetMessagesRequestType inboxRequest = new GetMessagesRequestType();
                    inboxRequest.setMessageType("Alert");
                    inboxRequest.setPatientId(patientId);
                    inboxRequest.setUserId(request.getMainRecipients().get(0));
                    inboxRequest.setLocation("INBOX");

                    DisplayAlertDataUtil alertUtil = new DisplayAlertDataUtil();
                    String jsonALert = alertUtil.getAllAlertsAsJSON(inboxRequest);

                    String msg = "ALERTS="+patientId+","+jsonALert;
                    System.out.println("===> TaskExecHandler.deliverMsg(): PREPARED ALERTS\n"+msg);
                    
                    //----------------------------------------
                    // Push Alerts to Client
                    //----------------------------------------
                    // OPTION 1: VIA internal WebSocket Client ..... NO GOOD
//                    PushSessionMgr client = new PushSessionMgr();
//                    try {
//                        client.pushMsg(msg);
//                        
//                    } catch (Exception ex) {
//                        Logger.getLogger(DeliverMsgHandler.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    // OPTION 2: VIA SOAP InboxPushClientWeb .....
                    // TODO
                    
                    try { // Call Web Service Operation
                        org.socraticgrid.inbox.MsgPush service = new org.socraticgrid.inbox.MsgPush();
                        org.socraticgrid.inbox.InboxPushPortType port = service.getInboxPushPortSoap11();
                        
                        //SET the endpoint uri of the webservice
                        String uri = PropertyHelper.getPropertyHelper().getProperty("InboxPushClientWeb");

                        ((BindingProvider) port).getRequestContext().put(
                                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                                uri);
                        
                        // initialize WS operation arguments 
                        java.lang.String sendMessageRequest = msg;
                        
                        // process result 
                        java.lang.String result = port.sendMessage(sendMessageRequest);
                        
                        System.out.println("Result = "+result);
                        
                    } catch (Exception ex) {
                         System.out.println("ERROR: "+ ex.getMessage());
                         ex.printStackTrace();
                    }
 
                    
                    
                }
            }
            
            if (type.contains("SMS")) {
                SwiftSMSHandler sms = new SwiftSMSHandler();

                String taskTicket = request.getRefId();
                List<String> mobiles = request.getMainRecipients();
                String msg = request.getBody();

                TaskService service = new TaskService();
                TaskServiceRef ref = service.getServiceRef(SERVICE_TASK_ID);

                try {
                    sms.sendSMSMessage(taskTicket, mobiles, msg, ref);
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus("Failure: " + e.getMessage());
                    return response;
                }
                response.setStatus("Success");
            }
        }
        return response;
    }
}
