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

import org.socraticgrid.adapter.fact.LabOrderFactType;
import org.socraticgrid.adapter.fact.SlotFactType;
import org.socraticgrid.common.task.DestinationContext;
import org.socraticgrid.common.task.PatientContext;
import org.socraticgrid.common.task.RuleContext;
import org.socraticgrid.common.task.TaskManagerResponseType;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import java.io.StringWriter;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Task Manager notifies individuals or another process of an event.
 *
 * @author cmatser
 */
public class TaskManagerImpl {

    /** Logging. */
    private static Log log = LogFactory.getLog(TaskManagerImpl.class);

    /** TaskManager property file. */
    public static final String TASKMANAGER_PROPERTY_FILE = "taskmanager";

    /** TaskManager queue factory. */
    public static final String PROPERTY_TASK_QUEUE_FACTORY = "taskQueueFactory";

    /** TaskManager queue property. */
    public static final String PROPERTY_TASK_QUEUE = "taskQueue";

    /** Message: success */
    public static final String TASK_MESSAGE_SUCCESS = "Success";

    /** Message: failure */
    public static final String TASK_MESSAGE_FAILURE = "Failure";

    /** Message: failure id */
    public static final String TASK_MESSAGE_FAILURE_ID = "-1";

    /**
     * This is a non-blocking call that starts the notification process.
     * A message is sent to a JMS Queue so that the client gets an
     * immediate response that the notification is taking place.  Task processing
     * is initiated by the queue listener.
     * 
     * @param request - notification information
     * @return notification response
     */
    public org.socraticgrid.common.task.TaskManagerResponseType startTaskFromRule(org.socraticgrid.common.task.StartTaskFromRuleRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        //Get request parts
        DestinationContext destCtx = request.getDestination();
        if (destCtx == null) {
            destCtx = new DestinationContext();
        }
        PatientContext ptCtx = request.getPatient();
        if (ptCtx == null) {
            ptCtx = new PatientContext();
        }
        RuleContext ruleCtx = request.getRule();
        if (ruleCtx == null) {
            ruleCtx = new RuleContext();
        }

        //Create message
        TaskRuleMessage ruleMessage = new TaskRuleMessage();
        ruleMessage.setTaskID(request.getTaskID());
        ruleMessage.setSubject(request.getSubject());
        ruleMessage.setMessage(request.getMessage());
        ruleMessage.setPriority(request.getPriority());
        ruleMessage.setEscalationMinutes(destCtx.getEscalationMinutes());
        if (destCtx.getDeliveryDate() != null) {
            ruleMessage.setDeliveryDate(destCtx.getDeliveryDate().toGregorianCalendar().getTime());
        }
        if (destCtx.getCompletionDate() != null) {
            ruleMessage.setCompletionDate(destCtx.getCompletionDate().toGregorianCalendar().getTime());
        }
        ruleMessage.setPatientUnitNumber(ptCtx.getPatientUnitNumber());
        ruleMessage.setPatientName(ptCtx.getPatientName());
        ruleMessage.setPatientSex(ptCtx.getPatientSex());
        ruleMessage.setPatientFMPSSN(ptCtx.getPatientFMPSSN());
        ruleMessage.setFactNCID(ruleCtx.getFactNCID());
        ruleMessage.setFactName(ruleCtx.getFactName());
        ruleMessage.setFactValue(ruleCtx.getFactValue());
        ruleMessage.setFactType(ruleCtx.getFactType());
        ruleMessage.setActionID(ruleCtx.getActionID());
        ruleMessage.setActionType(ruleCtx.getActionType());
        ruleMessage.setRuleID(ruleCtx.getRuleID());
        ruleMessage.setRuleDesc(ruleCtx.getRuleDesc());
        ruleMessage.setRuleName(ruleCtx.getRuleName());

        //Add providers
        for (org.socraticgrid.common.task.ContactDetails requestContact : destCtx.getProviders()) {
            TaskContact contact = new TaskContact();
            contact.setMethod(requestContact.getMethod());
            contact.setProvider(requestContact.getProvider());
            contact.setOrganization(requestContact.getOrganization());
            contact.setClinic(requestContact.getClinic());
            contact.setRole(requestContact.getRole());
            contact.setLocation(requestContact.getLocation());
            ruleMessage.getProviders().add(contact);
        }

        //Add escalation providers
        for (org.socraticgrid.common.task.ContactDetails requestContact : destCtx.getEscalationProviders()) {
            TaskContact contact = new TaskContact();
            contact.setMethod(requestContact.getMethod());
            contact.setProvider(requestContact.getProvider());
            contact.setOrganization(requestContact.getOrganization());
            contact.setClinic(requestContact.getClinic());
            contact.setRole(requestContact.getRole());
            contact.setLocation(requestContact.getLocation());
            ruleMessage.getEscalationProviders().add(contact);
        }

        //Send message
        QueueResponse result = queueMessage(ruleMessage);

        //Set response info
        response.setTicket(result.ticket);
        response.setDetail(result.detail);

        return response;
    }

