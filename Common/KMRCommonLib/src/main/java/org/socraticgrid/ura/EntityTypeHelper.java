
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

import java.util.HashMap;


/**
 * Helper class for dealing with EntityTypes.
 *
 * @author  Jerry Goodnough
 */
public class EntityTypeHelper
{

    /** Referense Name Map to Enumeration Type. */
    private static HashMap<String, EntityType> nameMap =
        new HashMap<String, EntityType>();

    static
    {
        nameMap.put("unknown", EntityType.UNKNOWN);
        nameMap.put("custom", EntityType.CUSTOM);
        nameMap.put("patient", EntityType.PATIENT);
        nameMap.put("provider", EntityType.PROVIDER);
        nameMap.put("device", EntityType.DEVICE);
        nameMap.put("group", EntityType.GROUP);
        nameMap.put("org", EntityType.ORG);
        nameMap.put("service", EntityType.SERVICE);
        nameMap.put("registry", EntityType.REGISTRY);
        nameMap.put("code", EntityType.CODE);
    }

    /**
     * Given a type name return the EntiyTypoe Enumeration that matches it.
     *
     * @param   type  Type name in any case
     *
     * @return  EntityType (Will be EntityType.UNKNOWN is type not matched)
     */
    public static EntityType getType(String type)
    {
        EntityType out;

        if (type != null)
        {
            out = nameMap.get(type.toLowerCase());

            if (out == null)
            {
                out = EntityType.UNKNOWN;
            }
        }
        else
        {
            out = EntityType.UNKNOWN;
        }

        return out;
    }

    /**
     * Given a EntityType get a string name for it in lowercase.
     *
     * @param   et  The EntityType to find the name of
     *
     * @return  Name of entity Type in lowercase (if not know "unkniown" will be
     *          returned
     */
    public static String getTypeName(EntityType et)
    {
        String out = "unknown";

        switch (et)
        {

            case CUSTOM:
            {
                out = "custom";

                break;
            }

            case PATIENT:
            {
                out = "patient";

                break;
            }

            case PROVIDER:
            {
                out = "provider";

                break;
            }

            case ROLE:
            {
                out = "role";

                break;
            }

            case DEVICE:
            {
                out = "device";

                break;
            }

            case GROUP:
            {
                out = "group";

                break;
            }

            case ORG:
            {
                out = "org";

                break;
            }

            case SERVICE:
            {
                out = "service";

                break;
            }

            case REGISTRY:
            {
                out = "registry";

                break;
            }

            case CODE:
            {
                out = "code";

                break;
            }
        }

        return out;
    }

    /**
     * Check if an EntityType is a standard form.
     *
     * @param   et  The EntityType to check
     *
     * @return  true if the EntityType is a standard name
     */
    public static boolean isStandard(EntityType et)
    {

        switch (et)
        {

            case CUSTOM:
            case UNKNOWN:
            {
                return false;
            }

            default:
            {
                return true;
            }
        }
    }
}
