/*
 * ****************************************************************************************************************
 *  *
 *  * Copyright (C) 2012 by Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is
 *  * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and limitations under the License.
 *  *
 *  ****************************************************************************************************************
 *
 * ****************************************************************************************************************
 *  * Socratic Grid contains components to which third party terms apply. To comply with these terms, the following
 *  * notice is provided:
 *  *
 *  * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 *  * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 *  * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 *  * the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *  *     following disclaimer.
 *  * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *  *     following disclaimer in the documentation and/or other materials provided with the distribution.
 *  * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or
 *  *     promote products derived from this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 *  * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  * END OF TERMS AND CONDITIONS
 *  *
 *  ****************************************************************************************************************
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.displaymaildata;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.imap.IMAPSSLStore;
import org.socraticgrid.common.dda.DetailData;
import org.socraticgrid.common.dda.GetComponentDetailDataForUserRequestType;
import org.socraticgrid.common.dda.GetComponentDetailDataResponseType;
import org.socraticgrid.common.dda.GetComponentSummaryDataForUserRequestType;
import org.socraticgrid.common.dda.GetComponentSummaryDataResponseType;
import org.socraticgrid.common.dda.GetDataSourceNameRequestType;
import org.socraticgrid.common.dda.GetDataSourceNameResponseType;
import org.socraticgrid.common.dda.GetMessageDetailRequestType;
import org.socraticgrid.common.dda.GetMessageDetailResponseType;
import org.socraticgrid.common.dda.GetMessagesRequestType;
import org.socraticgrid.common.dda.GetMessagesResponseType;
import org.socraticgrid.common.dda.NameValuesPair;
import org.socraticgrid.common.dda.ServiceError;
import org.socraticgrid.common.dda.SetMessageRequestType;
import org.socraticgrid.common.dda.SetMessageResponseType;
import org.socraticgrid.common.dda.SummaryData;
import org.socraticgrid.common.dda.UpdateComponentInboxStatusRequestType;
import org.socraticgrid.common.dda.UpdateComponentInboxStatusResponseType;
import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import org.socraticgrid.util.CommonUtil;
import org.socraticgrid.ldapaccess.ContactAcctDTO;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.search.HeaderTerm;
import javax.mail.search.SearchTerm;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Return a user's mail inbox data.
 *
 * @author cmatser
 */
public class DisplayMailDataHandler {

    String MAIL_HOST = "mail.host";
    String MAIL_PROTOCOL = "mail.protocol";

    /** Mail host. */
    private String host;
    private String protocol;
    private String mailUrl = null;
    /** Logging. */
    private static Log log = LogFactory.getLog(DisplayMailDataHandler.class);
    /**
     * Data source name.
     */
    String DATA_SOURCE = "Mail";
    /**
     * Standard error code.
     */
    Integer ERR_CODE = -1;
    /**
     * Service error message.
     */
    String ERR_MSG_ITEM_NOT_FOUND = "Item was not found.";
    /**
     * Item identifier for patient ids.
     */
    String ITEM_ID_PATIENT = "T";
    /**
     * Item identifier for provider ids.
     */
    String ITEM_ID_PROVIDER = "P";
    /**
     * Item id separater between userId and message number.
     */
    String ITEM_ID_SEPARATER = "?";
    /**
     * Item names for name value pairs.
     */
    String ITEM_READ = "Read";
    String ITEM_REPLIED = "Replied";
    String ITEM_STARRED = "Starred";
    /**
     * LDAP attribute for patient's user id.
     */
    String LDAP_PATIENT_ID_ATT = "uid";
    /**
     * LDAP attribute for provider's user id.
     */
    String LDAP_PROVIDER_ID_ATT = "employeeNumber";



    /**
     * Property constants.
     */
    String PROPERTY_FILE = "displayAggregator";
    //String ZIMBRA_URL = "https://192.168.5.11/";
    
    ContactDAO contactDAO = LdapService.getContactDAO();

    public DisplayMailDataHandler() {

        try {
            host = PropertyAccessor.getProperty(PROPERTY_FILE, MAIL_HOST);
            if (host == null) {
                throw new PropertyAccessException("Property "+ MAIL_HOST + " was null: " + MAIL_HOST);
            }

            protocol = PropertyAccessor.getProperty(PROPERTY_FILE, MAIL_PROTOCOL);
            if (protocol == null) {
                throw new PropertyAccessException("Property "+ MAIL_PROTOCOL +" was null: " + MAIL_PROTOCOL);
            }

//            this.mailUrl =  "https://"+ host +"/";
this.mailUrl = this.host;

System.out.println("\n===> DMD:    host="+ host);
System.out.println(  "===> DMD: mailUrl="+ mailUrl);

        } catch (PropertyAccessException e) {
            log.error("Error accessing properties in file:" + PROPERTY_FILE, e);
        }

    }

    public GetDataSourceNameResponseType getDataSourceName(GetDataSourceNameRequestType getDataSourceNameRequest) {
        GetDataSourceNameResponseType response = new GetDataSourceNameResponseType();
        response.setReturn(DATA_SOURCE);

        return response;
    }

    /**
     * Update Inbox status.
     * 
     * @param updateComponentInboxStatusRequest
     * @return
     *
    public UpdateComponentInboxStatusResponseType updateComponentInboxStatus(UpdateComponentInboxStatusRequestType updateComponentInboxStatusRequest) {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }
     */

    /**
     * Retrieves the message detail.
     *
     * @param request
     * @return
     *
    public GetComponentDetailDataResponseType getComponentDetailDataForUser(GetComponentDetailDataForUserRequestType request) {
        GetComponentDetailDataResponseType response = new GetComponentDetailDataResponseType();
        String userId = null;
        String userType;

        try {
            //Pull out userId
            userId = request.getItemId().substring(1, request.getItemId().indexOf(ITEM_ID_SEPARATER));
            userType = String.valueOf(request.getItemId().charAt(0));
            if ((userId == null) || userId.isEmpty()) {
                throw new Exception("Invalid item ID provided.");
            }

            //Get login info from ldap
            String access[] = retrieveMailAccess(userType, userId);
            if ((access[0] == null) || access[0].isEmpty()) {
                throw new Exception("Contact record not found for user: " + userId);
            }

            //Retrieve message
            DetailData detailData = retrieveMailDetail(access[0], access[1], request.getItemId(), "Email");

            response.setDetailObject(detailData);
        } catch (Throwable t) {
            log.error("Error getting mail summary data.", t);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(t.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }
     */

