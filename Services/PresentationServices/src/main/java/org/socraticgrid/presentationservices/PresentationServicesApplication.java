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

package org.socraticgrid.presentationservices;

import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.resources.AdmissionsResource;
import org.socraticgrid.presentationservices.resources.ProblemsResource;
import org.socraticgrid.presentationservices.resources.MedicationsResource;
import org.socraticgrid.presentationservices.resources.TestResource;
import org.socraticgrid.presentationservices.resources.ApiDocResource;
import org.socraticgrid.presentationservices.resources.AllergiesResource;
import org.socraticgrid.presentationservices.resources.AppointmentsResource;
import org.socraticgrid.presentationservices.resources.DiagnosticImagingResource;
import org.socraticgrid.presentationservices.resources.DocumentsResource;
import org.socraticgrid.presentationservices.resources.EmergencyContactResource;
import org.socraticgrid.presentationservices.resources.EncounterDetailResource;
import org.socraticgrid.presentationservices.resources.EncounterNoteResource;
import org.socraticgrid.presentationservices.resources.LabsResource;
import org.socraticgrid.presentationservices.resources.LoginResource;
import org.socraticgrid.presentationservices.resources.ExternalCalendarResource;
import org.socraticgrid.presentationservices.resources.GetAddressBookResource;
import org.socraticgrid.presentationservices.resources.GetCalendarResource;
import org.socraticgrid.presentationservices.resources.diagnostics.GetDGProcessStatusResource;
import org.socraticgrid.presentationservices.resources.GetECSResource;
import org.socraticgrid.presentationservices.resources.messages.GetMessagesResource;

import org.socraticgrid.presentationservices.resources.facts.GetPatientDataResource;

import org.socraticgrid.presentationservices.resources.GetRequiredsResource;
import org.socraticgrid.presentationservices.resources.ImmunizationsResource;
import org.socraticgrid.presentationservices.resources.LocationClinicsResource;
import org.socraticgrid.presentationservices.resources.LocationScheduleResource;
import org.socraticgrid.presentationservices.resources.LocationWardsResource;
import org.socraticgrid.presentationservices.resources.PatientDemographicsResource;
import org.socraticgrid.presentationservices.resources.MailResource;
import org.socraticgrid.presentationservices.resources.MailActionResource;
import org.socraticgrid.presentationservices.resources.MailStatusResource;
import org.socraticgrid.presentationservices.resources.MaintainAccountResource;
import org.socraticgrid.presentationservices.resources.messages.GetMessageDetailResource;
import org.socraticgrid.presentationservices.resources.riskmodel.GetRiskModelsDetailResource;
import org.socraticgrid.presentationservices.resources.riskmodel.GetRiskModelsResource;
import org.socraticgrid.presentationservices.resources.VitalsResource;
import org.socraticgrid.presentationservices.resources.SchedulingResource;
import org.socraticgrid.presentationservices.resources.ToDoResource;
import org.socraticgrid.presentationservices.resources.PmrPreferencesResource;
import org.socraticgrid.presentationservices.resources.MobileResource;


import org.socraticgrid.presentationservices.resources.OrdersResource;
import org.socraticgrid.presentationservices.resources.PatientCensusResource;
import org.socraticgrid.presentationservices.resources.PatientsResource;
import org.socraticgrid.presentationservices.resources.ProcedureResource;
import org.socraticgrid.presentationservices.resources.ProvidersImageResource;
import org.socraticgrid.presentationservices.resources.ProvidersResource;
import org.socraticgrid.presentationservices.resources.RegistryResource;
import org.socraticgrid.presentationservices.resources.SMSResource;
import org.socraticgrid.presentationservices.resources.diagnostics.SetDiagnosticActionStatusResource;
import org.socraticgrid.presentationservices.resources.messages.SetMessagesResource;
import org.socraticgrid.presentationservices.resources.riskmodel.SetRiskModelFavoritesResource;
import org.socraticgrid.presentationservices.resources.diagnostics.StartDGProcessResource;
import org.socraticgrid.presentationservices.resources.SurveyResource;
import org.socraticgrid.presentationservices.resources.ValidateAccountResource;
import org.socraticgrid.presentationservices.resources.diagnostics.AdvanceDGProcessResource;
import org.socraticgrid.presentationservices.resources.diagnostics.CompleteDGProcessResource;
import org.socraticgrid.presentationservices.resources.diagnostics.GetDGActionStatusResource;
import org.socraticgrid.presentationservices.resources.messages.GetDocumentsResource;
import org.socraticgrid.presentationservices.resources.simulator.CommandSimulationResource;
import org.socraticgrid.presentationservices.resources.simulator.GetConfigurationDetailResource;
import org.socraticgrid.presentationservices.resources.simulator.GetPlanningModelsResource;
import org.socraticgrid.presentationservices.resources.simulator.GetResultDetailsResource;

