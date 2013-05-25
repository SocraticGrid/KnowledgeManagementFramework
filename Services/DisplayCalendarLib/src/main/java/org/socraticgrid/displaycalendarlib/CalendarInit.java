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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.fortuna.ical4j.connector.dav.CalDavCalendarCollection;
import net.fortuna.ical4j.connector.dav.CalDavCalendarStore;
import net.fortuna.ical4j.connector.dav.PathResolver;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.util.WebdavStatus;
import org.apache.webdav.lib.methods.MkcolMethod;
import org.osaf.caldav4j.credential.CaldavCredential;
import org.osaf.caldav4j.dialect.CalDavDialect;
import org.osaf.caldav4j.methods.CalDAV4JMethodFactory;
import org.osaf.caldav4j.methods.DeleteMethod;
import org.osaf.caldav4j.methods.HttpClient;
import org.osaf.caldav4j.methods.MkCalendarMethod;
import org.osaf.caldav4j.methods.PutMethod;
import org.osaf.caldav4j.util.UrlUtils;

/**
 * Provides fixture support for CalDAV functional tests.
 * 
 * @author <a href="mailto:markhobson@gmail.com">Mark Hobson</a>
 * @version $Id: CalDavFixture.java 323 2011-07-26 16:30:44Z robipolli@gmail.com $
 */
public class CalendarInit {
    // fields -----------------------------------------------------------------

    protected static final Log log = LogFactory.getLog(CalendarInit.class);
    private HttpClient httpClient;
    private CalDAV4JMethodFactory methodFactory;
    private String collectionPath;
    private List<String> deleteOnTearDownPaths;
    private CalDavDialect dialect;
    public final String sogoProdId = "-//Inverse inc./SOGo 1.3.7//EN";
    private Calendar[] calendars;

    public CalendarInit(CaldavCredential credential, CalDavDialect dialect) throws Exception {
        httpClient = new HttpClient();
        configure(httpClient, credential);

        methodFactory = new CalDAV4JMethodFactory();
        collectionPath = UrlUtils.removeDoubleSlashes(credential.home + credential.collection);
        deleteOnTearDownPaths = new ArrayList<String>();
        
        URL calUrl = new URL("https://socraticgrid.org:10000/SOGo/dav");
        KmrPathResolver pathResolver = new KmrPathResolver();
        CalDavCalendarStore store = new CalDavCalendarStore(sogoProdId, calUrl, pathResolver);
        store.connect("sogo1", "sogo".toCharArray());
        List<CalDavCalendarCollection> collections = store.getCollections();
        CalDavCalendarCollection collection = collections.get(0);
        calendars = collection.getEvents();
        this.dialect = dialect;
    }
    
    public Calendar[] getCalendars() {
        return calendars;
    }


    public void makeCalendar(String relativePath) throws IOException {
        /*
        GoogleCalDavDialect gdialect = new GoogleCalDavDialect();
        if (dialect.equals(gdialect.getProdId())) {
        log.warn("Google Caldav Server doesn't support MKCALENDAR");
        return;
        }
         */
        MkCalendarMethod method = methodFactory.createMkCalendarMethod();
        method.setPath(relativePath);

        executeMethod(HttpStatus.SC_CREATED, method, true);
    }

    public void makeCollection(String relativePath) throws IOException {
        MkcolMethod method = new MkcolMethod(UrlUtils.removeDoubleSlashes(relativePath));

        executeMethod(HttpStatus.SC_CREATED, method, true);
    }

    public void putEvent(String relativePath, VEvent event) throws IOException {
        PutMethod method = methodFactory.createPutMethod();
        method.setPath(relativePath);
        method.setRequestBody(event);

        executeMethod(HttpStatus.SC_CREATED, method, true);
    }

    public void delete(String relativePath) throws IOException {
        DeleteMethod method = new DeleteMethod();
        method.setPath(relativePath);

        executeMethod(HttpStatus.SC_NO_CONTENT, method, false);
    }

    public void executeMethod(int expectedStatus, HttpMethod method, boolean deleteOnTearDown)
            throws IOException {
        int response = executeCalMethod(expectedStatus, method, deleteOnTearDown);
        if (response != 200) {
            throw new IOException("Error executing method: " + method
                    + " HTTP response code: " + response);
        }
    }

