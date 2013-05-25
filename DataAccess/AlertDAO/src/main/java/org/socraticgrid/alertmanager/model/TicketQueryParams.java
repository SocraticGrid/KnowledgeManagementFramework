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

package org.socraticgrid.alertmanager.model;

import java.util.Set;

/**
 * Parameter object for ticket queries
 * 
 * @author cmatser
 */
public class TicketQueryParams
{
    private String ticketUniqueId;
    private Integer escalationPeriodGT;
    private String action;
    private String actionUserId;
    private String patientId;
    private String type;
    private boolean archive;
    private boolean folder;
    private Set<AlertContact> providerList;
    private int deleteFlag;

    /**
     * @return the folder whether it belongs to starred folder or not
     */
    public boolean isFolder() {
        return folder;
    }

    /**
     * @param folder to set
     */

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    /**
     * @return the ticketUniqueId
     */
    public boolean isArchive() {
        return archive;
    }

    /**
     * @param archive to set
     */
    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    /**
     * @return the ticketUniqueId
     */
    public String getTicketUniqueId() {
        return ticketUniqueId;
    }

    /**
     * @param ticketUniqueId the ticketUniqueId to set
     */
    public void setTicketUniqueId(String ticketUniqueId) {
        this.ticketUniqueId = ticketUniqueId;
    }

    /**
     * @return the escalationPeriod
     */
    public Integer getEscalationPeriodGT() {
        return escalationPeriodGT;
    }

    /**
     * @param escalationPeriod the escalationPeriod to set
     */
    public void setEscalationPeriodGT(Integer escalationPeriodGT) {
        this.escalationPeriodGT = escalationPeriodGT;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the actionUserId
     */
    public String getActionUserId() {
        return actionUserId;
    }

    /**
     * @param actionUserId the actionUserId to set
     */
    public void setActionUserId(String actionUserId) {
        this.actionUserId = actionUserId;
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public Set<AlertContact> getProviderList() {
        return providerList;
    }

    public void setProviderList(Set<AlertContact> providerList) {
        this.providerList = providerList;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

}