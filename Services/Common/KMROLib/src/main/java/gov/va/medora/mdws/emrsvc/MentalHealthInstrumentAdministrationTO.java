
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MentalHealthInstrumentAdministrationTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MentalHealthInstrumentAdministrationTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTO">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patient" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="instrument" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="dateAdministered" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateSaved" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderedBy" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="administeredBy" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="isSigned" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isComplete" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="numberOfQuestionsAnswered" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transmitStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transmitTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hospitalLocation" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="results" type="{http://mdws.medora.va.gov/EmrSvc}MentalHealthInstrumentResultSetTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MentalHealthInstrumentAdministrationTO", propOrder = {
    "id",
    "patient",
    "instrument",
    "dateAdministered",
    "dateSaved",
    "orderedBy",
    "administeredBy",
    "isSigned",
    "isComplete",
    "numberOfQuestionsAnswered",
    "transmitStatus",
    "transmitTime",
    "hospitalLocation",
    "results"
})
public class MentalHealthInstrumentAdministrationTO
    extends AbstractTO
{

    protected String id;
    protected TaggedText patient;
    protected TaggedText instrument;
    protected String dateAdministered;
    protected String dateSaved;
    protected TaggedText orderedBy;
    protected TaggedText administeredBy;
    protected boolean isSigned;
    protected boolean isComplete;
    protected String numberOfQuestionsAnswered;
    protected String transmitStatus;
    protected String transmitTime;
    protected TaggedText hospitalLocation;
    protected MentalHealthInstrumentResultSetTO results;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the patient property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getPatient() {
        return patient;
    }

    /**
     * Sets the value of the patient property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setPatient(TaggedText value) {
        this.patient = value;
    }

    /**
     * Gets the value of the instrument property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getInstrument() {
        return instrument;
    }

    /**
     * Sets the value of the instrument property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setInstrument(TaggedText value) {
        this.instrument = value;
    }

    /**
     * Gets the value of the dateAdministered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateAdministered() {
        return dateAdministered;
    }

    /**
     * Sets the value of the dateAdministered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateAdministered(String value) {
        this.dateAdministered = value;
    }

    /**
     * Gets the value of the dateSaved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateSaved() {
        return dateSaved;
    }

    /**
     * Sets the value of the dateSaved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateSaved(String value) {
        this.dateSaved = value;
    }

    /**
     * Gets the value of the orderedBy property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getOrderedBy() {
        return orderedBy;
    }

    /**
     * Sets the value of the orderedBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setOrderedBy(TaggedText value) {
        this.orderedBy = value;
    }

    /**
     * Gets the value of the administeredBy property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getAdministeredBy() {
        return administeredBy;
    }

    /**
     * Sets the value of the administeredBy property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setAdministeredBy(TaggedText value) {
        this.administeredBy = value;
    }

    /**
     * Gets the value of the isSigned property.
     * 
     */
    public boolean isIsSigned() {
        return isSigned;
    }

    /**
     * Sets the value of the isSigned property.
     * 
     */
    public void setIsSigned(boolean value) {
        this.isSigned = value;
    }

    /**
     * Gets the value of the isComplete property.
     * 
     */
    public boolean isIsComplete() {
        return isComplete;
    }

    /**
     * Sets the value of the isComplete property.
     * 
     */
    public void setIsComplete(boolean value) {
        this.isComplete = value;
    }

    /**
     * Gets the value of the numberOfQuestionsAnswered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfQuestionsAnswered() {
        return numberOfQuestionsAnswered;
    }

    /**
     * Sets the value of the numberOfQuestionsAnswered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfQuestionsAnswered(String value) {
        this.numberOfQuestionsAnswered = value;
    }

    /**
     * Gets the value of the transmitStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmitStatus() {
        return transmitStatus;
    }

    /**
     * Sets the value of the transmitStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmitStatus(String value) {
        this.transmitStatus = value;
    }

    /**
     * Gets the value of the transmitTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransmitTime() {
        return transmitTime;
    }

    /**
     * Sets the value of the transmitTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransmitTime(String value) {
        this.transmitTime = value;
    }

    /**
     * Gets the value of the hospitalLocation property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getHospitalLocation() {
        return hospitalLocation;
    }

    /**
     * Sets the value of the hospitalLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setHospitalLocation(TaggedText value) {
        this.hospitalLocation = value;
    }

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link MentalHealthInstrumentResultSetTO }
     *     
     */
    public MentalHealthInstrumentResultSetTO getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link MentalHealthInstrumentResultSetTO }
     *     
     */
    public void setResults(MentalHealthInstrumentResultSetTO value) {
        this.results = value;
    }

}
