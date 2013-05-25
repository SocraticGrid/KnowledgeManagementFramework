
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="getMentalHealthInstrumentResultSetResult" type="{http://mdws.medora.va.gov/EmrSvc}MentalHealthInstrumentResultSetTO" minOccurs="0"/>
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
    "getMentalHealthInstrumentResultSetResult"
})
@XmlRootElement(name = "getMentalHealthInstrumentResultSetResponse")
public class GetMentalHealthInstrumentResultSetResponse {

    protected MentalHealthInstrumentResultSetTO getMentalHealthInstrumentResultSetResult;

    /**
     * Gets the value of the getMentalHealthInstrumentResultSetResult property.
     * 
     * @return
     *     possible object is
     *     {@link MentalHealthInstrumentResultSetTO }
     *     
     */
    public MentalHealthInstrumentResultSetTO getGetMentalHealthInstrumentResultSetResult() {
        return getMentalHealthInstrumentResultSetResult;
    }

    /**
     * Sets the value of the getMentalHealthInstrumentResultSetResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link MentalHealthInstrumentResultSetTO }
     *     
     */
    public void setGetMentalHealthInstrumentResultSetResult(MentalHealthInstrumentResultSetTO value) {
        this.getMentalHealthInstrumentResultSetResult = value;
    }

}
