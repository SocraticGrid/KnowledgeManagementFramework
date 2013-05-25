
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="getOrderDialogItemsResult" type="{http://mdws.medora.va.gov/EmrSvc}OrderDialogItemArray" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getOrderDialogItemsResult"
})
@XmlRootElement(name = "getOrderDialogItemsResponse")
public class GetOrderDialogItemsResponse {

    protected OrderDialogItemArray getOrderDialogItemsResult;

    /**
     * Gets the value of the getOrderDialogItemsResult property.
     * 
     * @return
     *     possible object is
     *     {@link OrderDialogItemArray }
     *     
     */
    public OrderDialogItemArray getGetOrderDialogItemsResult() {
        return getOrderDialogItemsResult;
    }

    /**
     * Sets the value of the getOrderDialogItemsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderDialogItemArray }
     *     
     */
    public void setGetOrderDialogItemsResult(OrderDialogItemArray value) {
        this.getOrderDialogItemsResult = value;
    }

}