import org.socraticgrid.presentationservices.resources.simulator.GetSimulationDetailResource;
import org.socraticgrid.presentationservices.resources.simulator.GetSimulationsResource;
import org.socraticgrid.presentationservices.resources.simulator.LaunchSimulationResource;
import org.socraticgrid.presentationservices.resources.simulator.RequestResultOperationResource;
import org.socraticgrid.presentationservices.resources.simulator.SaveConfigurationResource;
import org.socraticgrid.presentationservices.resources.simulator.SearchConfigurationResource;
import org.socraticgrid.presentationservices.resources.simulator.SearchPlanResultsResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.restlet.Application;
//import org.restlet.Context;
import org.restlet.Directory;
import org.restlet.Router;
import org.restlet.Restlet;
import org.restlet.data.LocalReference;
import org.restlet.Redirector;
import org.socraticgrid.presentationservices.resources.dsa.NotifyDSA;
import org.socraticgrid.presentationservices.resources.facts.GetFactDataResource;

/**
 *
 * @author mep
 */
public class PresentationServicesApplication extends Application {

    private static Log log = LogFactory.getLog(PresentationServicesApplication.class);
    private Router router;
    
    //create root
    @Override
    public Restlet createRoot() {

        //create router that routes each call to a new instance of PatientInfoResources
        router = new Router(getContext());

        router.attachDefault(ApiDocResource.class);

        log.info("STATIC RESOURCE DIR IS: ");
        log.info(PropertyHelper.getPropertyHelper().getProperty("staticResources"));

        router.attach("/", new Directory(
                getContext(),
                new LocalReference(PropertyHelper.getPropertyHelper().getProperty("staticResources"))));

        router.attach("/",
                new Redirector(router.getContext().createChildContext(),
                "/Resources",
                Redirector.MODE_CLIENT_SEE_OTHER));



        //PORTAL LANDING PAGE APIs
        router.attach("/getRequiredFields", GetRequiredsResource.class);
        //setFeedBack
        //getHelp

        //LOGIN/AUTHENTICATE APIs
        router.attach("/validateAccount", ValidateAccountResource.class);
        //getMicellaneous
        //getAccountInfo
        //getAccount
        router.attach("/setAccount", MaintainAccountResource.class);
        //getPatient
        //getProvider


        //INBOX MSGS APIs
        router.attach("/getMessages", GetMessagesResource.class);
        router.attach("/getMessageDetail", GetMessageDetailResource.class);
        router.attach("/setMessages", SetMessagesResource.class);
        router.attach("/getDocuments", GetDocumentsResource.class);
        router.attach("/getAddressBook", GetAddressBookResource.class);
        //searchMessages
        
        router.attach("/getCalendar", GetCalendarResource.class);
        //setCalendar

        router.attach("/getSurvey", SurveyResource.class);
        router.attach("/setSurvey", SurveyResource.class);



        //RISK MODEL PREDICTION APIs
        router.attach("/getRiskModels", GetRiskModelsResource.class);
        router.attach("/getRiskModelsDetail", GetRiskModelsDetailResource.class);
        router.attach("/setRiskModelFavorites", SetRiskModelFavoritesResource.class);


        //DX GUIDE APIs
        router.attach("/startDiagnosticGuideProcess", StartDGProcessResource.class);
        router.attach("/getDiagnosticGuideProcessStatus", GetDGProcessStatusResource.class);
        router.attach("/completeDiagnosticGuideProcess", CompleteDGProcessResource.class);
        router.attach("/advanceDiagnosticGuideProcess", AdvanceDGProcessResource.class);
        router.attach("/setDiagnosticActionStatus", SetDiagnosticActionStatusResource.class);
        router.attach("/getDiagnosticGuideActionStatus", GetDGActionStatusResource.class);

        //SIMULATION APIs
        router.attach("/getPlanningModels", GetPlanningModelsResource.class);
        router.attach("/searchConfigurationsResource", SearchConfigurationResource.class);
        router.attach("/getConfigurationDetail", GetConfigurationDetailResource.class);
        router.attach("/saveConfiguration", SaveConfigurationResource.class);
        router.attach("/getSimulations", GetSimulationsResource.class);
        router.attach("/getSimulationDetail", GetSimulationDetailResource.class);
        router.attach("/commandSimulation", CommandSimulationResource.class);
        router.attach("/launchSimulation", LaunchSimulationResource.class);
        router.attach("/searchPlanResults", SearchPlanResultsResource.class);

        router.attach("/getResultDetails", GetResultDetailsResource.class);
        router.attach("/requestResultOperation", RequestResultOperationResource.class);

        //FACTS-ECS APIs
        router.attach("/getPatientData", GetPatientDataResource.class);

        //NON-PATIENT-SPECIFIC FACT DATA
        router.attach("/getFactData", GetFactDataResource.class);


        //PATIENT ID NOTIFICATION
        router.attach("/notifyDSA", NotifyDSA.class);

        //FACTS - KMR1
        /*
        router.attach("/Allergies", AllergiesResource.class);
        router.attach("/Admissions", AdmissionsResource.class);
        router.attach("/Appointments", AppointmentsResource.class);
        router.attach("/Demographics", PatientDemographicsResource.class);
        router.attach("/DiagnosticImaging", DiagnosticImagingResource.class);
        router.attach("/Documents", DocumentsResource.class);
        router.attach("/EmergencyContact", EmergencyContactResource.class);
        router.attach("/EncounterDetail", EncounterDetailResource.class);
        router.attach("/Encounter/Detail", EncounterDetailResource.class);
        router.attach("/Encounter/Note", EncounterNoteResource.class);
        router.attach("/Immunizations", ImmunizationsResource.class);
        router.attach("/Labs", LabsResource.class);
        router.attach("/LocationSchedule", LocationScheduleResource.class);
        router.attach("/Locations/Clinics", LocationClinicsResource.class);
        router.attach("/Locations/Wards", LocationWardsResource.class);
        router.attach("/Orders", OrdersResource.class);
        router.attach("/PatientCensus", PatientCensusResource.class);
        router.attach("/Patients", PatientsResource.class);
        router.attach("/Medications", MedicationsResource.class);
        router.attach("/Problems", ProblemsResource.class);
        router.attach("/Procedures", ProcedureResource.class);
        router.attach("/Provider", ProvidersResource.class);
        router.attach("/Providers", ProvidersResource.class);
        router.attach("/Providers/Image", ProvidersImageResource.class);
        router.attach("/Todos", ToDoResource.class);
        router.attach("/Vitals", VitalsResource.class);
         * 
         */

        //router.attach("/getECS", GetECSResource.class);
        //router.attach("/MessageDetail", GetMessageDetailResource.class);
        //router.attach("/Calendar/External", ExternalCalendarResource.class);
        //router.attach("/Login", LoginResource.class);
        //router.attach("/Mail", MailResource.class);
        //router.attach("/Mail/Status", MailStatusResource.class);
        //router.attach("/MailAction", MailActionResource.class);
        //router.attach("/Mail/Action", MailActionResource.class);
        //router.attach("/Mobile", MobileResource.class);
        //router.attach("/PmrPreferences", PmrPreferencesResource.class);
        //router.attach("/Schedules", SchedulingResource.class);


        //TEST ONLY - this one is for testing - sandbox related
        //router.attach("/test", TestResource.class);
        //router.attach("/SMSResource", SMSResource.class);
        //router.attach("/RegistryResource", RegistryResource.class);
        
        return router;
    }
   
    
}
