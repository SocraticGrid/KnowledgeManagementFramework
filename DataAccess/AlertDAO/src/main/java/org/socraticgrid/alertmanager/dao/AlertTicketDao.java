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
package org.socraticgrid.alertmanager.dao;

import org.socraticgrid.alertmanager.model.AlertStatus;
import org.socraticgrid.alertmanager.model.AlertTicket;
import org.socraticgrid.alertmanager.model.TicketQueryParams;
import org.socraticgrid.alertmanager.util.HibernateUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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
public class AlertTicketDao {

    ObjectDao<AlertTicket> objectDao = new ObjectDao();
    Log log = LogFactory.getLog(AlertTicketDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param ticket AlertTicket object to save.
     */
    public void save(AlertTicket ticket) {
        log.debug("Performing ticket save");

        try {
            objectDao.save(ticket);
        } catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed ticket save");
    }

    /**
     * Delete a ticket
     *
     * @param ticket AlertTicket to delete
     */
    public void delete(AlertTicket ticket) {
        log.debug("Performing ticket delete");

        try {
            objectDao.delete(ticket);
        } catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed ticket delete");
    }

    /**
     * Retrieve a ticket by identifier
     *
     * @param ticketId AlertTicket identifier
     * @return Retrieved ticket
     */
    public AlertTicket findById(Long ticketId) {
        AlertTicket ticket = null;

        log.debug("Performing ticket retrieve using id: " + ticketId);

        try {
            ticket = objectDao.findById(ticketId, AlertTicket.class);
        } catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed ticket retrieve by id");

        return ticket;
    }

    /**
     * Retrieves all tickets
     *
     * @return All ticket records
     */
    @SuppressWarnings("unchecked")
    public List<AlertTicket> findAll() {
        List<AlertTicket> tickets = null;

        log.debug("Performing retrieve of all tickets");

        try {
            tickets = objectDao.findAll(AlertTicket.class);
        } catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed ticket retrieve all");

        return tickets;
    }

    /**
     * Perform a query for tickets
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<AlertTicket> findTickets(TicketQueryParams params) {
        log.debug("Beginning ticket query");

        String ticketUniqueId = null;
        Integer escalationPeriodGT = null;
        String action = null;
        String actionUserId = null;
        String patientId = null;
        String type = null;
        Boolean archive = null;
        Boolean deleteFlag = null;

        if (params != null) {
            ticketUniqueId = params.getTicketUniqueId();
            escalationPeriodGT = params.getEscalationPeriodGT();
            patientId = params.getPatientId();
            type = params.getType();
            
            archive = params.isArchive();
            deleteFlag = params.getDeleteFlag();
            actionUserId = params.getActionUserId();
        }

        List<AlertTicket> tickets = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(AlertTicket.class);

                    if (ticketUniqueId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Ticket query - ticket unique id: " + ticketUniqueId);
                        }
                        criteria.add(Restrictions.eq("ticketUniqueId", ticketUniqueId));
                    }

                    if (escalationPeriodGT != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Ticket query - escalationPeriod greater than: " + escalationPeriodGT);
                        }
                        criteria.add(Restrictions.gt("escalationPeriod", escalationPeriodGT));
                    }

                    if (patientId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Ticket query - patientId: " + patientId);
                        }

                        criteria.add(Restrictions.eq("patientUnitNumber", patientId));
                    }

                    if (type != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Ticket query - type: " + type);
                        }

                        criteria.add(Restrictions.eq("type", type));
                    }

                    //FILTER for AlertContact
                    if (actionUserId != null) {
                        
                        if (log.isDebugEnabled()) {
                            log.debug("Ticket query - Recipient id: " + actionUserId);
                        }

                        criteria.createCriteria("providers").add(Restrictions.eq("userId", actionUserId));
                    }
                    
                    //FILTER for AlertStatus
                    if ((archive != null) && (deleteFlag != null)) {
                        
                        if (actionUserId != null) {
                            criteria.createCriteria("status").add(
                               Restrictions.and(
                                    Restrictions.and(Restrictions.eq("archive", archive.booleanValue()), 
                                                     Restrictions.eq("deleted", deleteFlag.booleanValue()))
                                    ,Restrictions.eq("userId", actionUserId) ));
                        } else {
                            criteria.createCriteria("status").add(
                                    Restrictions.and(Restrictions.eq("archive", archive.booleanValue()), 
                                                     Restrictions.eq("deleted", deleteFlag.booleanValue()) ));
                        }
                    } else if ((archive != null) && (deleteFlag == null)) {
                        
                        if (actionUserId != null) {
                            criteria.createCriteria("status").add(
                                    Restrictions.and(Restrictions.eq("archive", archive.booleanValue()), 
                                                     Restrictions.eq("userId", actionUserId) ));
                        } else {
                            
                            criteria.createCriteria("status").add(
                                    Restrictions.eq("archive", archive.booleanValue()) );
                        }
                    } else if ((archive == null) && (deleteFlag != null)) {
                        
                        if (actionUserId != null) {
                            criteria.createCriteria("status").add(
                                    Restrictions.and(Restrictions.eq("deleted", deleteFlag.booleanValue()), 
                                                     Restrictions.eq("userId", actionUserId) ));
                        } else {
                            
                            criteria.createCriteria("status").add(
                                    Restrictions.eq("deleted", deleteFlag.booleanValue()) );
                        }
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    tickets = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed retrieve of tickets query. " + ((tickets == null) ? "0" : Integer.toString(tickets.size())) + " results returned.");
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
        return tickets;
    }
}
