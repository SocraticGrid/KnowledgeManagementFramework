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
package test;

import java.util.GregorianCalendar;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;
import mil.navy.med.dzreg.service.RegistriesServiceImpl;
import mil.navy.med.dzreg.types.AckType;
import mil.navy.med.dzreg.types.AddressType;
import mil.navy.med.dzreg.types.ObjectFactory;
import mil.navy.med.dzreg.types.PersonType;
import mil.navy.med.dzreg.types.RegisterPersonRequestType;
import mil.navy.med.dzreg.types.RegistryProfileType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kim
 */
public class RegistriesServiceTest {

    RegistriesServiceImpl impl = new RegistriesServiceImpl();

    public RegistriesServiceTest() {
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

    @After
    public void tearDown() {
    }

    /**
     * Test of register method, of class RegistryManagerDAO.
     */
    @Test
    public void testRegisterNewPerson() {
        System.out.println("testRegisterNewPerson()------------------------------");
        ObjectFactory o = new ObjectFactory();

        try {
            RegisterPersonRequestType request = new RegisterPersonRequestType();

            PersonType newperson = new PersonType();
            newperson.setId(new Long(999990));
            XMLGregorianCalendar cal = createXgc();
            newperson.setDateOfBirth(cal);
            newperson.setDataSource("KMR");
            newperson.setFmpssn("222-22-2222");
            newperson.setName("Annie Patient");
            newperson.setHomePhone("111-1111");
            newperson.setOfficePhone("222-2222");
            newperson.setEligibilityIdentifier(new Long(1234567890));

            AddressType address = new AddressType();
            address.setStreetAddress("111 Any Street Ave");
            address.setCity("ANY CITY");
            address.setPostalCode("00000");
            address.setState("CA");
            newperson.setAddress(o.createPersonTypeAddress(address));

            RegistryProfileType regprofile = new RegistryProfileType();
            regprofile.setRegistryId(4);
            regprofile.setRegistryDesc("BREAST HEALTH");

            request.setPerson(newperson);
            request.getRegistry().add(regprofile);

            request.setDataSource("KMR");

            AckType ack = impl.register(request);

            assertEquals("OK", ack.getResponseCode());
            assertEquals("", ack.getDetectedIssueText());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    private XMLGregorianCalendar createXgc() throws DatatypeConfigurationException {
        GregorianCalendar g = new GregorianCalendar();
        XMLGregorianCalendar cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(g);
        cal.setYear(2000);
        cal.setMonth(Calendar.JANUARY + 1);
        cal.setDay(1);
        return cal;
    }

    /**
     * Test of register method, of class RegistryManagerDAO.
     */
    @Test
    public void testUnregisterNewPerson() {
        System.out.println("testUnregisterNewPerson()------------------------------");
        ObjectFactory o = new ObjectFactory();

        try {
            RegisterPersonRequestType request = new RegisterPersonRequestType();

            PersonType newperson = new PersonType();
            newperson.setId(new Long(999990));
            XMLGregorianCalendar cal = createXgc();
            newperson.setDateOfBirth(cal);
            newperson.setDataSource("KMR");
            newperson.setFmpssn("222-22-2222");
            newperson.setName("Annie Patient");
            newperson.setHomePhone("111-1111");
            newperson.setOfficePhone("222-2222");
            newperson.setEligibilityIdentifier(new Long(1234567890));

            AddressType address = new AddressType();
            address.setStreetAddress("111 Any Street Ave");
            address.setCity("ANY CITY");
            address.setPostalCode("00000");
            address.setState("CA");
            newperson.setAddress(o.createPersonTypeAddress(address));

            RegistryProfileType regprofile = new RegistryProfileType();
            regprofile.setRegistryId(4);
            regprofile.setRegistryDesc("BREAST HEALTH");

            request.setPerson(newperson);
            request.getRegistry().add(regprofile);

            request.setDataSource("KMR");

            AckType ack = impl.unregister(request);

            assertEquals("OK", ack.getResponseCode());
            assertEquals("", ack.getDetectedIssueText());
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
}
