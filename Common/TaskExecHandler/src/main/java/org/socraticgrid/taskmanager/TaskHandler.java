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

import org.socraticgrid.properties.PropertyAccessor;
import org.socraticgrid.taskmanager.model.Specification;
import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.model.TaskType;
import org.socraticgrid.taskmanager.service.ServiceConstants;
import org.socraticgrid.taskmanager.service.SpecConstants;
import org.socraticgrid.taskmanager.service.TaskService;
import org.socraticgrid.taskmanagerclient.ContactDetails;
import org.socraticgrid.taskmanagerclient.TaskHandlerMessage;
import java.util.HashMap;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Start of task processing takes place here.  The task message contains the
 * task identifier and the context for this task.  With this information
 * it is determined how this task is processed.
 *
 * @author cmatser
 */
@MessageDriven(mappedName = "jms/TaskQueue", activationConfig =  {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
    })
public class TaskHandler implements MessageListener {

    @Resource
    private MessageDrivenContext mdc;
    
    /** Logging. */
    private static Log log = LogFactory.getLog(TaskHandler.class);

    public TaskHandler() {
    }

    public void onMessage(Message message) {
        TaskMessage taskMessage = null;

        try {
            log.debug("Received message id: " + message.getJMSMessageID());

            if (message instanceof ObjectMessage) {
                ObjectMessage oMsg = (ObjectMessage) message;
                Object object = oMsg.getObject();
                if (object instanceof TaskMessage) {
                    taskMessage =(TaskMessage) object;
                }
            }
        }
        catch (JMSException e) {
            log.error("Error processing message for task bean.", e);
            //Don't rollback else message gets stuck in queue
            //mdc.setRollbackOnly();
        } catch (Throwable te) {
            log.error("Error processing message for task handler.", te);
        }

        //Check if TaskMessage was found
        if (taskMessage == null) {
            log.error("Expected TaskMessage(ObjectMessage) not found, ignoring message.");
            return;
        }

        try {
            //Pull out task from database
            TaskService taskService = new TaskService();
            TaskType task = taskService.getTask(new Long(taskMessage.getTaskID()));

            if (task == null) {
                throw new Exception("Task id(" + taskMessage.getTaskID() + ") was not found.");
            }

            if ((task.getSpecifications() == null) || (task.getSpecifications().isEmpty())) {
                throw new Exception("Task id(" + task.getTaskTypeId() + ") has no specifications.");
            }

            //Find tasking service
            String taskingServiceId = null;
            for (Specification spec : task.getSpecifications()) {
                if (SpecConstants.NAME_SERVICE_REF.equals(spec.getName())) {
                    taskingServiceId = spec.getValue();
                    break;
                }
            }

            if (taskingServiceId == null) {
                throw new Exception("Task id(" + task.getTaskTypeId() + ") had no specification(" + SpecConstants.NAME_SERVICE_REF + ").");
            }

            TaskServiceRef serviceRef = taskService.getServiceRef(new Long(taskingServiceId));
            if (serviceRef == null) {
                throw new Exception("Task id(" + task.getTaskTypeId() + ") tasking service id(" + taskingServiceId + ") did not exist.");
            }

            //Handle each method appropriately
//            if (ServiceConstants.TYPE_JMS_QUEUE.equals(serviceRef.getType())) {
//                notifyQueue(message.getJMSMessageID(), serviceRef.getLocation(), taskMessage, task);
//            }
//            else 
            if (ServiceConstants.TYPE_SWIFT_SMS.equals(serviceRef.getType())) {
                new SwiftSMSHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_ZIMBRA_VTODO.equals(serviceRef.getType())) {
                new VTodoHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_ZIMBRA_VEVENT.equals(serviceRef.getType())) {
                new VEventHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_SMTP.equals(serviceRef.getType())) {
                new MailHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_DISEASE_REGISTRY.equals(serviceRef.getType())) {
                new DiseaseRegistryHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_LAB_ORDER.equals(serviceRef.getType())) {
                new LabOrderHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else if (ServiceConstants.TYPE_SLOT_REQUEST.equals(serviceRef.getType())) {
                new SlotRequestHandler().handleMessage(message.getJMSMessageID(), serviceRef, taskMessage, task);
            }
            else {
                throw new UnsupportedOperationException("Unsupported task service method: " + serviceRef.getType());
            }

        } catch (Throwable t) {
            log.error("Error processing message for task handler.", t);
        }

    }
    
    /**
     * Notify external handler via JMS Queue.
     * 
     * @param taskMessage
     */
//    private void notifyQueue(String taskTicket, String queueName, TaskMessage taskMessage, TaskType task) {
//        QueueConnection queueConnection = null;
//
//        try {
//            TaskRuleMessage ruleMessage = (TaskRuleMessage) taskMessage;
//
//            //Get task queue name & queue factory
//            String taskQFactory = PropertyAccessor.getProperty(
//                    TaskManagerImpl.TASKMANAGER_PROPERTY_FILE, TaskManagerImpl.PROPERTY_TASK_QUEUE_FACTORY);
//
//            //Get queue connection
//            Context jndiContext = new InitialContext();
//            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory)
//                jndiContext.lookup(taskQFactory);
//            Queue queue = (Queue) jndiContext.lookup(queueName);
//
//            //Create connection session
//            queueConnection = queueConnectionFactory.createQueueConnection();
//            QueueSession queueSession = queueConnection.createQueueSession(false,
//                    Session.AUTO_ACKNOWLEDGE);
//            QueueSender queueSender = queueSession.createSender(queue);
//
//            //Create message
//            TaskHandlerMessage externalHandlerMsg = new TaskHandlerMessage();
//            externalHandlerMsg.setTaskTicket(taskTicket);
//            externalHandlerMsg.setTaskID(ruleMessage.getTaskID());
//            externalHandlerMsg.setTaskDescription(task.getDescription());
//            externalHandlerMsg.setSubject(ruleMessage.getSubject());
//            externalHandlerMsg.setMessage(ruleMessage.getMessage());
//            externalHandlerMsg.setPriority(ruleMessage.getPriority());
//            externalHandlerMsg.setEscalationMinutes(ruleMessage.getEscalationMinutes());
//            externalHandlerMsg.setDeliveryDate(ruleMessage.getDeliveryDate());
//            externalHandlerMsg.setCompletionDate(ruleMessage.getCompletionDate());
//            externalHandlerMsg.setPatientUnitNumber(ruleMessage.getPatientUnitNumber());
//            externalHandlerMsg.setPatientName(ruleMessage.getPatientName());
//            externalHandlerMsg.setPatientSex(ruleMessage.getPatientSex());
//            externalHandlerMsg.setPatientFMPSSN(ruleMessage.getPatientFMPSSN());
//            externalHandlerMsg.setFactNCID(ruleMessage.getFactNCID());
//            externalHandlerMsg.setFactName(ruleMessage.getFactName());
//            externalHandlerMsg.setFactValue(ruleMessage.getFactValue());
//            externalHandlerMsg.setFactType(ruleMessage.getFactType());
//            externalHandlerMsg.setActionID(ruleMessage.getActionID());
//            externalHandlerMsg.setActionType(ruleMessage.getActionType());
//            externalHandlerMsg.setRuleID(ruleMessage.getRuleID());
//            externalHandlerMsg.setRuleDesc(ruleMessage.getRuleDesc());
//            externalHandlerMsg.setRuleName(ruleMessage.getRuleName());
//
//            //Add providers
//            for (TaskContact taskContact : ruleMessage.getProviders()) {
//                ContactDetails contact = new ContactDetails();
//                contact.setMethod(taskContact.getMethod());
//                contact.setProvider(taskContact.getProvider());
//                contact.setOrganization(taskContact.getOrganization());
//                contact.setClinic(taskContact.getClinic());
//                contact.setRole(taskContact.getRole());
//                contact.setLocation(taskContact.getLocation());
//                externalHandlerMsg.getProviders().add(contact);
//            }
//
//            //Add escalation providers
//            for (TaskContact taskContact : ruleMessage.getEscalationProviders()) {
//                ContactDetails contact = new ContactDetails();
//                contact.setMethod(taskContact.getMethod());
//                contact.setProvider(taskContact.getProvider());
//                contact.setOrganization(taskContact.getOrganization());
//                contact.setClinic(taskContact.getClinic());
//                contact.setRole(taskContact.getRole());
//                contact.setLocation(taskContact.getLocation());
//                externalHandlerMsg.getEscalationProviders().add(contact);
//            }
//
//            //Add task attributes
//            HashMap<String,String> taskAttrs = new HashMap<String,String>();
//            for (Specification spec : task.getSpecifications()) {
//                taskAttrs.put(spec.getName(), spec.getValue());
//            }
//            taskAttrs.put("taskTypeId", task.getTaskTypeId().toString());
//            taskAttrs.put("taskTypeName", task.getName());
//            taskAttrs.put("taskTypeDesc", task.getDescription());
//            externalHandlerMsg.setTaskAttributes(taskAttrs);
//
//            //Create JMS message
//            ObjectMessage message = queueSession.createObjectMessage(externalHandlerMsg);
//            message.setJMSCorrelationID(taskTicket);
//
//            //Send message
//            queueSender.send(message);
//        }
//        catch (Exception e) {
//            log.error("Error handling task ticket:" + taskTicket, e);
//        }
//        finally {
//            //Close queue
//            if (queueConnection != null) {
//                try {
//                    queueConnection.close();
//                } catch (JMSException e) {}
//            }
//        }
//
//    }
}