    /**
     * Retrieves the message summaries for a user's inbox.
     *
     * @param request
     * @return
     *
    public GetComponentSummaryDataResponseType getComponentSummaryDataForUser(GetComponentSummaryDataForUserRequestType request) {
        GetComponentSummaryDataResponseType response = new GetComponentSummaryDataResponseType();
        String userId;
        String userType;
        boolean isProvider = true;

        try {
            //Check for provider id
            userId = request.getProviderId();
            if ((userId == null) || userId.isEmpty()) {
                //Try patient id
                userId = request.getPatientId();
                isProvider = false;
            }

            //Check that we have an id
            if ((userId == null) || userId.isEmpty()) {
                throw new Exception("No provider/patient id provided.");
            }

            //Setup user type
            userType = isProvider ? ITEM_ID_PROVIDER : ITEM_ID_PATIENT;

            //Get login info from ldap
            String access[] = retrieveMailAccess(userType, userId);
            if ((access[0] == null) || access[0].isEmpty()) {
                throw new Exception("Contact record not found for user: " + userId);
            }

            //Retrieve messages
            List<SummaryData> summaryDataList =
                    retrieveMail(userType, userId, request.getPatientId(), access[0], access[1], "", "", request.isOnlyNew());

            response.getSummaryObjects().addAll(summaryDataList);

        } catch (Throwable t) {
            log.error("Error getting mail summary data.", t);

            ServiceError serviceError = new ServiceError();
            serviceError.setCode(ERR_CODE);
            serviceError.setText(t.getMessage());
            response.getErrorList().add(serviceError);
        }

        return response;
    }
     */

    /**
     * Creates the IMAP messages to be sent. Looks up access (email & pwd) from
     * LDAP, sets the values and returns the messages to be sent.
     * 
     * @param session Mail session 
     * @param emailAddr From email address
     * @param request Request from PS
     * @return Array of messages
     * @throws Exception 
     */
    private Message[] createMessage(Session session, String emailAddr,
                                    SetMessageRequestType request)
    throws Exception
    {


/*
//DBG ONLY - Check about CC entry.
List<String> ctlist = request.getContactTo();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> createMessage: TO="+ ctlist.get(0));
}
ctlist = request.getContactCC();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> createMessage: CC="+ ctlist.get(0));
}
ctlist = request.getContactBCC();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> createMessage: BCC="+ ctlist.get(0));
}
*/
        MimeMessage message = new MimeMessage(session);

        if (!CommonUtil.listNullorEmpty(request.getContactTo())) 
        {
            message.setRecipients(Message.RecipientType.TO, 
                                  getInetAddresses(request.getContactTo())); //getInetAddresses(request, "To"));
        }
        
        if (!CommonUtil.listNullorEmpty(request.getContactCC())) 
        {   
            message.setRecipients(Message.RecipientType.CC,
                                  getInetAddresses(request.getContactCC())); //getInetAddresses(request, "CC"));
        }
        
        if (!CommonUtil.listNullorEmpty(request.getContactBCC()))
        {
            message.setRecipients(Message.RecipientType.BCC,
                                  getInetAddresses(request.getContactBCC())); //getInetAddresses(request, "BCC"));
        }

        message.setSubject(request.getSubject());

        // Adding headers doesn't seem to be supported currently in zimbra. 
        // Adding patientId to body instead temporarily 
        // message.addHeader("X-PATIENTID", request.getPatientId());
        StringBuilder sb = new StringBuilder();

        // Only add in PATIENTID first line if there is a patientId in session.
        if (!CommonUtil.strNullorEmpty(request.getPatientId())) {
            sb.append("PATIENTID=").append(request.getPatientId()).append("\n");
        }
        
        if (CommonUtil.strNullorEmpty(request.getBody())) {
            sb.append("");
        } else {
            sb.append(request.getBody());
        }

        message.setContent(sb.toString(), "text/plain");
        message.setFrom(new InternetAddress(emailAddr));
        message.setSentDate(new Date());

        List<String> labelList = request.getLabels();
        if (labelList.size() > 0) {
            String label = labelList.get(0);
            if (label.equalsIgnoreCase("starred")) {
                message.setFlag(Flags.Flag.FLAGGED, true);
            }
        }
        Message[] msgArr = new Message[1];
        msgArr[0] = message;
        return msgArr;
    }

    /**
     * given a list of recipient ldap cn, locate their email address, stored in ldap.
     * @param recipients
     * @return
     * @throws Exception
     */
    private Address[] getInetAddresses(List<String> recipients)
    throws Exception
    {
        List<Address> toAddrList = new ArrayList<Address>();
        List<String> ctcList = new ArrayList<String>(); //List of all contacts' email access info to send to.


        for (String recip : recipients) {

System.out.println("====> getInetAddresses: CN recip="+recip);

            List<ContactDTO> contact = contactDAO.findContact("cn=" + recip);
            if (!CommonUtil.listNullorEmpty(contact)) {
                ContactDTO ctc = contact.get(0);
                ctcList.add(getContactEmailAndPass(ctc).get(0));
            }
        }

        for (String ctc : ctcList) {
System.out.println("====> getInetAddresses: Adding recip="+ctc);
            toAddrList.add(new InternetAddress(ctc));
        }
        return toAddrList.toArray(new InternetAddress[0]);

    }

    /**
     * 
     * @param session
     * @param sslStore
     * @param access
     * @param folderName
     * @return
     * @throws MessagingException
     * @throws NoSuchProviderException
     */
    private Folder getImapFolder(Session session, IMAPSSLStore sslStore,
            String[] access, String folderName) throws MessagingException, NoSuchProviderException {
        Folder folder;
        Provider provider = session.getProvider("imap");
        session.setProvider(provider);
        URLName urlName = new URLName(mailUrl);
        sslStore = new IMAPSSLStore(session, urlName);
        sslStore.connect(host, access[0], access[1]);
        folder = sslStore.getFolder(folderName);
        return folder;
    }

    /**
     * Find LDAP record from userId.
     *
     * @param userId
     * @return
     */
    private String[] retrieveMailAccess(String cnName, String userId)
    {
        String access[] = new String[2];

System.out.println("===> DMD.retrieveMailAccess:   cn="+ cnName);
System.out.println("===> DMD.retrieveMailAccess:  uid="+ userId);

        //---------------------------------------------
        // Find all contacts based on the look up param.
        // usually uid=####  or employeeNumber=####
        //---------------------------------------------
        List<ContactDTO> contacts = this.contactDAO.findContact("uid="+userId);

        //---------------------------------------------
        // Get the uid/pwd under MAIL.Account for the user whose
        // cn=<contacts.get(0).getCommonName()>
        //---------------------------------------------
        if (contacts.size() > 0) {
            //Get mail login info by looking up CN to get account
            List<ContactAcctDTO> accts = this.contactDAO.findContactAcct(
                                                cnName, ContactAcctDTO.CN_MAIL);
            if (accts.size() > 0) {
                access[0] = accts.get(0).getUid();
                access[1] = accts.get(0).getClearPassword();
            }
        }

        return access;
    }

    /**
     *
     * @param email
     * @return
     */
    private String getContactIdFromEmail(String email) {
        if (email == null || email.isEmpty()) {
            return email;
        }
        String notFound = "Not Found";
//        String emailAddr = 
//                email.substring(email.indexOf("<") + 1, email.indexOf(">"));

        List<ContactDTO> contacts = contactDAO.findContact("Mail.Account");
        for (ContactDTO contact : contacts) {
            String cn = contact.getCommonName();
            List<ContactAcctDTO> acctList =
                    contactDAO.findAllContactAccts(cn);
            for (ContactAcctDTO acct : acctList) {
                if (email.contains(acct.getUid())) {
                    return cn;
                }
            }
        }

        //----------------------
        // TODO - when an email sender is not in LDAP, requirement stated that
        //        the email should be added to the ldap.
        // CONCERN: Do we auto add even the CCs entries?
        // CONCERN: What will be the First/Last Name?  What if it's a system address.
        //----------------------
        return notFound;
    }

