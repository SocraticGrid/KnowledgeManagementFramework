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
 * A Utility for accessing a SPARQL 1.1 triple store.
 *
 * To use:
 * - down gson JAR and make sure it is in your classpath
 * - crude invocation if JAR for gson is in this file's directory
 *   - javac -classpath 'gson-VERSION.jar' KMROPatientTriples.java
 *   - java -classpath '.:gson-VERSION.jar' KMROPatientTriples
 * 
 *
 * To process JSON, this uses gson:
 * - https://sites.google.com/site/gson/gson-user-guide
 *
 */

public class SPARQLQueryUtil {

    //--------------------------------------------------------------------
    // JENA endpoint:  http://172.31.5.104/ds/sparql?query=
    //      i.e.   http://172.31.5.104/ds/sparql?query=SELECT * {?s ?p ?o}
    //--------------------------------------------------------------------

    private String sparqlEP;
    public String sparqlType = null;
    Boolean trace = false;

//    private static String JENA_EP = "http://192.168.1.111:3030/data/sparql?query=";
//    public SPARQLQueryUtil() {
//        this.sparqlEP = SPARQLQueryUtil.JENA_EP; // defaulting EP
//    }
    
    public SPARQLQueryUtil(String sparqlEP) {
        this.sparqlEP = sparqlEP + "?query=";
    }

    //------------------------------------
    //PREFIXES
    //------------------------------------
    private static String PREFIXES =
         "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
        +" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
        +" PREFIX skos: <http://www.w3.org/2004/02/skos/core#>"
        +" PREFIX cgkos: <http://datasets.caregraf.org/ontology#>"
        +" PREFIX owl: <http://www.w3.org/2002/07/owl#>"
        +" PREFIX splo: <http://datasets.caregraf.org/splo/>"
        +" PREFIX ndfrt: <http://datasets.caregraf.org/ndfrt/>"
        +" PREFIX snomed: <http://datasets.caregraf.org/snomed/>"
        +" PREFIX vacohorto: <http://datasets.caregraf.org/vacohorto/>"
        +" PREFIX ndfrto: <http://datasets.caregraf.org/ndfrto/>"
        +" PREFIX rxnorm: <http://datasets.caregraf.org/rxnorm/>"
        +" PREFIX icd9cm: <http://datasets.caregraf.org/icd9cm/> ";

    //------------------------------------
    // QUERY: 
    //------------------------------------
    private static String QUERY_PROBLEMS = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?drugClass"
        +" ?exampleDrug"
        +" ?initial_proportion_of_dx_receiving_rx"
        +" ?rr_initial"
        +" ?rr_p1q"
        +" ?rr_p2q"
        +" ?rr_p3q"
        +" ?dxrx_dx_p1q"
        +" ?dxrx_dx_p2q"
        +" ?dxrx_dx_p3q  "
        +" WHERE {"
        +" ?vacohort vacohorto:diag icd9cm:%s ;"  //code replacement
        +" vacohorto:class_drug ?vaDrugClass ;"
        +" vacohorto:eg_drug ?exampleDrug ;"
        +" vacohorto:dxrx_dx_initial_proportion_of_dx_receiving_rx ?initial_proportion_of_dx_receiving_rx ;"
        +" vacohorto:dxrx_dx_p1q ?dxrx_dx_p1q ;"
        +" vacohorto:dxrx_dx_p2q ?dxrx_dx_p2q ;"
        +" vacohorto:dxrx_dx_p3q ?dxrx_dx_p3q ;"
        +" vacohorto:dxrx_dx_p2q ?rr_initial ;"
        +" vacohorto:dxrx_dx_p2q ?rr_p1q ;"
        +" vacohorto:dxrx_dx_p2q ?rr_p2q ;"
        +" vacohorto:dxrx_dx_p2q ?rr_p3q ."
        +" ?vaDrugClass skos:prefLabel ?drugClass"
        +" }";

