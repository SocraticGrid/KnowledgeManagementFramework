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

import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.model.TaskType;
import java.util.Collections;
import javax.xml.ws.handler.MessageContext;
import mil.navy.med.dzreg.types.AckType;
import mil.navy.med.dzreg.types.PersonType;
import mil.navy.med.dzreg.types.RegisterPersonRequestType;
import mil.navy.med.dzreg.types.RegistryType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handle Disease Registry task events.  Register/Unregister patients
 * for a specified disease.
 *
 * @author cmatser
 */
public class DiseaseRegistryHandler {

    /** Disease Registry data provider. */
    public static final String DISEASE_REG_DATA_SOURCE = "KMR";
    /** Failure code. */
    public static final String RESPONSE_FAILURE = "-1";
    /** Logging. */
    private static Log log = LogFactory.getLog(DiseaseRegistryHandler.class);

    /**
     * Process task.
     */
    public void handleMessage(String taskTicket, TaskServiceRef serviceRef, TaskMessage taskMessage, TaskType task) {
        DiseaseRegistryMessage regMessage = null;

        log.debug("Handling Disease Registry task.");

        if (taskMessage instanceof DiseaseRegistryMessage) {
            regMessage = (DiseaseRegistryMessage) taskMessage;
        }

        if (regMessage == null) {
            log.error("Error, invalid disease registry task for ticket: " + taskTicket);
            return;
        }

        try {
            //Setup call to disease registry
            AckType result = null;
            RegisterPersonRequestType params = new RegisterPersonRequestType();
            params.setDataSource(DISEASE_REG_DATA_SOURCE);
            PersonType person = new PersonType();
            person.setId(Integer.parseInt(regMessage.getPatientId()));
            person.setName(regMessage.getPatientName());

            person.setDataSource(DISEASE_REG_DATA_SOURCE);
            params.setPerson(person);
            RegistryType type = new RegistryType();
            type.setRegistryId(Integer.parseInt(regMessage.getDiseaseType()));
            params.getRegistry().add(type);

            //Perform appropriate action
            if (regMessage.isActionRegister()) {
                result = registerPt(serviceRef.getLocation(), params);
            } else {
                result = unregisterPt(serviceRef.getLocation(), params);
            }

            log.debug("Task ticket: " + taskTicket
                    + ". Disease Registry result: " + result.getResponseCode() + ","
                    + result.getDetectedIssueText());
        } catch (Exception e) {
            log.error("Disease registry failure for task ticket: " + taskTicket, e);
        }
    }

    /**
     * Makes call to disease registry service to register a patient.
     * 
     * @param endpoint
     * @param params
     * @return
     */
    private AckType registerPt(String endpoint, RegisterPersonRequestType params) {
        AckType result = null;

        try { // Call Web Service Operation
            mil.navy.med.dzreg.service.RegistriesService service = new mil.navy.med.dzreg.service.RegistriesService();
            
            mil.navy.med.dzreg.service.RegistriesServicePortType port = service.getRegistriesServicePort();
            
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                    javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    endpoint);
            
            result = port.register(params);
        } catch (Exception ex) {
            result = new AckType();
            result.setResponseCode(RESPONSE_FAILURE);
            result.setDetectedIssueText(ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

    /**
     * Makes call to disease registry service to un-register a patient.
     *
     * @param endpoint
     * @param params
     * @return
     */
    private AckType unregisterPt(String endpoint, RegisterPersonRequestType params) {
        AckType result = null;

        try { // Call Web Service Operation
            mil.navy.med.dzreg.service.RegistriesService service = new mil.navy.med.dzreg.service.RegistriesService();
            mil.navy.med.dzreg.service.RegistriesServicePortType port = service.getRegistriesServicePort();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                    javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    endpoint);
            result = port.unregister(params);
        } catch (Exception ex) {
            result = new AckType();
            result.setResponseCode(RESPONSE_FAILURE);
            result.setDetectedIssueText(ex.getMessage());
        }

        return result;
    }
}