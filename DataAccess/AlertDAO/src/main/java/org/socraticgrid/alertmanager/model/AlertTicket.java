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

package org.socraticgrid.alertmanager.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 *
* @author cmatser
 */
public class AlertTicket {

    private Long ticketId;
    private String ticketUniqueId;
    private Date alertTimestamp;
    private Integer escalationPeriod;
    private Set<AlertContact> providers;
    private Set<AlertStatus> status;
    private Long alertId;
    private String type;
    private String description;
    private String alertOriginator;
    private String payload;
    private String priority;
    private String patientUnitNumber;
    private String patientName;
    private String patientSex;
    private String userLdap;
    private String ptFMPSSN;
    private Set<AlertAction> actionHistory;

    /**
     * @return the ticketId
     */
    public Long getTicketId() {
        return ticketId;
    }

    /**
     * @param ticketId the ticketId to set
     */
    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
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
     * @return the alertTimestamp
     */
    public Date getAlertTimestamp() {
        return alertTimestamp;
    }

    /**
     * @param alertTimestamp the alertTimestamp to set
     */
    public void setAlertTimestamp(Date alertTimestamp) {
        this.alertTimestamp = alertTimestamp;
    }

    /**
     * @return the escalationPeriod
     */
    public Integer getEscalationPeriod() {
        return escalationPeriod;
    }

    /**
     * @param escalationPeriod the escalationPeriod to set
     */
    public void setEscalationPeriod(Integer escalationPeriod) {
        this.escalationPeriod = escalationPeriod;
    }

    /**
     * @return the providers
     */
    public Set<AlertContact> getProviders() {
        if (providers == null) {
            providers = new HashSet<AlertContact>();
        }

        return providers;
    }

    /**
     * @param providers the providers to set
     */
    public void setProviders(Set<AlertContact> providers) {
        this.providers = providers;
    }

    /**
     * @return the status
     */
    public Set<AlertStatus> getStatus() {
        if (status == null) {
            status = new HashSet<AlertStatus>();
        }

        return status;
    }

    /**
     * @param providers the status to set
     */
    public void setStatus(Set<AlertStatus> status) {
        this.status = status;
    }


    /**
     * @return the alertId
     */
    public Long getAlertId() {
        return alertId;
    }

    /**
     * @param alertId the alertId to set
     */
    public void setAlertId(Long alertId) {
        this.alertId = alertId;
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

    /**
     * @return the payload
     */
    public String getPayload() {
        return payload;
    }

    /**
     * @param payload the payload to set
     */
    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the alertOriginator
     */
    public String getAlertOriginator() {
        return alertOriginator;
    }

    /**
     * @param alertOriginator the alertOriginator to set
     */
    public void setAlertOriginator(String alertOriginator) {
        this.alertOriginator = alertOriginator;
    }

    /**
     * @return the priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * @return the patientUnitNumber
     */
    public String getPatientUnitNumber() {
        return patientUnitNumber;
    }

    /**
     * @param patientUnitNumber the patientUnitNumber to set
     */
    public void setPatientUnitNumber(String patientUnitNumber) {
        this.patientUnitNumber = patientUnitNumber;
    }

    /**
     * @return the patientName
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * @param patientName the patientName to set
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * @return the patientSex
     */
    public String getPatientSex() {
        return patientSex;
    }

    /**
     * @param patientSex the patientSex to set
     */
    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    /**
     * @return the patientFMPSSN
     */
    public String getUserLdap() {
        return userLdap;
    }

    /**
     * @param patientFMPSSN the patientFMPSSN to set
     */
    public void setUserLdap(String userLdap) {
        this.userLdap = userLdap;
    }



    /**
     * @return the actionHistory
     */
    public Set<AlertAction> getActionHistory() {

        if (actionHistory == null) {
            actionHistory = new TreeSet<AlertAction>();
        }

        return actionHistory;
    }

    /**
     * @param actionHistory the actionHistory to set
     */
    public void setActionHistory(Set<AlertAction> actionHistory) {
        this.actionHistory = actionHistory;
    }


    public String getPtFMPSSN() {
        return ptFMPSSN;
    }

    public void setPtFMPSSN(String ptFMPSSN) {
        this.ptFMPSSN = ptFMPSSN;
    }
    
    
    @Override
    public String toString() {
        return "AlertTicket{" + "ticketId=" + ticketId + ", ticketUniqueId=" + ticketUniqueId + ", alertTimestamp=" + alertTimestamp + ", escalationPeriod=" + escalationPeriod + ", providers=" + providers + ", status=" + status + ", alertId=" + alertId + ", type=" + type + ", description=" + description + ", alertOriginator=" + alertOriginator + ", payload=" + payload + ", priority=" + priority + ", patientUnitNumber=" + patientUnitNumber + ", patientName=" + patientName + ", patientSex=" + patientSex + ", userLdap=" + userLdap + ", actionHistory=" + actionHistory + '}';
    }
}
