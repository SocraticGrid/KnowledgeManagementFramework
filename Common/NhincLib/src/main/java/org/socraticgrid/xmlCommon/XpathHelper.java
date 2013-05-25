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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.xmlCommon;

import org.socraticgrid.nhinclib.NullChecker;
import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.*;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSParser;

/**
 *
 * @author rayj
 */
public class XpathHelper {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(XpathHelper.class);

    public static Node performXpathQuery(String sourceXml, String xpathQuery) throws XPathExpressionException {
        return performXpathQuery(sourceXml, xpathQuery, null);
    }

    public static Node performXpathQuery(String sourceXml, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }

        InputSource inputSource = new InputSource(new ByteArrayInputStream(sourceXml.getBytes()));

        log.debug("perform xpath query (query='" + xpathQuery + "'");
        Node result = null;
        if (XmlUtfHelper.isUtf16(sourceXml)) {
            try {
                result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
            } catch (Exception ex) {
                // Exception may be due to the encoding of the message being incorrect.
                // retry using UTF-8
                log.warn("failed to perform xpath query - retrying with UTF-8");
                sourceXml = XmlUtfHelper.convertToUtf8(sourceXml);
                result = performXpathQuery(sourceXml, xpathQuery, namespaceContext);
            }
        } else {
            result = (Node) xpath.evaluate(xpathQuery, inputSource, XPathConstants.NODE);
        }
        log.debug("xpath query complete [result?=" + result + "]");
        return result;
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery) throws XPathExpressionException {
        return performXpathQuery(sourceElement, xpathQuery, null);
    }

    public static Node performXpathQuery(Element sourceElement, String xpathQuery, NamespaceContext namespaceContext) throws XPathExpressionException {
        javax.xml.xpath.XPathFactory factory = javax.xml.xpath.XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();

        if (namespaceContext != null) {
            xpath.setNamespaceContext(namespaceContext);
        }

        log.debug("About to perform xpath query (query='" + xpathQuery + "'");
        Node result = (Node) xpath.evaluate(xpathQuery, sourceElement, XPathConstants.NODE);
        return result;
    }
}
