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
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import org.apache.commons.logging.LogFactory;
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
public class DiagnosticsTests {

    private String server = PresentationServicesSuite.defaultServer;
    private String dxProcId = "b7ed7564-f832-4cb3-b120-98a1f8343ad2";
    private String actId = "f3889adf-31c0-40b0-bf46-fa18fd9e2229";
    
    public DiagnosticsTests() {
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
        System.out.println("DIAGNOSTICS TESTS - HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    
    @Test
    public void testStartDGProcess() throws Exception {
        System.out.println("TEST CASE: testStartDGProcess");
        System.out.println("Request URL is " + startDGProcessURL());
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(startDGProcessURL());        
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(startDGProcessResponse()));
        webClient.closeAllWindows();
    }
    
    @Test
    public void testGetDGProcessStatus() throws Exception {
        System.out.println("TEST CASE: testGetDGProcessStatus");
        System.out.println("Request URL is " + getDGProcessStatusUrl());
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(getDGProcessStatusUrl());
        System.out.println("Request URL is " + getDGProcessStatusUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(getDGProcessStatusResponse()));
        webClient.closeAllWindows();
    }

    @Test
    public void testSetDiagActionStatus() throws Exception {
        System.out.println("TEST CASE: testSetDiagActionStatus");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server
                + ":8080/PresentationServices/setDiagnosticActionStatus"),
                HttpMethod.POST);

        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("token", "6C"));
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("patientId", "99990070"));
        request.getRequestParameters().add(new NameValuePair("dxProcessId", dxProcId));
        request.getRequestParameters().add(new NameValuePair("actionId", actId));
        request.getRequestParameters().add(new NameValuePair("status", "Started"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\nSETDIAG RESPONSE IS: \n" + stringResponse + "\n");
        assertTrue(stringResponse.contains(setDiagActionStatusResponse()));

        client.closeAllWindows();
    }

    @Test
    public void testCompleteDGProcess() throws Exception {
        System.out.println("TEST CASE: testCompleteDGProcess");
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(completeDGProcessURL());
        System.out.println("Request URL is " + completeDGProcessURL());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(completeDGProcessResponse()));
        webClient.closeAllWindows();
    }

    @Test
    public void testAdvanceDGProcess() throws Exception {
        System.out.println("TEST CASE: testAdvanceDGProcess");
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(advanceDGProcessURL());
        System.out.println("Request URL is " + advanceDGProcessURL());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains(advanceDGProcessResponse()));
        webClient.closeAllWindows();
    }

    private String getDGProcessStatusUrl() {
        
        String url = "http://" + server + ":8080/PresentationServices"
                + "/getDiagnosticGuideProcessStatus?dxProcessId=" + dxProcId
                + "&userId=1&patientId=99990070&token=6C";
        return url;
    }

    private String startDGProcessURL() {
        String url = "http://" + server + ":8080/PresentationServices"
                + "/startDiagnosticGuideProcess?userId=1&token=6C&"
                + "disease=Post Traumatic Stress Disorder&patientId=99990070";
        return url;
    }

    private String completeDGProcessURL() {
        String url = "http://" + server + ":8080/PresentationServices"
                + "/completeDiagnosticGuideProcess?"
                + "dxProcessId=" + dxProcId + "&token=6C&"
                + "status=Complete&userId=1&patientId=99990070";
        return url;
    }

    private String advanceDGProcessURL() {
        String url = "http://" + server + ":8080/PresentationServices"
                + "/advanceDiagnosticGuideProcess?"
                + "dxProcessId=" + dxProcId + "&token=6C&"
                + "userId=1&patientId=99990070";
        return url;
    }
    
    // Just make sure some respnose came back. Can't completely check as
    // responses are dynamic

    private String getDGProcessStatusResponse() {
        String response = "diagModel";
        return response;
    }

    private String setDiagActionStatusResponse() {
        String response = "[";
        return response;
    }

    private String startDGProcessResponse() {
        String response = "[";
        return response;
    }

    private String completeDGProcessResponse() {
        String response = "";
        return response;
    }

    private String advanceDGProcessResponse() {
        String response = "diagModel";
        return response;
    }
}
