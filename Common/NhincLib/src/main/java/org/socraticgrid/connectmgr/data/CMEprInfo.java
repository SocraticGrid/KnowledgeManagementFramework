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

package org.socraticgrid.connectmgr.data;

/**
 * This class represents Endpoint Reference (EPR) Information 
 * 
 * @author Les Westberg
 */
public class CMEprInfo
{
    // Member variables
    //------------------
    private String uniformServiceName = "";
    private String namespacePrefix = "";
    private String namespaceURI = "";
    private String portName = "";
    private String serviceName = "";

    /**
     * Returns the uniform service name for this EPR.  This is the key by which
     * this service is known in the UDDI.
     * 
     * @return The uniform service name for this EPR.
     */
    public String getUniformServiceName()
    {
        return uniformServiceName;
    }

    /**
     * Sets the uniform service name for this EPR.  This is the key by which
     * this service is known in the UDDI.
     * 
     * @param uniformServiceName The uniform service name for this EPR.
     * 
     */
    public void setUniformServiceName(String uniformServiceName)
    {
        this.uniformServiceName = uniformServiceName;
    }

    
    /**
     * Return the namespace prefix.
     * 
     * @return The namespace prefix.
     */
    public String getNamespacePrefix()
    {
        return namespacePrefix;
    }

    /**
     * Set the namespace prefix.
     * 
     * @param namespacePrefix The namespace prefix.
     */
    public void setNamespacePrefix(String namespacePrefix)
    {
        this.namespacePrefix = namespacePrefix;
    }

    /**
     * Returns the namespace URI for the endpoint.
     * 
     * @return Returns the namespace URI for the endpoint.
     */
    public String getNamespaceURI()
    {
        return namespaceURI;
    }

    /**
     * Sets the namespace URI for the endpoint.
     * 
     * @param namespaceURI The namespace URI for the endpoint.
     */
    public void setNamespaceURI(String namespaceURI)
    {
        this.namespaceURI = namespaceURI;
    }

    /**
     * Returns the port name for the endpoint.
     * 
     * @return The port name for this endpoint.
     */
    public String getPortName()
    {
        return portName;
    }

    /**
     * Sets the port name for the endpoint.
     * 
     * @param portName The port name for the endpoint.
     */
    public void setPortName(String portName)
    {
        this.portName = portName;
    }

    /**
     * Returns the service name for the endpoint.
     * 
     * @return The service name for the endpoint.
     */
    public String getServiceName()
    {
        return serviceName;
    }

    /**
     * Sets the service name for the endpoint.
     * 
     * @param serviceName The service name for the endpoint.
     */
    public void setServiceName(String serviceName)
    {
        this.serviceName = serviceName;
    }
    
    
}
