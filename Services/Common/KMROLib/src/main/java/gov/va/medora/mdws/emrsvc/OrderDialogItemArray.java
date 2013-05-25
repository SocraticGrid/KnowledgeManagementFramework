
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderDialogItemArray complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderDialogItemArray">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractArrayTO">
 *       &lt;sequence>
 *         &lt;element name="items" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfOrderDialogItemTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OrderDialogItemArray", propOrder = {
    "items"
})
public class OrderDialogItemArray
    extends AbstractArrayTO
{

    protected ArrayOfOrderDialogItemTO items;

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfOrderDialogItemTO }
     *     
     */
    public ArrayOfOrderDialogItemTO getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfOrderDialogItemTO }
     *     
     */
    public void setItems(ArrayOfOrderDialogItemTO value) {
        this.items = value;
    }

}
