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

package mil.navy.med.dzreg.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for AddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="streetAddress" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="streetAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postalCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressType", propOrder = {
  "streetAddress",
  "streetAddress2",
  "city",
  "state",
  "postalCode"
})
public class AddressType {

  @XmlElement(name = "streetAddress", required = true)
  protected String streetAddress;
  @XmlElement(name = "streetAddress2")
  protected String streetAddress2;
  @XmlElement(name = "city", required = true)
  protected String city;
  @XmlElement(name = "state", required = true)
  protected String state;
  @XmlElement(name = "postalCode", required = true)
  protected String postalCode;

  /**
   * Gets the value of the streetAddress property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getStreetAddress() {
    return streetAddress;
  }

  /**
   * Sets the value of the streetAddress property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setStreetAddress(String value) {
    this.streetAddress = value;
  }

  /**
   * Gets the value of the streetAddress2 property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getStreetAddress2() {
    return streetAddress2;
  }

  /**
   * Sets the value of the streetAddress2 property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setStreetAddress2(String value) {
    this.streetAddress2 = value;
  }

  /**
   * Gets the value of the city property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getCity() {
    return city;
  }

  /**
   * Sets the value of the city property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setCity(String value) {
    this.city = value;
  }

  /**
   * Gets the value of the state property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getState() {
    return state;
  }

  /**
   * Sets the value of the state property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setState(String value) {
    this.state = value;
  }

  /**
   * Gets the value of the postalCode property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
  public String getPostalCode() {
    return postalCode;
  }

  /**
   * Sets the value of the postalCode property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
  public void setPostalCode(String value) {
    this.postalCode = value;
  }

  //----------------------------------------------------------------------------
  // Add any custom codes here
  //----------------------------------------------------------------------------
  @Override
  public String toString() {
    StringBuffer str = new StringBuffer();

    str.append("AddressFactType[streetAddress=" + streetAddress);
    str.append(",streetAddress2=" + streetAddress + ",state=" + state);
    str.append(",city=" + city + ",state=" + state);
    str.append(",postalCode=" + postalCode + "]");

    return str.toString();
  }
}
