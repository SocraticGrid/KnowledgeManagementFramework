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
package org.socraticgrid.displaycalendarlib;

import org.socraticgrid.util.CommonUtil;
import org.osaf.caldav4j.dialect.SogoCalDavDialect;
import java.io.IOException;
import java.util.List;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Component;
import net.fortuna.ical4j.model.ComponentList;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.UidGenerator;
import org.apache.commons.httpclient.HostConfiguration;
import org.osaf.caldav4j.credential.CaldavCredential;

/**
 *
 * @author nhin
 */
public class SogoCalendar {

    public String host = "172.16.128.130";
    public int port = 20000;
    public String protocol = "http";
    public String user = "sogo1";
    public int userId = 99990070;
    public String home = "/dav/" + user + "/";
    public String password = "sogo";
    public String collection = "collection/";

    public List<CalResponse> getCalsForUser(int userId) throws Exception,
            IOException {

        CalResponse cr = new CalResponse();
        Calendar cal = getCurrentCalendar(userId)[0];

        PropertyList props = cal.getProperties();
        cr.setCalendarId(props.getProperty(Property.UID).getValue());
        cr.setTextColor("#FFF");
        cr.setBackgroundColor("#2952A3");
        ComponentList components = cal.getComponents();
        for (Object o : components) {
            Component c = (Component) o;
            if (c instanceof VEvent) {
                Events e = new Events();
                e.setTitle(c.getName());
                e.setStart(c.getProperties().getProperty(Property.DTSTART).getValue());
                e.setEnd(c.getProperties().getProperty(Property.DTEND).getValue());
                String duration = c.getProperties().getProperty(Property.DURATION).getValue();
                if (duration.equals("+P1D")) {
                    e.setAllDay(true);
                } else {
                    e.setAllDay(false);
                }
                cr.getEvents().add(e);
            }
        }

        return null;
    }

    private Calendar[] getCurrentCalendar(int userId) throws Exception {
        SogoCalDavDialect dialect = new SogoCalDavDialect();
        CaldavCredential cred = new CaldavCredential(protocol, host, port,
                home, collection, user, password);
        CalendarInit init = new CalendarInit(cred, dialect);
        Calendar[] cals = init.getCalendars();
        return cals;
    }

    public String setCalendarEvent(SetCalendarRequest request) throws Exception {
        String ret = "";
        Calendar cal = getCurrentCalendar(Integer.parseInt(request.getUserId()))[0];
        if (request.getAction().equals("delete")) {
            deleteEvent(cal, request);
        } else if (CommonUtil.strNullorEmpty(request.getEventId())) {
            createEvent(cal, request);
        } else {
            updateEvent(cal, request);
        }

        return ret;
    }

    private void deleteEvent(Calendar cal, SetCalendarRequest request) {
        ComponentList comps = cal.getComponents();
        for (Object o : comps) {
            if (o instanceof VEvent) {
                VEvent event = (VEvent) o;
                cal.getComponents().remove(event);
            }
        }
    }

    private void createEvent(Calendar cal, SetCalendarRequest request) throws Exception {
        VEvent event = new VEvent(new Date(request.getStartDate()),
                new Date(request.getEndDate()), request.getTitle());
        UidGenerator ug = new UidGenerator("1");
        Uid uid = ug.generateUid();
        event.getProperties().add(uid);
        cal.getComponents().add(event);
    }

    private void updateEvent(Calendar cal, SetCalendarRequest request) throws Exception {
        ComponentList comps = cal.getComponents();
        for (Object o : comps) {
            if (o instanceof VEvent) {
                VEvent ve = (VEvent) o;
                if (ve.getUid().equals(request.getEventId())) {
                    ve.getProperties().getProperty(Property.DTSTART).setValue(request.getStartDate());
                    ve.getProperties().getProperty(Property.DTEND).setValue(request.getEndDate());
                    ve.getProperties().getProperty(Property.SUMMARY).setValue(request.getTitle());
                }
            }
        }
    }


    public HostConfiguration createHostConfiguration() {
        HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(host, port, protocol);
        return hostConfig;
    }
}
