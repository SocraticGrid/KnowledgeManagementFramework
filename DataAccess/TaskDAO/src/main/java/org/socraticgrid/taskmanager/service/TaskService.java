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

package org.socraticgrid.taskmanager.service;

import org.socraticgrid.taskmanager.dao.SpecificationDao;
import org.socraticgrid.taskmanager.dao.TaskServiceRefDao;
import org.socraticgrid.taskmanager.dao.TaskTypeDao;
import org.socraticgrid.taskmanager.model.Specification;
import org.socraticgrid.taskmanager.model.TaskServiceRef;
import org.socraticgrid.taskmanager.model.TaskType;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class TaskService {

    private static Log log = LogFactory.getLog(TaskService.class);

    /**
     * Save a task record.
     *
     * @param task Task object to save.
     */
    public void saveTask(TaskType task)
    {
        log.debug("Saving a task");

        if (task != null)
        {
            if (task.getTaskTypeId() != null)
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Performing an update for task: " + task.getTaskTypeId());
                }

                TaskType eTask = getTask(task.getTaskTypeId());
                if (eTask != null)
                {
                    // Delete existing specifications
                    Set<Specification> specs = eTask.getSpecifications();
                    if ((specs != null) && !specs.isEmpty())
                    {
                        SpecificationDao specDao = new SpecificationDao();
                        for (Specification spec : specs)
                        {
                            specDao.delete(spec);
                            spec.setSpecificationId(null);
                        }
                    }

                    // Reset specification identifiers
                    specs = task.getSpecifications();
                    if ((specs != null) && !specs.isEmpty())
                    {
                        for (Specification spec : specs)
                        {
                                spec.setSpecificationId(null);
                        }
                    }
                }
            }
            else
            {
                log.debug("Performing an insert");
            }

        }

        TaskTypeDao dao = new TaskTypeDao();
        dao.save(task);
    }

    /**
     * Delete a task
     *
     * @param task TaskType to delete
     * @throws TaskServiceException
     */
    public void deleteTask(TaskType task) throws TaskServiceException
    {
        log.debug("Deleting a task");
        TaskTypeDao dao = new TaskTypeDao();

        if (task == null)
        {
            throw new TaskServiceException("Task to delete was null");
        }

        dao.delete(task);
    }

    /**
     * Retrieve a task by identifier
     *
     * @param taskTypeId Task identifier
     * @return Retrieved task
     */
    public TaskType getTask(Long taskTypeId)
    {
        TaskTypeDao dao = new TaskTypeDao();
        return dao.findById(taskTypeId);
    }

    /**
     * Retrieves all tasks
     *
     * @return All task records
     */
    public List<TaskType> getAllTasks()
    {
        TaskTypeDao dao = new TaskTypeDao();
        return dao.findAll();
    }

    /**
     * Save a service ref record.
     *
     * @param serviceRef object to save.
     */
    public void saveServiceRef(TaskServiceRef serviceRef)
    {
        log.debug("Saving a task service ref");

        if (serviceRef != null)
        {
            if (serviceRef.getTaskServiceRefId() != null)
            {
                log.debug("Performing an update for service ref: " + serviceRef.getTaskServiceRefId());
            }
            else
            {
                log.debug("Performing an insert");
            }
        }

        TaskServiceRefDao dao = new TaskServiceRefDao();
        dao.save(serviceRef);
    }

    /**
     * Delete a service ref
     *
     * @param serviceRef record to delete
     * @throws TaskServiceException
     */
    public void deleteServiceRef(TaskServiceRef serviceRef) throws TaskServiceException
    {
        log.debug("Deleting a service ref");
        TaskServiceRefDao dao = new TaskServiceRefDao();

        if (serviceRef == null)
        {
            throw new TaskServiceException("Service ref to delete was null");
        }

        dao.delete(serviceRef);
    }

    /**
     * Retrieve a service ref by identifier
     *
     * @param taskServiceRefId identifier
     * @return Retrieved record
     */
    public TaskServiceRef getServiceRef(Long taskServiceRefId)
    {
        TaskServiceRefDao dao = new TaskServiceRefDao();
        return dao.findById(taskServiceRefId);
    }

    /**
     * Retrieves all service refs
     *
     * @return All records
     */
    public List<TaskServiceRef> getAllServiceRefs()
    {
        TaskServiceRefDao dao = new TaskServiceRefDao();
        return dao.findAll();
    }

}
