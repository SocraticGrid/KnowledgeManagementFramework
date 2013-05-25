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
import org.socraticgrid.common.docmgr.DocumentInfoType;
import org.socraticgrid.docmgr.msgobject.NHINRetrieveMessage;
import org.socraticgrid.common.nhinccommon.AssertionType;
import org.socraticgrid.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType;
import ihe.iti.xds_b._2007.RetrieveDocumentSetRequestType.DocumentRequest;
import ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType;
import java.util.Date;
import javax.xml.bind.JAXBElement;
import oasis.names.tc.ebxml_regrep.xsd.lcm._3.SubmitObjectsRequest;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.AssociationType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ClassificationType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExternalIdentifierType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ExtrinsicObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.InternationalStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.LocalizedStringType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ObjectFactory;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectListType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryObjectType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.RegistryPackageType;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.ValueListType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles the download of new documents.  Steps:
 * <ol>
 *   <li>Perform gateway retrieve of document
 *   <li>Store document in local repository
 *   <li>Notify requestor that document is available
 * </ol>
 * @author cmatser
 */
public class NHINRetrieveProcessor {

    /** XDS constants. */
    public static final String XDS_ORIG_DOC_UNIQUE_ID = "urn:org:socraticgrid:xds:OrigDocumentUniqueId";
    public static final String XDS_ORIG_HOME_ID = "urn:org:socraticgrid:xds:OrigHomeCommunityId";
    public static final String XDS_ORIG_REPOSITORY_ID = "urn:org:socraticgrid:xds:OrigRepositoryUniqueId";
    public static final String XDS_REPOSITORY_ID = "repositoryUniqueId";

    /** Logging. */
    private static Log log = LogFactory.getLog(NHINRetrieveProcessor.class);

    public NHINRetrieveProcessor() { }

    public void handleRetrieveMessage(String ticket, NHINRetrieveMessage msg) {

        log.debug("Handling retrieve document for ticket: " + ticket);

        try { // Call Web Service Operation
            XStream xstream = new XStream();
            xstream.alias("DocumentInfoType", DocumentInfoType.class);
            xstream.alias("ExtrinsicObjectType", ExtrinsicObjectType.class);
            xstream.alias("AssertionType", AssertionType.class);

            javax.xml.ws.Service myService = new ServiceUtil().createService(
                "EntityDocRetrieve.wsdl",
                "urn:org:socraticgrid:entitydocretrieve",
                "EntityDocRetrieve");
            org.socraticgrid.entitydocretrieve.EntityDocRetrievePortType port = myService.getPort(
                new javax.xml.namespace.QName("urn:org:socraticgrid:entitydocretrieve", "EntityDocRetrievePortSoap"),
                org.socraticgrid.entitydocretrieve.EntityDocRetrievePortType.class);
            ((javax.xml.ws.BindingProvider)port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                PropertyAccessor.getProperty(
                    DocumentManagerImpl.REPOSITORY_PROPERTY_FILE,
                    DocumentManagerImpl.NHINDOCRETRIEVE_ENDPOINT_PROP));

            RespondingGatewayCrossGatewayRetrieveRequestType gatewayRetrieveRequest = new RespondingGatewayCrossGatewayRetrieveRequestType();
            gatewayRetrieveRequest.setAssertion((AssertionType) xstream.fromXML(msg.getAssertionXML()));
            DocumentRequest docRequest = new DocumentRequest();
            docRequest.setHomeCommunityId(msg.getRemoteHomeCommunityId());
            docRequest.setRepositoryUniqueId(msg.getRemoteRepositoryId());
            docRequest.setDocumentUniqueId(msg.getRemoteDocumentUniqueId());
            RetrieveDocumentSetRequestType docRequestSet = new RetrieveDocumentSetRequestType();
            docRequestSet.getDocumentRequest().add(docRequest);
            gatewayRetrieveRequest.setRetrieveDocumentSetRequest(docRequestSet);

            log.info("Beginning retrieve request for document: " + docRequest.getDocumentUniqueId()
                        + ", in repository: " + docRequest.getRepositoryUniqueId()
                        + ", in home comm id: " + docRequest.getHomeCommunityId());
            RetrieveDocumentSetResponseType result = port.respondingGatewayCrossGatewayRetrieve(gatewayRetrieveRequest);

            //Store result in local repository
            storeDocument(
                msg.getLocalHomeCommunityId(), msg.getLocalRepositoryId(),
                msg.getLocalDocumentUniqueId(), msg.getLocalPatientId(),
                (ExtrinsicObjectType) xstream.fromXML(msg.getExtrinsicXML()), result);

            //Notify caller of status
            doCallback(true, "Success.", ticket,
                msg.getCallbackURL(),
                (DocumentInfoType) xstream.fromXML(msg.getDocumentInfoXML()));

        } catch (Exception e) {
            //Handle custom exceptions here
            log.error("Error handling NHIN task message.", e);

            //Notify caller of status
            doCallback(true, e.getMessage(), ticket,
                msg.getCallbackURL(), null);
        }
    }

