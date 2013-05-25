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
package org.socraticgrid.account.dao;

import org.socraticgrid.account.model.PatientProviderQueryParams;
import org.socraticgrid.account.model.PatientProviderItem;
import org.socraticgrid.account.util.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Sushma
 */
public class PatientProviderItemDao {

    ObjectDao<PatientProviderItem> objectDao = new ObjectDao();
    Log log = LogFactory.getLog(PatientProviderItemDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param item Patient ProviderItem object to save.
     */
    public void save(PatientProviderItem item) {
        System.out.println("Performing  Patient_Provider item save");

        try {
            //Update date
            item.setLastUpdated(new Date());

            objectDao.save(item);
        } catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed patient provider item save");
    }

    /**
     * Delete a patient provider item
     *
     * @param item Patient Provider Item to delete
     */
    public void delete(PatientProviderItem item) {
        log.debug("Performing patient provider item delete");

        try {
            objectDao.delete(item);
        } catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed patient provider item delete");
    }

    /**
     * Retrieve a Patient Provider item by identifier
     *
     * @param patientProviderId Patient ProviderItem identifier
     * @return Retrieved Patient Provider item
     */
    public PatientProviderItem findById(Long patientProviderId) {
        PatientProviderItem item = null;

        log.debug("Performing patient provider item retrieve using id: " + patientProviderId);

        try {
            item = objectDao.findById(patientProviderId, PatientProviderItem.class);
        } catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed patient provider item retrieve by id");

        return item;
    }

    /**
     * Retrieves all patient provider item
     *
     * @return All patient provider item records
     */
    @SuppressWarnings("unchecked")
    public List<PatientProviderItem> findAll() {
        List<PatientProviderItem> items = null;

        log.debug("Performing retrieve of all patient Provider item");

        try {
            items = objectDao.findAll(PatientProviderItem.class);
        } catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed patient Provider item retrieve all");

        return items;
    }

    /**
     * Perform a query for patient provider item
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<PatientProviderItem> findPatient(PatientProviderQueryParams params) {
        log.debug("Beginning patient provider item query");

        Long patientId = null;
        String providerId = "";

        if (params != null) {
            patientId = params.getPatientId();
            providerId = params.getProviderId();
        }

        List<PatientProviderItem> items = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(PatientProviderItem.class);

                    if (patientId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Patient Provider item query - patient provider item user id: " + patientId);
                        }
                        criteria.add(Restrictions.eq("patientId", patientId));
                    }

                    if (providerId != null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Patient Provider item query - Patient Provider item provider id: " + providerId);
                        }
                        criteria.add(Restrictions.eq("providerId", providerId));
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    items = criteria.list();
                } else {
                    log.error("Failed to obtain a session from the sessionFactory");
                }
            } else {
                log.error("Session factory was null");
            }
            if (log.isDebugEnabled()) {
                log.debug("Completed retrieve of Patient Provider item query. " + ((items == null) ? "0" : Integer.toString(items.size())) + " results returned.");
            }

        }
        finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return items;
    }
}
