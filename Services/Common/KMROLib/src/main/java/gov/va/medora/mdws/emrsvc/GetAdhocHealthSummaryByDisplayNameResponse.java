
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
 *         &lt;element name="getAdhocHealthSummaryByDisplayNameResult" type="{http://mdws.medora.va.gov/EmrSvc}TaggedTextArray" minOccurs="0"/>
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
    "getAdhocHealthSummaryByDisplayNameResult"
})
@XmlRootElement(name = "getAdhocHealthSummaryByDisplayNameResponse")
public class GetAdhocHealthSummaryByDisplayNameResponse {

    protected TaggedTextArray getAdhocHealthSummaryByDisplayNameResult;

    /**
     * Gets the value of the getAdhocHealthSummaryByDisplayNameResult property.
     * 
     * @return
     *     possible object is
     *     {@link TaggedTextArray }
     *     
     */
    public TaggedTextArray getGetAdhocHealthSummaryByDisplayNameResult() {
        return getAdhocHealthSummaryByDisplayNameResult;
    }

    /**
     * Sets the value of the getAdhocHealthSummaryByDisplayNameResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaggedTextArray }
     *     
     */
    public void setGetAdhocHealthSummaryByDisplayNameResult(TaggedTextArray value) {
        this.getAdhocHealthSummaryByDisplayNameResult = value;
    }

}
