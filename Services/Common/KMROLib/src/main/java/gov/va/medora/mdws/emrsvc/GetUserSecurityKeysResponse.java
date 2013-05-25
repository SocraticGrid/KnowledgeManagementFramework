
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
 *         &lt;element name="getUserSecurityKeysResult" type="{http://mdws.medora.va.gov/EmrSvc}UserSecurityKeyArray" minOccurs="0"/>
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
    "getUserSecurityKeysResult"
})
@XmlRootElement(name = "getUserSecurityKeysResponse")
public class GetUserSecurityKeysResponse {

    protected UserSecurityKeyArray getUserSecurityKeysResult;

    /**
     * Gets the value of the getUserSecurityKeysResult property.
     * 
     * @return
     *     possible object is
     *     {@link UserSecurityKeyArray }
     *     
     */
    public UserSecurityKeyArray getGetUserSecurityKeysResult() {
        return getUserSecurityKeysResult;
    }

    /**
     * Sets the value of the getUserSecurityKeysResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserSecurityKeyArray }
     *     
     */
    public void setGetUserSecurityKeysResult(UserSecurityKeyArray value) {
        this.getUserSecurityKeysResult = value;
    }

}
