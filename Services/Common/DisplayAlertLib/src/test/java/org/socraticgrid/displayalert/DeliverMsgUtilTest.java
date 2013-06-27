/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.displayalert;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.socraticgrid.dsa.DeliverMessageRequestType;
import org.socraticgrid.dsa.DeliverMessageResponseType;

/**
 *
 * @author tnguyen
 */
public class DeliverMsgUtilTest {

    public DeliverMsgUtilTest() {
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
     * Test of deliverMsg method, of class DeliverMsgUtil.
     */
    @Test
    public void testDeliverMsg() {
        System.out.println("deliverMsg");
        DeliverMessageRequestType request = new DeliverMessageRequestType();
        String propDir = "/home/nhin/Properties";

        request.setRefId("55555");
        request.getSubject().add("99990070");
        request.setBody("TEST PAYLOAD - DATE IS " + new Date());
        request.setHeader("TEST HEADER - DATE IS " + new Date());
        request.setDeliveryDate("10/01/2011 10:10:10");
        request.setSender("1");
        request.getMainRecipients().add("1");
        request.setPriority("HIGH");
        request.getType().add("ALERT");

        DeliverMsgUtil instance = new DeliverMsgUtil();
        DeliverMessageResponseType result = instance.deliverMsg(request, propDir);

    }
}
