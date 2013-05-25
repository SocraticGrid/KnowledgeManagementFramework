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
import org.socraticgrid.common.dda.GetMessageDetailRequestType;
import org.socraticgrid.common.dda.GetMessageDetailResponseType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.GetMessagesResponseType.GetMessageResponse;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.SetMessageResponseType;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.ContactAcctDTO;
import org.socraticgrid.ldapaccess.LdapService;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jharby
 */
public class DisplayAlertMessages {

    public static final String ACTION_UPDATE = "Update";
    public static final String ACTION_ARCHIVE = "Archive";
    public static final String FOLDER_STARRED = "Starred";
    public static final String ARCHIVE_STATUS = "Archive";
    public static final String ACTION_UNARCHIVE = "Unarchive";
    public static final String ACTION_DELETE = "Delete";
    public static final String ACTION_UNDELETE = "Undelete";
    public static final String ACTION_READ = "Read";
    public static final String ACTION_UNREAD = "Unread";
    private static Log log = LogFactory.getLog(DisplayAlertDataUtil.class);

    /**
     * setMessage()
     * LOGIC:
     * 
     * @param 
     * @param request
     *          .action= Read, Send, Delete, Archive, Update (labels only)
     *                   Save, Unread, Undelete, Unarchive, PermanentlyDelete
     *                   Note: for reply/reply all/forwarding, "Send" will be used.
     *          .types = "Email",  "Alert", or "Document"
     *          .location = "Inbox", "Sent", "Draft", "Archived", or "UserTrash"
     *                      Not required for New messages.

     * @return
     */
    public SetMessageResponseType setMessage(SetMessageRequestType request)
    {
        String dbgHdr = "===> DisplayAlertMessages.setMessage(): ";
        log.debug("DisplayAlertMessages:setMessage(): ");

        SetMessageResponseType response = new SetMessageResponseType();

//        String[] actionArr = {ACTION_UPDATE, ACTION_ARCHIVE, ACTION_DELETE,
//            ACTION_UNARCHIVE, ACTION_UNDELETE};
//        List<String> actionList = Arrays.asList(actionArr);


        log.info("Set Message");

        try {

            AlertService service = new AlertService();
            AlertTicket ticket = new AlertTicket();

            //Set Action
            if ((request.getAction() != null) && !request.getAction().isEmpty()) {
                log.info("====> setMessage: Action=" + request.getAction());

                //Checking READ and UNREAD actions setting
                if (request.getAction().equalsIgnoreCase(ACTION_READ)
                        || request.getAction().equalsIgnoreCase(ACTION_UNREAD)) {
                    response = setMsgRead(request);
                    return response;
                }
                else if ( request.getAction().equalsIgnoreCase(ACTION_UPDATE)
                        ||request.getAction().equalsIgnoreCase(ACTION_ARCHIVE)
                        ||request.getAction().equalsIgnoreCase(ACTION_DELETE)
                        ||request.getAction().equalsIgnoreCase(ACTION_UNARCHIVE)
                        ||request.getAction().equalsIgnoreCase(ACTION_UNDELETE))
                {

                    ticket = service.getTicket(Long.valueOf(request.getMessageId()));
                    if (ticket != null) {
                        log.info("Found " + ticket.getTicketId() + " id ticket found for user: "
                                + request.getUserId());
                    }
                    else {
                        response.setSuccessStatus(false);
                        response.setMessage("No ticket found for user: " + request.getUserId());
                        return response;
                    }

                    boolean foundStatus = false;
                    boolean foundFolder = false;

                    if (!ticket.getStatus().isEmpty()) {
                        for (AlertStatus status : ticket.getStatus()) {

                            if (status.getUserId().equals(request.getUserId())) {
                                foundStatus = true;
                                if (!CommonUtil.listNullorEmpty(request.getLabels())) {
                                    foundFolder = true;
                                    String label = request.getLabels().get(0);
                                    if (label == null || label.trim().isEmpty()) {
                                        status.setFlagged(false);
                                    }
                                    else if (label.equalsIgnoreCase(FOLDER_STARRED)) {
                                        status.setFlagged(true);
                                    }
                                }

                                if (request.getAction().equals(ACTION_ARCHIVE)) {
                                    status.setArchive(true);
                                }

                                if (request.getAction().equals(ACTION_UNARCHIVE)) {
                                    status.setArchive(false);
                                }

                                if (request.getAction().contains(ACTION_DELETE)) {
                                    status.setDeleted(true);
                                }

                                if (request.getAction().contains(ACTION_UNDELETE)) {
                                    status.setDeleted(false);
                                }

                            }
                        }
                    }
                    if (foundFolder || foundStatus) {
                        service.saveTicket(ticket);
                    }
                }
            }

            response.setMessage("Message Updated Successfully");
            response.setSuccessStatus(true);
        } catch (Exception e) {
            //     log.error("Error setting the message: " + request.getUserId(), e);
            response.setMessage(e.getMessage());
            response.setSuccessStatus(false);
        }
        return response;
    }

