
package mil.navy.med.dzreg.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PersonRegistryProfileResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PersonRegistryProfileResponseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="profile" type="{urn:mil:navy:med:dzreg:types}PersonRegistryProfileType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonRegistryProfileResponseType", propOrder = {
    "profile"
})
public class PersonRegistryProfileResponseType {

    protected PersonRegistryProfileType profile;

    /**
     * Gets the value of the profile property.
     * 
     * @return
     *     possible object is
     *     {@link PersonRegistryProfileType }
     *     
     */
    public PersonRegistryProfileType getProfile() {
        return profile;
    }

    /**
     * Sets the value of the profile property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonRegistryProfileType }
     *     
     */
    public void setProfile(PersonRegistryProfileType value) {
        this.profile = value;
    }

}
