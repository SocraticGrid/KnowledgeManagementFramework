/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.ldapaccess;

import java.util.List;
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
public class LdapServiceTest {
    
    public LdapServiceTest() {
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
     * Test of getBusinessUnitDAO method, of class LdapService.
     */
    @Test
    public void testGetBusinessUnitDAO() {
        System.out.println("getBusinessUnitDAO");
        BusinessUnitDAO expResult = null;
        BusinessUnitDAO result = LdapService.getBusinessUnitDAO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getContactDAO method, of class LdapService.
     */
    @Test
    public void testGetContactDAO_0args() {
        System.out.println("getContactDAO");
        
        String username = "fry.emory";
        
        List<ContactDTO> contacts = LdapService.getContactDAO().findContact("cn,="+username);
        if (contacts == null || contacts.isEmpty()){
            //invalid
            fail("FAIL");
        } else {
            System.out.println("TOTAL CONTACTS:" + contacts.size());
            //System.out.println(contacts.get(0).getDisplayName());
        }
    }

    /**
     * Test of getContactDAO method, of class LdapService.
     */
    @Test
    public void testGetContactDAO_String() {
        System.out.println("getContactDAO");
        String propertiesDir = "";
        ContactDAO expResult = null;
        ContactDAO result = LdapService.getContactDAO(propertiesDir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEquipmentDAO method, of class LdapService.
     */
    @Test
    public void testGetEquipmentDAO() {
        System.out.println("getEquipmentDAO");
        EquipmentDAO expResult = null;
        EquipmentDAO result = LdapService.getEquipmentDAO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLocationDAO method, of class LdapService.
     */
    @Test
    public void testGetLocationDAO() {
        System.out.println("getLocationDAO");
        LocationDAO expResult = null;
        LocationDAO result = LdapService.getLocationDAO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRoleDAO method, of class LdapService.
     */
    @Test
    public void testGetRoleDAO() {
        System.out.println("getRoleDAO");
        RoleDAO expResult = null;
        RoleDAO result = LdapService.getRoleDAO();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}