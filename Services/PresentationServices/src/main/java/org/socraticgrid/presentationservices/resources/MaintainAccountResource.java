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

import org.socraticgrid.account.AccountMgrImpl;
import org.socraticgrid.common.account.Address;
import org.socraticgrid.common.account.CreateAccountRequestType;
import org.socraticgrid.common.account.Name;
import org.socraticgrid.common.account.PhoneNumber;
import org.socraticgrid.util.SessionUtilities;
import org.socraticgrid.presentationservices.helpers.JSONHelper;
import org.socraticgrid.presentationservices.helpers.ParameterValidator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

/**
 *
 * @author Sushma
 */
public class MaintainAccountResource extends BaseResource {

    /** create or update action */
    private String action = "";
    private String userName = "";
    private String password = "";
    private String firstName = "";
    private String middleName = "";
    private String lastName = "";
    private String ssn = "";
    private String address1 = "";
    private String address2 = "";
    private String city = "";
    private String state = "";
    private String postalCode = "";
    private String homePhone = "";
    private String mobilePhone = "";
    private String workPhone = "";
    private String emailAddress = "";
    final Logger logger = Logger.getLogger(MaintainAccountResource.class.getName());
    org.socraticgrid.common.account.Response output = new org.socraticgrid.common.account.Response();

    /**
     * Creates a new MainitainAccountResource object.
     *
     * @param  context   context
     * @param  request   request
     * @param  response  response
     */
    public MaintainAccountResource(Context context, Request request, Response response) {
        super(context, request, response);
        setModifiable(true);
    }

