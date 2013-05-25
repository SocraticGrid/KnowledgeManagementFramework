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

//import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author Les Westberg
 */
public class CMInternalConnInfoService
{
    private String name;
    private String description;
    private String endpointURL;
    private boolean externalService;

    /**
     * Default constructor.
     */
    public CMInternalConnInfoService()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        name = "";
        description = "";
        endpointURL = "";
        externalService = false;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnInfoService oCompare)
    {
        if ((!this.name.equals(oCompare.name)) ||
            (!this.description.equals(oCompare.description)) ||
            (!this.endpointURL.equals(oCompare.endpointURL)) ||
            (this.externalService != oCompare.externalService))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    
    
    /**
     * This method returns the description of the service.
     * 
     * @return The description of the service.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * This method sets the description of the service.
     * 
     * @param description The description of the service.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * This method returns the URL for the endpoint.
     * 
     * @return The URL for the endpoint.
     */
    public String getEndpointURL()
    {
        return endpointURL;
    }

    /**
     * This method sets the URL for the endpoint.
     * 
     * @param endpointURL The URL for the endpoint.
     */
    public void setEndpointURL(String endpointURL)
    {
        this.endpointURL = endpointURL;
    }

    /**
     * If this is true then this service is an external service meaning
     * that it is one that is exposed to the nhin.
     * 
     * @return Returns true if this service is exposed to the nhin.
     */
    public boolean isExternalService()
    {
        return externalService;
    }

    /**
     * Set this to true if this service will be exposed to the nhin.
     * 
     * @param externalService This is true if this service is exposed to the nhin.
     */
    public void setExternalService(boolean externalService)
    {
        this.externalService = externalService;
    }

    /**
     * Return the name of this servie.
     * 
     * @return The name of this service.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the name of this service.
     * 
     * @param name The name of this service.
     */
    public void setName(String name)
    {
        this.name = name;
    }

}