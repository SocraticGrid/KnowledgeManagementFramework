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

import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.alertmanager.util.HibernateUtil;
import org.socraticgrid.util.CommonUtil;
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
public class RiskModelFavoriteDao {
    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

    ObjectDao<RiskModelFavorite> objectDao = new ObjectDao();
    Log log = LogFactory.getLog(RiskModelFavorite.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param alertStatus AlertStatus object to save.
     */
    public void save(RiskModelFavorite riskModelFavorite) {
        log.debug("Performing alertStatus save");

        try {
            objectDao.save(riskModelFavorite);
        } catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

    log.debug("Completed riskModelFavorite save");
    }

    /**
     * Delete a alertStatus
     *
     * @param alertStatus AlertStatus to delete
     */
    public void delete(RiskModelFavorite riskModelFavorite) {
        log.debug("Performing RiskModelFavorite delete");

        try {
            objectDao.delete(riskModelFavorite);
        } catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed RiskModelFavorite delete");
    }

    /**
     * Retrieve a alertStatus by identifier
     *
     * @param alertStatusId AlertStatus identifier
     * @return Retrieved alertStatus
     */
    public RiskModelFavorite findById(Long riskModelFavoriteId) {
        RiskModelFavorite riskModelFavorite = null;

        log.debug("Performing RiskModelFavorite retrieve using id: " + riskModelFavoriteId);

        try {
            riskModelFavorite = objectDao.findById(riskModelFavoriteId, RiskModelFavorite.class);
        } catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed alertStatus retrieve by id");

        return riskModelFavorite;
    }

    public List findByModelId(String modelId) {
        List riskModelFavorites = null;
        Session sess = null;

        // Return a null list since no modelId was given.
        if (CommonUtil.strNullorEmpty(modelId)) {
            log.info("NOTE:  findByModelId(String modelId) was called with NULL modelId.");
            return null;
        }
        
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(RiskModelFavorite.class);
                    if (modelId != null) {
                        criteria.add(Restrictions.eq("modelId", modelId));
                    }
                //    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    riskModelFavorites = criteria.list();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Performing RiskModelFavorite retrieve using modelId: " + modelId);


        return riskModelFavorites;
    }

    public List findByProviderPatient(String providerId, String patientId) {
        List riskModelFavorites = null;
        Session sess = null;

        // Return a null list since no modelId was given.
        if (CommonUtil.strNullorEmpty(providerId)||CommonUtil.strNullorEmpty(patientId)) {
            log.debug("NOTE:  findByModelId(String modelId) was called with NULL providerId or NULL patientId.");
            return null;
        }

        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null) {
                sess = fact.openSession();
                if (sess != null) {
                    Criteria criteria = sess.createCriteria(RiskModelFavorite.class);
                    criteria.add(Restrictions.eq("providerId", providerId));
                    criteria.add(Restrictions.eq("patientId", patientId));
                   
                    riskModelFavorites = criteria.list();
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (sess != null) {
                try {
                    sess.close();
                } catch (Throwable t) {
                    log.error("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        log.debug("Performing RiskModelFavorite retrieve using providerId="+providerId+" and patientId="+patientId);


        return riskModelFavorites;
    }

    /**
     * Retrieves all alertStatus
     *
     * @return All alertStatus records
     */
    @SuppressWarnings("unchecked")
    public List<RiskModelFavorite> findAll() {
        List<RiskModelFavorite> riskModelFavorites = null;

        log.debug("Performing retrieve of all alertStatus");

        try {
            riskModelFavorites = objectDao.findAll(RiskModelFavorite.class);
        } catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed alertStatus retrieve all");

        return riskModelFavorites;
    }
}
