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

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
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
public class SetMessagesEmailTest {

    private String server = PresentationServicesSuite.defaultServer;

    public SetMessagesEmailTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetMessagesEmail() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmail");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Read"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("locations", "INBOX"));
        request.getRequestParameters().add(new NameValuePair("labels", "starred"));
        request.getRequestParameters().add(new NameValuePair("body", "UNSTUBBED BODY COMING FROM UNIT TEST"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        request.getRequestParameters().add(new NameValuePair("subject", "UNSTUBBED SUBJECT COMING FROM UNIT TEST"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailManyArch() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmailManyArch");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Archive"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "1,1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1,2"));
        request.getRequestParameters().add(new NameValuePair("types", "Email,Email"));
        request.getRequestParameters().add(new NameValuePair("locations", "INBOX,INBOX"));
        //      request.getRequestParameters().add(new NameValuePair("labels", "starred,starred"));
        request.getRequestParameters().add(new NameValuePair("body", "UNSTUBBED BODY COMING FROM UNIT TEST"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        request.getRequestParameters().add(new NameValuePair("subject", "UNSTUBBED SUBJECT COMING FROM UNIT TEST"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailArchive() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmail");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Archive"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("locations", "INBOX"));
        //      request.getRequestParameters().add(new NameValuePair("labels", "starred"));
        //      request.getRequestParameters().add(new NameValuePair("body", "UNSTUBBED BODY COMING FROM UNIT TEST"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        //      request.getRequestParameters().add(new NameValuePair("subject", "UNSTUBBED SUBJECT COMING FROM UNIT TEST"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailDelete() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmail");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Delete"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("messageIds", "1"));
        request.getRequestParameters().add(new NameValuePair("locations", "UserTrash"));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailSend() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmailSend");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Send"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("patientId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("locations", "Inbox"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "doe.jane"));
        request.getRequestParameters().add(new NameValuePair("CCTo", "fry.emory,fry.emory"));
        request.getRequestParameters().add(new NameValuePair("BCCTo", "fry.emory"));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        String subject = "FROM UNIT TEST: TESTING AT TIME - " + new Date();
        request.getRequestParameters().add(new NameValuePair("subject", subject));
        request.getRequestParameters().add(new NameValuePair("body", "FROM UNIT TEST BODY - " + new Date()));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailSend2() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmailSend");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Send"));
        request.getRequestParameters().add(new NameValuePair("userId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("locations", "INBOX"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "fry.emory"));
        request.getRequestParameters().add(new NameValuePair("CCTo", ""));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        String subject = "TESTING AT TIME - " + new Date();
        request.getRequestParameters().add(new NameValuePair("subject", subject));
        request.getRequestParameters().add(new NameValuePair("body", "TEST BODY - " + new Date()));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testSetMessagesEmailDraft() throws Exception {
        System.out.println("TEST CASE: testSetMessagesEmailSave");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setMessages"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("action", "Save"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("locations", "Inbox"));
        request.getRequestParameters().add(new NameValuePair("sendTo", "fry.emory"));
        request.getRequestParameters().add(new NameValuePair("CCTo", "fry.emory"));
        request.getRequestParameters().add(new NameValuePair("BCCTo", "fry.emory"));
        request.getRequestParameters().add(new NameValuePair("types", "Email"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        String subject = "TESTING AT TIME - " + new Date();
        request.getRequestParameters().add(new NameValuePair("subject", subject));
        request.getRequestParameters().add(new NameValuePair("body", "TEST BODY - " + new Date()));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nEMAIL RESPONSE IS: \n" + stringResponse + "\n");
        System.out.println("EXPECTED FOR BOTH is: \n" + getExpectedResponse() + "\n");
        assertTrue(stringResponse.contains(getExpectedResponse()));

        client.closeAllWindows();
    }

    private String getExpectedResponse() {
        return "{\"setMessagesFact\":{\"successStatus\":true,\"statusMessage\":\"Set Messages successful\"}}";
    }
}
