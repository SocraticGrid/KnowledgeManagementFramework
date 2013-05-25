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

package org.socraticgrid.docmgr;

import com.thoughtworks.xstream.XStream;
import org.socraticgrid.common.docmgr.UpdateDocumentSlotRequestType;
import org.socraticgrid.docmgr.msgobject.ArchiveInfo;
import org.socraticgrid.docmgr.msgobject.MirthMessage;
import org.socraticgrid.docmgr.msgobject.NHINQueryMessage;
import org.socraticgrid.docmgr.msgobject.NHINRetrieveMessage;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import java.util.Date;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author cmatser
 */
public class DocumentManagerJob implements Job {

    /** Logging. */
    private static Log log = LogFactory.getLog(DocumentManagerJob.class);

    public DocumentManagerJob() {
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        try {
            XStream xstream = new XStream();
            JobDataMap data = context.getMergedJobDataMap();
            String identity = context.getJobDetail().getKey().getName();
            String param = data.getString(DocumentManagerImpl.DOCMGR_JOB_MESSAGE);
            Object message = xstream.fromXML(param);

            log.debug("Starting Background job: " + identity);

            if (message instanceof NHINQueryMessage) {
                new NHINQueryProcessor().handleQueryMessage(
                    identity, (NHINQueryMessage) message);
            }
            else if (message instanceof NHINRetrieveMessage) {
                new NHINRetrieveProcessor().handleRetrieveMessage(
                    identity, (NHINRetrieveMessage) message);
            }
            else if (message instanceof MirthMessage) {
                handleMirthMessage(identity, (MirthMessage) message);
            }
            else if (message instanceof ArchiveInfo) {
                handleArchive(identity, (ArchiveInfo) message);
            }
            else {
                throw new IllegalArgumentException("Found unexpected class: " + message.getClass().getName());
            }
        }
        catch (Throwable t) {
            log.error("Error processing message for document manager message handler.", t);
        }

    }

    private void handleMirthMessage(String ticket, MirthMessage mirthMsg) {

        //Check that url was sent, otherwise skip
        if ((mirthMsg.getWsdl() == null) || mirthMsg.getWsdl().isEmpty()) {
            log.debug("Skipping mirth routing, ticket: " + ticket);
            return;
        }

        try { // This code block invokes the MirthOrders:acceptMessage operation on web service
            javax.xml.ws.Service myService = new ServiceUtil().createService(
                "MirthOrders.wsdl",
                "http://components.mule.server.mirth.webreach.com",
                "_Proxy7Service");
            com.webreach.mirth.server.mule.components.Proxy7 port = myService.getPort(
                new javax.xml.namespace.QName("http://components.mule.server.mirth.webreach.com", "Orders"),
                com.webreach.mirth.server.mule.components.Proxy7.class);
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                mirthMsg.getWsdl());
            port.acceptMessage(mirthMsg.getBody());

            log.debug("Document sent to mirth, ticket: " + ticket);
        } catch(Exception e) {
            log.error("Error sending document to mirth, ticket: " + ticket, e);
        }

    }

    public void handleArchive(String msgId, ArchiveInfo archiveInfo) {
        log.debug("Starting archive document task.");

        try {
            if (archiveInfo == null) {
                throw new Exception("Empty request.");
            }

            if ((archiveInfo.getRepositoryId() == null) || (archiveInfo.getDocumentUniqueId() == null)) {
                throw new Exception("Either repositoryId or documentUniqueId is missing.");
            }

            //Prepare request for update
            UpdateDocumentSlotRequestType updateRequest = new UpdateDocumentSlotRequestType();
            updateRequest.setHomeCommunityId(archiveInfo.getHomeCommunityId());
            updateRequest.setRepositoryUniqueId(archiveInfo.getRepositoryId());
            updateRequest.setDocumentUniqueId(archiveInfo.getDocumentUniqueId());
            updateRequest.setSlotName(DocumentManagerImpl.XDS_ARCHIVE_SLOT);
            updateRequest.getSlotValueList().add(DocumentManagerImpl.XDS_DATE_FORMAT.format(new Date()));

            //Do store with metdata for "accessed" set
            RegistryResponseType response = new DocumentManagerImpl().documentManagerUpdateDocumentSlot(updateRequest);

            if (response.getStatus().equals(DocumentManagerImpl.XDS_FAILED_STATUS)) {
                throw new Exception("Update slot contained 'failed' status.");
            }
        }
        catch (Exception e) {
            log.error("Error performing archive.", e);
        }

    }

}
