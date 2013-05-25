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

import org.socraticgrid.xdsmanager.util.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

/**
 *
 * @author cmatser
 */
public class ObjectDao<E> {

    /**
     * Save a record to the database.
     * Insert if id is null. Update otherwise.
     *
     * @param object object to save.
     */
    void save(E entity)
            throws Exception
    {
        Session sess = null;
        Transaction trans = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.saveOrUpdate(entity);
                }
                else
                {
                    throw new Exception("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                throw new Exception("Session factory was null");
            }
        }
        finally
        {
            if (trans != null)
            {
                try
                {
                    trans.commit();
                }
                catch (Throwable t)
                {
                    throw new Exception("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    throw new Exception("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

    }

    /**
     * Delete an object
     *
     * @param object object to delete
     */
    void delete(E entity)
            throws Exception
    {
        Session sess = null;
        Transaction trans = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    trans = sess.beginTransaction();
                    sess.delete(entity);
                }
                else
                {
                    throw new Exception("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                throw new Exception("Session factory was null");
            }
        }
        finally
        {
            if (trans != null)
            {
                try
                {
                    trans.commit();
                }
                catch (Throwable t)
                {
                    throw new Exception("Failed to commit transaction: " + t.getMessage(), t);
                }
            }
            if (sess != null)
            {
                try
                {
                    sess.close();
                }
                catch (Throwable t)
                {
                    throw new Exception("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

    }

    /**
     * Retrieve a object by identifier
     *
     * @param entityId Entity identifier
     * @return Retrieved object
     */
    E findById(Long entityId, Class entityClass)
            throws Exception
    {
        E entity = null;
        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    entity = (E) sess.get(entityClass, entityId);
                }
                else
                {
                    throw new Exception("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                throw new Exception("Session factory was null");
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
                    throw new Exception("Failed to close session: " + t.getMessage(), t);
                }
            }
        }

        return entity;
    }

    /**
     * Retrieves all objects
     *
     * @return All object records
     */
    @SuppressWarnings("unchecked")
    List<E> findAll(Class entityClass)
            throws Exception
    {
        List<E> entities = null;
        Session sess = null;
        try
        {
            SessionFactory fact = HibernateUtil.getSessionFactory();
            if (fact != null)
            {
                sess = fact.openSession();
                if (sess != null)
                {
                    Criteria criteria = sess.createCriteria(entityClass);
                    entities = criteria.list();
                }
                else
                {
                    throw new Exception("Failed to obtain a session from the sessionFactory");
                }
            }
            else
            {
                throw new Exception("Session factory was null");
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
                    throw new Exception("Failed to close session: " + t.getMessage(), t);
                }
            }
        }
        return entities;
    }

}

