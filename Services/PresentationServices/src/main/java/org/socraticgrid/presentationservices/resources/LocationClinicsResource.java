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


import org.socraticgrid.presentationservices.helpers.JSONHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import org.restlet.Context;

import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;


/**
 * @author  markpitman
 */
public class LocationClinicsResource extends BaseResource
{

    /** DOCUMENT ME! */
    private static int currentId = 0;

    /** DOCUMENT ME! */
    private static JSONArray array;

    static
    {

        try
        {

            array = new JSONArray();

            //TODO: Replace Dummy data with real patients
            currentId = 0;
            array.put(getClinic("11110-6", "Allergy Clinic"));
            array.put(getClinic("11166-8", "Ambulatory Surgery Center"));
            array.put(getClinic("11145-2", "Behavioral Health Clinic"));
            array.put(getClinic("11147-8", "Blood Collection Center"));
            array.put(getClinic("11112-2", "Cardiac Rehabilitation Center"));
            array.put(getClinic("11113-0", "Cardiology Clinic"));
            array.put(getClinic("11148-6", "Continence Clinic"));
            array.put(getClinic("11115-5", "Dermatology Clinic"));
            array.put(getClinic("11207-0", "Donor Apheresis Center"));
            array.put(getClinic("11108-0", "Emergency Department"));
            array.put(getClinic("11116-3", "Diabetes/Endocrinology Clinic"));
            array.put(getClinic("11117-1", "Family Medicine Clinic"));
            array.put(getClinic("11118-9",
                    "Outpatient Gastrointestinal (GI) Clinic"));
            array.put(getClinic("11120-5", "Outpatient Medical Clinic"));
            array.put(getClinic("11121-3", "Gynecology Clinic"));
            array.put(getClinic("11200-5",
                    "Outpatient Hematology/Oncology Clinic"));
            array.put(getClinic("11017-3", "Hyperbaric Oxygen Center"));
            array.put(getClinic("11018-1", "Infusion Center"));
            array.put(getClinic("11122-1", "Genetics Clinic"));
            array.put(getClinic("11029-8", "Medical/Surgical Critical Care"));
            array.put(getClinic("11135-3", "Nephrology clinic"));
            array.put(getClinic("11123-9", "Neurology Clinic"));
            array.put(getClinic("11151-0", "Occupational Health Clinic"));
            array.put(getClinic("11152-8", "Occupational Therapy Clinic"));
            array.put(getClinic("11124-7", "Ophthalmology Clinic"));
            array.put(getClinic("11125-4", "Orthopedic Clinic"));
            array.put(getClinic("11149-4", "Ostomy Clinic"));
            array.put(getClinic("11126-2", "Ear, Nose, Throat Clinic"));
            array.put(getClinic("11150-2", "Outpatient Dental Clinic"));
            array.put(getClinic("11153-6", "Outpatient Hemodialysis Clinic"));
            array.put(getClinic("11154-4", "Outpatient HIV Clinic"));
            array.put(getClinic("11167-6",
                    "Outpatient Pediatric Surgery Center"));
            array.put(getClinic("11168-4",
                    "Outpatient Plastic Surgery Center"));
            array.put(getClinic("11155-1", "Outpatient Rehabilitation Clinic"));
            array.put(getClinic("11127-0", "Pain Clinic"));
            array.put(getClinic("11146-0",
                    "Pediatric Behavioral Health Clinic"));
            array.put(getClinic("11129-6", "Pediatric Cardiology Center"));
            array.put(getClinic("11128-8", "Pediatric Clinic"));
            array.put(getClinic("11130-4", "Pediatric Dental Clinic"));
            array.put(getClinic("11131-2", "Pediatric Dermatology Clinic"));
            array.put(getClinic("11132-0",
                    "Pediatric Diabetes/Endocrinology Clinic"));
            array.put(getClinic("11091-8", "Pediatric Dialysis SCA"));
            array.put(getClinic("11109-8", "Pediatric Emergency Department"));
            array.put(getClinic("11119-7",
                    "Pediatric Gastrointestinal Clinic"));
            array.put(getClinic("11136-1",
                    "Pediatric Hematology/Oncology Clinic"));
            array.put(getClinic("11089-2",
                    "Pediatric Hematology/Oncology SCA "));
            array.put(getClinic("11213-8",
                    "Pediatric long-term acute care (LTAC)"));
            array.put(getClinic("11137-9", "Pediatric Nephrology Clinic"));
            array.put(getClinic("11133-8", "Pediatric Orthopedic Clinic"));
            array.put(getClinic("11138-7", "Pediatric Rheumatology Clinic"));
            array.put(getClinic("11134-6", "Pediatric Scoliosis Clinic"));
            array.put(getClinic("11202-1", "Physical Therapy Clinic"));
            array.put(getClinic("11140-3", "Podiatry Clinic"));
            array.put(getClinic("11156-9", "Prenatal Clinic"));
            array.put(getClinic("11157-7", "Pulmonary Clinic"));
            array.put(getClinic("11142-9", "Rheumatology Clinic"));
            array.put(getClinic("11201-3", "Scoliosis clinic"));
            array.put(getClinic("11158-5", "Speech Therapy Clinic"));
            array.put(getClinic("11143-7", "Surgical Services Clinic"));
            array.put(getClinic("11160-1", "Urgent Care Center"));
            array.put(getClinic("11139-5", "Well Baby Clinic"));
            array.put(getClinic("11144-5", "Wound Center"));
            array.put(getClinic("11159-3", "Wound Ostomy Continence Clinic"));


        }
        catch (Exception e)
        {
            //Should Never occur
        }
    }

