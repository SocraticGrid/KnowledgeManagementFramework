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
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:46 PM
 */
public class CMBindingTemplate
{
    private String bindingKey = "";
    private String endpointURL = "";
    private String wsdlURL = "";

    /**
     * Default constructor.
     */
    public CMBindingTemplate()
    {
        clear();
    }    

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        endpointURL = "";
        wsdlURL = "";
        bindingKey = "";
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingTemplate oCompare)
    {
        if ((!this.bindingKey.equals(oCompare.bindingKey)) ||
            (!this.endpointURL.equals(oCompare.endpointURL)) ||
            (!this.wsdlURL.equals(oCompare.wsdlURL)))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Returns the binding key for this binding.
     * 
     * @return The binding key for this binding.
     */
    public String getBindingKey()
    {
        return bindingKey;
    }

    /**
     * Sets the binding key for this binding.
     * 
     * @param bindingKey The binding key for this binding.
     */
    public void setBindingKey(String bindingKey)
    {
        this.bindingKey = bindingKey;
    }

    /**
     * Returns the end point URL for this binding.
     * 
     * @return The end point URL for this binding.
     */
    public String getEndpointURL()
    {
        return endpointURL;
    }

    /**
     * Sets the end point URL for this binding.
     * 
     * @param endpointURL The end point URL for this binding.
     */
    public void setEndpointURL(String endpointURL)
    {
        this.endpointURL = endpointURL;
    }

    /**
     * Returns the URL for the WSDL for this binding.
     * 
     * @return The URL for the WSDL for this binding.
     */
    public String getWsdlURL()
    {
        return wsdlURL;
    }

    /**
     * Sets the URL for the WSDL for this binding.
     * 
     * @param wsdlURL The URL for the WSDL for this binding.
     */
    public void setWsdlURL(String wsdlURL)
    {
        this.wsdlURL = wsdlURL;
    }
    
    
}