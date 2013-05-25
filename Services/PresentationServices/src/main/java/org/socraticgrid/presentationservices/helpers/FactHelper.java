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

package org.socraticgrid.presentationservices.helpers;

import java.util.List;

/**
 *
 * @author Sushma
 */
public class FactHelper {
    private String providerName;
    private String factDate;
    private String factLabelName;
    private String specimenDate;
    private String resultStatus;
    private String patientId;
    private List reactions;
    private String allergyName;
    private String allergyType;
    private String adverseEventDate;
    private int factNo;
    private String code;
    private String reportDate;
    private String itemId;
    private String onsetDate;
    private String recordDate;
    private String firstName;
    private String middleName;
    private String lastName;
    private String suffix;
    private String dateOfBirth;
    private String age;
    private String sex;
    private String emergencyContact;
    private String relationship;
    private String orderDate;
    private String medication;
    private String orderExpirationDate;
    private String refills;
    private String codeSystem;
    private String codeSystemName;
    private String label;
    private String resultDate;
    private String orderingProvider;
    private String administeredDate;
    private String procedureDate;
    private String treatingProviders;
    private String problemDate;
    private String resolutionDate;
    private String problemType;
    private String codedProblemStatus;
    private String sigFreeText;
    private String codeSigRouteLabel;
    private String sigDoseValue;
    private String sigDoseUnit;
    private String sigFrequencyValue;
    private String sigFrequencyUnit;
    private String sigDuration;
    private String codedAdministrationUnitCodeLabel;
    private String communityId;
    private String resultValue;
    private String resultValueUnit;
    private String type;
    private String source;
    private String accessionDate;
    private String panelStatus;

    public String getPanelStatus() {
        return panelStatus;
    }

    public void setPanelStatus(String panelStatus) {
        this.panelStatus = panelStatus;
    }

    public String getAccessionDate() {
        return accessionDate;
    }

    public void setAccessionDate(String accessionDate) {
        this.accessionDate = accessionDate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultValueUnit() {
        return resultValueUnit;
    }

    public void setResultValueUnit(String resultValueUnit) {
        this.resultValueUnit = resultValueUnit;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCodeSigRouteLabel() {
        return codeSigRouteLabel;
    }

    public void setCodeSigRouteLabel(String codeSigRouteLabel) {
        this.codeSigRouteLabel = codeSigRouteLabel;
    }

    public String getCodedAdministrationUnitCodeLabel() {
        return codedAdministrationUnitCodeLabel;
    }

    public void setCodedAdministrationUnitCodeLabel(String codedAdministrationUnitCodeLabel) {
        this.codedAdministrationUnitCodeLabel = codedAdministrationUnitCodeLabel;
    }

    public String getSigDoseUnit() {
        return sigDoseUnit;
    }

    public void setSigDoseUnit(String sigDoseUnit) {
        this.sigDoseUnit = sigDoseUnit;
    }

    public String getSigDoseValue() {
        return sigDoseValue;
    }

    public void setSigDoseValue(String sigDoseValue) {
        this.sigDoseValue = sigDoseValue;
    }

    public String getSigDuration() {
        return sigDuration;
    }

    public void setSigDuration(String sigDuration) {
        this.sigDuration = sigDuration;
    }

    public String getSigFreeText() {
        return sigFreeText;
    }

    public void setSigFreeText(String sigFreeText) {
        this.sigFreeText = sigFreeText;
    }

    public String getSigFrequencyUnit() {
        return sigFrequencyUnit;
    }

    public void setSigFrequencyUnit(String sigFrequencyUnit) {
        this.sigFrequencyUnit = sigFrequencyUnit;
    }

    public String getSigFrequencyValue() {
        return sigFrequencyValue;
    }

    public void setSigFrequencyValue(String sigFrequencyValue) {
        this.sigFrequencyValue = sigFrequencyValue;
    }

    public String getCodedProblemStatus() {
        return codedProblemStatus;
    }

    public void setCodedProblemStatus(String codedProblemStatus) {
        this.codedProblemStatus = codedProblemStatus;
    }

    public String getProblemType() {
        return problemType;
    }

    public void setProblemType(String problemType) {
        this.problemType = problemType;
    }

    public String getProblemDate() {
        return problemDate;
    }

    public void setProblemDate(String problemDate) {
        this.problemDate = problemDate;
    }

    public String getResolutionDate() {
        return resolutionDate;
    }

    public void setResolutionDate(String resolutionDate) {
        this.resolutionDate = resolutionDate;
    }

    public String getProcedureDate() {
        return procedureDate;
    }

    public void setProcedureDate(String procedureDate) {
        this.procedureDate = procedureDate;
    }

    public String getTreatingProviders() {
        return treatingProviders;
    }

    public void setTreatingProviders(String testingProviders) {
        this.treatingProviders = testingProviders;
    }

    public String getAdministeredDate() {
        return administeredDate;
    }

    public void setAdministeredDate(String administeredDate) {
        this.administeredDate = administeredDate;
    }
    
    public String getOrderingProvider() {
        return orderingProvider;
    }

    public void setOrderingProvider(String orderingProvider) {
        this.orderingProvider = orderingProvider;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public void setCodeSystem(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    public void setCodeSystemName(String codeSystemName) {
        this.codeSystemName = codeSystemName;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRefills() {
        return refills;
    }

    public void setRefills(String refills) {
        this.refills = refills;
    }

    public String getOrderExpirationDate() {
        return orderExpirationDate;
    }

    public void setOrderExpirationDate(String orderExpirationDate) {
        this.orderExpirationDate = orderExpirationDate;
    }

    public String getMedication() {
        return medication;
    }

    public void setMedication(String medication) {
        this.medication = medication;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }


    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAdverseEventDate() {
        return adverseEventDate;
    }

    public void setAdverseEventDate(String adverseEventDate) {
        this.adverseEventDate = adverseEventDate;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getAllergyType() {
        return allergyType;
    }

    public void setAllergyType(String allergyType) {
        this.allergyType = allergyType;
    }

    public String getOnsetDate() {
        return onsetDate;
    }

    public void setOnsetDate(String onsetDate) {
        this.onsetDate = onsetDate;
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getFactNo() {
        return factNo;
    }

    public void setFactNo(int factNo) {
        this.factNo = factNo;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    public List getReactions() {
        return reactions;
    }

    public void setReactions(List reactions) {
        this.reactions = reactions;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getSpecimenDate() {
        return specimenDate;
    }

    public void setSpecimenDate(String specimanDate) {
        this.specimenDate = specimanDate;
    }

    public String getFactDate() {
        return factDate;
    }

    public void setFactDate(String factDate) {
        this.factDate = factDate;
    }

    public String getFactLabelName() {
        return factLabelName;
    }

    public void setFactLabelName(String factLabelName) {
        this.factLabelName = factLabelName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

}
