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


import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import org.junit.After;
import org.junit.Before;
import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.util.CommonUtil;


import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashMap;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.drools.mas.helpers.SyncDialogueHelper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


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
public class TestGetRiskModels {

    public TestGetRiskModels() {
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

    final Logger logger = Logger.getLogger(TestGetRiskModels.class.getName());

    private String SAMPLE_DSA_MODELS_LIST =
        "<org.socraticgrid.kmr2.clinicalAgent.services.ModelList>"
        + "<patientId>99990070</patientId>"
        + "<tags>"
        + "<string>Risk</string>"
        + "</tags>"
        + "<models>"
        + "<org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
            + "<modelId>MockCold</modelId>"
            + "<tags>"
            + "<string>Risk</string>"
            + "<string>E</string>"
            + "</tags>"
            + "<title>Uncommon Cold</title>"
            + "<disease>Uncommon Cold</disease>"
            + "<displayThreshold>50</displayThreshold>"
        + "</org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
        + "<org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
            + "<modelId>MockDiabetes</modelId>"
            + "<tags>"
            + "<string>Risk</string>"
            + "</tags>"
            + "<title> Particularly Nasty Triabetes </title>"
            + "<disease> Particularly Nasty Triabetes </disease>"
            + "<displayThreshold>50</displayThreshold>"
            + "</org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
        + "<org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
            + "<modelId>MockPTSD</modelId>"
            + "<tags>"
            + "<string>Risk</string>"
            + "<string>E</string>"
            + "</tags>"
            + "<title> Post-Traumatic Stress Disorder </title>"
            + "<disease> Post-Traumatic Stress Disorder </disease>"
            + "<displayThreshold>50</displayThreshold>"
            + "</org.socraticgrid.kmr2.clinicalAgent.services.ModelElement>"
            + "</models>"
        + "</org.socraticgrid.kmr2.clinicalAgent.services.ModelList>";

    @Test
    public void testGetRiskModels() throws Exception {
        String resp = this.getRiskModels("1", "99990070", "All");
    }
    private String getRiskModels(String userId, String patientId, String type) throws Exception {

        //String patientId = "99990070";
        //String type = "All";
        
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
            //-----------------------------------------
            // RETRIEVE xml formatted string of all
            // avilable models.
            //-----------------------------------------
//            helper.invokeRequest("getRiskModels", args);
//            String xml = (String) helper.getReturn(true);
//DEBUG ONLY - REMOVE
String xml = SAMPLE_DSA_MODELS_LIST;
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

//                        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
//                        List rlist = util.getRMFByModelId(modelId);
System.out.println("================= working modelId = " + modelId);
System.out.println("xml BEFORE= " + xml);

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
                                String xml, Node node, Document doc)
    {
        //---------------------
        // UPDATE the THRESHOLD
        //---------------------
        Node p = node.getParentNode();
        NodeList nl = p.getChildNodes();
        for (int i = 0; i < nl.getLength(); ++i) {
            Node txt = (Node) nl.item(i);
            if (txt.getNodeName().equals("displayThreshold")) {
                NodeList nl2 = txt.getChildNodes();
                nl2.item(0).setTextContent(dispThresh);
            }
        }

        //-------------------------
        // ADD <favorite> indicator
        //-------------------------
        DOMImplementationLS domImplLS = (DOMImplementationLS) doc.getImplementation();
        LSSerializer serializer = domImplLS.createLSSerializer();
        xml = serializer.writeToString(doc);

        String cur = "<modelId>" + modelId + "</modelId>";
        String end = cur + "<favorite>true</favorite>";
        xml = xml.replaceFirst(cur, end);
        return xml;
    }

}