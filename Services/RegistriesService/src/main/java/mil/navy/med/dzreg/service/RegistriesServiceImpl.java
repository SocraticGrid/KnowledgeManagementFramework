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

package mil.navy.med.dzreg.service;

import mil.navy.med.dzreg.Constants;
import mil.navy.med.dzreg.dao.RegistriesManagerDAO;
import mil.navy.med.dzreg.types.AckType;
import mil.navy.med.dzreg.types.AddressType;
import mil.navy.med.dzreg.types.PersonRegistryProfileResponseType;
import mil.navy.med.dzreg.types.PersonRegistryProfileType;
import mil.navy.med.dzreg.types.PersonType;
import mil.navy.med.dzreg.types.RegisterPersonRequestType;
import mil.navy.med.dzreg.types.RegistryProfileType;
import mil.navy.med.dzreg.types.RegistryType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author kim
 */
public class RegistriesServiceImpl {

    private Log logger = LogFactory.getLog(RegistriesServiceOrig.class);

    public mil.navy.med.dzreg.types.PersonRegistryProfileResponseType getRegistryProfile(mil.navy.med.dzreg.types.PersonRegistryProfileRequestType request) {
        PersonRegistryProfileResponseType response = new PersonRegistryProfileResponseType();

        try {
            if (request != null) {
                // get input parameters
                long id = request.getId();
                PersonRegistryProfileType profile = RegistriesManagerDAO.getInstance().getRegistryProfile(id);
                response.setProfile(profile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public mil.navy.med.dzreg.types.AckType register(mil.navy.med.dzreg.types.RegisterPersonRequestType request) {
        AckType response = new AckType();

        try {
            if (request != null
                    && request.getPerson() != null
                    && request.getRegistry() != null && !request.getRegistry().isEmpty()) {
                if (logger.isDebugEnabled()) {
                    dumpRequest(request);
                }

                PersonRegistryProfileType profile = new PersonRegistryProfileType();
                profile.setPerson(request.getPerson());
                profile.setDataSource(request.getDataSource());

                RegistryProfileType regprofile = null;
                for (RegistryType regtype : request.getRegistry()) {
                    regprofile = new RegistryProfileType();
                    regprofile.setRegistryId(regtype.getRegistryId());
                    regprofile.setRegistryDesc(regtype.getRegistryDesc());
                    profile.getRegistry().add(regprofile);
                }

                if (logger.isDebugEnabled()) {
                    dump(profile);
                }

                response = RegistriesManagerDAO.getInstance().register(profile);
            } else {
                response.setResponseCode(Constants._APPLICATION_ERROR);
                response.setDetectedIssueText("Missing demographics information for person to be register.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        logger.debug("response=" + response.getResponseCode() + "|" + response.getDetectedIssueText());

        return response;
    }

    public mil.navy.med.dzreg.types.AckType unregister(mil.navy.med.dzreg.types.RegisterPersonRequestType request) {
        AckType response = new AckType();

        try {
            if (request != null && request.getPerson() != null) {
                if (logger.isDebugEnabled()) {
                    dumpRequest(request);
                }

                PersonRegistryProfileType profile = new PersonRegistryProfileType();
                profile.setPerson(request.getPerson());
                profile.setDataSource(request.getDataSource());

                RegistryProfileType regprofile = null;
                for (RegistryType regtype : request.getRegistry()) {
                    regprofile = new RegistryProfileType();
                    regprofile.setRegistryId(regtype.getRegistryId());
                    regprofile.setRegistryDesc(regtype.getRegistryDesc());
                    profile.getRegistry().add(regprofile);
                }

                if (logger.isDebugEnabled()) {
                    dump(profile);
                }

                response = RegistriesManagerDAO.getInstance().unregister(profile);
            } else {
                response.setResponseCode(Constants._APPLICATION_ERROR);
                response.setDetectedIssueText("Missing demographics information for person to be unregister.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("response=" + response.getResponseCode() + "|" + response.getDetectedIssueText());

        return response;
    }

    private void dump(PersonRegistryProfileType obj) {
        StringBuffer str = new StringBuffer();
        str.append("PersonRegistryProfileType[dataSource=" + obj.getDataSource());

        if (obj.getPerson() != null) {
            PersonType person = obj.getPerson();
            str.append("] person=[" + person.getId() + "|" + person.getName() + "|" + person.getDateOfBirth() + "|"
                    + person.getEligibilityIdentifier() + "|" + person.getHomePhone() + "|" + person.getOfficePhone() + "]");
            if (person.getAddress() != null) {
                AddressType address = person.getAddress().getValue();
                str.append(" address=[" + address.getStreetAddress() + "|" + address.getStreetAddress2() + "|" + address.getCity() + "|"
                        + address.getPostalCode() + "|" + address.getState() + "]");
            }
        }

        if (obj.getRegistry() != null && !obj.getRegistry().isEmpty()) {
            for (RegistryProfileType registry : obj.getRegistry()) {
                str.append(" registry=[" + registry.getRegistryId() + "|" + registry.getRegistryDesc() + "]");
            }
        }

        System.out.println(str.toString());
    }

    private void dumpRequest(RegisterPersonRequestType obj) {
        StringBuffer str = new StringBuffer();
        str.append("RegisterPersonRequestType[dataSource=" + obj.getDataSource());

        if (obj.getPerson() != null) {
            PersonType person = obj.getPerson();
            str.append("] person=[" + person.getId() + "|" + person.getName() + "|" + person.getDateOfBirth() + "|"
                    + person.getEligibilityIdentifier() + "|" + person.getHomePhone() + "|" + person.getOfficePhone() + "]");
            if (person.getAddress() != null) {
                AddressType address = person.getAddress().getValue();
                str.append(" address=[" + address.getStreetAddress() + "|" + address.getStreetAddress2() + "|" + address.getCity() + "|"
                        + address.getPostalCode() + "|" + address.getState() + "]");
            }
        }

        if (obj.getRegistry() != null && !obj.getRegistry().isEmpty()) {
            for (RegistryType registry : obj.getRegistry()) {
                str.append(" registry=[" + registry.getRegistryId() + "|" + registry.getRegistryDesc() + "]");
            }
        }

        System.out.println(str.toString());
    }
}
