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
 * To process JSON, this uses gson:
 * - https://sites.google.com/site/gson/gson-user-guide
 *
 * SVN Ontology: http://184.191.173.238:90/svn/socraticgrid/trunk/projects/KMR/Ontology/KMR_Ontology.ttl
 *
 */

public class KMROPatientTriples {

    //--------------------------------------------------------------------
    // FMQL endpoint:  http://172.31.5.104:8080/fmqlEP?
    //      i.e. http://172.31.5.48:8080/fmqlEP?fmql=DESCRIBE 2-9
    //
    // MDWS endpoint:  http://172.31.5.104/mdws2/EmrSvc.asmx?
    //      i.e. You have to issue a SELECT of patient DFN first, before
    //      executing any other requests.
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/select?DFN=100022
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllOrders
    //           http://172.31.5.104/mdws2/EmrSvc.asmx/getAllMeds
    //
    // JENA endpoint:  http://172.31.5.254/ds/sparql?query=
    //      i.e.   http://172.31.5.104/ds/sparql?query=SELECT * {?s ?p ?o}
    //--------------------------------------------------------------------

    protected String repoEP = null;
    protected String repoInterface = null; // FMQL, MDWS
//    protected String repoUser = null;
//    protected String repoPassword = null;
    protected String repoSiteId = null;

    private static String respFormatXML = "xml";

    Boolean trace = false;

    public KMROPatientTriples() {
    }
    
    public KMROPatientTriples(String repoEndpoint) {
        this.repoEP = repoEndpoint;
    }

    /**
     *
     * @param dataSourceURL - the data source server endpoint
     * @param dataSourceInterface - "FMQL" or "MDWS"
     */
    public KMROPatientTriples(String dataSourceURL, String dataSourceInterface)
    {
        this.repoEP = dataSourceURL;
        this.repoInterface = dataSourceInterface;
    }

    /**
     * 
     * @param patientId
     * @param domain
     * @param user
     * @param password
     * @param siteId
     * @return
     * @throws Exception
     */
    public String getMDWSData(String patientId, String domain, String user,
                              String password, String siteId) throws Exception {

        String response = null;

        MDWSQueryUtil request = new MDWSQueryUtil(this.repoEP, user, password);
        response = request.getData(patientId, siteId, domain);

        return response;
    }

    /**
     * UNUSED as yet
     *
     * @param patientId
     * @return
     * @throws Exception
     */
//    public String getSPARQLData(String patientId, String domain, String user) throws Exception {
//
//        String response = null;
//
//        SPARQLQueryUtil request = new SPARQLQueryUtil(this.repoEP, this.respFormatXML);
//        //response = request.getShallowFactItem(patientId, domain, user)
//
//        return response;
//    }

    /**
     * UNUSED as yet
     * 
     * @param patientId
     * @return
     * @throws Exception
     */
    public String getFMQLData(String patientId) throws Exception {
        String response = null;

        FMQLQueryUtil request = new FMQLQueryUtil(this.repoEP, this.respFormatXML);
        response = request.getShallowFactItem(patientId);
        
        return response;
    }

    /**
     * Given a patientId, get all demographics data from the defined data source
     * endpoint listed in the PresentationServices.properties file, under attributes:
     *
     *      PatientDataSource
     *      PatientDataSourceType
     *      PatientDataReturnFormat
     *
     * @param patientId
     * @return
     * @throws Exception
     */
    public String getDemographicsByPatient(String patientId) throws Exception
    {
        String response = null;

        if (this.repoInterface.equalsIgnoreCase("FMQL")) {

            FMQLQueryUtil request = new FMQLQueryUtil(this.repoEP, this.respFormatXML);
            response = request.getShallowFactItem(patientId);



        } else if (this.repoInterface.equalsIgnoreCase("SPARQL")) {
            
            SPARQLQueryUtil request = new SPARQLQueryUtil(this.repoEP);
//TDB            response = request....

        } else {
            System.out.println("ERROR: Invalid or no Data Source type given.");
        }

        return response;
    }

    public String getLabsByPatient(String patientId) throws Exception
    {
        String response = null;

        if (this.repoInterface.equalsIgnoreCase("FMQL")) {

            FMQLQueryUtil request = new FMQLQueryUtil(this.repoEP, this.respFormatXML);
            response = request.getShallowFactItem(patientId);

        } else {
            System.out.println("ERROR: Invalid or no Data Source type given.");
        }

        return response;
    }

    /**
     * GET MEDICATIONS by PATIENTID
     *
     * Note: response is a flatten out version of the deep arrangement
     *       of KMRO for Medication Orders
     * @param patientId
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getMedicationOrdersByPatient(String patientId) throws Exception
    {
        List<Map<String, String>> response = null;

        if (this.repoInterface.equalsIgnoreCase("FMQL")) {

            FMQLQueryUtil request = new FMQLQueryUtil(this.repoEP, this.respFormatXML);
            //response = request.getMedicationOrdersByPatient(patientId);


        } else if (this.repoInterface.equalsIgnoreCase("SPARQL")) {
            SPARQLQueryUtil request = new SPARQLQueryUtil(this.repoEP);
            //response = request.getMedicationOrdersByPatient(patientId);

        } else {
            System.out.println("ERROR: Invalid or no Data Source type given.");
        }

        return response;
    }


    /**
     * TODO
     *
     * From Order to Dispensation.
     * @param orderId
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getDispensationsOfMedicationOrder(String orderId) throws Exception
    {
        List<Map<String, String>> response = null;

        if (this.repoInterface.equalsIgnoreCase("FMQL")) {
            FMQLQueryUtil request = new FMQLQueryUtil(this.repoEP, this.respFormatXML);
            //response = request....


        } else if (this.repoInterface.equalsIgnoreCase("SPARQL")) {
            SPARQLQueryUtil request = new SPARQLQueryUtil(this.repoEP);
            //response = request....

        } else {
            System.out.println("ERROR: Invalid or no Data Source type given.");
        }

        return response;
    }



    /**
     * TODO
     * @param patientId
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getDiagnosesByPatient(String patientId) throws Exception
    {
        List<Map<String, String>> response = null;

        //TBD Query call.

        return response;
    }


    /**
     *  TODO
     * @param patientId
     * @param domain
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getConceptsOfPatientByDomain(String patientId, String domain) throws Exception
    {
        List<Map<String, String>> response = null;

        //TBD Query call.

        return response;
    }


    /**
     *  TODO
     * @param patientId
     * @param domain
     * @return
     * @throws Exception
     */
    public List<String> getDatesOfPatientByDomain(String patientId, String domain) throws Exception
    {
        List<String> response = null;

        //TBD Query call.

        return response;
    }

    /**
     *  TODO
     * @param patientId
     * @param domain
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getValuesOfPatientByDomainAndConcept(String patientId, String domain, String conceptCode) throws Exception
    {
        List<Map<String, String>> response = null;

        //TBD Query call.

        return response;
    }




    public void printBuffer(BufferedReader in) {
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