    /**
     * getMessages(GetMessagesRequestType request)
     *
     * LOGIC:
     * If coming from Patient Inbox:
     *          Find ALL alerts that have request.userId listed as AlertContact.
     *
     * If coming from Provider Inbox:
     *          Find ALL alerts that have request.userId listed as AlertContact.
     *
     * If coming from EMR Inbox:
     *          Find ALL alerts that have request.userid listed as AlertContact
     *          AND request.patientId == alertticket.ptunitnumber.
     *
     * @param request GetMessageRequestType - Request type defined in WS schema
     * @param request.userId - Unique Id of the user in session.
     * @param request.patientId - Unique Id of the patient's whose msgs are 
     *                            being requested.  If patientId is not given,
     *                            the msgs of the userId will be retrieved.
     * @param request.location - the folder of msgs being requested 
     *                           ( Inbox", "Sent", "Draft", "Archived", 
     *                           "UserTrash", "AdminTrash" ).
     * @param request.type - The type of msgs bing requested:  
     *                              "Email",  "Alert", "Document" 
     * @return GetMessagesResponseType
     */
    public GetMessagesResponseType getMessages(GetMessagesRequestType request)
    {
        String dbgHdr = "===> DisplayAlertMessages: ";
        //System.out.println(dbgHdr+" - FIND ALERTS .........\n");

        GetMessagesResponseType totalResponse = new GetMessagesResponseType();


        //==================================================================
        //LOGIC:
        // 1. Get ALL tickets.
        //
        // 2. As necessary, Reduce to tickets ABOUT request.patientId.
        //    IF request.patiendId is given (this means being called from EMR),
        //    then only pull tickets with ptunitnumber == request.patiendId.
        //
        // 3. Further filter tickets to ONLY tickets with request.userId
        //    listed as a recipient.
        //       alertContact.userid == request.userid
        //
        // 4. As necessary, Reduce further to tickets with correct request.location.
        //
        //       IF (   [(request.location == Archive) AND (ticket.alertStatus.Archive == true)]
        //           OR [(request.location == UserTrash) AND (ticket.alertStatus.Deleted == true)]
        //           OR [(NO request.location is given) AND (ticket IS NOT archived) AND (ticket IS NOT deleted)]
        //          )
        //             Count this ticket for response.
        //       ELSE
        //             Do NOT count this ticket for response
        //       END IF
        //==================================================================
        try {
            AlertService service = new AlertService();

            //-------------------------------------------------
            // IF (userId AND patientId are both given)
            // THEN GET the userId's employeeType atrib fromm ldap.
            //      IF (userId AND patientId are both given) AND the employeeType="administrator"
            //      THEN pass along flag to ONLY get the msgs about the patientId (CDS).
            //      ENDIF
            // ENDIF
            //-------------------------------------------------
            ContactDTO contactDto = findContactByUID(request.getUserId());
            String userRole = contactDto.getEmployeeType();

            //----------------------------------------------------
            // 1. Get ALL tickets.
            //----------------------------------------------------

            List<AlertTicket> ticketList = null;

// TMN - CHECK SLOW PERFORMANCE ISSUE HERE:
            
//TMN: have to debug why filter select doesn't work before using.
//            if (CommonUtil.strNullorEmpty(request.getPatientId())) {
                ticketList = service.getAllTickets();

//            } else {
//                TicketQueryParams filters = new TicketQueryParams();
//                filters.setPatientId(request.getPatientId());
//                ticketList = service.getTicketsByParams(filters);
//            }

            //.........................................................
            // DBG: incoming filter attributes
            System.out.println(dbgHdr + "GIVEN ALERT FILTERS: "
                    + "/userId=" + request.getUserId() + "/patientId=" + request.getPatientId()
                    + "/location=" + request.getLocation());
            System.out.append(dbgHdr +  "and implied filter:  /userRole=" + userRole);
            if (ticketList != null) System.out.println(dbgHdr+"Total pre-filtered ticketList="+ticketList.size() );
            System.out.println(dbgHdr+"Tickets selected:");
            //.........................................................
            
            for (AlertTicket ticket : ticketList) {
                
                //----------------------------------------------------
                // 2. As necessary, Reduce to tickets ABOUT request.patientId.
                //    Skip this ticket if given Request.patientId <> ticket.ptunitnumber
                //----------------------------------------------------
                if (   !CommonUtil.strNullorEmpty(request.getPatientId())
                    && !ticket.getPatientUnitNumber().equals(request.getPatientId()) )
                {
                    continue;
                }

                boolean isArchived = this.checkArchive(ticket, request.getUserId());
                boolean isDeleted = this.checkDeleted(ticket, request.getUserId());

                //.........................................................
                // DBG: ticket state
                //System.out.print(dbgHdr+"Processing ticketId="+ticket.getTicketId()+"/ptUnitNumber=" +ticket.getPatientUnitNumber());

                //if (isArchived) System.out.print("/Archived"); else System.out.print("/NOT Archived");
                //if (isDeleted) System.out.print("/Deleted"); else System.out.print("/NOT Deleted");
                //.........................................................

                //----------------------------------------------------
                // 3. CHOOSE ticket IF request.userId listed as a recipient of
                //    this ticket.  Get ALL recipients(contacts) for
                //    this alert.
                //    THEN only continue further checks IF
                //         (this recipient == request.userId)
                //      OR (request userId has userRole == "administrator"
                //----------------------------------------------------
                Set<AlertContact> tContacts = ticket.getProviders();
                Iterator<AlertContact> contactIter = tContacts.iterator();

                while (contactIter.hasNext()) {
                    AlertContact alertContact = contactIter.next();

                    //System.out.println(" ...... TIX:contact= "+ alertContact.getUserId()+" request.getUserId()="+request.getUserId());

                     //FOUND userId as a recipient of this ticket...continue checks..
                    if (   alertContact.getUserId().equals(request.getUserId())
                        || ((userRole != null) && (userRole.equalsIgnoreCase("administrator")))
                        ) {
                        //----------------------------------------------------
                        // 4. As necessary, filter further for tickets with
                        //    correct request.location . Else, include the ticket.
                        //----------------------------------------------------
                        //----------------------------------------------------
                        // return this alert only if
                        //     1) asking for Archive AND this alert is archived.
                        // OR  2) asking for UserTrash AND this alert is Deleted.
                        // OR  3) asking for Inbox AND this alert is (NOT archived) AND (NOT Deleted).
                        // OR  4) asking for NO location AND this alert is (NOT archived) AND (NOT Deleted).
                        //----------------------------------------------------
                        boolean noLocationRequested = CommonUtil.strNullorEmpty(request.getLocation());

                        if (  (!noLocationRequested && request.getLocation().equalsIgnoreCase("Archive") && isArchived)
                            ||(!noLocationRequested && request.getLocation().equalsIgnoreCase("UserTrash") && isDeleted)
                            ||(!noLocationRequested && request.getLocation().equalsIgnoreCase("INBOX") && !isArchived && !isDeleted)
                            ||(noLocationRequested && !isArchived && !isDeleted) )
                        {
                            GetMessageResponse gmr =
                                    createAlertResponse(ticket, alertContact,
                                                        isArchived, request.getLocation());

                            totalResponse.getGetMessageResponse().add(gmr);

                            //System.out.println(dbgHdr+"  ADDING Alert="+ticket.getTicketId() + " location="+request.getLocation());

                            break;
                        }
//                        else {
//                            System.out.println("     req.location="+request.getLocation()
//                                    +" NOT SAME as ticket condition.");
//                        }

                    }
                }//end-while
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalResponse;

/* TMN
        // LDAP lookup of ContactDTO to find if pt or provider
        try {
            contactDto = findContactByUID(contactDAO, request.getUserId());
        } catch (Exception e) {
            response.setSuccessStatus(false);
            response.setStatusMessage(e.getMessage());
            totalResponse.getGetMessageResponse().add(response);
            return totalResponse;
        }

        if (contactDto.getEmployeeNumber() != null) {
            type = "pv";
        }
        else {
            type = "pt";
        }

        try {
            AlertService service = new AlertService();

            List<AlertTicket> ticketList = service.getAllTickets();

            // Case where patient id is sent, find messages for provider for this
            // patient (where the ptUnitNumber in the alertticket table equals
            // the patientId).
            if (request.getPatientId() != null && !CommonUtil.listNullorEmpty(ticketList)) {

                //
                for (AlertTicket ticket : ticketList) {
                    boolean isArch = checkArchive(ticket, request);
                    if (!isArch && request.getLocation().equals("Archive")) {
                        continue;
                    }
                    if (checkDeleted(ticket, request)
                            && !request.getLocation().equals("UserTrash")) {
                        continue;
                    }

                    if (ticket.getPatientUnitNumber().equals(request.getPatientId())) {
                        Set<AlertContact> tContacts = ticket.getProviders();
                        Iterator<AlertContact> iter = tContacts.iterator();
                        while (iter.hasNext()) {
                            AlertContact alertContact = iter.next();
                            if (alertContact.getUserId().equals(request.getUserId())) {
                                if (request.getLocation().equalsIgnoreCase("Archive") && isArch) {
                                    GetMessageResponse gmr = createAlertResponse(
                                            ticket, alertContact,
                                            isArch, request.getLocation());
                                    if (gmr.getLocation().equals(request.getLocation())) {
                                        totalResponse.getGetMessageResponse().add(gmr);
                                        break;
                                    }
                                }
                                else {
                                    break;
                                }
                                totalResponse.getGetMessageResponse().add(createAlertResponse(
                                        ticket, alertContact, isArch, request.getLocation()));
                                break;
                            }
                        }
                    }
                }
                return totalResponse;
            }
            else {
                List<GetMessagesResponseType.GetMessageResponse> responseList =
                        new ArrayList<GetMessagesResponseType.GetMessageResponse>();
                if (!CommonUtil.listNullorEmpty(ticketList)) {
                    // Case patientId is null, retrieve all tickets for userId (this
                    // could be a patient or a provider)
                    for (AlertTicket ticket : ticketList) {
                        boolean isArch = checkArchive(ticket, request);
                        if (!isArch && request.getLocation().equals("Archive")) {
                            continue;
                        }
                        if (checkDeleted(ticket, request)) {
                            continue;
                        }
                        if (type.equals("pt")
                                && ticket.getPatientUnitNumber().equals(request.getUserId())) {
                            Set<AlertContact> ticketContacts = ticket.getProviders();
                            for (AlertContact alertContact : ticketContacts) {
                                if (alertContact.getUserId().equals(request.getUserId())) {
                                    responseList.add(createAlertResponse(ticket, alertContact,
                                            isArch, request.getLocation()));
                                }
                            }
                        }
                        else if (type.equals("pv")) {
                            // for provider their userId will be in contact table
                            Set<AlertContact> tContacts = ticket.getProviders();
                            Iterator<AlertContact> iter = tContacts.iterator();
                            while (iter.hasNext()) {
                                AlertContact alertContact = iter.next();

                                if (alertContact.getUserId().equals(request.getUserId())) {
                                    GetMessageResponse gmr = createAlertResponse(
                                            ticket, alertContact, isArch, request.getLocation());

                                    if (gmr.getLocation().equals(request.getLocation())) {
                                        totalResponse.getGetMessageResponse().add(gmr);
                                    }
                                }
                            }
                        }
                    }

                    totalResponse.getGetMessageResponse().addAll(responseList);
                }
                else {
                    GetMessagesResponseType.GetMessageResponse rsp =
                            new GetMessagesResponseType.GetMessageResponse();
                    rsp.setSuccessStatus(false);
                    rsp.setStatusMessage("No alert tickets found for id " + request.getUserId());
                    responseList.add(rsp);
                    totalResponse.getGetMessageResponse().addAll(responseList);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalResponse;
*/
    }

    private boolean checkDeleted(AlertTicket ticket, String userId) throws Exception {

        for (AlertStatus as : ticket.getStatus()) {
            if (as.getUserId().equals(userId)) {
                return as.isDeleted();
            }
        }
        return false;
    }

    private boolean checkArchive(AlertTicket ticket, String userId) throws Exception {
        for (AlertStatus as : ticket.getStatus()) {
            if (as.getUserId().equals(userId)) {
                return as.isArchive();
            }
        }
        return false;
    }

    private boolean checkRead(AlertTicket ticket, String userId) {
        boolean value = false;
        Date recentDate = null;

        for (AlertAction alertAction : ticket.getActionHistory()) {
            if (recentDate == null
                    || alertAction.getActionTimestamp().compareTo(recentDate) > 0) {
                recentDate = alertAction.getActionTimestamp();
            }

            // If most recent alert action is Notfication generated then
            // set return value to false
            if (recentDate.compareTo(alertAction.getActionTimestamp()) > 0) {

                if (alertAction.getMessage().equals("Notfication generated")
                        && alertAction.getUserId().equals(userId)) {
                    value = false;
                }

            }

            // If alert action message is not Notification generated then
            // set return value to true
            value = (!alertAction.getMessage().equals("Notification generated")
                    && alertAction.getUserId().equals(userId));


            if (value) {
                break;
            }
        }

        return value;
    }

    /**
     * Create one of the responses to be returned by getMessages
     * 
     * 
     * @param ticket AlertTicket
     * @param contact AlertContact record corresponding to ticket
     * @param isArch is the ticket archived or not
     * @param location location passed in request
     * @return response object to be added to return
     * @throws DatatypeConfigurationException 
     */
    private GetMessageResponse createAlertResponse( AlertTicket ticket
                                                   ,AlertContact contact
                                                   ,boolean isArch
                                                   ,String location)
    throws DatatypeConfigurationException
    {
        GetMessagesResponseType.GetMessageResponse response =
                new GetMessagesResponseType.GetMessageResponse();

        response.setMessageType("Alert");
        
        //--------------------------
        // Set ARCHIVED attribute
        //--------------------------
        if (isArch) {
            response.setLocation("Archive");
        }
        else {
            response.setLocation(location);
        }

        //--------------------------
        // Set READ/UNREAD attribute
        //--------------------------
        if (checkRead(ticket, contact.getUserId())) {
            response.setMessageStatus("Read");
        }
        else {
            response.setMessageStatus("Unread");
        }

        //System.out.println("TICKET IS: " + ticket.toString());
        System.out.println("===> TICKET ID: " + ticket.getTicketId()
                          +"     DESC: " + ticket.getDescription());


        //--------------------------
        response.setDescription(ticket.getDescription());
        response.setFrom(contact.getUserName());
        response.setTitle(ticket.getDescription());
        //--------------------------
        // Temporarily setting task % from DSA in ptFMPSSN column
        //--------------------------
        if (!CommonUtil.strNullorEmpty(ticket.getPtFMPSSN())) {
            try {
            response.setTasksComplete(Integer.parseInt(ticket.getPtFMPSSN()));
            } catch (NumberFormatException nfe) {
                response.setTasksComplete(0);
            }
        }
        
        //--------------------------
        // Set MSG DATE
        //--------------------------
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(ticket.getAlertTimestamp());
        XMLGregorianCalendar xgcDate =
                DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        response.setMessageDate(xgcDate);

        //--------------------------
        String msgId = ticket.getTicketId().toString();
        response.setMessageId(msgId);

        //--------------------------
        // Check the starred/flagged label in the status table
        //--------------------------
        for (AlertStatus status : ticket.getStatus()) {
            if (!CommonUtil.strNullorEmpty(contact.getUserId())) {
                if (contact.getUserId().equals(status.getUserId()) && status.isFlagged()) {
                    response.getLabels().add("Starred");
                }
            }
        }
        //--------------------------
        response.setPriority(ticket.getPriority());
        response.setSuccessStatus(true);
        response.setFrom(ticket.getAlertOriginator());
        //response.setTasksComplete(ticket.getActionHistory().size());
        response.setTasksCount(ticket.getActionHistory().size());

        return response;
    }

    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType request) {

        log.debug("getMessageDetail");

        GetMessageDetailResponseType response =
                new GetMessageDetailResponseType();

        List<String> responseList = response.getMessageDetail();
        try {
            AlertService service = new AlertService();
            AlertTicket ticket =
                    service.getTicket(Long.parseLong(request.getMessageId()));

            if (ticket == null) {
                response.setSuccessStatus(false);
                response.setStatusMessage("The ticket was not found for id = "
                        + ticket.getTicketId());
                return response;
            }

            response.setPatientId(ticket.getPatientUnitNumber());
            response.getMessageDetail().add(ticket.getPayload());

            // Set the AlertAction to read ???
            // Decided at this point for GUI to call setMessage instead
//            AlertAction action = new AlertAction();
//            action.setActionName("Alert");
//            action.setMessage(ACTION_READ);
//            action.setTicket(ticket);
//            action.setActionTimestamp(new Date());
//            action.setUserId(request.getUserId());
//            setProvidersInAction(ticket, action);
//
//            ticket.getActionHistory().add(action);
//            service.saveTicket(ticket);


        } catch (Exception e) {
            response.setSuccessStatus(false);
            response.setStatusMessage("Get message detail failed: " + e.getMessage());
            e.printStackTrace();
        }
        response.setSuccessStatus(true);
        return response;
    }

    private SetMessageResponseType setMsgRead(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();
        AlertService service = new AlertService();
        AlertTicket ticket = new AlertTicket();

        if (!CommonUtil.strNullorEmpty(request.getUserId())
                && !CommonUtil.strNullorEmpty(request.getMessageId())) {
            ticket = service.getTicket(Long.valueOf(request.getMessageId()));
        }

        if (ticket == null) {
            response.setSuccessStatus(false);
            response.setMessage("No alerts found: USERID: "
                    + request.getUserId() + ", MESSAGEID: " + request.getMessageId());
        }
        AlertAction action = new AlertAction();
        action.setActionName("Alert");
        action.setActionTimestamp(new Date());
        action.setTicket(ticket);
        if (request.getAction().equalsIgnoreCase(ACTION_READ)) {
            action.setMessage(ACTION_READ);
        }
        else if (request.getAction().equalsIgnoreCase(ACTION_UNREAD)) {
            action.setMessage(ACTION_UNREAD);
        }
        action.setUserId(request.getUserId());
        setProvidersInAction(ticket, action);

        ticket.getActionHistory().add(action);
        service.saveTicket(ticket);
        response.setSuccessStatus(true);
        return response;
    }

    private void setProvidersInAction(AlertTicket ticket, AlertAction action) {
        if (CommonUtil.setNullorEmpty(ticket.getProviders())) {
            action.setUserProvider(Boolean.TRUE);
        }

        for (AlertContact provider : ticket.getProviders()) {
            if (provider.getUserLdap() != null
                    && ticket.getTicketId().equals(provider.getTicket().getTicketId())) {
                action.setUserProvider(Boolean.TRUE);
            }
            else {
                action.setUserProvider(Boolean.FALSE);
            }
        }
    }

    private ContactDTO findContactByUID(String userId) throws Exception {
        List<ContactDTO> allContacts = LdapService.getContactDAO().findContact("uid=" + userId);

        if (allContacts.isEmpty()) {
            throw new Exception("No contact found for userId: " + userId);
        }
        return allContacts.get(0);
    }
}
