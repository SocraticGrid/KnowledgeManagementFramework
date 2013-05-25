
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LabTestArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LabTestArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractArrayTO">
 *       &lt;sequence>
 *         &lt;element name="tests" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfLabTestTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LabTestArray", propOrder = {
    "tests"
})
public class LabTestArray
    extends AbstractArrayTO
{

    protected ArrayOfLabTestTO tests;

    /**
     * Gets the value of the tests property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfLabTestTO }
     *     
     */
    public ArrayOfLabTestTO getTests() {
        return tests;
    }

    /**
     * Sets the value of the tests property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfLabTestTO }
     *     
     */
    public void setTests(ArrayOfLabTestTO value) {
        this.tests = value;
    }

}
