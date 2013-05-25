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

package org.socraticgrid.displaymanager.dao;

import org.socraticgrid.displaymanager.model.InboxStatus;
import org.socraticgrid.displaymanager.model.InboxStatusQueryParams;
import org.socraticgrid.displaymanager.util.HibernateUtil;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author cmatser
 */
public class InboxStatusDao {

    ObjectDao<InboxStatus> objectDao = new ObjectDao();

    Log log = LogFactory.getLog(InboxStatusDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param service InboxStatus object to save.
     */
    public void save(InboxStatus service) {
        log.debug("Performing save");

        try {
            objectDao.save(service);
        }
        catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed save");
    }

    /**
     * Delete a service
     *
     * @param service InboxStatus to delete
     */
    public void delete(InboxStatus service) {
        log.debug("Performing delete");

        try {
            objectDao.delete(service);
        }
        catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed delete");
    }

    /**
     * Retrieve a service by identifier
     *
     * @param alertServiceRefId InboxStatus identifier
     * @return Retrieved service
     */
    public InboxStatus findById(Long alertServiceRefId) {
        InboxStatus service = null;

        log.debug("Performing retrieve using id: " + alertServiceRefId);

        try {
            service = objectDao.findById(alertServiceRefId, InboxStatus.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed retrieve by id");

        return service;
    }

    /**
     * Retrieves all service
     *
     * @return All service records
     */
    @SuppressWarnings("unchecked")
    public List<InboxStatus> findAll() {
        List<InboxStatus> services = null;

        log.debug("Performing retrieve of all objects");

        try {
            services = objectDao.findAll(InboxStatus.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed retrieve all");

        return services;
    }

    /**
     * Perform a query
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<InboxStatus> findInboxStatus(InboxStatusQueryParams params) {
        log.debug("Beginning query");

        String user = null;
        String source = null;
        String item = null;

        if (params != null) {
            user = params.getUser();
            source = params.getSource();
            item = params.getItem();
        }

        List<InboxStatus> statusList = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(InboxStatus.class);

                    if (user != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("InboxStatus query - user: " + user);
                        }
                        criteria.add(Restrictions.eq("user", user));
                    }

                    if (source != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("InboxStatus query - source: " + source);
                        }
                        criteria.add(Restrictions.eq("source", source));
                    }

                    if (item != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("InboxStatus query - item: " + item);
                        }
                        criteria.add(Restrictions.eq("item", item));
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    statusList = criteria.list();
                }
                else
                {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled())
            {
                log.debug("Completed retrieve of inboxstatus query. " + ((statusList == null) ? "0" : Integer.toString(statusList.size())) + " results returned.");
            }
        }
        finally
        {
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return statusList;
    }

}
