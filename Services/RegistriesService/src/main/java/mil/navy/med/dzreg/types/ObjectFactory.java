
package mil.navy.med.dzreg.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the mil.navy.med.dzreg.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RegisterPersonRequest_QNAME = new QName("urn:mil:navy:med:dzreg:types", "RegisterPersonRequest");
    private final static QName _PersonRegistryProfileRequest_QNAME = new QName("urn:mil:navy:med:dzreg:types", "PersonRegistryProfileRequest");
    private final static QName _RegistryTypeResponse_QNAME = new QName("urn:mil:navy:med:dzreg:types", "RegistryTypeResponse");
    private final static QName _PersonRegistryProfileResponse_QNAME = new QName("urn:mil:navy:med:dzreg:types", "PersonRegistryProfileResponse");
    private final static QName _UnregisterPersonRequest_QNAME = new QName("urn:mil:navy:med:dzreg:types", "UnregisterPersonRequest");
    private final static QName _ResponseAck_QNAME = new QName("urn:mil:navy:med:dzreg:types", "ResponseAck");
    private final static QName _PersonTypeAddress_QNAME = new QName("urn:mil:navy:med:dzreg:types", "address");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: mil.navy.med.dzreg.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RegistryType }
     * 
     */
    public RegistryType createRegistryType() {
        return new RegistryType();
    }

    /**
     * Create an instance of {@link RegistryTypeResponseType }
     * 
     */
    public RegistryTypeResponseType createRegistryTypeResponseType() {
        return new RegistryTypeResponseType();
    }

    /**
     * Create an instance of {@link RegistryProfileType }
     * 
     */
    public RegistryProfileType createRegistryProfileType() {
        return new RegistryProfileType();
    }

    /**
     * Create an instance of {@link PersonRegistryProfileType }
     * 
     */
    public PersonRegistryProfileType createPersonRegistryProfileType() {
        return new PersonRegistryProfileType();
    }

    /**
     * Create an instance of {@link PersonRegistryProfileResponseType }
     * 
     */
    public PersonRegistryProfileResponseType createPersonRegistryProfileResponseType() {
        return new PersonRegistryProfileResponseType();
    }

    /**
     * Create an instance of {@link RegisterPersonRequestType }
     * 
     */
    public RegisterPersonRequestType createRegisterPersonRequestType() {
        return new RegisterPersonRequestType();
    }

    /**
     * Create an instance of {@link AddressType }
     * 
     */
    public AddressType createAddressType() {
        return new AddressType();
    }

    /**
     * Create an instance of {@link PersonType }
     * 
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link PersonRegistryProfileRequestType }
     * 
     */
    public PersonRegistryProfileRequestType createPersonRegistryProfileRequestType() {
        return new PersonRegistryProfileRequestType();
    }

    /**
     * Create an instance of {@link AckType }
     * 
     */
    public AckType createAckType() {
        return new AckType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterPersonRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "RegisterPersonRequest")
    public JAXBElement<RegisterPersonRequestType> createRegisterPersonRequest(RegisterPersonRequestType value) {
        return new JAXBElement<RegisterPersonRequestType>(_RegisterPersonRequest_QNAME, RegisterPersonRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonRegistryProfileRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "PersonRegistryProfileRequest")
    public JAXBElement<PersonRegistryProfileRequestType> createPersonRegistryProfileRequest(PersonRegistryProfileRequestType value) {
        return new JAXBElement<PersonRegistryProfileRequestType>(_PersonRegistryProfileRequest_QNAME, PersonRegistryProfileRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegistryTypeResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "RegistryTypeResponse")
    public JAXBElement<RegistryTypeResponseType> createRegistryTypeResponse(RegistryTypeResponseType value) {
        return new JAXBElement<RegistryTypeResponseType>(_RegistryTypeResponse_QNAME, RegistryTypeResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonRegistryProfileResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "PersonRegistryProfileResponse")
    public JAXBElement<PersonRegistryProfileResponseType> createPersonRegistryProfileResponse(PersonRegistryProfileResponseType value) {
        return new JAXBElement<PersonRegistryProfileResponseType>(_PersonRegistryProfileResponse_QNAME, PersonRegistryProfileResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegisterPersonRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "UnregisterPersonRequest")
    public JAXBElement<RegisterPersonRequestType> createUnregisterPersonRequest(RegisterPersonRequestType value) {
        return new JAXBElement<RegisterPersonRequestType>(_UnregisterPersonRequest_QNAME, RegisterPersonRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AckType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "ResponseAck")
    public JAXBElement<AckType> createResponseAck(AckType value) {
        return new JAXBElement<AckType>(_ResponseAck_QNAME, AckType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:mil:navy:med:dzreg:types", name = "address", scope = PersonType.class)
    public JAXBElement<AddressType> createPersonTypeAddress(AddressType value) {
        return new JAXBElement<AddressType>(_PersonTypeAddress_QNAME, AddressType.class, PersonType.class, value);
    }

}
