/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.resources.facts;

import java.util.Map;
import net.sf.json.JSON;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.presentationservices.utils.factQuery.*;
import org.socraticgrid.util.CommonUtil;

/**
 *
 * @author tnguyen
 */
public class GetDataUtil extends BaseQuery {

    public GetDataUtil() {}


//    /**
//     * Get non-patient specific data (list or detail).
//     *
//     * @param fieldMap
//     * @return
//     * @throws Exception
//     */
//    public String getCodeData(Map fieldMap) throws Exception {
//
//        String resp = "";
//        JSON jsonOut = null;
//        String jsonRsp = null;
//
//        String domain = (String)fieldMap.get("domain");
//        String responseType = (String)fieldMap.get("responseType");
//
//
//    }


    private boolean isPropsTrue (String propName) {

        if (PropertyHelper.getPropertyHelper().getProperty(propName).equalsIgnoreCase("true"))
            return true;
        else
            return false;

    }
    /**
     * Get patient specific data (list, detail, ecs). --> note: ecs should really be under getCodeData call...LATER
     * 
     * @param fieldMap
     * @return
     * @throws Exception
     */
    public String getData(Map fieldMap) throws Exception {
        
        String resp = "";
        JSON jsonOut = null;
        String jsonRsp = null;

        String errMsg = null;

        String domain = (String)fieldMap.get("domain");
        String responseType = (String)fieldMap.get("responseType");

        //GET data for codeDetail, detail, list, or ecs
        if (responseType.equalsIgnoreCase("ecs")) {
            jsonRsp = EcsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedEcsData"));

        } else {

            if (domain.equalsIgnoreCase("Medications")) {
                jsonRsp = MedicationsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedMedications"));

            } else if (domain.equalsIgnoreCase("Immunizations")) {
                jsonRsp = ImmunizationsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedImmunizations"));

            } else if (domain.equalsIgnoreCase("Labs")) {
                jsonRsp = LabsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedLabs"));

            } else if (domain.equalsIgnoreCase("Admissions")) {
                jsonRsp = AdmissionsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedAdmissions"));

            } else if (domain.equalsIgnoreCase("Allergies")) {
                jsonRsp = AllergiesQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedAllergies"));

            } else if (domain.equalsIgnoreCase("Demographics")) {
                jsonRsp = DemographicsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedDemographics"));

            } else if (domain.equalsIgnoreCase("Problems")) { //Diagnoses
                jsonRsp = DiagnosesQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedDiagnoses"));

            } else if (domain.equalsIgnoreCase("DocInpatient")) {
                jsonRsp = DocInpatientQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedDocInpatient"));


/************/
/* TMN - DOCUMENT is currently being used for OUTPATIENT
 *       domain = "Documentation" is too general.
 *       domain should be:  ClinicNotes, Directives
 *       and need to have relative instance class.


            } else if (domain.equalsIgnoreCase("DocDirectives")) {
                xml = DocDirectives.getInstance().createResponse(fieldMap, useStubbedFacts);
*/

            } else if (domain.equalsIgnoreCase("ClinicNotes")) {
                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedClinicNotes"));

            } else if (domain.equalsIgnoreCase("Documentation")) {
                jsonRsp = DocClinicNotes.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedDocumentation"));
/************/

            } else if (domain.equalsIgnoreCase("Equipment")) {
                jsonRsp = EquipmentsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedEquipment"));

            } else if (domain.equalsIgnoreCase("Imaging")) {
                jsonRsp = ImagingQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedImaging"));;

            } else if (domain.equalsIgnoreCase("Procedures")) {
                jsonRsp = ProceduresQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedProcedures"));

            } else if (domain.equalsIgnoreCase("Vitals")) {
                jsonRsp = VitalsQuery.getInstance().createResponse(fieldMap, isPropsTrue("useStubbedVitals"));

            } else {
                errMsg = "\n===>ERROR: GetPatientDataResource: Domain " + domain + " not supported.\n";
            }


            // CREATE EMPTY RESPONSE if statusMsg NOT NULL AND jsonRsp IS NULL
            if (CommonUtil.strNullorEmpty(errMsg) && CommonUtil.strNullorEmpty(jsonRsp)) {

                errMsg = "\n===>ERROR: GetDataUtil: No response for domain=" + domain
                            + " responseType=" + responseType
                            + "query.\n";
            }
            
            if (!CommonUtil.strNullorEmpty(errMsg)) {
                jsonRsp = createEmptyPatientDataFact(responseType, domain, false,
                                errMsg,  (String) fieldMap.get("sectionId"));

                System.out.println(errMsg);
            }
            
            //System.out.println("==> PS: jsonResp=\n"+jsonRsp); //DBG ONLY

        }

        return jsonRsp;
    }

}
