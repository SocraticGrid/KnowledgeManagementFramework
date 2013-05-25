
package gov.va.medora.mdws.emrsvc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTaggedPatientAssociateArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTaggedPatientAssociateArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaggedPatientAssociateArray" type="{http://mdws.medora.va.gov/EmrSvc}TaggedPatientAssociateArray" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTaggedPatientAssociateArray", propOrder = {
    "taggedPatientAssociateArray"
})
public class ArrayOfTaggedPatientAssociateArray {

    @XmlElement(name = "TaggedPatientAssociateArray", nillable = true)
    protected List<TaggedPatientAssociateArray> taggedPatientAssociateArray;

    /**
     * Gets the value of the taggedPatientAssociateArray property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taggedPatientAssociateArray property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaggedPatientAssociateArray().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaggedPatientAssociateArray }
     * 
     * 
     */
    public List<TaggedPatientAssociateArray> getTaggedPatientAssociateArray() {
        if (taggedPatientAssociateArray == null) {
            taggedPatientAssociateArray = new ArrayList<TaggedPatientAssociateArray>();
        }
        return this.taggedPatientAssociateArray;
    }

}