    private void storeDocument(
            String localHomeCommunityId, String localRepositoryId,
            String localDocumentUniqueId, String localPatientId,
            ExtrinsicObjectType gatewayExtrinsic, RetrieveDocumentSetResponseType result) {

        try {
            ProvideAndRegisterDocumentSetRequestType request = new ProvideAndRegisterDocumentSetRequestType();

            //Build document metadata from original replacing some key values with
            // local ones.
            ExtrinsicObjectType extrinsic = new ExtrinsicObjectType();
            extrinsic.setId("Document1");
            extrinsic.setMimeType(gatewayExtrinsic.getMimeType());
            extrinsic.setObjectType(gatewayExtrinsic.getObjectType());
            extrinsic.setName(gatewayExtrinsic.getName());
            extrinsic.setDescription(gatewayExtrinsic.getDescription());

            //Add slots (overwrite some w/local values)
            SlotType1 origHomeIdSlot, origReposIdSlot, origDocIdSlot, reposIdSlot;
            origHomeIdSlot = origReposIdSlot = origDocIdSlot = reposIdSlot = null;
            ValueListType valList;
            for (SlotType1 gatewaySlot : gatewayExtrinsic.getSlot()) {
                //Hold on to some slots of interest
                if (XDS_ORIG_HOME_ID.equals(gatewaySlot.getName())) {
                    origHomeIdSlot = gatewaySlot;
                }
                else if (XDS_ORIG_REPOSITORY_ID.equals(gatewaySlot.getName())) {
                    origReposIdSlot = gatewaySlot;
                }
                else if (XDS_ORIG_DOC_UNIQUE_ID.equals(gatewaySlot.getName())) {
                    origDocIdSlot = gatewaySlot;
                }
                else if (XDS_REPOSITORY_ID.equals(gatewaySlot.getName())) {
                    reposIdSlot = gatewaySlot;
                }

                //add the slot
                extrinsic.getSlot().add(gatewaySlot);
            }

            //Override/add original home community id
            if (origHomeIdSlot == null) {
                origHomeIdSlot = new SlotType1();
                origHomeIdSlot.setName(XDS_ORIG_HOME_ID);
                extrinsic.getSlot().add(origHomeIdSlot);
            }
            valList = new ValueListType();
            valList.getValue().add(result.getDocumentResponse().get(0).getHomeCommunityId());
            origHomeIdSlot.setValueList(valList);

            //Override/add original repository id
            if (origReposIdSlot == null) {
                origReposIdSlot = new SlotType1();
                origReposIdSlot.setName(XDS_ORIG_REPOSITORY_ID);
                extrinsic.getSlot().add(origReposIdSlot);
            }
            valList = new ValueListType();
            valList.getValue().add(result.getDocumentResponse().get(0).getRepositoryUniqueId());
            origReposIdSlot.setValueList(valList);

            //Override/add original document unique id
            if (origDocIdSlot == null) {
                origDocIdSlot = new SlotType1();
                origDocIdSlot.setName(XDS_ORIG_DOC_UNIQUE_ID);
                extrinsic.getSlot().add(origDocIdSlot);
            }
            valList = new ValueListType();
            valList.getValue().add(result.getDocumentResponse().get(0).getDocumentUniqueId());
            origDocIdSlot.setValueList(valList);

            //Override/Add repository id
            if (reposIdSlot == null) {
                reposIdSlot = new SlotType1();
                reposIdSlot.setName(XDS_REPOSITORY_ID);
                extrinsic.getSlot().add(reposIdSlot);
            }
            valList = new ValueListType();
            valList.getValue().add(localRepositoryId);
            reposIdSlot.setValueList(valList);

            //Add classifications
            extrinsic.getClassification().addAll(gatewayExtrinsic.getClassification());

            //Add patientId identifier
            addExternalIdentifier(extrinsic,
                "Document1", //registryObject
                "urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427", //identificationScheme
                "id_9", //id
                "XDSDocumentEntry.patientId", //name
                localPatientId + "^^^&" + localHomeCommunityId + "&ISO" //value
            );

            //Add uniqueId identifier
            addExternalIdentifier(extrinsic,
                "Document1", //registryObject
                "urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab", //identificationScheme
                "id_10", //id
                "XDSDocumentEntry.uniqueId", //name
                localDocumentUniqueId //value
            );

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
                    new String [] { "^DocumentManager^GatewayRetrieve^Automated^^" },
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
            association.setAssociationType("urn:oasis:names:tc:ebxml-regrep:AssociationType:HasMember");
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
            document.setValue(result.getDocumentResponse().get(0).getDocument());

            //Add request to body for submission
            SubmitObjectsRequest submitObjects = new SubmitObjectsRequest();
            submitObjects.setRegistryObjectList(registryList);
            request.setSubmitObjectsRequest(submitObjects);
            request.getDocument().add(document);

            RegistryResponseType response = new DocumentManagerImpl().documentManagerStoreDocument(request);
        }
        catch (Exception e) {
            log.error("Error storing gateway document.", e);
        }

    }

    private void doCallback(boolean success, String detail, String ticket, String requester, DocumentInfoType documentInfo) {

        try { // Call Web Service Operation
            org.socraticgrid.docmgrrequester.DocMgrRequester service = new org.socraticgrid.docmgrrequester.DocMgrRequester();
            org.socraticgrid.docmgrrequester.DocMgrRequesterPortType port = service.getDocMgrRequesterPortSoap11();
            ((javax.xml.ws.BindingProvider) port).getRequestContext().put(
                javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                requester);

            //Initialize WS operation arguments here
            org.socraticgrid.common.docmgr.DocDownloadInfoType docAvailableRequest = new org.socraticgrid.common.docmgr.DocDownloadInfoType();
            docAvailableRequest.setTicket(ticket);
            docAvailableRequest.setSuccess(success);
            docAvailableRequest.setDetail(detail);
            docAvailableRequest.setDocInfo(documentInfo);

            //Process result here
            org.socraticgrid.common.docmgr.DocAvailableResponseType result = port.docAvailable(docAvailableRequest);
        } catch (Exception e) {
            //Handle custom exceptions here
            log.error("Error notifying document requestor ticket complete: " + ticket, e);
        }

    }

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

        //Add identifier
        registry.getExternalIdentifier().add(externalId);
    }

}