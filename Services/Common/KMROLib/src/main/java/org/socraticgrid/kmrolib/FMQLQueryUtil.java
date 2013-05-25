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
package org.socraticgrid.kmrolib;

import java.net.*;
import java.io.*;
import java.util.*;
import com.google.gson.*; /* http://code.google.com/p/google-gson/ */
import java.util.UUID;
import java.text.SimpleDateFormat;

/*
 * A Utility for accessing a KMRO-conformant triple store.
 *
 * To use:
 * - down gson JAR and make sure it is in your classpath
 * - crude invocation if JAR for gson is in this file's directory
 *   - javac -classpath 'gson-VERSION.jar' KMROPatientTriples.java
 *   - java -classpath '.:gson-VERSION.jar' KMROPatientTriples
 * Look at invocation code in 'main' and copy in your own code.
 *
 * Background:
 * KMR has three triple stores. Two (KMRO, KMRO10) are accessible through
 * this utility.
 * - DANNOCOPY_EP: Copy of Danno (VS scheme)
 * - KMRO_EP: Danno patients in KMRO
 * - KMRO10_EP: best of KMRO Danno patients. Hand done until end.
 *
 * To process JSON, this uses gson:
 * - https://sites.google.com/site/gson/gson-user-guide
 *
 * SVN Ontology: http://184.191.173.238:90/svn/socraticgrid/trunk/projects/KMR/Ontology/KMR_Ontology.ttl
 *
 */

public class FMQLQueryUtil {

    //--------------------------------------------------------------------
    // FMQL endpoint:  http://172.31.5.104:8080/fmqlEP?
    //      i.e. http://172.31.5.104:8080/fmqlEP?fmql=DESCRIBE 2-9
    //
    // MDWS endpoint:  http://172.31.5.104/mdws2/EmrSvc.asmx?
    //      i.e. You have to issue a SELECT of patient DFN first, before
    //      executing any other requests.
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/select?DFN=100022
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllOrders
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllMeds
    //
    // JENA endpoint:  http://172.31.5.104/ds/sparql?query=
    //      i.e.   http://172.31.5.104/ds/sparql?query=SELECT * {?s ?p ?o}
    //--------------------------------------------------------------------

    Boolean trace = false;

    //--------------------------------------------------------------------
    // SPARQL formatted queries
    //--------------------------------------------------------------------
    private String sparqlEP;
    private String returnFormat;

    private static String FMQL_QUERY_DESCRIBEFACT = "DESCRIBE %s";

    //...... TDB .........................
    private static String FMQL_QUERY_GETMEDSBYPATIENTID = "DESCRIBE %s";  
    private static String FMQL_QUERY_GETDIAGNOSESBYPATIENTID = "DESCRIBE %s";



    //--------------------------------------------------------------------
    // CONSTRUCTORs
    //--------------------------------------------------------------------
    public FMQLQueryUtil() {
    }

    public FMQLQueryUtil(String serviceEndpoint, String retFormat) {
        this.sparqlEP = serviceEndpoint;
        this.returnFormat = retFormat;
    }

    //--------------------------------------------------------------------
    // PROCESS
    //--------------------------------------------------------------------
    /**
     * This will send the FMQL format query to the FILEMAN interface, and get a
     * response as a BufferedReader object.
     * 
     * @param query
     * @return
     * @throws Exception
     */
    public BufferedReader request(String query) throws Exception {

        String enc = URLEncoder.encode(query, "UTF-8");
        System.out.println("query= "+ query);
        System.out.println("enc= "+ enc);
        
        //String sparqlrs = this.sparqlEP + "?fmql=" + URLEncoder.encode(query, "UTF-8");
        String sparqlrs = this.sparqlEP + URLEncoder.encode(query, "UTF-8");
        


        
        System.out.println("SPARQLEP+query= "+ sparqlrs);

        URL sparqlr = new URL(sparqlrs);
        // 1. Make the query
        URLConnection sparqlc = sparqlr.openConnection();
        // 2. Read the Response
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                sparqlc.getInputStream()));
        return in;
    }

    
    /**
     *
     * @param entryId - is unique identifier to the record
     *   i.e.   "2-9"  indicates the demographic data for patientId=9
     * @return
     * @throws Exception
     */
    public String getShallowFactItem(String entryId) throws Exception
    {
        String query = String.format(FMQL_QUERY_DESCRIBEFACT, entryId);
        
        // EXEC the query
        BufferedReader in = null;
        in = request(query);
//
        // PARSE into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject reply = parser.parse(in).getAsJsonObject();
        return reply.toString();
//
//        String xmlresponse = null;
//        String inputLine = null;
//        while ((inputLine = in.readLine()) != null) {
//System.out.println(inputLine);
//            xmlresponse = xmlresponse + inputLine;
//        }
//
//        System.out.println("XML: "+xmlresponse);
//        return inputLine;
    }

    private void printBuffer(BufferedReader in) {
        try {
            String inputLine = null;
            System.out.println("---------- BUFFER ----------\n");
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            System.out.println("---------- BUFFER ----------\n");

        } catch (IOException e) {
            System.err.println("Error: " + e);
        }
    }
}
