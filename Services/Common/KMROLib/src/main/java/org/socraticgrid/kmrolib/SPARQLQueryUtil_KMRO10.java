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

public class SPARQLQueryUtil_KMRO10 {

    //--------------------------------------------------------------------
    // JENA endpoint:  http://172.31.5.104/ds/sparql?query=
    //      i.e.   http://172.31.5.104/ds/sparql?query=SELECT * {?s ?p ?o}
    //--------------------------------------------------------------------

    private static  String sparqlEP;

    /* 2: KMRO Danno equivalents and Best 10 KMRO */
    public static String KMRO_EP = "http://184.191.173.237/kmro/sparql?query=";
    public static String KMRO10_EP = "http://184.191.173.237/kmro10/sparql?query=";

    public String sparqlType = null;

    Boolean trace = false;

    public SPARQLQueryUtil_KMRO10() {
    }

    public SPARQLQueryUtil_KMRO10(String dataSourceURL, String dataSourceType) {
        this.sparqlEP = dataSourceURL;
        this.sparqlType = dataSourceType;
    }
    
    public SPARQLQueryUtil_KMRO10(String sparqlEP) {
        this.sparqlEP = sparqlEP;
    }




    /*
     * "queryTail" ie/ details of ORDER BY/LIMIT/OFFSET are added by invokers
     */
    private static String QUERY_SFACTSOFTYPE = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT ?s WHERE {?s a kmro:%s%s";
    private static String QUERY_SFACTSOFTYPEBYPATIENT = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT ?s WHERE {?s kmro:hasPatient <%s> ; a kmro:%s%s";
    private static String QUERY_DFACTSOFTYPE = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> DESCRIBE ?s WHERE {?s a kmro:%s%s";
    private static String QUERY_DFACTSOFTYPEBYPATIENT = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> DESCRIBE ?s WHERE {?s kmro:hasPatient <%s> ; a kmro:%s%s";

    //------------------------------------------------------------
    // FMQL queries
    //------------------------------------------------------------
    private static String QUERY_DESCRIBEFACT = "DESCRIBE <%s>";



    /*
     * Precise queries for Tia:
     * - distinct concepts per patient for a given domain (VITAL etc)
     * - distinct dates per patient for a given domain
     * - values of domain concept for a patient
     */
    private static String QUERY_SCONCEPTS_BYPATIENTANDTYPE = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT DISTINCT ?terminologySystemLabel ?terminologySystemCode ?conceptCode ?conceptLabel WHERE {?s kmro:hasPatient <%s> ; a kmro:%s ; kmro:hasTypeReference ?tr . ?tr kmro:terminologySystemLabel ?terminologySystemLabel ; kmro:terminologySystemCode ?terminologySystemCode ; kmro:conceptCode ?conceptCode ; kmro:conceptLabel ?conceptLabel} ORDER BY ?terminologySystemLabel ?conceptLabel";
    private static String QUERY_SDATES_BYPATIENTANDTYPE = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT DISTINCT ?dateTimeReported WHERE {?s kmro:hasPatient <%s> ; a kmro:%s ; kmro:dateTimeReported ?dateTimeReported} ORDER BY ?dateTimeReported";
    // Need patient, domain, conceptCode and the predicate for value == hasResultValue, hasVitalSignResult
    private static String QUERY_SVALUES_BYPATIENTTYPECONCEPT = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT ?dateTimeReported ?value ?unit WHERE {?s kmro:hasPatient <%s> ; a kmro:%s ; kmro:dateTimeReported ?dateTimeReported ; kmro:%s ?sv ; kmro:hasTypeReference ?tr . ?tr kmro:conceptCode ?conceptCode . FILTER (?conceptCode='%s') . ?sv kmro:val ?value ; kmro:unit ?unit} ORDER BY ?dateTimeReported";

    /**
     * Need Values etc at that level: freeTextSig (string), orderStatus ( concept), numberRefillsAllowed (quan),
     *
     * NOTE: far too many levels of indirection just to get the Medication. The KMRO format is not SPARQL friendly.
     */
    private static String QUERY_MEDORDER_BYPATIENT = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT ?s ?dateTimeOfOrder ?medicationCode ?medicationLabel ?medicationTerminologyCode ?medicationTerminologyLabel ?dose ?numberOfRefillsAllowed ?sig ?statusCode ?statusTerminologyCode WHERE {?s kmro:hasPatient <%s> ; a kmro:MedicationOrder ; kmro:dateTimeOfOrder ?dateTimeOfOrder ; kmro:hasMedication ?medication . ?medication kmro:medicationProduct ?medicationProduct . ?medicationProduct  kmro:hasDrugItem ?drugItem . ?drugItem kmro:conceptCode ?medicationCode ; kmro:conceptLabel ?medicationLabel ; kmro:terminologySystemCode ?medicationTerminologyCode ; kmro:terminologySystemLabel ?medicationTerminologyLabel . OPTIONAL { ?s kmro:freeTextSig ?sig} . OPTIONAL { ?medication kmro:hasDose ?hasDoseQ . ?hasDoseQ kmro:val ?dose} . OPTIONAL { ?s kmro:numberRefillsAllowed ?numberRefillsAllowedQ . ?numberRefillsAllowedQ kmro:val ?numberOfRefillsAllowed } . OPTIONAL { ?s kmro:hasOrderStatus ?hasOrderStatus . ?hasOrderStatus kmro:conceptCode ?statusCode ; kmro:terminologySystemCode ?statusTerminologyCode }  } ORDER BY ?dateTimeOfOrder";

    /* Add legal name with performedBy */
    private static String QUERY_DISPENSATIONS_OF_MEDORDER = "PREFIX kmro: <urn:gov:hhs:fha:nhinc:adapter:fact#> SELECT ?dateTimeOrderDispensed ?dateTimeOfExpiration ?dateTimeOrderFilled ?medicationCode ?medicationLabel ?medicationTerminologyCode ?medicationTerminologyLabel ?hasFillQuantity WHERE {?s kmro:hasOriginalOrder <%s> ; a kmro:Dispensation ; kmro:dateTimeOrderDispensed ?dateTimeOrderDispensed ; kmro:dateTimeOfExpiration ?dateTimeOfExpiration ; kmro:dateTimeOrderFilled ?dateTimeOrderFilled ; kmro:medicationProduct ?medicationProduct . ?medicationProduct  kmro:hasDrugItem ?drugItem . ?drugItem kmro:conceptCode ?medicationCode ; kmro:conceptLabel ?medicationLabel ; kmro:terminologySystemCode ?medicationTerminologyCode ; kmro:terminologySystemLabel ?medicationTerminologyLabel . OPTIONAL { ?s kmro:hasFillQuantity ?hasFillQuantityQ . ?hasFillQuantityQ kmro:val ?hasFillQuantity} } ORDER BY ?dateTimeOrderDispensed";


    /**
     * By domain, return distinct concepts used for patient
     *
     * Ex/ what lab result types were taken
     */
    public List<Map<String, String>> getConceptsOfPatientByDomain(String patientId, String domain) throws Exception
    {
        String query = String.format(QUERY_SCONCEPTS_BYPATIENTANDTYPE, patientId, domain);

        JsonObject listReply = this.requestJSON(query);

        return processListReply(listReply);
    }

    /**
     * By domain, return distinct dates where a patient has data
     *
     * Date xmlDateIn = sdf.parse(value)
     */
    public List<String> getDatesOfPatientByDomain(String patientId, String domain) throws Exception
    {
        String query = String.format(QUERY_SDATES_BYPATIENTANDTYPE, patientId, domain);

        JsonObject listReply = this.requestJSON(query);

        JsonObject results = listReply.getAsJsonObject("results");
        JsonArray bindings = results.getAsJsonArray("bindings");
        Iterator<JsonElement> bindingsIterator = bindings.iterator();
        List<String> singleValues = new ArrayList<String>();
        while(bindingsIterator.hasNext())
        {
            JsonObject binding = bindingsIterator.next().getAsJsonObject();
            Set<Map.Entry<String,JsonElement>> bindingEntries = binding.entrySet();
            for (Map.Entry<String,JsonElement> bindingEntry : bindingEntries) {
                String key = bindingEntry.getKey();
                JsonElement value = bindingEntry.getValue();
                String stringValue = value.getAsJsonObject().getAsJsonPrimitive("value").getAsString();
                singleValues.add(stringValue);
            }
        }
        return singleValues;
    }

    /**
     * By domain and conceptCode, return values a patient has, ordered by date
     */
    public List<Map<String, String>> getValuesOfPatientByDomainAndConcept(String patientId, String domain, String conceptCode) throws Exception
    {
        String valuePredicate = "hasVitalSignResult";
        if (domain != "VitalSign")
            valuePredicate = "hasResultValue";

        String query = String.format(QUERY_SVALUES_BYPATIENTTYPECONCEPT, patientId, domain, valuePredicate, conceptCode);

        JsonObject listReply = this.requestJSON(query);

        return processListReply(listReply);
    }

    /*
     * Utility - turns JsonObject list into List of Maps. All values become Strings.
     *
     * TBD: could type the values
     */
    private List<Map<String, String>> processListReply(JsonObject listReply)
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

    /*
     * QUERY_MEDORDER_BYPATIENT
     *
     * Note: flattens out the oh so deep arrangement of KMRO for Medication Orders
     */
    public List<Map<String, String>> getMedicationOrdersByPatient(String patientId) throws Exception
    {
        String query = String.format(QUERY_MEDORDER_BYPATIENT, patientId);

        JsonObject listReply = this.requestJSON(query);

        return processListReply(listReply);
    }

    /*
     * From Order to Dispensations
     */
    public List<Map<String, String>> getDispensationsOfMedicationOrder(String orderId) throws Exception
    {
        String query = String.format(QUERY_DISPENSATIONS_OF_MEDORDER, orderId);

        JsonObject listReply = this.requestJSON(query);

        return processListReply(listReply);
    }

    /*
     * DEFAULT LIMIT FOR Queries if none specified
     */
    private static Integer DEFAULTLIMIT = 100;

    /**
     * This method will return a list of entryIds for all matching domain
     * records for the given patientId and domain type.
     *
     * entryId = unique across all records within this domain, for this patientId.
     *
     * - patientId == patient resource URI in triple store
     *   ex/ 'http://.../2-224'
     * - domain == KMRO type label ex/ VitalSign
     *
     * Note: patientId == null => all facts of a type
     *
     */
    public String getReferenceFactList(String patientId, String domain, Integer limit, Integer offset) throws Exception
    {
        String query;

        String queryTail = getQueryTail(limit, offset);

        if (patientId != null)
            query = String.format(QUERY_SFACTSOFTYPEBYPATIENT, patientId, domain, queryTail);
        else
            query = String.format(QUERY_SFACTSOFTYPE, domain, queryTail);

        JsonObject reply = this.requestJSON(query);
        String processedReply = this.xmlListOfType(reply, domain);

        return processedReply;
    }

    /**
     * This method will return a list of entries and the surface-level
     * (shallow) content for all matching domain records, for the
     * given patientId and domain type.
     *
     * surface-level content =
     *             literal values
     *           + anonymous entities (ie Concept Pointer - value/unit pair)
     *           + reference to complex objects.
     */
    public String getShallowFactList(String patientId, String domain, Integer limit, Integer offset) throws Exception
    {
        return getFactList(patientId, domain, limit, offset, true);
    }

    /**
     * This method will return a list of entries and all-level (deep) content
     * for all matching domain records, for the given patientId and domain type.
     *
     * all-level content = fully expanded on all nodes and branches.
     */
    public String getDeepFactList(String patientId, String domain, Integer limit, Integer offset) throws Exception
    {
        return getFactList(patientId, domain, limit, offset, false);
    }

    private String getFactList(String patientId, String domain, Integer limit, Integer offset, boolean shallow) throws Exception
    {
        String query;
        String queryTail = getQueryTail(limit, offset);

        if (patientId != null)
            query = String.format(QUERY_DFACTSOFTYPEBYPATIENT, patientId, domain, queryTail);
        else
            query = String.format(QUERY_DFACTSOFTYPE, domain, queryTail);

        JsonObject reply = this.requestJSON(query);
        String processedReply = this.xmlDescriptions(reply, "", domain, true, "CONTAINS", new HashMap<String, String>(), shallow);

        return processedReply;
    }

    String getQueryTail(Integer limit, Integer offset)
    {
        if ((limit == null) || (offset == null))
        {
            limit = DEFAULTLIMIT;
            offset = 0;
        }
        String queryTail = String.format("} ORDER BY ?s LIMIT %s OFFSET %s", limit, offset);
        return queryTail;
    }

    /**
     * This method will return a list of entries and surface-level (shallow) content
     * for the matching domain record, for the given patientId and domain type and entryId.
     *
     * entryId = unique across all records within this domain, for this patientId.
     * surface level content =
     *             literal values
     *           + anonymous entities (ie Concept Pointer - valu/unit pair)
     *           + reference to complex objects.
     */
    public String getShallowFactItem(String patientId, String domain, String entryId
    ) throws Exception
    {
        // Note: in this triple store, entryId is globally unique. There
        // is no need for patientId or domain.
        String query = String.format(QUERY_DESCRIBEFACT, entryId);
        JsonObject reply = this.requestJSON(query);
        String processedReply = this.xmlResourceDescr(entryId, reply, "", "IMPL", entryId, new HashMap<String, String>(), true);

        return processedReply;
    }
    
    /**
     * This method will return a list of entries and all-level (deep) content
     * for the matching domain record, for the given patientId and domain type and entryId.
     *
     * entryId = unique across all records within this domain, for this patientId.
     * all-level content =
     *             literal values
     *           + anonymous entities (ie Concept Pointer - valu/unit pair)
     *           + reference to complex objects.
     */
    public String getDeepFactItem(String patientId, String domain, String entryId
    ) throws Exception
    {
        // Note: in this triple store, entryId is globally unique. There
        // is no need for patientId or domain.
        String query = String.format(QUERY_DESCRIBEFACT, entryId);

        JsonObject reply = this.requestJSON(query);
        String processedReply = this.xmlResourceDescr(entryId, reply, "", "IMPL", entryId, new HashMap<String, String>(), false);

        return processedReply;
    }

    /*
     * Format == json or
     *
     * Returns a buffered reader
     */

    public BufferedReader request(String query, String format) throws Exception {

        String sparqlrs = this.sparqlEP
                + URLEncoder.encode(query, "UTF-8");
                //+ query;

        if (format != null) {
            sparqlrs = sparqlrs +  "&output=" + format;
        }

        System.out.println("SPARQL= "+ sparqlrs);
        
        URL sparqlr = new URL(sparqlrs);
        // 1. Make the query
        URLConnection sparqlc = sparqlr.openConnection();
        // 2. Read the Response
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                sparqlc.getInputStream()));
        return in;
    }

    /*
     * Local, JSON only call
     */
    private JsonObject requestJSON(String query) throws Exception
    {
        BufferedReader in = null;

        if (SPARQLQueryUtil_KMRO10.sparqlEP == KMRO_EP)
            in = request(query, "json");
        else
            in = request(query, null); //FMQL does not need "output" param for json format.
        
        // 3. Parse as JSON
        JsonParser parser=new JsonParser();
        // 4. Walk the JSON
        JsonObject reply=parser.parse(in).getAsJsonObject();
        return reply;
    }

    /*
     * FORM 1 RESPONSE: SPARQL DESCRIBE
     *
     * It returns one or more resource definitions as XML. Blank nodes are
     * treated as full resources. Optionally it can trace as it goes.
     *
     * {"resourceId": {"pred1": [{"value": "val1", "type": "pred1Type"}, ...],
     */
    private String xmlDescriptions(JsonObject resourceDescrs, String indent, String type, boolean isList, String tagAs, HashMap<String, String> urisDescribed, boolean shallowOnly) throws Exception {

        String xmlReply = "";

        if (isList)
        {
            xmlReply = "<FactListImpl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\">\n\t";
            indent += "\t";
            tagAs = "CONTAINS";
            xmlReply += this.xmlDyInfo(indent, type, UUID.randomUUID().toString(), false);
        }

        Set<Map.Entry<String,JsonElement>> rdsMap = resourceDescrs.entrySet();
        for (Map.Entry<String,JsonElement> rme : rdsMap) {

            String resourceURI = rme.getKey();

            // Embed blank nodes. Don't process at the top level.
            if (!resourceURI.startsWith("http:"))
                continue;

            String sxmlReply = xmlResourceDescr(resourceURI, resourceDescrs, indent, tagAs, resourceURI, urisDescribed, shallowOnly);

            xmlReply += sxmlReply;
        }

        if (isList)
            xmlReply += "\n</FactListImpl>\n";

        return xmlReply;
    }

    /*
     * A resource description is a dictionary of:
     *    predicate: {"value": VAL, "type": TYPEOFVAL}
     * where VAL can be a date, an integer, a URI or a "blank node id".
     *
     * Note: one description == one uri in outer dictionary. List has many
     */
    private String xmlResourceDescr(String resourceURI, JsonObject resourceDescrs, String indent, String tagAs, String globalId, HashMap<String, String> urisDescribed, boolean shallowOnly) throws Exception {
        String xmlReply = "";
        String oindent = indent;
        JsonObject resourceDescr = resourceDescrs.getAsJsonObject(resourceURI);
        if (resourceDescr == null)
            return "";
        Set<Map.Entry<String,JsonElement>> predValueMap = resourceDescr.entrySet();

        // rdf:type get special treatment - a processor version of this
        // dumper would key off type
        String pred = "rdf:type";
        // Assuming resource have only one type
        String value = resourceDescr.getAsJsonArray("http://www.w3.org/1999/02/22-rdf-syntax-ns#type").get(0).getAsJsonObject().getAsJsonPrimitive("value").getAsString();

        String typeValue = value.split("#")[1];

        if (tagAs == "IMPL")
            // no indent if standalone and this needs full xmlns
            if (indent=="")
                xmlReply += "\n" + indent + "<" + typeValue + "Impl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\">";
            else
                xmlReply += "\n" + indent + "<" + typeValue + "Impl>";
        else if (tagAs == "CONTAINS")
            xmlReply += "\n" + indent + "<contains xsi:type=\"" + typeValue + "\" xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";

        indent += "\t";

        xmlReply += this.xmlDyInfo(indent, typeValue, globalId, false);
        urisDescribed.put(globalId, typeValue);

        for (Map.Entry<String,JsonElement> pvme : predValueMap) {

            // Handled rdf:type above.
            if (pvme.getKey().startsWith("http://www.w3.org"))
                continue;

            // take off: urn:gov:hhs:fha:nhinc:adapter
            pred = pvme.getKey().split("#")[1];

            // Assuming can have > 1 value
            // TBD: must add configuration file to dictate whether multiple values allowed or not.
            JsonArray values = pvme.getValue().getAsJsonArray();

            // All values in array have same type so just get from first
            // value
            JsonObject firstValue = values.get(0).getAsJsonObject();
            String valueType = firstValue.getAsJsonPrimitive("type").getAsString();
            String dataType = "";
            if (firstValue.has("datatype"))
            {
                dataType = firstValue.getAsJsonPrimitive("datatype").getAsString();
            }

            Iterator<JsonElement> valuesIterator = values.iterator();
            while(valuesIterator.hasNext())
            {
                xmlReply += "\n" + indent + "<" + pred + ">";

                JsonObject sparqlValue = valuesIterator.next().getAsJsonObject();
                value = sparqlValue.getAsJsonPrimitive("value").getAsString();

                // A date - then type it (java.util.date?)
                if (dataType.matches("http://www.w3.org/2001/XMLSchema#dateTime"))
                {
                    // Reverting to XML time but for reference leaving what
                    // KMR wanted ("E, dd MMM yyyy HH:mm:ss.SSS Z");
                    // Day, dayno month no year no hour min sec ...SSS? Z
                    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    // Date xmlDateIn = sdf.parse(value);
                    // sdf.applyPattern("E, dd MMM yyyy HH:mm:ss.SSS Z");
                    // xmlReply += sdf.format(xmlDateIn).toString();
                    xmlReply += value;
                }
                // If reference to Blank Node/struct then recurse
                else if (valueType.matches("bnode"))
                {
                    // Needs a global id
                    String bnodeGlobalId = UUID.randomUUID().toString();
                    String bnodeId = value;
                    // as not explicitly tagging, don't indent for it
                    xmlReply += xmlResourceDescr(bnodeId, resourceDescrs, indent, "", bnodeGlobalId, urisDescribed, shallowOnly) + "\n" + indent;
                }
                // Distinguish pointers/references
                // 200 (Agent) and 2 (Patient) are exs of referenced things.
                else if (valueType.matches("uri"))
                {
                    if (urisDescribed.get(value) != null)
                        xmlReply += this.xmlDyInfo(indent + "\t", urisDescribed.get(value), value, true);
                    // will fetch: always dy info
                    else if (shallowOnly)
                    {
                        JsonObject listReply = this.requestJSON("SELECT ?t WHERE {<" + value + "> a ?t}");
                        JsonObject results = listReply.getAsJsonObject("results");
                        JsonArray bindings = results.getAsJsonArray("bindings");
                        JsonObject binding = bindings.get(0).getAsJsonObject();
                        String tValue = binding.getAsJsonObject("t").getAsJsonPrimitive("value").getAsString().split("#")[1];
                        xmlReply += this.xmlDyInfo(indent + "\t", tValue, value, true);
                        urisDescribed.put(value, tValue);
                    }
                    else
                    {
                        JsonObject reply = this.requestJSON("DESCRIBE <" + value + ">");
                        // TBD: raise exception if no reply!
                        String processedReply = this.xmlDescriptions(reply, indent, "", false, "", urisDescribed, shallowOnly);
                        xmlReply += processedReply;
                    }
                    xmlReply += "\n" + indent;
                }
                // Inlined values: string or Integer (KMR doesn't distinguish)
                else
                    xmlReply += value;

                xmlReply += "</" + pred + ">";

            }
        }

        if (tagAs == "IMPL")
            xmlReply += "\n" + oindent + "</" + typeValue + "Impl" + ">";
        else if (tagAs == "CONTAINS")
            xmlReply += "\n" + oindent + "</contains>";

        return xmlReply;
    }

    /*
     * Creates XML of x's from a SELECT x of type y
     */
    private String xmlListOfType(JsonObject listReply, String type) throws Exception {

        String xmlReply = "";

        xmlReply = "<FactListImpl xmlns=\"fact.adapter.nhinc.fha.hhs.gov.urn\">\n";

        String indent = "\t";

        xmlReply += this.xmlDyInfo(indent, type, UUID.randomUUID().toString(), false) + "\r";

        JsonObject results = listReply.getAsJsonObject("results");
        JsonArray bindings = results.getAsJsonArray("bindings");
        Iterator<JsonElement> bindingsIterator = bindings.iterator();
        Object[] resourceDescriptions;
        while(bindingsIterator.hasNext())
        {
            JsonObject binding = bindingsIterator.next().getAsJsonObject();
            Set<Map.Entry<String,JsonElement>> bindingEntries = binding.entrySet();
            for (Map.Entry<String,JsonElement> bindingEntry : bindingEntries) {
                xmlReply += "\t<contains xsi:type=\"" + type + "\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
                String key = bindingEntry.getKey();
                JsonElement value = bindingEntry.getValue();
                String stringValue = value.getAsJsonObject().getAsJsonPrimitive("value").getAsString();
                xmlReply += this.xmlDyInfo(indent + "\t", type, stringValue, true);
                xmlReply += "\n\t</contains>\n";
            }
        }

        xmlReply += "\n</FactListImpl>\n";

        return xmlReply;
    }

    private String xmlDyInfo(String indent, String entryType, String entryId, boolean reference)
    {
        String dyXML = "";
        dyXML += "\n" + indent + "<dyEntryType>" + entryType + "</dyEntryType>";
        dyXML += "\n" + indent + "<dyEntryId>" + entryId + "</dyEntryId>";
        dyXML += "\n" + indent + "<dyReference>" + reference + "</dyReference>";
        return dyXML;
    }

    /**
     * Davide examples: bulk gets
     */
    private static void runDavideExamples() throws Exception
    {
        SPARQLQueryUtil_KMRO10 KMROPatientTriples = new SPARQLQueryUtil_KMRO10();

        String patientId;
        String domain;
        String factId;
        String xmlReply;

        // NOTE: runs against both KMRO and KMRO_10

        patientId = "http://patients.kmr.org/danno/2-1";
        // use patient 224 for examples if going to KMRO; use 1 for 10
        if (KMROPatientTriples.sparqlEP == KMRO_EP)
            patientId = "http://patients.kmr.org/danno/2-224";

        domain = "VitalSign";
        System.out.println("\n\n=== Get Reference Fact List: " + domain + " of Patient <" + patientId + ">, first 100, triple-store order ===\n");
        xmlReply = KMROPatientTriples.getReferenceFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        domain = "VitalSign";
        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getShallowFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        domain = "VitalSign";
        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getShallowFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        domain = "VitalSign";
        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + ">, next 10 ===\n");
        xmlReply = KMROPatientTriples.getShallowFactList(patientId, domain, 10, 10);
        System.out.println(xmlReply + "\n==========\n");

        domain = "VitalSign";
        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        domain = "VitalSign";
        if (KMROPatientTriples.sparqlEP == KMRO_EP)
            factId = "http://patients.kmr.org/danno/120_5-13314";
        else
            factId = "http://patients.kmr.org/danno/120_5-1";
        System.out.println("\n\n=== Get Fact (Shallow): <" + factId + "> ===\n");
        xmlReply = KMROPatientTriples.getShallowFactItem(patientId, domain, factId);
        System.out.println(xmlReply + "\n==========\n");

        if (KMROPatientTriples.sparqlEP == KMRO_EP)
            factId = "http://patients.kmr.org/danno/120_5-13314";
        else
            factId = "http://patients.kmr.org/danno/120_5-1";

        System.out.println("\n\n=== Get Fact (Deep): <" + factId + "> ===\n");
        // Note: in Danno triples factId is globally unique so no need to pass in patient or domain
        xmlReply = KMROPatientTriples.getDeepFactItem(null, null, factId);
        System.out.println(xmlReply + "\n==========\n");

        // For Patient References, pass in null patient id
        domain = "Patient";
        System.out.println("\n\n=== Get Patient References, first 10 ===\n");
        xmlReply = KMROPatientTriples.getReferenceFactList(null, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        // For Patient Fact List, DEEP <=> SHALLOW
        domain = "Patient";
        System.out.println("\n\n=== Get Patient Fact List (Get Fact List (Deep)), first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(null, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        // For Patient Fact Item, DEEP <=> SHALLOW
        System.out.println("\n\n=== Get Patient Details (Get Fact (Deep)): <" + patientId + "> ===\n");
        xmlReply = KMROPatientTriples.getDeepFactItem(null, null, patientId);
        System.out.println(xmlReply + "\n==========\n");

        domain = "LabTestResult";
        System.out.println("\n\n=== Get Fact List (Shallow): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getShallowFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        domain = "LabTestResult";
        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        // Panels contain results - deep == get both
        domain = "LabPanel";
        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        // Get Medication Orders
        domain = "MedicationOrder";
        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");

        // Get Dispensations - will refer back to orders
        domain = "Dispensation";
        System.out.println("\n\n=== Get Fact List (Deep): " + domain + " of Patient <" + patientId + ">, first 10 ===\n");
        xmlReply = KMROPatientTriples.getDeepFactList(patientId, domain, 10, 0);
        System.out.println(xmlReply + "\n==========\n");
    }

    /**
     * Tia Examples: fine-grained queries
     */
    private static void runTiaExamples() throws Exception
    {
        SPARQLQueryUtil_KMRO10 KMROPatientTriples = new SPARQLQueryUtil_KMRO10();

        String patientId;
        String domain;

        patientId = "http://patients.kmr.org/danno/2-1";
        // use patient 224 for examples if going to KMRO; use 1 for 10
        if (KMROPatientTriples.sparqlEP == KMRO_EP)
            patientId = "http://patients.kmr.org/danno/2-224";

        System.out.println("\n\n=== Get distinct Vital Sign concepts asserted for a patient ===\n");
        domain = "VitalSign";
        List<Map<String, String>> conceptDefinitionList = KMROPatientTriples.getConceptsOfPatientByDomain(patientId, domain);
        System.out.println(conceptDefinitionList);

        System.out.println("\n\n=== Get distinct dates, in order, on which any vital was asserted for a patient ===\n");
        List<String> datesOfDomain = KMROPatientTriples.getDatesOfPatientByDomain(patientId, domain);
        System.out.println(datesOfDomain);

        System.out.println("\n\n=== Get values of particular concept for a patient, ordered by date ===\n");
        String conceptCode = "4500638"; // VA for Temperature.s
        List<Map<String, String>> conceptValueList = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
        System.out.println(conceptValueList);

        System.out.println("\n\n=== For all LOINC vital types of a patient, get values, ordered by date ===\n");
        Iterator<Map<String, String>> itr = conceptDefinitionList.iterator();
        while(itr.hasNext()) {
            Map<String, String> conceptDefinition = itr.next();
            String terminologySystemLabel = conceptDefinition.get("terminologySystemLabel");
            // only get LOINC
            if (!terminologySystemLabel.equals("LOINC"))
                continue;
            conceptCode = conceptDefinition.get("conceptCode");
            String conceptLabel = conceptDefinition.get("conceptLabel");
            List<Map<String, String>> cvl = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
            System.out.println("\n\tLOINC:" + conceptLabel + ":" + conceptCode + "\n\t\t" + cvl);
        }




        System.out.println("\n\n=== Get distinct Lab concepts asserted for a patient ===\n");
        domain = "LabTestResult";
        conceptDefinitionList = KMROPatientTriples.getConceptsOfPatientByDomain(patientId, domain);
        System.out.println(conceptDefinitionList);

        System.out.println("\n\n=== For all LOINC labs of a patient, get values, ordered by date ===\n");
        itr = conceptDefinitionList.iterator();
        while(itr.hasNext()) {
            Map<String, String> conceptDefinition = itr.next();
            String terminologySystemLabel = conceptDefinition.get("terminologySystemLabel");
            // only get LOINC
            if (!terminologySystemLabel.equals("LOINC"))
                continue;
            conceptCode = conceptDefinition.get("conceptCode");
            String conceptLabel = conceptDefinition.get("conceptLabel");
            List<Map<String, String>> cvl = KMROPatientTriples.getValuesOfPatientByDomainAndConcept(patientId, domain, conceptCode);
            System.out.println("\n\tLOINC:" + conceptLabel + ":" + conceptCode + "\n\t\t" + cvl);
        }

        System.out.println("\n\n=== Get medication orders for a patient, ordered by date ===\n");
        List<Map<String, String>> medicationOrderList = KMROPatientTriples.getMedicationOrdersByPatient(patientId);
        System.out.println(medicationOrderList);

        System.out.println("\n\n=== For all med orders, get dispensations ===\n");
        itr = medicationOrderList.iterator();
        while(itr.hasNext()) {
            Map<String, String> medicationOrder = itr.next();
            String orderId = medicationOrder.get("s");
            List<Map<String, String>> diss = KMROPatientTriples.getDispensationsOfMedicationOrder(orderId);
            System.out.println("\n\tOrder:" + orderId + ":" + "\n\t\t" + diss);
        }
    }

    public static void main(String[] args) throws Exception {
        // runDavideExamples();
        runTiaExamples();
    }
}
