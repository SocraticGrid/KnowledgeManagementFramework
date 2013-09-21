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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;
import org.socraticgrid.alertmanager.dao.RiskModelFavoriteDao;
import org.socraticgrid.alertservice.AlertUtil;
import org.socraticgrid.alertmanager.model.AlertAction;
import org.socraticgrid.alertmanager.model.AlertContact;
import org.socraticgrid.alertmanager.model.AlertStatus;
import org.socraticgrid.alertmanager.model.AlertTicket;
import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.alertmanager.model.TicketQueryParams;
import org.socraticgrid.alertmanager.service.ActionConstants;
import org.socraticgrid.alertmanager.service.AlertService;
import org.socraticgrid.alertmanager.service.TicketConstants;
import org.socraticgrid.alertpayload.Action;
import org.socraticgrid.alertpayload.ActionParam;
import org.socraticgrid.alertpayload.Recommendation;
import org.socraticgrid.common.dda.DetailData;
import org.socraticgrid.common.dda.GetComponentDetailDataForUserRequestType;
import org.socraticgrid.common.dda.GetComponentDetailDataResponseType;
import org.socraticgrid.common.dda.GetComponentSummaryDataForUserRequestType;
import org.socraticgrid.common.dda.GetComponentSummaryDataResponseType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.NameValuesPair;
import org.socraticgrid.common.dda.ServiceError;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.SetMessageResponseType;
import org.socraticgrid.common.dda.SummaryData;
import org.socraticgrid.common.dda.UpdateComponentInboxStatusRequestType;
import org.socraticgrid.common.dda.UpdateComponentInboxStatusResponseType;
import org.socraticgrid.common.task.AlertFact;
import org.socraticgrid.common.task.GetAlertFactsResponseType;
import org.socraticgrid.common.task.PatientContext;
import org.socraticgrid.dsa.DeliverMessageRequestType;
import org.socraticgrid.dsa.DeliverMessageResponseType;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.datatype.DatatypeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.socraticgrid.presentationservices.helpers.MessageResponse;

/**
 * Utility to retrieve Alerts (both Alert and Message types).
 *
 * @author cmatser
 */
public class DisplayAlertDataUtil {

    /** Data Source name. */
    public static final String DATA_SOURCE_ALERTS = "MedAlerts";
    public static final String DATA_SOURCE_ALERTS_MOBILE = "MedAlerts - mobile";
    public static final String DATA_SOURCE_PT_ALERTS = "Patient Alerts";
    public static final String DATA_SOURCE_MSGS = "Messages";
    public static final String DATA_SOURCE_ALERT_FACTS = "Alert Facts";
    /** Provider id for patient directed alerts. */
    public static final String PATIENT_ALERTS_PROVIDER_ID = "0";
    /** Id for system user. */
    public static final String SYSTEM_USER_ID = "0";
    /** Status for new alerts. */
    public static final String ALERT_STATUS_NEW = "New";
    /** SetMessage Action Update. */
    public static final String ACTION_UPDATE = "Update";
    /** SetMessage - Starred Folder */
    public static final String FOLDER_STARRED = "Starred";
    /** SetMessage - Archive Status */
    public static final String ARCHIVE_STATUS = "ARCHIVED";
    /** Item names for name value pairs. */
    public static final String ITEM_PRIORITY = "Priority";
    public static final String ITEM_FOLDERS = "Folders";
    public static final String ITEM_STATUS = "Status";
    public static final String ITEM_PATIENT_UNIT_NUMBER = "Patient Unit Number";
    public static final String ITEM_PATIENT_SEX = "Patient Sex";
    public static final String ITEM_PATIENT_FMPSSN = "Patient FMP/SSN";
    public static final String ITEM_FACT_NCID = "Fact NCID";
    public static final String ITEM_FACT_NAME = "Fact Name";
    public static final String ITEM_FACT_VALUE = "Fact Value";
    public static final String ITEM_FACT_TYPE = "Fact Type";
    public static final String ITEM_ACTION_ID = "Action ID";
    public static final String ITEM_ACTION_TYPE = "Action Type";
    public static final String ITEM_RULE_ID = "Rule ID";
    public static final String ITEM_RULE_DESCRIPTION = "Rule Description";
    public static final String ITEM_RULE_NAME = "Rule Name";
    public static final String ITEM_UPDATE_REC_PREFIX = "update.";
    public static final String ITEM_UPDATE_REC_NAME = ".name";
    public static final String ITEM_UPDATE_REC_TIME = ".time";
    public static final String ITEM_UPDATE_REC_USER_ID = ".userId";
    public static final String ITEM_UPDATE_REC_USER_NAME = ".userName";
    public static final String ITEM_UPDATE_REC_USER_PROVIDER = ".userProvider";
    public static final String ITEM_UPDATE_REC_MESSAGE = ".message";
    /** Standard error code. */
    public static final Integer ERR_CODE = -1;
    /** Service error message. */
    public static final String ERR_MSG_ITEM_NOT_FOUND = "Item was not found.";
    /** Logging. */
    private static Log log = LogFactory.getLog(DisplayAlertDataUtil.class);

