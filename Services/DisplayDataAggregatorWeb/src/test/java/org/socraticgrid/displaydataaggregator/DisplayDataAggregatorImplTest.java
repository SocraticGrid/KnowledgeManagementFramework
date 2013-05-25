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
package org.socraticgrid.displaydataaggregator;

import org.socraticgrid.displaydataaggregator.DisplayDataAggregatorImpl;
import org.socraticgrid.common.dda.MessageDataRequestType;
import java.util.Scanner;
import java.util.ArrayList;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType.GetMessageResponse;
import java.util.Iterator;
import java.util.List;
import org.socraticgrid.common.dda.GetAddressBookRequestType;
import org.socraticgrid.common.dda.GetAddressBookResponseType;
import org.socraticgrid.common.dda.GetAvailableSourcesRequestType;
import org.socraticgrid.common.dda.GetAvailableSourcesResponseType;
import org.socraticgrid.common.dda.GetDetailDataForUserRequestType;
import org.socraticgrid.common.dda.GetDetailDataRequestType;
import org.socraticgrid.common.dda.GetDetailDataResponseType;
import org.socraticgrid.common.dda.GetDirectoryAttributeRequestType;
import org.socraticgrid.common.dda.GetDirectoryAttributeResponseType;
import org.socraticgrid.common.dda.GetMessageDetailRequestType;
import org.socraticgrid.common.dda.GetMessageDetailResponseType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.GetSummaryDataForUserRequestType;
import org.socraticgrid.common.dda.GetSummaryDataRequestType;
import org.socraticgrid.common.dda.GetSummaryDataResponseType;
import org.socraticgrid.common.dda.SetMessageDataRequestType;
import org.socraticgrid.common.dda.SetMessageDataResponseType;
import org.socraticgrid.common.dda.UpdateInboxStatusRequestType;
import org.socraticgrid.common.dda.UpdateInboxStatusResponseType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnguyen
 */
public class DisplayDataAggregatorImplTest {

    public DisplayDataAggregatorImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    /**
     * OK
     * @throws Exception
     */
    @Test
    public void testStrScanner() throws Exception {

        String fromFull = "Jane M. Doe <janemdoe@lab.socarticgrid.org>";
        String pfrom = fromFull.substring(fromFull.indexOf("<")+1, fromFull.indexOf(">"));
        System.out.println(pfrom);
        /*
        String ptId = "99990070";
        String patientIdFound = null;
        String content = "PATIENTID=99990070\nDear Dr. Fry, Jane M Doe blood work is fine.  She is super healthy!Clinic123";
        System.out.println("msgBody=\n" + content);

        if (content.startsWith("PATIENTID=")) {

            // TEMPORARILY extract patientId from body if present
            Scanner scanner = new Scanner(content);
            boolean first = true;
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                if (first) {
                    String[] parts = scanner.nextLine().split("=");
                    patientIdFound = parts[1];
                    first = false;
                } else {
                    sb.append(scanner.nextLine());
                }
            }
        }
        System.out.println("ptId=" + ptId + " vs patientIdFound=" + patientIdFound);
         *
         */

        
        assert(true);
    }

    /**
     * OK
     * Test of getMessages method, of class DisplayDataAggregatorImpl.
     * Getting all msgs for the logged in user.
     */
    @Test
    public void testGetAlertMsg() throws Exception {
        System.out.println("testGetAllMessages");

        //------------
        //PREP REQUEST
        //------------
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setLocation("INBOX");
        request.setMessageType("Alert");
        //request.setPatientId("99990070");
        request.setUserId("1");
                
        //------------
        //SEND REQUEST
        //------------
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetMessagesResponseType result = instance.getMessages(request);

        //------------
        //REVIEW RESPONSE
        //------------
        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

        //GetMessagesResponseType expResult = null;
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");

    }

