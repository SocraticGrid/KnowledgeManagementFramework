
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
 *         &lt;element name="getChemHemReportsSimpleResult" type="{http://mdws.medora.va.gov/EmrSvc}TaggedChemHemRptArrays" minOccurs="0"/>
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
    "getChemHemReportsSimpleResult"
})
@XmlRootElement(name = "getChemHemReportsSimpleResponse")
public class GetChemHemReportsSimpleResponse {

    protected TaggedChemHemRptArrays getChemHemReportsSimpleResult;

    /**
     * Gets the value of the getChemHemReportsSimpleResult property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedChemHemRptArrays }
     *     
     */
    public TaggedChemHemRptArrays getGetChemHemReportsSimpleResult() {
        return getChemHemReportsSimpleResult;
    }

    /**
     * Sets the value of the getChemHemReportsSimpleResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedChemHemRptArrays }
     *     
     */
    public void setGetChemHemReportsSimpleResult(TaggedChemHemRptArrays value) {
        this.getChemHemReportsSimpleResult = value;
    }

}
