
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
 *         &lt;element name="userHasPermissionResult" type="{http://mdws.medora.va.gov/EmrSvc}BoolTO" minOccurs="0"/>
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
    "userHasPermissionResult"
})
@XmlRootElement(name = "userHasPermissionResponse")
public class UserHasPermissionResponse {

    protected BoolTO userHasPermissionResult;

    /**
     * Gets the value of the userHasPermissionResult property.
     * 
     * @return
     *     possible object is
     *     {@link BoolTO }
     *     
     */
    public BoolTO getUserHasPermissionResult() {
        return userHasPermissionResult;
    }

    /**
     * Sets the value of the userHasPermissionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link BoolTO }
     *     
     */
    public void setUserHasPermissionResult(BoolTO value) {
        this.userHasPermissionResult = value;
    }

}