    /**
     * ??
     * Test of getMessages method, of class DisplayDataAggregatorImpl.
     * Getting all msgs for a patient within the Provider Portal (EMR).
     */
    @Test
    public void testGetEmailMsg() throws Exception {
        System.out.println("testGetAllMessages");

        //------------
        //PREP REQUEST
        //------------
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setLocation("INBOX");
        request.setMessageType("Email");
        request.setPatientId("99990070");
        request.setUserId("1");

        //------------
        //SEND REQUEST
        //------------
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetMessagesResponseType result = instance.getMessages(request);

        //------------
        //REVIEW RESPONSE
        //------------
        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

        //GetMessagesResponseType expResult = null;
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");

    }

    /**
     * OK
     * Test of getMessages method, of class DisplayDataAggregatorImpl.
     */
    @Test
    public void testGetDeletedAlertMessages() throws Exception {
        System.out.println("testGetDeletedMessages");
        
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setLocation("UserTrash");
        request.setMessageType("Alert");
        //request.setPatientId("99990070");
        request.setUserId("1");
        
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        //GetMessagesResponseType expResult = null;
        GetMessagesResponseType result = instance.getMessages(request);

        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");

    }

    /**
     * OK
     * Test of setMessage method, of class DisplayDataAggregatorImpl.
     */
    @Test
    public void testSendEmail() {
        System.out.println("testSendEmail");
        List<String> ls=new ArrayList<String>();

        //------------
        //PREP REQUEST
        //------------
        MessageDataRequestType aReq = new MessageDataRequestType();
        aReq.getLabels().add ("Starred");
        aReq.setUserId       ("1");       //REQUIRED
        //request.setPatientId  ("99990070");
        aReq.setAction       ("Send");
        //request.setAttachment ("");
        //---------------------------
        //ls.clear();
        //ls.add("");
        //request.getContactBCC().addAll(ls);
        //---------------------------
        //ls.clear();
        //ls.add("");
        //request.getContactCC().addAll(ls);
        //---------------------------
        //ls.clear();
        //ls.add("doe.jane");
        ls.add("999");
        aReq.getContactTo().addAll(ls);
        //---------------------------
        //ls.clear();
        //ls.add("");
        aReq.getLabels().addAll(aReq.getLabels());
        //---------------------------
        //request.setDocument   ("");
        //request.setMessageId  ("");
        //request.setTasks      ("");
        aReq.setSubject      ("msg sbj");
        aReq.setBody         ("msg conent");
        aReq.setLocation     ("INBOX");    //OPTIONAL:  but ..
        aReq.setDataSource   ("Email");    //REQUIRED: in wiki it's "types"

        SetMessageDataRequestType request = new SetMessageDataRequestType();
        request.getRequestMessage().add(aReq);
        
        //------------
        //SEND REQUEST
        //------------
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        SetMessageDataResponseType result = instance.setMessage(request);

        //------------
        //REVIEW RESPONSE
        //------------

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }


