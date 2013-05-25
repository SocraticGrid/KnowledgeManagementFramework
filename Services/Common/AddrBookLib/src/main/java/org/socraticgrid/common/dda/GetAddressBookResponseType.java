
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

package org.socraticgrid.common.dda;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetAddressBookResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetAddressBookResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetAddressResponse" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="successStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   &lt;element name="statusMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="contactId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="address1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="address2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="zipCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="phones" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
 *                   &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAddressBookResponseType", propOrder = {
    "getAddressResponse"
})
public class GetAddressBookResponseType {

    @XmlElement(name = "GetAddressResponse")
    protected List<GetAddressBookResponseType.GetAddressResponse> getAddressResponse;

    /**
     * Gets the value of the getAddressResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getAddressResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetAddressResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetAddressBookResponseType.GetAddressResponse }
     * 
     * 
     */
    public List<GetAddressBookResponseType.GetAddressResponse> getGetAddressResponse() {
        if (getAddressResponse == null) {
            getAddressResponse = new ArrayList<GetAddressBookResponseType.GetAddressResponse>();
        }
        return this.getAddressResponse;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="successStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         &lt;element name="statusMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="contactId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="address1" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="address2" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="city" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="zipCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="phones" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded"/>
     *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "successStatus",
        "statusMessage",
        "contactId",
        "name",
        "address1",
        "address2",
        "city",
        "state",
        "zipCode",
        "phones",
        "email"
    })
    public static class GetAddressResponse {

        protected boolean successStatus;
        @XmlElement(required = true)
        protected String statusMessage;
        @XmlElement(required = true)
        protected String contactId;
        @XmlElement(required = true)
        protected String name;
        @XmlElement(required = true)
        protected String address1;
        @XmlElement(required = true)
        protected String address2;
        @XmlElement(required = true)
        protected String city;
        @XmlElement(required = true)
        protected String state;
        @XmlElement(required = true)
        protected String zipCode;
        @XmlElement(required = true)
        protected List<String> phones;
        @XmlElement(required = true)
        protected String email;

        /**
         * Gets the value of the successStatus property.
         * 
         */
        public boolean isSuccessStatus() {
            return successStatus;
        }

        /**
         * Sets the value of the successStatus property.
         * 
         */
        public void setSuccessStatus(boolean value) {
            this.successStatus = value;
        }

        /**
         * Gets the value of the statusMessage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getStatusMessage() {
            return statusMessage;
        }

        /**
         * Sets the value of the statusMessage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setStatusMessage(String value) {
            this.statusMessage = value;
        }

        /**
         * Gets the value of the contactId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getContactId() {
            return contactId;
        }

        /**
         * Sets the value of the contactId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setContactId(String value) {
            this.contactId = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the address1 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAddress1() {
            return address1;
        }

        /**
         * Sets the value of the address1 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAddress1(String value) {
            this.address1 = value;
        }

        /**
         * Gets the value of the address2 property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAddress2() {
            return address2;
        }

        /**
         * Sets the value of the address2 property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAddress2(String value) {
            this.address2 = value;
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
         * Gets the value of the zipCode property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getZipCode() {
            return zipCode;
        }

        /**
         * Sets the value of the zipCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setZipCode(String value) {
            this.zipCode = value;
        }

        /**
         * Gets the value of the phones property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the phones property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPhones().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getPhones() {
            if (phones == null) {
                phones = new ArrayList<String>();
            }
            return this.phones;
        }

        /**
         * Gets the value of the email property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getEmail() {
            return email;
        }

        /**
         * Sets the value of the email property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setEmail(String value) {
            this.email = value;
        }

    }

}
