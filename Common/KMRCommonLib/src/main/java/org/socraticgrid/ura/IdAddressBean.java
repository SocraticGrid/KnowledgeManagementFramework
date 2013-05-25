

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

package org.socraticgrid.ura;

import java.net.URISyntaxException;


/**
 * Concrete Implementation of the UniversalResourceAddressBean for the Id Scheme
 * The class add the fields subScheme and id to the generic iterface.
 *
 * <p>Example URA: patient://id/578855</p>
 *
 * @author  Jerry Goodnough
 */
public class IdAddressBean extends BaseResourceAddressBean
    implements UniversalResourceAddressBean
{

    /** Teh actual Id. */
    private String id;

    /** The Sub type of the Id. */
    private String idSubScheme = "";

    /**
     * Creates a new IdAddressBean object.
     *
     * @param  entityType   Entity Type (e.g. EntityType.PATIENT
     * @param  id           The actual Id that this bean represents (e.g 100 or
     *                      88390-3873)
     * @param  idSubScheme  The SubScheme of the Id (Optional)
     */
    public IdAddressBean(EntityType entityType, String id, String idSubScheme)
    {
        this.entityType = entityType;
        this.id = id;

        if (idSubScheme != null)
        {
            this.idSubScheme = idSubScheme;
        }
    }

    /**
     * Check if a given URA string is a Valid IdAddressBean. For example
     * patient://id.test/10 should be valid, while patient://ldap/cn=georgebush
     * should be invalid since the valid ura is not in the id scheme.
     *
     * @param   ura  The ura to check if is a valid Id URA
     *
     * @return  true if the ura is a valid Id Address.
     */
    public static boolean isIdAddressBean(String ura)
    {

        if (ura != null)
        {

            if (!ura.isEmpty())
            {

                try
                {
                    UniversalResourceAddressBean bean =
                        UniversalResourceAddressBeanFactory.getInstance()
                        .createUniversalResourceBean(ura);

                    if (bean == null)
                    {
                        return false;
                    }
                    else
                    {
                        return bean.getScheme().compareTo("id") == 0;
                    }
                }
                catch (URISyntaxException e)
                {
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Get the Id. The Id is formed from the path of the ura.
     *
     * @return  The Id string associaited with the IdAddress bean
     */
    public String getId()
    {
        return id;
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getPath()
     */
    public String getPath()
    {
        return id;
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getScheme()
     */
    public String getScheme()
    {
        return "id";
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getSubScheme()
     */
    public String getSubScheme()
    {
        return idSubScheme;
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getFullScheme()
     */
    public String getFullScheme()
    {

        if (idSubScheme.length() > 0)
        {
            return "id." + idSubScheme;
        }
        else
        {
            return "id";
        }
    }

    /**
     * Creates a IdAddressBean from a valid Id URA.
     *
     * @param   ura  An Id URA
     *
     * @return  A IdAddress Bean or null
     *
     * @throws  URISyntaxException  If the URA is ill formed
     */
    public static IdAddressBean parse(String ura) throws URISyntaxException
    {
        return UniversalResourceAddressBeanFactory.getInstance()
            .createIdAddressBean(ura);
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean##getURA()
     */
    @Override public String getURA()
    {
        return getEntityTypeName() + "://" + getFullScheme() + "/" + id;
    }
}
