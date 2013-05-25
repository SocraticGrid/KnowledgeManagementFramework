
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
 *         &lt;element name="getPatientAssociatesResult" type="{http://mdws.medora.va.gov/EmrSvc}TaggedPatientAssociateArrays" minOccurs="0"/>
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
    "getPatientAssociatesResult"
})
@XmlRootElement(name = "getPatientAssociatesResponse")
public class GetPatientAssociatesResponse {

    protected TaggedPatientAssociateArrays getPatientAssociatesResult;

    /**
     * Gets the value of the getPatientAssociatesResult property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedPatientAssociateArrays }
     *     
     */
    public TaggedPatientAssociateArrays getGetPatientAssociatesResult() {
        return getPatientAssociatesResult;
    }

    /**
     * Sets the value of the getPatientAssociatesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedPatientAssociateArrays }
     *     
     */
    public void setGetPatientAssociatesResult(TaggedPatientAssociateArrays value) {
        this.getPatientAssociatesResult = value;
    }

}