    public int executeCalMethod(int expectedStatus, HttpMethod method,
            boolean deleteOnTearDown) throws IOException {
        String relativePath = method.getPath();

        // prefix path with collection path
        method.setPath(collectionPath + relativePath);

        int response = executeHttpMethod(expectedStatus, httpClient, method);

        if (deleteOnTearDown) {
            deleteOnTearDownPaths.add(relativePath);
        }

        return response;
    }

    public static int executeHttpMethod(int expectedStatus,
            HttpClient httpClient, HttpMethod method) throws IOException {
        try {
            int actualStatus = httpClient.executeMethod(method);
            return actualStatus;
        } finally {
            method.releaseConnection();
        }
    }

    // private methods --------------------------------------------------------
    private static void configure(HttpClient httpClient, CaldavCredential credential) {
        httpClient.getHostConfiguration().setHost(credential.host, credential.port, credential.protocol);

        Credentials httpCredentials = new UsernamePasswordCredentials(credential.user, credential.password);
        httpClient.getState().setCredentials(AuthScope.ANY, httpCredentials);

        httpClient.getParams().setAuthenticationPreemptive(true);
    }

    public void setDialect(CalDavDialect dialect) {
        this.dialect = dialect;
    }

    public CalDavDialect getDialect() {
        return dialect;
    }

    protected void mkcalendar(String path) {
        MkCalendarMethod mk = new MkCalendarMethod();
        mk.setPath(path);
        mk.addDescription("en");
        try {
            executeMethod(HttpStatus.SC_CREATED, mk, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void mkcol(String path) {
        MkcolMethod mk = new MkcolMethod(path);
        try {
            executeMethod(HttpStatus.SC_CREATED, mk, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * FIXME this put updates automatically the timestamp of the event 
     * @param resourceFileName
     * @param path
     */
    public void put(String resourceFileName, String path) {
        PutMethod put = methodFactory.createPutMethod();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
        String event = UrlUtils.parseISToString(stream);
        event = event.replaceAll("DTSTAMP:.*", "DTSTAMP:" + new DateTime(true).toString());
        log.debug(new DateTime(true).toString());
        //log.trace(event);        

        put.setRequestEntity(event);
        put.setPath(path);
        log.debug("\nPUT " + put.getPath());
        try {
            executeMethod(WebdavStatus.SC_CREATED, put, true);

            int statusCode = put.getStatusCode();

            switch (statusCode) {
                case WebdavStatus.SC_CREATED:
                case WebdavStatus.SC_NO_CONTENT:
                    break;
                case WebdavStatus.SC_PRECONDITION_FAILED:
                    log.error("item exists?");
                    break;
                case WebdavStatus.SC_CONFLICT:
                    log.error("conflict: item still on server");
                default:
                    log.error(put.getResponseBodyAsString());
                    throw new Exception("trouble executing PUT of " + resourceFileName + "\nresponse:" + put.getResponseBodyAsString());

            }
        } catch (Exception e) {
            log.info("Error while put():" + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * remove an event on a caldav store using UID.ics
     * @throws IOException 
     */
    public void caldavDel(String s) throws IOException {
        Calendar cal = getCalendarResource(s);
        String delPath = collectionPath + "/" + cal.getComponent("VEVENT").getProperty("UID").getValue() + ".ics";
        log.debug("DEL " + delPath);
        delete(delPath);

    }

    /**
     * put an event on a caldav store using UID.ics
     */
    public void caldavPut(String s) {
        Calendar cal = getCalendarResource(s);

        String resPath = //collectionPath + "/" +
                cal.getComponent("VEVENT").getProperty("UID").getValue() + ".ics";

        put(s, resPath);
    }

    public CalDAV4JMethodFactory getMethodFactory() {
        return methodFactory;
    }

    public void setMethodFactory(CalDAV4JMethodFactory methodFactory) {
        this.methodFactory = methodFactory;
    }

    public String getCollectionPath() {
        return collectionPath;
    }

    public void setCollectionPath(String collectionPath) {
        this.collectionPath = collectionPath;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public Calendar getCalendarResource(String resourceName) {
        Calendar cal;

        InputStream stream = CalendarInit.class.getClassLoader().getResourceAsStream(resourceName);
        CalendarBuilder cb = new CalendarBuilder();
        try {
            cal = cb.build(stream);
        } catch (Exception e) {
            throw new RuntimeException("Problems opening file:" + resourceName + "\n" + e);
        }

        return cal;
    }
}
