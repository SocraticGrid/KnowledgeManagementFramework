/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.resources.dsa;

import java.util.List;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.socraticgrid.account.model.UserSession;
import org.socraticgrid.account.service.AccountService;
import org.socraticgrid.aggregator.DisplayDataAggregator;
import org.socraticgrid.aggregator.DisplayDataAggregatorPortType;
import org.socraticgrid.alertmanager.dao.AlertTicketDao;
import org.socraticgrid.alertmanager.model.AlertTicket;
import org.socraticgrid.alertmanager.model.TicketQueryParams;
import org.socraticgrid.alertmanager.service.AlertService;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.SetMessageResponseType;
import org.socraticgrid.displayalert.DisplayAlertMessages;
import org.socraticgrid.patientdsanotification.DSANotify;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;
import org.socraticgrid.taps.tapsdsanotification.TapsDSANotify;

/**
 *
 * @author tnguyen
 */
public class NotifyDSAUtil {
    private static Log log = LogFactory.getLog(NotifyDSAUtil.class);

    public NotifyDSAUtil() {
    }
    
    public String notifyClean(String patientId, String userId) {

        //---------------------------------
        // PROCESSING
        //---------------------------------

        System.out.println("===> NOTIFY DSA about PATIENT ID " + patientId );

        String resp = "";
        try {
            
            //---------------------------------
            // GENERAL instantiation that does nothing..placeholder
            // NOTE:  Replace instantiation wiht more specific class as needed.
            //---------------------------------
//            DSANotify dsa = new BasicDSANotify(getProperty("ActionAgentService"));
            
            //------------- TAPS ONLY -------------
            // TAPS ONLY: specific instantiation
            DSANotify dsa = new TapsDSANotify(PropertyHelper.getPropertyHelper().getProperty("ActionAgentService"));
                
            // TAPS ONLY: CLEAN out all alerts for this patient BEFORE notifying DSA
            AlertService service = new AlertService();
            TicketQueryParams params = new TicketQueryParams();
            params.setPatientId(patientId);
            
System.out.println("===> CLEANING up old Alert(s) for patientid= "+ patientId);
            List<AlertTicket> alertList = service.getTicketsByParams(params);
System.out.println("===> CLEANING up total tickets: "+alertList.size());

            for (AlertTicket a : alertList) {
                System.out.println("===> CLEANING up old Alert(s) = "+a.getTicketId());
                service.deleteTicket(a);
            }

            //------------- TAPS ONLY -------------
            
            // NOTIFY DSA
            dsa.sendPatientIdToDSA(patientId, userId);
            

            Thread.sleep(10000);

            resp = createNotifyDSAResponse(null);
        
        } catch (Exception e) {
            e.printStackTrace();
            resp = createNotifyDSAResponse(e.getMessage());
        }        

        return resp;
    }
    
    public String notify(String patientId, String userId) {

        //---------------------------------
        // PROCESSING
        //---------------------------------

        System.out.println("===> NOTIFY DSA about PATIENT ID " + patientId );

        String resp = "";
        try {
            
            //---------------------------------
            // GENERAL instantiation that does nothing..placeholder
            // NOTE:  Replace instantiation wiht more specific class as needed.
            //---------------------------------
//            DSANotify dsa = new BasicDSANotify(getProperty("ActionAgentService"));
            
            //------------- TAPS ONLY -------------
            // TAPS ONLY: specific instantiation
            DSANotify dsa = new TapsDSANotify(PropertyHelper.getPropertyHelper().getProperty("ActionAgentService"));               
            
            // NOTIFY DSA
            dsa.sendPatientIdToDSA(patientId, userId);
            

            Thread.sleep(10000);

            resp = createNotifyDSAResponse(null);
        
        } catch (Exception e) {
            e.printStackTrace();
            resp = createNotifyDSAResponse(e.getMessage());
        }        

        return resp;
    }
    
    private String createNotifyDSAResponse(String errMsg) {

        StringBuilder responseBuilder = new StringBuilder();
        
        if (errMsg == null) 
            responseBuilder.append("{\"notifyDSA\" : {\"statusMessage\" : \"Notification Sent.\", \"successStatus\" : true");
        else {
            responseBuilder.append("{\"notifyDSA\" : {\"statusMessage\" : \""
                    + errMsg
                    +"\", \"successStatus\" : false");
        }
            
        responseBuilder.append("}}");

        return responseBuilder.toString();
    }
}
