/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.taskmanager;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.socraticgrid.dsa.DeliverMessageRequestType;
import org.socraticgrid.dsa.DeliverMessageResponseType;

import java.net.*;
import java.io.*;
//import org.socraticgrid.common.dda.GetMessagesRequestType;
//import org.socraticgrid.displayalert.DisplayAlertDataUtil;
//import org.socraticgrid.inboxpushclient.PushSessionMgr;

/**
 *
 * @author tnguyen
 */
public class DeliverMsgHandlerTest {
    
    public DeliverMsgHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of deliverMessage method, of class DeliverMsgHandler.
     */
    @Test
    public void testDeliverMessage() {
        System.out.println("deliverMessage");
        
        DeliverMessageRequestType request = new DeliverMessageRequestType();
        String propDir = "/home/nhin/Properties";

        request.setRefId("20301");
        request.getSubject().add("997");
        request.setBody("TEST PAYLOAD - DATE IS " + new Date());
        request.setHeader("TEST HEADER - DATE IS " + new Date());
        request.setDeliveryDate("10/01/2013 10:10:10");
        request.setSender("TMN");
        request.getMainRecipients().add("1");
        request.setPriority("HIGH");
        request.getType().add("ALERT");
        
        DeliverMsgHandler instance = new DeliverMsgHandler();
        DeliverMessageResponseType result = instance.deliverMessage(request);
        
        System.out.println("STATUS: " + result.getStatus());
        
    }
    
    @Test
    public void testPushYahoo() throws MalformedURLException, IOException {

        URL yahoo = new URL("http://www.yahoo.com/");
        URLConnection yc = yahoo.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            System.out.println(inputLine);
        in.close();
    }
    
    
//    @Test
//    public void testPushClient() throws IOException, Exception {
//        
//        
//        String userId = "1";
//        String patientId = "997";
//        String jsonALert = null;
//        
////        // STATIC msg to send 
////       jsonALert =
////            "ALERTS="+patientId+",{"
////            +"\"messagesFact\": {"
////            +"\"successStatus\": true ,"
////            +"\"messageObjects\": ["
////            +"{"
////            +"  \"messageId\": \"444\","
////            +"  \"type\": \"Alert\","
////            +"  \"location\": \"INBOX\","
////            +"  \"labels\": [],"
////            +"  \"messageDate\": \"2013-09-11\","
////            +"  \"messageTime\": \"12:30\","
////            +"  \"description\": \"This patient was prescribed Demerol that has a metabolite, which in the setting of kidney disease may result in increased seizure risk.   The average Creatinine value over the previous 6 measurements was 1.1 indicating impaired renal function.  If an opioid is needed for pain control, consider hydromorphone or fentanyl as alternatives.\","
////            +"  \"from\": \"CDS\","
////            +"  \"title\": \"Seizure Risk.\","
////            +"  \"status\": \"Unread\","
////            +"  \"priority\": \"HIGH\","
////            +"  \"tasksCount\": 1,"
////            +"  \"tasksComplete\": 0"
////            +"}"
////            +"]"
////            +"}"
////            +"} ";
//        
//        //----------------------------------------
//        // PREP and SEND REQUEST to get Alerts as JSON
//        //----------------------------------------
//        
//        GetMessagesRequestType request = new GetMessagesRequestType();
//        request.setMessageType("Alert");
//        request.setPatientId(patientId);
//        request.setUserId(userId);
//        request.setLocation("INBOX");
//        
//        DisplayAlertDataUtil alertUtil = new DisplayAlertDataUtil();
//        jsonALert = alertUtil.getAllAlertsAsJSON(request);
//                
//        String msg = "ALERTS="+patientId+","+jsonALert;
//        
//        //----------------------------------------
//        // Push Alerts to Client
//        //----------------------------------------
//        PushSessionMgr client = new PushSessionMgr();
//        client.pushMsg(msg);
//
//        
//    }
    
    
}
