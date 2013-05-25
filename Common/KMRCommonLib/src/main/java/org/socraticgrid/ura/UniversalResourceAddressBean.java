
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

/**
 * Generic interface for a Universal Address Bean (UAB).
 *
 * @see     https://socraticgrid.org/display/docs/Universal+Resource+Addresses
 * @author  Jerry Goodnough
 */
public interface UniversalResourceAddressBean
{


    /**
     * The Entity Type of the Universal Address Bean. For example if the URA is
     * patient://id.ssn/555667777 the entity type name is EntityType.PATIENT
     *
     * @return  The EntityType of the Universal Address Bean.
     */
    public EntityType getEntityType();

    /**
     * The name of the Type associated with the Universal Address Bean. For
     * example if the URA is patient://id.ssn/555667777 the entity type name is
     * "patient".
     *
     * @return  The name of the Type associated with the Universal Address Bean.
     */
    public String getEntityTypeName();

    /**
     * Just the name of the Scheme used to encode the id. Any subscheme is
     * excluded. For example if the URA is patient://id.ssn/555667777 the scheme
     * is "id".
     *
     * @return  Just the name of the Scheme used to encode the id
     */
    public String getScheme();

    /**
     * The name of the Scheme used to encode the id including the subscheme. see
     * For example if the URA is patient://id.ssn/555667777 the full scheme is
     * "id.ssn".
     *
     * @return  The name of the Scheme used to encode the id including the
     *          subscheme
     */
    public String getFullScheme();

    /**
     * The name of the Scheme used to encode the id including the subscheme. see
     * For example if the URA is patient://id.ssn/555667777 the full scheme is
     * "id.ssn".
     *
     * @return  The name of the Scheme used to encode the id including the
     *          subscheme.
     */
    public String getSubScheme();

    /**
     * The path portion of the URA. For example if the URA is
     * patient://id.ssn/555667777 the path is "555667777".
     *
     * @return  The path portion of the URA.
     */
    public String getPath();

    /**
     * Returns the string representation of the bean which should be a valid
     * ura. For example 'patient://id.ssn/555667777'
     *
     * @return  Returns the string representation of the bean which should be a
     *          valid ura.
     */
    public String toString();

    /**
     * Returns the string representation of the bean which should be a valid
     * ura. For example 'patient://id.ssn/555667777'
     *
     * @return  Returns the string representation of the bean which should be a
     *          valid ura.
     */
    public String getURA();

    /**
     * Indicates whether some other object is "equal to" this one. Two Id's are
     * equal if the URAs are equal.
     *
     * @param   obj  the reference object with which to compare.
     *
     * @return  true if this object is the same as the obj argument; false
     *          otherwise.
     */
    @Override public boolean equals(Object obj);

    /**
     * Returns a hash code value for the object.
     *
     * @return  a hash code value for this object.
     */
    @Override public int hashCode();
}
