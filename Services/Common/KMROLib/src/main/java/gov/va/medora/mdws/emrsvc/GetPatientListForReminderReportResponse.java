
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
 *         &lt;element name="getPatientListForReminderReportResult" type="{http://mdws.medora.va.gov/EmrSvc}ReminderReportPatientListTO" minOccurs="0"/>
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
    "getPatientListForReminderReportResult"
})
@XmlRootElement(name = "getPatientListForReminderReportResponse")
public class GetPatientListForReminderReportResponse {

    protected ReminderReportPatientListTO getPatientListForReminderReportResult;

    /**
     * Gets the value of the getPatientListForReminderReportResult property.
     * 
     * @return
     *     possible object is
     *     {@link ReminderReportPatientListTO }
     *     
     */
    public ReminderReportPatientListTO getGetPatientListForReminderReportResult() {
        return getPatientListForReminderReportResult;
    }

    /**
     * Sets the value of the getPatientListForReminderReportResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ReminderReportPatientListTO }
     *     
     */
    public void setGetPatientListForReminderReportResult(ReminderReportPatientListTO value) {
        this.getPatientListForReminderReportResult = value;
    }

}
