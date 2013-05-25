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
package org.socraticgrid.docmgr;

import com.thoughtworks.xstream.XStream;
import org.socraticgrid.account.util.PropertyAccessor;
import org.socraticgrid.docmgr.msgobject.NHINRetrieveMessage;
import org.socraticgrid.docmgr.msgobject.NHINQueryMessage;
import org.socraticgrid.common.docmgr.DocDownloadInfoType;
import org.socraticgrid.common.docmgr.DocumentInfoType;
import org.socraticgrid.common.docmgr.NameValuesPair;
import org.socraticgrid.common.nhinccommon.AssertionType;
import org.socraticgrid.common.nhinccommon.CeType;
import org.socraticgrid.common.nhinccommon.HomeCommunityType;
import org.socraticgrid.common.nhinccommon.PersonNameType;
import org.socraticgrid.common.nhinccommon.SamlAuthnStatementType;
import org.socraticgrid.common.nhinccommon.SamlAuthzDecisionStatementEvidenceAssertionType;
import org.socraticgrid.common.nhinccommon.SamlAuthzDecisionStatementEvidenceType;
import org.socraticgrid.common.nhinccommon.SamlAuthzDecisionStatementType;
import org.socraticgrid.common.nhinccommon.SamlAuthzDecisionStatementEvidenceConditionsType;
import org.socraticgrid.common.nhinccommon.UserType;
import org.socraticgrid.common.nhinccommonentity.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles the initial NHIN Query.  Steps:
 * <ol>
 *   <li>Perform the gateway query
 *   <li>Checks response for documents that haven't already been downloaded
 *   <li>Starts the download process of new documents
 *   <li>Informs requestor that query is done.
 * </ol>
 * @author cmatser
 */
public class NHINQueryProcessor {

    /** Assertion values (where to set?). */
    public static final String SIGNATURE_DATE = "01/01/2009 01:22:30";
    public static final String EXPIRATION_DATE = "01/01/2014 01:22:30";
    public static final String ROLE_NAME = "Physician";
    public static final String ROLE_CODE = "5555555";
    public static final String ROLE_CODE_SYSTEM = "2.16.840.1.113883.6.96";
    public static final String ROLE_CODE_SYSTEM_NAME = "SNOMED_CT";
    public static final String ROLE_CODE_SYSTEM_VERSION = "1.0";
    public static final String USER_DOD_EXTENSION = "*DoD";
    public static final String DOD_ROLE_NAME = "P";
    public static final String DOD_ROLE_CODE = "P";
    public static final String DOD_ROLE_CODE_SYSTEM = "30";
    public static final String DOD_ROLE_CODE_SYSTEM_NAME = "nameType";
    public static final String DOD_ROLE_CODE_SYSTEM_VERSION = "1.0";
    public static final String PURPOSE_OF_USE_ROLE_NAME = "TREATMENT";
    public static final String PURPOSE_OF_USE_ROLE_CODE = "TREATMENT";
    public static final String PURPOSE_OF_USE_ROLE_CODE_SYSTEM = "2.16.840.1.113883.3.18.71.1";
    public static final String PURPOSE_OF_USE_ROLE_CODE_SYSTEM_NAME = "nhin-purpose";
    public static final String PURPOSE_OF_USE_ROLE_CODE_SYSTEM_VERSION = "1.0";
    public static final String CLAIM_FORM_REF = "DoD-Ref-Claim";
    public static final String CLAIM_FORM_STRING = "blahblah";

    /** Date precision. */
    public static final int XDS_DATE_QUERY_FROM_PRECISION = 8;
    public static final int XDS_DATE_QUERY_TO_PRECISION = 14;
    public static final int ASSERTION_DOB_PRECISION = 12;

    /** XDS spec does not use timezone (but CONNECT does) */
    public static final String XDS_DATE_FORMAT_FULL = "yyyyMMddHHmmssZ";

    /** Search criteria. */
    public static final int NUMBER_OF_YEARS = 5;

    /** Data Source name. */
    public static final String DATA_SOURCE = "NHIN Documents";

