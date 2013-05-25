
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
 *         &lt;element name="getOrderStatusForPatientResult" type="{http://mdws.medora.va.gov/EmrSvc}TextTO" minOccurs="0"/>
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
    "getOrderStatusForPatientResult"
})
@XmlRootElement(name = "getOrderStatusForPatientResponse")
public class GetOrderStatusForPatientResponse {

    protected TextTO getOrderStatusForPatientResult;

    /**
     * Gets the value of the getOrderStatusForPatientResult property.
     * 
     * @return
     *     possible object is
     *     {@link TextTO }
     *     
     */
    public TextTO getGetOrderStatusForPatientResult() {
        return getOrderStatusForPatientResult;
    }

    /**
     * Sets the value of the getOrderStatusForPatientResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TextTO }
     *     
     */
    public void setGetOrderStatusForPatientResult(TextTO value) {
        this.getOrderStatusForPatientResult = value;
    }

}
