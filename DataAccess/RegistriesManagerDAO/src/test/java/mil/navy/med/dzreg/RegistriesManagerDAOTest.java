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
package mil.navy.med.dzreg;

import org.junit.Ignore;
import mil.navy.med.dzreg.dao.AlreadyRegisteredException;
import mil.navy.med.dzreg.dao.RegistriesManagerDAO;
import java.util.List;
import mil.navy.med.dzreg.types.AckType;
import mil.navy.med.dzreg.types.PersonRegistryProfileType;
import mil.navy.med.dzreg.types.PersonType;
import mil.navy.med.dzreg.types.RegistryProfileType;
import mil.navy.med.dzreg.types.RegistryType;
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
public class RegistriesManagerDAOTest {

  private RegistriesManager instance = null;
  private long[] ids = {647922, 648995};
  private Long newPerson = new Long(999995);
  
  private String[] names = {"STONE,HAROLD", "SHETLER,HAROLD"};
  private String[] registries = {"DIABETES", "ASTHMAS", "NEWBORN", "BREAST HEALTH", "IDIOPATHIC THROMBOCYTOSIS"};

  private PersonRegistryProfileType invalidPersonProfile;
  
  public RegistriesManagerDAOTest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }

  @Before
  public void setUp() {
    instance = RegistriesManagerDAO.getInstance();

    //--------------------------------------------------------------------------
    // setup invalid profile
    //--------------------------------------------------------------------------
    invalidPersonProfile = new PersonRegistryProfileType();

    PersonType person = new PersonType();
    person.setId(999990);
    invalidPersonProfile.setPerson(person);

    RegistryProfileType registryProfile = new RegistryProfileType(1, "DIABETES");
    invalidPersonProfile.getRegistry().add(registryProfile);

    invalidPersonProfile.setDataSource("KMR");
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getRegistryProfile method, of class RegistryManagerDAO.
   */
  //@Test
  public void testGetRegistryProfile() {
    System.out.println("getRegistryProfile()---------------------------------");
    for (int i = 0; i < ids.length; i++) {
      Long personId = Long.valueOf(ids[i]);
      PersonRegistryProfileType result = instance.getRegistryProfile(personId);

      //assertEquals(expResult, result);
      assertNotNull(result);

      System.out.println(result);

      String name = result.getPerson().getName();
      System.out.println("id: " + personId + " ==> name: " + name);
      assertEquals(names[i], name);
    }
  }

  /**
   * Test of getRegistryTypes method, of class RegistryManagerDAO.
   */
  //@Test
  public void testGetRegistryTypes() {
    System.out.println("getRegistryTypes()-----------------------------------");
    List<RegistryType> results = instance.getRegistryTypes();

    //assertEquals(expResult, result);
    assertNotNull(results);
    assertEquals(5, results.size());

    for (RegistryType result : results) {
      System.out.println(result);
    }
  }

  /**
   * Test of register method, of class RegistryManagerDAO.
   */
  //@Test
  public void testAlreadyRegistered() {
    System.out.println("testAlreadyRegistered()------------------------------");

    Long personId = Long.valueOf(647922);
    PersonRegistryProfileType result = instance.getRegistryProfile(personId);

    // already registered test
    PersonRegistryProfileType profile = result;
    try {
      AckType ack = instance.register(profile);
      System.out.println("Ack code:" + ack.getResponseCode() + ", message:" + ack.getDetectedIssueText());
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      assertEquals(AlreadyRegisteredException.class, ex.getClass());
    }
  }

  /**
   * Test of register method, of class RegistryManagerDAO.
   */
  //@Test
  public void testRegisterExistingPersonInNewRegistry() {
    System.out.println("testRegisterExistingPersonInNewRegistry()------------");

    Long personId = Long.valueOf(647922);

    // existing patient + new registry
    try {
      PersonRegistryProfileType result = instance.getRegistryProfile(personId);

      PersonRegistryProfileType profile = result;
      RegistryProfileType registry = new RegistryProfileType();
      registry.setRegistryId(5);
      registry.setRegistryDesc(registries[4]);
      profile.getRegistry().remove(0);
      profile.getRegistry().add(registry);
      profile.setDataSource("KMR");

      AckType ack = instance.register(profile);
      System.out.println("Ack code:" + ack.getResponseCode() + ", message:" + ack.getDetectedIssueText());
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Test of register method, of class RegistryManagerDAO.
   */
  //@Test
  public void testRegisterNewPerson() {
    System.out.println("testRegisterNewPerson()------------------------------");

    try {
      Long personId = new Long(648995);
      PersonRegistryProfileType profile = instance.getRegistryProfile(personId);

      profile.getPerson().setId(newPerson);
      profile.getPerson().setName("TESTER," + newPerson);
      profile.getPerson().setFmpssn("999-99-" + newPerson);
      profile.setDataSource("KMR");

      AckType ack = instance.register(profile);

      assertEquals(ack.getResponseCode(), "OK");
      assertEquals(ack.getDetectedIssueText(), "");
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Test of unregister method, of class RegistryManagerDAO.
   */
  //@Test
  public void testUnregisterInvalidPerson() {
    System.out.println("testUnregisterInvalidPerson()------------------------");

    try {
      AckType ack = instance.unregister(invalidPersonProfile);
      System.out.println("Ack code:" + ack.getResponseCode() + ", message:" + ack.getDetectedIssueText());
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      //assertEquals(AlreadyRegisteredException.class, ex.getClass());
    }
  }

    /**
   * Test of unregister method, of class RegistryManagerDAO.
   */
  //@Test
  public void testUnregisterValidPerson() {
    System.out.println("testUnregisterValidPerson()--------------------------");

    try {
      PersonRegistryProfileType profile = instance.getRegistryProfile(newPerson);
      AckType ack = instance.unregister(profile);
      System.out.println("Ack code:" + ack.getResponseCode() + ", message:" + ack.getDetectedIssueText());
      
      assertEquals(ack.getResponseCode(), "OK");
      assertEquals(ack.getDetectedIssueText(), "");
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Test of getRegistryProfile method, of class RegistryManagerDAO.
   */
  @Test
  @Ignore
  public void testRegistriesManagerDAO() {
//    testGetRegistryTypes();
//    testGetRegistryProfile();
//    testAlreadyRegistered();
//    testRegisterExistingPersonInNewRegistry();
    testRegisterNewPerson();
    testUnregisterInvalidPerson();
    testUnregisterValidPerson();
  }
}
