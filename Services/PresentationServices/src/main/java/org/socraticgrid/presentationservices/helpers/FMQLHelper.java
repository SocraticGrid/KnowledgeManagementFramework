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
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

package org.socraticgrid.presentationservices.helpers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringWriter;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;


/**
 * DOCUMENT ME!
 *
 * @author  Jerry Goodnough
 */
public class FMQLHelper
{

    /** DOCUMENT ME! */
    private static FMQLHelper singleton;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static FMQLHelper getFMQLHelper()
    {

        if (singleton == null)
        {
            singleton = new FMQLHelper();
        }

        return singleton;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   queryParameters  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String makeFMQLQuery(String queryParameters)
    {
        PropertyHelper ph = PropertyHelper.getPropertyHelper();
        String endpoint = ph.getProperty("FMQLEndpoint");
        String user = ph.getProperty("FMQLEndpointUser");
        String password = ph.getProperty("FMQLEndpointPassword");

        StringWriter sw = new StringWriter();

        String query = endpoint + "?" + queryParameters;
        
        Authenticator.setDefault(new PasswordAuthenticator(user, password));

        try
        {
            URL fmqlr = new URL(query);
            URLConnection fmqlc = fmqlr.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                        fmqlc.getInputStream()));
            int c = in.read();

            while (c != -1)
            {
                sw.write(c);
                c = in.read();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return sw.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   patientId  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPatientAppointments(String patientId)
    {
        String typeId = "9002018_4";
        String filterBase = ".05=2-";
        String query = String.format("op=Describe&typeId=%s&filter=%s%s",
                typeId, filterBase, patientId);

        return makeFMQLQuery(query);

    }

    /**
     * DOCUMENT ME!
     *
     * @author   $author$
     * @version  $Revision$, $Date$
     */
    private class PasswordAuthenticator extends Authenticator
    {

        /** DOCUMENT ME! */
        private String password;

        /** DOCUMENT ME! */
        private String user;

        /**
         * Creates a new PasswordAuthenticator object.
         *
         * @param  user      DOCUMENT ME!
         * @param  password  DOCUMENT ME!
         */
        PasswordAuthenticator(String user, String password)
        {
            this.password = password;
            this.user = user;
        }

        /**
         * @see  java.net.Authenticator#getPasswordAuthentication()
         */
        @Override protected PasswordAuthentication getPasswordAuthentication()
        {
            return new PasswordAuthentication(user, password.toCharArray());
        }
        
    }
    
    /*

     * // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            }
        };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
        }

        // Now you can access an https URL without having the certificate in the truststore
        try {
            URL url = new URL("https://hostname/index.html");
        } catch (MalformedURLException e) {
        }
*/
}
