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
package org.socraticgrid.presentationservices.resources;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.ErrorResponse;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.drools.mas.helpers.SyncDialogueHelper;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Status;
import org.restlet.resource.ResourceException;

/**
 *
 * @author jharby
 */
public class SurveyResource extends BaseResource {

    private String userId;
    private String patientId;
    private String surveyId;
    private String questionId;
    private String answer;
    private String token = "";
    final Logger logger = Logger.getLogger(SurveyResource.class.getName());

    public SurveyResource(Context context, Request request, Response response) {
        super(context, request, response);
        if (request.getMethod().equals(Method.GET)) {
            SessionUtilities.setCORSHeaders(this);
            try {
                this.token =
                        request.getResourceRef().getQueryAsForm().getFirstValue("token");
                this.userId =
                        request.getResourceRef().getQueryAsForm().getFirstValue("userId");
                this.patientId =
                        request.getResourceRef().getQueryAsForm().getFirstValue("patientId");
                this.surveyId =
                        request.getResourceRef().getQueryAsForm().getFirstValue("surveyId");
                this.questionId =
                        request.getResourceRef().getQueryAsForm().getFirstValue("questionId");
                this.answer =
                        request.getResourceRef().getQueryAsForm().getFirstValue("answer");

                if (token == null || !SessionUtilities.verifyToken(token)) {
                    String errorMsg = "The token was not found, the session may have timed out.";
                    SessionUtilities.generateErrorRepresentation(errorMsg, "400", getResponse());
                }

            } catch (Exception e) {
                logger.severe(e.getMessage());
                e.printStackTrace();
            }
        }

        if (request.getMethod().equals(Method.POST)) {
            setModifiable(true);
            Representation rep = request.getEntity();
            Form form = new Form(rep);
            setParameters(form);
        }
    }

    private void setParameters(Form form) {

        for (Parameter parameter : form) {
            System.out.print("===> SurveyResource: parameter= " + parameter.getName());
            System.out.println("/" + parameter.getValue());
        }

        if (getRequestParameter(form, "token")) {
            token = form.getFirst("token").getValue();
        }
        if (getRequestParameter(form, "patientId")) {
            patientId = form.getFirst("patientId").getValue();
        }
        if (getRequestParameter(form, "surveyId")) {
            surveyId = form.getFirst("surveyId").getValue();
        }
        if (getRequestParameter(form, "userId")) {
            userId = form.getFirst("userId").getValue();
        }
        if (getRequestParameter(form, "questionId")) {
            questionId = form.getFirst("questionId").getValue();
        }
        if (getRequestParameter(form, "answer")) {
            answer = form.getFirst("answer").getValue();
        }
    }

