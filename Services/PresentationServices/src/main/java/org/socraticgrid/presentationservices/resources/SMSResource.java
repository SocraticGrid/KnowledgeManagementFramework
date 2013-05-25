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

import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.taskmanager.SwiftSMSHandler;
import org.socraticgrid.taskmanager.TaskContact;
import org.socraticgrid.taskmanager.TaskRuleMessage;
import org.socraticgrid.taskmanager.model.Specification;
import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.model.TaskType;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * RESOURCE CREATED TO TEST SMS - TEST ONLY
 *
 * @author jharby
 */
public class SMSResource extends BaseResource {
        private String apiName;
    String NL = System.getProperty("line.separator");
    
    public SMSResource(Context context, Request request, Response response) {
        super(context, request, response);
        apiName = request.getResourceRef().getQueryAsForm().getFirstValue("apiName");
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        init(context, request, response);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        SessionUtilities.setCORSHeaders(this);
        super.init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        System.out.println("handleMessage");
        String taskTicket = "Test Ticket: " + new Date();
        TaskServiceRef serviceRef = new TaskServiceRef();
        TaskRuleMessage taskMessage = new TaskRuleMessage();
        TaskType task = new TaskType();
        SwiftSMSHandler instance = new SwiftSMSHandler();
        
        serviceRef.setName("Test service ref");
        serviceRef.setLocation("http://smsgateway.ca/SendSMS.asmx");
        serviceRef.setType("SMS");
        serviceRef.setDescription("Unit test for SMS");
        serviceRef.setServiceParam1("XAO706fy87");
        Map<String, String> pMap = new HashMap<String,String>();
        pMap.put("99990070", "619-761-6471");
        
        taskMessage.setTaskID("1");
        taskMessage.setMessage("TESTING FROM TOMCAT");
        TaskContact contact = new TaskContact();
        contact.setProvider("99990070");
        contact.setLocation("San Diego");
        contact.setOrganization("NHINC");
        contact.setMethod("test");
        taskMessage.getProviders().add(contact);
       
        task.setDescription("Test task type");
        task.setName("First test");
        task.setTaskTypeId(1L);
        
        Specification spec = new Specification();
        spec.setName("First spec test");
        spec.setSpecificationId(1L);
        spec.setValue("This is a test spec value");
        spec.setTaskType(task);
        
      //  task.getSpecifications().add(spec);
        

        instance.handleMessage(taskTicket, serviceRef, taskMessage, task);

        Representation representation =
        new StringRepresentation("Success", MediaType.APPLICATION_JSON);       
        return representation;
    }
}
