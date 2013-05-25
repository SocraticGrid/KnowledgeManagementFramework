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
package org.socraticgrid.presentationservices.helpers;

import org.socraticgrid.common.account.Response;
import org.socraticgrid.common.dda.DetailData;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.SummaryData;
import org.socraticgrid.common.dda.NameValuesPair;
import org.socraticgrid.common.dda.SetMessageDataResponseType;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author  Jerry Goodnough
 */
public class JSONHelper {

    /*
    "id":{"codeSystem":"2.16.840.1.113883.3.200",
    "codeSystemName":"","value":"11987",
    "floatValue":"11987.0",
    "displayable":"true"},
     */
    public static JSONObject getDetaitData(DetailData data, boolean includeItemUpdates) {

        JSONObject out = new JSONObject();
        try {
            JSONArray joa = new JSONArray();
            out.put("itemValues", joa);
            if (data.getItemValues() != null) {
                Iterator<NameValuesPair> itr = data.getItemValues().iterator();
                while (itr.hasNext()) {
                    NameValuesPair val = itr.next();
                    String name = val.getName();
                    if ((includeItemUpdates) || (!name.startsWith("update"))) {
                        JSONObject o = new JSONObject();
                        JSONArray joa1 = new JSONArray();
                        o.put("values", joa1);
                        o.put("name", name);
                        if (val.getValues() != null) {
                            Iterator<String> itr1 = val.getValues().iterator();
                            while (itr1.hasNext()) {
                                joa1.put(itr1.next());
                            }
                        }
                        joa.put(o);
                    }
                }
            }
            out.put("data", data.getData());
            out.put("patient", data.getPatient());
            out.put("itemId", data.getItemId());
            out.put("description", data.getDescription());
            out.put("dateCreated", data.getDateCreated().toString());
            out.put("dataSource", data.getDataSource());
            out.put("author", data.getAuthor());

        } catch (JSONException exp) {
        }
        return out;
    }

