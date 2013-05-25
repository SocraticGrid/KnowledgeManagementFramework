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
package org.socraticgrid.displaydataaggregator;

import org.socraticgrid.common.dda.GetMessagesResponseType;
import javax.jws.WebService;
import org.socraticgrid.common.dda.*;

/**
 *
 * @author nhin
 */
@WebService(serviceName = "DisplayDataAggregator", portName = "DisplayDataAggregatorPortSoap11", 
        endpointInterface = "org.socraticgrid.aggregator.DisplayDataAggregatorPortType",
        targetNamespace = "urn:org:socraticgrid:aggregator",
        wsdlLocation = "WEB-INF/wsdl/DisplayDataAggregator.wsdl")
public class DisplayDataAggregator {

    public UpdateInboxStatusResponseType updateInboxStatus(UpdateInboxStatusRequestType updateInboxStatusRequest) {
        return new DisplayDataAggregatorImpl().updateInboxStatus(updateInboxStatusRequest);
    }

    public GetAvailableSourcesResponseType getAvailableSources(GetAvailableSourcesRequestType getAvailableSourcesRequest) {
        return new DisplayDataAggregatorImpl().getAvailableSources(getAvailableSourcesRequest);
    }

    public GetDetailDataResponseType getDetailData(GetDetailDataRequestType getDetailDataRequest) {
        return new DisplayDataAggregatorImpl().getDetailData(getDetailDataRequest);
    }

    public GetSummaryDataResponseType getSummaryData(GetSummaryDataRequestType getSummaryDataRequest) {
        return new DisplayDataAggregatorImpl().getSummaryData(getSummaryDataRequest);
    }

    public GetDetailDataResponseType getDetailDataForUser(GetDetailDataForUserRequestType getDetailDataForUserRequest) {
        return new DisplayDataAggregatorImpl().getDetailDataForUser(getDetailDataForUserRequest);
    }

    public GetSummaryDataResponseType getSummaryDataForUser(GetSummaryDataForUserRequestType getSummaryDataForUserRequest) {
        return new DisplayDataAggregatorImpl().getSummaryDataForUser(getSummaryDataForUserRequest);
    }

    public SetMessageDataResponseType setMessage(SetMessageDataRequestType setMessageDataRequestType) {
        return new DisplayDataAggregatorImpl().setMessage(setMessageDataRequestType);
    }

    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType getMessageDetailRequest) {
        return new DisplayDataAggregatorImpl().getMessageDetail(getMessageDetailRequest);
    }

    public GetMessagesResponseType getMessages(GetMessagesRequestType getMessagesRequest) {
        GetMessagesResponseType responseType = new GetMessagesResponseType();
        try {
            responseType = new DisplayDataAggregatorImpl().getMessages(getMessagesRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseType;
    }

    public GetAddressBookResponseType getAddressBook(GetAddressBookRequestType request) {
        return new DisplayDataAggregatorImpl().getAddressBook(request);
    }

    @Deprecated
    public GetDirectoryAttributeResponseType getDirectoryAttribute(GetDirectoryAttributeRequestType request) {
        return new DisplayDataAggregatorImpl().getDirectoryAttribute(request);
    }
}