    /**
     * @see  org.restlet.resource.Resource #acceptRepresentation(org.restlet.resource.Representation)
     */
    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {

        SessionUtilities.setCORSHeaders(this);
        Form form = new Form(entity);
        setRequestValues(form);

        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("action", action);
        paramMap.put("userName", userName);
        paramMap.put("password", password);
        paramMap.put("firstName", firstName);
        paramMap.put("lastName", lastName);
        paramMap.put("ssn", ssn);
        paramMap.put("address1", address1);
        paramMap.put("city", city);
        paramMap.put("state", state);
        paramMap.put("postalCode", postalCode);
        paramMap.put("emailAddress", emailAddress);

        ParameterValidator validator = new ParameterValidator(paramMap);
        String failures = validator.validateMissingOrEmpty();

        if (failures.length() > 1) {
            String errorMessage = "setAccount: "
                    + failures + "are missing required field(s)";
            generateError(errorMessage);
            return;
        }

//        if (null == action || action.isEmpty()) {
//            generateError("maintainAcount: Action is a Required Field");
//            return;
//        } else if (null == userName || userName.isEmpty()) {
//            generateError("maintainAcount: UserName is a Required Field");
//            return;
//        } else if (null == password || password.isEmpty()) {
//            generateError("maintainAcount: Password is a Required Field");
//            return;
//        } else if (null == firstName || firstName.isEmpty()) {
//            generateError("maintainAcount: FirstName is a Required Field");
//            return;
//        } else if (null == lastName || lastName.isEmpty()) {
//            generateError("maintainAcount:  LastName is a Required Field");
//            return;
//        } else if (null == ssn || ssn.isEmpty()) {
//            generateError("maintainAcount: SSN is a Required Field");
//            return;
//        } else if (null == address1 || address1.isEmpty()) {
//            generateError("maintainAcount: Address1 is a Required Field");
//            return;
//        } else if (null == city || city.isEmpty()) {
//            generateError("maintainAcount: City is a Required Field");
//            return;
//        } else if (null == state || state.isEmpty()) {
//            generateError("maintainAcount: State is a Required Field");
//            return;
//        } else if (null == postalCode || postalCode.isEmpty()) {
//            generateError("maintainAcount: PostalCode is a Required Field");
//            return;
//        }
//        else if (null == emailAddress || emailAddress.isEmpty()) {
//            generateError("maintainAcount: EmailAddress is a Required Field");
//            return;
//        }

        if (null != emailAddress && !isEmailValid(emailAddress)) {
            output.setSuccess(false);
            output.setText("setAcount: Email Address is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }

        if (null != homePhone && !isPhoneNumberValid(homePhone)) {
            output.setSuccess(false);
            output.setText("setAcount: Home Phone Number is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }

        if (null != mobilePhone && !mobilePhone.isEmpty() && !isPhoneNumberValid(mobilePhone)) {
            output.setSuccess(false);
            output.setText("setAcount: Mobile Phone Number is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }

        if (null != workPhone && !workPhone.isEmpty() && !isPhoneNumberValid(workPhone)) {
            output.setSuccess(false);
            output.setText("setAcount: Work Phone Number is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }
        if(null != postalCode && !postalCode.isEmpty() && !isPostalCodeValid(postalCode)){
            output.setSuccess(false);
            output.setText("setAcount: Postal Code is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }
          if(null != state && !state.isEmpty() && !isStateValid(state)){
            output.setSuccess(false);
            output.setText("setAcount: State is invalid");
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                    MediaType.APPLICATION_JSON);
            return;
        }
        if (action.equalsIgnoreCase("create")) {
            output = this.createAccount(userName, password, firstName, middleName, lastName, address1, address2, city, state, postalCode, homePhone, mobilePhone, workPhone, emailAddress, ssn);
        } else {
            generateError("setAcount: Action cannot be other than create");
            return;
        }
        getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                MediaType.APPLICATION_JSON);

        if (!output.isSuccess()) {
            output.setText("Unsuccessful Account Creation");

        }
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(),
                MediaType.APPLICATION_JSON);
        return;
    }

    /**
     * Handle PUT requests. replace or update resource
     */
    @Override
    public void storeRepresentation(Representation entity)
            throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    /**
     * @see  org.restlet.resource.Resource#allowPut()
     */
    @Override
    public boolean allowPut() {
        return false;
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    /**
     * Handle DELETE requests. remove/delete resource
     */
    @Override
    public void removeRepresentations() throws ResourceException {
        getResponse().setStatus(Status.SERVER_ERROR_NOT_IMPLEMENTED);
    }

    private org.socraticgrid.common.account.Response createAccount(String userName, String password, String firstName, String middleName, String lastName, String address1, String address2, String city, String state, String postalCode, String homePhone, String mobilePhone, String workPhone, String emailAddress, String ssn) {
//        AccountManager service = new org.socraticgrid.acctmgr.AccountManager();
//        AccountManagerPortType port = service.getAccountManagerPortSoap11();
//        ((BindingProvider) port).getRequestContext().put(
//                BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
//                getProperty("AccountManagerService"));
       
        AccountMgrImpl acctMgr = new AccountMgrImpl();
        
        CreateAccountRequestType request = new CreateAccountRequestType();
        org.socraticgrid.common.account.Response response =
                new org.socraticgrid.common.account.Response();
        Name name = new Name();
        Address address = new Address();
        PhoneNumber phNo = new PhoneNumber();
        try {
            request.setUserName(userName);
            request.setPassword(password);
            name.setFirstName(firstName);
            name.setMiddleName(middleName);
            name.setLastName(lastName);
            request.setName(name);
            address.setAddress1(address1);
            address.setAddress2(address2);
            address.setCity(city);
            address.setState(state);
            address.setZipCode(postalCode);
            request.setAddress(address);
            phNo.setHomeNumber(homePhone);
            phNo.setMobileNumber(mobilePhone);
            phNo.setWorkNumber(workPhone);
            request.setPhoneNumber(phNo);
            request.setEmailAddress(emailAddress);
            request.setSsn(ssn);
            response = acctMgr.createAccount(request);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            response.setSuccess(false);
            response.setText(e.toString());
            getResponse().setStatus(Status.SUCCESS_OK);
            getResponse().setEntity(JSONHelper.getResponse(response).toString(),
                    MediaType.APPLICATION_JSON);
            e.printStackTrace();
        }

        return response;
    }

    private boolean isEmailValid(String emailAddress) {
        boolean isValid = false;
        //Initialize reg ex for email.
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = emailAddress;
        //Make the comparison case-insensitive.
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;

        //Initialize reg ex for phone number.
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isStateValid(String state) {
        boolean isValid = false;

        //Initialize reg ex for state.
        String expression = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)" ;
        CharSequence inputStr = state;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean isPostalCodeValid(String zipCode) {
        boolean isValid = false;

        //Initialize reg ex for zipcode.
        String expression = "\\d{5}(-\\d{4})?";
        CharSequence inputStr = zipCode;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private boolean getRequestParameter(Form form, String param) {
        if (null != form.getFirst(param)) {
            return true;
        }
        return false;
    }

    private void generateError(String errorMsg) {
        output.setSuccess(false);
        output.setText(errorMsg);
        getResponse().setStatus(Status.SUCCESS_OK);
        getResponse().setEntity(JSONHelper.getResponse(output).toString(), MediaType.APPLICATION_JSON);
        logger.log(Level.SEVERE, errorMsg);
    }

    private void setRequestValues(Form form) {

        if (getRequestParameter(form, "action")) {
            action = form.getFirst("action").getValue();
            logger.log(Level.INFO, "action: " + action);
        }
        if (getRequestParameter(form, "userName")) {
            userName = form.getFirst("userName").getValue();
            logger.log(Level.INFO, "userName: " + userName);
        }
        if (getRequestParameter(form, "password")) {
            password = form.getFirst("password").getValue();
            logger.log(Level.INFO, "password: " + password);
        }
        if (getRequestParameter(form, "firstName")) {
            firstName = form.getFirst("firstName").getValue();
            logger.log(Level.INFO, "firstName: " + firstName);
        }
        if (getRequestParameter(form, "middleName")) {
            middleName = form.getFirst("middleName").getValue();
            logger.log(Level.INFO, "middleName: " + middleName);
        }
        if (getRequestParameter(form, "lastName")) {
            lastName = form.getFirst("lastName").getValue();
            logger.log(Level.INFO, "lastName: " + lastName);
        }
        if (getRequestParameter(form, "ssn")) {
            ssn = form.getFirst("ssn").getValue();
            logger.log(Level.INFO, "ssn: " + ssn);
        }
        if (getRequestParameter(form, "address1")) {
            address1 = form.getFirst("address1").getValue();
            logger.log(Level.INFO, "address1: " + address1);
        }
        if (getRequestParameter(form, "address2")) {
            address2 = form.getFirst("address2").getValue();
            logger.log(Level.INFO, "address2: " + address2);
        }
        if (getRequestParameter(form, "city")) {
            city = form.getFirst("city").getValue();
            logger.log(Level.INFO, "city: " + city);
        }
        if (getRequestParameter(form, "state")) {
            state = form.getFirst("state").getValue();
            logger.log(Level.INFO, "state: " + state);
        }
        if (getRequestParameter(form, "postalCode")) {
            postalCode = form.getFirst("postalCode").getValue();
            logger.log(Level.INFO, "postalCode: " + postalCode);
        }
        if (getRequestParameter(form, "homePhone")) {
            homePhone = form.getFirst("homePhone").getValue();
            logger.log(Level.INFO, "homePhone: " + homePhone);
        }
        if (getRequestParameter(form, "mobilePhone")) {
            mobilePhone = form.getFirst("mobilePhone").getValue();
            logger.log(Level.INFO, "mobilePhone: " + mobilePhone);
        }
        if (getRequestParameter(form, "workPhone")) {
            workPhone = form.getFirst("workPhone").getValue();
            logger.log(Level.INFO, "workPhone: " + workPhone);
        }
        if (getRequestParameter(form, "emailAddress")) {
            emailAddress = form.getFirst("emailAddress").getValue();
            logger.log(Level.INFO, "emailAddress: " + emailAddress);
        }
    }
}
