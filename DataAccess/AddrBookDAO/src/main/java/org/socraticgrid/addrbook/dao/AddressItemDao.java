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

package org.socraticgrid.addrbook.dao;

import org.socraticgrid.addrbook.model.AddressQueryParams;
import org.socraticgrid.addrbook.model.AddressItem;
import org.socraticgrid.addrbook.util.HibernateUtil;
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
 * @author cmatser
 */
public class AddressItemDao {

    ObjectDao<AddressItem> objectDao = new ObjectDao();

    Log log = LogFactory.getLog(AddressItemDao.class);

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param item AddressItem object to save.
     */
    public void save(AddressItem item) {
        log.debug("Performing address item save");

        try {
            //Update date
            item.setLastUpdated(new Date());

            objectDao.save(item);
        }
        catch (Throwable t) {
            log.error("Failure during object save.", t);
        }

        log.debug("Completed address item save");
    }

    /**
     * Delete a address item
     *
     * @param item AddressItem to delete
     */
    public void delete(AddressItem item) {
        log.debug("Performing address item delete");

        try {
            objectDao.delete(item);
        }
        catch (Throwable t) {
            log.error("Failure during object delete.", t);
        }

        log.debug("Completed address item delete");
    }

    /**
     * Retrieve a address item by identifier
     *
     * @param itemId AddressItem identifier
     * @return Retrieved address item
     */
    public AddressItem findById(Long itemId) {
        AddressItem item = null;

        log.debug("Performing address item retrieve using id: " + itemId);

        try {
            item = objectDao.findById(itemId, AddressItem.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findById", t);
        }

        log.debug("Completed address item retrieve by id");

        return item;
    }

    /**
     * Retrieves all address item
     *
     * @return All address item records
     */
    @SuppressWarnings("unchecked")
    public List<AddressItem> findAll() {
        List<AddressItem> items = null;

        log.debug("Performing retrieve of all address item");

        try {
            items = objectDao.findAll(AddressItem.class);
        }
        catch (Throwable t) {
            log.error("Failure during object findAll", t);
        }

        log.debug("Completed address item retrieve all");

        return items;
    }

    /**
     * Perform a query for address item
     *
     * @param params Query parameters
     * @return Query results
     */
    @SuppressWarnings("unchecked")
    public List<AddressItem> findAddresses(AddressQueryParams params) {
        log.debug("Beginning address item query");

        String userId = null;
        String classId = null;

        if (params != null) {
            userId = params.getUserId();
            classId = params.getClassId();
        }

        List<AddressItem> items = null;
        Session sess = null;
        try {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(AddressItem.class);

                    if (userId != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("Address item query - address item user id: " + userId);
                        }
                        criteria.add(Restrictions.eq("userId", userId));
                    }

                    if (classId != null)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.debug("Address item query - address item class id: " + classId);
                        }
                        criteria.add(Restrictions.eq("classId", classId));
                    }

                    criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
                    items = criteria.list();
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
                log.debug("Completed retrieve of address item query. " + ((items == null) ? "0" : Integer.toString(items.size())) + " results returned.");
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
        return items;
    }

}

