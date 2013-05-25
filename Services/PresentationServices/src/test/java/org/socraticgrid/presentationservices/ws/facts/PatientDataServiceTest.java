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

package org.socraticgrid.presentationservices.ws.facts;

import javax.xml.soap.MimeHeaders;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.MessageFactory;
import org.xml.sax.InputSource;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.socraticgrid.ps.PatientDataRequestType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 *
 * @author tnguyen
 */
public class PatientDataServiceTest {

    public PatientDataServiceTest() {
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

    /**
     * Test of getPatientData method, of class PatientDataService.
     */
    
    public void testGetFMQLPatientData() {
        System.out.println("getPatientData");

        PatientDataRequestType patientDataRequest = new PatientDataRequestType();
        patientDataRequest.setDomain("patient");
        patientDataRequest.setPatientId("2-8");
        patientDataRequest.setRequestType("raw");
        patientDataRequest.setUserId("1");
        patientDataRequest.setDataSource("FMQL");



        PatientDataService instance = new PatientDataService();
        String expResult = null;

        String dataURL = "http://172.31.5.104:8080/fmqlEP?fmql=";

        String result =
                new PatientDataImpl().getData(patientDataRequest);

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    @Test
    public void testGetMDWSLab() {
        System.out.println("getPatientData");

        //-------------------------------------------------
        // PARAMS that are normally pulled
        // from PresentationServices.properties at runtime
        //-------------------------------------------------
        String dataURL =  "http://172.31.5.48/mdws2/EmrSvc.asmx";
        String user = "1programmer";
        String password = "programmer1";
        String site = "102";

        //-------------------------------------------------
        // Preping the request
        //-------------------------------------------------
        PatientDataRequestType patientDataRequest = new PatientDataRequestType();
        patientDataRequest.setDomain("lab;med");
        patientDataRequest.setPatientId("100022");
        patientDataRequest.setRequestType("raw");
        patientDataRequest.setDataSource("MDWS");

        String result = new PatientDataImpl().getData(patientDataRequest);
//                new PatientDataImpl().getData(patientDataRequest,
//                                              dataURL,
//                                              patientDataRequest.getRequestType(),
//                                              user, password, site);


        System.out.println("\n========================================================");
        System.out.println("USING incoming user/pwd from request (i.e. SOAP Request)");
        patientDataRequest.setUserId("1programmer");
        patientDataRequest.setToken("programmer1");
        String result2 =
                new PatientDataImpl().getData(patientDataRequest);

    }


    private String getMdwsDataString () {
        return getMdwsData().toString();
    }
    private StringBuilder getMdwsData() {

        System.out.println("getPatientData");

        String user = "1programmer";
        String password = "programmer1";
        String site = "102";

        PatientDataRequestType patientDataRequest = new PatientDataRequestType();
        patientDataRequest.setDomain("lab");
        patientDataRequest.setPatientId("100022");
        patientDataRequest.setRequestType("raw");
        patientDataRequest.setUserId("1");
        patientDataRequest.setDataSource("MDWS");



        PatientDataService instance = new PatientDataService();
        String expResult = null;

        String dataURL =  "http://172.31.5.48/mdws2/EmrSvc.asmx";

        String result =
                new PatientDataImpl().getData(patientDataRequest,
                                              dataURL,
                                              patientDataRequest.getRequestType(),
                                              user, password, site);
        System.out.println(result);

        StringBuilder sb = new StringBuilder();
        sb.append(result);

        return sb;
    }
    public static Document buildMessage(StringBuilder sb) throws Exception
    {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        Document retMessage = docBuilder.parse(new InputSource(new StringReader(sb.toString())));
        return retMessage;

//       DocumentBuilderFactory fctr = DocumentBuilderFactory.newInstance();
//       DocumentBuilder bldr = fctr.newDocumentBuilder();
//       InputSource insrc = new InputSource(new StringReader(xml));
//       return bldr.parse(insrc);
    }
    @Test
    public void test_stringTOXML() throws Exception {

        //Document doc = buildMessage(getMdwsData());



    }
}