    private List<String> getContactEmailAndPass(ContactDTO contact) {
        List<String> ret = new ArrayList();
        ContactAcctDTO acct =
                contactDAO.findContactAcct(contact.getCommonName(), "Mail.Account").get(0);
        ret.add(acct.getUid());
        ret.add(acct.getClearPassword());
        return ret;
    }

    private void updateImapSSLMail(SetMessageRequestType request, Folder folder,
                                   IMAPMessage message)
    throws MessagingException
    {
        // folder.open(Folder.READ_WRITE);

        List<String> labelList = request.getLabels();
        if (!labelList.isEmpty() && labelList.get(0).equalsIgnoreCase("starred")) {
            message.setFlag(Flags.Flag.FLAGGED, true);
        }
        else {
            message.setFlag(Flags.Flag.FLAGGED, false);
        }
        if (request.getAction().equalsIgnoreCase("Read")) {
            message.setFlag(Flags.Flag.SEEN, true);
        }
        //      message.saveChanges();
        folder.close(false);
    }

    // Handle Archive and Unarchive actions
    public SetMessageResponseType archiveMessage(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();

        IMAPSSLStore sslStore = null;
        Folder folder = null;
        Folder destinationFolder = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        // INPUT:  request.getUserId()
        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();
        List<ContactDTO> contacts = contactDAO.findAllContacts();
        for (ContactDTO contact : contacts) {
            if (contact.getUid() != null
                    && contact.getUid().equals(request.getUserId())) {
                foundContact = contact;
                break;
            }
        }

        String userType = "";
        String[] access = new String[2];
        String userCName = foundContact.getCommonName();
        if (contacts.isEmpty()) {
            log.error("Contact record not found for user: " + userCName);
            response.setMessage("Contact record not found for user: " + userCName);
            response.setSuccessStatus(false);
            return response;
        }

        access = retrieveMailAccess(userCName, foundContact.getUid()); //TMN

        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        //PROCESSING the action.
        // folder --> the current folder the msg being processed is in.
        // destinationFolder --> the destination folder where the msg will be moved.
        try {

            //----------------------------------
            // Determine/Set destination folder
            //----------------------------------
            if (request.getAction().equals("Archive")) {
                destinationFolder = getImapFolder(session, sslStore, access, "Archives");
            }
            else if (request.getAction().equals("Unarchive")) {
                destinationFolder = getImapFolder(session, sslStore, access, "INBOX");
            }
            destinationFolder.open(Folder.READ_WRITE);

            //----------------------------------
            // Set originating folder
            //----------------------------------
            folder = getImapFolder(session, sslStore, access
                       ,this.mapKmrLocationToImapFolder(request.getLocation(), this.host) );
            folder.open(Folder.READ_WRITE);

            System.out.println("===> DMD.archiveMessage: "+ request.getAction()
                    + "-ing for msgId="+ request.getMessageId()
                    + "\n===> from "+ folder.getName()
                    + " to folder="+ destinationFolder.getName());

            //--------------------------------------------
            // Find the message by the given Message-ID
            //--------------------------------------------
            IMAPMessage imapMessage = (IMAPMessage) findMsgByMessageId(folder, request.getMessageId());

            //--------------------------------------------
            // Process the message
            //--------------------------------------------
            if (imapMessage != null) 
            {
                Message[] messages = new Message[]{imapMessage};
                folder.copyMessages(messages, destinationFolder);
                imapMessage.setFlag(Flags.Flag.DELETED, true);
                folder.expunge();

                System.out.println("===> DMD.archiveMessage: Done "
                                    + request.getAction()
                                    + " for msgId="+ request.getMessageId());

                response.setSuccessStatus(true);

             } else {
                String statMsg = "Msg NOT found for Message-ID="+ request.getMessageId();
                System.out.println("===> "+ statMsg);

                response.setSuccessStatus(false);
                response.setMessage(statMsg);
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error "+request.getAction()+" mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            e.printStackTrace();
            return response;
            
        } finally {
            try {
                if ((folder != null) && folder.isOpen()) folder.close(false);
                if ((destinationFolder != null) && destinationFolder.isOpen()) destinationFolder.close(false);
            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }

        return response;
    }

    /**
     * 
     * @param folder
     * @param messageId
     * @return 
     */
    Message findMsgByMessageId (Folder folder, String messageId) throws MessagingException {

        Message msg = null;

        System.out.println("===> findMsgByMessageId: Looking for Message-Id="+ messageId);

//DEBUG ONLY
//Message[] ms = folder.getMessages();
//FetchProfile fp = new FetchProfile();
//fp.add(IMAPFolder.FetchProfileItem.HEADERS);
//folder.fetch(ms, fp);
//
/////
//  int i = 0;
//  for (Message m : ms) {
//    ++i;
//    IMAPMessage im = (IMAPMessage) m;
//    System.out.println("===> findMsgByMessageId: msg-subject= "
//            + m.getSubject() + "\n\tIMAPMessage id=" + im.getMessageID());
//
//    Enumeration headers = ms[i].getAllHeaders();
//    while (headers.hasMoreElements()) {
//      Header h = (Header) headers.nextElement();
//      if (h.getName().equalsIgnoreCase("Message-Id")) {
//          System.out.println("\t" + h.getName() + ": " + h.getValue());
//      }
//    }
//    System.out.println();
//
//  }

        // Prep and search through all msgs in folder for that id.
        // take the first one found.
        //SearchTerm st = new HeaderTerm("Message-Id:", messageId) ;
        SearchTerm st = new javax.mail.search.MessageIDTerm(messageId);

        Message[] msgs = folder.search(st);

        if ((msgs != null) && (msgs.length > 0))
        {
            msg = msgs[0];
            //this.printMsgIdSubject(msg); //DBG printout
            
        } else {
            System.out.println("===> findMsgByMessageId: Msg NOT FOUND for Message-Id="+ messageId);
        }

        return msg;
    }

    /**
     * MAp from kmr location folder name to Email server's location/folder.
     *
     * KMR location = INBOX ,
     * @param kmrInboxFolderName 
     */
    private String mapKmrLocationToImapFolder (String locationName, String mailServer) {

        String serverFolder = null;

        if (CommonUtil.strNullorEmpty(locationName)) {
            serverFolder = null;
        }
        else if (locationName.equalsIgnoreCase("INBOX")) {
            serverFolder = "INBOX";
        }
        else if (locationName.equalsIgnoreCase("Draft")) {
            if (mailServer.contains("gmail"))
                serverFolder = "[Gmail]/Drafts";
            else
                serverFolder = "Drafts";
        }
        else if (locationName.equalsIgnoreCase("Sent")) {
            if (mailServer.contains("gmail"))
                serverFolder = "[Gmail]/Sent Mail";
            else
                serverFolder = "Sent";
        }
        else if ((locationName.equalsIgnoreCase("Archived")) || (locationName.equalsIgnoreCase("Archive")))
        {
            serverFolder = "Archives";
        }
        else if (locationName.equalsIgnoreCase("UserTrash")) {
            serverFolder = "UserTrash";
        }
        else {
            serverFolder = locationName;
        }
        
        System.out.println("===> mapKmrLocationtoImapFolder FROM location="+ locationName +" --> folder="+ serverFolder);

        return serverFolder;
    }

    /**
     *
     * @param request
     *        where request.action = [ "Send" , "Update" ,"Read" , "Save" ]
     * @return
     */
    private SetMessageResponseType sendImapSSLMail(SetMessageRequestType request) {

        //System.out.println("===> sendImapSSLMail: Action= " + request.getAction());

        SetMessageResponseType response = new SetMessageResponseType();
        IMAPSSLStore sslStore = null;
        Folder folder = null; //the email server folder the msg is CURRENTLY in.

        String userType = "";
        String[] access = new String[2];

        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDTO foundContact = null;
        try {
            foundContact = findContactByUserId(userId);
        } catch (Exception e) {
            log.error("Contact record not found for userid: " + request.getUserId());
            response.setMessage("Contact record not found for userid: " + request.getUserId());
            response.setSuccessStatus(false);
            return response;
        }

        access = retrieveMailAccess(foundContact.getCommonName(), foundContact.getUid()); 


        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        // Use the sslStore to send/change the message
        try {
            //action = Save
            if (request.getAction().equalsIgnoreCase("Save"))
            {
                response = saveDraft(request, access, sslStore,
                                    this.mapKmrLocationToImapFolder("Drafts", this.host), session);
                response.setSuccessStatus(true);
                //return response;
            }
            //action = Send
            else if (request.getAction().equalsIgnoreCase("Send"))
            {
                // create and send msg to recipient(s).
                Message[] msgArr = createMessage(session, access[0], request);
                sendMessagesTOCCBCC(msgArr, request, session);

                // Store a copy to sender's Sent folder
                folder = getImapFolder(session, sslStore, access, 
                                       this.mapKmrLocationToImapFolder("Sent", this.host));
                folder.appendMessages(msgArr);

                response.setSuccessStatus(true);
            }
            //action = ..any other..
            else {
                folder = getImapFolder(session, sslStore, access
                                       ,this.mapKmrLocationToImapFolder(request.getLocation(), this.host));
                folder.open(Folder.READ_WRITE);

                //--------------------------------------------
                // Find the message by the given Message-ID
                //--------------------------------------------
                IMAPMessage imapMessage = (IMAPMessage) findMsgByMessageId(folder, request.getMessageId());

                //--------------------------------------------
                // Process the message
                //--------------------------------------------
                if (imapMessage != null)
                {
System.out.println("===> sendImapSSLMail:Updating:   action="+ request.getAction());
System.out.println("===> sendImapSSLMail:Updating:   folder="+ folder.getName());
System.out.println("===> sendImapSSLMail:Updating:ImapMsgID="+ imapMessage.getMessageID());

                    updateImapSSLMail(request, folder, imapMessage);

                    System.out.println("===> sendImapSSLMail: Done Setting "
                    + request.getAction()
                    + " for msgId="+ request.getMessageId());

                    response.setSuccessStatus(true);

                } else {
                    String statMsg = "Msg NOT found for Message-ID="+ request.getMessageId();
                    System.out.println("===> sendImapSSLMail: "+ statMsg);

                    response.setSuccessStatus(false);
                    response.setMessage(statMsg);
                }

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error sending mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            return response;
        } finally {
            if (folder != null && folder.isOpen()) {
                try {
                    folder.close(false);
                } catch (MessagingException me) {
                    log.error("Error closing folder");
                }
            }
            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (MessagingException me) {
                    log.error("Error closing SSLStore");
                }
            }
        }

        return response;

    }

    private String extractEmailAddressFromSender (String sender) {

        String emailAddr = "";
        
        if (!CommonUtil.strNullorEmpty(sender)) {
            
            //System.out.println("FROM=" + sender);

            //-------------------------------------------------
            // if FROM format ==  "displayName <direct@email>"
            // then extract out the email portion only.
            //-------------------------------------------------
            if (sender.indexOf("<") > 0) {
                //from = fromFull.substring(0, fromFull.indexOf("<") - 1);
                emailAddr = sender.substring(sender.indexOf("<") + 1, sender.indexOf(">"));
            } else {
                emailAddr = sender;
            }
        }
        return emailAddr;
    }

    private List<SummaryData> retrieveMail(String userType, String userId, String patientId,
                                           String login, String pswd, String folderName,
                                           String patientName, boolean onlyNew)
     throws Exception, DatatypeConfigurationException
    {
        return this.retrieveMail(userType, userId, patientId, login, pswd,
                                folderName, patientName, onlyNew,
                                this.host, this.mailUrl);
    }
    /**
     * Retrieve mail for user.
     *
     * @param userType = "P" if logged in user is a provider
     *                   "T" if logged in user is a patient
     * @param userId = unique id of logged in user.
     * @param login = email server user name of logged in user.
     * @param pswd  = password of logged in user.
     * @param folderName = 
     * @param displayName = full name
     * @param onlyNew =
     *
     * @return
     * @throws java.lang.Exception
     */
    //private  //DBG ONLY - REMOVE COMMENT MARKS
    public List<SummaryData> retrieveMail(String userType, String userId, String patientId,
                                           String login, String pswd, String folderName,
                                           String patientName, boolean onlyNew,
                                           String mailHost, String mailUrl)
    throws Exception, DatatypeConfigurationException
    {
        List<SummaryData> dataList = new LinkedList<SummaryData>();
        IMAPSSLStore sslStore = null;
        IMAPFolder currentFolder = null;
        String folderToOpen = folderName;

System.out.println("===> retrieveMail Incoming params:");
System.out.println("===>     mailHost="+ mailHost);
System.out.println("===>      mailUrl="+ mailUrl);
System.out.println("===>    maillogin="+ login);
System.out.println("===>   folderName="+ folderName);
        try {
            //Get session
            Session session = Session.getInstance(new Properties());
            URLName urlName = new URLName(mailUrl);
            //Get the sslStore
            sslStore = new IMAPSSLStore(session, urlName);
            sslStore.connect(mailHost, login, pswd);

            folderToOpen = this.mapKmrLocationToImapFolder(folderName, this.host);

            currentFolder = (IMAPFolder) sslStore.getFolder(folderToOpen);
            currentFolder.open(Folder.READ_ONLY);
            
            Message[] allMessages = currentFolder.getMessages();
            
            GregorianCalendar cal = new GregorianCalendar();

            System.out.println("====> FILTER PARAMS for Emails:");
            System.out.println("====>     folder = "+ folderToOpen);
            System.out.println("====>     User   = "+ login);
//            System.out.println("====>     from   = "+ patientName +"/"+ patientEmail);
//            System.out.println("====>     ptid   = "+ patientId);
            System.out.println("====> Total Emails found = "+ allMessages.length);
            System.out.println();

// TMN - CHECK SLOW PERFORMANCE ISSUE HERE:

            //Loop through each email and find ONLY the ones required for return.
            for (Message msg : allMessages) {
                if (msg == null) {
                    continue;
                }

// Keep this in case we want to search entire message
//
//                OutputStream os = new ByteArrayOutputStream();
//                msg.writeTo(os);
//                String msgContent = os.toString();

                SummaryData summaryData = new SummaryData();
                summaryData.setDataSource(DATA_SOURCE);

                String from = "";
                Address[] fromAddr = msg.getFrom();
                
                if (fromAddr != null && fromAddr.length > 0) {

                    String fromFull = fromAddr[0].toString();
                    from = getContactIdFromEmail(extractEmailAddressFromSender(fromFull));

                    //System.out.println("retrieveMail: FROM=" + fromFull + " ldap.cn=" + from);
          
                }
                

                //------------------------------------------------------
                //FILTERING: Check to exclude email if
                //     0) patientId is passed in as a param
                // AND 1) email does NOT contain PATIENTID=<patientId>
                // AND 2) email FROM field <> patientName
                // AND 3) email FROM field <> patientEmail.
                //
                // because must becoming from EMR inbox and looking for emails
                // addressed to userId BUT only ABOUT or FROM patientId.
                //------------------------------------------------------

                summaryData.setFrom(from);
                summaryData.setAuthor(summaryData.getFrom());
                cal.setTime(msg.getReceivedDate());
                summaryData.setDateCreated(
                        DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));

                summaryData.setDescription(msg.getSubject());

                summaryData.setItemId(
                        userType + userId + ITEM_ID_SEPARATER
                        + msg.getFolder().getName() + ITEM_ID_SEPARATER
                        + msg.getHeader("Message-ID")[0]);

                //this.printMsgIdSubject(msg); //DBG printout

                boolean msgRead = msg.isSet(Flags.Flag.SEEN);
                addNameValue(summaryData.getItemValues(), ITEM_READ,
                        String.valueOf(msgRead));

                boolean msgStar = msg.isSet(Flags.Flag.FLAGGED);
                if (msgStar) {
                    addNameValue(summaryData.getItemValues(), ITEM_STARRED,
                            "Starred");
                }

                addNameValue(summaryData.getItemValues(),
                        ITEM_REPLIED, String.valueOf(msg.isSet(Flags.Flag.ANSWERED)));
                addNameValue(summaryData.getItemValues(),
                        "MESSAGE_TYPE", msg.getFolder().getName());
                if (onlyNew) {
                    if (!msg.isSet(Flags.Flag.SEEN)) {
                        dataList.add(summaryData);
                    }
                }
                else {
                    dataList.add(summaryData);
                }
            }

        } catch (MessagingException me) {
            log.error("Error in processing email");
            me.printStackTrace();
        } finally {
            // Close connections

            if (currentFolder != null) {
                try {
                    currentFolder.close(false);
                } catch (Exception e) {
                }
            }

            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (Exception e) {
                }
            }
        }

        return dataList;
    }

