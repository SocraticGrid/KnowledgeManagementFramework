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
package org.socraticgrid.util.format;

import org.socraticgrid.nhinclib.NullChecker;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rayj
 */
public class DocumentClassCodeParser {

    private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DocumentClassCodeParser.class);

    public static List<String> parseFormattedParameter(List<String> rawList) {
        List<String> normalizedList = new ArrayList<String>();
        for (String item : rawList) {
            normalizedList = parseFormattedParameter(item, normalizedList);
        }
        return normalizedList;
    }

    public static List<String> parseFormattedParameter(String paramFormattedString) {
        return parseFormattedParameter(paramFormattedString, null);
    }

    public static List<String> parseFormattedParameter(String paramFormattedString, List<String> resultCollection) {
        if (resultCollection == null) {
            resultCollection = new ArrayList<String>();
        }

        if ((NullChecker.isNotNullish(paramFormattedString)) && (resultCollection != null)) {
            if (paramFormattedString.startsWith("(")) {
                String working = paramFormattedString.substring(1);
                int endIndex = working.indexOf(")");
                if (endIndex != -1) {
                    working = working.substring(0, endIndex);
                }
                String[] multiValueString = working.split(",");
                if (multiValueString != null) {
                    for (int i = 0; i < multiValueString.length; i++) {
                        String singleValue = multiValueString[i];
                        if (singleValue != null) {
                            singleValue = singleValue.trim();
                        }
                        if (singleValue.startsWith("'")) {
                            singleValue = singleValue.substring(1);
                        }
                        if (singleValue.endsWith("'")) {
                            int endTickIndex = singleValue.indexOf("'");
                            if (endTickIndex != -1) {
                                singleValue = singleValue.substring(0, endTickIndex);
                            }
                        }
                        resultCollection.add(singleValue);
                        if (log.isDebugEnabled()) {
                            log.debug("Added single value: " + singleValue + " to query parameters");
                        }
                    }
                }
            } else {
                resultCollection.add(paramFormattedString);
                if (log.isDebugEnabled()) {
                    log.debug("No wrapper on status - adding status: " + paramFormattedString + " to query parameters");
                }
            }
        }

        return resultCollection;
    }

    public static String buildDocumentClassCodeItem(List<String> documentClassCodeList) {
        String buffer = "";

        if ((documentClassCodeList != null) && (documentClassCodeList.size() > 0)) {
            buffer = "(";
            for (String documentClassCode : documentClassCodeList) {
                documentClassCode = documentClassCode.trim();
                if (NullChecker.isNotNullish(buffer)) {
                    buffer = buffer + "'" + documentClassCode + "'" + ",";
                }
            }
            if (buffer.endsWith(",")) {
                buffer = buffer.substring(0, buffer.length() - 1);
            }
            buffer = buffer + ")";
        }
        if (buffer.contentEquals("()")) {
            buffer = "";
        }
        return buffer;
    }
}
