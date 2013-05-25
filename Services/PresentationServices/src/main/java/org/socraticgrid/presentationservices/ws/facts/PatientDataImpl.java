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
package org.socraticgrid.presentationservices.ws.facts;

import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.utils.factQuery.DemographicsQuery;
import org.socraticgrid.presentationservices.utils.factQuery.DiagnosesQuery;
import org.socraticgrid.presentationservices.utils.factQuery.MedicationsQuery;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.socraticgrid.kmrolib.KMROPatientTriples;
import org.socraticgrid.presentationservices.utils.factQuery.LabsQuery;
import org.socraticgrid.ps.PatientDataRequestType;

public class PatientDataImpl extends PatientDataService {

    private final String PATIENT = "patient";
    private final String ALLERGY = "allergy";
    private final String PROBLEM = "problem";
    private final String VITAL = "vital";
    private final String LAB = "lab";
    private final String MED = "med";
    private final String XRAY = "xray";
    private final String CONSULT = "consult";
    private final String PROCEDURE = "procedure";
    private final String SURGERY = "surgery";
    private final String DOCUMENT = "document";
    private final String ENCOUNTER = "encounter";
    private final String FACTOR = "factor";
    

//    private String FMQLServive = "http://172.31.5.104:8080/fmqlEP?fmql=";
//    private String MDWSService = "http://172.31.5.104/mdws2/EmrSvc.asmx";

    @Override
    public String getPatientData(PatientDataRequestType patientDataRequest)  {



        return this.getData(patientDataRequest);
    }


    /**
     * Per the incoming requested data source, get the corresponding service
     * endpoint from  the PresentationServices.properties file.
     *
     * PREREQ:  Following properties attributes needs to defined in
     *          PresentationServices.properties file.
     *
     *          For MDWS fact service:
                        MDWSService=http://xxx.xx.xx.xxx/mdws2/EmrSvc.asmx
                        MDWSUser=<the mdws service username>
                        MDWSPwd=<the mdws service password>
                        MDWSSite=<the site number>
     *
     *          For FMQL fact service:
                        FMQLServive=http://xxx.xx.xx.xxx:3030/fmqlEP?fmql=

     *
     *          For JENA/SPARQL fact service:
                        JENAServive=http://xxx.xx.xx.xxx:3030/data/sparql?query=
     * 
     * @param request
     * @return
     */
    public String getData(PatientDataRequestType request) {
        
        String dataURL = null;
        String mdwsUser = null;
        String mdwsPwd = null;
        String mdwsSite = null;
        String dataRequestType = "raw";

        String propsService = null;
        String propsUser = null;
        String propsPwd = null;
        String propsSite = null;

        // -----------------------------------------------------------------
        // SET the data server query URL based on incoming dataSource param.
        // -----------------------------------------------------------------
        if (request.getDataSource().startsWith("FMQL")) {

            if (request.getDataSource().endsWith("PROD")) {
                propsService = "FMQLService-PROD";
            } else {
                propsService = "FMQLService-DEV";
            }
            dataURL = PropertyHelper.getPropertyHelper().getProperty(propsService);

        } else if (request.getDataSource().startsWith("MDWS")) {

            if (request.getDataSource().endsWith("PROD")) {
                propsService = "MDWSService-PROD";
                propsUser = "MDWSUser-PROD";
                propsPwd = "MDWSPwd-PROD";
                propsSite = "MDWSSite-PROD";
            } else {
                propsService = "MDWSService-DEV";
                propsUser = "MDWSUser-DEV";
                propsPwd = "MDWSPwd-DEV";
                propsSite = "MDWSSite-DEV";
            }
            dataURL = PropertyHelper.getPropertyHelper().getProperty(propsService);
            
            // When given BOTH userId and pwd in the SOAP request, then use them
            if ((request != null) 
                && !CommonUtil.strNullorEmpty(request.getUserId())
                && !CommonUtil.strNullorEmpty(request.getToken()))
            {
                mdwsUser = request.getUserId();
                mdwsPwd = request.getToken();

                System.out.println("SOAP input: userId= "+ mdwsUser);
                System.out.println("SOAP input: token= "+ mdwsPwd);

            } else {
                mdwsUser = PropertyHelper.getPropertyHelper().getProperty(propsUser);
                mdwsPwd = PropertyHelper.getPropertyHelper().getProperty(propsPwd);

                System.out.println("PROPS input: MDWSUser= "+ mdwsUser);
                System.out.println("PROPS input: MDWSPwd= "+ mdwsPwd);
            }
            mdwsSite = PropertyHelper.getPropertyHelper().getProperty(propsSite);


            System.out.println("PROPS input: MDWSService= "+ dataURL);
            System.out.println("PROPS input: MDWSSite= "+ mdwsSite);

        } else if (request.getDataSource().startsWith("JENA")) {

            if (request.getDataSource().endsWith("PROD")) {
                propsService = "JENAService-PROD";
            } else {
                propsService = "JENAService-DEV";
            }
            dataURL = PropertyHelper.getPropertyHelper().getProperty(propsService);

        } else {
            dataRequestType = null;
            dataURL = null;
        }

        return getData(request, dataURL, dataRequestType, mdwsUser, mdwsPwd, mdwsSite);
    }
    
