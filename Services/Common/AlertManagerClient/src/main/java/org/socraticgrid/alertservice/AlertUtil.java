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

package org.socraticgrid.alertservice;

import org.socraticgrid.alertmanager.model.AlertAction;
import org.socraticgrid.alertmanager.model.AlertTicket;
import org.socraticgrid.alertmanager.service.ActionConstants;
import org.socraticgrid.alertmanager.service.AlertService;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class to help with service calls.
 *
 * @author cmatser
 */
public class AlertUtil {

    public static Log log = LogFactory.getLog(AlertUtil.class);

    /**
     * Determine if the action is allowed for the ticket.
     *
     * @param userId
     * @param action
     * @param ticket
     * @return
     */
    public static boolean isActionAllowed(String userId, String action, AlertTicket ticket) {

        //Make sure action is valid
        if (!AlertService.getValidActions().contains(action)) {
            log.debug("Provided action is not valid: " + action);
            return false;
        }

        //Get current actions
        Set<AlertAction> history = ticket.getActionHistory();

        //No more actions after these
        for (AlertAction a : history) {
            if (ActionConstants.ACTION_ACCEPT.equals(a.getActionName())) {
                log.debug("Alert has been accepted, " + action + " not allowed.");
                return false;
            }
            if (ActionConstants.ACTION_ACKNOWLEDGE.equals(a.getActionName())) {
                log.debug("Alert has been acknowledged, " + action + " not allowed.");
                return false;
            }
            if (ActionConstants.ACTION_REJECT.equals(a.getActionName())) {
                log.debug("Alert has been rejected, " + action + " not allowed.");
                return false;
            }
            if (ActionConstants.ACTION_DISCARD.equals(a.getActionName())) {
                log.debug("Alert has been discarded, " + action + " not allowed.");
                return false;
            }
        }

        //Further, Escalation cannot happen under these circumstances
        if (ActionConstants.ACTION_ESCALATE.equals(action)
                || ActionConstants.ACTION_MANUAL_ESCALATE.equals(action)) {

            if (ticket.getEscalationPeriod() <= 0) {
                log.debug("Escalation is not configured for this alert, " + action + " not allowed.");
                return false;
            }

            for (AlertAction a : history) {
                if (ActionConstants.ACTION_ESCALATE.equals(a.getActionName())) {
                    log.debug("Alert has already been escalated, " + action + " not allowed.");
                    return false;
                }
                if (ActionConstants.ACTION_MANUAL_ESCALATE.equals(a.getActionName())) {
                    log.debug("Alert has been escalated, " + action + " not allowed.");
                    return false;
                }
                if (ActionConstants.ACTION_HOLD.equals(a.getActionName())) {
                    log.debug("Alert is on hold, " + action + " not allowed.");
                    return false;
                }
            }
        }

        //Original alerted user is not allowed any further action
        //  after manual escalation, unless user escalated to themself
        Iterator<AlertAction> historyIter = history.iterator();
        while (historyIter.hasNext()) {
            AlertAction a = historyIter.next();
            if (ActionConstants.ACTION_MANUAL_ESCALATE.equals(a.getActionName())
                    && (a.getUserId() != null)
                    && a.getUserId().equals(userId)) {
                //Go through the rest of the history to see if this user is one
                //  of the users escalated to
                while (historyIter.hasNext()) {
                    AlertAction actionAfterManualEscalate = historyIter.next();
                    if (ActionConstants.ACTION_ALERT.equals(actionAfterManualEscalate.getActionName())
                            && actionAfterManualEscalate.getUserId().equals(userId)) {
                        //Action is allowed because user is in the list of notifed
                        //  users, even after manual escalation
                        return true;
                    }
                }

                log.debug("Alert has been manually escalated, " + action + " not allowed for this user.");
                return false;
            } //if manual escalate by user
        }

        //No duplicate actions allowed for the same user
        for (AlertAction a : history) {
            if (a.getActionName().equals(action) && a.getUserId().equals(userId)) {
                log.debug("Alert already has " + action + " for user: " + userId);
                return false;
            }
        }

        //If we get here, action is allowed
        return true;
    }

    /**
     * Determine if the alert ticket is closed.
     *
     * @param userId
     * @param action
     * @param ticket
     * @return
     */
    public static boolean isTickedClosed(AlertTicket ticket) {

        //Get current actions
        Set<AlertAction> history = ticket.getActionHistory();

        //No more actions after these
        for (AlertAction a : history) {
            if (ActionConstants.ACTION_ACCEPT.equals(a.getActionName())) {
                return true;
            }
            if (ActionConstants.ACTION_ACKNOWLEDGE.equals(a.getActionName())) {
                return true;
            }
            if (ActionConstants.ACTION_REJECT.equals(a.getActionName())) {
                return true;
            }
            if (ActionConstants.ACTION_DISCARD.equals(a.getActionName())) {
                return true;
            }
        }

        //If we get here, ticket is open
        return false;
    }

}
