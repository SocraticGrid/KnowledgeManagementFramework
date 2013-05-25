
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaggedPatientAssociateArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaggedPatientAssociateArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTaggedArrayTO">
 *       &lt;sequence>
 *         &lt;element name="pas" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfPatientAssociateTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaggedPatientAssociateArray", propOrder = {
    "pas"
})
public class TaggedPatientAssociateArray
    extends AbstractTaggedArrayTO
{

    protected ArrayOfPatientAssociateTO pas;

    /**
     * Gets the value of the pas property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPatientAssociateTO }
     *     
     */
    public ArrayOfPatientAssociateTO getPas() {
        return pas;
    }

    /**
     * Sets the value of the pas property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPatientAssociateTO }
     *     
     */
    public void setPas(ArrayOfPatientAssociateTO value) {
        this.pas = value;
    }

}
