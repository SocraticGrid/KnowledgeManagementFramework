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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Jerry Goodnough
 */
public class PreferencesHelper
{

    /** DOCUMENT ME! */
    private static PreferencesHelper singleton = new PreferencesHelper();

    private String staticEmptyPrefernece =
        " {\"preferences\":{\"general\":{\"use24HourClock\":\"true\"},\"pmrCalendar\":[],\"externalCalendar\":[]}}";

    public static PreferencesHelper getPreferencesHelper()
    {
        return singleton;
    }

    public String getPreferencesAsJSON(String patientId)
    {
        String out = "";
        String location = PropertyHelper.getPropertyHelper().getProperty(
                "pmrPreferencesLocation");

        try
        {
            BufferedReader in = new BufferedReader(new FileReader(
                        location + patientId + ".json"));
            String str;

            while ((str = in.readLine()) != null)
            {
                out += str;
            }
        }
        catch (Exception e)
        {

            //e.printStackTrace();
            out = staticEmptyPrefernece;
        }

        return out;
    }

    public boolean savePreferencesAsJSON(String patientId, String data)
    {
        BufferedWriter out = null;
        String location = PropertyHelper.getPropertyHelper().getProperty(
                "pmrPreferencesLocation");
        boolean ok = true;

        try
        {

            out = new BufferedWriter(new FileWriter(
                        location + patientId + ".json"));
            out.write(data);
        }
        catch (IOException e)
        {
            Logger.getLogger(PreferencesHelper.class.getName()).log(
                Level.SEVERE,
                e.getMessage() + ", Error Writeing to " + location + patientId +
                ".json", e);
            ok = false;
        }
        finally
        {

            try
            {
                out.close();
            }
            catch (IOException e)
            {
                Logger.getLogger(PreferencesHelper.class.getName()).log(
                    Level.SEVERE,
                    e.getMessage() + ", Error Writeing to " + location +
                    patientId + ".json", e);
                ok = false;
            }
        }

        return ok;
    }
}
