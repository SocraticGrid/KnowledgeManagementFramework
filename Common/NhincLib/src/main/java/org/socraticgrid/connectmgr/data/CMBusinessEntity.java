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

import java.util.List;
import java.util.ArrayList;

/**
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:50 PM
 */
public class CMBusinessEntity
{

    private String businessKey = "";
    private CMDiscoveryURLs discoveryURLs = null;
    private CMBusinessNames names = null;
    private CMBusinessDescriptions descriptions = null;
    private CMContacts contacts = null;
    private String homeCommunityId = "";
    private CMStates states = null;
    private boolean federalHIE = false;
    private String publicKeyURI = "";
    private byte[] publicKey = null;
    private CMBusinessServices businessServices = null;

    /**
     * Default constructor.
     */
    public CMBusinessEntity()
    {
        clear();
    }
    
    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        homeCommunityId = "";
        federalHIE = false;
        publicKeyURI = "";
        publicKey = null;
        businessKey = "";
        discoveryURLs = null;
        names = null;
        descriptions = null;
        contacts = null;
        states = null;
        businessServices = null;
    }

    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBusinessEntity oCompare)
    {
        if (!this.homeCommunityId.equals(oCompare.homeCommunityId))
        {
            return false;
        }

        if (this.federalHIE != oCompare.federalHIE)
        {
            return false;
        }
        
        if (!this.publicKeyURI.equals(oCompare.publicKeyURI))
        {
            return false;
        }
        
        if (!this.businessKey.equals(oCompare.businessKey))
        {
            return false;
        }
        
        if (!this.discoveryURLs.equals(oCompare.discoveryURLs))
        {
            return false;
        }
        
        if (!this.names.equals(oCompare.names))
        {
            return false;
        }
        
        if (!this.descriptions.equals(oCompare.descriptions))
        {
            return false;
        }
        
        if (!this.contacts.equals(oCompare.contacts))
        {
            return false;
        }
        
        if (!this.states.equals(oCompare.states))
        {
            return false;
        }
        
        if (!this.businessServices.equals(oCompare.businessServices))
        {
            return false;
        }
        
        if ((this.publicKey.length != oCompare.publicKey.length))
        {
            return false;
        }
        
        int iCnt = this.publicKey.length;
        for (int i = 0; i < iCnt; i++)
        {
            if (this.publicKey[i] != oCompare.publicKey[i])
            {
                return false;
            }
        }

        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    
    /**
     * Return the business key associated with this entity.
     * 
     * @return The business key associated with this entity.
     */
    public String getBusinessKey()
    {
        return businessKey;
    }

    /**
     * Sets the business key associated with this entity.
     * 
     * @param businessKey The business key associated with this entity.
     */
    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    /**
     * Returns the business services assocaited with this business entity.
     * 
     * @return The business services associated with this business entity.
     */
    public CMBusinessServices getBusinessServices()
    {
        return businessServices;
    }

    /**
     * Sets the business services assocaited with this business entity.
     * 
     * @param businessServices The business services associated with this business entity.
     */
    public void setBusinessServices(CMBusinessServices businessServices)
    {
        this.businessServices = businessServices;
    }

    /**
     * Return the contacts associated with this business entity.
     * 
     * @return The contacts associated with this business entity.
     */
    public CMContacts getContacts()
    {
        return contacts;
    }

    /**
     * Sets the contacts associated with this business entity.
     * 
     * @param contacts The contacts associated with this business entity.
     */
    public void setContacts(CMContacts contacts)
    {
        this.contacts = contacts;
    }

    /**
     * Return the description of this business entity.
     * 
     * @return The description of this business entity.
     */
    public CMBusinessDescriptions getDescriptions()
    {
        return descriptions;
    }

    /**
     * Sets the description of this business entity.
     * 
     * @return The description of this business entity.
     */
    public void setDescriptions(CMBusinessDescriptions descriptions)
    {
        this.descriptions = descriptions;
    }

    /**
     * Returns the dicovery URLs for this business entity.
     * 
     * @return The discovery URLs for this business entity.
     */
    public CMDiscoveryURLs getDiscoveryURLs()
    {
        return discoveryURLs;
    }

    /**
     * Sets the dicovery URLs for this business entity.
     * 
     * @return The discovery URLs for this business entity.
     */
    public void setDiscoveryURLs(CMDiscoveryURLs discoveryURLs)
    {
        this.discoveryURLs = discoveryURLs;
    }

    /**
     * Returns true if this business entity represents a federal HIE.
     * 
     * @return True if this business entity represents a federal HIE.
     */
    public boolean isFederalHIE()
    {
        return federalHIE;
    }

    /**
     * Set to true if this business entity represents a federal HIE.
     * 
     * @param federalHIE True if this business entity represents a federal HIE.
     */
    public void setFederalHIE(boolean federalHIE)
    {
        this.federalHIE = federalHIE;
    }

    /**
     * Returns the home community ID for this business entity.
     * 
     * @return The home community ID for this business entity.
     */
    public String getHomeCommunityId()
    {
        return homeCommunityId;
    }

    /**
     * Sets the home community ID for this business entity.
     * 
     * @param homeCommunityId The home community ID for this business entity.
     */
    public void setHomeCommunityId(String homeCommunityId)
    {
        this.homeCommunityId = homeCommunityId;
    }

    /**
     * Returns the business names associated with this business entity.
     * 
     * @return The business names associated with this business entity.
     */
    public CMBusinessNames getNames()
    {
        return names;
    }

    /**
     * Sets the business names associated with this business entity.
     * 
     * @param names The business names associated with this business entity.
     */
    public void setNames(CMBusinessNames names)
    {
        this.names = names;
    }

    /**
     * Returns the PKI public key for this business entity.
     * 
     * @return The PKI public key for this business entity.
     */
    public byte[] getPublicKey()
    {
        return publicKey;
    }

    /**
     * sets the PKI public key for this business entity.
     * 
     * @param publicKey The PKI public key for this business entity.
     */
    public void setPublicKey(byte[] publicKey)
    {
        this.publicKey = publicKey;
    }

    /**
     * Returns the URL for the location to retrieve the business entity's PKI public key.
     * 
     * @return The URL for the location to retrieve the business entity's PKI public key.
     */
    public String getPublicKeyURI()
    {
        return publicKeyURI;
    }

    /**
     * Sets the URL for the location to retrieve the business entity's PKI public key.
     * 
     * @param publicKeyURI The URL for the location to retrieve the business entity's PKI public key.
     */
    public void setPublicKeyURI(String publicKeyURI)
    {
        this.publicKeyURI = publicKeyURI;
    }

    /**
     * Returns the states associated with this business entity.
     * 
     * @return The states associated with this business entity.
     */
    public CMStates getStates()
    {
        return states;
    }

    /**
     * Sets the states associated with this business entity.
     * 
     * @param states The states associated with this business entity.
     */
    public void setStates(CMStates states)
    {
        this.states = states;
    }
    
}