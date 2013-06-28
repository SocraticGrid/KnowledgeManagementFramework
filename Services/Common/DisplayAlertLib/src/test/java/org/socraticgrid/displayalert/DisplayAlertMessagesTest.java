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
package org.socraticgrid.displayalert;

import org.socraticgrid.displayalert.DisplayAlertMessages;
import org.socraticgrid.displayalert.DisplayAlertDataUtil;
import org.socraticgrid.alertmanager.model.RiskModelFavorite;
import org.socraticgrid.common.dda.GetMessagesResponseType.GetMessageResponse;
import java.util.Iterator;
import java.util.List;
import org.socraticgrid.common.dda.GetMessageDetailRequestType;
import org.socraticgrid.common.dda.GetMessageDetailResponseType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.SetMessageResponseType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnguyen
 */
public class DisplayAlertMessagesTest {

    public DisplayAlertMessagesTest() {
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
     * Test of setMessage method, of class DisplayAlertMessages.
     */
    @Test
    public void testSetMessage() {
        System.out.println("setMessage");

        SetMessageRequestType request = new SetMessageRequestType();

        request.setAction( "Unarchive");
        request.setPatientId("99990070");
        request.setMessageId("5");
        request.setLocation("INBOX");
        request.setUserId("1");
        
        DisplayAlertMessages instance = new DisplayAlertMessages();
        SetMessageResponseType result = instance.setMessage(request);
        
        System.out.println("DONE");
    }

    @Test
    public void testSetMessages_EMRinbox() {
        System.out.println("getMessages ");

        System.out.println("GET EMR INBOX Alerts");
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setMessageType("Alert");
        //request.setPatientId("99990070");
        request.setUserId("99990070");
        request.setLocation("INBOX");

        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessagesResponseType result = instance.getMessages(request);

        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

        // SETTING
        SetMessageRequestType setRequest = new SetMessageRequestType();
        setRequest.setUserId("99990070");
        setRequest.setAction("Read");
        setRequest.setMessageId("64");

        SetMessageResponseType respResult = instance.setMessage(setRequest);

        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of getMessages method, of class DisplayAlertMessages.
     *
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
        GetMessagesRequestType componentRequest = new GetMessagesRequestType();
        componentRequest.setMessageType(request.getMessageType());
        componentRequest.setPatientId(request.getPatientId());
        componentRequest.setUserId(request.getUserId());
        componentRequest.setLocation(request.getLocation());
        // Fix this - do we need to pass MedAlerts?
        return util.getMessages("MedAlerts", componentRequest);
     *
     */
    @Test
    public void testGetMessages_EMRinbox() {
        System.out.println("getMessages ");

        System.out.println("GET EMR INBOX Alerts");
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setMessageType("Alert");
        request.setPatientId("100023");
        request.setUserId("101");
        request.setLocation("INBOX");

        DisplayAlertMessages instance = new DisplayAlertMessages();
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


    //@Test
    public void testGetMessages_USERinbox() {
        System.out.println("getMessages");

        System.out.println("GET EMR INBOX Alerts");
        GetMessagesRequestType request = new GetMessagesRequestType();
        request.setMessageType("Alert");
        //request.setPatientId("99990070");
        request.setUserId("1");
        //request.setLocation("Archive");

        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessagesResponseType result = instance.getMessages(request);

        List<GetMessageResponse> alist = result.getGetMessageResponse();
        Iterator<GetMessageResponse> iter = alist.iterator();
        while (iter.hasNext()) {
            GetMessageResponse msg = iter.next();
            System.out.println("ID/DESCR: "+msg.getMessageId()+" , "+msg.getDescription());

        }

    }

    /**
     * Test of getMessageDetail method, of class DisplayAlertMessages.
     */
    //@Test
    public void testGetMessageDetail() {
        System.out.println("getMessageDetail");
        GetMessageDetailRequestType request = null;
        DisplayAlertMessages instance = new DisplayAlertMessages();
        GetMessageDetailResponseType expResult = null;
        GetMessageDetailResponseType result = instance.getMessageDetail(request);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    //@Test
    public void testgetFavModels() {
        String modelId = "MockDiabetes";
        DisplayAlertDataUtil util = new DisplayAlertDataUtil();
        List rlist = util.getRMFByModelId(modelId);
        if (rlist.isEmpty()) {
            System.out.println("EMOTY");

        } else {
            RiskModelFavorite fav = (RiskModelFavorite) rlist.get(0);

        }

    }
}
