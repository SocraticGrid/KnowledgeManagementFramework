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

import java.util.Date;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import java.io.IOException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author nhin
 */
public class GetMessagesTest {

    private String server = PresentationServicesSuite.defaultServer;

    public GetMessagesTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        System.out.println("\n\nGETMESSAGES Test beginning: " + new Date());
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        System.out.println("GETMESSAGES HOST IS: " + server);
    }

    @After
    public void tearDown() {
        System.out.println("\nGETMESSAGES Test ending: " + new Date());
    }

    @Test
    public void testGetMessagesAlertsNoPt() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAlerts - no patient");
        System.out.println("REQUEST URL IS " + getAlertURLNoPt());
        final WebClient webClient = new WebClient();
        final Page page =
                webClient.getPage(getAlertURLNoPt());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesAlertsResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAlertsNoPtArch() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAlerts - NO patient Archive");
        System.out.println("REQUEST URL IS " + getAlertURLNoPtArch());
        final WebClient webClient = new WebClient();
        final Page page =
                webClient.getPage(getAlertURLNoPtArch());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesAlertsResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAlertsPt() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAlerts - with patient");
        System.out.println("REQUEST URL iS " + getAlertURLPt());
        final WebClient webClient = new WebClient();
        final Page page =
                webClient.getPage(getAlertURLPt());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesAlertsResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAlertsBadParam() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAlertsBadParam");
        System.out.println("REQUEST URL iS " + getAlertURLBadParam());
        final WebClient webClient = new WebClient();
        final Page page =
                webClient.getPage(getAlertURLBadParam());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        System.out.println("Response expected: \n" + getMessagesAlertsBadParamResponse());
        assertTrue(response.getContentAsString().contains(getMessagesAlertsBadParamResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAllPt() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAll - Patient");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllURLPt());
        System.out.println("REQUEST URL iS " + getAllURLPt());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesEmailResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAllNoPtSent() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAll - No Patient Sent");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllURLNoPtSent());
        System.out.println("REQUEST URL iS " + getAllURLNoPtSent());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesEmailResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAllNoPt() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAll - No Patient");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllURLNoPt());
        System.out.println("REQUEST URL iS " + getAllURLNoPt());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesEmailResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAllArchNoPt() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAll - Archive No Patient");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllURLArchNoPt());
        System.out.println("REQUEST URL iS " + getAllURLArchNoPt());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesEmailResponse()));

        webClient.closeAllWindows();
    }

    @Test
    public void testGetMessagesAllNoPtDraft() throws IOException {
        System.out.println("TEST CASE: testGetMessagesAll - No Patient Draft");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllURLNoPtDraft());
        System.out.println("REQUEST URL iS " + getAllURLNoPtDraft());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getMessagesEmailResponse()));

        webClient.closeAllWindows();
    }

    private String getMessagesAlertsResponse() {
        String expectedResponse = "\"successStatus\": true";
        return expectedResponse;
    }

    private String getMessagesAlertsBadParamResponse() {
        String response = "{\"getMessagesFact\":{\"successStatus\":false,\"statusMessage\":\""
                + "getMessages: userId is a missing required field\"}}";
        return response;
    }

    private String getAlertURLNoPt() {
        return "http://" + server + ":8080/PresentationServices/getMessages?"
                + "userId=1&type=Alert&location=INBOX&token=6C";
    }

    private String getAlertURLNoPtArch() {
        return "http://" + server + ":8080/PresentationServices/getMessages?"
                + "userId=1&type=Alert&location=Archive&token=6C";
    }

    private String getAlertURLPt() {
        return "http://" + server + ":8080/PresentationServices/getMessages?"
                + "userId=1&patientId=99990070&type=Alert&location=INBOX&token=6C";
    }

    private String getAlertURLBadParam() {
        return "http://" + server + ":8080/PresentationServices/getMessages?"
                + "patientId=99990070&user=99990070&type=Alert&token=6C";
    }

    private String getAllURLPt() {
        return "http://" + server + ":8080/PresentationServices/getMessages?"
                + "patientId=99990070&userId=1&location=INBOX&token=6C";
    }

    private String getAllURLNoPt() {
        return "http://" + server + ":8080/PresentationServices/getMessages"
                + "?type=All&userId=1&location=INBOX&token=6C";
    }

    private String getAllURLArchNoPt() {
        return "http://" + server + ":8080/PresentationServices/getMessages"
                + "?type=All&userId=1&location=Archived&token=6C";
    }

    private String getMessagesEmailResponse() {
        String expectedResponse = "\"successStatus\": true";
        return expectedResponse;
    }

    private String getAllURLNoPtSent() {
        return "http://" + server + ":8080/PresentationServices/getMessages"
                + "?type=All&userId=1&location=Sent&token=6C";
    }

    private String getAllURLNoPtDraft() {
        return "http://" + server + ":8080/PresentationServices/getMessages"
                + "?type=All&userId=1&location=Draft&token=6C";
    }
}
