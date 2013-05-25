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
package org.socraticgrid.displayalert;

import org.socraticgrid.alertmanager.model.AlertAction;
import org.socraticgrid.alertmanager.model.AlertContact;
import org.socraticgrid.alertmanager.model.AlertStatus;
import org.socraticgrid.alertmanager.model.AlertTicket;
import org.socraticgrid.alertmanager.service.AlertService;
import org.socraticgrid.dsa.DeliverMessageRequestType;
import org.socraticgrid.dsa.DeliverMessageResponseType;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nhin
 */
public class DeliverMsgUtil {

    public DeliverMessageResponseType deliverMsg(DeliverMessageRequestType request,
            String propDir) {
        boolean update = false;
        DeliverMessageResponseType response = new DeliverMessageResponseType();
        String patientId = request.getSubject().get(0);
        ContactDAO contactDAO = null;

        if (propDir != null) {
            contactDAO = LdapService.getContactDAO(propDir);
        }
        else {
            contactDAO = LdapService.getContactDAO();
        }

        List<ContactDTO> ldapList = contactDAO.findContact("uid=" + patientId);
        ContactDTO contact = ldapList.get(0);
        String name = ldapList.get(0).getCommonName();
        if (ldapList.size() < 1) {
            response.setStatus("PtUnitNumber not found in LDAP");
            return response;
        }

        AlertService service = new AlertService();

        List<AlertTicket> tickets = service.getAllTickets();
        AlertTicket ticket = new AlertTicket();
        for (AlertTicket tik : tickets) {
            if (tik.getTicketUniqueId().equals(request.getRefId())) {
                update = true;
                ticket = tik;
                break;
            }
        }
        
        ticket.setPatientName(contact.getDisplayName());
        ticket.setPatientSex(contact.getGender());
        
        if (!CommonUtil.strNullorEmpty(request.getStatus())) {
            ticket.setPtFMPSSN(request.getStatus());
        }

        if (!CommonUtil.strNullorEmpty(request.getBody())) {
            ticket.setPayload(request.getBody());
        }

        ticket.setTicketUniqueId(request.getRefId());

        if (!CommonUtil.strNullorEmpty(request.getHeader())) {
            ticket.setDescription(request.getHeader());
        }

        if (!CommonUtil.strNullorEmpty(request.getPriority())) {
            ticket.setPriority(request.getPriority());
        }
        else {
            ticket.setPriority("Low");
        }
        ticket.setAlertOriginator("Clinical Decision");

        ticket.setPatientUnitNumber(patientId);
//        Date date = null;
//        try {
//            date = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss",
//                    Locale.ENGLISH).parse(request.getDeliveryDate());
//        } catch (ParseException ex) {
//            Logger.getLogger(DisplayAlertDataUtil.class.getName()).log(Level.SEVERE, null, ex);
//            response.setStatus("Error in date input: " + ex.toString());
//        }
        ticket.setAlertTimestamp(new Date());

        ticket.setEscalationPeriod(0);
        ticket.setType("MedAlerts");
        ticket.setAlertId(new Long(8));

        if (!update) {
            List<String> all = new ArrayList();
            all.addAll(request.getMainRecipients());
            all.addAll(request.getSecondaryRecipients());
            all.addAll(request.getHiddenRecipients());
            for (String recip : all) {
                List<ContactDTO> rList = contactDAO.findContact("uid=" + recip);
                if (CommonUtil.listNullorEmpty(rList)) {
                    response.setStatus("No contact found for uid=" + recip);
                    return response;
                }
                ContactDTO ctc = rList.get(0);
                insertContact(ctc.getUid(), ctc.getDisplayName(), ticket);
                insertStatus(ctc.getUid(), ticket);
            }
        }


        AlertAction action = new AlertAction();
        action.setActionName("Alert");
        if (!update) {
            action.setMessage("Notification generated");
        }
        else {
            action.setMessage("Update");
        }
        action.setUserId(patientId);
        action.setTicket(ticket);
        if (contact.getEmployeeNumber() != null) {
            action.setUserProvider(Boolean.TRUE);
        }
        else {
            action.setUserProvider(Boolean.FALSE);
        }
        action.setActionTimestamp(new Date());
        ticket.getActionHistory().add(action);

        service.saveTicket(ticket);
        response.setStatus("success");
        return response;

    }

    private void insertContact(String ptId, String name, AlertTicket ticket) {
        AlertContact alertContact = new AlertContact();
        alertContact.setUserId(ptId);
        alertContact.setMethod("Alert");
        alertContact.setUserName(name);
        alertContact.setTicket(ticket);
        ticket.getProviders().add(alertContact);
    }

    private void insertStatus(String ptId, AlertTicket ticket) {
        AlertStatus status = new AlertStatus();
        status.setUserId(ptId);
        status.setArchive(false);
        status.setFlagged(false);
        status.setCreateTimeStamp(new Date());
        status.setTicket(ticket);
        status.setDeleted(false);
        ticket.getStatus().add(status);
    }
}
