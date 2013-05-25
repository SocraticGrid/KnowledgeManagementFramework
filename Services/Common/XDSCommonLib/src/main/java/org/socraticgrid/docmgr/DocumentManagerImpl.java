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
import org.socraticgrid.common.dda.GetMessageDetailRequestType;
import org.socraticgrid.common.dda.GetMessageDetailResponseType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.GetMessagesResponseType.GetMessageResponse;
import org.socraticgrid.common.dda.SetMessageDataResponseType;
import org.socraticgrid.common.docmgr.GetNHINDocumentsResponseType;
import org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadRequestType;
import org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadResponseType;
import org.socraticgrid.common.docmgr.UpdateDocumentSlotRequestType;
import org.socraticgrid.docmgr.msgobject.ArchiveInfo;
import org.socraticgrid.docmgr.msgobject.MirthMessage;
import org.socraticgrid.docmgr.msgobject.NHINQueryMessage;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import org.socraticgrid.xdsmanager.model.ProcessQueryParams;
import org.socraticgrid.xdsmanager.model.XDSProcess;
import org.socraticgrid.xdsmanager.service.XDSProcessConstants;
import org.socraticgrid.xdsmanager.service.XDSService;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType.DocumentResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.query._3.ResponseOptionType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AdhocQueryType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.IdentifiableType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Implements the DocumentManager web serivce.
 *
 * @author cmatser
 */
public class DocumentManagerImpl {

    /** Property constants. */
    public static final String REPOSITORY_PROPERTY_FILE = "docmgr";
    public static final String C32STYLESHEET_DEFAULT_PROP = "C32StyleSheet_default";
    public static final String C32STYLESHEET_PREFIX_PROP = "C32StyleSheet_";
    public static final String DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP = "dynamicDocumentRepositoryId";
    public static final String INBOUND_DOCUMENT_REPOSITORY_ID_PROP = "inboundDocumentRepositoryId";
    public static final String INBOUND_DOCUMENT_MIRTH_WSDL = "mirthChannel";
    public static final String POLICY_REPOSITORY_ID_PROP = "policyRepositoryId";
    public static final String DOCUMENT_UNIQUE_OID_PROP = "documentUniqueOID";
    public static final String DEFAULT_HOME_COMMUNITY_ID = "document.defaultHomeCommunityId";
    public static final String DOCMGR_QUEUE = "documentManager.queue";
    public static final String DOCMGR_QUEUE_FACTORY = "documentManager.queueFactory";
    public static final String NHINDOCQUERY_ENDPOINT_PROP = "nhinDocQuery";
    public static final String NHINDOCRETRIEVE_ENDPOINT_PROP = "nhinDocRetrieve";
    public static final String XDS_PROCESS_ENDPOINT_PROP = "xdsProcessManager";

    /** InfoButton Assertion properties. */
    public static final String IB_SIGNATURE_DATE_PROP = "ib.assertion.signature_date";
    public static final String IB_EXPIRATION_DATE_PROP = "ib.assertion.expiration_date";
    public static final String IB_ROLE_NAME_PROP = "ib.assertion.role_name";
    public static final String IB_ROLE_CODE_PROP = "ib.assertion.role_code";
    public static final String IB_ROLE_CODE_SYSTEM_PROP = "ib.assertion.role_code_system";
    public static final String IB_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.role_code_system_name";
    public static final String IB_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.role_code_system_version";
    public static final String IB_USER_DOD_EXTENSION_PROP = "ib.assertion.user_dod_extension";
    public static final String IB_DOD_ROLE_NAME_PROP = "ib.assertion.dod_role_name";
    public static final String IB_DOD_ROLE_CODE_PROP = "ib.assertion.dod_role_code";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_PROP = "ib.assertion.dod_role_code_system";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.dod_role_code_system_name";
    public static final String IB_DOD_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.dod_role_code_system_version";
    public static final String IB_PURPOSE_OF_USE_ROLE_NAME_PROP = "ib.assertion.purpose_of_use_role_name";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_PROP = "ib.assertion.purpose_of_use_role_code";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_PROP = "ib.assertion.purpose_of_use_role_code_system";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_NAME_PROP = "ib.assertion.purpose_of_use_role_code_system_name";
    public static final String IB_PURPOSE_OF_USE_ROLE_CODE_SYSTEM_VERSION_PROP = "ib.assertion.purpose_of_use_role_code_system_version";
    public static final String IB_CLAIM_FORM_REF_PROP = "ib.assertion.claim_form_ref";
    public static final String IB_CLAIM_FORM_STRING_PROP = "ib.assertion.claim_form_string";

    /** Date precision. */
    public static final int XDS_DATE_QUERY_FROM_PRECISION = 8;
    public static final int XDS_DATE_QUERY_TO_PRECISION = 14;
    public static final int ASSERTION_DOB_PRECISION = 12;
    public static final String XDS_DATE_FORMAT_FULL = "yyyyMMddHHmmssZ";

    /** Value for archive field in metadata */
    public static final String XDS_ARCHIVE_SLOT = "urn:gov:hhs:fha:nhinc:xds:hasBeenAccessed";

    /** Repository id field for segmenting the data. */
    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";
    public static final String XDS_REPOSITORY_ID_QUERY = "$XDSRepositoryUniqueId";

    /** Error values */
    public static final String XDS_SUCCESS_STATUS
       = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Success";
    public static final String XDS_FAILED_STATUS
       = "urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure";
    public static final String XDS_ERROR_CODE = "DOCUMENT_MANAGER_ERROR";
    public static final String XDS_ERROR_SEVERITY = "ERROR";

    /** Job constants. */
    public static final String DOCMGR_JOB_MESSAGE = "jobMessage";
    public static final String JOB_START_SUCCESS = "Success";
    public static final String JOB_START_FAILURE = "Failure";
    public static final String JOB_SUCCESS_ID = "1";
    public static final String JOB_FAILURE_ID = "-1";

    /** KMR Inbox nhin query minutes before give up. */
    public static final int XDS_QUERY_DURATION = 20;

    /** KMR INbox nhin query action. */
    public static final String XDS_INBOX_ACTION = "Start";

    /** KMR document metadata. */
    public static final String XDS_KMR_STARRED = "urn:org:socraticgrid:xds:starred";
    public static final String XDS_KMR_ARCHIVE = "urn:org:socraticgrid:xds:archive";
    public static final String XDS_KMR_DELETE = "urn:org:socraticgrid:xds:delete";
    public static final String XDS_KMR_READ = "urn:org:socraticgrid:xds:read";
    public static final String XDS_KMR_STOREDATE = "urn:org:socraticgrid:xds:storedate";
    public static final String XDS_CUSTOM_METADATA_PREFIX = "urn:";
    public static final String XDS_CREATION_TIME = "creationTime";
    public static final String XDS_SERVICE_START = "serviceStartTime";
    public static final String XDS_SERVICE_STOP = "serviceStopTime";
    public static final String XDS_LANGUAGE_CODE = "languageCode";
    public static final String XDS_PATIENT_INFO = "sourcePatientInfo";
    public static final String XDS_DOC_SIZE = "size";
    public static final String XDS_PATIENT_NAME = "PID-5|";
    public static final String XDS_PATIENT_GENDER = "PID-8|";
    public static final String XDS_PATIENT_DOB = "PID-7|";
    public static final String XDS_PATIENT_ADDRESS = "PID-11|";
    public static final String XDS_DOC_ID = "XDSDocumentEntry.uniqueId";
    public static final String XDS_CLASS_AUTHOR = "urn:uuid:93606bcf-9494-43ec-9b4e-a7748d1a838d";
    public static final String XDS_SLOT_AUTHOR = "authorPerson";
    public static final String XDS_SLOT_INSTITUTION = "authorInstitution";
    public static final String XDS_HAS_BEEN_ACCESSED = "urn:org:socraticgrid:xds:hasBeenAccessed";