    /** DOCUMENT ME! */
    private String apiKey;

    /** DOCUMENT ME! */
    private String jsonRequestString = "";

    //we had a default user/patient coded for development - but commenting that out
    /** DOCUMENT ME! */
    private String locationId = "";

    /**
     * Creates a new LocationClinicsResource object.
     *
     * @param  context   DOCUMENT ME!
     * @param  request   DOCUMENT ME!
     * @param  response  DOCUMENT ME!
     */
    public LocationClinicsResource(Context context, Request request,
        Response response)
    {
        super(context, request, response);

        try
        {
            String query = request.getResourceRef().getQueryAsForm()
                .getQueryString();
//        System.out.println("query: "+query);
            if (checkApiCaller(query) != true)
            {
                getResponse().setStatus(Status.CLIENT_ERROR_UNAUTHORIZED);

                return;
            }

        }
        catch (Exception e)
        {
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);

            return;
        }

        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));

        //store passed parms
        apiKey = request.getResourceRef().getQueryAsForm().getFirstValue(
                "apiKey");

        String locationIdParam = request.getResourceRef().getQueryAsForm()
            .getFirstValue("locationId");

        if (locationIdParam != null)
        {
            locationId = locationIdParam;
        }
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override public Representation represent(Variant variant)
        throws ResourceException
    {

        // if (checkApiKey(apiKey)) {
//            String result = makeSOAPCall();

//            Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
//            return representation;
        Representation representation = null;

        try
        {

            JSONObject jo = new JSONObject();
            jo.put("locationId", locationId);
            jo.put("clinics", array);

            representation = new StringRepresentation(jo.toString(),
                    MediaType.APPLICATION_JSON);
        }
        catch (Exception e)
        {
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }

        return representation;

        /*
        jsonRequestString =
            "{\"GetProviderFacts\":{\"criteria\":{\"queryId\":\"6858a017-39c1-4153-bbd4-eaedac72a0e7\",\"senderId\":\"Adapter Assembly Service\",\"interactionId\":\"PRPM_IN306010UV01\",\"triggerEventCode\":\"PRPM_TE306010UV01\",\"providerSearchPayloadType:\":{\"id\":\"" +
            providerId + "\",\"name\":\""+providerName+"\",\"roleClass\":\"\",\"roleType\":\"\",\"roleClass\":\"\",\"roleType\":\"\",\"serviceDeliveryLocld\":\"\",\"serviceDeliveryLocType\":\"\"}}}}";

        try
        {
            Client client = new Client(Protocol.HTTP);
            client.setConnectTimeout(10);

            Request request = new Request(Method.POST,
                    new Reference(
                        this.getProperty("JSONPojoFactServiceEndpoint")),
                    new StringRepresentation(jsonRequestString));
            Response response = client.handle(request);

            if (response.getStatus().isSuccess())
            {
                Representation representation = new StringRepresentation(
                        response.getEntity().getText(),
                        MediaType.APPLICATION_JSON);

                return representation;
            }
        }
        catch (Exception e)
        {
            Logger.getLogger(PatientCensusResource.class.getName()).log(
                Level.SEVERE, null, e);
            throw new ResourceException(Status.SERVER_ERROR_INTERNAL,
                "Error during call to JSONPojoFactServiceEndpoint", e);
        }

        return null;
        */
        //}
        //return new StringRepresentation("APIKey invalid", MediaType.APPLICATION_JSON);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   code  DOCUMENT ME!
     * @param   name  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static JSONObject getClinic(String code, String name)
    {
        currentId++;

        return JSONHelper.getClinic(Integer.toString(currentId), name, code);
    }
}
