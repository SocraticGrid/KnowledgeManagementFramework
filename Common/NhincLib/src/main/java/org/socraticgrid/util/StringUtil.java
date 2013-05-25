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

import java.io.FileReader;
import java.util.StringTokenizer;

/**
 * String utilities...
 * 
 * @author Les Westberg
 */
public class StringUtil {

    /**
     * This method reads the entire contents of a text file and returns the contents in 
     * a string variable.
     * 
     * @param sFileName The path and location of the text file.
     * @return The contents that was read in.
     */
    public static String readTextFile(String sFileName)
            throws UtilException {
        String sText = "";
        FileReader frTextFile = null;

        try {
            frTextFile = new FileReader(sFileName);
            char caBuf[] = new char[1024];
            int iLen = 0;
            StringBuffer sbText = new StringBuffer();
            while ((iLen = frTextFile.read(caBuf, 0, 1024)) != -1) {
                sbText.append(caBuf, 0, iLen);
            }

            sText = sbText.toString();
        } catch (Exception e) {
            String sErrorMessage = "Failed to read text file: " + sFileName + ". Error: " + e.getMessage();
            throw new UtilException(sErrorMessage, e);
        } finally {
            if (frTextFile != null) {
                try {
                    frTextFile.close();
                } catch (Exception e) {
                }
            }
        }

        return sText;
    }

    /**
     * Extracts required string by removing the tokens given as input
     * @param tokenString
     * @param tokens
     * @return String
     */
    public static String extractStringFromTokens(String tokenString, String tokens) {
        String resultString = "";
        if (tokens != null && !tokens.equals("")) {
            StringTokenizer tk = new StringTokenizer(tokenString, tokens);
            StringBuffer outString = new StringBuffer();
            while (tk.hasMoreTokens()) {
                outString.append(tk.nextToken());
            }
            resultString = outString.toString();
        } else {
            resultString = tokenString;
        }
        return resultString;
    }
}