    private boolean getRequestParameter(Form form, String param) {
        if (form.getFirst(param) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void acceptRepresentation(Representation entity) throws ResourceException {
        if (!SessionUtilities.verifyToken(token)) {
            return;
        }
        SessionUtilities.setCORSHeaders(this);
        String output = setSurvey();
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(output,
                MediaType.APPLICATION_JSON);
    }

    @Override
    public void init(Context context, Request request, Response response) {
        super.init(context, request, response);
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    @Override
    public Representation represent(Variant variant) throws org.restlet.resource.ResourceException {
        String result = "";
        if (questionId == null || questionId.equals("")) {
            result = getSurvey();
        }
        else {
            result = setSurvey();
        }
        Representation representation =
                new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }

    private void generateError(String errorMsg, String output) {
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(output, MediaType.APPLICATION_JSON);
        logger.log(Level.SEVERE, errorMsg);
    }

    private String getSurveyStub() {
        return getMockReturn();
    }

    private String getSurvey() {
        String json = "";

        String failures = validateFieldsGet();

        if (failures.length() > 1) {
            String errorMessage = "GetSurvey: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "getSurveyFact");
            String ret = err.generateError();
            return ret;
        }

        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SyncDialogueHelper helper = SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();

        // Remove after dev
//        args.put("userId", "patient1");
//        args.put("patientId", "surveyPatient");
//        args.put("surveyId", "123456UNIQUESURVEYID");

        args.put("userId", userId);
        args.put("patientId", patientId);
        args.put("surveyId", surveyId);

        String root = "{\n\"surveyFact\": {";
        String jsonRoot = null;
        String jsonResp = null;
        try {

            helper.invokeRequest("getSurvey", args);
            String xml = (String) helper.getReturn(false);

            XMLSerializer xmlser = new XMLSerializer();
            JSON jsonOut = xmlser.read(xml);
//        String jsonResp = jsonOut.toString(2);
//        jsonResp = jsonResp.replaceAll("\"false\"", "false");
//        jsonResp = jsonResp.replaceAll("\"true\"", "true");
//        logger.info("GET SURVEY RETURNING: " + jsonResp);

            jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
            jsonResp = jsonRoot + ","+ jsonOut.toString(2).substring(1) + "}";

        }
        catch (Exception e) {
            jsonResp = SynchronousRequestHelperFactory.prepErrorResponse(root, e.getMessage());

            e.printStackTrace();
        }

        return jsonResp;
    }

    private String setSurvey() {

        String failures = validateFieldsSet();

        if (failures.length() > 1) {
            String errorMessage = "SetSurvey: "
                    + failures + "is a missing required field";
            ErrorResponse err = new ErrorResponse(errorMessage, "setSurveyFact");
            String ret = err.generateError();
            return ret;
        }

        XStream serializer = new XStream(new JsonHierarchicalStreamDriver());
        SyncDialogueHelper helper = SynchronousRequestHelperFactory.getInstance();
        LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>();

        System.out.println("===> Calling setSurvey for surveyId/questionId="+ surveyId +"/"+questionId);

        args.put("userId", userId);
        args.put("patientId", patientId);
        args.put("surveyId", surveyId);
        args.put("questionId", questionId);
        args.put("answer", answer);

        String root = "{\n\"surveyFact\": {";
        String jsonRoot = null;
        String jsonResp = null;
        try {
            helper.invokeRequest("setSurvey", args);
            String xml = (String) helper.getReturn(false);

            XMLSerializer xmlser = new XMLSerializer();
            JSON jsonOut = xmlser.read(xml);

            jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
            jsonResp = jsonRoot + ","+ jsonOut.toString(2).substring(1) + "}";

        }
        catch (Exception e) {
            jsonResp = SynchronousRequestHelperFactory.prepErrorResponse(root, e.getMessage());

            e.printStackTrace();
        }


//            jsonRsp = jsonOut.toString(2); // 2 == number of indents.
//            jsonRsp = jsonRsp.replaceAll("\"true\"", "true");
//            jsonRsp = jsonRsp.replaceAll("\"false\"", "false");
//        }
//        catch (Exception e) {
//            System.out.println("===> ERROR:  Exception thrown! " + e.toString());
//            e.printStackTrace();
//
//            jsonRsp =
//                  "{\n"
//                + "    \"org.drools.informer.presentation.surveyFact\" : {\n"
//                + "        \"successStatus\":false,\n"
//                //+ "        \"statusMessage\":\"" + e.toString() + "\",\n"
//                + SynchronousRequestHelperFactory.insertPSStatus(jsonRsp, false, e.getMessage())
//                + "    }\n"
//                + "}";
//
//
////            jsonRsp = generateJsonErrorResp(
////                    false
////                    ,"Error thrown @ DSA.setSurvey \n" + e.toString() );
//        }
//
//        if (CommonUtil.strNullorEmpty(xml)) {
//            System.out.println("===> Calling setSurvey resulted in a null return!");
//
//            jsonRsp = generateJsonErrorResp(
//                    false
//                    ,"DSA.setSurvey gave null response!" );
//        }

        System.out.println("SET SURVEY RETURNING: " + jsonResp);
        return jsonResp;
    }

    private String validateFieldsGet() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("userId", userId);
        fields.put("patientId", patientId);
        fields.put("surveyId", surveyId);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }

    private String validateFieldsSet() {
        Map<String, Object> fields = new HashMap<String, Object>();
        fields.put("userId", userId);
        fields.put("patientId", patientId);
        fields.put("surveyId", surveyId);
        fields.put("questionId", questionId);
        fields.put("answer", answer);
        ParameterValidator validator = new ParameterValidator(fields);
        return validator.validateMissingOrEmpty();
    }

    /**
     * Generate a static Json Error Response.
     * @param status
     * @param errMsg
     * @return
     */
    private String generateJsonErrorResp( boolean status, String errMsg ) {
            return
                  "{\n"
                + "    \"surveyFact\":{\n"
                + "        \"successStatus\":"+ status +",\n"
                + "        \"statusMessage\":"
                + "\"" + errMsg + "\""
                + "\n    }\n"
                + "}";
    }

    private String getMockReturn() {
        String s = "{\n"
                + "    \"surveyFact\":{\n"
                + "       \"surveyId\":\"" + surveyId + "\",\n"
                + "       \"successStatus\":true,\n"
                + "       \"statusMessage\":\"(ok)\",\n"
                + "       \"name\":\"Name or title of the Survey (TestSurvey)\",\n"
                + "       \"interactive\":false,\n"
                + "       \"language\":\"EN\",\n"
                + "       \"contextDesc\":\"Inbox\",\n"
                + "       \"surveyClass\":\"TestSurvey\",\n"
                + "       \"surveyQuestions\":[\n"
                + "          {\n"
                + "             \"questionId\":\"123UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please choose one:\",\n"
                + "             \"required\":false,\n"
                + "             \"answerType\":\"number\",\n"
                + "             \"suggestedControl\":\"combobox\",\n"
                + "             \"singleAnswer\":false,\n"
                + "             \"possibleAnswers\":[\n"
                + "                {\n"
                + "                   \"key\":\"6\",\n"
                + "                   \"value\":\"Choice6\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"5\",\n"
                + "                   \"value\":\"Choice5\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"4\",\n"
                + "                   \"value\":\"Choice4\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"3\",\n"
                + "                   \"value\":\"Choice3\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"2\",\n"
                + "                   \"value\":\"Choice2\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"1\",\n"
                + "                   \"value\":\"Choice1\"\n"
                + "                }\n"
                + "             ]\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"124UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please insert Domicile:\",\n"
                + "             \"required\":false,\n"
                + "             \"answerType\":\"text\",\n"
                + "             \"suggestedControl\":\"radio\",\n"
                + "             \"singleAnswer\":false,\n"
                + "             \"possibleAnswers\":[\n"
                + "                {\n"
                + "                   \"key\":\"Rural\",\n"
                + "                   \"value\":\"Rural Dom.\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"Urban\",\n"
                + "                   \"value\":\"Urban Dom.\"\n"
                + "                }\n"
                + "             ]\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"112UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please choose your favorite hobby:\",\n"
                + "             \"required\":false,\n"
                + "             \"answerType\":\"text\",\n"
                + "             \"suggestedControl\":\"radio\",\n"
                + "             \"singleAnswer\":true,\n"
                + "             \"possibleAnswers\":[\n"
                + "                {\n"
                + "                   \"key\":\"HGL\",\n"
                + "                   \"value\":\"Hang Gliding\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"GLF\",\n"
                + "                   \"value\":\"Golf\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"FSH\",\n"
                + "                   \"value\":\"Fishing\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"CMP\",\n"
                + "                   \"value\":\"Camping\"\n"
                + "                }\n"
                + "             ]\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"200UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please enter a numeric value:\",\n"
                + "             \"required\":true,\n"
                + "             \"answerType\":\"numeric\",\n"
                + "              \"currentAnswer\":\"3\",\n"
                + "             \"suggestedControl\":\"text\",\n"
                + "             \"singleAnswer\":true,\n"
                + "              \"successType\":\"valid\"\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"211UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please confirm that you received the radiology scans:\",\n"
                + "             \"required\":true,\n"
                + "             \"answerType\":\"text\",\n"
                + "             \"suggestedControl\":\"button\",\n"
                + "             \"singleAnswer\":true,\n"
                + "              \"possibleAnswers\":[\n"
                + "                 {\n"
                + "                    \"key\":\"1\",\n"
                + "                    \"value\":\"Yes\"\n"
                + "                 },\n"
                + "                 {\n"
                + "                    \"key\":\"2\",\n"
                + "                    \"value\":\"No, I haven't\"\n"
                + "                 },\n"
                + "                 {\n"
                + "                    \"key\":\"3\",\n"
                + "                    \"value\":\"I don't understand\"\n"
                + "                 }\n"
                + "              ]\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"212UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please enter a date value:\",\n"
                + "             \"required\":true,\n"
                + "              \"currentAnswer\":\"2011-05-01\",\n"
                + "             \"answerType\":\"date\",\n"
                + "             \"suggestedControl\":\"text\",\n"
                + "             \"singleAnswer\":true,\n"
                + "              \"successType\":\"valid\"\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"213UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please enter another numeric value:\",\n"
                + "             \"required\":true,\n"
                + "             \"answerType\":\"numeric\",\n"
                + "             \"suggestedControl\":\"text\",\n"
                + "             \"singleAnswer\":true,\n"
                + "              \"currentAnswer\":\"gwe5\",\n"
                + "              \"successType\":\"invalid\",\n"
                + "              \"statusMessage\": \"Value is not numeric\"\n"
                + "          },\n"
                + "          {\n"
                + "             \"questionId\":\"201UNIQUEQUESTIONID\",\n"
                + "             \"preLabel\":\"Please choose ALL the things you like to do:\",\n"
                + "             \"required\":false,\n"
                + "             \"answerType\":\"text\",\n"
                + "             \"suggestedControl\":\"checkbox\",\n"
                + "              \"currentAnswer\":[ \"HGL\",\"CMP\"],\n"
                + "             \"singleAnswer\":false,\n"
                + "              \"successType\":\"valid\",\n"
                + "             \"possibleAnswers\":[\n"
                + "                {\n"
                + "                   \"key\":\"HGL\",\n"
                + "                   \"value\":\"Hang Gliding\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"GLF\",\n"
                + "                   \"value\":\"Golf\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"FSH\",\n"
                + "                   \"value\":\"Fishing\"\n"
                + "                },\n"
                + "                {\n"
                + "                   \"key\":\"CMP\",\n"
                + "                   \"value\":\"Camping\"\n"
                + "                }\n"
                + "             ]\n"
                + "          }\n"
                + "       ]\n"
                + "    }\n"
                + "}";
        return s;
    }

    private String setMockReturn() {
        String s = "{\n"
                + "    \"surveyFact\":{\n"
                + "        \"successStatus\":true,\n"
                + "        \"statusMessage\":\"There was a problem with this answer\",\n"
                + "        \"surveyPercentComplete\":67,\n"
                + "        \"updateQuestions\":[\n"
                + "            {\n"
                + "               \"questionId\":\"" + questionId + "\",\n"
                + "                \"action\":\"add\",\n"
                + "                \"position\":0,\n"
                + "               \"preLabel\":\"This question was put on top of all questions with position 0 : \",\n"
                + "               \"required\":true,\n"
                + "               \"answerType\":\"date\",\n"
                + "               \"suggestedControl\":\"text\",\n"
                + "               \"singleAnswer\":true\n"
                + "            },\n"
                + "            {\n"
                + "               \"questionId\":\"400UNIQUEQUESTIONID\",\n"
                + "                \"action\":\"add\",\n"
                + "                \"position\":4,\n"
                + "               \"preLabel\":\"This question was inserted into index position 4: \",\n"
                + "               \"required\":true,\n"
                + "               \"answerType\":\"text\",\n"
                + "               \"suggestedControl\":\"text\",\n"
                + "               \"singleAnswer\":true\n"
                + "            },\n"
                + "            {\n"
                + "               \"questionId\":\"500UNIQUEQUESTIONID\",\n"
                + "                \"action\":\"add\",\n"
                + "               \"preLabel\":\"This question will be appended to the bottom since there is no position : \",\n"
                + "               \"required\":true,\n"
                + "               \"answerType\":\"text\",\n"
                + "               \"suggestedControl\":\"text\",\n"
                + "               \"singleAnswer\":true\n"
                + "            },\n"
                + "            {\n"
                + "               \"questionId\":\"213UNIQUEQUESTIONID\",\n"
                + "               \"preLabel\":\"This question was answered incorrectly.  Please enter a valid numeric value : \",\n"
                + "                \"currentAnswer\":\"gwe5\",\n"
                + "                \"successType\":\"invalid\",\n"
                + "                \"action\":\"update\"\n"
                + "            },\n"
                + "            {\n"
                + "               \"questionId\":\"212UNIQUEQUESTIONID\",\n"
                + "               \"preLabel\":\"This question was updated, but the value cannot be changed anymore : \",\n"
                + "                \"currentAnswer\":\"2011-05-01\",\n"
                + "                \"successType\":\"valid\",\n"
                + "                \"action\":\"disable\"\n"
                + "            },\n"
                + "            {\n"
                + "               \"questionId\":\"123UNIQUEQUESTIONID\",\n"
                + "                \"action\":\"delete\"\n"
                + "            },\n"
                + "            {\n"
                + "                \"questionId\":\"124UNIQUEQUESTIONID\",\n"
                + "                \"action\":\"update\",\n"
                + "                \"position\":2,\n"
                + "                \"preLabel\":\"This Domicile question was updated and moved to position 2 : \",\n"
                + "                \"required\":false,\n"
                + "                \"answerType\":\"text\",\n"
                + "                \"currentAnswer\":\"Rural\",\n"
                + "                \"suggestedControl\":\"radio\",\n"
                + "                \"singleAnswer\":false,\n"
                + "                \"successType\":\"valid\",\n"
                + "                \"possibleAnswers\":[\n"
                + "                   {\n"
                + "                      \"key\":\"Rural\",\n"
                + "                      \"value\":\"Rural Dom.\"\n"
                + "                   },\n"
                + "                    {\n"
                + "                       \"key\":\"Urban\",\n"
                + "                       \"value\":\"Urban Dom.\"\n"
                + "                    },\n"
                + "                    {\n"
                + "                       \"key\":\"Suburban\",\n"
                + "                       \"value\":\"Suburban Dom.\"\n"
                + "                    }\n"
                + "                ]\n"
                + "             }\n"
                + "\n"
                + "        ]\n"
                + "    }\n"
                + "}";

        return s;
    }
}
