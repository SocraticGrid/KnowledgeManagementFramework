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
package org.socraticgrid.presentationservices;

import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author nhin
 */
public class SetAccountTest {

    private String server = PresentationServicesSuite.defaultServer;

    public SetAccountTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        System.out.println("MAINTAINACCOUNT HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetAccount() throws MalformedURLException, IOException {
        System.out.println("TEST CASE: testMaintainAccount");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setAccount"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "create"));
        request.getRequestParameters().add(new NameValuePair("userName", "testuser"));
        request.getRequestParameters().add(new NameValuePair("password", "test"));
        request.getRequestParameters().add(new NameValuePair("firstName", "Sogo"));
        request.getRequestParameters().add(new NameValuePair("lastName", "Admin"));
        request.getRequestParameters().add(new NameValuePair("city", "San Diego"));
        request.getRequestParameters().add(new NameValuePair("state", "CA"));
        request.getRequestParameters().add(new NameValuePair("postalCode", "99999"));
        request.getRequestParameters().add(new NameValuePair("homePhone", "7777777777"));
        request.getRequestParameters().add(new NameValuePair("emailAddress", "testuser@lab.socraticgrid.org"));
        request.getRequestParameters().add(new NameValuePair("ssn", "999999999"));
        request.getRequestParameters().add(new NameValuePair("address1", "111 Main St."));
        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nRESPONSE IS: \n" + stringResponse + "\n");

        assertTrue(stringResponse.contains(getResponseAlreadyExists())
                || stringResponse.contains(getResponseNewEntry()));


        client.closeAllWindows();
    }

    @Test
    public void testSetAccountBadParam() throws MalformedURLException, IOException {
        System.out.println("TEST CASE: testMaintainAccount");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setAccount"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "create"));
        //  request.getRequestParameters().add(new NameValuePair("userName", "jharby"));
        request.getRequestParameters().add(new NameValuePair("password", "test"));
        //  request.getRequestParameters().add(new NameValuePair("firstName", "John"));
        //  request.getRequestParameters().add(new NameValuePair("lastName", "Harby"));
        request.getRequestParameters().add(new NameValuePair("city", "San Diego"));
        request.getRequestParameters().add(new NameValuePair("state", "CA"));
        request.getRequestParameters().add(new NameValuePair("postalCode", "99999"));
        request.getRequestParameters().add(new NameValuePair("homePhone", "7777777777"));
        request.getRequestParameters().add(new NameValuePair("emailAddress", "janemdoe@lab.socraticgrid.org"));
        request.getRequestParameters().add(new NameValuePair("ssn", "999999999"));
        request.getRequestParameters().add(new NameValuePair("address1", "111 Main St."));
        System.out.println("\nREQUEST URL IS: " + request.toString());
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nRESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("\nEXPECTED RESPONSE: \n" + getResponseBadParam());

        assertTrue(stringResponse.contains(getResponseBadParam()));


        client.closeAllWindows();
    }

    private String getResponseAlreadyExists() {
        String expectedResponse =
                "{\"setAccountFact\":{\"successStatus\":false,\"statusMessage\":\"Unsuccessful Account Creation\"}}";

        return expectedResponse;
    }

    private String getResponseNewEntry() {
        String expectedResponse =
                "{\"setAccountFact\":{\"successStatus\":true,\"statusMessage\":\""
                + "Your information has been received, we will email you once your account has been created\"}}";
        return expectedResponse;
    }

    private String getResponseBadParam() {
        String expectedResponse = "{\"setAccountFact\":{\"successStatus\":false,"
                + "\"statusMessage\":\"setAccount: lastName userName firstName are "
                + "missing required field(s)\"}}";

        return expectedResponse;
    }
}