    /** KMR inbox values. */
    public static final String KMR_INBOX_STARRED = "Starred";
    public static final String KMR_INBOX_UPDATE = "Update";
    public static final String KMR_INBOX_ARCHIVE = "Archive";
    public static final String KMR_INBOX_UNARCHIVE = "Unarchive";
    public static final String KMR_INBOX_READ = "Read";
    public static final String KMR_INBOX_UNREAD = "Unread";
    public static final String KMR_INBOX_DELETE = "Delete";
    public static final String KMR_INBOX_UNDELETE = "Undelete";
    public static final String KMR_INBOX_USERTRASH = "UserTrash";
    public static final String KMR_INBOX = "Inbox";

    /** Date format for XDS */
    public static final DateFormat XDS_DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    /** Logging. */
    private static Log log = LogFactory.getLog(DocumentManagerImpl.class);

    ////////////////////////////////////////////////////////////////////////////
    //KMR(Inbox) Interface implementation
    ////////////////////////////////////////////////////////////////////////////
    public org.socraticgrid.common.dda.GetMessagesResponseType getMessages(org.socraticgrid.common.dda.GetMessagesRequestType request) {
        GetMessagesResponseType response = new GetMessagesResponseType();

        log.debug("Retrieving NHIN document messages.");

        try {
            //If no patient is passed, return
            if ((request.getPatientId() == null) || request.getPatientId().isEmpty()) {
                throw new IllegalArgumentException("PatientId is required.");
            }

            String defaultHomeCommId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DEFAULT_HOME_COMMUNITY_ID);

            //Create query
            AdhocQueryRequest metaRequest = createQuery(
                new String[] {
                    "$XDSDocumentEntryPatientId",
                    "$XDSDocumentEntryStatus",
                    "$XDSRepositoryUniqueId",
                },
                new String[] {
                    "'" + request.getPatientId() + "^^^&" + defaultHomeCommId + "&ISO'",
                    "('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')",
                    PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP),
                }
            );

