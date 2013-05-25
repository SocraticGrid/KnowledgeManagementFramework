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
package org.socraticgrid.presentationservices.utils.factQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.account.util.PropertyAccessException;
import org.socraticgrid.account.util.PropertyAccessor;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.presentationsservices.facttypes.ecsDetail.Facts;
import org.socraticgrid.presentationsservices.facttypes.ecsDetail.PatientDataFact;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author nhin
 */
public class EcsQuery {
    
    private String ECS_URL = null;
    private String classname = "\n"+this.getClass().getName()+":";
    private static EcsQuery instance = null;

    String PROPERTY_FILE = "PresentationServices";

    public static EcsQuery getInstance() {
        synchronized (EcsQuery.class) {
            if (instance == null) {
                instance = new EcsQuery();
            }
        }
        return instance;
    }

    public void initProps() {
        try {
            ECS_URL = PropertyAccessor.getProperty(PROPERTY_FILE, "ECSUrl");
System.err.println("===> USING ECS= "+ ECS_URL);

        } catch (PropertyAccessException e) {
            System.out.println("Error accessing properties in file:" + PROPERTY_FILE
                    + "\n[EXCEPTION] " + e);
        }
    }
    
    /**
    /* args = all expected incoming attributes:
     *
     *  userId
     *  token
     *  domain
     *  responseType     list, detail, ecs
     *  code             optional
     *  codeSystemCode   optional
     *  sectionId        optional
     *  fromDate         optional
     *  toDate           optional
     *  returnCount      optional
     *
     * @param args
     * @return String containing the JSON 
     * @throws FileNotFoundException 
     */
    public String createResponse(Map args, boolean useStubbedData) throws FileNotFoundException{

        this.initProps();
        
        return getECS( (String) args.get("code")
                      ,(String) args.get("codeSystemCode")
                      ,(String) args.get("sectionId")
                      ,(String) args.get("domain")
                      ,(String) args.get("responseType")
                      ,(String) args.get("")
                      ,useStubbedData
                     );
        //return this.getStubbedXMLDetailData(codeSystemCode, code, sectionId);
    }
        

    private String getECS( String code, String codeSystemCode
                          ,String sectionId, String domain
                          ,String responseType, String factStatus
                          ,boolean useStubbedData)
    {
        String ecsResp = null;
        StringBuilder sb = new StringBuilder("{\n\"patientDataFact\" : \n");
        StringBuilder urlString = new StringBuilder(ECS_URL).append(codeSystemCode);
        
        try {

            if (!CommonUtil.strNullorEmpty(code)) {
                urlString.append("/").append(code);
            }
            if (!CommonUtil.strNullorEmpty(sectionId)) {
                urlString.append("/").append(sectionId);
            } 

            
            URL url = new URL(urlString.toString());


            //Get ECS content.  Stubbed if useStubbedData == true.  Else live.
            if (useStubbedData) {
                System.out.println("EcsQuery: USING STATIC ECS DATA");
                ecsResp = this.getStubbedXMLDetailData(codeSystemCode, code, sectionId);
            }
            else {

                System.out.println("EcsQuery: ECS URL request: " + urlString);
                ecsResp = getURLContent(url);

                //set "No ECS data found" msg for ecs content when no ecs data found.
                if ((ecsResp == null) ||ecsResp.isEmpty()) {
                    ecsResp = "<p/>No ECS data available yet for:   "
                            + codeSystemCode +"/"+ code +"/"+ sectionId;
                }
            }
                

            //PREP gui response with ecs data
            //to classes in org.socraticgrid.presentationsservices.ecsDetails.ecsDetail

            PatientDataFact gui = new PatientDataFact();

            //PREP 
            gui.setFactType(domain);
            gui.setStatus(factStatus);
            gui.setSuccessStatus(true);
            gui.setTrxnType(responseType);

            Facts aFact = new Facts();
            aFact.setReport(ecsResp);

            gui.getFacts().add(aFact);


            //TRANSFORM TO JSON
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String retjson = gson.toJson(gui);

            //ADD to 
            sb.append(retjson + "}");

            System.out.println("EcsQuery: RESP=\n" + sb.toString());
            
        } catch (IOException ex) {
            String msg = "{ getPatientData: { successStatus: false, statusMessage: \"Error fetching content from ECS URL=\""+urlString+"  }}";
            System.out.println(msg);
            return msg;
        }
        
        return sb.toString();
        
    }

    private String getURLContent(URL url) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                // Process each line.
                sb.append(inputLine);
            }
        } finally {
            in.close();
        }
        return sb.toString();
    }
    
    
    private String getStubbedXMLDetailData(String codeSystemCode, String code,
                                          String sectionId)
            throws FileNotFoundException 
    {       
        String filename = "/home/nhin/Properties/facts/data/ecs/"
                            + codeSystemCode+"_"+code+"_"+sectionId+".html";
     
        System.out.println("==>PULLING ecs STATIC DATA:  "+filename);
        
        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
        return text;
    }
    
}