    /**
     * Test of setMessage method, of class DisplayDataAggregatorImpl.
http://192.168.5.47:9763/PresentationServices/setMessages?
     action=archive&messageIds=7%2C6%2C5%2C29&locations=INBOX%2CINBOX%2CINBOX%2CINBOX&types=Email%2CEmail%2CEmail%2CAlert&userId=99990070&token=
http://192.168.5.47:9763/PresentationServices/setMessages?
     action=archive&messageIds=7&locations=INBOX&types=Email&userId=99990070&token=
     */
    @Test
    public void testSetAlertToArchive() {
        System.out.println("testSetToArchive");
        List<String> ls=new ArrayList<String>();

        //------------
        //PREP REQUEST
        //------------
        MessageDataRequestType aReq = new MessageDataRequestType();
        //request.getLabels().add ("");
        aReq.setUserId       ("1");       //REQUIRED
        //request.setPatientId  ("99990070");
        aReq.setAction       ("Archive");
        //---------------------------
        //---------------------------
        //request.setDocument   ("");
        aReq.setMessageId  ("5");
        aReq.setLocation     ("INBOX");


        //request.setDataSource   ("Email");    //REQUIRED: in wiki it's "types"
        aReq.setDataSource   ("Alert");    //REQUIRED: in wiki it's "types"

        SetMessageDataRequestType request = new SetMessageDataRequestType();
        request.getRequestMessage().add(aReq);

        //------------
        //SEND REQUEST
        //------------
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        SetMessageDataResponseType result = instance.setMessage(request);


        //------------
        //REVIEW RESPONSE
        //------------

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * ??
     */
    @Test
    public void testSetEmailToArchive() {
        System.out.println("..............testSetEmailToArchive");
        List<String> ls=new ArrayList<String>();

        //------------
        //PREP REQUEST
        //------------
        MessageDataRequestType aReq = new MessageDataRequestType();
        //request.getLabels().add ("");
        aReq.setUserId       ("1");       //REQUIRED
        //request.setPatientId  ("99990070");
        aReq.setAction       ("Archive");
        //---------------------------
        //---------------------------
        //request.setDocument   ("");
        aReq.setMessageId  ("2");
        aReq.setLocation     ("INBOX");


        aReq.setDataSource   ("Email");    //REQUIRED: in wiki it's "types"

        SetMessageDataRequestType request = new SetMessageDataRequestType();
        request.getRequestMessage().add(aReq);

        //------------
        //SEND REQUEST
        //------------
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        SetMessageDataResponseType result = instance.setMessage(request);


        //------------
        //REVIEW RESPONSE
        //------------

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    
    
    
    
    //================ TO BE TESTED ================================
    /**
     * Test of getAddressBook method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetAddressBook() {
        System.out.println("getAddressBook");
        GetAddressBookRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetAddressBookResponseType expResult = null;
        GetAddressBookResponseType result = instance.getAddressBook(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDirectoryAttribute method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetDirectoryAttribute() {
        System.out.println("getDirectoryAttribute");
        GetDirectoryAttributeRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetDirectoryAttributeResponseType expResult = null;
        GetDirectoryAttributeResponseType result = instance.getDirectoryAttribute(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of updateInboxStatus method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testUpdateInboxStatus() {
        System.out.println("updateInboxStatus");
        UpdateInboxStatusRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        UpdateInboxStatusResponseType expResult = null;
        UpdateInboxStatusResponseType result = instance.updateInboxStatus(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAvailableSources method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetAvailableSources() {
        System.out.println("getAvailableSources");
        GetAvailableSourcesRequestType getAvailableSourcesRequest = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetAvailableSourcesResponseType expResult = null;
        GetAvailableSourcesResponseType result = instance.getAvailableSources(getAvailableSourcesRequest);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDetailData method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetDetailData() {
        System.out.println("getDetailData");
        GetDetailDataRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetDetailDataResponseType expResult = null;
        GetDetailDataResponseType result = instance.getDetailData(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSummaryData method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetSummaryData() {
        System.out.println("getSummaryData");
        GetSummaryDataRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetSummaryDataResponseType expResult = null;
        GetSummaryDataResponseType result = instance.getSummaryData(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDetailDataForUser method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetDetailDataForUser() {
        System.out.println("getDetailDataForUser");
        GetDetailDataForUserRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetDetailDataResponseType expResult = null;
        GetDetailDataResponseType result = instance.getDetailDataForUser(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSummaryDataForUser method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetSummaryDataForUser() {
        System.out.println("getSummaryDataForUser");
        GetSummaryDataForUserRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetSummaryDataResponseType expResult = null;
        GetSummaryDataResponseType result = instance.getSummaryDataForUser(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getMessageDetail method, of class DisplayDataAggregatorImpl.
     */
    //@Test
    public void testGetMessageDetail() {
        System.out.println("getMessageDetail");
        GetMessageDetailRequestType request = null;
        DisplayDataAggregatorImpl instance = new DisplayDataAggregatorImpl();
        GetMessageDetailResponseType expResult = null;
        GetMessageDetailResponseType result = instance.getMessageDetail(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
