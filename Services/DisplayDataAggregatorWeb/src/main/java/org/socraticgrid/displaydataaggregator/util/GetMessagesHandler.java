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
package org.socraticgrid.displaydataaggregator.util;

import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.displaymaildata.DisplayMailDataHandler;
import org.socraticgrid.docmgr.DocumentManagerImpl;
import java.util.Map;

/**
 * Called by DisplayDataAggregatorImpl to handle the different types of GetMessages
 * requests
 *
 * @author jharby
 */
public class GetMessagesHandler {

    /**
     * Get all messages for all types, URLs from displayAggregator.properties file
     * passed in a Map.
     *
     * @param request
     * @return
     */
    public GetMessagesResponseType getAllMessages(GetMessagesRequestType request)
    {
        GetMessagesResponseType response = new GetMessagesResponseType();

        //Get all EMAILS
        response.getGetMessageResponse().
                addAll(getEmailMessages(request).getGetMessageResponse());

        //Get all ALERTS only if NOT looking from Draft and Sent
        if (request.getLocation() != null
            && !request.getLocation().equalsIgnoreCase("Draft")
            && !request.getLocation().equalsIgnoreCase("Sent"))
        {
            response.getGetMessageResponse().
                    addAll(getAlertMessages(request).getGetMessageResponse());
        }

        //Get all DOCUMENTS
        response.getGetMessageResponse().
                addAll(getDocumentMessages(request).getGetMessageResponse());

        //Prep an empty Reponse only if no msgs found.
        if (response.getGetMessageResponse().isEmpty()) {
            GetMessagesResponseType.GetMessageResponse resp = 
                    new GetMessagesResponseType.GetMessageResponse();
            resp.setSuccessStatus(true);
        }
        return response;

    }

    /**
     * Get messages from alert tables in KMR schema
     *
     * @param request
     * @return
     */
    public GetMessagesResponseType getAlertMessages(GetMessagesRequestType request)
    {
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
        GetMessagesRequestType componentRequest = new GetMessagesRequestType();
        componentRequest.setMessageType(request.getMessageType());
        componentRequest.setPatientId(request.getPatientId());
        componentRequest.setUserId(request.getUserId());
        componentRequest.setLocation(request.getLocation());
        // Fix this - do we need to pass MedAlerts?
        return util.getMessages(componentRequest);
    }

    /**
     * Make WS call to get email messages from Zimbra. Uses SSL connection.
     *
     * @param request
     * @return
     */
    public GetMessagesResponseType getEmailMessages(GetMessagesRequestType request)
    {
        GetMessagesRequestType componentRequest = new GetMessagesRequestType();
        componentRequest.setMessageType(request.getMessageType());
        componentRequest.setPatientId(request.getPatientId());
        componentRequest.setUserId(request.getUserId());
        componentRequest.setLocation(request.getLocation());
        return new DisplayMailDataHandler().getMessages(componentRequest);
    }

    /**
     * TBD - returns stub currently
     *
     * @param request
     * @return
     */
    public GetMessagesResponseType getDocumentMessages(GetMessagesRequestType request)
    {
        DocumentManagerImpl docMgr = new DocumentManagerImpl();

        if ((request.getPatientId() == null) || request.getPatientId().isEmpty()) {
            request.setPatientId(request.getUserId());
        }

        request.setMessageType("Document");

        return docMgr.getMessages(request);
    }

}
