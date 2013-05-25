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


import java.util.Map;
import java.util.HashMap;
import org.socraticgrid.presentationservices.utils.factQuery.EcsQuery;
import org.junit.After;
import org.junit.Before;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import java.io.IOException;
import java.util.Iterator;

import org.socraticgrid.presentationsservices.facttypes.ecsDetail.PatientDataFact;
import org.socraticgrid.presentationsservices.facttypes.ecsDetail.*;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author nhin
 */
public class TestEcsDetail {

    public TestEcsDetail() {
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
    
    
    
    private String getEcsData( String codeSystemName
                              ,String code
                              ,String sectionId ) throws IOException
    {
        
        String ECS_URL = 
            "http://stormwoods.info:8080/WexContentService/resources/codeinfo"
                + "/" + codeSystemName
                + "/" + code
                + "/" + sectionId;
        
        System.out.println("REQUEST URL is: " + ECS_URL);
        
        final WebClient webClient = new WebClient();
        final Page page = webClient.getPage(ECS_URL);
        WebResponse response = page.getWebResponse();
        
        String resultStr = "\n" + response.getContentAsString().toString() + "\n";
        System.out.println("ECS=\n"+resultStr);
        
        webClient.closeAllWindows();    
        
        return resultStr;
        
    }
    
    @Test
    public void testGetStaticECS() throws Exception {
        
        System.out.println("========= TESTING testGetStaticECS =========\n");
        
        //READ in ECS return from a file
        //sample ecs for ALLERGIES - SULFA DRUGS
        String filename = "/home/nhin/Properties/facts/data/ecs/snomed_91939003_2.html";
        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();

        
        //PREP gui response with ecs data
        //   from org.socraticgrid.presentationsservices.facttypes.ecsDetail
        
        PatientDataFact gui = new PatientDataFact();
        
        //PREP 
        gui.setFactType("Allergies");
        gui.setItemId(10);
        gui.setStatus("Complete");
        gui.setSuccessStatus(true);
        gui.setTrxnType("list");
        
        Facts aFact = new Facts();
        aFact.setReport(text);
        
        gui.getFacts().add(aFact);
        
        
        //TRANSFORM TO JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String retjson = gson.toJson(gui);
        
        //Add the required patientDataFact attribute wrapper.
        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\" : \n");
        sb.append(retjson + "}");

        System.out.println("RESPONSE=\n" + sb.toString());

        assertTrue(true);

    }
    
    
    @Test
    public void testGetDynamicECS() throws Exception {
        
        System.out.println("========= TESTING testGetDynamicECS =========\n");
        
        //CALL for ECS directly
        String text = this.getEcsData("snomed", "91939003", "2" );
        
        //PREP gui response with ecs data
        // from org.socraticgrid.presentationsservices.facttypes.ecsDetail
        
        PatientDataFact gui = new PatientDataFact();
        
        //PREP 
        gui.setFactType("Medications");
        gui.setItemId(10);
        gui.setStatus("Complete");
        gui.setSuccessStatus(true);
        gui.setTrxnType("ecs");
        
        Facts aFact = new Facts();
        aFact.setReport(text);
        
        gui.getFacts().add(aFact);
        
        
        //TRANSFORM TO JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String retjson = gson.toJson(gui);
        
        
        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\" : \n");
        sb.append(retjson + "}");

        System.out.println("RESPONSE=\n" + sb.toString());

        assertTrue(true);

        
    }
    
    @Test
    public void testGetPsECS() throws Exception {
        
        System.out.println("========= TESTING testGetPsECS ===========\n");
        
        String text = null;
        Map fieldMap = null;


        //CALL for ECS thru PS method
        fieldMap = new HashMap();
        fieldMap.put("domain", "Medications");
        fieldMap.put("responseType", "ecs");
        fieldMap.put("successStatus", true);
        fieldMap.put("code", "91939003");
        fieldMap.put("codeSystemCode", "snomed");
        fieldMap.put("sectionId", "2");
        text = EcsQuery.getInstance().createResponse(fieldMap, false);
        //System.out.println("RESP(snomed 91939003 sect=2)=\n" + text);

        //CALL for ECS thru PS method
        fieldMap = new HashMap();
        fieldMap.put("domain", "Allergies");
        fieldMap.put("responseType", "ecs");
        fieldMap.put("successStatus", true);
        fieldMap.put("code", "0093-4160-78");
        fieldMap.put("codeSystemCode", "ndc");
        fieldMap.put("sectionId", "2");
        text = EcsQuery.getInstance().createResponse(fieldMap, false);
        //System.out.println("RESP(2)=\n" + text);

        
        //CALL for ECS thru PS method
        fieldMap = new HashMap();
        fieldMap.put("domain", "Allergies");
        fieldMap.put("responseType", "ecs");
        fieldMap.put("successStatus", true);
        fieldMap.put("code", "0093-4160-78");
        fieldMap.put("codeSystemCode", "ndc");
        fieldMap.put("sectionId", "20");
        text = EcsQuery.getInstance().createResponse(fieldMap, false);
        //System.out.println("RESP(5)=\n" + text);
        
        
        assertTrue(true);

        
    }

    


}
