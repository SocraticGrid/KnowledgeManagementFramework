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

package org.socraticgrid.ldapaccess;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Maps a set of attributes to a Contact object.
 *
 * @author cmatser
 */
public class ContactAttributeMapper implements AttributesMapper {

    public Object mapFromAttributes(Attributes attributes) throws NamingException {
        ContactDTO contactDTO = new ContactDTO();
        Attribute businessCategory = attributes.get("businessCategory");
        if(businessCategory != null)
            contactDTO.setBusinessCategory((String) businessCategory.get());
        Attribute carLicense = attributes.get("carLicense");
        if(carLicense != null)
            contactDTO.setCarLicense((String) carLicense.get());
        Attribute commonName = attributes.get("cn");
        if(commonName != null)
            contactDTO.setCommonName((String) commonName.get());
        Attribute departmentNumber = attributes.get("departmentNumber");
        if(departmentNumber != null)
            contactDTO.setDepartmentNumber((String) departmentNumber.get());
        Attribute description = attributes.get("description");
        if(description != null)
            contactDTO.setDescription((String) description.get());
        Attribute destinationIndicator = attributes.get("destinationIndicator");
        if(destinationIndicator != null)
            contactDTO.setDestinationIndicator((String) destinationIndicator.get());
        Attribute displayName = attributes.get("displayName");
        if(displayName != null)
            contactDTO.setDisplayName((String) displayName.get());
        Attribute employeeNumber = attributes.get("employeeNumber");
        if(employeeNumber != null)
            contactDTO.setEmployeeNumber((String) employeeNumber.get());
        Attribute employeeType = attributes.get("employeeType");
        if(employeeType != null)
            contactDTO.setEmployeeType((String) employeeType.get());
        Attribute facsimileTelephoneNumber = attributes.get("facsimileTelephoneNumber");
        if(facsimileTelephoneNumber != null)
            contactDTO.setFacsimileTelephoneNumber((String) facsimileTelephoneNumber.get());
        Attribute gender = attributes.get("gender");
        if(gender != null)
            contactDTO.setGender((String) gender.get());
        Attribute givenName = attributes.get("givenName");
        if(givenName != null)
            contactDTO.setGivenName((String) givenName.get());
        Attribute homePhone = attributes.get("homePhone");
        if(homePhone != null)
            contactDTO.setHomePhone((String) homePhone.get());
        Attribute homePostalAddress = attributes.get("homePostalAddress");
        if(homePostalAddress != null)
            contactDTO.setHomePostalAddress((String) homePostalAddress.get());
        Attribute initials = attributes.get("initials");
        if(initials != null)
            contactDTO.setInitials((String) initials.get());
        Attribute labeledURI = attributes.get("labeledURI");
        if(labeledURI != null)
            contactDTO.setLabeledURI((String) labeledURI.get());
        Attribute languages = attributes.get("languages");
        if(languages != null)
            contactDTO.setLanguages((String) languages.get());
        Attribute locality = attributes.get("l");
        if(locality != null)
            contactDTO.setLocality((String) locality.get());
        Attribute locationCode = attributes.get("locationCode");
        if(locationCode != null)
            contactDTO.setLocationCode((String) locationCode.get());
        Attribute mail = attributes.get("mail");
        if(mail != null)
            contactDTO.setMail((String) mail.get());
        Attribute manager = attributes.get("manager");
        if(manager != null)
            contactDTO.setManager((String) manager.get());
        Attribute mobile = attributes.get("mobile");
        if(mobile != null)
            contactDTO.setMobile((String) mobile.get());
        Attribute npi = attributes.get("npi");
        if(npi != null)
            contactDTO.setNpi((String) npi.get());
        Attribute organization = attributes.get("o");
        if(organization != null)
            contactDTO.setOrganization((String) organization.get());
        Attribute organizationalUnit = attributes.get("ou");
        if(organizationalUnit != null)
            contactDTO.setOrganizationalUnit((String) organizationalUnit.get());
        Attribute orgCode = attributes.get("orgCode");
        if(orgCode != null)
            contactDTO.setOrgCode((String) orgCode.get());
        Attribute pager = attributes.get("pager");
        if(pager != null)
            contactDTO.setPager((String) pager.get());
        Attribute jpegPhoto = attributes.get("jpegPhoto");
        if(jpegPhoto != null)
            contactDTO.setJpegPhoto((byte[]) jpegPhoto.get());
        Attribute postalAddress = attributes.get("postalAddress");
        if(postalAddress != null)
            contactDTO.setPostalAddress((String) postalAddress.get());
        Attribute postalCode = attributes.get("postalCode");
        if(postalCode != null)
            contactDTO.setPostalCode((String) postalCode.get());
        Attribute preferredContactMethod = attributes.get("preferredContactMethod");
        if(preferredContactMethod != null)
            contactDTO.setPreferredContactMethod((String) preferredContactMethod.get());
        Attribute preferredLanguage = attributes.get("preferredLanguage");
        if(preferredLanguage != null)
            contactDTO.setPreferredLanguage((String) preferredLanguage.get());
        Attribute providerTaxonomy = attributes.get("providerTaxonomy");
        if(providerTaxonomy != null)
            contactDTO.setProviderTaxonomy((String) providerTaxonomy.get());
        Attribute roomNumber = attributes.get("roomNumber");
        if(roomNumber != null)
            contactDTO.setRoomNumber((String) roomNumber.get());
        Attribute secretary = attributes.get("secretary");
        if(secretary != null)
            contactDTO.setSecretary((String) secretary.get());
        Attribute state = attributes.get("st");
        if(state != null)
            contactDTO.setState((String) state.get());
        Attribute street = attributes.get("street");
        if(street != null)
            contactDTO.setStreet((String) street.get());
        Attribute surname = attributes.get("surname");
        if(surname != null)
            contactDTO.setSurname((String) surname.get());
        Attribute telephoneNumber = attributes.get("telephoneNumber");
        if(telephoneNumber != null)
            contactDTO.setTelephoneNumber((String) telephoneNumber.get());
        Attribute title = attributes.get("title");
        if(title != null)
            contactDTO.setTitle((String) title.get());
        Attribute uid = attributes.get("uid");
        if(uid != null)
            contactDTO.setUid((String) uid.get());
        Attribute userCertificate = attributes.get("userCertificate");
        if(userCertificate != null)
            contactDTO.setUserCertificate((String) userCertificate.get());
        Attribute userPassword = attributes.get("userPassword");
        if(userPassword != null)
            contactDTO.setUserPassword((byte[]) userPassword.get());

        return contactDTO;
    }

}