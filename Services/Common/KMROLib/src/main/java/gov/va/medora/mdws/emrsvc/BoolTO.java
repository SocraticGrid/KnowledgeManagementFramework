
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BoolTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BoolTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTO">
 *       &lt;sequence>
 *         &lt;element name="trueOrFalse" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoolTO", propOrder = {
    "trueOrFalse"
})
public class BoolTO
    extends AbstractTO
{

    protected boolean trueOrFalse;

    /**
     * Gets the value of the trueOrFalse property.
     * 
     */
    public boolean isTrueOrFalse() {
        return trueOrFalse;
    }

    /**
     * Sets the value of the trueOrFalse property.
     * 
     */
    public void setTrueOrFalse(boolean value) {
        this.trueOrFalse = value;
    }

}