    private static String QUERY_MED_INDICATIONS = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?indication"
        +" ?indicationLabel"
        +" WHERE {"
        +" rxnorm:%s splo:HAS_INDICATION ?indication ."     //code replacement
        +" ?indication skos:prefLabel ?indicationLabel ."
        +" }";
    private static String QUERY_MED_MONITORING = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?monitoring"
        +" ?monitoringLabel"
        +" WHERE {"
        +" rxnorm:%s splo:HAS_LAB_TEST ?monitoring ."      //code replacement
        +" ?monitoring skos:prefLabel ?monitoringLabel ."
        +" }";
    private static String QUERY_MED_BOXWARNING = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?boxWarning"
        +" ?boxWarningLabel"
        +" WHERE {"
        +" rxnorm:%s splo:HAS_BOXED_WARNING ?boxWarning ."  //code replacement
        +" ?boxWarning skos:prefLabel ?boxWarningLabel ."
        +" }";

    private static String QUERY_MED_SIDEEFFECTS = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?sideEffect"
        +" ?sideEffectLabel"
        +" WHERE {"
        +" rxnorm:%s splo:HAS_ADVERSE_EFFECT ?sideEffect ."  //code replacement
        +" ?sideEffect skos:prefLabel ?sideEffectLabel ."
        +" }";
    
    private static String QUERY_ASSOCIATEDTRIPLES = SPARQLQueryUtil.PREFIXES +
         "SELECT DISTINCT"
        +" ?associationType"
        +" ?associationTypeLabel"
        +" ?additionalAssociation"
        +" ?additionalAssociationLabel"
        +" WHERE {"
        +" rxnorm:%s ?associationType ?additionalAssociation ."   //code replacement
        +" ?additionalAssociation skos:prefLabel ?additionalAssociationLabel ."
        +" BIND ( REPLACE(STR(?associationType), \"http://datasets.caregraf.org/splo/\" , \"\") as ?associationTypeLabel)"
        +" FILTER("
        +" STRSTARTS(STR(?associationType), \"http://datasets.caregraf.org/splo/\") &&"
        +" ?associationType != splo:HAS_INDICATION &&  "
        +" ?associationType != splo:HAS_LAB_TEST &&  "
        +" ?associationType != splo:HAS_BOXED_WARNING &&  "
        +" ?associationType != splo:HAS_ADVERSE_EFFECT"
        +" )"
        +" }"
        +" ORDER BY ?associationTypeLabel";

    private static String QUERY_NEWDIAGNOSES = SPARQLQueryUtil.PREFIXES +
         "SELECT"
        +" ?label"
        +" ?icd9"
        +" ?icd9Code"
        +" ?description"
        +" WHERE {"
        +" ?icd9 skos:inScheme <http://datasets.caregraf.org/icd9cm/scheme> ;"
        +" skos:prefLabel ?label ."
        +" BIND ( REPLACE(STR(?icd9), \"http://datasets.caregraf.org/icd9cm/\" , \"\") as ?icd9Code)"
        +" FILTER(CONTAINS(UCASE(?label), UCASE('%s')))" //search pattern replacement
        +" }"
        +" ORDER BY ?label"
        +" LIMIT 100";

    private static String QUERY_NEWMEDICATIONS = SPARQLQueryUtil.PREFIXES +
         "SELECT"
        +" ?label"
        +" ?rxnorm"
        +" ?rxnormCode"
        +" ?description"
        +" WHERE {"
        +" ?rxnorm skos:inScheme <http://datasets.caregraf.org/rxnorm/scheme> ;"
        +" skos:prefLabel ?label ."
        +" EXISTS { ?rxnorm splo:HAS_ADVERSE_EFFECT ?sideEffect} ."
        +" BIND ( REPLACE(STR(?rxnorm), \"http://datasets.caregraf.org/rxnorm/\" , \"\") as ?rxnormCode)"
        +" FILTER(CONTAINS(UCASE(?label), UCASE('%s')))"    //search pattern replacement
        +" }"
        +" ORDER BY ?label"
        +" LIMIT 100";