    /**
     * Get an array of all messages from an email folder.
     * 
     * @param folderName
     * @param sslStore
     * @return
     * @throws MessagingException
     */
    private Message[] getFolderMessages(String folderName, IMAPSSLStore sslStore) throws MessagingException {

        IMAPFolder folder = (IMAPFolder) sslStore.getFolder(folderName);
        folder.open(Folder.READ_ONLY);
        Message[] messageArr = folder.getMessages();

        return messageArr;
    }

    /**
     *
     * @param folders
     * @param sslStore
     * @return
     * @throws MessagingException
     */
    private Message[] getAllFolderMessages(IMAPFolder[] folders, IMAPSSLStore sslStore) throws MessagingException {
        folders[0] = (IMAPFolder) sslStore.getFolder(this.mapKmrLocationToImapFolder("INBOX", this.host));
        folders[0].open(Folder.READ_ONLY);
        Message[] messageArr = folders[0].getMessages();

        folders[0] = (IMAPFolder) sslStore.getFolder(this.mapKmrLocationToImapFolder("archive", this.host));
        folders[1].open(Folder.READ_ONLY);
        Message[] archMsgArr = folders[1].getMessages();

        folders[0] = (IMAPFolder) sslStore.getFolder(this.mapKmrLocationToImapFolder("Sent", this.host));
        folders[2].open(Folder.READ_ONLY);
        Message[] sentMsgArr = folders[2].getMessages();

        folders[0] = (IMAPFolder) sslStore.getFolder(this.mapKmrLocationToImapFolder("Drafts", this.host));
        folders[3].open(Folder.READ_ONLY);
        Message[] draftMsgArr = folders[3].getMessages();

        Message[] allMessages =
                new Message[messageArr.length + archMsgArr.length
                + sentMsgArr.length + draftMsgArr.length];

        if (messageArr.length > 0) {
            System.arraycopy(messageArr, 0, allMessages, 0, messageArr.length);
        }
        if (archMsgArr.length > 0) {
            System.arraycopy(archMsgArr, 0, allMessages, messageArr.length, archMsgArr.length);
        }
        if (sentMsgArr.length > 0) {
            System.arraycopy(sentMsgArr, 0, allMessages, messageArr.length + archMsgArr.length,
                    sentMsgArr.length);
        }
        if (draftMsgArr.length > 0) {
            System.arraycopy(draftMsgArr, 0, allMessages, messageArr.length + archMsgArr.length
                    + sentMsgArr.length, draftMsgArr.length);
        }

        return allMessages;
    }


