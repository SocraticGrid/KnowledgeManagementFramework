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
package org.socraticgrid.presentationservices.resources.riskmodel;

import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import org.socraticgrid.presentationservices.resources.BaseResource;
import org.socraticgrid.presentationservices.resources.GetCalendarResource;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.mas.helpers.SyncDialogueHelper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSException;
import org.w3c.dom.ls.LSSerializer;

/**
 *
 * @author nhin
 */
public class GetRiskModelsResource extends BaseResource {

    private String userId;
    private String patientId;
    private String type;
    private String token;
    private String searchFor;
    final Logger logger = Logger.getLogger(GetRiskModelsResource.class.getName());

    public GetRiskModelsResource(Context context, Request request, Response response) {
        super(context, request, response);

        setModifiable(true);

        try {
            String query = request.getResourceRef().getQueryAsForm().getQueryString();
            logger.log(Level.INFO, "query: " + query);

            if (checkApiCaller(query) != true) {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        } catch (Exception e) {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }
        try {
            this.userId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("userId");
            this.searchFor =
                    request.getResourceRef().getQueryAsForm().getFirstValue("searchFor");
            this.patientId =
                    request.getResourceRef().getQueryAsForm().getFirstValue("patientId");
            this.type =
                    request.getResourceRef().getQueryAsForm().getFirstValue("type");
            this.token =
                    request.getResourceRef().getQueryAsForm().getFirstValue("token");
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

        if (token == null || !SessionUtilities.verifyToken(token)) {
            String errorMsg = "The token was not found, the session may have timed out.";
            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
        }

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
        SessionUtilities.setCORSHeaders(this);
        init(context, request, response);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
    }

    @Override
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String ret = handleParamValidation();
        if (!ret.equals("")) {
            return new StringRepresentation(ret, MediaType.APPLICATION_JSON);
        }

        // GetRiskModelsResponse result = getRiskModels(userId, searchFor);
        // Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        // String toReturn = gson.toJson(result);
        String resp = "";
        try {
            resp = getRiskModels(this.userId, this.patientId, this.type);
            
        } catch (Exception e) {
            e.printStackTrace();
            resp = e.getMessage();
        }
        System.out.println("GET RISK MODELS RETURNING: " + resp);
        Representation representation =
                new StringRepresentation(resp, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String handleParamValidation() {
        String ret = "";
        Map<String, String> fieldMap = new HashMap<String, String>();
        fieldMap.put("userId", userId);
        fieldMap.put("patientId", patientId);
        fieldMap.put("type", type);
        ParameterValidator validator = new ParameterValidator(fieldMap);
        String failures = validator.validateMissingOrEmpty();
        if (failures.length() > 1) {
            String errorMessage = "riskModelsFact: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "riskModelsFact");
            ret = err.generateError();
            return ret;
        }
        return ret;
    }


    private String getRiskModels(String userId, String patientId, String type)
    throws Exception
    {
        SyncDialogueHelper helper = SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();

        args.put("userId", userId);
        args.put("patientId", patientId);

        String root = "{\n\"riskModels\": {";
        String jsonRoot = null;
        String jsonResp = null;

        //??? Is searchFor used by DSA ???
        if (!CommonUtil.strNullorEmpty(searchFor)) {
            args.put("searchFor", searchFor);
        }
        System.out.println("===> PS.getRiskModels: args="+args.toString());

        try {
            //-----------------------------------------
            // RETRIEVE xml formatted string of all
            // avilable models.
            //-----------------------------------------
            helper.invokeRequest("getRiskModels", args);
            String xml = (String) helper.getReturn(true);

            logger.log(Level.INFO, "DSA XML response: {0}", xml);

            //-----------------------------------------
            // What should they
            //-----------------------------------------
            if (type.equalsIgnoreCase("Favorites") || type.equalsIgnoreCase("All")) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                try {
                    //-----------------------------------------
                    // Transform xml string into an Document object
                    // so can more easily parse the xml nodes.
                    //-----------------------------------------
                    org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
                    inStream.setCharacterStream(new java.io.StringReader(xml));

                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document doc = db.parse(inStream);
                    doc.getDocumentElement().normalize();

                    NodeList nodes = doc.getElementsByTagName("modelId");

                    //------------------------------------------
                    // List<RiskModelFavorite>
                    // rlist = List of RiskModelFavorite object, containing
                    //         all favorite models for this provider/patient combo.
                    //------------------------------------------
                    DisplayAlertDataUtil util = new DisplayAlertDataUtil();
                    List rlist = util.getRMFByProviderPatient(userId, patientId);

                    //-----------------------------------------
                    // for each DSA xml node, determine if that node's modelId tag matches
                    // any rlist.modelId.
                    // If match, then this is a provider's favorite model and so
                    //    1) tag that DSA xml model as a favorite, and
                    //    2) overlay the DSA given threshold for that model with the rlist.threshold
                    // Is no match, keep DSA xml node for that model as is.
                    //-----------------------------------------
                    for (int i = 0; i < nodes.getLength(); ++i) {
                        Node node = nodes.item(i);
                        String modelId = node.getTextContent();

                        // LOOP through all rlist model and check for a match.
                        Iterator<RiskModelFavorite> rlistIter = rlist.iterator();
                        while (rlistIter.hasNext()) {

                            RiskModelFavorite fav = rlistIter.next();

                            //------------------------------------------
                            // Modify Node if match on modelId is found.
                            //------------------------------------------
                            if (fav.getModelId().equals(modelId)) {

                                String dispThresh = Long.toString(fav.getDisplayThreshold());

                                //GET the parent element of this modelId
                                Node p = node.getParentNode();

                                //-----------------------
                                // Add FAVORITE indicator
                                //-----------------------
                                Element newElem = doc.createElement("favorite");
                                newElem.setTextContent("true");
                                p.appendChild(newElem);

                                //-----------------------
                                // Update the THRESHOLD
                                //-----------------------
                                NodeList nl = p.getChildNodes();
                                for (int j = 0; j < nl.getLength(); ++j) {
                                    Node txt = (Node) nl.item(j);

                                    if (txt.getNodeName().equals("displayThreshold")) {
                                        txt.setTextContent(dispThresh);
                                    }
                                }
                                break;
                            }
                        }//end-rlist-while
                    }//end-dsa-modes-for

                    DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
                    LSSerializer serializer = domImplLS.createLSSerializer();
                    xml = serializer.writeToString(doc);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            XMLSerializer xmlser = new XMLSerializer();
            JSON jsonOut = xmlser.read(xml);

            jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
            jsonResp = jsonRoot + ","+ jsonOut.toString(2).substring(1) + "}";

        }
        catch (Exception e) {
            jsonResp = SynchronousRequestHelperFactory.prepErrorResponse(root, e.getMessage());

            e.printStackTrace();
        }

        System.out.println(jsonResp);

        return jsonResp;
    }


/*
    private String getRiskModels(String userId, String searchFor) throws Exception {

        SyncDialogueHelper helper = SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();
        args.put("userId", userId);
        args.put("patientId", patientId);

        String root = "{\n\"riskModels\": {";
        String jsonRoot = null;
        String jsonResp = null;
        
//        if (!CommonUtil.strNullorEmpty(searchFor)) {
//            args.put("searchFor", searchFor);
//        }
System.out.println("===> PS.getRiskModels: args="+args.toString());

        try {
            helper.invokeRequest("getRiskModels", args);

            String xml = (String) helper.getReturn(true);

            logger.log(Level.INFO, "DSA XML response: {0}", xml);
            if (type.equalsIgnoreCase("Favorites") || type.equalsIgnoreCase("All")) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

                try {
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    org.xml.sax.InputSource inStream = new org.xml.sax.InputSource();
                    inStream.setCharacterStream(new java.io.StringReader(xml));
                    Document doc = db.parse(inStream);
                    doc.getDocumentElement().normalize();
                    NodeList nodes = doc.getElementsByTagName("modelId");
                    NodeList elts = doc.getChildNodes();
                    // XML modelId nodes
                    for (int i = 0; i < nodes.getLength(); ++i) {
                        Node node = nodes.item(i);
                        String modelId = node.getTextContent();
                        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
                        List rlist = util.getRMFByModelId(modelId);

System.out.println("REQ type="+type);


                        // XML node-values that match a db modelId
                        if (rlist.isEmpty() && type.equalsIgnoreCase("Favorites")) {
System.out.println("rlist is empty");

                            xml = removeFragment(doc, i);
                            break;
                        } else {
                            RiskModelFavorite fav = (RiskModelFavorite) rlist.get(0);
                            xml = updateXmlDoc(modelId,
                                    Long.toString(fav.getDisplayThreshold()),
                                    xml, node, doc);

System.out.println("xml AFTER update= " + xml);

                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            XMLSerializer xmlser = new XMLSerializer();
            JSON jsonOut = xmlser.read(xml);

            jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
            jsonResp = jsonRoot + ","+ jsonOut.toString(2).substring(1) + "}";

        }
        catch (Exception e) {
            jsonResp = SynchronousRequestHelperFactory.prepErrorResponse(root, e.getMessage());

            e.printStackTrace();
        }

        System.out.println(jsonResp);
        
        return jsonResp;
    }

    private String removeFragment(Document doc, int i) throws DOMException, LSException {
        String xml;
        Element elt =
                (Element) doc.getElementsByTagName("org.drools.test.ModelElement").item(i);
        elt.getParentNode().removeChild(elt);
        doc.normalize();
        DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        xml = serializer.writeToString(doc);
        return xml;
    }

    private String updateXmlDoc(String modelId, String dispThresh,
            String xml, Node node, Document doc) {
        Node p = node.getParentNode();
        NodeList nl = p.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node txt = (Node) nl.item(i);
            if (txt.getNodeName().equals("displayThreshold")) {
                NodeList nl2 = txt.getChildNodes();
                nl2.item(0).setTextContent(dispThresh);
            }
        }

        DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        xml = serializer.writeToString(doc);

        String cur = "<modelId>" + modelId + "</modelId>";
        String end = cur + "<favorite>true</favorite>";
        xml = xml.replaceFirst(cur, end);
        return xml;
    }
*/

//TODO: Need to realign to new response structure if still want to use this function.
//    private String getStubData() {
//        return "{\n"
//                + "    \"riskModelsFact\" : {\n"
//                + "        \"successStatus\":true,\n"
//                + "        \"statusMessage\":\"This is a message to the user on error status\",\n"
//                + "        \"searchModels\": [\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId1\",\n"
//                + "                \"title\":\"PTSD\",\n"
//                + "                \"disease\":\"PTSD description\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId2\",\n"
//                + "                \"title\":\"Aesthma\",\n"
//                + "                \"disease\":\"Aesthma description\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId3\",\n"
//                + "                \"title\":\"Diabetes\",\n"
//                + "                \"disease\":\"Diabetes description\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId4\",\n"
//                + "                \"title\":\"Hypertension\",\n"
//                + "                \"disease\":\"Hypertension description\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId5\",\n"
//                + "                \"title\":\"Psoriasis\",\n"
//                + "                \"disease\":\"Psoriasis description\"\n"
//                + "            },\n"
//                + "            {\n"
//                + "                \"modelId\":\"modelId6\",\n"
//                + "                \"title\":\"Schizophrenia\",\n"
//                + "                \"disease\":\"Schizophrenia description\"\n"
//                + "            }\n"
//                + "        ]\n"
//                + "    }\n"
//                + "}";
//    }
}