            //Parse result
            AdhocQueryResponse queryResponse = documentManagerQueryForDocument(metaRequest);
            response = parseMetadataMessageResponse(request.getUserId(), request.getMessageType(),
                    request.getLocation(), queryResponse);

        }
        catch (Exception e) {
            log.error("Error retriving messages NHIN documents on patient: " + request.getPatientId(), e);

            GetMessageResponse msgResponse = new GetMessageResponse();
            msgResponse.setStatusMessage(e.getMessage());
            msgResponse.setSuccessStatus(false);
            response.getGetMessageResponse().add(msgResponse);
        }

        return response;
    }

    /**
     * Parse metadata query result for getMessages() return.
     *
     * @param msgType
     * @param metadata result
     * @return
     */
    private GetMessagesResponseType parseMetadataMessageResponse(String userId, String msgType,
            String location, oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse result) {

        GetMessagesResponseType response = new GetMessagesResponseType();

        try {
            GetMessageResponse msgResponse;

            //Check for errors
            if (result.getRegistryErrorList() != null) {
                msgResponse = new GetMessageResponse();
                List<RegistryError> errorList = result.getRegistryErrorList().getRegistryError();
                for (RegistryError error : errorList) {
                    msgResponse.setStatusMessage(error.getValue());
                    break;
                }
                msgResponse.setSuccessStatus(false);
                response.getGetMessageResponse().add(msgResponse);
            }
            List<JAXBElement<? extends IdentifiableType>> objectList = result.getRegistryObjectList().getIdentifiable();
            log.debug("Found metadata for documents: " + objectList.size());
            for (JAXBElement<? extends IdentifiableType> object : objectList) {
                IdentifiableType identifiableType = object.getValue();
                if (identifiableType instanceof ExtrinsicObjectType) {
                    msgResponse = new GetMessageResponse();
                    msgResponse.setSuccessStatus(true);

                    //Poplulate summary data object
                    GregorianCalendar cal = new GregorianCalendar();
                    String author = null;
                    String institution = null;
                    msgResponse.setMessageType(msgType);
                    msgResponse.setMessageStatus(KMR_INBOX_UNREAD);
                    ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) identifiableType;
                    try {
                        msgResponse.setDescription(extrinsic.getDescription().getLocalizedString().get(0).getValue());
                    }
                    catch (Exception e) {
                        msgResponse.setDescription("");
                    }
                    msgResponse.setTitle(extrinsic.getName().getLocalizedString().get(0).getValue());
                    for (SlotType1 metaSlot : extrinsic.getSlot()) {
                        //Set only if not already set, we use the creation date if special kmr
                        //  metadata tag is not found.
                        if (XDS_CREATION_TIME.equals(metaSlot.getName()) && (msgResponse.getMessageDate() == null)) {
                            try {
                                cal.setTime(parseXDSDate(metaSlot.getValueList().getValue().get(0)));
                                if (msgResponse.getMessageDate() == null)
                                msgResponse.setMessageDate(
                                   DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
                            }
                            catch (Exception pe) {
                                String msg = "Error parsing: " + XDS_CREATION_TIME;
                                log.error(msg, pe);

                                msgResponse.setStatusMessage(msg + ". " + pe.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                        else if((XDS_KMR_READ + ":" + userId).equals(metaSlot.getName())) {
                            try {
                                if (Boolean.TRUE.equals(Boolean.valueOf(metaSlot.getValueList().getValue().get(0)))) {
                                    msgResponse.setMessageStatus(KMR_INBOX_READ);
                                }
                                else {
                                    msgResponse.setMessageStatus(KMR_INBOX_UNREAD);
                                }
                            }
                            catch (Exception e) {
                                String msg = "Error parsing: " + XDS_KMR_READ + ":" + userId;
                                log.error(msg, e);

                                msgResponse.setStatusMessage(msg + ". " + e.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                        else if((XDS_KMR_STARRED + ":" + userId).equals(metaSlot.getName())) {
                            try {
                                if (Boolean.TRUE.equals(Boolean.valueOf(metaSlot.getValueList().getValue().get(0)))) {
                                    msgResponse.getLabels().add(KMR_INBOX_STARRED);
                                }
                            }
                            catch (Exception e) {
                                String msg = "Error parsing: " + XDS_KMR_STARRED + ":" + userId;
                                log.error(msg, e);

                                msgResponse.setStatusMessage(msg + ". " + e.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                        else if((XDS_KMR_ARCHIVE + ":" + userId).equals(metaSlot.getName())) {
                            try {
                                if (Boolean.TRUE.equals(Boolean.valueOf(metaSlot.getValueList().getValue().get(0)))) {
                                    //Set archive if location value not already set (trash)
                                    if ((msgResponse.getLocation() == null) || msgResponse.getLocation().isEmpty()) {
                                        msgResponse.setLocation(KMR_INBOX_ARCHIVE);
                                    }
                                }
                            }
                            catch (Exception e) {
                                String msg = "Error parsing: " + XDS_KMR_ARCHIVE + ":" + userId;
                                log.error(msg, e);

                                msgResponse.setStatusMessage(msg + ". " + e.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                        else if((XDS_KMR_DELETE + ":" + userId).equals(metaSlot.getName())) {
                            try {
                                if (Boolean.TRUE.equals(Boolean.valueOf(metaSlot.getValueList().getValue().get(0)))) {
                                    msgResponse.setLocation(KMR_INBOX_USERTRASH);
                                }
                            }
                            catch (Exception e) {
                                String msg = "Error parsing: " + XDS_KMR_DELETE + ":" + userId;
                                log.error(msg, e);

                                msgResponse.setStatusMessage(msg + ". " + e.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                        else if((XDS_KMR_STOREDATE).equals(metaSlot.getName())) {
                            try {
                                cal.setTime(parseXDSDate(metaSlot.getValueList().getValue().get(0)));
                                msgResponse.setMessageDate(
                                   DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
                            }
                            catch (Exception pe) {
                                String msg = "Error parsing: " + XDS_KMR_STOREDATE;
                                log.error(msg, pe);

                                msgResponse.setStatusMessage(msg + ". " + pe.getMessage());
                                msgResponse.setSuccessStatus(false);
                            }
                        }
                    } //end for meta slots
                    for (ExternalIdentifierType identifier : extrinsic.getExternalIdentifier()) {
                        if (XDS_DOC_ID.equals(identifier.getName().getLocalizedString().get(0).getValue())) {
                            msgResponse.setMessageId(identifier.getValue());
                        }
                    }
                    for (ClassificationType classification : extrinsic.getClassification()) {
                        if (XDS_CLASS_AUTHOR.equals(classification.getClassificationScheme())) {
                            for (SlotType1 authorSlot : classification.getSlot()) {
                                if (XDS_SLOT_AUTHOR.equals(authorSlot.getName())) {
                                    author = authorSlot.getValueList().getValue().get(0);
                                }
                            }
                            for (SlotType1 authorSlot : classification.getSlot()) {
                                if (XDS_SLOT_INSTITUTION.equals(authorSlot.getName())) {
                                    institution = authorSlot.getValueList().getValue().get(0);
                                }
                            }
                        }
                    }

                    //Set From field as combo of author and institution
                    if ((institution != null) && !institution.isEmpty()) {
                        msgResponse.setFrom(institution + " - " + author);
                    }
                    else {
                        msgResponse.setFrom(author);
                    }

                    msgResponse.setPriority("");
                    msgResponse.setTasksComplete(0);
                    msgResponse.setTasksCount(0);

                    //Filter documents not in requested location
                    if ((location == null) || location.isEmpty()
                            || KMR_INBOX.equalsIgnoreCase(location)) {
                        //If we're here, we want inbox only items,
                        //  which means an empty location value
                        if ((msgResponse.getLocation() != null) && !msgResponse.getLocation().isEmpty()) {
                            continue;
                        }
                    }
                    else {
                        //A location has been specified that is not Inbox,
                        //  so, make sure location mathces
                        if (!location.equalsIgnoreCase(msgResponse.getLocation())) {
                            continue;
                        }
                    }

                    //Add the document to the response
                    response.getGetMessageResponse().add(msgResponse);
                } //end if extrinisc object
            } //end for result object list
        }
        catch (Exception e) {
            log.error("Error parsing metadata result.", e);

            GetMessageResponse msgResponse = new GetMessageResponse();
            msgResponse.setStatusMessage(e.getMessage());
            msgResponse.setSuccessStatus(false);
            response.getGetMessageResponse().add(msgResponse);
        }

        return response;
    }

    private Date parseXDSDate(String dateStr)
            throws ParseException {

        //Often, date consists of just year,month,day.  So, we
        //  pad the time in order for the parse to succeed
        if (dateStr.length() == 8) {
            dateStr = dateStr + "00000000";
        }

        return XDS_DATE_FORMAT.parse(dateStr);
    }

    /**
     * Get the KMR Inbox detail information.
     *
     * @param request
     * @return
     */
    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType request) {
        GetMessageDetailResponseType response = new GetMessageDetailResponseType();

        log.debug("Retrieving NHIN Document detail.");

        try {
            response.getSentTo().add(request.getUserId());
            String repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);

            //Retrieve based on item id
            //Create document retrieve
            RetrieveDocumentSetRequestType docQueryRequest = new RetrieveDocumentSetRequestType();

            DocumentRequest retrieve = new DocumentRequest();
            retrieve.setDocumentUniqueId(request.getMessageId());
            retrieve.setRepositoryUniqueId(repositoryId);

            docQueryRequest.getDocumentRequest().add(retrieve);

            //Parse document result
            RetrieveDocumentSetResponseType docResult = documentManagerRetrieveDocument(docQueryRequest);

            //Check for errors
            if ((docResult.getRegistryResponse() != null) && (docResult.getRegistryResponse().getRegistryErrorList() != null)) {
                List<RegistryError> errorList = docResult.getRegistryResponse().getRegistryErrorList().getRegistryError();
                for (RegistryError error : errorList) {
                    response.setStatusMessage(error.getErrorCode() + ": " + error.getValue());
                    response.setSuccessStatus(false);
                    break;
                }
            }
            List<DocumentResponse> objectList = docResult.getDocumentResponse();
            log.debug("Found " + objectList.size() + " document found regarding item: " + request.getMessageId());
            for (DocumentResponse object : objectList) {

                //For now all docs are C32s...this is not always the case!
                log.debug("Adding document unique id: " + object.getDocumentUniqueId()
                    + ", home community id: " + object.getHomeCommunityId() + " to response.");

                //Find stylesheet
                String styleSheet = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, C32STYLESHEET_PREFIX_PROP + object.getHomeCommunityId());

                //Get default stylesheet if necessary
                if ((styleSheet == null) || (styleSheet.isEmpty())) {
                    styleSheet = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, C32STYLESHEET_DEFAULT_PROP);
                }

                //Convert to html
                String dirSep = System.getProperty("file.separator");
                String propertyDir = PropertyAccessor.getPropertyFileLocation();
                if (!propertyDir.endsWith(dirSep)) {
                    propertyDir += dirSep;
                }
                FileReader reader = new FileReader(propertyDir + styleSheet);
                String html = convertXMLToHTML(new ByteArrayInputStream(object.getDocument()), reader);

                response.getMessageDetail().add(html);
                break;
            } //end for result object list

            response.setStatusMessage("");
            response.setSuccessStatus(true);

        } //end try
        catch (Exception e) {
            log.error("Error retriving detail NHIN document: " + request.getMessageId(), e);
            response.setStatusMessage(e.getMessage());
            response.setSuccessStatus(false);
        }

        return response;
    }

    /**
     * Transform C32.
     * 
     * @param xml
     * @param xsl
     * @return
     */
    private String convertXMLToHTML(ByteArrayInputStream xml, FileReader xsl) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer =
                    tFactory.newTransformer(new javax.xml.transform.stream.StreamSource(xsl));
            transformer.transform(new javax.xml.transform.stream.StreamSource(xml),
                    new javax.xml.transform.stream.StreamResult(output));
        } catch (Exception e) {
            log.error("Exception in transforming xml to html", e);
        }

        return output.toString();
    }

    /**
     * KMR Inbox setMessage used to update message items.
     *
     * @param request
     * @return
     */
    public org.socraticgrid.common.dda.SetMessageDataResponseType setMessage(org.socraticgrid.common.dda.MessageDataRequestType request) {
        SetMessageDataResponseType response = new SetMessageDataResponseType();

        log.debug("Setting NHIN document message.");

        try {
            //If no message id is passed, return
            if (((request.getMessageId() == null) || request.getMessageId().isEmpty())
                    && ((request.getUserId() == null) || request.getUserId().isEmpty())) {
                throw new IllegalArgumentException("UserId and MessageId is required.");
            }

            String defaultHomeCommId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DEFAULT_HOME_COMMUNITY_ID);
            String repositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);

            //Check for action
            if (!request.getAction().isEmpty()) {
                String slotName;
                String slotValue;
                if (request.getAction().equalsIgnoreCase(KMR_INBOX_UPDATE)) {
                    slotName = XDS_KMR_STARRED + ":" + request.getUserId();

                    if ((request.getLabels() != null)
                            && !request.getLabels().isEmpty()
                            && (null != request.getLabels().get(0))
                            && request.getLabels().get(0).equalsIgnoreCase(KMR_INBOX_STARRED)) {
                        slotValue = Boolean.TRUE.toString();
                    }
                    else {
                        slotValue = Boolean.FALSE.toString();
                    }
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_ARCHIVE)) {
                    slotName = XDS_KMR_ARCHIVE + ":" + request.getUserId();
                    slotValue = Boolean.TRUE.toString();
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_UNARCHIVE)) {
                    slotName = XDS_KMR_ARCHIVE + ":" + request.getUserId();
                    slotValue = Boolean.FALSE.toString();
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_DELETE)) {
                    slotName = XDS_KMR_DELETE + ":" + request.getUserId();
                    slotValue = Boolean.TRUE.toString();
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_UNDELETE)) {
                    slotName = XDS_KMR_DELETE + ":" + request.getUserId();
                    slotValue = Boolean.FALSE.toString();
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_READ)) {
                    slotName = XDS_KMR_READ + ":" + request.getUserId();
                    slotValue = Boolean.TRUE.toString();
                }
                else if(request.getAction().equalsIgnoreCase(KMR_INBOX_UNREAD)) {
                    slotName = XDS_KMR_READ + ":" + request.getUserId();
                    slotValue = Boolean.FALSE.toString();
                }
                else {
                    throw new Exception("Unknown action: " + request.getAction());
                }

                //Perform action by updated the document metadata
                doMetadataUpdate(request.getMessageId(), defaultHomeCommId, repositoryId,
                    slotName, slotValue);
            }

            response.setMessage("Document Updated Successfully");
            response.setSuccessStatus(true);

        } //end try
        catch (Exception e) {
            log.error("Error setting message NHIN document: " + request.getMessageId(), e);
            response.setMessage("Error setting message for NHIN documents: " + request.getMessageId() + ". " + e.getMessage());
            response.setSuccessStatus(false);
        }

        return response;
    }

    private void doMetadataUpdate(String documentId, String homeCommunityId, String repositoryId, String slotName, String slotValue)
            throws Exception {

        UpdateDocumentSlotRequestType updateRequest = new UpdateDocumentSlotRequestType();
        updateRequest.setDocumentUniqueId(documentId);
        updateRequest.setHomeCommunityId(homeCommunityId);
        updateRequest.setRepositoryUniqueId(repositoryId);
        updateRequest.setSlotName(slotName);
        updateRequest.getSlotValueList().add(slotValue);

        RegistryResponseType updateResult = documentManagerUpdateDocumentSlot(updateRequest);

        if ((updateResult.getRegistryErrorList() != null)
                && (updateResult.getRegistryErrorList().getRegistryError() != null)
                && !updateResult.getRegistryErrorList().getRegistryError().isEmpty()) {
            throw new Exception(updateResult.getRegistryErrorList().getRegistryError().get(0).getValue());
        }

        return;
    }

    ////////////////////////////////////////////////////////////////////////////
    //(Original) Web Service Interface implementation
    ////////////////////////////////////////////////////////////////////////////

    /**
     * Query for document.
     *
     * Before we do, ensure repository Id is in the request.  This will tell the
     * repository to filter the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse documentManagerQueryForDocument(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        log.debug("Querying document archive.");

        oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse result = null;

        try { // Call DocumentRepository

            //Ensure valid repository id in query
            String repositoryId = pullRepositoryId(body);
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }

            result = new DocumentRegistryHelper().documentRegistryRegistryStoredQuery(body);
        } catch (Exception e) {
            result = new oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse();
            result.setStatus(XDS_FAILED_STATUS);
            result.setRegistryObjectList(new RegistryObjectListType());

            log.error("Error querying for document.", e);
        }

        return result;
    }


    /**
     * Retrieve document.
     * 
     * Before we do, ensure repository Id is in the request.
     *
     * Here we can just forward the request to the repository.  For a real XDS
     * server, we would need to call the appropriate repository for the document.
     *
     * @param body
     * @return
     */
    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType documentManagerRetrieveDocument(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        log.debug("Retrieving document.");

        RetrieveDocumentSetResponseType result = null;

        try { // Call Web Service Operation

            //Ensure valid repository id in query
            String repositoryId = pullRepositoryId(body);
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }

            result = new DocumentRepositoryHelper().documentRepositoryRetrieveDocumentSet(body);
        } catch (Exception e) {
            RegistryResponseType response = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            response.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList();
            RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not retrieve document.");
            error.setLocation("DocumentManagerImpl.retrieveDocument");
            errorList.getRegistryError().add(error);
            response.setRegistryErrorList(errorList);
            result.setRegistryResponse(response);

            log.error("Error retrieving document.", e);
        }

        return result;
    }

    /**
     * Store document.
     * 
     * Perform the actual store.  A unique Id is inserted if one hasn't already
     * been created.
     *
     * Before we do, we ensure a valid repository Id is in the request.  This will tell the
     * repository to store the request appropriately.  Unfortunately, this
     * isn't how a real XDS Registry behaves.  So when/if we use one, there will
     * need to be a rework done here.
     *
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerStoreDocument(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        log.debug("Storing document.");

        oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType result = null;

        try { // Call Web Service Operation

            //Insert doc unique id if one does not exist
            if (!createDocumentUniqueId(body)) {
                throw new Exception("Failed to create document unique Id");
            }

            //Ensure valid repository id in query
            String repositoryId = pullRepositoryId(body);
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }

            //Send to mirth if destination is Inbound Repository
            String inboundRepositoryId = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP);
            if (repositoryId.equals(inboundRepositoryId)) {
                try {
                    String mirthWSDL = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_MIRTH_WSDL);
                    if ((mirthWSDL != null) && !mirthWSDL.isEmpty()) {
                        String msgBody = null;
                        if ((body.getDocument() != null) && (body.getDocument().size() > 0)) {
                            msgBody = new String(body.getDocument().get(0).getValue());
                        }

                        if (msgBody == null) {
                            throw new Exception("Message body not found in request.");
                        }

                        MirthMessage msg = new MirthMessage();
                        msg.setWsdl(mirthWSDL);
                        msg.setBody(msgBody);

                        String msgResult[] = startBackgroundJob(msg);

                        if (msgResult[0].equals(JOB_FAILURE_ID)) {
                            throw new Exception(msgResult[1]);
                        }
                        else {
                            log.debug("Passed document for mirth handling, ticket: " + msgResult[0]);
                        }
                    }
                }
                catch (Exception e) {
                    log.error("Error sending inbound document to Mirth channel", e);
                    //continue to store locally
                }
            }

            try {
                //Add store date
                for (JAXBElement<? extends IdentifiableType> object : body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable()) {
                    IdentifiableType identifiableType = object.getValue();
                    if (identifiableType instanceof ExtrinsicObjectType) {
                        ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) identifiableType;
                        boolean found = false;
                        for (SlotType1 slot : extrinsic.getSlot()) {
                            if (XDS_KMR_STOREDATE.equals(slot.getName())) {
                                found = true;
                            }
                        }

                        if (!found) {
                            GregorianCalendar cal = new GregorianCalendar();
                            addSlot(extrinsic, XDS_KMR_STOREDATE,
                                new String[] { formatXDSDate(cal.getTime(), XDS_DATE_QUERY_TO_PRECISION) });
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Unable to add document metadata: " + XDS_KMR_STOREDATE, e);
            }

            result = new DocumentRepositoryHelper().documentRepositoryProvideAndRegisterDocumentSet(body);
        } catch (Exception e) {
            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList();
            RegistryError error = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not store document.");
            error.setLocation("DocumentManagerImpl.storeDocument");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);

            log.error("Error storing document.", e);
        }

        return result;
    }

    /**
     * The document is archived by first querying for the metadata, then querying for
     * the document itself.  With this information, we can re-store the document as a replacement
     * of the original (updating the hasBeenAccessed flag).  The hasBeenAccessed flag is our
     * archiving flag.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerArchiveDocument(org.socraticgrid.common.docmgr.ArchiveDocumentRequestType body) {
        log.debug("Archiving document.");

        RegistryResponseType result = null;
        String homeCommunityId = null;
        String repositoryId = null;
        String documentUniqueId = null;

        try {
            //Pull out parameters
            homeCommunityId = body.getHomeCommunityId();
            repositoryId = body.getRepositoryUniqueId();
            documentUniqueId = body.getDocumentUniqueId();

            if ((homeCommunityId == null) || (repositoryId == null) || (documentUniqueId == null)) {
                throw new Exception("Either homeCommunityId, repositoryUniqueId, or documentUniqueId is missing.");
            }

            //Ensure valid repository id in query
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }

        }
        catch (Exception e) {
            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not archive document.");
            error.setLocation("DocumentManagerImpl.archiveDocument");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);

            log.error("Error archving document.", e);

            return result;
        }

        //Kick off process
        ArchiveInfo archiveInfo = new ArchiveInfo();
        archiveInfo.setHomeCommunityId(homeCommunityId);
        archiveInfo.setRepositoryId(repositoryId);
        archiveInfo.setDocumentUniqueId(documentUniqueId);
        startBackgroundJob(archiveInfo);

        result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
        result.setStatus(XDS_SUCCESS_STATUS);

        return result;
    }

    /**
     * Update document slot.
     * 
     * @param body
     * @return
     */
    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType documentManagerUpdateDocumentSlot(org.socraticgrid.common.docmgr.UpdateDocumentSlotRequestType body) {
        log.debug("Updating document slot.");

        RegistryResponseType result = null;
        String homeCommunityId = null;
        String repositoryId = null;
        String documentUniqueId = null;

        try {
            //Pull out parameters
            homeCommunityId = body.getHomeCommunityId();
            repositoryId = body.getRepositoryUniqueId();
            documentUniqueId = body.getDocumentUniqueId();

            if ((homeCommunityId == null) || (repositoryId == null) || (documentUniqueId == null)) {
                throw new Exception("Either homeCommunityId, repositoryUniqueId, or documentUniqueId is missing.");
            }

            //Ensure valid repository id in query
            if (!getValidRepositories().contains(repositoryId)) {
                throw new Exception("Repository id not valid: " + repositoryId);
            }

            //Handle update
            doUpdateSlot(repositoryId, documentUniqueId, body.getSlotName(), body.getSlotValueList());

            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_SUCCESS_STATUS);
        }
        catch (Exception e) {
            log.error("Error updating document slot.", e);

            result = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryResponseType();
            result.setStatus(XDS_FAILED_STATUS);
            RegistryErrorList errorList = new oasis.names.tc.ebxml_regrep.xsd.rs._3.ObjectFactory().createRegistryErrorList();
            RegistryError error = new RegistryError();
            error.setValue(e.getMessage());
            error.setErrorCode(XDS_ERROR_CODE);
            error.setSeverity(XDS_ERROR_SEVERITY);
            error.setCodeContext("Could not update document slot.");
            error.setLocation("DocumentManagerImpl.updateDocumentSlot");
            errorList.getRegistryError().add(error);
            result.setRegistryErrorList(errorList);
        }

        return result;
    }

    /**
     * Generate unique Id that can be used for document unique ids.
     * 
     * @param request
     * @return
     */
    public org.socraticgrid.common.docmgr.GenerateUniqueIdResponseType generateUniqueId(org.socraticgrid.common.docmgr.GenerateUniqueIdRequestType request) {
        org.socraticgrid.common.docmgr.GenerateUniqueIdResponseType response  = new org.socraticgrid.common.docmgr.GenerateUniqueIdResponseType();
        String oid = "1.1.1.1.1.1";

        try {
            oid = PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DOCUMENT_UNIQUE_OID_PROP);
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing property:" + DOCUMENT_UNIQUE_OID_PROP
                    + " in file:" + REPOSITORY_PROPERTY_FILE + ".", e);
        }

        //OID^extension format
        response.setUniqueId(oid + "^" + new Date().getTime());

        return response;
    }

    /**
     * Start the process that performs the gateway query and download new documents.
     * 
     * This method sends the message to the JMS Queue that begins the process.
     * 
     * @param request
     * @return
     */
    public org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadResponseType startNHINQueryAndDownload(org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadRequestType request) {
        org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadResponseType response = new org.socraticgrid.common.docmgr.StartNHINQueryAndDownloadResponseType();

        log.debug("Starting NHIN query and download process.");

        //Create message
        NHINQueryMessage nhinMessage = new NHINQueryMessage();
        nhinMessage.setCallbackURL(request.getCallbackURL());
        nhinMessage.setUsername(request.getUsername());
        if (request.getQueryFromDate() != null) {
            nhinMessage.setQueryFromDate(request.getQueryFromDate().toGregorianCalendar().getTime());
        }
        if (request.getQueryToDate() != null) {
            nhinMessage.setQueryToDate(request.getQueryToDate().toGregorianCalendar().getTime());
        }
        nhinMessage.setPatientUnitNumber(request.getPatientUnitNumber());
        nhinMessage.setPatientFirstName(request.getPatientFirstName());
        nhinMessage.setPatientMiddleName(request.getPatientMiddleName());
        nhinMessage.setPatientLastName(request.getPatientLastName());
        if (request.getPatientDOB() != null) {
            nhinMessage.setPatientDOB(request.getPatientDOB().toGregorianCalendar().getTime());
        }
        nhinMessage.setProviderFirstName(request.getProviderFirstName());
        nhinMessage.setProviderMiddleName(request.getProviderMiddleName());
        nhinMessage.setProviderLastName(request.getProviderLastName());
        nhinMessage.setHomeCommunityId(request.getHomeCommunityId());
        nhinMessage.setHomeCommunityName(request.getHomeCommunityName());
        nhinMessage.setHomeCommunityDesc(request.getHomeCommunityDescription());

        //Send message
        String result[] = startBackgroundJob(nhinMessage);

        //Set response info
        response.setTicket(result[0]);
        response.setRequestDetail(result[1]);

        return response;
    }

    /**
     * KMR Inbox request for cross-gateway NHIN documents.
     *
     * @param request
     * @return
     */
    public org.socraticgrid.common.docmgr.GetNHINDocumentsResponseType getNHINDocuments(org.socraticgrid.common.docmgr.GetNHINDocumentsRequestType request) {
        GetNHINDocumentsResponseType response = new GetNHINDocumentsResponseType();

        //Assign default response
        response.setProcessState(XDSProcessConstants.PROCESS_STATE_AVAILABLE);
        response.setSuccessStatus(true);

        //Check if there is an existing query.
        XDSService xdsService = new XDSService();
        ProcessQueryParams query = new ProcessQueryParams();
        query.setPatientId(request.getPatientId());
        List<XDSProcess> processes = xdsService.getProcessesByParams(query);

        //If query exists and is stale, delete
        Iterator<XDSProcess> procIter = processes.iterator();
        while (procIter.hasNext()) {
            XDSProcess process = procIter.next();
            GregorianCalendar now = new GregorianCalendar();
            GregorianCalendar staleTime = new GregorianCalendar();
            staleTime.setTime(process.getStartTime());
            staleTime.add(Calendar.MINUTE, XDS_QUERY_DURATION);
            if (now.after(staleTime)) {
                log.info("Removing stale process for patient: " + process.getPatientId()
                    + ", ticket: " + process.getTicket());
                xdsService.deleteProcess(process);
                procIter.remove();
            }
        }

        //Check if there is an existing query that is still valid
        if (!processes.isEmpty()) {
            XDSProcess process = processes.get(0);
            response.setProcessState(XDSProcessConstants.PROCESS_STATE_IN_PROGRESS);
            response.setStatusMessage("Current query in progress for patient: "
                + process.getPatientId() + ", by user: " + process.getUserId()
                + ", started at: " + process.getStartTime());
        }

        //Check action value if we don't have an existing query
        if (processes.isEmpty() && (request.getAction() != null) && request.getAction().equals(XDS_INBOX_ACTION)) {

            StartNHINQueryAndDownloadResponseType startResponse = null;

            try {
                log.debug("Starting NHIN query for patient: " + request.getPatientId());

                //Start new query
                StartNHINQueryAndDownloadRequestType startRequest = new StartNHINQueryAndDownloadRequestType();
                startRequest.setPatientUnitNumber(request.getPatientId());
                startRequest.setPatientDOB(request.getPatientDOB());
                startRequest.setPatientFirstName(request.getPatientFirstName());
                startRequest.setPatientMiddleName(request.getPatientMiddleName());
                startRequest.setPatientLastName(request.getPatientLastName());
                startRequest.setUsername(request.getUserId());
                startRequest.setProviderFirstName(request.getUserFirstName());
                startRequest.setProviderMiddleName(request.getUserMiddleName());
                startRequest.setProviderLastName(request.getUserLastName());
                startRequest.setHomeCommunityId(PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DEFAULT_HOME_COMMUNITY_ID));
                GregorianCalendar queryDate = new GregorianCalendar();
                queryDate.add(Calendar.YEAR, -5);
                startRequest.setQueryFromDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(queryDate));
                startRequest.setCallbackURL(PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, XDS_PROCESS_ENDPOINT_PROP));
                startResponse = startNHINQueryAndDownload(startRequest);
            }
            catch (Exception e) {
                log.error("Error handling getDocuments for patient: " + request.getPatientId(), e);
            }

            //Create new process record
            if ((startResponse != null) && !startResponse.getTicket().equals(JOB_FAILURE_ID)) {
                XDSProcess newProcess = new XDSProcess();
                newProcess.setTicket(startResponse.getTicket());
                newProcess.setPatientId(request.getPatientId());
                newProcess.setUserId(request.getUserId());
                newProcess.setStartTime(new Date());
                newProcess.setDownloadCount(-1L);
                newProcess.setExistedCount(-1L);
                newProcess.setResultCount(-1L);
                xdsService.saveProcess(newProcess);

                response.setProcessState(XDSProcessConstants.PROCESS_STATE_IN_PROGRESS);
                response.setStatusMessage("Process started.");
            }
            else {
                response.setProcessState(XDSProcessConstants.PROCESS_STATE_AVAILABLE);
                response.setStatusMessage("Process start failed.");
            }
        }

        return response;
    }
    
    public org.socraticgrid.common.docmgr.QueryDoneResponseType getNHINDocumentsQueryDone(org.socraticgrid.common.docmgr.QueryDoneRequestType request) {
        XDSService xdsService = new XDSService();

        ProcessQueryParams queryParams = new ProcessQueryParams();
        queryParams.setTicket(request.getTicket());
        List<XDSProcess> processes = xdsService.getProcessesByParams(queryParams);

        //Check if not found
        if (processes.isEmpty()) {
            log.warn("NHIN Document query finished, but process ticket was not found: "
                + request.getTicket());
            return null;
        }

        XDSProcess process = processes.get(0);

        //Check for success
        if (!request.isSuccess()) {
            log.error("Error with NHIN Document query: " + request.getDetail()
               + ", removing ticket: " + process.getTicket()
               + ", for patient: " + process.getPatientId());
            xdsService.deleteProcess(process);
            return null;
        }

        //Check for downloading documents
        if ((request.getNewDocs() != null) && request.getNewDocs().isEmpty()) {
            log.info("NHIN Document query complete, no new documents, removing ticket: "
               + process.getTicket() + ", for patient: " + process.getPatientId());
            xdsService.deleteProcess(process);
            return null;
        }

        //Update process
        log.info("NHIN Document query complete, " + request.getNewDocs().size()
           + " new documents, updating ticket: " + process.getTicket()
           + ", for patient: " + process.getPatientId());
        process.setDownloadCount((long) request.getNewDocs().size());
        process.setResultCount(0L);
        xdsService.saveProcess(process);

        return null;
    }

    public org.socraticgrid.common.docmgr.DocAvailableResponseType getNHINDocumentsDocAvailable(org.socraticgrid.common.docmgr.DocDownloadInfoType request) {
        XDSService xdsService = new XDSService();

        ProcessQueryParams queryParams = new ProcessQueryParams();
        queryParams.setTicket(request.getTicket());
        List<XDSProcess> processes = xdsService.getProcessesByParams(queryParams);

        //Check if not found
        if (processes.isEmpty()) {
            log.warn("NHIN Document download finished, but process ticket was not found: "
                + request.getTicket());
            return null;
        }

        XDSProcess process = processes.get(0);

        //Check for success
        if (!request.isSuccess()) {
            log.error("Error with NHIN Document download: " + request.getDetail()
               + ", ticket: " + process.getTicket()
               + ", patient: " + process.getPatientId());
        }
        else {
            log.info("NHIN Document download complete: " + request.getDocInfo().getItemId()
               + ", ticket: " + process.getTicket()
               + ", patient: " + process.getPatientId());
        }

        //Update process
        process.setDownloadCount(process.getDownloadCount() - 1);
        if (process.getDownloadCount() <= 0) {
            log.info("NHIN Document all downloads complete, removing ticket: "
               + process.getTicket() + ", patient: " + process.getPatientId());
            xdsService.deleteProcess(process);
        }
        else {
            xdsService.saveProcess(process);
        }

        return null;
    }

    /**
     * Internal method to handle document slot update.
     * 
     * @param repositoryId
     * @param documentUniqueId
     * @param slotName
     * @param slotValueList
     * @throws Exception
     */
    private void doUpdateSlot(String repositoryId, String documentUniqueId,
            String slotName, List<String> slotValueList)
        throws Exception {

        try {
            log.debug("Querying document to update slot.");

            //Create metadata query
            AdhocQueryRequest metaRequest = createQuery(
                new String[] {
                    "$XDSRepositoryUniqueId",
                    "$XDSDocumentEntryUniqueId",
                },
                new String[] {
                    repositoryId,
                    documentUniqueId,
                }
            );

            //Perform query for metadata
            AdhocQueryResponse queryResponse = documentManagerQueryForDocument(metaRequest);

            log.debug("Retrieving document to update slot.");

            //Create document retrieve
            RetrieveDocumentSetRequestType docRequest = createRetrieve(
                repositoryId, documentUniqueId);

            //Retrieve document
            RetrieveDocumentSetResponseType docResponse  = documentManagerRetrieveDocument(docRequest);

            log.debug("Replacing document to update slot.");

            //Create document retrieve
            //Create replacement
            ProvideAndRegisterDocumentSetRequestType replaceRequest = createReplaceRequest(
                queryResponse, docResponse, slotName, slotValueList);

            //Do store with updated metdata
            documentManagerStoreDocument(replaceRequest);
        }
        catch (Exception e) {
            log.error("Error performing slot update.", e);
            throw new Exception ("Error performing slot update.", e);
        }

    }

    /**
     * Internal method to create query used in document update slot.
     *
     * @param names
     * @param values
     * @return
     */
    private AdhocQueryRequest createQuery(String [] names, String [] values) {
        if ((names == null) || (values == null) || (names.length != values.length))
            return null;

        AdhocQueryRequest request = new AdhocQueryRequest();

        //Create FindDocuments query
        AdhocQueryType query = new AdhocQueryType();
        query.setId("urn:uuid:14d4debf-8f97-4251-9a74-a90016b0af0d");

        for (int i = 0; i < names.length; i++) {
            SlotType1 slot = new SlotType1();
            slot.setName(names[i]);
            ValueListType valList = new ValueListType();
            valList.getValue().add(values[i]);
            slot.setValueList(valList);
            query.getSlot().add(slot);
        }

        request.setAdhocQuery(query);

        ResponseOptionType option = new ResponseOptionType();
        option.setReturnComposedObjects(true);
        option.setReturnType("LeafClass");
        request.setResponseOption(option);

        return request;
    }

    /**
     * Internal method to create retrieve request used by doument update slot.
     *
     * @param repositoryId
     * @param documentUniqueId
     * @return
     */
    private RetrieveDocumentSetRequestType createRetrieve(String repositoryId, String documentUniqueId) {
        RetrieveDocumentSetRequestType request = new RetrieveDocumentSetRequestType();

        //Create retrieve request
        DocumentRequest retrieve = new DocumentRequest();
        retrieve.setRepositoryUniqueId(repositoryId);
        retrieve.setDocumentUniqueId(documentUniqueId);
        request.getDocumentRequest().add(retrieve);

        return request;
    }

    /**
     * Internal method to create store request used by document update slot.
     * @param queryResponse
     * @param docResponse
     * @return
     * @throws Exception
     */
    private ProvideAndRegisterDocumentSetRequestType createReplaceRequest(
            AdhocQueryResponse queryResponse,
            RetrieveDocumentSetResponseType docResponse,
            String slotName,
            List<String> slotValueList)
        throws Exception {

        ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();

        //Check for document metadata
        ExtrinsicObjectType extrinsic = null;
        if (queryResponse.getRegistryObjectList() == null) {
            throw new Exception("No document metadata returned.");
        }

        //Find document metadata
        List<JAXBElement<? extends IdentifiableType>> objectList = queryResponse.getRegistryObjectList().getIdentifiable();
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;
                break;
            }
        }

        //Check if metadata found
        if (extrinsic == null) {
            throw new Exception("Document metadata not found in query response.");
        }

        //Check for document data
        if (docResponse.getDocumentResponse().isEmpty()) {
            throw new Exception("No document metadata returned.");
        }

        //Update metadata
        SlotType1 updateSlot = findSlot(extrinsic, slotName);
        //Create if it doesn't exist
        if (updateSlot == null) {
            updateSlot = new SlotType1();
            updateSlot.setName(slotName);
            extrinsic.getSlot().add(updateSlot);
        }
        ValueListType valList = new ValueListType();
        valList.getValue().addAll(slotValueList);
        updateSlot.setValueList(valList);

        /**The Submission set is actually ignored by the reference implemenation.*/
        //Create submission set
        RegistryPackageType registryPackage = new RegistryPackageType();
        registryPackage.setId("SubmissionSet01");
        registryPackage.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:RegistryPackage");

        //Submission time
        Date now = new Date();
        addSlot(registryPackage, "submissionTime", new String [] { DocumentManagerImpl.XDS_DATE_FORMAT.format(now) });

        //Add submission author classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:a7058bb9-b4e4-4307-ba5b-e3f0ab85e12d", //scheme
            "", //node representation
            "id_20", //id
			null, //name
            new String [] {
                "authorPerson",
				"authorInstitution",
				"authorRole",
				"authorSpecialty",
            }, //slot names
            new String [][] {
                new String [] { "^DocumentManager^Automated^^^" },
                new String [] {
                    "LocalMHS",
                },
                new String [] { "Automated" },
                new String [] { "Automated" },
            } //slot values
        );

        //Add submission content type classification
        addClassification(registryPackage,
            "SubmissionSet01", //classifiedObject
            "urn:uuid:aa543740-bdda-424e-8c96-df4873be8500", //scheme
			"contentTypeCode", //node representation
            "id_21", //id
			"contentTypeDisplayName", //name
            new String [] {
                "codingScheme",
            }, //slot names
            new String [][] {
                new String [] { "Connect-a-thon contentTypeCodes" },
            } //slot values
        );

        //Add submission uniqueId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:96fdda7c-d067-4183-912e-bf5ee74998a8", //identificationScheme
            "id_22", //id
            "XDSSubmissionSet.uniqueId", //name
            new java.rmi.server.UID().toString()
        );

        //Add submission sourceId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832", //identificationScheme
            "id_23", //id
            "XDSSubmissionSet.sourceId", //name
            "1.1.1.1" //value
        );

        //Add submission patientId identifier
        addExternalIdentifier(registryPackage,
            "SubmissionSet01", //registryObject
            "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446", //identificationScheme
            "id_24", //id
            "XDSSubmissionSet.patientId", //name
            "IGNORED" //value
        );

        //Build association
        AssociationType1 association = new AssociationType1();
        association.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:RPLC");
        association.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Association");
        association.setId("ID_25276323_1");
        association.setSourceObject("SubmissionSet01");
        association.setTargetObject(extrinsic.getId());

        //Add submission status to assocation
        addSlot(association, "SubmissionSetStatus", new String [] { "Original" });

        //Build classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationNode("urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd");
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setClassifiedObject("SubmissionSet01");
        classification.setId("ID_25276323_3");

        //Build registry object
        ObjectFactory rimObjectFactory = new ObjectFactory();
        JAXBElement<ExtrinsicObjectType> extrinsicMetadata = rimObjectFactory.createExtrinsicObject(extrinsic);
        JAXBElement<RegistryPackageType> submission = rimObjectFactory.createRegistryPackage(registryPackage);
        JAXBElement<AssociationType1> associationObject = rimObjectFactory.createAssociation(association);
        JAXBElement<ClassificationType> classificationObject = rimObjectFactory.createClassification(classification);
        RegistryObjectListType registryList = new RegistryObjectListType();
        registryList.getIdentifiable().add(extrinsicMetadata);
        registryList.getIdentifiable().add(submission);
        registryList.getIdentifiable().add(associationObject);
        registryList.getIdentifiable().add(classificationObject);

        //Build document object
        Document document = new Document();
        document.setId(extrinsic.getId());
        document.setValue(docResponse.getDocumentResponse().get(0).getDocument());

        //Add request to body for submission
        SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
        submitObjects.setRegistryObjectList(registryList);
        request.setSubmitObjectsRequest(submitObjects);
        request.getDocument().add(document);

        return request;
    }

    /**
     * Add classification to submission object.
     *
     * @param registry
     * @param classifiedObject
     * @param classificationScheme
     * @param nodeRepresentation
     * @param id
     * @param name
     * @param slotNames
     * @param slotValues
     */
    private static void addClassification(
            RegistryObjectType registry,
            String classifiedObject,
            String classificationScheme,
            String nodeRepresentation,
            String id,
            String name,
            String [] slotNames,
            String [][] slotValues) {

        //Create classification
        ClassificationType classification = new ClassificationType();
        classification.setClassificationScheme(classificationScheme);
        classification.setNodeRepresentation(nodeRepresentation);
        classification.setClassifiedObject(classifiedObject);
        classification.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:Classification");
        classification.setId(id);

        //Classification name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            classification.setName(intlName);
        }

        //Slots
        for (int i = 0; i < slotNames.length; i++) {
            addSlot(classification, slotNames[i], slotValues[i]);
        }

        //Add classification
        registry.getClassification().add(classification);
    }

    /**
     * Add external identifier on submission object.
     *
     * @param registry
     * @param registryObject
     * @param identificationScheme
     * @param id
     * @param name
     * @param value
     */
    private static void addExternalIdentifier(
            RegistryObjectType registry,
            String registryObject,
            String identificationScheme,
            String id,
            String name,
            String value) {

        ExternalIdentifierType externalId = new ExternalIdentifierType();
        externalId.setObjectType("urn:oasis:names:tc:ebxml-regrep:ObjectType:RegistryObject:ExternalIdentifier");
        externalId.setRegistryObject(registryObject);
        externalId.setIdentificationScheme(identificationScheme);
        externalId.setId(id);
        externalId.setValue(value);

        //Identifier name
        if (name != null) {
            LocalizedStringType localString = new LocalizedStringType();
            localString.setValue(name);
            InternationalStringType intlName = new InternationalStringType();
            intlName.getLocalizedString().add(localString);
            externalId.setName(intlName);
        }

        //Add classification
        registry.getExternalIdentifier().add(externalId);
    }


    /**
     * Finds a slot in the document metadata.
     *
     * @param registry
     * @param slotName
     * @return
     */
    private SlotType1 findSlot(RegistryObjectType registry, String slotName) {
        SlotType1 result = null;

        for (SlotType1 slot : registry.getSlot()) {
            if (slot.getName().equals(slotName)) {
                result = slot;
                break;
            }
        }

        return result;
    }

    /**
     * Return a list of valid repository ids.
     * 
     * @return
     */
    private List<String> getValidRepositories() {
        List<String> repoIds = new LinkedList<String>();

        try {
            repoIds.add(PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, DYNAMIC_DOCUMENT_REPOSITORY_ID_PROP));
            repoIds.add(PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, INBOUND_DOCUMENT_REPOSITORY_ID_PROP));
            repoIds.add(PropertyAccessor.getProperty(REPOSITORY_PROPERTY_FILE, POLICY_REPOSITORY_ID_PROP));
        }
        catch (PropertyAccessException e) {
            log.error("Error accessing repository id properties.", e);
        }

        return repoIds;
    }

    /**
     * Pull repository id out of query request.
     * 
     * @param body
     * @return
     */
    private String pullRepositoryId(oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest body) {
        String repositoryIdValue = null;

        for (SlotType1 slot : body.getAdhocQuery().getSlot()) {
            if (XDS_REPOSITORY_ID_QUERY.equals(slot.getName())) {
                repositoryIdValue = slot.getValueList().getValue().get(0);
                break;
            }
        }

        return repositoryIdValue;
    }

    /**
     * Pull repository id out of retrieve request.
     * 
     * @param body
     * @return
     */
    private String pullRepositoryId(ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType body) {
        String repositoryIdValue = null;

        if (!body.getDocumentRequest().isEmpty()) {
            repositoryIdValue = body.getDocumentRequest().get(0).getRepositoryUniqueId();
        }

        return repositoryIdValue;
    }

    /**
     * Pull repository id out of document store.
     * 
     * @param body
     * @return
     */
    public String pullRepositoryId(ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType body) {
        ExtrinsicObjectType extrinsic = null;
        String repositoryIdValue = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            body.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find repositoryl id (if present)
                for (SlotType1 slot : extrinsic.getSlot()) {
                    if (XDS_REPOSITORY_ID.equals(slot.getName())) {
                        repositoryIdValue = slot.getValueList().getValue().get(0);
                        break;
                    }
                }
            }
        }

        return repositoryIdValue;
    }


    /**
     * Start a background job.
     * 
     * @param msg
     * @return ticket, detail
     */
    public static String[] startBackgroundJob(java.io.Serializable msg) {
        String result[] = new String[2];

        try {
            //Create job id
            Date today = new Date();
            String identity = String.valueOf(today.getTime())
                + String.valueOf(Math.round(Math.random() % 10000));
            log.debug("Scheduling job type '" + msg.getClass().getName()
                + "' with id: " + identity);

            // define the job and tie it to our HelloJob class
            XStream xstream = new XStream();
            JobDetail job = JobBuilder.newJob(DocumentManagerJob.class)
                .withIdentity(identity, "docmgr")
                .usingJobData(DOCMGR_JOB_MESSAGE, xstream.toXML(msg))
                .build();

            // Trigger the job to run now, and then repeat every 40 seconds
            Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(identity, "docmgr")
                .startNow()
                .build();

            // Tell quartz to schedule the job using our trigger
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler scheduler = sf.getScheduler("DocMgrScheduler");
            scheduler.scheduleJob(job, trigger);

            //Set response info
            result[0] = identity;
            result[1] = JOB_START_SUCCESS;
        }
        catch (Throwable t) {
            String errMsg = JOB_START_FAILURE + ": error occurred trying to start docmgr job.";
            log.error(errMsg, t);
            result[0] = JOB_FAILURE_ID;
            result[1] = errMsg;
        }

        return result;
    }


    /**
     * Insert a document unique Id if one does not already exist.  External
     * Identifier object should at least be in place.
     *
     * @param request
     * @return true if successful, false otherwise
     */
    private boolean createDocumentUniqueId(ProvideAndRegisterDocumentSetRequestType request) {

        String docUniqueId = null;
        ExternalIdentifierType externalId = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                ExtrinsicObjectType extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find doc unique identifier
                for (ExternalIdentifierType extId : extrinsic.getExternalIdentifier()) {
                    if ("XDSDocumentEntry.uniqueId".equals(extId.getName().getLocalizedString().get(0).getValue())) {
                        externalId = extId;
                        docUniqueId = extId.getValue();
                    }
                }
            }
        }

        //Check if external Id was found
        if (externalId == null) {
            return false;
        }

        //Check if docUniqueId needs filling
        if ((docUniqueId == null) || docUniqueId.isEmpty()) {
            docUniqueId = generateUniqueId(null).getUniqueId();
            externalId.setValue(docUniqueId);
        }

        return true;
    }

    /**
     * Add slot to submission object.
     *
     * @param registry - submission object
     * @param name - slot name
     * @param values - slot values
     */
    private static void addSlot(RegistryObjectType registry,
            String name, String [] values) {

        SlotType1 slot = new SlotType1();
        slot.setName(name);

        ValueListType valList = new ValueListType();
        for (String value : values) {
            valList.getValue().add(value);
        }

        slot.setValueList(valList);
        registry.getSlot().add(slot);
    }

    /**
     * Before storing, ensure that repositoryID is present.
     * 
     * @param request
     * @param repositoryId
     */
    private void insertRepositoryId(
            ProvideAndRegisterDocumentSetRequestType request,
            String repositoryId) {

        ExtrinsicObjectType extrinsic = null;
        SlotType1 repositoryIdSlot = null;

        //Pull out submit objects
        List<JAXBElement<? extends IdentifiableType>> objectList =
            request.getSubmitObjectsRequest().getRegistryObjectList().getIdentifiable();

        //Find extrinsic object
        for (JAXBElement<? extends IdentifiableType> object : objectList) {
            IdentifiableType identifiableType = object.getValue();
            if (identifiableType instanceof ExtrinsicObjectType) {
                extrinsic = (ExtrinsicObjectType) identifiableType;

                //Find repositoryl id (if present)
                for (SlotType1 slot : extrinsic.getSlot()) {
                    if (XDS_REPOSITORY_ID.equals(slot.getName())) {
                        repositoryIdSlot = slot;
                        break;
                    }
                }
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(extrinsic, XDS_REPOSITORY_ID, new String[] { repositoryId });
            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
    }

    /**
     * Before query, ensure repository id present.
     *
     * @param request
     * @param repositoryId
     */
    private void insertRepositoryIdQuery(
            AdhocQueryRequest request,
            String repositoryId) {

        SlotType1 repositoryIdSlot = null;

        //Find repositoryl id (if present)
        for (SlotType1 slot : request.getAdhocQuery().getSlot()) {
            if (XDS_REPOSITORY_ID_QUERY.equals(slot.getName())) {
                repositoryIdSlot = slot;
                break;
            }
        }

        //Create repository ID if not found
        if (repositoryIdSlot == null) {
            repositoryIdSlot = new SlotType1();
            addSlot(request.getAdhocQuery(), XDS_REPOSITORY_ID_QUERY, new String[] { repositoryId });
            return;
        }

        //Ensure repository ID is correct
        ValueListType valList = new ValueListType();
        valList.getValue().add(repositoryId);
        repositoryIdSlot.setValueList(valList);
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

}