    /** Item names for name value pairs. */
    public static final String ITEM_DOCUMENT_NAME = "Name";
    public static final String ITEM_MIME_TYPE = "MIME Type";
    public static final String ITEM_DOCUMENT_SIZE = "Size";
    public static final String ITEM_INSTITUTION = "Author Institution";
    public static final String ITEM_CREATION_TIME = "Creation Time";
    public static final String ITEM_LANGUAGE_CODE = "Language Code";
    public static final String ITEM_SERVICE_START = "Service Start Time";
    public static final String ITEM_SERVICE_STOP = "Service Stop Time";
    public static final String ITEM_PATIENT_ADDRESS = "Patient Address";
    public static final String ITEM_PATIENT_GENDER = "Patient Gender";
    public static final String ITEM_PATIENT_DOB = "Patient DOB";
    public static final String ITEM_REPOSITORY_ID = "Repository ID";
    public static final String ITEM_DOCUMENT_UNIQUE_ID = "Document Unique ID";
    public static final String ITEM_HOME_COMMUNITY_ID = "Home Community ID";

    /** XDS ids */
    public static final String XDS_FINDDOC_QUERY = "urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d";
    public static final String XDS_GETDOC_QUERY = "urn:uuid:5c4f972b-d56b-40ac-a5fc-c8ca9b40b9d4";
    public static final String XDS_QUERY_PATIENT_ID = "$XDSDocumentEntryPatientId";
    public static final String XDS_QUERY_ENTRY_STATUS = "$XDSDocumentEntryStatus";
    public static final String XDS_QUERY_APPROVED_STATUS = "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')";
    public static final String XDS_QUERY_DOCUMENT_ID = "$XDSDocumentEntryUniqueId";
    public static final String XDS_RETURN_TYPE = "LeafClass";
    public static final String XDS_CUSTOM_METADATA_PREFIX = "urn:";
    public static final String XDS_CREATION_TIME = "creationTime";
    public static final String XDS_SERVICE_START = "serviceStartTime";
    public static final String XDS_SERVICE_STOP = "serviceStopTime";
    public static final String XDS_LANGUAGE_CODE = "languageCode";
    public static final String XDS_PATIENT_INFO = "sourcePatientInfo";
    public static final String XDS_DOC_SIZE = "size";
    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_PATIENT_NAME = "PID-5|";
    public static final String XDS_PATIENT_GENDER = "PID-8|";
    public static final String XDS_PATIENT_DOB = "PID-7|";
    public static final String XDS_PATIENT_ADDRESS = "PID-11|";
    public static final String XDS_DOC_ID = "XDSDocumentEntry.uniqueId";
    public static final String XDS_CLASS_AUTHOR = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String XDS_SLOT_AUTHOR = "authorPerson";
    public static final String XDS_SLOT_INSTITUTION = "authorInstitution";

    /** Logging. */
    private static Log log = LogFactory.getLog(NHINQueryProcessor.class);

    public NHINQueryProcessor() { }

