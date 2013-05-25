
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

package org.socraticgrid.presentationservices.helpers;

import org.socraticgrid.ldapaccess.ContactAcctDTO;
import org.socraticgrid.ldapaccess.ContactDAO;
import org.socraticgrid.ldapaccess.ContactDTO;
import org.socraticgrid.ldapaccess.LdapService;

import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Session;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Store;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 * @author  Jerry Goodnough
 */
public class MailServiceHelper
{

    /** Property constants. */
    public static final String PROPERTY_FILE = "displayAggregator";

    /** DOCUMENT ME! */
    public static final String MAIL_HOST = "mail.host";

    /** DOCUMENT ME! */
    public static final String MAIL_PROTOCOL = "mail.protocol";

    /** LDAP attribute for provider's user id. */
    public static final String LDAP_PROVIDER_ID_ATT = "employeeNumber";

    /** LDAP attribute for patient's user id. */
    public static final String LDAP_PATIENT_ID_ATT = "uid";

    /** Item identifier for provider ids. */
    public static final String ITEM_ID_PROVIDER = "P";

    /** Item identifier for patient ids. */
    public static final String ITEM_ID_PATIENT = "T";

    /** Item id separater between userId and message number. */
    public static final String ITEM_ID_SEPARATER = "?";

    /** Data source name. */
    public static final String DATA_SOURCE = "Mail";

    /** Item names for name value pairs. */
    public static final String ITEM_READ = "Read";

    /** DOCUMENT ME! */
    public static final String ITEM_REPLIED = "Replied";

    /** Standard error code. */
    public static final Integer ERR_CODE = -1;

    /** Service error message. */
    public static final String ERR_MSG_ITEM_NOT_FOUND = "Item was not found.";

    /** Mail host. */
    private String host = "zimbra.scoraticgrid.org";

    /** Mail protocol. */
    private String protocol = "imap";

    private static MailServiceHelper singleton = new MailServiceHelper();

    public static MailServiceHelper getMailServiceHelper()
    {
        return singleton;
    }

    private MailServiceHelper()
    {
        PropertyHelper ph = PropertyHelper.getPropertyHelper();
        host = ph.getProperty("EmailServer");
        protocol = ph.getProperty("EmailProtocol");
    }

    /**
     * Translate a list of
     *
     * @param   flag  One of the following: ANSWERED, DELETED, DRAFT, FLAGGED, RECENT, SEEN
     *
     * @return  Internal Flag Code
     */
    public static int translateFlag(String flag)
    {
        String check = flag.toUpperCase();

        if (check.compareTo("SEEN") == 0)
        {
            return 6;
        }
        else if (check.compareTo("ANSWERED") == 0)
        {
            return 1;
        }
        else if (check.compareTo("DELETED") == 0)
        {
            return 2;
        }
        else if (check.compareTo("DRAFT") == 0)
        {
            return 3;
        }
        else if (check.compareTo("FLAGGED") == 0)
        {
            return 4;
        }
        else if (check.compareTo("RECENT") == 0)
        {
            return 5;
        }

        return 0;
    }

