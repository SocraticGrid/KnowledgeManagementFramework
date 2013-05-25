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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handle "Mail" task events.  Send mail message via IMAP server.
 *
 * @author cmatser
 */
public class MailHandler {

    /** LDAP attribute for provider's user id. */
    public static final String LDAP_PROVIDER_ID_ATT = "employeeNumber";

    /** LDAP attribute for patient's user id. */
    public static final String LDAP_PATIENT_ID_ATT = "uid";

    /** X-Mailer header. */
    public static final String X_MAILER = "KMR Mailer";

    /** Logging. */
    private static Log log = LogFactory.getLog(MailHandler.class);

    /**
     * Process task.
     */
    public void handleMessage(String taskTicket, TaskServiceRef serviceRef, TaskMessage taskMessage, TaskType task) {
        TaskMailMessage mailMessage = null;

        log.debug("Handling Mail task.");

        if (taskMessage instanceof TaskMailMessage) {
            mailMessage = (TaskMailMessage) taskMessage;
        }

        if (mailMessage == null) {
            log.error("Error, invalid mail task for ticket: " + taskTicket);
            return;
        }

        boolean result = sendMail(serviceRef.getLocation(), mailMessage.getFromUser(),
            mailMessage.isFromUserProvider(), mailMessage.getToUser(),
            mailMessage.isToUserProvider(), mailMessage.getSubject(),
            mailMessage.getMessage());

        log.debug("Task ticket: " + taskTicket
            + ". Mail result: " + (result?"success.":"failed."));
    }

    private boolean sendMail(String host, String fromUser, boolean isFromUserProvider,
            String toUser, boolean isToUserProvider, String subject, String text) {
        boolean retVal = true;

        try {
            String fromAddr = getEmailAddr(isFromUserProvider, fromUser);
            String toAddr = getEmailAddr(isToUserProvider, toUser);

            //Get session
            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            Session session = Session.getInstance(props);

            //Create messages
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromAddr));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toAddr));
            msg.setSubject(subject);
            msg.setText(text);
            msg.setHeader("X-Mailer", X_MAILER);
            msg.setSentDate(new Date());

            // send the thing off
            Transport.send(msg);

            log.debug("Mail was sent successfully.");
        }
        catch (Exception e) {
            log.error("Error sending mail.", e);
            retVal = false;
        }

        return retVal;
    }

    /**
     * Use LDAP to get email address
     *
     * @param user ldap lookup
     * @return
     */
    private String getEmailAddr(boolean isProvider, String lookup) {
        String email = null;

        //If the lookup field appears to be a number, use it
        //  as a userId to search LDAP
        lookup = lookup.trim();
        if (lookup.length() > 0) {
            boolean isNumber = true;
            for (int i = 0; i < lookup.length(); i++) {
                if (!Character.isDigit(lookup.charAt(i))) {
                    isNumber = false;
                    break;
                }
            }

            if (isNumber) {
                lookup = (isProvider ? LDAP_PROVIDER_ID_ATT : LDAP_PATIENT_ID_ATT) + lookup;
            }
        }

        //Search LDAP
        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact(lookup);
        if (contacts.size() > 0) {
            email = contacts.get(0).getMail();
        }

        return email;
    }

}
