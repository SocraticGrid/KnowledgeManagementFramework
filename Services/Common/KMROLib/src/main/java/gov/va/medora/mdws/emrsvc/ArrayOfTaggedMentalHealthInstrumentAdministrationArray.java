
package gov.va.medora.mdws.emrsvc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTaggedMentalHealthInstrumentAdministrationArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTaggedMentalHealthInstrumentAdministrationArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaggedMentalHealthInstrumentAdministrationArray" type="{http://mdws.medora.va.gov/EmrSvc}TaggedMentalHealthInstrumentAdministrationArray" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTaggedMentalHealthInstrumentAdministrationArray", propOrder = {
    "taggedMentalHealthInstrumentAdministrationArray"
})
public class ArrayOfTaggedMentalHealthInstrumentAdministrationArray {

    @XmlElement(name = "TaggedMentalHealthInstrumentAdministrationArray", nillable = true)
    protected List<TaggedMentalHealthInstrumentAdministrationArray> taggedMentalHealthInstrumentAdministrationArray;

    /**
     * Gets the value of the taggedMentalHealthInstrumentAdministrationArray property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taggedMentalHealthInstrumentAdministrationArray property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaggedMentalHealthInstrumentAdministrationArray().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaggedMentalHealthInstrumentAdministrationArray }
     * 
     * 
     */
    public List<TaggedMentalHealthInstrumentAdministrationArray> getTaggedMentalHealthInstrumentAdministrationArray() {
        if (taggedMentalHealthInstrumentAdministrationArray == null) {
            taggedMentalHealthInstrumentAdministrationArray = new ArrayList<TaggedMentalHealthInstrumentAdministrationArray>();
        }
        return this.taggedMentalHealthInstrumentAdministrationArray;
    }

}