    public String getData(PatientDataRequestType request,
                           String dataURL, String dataRequestType,
                           String mUser, String mPwd, String mSite) {
        
        String result = null;
        String domain = request.getDomain();

        // ============================================
        // VERIFY THAT TOKEN BEIGN USED IS STILL VALID.
        //
        // NOTE:  NOT USED FOR NOW.
        //        User/Secutity authentication not yet fully designed.
        //
        // ============================================
//        if ((token == null) || (!SessionUtilities.verifyToken(token))) {
//            String errorMsg = "The token was not found, the session may have timed out.";
//            SessionUtilities.generateErrorRepresentation(errorMsg, "400", response);
//        }

        Map fieldMap = new HashMap();
        fieldMap.put("domain", domain);
        fieldMap.put("userId", request.getUserId());
        fieldMap.put("token", request.getToken());   // incoming password
        fieldMap.put("patientId", request.getPatientId());
        fieldMap.put("requestType", "raw");          // Is always raw for SPARQL
        fieldMap.put("itemId", request.getItemId());
        fieldMap.put("dataURL", dataURL);                    // Endpoint to data
        fieldMap.put("dataSource", request.getDataSource());    // FMQL, MDWS

        boolean useStubbedData = false;

        try {
            //------------ MDWS QUERY ---------------
            if (request.getDataSource().startsWith("MDWS")) {

                KMROPatientTriples triples = new KMROPatientTriples(dataURL, request.getDataSource());

                System.out.println("\n============================");
                result = triples.getMDWSData(request.getPatientId(),
                                             request.getDomain(),
                                             mUser, mPwd, mSite);
                
                System.out.println("\n"+result + "\n============================\n");

            }

            //------------ JENA QUERY ---------------
            else if (request.getDataSource().startsWith("JENA")) {

                KMROPatientTriples triples = new KMROPatientTriples(dataURL, request.getDataSource());

                System.out.println("\n============================");
//                result = triples.getSPARQLData(request.getPatientId(),
//                                               request.getDomain(),
//                                               mUser, mPwd, mSite);

                System.out.println("\n"+result + "\n============================\n");

            }

            //------------ FMQL QUERY ---------------
            else if (request.getDataSource().startsWith("FMQL")) {

//                //GET data for detail, list, or ecs
//                if (responseType.equalsIgnoreCase("ecs"))
//                {
//                    jsonRsp = EcsQuery.getInstance().createResponse(fieldMap, useStubbedEcs);
//
//                } else {

                if (domain.equalsIgnoreCase(this.MED)) {

                    result = MedicationsQuery.getInstance().createResponse(fieldMap, useStubbedData);

//                } else if (domain.equalsIgnoreCase("Immunizations")) {
//                    jsonRsp = ImmunizationsQuery.getInstance().createResponse(fieldMap, useStubbedData);

                } else if (domain.equalsIgnoreCase(this.LAB)) {
                    result = LabsQuery.getInstance().createResponse(fieldMap, useStubbedData);

//                } else if (domain.equalsIgnoreCase("Admissions")) {
//                    jsonRsp = AdmissionsQuery.getInstance().createResponse(fieldMap, useStubbedData);
//
//                } else if (domain.equalsIgnoreCase("Allergies")) {
//                    jsonRsp = AllergiesQuery.getInstance().createResponse(fieldMap, useStubbedData);


                } else if (domain.equalsIgnoreCase(this.PATIENT)) {
                    result = DemographicsQuery.getInstance().createResponse(fieldMap, useStubbedData);

                } else if (domain.equalsIgnoreCase(this.PROBLEM)) {
                    result = DiagnosesQuery.getInstance().createResponse(fieldMap, useStubbedData);

//                } else if (domain.equalsIgnoreCase("DocInpatient")) {
//                    jsonRsp = DocInpatientQuery.getInstance().createResponse(fieldMap, useStubbedData);


    /************/
    /* TMN - DOCUMENT is currently being used for OUTPATIENT
     *       domain = "Documentation" is too general.
     *       domain should be:  ClinicNotes, Directives
     *       and need to have relative instance class.


                } else if (domain.equalsIgnoreCase("DocDirectives")) {
                    xml = DocDirectives.getInstance().createResponse(fieldMap, useStubbedFacts);
    */

    //            } else if (domain.equalsIgnoreCase("ClinicNotes")) {
    //                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, useStubbedData);
    //
    //            } else if (domain.equalsIgnoreCase("Documentation")) {
    //                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, useStubbedData);
    ///************/
    //
    //            } else if (domain.equalsIgnoreCase("Equipment")) {
    //                jsonRsp = EquipmentsQuery.getInstance().createResponse(fieldMap, useStubbedData);
    //
    //            } else if (domain.equalsIgnoreCase("Imaging")) {
    //                jsonRsp = ImagingQuery.getInstance().createResponse(fieldMap, useStubbedData);
    //
    //            } else if (domain.equalsIgnoreCase("Procedures")) {
    //                jsonRsp = ProceduresQuery.getInstance().createResponse(fieldMap, useStubbedData);
    //
    //            } else if (domain.equalsIgnoreCase("Vitals")) {
    //                jsonRsp = VitalsQuery.getInstance().createResponse(fieldMap, useStubbedData);

                } else {
                    System.out.println("\n===>ERROR: GetPatientDataResource: Domain " + domain + " not supported.\n");
                    //TBD - Need better error msg return.
                }

                if (CommonUtil.strNullorEmpty(result)) {
                    System.out.println("\n===>ERROR: GetPatientData: No response for domain=" + domain
                            + " responseType=" + dataRequestType
                            + "query.\n");

                    //TBD - Need better error msg return.
                }

                //DBG ONLY
                //System.out.println("==> PS: jsonResp=\n"+jsonRsp);

    //        }

            }//end-if-request.getDataSource()

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PatientDataImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PatientDataImpl.class.getName()).log(Level.SEVERE, null, ex);
        }


        return result;
    }
}
