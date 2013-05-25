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
 * Generic Implementation for most
 *
 * @author  Jerry Goodnough
 */
public class GenericResourceAddressBean extends BaseResourceAddressBean
    implements UniversalResourceAddressBean
{


    /** Genric Type name support */
    protected String entityTypeName = "unknown";

    /** Path */
    protected String path = "";

    /** Scheme */
    protected String scheme = "";

    /** SubScheme */
    protected String schemeType = "";


    /**
     * Creates a new GenericResourceAddressBean object.
     */
    public GenericResourceAddressBean()
    {

    }

    /**
     * Creates a new GenericResourceAddressBean object.
     * The Entity Type will be custom
     *
     * @param  typeName    Unrestricted type name
     * @param  scheme      Scheme Name
     * @param  schemeType  SubScheme (if any)
     * @param  path        Path in Scheme
     */
    public GenericResourceAddressBean(String typeName, String scheme,
        String schemeType, String path)
    {
        this.entityTypeName = typeName;
        this.entityType=EntityType.CUSTOM;

        EntityType tempType = EntityTypeHelper.getType(typeName);
        if (EntityTypeHelper.isStandard(tempType))
        {
            this.entityType=tempType;
        }
        this.scheme = scheme;
        if(schemeType != null)
        {
            this.schemeType = schemeType;
        }
        this.path = path;
    }

    /**
     * Creates a new GenericResourceAddressBean object.
     *
     * @param  entityType  The Entity Type
     * @param  typeName    Unrestricted type name (Uss if type non standard)
     * @param  scheme      Scheme Name
     * @param  schemeType  SubScheme (if any)
     * @param  path        Path in Scheme
     */
    public GenericResourceAddressBean(EntityType entityType, String typeName,
        String scheme, String schemeType, String path)
    {
        this.entityTypeName = typeName;
        this.schemeType = schemeType;
        this.path = path;
        this.entityType = entityType;
        this.scheme = scheme;
                if(schemeType != null)
        {
            this.schemeType = schemeType;
        }

    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getEntityTypeName()
     */
    @Override public String getEntityTypeName()
    {

        if (!EntityTypeHelper.isStandard(entityType))
        {
            return entityTypeName;
        }
        else
        {
            return EntityTypeHelper.getTypeName(entityType);
        }
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getFullScheme()
     */
    public String getFullScheme()
    {

        if (schemeType.length() > 0)
        {
            return scheme + "." + schemeType;
        }
        else
        {
            return scheme;
        }
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getPath()
     */
    public String getPath()
    {
        return path;
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getScheme()
     */
    public String getScheme()
    {
        return scheme;
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getSubScheme()
     */
    public String getSubScheme()
    {
        return schemeType;
    }

    /**
     * Creates UniversalResourceAddressBean based on a URA
     *
     * @param   ura  The URA to use to create the UniversalResourceAddressBean
     *
     * @return  UniversalResourceAddressBean represented by the URA
     *
     * @throws  URISyntaxException  If the URA is invalid
     */
    public static UniversalResourceAddressBean parse(String ura)
        throws URISyntaxException
    {
        return UniversalResourceAddressBeanFactory.getInstance()
            .createUniversalResourceBean(ura);
    }

    /**
     * @see  org.socraticgrid.nhinc.kmr.ura.UniversalResourceAddressBean#getURA()
     */
    @Override public String getURA()
    {
        return getEntityTypeName() + "://" + getFullScheme() + "/" + path;
    }
}