    public void handleQueryMessage(String ticket, NHINQueryMessage msg) {

        try { // Call Web Service Operation
            List<WebServiceFeature> wsfeatures = new ArrayList<WebServiceFeature>();
            wsfeatures.add(new MTOMFeature(0));
            WebServiceFeature[] wsfeaturearray = wsfeatures.toArray(new WebServiceFeature[0]);

            javax.xml.ws.Service myService = new ServiceUtil().createService(
                "EntityDocQuery.wsdl",
                "urn:org:socraticgrid:entitydocquery",
                "EntityDocQuery");
            org.socraticgrid.entitydocquery.EntityDocQueryPortType port = myService.getPort(
                new javax.xml.namespace.QName("urn:gov:hhs:fha:nhinc:entitydocquery", "EntityDocQueryPortSoap"),
                org.socraticgrid.entitydocquery.EntityDocQueryPortType.class);
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                PropertyAccessor.getProperty(
                    DocumentManagerImpl.REPOSITORY_PROPERTY_FILE,
                    DocumentManagerImpl.NHINDOCQUERY_ENDPOINT_PROP));
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(
                "com.sun.xml.ws.request.timeout", 3 * 60 * 1000);
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(
                "com.sun.xml.internal.ws.request.timeout", 3 * 60 * 1000);

            RespondingGatewayCrossGatewayQueryRequestType gatewayQueryRequest = new RespondingGatewayCrossGatewayQueryRequestType();
            AdhocQueryRequest adhocQueryRequest = createQuery(msg);
            AssertionType assertion = createAssertion(msg);
            gatewayQueryRequest.setAdhocQueryRequest(adhocQueryRequest);
            gatewayQueryRequest.setAssertion(assertion);

            AdhocQueryResponse result = port.respondingGatewayCrossGatewayQuery(gatewayQueryRequest);

            //Compare gateway results with local repository
            List<NewDocInfo> newDocs = findNewDocuments(result, adhocQueryRequest);

            //Start async retrieve of new documents
            for (NewDocInfo newDoc : newDocs) {
                startRetrieve(newDoc, assertion,
                        msg.getPatientUnitNumber(),
                        msg.getHomeCommunityId(),
                        PropertyAccessor.getProperty(
                            DocumentManagerImpl.REPOSITORY_PROPERTY_FILE,
                            DocumentManagerImpl.INBOUND_DOCUMENT_REPOSITORY_ID_PROP),
                        new DocumentManagerImpl().generateUniqueId(null).getUniqueId(),
                        msg.getCallbackURL());
            }

            //Notify caller of status
            doCallback(true, "Success", ticket, msg.getCallbackURL(), newDocs);
        } catch (Exception e) {
            //Handle custom exceptions here
            log.error("Error handling NHIN query message.", e);

            //Notify caller of status
            doCallback(false, e.getMessage(), ticket, msg.getCallbackURL(), new LinkedList<NewDocInfo>());
        }

    }

    /**
     * Compare the results from the gateway query to determine which documents
     * are not in the local store.
     * 
     * @param gatewayResult
     * @param adhocQueryRequest
     * @return
     */
    private List<NewDocInfo> findNewDocuments(
            AdhocQueryResponse gatewayResult,
            AdhocQueryRequest adhocQueryRequest) {
        List<NewDocInfo> newDocs = new LinkedList<NewDocInfo>();

        //if no gateway documents, return
        if (gatewayResult.getRegistryObjectList() == null) {
            log.debug ("NHIN Query done, no objects returned.");
            return newDocs;
        }

        AdhocQueryResponse localResult = getCurrentDocuments(adhocQueryRequest);

        //Loop through gateway results and check if they are there locally
        List<JAXBElement<? extends IdentifiableType>> gatewayObjects = gatewayResult.getRegistryObjectList().getIdentifiable();
        for (JAXBElement<? extends IdentifiableType> gatewayObj : gatewayObjects) {
            IdentifiableType gatewayDoc = gatewayObj.getValue();
            String homeId = "";
            String reposId = "";
            String docId = "";
            String origHomeId = null;
            String origReposId = null;
            String origDocId = null;
            boolean found = false;

            if (gatewayDoc instanceof ExtrinsicObjectType) {
                ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) gatewayDoc;
                homeId = extrinsic.getHome();
                for (SlotType1 returnSlot : extrinsic.getSlot()) {
                    if ("repositoryUniqueId".equals(returnSlot.getName())) {
                        reposId = returnSlot.getValueList().getValue().get(0);
                    }
                }
                for (ExternalIdentifierType returnId : extrinsic.getExternalIdentifier()) {
                    if ("XDSDocumentEntry.uniqueId".equals(returnId.getName().getLocalizedString().get(0).getValue())) {
                        docId = returnId.getValue();
                    }
                }
            } //if extract gateway values

            //Loop through current results and check if the gateway doc already exists
            List<JAXBElement<? extends IdentifiableType>> localObjects = localResult.getRegistryObjectList().getIdentifiable();
            for (JAXBElement<? extends IdentifiableType> localObj: localObjects) {
                IdentifiableType localDoc = localObj.getValue();
                if (localDoc instanceof ExtrinsicObjectType) {
                    ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) localDoc;
                    for (SlotType1 returnSlot : extrinsic.getSlot()) {
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigHomeCommunityId".equals(returnSlot.getName())) {
                            origHomeId = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigRepositoryUniqueId".equals(returnSlot.getName())) {
                            origReposId = returnSlot.getValueList().getValue().get(0);
                        }
                        if ("urn:gov:hhs:fha:nhinc:xds:OrigDocumentUniqueId".equals(returnSlot.getName())) {
                            origDocId = returnSlot.getValueList().getValue().get(0);
                        }
                    } //for slot loop

                } //if extract local values

                //Check if gateway document matches local document
                if (homeId.equals(origHomeId)
                        && reposId.equals(origReposId)
                        && docId.equals(origDocId)) {
                    found = true;
                    log.debug("NHIN query returned existing document: " + docId
                        + ", in repository: " + reposId);
                    break;
                }

            } //for loop through local results

            //If the document doesn't exist locally, save the info
            if (!found) {
                NewDocInfo newDoc = new NewDocInfo();
                newDoc.doc = new DocDownloadInfoType();
                newDoc.doc.setDocInfo(parseMetadata((ExtrinsicObjectType) gatewayDoc));
                newDoc.extrinsic = (ExtrinsicObjectType) gatewayDoc;
                newDocs.add(newDoc);
            }

        } //for loop through gateway results

        return newDocs;
    }

    /**
     * Find the existing documents in the local store.
     * 
     * @param query
     */
    private AdhocQueryResponse getCurrentDocuments(AdhocQueryRequest query) {
        return new DocumentManagerImpl().documentManagerQueryForDocument(query);
    }

    /**
     * Kickoff the async retrieval of new document.
     *
     * @param newDoc
     */
    private void startRetrieve(
            NewDocInfo newDoc,
            AssertionType assertion,
            String localPatientId,
            String localHomeCommunityId,
            String localRepositoryId,
            String localDocumentUniqueId,
            String callbackURL) {

        //Create message
        NHINRetrieveMessage nhinMessage = new NHINRetrieveMessage();
        nhinMessage.setCallbackURL(callbackURL);

        //Serialize un-serializable objects to xml
        XStream xstream = new XStream();
        xstream.alias("DocumentInfoType", DocumentInfoType.class);
        xstream.alias("ExtrinsicObjectType", ExtrinsicObjectType.class);
        xstream.alias("AssertionType", AssertionType.class);
        nhinMessage.setDocumentInfoXML(xstream.toXML(newDoc.doc.getDocInfo()));
        nhinMessage.setExtrinsicXML(xstream.toXML(newDoc.extrinsic));
        nhinMessage.setAssertionXML(xstream.toXML(assertion));

        //Set remote values
        nhinMessage.setRemoteDocumentUniqueId(newDoc.doc.getDocInfo().getItemId());
        for (NameValuesPair pair : newDoc.doc.getDocInfo().getItemValues()) {
            if (ITEM_HOME_COMMUNITY_ID.equals(pair.getName())) {
                nhinMessage.setRemoteHomeCommunityId(pair.getValues().get(0));
            }
            if (ITEM_REPOSITORY_ID.equals(pair.getName())) {
                nhinMessage.setRemoteRepositoryId(pair.getValues().get(0));
            }
        }

        //Set local values
        nhinMessage.setLocalHomeCommunityId(localHomeCommunityId);
        nhinMessage.setLocalRepositoryId(localRepositoryId);
        nhinMessage.setLocalDocumentUniqueId(localDocumentUniqueId);
        nhinMessage.setLocalPatientId(localPatientId);

        //Start in background
        String[] startResult = DocumentManagerImpl.startBackgroundJob(nhinMessage);

        //Set response info
        newDoc.doc.setTicket(startResult[0]);
    }

    /**
     * Notify requestor of the completion of the gateway query.
     *
     * @param success
     * @param detail
     * @param ticket
     * @param requester
     * @param newDocs
     */
    private void doCallback(boolean success, String detail, String ticket, String requester, List<NewDocInfo> newDocs) {
        
        try { // Call Web Service Operation
            org.socraticgrid.docmgrrequester.DocMgrRequester service = new org.socraticgrid.docmgrrequester.DocMgrRequester();
            org.socraticgrid.docmgrrequester.DocMgrRequesterPortType port = service.getDocMgrRequesterPortSoap11();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                requester);

            //Initialize WS operation arguments here
            org.socraticgrid.common.docmgr.QueryDoneRequestType queryDoneRequest = new org.socraticgrid.common.docmgr.QueryDoneRequestType();
            queryDoneRequest.setTicket(ticket);
            queryDoneRequest.setSuccess(success);
            queryDoneRequest.setDetail(detail);
            for (NewDocInfo newDoc : newDocs) {
                queryDoneRequest.getNewDocs().add(newDoc.doc);
            }

            //Process result here
            org.socraticgrid.common.docmgr.QueryDoneResponseType result = port.queryDone(queryDoneRequest);
        } catch (Exception e) {
            //Handle custom exceptions here
            log.error("Error notifying requestor ticket complete: " + ticket, e);
        }

    }

    /**
     * Create the nhin query for inbound repository based on the passed parameters and some fixed values.
     * 
     * @param msg
     * @return
     */
    private AdhocQueryRequest createQuery(NHINQueryMessage msg) {
        AdhocQueryRequest retVal = new AdhocQueryRequest();
        retVal.setFederated(false);
        retVal.setStartIndex(BigInteger.valueOf(0));
        retVal.setMaxResults(BigInteger.valueOf(-1));

        ResponseOptionType resp = new ResponseOptionType();
        resp.setReturnComposedObjects(true);
        resp.setReturnType("LeafClass");
        retVal.setResponseOption(resp);

        AdhocQueryType queryType = new AdhocQueryType();
        queryType.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

        String inboundRepos = "";
        try {
            inboundRepos = PropertyAccessor.getProperty(
                DocumentManagerImpl.REPOSITORY_PROPERTY_FILE,
                DocumentManagerImpl.INBOUND_DOCUMENT_REPOSITORY_ID_PROP);
        }
        catch (Exception e) {
            //ignore
        }
        addSlot(queryType, "$XDSRepositoryUniqueId",
            new String[] { inboundRepos } );

        addSlot(queryType, "$XDSDocumentEntryPatientId",
            new String[] { msg.getPatientUnitNumber() + "^^^&" + msg.getHomeCommunityId() + "&ISO" } );

        addSlot(queryType, "$XDSDocumentEntryStatus",
            new String[] { "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')" });

        //Set Creation From Date
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.YEAR, -NUMBER_OF_YEARS);
        addSlot(queryType, "$XDSDocumentEntryCreationTimeFrom",
            new String[] { formatXDSDate(cal.getTime(), XDS_DATE_QUERY_FROM_PRECISION) });

        // Omit Creation To Date for most current
        //addSlot(queryType, "$XDSDocumentEntryCreationTimeTo",
        //    new String[] { XDSDateQueryToFormat.format(new Date()) });

        retVal.setAdhocQuery(queryType);

        return retVal;
    }

    /**
     * Create gateway assertion object based on the passed parameters and some fixed values.
     * 
     * @param msg
     * @return
     */
    private AssertionType createAssertion(NHINQueryMessage msg) {
        AssertionType assertion = new AssertionType();
        assertion.setDateOfBirth(formatXDSDate(msg.getPatientDOB(), ASSERTION_DOB_PRECISION));

        //SAMLAuthStatementType is new for CONNECT v3.1
        SamlAuthnStatementType samlAuthnStatement = new SamlAuthnStatementType();
        SamlAuthzDecisionStatementType samlAuthzDecisionStatement = new SamlAuthzDecisionStatementType();
        SamlAuthzDecisionStatementEvidenceType samlAuthzDecisionStatementEvidence = new SamlAuthzDecisionStatementEvidenceType();
        SamlAuthzDecisionStatementEvidenceAssertionType samlAuthzDecisionStatementAssertion = new SamlAuthzDecisionStatementEvidenceAssertionType();
        SamlAuthzDecisionStatementEvidenceConditionsType samlAuthzDecisionStatementEvidenceConditions = new SamlAuthzDecisionStatementEvidenceConditionsType();
        samlAuthzDecisionStatementEvidenceConditions.setNotOnOrAfter(EXPIRATION_DATE);
        samlAuthzDecisionStatementEvidenceConditions.setNotBefore(SIGNATURE_DATE);
        samlAuthzDecisionStatementAssertion.setConditions(samlAuthzDecisionStatementEvidenceConditions);
        samlAuthzDecisionStatementAssertion.setAccessConsentPolicy(CLAIM_FORM_REF);
        samlAuthzDecisionStatementEvidence.setAssertion(samlAuthzDecisionStatementAssertion);
        samlAuthzDecisionStatement.setEvidence(samlAuthzDecisionStatementEvidence);
        assertion.setSamlAuthzDecisionStatement(samlAuthzDecisionStatement);
        assertion.setSamlAuthnStatement(samlAuthnStatement);

        PersonNameType pName = new PersonNameType();
        pName.setFamilyName(msg.getPatientLastName());
        pName.setGivenName(msg.getPatientFirstName());
        pName.setSecondNameOrInitials(msg.getPatientMiddleName());
        assertion.setPersonName(pName);

        HomeCommunityType hc = new HomeCommunityType();
        hc.setDescription(msg.getHomeCommunityDesc());
        hc.setHomeCommunityId(msg.getHomeCommunityId());
        hc.setName(msg.getHomeCommunityName());

        UserType muser = new UserType();
        CeType roleType = new CeType();
        roleType.setCode(ROLE_CODE);
        roleType.setCodeSystem(ROLE_CODE_SYSTEM);
        roleType.setCodeSystemName(ROLE_CODE_SYSTEM_NAME);
        roleType.setCodeSystemVersion(ROLE_CODE_SYSTEM_VERSION);
        roleType.setDisplayName(ROLE_NAME);
        roleType.setOriginalText(ROLE_NAME);
        muser.setOrg(hc);
        muser.setRoleCoded(roleType);

        //VA DoD Requirement
        String uPlusDoD = msg.getUsername() + USER_DOD_EXTENSION;
        muser.setUserName(uPlusDoD);
        PersonNameType uName = new PersonNameType();
        CeType nType = new CeType();
        nType.setCode(DOD_ROLE_CODE);
        nType.setCodeSystem(DOD_ROLE_CODE_SYSTEM);
        nType.setCodeSystemName(DOD_ROLE_CODE_SYSTEM_NAME);
        nType.setCodeSystemVersion(DOD_ROLE_CODE_SYSTEM_VERSION);
        nType.setDisplayName(DOD_ROLE_NAME);
        nType.setOriginalText(DOD_ROLE_NAME);

        uName.setNameType(nType);
        uName.setFamilyName(msg.getProviderLastName());
        uName.setGivenName(msg.getProviderFirstName());
        uName.setSecondNameOrInitials(msg.getProviderMiddleName());
        muser.setPersonName(uName);
        assertion.setUserInfo(muser);

        assertion.setHomeCommunity(hc);
        CeType pouType = new CeType();
        pouType.setCode(PURPOSE_OF_USE_ROLE_CODE);
        pouType.setCodeSystem(PURPOSE_OF_USE_ROLE_CODE_SYSTEM);
        pouType.setCodeSystemName(PURPOSE_OF_USE_ROLE_CODE_SYSTEM_NAME);
        pouType.setCodeSystemVersion(PURPOSE_OF_USE_ROLE_CODE_SYSTEM_VERSION);
        pouType.setDisplayName(PURPOSE_OF_USE_ROLE_NAME);
        pouType.setOriginalText(PURPOSE_OF_USE_ROLE_NAME);
        assertion.setPurposeOfDisclosureCoded(pouType);

        return assertion;
    }

    /**
     * Add slot to query object.
     *
     * @param registry - submission object
     * @param name - slot name
     * @param values - slot values
     */
    private static void addSlot(AdhocQueryType query,
            String name, String [] values) {

        SlotType1 slot = new SlotType1();
        slot.setName(name);

        ValueListType valList = new ValueListType();
        for (String value : values) {
            valList.getValue().add(value);
        }

        slot.setValueList(valList);
        query.getSlot().add(slot);
    }

    /**
     * Parse metadata query result for summary return.
     *
     * @param result summary info
     * @return
     */
    private DocumentInfoType parseMetadata(ExtrinsicObjectType docMeta) {
        GregorianCalendar cal = new GregorianCalendar();

        //Poplulate summary data object
        DocumentInfoType summaryData = new DocumentInfoType();
        summaryData.setDataSource(DATA_SOURCE);
        String desc ="NHIN Document";
        try {
            //So many possible nulls, this try catch seems to be the best
            //  way to make sure we get the data.
            desc = docMeta.getDescription().getLocalizedString().get(0).getValue();
        }
        catch (Exception e1) {
            try {
                desc = docMeta.getName().getLocalizedString().get(0).getValue();
            }
            catch (Exception e2) {
                //do nothing
            }
        }
        summaryData.setDescription(desc);
        addNameValue(summaryData.getItemValues(), ITEM_DOCUMENT_NAME, docMeta.getName().getLocalizedString().get(0).getValue());
        addNameValue(summaryData.getItemValues(), ITEM_MIME_TYPE, docMeta.getMimeType());
        addNameValue(summaryData.getItemValues(), ITEM_HOME_COMMUNITY_ID, docMeta.getHome());
        for (SlotType1 metaSlot : docMeta.getSlot()) {
            if (XDS_CREATION_TIME.equals(metaSlot.getName())) {
                try {
                    cal.setTime(parseXDSDate(metaSlot.getValueList().getValue().get(0)));
                    summaryData.setDateCreated(
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
                }
                catch (Exception pe) {
                    String msg = "Error parsing: " + XDS_CREATION_TIME;
                    log.error(msg, pe);
                }
            }
            if (XDS_PATIENT_INFO.equals(metaSlot.getName())) {
                //Find patient name
                for (String ptVal : metaSlot.getValueList().getValue()) {
                    //Patient name is PID-5
                    if (ptVal.startsWith(XDS_PATIENT_NAME)) {
                        summaryData.setPatient(ptVal.substring(XDS_PATIENT_NAME.length()));
                    }
                }
            }
            if (XDS_DOC_SIZE.equals(metaSlot.getName())) {
                addNameValue(summaryData.getItemValues(), ITEM_DOCUMENT_SIZE, metaSlot.getValueList().getValue().get(0));
            }
            if (XDS_REPOSITORY_ID.equals(metaSlot.getName())) {
                addNameValue(summaryData.getItemValues(), ITEM_REPOSITORY_ID, metaSlot.getValueList().getValue().get(0));
            }
        } //end for meta slots
        for (ExternalIdentifierType identifier : docMeta.getExternalIdentifier()) {
            if (XDS_DOC_ID.equals(identifier.getName().getLocalizedString().get(0).getValue())) {
                summaryData.setItemId(identifier.getValue());
            }
        }
        for (ClassificationType classification : docMeta.getClassification()) {
            if (XDS_CLASS_AUTHOR.equals(classification.getClassificationScheme())) {
                for (SlotType1 authorSlot : classification.getSlot()) {
                    if (XDS_SLOT_AUTHOR.equals(authorSlot.getName())) {
                        summaryData.setAuthor(authorSlot.getValueList().getValue().get(0));
                    }
                }
                for (SlotType1 authorSlot : classification.getSlot()) {
                    if (XDS_SLOT_INSTITUTION.equals(authorSlot.getName())) {
                        addNameValue(summaryData.getItemValues(), ITEM_INSTITUTION, authorSlot.getValueList().getValue().get(0));
                    }
                }
            }
        }

        return summaryData;
    }

    /**
     * Add name/value pair to response.
     *
     * @param pairList
     * @param name
     * @param value
     */
    private void addNameValue(List<NameValuesPair> pairList, String name, String value) {
        NameValuesPair nameVal = new NameValuesPair();
        nameVal.setName(name);
        nameVal.getValues().add(value);
        pairList.add(nameVal);

        return;
    }

    /**
     * Parses XDS date using scaling precision (as according to XDS Spec).
     *
     * @param dateStr
     * @return
     * @throws java.text.ParseException
     */
    private Date parseXDSDate(String dateStr)
            throws ParseException {
        DateFormat xdFormat;
        if (dateStr.length() >= XDS_DATE_FORMAT_FULL.length()) {
            xdFormat = new SimpleDateFormat(XDS_DATE_FORMAT_FULL);
        }
        else {
            xdFormat = new SimpleDateFormat(XDS_DATE_FORMAT_FULL.substring(0, dateStr.length()));
        }

        return xdFormat.parse(dateStr);
    }

    /**
     * Format XDS date using scaling precision (as according to XDS Spec).
     *
     * @param date
     * @param precision
     * @return
     */
    private String formatXDSDate(Date date, int precision) {
        DateFormat xdsFormat = new SimpleDateFormat(XDS_DATE_FORMAT_FULL.substring(0, precision));
        return xdsFormat.format(date);
    }

    /**
     * Temporary object used for passing data in this class.
     */
    class NewDocInfo {
        DocDownloadInfoType doc;
        ExtrinsicObjectType extrinsic;
    }
}
