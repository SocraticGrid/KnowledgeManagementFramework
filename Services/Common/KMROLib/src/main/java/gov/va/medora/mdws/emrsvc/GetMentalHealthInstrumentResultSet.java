
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
 *         &lt;element name="sitecode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="administrationId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "sitecode",
    "administrationId"
})
@XmlRootElement(name = "getMentalHealthInstrumentResultSet")
public class GetMentalHealthInstrumentResultSet {

    protected String sitecode;
    protected String administrationId;

    /**
     * Gets the value of the sitecode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSitecode() {
        return sitecode;
    }

    /**
     * Sets the value of the sitecode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSitecode(String value) {
        this.sitecode = value;
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

}
