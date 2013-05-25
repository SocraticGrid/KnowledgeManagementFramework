
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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for GetMessagesResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetMessagesResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GetMessageResponse" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="messageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="labels" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="messageDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="messageStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="tasksCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="tasksComplete" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="requestDocumentStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="statusMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="successStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
@XmlType(name = "GetMessagesResponseType", propOrder = {
    "getMessageResponse"
})
public class GetMessagesResponseType {

    @XmlElement(name = "GetMessageResponse")
    protected List<GetMessagesResponseType.GetMessageResponse> getMessageResponse;

    /**
     * Gets the value of the getMessageResponse property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the getMessageResponse property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGetMessageResponse().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GetMessagesResponseType.GetMessageResponse }
     * 
     * 
     */
    public List<GetMessagesResponseType.GetMessageResponse> getGetMessageResponse() {
        if (getMessageResponse == null) {
            getMessageResponse = new ArrayList<GetMessagesResponseType.GetMessageResponse>();
        }
        return this.getMessageResponse;
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
     *         &lt;element name="messageType" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="labels" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="messageId" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="messageDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="from" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="title" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="messageStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="tasksCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="tasksComplete" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="requestDocumentStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="statusMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="successStatus" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
        "messageType",
        "location",
        "labels",
        "messageId",
        "messageDate",
        "description",
        "from",
        "title",
        "messageStatus",
        "priority",
        "tasksCount",
        "tasksComplete",
        "requestDocumentStatus",
        "statusMessage",
        "successStatus"
    })
    public static class GetMessageResponse {

        @XmlElement(required = true)
        protected String messageType;
        @XmlElement(required = true)
        protected String location;
        @XmlElement(nillable = true)
        protected List<String> labels;
        @XmlElement(required = true)
        protected String messageId;
        @XmlElement(required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar messageDate;
        @XmlElement(required = true)
        protected String description;
        @XmlElement(required = true)
        protected String from;
        @XmlElement(required = true)
        protected String title;
        @XmlElement(required = true)
        protected String messageStatus;
        @XmlElement(required = true)
        protected String priority;
        protected int tasksCount;
        protected int tasksComplete;
        @XmlElement(required = true)
        protected String requestDocumentStatus;
        @XmlElement(required = true)
        protected String statusMessage;
        protected boolean successStatus;

        /**
         * Gets the value of the messageType property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessageType() {
            return messageType;
        }

        /**
         * Sets the value of the messageType property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessageType(String value) {
            this.messageType = value;
        }

        /**
         * Gets the value of the location property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getLocation() {
            return location;
        }

        /**
         * Sets the value of the location property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setLocation(String value) {
            this.location = value;
        }

        /**
         * Gets the value of the labels property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the labels property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getLabels().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link String }
         * 
         * 
         */
        public List<String> getLabels() {
            if (labels == null) {
                labels = new ArrayList<String>();
            }
            return this.labels;
        }

        /**
         * Gets the value of the messageId property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessageId() {
            return messageId;
        }

        /**
         * Sets the value of the messageId property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessageId(String value) {
            this.messageId = value;
        }

        /**
         * Gets the value of the messageDate property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getMessageDate() {
            return messageDate;
        }

        /**
         * Sets the value of the messageDate property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setMessageDate(XMLGregorianCalendar value) {
            this.messageDate = value;
        }

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

        /**
         * Gets the value of the from property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getFrom() {
            return from;
        }

        /**
         * Sets the value of the from property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setFrom(String value) {
            this.from = value;
        }

        /**
         * Gets the value of the title property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getTitle() {
            return title;
        }

        /**
         * Sets the value of the title property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setTitle(String value) {
            this.title = value;
        }

        /**
         * Gets the value of the messageStatus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getMessageStatus() {
            return messageStatus;
        }

        /**
         * Sets the value of the messageStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setMessageStatus(String value) {
            this.messageStatus = value;
        }

        /**
         * Gets the value of the priority property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getPriority() {
            return priority;
        }

        /**
         * Sets the value of the priority property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setPriority(String value) {
            this.priority = value;
        }

        /**
         * Gets the value of the tasksCount property.
         * 
         */
        public int getTasksCount() {
            return tasksCount;
        }

        /**
         * Sets the value of the tasksCount property.
         * 
         */
        public void setTasksCount(int value) {
            this.tasksCount = value;
        }

        /**
         * Gets the value of the tasksComplete property.
         * 
         */
        public int getTasksComplete() {
            return tasksComplete;
        }

        /**
         * Sets the value of the tasksComplete property.
         * 
         */
        public void setTasksComplete(int value) {
            this.tasksComplete = value;
        }

        /**
         * Gets the value of the requestDocumentStatus property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getRequestDocumentStatus() {
            return requestDocumentStatus;
        }

        /**
         * Sets the value of the requestDocumentStatus property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setRequestDocumentStatus(String value) {
            this.requestDocumentStatus = value;
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

    }

}
