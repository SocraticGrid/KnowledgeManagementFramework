/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.ws.facts;

import javax.jws.WebService;
import org.socraticgrid.ps.PatientDataRequestType;

/**
 * Sample POST call:
 *           http://172.31.5.82:8080/PresentationServices/PatientDataService
 * With SOAP request:

    <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:org:socraticgrid:ps">
    <soapenv:Header/>
    <soapenv:Body>
    <urn:PatientDataRequest>
    <urn:patientId>100022</urn:patientId>
    <urn:domain>lab</urn:domain>
    <urn:dataSource>MDWS-PROD</urn:dataSource>
    </urn:PatientDataRequest>
    </soapenv:Body>
    </soapenv:Envelope>

 * 
 * @author tnguyen
 */
@WebService(
        serviceName = "PatientDataService",
        portName = "PatientDataServicePortSoap11",
        endpointInterface = "org.socraticgrid.ps.PatientDataPortType",
        targetNamespace = "urn:org:socraticgrid:ps",
        wsdlLocation = "WEB-INF/wsdl/PatientDataService.wsdl")
public class PatientDataService {

    public String getPatientData(PatientDataRequestType patientDataRequest) {
        return new PatientDataImpl().getPatientData(patientDataRequest);
        //throw new UnsupportedOperationException("Not implemented yet.");
    }

}
