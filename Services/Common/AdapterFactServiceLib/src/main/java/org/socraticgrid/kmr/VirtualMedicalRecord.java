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

package org.socraticgrid.kmr;

import org.socraticgrid.adapter.fact.AdmissionFactType;
import org.socraticgrid.adapter.fact.AllergyFactType;
import org.socraticgrid.adapter.fact.AppointmentFactType;
import org.socraticgrid.adapter.fact.DiagnosisFactType;
import org.socraticgrid.adapter.fact.ImmunizationFactType;
import org.socraticgrid.adapter.fact.PersonFactType;
import org.socraticgrid.adapter.fact.SupportSourceFactType;
import org.socraticgrid.adapter.fact.MedicationFactType;
import org.socraticgrid.adapter.fact.ProblemFactType;
import org.socraticgrid.adapter.fact.ProcedureFactType;
import org.socraticgrid.adapter.fact.ResultFactType;
import org.socraticgrid.adapter.fact.VitalFactType;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Duane DeCouteau
 */
public class VirtualMedicalRecord {
    private String patientId;
    private XMLGregorianCalendar dateStoreCreated;
    private PersonFactType demographics;
    private List<SupportSourceFactType> supportSources;
    private List<AllergyFactType> allergies;
    private List<MedicationFactType> medications;
    private List<ProblemFactType> problems;
    private List<ResultFactType> results;
    private List<ProcedureFactType> procedures;
    private List<AppointmentFactType> appointments;
    private List<AdmissionFactType> admissions;
    private List<DiagnosisFactType> diagnoses;
    private List<ImmunizationFactType> immunizations;
    private List<VitalFactType> vitals;
    //add additional domains as they become available

    public VirtualMedicalRecord() {
    }

    /**
     * @return the patientId
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the dateStoreCreated
     */
    public XMLGregorianCalendar getDateStoreCreated() {
        return dateStoreCreated;
    }

    /**
     * @param dateStoreCreated the dateStoreCreated to set
     */
    public void setDateStoreCreated(XMLGregorianCalendar dateStoreCreated) {
        this.dateStoreCreated = dateStoreCreated;
    }

    /**
     * @return the demographics
     */
    public PersonFactType getDemographics() {
        return demographics;
    }

    /**
     * @param demographics the demographics to set
     */
    public void setDemographics(PersonFactType demographics) {
        this.demographics = demographics;
    }

    /**
     * @return the support Sources
     */
    public List<SupportSourceFactType> getSupportSources() {
        if (supportSources == null) {
            supportSources = new ArrayList<SupportSourceFactType>();
        }
        return supportSources;
    }

    /**
     * @return the allergies
     */
    public List<AllergyFactType> getAllergies() {
        if (allergies == null) {
            allergies = new ArrayList<AllergyFactType>();
        }
        return allergies;
    }

    /**
     * @return the medications
     */
    public List<MedicationFactType> getMedications() {
        if (medications == null) {
            medications = new ArrayList<MedicationFactType>();
        }
        return medications;
    }

    /**
     * @return the problems
     */
    public List<ProblemFactType> getProblems() {
        if (problems == null) {
            problems = new ArrayList<ProblemFactType>();
        }
        return problems;
    }

    /**
     * @return the results
     */
    public List<ResultFactType> getResults() {
        if (results == null) {
            results = new ArrayList<ResultFactType>();
        }
        return results;
    }

    /**
     * @return procedures from VMR
     */
    public List<ProcedureFactType> getProcedures() {
        if (procedures == null) {
            procedures = new ArrayList<ProcedureFactType>();
        }
        return procedures;
    }

    /**
     * @return appointments from VMR
     */
    public List<AppointmentFactType> getAppointments() {
        if (appointments == null) {
            appointments = new ArrayList<AppointmentFactType>();
        }
        return appointments;
    }

    /**
     * @return admissions from VMR
     */
    public List<AdmissionFactType> getAdmissions() {
        if (admissions == null) {
            admissions = new ArrayList<AdmissionFactType>();
        }
        return admissions;
    }

    /**
     * @return immunizations from VMR
     */
    public List<ImmunizationFactType> getImmunizations() {
        if (immunizations == null) {
            immunizations = new ArrayList<ImmunizationFactType>();
        }
        return immunizations;
    }

    /**
     * @return diagnoses from VMR
     */
    public List<DiagnosisFactType> getDiagnoses() {
        if (diagnoses == null) {
            diagnoses = new ArrayList<DiagnosisFactType>();
        }
        return diagnoses;
    }

    /**
     * @return vitals from VMR
     */
    public List<VitalFactType> getVitals() {
        if (vitals == null) {
            vitals = new ArrayList<VitalFactType>();
        }
        return vitals;
    }
}