    /**
     * Extract folder and message number from item id.
     * 
     * @param itemId
     * @return
     */
    private String[] parseMsgId(String itemId) {
        String msgId[] = new String[3];
        StringTokenizer st = new StringTokenizer(itemId, ITEM_ID_SEPARATER);

        msgId[0] = st.nextToken();//userId
        msgId[1] = st.nextToken();//folder
        msgId[2] = st.nextToken();//msg num

        return msgId;
    }

    /**
     * Extract/format mail message content.
     * 
     * @param msg
     * @return
     */
    private String fetchMsgContent(Message msg) throws Exception {
        
        String content = null;
        String html = null;
        String text = null;

        if (msg.isMimeType("multipart/*")) {

            Multipart mp = (Multipart)msg.getContent();
            // the content was not fetched from the server

            // parse each Part
            for (int i = 0; i < mp.getCount(); i++) {
                Part inner_part = mp.getBodyPart(i);

                if ( inner_part.isMimeType("text/plain") ) {
                    text = (String) inner_part.getContent();
                    System.out.println("TEXT=\n"+text);

                } else if (inner_part.isMimeType("text/html")) {
                    html = (String) inner_part.getContent();
                    System.out.println("HTML=\n"+content);

                }
            }
        } else if (msg.isMimeType("text/plain")) {
            text = (String) msg.getContent();
            System.out.println("TEXT only=\n"+text);
        }
        if (!CommonUtil.strNullorEmpty(html)) {
            content = html;
        } else {
            content = text;
        }
        return content;
    }
    
    private boolean isEmailAboutPatientId(String ptId, Message msg) throws Exception {

        String patientIdFound = null;

        String content = fetchMsgContent(msg);
        
//System.out.println("msgBody=\n" + content);

        if (content.startsWith("PATIENTID=")) {

            // TEMPORARILY extract patientId from body if present
            Scanner scanner = new Scanner(content);
            boolean first = true;
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                if (first) {
                    String[] parts = scanner.nextLine().split("=");
                    patientIdFound = parts[1];
                    first = false;
                }
                else {
                    sb.append(scanner.nextLine());
                }
            }
        }
System.out.println("ptId="+ ptId +" vs patientIdFound="+ patientIdFound);

