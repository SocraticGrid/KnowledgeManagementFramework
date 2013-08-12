/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.resources.dsa;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import java.net.URL;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tnguyen
 */
public class NotifyDSAUtilTest {
    
    public NotifyDSAUtilTest() {
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
     * Test of notify method, of class NotifyDSAUtil.
     */
    //@Test
    public void testNotify() {
        System.out.println("notify");
        String patientId = "1545";
        String userId = "1";
        
        NotifyDSAUtil instance = new NotifyDSAUtil();
        String result = instance.notify(patientId, userId);
        
        System.out.println("DONE");
    }
    
    @Test
    public void testNotifyCleanWS() throws Exception {
        System.out.println("TEST CASE: testNotifyCleanWS");
        final WebClient client = new WebClient();
        
        String server = "10.255.167.124";
        
        WebRequest request = new WebRequest(
                new URL("http://" + server + ":8080/PresentationServices/notifyDSA2"),
                HttpMethod.GET);
        request.setRequestParameters(new ArrayList());
        request.getRequestParameters().add(new NameValuePair("userId", "1"));
        request.getRequestParameters().add(new NameValuePair("patientId", "1545"));
        
        
        System.out.println("\nREQUEST URL IS: " + request.toString());
        
        Page page = client.getPage(request);
        String stringResponse = page.getWebResponse().getContentAsString();
        
        System.out.println("\nALERT RESPONSE IS: \n" + stringResponse + "\n");
        
        client.closeAllWindows();
    }

}