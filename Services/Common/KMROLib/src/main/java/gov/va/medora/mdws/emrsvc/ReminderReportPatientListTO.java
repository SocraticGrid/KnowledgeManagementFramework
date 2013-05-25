
package gov.va.medora.mdws.emrsvc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReminderReportPatientListTO complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReminderReportPatientListTO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://mdws.medora.va.gov/EmrSvc}AbstractTO">
 *       &lt;sequence>
 *         &lt;element name="reportId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="reportTimestamp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="patients" type="{http://mdws.medora.va.gov/EmrSvc}ArrayOfPatientListEntryTO" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReminderReportPatientListTO", propOrder = {
    "reportId",
    "reportName",
    "reportTimestamp",
    "patients"
})
public class ReminderReportPatientListTO
    extends AbstractTO
{

    protected String reportId;
    protected String reportName;
    protected String reportTimestamp;
    protected ArrayOfPatientListEntryTO patients;

    /**
     * Gets the value of the reportId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportId() {
        return reportId;
    }

    /**
     * Sets the value of the reportId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportId(String value) {
        this.reportId = value;
    }

    /**
     * Gets the value of the reportName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * Sets the value of the reportName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportName(String value) {
        this.reportName = value;
    }

    /**
     * Gets the value of the reportTimestamp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportTimestamp() {
        return reportTimestamp;
    }

    /**
     * Sets the value of the reportTimestamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportTimestamp(String value) {
        this.reportTimestamp = value;
    }

    /**
     * Gets the value of the patients property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfPatientListEntryTO }
     *     
     */
    public ArrayOfPatientListEntryTO getPatients() {
        return patients;
    }

    /**
     * Sets the value of the patients property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfPatientListEntryTO }
     *     
     */
    public void setPatients(ArrayOfPatientListEntryTO value) {
        this.patients = value;
    }

}