    //------------------------------------
    //QUERY: FROM VUID to RxNORM
    //------------------------------------
    private static String QUERY_VUIDTORXNORM = //SPARQLQueryUtil.PREFIXES +
        "SELECT ?RxNORMUri"
        +" (REPLACE(STR(?RxNORMUri),\"http://datasets.caregraf.org/rxnorm/\",\"\") as ?RxNORMCode)"
        +" WHERE {"
        +" GRAPH <http://datasets.caregraf.org/rxnorm> {"
        +" ?RxNORMUri <http://www.w3.org/2004/02/skos/core#exactMatch> <http://datasets.caregraf.org/vandf/%s> ."
        +" } }"
        ;

    //------------------------------------
    //
    //------------------------------------
    /**
     *
     * @param vuid - the vuid code to be converted to RXNORM code.
     * @param outputFormat - [null, "xml", "json"]; null will result in xml as default.
     * @return
     * @throws Exception
     */
    public String getRXNORM (String vuid, String outputFormat) throws Exception
    {
        String query = String.format(this.QUERY_VUIDTORXNORM, vuid);
        return runQuery(query, outputFormat);
    }

    //------------------------------------
    // PROBLEM - DRUGS
    //------------------------------------
    /**
     * Getting the Drug list for a given Problems code.
     *
     * @param code
     * @param outputFormat - [null, "xml", "json"]; null will result in xml as default.
     * @return
     * @throws Exception
     */
    public String getDrugsAsString (String problemCode, String outputFormat) throws Exception
    {
        String query = String.format(this.QUERY_PROBLEMS, problemCode);
        return runQuery(query, outputFormat);
    }
    /**
     * Returning a JsonObject of the Drugs associated with a Problem Code.
     * @param problemCode
     * @return
     * @throws Exception
     */
    public JsonObject getDrugsAsJson (String problemCode) throws Exception
    {
        String query = String.format(this.QUERY_PROBLEMS, problemCode);
        return runQuery(query);
    }
    /**
     * Returning a List<Map<String, String>> of drugs and their
     * attirbutes name/value pair.
     *
     * @param problemCode
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getDrugsAsMap (String problemCode) throws Exception
    {
        String query = String.format(this.QUERY_PROBLEMS, problemCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> drugs = processListReply(jsonResp);

        return drugs;
    }

    //------------------------------------
    // NEW DIAGNOSES
    //------------------------------------
    /**
     * Getting the list of new Diagnoses based on a partial search pattern.
     *
     * @param code
     * @param outputFormat - [null, "xml", "json"]; null will result in xml as default.
     * @return
     * @throws Exception
     */
    public String getNewDiagnosesAsString (String searchPattern, String outputFormat) throws Exception
    {
        String query = String.format(this.QUERY_NEWDIAGNOSES, searchPattern);
        return runQuery(query, outputFormat);
    }
    /**
     * Returning a JsonObject of list of new Diagnoses based on a partial search pattern.
     *
     * @param problemCode
     * @return
     * @throws Exception
     */
    public JsonObject getNewDiagnosesAsJson (String searchPattern) throws Exception
    {
        String query = String.format(this.QUERY_NEWDIAGNOSES, searchPattern);
        return runQuery(query);
    }
    /**
     * Returning a List<Map<String, String>> of new Diagnoses and their
     * attributes name/value pair.
     *
     * @param problemCode
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getNewDiagnosesAsMap (String searchPattern) throws Exception
    {
        String query = String.format(this.QUERY_NEWDIAGNOSES, searchPattern);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> drugs = processListReply(jsonResp);

        return drugs;
    }
    //------------------------------------
    // NEW MEDICATIONS
    //------------------------------------
    /**
     * Getting the list of new Medications based on a partial search pattern.
     *
     * @param searchPattern
     * @param outputFormat - [null, "xml", "json"]; null will result in xml as default.
     * @return
     * @throws Exception
     */
    public String getNewMedicationsAsString (String searchPattern, String outputFormat) throws Exception
    {
        String query = String.format(this.QUERY_NEWMEDICATIONS, searchPattern);
        return runQuery(query, outputFormat);
    }
    /**
     * Returning a JsonObject of list of new Medications based on a partial search pattern.
     *
     * @param searchPattern
     * @return
     * @throws Exception
     */
    public JsonObject getNewMedicationsAsJson (String searchPattern) throws Exception
    {
        String query = String.format(this.QUERY_NEWMEDICATIONS, searchPattern);
        return runQuery(query);
    }
    /**
     * Returning a List<Map<String, String>> of new Medications and their
     * attributes name/value pair.
     *
     * @param searchPattern
     * @return
     * @throws Exception
     */
    public List<Map<String, String>> getNewMedicationsAsMap (String searchPattern) throws Exception
    {
        String query = String.format(this.QUERY_NEWMEDICATIONS, searchPattern);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> drugs = processListReply(jsonResp);

        return drugs;
    }

