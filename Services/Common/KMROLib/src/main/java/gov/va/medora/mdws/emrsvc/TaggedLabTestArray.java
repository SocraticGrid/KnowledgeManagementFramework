
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TaggedLabTestArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TaggedLabTestArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTaggedArrayTO">
 *       &lt;sequence>
 *         &lt;element name="labTests" type="{http://mdws.medora.va.gov/EmrSvc}LabTestArray" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TaggedLabTestArray", propOrder = {
    "labTests"
})
public class TaggedLabTestArray
    extends AbstractTaggedArrayTO
{

    protected LabTestArray labTests;

    /**
     * Gets the value of the labTests property.
     * 
     * @return
     *     possible object is
     *     {@link LabTestArray }
     *     
     */
    public LabTestArray getLabTests() {
        return labTests;
    }

    /**
     * Sets the value of the labTests property.
     * 
     * @param value
     *     allowed object is
     *     {@link LabTestArray }
     *     
     */
    public void setLabTests(LabTestArray value) {
        this.labTests = value;
    }

}
