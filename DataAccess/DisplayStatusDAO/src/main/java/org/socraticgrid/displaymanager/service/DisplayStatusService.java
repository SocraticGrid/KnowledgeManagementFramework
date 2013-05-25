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

package org.socraticgrid.displaymanager.service;

import org.socraticgrid.displaymanager.dao.InboxStatusDao;
import org.socraticgrid.displaymanager.model.InboxStatus;
import org.socraticgrid.displaymanager.model.InboxStatusQueryParams;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class DisplayStatusService {

    private static Log log = LogFactory.getLog(DisplayStatusService.class);

    /**
     * Save a status record.
     *
     * @param status Status object to save.
     */
    public void saveInboxStatus(InboxStatus status)
    {
        log.debug("Saving a status");

        if (status != null)
        {
            if (status.getStatusId() != null)
            {
                log.debug("Performing an update for status: " + status.getStatusId());
            }
            else
            {
                log.debug("Performing an insert");
            }

        }

        InboxStatusDao dao = new InboxStatusDao();
        dao.save(status);
    }

    /**
     * Delete a status
     *
     * @param status InboxStatus to delete
     * @throws DisplayStatusException
     */
    public void deleteInboxStatus(InboxStatus status) throws DisplayStatusException
    {
        log.debug("Deleting a status");
        InboxStatusDao dao = new InboxStatusDao();

        if (status == null)
        {
            throw new DisplayStatusException("Status to delete was null");
        }

        dao.delete(status);
    }

    /**
     * Retrieve a status by identifier
     *
     * @param statusId Status identifier
     * @return Retrieved status
     */
    public InboxStatus getInboxStatus(Long statusId)
    {
        InboxStatusDao dao = new InboxStatusDao();
        return dao.findById(statusId);
    }

    /**
     * Retrieves all status
     *
     * @return All status records
     */
    public List<InboxStatus> getAllInboxStatus()
    {
        InboxStatusDao dao = new InboxStatusDao();
        return dao.findAll();
    }

    /**
     * Status query
     *
     * @param params Status query parameters
     * @return Query results
     */
    public List<InboxStatus> inboxStatusQuery(InboxStatusQueryParams params)
    {
        InboxStatusDao dao = new InboxStatusDao();
        return dao.findInboxStatus(params);
    }

}
