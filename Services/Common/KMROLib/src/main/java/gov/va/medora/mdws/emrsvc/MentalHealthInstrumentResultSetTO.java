
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MentalHealthInstrumentResultSetTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MentalHealthInstrumentResultSetTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTO">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="administrationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scale" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *         &lt;element name="rawScore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transformedScores" type="{http://mdws.medora.va.gov/EmrSvc}TaggedTextArray" minOccurs="0"/>
 *         &lt;element name="instrument" type="{http://mdws.medora.va.gov/EmrSvc}TaggedText" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MentalHealthInstrumentResultSetTO", propOrder = {
    "id",
    "administrationId",
    "scale",
    "rawScore",
    "transformedScores",
    "instrument"
})
public class MentalHealthInstrumentResultSetTO
    extends AbstractTO
{

    protected String id;
    protected String administrationId;
    protected TaggedText scale;
    protected String rawScore;
    protected TaggedTextArray transformedScores;
    protected TaggedText instrument;

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
     * Gets the value of the administrationId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdministrationId() {
        return administrationId;
    }

    /**
     * Sets the value of the administrationId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdministrationId(String value) {
        this.administrationId = value;
    }

    /**
     * Gets the value of the scale property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedText }
     *     
     */
    public TaggedText getScale() {
        return scale;
    }

    /**
     * Sets the value of the scale property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedText }
     *     
     */
    public void setScale(TaggedText value) {
        this.scale = value;
    }

    /**
     * Gets the value of the rawScore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRawScore() {
        return rawScore;
    }

    /**
     * Sets the value of the rawScore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRawScore(String value) {
        this.rawScore = value;
    }

    /**
     * Gets the value of the transformedScores property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedTextArray }
     *     
     */
    public TaggedTextArray getTransformedScores() {
        return transformedScores;
    }

    /**
     * Sets the value of the transformedScores property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedTextArray }
     *     
     */
    public void setTransformedScores(TaggedTextArray value) {
        this.transformedScores = value;
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

}
