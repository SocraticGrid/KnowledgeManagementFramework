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

package org.socraticgrid.connectmgr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.socraticgrid.properties.PropertyAccessor;

import org.socraticgrid.connectmgr.data.CMEprInfo;

/**
 * This class has utilities for constructing the information needed for an endpoint reference
 * object.  It retrieves the information based on a uniform service name from the
 * connectionsEPR.properties file.
 * 
 * @author Les Westberg
 */
public class CMEprUtil
{
    private static Log log = LogFactory.getLog(CMEprUtil.class);
    private static final String EPR_PROPERTY_FILE_NAME = "connectionEPR";
    private static final String EPR_NAMESPACE_URI = "NamespaceURI";
    private static final String EPR_PORT_NAME = "PortName";
    private static final String EPR_SERVICE_NAME = "ServiceName";
    private static final String EPR_NAMESPACE_PREFIX = "NamespacePrefix";

    
    /**
     * This method returns specific EPR data about a service.  The information is
     * in the connectionEPR.properties file.  
     * 
     * @param sServiceName The Uniform Service name - this is used as a key to the properties in 
     *                     the property file that are specific to this service.
     * @param sEPRKey The specific key that is being wanted.
     * @return The value of that key for that service.
     * @throws ConnectionManagerException 
     */
    public static String getServiceSpecificEPRInfo(String sServiceName, String sEPRKey)
        throws ConnectionManagerException
    {
        String sKey = sServiceName + "." + sEPRKey;
        String sValue = "";
        try
        {
            sValue = PropertyAccessor.getProperty(EPR_PROPERTY_FILE_NAME, sKey);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Failed to retrieve property: '" + sKey + "' from " + 
                                   EPR_PROPERTY_FILE_NAME + ".properties file.  Exception: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new ConnectionManagerException(sErrorMessage, e);
        }
        if (sValue == null)
        {
            sValue = "";
        }
        
        return sValue;
    }

    /**
     * This method creates an endpoint for the given service name and URL.
     * 
     * @param sUniformServiceName The service name for the service.
     * @return The Endpoint reference to be returned.
     * @throws ConnectionManagerException 
     */
    public static CMEprInfo createEPR(String sUniformServiceName)
        throws ConnectionManagerException
    {
        if ((sUniformServiceName == null) || (sUniformServiceName.length() <= 0))
        {
            return null;
        }
        
        CMEprInfo oEpr = new CMEprInfo();

        oEpr.setUniformServiceName(sUniformServiceName);
        oEpr.setNamespacePrefix(getServiceSpecificEPRInfo(sUniformServiceName, EPR_NAMESPACE_PREFIX));
        oEpr.setNamespaceURI(getServiceSpecificEPRInfo(sUniformServiceName, EPR_NAMESPACE_URI));
        oEpr.setPortName(getServiceSpecificEPRInfo(sUniformServiceName, EPR_PORT_NAME));
        oEpr.setServiceName(getServiceSpecificEPRInfo(sUniformServiceName, EPR_SERVICE_NAME));

        return oEpr;
    }
    
}
