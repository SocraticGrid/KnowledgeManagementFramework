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

/**
 *
 * @author cmatser
 */
public class AlertAction
        implements Comparable<AlertAction> {

    private Long actionId;
    private String actionName;
    private String userId;
    private String userName;
    private Boolean userProvider;
    private String message;
    private Date actionTimestamp;
    private AlertTicket ticket;

    /**
     * @return the actionId
     */
    public Long getActionId() {
        return actionId;
    }

    /**
     * @param actionId the actionId to set
     */
    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userProvider
     */
    public Boolean getUserProvider() {
        return userProvider;
    }

    /**
     * @param userProvider the userProvider to set
     */
    public void setUserProvider(Boolean userProvider) {
        this.userProvider = userProvider;
    }

    /**
     * @return the actionName
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * @param actionName the actionName to set
     */
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the actionTimestamp
     */
    public Date getActionTimestamp() {
        return actionTimestamp;
    }

    /**
     * @param actionTimestamp the actionTimestamp to set
     */
    public void setActionTimestamp(Date actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }

    /**
     * @return the ticket
     */
    public AlertTicket getTicket() {
        return ticket;
    }

    /**
     * @param ticket the ticket to set
     */
    public void setTicket(AlertTicket ticket) {
        this.ticket = ticket;
    }

    /**
     * 
     * @param that
     * @return
     * @exception NullPointerException if actionTimestamp or actionName is null.
     */
    public int compareTo(AlertAction that) {

        int compareTo = actionTimestamp.compareTo(that.getActionTimestamp());

        //If dates are equal, check action id (db generated incrementer)
        if (compareTo == 0) {
            if ((actionId != null))
                return that.getActionId().compareTo(actionId);
        }

        return compareTo;
    }
}