    public static JSONObject getSummaryData(SummaryData data, boolean includeItemUpdates) {

        JSONObject out = new JSONObject();
        try {
            JSONArray joa = new JSONArray();

            out.put("itemValues", joa);
            if (data.getItemValues() != null) {
                Iterator<NameValuesPair> itr = data.getItemValues().iterator();
                while (itr.hasNext()) {
                    NameValuesPair val = itr.next();
                    String name = val.getName();
                    if ((includeItemUpdates) || (!name.startsWith("update"))) {
                        JSONObject o = new JSONObject();
                        JSONArray joa1 = new JSONArray();
                        o.put("name", name);
                        if (val.getValues() != null) {
                            Iterator<String> itr1 = val.getValues().iterator();
                            while (itr1.hasNext()) {
                                joa1.put(itr1.next());
                            }
                        }
                        o.put("values", joa1);
                        joa.put(o);
                    }
                }
            }
            out.put("patient", data.getPatient());
            out.put("itemId", data.getItemId());
            out.put("description", data.getDescription());
            out.put("dateCreated", data.getDateCreated().toString());
            out.put("dataSource", data.getDataSource());
            out.put("author", data.getAuthor());
        } catch (JSONException exp) {
        }
        return out;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   id           DOCUMENT ME!
     * @param   codeSystem   DOCUMENT ME!
     * @param   codeSysName  DOCUMENT ME!
     * @param   displayable  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JSONObject getId(String id, String codeSystem,
            String codeSysName, String displayable) {
        JSONObject out = new JSONObject();

        try {

            if (codeSystem == null) {
                codeSystem = "2.16.840.1.113883.3.200";
            }

            if (codeSysName == null) {
                codeSysName = "";
            }

            if (displayable == null) {
                displayable = "true";
            }

            String floatVal = "";

            if (!id.contains(".")) {
                floatVal = id + ".0";
            }

            out.put("codeSystem", codeSystem);
            out.put("CodeSystemName", codeSysName);
            out.put("value", id);
            out.put("floatValue", floatVal);
            out.put("displayable", displayable);
        } catch (Exception e) {
            //Should never occur
        }

        return out;
    }

    /*
    "firstName":"BOB",
    "familyName":"PROVIDER0",
    "nameType":
    {
    "codeSystem":"","codeSystemName":"",
    "code":"L","label":"Legal"
    }
     */
    /**
     * DOCUMENT ME!
     *
     * @param   firstName   DOCUMENT ME!
     * @param   familyName  DOCUMENT ME!
     * @param   typeCode    DOCUMENT ME!
     * @param   typeLabel   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JSONObject getName(String firstName, String familyName,
            String typeCode, String typeLabel) {
        JSONObject out = new JSONObject();
        JSONObject type = new JSONObject();

        try {
            type.put("codeSystem", "");
            type.put("codeSystemName", "");
            type.put("code", typeCode);
            type.put("label", typeLabel);
            out.put("firstName", firstName);
            out.put("familyName", familyName);
            out.put("nameType", type);
        } catch (Exception e) {
            //Should never occur
        }

        return out;

    }

    /*
     * 		"gender":{"codeSystem":"","codeSystemName":"","code":"M","label":"Male"},
     */
    public static JSONObject getGender(String genderLabel) {
        JSONObject out = new JSONObject();

        try {
            out.put("codeSystem", "");
            out.put("codeSystemName", "");
            out.put("code", genderLabel.substring(0, 1));
            out.put("label", genderLabel);
        } catch (Exception e) {
            //Should never occur
        }

        return out;

    }

    /*
    "age":{"value":"35","floatValue":"35.0","unit":"yrs"},
     */
    public static JSONObject getAge(String age) {
        JSONObject out = new JSONObject();

        try {
            out.put("value", age);
            out.put("floatValue", age + ".0");
            out.put("unit", "yrs");
        } catch (Exception e) {
            //Should never occur
        }

        return out;

    }

    public static JSONObject getPatient(String id, String firstName, String familyName, String gender, String age) {
        JSONObject out = new JSONObject();

        try {
            out.put("id", JSONHelper.getId(id, null, null, null));
            out.put("name", JSONHelper.getName(firstName, familyName, "L", "Legal"));
            out.put("gender", JSONHelper.getGender(gender));
            out.put("age", age);
        } catch (Exception e) {
            //should never get here
        }
        return out;
    }

    public static JSONObject getWard(String id, String name, String type) {
        JSONObject out = new JSONObject();

        try {
            out.put("id", id);
            out.put("name", name);
            out.put("type", type);
        } catch (Exception e) {
            //should never get here
        }
        return out;
    }

    public static JSONObject getClinic(String id, String name, String type) {
        JSONObject out = new JSONObject();

        try {
            out.put("id", id);
            out.put("name", name);
            out.put("type", type);
        } catch (Exception e) {
            //should never get here
        }
        return out;
    }

    public static JSONObject getCallStatus(CallStatus status) {
        JSONObject out = new JSONObject();
        try {
            out.put("isError", status.isError());
            out.put("code", status.getStatusCode());
            out.put("detail", status.getStatusDetail());
        } catch (Exception e) {
            //should never get here
        }
        return out;

    }

    /**
     * Converts the Response Object to JSON Object.
     *
     * @param Response
     *
     * @return JSONObject
     */
    public static JSONObject getResponse(Response response) {
        JSONObject out = new JSONObject();
        JSONObject out1 = new JSONObject();
        String header = "setAccountFact";
        try {
            out.put(header, out1);
            out1.put("successStatus", response.isSuccess());
            out1.put("statusMessage", response.getText());
        } catch (Exception e) {
            //should never get here
        }
        return out;
    }

    public static Object getResponse(SetMessageDataResponseType response) {
        JSONObject out1 = new JSONObject();
        JSONObject out = new JSONObject();
        String header = "setMessagesFact";
        try {
            out.put(header, out1);
            out1.put("successStatus", response.isSuccessStatus());
            out1.put("statusMessage", response.getMessage());
        } catch (Exception e) {
            //should never get here
        }
        return  out;
    }

    public static Object getErrorResponse(ErrorResponse err) {
        JSONObject out1 = new JSONObject();
        JSONObject out = new JSONObject();
        try {
            out.put(err.getHeader(), out1);
            out1.put("successStatus", err.isSuccessStatus());
            out1.put("statusMessage", err.getErrorMsg());
        } catch (Exception e) {
            //should never get here
        }
        return out;
    }
}
