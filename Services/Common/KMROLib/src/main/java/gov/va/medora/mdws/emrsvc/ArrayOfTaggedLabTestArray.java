
package gov.va.medora.mdws.emrsvc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfTaggedLabTestArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTaggedLabTestArray">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TaggedLabTestArray" type="{http://mdws.medora.va.gov/EmrSvc}TaggedLabTestArray" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTaggedLabTestArray", propOrder = {
    "taggedLabTestArray"
})
public class ArrayOfTaggedLabTestArray {

    @XmlElement(name = "TaggedLabTestArray", nillable = true)
    protected List<TaggedLabTestArray> taggedLabTestArray;

    /**
     * Gets the value of the taggedLabTestArray property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the taggedLabTestArray property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTaggedLabTestArray().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TaggedLabTestArray }
     * 
     * 
     */
    public List<TaggedLabTestArray> getTaggedLabTestArray() {
        if (taggedLabTestArray == null) {
            taggedLabTestArray = new ArrayList<TaggedLabTestArray>();
        }
        return this.taggedLabTestArray;
    }

}
