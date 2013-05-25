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

import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.model.TaskType;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handle a new task event to send a SMS message using the Swift SMS provider.
 *
 * @author cmatser
 */
public class SwiftSMSHandler {

    /** Logging. */
    private static Log log = LogFactory.getLog(SwiftSMSHandler.class);

    /** Max length of a single SMS message. */
    public static final int SMS_MSG_LENGTH = 159;

    /** Max number of SMS messages sent per task. Longer messages are truncated. */
    public static final int SMS_MSG_COUNT = 2;

    /**
     * Process task.
     */
    public void handleMessage(String taskTicket, TaskServiceRef serviceRef, TaskMessage taskMessage, TaskType task) {
        List<TaskContact> providers;
        List<String> cellNumbers = new LinkedList<String>();
        String messageBody;
        StringBuilder errMsg = new StringBuilder();

        log.debug("Handling SMS task.");

        try {
            //Pull out providers
            TaskRuleMessage ruleMessage = (TaskRuleMessage) taskMessage;
            if (ruleMessage.getProviders().isEmpty()) {
                throw new Exception("Providers not found for SMS task.");
            }

            //Pull cell numbers
            for (TaskContact provider : ruleMessage.getProviders()) {

                //Check for role
                if ((provider.getProvider() == null) || provider.getProvider().isEmpty()) {
                    String role = provider.getRole();
                    String location = provider.getLocation();

                    //Find based on role, location
                    List<String> providerLdaps = TaskManagerUtil.retrieveProviderLdaps(role, location);
                    for (String ldap : providerLdaps) {
                        String mobile = retrieveCellNumber(ldap);
                        if (mobile != null) {
                            //Don't add duplicates
                            if (!cellNumbers.contains(mobile)) {
                                cellNumbers.add(mobile);
                            }
                        }
                        else {
                            errMsg.append("Mobile number not found for provider: ");
                            errMsg.append(ldap);
                            errMsg.append(".\n");
                        }
                    }

                    //Check if provider was found
                    if (providerLdaps.isEmpty()) {
                        errMsg.append("Provider not found for role: ");
                        errMsg.append(role);
                        errMsg.append(", ");
                        errMsg.append(location);
                        errMsg.append(".\n");
                    }
                }
                else {
                    //Get cell to dial
                    String mobile = retrieveCellNumber(provider.getProvider());
                    if (mobile != null) {
                        //Don't add duplicates
                        if (!cellNumbers.contains(mobile)) {
                            cellNumbers.add(mobile);
                        }
                    }
                    else {
                        errMsg.append("Mobile number not found for provider: ");
                        errMsg.append(provider.getProvider());
                        errMsg.append(".\n");
                    }
                }
            }

            //Ensure message is not too long to avoid excessive fees
            //  Messages longer than 160 characters are broken up into multiple
            //  SMS messages.
            messageBody = ruleMessage.getMessage();
            if (messageBody.length() > (SMS_MSG_LENGTH * SMS_MSG_COUNT)) {
                messageBody = messageBody.substring(0, SMS_MSG_LENGTH * SMS_MSG_COUNT);
            }

            //Send SMS
            sendSMSMessage(taskTicket, cellNumbers, ruleMessage.getMessage(), serviceRef);

        } catch (Exception e) {
            log.error("Error handling task ticket: " + taskTicket
                + ". Error sending SMS message.", e);
        }

    }

    /**
     * Send the message to the list of numbers.
     * 
     * @param taskTicket
     * @param mobiles
     * @param msg
     * @param serviceRef
     * @throws java.lang.Exception
     */
    public void sendSMSMessage(String taskTicket, List<String> mobiles,
        String msg, TaskServiceRef serviceRef)
            throws Exception {

        List<String> results = new LinkedList<String>();
        boolean success = true;

        // Call Web Service Operation
        ca.smsgateway.sendsms.SendSMS service = new ca.smsgateway.sendsms.SendSMS();
        ca.smsgateway.sendsms.SendSMSSoap port = service.getSendSMSSoap12();
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
            javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
            serviceRef.getLocation());

        //Swift account pin
        String accountKey = serviceRef.getServiceParam1();

        //Ensure message is not too long to avoid excessive fees
        //  Messages longer than 160 characters are broken up into multiple
        //  SMS messages.
        if (msg.length() > (SMS_MSG_LENGTH * SMS_MSG_COUNT)) {
            msg = msg.substring(0, SMS_MSG_LENGTH * SMS_MSG_COUNT);
        }

        //Send SMS
        for (String mobile : mobiles) {
            String result = port.sendMessage(mobile, msg, accountKey);

            if (!result.contains("success")) {
                success = false;
            }

            results.add("Number: " + mobile + ", result: " + result);
            log.debug(results.get(results.size()-1));
        }

        if (!success) {
            StringBuilder errMsg = new StringBuilder();
            errMsg.append("Error handling task ticket: ");
            errMsg.append(taskTicket);
            errMsg.append(". Results:");
            for (String s : results) {
                errMsg.append("\n");
                errMsg.append(s);
            }
            log.error(errMsg.toString());
        }
        else {
            log.debug("Task ticket: " + taskTicket + ". SMS result: success.");
        }

    }

    /**
     * Use LDAP to get cell number.
     * 
     * @param provider
     * @return
     */
    private String retrieveCellNumber(String provider) {
        String cell = null;

        //If the provider field appears to be a phone number, use it
        //  otherwise search LDAP
        provider = provider.trim();
        if ((provider.length() == 10) || (provider.length() == 11)) {
            boolean isNumber = true;
            for (int i = 0; i < provider.length(); i++) {
                if (!Character.isDigit(provider.charAt(i))) {
                    isNumber = false;
                    break;
                }
            }

            if (isNumber) {
                cell = provider;
            }
        }

        //If we have not determined the number, search LDAP
        if (cell == null) {
            ContactDAO contactDAO = LdapService.getContactDAO();
            List<ContactDTO> contacts = contactDAO.findContact(provider);
            if (contacts.size() > 0) {
                cell = contacts.get(0).getMobile();
            }
        }

        return cell;
    }

}
