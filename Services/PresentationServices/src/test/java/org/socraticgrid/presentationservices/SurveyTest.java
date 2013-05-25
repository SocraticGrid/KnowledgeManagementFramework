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

import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
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
public class SurveyTest {

    private String server = PresentationServicesSuite.defaultServer;

    public SurveyTest() {
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
        System.out.println("SURVEY TEST HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }


    @Test
    public void testGetSurvey() throws Exception {
        System.out.println("TEST CASE: testGetSurvey");
        System.out.println("REQUEST URL is: " + getSurveyURL());
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(getSurveyURL());
        WebResponse response = page.getWebResponse();
        String resultStr = "\n" + response.getContentAsString().toString() + "\n";
        System.out.println(resultStr);
        webClient.closeAllWindows();
        assertTrue(response.getContentAsString().contains("surveyFact"));
    }

    @Test
    public void testSetSurvey() throws Exception {
        System.out.println("TEST CASE: testSetSurvey");
        final WebClient client = new WebClient();
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/setSurvey"),
                HttpMethod.POST);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("patientId", "patient1"));
        request.getRequestParameters().add(new NameValuePair("surveyId", "123456UNIQUESURVEYID"));
        request.getRequestParameters().add(new NameValuePair("questionId", "39efb60c-616b-4276-a644-9421b22f0746"));
        request.getRequestParameters().add(new NameValuePair("answer", "22"));
        request.getRequestParameters().add(new NameValuePair("token", "6C"));

        System.out.println("\nREQUEST URL IS: " + request.toString());

        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        System.out.println("\n SET SURVEY RESPONSE IS: \n" + stringResponse + "\n");
        client.closeAllWindows();
        assertTrue(stringResponse.contains(getExpectedResponse()));
    }

    private String getSurveyURL() {
        String url = "http://" + server + ":8080/PresentationServices/"
                + "getSurvey?userId=patient1&patientId=surveyPatient&"
                + "surveyId=123456UNIQUESURVEYID&token=6C";
        return url;
    }

    private String getExpectedResponse() {
        return "";
    }
}