        if (ptId.equalsIgnoreCase(patientIdFound)) {
            return true;
        } else {
            return false;
        }
    }

    private void addNameValue(List<NameValuesPair> pairList, String name, String value) {
        NameValuesPair nameVal = new NameValuesPair();
        nameVal.setName(name);
        nameVal.getValues().add(value);
        pairList.add(nameVal);

        return;
    }

    public SetMessageResponseType setMessage(SetMessageRequestType request) {

        SetMessageResponseType response = sendImapSSLMail(request);
        return response;
    }

    /**
     * IN USE
     * 
     * If request.getPatientId() is given, this means request originated from the EMR so
     * should get
     *     1) all email msgs sent TO the request.userId FROM request.getPatientId().
     * AND 2) all email msgs sent TO the request.userID ABOUT request.getPatientId().
     *
     * NOTE:  The ABOUT info will be embedded within the body (first line) of the email msg as the following line:
     *            PATIENTID=12346
     *
     * @param request
     * @return  GetMessagesResponseType
     */
    public GetMessagesResponseType getMessages(GetMessagesRequestType request)
    {
        GetMessagesResponseType toReturn = new GetMessagesResponseType();

        List<GetMessagesResponseType.GetMessageResponse> getResponse =
                toReturn.getGetMessageResponse();

        //--------------------------------------------------------------------------------------
        // Get from LDAP, the email address/pwd of the request.userId's email box.
        // Also get the email address/pwd of the patient being referenced, if at all.
        //
        //    contact   ==> the user email box to pull from
        //    ptContact ==> email address of the patient (FROM whom the email may be coming from).
        //--------------------------------------------------------------------------------------

        String patientName = "";
        String patientEmail = "";
        
        String userType = "";
        String[] access = new String[2];

        //ContactDAO contactDAO = LdapService.getContactDAO();

        //------------------------------------------------------------------
        // When patientId is given AND logged in user is a Provider,
        // access must be from EMR Inbox.
        //      Get email msgs from Patient's email account.
        // Else
        //      Get email msgs from logged in user's email account.
        //------------------------------------------------------------------
        ContactDTO contact = null;

        if (request.getPatientId() != null) {
            contact = this.contactDAO.findContact("uid=" + request.getPatientId()).get(0);
            patientName = contact.getDisplayName();
            userType = this.ITEM_ID_PATIENT;

        } else {
            contact = this.contactDAO.findContact("uid=" + request.getUserId()).get(0);
            userType = this.ITEM_ID_PROVIDER;
        }
        access = retrieveMailAccess(contact.getCommonName(), contact.getUid());

        
        try {
            List<SummaryData> allMail = new ArrayList<SummaryData>();

            //-----------------------------------------------------------------
            // Call retrieveMail to get ONLY valid msgs, per the request filter params given.
            // When ptContact exists, then a provider is requesting emails ABOUT a patient, or FROM a patient.
            // else, a provider/patient is requesting emails sent TO themselves.
            //-----------------------------------------------------------------
            allMail = retrieveMail(userType, request.getUserId(), request.getPatientId(),
                                   access[0], access[1], request.getLocation(),
                                   patientName, false);


            // Prepare the response object to contain the valid email msgs.
            for (SummaryData summary : allMail) {
                GetMessagesResponseType.GetMessageResponse resp =
                        new GetMessagesResponseType.GetMessageResponse();
                resp.setMessageType("Email");
                String[] msgIds = parseMsgId(summary.getItemId());

//System.out.println("\n===> getMessages.Sending msgid= "+msgIds[2]);
//String encodedString = URLEncoder.encode(msgIds[2], "UTF-8");
//System.out.println("===>             Encoded msgid= "+encodedString);

                //resp.setMessageId(encodedString);
                resp.setMessageId(msgIds[2]);


                //              resp.getLabels().add(summary.getFolder());
                resp.setTitle(summary.getDescription());
                resp.setDescription(summary.getDescription());
                resp.setFrom(summary.getFrom());
                resp.setSuccessStatus(true);
                resp.setMessageDate(summary.getDateCreated());

                List<NameValuesPair> itemValues = summary.getItemValues();
                NameValuesPair nvp = itemValues.get(0);
                List<String> values = nvp.getValues();
                String isRead = values.get(0);
                if (isRead.equals("true")) {
                    resp.setMessageStatus("Read");
                }
                else {
                    resp.setMessageStatus("Unread");
                }

                NameValuesPair nvp1 = itemValues.get(1);
                if (nvp1.getValues().get(0).equalsIgnoreCase("starred")) {
                    resp.getLabels().add("Starred");
                }

                NameValuesPair nvp2 = itemValues.get(2);
                values = nvp2.getValues();
                String msgType = values.get(0);
                if (msgType.equals("INBOX")) {
                    resp.setLocation("Inbox");
                    resp.setMessageType("Email");
                }
                if (msgType.equalsIgnoreCase("archive")) {
                    resp.setLocation("Archive");
                }
                if (msgType.equalsIgnoreCase("Sent")) {
                    resp.setMessageType("Email");
                    resp.setLocation("Sent");
                }
                if (msgType.contains("Draft")) {
                    resp.setMessageType("Email");
                    resp.setLocation("Draft");
                }
                toReturn.getGetMessageResponse().add(resp);
            }

        } catch (AuthenticationFailedException afe) {
            GetMessagesResponseType.GetMessageResponse resp =
                    new GetMessagesResponseType.GetMessageResponse();
            resp.setStatusMessage("Email authentication failed for userId: " + request.getUserId()
                    + " email: " + access[0]);
            resp.setSuccessStatus(false);
            toReturn.getGetMessageResponse().add(resp);
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    /**
     * IN USE
     * 
     * @param request
     * @return
     * @throws Exception
     */
    public GetMessageDetailResponseType getMessageDetail(GetMessageDetailRequestType request)
    throws Exception
    {
        System.out.println("===> DMD.getMessageDetail: Looking for msgId="+request.getMessageId());
        System.out.println("===> DMD.getMessageDetail: request patientId="+request.getPatientId());
        System.out.println("===> DMD.getMessageDetail: request    userId="+request.getUserId());


        GetMessageDetailResponseType response = new GetMessageDetailResponseType();

        IMAPFolder msgFolder = null;
        IMAPSSLStore sslStore = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);


        //------------------------------------------------------------------
        // When patientId is given, access must be from EMR Inbox.
        //      Get email msgs from Patient's email account.
        // Else
        //      Get email msgs from logged in user's email account.
        //------------------------------------------------------------------
        ContactDTO contact = null;
        String userType = "";
        String[] access = new String[2];

        if (!CommonUtil.strNullorEmpty(request.getPatientId())) {
            contact = contactDAO.findContact("uid=" + request.getPatientId()).get(0);
            userType = ITEM_ID_PATIENT;

        } else {
            contact = contactDAO.findContact("uid=" + request.getUserId()).get(0);
            userType = ITEM_ID_PROVIDER;
        }
        access = retrieveMailAccess(contact.getCommonName(), contact.getUid());

        try {
            session = Session.getInstance(new Properties());
            URLName urlName = new URLName(mailUrl);

            //--------------------------------------------
            //Get the sslStore and connect
            //--------------------------------------------
            sslStore = new IMAPSSLStore(session, urlName);
            sslStore.connect(host, access[0], access[1]);

            //--------------------------------------------
            // Set the originating folder.
            // Default to INBOX if not given.
            // Get and open the IMAP folder
            //--------------------------------------------
            String folderName = null;
            if (CommonUtil.strNullorEmpty(request.getLocation()))
            {
                folderName = "INBOX";
            } else {
                folderName = mapKmrLocationToImapFolder(request.getLocation(), this.host);
            }


            msgFolder = (IMAPFolder) sslStore.getFolder(folderName);
            msgFolder.open(Folder.READ_ONLY);

            //--------------------------------------------
            // Find the message by the given Message-ID
            //--------------------------------------------
            Message msg = this.findMsgByMessageId(msgFolder, request.getMessageId());

            if (msg == null) {
                String errmsg = "Msg NOT FOUND for Message-ID="+ request.getMessageId();
                System.out.println("===> getMessageDetail: "+ errmsg);

                response.setSuccessStatus(false);
                response.setStatusMessage(errmsg);

            } else {
                //this.printMsgIdSubject(msg); //DBG printout
                System.out.println("===> getMessageDetail: Msg FOUND for Message-ID="+ request.getMessageId());

                //---------------------------------------------------
                // Extract "PATIENTID=" from body if present, so that
                // user does not see it.
                //---------------------------------------------------
                String content = fetchMsgContent(msg);

                if (content.startsWith("PATIENTID="))
                {
                    Scanner scanner = new Scanner(content);
                    boolean first = true;
                    StringBuilder sb = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        if (first) {
                            String[] parts = scanner.nextLine().split("=");
                            response.setPatientId(parts[1]);
                            first = false;
                        }
                        else {
                            sb.append(scanner.nextLine());
                        }

                    }
                    response.getMessageDetail().add(sb.toString());
                }
                else {
                    response.getMessageDetail().add(content);
                }


    // Adding patientId coming from the message header.
    //            if (msg.getHeader("X-PATIENTID") != null &&
    //                msg.getHeader("X-PATIENTID").length > 0) {
    //
    //                response.setPatientId(msg.getHeader("X-PATIENT_ID")[0]);
    //            }

                if (msg.getRecipients(Message.RecipientType.TO) != null) {
                    for (Address a : msg.getRecipients(Message.RecipientType.TO)) {
                        String contactId = getContactIdFromEmail(a.toString());
                        response.getSentTo().add(contactId);

                        //System.out.println("DisplayMailDataHandler: TO="+ a.toString() +" ldap.cn="+ contactId);
                    }
                }


                if (msg.getRecipients(Message.RecipientType.CC) != null) {
                    for (Address a : msg.getRecipients(Message.RecipientType.CC)) {
                        String contactId = getContactIdFromEmail(a.toString());
                        response.getCCTo().add(contactId);

                        //System.out.println("DisplayMailDataHandler: CC="+ a.toString() +" ldap.cn="+ contactId);
                    }
                }

                if (msg.getRecipients(Message.RecipientType.BCC) != null) {
                    for (Address a : msg.getRecipients(Message.RecipientType.BCC)) {
                        String contactId = getContactIdFromEmail(a.toString());
                        response.getBCCTo().add(contactId);

                        //System.out.println("DisplayMailDataHandler: BCC="+ a.toString() +" ldap.cn="+ contactId);
                    }
                }
                response.setSuccessStatus(true);
                response.setStatusMessage("");
            }

        } catch (Exception e) {
            response.setSuccessStatus(false);
            response.setStatusMessage("Error getting message detail for user: " + access[0]
                        + "\n[EXCEPTION] "+ e.toString());
            e.printStackTrace();
            
        } finally {
            if (msgFolder != null) {
                try {
                    msgFolder.close(false);
                } catch (Exception e) {
                }
            }

            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (Exception e) {
                }
            }
        }
        
        return response;
    }

    private Properties initializeMailProperties() {
        Properties prop = new Properties();
        prop.setProperty("mail.imap.starttls.enable", "false");
        prop.setProperty("mail.imap.ssl.snable", "true");
        // Use SSL
        prop.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.setProperty("mail.imap.socketFactory.fallback", "false");
        // Use port 993
        prop.setProperty("mail.imap.port", "993");
        prop.setProperty("mail.imap.socketFactory.port", "993");
        prop.setProperty("mail.imaps.class", "com.sun.mail.imap.IMAPSSLStore");
        prop.setProperty("mail.transport.protocol", "imap");
        prop.setProperty("mail.imap.host", host);
        return prop;
    }

    public SetMessageResponseType deleteMessage(SetMessageRequestType request) {
        SetMessageResponseType response = new SetMessageResponseType();
        IMAPSSLStore sslStore = null;
        Folder sourceFolder = null;
        Folder targetFolder = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);

        //Get email address and password from LDAP
        String userId = request.getUserId();
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();
        List<ContactDTO> contacts = contactDAO.findAllContacts();
        for (ContactDTO contact : contacts) {
            if (contact.getUid() != null
                    && contact.getUid().equals(request.getUserId())) {
                foundContact = contact;
                break;
            }
        }

        String userType = "";
        String[] access = new String[2];
        String userCName = foundContact.getCommonName();
        if (contacts.isEmpty()) {
            log.error("Contact record not found for user: " + userCName);
            response.setMessage("Contact record not found for user: " + userCName);
            response.setSuccessStatus(false);
            return response;
        }

        access = retrieveMailAccess(userCName, foundContact.getUid()); //TMN
