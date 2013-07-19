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

package org.socraticgrid.presentationservices;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import org.apache.commons.logging.LogFactory;
import java.io.IOException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.socraticgrid.ldapaccess.ContactAcctDTO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import org.socraticgrid.presentationservices.resources.ValidateAccountResource;
import org.socraticgrid.util.SessionUtilities;

/**
 *
 * @author nhin
 */
public class ValidateAccountTest {

    private String server = PresentationServicesSuite.defaultServer;

    public ValidateAccountTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        System.out.println("VALIDATEACCOUNT HOST IS: " + server);
    }

    @After
    public void tearDown() {
    }

    
    @Test
    public void testValidate() throws IOException {
        
        String username = "fry.emory";
        String password = "nhinpass";
        
        this.authenticate(username, password);
    }

    private String authenticate(String username, String password) {
        String retVal = "{\"validateAccountFact\": {\"statusMessage\" : \"Invalid User or System down\" , \"successStatus\" : false }}";
        List<ContactDTO> contacts = LdapService.getContactDAO().findContact("cn,="+username);
        if (contacts == null || contacts.isEmpty()){
            //invalid
            return retVal;
        }
        
        List<ContactAcctDTO> accts = LdapService.getContactDAO().findContactAcct(username, "Mail.Account");
        if (accts == null || accts.isEmpty()){
            //invalid
            return retVal;
        }
        
        ContactAcctDTO acct = accts.get(0);
        
        if (!password.equals(acct.getClearPassword())){
            //invalid
            return retVal;
        }
        
        ContactDTO contact = contacts.get(0);
        
        String uid = contact.getUid();
        String providerId = contact.getEmployeeNumber();
        String securityToken = UUID.randomUUID().toString();
        String role = null;
        
        if (contact.getEmployeeNumber() == null){
            //It is a Patient
            role = "patient";
        }else{
            role = "provider";
        }

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("{\"validateAccountFact\" : {\"statusMessage\" : \"valid\", \"successStatus\" : true");
        
        //token
        responseBuilder.append(",\"token\":\"");
        responseBuilder.append(securityToken);
        responseBuilder.append("\"");
        
        //role
        responseBuilder.append(",\"role\":\"");
        responseBuilder.append(role);
        responseBuilder.append("\"");
        
        //userId
        responseBuilder.append(",\"userId\":\"");
        responseBuilder.append(uid);
        responseBuilder.append("\"");

        //providerId
        if (providerId != null){
            responseBuilder.append(",\"providerId\":\"");
            responseBuilder.append(providerId);
            responseBuilder.append("\"");
        }
        
        responseBuilder.append("}}");

        SessionUtilities.storeTokenData(username, providerId, uid, securityToken);

        //removeDocs();

        return responseBuilder.toString();
    }
    
    //================================================
    @Test
    public void testValidateAcctProvider() throws IOException {
        System.out.println("TEST CASE: testValidateAcct - Provider");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getValidateAcctUrlProvider());
        System.out.println("Request URL is " + getValidateAcctUrlProvider());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("{\"validateAccountFact\" : {\"statusMessage\" : \"valid\", \"successStatus\" : true"));

        webClient.closeAllWindows();
    }

    @Test
    public void testValidateAcctPatient() throws IOException {
        System.out.println("TEST CASE: testValidateAcct - Patient");
        final WebClient webClient = new WebClient();

        final Page page =
                webClient.getPage(getValidateAcctUrlPatient());
        System.out.println("Request URL is " + getValidateAcctUrlPatient());
        WebResponse response = page.getWebResponse();
        System.out.println("RESPONSE IS:\n" + response.getContentAsString() + "\n");
        assertTrue(response.getContentAsString().contains("{\"validateAccountFact\" : {\"statusMessage\" : \"valid\", \"successStatus\" : true"));

        webClient.closeAllWindows();
    }

    private String getValidateAcctUrlProvider() {
        return "http://" + server + ":8080/PresentationServices/validateAccount?"
                + "userName=fry.emory&password=test";
    }

    private String getValidateAcctUrlPatient() {
        return "http://" + server + ":8080/PresentationServices/validateAccount?"
                + "userName=doe.jane&password=test";
    }
}
