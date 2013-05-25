//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.30 at 05:30:59 PM PDT 
//


package org.socraticgrid.presentationservices.utils.factModels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PatientDemogFact {

    //protected String noNamespaceSchemaLocation;
    protected String factType;
    protected String trxnType;
    protected boolean visibleGridHeaders;
    protected int maxColumns;
    protected String itemId;
    protected List<ListTabs> listTabs;
    protected List<DetailTabs> detailTabs;
    protected List<GridHeaders> gridHeaders;
    protected Facts facts;
    protected boolean successStatus;
    protected String sectionId;
    protected String statusMessage;
    protected String patientId;

    public String getSectionId()                { return this.sectionId; }
    public String getStatusMessage()            { return this.statusMessage; }

    public void setSectionId(String s)          { this.sectionId = s; }
    public void setStatusMessage(String s)      { this.statusMessage = s; }

    public List<ListTabs> getListTabs() {
        if (this.listTabs == null) {
            this.listTabs = new ArrayList<ListTabs>();
        }
        return this.listTabs;
    }


    /**
     * Gets the value of the factType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFactType() {
        return factType;
    }

    /**
     * Sets the value of the factType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFactType(String value) {
        this.factType = value;
    }

    /**
     * Gets the value of the trxnType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrxnType() {
        return trxnType;
    }

    /**
     * Sets the value of the trxnType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrxnType(String value) {
        this.trxnType = value;
    }

    /**
     * Gets the value of the visibleGridHeaders property.
     * 
     */
    public boolean isVisibleGridHeaders() {
        return visibleGridHeaders;
    }

    /**
     * Sets the value of the visibleGridHeaders property.
     * 
     */
    public void setVisibleGridHeaders(boolean value) {
        this.visibleGridHeaders = value;
    }

    /**
     * Gets the value of the maxColumns property.
     * 
     */
    public int getMaxColumns() {
        return maxColumns;
    }

    /**
     * Sets the value of the maxColumns property.
     * 
     */
    public void setMaxColumns(int value) {
        this.maxColumns = value;
    }

    /**
     * Gets the value of the itemId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * Sets the value of the itemId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemId(String value) {
        this.itemId = value;
    }

    /**
     * Gets the value of the detailTabs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the detailTabs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDetailTabs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DetailTabs }
     * 
     * 
     */
    public List<DetailTabs> getDetailTabs() {
        if (detailTabs == null) {
            detailTabs = new ArrayList<DetailTabs>();
        }
        return this.detailTabs;
    }

    /**
     * Gets the value of the gridHeaders property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the gridHeaders property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGridHeaders().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link GridHeaders }
     * 
     * 
     */
    public List<GridHeaders> getGridHeaders() {
        if (gridHeaders == null) {
            gridHeaders = new ArrayList<GridHeaders>();
        }
        return this.gridHeaders;
    }

//    /**
//     * Gets the value of the facts property.
//     *
//     * <p>
//     * This accessor method returns a reference to the live list,
//     * not a snapshot. Therefore any modification you make to the
//     * returned list will be present inside the JAXB object.
//     * This is why there is not a <CODE>set</CODE> method for the facts property.
//     *
//     * <p>
//     * For example, to add a new item, do as follows:
//     * <pre>
//     *    getFacts().add(newItem);
//     * </pre>
//     *
//     *
//     * <p>
//     * Objects of the following type(s) are allowed in the list
//     * {@link Facts }
//     *
//     *
//     */
//    public List<Facts> getFacts() {
//        if (facts == null) {
//            facts = new ArrayList<Facts>();
//        }
//        return this.facts;
//    }
//    public void setFacts(Collection facts) {
//        this.facts.addAll(facts);
//    }

    public Facts getFacts()       { return this.facts; }
    public void setFacts(Facts f) { this.facts = f; }

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