    /**
     * Update Inbox status.
     *
     * @param request
     * @return
     */
    public UpdateComponentInboxStatusResponseType updateComponentInboxStatus(String source, UpdateComponentInboxStatusRequestType request) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Retrieve detail data.
     *
     * @param request
     * @return
     */
    public GetComponentDetailDataResponseType getComponentDetailDataForUser(String source, GetComponentDetailDataForUserRequestType request) {
        GetComponentDetailDataResponseType response = new GetComponentDetailDataResponseType();

        log.debug("Retrieving " + source + " detail for ticket for user: " + request.getUserId());

        try {
            //Query based on ticket ticket id
            AlertService service = new AlertService();
            TicketQueryParams query = new TicketQueryParams();
            query.setTicketUniqueId(request.getItemId());
            List<AlertTicket> tickets = service.ticketQuery(query);

            if ((tickets == null) || (tickets.isEmpty())) {
                log.debug("Cound not find " + source + " ticket: " + request.getItemId());
                throw new Exception(ERR_MSG_ITEM_NOT_FOUND);
            }

            //Poplulate detail data object
            GregorianCalendar cal = new GregorianCalendar();
            AlertTicket ticket = tickets.get(0);
            DetailData detailData = new DetailData();
            detailData.setAuthor(ticket.getAlertOriginator());
            detailData.setFrom(ticket.getAlertOriginator());
            detailData.setData(sanitizePayload(request.getUserId(), ticket.getPayload(), ticket));
            detailData.setDataSource(source);
            cal.setTime(ticket.getAlertTimestamp());
            detailData.setDateCreated(
                    DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
            detailData.setDescription(ticket.getDescription());
            detailData.setItemId(ticket.getTicketUniqueId());
            detailData.setPatient(ticket.getPatientName());

            //Object specific name/value pairs
            addNameValue(detailData.getItemValues(), ITEM_PRIORITY, ticket.getPriority());
            addNameValue(detailData.getItemValues(), ITEM_PATIENT_UNIT_NUMBER, ticket.getPatientUnitNumber());
            addNameValue(detailData.getItemValues(), ITEM_PATIENT_SEX, ticket.getPatientSex());
            //          addNameValue(detailData.getItemValues(), ITEM_PATIENT_FMPSSN, ticket.getPatientFMPSSN());

            //Go through action history and add to name/value
            //  Also, check if ticket is new for this user
            //  Also, hold onto last action performed by user or system user
            //  Also, hold onto last action
            int i = 1;
            boolean isNewAlert = true;
            AlertAction lastAction = null;
            AlertAction lastUserAction = null;
            for (AlertAction action : ticket.getActionHistory()) {
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_NAME, action.getActionName());
                cal.setTime(action.getActionTimestamp());
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_TIME, DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).toString());
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_ID, action.getUserId());
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_NAME, action.getUserName());
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_PROVIDER, action.getUserProvider().toString());
                addNameValue(detailData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_MESSAGE, action.getMessage());
                i++;

                //Check if ticket is new for this user
                if (SYSTEM_USER_ID.equals(action.getUserId())
                        || ((request.getUserId() != null) && request.getUserId().equals(action.getUserId()))) {

                    if (ActionConstants.ACTION_READ.equals(action.getActionName())) {
                        isNewAlert = false;
                    }

                    lastUserAction = action;
                }

                //Set last action
                lastAction = action;
            }

            //Set appropriate status value
            String status = "";
            if (isNewAlert) {
                //The ticket may be new to this user, but if it is closed and no further
                //  action can be done, then set the status to the last action
                if (AlertUtil.isTickedClosed(ticket)) {
                    status = lastAction.getActionName();
                }
                else {
                    status = ALERT_STATUS_NEW;
                }
            }
            else if (lastUserAction != null) {
                status = lastUserAction.getActionName();
            }
            else if (lastAction != null) {
                status = lastAction.getActionName();
            }
            addNameValue(detailData.getItemValues(), ITEM_STATUS, status);

            response.setDetailObject(detailData);
        } catch (Exception e) {
            log.error("Error retriving detail for alert ticket: " + request.getItemId(), e);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(e.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }

    /**
     * Retreive summary objects for all alerts.
     *
     * @param request
     * @return
     */
    public GetComponentSummaryDataResponseType getComponentSummaryDataForUser(String source, GetComponentSummaryDataForUserRequestType request) {
        GetComponentSummaryDataResponseType response = new GetComponentSummaryDataResponseType();

        log.debug("Retrieving " + source + " summaries for user: " + request.getUserId());

        try {

            if (DATA_SOURCE_PT_ALERTS.equals(source)) {
                //If no patient id is passed, just return
                if ((request.getPatientId() == null) || request.getPatientId().isEmpty()) {
                    return response;
                }
            }
            else { //check for provider
                //If no provider id is passed, just return
                if ((request.getProviderId() == null) || request.getProviderId().isEmpty()) {
                    return response;
                }
            }

            //Find allTickets based on data source
            List<AlertTicket> tickets = findTickets(source, request.getPatientId(), request.getProviderId(), request.isArchive());
            if (tickets == null) {
                throw new Exception("Null ticket query result.");
            }

            log.debug("Found " + tickets.size() + " tickets found for provider: " + request.getProviderId());

            GregorianCalendar cal = new GregorianCalendar();
            List<Long> usedIds = new LinkedList<Long>();
            for (AlertTicket ticket : tickets) {

                //If the ticket has already been added, ignore
                if (usedIds.contains(ticket.getTicketId())) {
                    continue;
                }
                usedIds.add(ticket.getTicketId());

                //Poplulate summary data object
                SummaryData summaryData = new SummaryData();
                summaryData.setAuthor(ticket.getAlertOriginator());
                summaryData.setFrom(ticket.getAlertOriginator());
                summaryData.setDataSource(source);
                cal.setTime(ticket.getAlertTimestamp());
                summaryData.setDateCreated(
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
                summaryData.setDescription(ticket.getDescription());
                summaryData.setItemId(ticket.getTicketUniqueId());
                summaryData.setPatient(ticket.getPatientName());
                Set<AlertStatus> statuses = ticket.getStatus();
                for (AlertStatus status : statuses) {
                    if (status.getTicket().getTicketId().equals(ticket.getTicketId()));
                    if (status.isFlagged()) {
                        summaryData.setFolder("Starred");
                    }
                    else {
                        summaryData.setFolder("");
                    }
                }

                //Object specific name/value pairs
                addNameValue(summaryData.getItemValues(), ITEM_PRIORITY, ticket.getPriority());
                addNameValue(summaryData.getItemValues(), ITEM_FOLDERS, summaryData.getFolder());

                //Go through action history and add to name/value
                //  Also, check if ticket is new for this user
                //  Also, hold onto last action
                int i = 1;
                boolean isNewAlert = true;
                AlertAction lastAction = null;
                for (AlertAction action : ticket.getActionHistory()) {
                    //For mobile, we don't add actions
                    if (!DATA_SOURCE_ALERTS_MOBILE.equals(source)) {
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_NAME, action.getActionName());
                        cal.setTime(action.getActionTimestamp());
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_TIME, DatatypeFactory.newInstance().newXMLGregorianCalendar(cal).toString());
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_ID, action.getUserId());
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_NAME, action.getUserName());
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_USER_PROVIDER, action.getUserProvider().toString());
                        addNameValue(summaryData.getItemValues(), ITEM_UPDATE_REC_PREFIX + i + ITEM_UPDATE_REC_MESSAGE, action.getMessage());
                        i++;
                    }

                    //Check if ticket is new for this user
                    if ((request.getUserId() != null)
                            && request.getUserId().equals(action.getUserId())
                            && ActionConstants.ACTION_READ.equals(action.getActionName())) {
                        isNewAlert = false;
                    }

                    //Set last action
                    lastAction = action;
                }

                //Set appropriate status value
                String status = "";
                if (isNewAlert) {
                    //The ticket may be new to this user, but if it is closed and no further
                    //  action can be done, then set the status to the last action
                    if (AlertUtil.isTickedClosed(ticket)) {
                        status = lastAction.getActionName();
                    }
                    else {
                        status = ALERT_STATUS_NEW;
                    }
                }
                else {
                    status = lastAction.getActionName();
                }
                addNameValue(summaryData.getItemValues(), ITEM_STATUS, status);

                //Check if we are only return new/needing action items
                if (request.isOnlyNew()) {
                    //Items of concern have actions that are still allowed
                    if (actionsAvailable(request.getUserId(), ticket.getPayload(), ticket)) {
                        response.getSummaryObjects().add(summaryData);
                    }
                }
                else {
                    response.getSummaryObjects().add(summaryData);
                }

            }

        } catch (Exception e) {
            log.error("Error retriving summary " + source + " for provider: " + request.getProviderId(), e);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(e.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }

    public org.socraticgrid.common.task.GetAlertFactsResponseType getAlertFacts(org.socraticgrid.common.task.GetAlertFactsRequestType request) {
        GetAlertFactsResponseType response = new GetAlertFactsResponseType();

        log.debug("Retrieving alert facts for user: " + request.getUserId());

        try {

            //If no user id is passed, just return
            if ((request.getUserId() == null) || request.getUserId().isEmpty()) {
                throw new Exception("User id empty.");
            }

            //Find allTickets based on data source
            List<AlertTicket> tickets = null;
            if (request.isUserProvider()) {
                tickets = findTickets(DATA_SOURCE_ALERT_FACTS, null, request.getUserId(), null);
            }
            else {
                tickets = findTickets(DATA_SOURCE_ALERT_FACTS, request.getUserId(), null, null);
            }

            log.debug("Found " + tickets.size() + " tickets found for user: " + request.getUserId());

            GregorianCalendar cal = new GregorianCalendar();
            List<Long> usedIds = new LinkedList<Long>();
            for (AlertTicket ticket : tickets) {

                //If the ticket has already been added, ignore
                if (usedIds.contains(ticket.getTicketId())) {
                    continue;
                }
                usedIds.add(ticket.getTicketId());

                //Poplulate fact object
                AlertFact fact = new AlertFact();
                fact.setTicketId(ticket.getTicketUniqueId());
                cal.setTime(ticket.getAlertTimestamp());
                fact.setDateCreated(
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
                fact.setDescription(ticket.getDescription());
                fact.setPayload(ticket.getPayload());
                fact.setPriority(ticket.getPriority());
                PatientContext ptCtx = new PatientContext();
                ptCtx.setPatientName(ticket.getPatientName());
                //     ptCtx.setPatientFMPSSN(ticket.getPatientFMPSSN());
                ptCtx.setPatientSex(ticket.getPatientSex());
                ptCtx.setPatientUnitNumber(ticket.getPatientUnitNumber());
                fact.setPatient(ptCtx);

                //Go through action history
                //   deterimine status
                boolean isNewAlert = true;
                AlertAction lastAction = null;
                for (AlertAction action : ticket.getActionHistory()) {

                    //Check if ticket is new for this user
                    if ((request.getUserId() != null)
                            && request.getUserId().equals(action.getUserId())
                            && ActionConstants.ACTION_READ.equals(action.getActionName())) {
                        isNewAlert = false;
                    }

                    //Set last action
                    lastAction = action;
                }

                //Set appropriate status value
                String status = "";
                if (isNewAlert) {
                    //The ticket may be new to this user, but if it is closed and no further
                    //  action can be done, then set the status to the last action
                    if (AlertUtil.isTickedClosed(ticket)) {
                        status = lastAction.getActionName();
                    }
                    else {
                        status = ALERT_STATUS_NEW;
                    }
                }
                else {
                    status = lastAction.getActionName();
                }
                fact.setStatus(status);

                //Add to list of facts
                response.getFactObjects().add(fact);
            }

        } catch (Exception e) {
            log.error("Error retriving alert facts for user: " + request.getUserId(), e);
            response.setStatusCode(ERR_CODE);
            response.setStatusDetail(e.getMessage());
        }

        return response;
    }

    /**
     * Find allTickets based on source type.
     * <br>
     * MedAlerts - Find alerts for provider or updated by provider.
     *             If patientId is passed, return only alerts regarding that patient.
     *             otherwise, return all.
     * Pt Alerts - Find alerts specifically directed to the passed patientId.
     *             This is a special case where providerId is zero.
     * Messages  - Find messages for provider or updated by provider.
     *             If patientId is passed, return only messages regarding that patient.
     *             otherwise, return all.
     * @param source
     * @param request
     * @return
     */
    private List<AlertTicket> findTickets(String source, String patientId, String providerId, Boolean archive) {

        //Query based on provider id and patient id, look for both alerts for a provider
        //  or updated by a provider
        AlertService service = new AlertService();
        TicketQueryParams query1 = new TicketQueryParams();

        //Set provider
        if ((providerId != null) && !providerId.isEmpty()) {
            query1.setActionUserId(providerId);
        }

        //Set patient
        if ((patientId != null) && !patientId.isEmpty()) {
            query1.setPatientId(patientId);
        }

        //Set Archive
        if (archive != null) {
            query1.setArchive(archive.booleanValue());
        }

        //Set ticket type
        if (DATA_SOURCE_ALERTS.equals(source)
                || DATA_SOURCE_PT_ALERTS.equals(source)
                || DATA_SOURCE_ALERTS_MOBILE.equals(source)
                || DATA_SOURCE_ALERT_FACTS.equals(source)) {
            query1.setType(TicketConstants.TICKET_TYPE_ALERT);
        }
        else if (DATA_SOURCE_MSGS.equals(source)) {
            query1.setType(TicketConstants.TICKET_TYPE_MESSAGE);
        }

        //Get allTickets
        List<AlertTicket> tickets = service.ticketQuery(query1);

        return tickets;
    }

    private void addNameValue(List<NameValuesPair> pairList, String name, String value) {
        NameValuesPair nameVal = new NameValuesPair();
        nameVal.setName(name);
        nameVal.getValues().add(value);
        pairList.add(nameVal);

        return;
    }

    /**
     * Parses the actions in the payload to ensure the only actions returned
     * are the ones that are allowed.
     * 
     * @param userId
     * @param payload
     * @param alertTicket
     * @return
     */
    private String sanitizePayload(String userId, String payload, AlertTicket alertTicket) {
        Recommendation alertPayload = null;
        String retPayload = payload;

        //Init xml parser
        XStream xstream = new XStream();
        xstream.alias("Recommendation", Recommendation.class);
        xstream.alias("Action", Action.class);
        xstream.alias("ActionParam", ActionParam.class);

        //Check if payload is a recommendation
        try {
            alertPayload = (Recommendation) xstream.fromXML(payload);

            if (alertPayload != null) {
                //Get allowed actions
                TreeSet<Action> allowedActions = new TreeSet<Action>();
                for (Action a : alertPayload.getActions()) {
                    if (AlertUtil.isActionAllowed(userId, a.getName(), alertTicket)) {
                        allowedActions.add(a);
                    }

                }

                //Set allowed actions
                alertPayload.getActions().clear();
                alertPayload.getActions().addAll(allowedActions);

                //Convert back to stream
                retPayload = xstream.toXML(alertPayload);
            }

        } catch (Throwable t) {
            //ignore
        }

        return retPayload;
    }

    /**
     * Checks the payload and sees if any of the configured actions are still available.
     *
     * @param userId
     * @param payload
     * @param alertTicket
     * @return
     */
    private boolean actionsAvailable(String userId, String payload, AlertTicket alertTicket) {
        boolean actionsAvailable = false;

        //Init xml parser
        XStream xstream = new XStream();
        xstream.alias("Recommendation", Recommendation.class);
        xstream.alias("Action", Action.class);
        xstream.alias("ActionParam", ActionParam.class);

        //Check if payload is a recommendation
        try {
            Recommendation alertPayload = (Recommendation) xstream.fromXML(payload);

            if (alertPayload != null) {
                //Get actions and see if any are allowed
                for (Action a : alertPayload.getActions()) {
                    if (AlertUtil.isActionAllowed(userId, a.getName(), alertTicket)) {
                        actionsAvailable = true;
                    }

                }
            }

        } catch (Throwable t) {
            //ignore
        }

        //If there are no configured actions available, let's see if the
        //  user can still peform a read action on the item
        if (!actionsAvailable
                && AlertUtil.isActionAllowed(userId, ActionConstants.ACTION_READ, alertTicket)) {
            actionsAvailable = true;
        }

        return actionsAvailable;
    }

    public SetMessageResponseType setMessage(SetMessageRequestType request) {
        DisplayAlertMessages displayMessages = new DisplayAlertMessages();
        return displayMessages.setMessage(request);
    }

    public GetMessagesResponseType getMessages(GetMessagesRequestType request) {
        DisplayAlertMessages displayMessage = new DisplayAlertMessages();
        return displayMessage.getMessages(request);
    }


    public List<RiskModelFavorite> getRMFByModelId(String modelId) {
        RiskModelFavoriteDao dao = new RiskModelFavoriteDao();
        return dao.findByModelId(modelId);
    }
    public List<RiskModelFavorite> getRMFByProviderPatient(String providerId,
                                                           String patientId) {
        RiskModelFavoriteDao dao = new RiskModelFavoriteDao();
        return dao.findByProviderPatient(providerId, patientId);
    }
    public List<RiskModelFavorite> getAllRMF() {
        RiskModelFavoriteDao dao = new RiskModelFavoriteDao();
        return dao.findAll();
    }

    public void saveRiskModelFavorite(RiskModelFavorite fav) {
        RiskModelFavoriteDao dao = new RiskModelFavoriteDao();
        dao.save(fav);
    }
    
    /**
     * 
     * @param alerts
     * @param location  [INBOX, Archive, ..]
     * @return 
     */
    public String getAllAlertsAsJSON(GetMessagesRequestType request) {
        return prepAllAlertsAsJSON(this.getMessages(request), request.getLocation());
        
    }
        
    public String prepAllAlertsAsJSON(GetMessagesResponseType alerts, String location) {

        //StringBuilder jsonResponse = new StringBuilder();
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        
        StringBuilder sb = new StringBuilder("{\n\"messagesFact\": {\n");
        int i = 1;
        
        List<MessageResponse> msgRespList = new ArrayList<MessageResponse>();
        for (GetMessagesResponseType.GetMessageResponse msgResp : alerts.getGetMessageResponse()) {

            if (i == 1) {
                sb.append("\"successStatus\": " + msgResp.isSuccessStatus() + " ,\n");
                if (!msgResp.isSuccessStatus()) {
                    sb.append("\"statusMessage\": " + msgResp.getStatusMessage() + " ,\n");
                }
                sb.append("\"messageObjects\": [\n");
                if (msgResp.isSuccessStatus() == false) {
                    break;
                }
            }

            if (msgResp.getMessageType() == null || msgResp.getMessageType().equals("")) {
                continue;
            }

            MessageResponse msg = new MessageResponse();
            if (!CommonUtil.strNullorEmpty(msgResp.getMessageId())) {
                msg.setMessageId(msgResp.getMessageId());
            }
            if (CommonUtil.strNullorEmpty(location)) {
                msg.setLocation("INBOX");
            } else {
                msg.setLocation(location);
            }

            if (msgResp.getLabels().contains("Starred")
                    || msgResp.getLabels().contains("starred")) {
                msg.getLabels().add("Starred");
            }

            if (msgResp.getMessageDate() != null) {
                List<String> dateTime =
                        CommonUtil.getDateTime(msgResp.getMessageDate());
                msg.setMessageDate(dateTime.get(0));
                msg.setMessageTime(dateTime.get(1));
            }

            msg.setMessageType(msgResp.getMessageType());
            msg.setDescription(msgResp.getDescription());
            String mailFrom = msgResp.getFrom();
            if (mailFrom != null) {
                msg.setFrom(mailFrom.replaceAll("\"", ""));
            }
            msg.setTitle(msgResp.getTitle());
            msg.setStatus(msgResp.getMessageStatus());
            msg.setPriority(msgResp.getPriority());
            msg.setTasksCount(msgResp.getTasksCount());
            msg.setTasksComplete(msgResp.getTasksComplete());
            msgRespList.add(msg);
            ++i;
        }

        Collections.sort(msgRespList);
        Collections.reverse(msgRespList);

        int j = 1;
        for (MessageResponse msgResp : msgRespList) {
//            if (i < msgRespList.size()) {
//                sb.append(gson.toJson(msgResp) + ",");
//            } else {
//                sb.append(gson.toJson(msgResp));
//            }
            if (j < msgRespList.size()) {
                sb.append(gson.toJson(msgResp) + ",");
            } else {
                sb.append(gson.toJson(msgResp));
            }
            ++j;
        }
        sb.append("\n]\n}\n}");
        String s = sb.toString();
        return s;
    }

}