    //------------------------------------
    // MEDICATION
    //------------------------------------

//    public String getMedsAsString (String medCode, String outputFormat) throws Exception
//    {
//    }
//    /**
//     * Returning a JsonObject of the Drugs associated with a Problem Code.
//     * @param problemCode
//     * @return
//     * @throws Exception
//     */
//    public JsonObject getMedsAsJson (String medCode) throws Exception
//    {
//    }

    public List<Map<String, String>> getMedIndicationsAsMap (String medCode) throws Exception
    {
        String query = String.format(this.QUERY_MED_INDICATIONS, medCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> medItems = processListReply(jsonResp);

        return medItems;
    }
    public List<Map<String, String>> getMedMonitoringAsMap (String medCode) throws Exception
    {
        String query = String.format(this.QUERY_MED_MONITORING, medCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> medItems = processListReply(jsonResp);

        return medItems;
    }
    public List<Map<String, String>> getMedBoxWarningAsMap (String medCode) throws Exception
    {
        String query = String.format(this.QUERY_MED_BOXWARNING, medCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> medItems = processListReply(jsonResp);

        return medItems;
    }
    public List<Map<String, String>> getMedSideEffectAsMap (String medCode) throws Exception
    {
        String query = String.format(this.QUERY_MED_SIDEEFFECTS, medCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> medItems = processListReply(jsonResp);

        return medItems;
    }
    public List<Map<String, String>> getMedAssociationsAsMap (String medCode) throws Exception
    {
        String query = String.format(this.QUERY_ASSOCIATEDTRIPLES, medCode);

        JsonObject jsonResp = runQuery(query);
        List<Map<String, String>> medItems = processListReply(jsonResp);

        return medItems;
    }
//
//    public List<Map<String, String>> getMeds (String code) throws Exception
//    {
//        JsonObject MedsJson = null;
//
//        JsonObject indicationJson =  runQuery(String.format(this.QUERY_MED_INDICATIONS, code));
//
//
//        JsonObject monitorJson       =  runQuery(String.format(this.QUERY_MED_MONITORING, code));
//        JsonObject bosWarningJson   =  runQuery(String.format(this.QUERY_MED_BOXWARNING, code));
//        JsonObject sideEffectsJson  =  runQuery(String.format(this.QUERY_MED_SIDEEFFECTS, code));
//        JsonObject associatedTriplesJson =  runQuery(String.format(this.QUERY_ASSOCIATEDTRIPLES, code));
//
//
//        //---------------------------------------------------------------
//        // ASSOCIATED TRIPLES - will contain all associated triples (list)
//        // AND its detail.
//        //
//        // ------------ Receiving JENA facts ------------
//        //"bindings": [
//        //  {
//        //    "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/not_HAS_INDICATION" } ,
//        //    "associationTypeLabel": { "type": "literal" , "value": "not_HAS_INDICATION A" } ,
//        //    "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/24321005" } ,
//        //    "additionalAssociationLabel": { "type": "literal" , "value": "Fungal meningitis" }
//        //  },
//        //  {
//        //    "associationType": { "type": "uri" , "value": "http://datasets.caregraf.org/splo/not_HAS_INDICATION" } ,
//        //    "associationTypeLabel": { "type": "literal" , "value": "not_HAS_INDICATION A" } ,
//        //    "additionalAssociation": { "type": "uri" , "value": "http://datasets.caregraf.org/snomed/24321005" } ,
//        //    "additionalAssociationLabel": { "type": "literal" , "value": "Pnuemonia" }
//        //  }
//        // ]
//        //
//        // Will have to parse and pull unique associationTypeLabel
//        // into one Fact arry.  Then push the additionalAssociationLabel into
//        // the sub-array (items) under that Fact array.
//        //
//        // ------------ Resulting Facts array for GUI ------------
//        // {
//        //    "title": "not_HAS_INDICATION A",
//        //    "items": [
//        //        "Fungal meningitis",
//        //        "Pnuemonia"]
//        // },
//        // {
//        //    "title": "not_HAS_INDICATION B",
//        //    "items": [
//        //        "Fungal meningitis B",
//        //        "Pnuemonia B",
//        //        "Lorem Ipsum"]
//        // },
//        //---------------------------------------------------------------
//
//
//        // .....TBD TBD TBD TBD .....
//
//        return medications;
//    }










    //------------------------------------
    // QUERY EXECUTION
    //------------------------------------
    /**
     * Executes a SPARQL query against the configured JENA server and
     * return the resulting output as a String.
     * 
     * @param query
     * @param outputFormat - [null, "xml", "json"]; null will result in xml as default.
     * @return String
     * @throws Exception
     */
    private String runQuery (String query, String outputFormat) throws Exception
    {
        String ret = null;

        // EXEC the query
        BufferedReader response = null;
        response = request(query, outputFormat);

        if ((outputFormat != null) && (outputFormat.equalsIgnoreCase("json"))) {

            // PARSE into a JsonObject
            JsonParser parser = new JsonParser();
            JsonObject reply = parser.parse(response).getAsJsonObject();
            ret = reply.toString();
        }
        else {
            String inputLine = null;
            while ((inputLine = response.readLine()) != null) {
                ret = ret + inputLine;
            }

        }

        return ret;
    }

    /**
     * Executes a SPARQL query against the configured JENA server and
     * return the resulting output as a JsonObject
     *
     * @param query
     * @return JsonObject
     * @throws Exception
     */
    private JsonObject runQuery (String query) throws Exception
    {
        // EXEC the query
        BufferedReader response = null;
        response = request(query, "json");

        // PARSE into a JsonObject
        JsonParser parser = new JsonParser();
        JsonObject reply = parser.parse(response).getAsJsonObject();

        return reply;
    }


    /**
     * Returns a BufferedReader object containing the resultset of a SPARQL query.
     *
     * @param query
     * @param format - [null, "xml", "json"]; null will result in xml as default.
     * @return
     * @throws Exception
     */
    private BufferedReader request(String query, String format) throws Exception {

        String enc = URLEncoder.encode(query, "UTF-8");
        //System.out.println("JENA query= "+ query);
        //System.out.println("JENA query@UTF-8= "+ enc);

        String sparqlrs = this.sparqlEP + URLEncoder.encode(query, "UTF-8");

        if (format != null) {
            sparqlrs = sparqlrs +  "&output=" + format;
        } else {
            // the default output format is XML.
        }

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
     * Utility - turns JsonObject list into List of Maps. All values become Strings.
     *
     * TBD: could type the values
     */
    protected List<Map<String, String>> processListReply(JsonObject listReply)
    {
        JsonObject results = listReply.getAsJsonObject("results");
        JsonArray bindings = results.getAsJsonArray("bindings");

        Iterator<JsonElement> bindingsIterator = bindings.iterator();
        List<Map<String, String>> typedBindingList = new ArrayList<Map<String, String>>();
        while(bindingsIterator.hasNext())
        {
            JsonObject binding = bindingsIterator.next().getAsJsonObject();
            Set<Map.Entry<String,JsonElement>> bindingEntries = binding.entrySet();
            Map<String, String> typedBinding = new HashMap<String, String>();
            for (Map.Entry<String,JsonElement> bindingEntry : bindingEntries) {
                String key = bindingEntry.getKey();
                JsonElement value = bindingEntry.getValue();
                String stringValue = value.getAsJsonObject().getAsJsonPrimitive("value").getAsString();
                typedBinding.put(key, stringValue);
            }
            typedBindingList.add(typedBinding);
        }
        return typedBindingList;
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
