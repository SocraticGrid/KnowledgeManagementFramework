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

package org.socraticgrid.presentationservices.helpers;

import org.socraticgrid.ura.EntityType;
import org.socraticgrid.ura.IdAddressBean;
import org.socraticgrid.ura.UniversalResourceAddressBean;
import org.socraticgrid.ura.UniversalResourceAddressBeanFactory;

import java.net.URISyntaxException;

import java.util.HashMap;

import javax.xml.ws.BindingProvider;

import org.restlet.resource.ResourceException;


/**
 *
 * @author Jerry Goodnough 
 */
public class ActionHelper
{

    private static HashMap<String, String> alertDataSources;
    private static HashMap<String, String> alertAlternateActions;
    private static HashMap<String, String> mailAlternateActions;
    private static HashMap<String, String> docAlternateActions;
    private static HashMap<String, String> defaultStatus;

    private static ActionHelper singleton;

    static
    {

        singleton = new ActionHelper();
        alertDataSources = new HashMap<String, String>();

        alertDataSources.put("MedAlerts", "");
        alertDataSources.put("MedAlerts - mobile", "");
        alertDataSources.put("Patient Alerts", "");
        alertDataSources.put("Messages", "");

        alertAlternateActions = new HashMap<String, String>();
        alertAlternateActions.put("SEEN", "Read");

        mailAlternateActions = new HashMap<String, String>();
        mailAlternateActions.put("Read", "SEEN");

        docAlternateActions = new HashMap<String, String>();


        defaultStatus = new HashMap<String, String>();
        defaultStatus.put("Read", "true");
        defaultStatus.put("SEEN", "true");
    }

    public static ActionHelper getActionHelper()
    {
        return singleton;
    }

    public CallStatus updateAction(String dataSource, String itemId,
        String reqAction, String status, String message, String userId,
        String patientId) throws ResourceException
    {
        CallStatus result = new CallStatus();
        result.setError(false);

        boolean isPatient = false;
        String user;
        UniversalResourceAddressBean uid;

        try
        {

            if (userId.length() > 0)
            {
                user = userId;

                if (UniversalResourceAddressBeanFactory.isAddressBean(userId))
                {
                    uid = UniversalResourceAddressBeanFactory.getInstance()
                        .createIdAddressBean(userId);
                }

                if (userId.compareTo(patientId) == 0)
                {

                    //If both are passed we are assuming the user is the patient - Which might be wrong
                    isPatient = true;
                    uid = new IdAddressBean(EntityType.PATIENT, userId, "");
                }
                else
                {
                    uid = new IdAddressBean(EntityType.PROVIDER, userId, "");
                }
            }
            else if (patientId.length() > 0)
            {
                user = patientId;
                isPatient = true;
                uid = new IdAddressBean(EntityType.PATIENT, patientId, "");
            }
            else
            {
                result.setError(true);
                result.setStatusDetail("No userId or patientId specified");

                return result;

            }
        }
        catch (URISyntaxException e)
        {
            result.setError(true);
            result.setStatusDetail(e.getMessage());

            return result;

        }

        // Sould support MedAlerts, Patient Alerts, MedAlerts - mobile, (Messages?)
        if (alertDataSources.containsKey(dataSource))
        {
            String action = reqAction;

            if (alertAlternateActions.containsKey(reqAction))
            {
                action = alertAlternateActions.get(reqAction);
            }

            result = AlertHelper.getAlertHelper().updateAlert(itemId, action,
                    message, user, isPatient);


        }
        else if (dataSource.compareToIgnoreCase("Mail") == 0)
        {
            String flagAction = reqAction;

            if (mailAlternateActions.containsKey(reqAction))
            {
                flagAction = mailAlternateActions.get(reqAction);
            }

            if ((status.isEmpty()) && (defaultStatus.containsKey(flagAction)))
            {
                status = defaultStatus.get(flagAction);
            }


            boolean ok = MailServiceHelper.getMailServiceHelper()
                .markMailMessage(itemId, flagAction, status);

            if (!ok)
            {
                result.setError(true);
                result.setStatusDetail(
                    "Mail Status update failed, check server log");
            }
        }
        else if (dataSource.compareToIgnoreCase("NHIN Documents") == 0)
        {
            String flagAction = reqAction;

            if (docAlternateActions.containsKey(reqAction))
            {
                flagAction = docAlternateActions.get(reqAction);
            }

            if ((status.isEmpty()) && (defaultStatus.containsKey(flagAction)))
            {
                status = defaultStatus.get(flagAction);
            }

            if (flagAction.compareTo("Read") == 0)
            {

                try // Call Web Service Operation
                {
                    org.socraticgrid.aggregator.DisplayDataAggregator service =
                        new org.socraticgrid.aggregator.DisplayDataAggregator();
                    org.socraticgrid.aggregator.DisplayDataAggregatorPortType port =
                        service.getDisplayDataAggregatorPortSoap11();

                    //Bind call to Endpoint
                    ((BindingProvider) port).getRequestContext().put(
                        BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                        PropertyHelper.getPropertyHelper().getProperty(
                            "DisplayDataAggregatorService"));


                    org.socraticgrid.common.dda.UpdateInboxStatusRequestType updateInboxStatusRequest =
                        new org.socraticgrid.common.dda.UpdateInboxStatusRequestType();

                    updateInboxStatusRequest.setDataSource(dataSource);
                    updateInboxStatusRequest.setItemId(itemId);
                    updateInboxStatusRequest.setUserId(uid.toString());

                    boolean bstat = Boolean.parseBoolean(status);
                    updateInboxStatusRequest.setRead(bstat);

                    org.socraticgrid.common.dda.UpdateInboxStatusResponseType callresult =
                        port.updateInboxStatus(updateInboxStatusRequest);

                    if (callresult.getErrorList().size() > 0)
                    {
                        result.setError(true);
                        result.setStatusDetail(callresult.getErrorList().get(
                                0).getText());
                    }
                    else
                    {
                        result.setError(false);
                    }
                }
                catch (Exception ex)
                {
                    result.setError(true);
                    result.setStatusDetail(ex.getMessage());
                }
            }
        }

        return result;
    }
}
