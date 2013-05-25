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

package org.socraticgrid.presentationservices;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.Ignore;
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
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
public class GetPatientDataTest {

    private String server = PresentationServicesSuite.defaultServer;

    public GetPatientDataTest() {
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
        System.out.println("GETPATIENTDATA HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLabsFact() throws IOException {
        System.out.println("LABS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getTestResultsUrl());
        System.out.println("Request URL is " + getTestResultsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));
        webClient.closeAllWindows();
    }

    @Test
    public void testAllergiesFact() throws IOException {
        System.out.println("ALLERGIES TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getAllergiesUrl());
        System.out.println("Request URL is " + getAllergiesUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testDemographicsFact() throws IOException {
        System.out.println("DEMOGRAPHICS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getDemographicsUrl());
        System.out.println("Request URL is " + getDemographicsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

        @Test
    public void testVitalsFact() throws IOException {
        System.out.println("VITALS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getVitalsUrl());
        System.out.println("Request URL is " + getVitalsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testImmunizationsFact() throws IOException {
        System.out.println("IMMUNIZATIONS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getImmunizationsUrl());
        System.out.println("Request URL is " + getImmunizationsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testMedicationsFact() throws IOException {
        System.out.println("MEDICATIONS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getMedicationsUrl());
        System.out.println("Request URL is " + getMedicationsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testProblemsFact() throws IOException {
        System.out.println("PROBLEMS TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getProblemsUrl());
        System.out.println("Request URL is " + getProblemsUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testProceduresFact() throws IOException {
        System.out.println("PROCEDURES TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getProceduresUrl());
        System.out.println("Request URL is " + getProceduresUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true,"));

        webClient.closeAllWindows();
    }

    @Test
    @Ignore
    public void testProceduresDetailFact() throws IOException {
        System.out.println("PROCEDURES DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getProceduresDetailUrl());
        System.out.println("Request URL is " + getProceduresDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }

    @Test
    @Ignore
    public void testLabsDetailFact() throws IOException {
        System.out.println("LABS DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getTestResultsDetailUrl());
        System.out.println("Request URL is " + getTestResultsDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }

    @Test
    public void testDemographicsDetailFact() throws IOException {
        System.out.println("DEMOGRAPHICS DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getDemographicsDetailUrl());
        System.out.println("Request URL is " + getDemographicsDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }

    @Test
    @Ignore
    public void testImmunizationsDetailFact() throws IOException {
        System.out.println("IMMUNIZATIONS DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getImmunizationsDetailUrl());
        System.out.println("Request URL is " + getImmunizationsDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }

    @Test
    @Ignore
    public void testMedicationsDetailFact() throws IOException {
        System.out.println("MEDICATIONS DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getMedicationsDetailUrl());
        System.out.println("Request URL is " + getMedicationsDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }
    
    @Test
    @Ignore
    public void testProblemsDetailFact() throws IOException {
        System.out.println("PROBLEMS DETAIL TEST CASE: testGetPatientDataFact");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getProblemsDetailUrl());
        System.out.println("Request URL is " + getProblemsDetailUrl());
        WebResponse response = page.getWebResponse();
        System.out.println("\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("\"successStatus\": true ,"));

        webClient.closeAllWindows();
    }

    private String getTestResultsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=TestResults&responseType=list&token=6C";
    }

     private String getDemographicsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Demographics&responseType=list&token=6C";
    }

    private String getAllergiesUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Allergies&responseType=list&token=6C";
    }

    private String getVitalsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Vitals&responseType=list&token=6C";
    }

    private String getImmunizationsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Immunizations&responseType=list&token=6C";
    }

    private String getMedicationsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Medications&responseType=list&token=6C";
    }

    private String getProblemsUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Problems&responseType=list&token=6C";
    }

    private String getProceduresDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=procedures&responseType=detail&itemId=6ef15284-77f6-4e1f-bf9f-8729c4c1b37d&communityId=2.16.840.1.113883.3.200&token=6C";
    }

    private String getProceduresUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=Procedures&responseType=list&token=6C";
    }

    private String getTestResultsDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=testResults&responseType=detail&itemId=17d896be-d969-41a9-a87d-9ddc4d7f19ce&communityId=2.16.840.1.113883.3.200&token=6C";
    }

    private String getImmunizationsDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=immunizations&responseType=detail&itemId=96787be6-bc9a-41cc-832a-471a452b6dd8&communityId=2.16.840.1.113883.3.200&token=6C";
    }

    private String getMedicationsDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=medications&responseType=detail&itemId=133208ed-a21c-4022-91c6-79241d37d627&communityId=2.16.840.1.113883.3.200&token=6C";
    }

    private String getDemographicsDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=demographics&responseType=detail&token=6C&itemId=99990070&communityId=2.16.840.1.113883.3.200";
    }
    
    private String getProblemsDetailUrl() {
        return "http://" + server + ":8080/PresentationServices/getPatientData?userId=99990070&domain=problems&responseType=detail&token=6C&itemId=6ccae4fb-96ee-4450-a2a6-2e91963da27b&communityId=2.16.840.1.113883.3.200";
    }
}
