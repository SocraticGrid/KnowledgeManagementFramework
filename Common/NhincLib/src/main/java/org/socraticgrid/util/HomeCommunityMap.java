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

package org.socraticgrid.util;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.socraticgrid.connectmgr.ConnectionManagerCache;
import org.socraticgrid.connectmgr.data.CMBusinessEntity;

/**
 * This class is used to map a home community ID to the
 * textual name of the home community.  The information
 * is stored in a properties file so that it can be tweaked
 * and changed without having to recompile...
 * 
 * @author Les Westberg
 */
public class HomeCommunityMap 
{
    private static Log log = LogFactory.getLog(HomeCommunityMap.class);
    
    /**
     * This method retrieves the name of the home community baased on the
     * home community Id.
     * 
     * @param sHomeCommunityId The home community ID to be looked up.
     * @return The textual name of the home community.
     */
    public String getHomeCommunityName(String sHomeCommunityId)
    {
        String sHomeCommunityName = "";
        
        try
        {
            CMBusinessEntity oEntity = ConnectionManagerCache.getBusinessEntity(sHomeCommunityId);
            if ((oEntity != null) &&
                (oEntity.getNames() != null) &&
                (oEntity.getNames().getBusinessName() != null) &&
                (oEntity.getNames().getBusinessName().size() > 0) &&
                (oEntity.getNames().getBusinessName().get(0) != null) &&
                (oEntity.getNames().getBusinessName().get(0).length() > 0))
            {
                sHomeCommunityName = oEntity.getNames().getBusinessName().get(0);
            }
        }
        catch (Exception e)
        {
            log.warn("Failed to retrieve textual name for home community ID: " + sHomeCommunityId, e);
        }
        
        return sHomeCommunityName;
    }
}
