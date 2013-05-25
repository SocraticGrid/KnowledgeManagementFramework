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

import com.thoughtworks.xstream.XStream;

/**
 * This class is used to serialize/deserialize to/from XML using XStream.
 * 
 * @author Les Westberg
 */
public class CMInternalConnectionInfosXML
{
    /**
     * This method serializes an InternalConnectionInfos object to an
     * XML string.
     * 
     * @param oInternalConnectionInfos The object to be serialized.
     * @return The XML string representation of the object.
     */
    public static String serialize(CMInternalConnectionInfos oInternalConnectionInfos)
    {
        String sXML = "";
        
        XStream oXStream = new XStream();
        oXStream.alias("InternalConnectionInfos", CMInternalConnectionInfos.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfos.class, "internalConnectionInfoList");
        oXStream.alias("internalConnectionInfo", CMInternalConnectionInfo.class);
        oXStream.addImplicitCollection(CMInternalConnInfoServices.class, "serviceList");
        oXStream.alias("service", CMInternalConnInfoService.class);
        oXStream.processAnnotations(CMInternalConnectionInfos.class);
        sXML = oXStream.toXML(oInternalConnectionInfos);
        
        return sXML;
    }
    
    /**
     * This method takes an XML representation of CMInternalConnectionInfos and
     * produces an instance of the object.
     * 
     * @param sXML The serialized representation of the CMInternalConnectionInfos object.
     * @return The object instance of the XML.
     */
    public static CMInternalConnectionInfos deserialize(String sXML)
    {
        CMInternalConnectionInfos oInternalConnectionInfos = new CMInternalConnectionInfos();
        
        XStream oXStream = new XStream();
        oXStream.alias("InternalConnectionInfos", CMInternalConnectionInfos.class);
        oXStream.addImplicitCollection(CMInternalConnectionInfos.class, "internalConnectionInfoList");
        oXStream.alias("internalConnectionInfo", CMInternalConnectionInfo.class);
        oXStream.addImplicitCollection(CMInternalConnInfoServices.class, "serviceList");
        oXStream.alias("service", CMInternalConnInfoService.class);
        oXStream.processAnnotations(CMInternalConnectionInfos.class);
        Object oObject = oXStream.fromXML(sXML);
        if (oObject instanceof CMInternalConnectionInfos)
        {
            oInternalConnectionInfos = (CMInternalConnectionInfos) oObject;
        }
        
        return oInternalConnectionInfos;
    }
}
