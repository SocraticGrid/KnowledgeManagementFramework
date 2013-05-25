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
package org.socraticgrid.nhinclib;

/**
 *
 * @author Jon Hoppesch
 */
public class NhincConstants {
    // Property File Constants

    public static final String GATEWAY_PROPERTY_FILE = "gateway";
    public static final String HOME_COMMUNITY_ID_PROPERTY = "localHomeCommunityId";
    public static final String NHINC_PROPERTIES_DIR = "NHINC_PROPERTIES_DIR";
    public static final String SPRING_CONFIG_FILE = "ComponentProxyConfig.xml";

    // SAML Constants
    public static final String ACTION_PROP = "action";
    public static final String RESOURCE_PROP = "resource";
    public static final String PURPOSE_CODE_PROP = "purposeForUseRoleCode";
    public static final String PURPOSE_SYST_PROP = "purposeForUseCodeSystem";
    public static final String PURPOSE_SYST_NAME_PROP = "purposeForUseCodeSystemName";
    public static final String PURPOSE_DISPLAY_PROP = "purposeForUseDisplayName";
    public static final String USER_FIRST_PROP = "userFirstName";
    public static final String USER_MIDDLE_PROP = "userMiddleName";
    public static final String USER_LAST_PROP = "userLastName";
    public static final String USER_NAME_PROP = "userName";
    public static final String USER_ORG_PROP = "userOrganization";
    public static final String USER_CODE_PROP = "userRoleCode";
    public static final String USER_SYST_PROP = "userRoleCodeSystem";
    public static final String USER_SYST_NAME_PROP = "userRoleCodeSystemName";
    public static final String USER_DISPLAY_PROP = "userRoleCodeDisplayName";
    public static final String EXPIRE_PROP = "expirationDate";
    public static final String SIGN_PROP = "signDate";
    public static final String CONTENT_REF_PROP = "contentReference";
    public static final String CONTENT_PROP = "content";
    public static final String AUDIT_QUERY_ACTION = "queryAuditLog";
    public static final String NOTIFY_ACTION = "notify";
    public static final String SUBSCRIBE_ACTION = "subscribe";
    public static final String UNSUBSCRIBE_ACTION = "unsubscribe";

    // Audit Logging Constants
    public static final String AUDIT_REPO_SERVICE_NAME = "auditrepository";
    public static final String AUDIT_LOG_SERVICE_NAME = "auditlog";
    public static final String AUDIT_QUERY_SERVICE_NAME = "auditquery";
    public static final String AUDIT_LOG_ADAPTER_SERVICE_NAME = "adapterauditquerypassthrough";
    public static final String AUDIT_LOG_INBOUND_DIRECTION = "Inbound";
    public static final String AUDIT_LOG_OUTBOUND_DIRECTION = "Outbound";
    public static final String AUDIT_LOG_ENTITY_INTERFACE = "Entity";
    public static final String AUDIT_LOG_NHIN_INTERFACE = "Nhin";
    public static final String AUDIT_LOG_ADAPTER_INTERFACE = "Adapter";
    public static final String AUDIT_LOG_SERVICE_PROPERTY = "serviceAuditRepository";
    public static final String AUDIT_LOG_SERVICE_PASSTHRU_PROPERTY = "auditRepositoryPassthrough";
    public static final String AUDIT_DISABLED_ACK_MSG = "Audit Service is not enabled";

    // Policy Engine Constants
    public static final String POLICYENGINE_DTE_SERVICE_NAME = "policyenginedte";
    public static final String POLICYENGINE_SERVICE_NAME = "policyengineservice";
    public static final String POLICYENGINE_FACADE_SERVICE_NAME = "policyenginefacade";
    public static final String POLICYENGINE_INBOUND_DIRECTION = "Inbound";
    public static final String POLICYENGINE_OUTBOUND_DIRECTION = "Outbound";
    public static final String POLICY_PERMIT = "Permit";

    // HIEM - NHIN interface
    public static final String HIEM_SUBSCRIPTION_MANAGER_SERVICE_NAME = "subscriptionmanager";
    public static final String HIEM_SUBSCRIBE_SERVICE_NAME = "notificationproducer";
    public static final String HIEM_NOTIFY_SERVICE_NAME = "notificationconsumer";

    // HIEM - entity interface
    public static final String HIEM_SUBSCRIBE_ENTITY_SERVICE_NAME = "entitynotificationproducer";
    public static final String HIEM_UNSUBSCRIBE_ENTITY_SERVICE_NAME = "entitysubscriptionmanager";
    public static final String HIEM_NOTIFY_ENTITY_SERVICE_NAME = "entitynotificationconsumer";

    // HIEM - adapter interface
    public static final String HIEM_SUBSCRIBE_ADAPTER_SERVICE_NAME = "adapternotificationproducerpassthrough";
    public static final String HIEM_UNSUBSCRIBE_ADAPTER_SERVICE_NAME = "adaptersubscriptionmanagerpassthrough";
    public static final String HIEM_NOTIFY_ADAPTER_SERVICE_NAME = "adapternotificationconsumerpassthrough";
    
    public static final String HIEM_SUBSCRIPTION_SERVICE_PROPERTY = "serviceSubscription";
    public static final String HIEM_SUBSCRIPTION_SERVICE_PASSTHRU_PROPERTY = "subscriptionPassthrough";
    public static final String HIEM_NOTIFY_SERVICE_PROPERTY = "serviceNotify";
    public static final String HIEM_NOTIFY_SERVICE_PASSTHRU_PROPERTY = "notifyPassthrough";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_PROPERTY = "hiem.AdapterSubscriptionMode";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_SUBSCRIPTIONS = "createchildsubscription";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_FORWARD = "forward";
    public static final String HIEM_ADAPTER_SUBSCRIPTION_MODE_CREATE_CHILD_DISABLED = "disabled";

    // MPI constants
    public static final String ADAPTER_MPI_SERVICE_NAME = "adaptercomponentmpiservice";

    // SOAP Headers
    public static final String HTTP_REQUEST_ATTRIBUTE_SOAPMESSAGE = "SoapMessage";
    public static final String HIEM_SUBSCRIBE_SOAP_HDR_ATTR_TAG = "subscribeSoapMessage";
    public static final String HIEM_UNSUBSCRIBE_SOAP_HDR_ATTR_TAG = "unsubscribeSoapMessage";
    public static final String HIEM_NOTIFY_SOAP_HDR_ATTR_TAG = "notifySoapMessage";


    private NhincConstants() {
    }
}
