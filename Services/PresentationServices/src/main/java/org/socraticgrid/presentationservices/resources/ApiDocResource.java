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

package org.socraticgrid.presentationservices.resources;

import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.presentationservices.PresentationServicesApplication;
import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;

/**
 * @author  mep
 */
public class ApiDocResource extends BaseResource {

    /**
     * Creates a new ApiDocResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public ApiDocResource(Context context, Request request, Response response) {
        super(context, request, response);

//        if (checkApiCaller(request.getResourceRef().getQuery())!=true){
//           getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);
//           return;
//        }

        //we only have one type of representation - text (or html)
        getVariants().add(new Variant(MediaType.TEXT_PLAIN));
        getVariants().add(new Variant(MediaType.APPLICATION_XML));
    }

    /**
     * @see  org.restlet.resource.Resource#represent(org.restlet.resource.Variant)
     */
    @Override
    public Representation represent(Variant variant)
            throws ResourceException {

        
        String response = "LIST OF DEPLOYED RESOURCES PASTED FROM PresentationServicesApplication.java:\n\n" +
                            "\n/validateAccount " + 
                            "\n/setAccount " +

                            //INBOX MSGS APIs
                            "\n/getMessages " + 
                            "\n/getMessageDetail " + 
                            "\n/setMessages " + 
                            "\n/getDocuments " + 
                            "\n/getAddressBook " +

                            "\n/getCalendar " + 

                            "\n/getSurvey " + 
                            "\n/setSurvey " + 

                            //RISK MODEL PREDICTION APIs
                            "\n/getRiskModels " +
                            "\n/getRiskModelsDetail " +
                            "\n/setRiskModelFavorites " + 


                            //DX GUIDE APIs
                            "\n/startDiagnosticGuideProcess " + 
                            "\n/getDiagnosticGuideProcessStatus " + 
                            "\n/completeDiagnosticGuideProcess " + 
                            "\n/advanceDiagnosticGuideProcess " +
                            "\n/setDiagnosticActionStatus " + 
                            "\n/getDiagnosticGuideActionStatus " + 

                            //SIMULATION APIs
                            "\n/getPlanningModels " + 
                            "\n/searchConfigurationsResource " + 
                            "\n/getConfigurationDetail " + 
                            "\n/saveConfiguration " + 
                            "\n/getSimulations " + 
                            "\n/getSimulationDetail " +
                            "\n/commandSimulation " + 
                            "\n/launchSimulation " +
                            "\n/searchPlanResults " + 

                            "\n/getResultDetails " +
                            "\n/requestResultOperation " +

                            //FACTS-ECS APIs
                            "\n/getPatientData " +

                            //NON-PATIENT-SPECIFIC FACT DATA
                            "\n/getFactData " +

                            //PATIENT ID NOTIFICATION
                            "\n/notifyDSA " ;
        
        Representation rep = new StringRepresentation(response,
                MediaType.TEXT_PLAIN);

        return rep;
    }

    private void addRef(StringBuffer result, String refStr) {
        Reference ref = getRequest().getRootRef();
        ref.setPath("/PresentationServices" + refStr);
        result.append("\t" + ref.getTargetRef().toString() + "\n");

    }

}