//        if (foundContact.getEmployeeNumber() != null) {
//            userType = ITEM_ID_PROVIDER;
//            access = retrieveMailAccess(userType, foundContact.getEmployeeNumber());
//        }
//        else {
//            userType = ITEM_ID_PATIENT;
//            access = retrieveMailAccess(userType, foundContact.getUid());
//        }

        if ((access[0] == null) || access[0].isEmpty()) {
            log.error("Contact record not found for user: " + userId);
            response.setMessage("Contact record not found for user: " + userId);
            response.setSuccessStatus(false);
            return response;
        }

        try {

            //--------------------------
            // Set originating folder
            //--------------------------
            sourceFolder = getImapFolder(session, sslStore, access
                                        ,this.mapKmrLocationToImapFolder(request.getLocation(), this.host));

            // Is this really needed if request comes in with location=UserTrash already?
            if (request.getAction().equals("Undelete")) {
                sourceFolder = getImapFolder(session, sslStore, access, "UserTrash");
            }
            sourceFolder.open(Folder.READ_WRITE);

            //--------------------------
            // Set destination folder
            //--------------------------
            if (request.getAction().equals("Delete")) {
                targetFolder = getImapFolder(session, sslStore, access, "UserTrash");
            }
            if (request.getAction().equals("DeleteForever")) {
                targetFolder = getImapFolder(session, sslStore, access, "AdminTrash");
            }
            else if (request.getAction().equals("Undelete")) {
                targetFolder = getImapFolder(session, sslStore, access, "INBOX");
            }
            targetFolder.open(Folder.READ_WRITE);

            

            //--------------------------------------------
            // Find the message by the given Message-ID
            //-------------------------------------------
            Message msg = this.findMsgByMessageId(sourceFolder, request.getMessageId());

            if (msg == null) {
                String errmsg = "Msg NOT found for Message-ID="+ request.getMessageId();
                System.out.println("===> deleteMessage: "+ errmsg);

                response.setSuccessStatus(false);
                response.setMessage(errmsg);

            } else {
                
                //this.printMsgIdSubject(msg); //DBG printout

                //----------------------
                //copy to new folder
                //----------------------
                Message[] messages = new Message[]{msg};
                sourceFolder.copyMessages(messages, targetFolder);

                //----------------------
                //remove from old folder
                //----------------------
                msg.setFlag(Flags.Flag.DELETED, true);
                sourceFolder.expunge();

                response.setSuccessStatus(true);
            }
               


        } catch (Exception e) {
            log.error(e.getMessage());
            response.setMessage("Error archiving mail with Zimbra mail server: " + e.getMessage());
            response.setSuccessStatus(false);
            e.printStackTrace();
            return response;
        } finally {
            try {
                sourceFolder.close(false);
                targetFolder.close(false);
            } catch (MessagingException me) {
                me.printStackTrace();
            }
        }

        return response;
    }

    /**
     * Find an Ldap Contact object that matches the given userId.
     * @param userId
     * @return
     * @throws Exception
     */
    private ContactDTO findContactByUserId(String userId) throws Exception {


        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();

        List<ContactDTO> contacts = contactDAO.findContact("uid=" + userId);
        if (contacts.isEmpty()) {
            throw new Exception("No contacts found");
        }
        foundContact = contacts.get(0);

        return foundContact;
    }

    /**
     * Find an Ldap Contact object that matches the given cname.
     * @param cname
     * @return
     * @throws Exception
     */
    private ContactDTO findContactByCn(String cname) throws Exception {
        ContactDAO contactDAO = LdapService.getContactDAO();
        ContactDTO foundContact = new ContactDTO();

        List<ContactDTO> contacts = contactDAO.findContact("cn=" + cname);
        if (contacts.isEmpty()) {
            throw new Exception("No contacts found");
        }
        foundContact = contacts.get(0);

        return foundContact;
    }


    private SetMessageResponseType saveDraft(SetMessageRequestType request, String[] access,
            IMAPSSLStore sslStore, String destinationFolder, Session session)
            throws MessagingException
    {
        SetMessageResponseType response = new SetMessageResponseType();
        Folder folder = getImapFolder(session, sslStore, access, destinationFolder);
        folder.open(Folder.READ_WRITE);
        IMAPMessage imapMessage = null;
        Message[] msgArr = null;
        try {
            msgArr = createMessage(session, access[0], request);
            folder.appendMessages(msgArr);
        } catch (Exception e) {
            log.error("Error creating draft message");
            response.setSuccessStatus(false);
            response.setMessage("Error saving draft message for user = "
                                 + request.getUserId());
            e.printStackTrace();
            return response;
        }
        response.setSuccessStatus(true);
        response.setMessage(" ");
        return response;
    }

    /**
     * This methog 
     * @param message
     * @param request
     * @param session
     * @throws Exception 
     */
    private void sendMessagesTOCCBCC(Message[] message, SetMessageRequestType request,
            Session session) throws Exception
    {
        IMAPFolder folder = null;
        IMAPSSLStore sslStore = null;
        Set<String> allContacts = new HashSet<String>();

//DBG - Check about CC entry.
List<String> ctlist = request.getContactTo();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> TO="+ ctlist.get(0));
}
ctlist = request.getContactCC();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> CC="+ ctlist.get(0));
}
ctlist = request.getContactBCC();
if (!CommonUtil.listNullorEmpty(ctlist)) {
    System.out.println("===> BCC="+ ctlist.get(0));
}

        if (!CommonUtil.listNullorEmpty(request.getContactTo())) {
            allContacts.addAll(request.getContactTo());
        }

        if (!CommonUtil.listNullorEmpty(request.getContactCC())) {
            allContacts.addAll(request.getContactCC());
        }

        if (!CommonUtil.listNullorEmpty(request.getContactBCC())) {
            allContacts.addAll(request.getContactBCC());
        }

        try {

            for (String ctcName : allContacts) {

                System.out.println("===> Looking for Contact of given CN="+ctcName);

                if (CommonUtil.strNullorEmpty(ctcName)) {
                    continue;
                }

                ContactDTO dto = findContactByCn(ctcName);
                List<String> acctPass = getContactEmailAndPass(dto);
                String[] access = acctPass.toArray(new String[0]);

                System.out.println("===> Sending to Contact="+ access[0] + " mapped from given CN="+ctcName);

                URLName urlName = new URLName(mailUrl);
                sslStore = new IMAPSSLStore(session, urlName);
                sslStore.connect(host, access[0], access[1]);

                folder = (IMAPFolder) sslStore.getFolder(this.mapKmrLocationToImapFolder("INBOX", this.host));
                folder.open(Folder.READ_WRITE);
                folder.appendMessages(message);
                folder.close(false);
                sslStore.close();
            }

        } catch (Exception e) {
            log.error("Error sending TO, CC and BCC emails: " + e.getMessage());
        } finally {
            if (folder != null && folder.isOpen()) {
                try {
                    folder.close(false);
                } catch (MessagingException me) {
                    log.error("Error closing folder");
                }
            }
            if (sslStore != null) {
                try {
                    sslStore.close();
                } catch (MessagingException me) {
                    log.error("Error closing SSLStore");
                }
            }
        }
    }

    /**
     * 
     * @param msg
     * @throws MessagingException
     */
    void printMsgIdSubject(Message msg) throws MessagingException
    {
        if ((msg.getHeader("Subject") != null) && (msg.getHeader("Subject").length > 0))

            System.out.println("===> Msg Found: Message-ID="
                            + msg.getHeader("Message-ID")[0]
                            +" Subject="+ msg.getHeader("Subject")[0]);
        else
            System.out.println("===> Msg Found: Message-ID="
                                + msg.getHeader("Message-ID")[0]);
    }
    /**
     * Prints all header attributes in a Msg
     * @param msgs
     */
    void printEmailMsg(Message msg) throws MessagingException
    {
        Enumeration headers = msg.getAllHeaders();
        while (headers.hasMoreElements()) {
            Header h = (Header) headers.nextElement();
            System.out.println(h.getName() + ": " + h.getValue());
        }
    }

    /**
     * Prints all msgs in array.
     * @param msgs
     */
    void printAllMsgs(Message[] msgs) throws MessagingException
    {
        System.out.println("Total Email Msgs to print: " + msgs.length);

        for (int i = 0; i < msgs.length; i++) {
            System.out.println("------------ Message " + (i + 1) + " ------------");
            this.printEmailMsg(msgs[i]);
            System.out.println();
        }
    }

    /**
     * Opens a folder and print all msgs found.
     * @param folder
     * @param userId
     * @param pwd
     */
    void printAllMsgs(String folderName, String userId, String pwd) throws MessagingException
    {
        IMAPSSLStore sslStore = null;
        Properties prop = initializeMailProperties();
        Session session = Session.getDefaultInstance(prop);
        String[] access = new String[2];
        access[0] = userId;
        access[1] = pwd;

        Folder folder = getImapFolder(session, sslStore, access, folderName);
        folder.open(Folder.READ_WRITE);

        try {
            this.printAllMsgs(folder.getMessages());

        } catch (MessagingException ex) {
            System.out.println("ERROR at printAllEmailMsgs.");
        }
    }

}
