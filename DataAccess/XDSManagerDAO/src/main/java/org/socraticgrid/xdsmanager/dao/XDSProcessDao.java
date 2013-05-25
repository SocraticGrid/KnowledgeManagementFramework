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
package org.socraticgrid.xdsmanager.dao;

import org.socraticgrid.xdsmanager.model.XDSProcess;
import org.socraticgrid.xdsmanager.model.ProcessQueryParams;
import org.socraticgrid.xdsmanager.util.HibernateUtil;
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
public class XDSProcessDao {

    ObjectDao<XDSProcess> objectDao = new ObjectDao();
    Log log = LogFactory.getLog(XDSProcessDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param process XDSProcess object to save.
     */
    public void save(XDSProcess process) {
        log.debug("Performing process save");

        try {
            objectDao.save(process);
        } catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed process save");
    }

    /**
     * Delete a process
     *
     * @param process XDSProcess to delete
     */
    public void delete(XDSProcess process) {
        log.debug("Performing process delete");

        try {
            objectDao.delete(process);
        } catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed process delete");
    }

    /**
     * Retrieve a process by identifier
     *
     * @param processId XDSProcess identifier
     * @return Retrieved process
     */
    public XDSProcess findById(Long processId) {
        XDSProcess process = null;

        log.debug("Performing process retrieve using id: " + processId);

        try {
            process = objectDao.findById(processId, XDSProcess.class);
        } catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed process retrieve by id");

        return process;
    }

    /**
     * Retrieves all processes
     *
     * @return All process records
     */
    @SuppressWarnings("unchecked")
    public List<XDSProcess> findAll() {
        List<XDSProcess> processes = null;

        log.debug("Performing retrieve of all processes");

        try {
            processes = objectDao.findAll(XDSProcess.class);
        } catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed process retrieve all");

        return processes;
    }

    /**
     * Perform a query for processes
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<XDSProcess> findProcesses(ProcessQueryParams params) {
        log.debug("Beginning process query");

        String userId = null;
        String patientId = null;
        String ticket = null;

        if (params != null) {
            userId = params.getUserId();
            patientId = params.getPatientId();
            ticket = params.getTicket();
        }

        List<XDSProcess> processes = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(XDSProcess.class);

                    if (userId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Process query - userId: " + userId);
                        }

                        criteria.add(Restrictions.eq("userId", userId));
                    }

                    if (patientId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Process query - patientId: " + patientId);
                        }

                        criteria.add(Restrictions.eq("patientId", patientId));
                    }

                    if (ticket != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Process query - ticket: " + ticket);
                        }

                        criteria.add(Restrictions.eq("ticket", ticket));
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    processes = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed retrieve of processes query. " + ((processes == null) ? "0" : Integer.toString(processes.size())) + " results returned.");
            }
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return processes;
    }
}