    /**
     * This is a non-blocking call that starts the mail process.
     * A message is sent to a JMS Queue so that the client gets an
     * immediate response that the mail is taking place.  Task processing
     * is initiated by the queue listener.
     *
     * @param request - notification information
     * @return notification response
     */
    public org.socraticgrid.common.task.TaskManagerResponseType sendMailTask(org.socraticgrid.common.task.SendMailTaskRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        //Create message
        TaskMailMessage message = new TaskMailMessage();
        message.setTaskID(request.getTaskID());
        message.setFromUser(request.getFromUser());
        message.setFromUserProvider(request.isFromUserProvider());
        message.setToUser(request.getToUser());
        message.setToUserProvider(request.isToUserProvider());
        message.setSubject(request.getSubject());
        message.setMessage(request.getMessage());

        //Send message
        QueueResponse result = queueMessage(message);

        //Set response info
        response.setTicket(result.ticket);
        response.setDetail(result.detail);

        return response;
    }

    /**
     * Register a patient in the disease registry.
     * 
     * @param registerPersonDiseaseRequest
     * @return
     */
    public org.socraticgrid.common.task.TaskManagerResponseType registerPersonDisease(org.socraticgrid.common.task.RegisterPersonDiseaseRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        //Create message
        DiseaseRegistryMessage message = new DiseaseRegistryMessage();
        message.setActionRegister(true);
        message.setTaskID(request.getTaskID());
        message.setDiseaseType(request.getDiseaseType());
        message.setPatientId(request.getPatientId());
        message.setPatientName(request.getPatientName());
        if (request.getPatientDOB() != null) {
            message.setPatientDOB(request.getPatientDOB().toGregorianCalendar());
        }

        //Send message
        QueueResponse result = queueMessage(message);

        //Set response info
        response.setTicket(result.ticket);
        response.setDetail(result.detail);

        return response;
    }

    /**
     * Unregister a patient from the disease registry.
     *
     * @param unRegisterPersonDiseaseRequest
     * @return
     */
    public org.socraticgrid.common.task.TaskManagerResponseType unRegisterPersonDisease(org.socraticgrid.common.task.UnRegisterPersonDiseaseRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        //Create message
        DiseaseRegistryMessage message = new DiseaseRegistryMessage();
        message.setActionRegister(false);
        message.setTaskID(request.getTaskID());
        message.setDiseaseType(request.getDiseaseType());
        message.setPatientId(request.getPatientId());

        //Send message
        QueueResponse result = queueMessage(message);

        //Set response info
        response.setTicket(result.ticket);
        response.setDetail(result.detail);

        return response;
    }


