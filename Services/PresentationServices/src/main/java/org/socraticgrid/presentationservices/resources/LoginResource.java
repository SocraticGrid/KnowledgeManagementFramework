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

package org.socraticgrid.presentationservices.resources;

// add this import if you need soapaction
//import javax.xml.soap.MimeHeaders;
import java.net.URLEncoder;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.restlet.Context;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

import org.restlet.Client;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
/**
 *
 * @author markpitman
 */
public class LoginResource extends BaseResource {

    private String loginHost = "http://nhinint04.asu.edu:8080";
    private String username;
    private String password;
    
    public LoginResource(Context context, Request request, Response response) {
        super(context, request, response);

//        try {
//            String query = request.getResourceRef().getQueryAsForm().getQueryString();
//            System.out.println("LOGINquery: "+query);
//            if (checkApiCaller(query)!=true){
//               getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
//               return;
//            }
//
//        } catch (Exception e) {
//            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
//            return;
//        }


        /*
         *  Form form = request.getEntityAsForm();
            for (Parameter parameter : form) {
                System.out.print("parameter " + parameter.getName());
                System.out.println("/" + parameter.getValue());
            }
         */

        this.username = request.getResourceRef().getQueryAsForm().getFirstValue("username");
        this.password = request.getResourceRef().getQueryAsForm().getFirstValue("password");

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        String result="";

        result=authenticate();

        Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }

    private String authenticate(){
        String retVal = "{\"login\": {\"status\" : \"invalid\" }}";
        try {
            loginHost = this.getProperty("SSOEndpoint");
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);
            Request request = new Request(Method.PUT, new Reference(loginHost+"/opensso/identity/authenticate?username="+username+"&password="+password));
            Response response = client.handle(request);
            
            if (response.getStatus().isSuccess()) {
                String ret= response.getEntity().getText();
                ret = ret.substring(0, ret.length()-1);

                Logger.getLogger(LoginResource.class.getName()).log(Level.INFO, ret);
                retVal = "{\"login\": {\"status\" : \"" + ret + "\" }}";
                if (ret.indexOf("token.id=")>-1){
                    String[] ay = ret.split("token.id=");
                    System.out.println("token: "+ ay[1]);
                    String enc = URLEncoder.encode(ay[1], "ISO-8859-1");
                    System.out.println("enc: "+enc);
                    Client client1 = new Client(Protocol.HTTP);
                    client1.setConnectTimeout(10);
                    Request request1 = new Request(Method.POST, new Reference(loginHost+"/opensso/identity/attributes?subjectid="+enc));
                    Response response1 = client1.handle(request1);
                    if (response1.getStatus().isSuccess()){
                        //Map map = new HashMap();
                        String key="";
                        String value="";
                        String[] attribs = response1.getEntity().getText().split("\n");
                        //String jsonHeader = "{\"login\": {";
                        String jsonHeader = "{\"login\":{\"status\":\"valid\",";
                        String jsonFooter = "}}";
                        String jsonBody = "";
                        
                        for (int i = 0; i < attribs.length; i++) {
                            String string = attribs[i];
                            System.out.println(string);
                            if (string.startsWith("userdetails.token.id"))
                                jsonBody= "\"userdetailsTokenId\":\"" + string.substring(21) + "\",";
                            if (string.startsWith("userdetails.attribute.name="))
                                key = string.replaceFirst("userdetails.attribute.name=", "");
                            else if (string.startsWith("userdetails.attribute.value="))
                                value = string.replaceFirst("userdetails.attribute.value=", "");

                            if (!key.equals("") && !value.equals("")){
                                jsonBody = jsonBody + "\"" +key +"\":"+ "\""+ value + "\",";
                                key="";
                                value="";
                            }
                                

                            
                        }

                        retVal=jsonHeader+jsonBody.substring(0, jsonBody.length()-1)+jsonFooter;
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(LoginResource.class.getName()).log(Level.SEVERE, e.toString());
            retVal="-1";
        }
        return retVal;
    }
}
