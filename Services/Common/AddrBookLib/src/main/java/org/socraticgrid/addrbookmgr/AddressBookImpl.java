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
package org.socraticgrid.addrbookmgr;

import org.socraticgrid.addrbook.model.AddressItem;
import org.socraticgrid.addrbook.model.AddressQueryParams;
import org.socraticgrid.addrbook.service.AddrBookService;
import org.socraticgrid.common.addrbook.ContactDetails;
import org.socraticgrid.common.addrbook.ContactSummary;
import org.socraticgrid.common.addrbook.GetContactDetailsRequestType;
import org.socraticgrid.common.addrbook.GetContactDetailsResponseType;
import org.socraticgrid.common.addrbook.GetSummariesResponseType;
import org.socraticgrid.common.addrbook.SearchAddrRequestType;
import org.socraticgrid.common.addrbook.ServiceError;
import org.socraticgrid.common.dda.GetAddressBookResponseType;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class AddressBookImpl {

    /** List of address agents. */
    private List<AddrBookAgent> agents = null;
    /** General error. */
    private static int ERROR_ADDRBOOK = -1;
    /** Delimiter used in address id for detail retrieval. */
    public static final String ADDR_ID_DELIM = "/";
    /** Logger. */
    private static Log log = LogFactory.getLog(AddressBookImpl.class);

    public AddressBookImpl() {

        //Init agent list
        if (agents == null) {
            agents = new LinkedList<AddrBookAgent>();
            agents.add(new AllPatientAgent());
            agents.add(new AllProviderAgent());
            agents.add(new OptInProviderAgent());
            agents.add(new AppointmentAgent());
            agents.add(new PrimaryCareAgent());
        }

    }

    /**
     * Return all addresses for this user.
     * 
     * @param request
     * @return
     */
    public org.socraticgrid.common.addrbook.GetSummariesResponseType getAllAddr(org.socraticgrid.common.addrbook.GetAllAddrRequestType request) {

        log.debug("Starting address agents for getAll query.");

        //Forward to search query (search for all)
        SearchAddrRequestType searchRequest = new SearchAddrRequestType();
        searchRequest.setUserId(request.getUserId());
        searchRequest.setSearch(null);

        return searchAddr(searchRequest);
    }

    /**
     * Return the addresses that match the query.
     * 
     * @param request
     * @return
     */
    public org.socraticgrid.common.addrbook.GetSummariesResponseType searchAddr(org.socraticgrid.common.addrbook.SearchAddrRequestType request) {
        GetSummariesResponseType response = new GetSummariesResponseType();

        try {

            System.out.println("searchAddr(): Query="+ request.getUserId());

            AddrBookService dbService = new AddrBookService();
            AddressQueryParams query = new AddressQueryParams();
            query.setUserId(request.getUserId());
            List<AddressItem> addrs = dbService.addressQuery(query);

            log.debug("Starting address agents for query.");

            //Loop through all agents and ask them to return the contact ids
            List<Thread> threads = new LinkedList<Thread>();
            for (AddrBookAgent agent : agents) {
                agent.setAddressStart(addrs);
                Thread thread = new Thread(agent);
                thread.start();
                threads.add(thread);
            }

            //Wait for all threads to finish
            for (Thread thread : threads) {
                while (thread.isAlive()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                    }
                }
            }

            log.debug("Processing agent retrieved contact info for query: " + request.getSearch());

            //Loop through get contacts
            ContactDAO contactDAO = LdapService.getContactDAO();
            List<String> lookups = new LinkedList<String>();
            for (AddrBookAgent agent : agents) {
                for (AddressItem addr : agent.getAddressFinish()) {

                    //First check if the contact matches our search query
                    if ((request.getSearch() != null)
                            && !request.getSearch().isEmpty()
                            && !addr.getName().matches(request.getSearch())) {
                        continue;
                    }

                    //Check if we've already done this lookup
                    if (lookups.contains(addr.getContactId())) {
                        continue;
                    }
                    lookups.add(addr.getContactId());

                    log.debug("Looking up ldap contact: " + addr.getContactId());

                    List<ContactDTO> contacts = contactDAO.findContact(addr.getContactId());
                    for (ContactDTO contact : contacts) {
                        ContactSummary summary = new ContactSummary();
                        summary.setAddressId(addr.getAddressId().toString()
                                + ADDR_ID_DELIM + addr.getName()
                                + ADDR_ID_DELIM + "cn=" + contact.getCommonName());
                        summary.setName(addr.getName());
                        summary.setLocation(contact.getLocality() == null ? "" : contact.getLocality());
                        summary.setDescription(contact.getDescription() == null ? "" : contact.getDescription());

                        response.getSummaryObjects().add(summary);
                    }
                }
            }

            //Sort contacts
            Collections.sort(response.getSummaryObjects(), new SummaryComparator());
        } catch (Throwable t) {
            log.error("Error getting all addresses.", t);

            ServiceError error = new ServiceError();
            error.setCode(ERROR_ADDRBOOK);
            error.setText(t.getMessage() == null ? t.getClass().getName() : t.getMessage());
            response.getErrorList().add(error);
        }

        return response;
    }

    /**
     * Return the contact details.
     * 
     * @param request
     * @return
     */
    public org.socraticgrid.common.addrbook.GetContactDetailsResponseType getContactDetails(org.socraticgrid.common.addrbook.GetContactDetailsRequestType request) {
        GetContactDetailsResponseType response = new GetContactDetailsResponseType();

        try {
            log.debug("Finding contact details for: " + request.getAddressId());

            //Extract info from address id in request
            StringTokenizer tokenizer = new StringTokenizer(request.getAddressId(), ADDR_ID_DELIM);
            String addrId = null;
            String addrName = null;
            String addrContactId = null;
            if (tokenizer.hasMoreTokens()) {
                addrId = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreTokens()) {
                addrName = tokenizer.nextToken();
            }
            if (tokenizer.hasMoreTokens()) {
                addrContactId = tokenizer.nextToken();
            } else {
                addrContactId = addrName; //in case name not in tokens
            }

            if ((addrContactId == null) || addrContactId.isEmpty()) {
                throw new Exception("Contact id not found.");
            }

            //Search ldap
            ContactDAO contactDAO = LdapService.getContactDAO();
            List<ContactDTO> contacts = contactDAO.findContact(addrContactId);
            if (contacts.isEmpty()) {
                throw new Exception("Contact not found: " + request.getAddressId());
            }

            //Create return object
            ContactDetails details = new ContactDetails();
            details.setAddressId(request.getAddressId());
            details.setName(addrName);
            details.setLocation(contacts.get(0).getLocality() == null ? "" : contacts.get(0).getLocality());
            details.setRoomNumber(contacts.get(0).getRoomNumber() == null ? "" : contacts.get(0).getRoomNumber());
            details.setDescription(contacts.get(0).getDescription() == null ? "" : contacts.get(0).getDescription());
            details.setEmail(contacts.get(0).getMail() == null ? "" : contacts.get(0).getMail());
            details.setFax(contacts.get(0).getFacsimileTelephoneNumber() == null ? "" : contacts.get(0).getFacsimileTelephoneNumber());
            details.setGender(contacts.get(0).getGender() == null ? "" : contacts.get(0).getGender());
            details.setOffice(contacts.get(0).getTelephoneNumber() == null ? "" : contacts.get(0).getTelephoneNumber());

            response.setContact(details);
        } catch (Throwable t) {
            log.error("Error getting contact details.", t);

            ServiceError error = new ServiceError();
            error.setCode(ERROR_ADDRBOOK);
            error.setText(t.getMessage() == null ? t.getClass().getName() : t.getMessage());
            response.getErrorList().add(error);
        }

        return response;
    }

    /**
     * 
     * @param userId
     * @return
     */
    public GetAddressBookResponseType getAddrBookForUserId(String userId) {
        
        //---------------------------------------------
        // Query db table addrbook to find all contacts
        //    for addrbook.User_ID == userId
        //---------------------------------------------
        AddrBookService dbService = new AddrBookService();
        AddressQueryParams query = new AddressQueryParams();
        query.setUserId(userId);
        List<AddressItem> addrs = dbService.addressQuery(query);

        //------------------------------------------------------
        // For each contact found, 
        //    locate the person's ldap data using the addrbook.Contact_ID, which
        //    is equivalent to the ldap's cn for that person.
        //    and add to a response object for return.
        //-------------------------------------------------------
        GetAddressBookResponseType response = new GetAddressBookResponseType();

        for (AddressItem contact : addrs) {
            GetAddressBookResponseType.GetAddressResponse gabResponse =
                    new GetAddressBookResponseType.GetAddressResponse();

            gabResponse.setName(contact.getName());

            //Search ldap
            ContactDAO contactDAO = LdapService.getContactDAO();
            List<ContactDTO> contacts = contactDAO.findContact("cn=" + contact.getContactId());
            if (contacts.isEmpty()) {
                log.error("Contact not found for userId: " + userId);
                continue;
            }

            ContactDTO ctc = contacts.get(0);
            gabResponse.setAddress1(ctc.getStreet());
            gabResponse.setCity(ctc.getCity());
            gabResponse.setState(ctc.getState());
            gabResponse.setZipCode(ctc.getPostalCode());
            gabResponse.setContactId(contact.getContactId());
            gabResponse.setCity(ctc.getCity());

            if (ctc.getTelephoneNumber() != null) {
                gabResponse.getPhones().add(ctc.getTelephoneNumber());
            }

            if (ctc.getMobile() != null) {
                gabResponse.getPhones().add(ctc.getMobile());
            }

            if (ctc.getHomePhone() != null) {
                gabResponse.getPhones().add(ctc.getHomePhone());
            }

            if (ctc.getPager() != null) {
                gabResponse.getPhones().add(ctc.getPager());
            }

            if (ctc.getFacsimileTelephoneNumber() != null) {
                gabResponse.getPhones().add(ctc.getFacsimileTelephoneNumber());
            }


            gabResponse.setSuccessStatus(true);
            response.getGetAddressResponse().add(gabResponse);
        }

        return response;
    }

    public void saveAddressBookEntry(String providerId) {
        AddrBookService dbService = new AddrBookService();

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact("uid=" + providerId);
        if (contacts.isEmpty()) {
            log.error("Contact not found: " + providerId);
        }
        ContactDTO contact = contacts.get(0);
        
        AddressItem item = new AddressItem();
        item.setUserId(providerId);
        item.setContactId(contact.getCommonName());
        item.setLastUpdated(new Date());
        item.setName(contact.getDisplayName());
        
        dbService.saveAddress(item);
    }
}
