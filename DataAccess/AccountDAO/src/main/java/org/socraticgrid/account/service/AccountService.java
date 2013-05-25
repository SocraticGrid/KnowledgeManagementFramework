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
package org.socraticgrid.account.service;

import org.socraticgrid.account.dao.PatientProviderItemDao;
import org.socraticgrid.account.dao.UserSessionDao;
import org.socraticgrid.account.model.PatientProviderItem;
import org.socraticgrid.account.model.PatientProviderQueryParams;
import org.socraticgrid.account.model.UserSession;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Sushma
 */
public class AccountService {

    private static Log log = LogFactory.getLog(AccountService.class);

    /**
     * Save address record.
     *
     * @param addr Address object to save.
     */
    public void savePatientProvider(PatientProviderItem ppi) {
        log.debug("Saving a PatientProvider");

        if (ppi != null) {
            if (ppi.getPatientProviderId() != null) {
                log.debug("Performing an update for patient provider: " + ppi.getPatientProviderId());
            } else {
                log.debug("Performing an insert");
            }

        }

        PatientProviderItemDao dao = new PatientProviderItemDao();
        dao.save(ppi);
    }

    /**
     * Saves the user session in the User Session table in the database.
     * 
     * @param userSession
     */
    public void saveUserSession(UserSession userSession) {
        log.debug("Saving a UserSession");

        if (userSession != null) {
            log.debug("Performing an update for user session, token is: " + userSession.getToken());
        }
        else {
            log.debug("Inserting user session");
        }

        UserSessionDao dao = new UserSessionDao();
        dao.save(userSession);
    }

    /**
     * Deletes a user session. To be called from the KMRHttpBindingListener
     * when a session times out.
     *
     * @param userSession
     * @throws AccountServiceException
     */
    public void deleteUserSession(UserSession userSession) throws AccountServiceException {
        if (userSession == null) {
            throw new AccountServiceException("UserSession to delete was null");
        }
        log.debug("Deleting a UserSession for " + userSession.getUserId());

        UserSessionDao dao = new UserSessionDao();
        dao.delete(userSession);
    }

    /**
     * Delete a patientProvider
     *
     * @param ppi patient provider to delete
     * @throws AccountServiceException
     */
    public void deleteAdress(PatientProviderItem ppi) throws AccountServiceException {
        log.debug("Deleting a PatientProvider");
        PatientProviderItemDao dao = new PatientProviderItemDao();

        if (ppi == null) {
            throw new AccountServiceException("PatientProvider to delete was null");
        }

        dao.delete(ppi);
    }

    public UserSession getUserSession(String token) {
        UserSessionDao dao = new UserSessionDao();
        return dao.findById(token);
    }

    /**
     * Retrieve a PatientProvider by identifier
     *
     * @param PatientProviderId PatientProvider identifier
     * @return Retrieved PatientProviderItem
     */
    public PatientProviderItem getPatient(Long providerId) {
        PatientProviderItemDao dao = new PatientProviderItemDao();
        return dao.findById(providerId);
    }

    /**
     * Retrieve a PatientProvider by identifier
     *
     * @param PatientProviderId PatientProvider identifier
     * @return Retrieved PatientProviderItem
     */
    public PatientProviderItem getProvider(Long patientId) {
        PatientProviderItemDao dao = new PatientProviderItemDao();
        return dao.findById(patientId);
    }

    /**
     * Retrieve a list of PatientProviders by PatientProviderQueryParams
     *
     * @param queryParams PatientProviderQueryParams 
     * @return Retrieved List<PatientProviderItem>
     */
    public List<PatientProviderItem> getProvidersByParams(PatientProviderQueryParams queryParams) {
        PatientProviderItemDao dao = new PatientProviderItemDao();
        return dao.findPatient(queryParams);
    }

    /**
     * Retrieves all PatientProvider
     *
     * @return All PatientProvider records
     */
    public List<PatientProviderItem> getAllPatientProvider() {
        PatientProviderItemDao dao = new PatientProviderItemDao();
        return dao.findAll();
    }
}
