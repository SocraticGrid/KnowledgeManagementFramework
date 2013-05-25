
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaggedMentalHealthInstrumentAdministrationArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaggedMentalHealthInstrumentAdministrationArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTaggedArrayTO">
 *       &lt;sequence>
 *         &lt;element name="items" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfMentalHealthInstrumentAdministrationTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaggedMentalHealthInstrumentAdministrationArray", propOrder = {
    "items"
})
public class TaggedMentalHealthInstrumentAdministrationArray
    extends AbstractTaggedArrayTO
{

    protected ArrayOfMentalHealthInstrumentAdministrationTO items;

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMentalHealthInstrumentAdministrationTO }
     *     
     */
    public ArrayOfMentalHealthInstrumentAdministrationTO getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMentalHealthInstrumentAdministrationTO }
     *     
     */
    public void setItems(ArrayOfMentalHealthInstrumentAdministrationTO value) {
        this.items = value;
    }

}