    /**
     * Creates a lab order.
     * 
     * @param createLabOrderRequest
     * @return
     */
    public org.socraticgrid.common.task.TaskManagerResponseType createLabOrder(org.socraticgrid.common.task.CreateLabOrderRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        try {
            //Serialize lab order
            JAXBContext context = JAXBContext.newInstance(LabOrderFactType.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.marshal(new JAXBElement(new QName("uri","local"),
                LabOrderFactType.class, request.getLabOrderFact()), sw);

            //Create message
            LabOrderMessage message = new LabOrderMessage();
            message.setTaskID(request.getTaskID());
            message.setLabOrder(sw.toString());

            //Send message
            QueueResponse result = queueMessage(message);

            //Set response info
            response.setTicket(result.ticket);
            response.setDetail(result.detail);
        }
        catch (Exception e) {
            String msg = e.getMessage();
            if ((msg == null) || msg.isEmpty()) {
                msg = "Error handling lab order message.";
            }
            log.error("Error handling lab order message.", e);
            response.setTicket(TASK_MESSAGE_FAILURE_ID);
            response.setDetail(msg);
        }

        return response;
    }


    /**
     * Schedule appointment.
     * 
     * @param scheduleApptRequest
     * @return
     */
    public org.socraticgrid.common.task.TaskManagerResponseType scheduleAppt(org.socraticgrid.common.task.ScheduleApptRequestType request) {
        TaskManagerResponseType response = new TaskManagerResponseType();

        try {
            //Serialize lab order
            JAXBContext context = JAXBContext.newInstance(SlotFactType.class);
            Marshaller marshaller = context.createMarshaller();
            StringWriter sw = new StringWriter();
            marshaller.marshal(new JAXBElement(new QName("uri","local"),
                SlotFactType.class, request.getSlotRequest()), sw);

            //Create message
            SlotRequestMessage message = new SlotRequestMessage();
            message.setTaskID(request.getTaskID());
            message.setSlotRequest(sw.toString());

            //Send message
            QueueResponse result = queueMessage(message);

            //Set response info
            response.setTicket(result.ticket);
            response.setDetail(result.detail);
        }
        catch (Exception e) {
            String msg = e.getMessage();
            if ((msg == null) || msg.isEmpty()) {
                msg = "Error handling schedule appointment message.";
            }
            log.error("Error handling schedule appointment message.", e);
            response.setTicket(TASK_MESSAGE_FAILURE_ID);
            response.setDetail(msg);
        }

        return response;
    }


    /**
     * Queue the message to the task handler.
     *
     * @param msgObject
     * @return
     */
    private QueueResponse queueMessage(java.io.Serializable msgObject) {
        QueueResponse response = new QueueResponse();
        String taskQ = null;
        QueueConnection queueConnection = null;

        try {
            //Get task queue name & queue factory
            taskQ = PropertyAccessor.getProperty(TASKMANAGER_PROPERTY_FILE, PROPERTY_TASK_QUEUE);
            String taskQFactory = PropertyAccessor.getProperty(TASKMANAGER_PROPERTY_FILE, PROPERTY_TASK_QUEUE_FACTORY);

            //Get queue connection
            Context jndiContext = new InitialContext();
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)
                jndiContext.lookup(taskQFactory);
            Queue queue = (Queue) jndiContext.lookup(taskQ);

            //Create connection session
            queueConnection = queueConnectionFactory.createQueueConnection();
            QueueSession queueSession = queueConnection.createQueueSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = queueSession.createSender(queue);

            //Create message
            ObjectMessage message = queueSession.createObjectMessage(msgObject);

            //Send message
            queueSender.send(message);

            //Set response info
            response.ticket = message.getJMSMessageID();
            response.detail = TASK_MESSAGE_SUCCESS;
        }
        catch (PropertyAccessException pae) {
            String msg = TASK_MESSAGE_FAILURE + ": error accessing task properties in file:"
                    + TASKMANAGER_PROPERTY_FILE + ".";
            log.error(msg, pae);
            response.ticket = TASK_MESSAGE_FAILURE_ID;
            response.detail = msg;
        }
        catch (NamingException ne) {
            String msg = TASK_MESSAGE_FAILURE + ": error creating connection to queue: " + taskQ + ".";
            log.error(msg, ne);
            response.ticket = TASK_MESSAGE_FAILURE_ID;
            response.detail = msg;
        }
        catch (JMSException jmse) {
            String msg = TASK_MESSAGE_FAILURE + ": error occurred trying to send notificaiton to task queue: "
                    + taskQ + ".";
            log.error(msg, jmse);
            response.ticket = TASK_MESSAGE_FAILURE_ID;
            response.detail = msg;
        }
        finally {
            //Close queue
            if (queueConnection != null) {
                try {
                    queueConnection.close();
                } catch (JMSException e) {}
            }
        }

        return response;
    }

    class QueueResponse {
        String ticket;
        String detail;
    }
}
