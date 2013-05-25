
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

package org.socraticgrid.common.addrbook;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.hhs.fha.nhinc.common.addrbook package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetContactDetailsResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetContactDetailsResponse");
    private final static QName _GetContactDetailsRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetContactDetailsRequest");
    private final static QName _GetAllAddrRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetAllAddrRequest");
    private final static QName _GetSummariesResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "GetSummariesResponse");
    private final static QName _SearchAddrRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:addrbook", "SearchAddrRequest");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.hhs.fha.nhinc.common.addrbook
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetContactDetailsResponseType }
     * 
     */
    public GetContactDetailsResponseType createGetContactDetailsResponseType() {
        return new GetContactDetailsResponseType();
    }

    /**
     * Create an instance of {@link ContactDetails }
     * 
     */
    public ContactDetails createContactDetails() {
        return new ContactDetails();
    }

    /**
     * Create an instance of {@link SearchAddrRequestType }
     * 
     */
    public SearchAddrRequestType createSearchAddrRequestType() {
        return new SearchAddrRequestType();
    }

    /**
     * Create an instance of {@link GetAllAddrRequestType }
     * 
     */
    public GetAllAddrRequestType createGetAllAddrRequestType() {
        return new GetAllAddrRequestType();
    }

    /**
     * Create an instance of {@link GetSummariesResponseType }
     * 
     */
    public GetSummariesResponseType createGetSummariesResponseType() {
        return new GetSummariesResponseType();
    }

    /**
     * Create an instance of {@link GetContactDetailsRequestType }
     * 
     */
    public GetContactDetailsRequestType createGetContactDetailsRequestType() {
        return new GetContactDetailsRequestType();
    }

    /**
     * Create an instance of {@link ServiceError }
     * 
     */
    public ServiceError createServiceError() {
        return new ServiceError();
    }

    /**
     * Create an instance of {@link ContactSummary }
     * 
     */
    public ContactSummary createContactSummary() {
        return new ContactSummary();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactDetailsResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetContactDetailsResponse")
    public JAXBElement<GetContactDetailsResponseType> createGetContactDetailsResponse(GetContactDetailsResponseType value) {
        return new JAXBElement<GetContactDetailsResponseType>(_GetContactDetailsResponse_QNAME, GetContactDetailsResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetContactDetailsRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetContactDetailsRequest")
    public JAXBElement<GetContactDetailsRequestType> createGetContactDetailsRequest(GetContactDetailsRequestType value) {
        return new JAXBElement<GetContactDetailsRequestType>(_GetContactDetailsRequest_QNAME, GetContactDetailsRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllAddrRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetAllAddrRequest")
    public JAXBElement<GetAllAddrRequestType> createGetAllAddrRequest(GetAllAddrRequestType value) {
        return new JAXBElement<GetAllAddrRequestType>(_GetAllAddrRequest_QNAME, GetAllAddrRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummariesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "GetSummariesResponse")
    public JAXBElement<GetSummariesResponseType> createGetSummariesResponse(GetSummariesResponseType value) {
        return new JAXBElement<GetSummariesResponseType>(_GetSummariesResponse_QNAME, GetSummariesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SearchAddrRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:addrbook", name = "SearchAddrRequest")
    public JAXBElement<SearchAddrRequestType> createSearchAddrRequest(SearchAddrRequestType value) {
        return new JAXBElement<SearchAddrRequestType>(_SearchAddrRequest_QNAME, SearchAddrRequestType.class, null, value);
    }

}
