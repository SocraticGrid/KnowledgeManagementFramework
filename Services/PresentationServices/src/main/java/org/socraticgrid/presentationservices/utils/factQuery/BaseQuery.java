/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.utils.factQuery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.socraticgrid.presentationservices.utils.factModels.*;
import org.socraticgrid.util.CommonUtil;

/**
 *
 * @author tnguyen
 */
public class BaseQuery {


    public GridHeaders createGH(String factType, String trxnType,
                                 String descr, int width)
    {
        if (CommonUtil.strNullorEmpty(descr)) descr = "A column header.";

        GridHeaders grh = new GridHeaders();
        grh.setColumnId(factType);
        grh.setValue(trxnType);
        grh.setDescription(descr);
        grh.setWidth(width);

        return grh;
    }
    
    public String createEmptyPatientDataFact(String domain,
                                             String responseType,
                                             boolean successStatus,
                                             String statusMsg,
                                             String sectionId) {

        PatientDataFact pdf = new PatientDataFact();
        pdf.setFactType(domain);
        pdf.setTrxnType(responseType);
        pdf.setSuccessStatus(successStatus);
        pdf.setStatusMessage(statusMsg);
        pdf.setSectionId(sectionId);

        //-----------------------------------------
        // BUILD parent json element
        //-----------------------------------------
        GetPatientDataFactObject pdfParent = new GetPatientDataFactObject();
        pdfParent.setpatientDataFact(pdf);

        //-------------------------------------------------------
        // Convert objects to JSON and string-i-fied for return
        //-------------------------------------------------------
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setDateFormat("yyyy-MM-dd").create();

        StringBuilder sb = new StringBuilder();
        sb.append(gson.toJson(pdfParent));

        return sb.toString();
    }
}
