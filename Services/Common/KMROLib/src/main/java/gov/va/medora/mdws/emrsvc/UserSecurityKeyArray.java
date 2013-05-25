
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for UserSecurityKeyArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UserSecurityKeyArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractArrayTO">
 *       &lt;sequence>
 *         &lt;element name="keys" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfUserSecurityKeyTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserSecurityKeyArray", propOrder = {
    "keys"
})
public class UserSecurityKeyArray
    extends AbstractArrayTO
{

    protected ArrayOfUserSecurityKeyTO keys;

    /**
     * Gets the value of the keys property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfUserSecurityKeyTO }
     *     
     */
    public ArrayOfUserSecurityKeyTO getKeys() {
        return keys;
    }

    /**
     * Sets the value of the keys property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfUserSecurityKeyTO }
     *     
     */
    public void setKeys(ArrayOfUserSecurityKeyTO value) {
        this.keys = value;
    }

}
