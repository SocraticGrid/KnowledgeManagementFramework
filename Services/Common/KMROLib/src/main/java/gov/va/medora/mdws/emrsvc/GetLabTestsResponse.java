
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
 *         &lt;element name="getLabTestsResult" type="{http://mdws.medora.va.gov/EmrSvc}TaggedLabTestArrays" minOccurs="0"/>
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
    "getLabTestsResult"
})
@XmlRootElement(name = "getLabTestsResponse")
public class GetLabTestsResponse {

    protected TaggedLabTestArrays getLabTestsResult;

    /**
     * Gets the value of the getLabTestsResult property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedLabTestArrays }
     *     
     */
    public TaggedLabTestArrays getGetLabTestsResult() {
        return getLabTestsResult;
    }

    /**
     * Sets the value of the getLabTestsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedLabTestArrays }
     *     
     */
    public void setGetLabTestsResult(TaggedLabTestArrays value) {
        this.getLabTestsResult = value;
    }

}
