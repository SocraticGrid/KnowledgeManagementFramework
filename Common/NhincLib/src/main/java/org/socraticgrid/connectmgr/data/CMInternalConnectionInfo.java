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
 * @author Les Westberg
 */
public class CMInternalConnectionInfo 
{
    private String homeCommunityId;
    private String name;
    private String description;
    private CMInternalConnInfoServices services;

    /**
     * Default Constructor.
     */
    public CMInternalConnectionInfo()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        homeCommunityId = "";
        name = "";
        description = "";
        services = null;
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMInternalConnectionInfo oCompare)
    {
        if ((!this.homeCommunityId.equals(oCompare.homeCommunityId)) ||
            (!this.name.equals(oCompare.name)) ||
            (!this.description.equals(oCompare.description)) ||
            (!this.services.equals(oCompare.services)))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    

    /**
     * Return the description of the connection.
     * 
     * @return The description of the connection.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description of the connection.
     * 
     * @param description  The description of the connection.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return the home community ID associated with this connection.
     * 
     * @return The home community ID associated with this connection.
     */
    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    /**
     * Set the home community ID associated with this connection.
     * 
     * @param homeCommunityId The home community ID associated with this connection.
     */
    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Return the name of this home community.
     * 
     * @return The name of this home community.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of this home community.
     * 
     * @param name The name of this home community.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Return the services associated with this home community.
     * 
     * @return The services associated with this home community.
     */
    public CMInternalConnInfoServices getServices()
    {
        return services;
    }

    /**
     * Sets the services associated with this home community.
     * 
     * @param services The services associated with this home community.
     */
    public void setServices(CMInternalConnInfoServices services)
    {
        this.services = services;
    }

}