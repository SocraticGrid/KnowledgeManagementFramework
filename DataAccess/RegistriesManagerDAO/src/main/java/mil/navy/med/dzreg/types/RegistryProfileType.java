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

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for RegistryProfileType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RegistryProfileType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:mil:navy:med:dzreg:types}RegistryType">
 *       &lt;sequence>
 *         &lt;element name="registeredDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="active" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RegistryProfileType", propOrder = {
  "registeredDate",
  "active"
})
public class RegistryProfileType
        extends RegistryType {

  @XmlElement(name = "active", required = true)
  protected boolean active;
  @XmlElement(name = "registeredDate", required = true, type = Date.class)
  @XmlSchemaType(name = "dateTime")
  protected Date registeredDate;

  /**
   * Gets the value of the active property.
   *
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Sets the value of the active property.
   *
   */
  public void setActive(boolean value) {
    this.active = value;
  }

  /**
   * Gets the value of the registeredDate property.
   *
   * @return
   *     possible object is
   *     {@link Date }
   *
   */
  public Date getRegisteredDate() {
    return registeredDate;
  }

  /**
   * Sets the value of the registeredDate property.
   *
   * @param value
   *     allowed object is
   *     {@link Date }
   *
   */
  public void setRegisteredDate(Date value) {
    this.registeredDate = value;
  }

  //----------------------------------------------------------------------------
  // Add any custom codes here
  //----------------------------------------------------------------------------
  public RegistryProfileType(int registryId, String registryDesc, boolean active, Date registeredDate) {
    super(registryId, registryDesc);
    this.active = active;
    this.registeredDate = registeredDate;
  }

  public RegistryProfileType(int registryId, String registryDesc) {
    super(registryId, registryDesc);
  }

  public RegistryProfileType(RegistryType registry) {
    super(registry);
  }

  public RegistryProfileType() {
    super();
  }

  @Override
  public String toString() {
    StringBuffer str = new StringBuffer();

    str.append("RegistryProfileType[registryId=" + registryId);
    str.append(",registryDesc=" + registryDesc + ",registeredDate=" + registeredDate);
    str.append(",active=" + active + "]");

    return str.toString();
  }
}
