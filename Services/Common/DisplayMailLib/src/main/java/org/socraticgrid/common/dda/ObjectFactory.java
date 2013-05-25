
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

package org.socraticgrid.common.dda;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the gov.hhs.fha.nhinc.common.dda package. 
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

    private final static QName _UpdateInboxStatusResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "UpdateInboxStatusResponse");
    private final static QName _SetMessageDataRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "SetMessageDataRequest");
    private final static QName _SetMessageRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "SetMessageRequest");
    private final static QName _GetSummaryDataForUserRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetSummaryDataForUserRequest");
    private final static QName _GetSummaryDataResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetSummaryDataResponse");
    private final static QName _GetMessageDetailResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetMessageDetailResponse");
    private final static QName _GetMessagesRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetMessagesRequest");
    private final static QName _GetAddressBookResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetAddressBookResponse");
    private final static QName _GetDetailDataForUserRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDetailDataForUserRequest");
    private final static QName _GetSummaryDataForUserResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetSummaryDataForUserResponse");
    private final static QName _GetDirectoryAttributeRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDirectoryAttributeRequest");
    private final static QName _UpdateComponentInboxStatusResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "UpdateComponentInboxStatusResponse");
    private final static QName _UpdateComponentInboxStatusRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "UpdateComponentInboxStatusRequest");
    private final static QName _SetMessageDataResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "SetMessageDataResponse");
    private final static QName _GetSummaryDataRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetSummaryDataRequest");
    private final static QName _GetDetailDataRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDetailDataRequest");
    private final static QName _GetMessagesResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetMessagesResponse");
    private final static QName _GetComponentSummaryDataForUserResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetComponentSummaryDataForUserResponse");
    private final static QName _GetComponentDetailDataForUserRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetComponentDetailDataForUserRequest");
    private final static QName _DeliverMessageRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "DeliverMessageRequest");
    private final static QName _GetDetailDataForUserResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDetailDataForUserResponse");
    private final static QName _GetComponentSummaryDataForUserRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetComponentSummaryDataForUserRequest");
    private final static QName _GetAvailableSourcesRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetAvailableSourcesRequest");
    private final static QName _GetDetailDataResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDetailDataResponse");
    private final static QName _DeliverMessageResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "DeliverMessageResponse");
    private final static QName _GetDataSourceNameResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDataSourceNameResponse");
    private final static QName _UpdateInboxStatusRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "UpdateInboxStatusRequest");
    private final static QName _GetComponentDetailDataForUserResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetComponentDetailDataForUserResponse");
    private final static QName _GetMessageDetailRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetMessageDetailRequest");
    private final static QName _GetAddressBookRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetAddressBookRequest");
    private final static QName _GetAvailableSourcesResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetAvailableSourcesResponse");
    private final static QName _SetMessageResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "SetMessageResponse");
    private final static QName _GetDataSourceNameRequest_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDataSourceNameRequest");
    private final static QName _GetDirectoryAttributeResponse_QNAME = new QName("urn:gov:hhs:fha:nhinc:common:dda", "GetDirectoryAttributeResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: gov.hhs.fha.nhinc.common.dda
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SetMessageRequestType }
     * 
     */
    public SetMessageRequestType createSetMessageRequestType() {
        return new SetMessageRequestType();
    }

    /**
     * Create an instance of {@link UpdateInboxStatusRequestType }
     * 
     */
    public UpdateInboxStatusRequestType createUpdateInboxStatusRequestType() {
        return new UpdateInboxStatusRequestType();
    }

    /**
     * Create an instance of {@link UpdateComponentInboxStatusResponseType }
     * 
     */
    public UpdateComponentInboxStatusResponseType createUpdateComponentInboxStatusResponseType() {
        return new UpdateComponentInboxStatusResponseType();
    }

    /**
     * Create an instance of {@link SetMessageDataResponseType }
     * 
     */
    public SetMessageDataResponseType createSetMessageDataResponseType() {
        return new SetMessageDataResponseType();
    }

    /**
     * Create an instance of {@link GetSummaryDataRequestType }
     * 
     */
    public GetSummaryDataRequestType createGetSummaryDataRequestType() {
        return new GetSummaryDataRequestType();
    }

    /**
     * Create an instance of {@link GetMessagesResponseType.GetMessageResponse }
     * 
     */
    public GetMessagesResponseType.GetMessageResponse createGetMessagesResponseTypeGetMessageResponse() {
        return new GetMessagesResponseType.GetMessageResponse();
    }

    /**
     * Create an instance of {@link SummaryData }
     * 
     */
    public SummaryData createSummaryData() {
        return new SummaryData();
    }

    /**
     * Create an instance of {@link UpdateInboxStatusResponseType }
     * 
     */
    public UpdateInboxStatusResponseType createUpdateInboxStatusResponseType() {
        return new UpdateInboxStatusResponseType();
    }

    /**
     * Create an instance of {@link GetMessagesRequestType }
     * 
     */
    public GetMessagesRequestType createGetMessagesRequestType() {
        return new GetMessagesRequestType();
    }

    /**
     * Create an instance of {@link GetDetailDataForUserRequestType }
     * 
     */
    public GetDetailDataForUserRequestType createGetDetailDataForUserRequestType() {
        return new GetDetailDataForUserRequestType();
    }

    /**
     * Create an instance of {@link DeliverMessageRequestType }
     * 
     */
    public DeliverMessageRequestType createDeliverMessageRequestType() {
        return new DeliverMessageRequestType();
    }

    /**
     * Create an instance of {@link GetAvailableSourcesResponseType }
     * 
     */
    public GetAvailableSourcesResponseType createGetAvailableSourcesResponseType() {
        return new GetAvailableSourcesResponseType();
    }

    /**
     * Create an instance of {@link GetAddressBookRequestType }
     * 
     */
    public GetAddressBookRequestType createGetAddressBookRequestType() {
        return new GetAddressBookRequestType();
    }

    /**
     * Create an instance of {@link GetDirectoryAttributeRequestType }
     * 
     */
    public GetDirectoryAttributeRequestType createGetDirectoryAttributeRequestType() {
        return new GetDirectoryAttributeRequestType();
    }

    /**
     * Create an instance of {@link GetDetailDataRequestType }
     * 
     */
    public GetDetailDataRequestType createGetDetailDataRequestType() {
        return new GetDetailDataRequestType();
    }

    /**
     * Create an instance of {@link GetDataSourceNameResponseType }
     * 
     */
    public GetDataSourceNameResponseType createGetDataSourceNameResponseType() {
        return new GetDataSourceNameResponseType();
    }

    /**
     * Create an instance of {@link GetDirectoryAttributeResponseType }
     * 
     */
    public GetDirectoryAttributeResponseType createGetDirectoryAttributeResponseType() {
        return new GetDirectoryAttributeResponseType();
    }

    /**
     * Create an instance of {@link GetAddressBookResponseType.GetAddressResponse }
     * 
     */
    public GetAddressBookResponseType.GetAddressResponse createGetAddressBookResponseTypeGetAddressResponse() {
        return new GetAddressBookResponseType.GetAddressResponse();
    }

    /**
     * Create an instance of {@link GetAvailableSourcesRequestType }
     * 
     */
    public GetAvailableSourcesRequestType createGetAvailableSourcesRequestType() {
        return new GetAvailableSourcesRequestType();
    }

    /**
     * Create an instance of {@link GetSummaryDataForUserRequestType }
     * 
     */
    public GetSummaryDataForUserRequestType createGetSummaryDataForUserRequestType() {
        return new GetSummaryDataForUserRequestType();
    }

    /**
     * Create an instance of {@link NameValuesPair }
     * 
     */
    public NameValuesPair createNameValuesPair() {
        return new NameValuesPair();
    }

    /**
     * Create an instance of {@link SetMessageDataRequestType }
     * 
     */
    public SetMessageDataRequestType createSetMessageDataRequestType() {
        return new SetMessageDataRequestType();
    }

    /**
     * Create an instance of {@link GetMessagesResponseType }
     * 
     */
    public GetMessagesResponseType createGetMessagesResponseType() {
        return new GetMessagesResponseType();
    }

    /**
     * Create an instance of {@link GetComponentDetailDataForUserRequestType }
     * 
     */
    public GetComponentDetailDataForUserRequestType createGetComponentDetailDataForUserRequestType() {
        return new GetComponentDetailDataForUserRequestType();
    }

    /**
     * Create an instance of {@link GetComponentSummaryDataForUserRequestType }
     * 
     */
    public GetComponentSummaryDataForUserRequestType createGetComponentSummaryDataForUserRequestType() {
        return new GetComponentSummaryDataForUserRequestType();
    }

    /**
     * Create an instance of {@link DetailData }
     * 
     */
    public DetailData createDetailData() {
        return new DetailData();
    }

    /**
     * Create an instance of {@link GetSummaryDataResponseType }
     * 
     */
    public GetSummaryDataResponseType createGetSummaryDataResponseType() {
        return new GetSummaryDataResponseType();
    }

    /**
     * Create an instance of {@link SetMessageResponseType }
     * 
     */
    public SetMessageResponseType createSetMessageResponseType() {
        return new SetMessageResponseType();
    }

    /**
     * Create an instance of {@link GetComponentDetailDataResponseType }
     * 
     */
    public GetComponentDetailDataResponseType createGetComponentDetailDataResponseType() {
        return new GetComponentDetailDataResponseType();
    }

    /**
     * Create an instance of {@link GetComponentSummaryDataResponseType }
     * 
     */
    public GetComponentSummaryDataResponseType createGetComponentSummaryDataResponseType() {
        return new GetComponentSummaryDataResponseType();
    }

    /**
     * Create an instance of {@link ServiceError }
     * 
     */
    public ServiceError createServiceError() {
        return new ServiceError();
    }

    /**
     * Create an instance of {@link MessageDataRequestType }
     * 
     */
    public MessageDataRequestType createMessageDataRequestType() {
        return new MessageDataRequestType();
    }

    /**
     * Create an instance of {@link DeliverMessageResponseType }
     * 
     */
    public DeliverMessageResponseType createDeliverMessageResponseType() {
        return new DeliverMessageResponseType();
    }

    /**
     * Create an instance of {@link GetMessageDetailRequestType }
     * 
     */
    public GetMessageDetailRequestType createGetMessageDetailRequestType() {
        return new GetMessageDetailRequestType();
    }

    /**
     * Create an instance of {@link GetAddressBookResponseType }
     * 
     */
    public GetAddressBookResponseType createGetAddressBookResponseType() {
        return new GetAddressBookResponseType();
    }

    /**
     * Create an instance of {@link UpdateComponentInboxStatusRequestType }
     * 
     */
    public UpdateComponentInboxStatusRequestType createUpdateComponentInboxStatusRequestType() {
        return new UpdateComponentInboxStatusRequestType();
    }

    /**
     * Create an instance of {@link GetMessageDetailResponseType }
     * 
     */
    public GetMessageDetailResponseType createGetMessageDetailResponseType() {
        return new GetMessageDetailResponseType();
    }

    /**
     * Create an instance of {@link GetDataSourceNameRequestType }
     * 
     */
    public GetDataSourceNameRequestType createGetDataSourceNameRequestType() {
        return new GetDataSourceNameRequestType();
    }

    /**
     * Create an instance of {@link GetDetailDataResponseType }
     * 
     */
    public GetDetailDataResponseType createGetDetailDataResponseType() {
        return new GetDetailDataResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateInboxStatusResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "UpdateInboxStatusResponse")
    public JAXBElement<UpdateInboxStatusResponseType> createUpdateInboxStatusResponse(UpdateInboxStatusResponseType value) {
        return new JAXBElement<UpdateInboxStatusResponseType>(_UpdateInboxStatusResponse_QNAME, UpdateInboxStatusResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageDataRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "SetMessageDataRequest")
    public JAXBElement<SetMessageDataRequestType> createSetMessageDataRequest(SetMessageDataRequestType value) {
        return new JAXBElement<SetMessageDataRequestType>(_SetMessageDataRequest_QNAME, SetMessageDataRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "SetMessageRequest")
    public JAXBElement<SetMessageRequestType> createSetMessageRequest(SetMessageRequestType value) {
        return new JAXBElement<SetMessageRequestType>(_SetMessageRequest_QNAME, SetMessageRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummaryDataForUserRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetSummaryDataForUserRequest")
    public JAXBElement<GetSummaryDataForUserRequestType> createGetSummaryDataForUserRequest(GetSummaryDataForUserRequestType value) {
        return new JAXBElement<GetSummaryDataForUserRequestType>(_GetSummaryDataForUserRequest_QNAME, GetSummaryDataForUserRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummaryDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetSummaryDataResponse")
    public JAXBElement<GetSummaryDataResponseType> createGetSummaryDataResponse(GetSummaryDataResponseType value) {
        return new JAXBElement<GetSummaryDataResponseType>(_GetSummaryDataResponse_QNAME, GetSummaryDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageDetailResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetMessageDetailResponse")
    public JAXBElement<GetMessageDetailResponseType> createGetMessageDetailResponse(GetMessageDetailResponseType value) {
        return new JAXBElement<GetMessageDetailResponseType>(_GetMessageDetailResponse_QNAME, GetMessageDetailResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetMessagesRequest")
    public JAXBElement<GetMessagesRequestType> createGetMessagesRequest(GetMessagesRequestType value) {
        return new JAXBElement<GetMessagesRequestType>(_GetMessagesRequest_QNAME, GetMessagesRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddressBookResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetAddressBookResponse")
    public JAXBElement<GetAddressBookResponseType> createGetAddressBookResponse(GetAddressBookResponseType value) {
        return new JAXBElement<GetAddressBookResponseType>(_GetAddressBookResponse_QNAME, GetAddressBookResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailDataForUserRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDetailDataForUserRequest")
    public JAXBElement<GetDetailDataForUserRequestType> createGetDetailDataForUserRequest(GetDetailDataForUserRequestType value) {
        return new JAXBElement<GetDetailDataForUserRequestType>(_GetDetailDataForUserRequest_QNAME, GetDetailDataForUserRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummaryDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetSummaryDataForUserResponse")
    public JAXBElement<GetSummaryDataResponseType> createGetSummaryDataForUserResponse(GetSummaryDataResponseType value) {
        return new JAXBElement<GetSummaryDataResponseType>(_GetSummaryDataForUserResponse_QNAME, GetSummaryDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectoryAttributeRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDirectoryAttributeRequest")
    public JAXBElement<GetDirectoryAttributeRequestType> createGetDirectoryAttributeRequest(GetDirectoryAttributeRequestType value) {
        return new JAXBElement<GetDirectoryAttributeRequestType>(_GetDirectoryAttributeRequest_QNAME, GetDirectoryAttributeRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateComponentInboxStatusResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "UpdateComponentInboxStatusResponse")
    public JAXBElement<UpdateComponentInboxStatusResponseType> createUpdateComponentInboxStatusResponse(UpdateComponentInboxStatusResponseType value) {
        return new JAXBElement<UpdateComponentInboxStatusResponseType>(_UpdateComponentInboxStatusResponse_QNAME, UpdateComponentInboxStatusResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateComponentInboxStatusRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "UpdateComponentInboxStatusRequest")
    public JAXBElement<UpdateComponentInboxStatusRequestType> createUpdateComponentInboxStatusRequest(UpdateComponentInboxStatusRequestType value) {
        return new JAXBElement<UpdateComponentInboxStatusRequestType>(_UpdateComponentInboxStatusRequest_QNAME, UpdateComponentInboxStatusRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "SetMessageDataResponse")
    public JAXBElement<SetMessageDataResponseType> createSetMessageDataResponse(SetMessageDataResponseType value) {
        return new JAXBElement<SetMessageDataResponseType>(_SetMessageDataResponse_QNAME, SetMessageDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSummaryDataRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetSummaryDataRequest")
    public JAXBElement<GetSummaryDataRequestType> createGetSummaryDataRequest(GetSummaryDataRequestType value) {
        return new JAXBElement<GetSummaryDataRequestType>(_GetSummaryDataRequest_QNAME, GetSummaryDataRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailDataRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDetailDataRequest")
    public JAXBElement<GetDetailDataRequestType> createGetDetailDataRequest(GetDetailDataRequestType value) {
        return new JAXBElement<GetDetailDataRequestType>(_GetDetailDataRequest_QNAME, GetDetailDataRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessagesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetMessagesResponse")
    public JAXBElement<GetMessagesResponseType> createGetMessagesResponse(GetMessagesResponseType value) {
        return new JAXBElement<GetMessagesResponseType>(_GetMessagesResponse_QNAME, GetMessagesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentSummaryDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetComponentSummaryDataForUserResponse")
    public JAXBElement<GetComponentSummaryDataResponseType> createGetComponentSummaryDataForUserResponse(GetComponentSummaryDataResponseType value) {
        return new JAXBElement<GetComponentSummaryDataResponseType>(_GetComponentSummaryDataForUserResponse_QNAME, GetComponentSummaryDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentDetailDataForUserRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetComponentDetailDataForUserRequest")
    public JAXBElement<GetComponentDetailDataForUserRequestType> createGetComponentDetailDataForUserRequest(GetComponentDetailDataForUserRequestType value) {
        return new JAXBElement<GetComponentDetailDataForUserRequestType>(_GetComponentDetailDataForUserRequest_QNAME, GetComponentDetailDataForUserRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeliverMessageRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "DeliverMessageRequest")
    public JAXBElement<DeliverMessageRequestType> createDeliverMessageRequest(DeliverMessageRequestType value) {
        return new JAXBElement<DeliverMessageRequestType>(_DeliverMessageRequest_QNAME, DeliverMessageRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDetailDataForUserResponse")
    public JAXBElement<GetDetailDataResponseType> createGetDetailDataForUserResponse(GetDetailDataResponseType value) {
        return new JAXBElement<GetDetailDataResponseType>(_GetDetailDataForUserResponse_QNAME, GetDetailDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentSummaryDataForUserRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetComponentSummaryDataForUserRequest")
    public JAXBElement<GetComponentSummaryDataForUserRequestType> createGetComponentSummaryDataForUserRequest(GetComponentSummaryDataForUserRequestType value) {
        return new JAXBElement<GetComponentSummaryDataForUserRequestType>(_GetComponentSummaryDataForUserRequest_QNAME, GetComponentSummaryDataForUserRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableSourcesRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetAvailableSourcesRequest")
    public JAXBElement<GetAvailableSourcesRequestType> createGetAvailableSourcesRequest(GetAvailableSourcesRequestType value) {
        return new JAXBElement<GetAvailableSourcesRequestType>(_GetAvailableSourcesRequest_QNAME, GetAvailableSourcesRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDetailDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDetailDataResponse")
    public JAXBElement<GetDetailDataResponseType> createGetDetailDataResponse(GetDetailDataResponseType value) {
        return new JAXBElement<GetDetailDataResponseType>(_GetDetailDataResponse_QNAME, GetDetailDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeliverMessageResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "DeliverMessageResponse")
    public JAXBElement<DeliverMessageResponseType> createDeliverMessageResponse(DeliverMessageResponseType value) {
        return new JAXBElement<DeliverMessageResponseType>(_DeliverMessageResponse_QNAME, DeliverMessageResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataSourceNameResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDataSourceNameResponse")
    public JAXBElement<GetDataSourceNameResponseType> createGetDataSourceNameResponse(GetDataSourceNameResponseType value) {
        return new JAXBElement<GetDataSourceNameResponseType>(_GetDataSourceNameResponse_QNAME, GetDataSourceNameResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UpdateInboxStatusRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "UpdateInboxStatusRequest")
    public JAXBElement<UpdateInboxStatusRequestType> createUpdateInboxStatusRequest(UpdateInboxStatusRequestType value) {
        return new JAXBElement<UpdateInboxStatusRequestType>(_UpdateInboxStatusRequest_QNAME, UpdateInboxStatusRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetComponentDetailDataResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetComponentDetailDataForUserResponse")
    public JAXBElement<GetComponentDetailDataResponseType> createGetComponentDetailDataForUserResponse(GetComponentDetailDataResponseType value) {
        return new JAXBElement<GetComponentDetailDataResponseType>(_GetComponentDetailDataForUserResponse_QNAME, GetComponentDetailDataResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageDetailRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetMessageDetailRequest")
    public JAXBElement<GetMessageDetailRequestType> createGetMessageDetailRequest(GetMessageDetailRequestType value) {
        return new JAXBElement<GetMessageDetailRequestType>(_GetMessageDetailRequest_QNAME, GetMessageDetailRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAddressBookRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetAddressBookRequest")
    public JAXBElement<GetAddressBookRequestType> createGetAddressBookRequest(GetAddressBookRequestType value) {
        return new JAXBElement<GetAddressBookRequestType>(_GetAddressBookRequest_QNAME, GetAddressBookRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAvailableSourcesResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetAvailableSourcesResponse")
    public JAXBElement<GetAvailableSourcesResponseType> createGetAvailableSourcesResponse(GetAvailableSourcesResponseType value) {
        return new JAXBElement<GetAvailableSourcesResponseType>(_GetAvailableSourcesResponse_QNAME, GetAvailableSourcesResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SetMessageResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "SetMessageResponse")
    public JAXBElement<SetMessageResponseType> createSetMessageResponse(SetMessageResponseType value) {
        return new JAXBElement<SetMessageResponseType>(_SetMessageResponse_QNAME, SetMessageResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDataSourceNameRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDataSourceNameRequest")
    public JAXBElement<GetDataSourceNameRequestType> createGetDataSourceNameRequest(GetDataSourceNameRequestType value) {
        return new JAXBElement<GetDataSourceNameRequestType>(_GetDataSourceNameRequest_QNAME, GetDataSourceNameRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetDirectoryAttributeResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:gov:hhs:fha:nhinc:common:dda", name = "GetDirectoryAttributeResponse")
    public JAXBElement<GetDirectoryAttributeResponseType> createGetDirectoryAttributeResponse(GetDirectoryAttributeResponseType value) {
        return new JAXBElement<GetDirectoryAttributeResponseType>(_GetDirectoryAttributeResponse_QNAME, GetDirectoryAttributeResponseType.class, null, value);
    }

}