    /**
     * Mark a Mail Message False - String Version
     *
     * @param   itemId         Item Id from Aggregator Service
     * @param   msgFlagString  Comma separated  list of ANSWERED, DELETED, DRAFT, FLAGGED, RECENT, SEEN
     * @param   valueString    True or False
     *
     * @return  true or false (Data Fault or exception)
     */
    public boolean markMailMessage(String itemId, String msgFlagString,
        String valueString)
    {
        String[] flags = msgFlagString.split(",");

        int[] msgFlag = new int[flags.length];

        for (int i = 0; i < flags.length; i++)
        {
            msgFlag[i] = translateFlag(flags[i]);
        }

        boolean value = Boolean.parseBoolean(valueString);

        return markMailMessage(itemId, msgFlag, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   itemId   DOCUMENT ME!
     * @param   msgFlag  DOCUMENT ME!
     * @param   value    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean markMailMessage(String itemId, int[] msgFlag, boolean value)
    {
        String userId;
        String userType;

        //Pull out userId
        userId = itemId.substring(1, itemId.indexOf(ITEM_ID_SEPARATER));
        userType = String.valueOf(itemId.charAt(0));

        if ((userId == null) || userId.isEmpty())
        {
            return false;
        }

        //Get login info from ldap
        String[] access = retrieveMailAccess(userType, userId);

        if ((access[0] == null) || access[0].isEmpty())
        {
            return false;
        }

        //Retrieve message
        return markMailMessage(access[0], access[1], itemId, msgFlag, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   login    DOCUMENT ME!
     * @param   pswd     DOCUMENT ME!
     * @param   itemId   DOCUMENT ME!
     * @param   msgFlag  DOCUMENT ME!
     * @param   value    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean markMailMessage(String login, String pswd, String itemId,
        int[] msgFlag, boolean value)
    {
        boolean out = false;
        Store store = null;
        Folder folder = null;

        try
        {
            String[] msgId = parseMsgId(itemId);

            //Get session
            Session session = Session.getDefaultInstance(new Properties());

            //Get the store
            store = session.getStore(protocol);
            store.connect(host, login, pswd);

            //Get folder
            folder = store.getFolder(msgId[1]);
            folder.open(Folder.READ_WRITE);

            //int[] msgs = new int[1];
            //msgs[1]=Integer.parseInt(msgId[2]);
            int[] imsgId = new int[1];
            imsgId[0] = Integer.parseInt(msgId[2]);

            Flags.Flag flg = null;
            Flags flags = new Flags();

            for (int i = 0; i < msgFlag.length; i++)
            {

                switch (msgFlag[i])
                {

                    case 1:
                    {
                        flg = Flags.Flag.ANSWERED;

                        break;
                    }

                    case 2:
                    {
                        flg = Flags.Flag.DELETED;

                        break;
                    }

                    case 3:
                    {
                        flg = Flags.Flag.DRAFT;

                        break;
                    }

                    case 4:
                    {
                        flg = Flags.Flag.FLAGGED;

                        break;
                    }

                    case 5:
                    {
                        flg = Flags.Flag.RECENT;

                        break;
                    }

                    case 6:
                    {
                        flg = Flags.Flag.SEEN;

                        break;
                    }
                }

                if (flg != null)
                {
                    flags.add(flg);
                }
            }

            folder.setFlags(imsgId, flags, value);
            out = true;
        }
        catch (Exception e)
        {
            out = false;
            Logger.getLogger(MailServiceHelper.class.getName()).log( Level.ERROR,"Exception during markMailMessage: "+ e.getMessage(),e);
        }
        finally
        {

            // Close connection
            if (folder != null)
            {

                try
                {
                    folder.close(false);
                }
                catch (Exception e)
                {
                }
            }

            if (store != null)
            {

                try
                {
                    store.close();
                }
                catch (Exception e)
                {
                }
            }
        }

        return out;

    }

    /**
     * Extract folder and message number from item id.
     *
     * @param   itemId
     *
     * @return
     */
    private String[] parseMsgId(String itemId)
    {
        String[] msgId = new String[3];
        StringTokenizer st = new StringTokenizer(itemId, ITEM_ID_SEPARATER);

        msgId[0] = st.nextToken(); //userId
        msgId[1] = st.nextToken(); //folder
        msgId[2] = st.nextToken(); //msg num

        return msgId;
    }

    /**
     * Find LDAP record from userId.
     *
     * @param   userId
     *
     * @return
     */
    public String[] retrieveMailAccess(String userType, String userId)
    {
        String[] access = new String[2];

        ContactDAO contactDAO = LdapService.getContactDAO();
        List<ContactDTO> contacts = contactDAO.findContact(
                (ITEM_ID_PROVIDER.equals(userType) ? LDAP_PROVIDER_ID_ATT
                                                   : LDAP_PATIENT_ID_ATT) +
                "=" + userId);

        if (contacts.size() > 0)
        {

            //Get mail login info
            List<ContactAcctDTO> accts = contactDAO.findContactAcct(
                    contacts.get(0).getCommonName(), ContactAcctDTO.CN_MAIL);

            if (accts.size() > 0)
            {
                access[0] = accts.get(0).getUid();
                access[1] = accts.get(0).getClearPassword();
            }
        }

        return access;
    }
}
