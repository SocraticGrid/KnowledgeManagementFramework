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
package org.socraticgrid.presentationservices;

import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author nhin
 */
public class TestFactRequestTransform {

    public TestFactRequestTransform() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test Output =
      
            {
              "facts":   [
                    {
                  "fillDt": "1989-01-15",
                  "dispenseDt": "2011-12-11",
                  "drugDispensed": "Amoxicillin",
                  "code": "0093-4160-78",
                  "codeSystemName": "NDC",
                  "codeSystemCode": "NDC",
                  "manufacturer": "Abott",
                  "dispAmount": "5",
                  "expirationDt": "2011-12-12",
                  "location": "NMXSD",
                  "pharmacist": "John Terry",
                  "hoverTexts": ["0093-4160-78 (PH_FDA_DRLS)"]
                },
                []
              ],
              "successStatus": "true"
            }
     * @throws Exception
     */
    @Test
    public void testxml() throws Exception {
String text =
"<patientDataFact>"
+"<facts>"
+"<fillDt>1989-01-15</fillDt>"
+"<dispenseDt>2011-12-11</dispenseDt>"
+"<drugDispensed>Amoxicillin</drugDispensed>"
+"<code>0093-4160-78</code>"
+"<codeSystemName>NDC</codeSystemName>"
+"<codeSystemCode>NDC</codeSystemCode>"
+"<manufacturer>Abott</manufacturer>"
+"<dispAmount>5</dispAmount>"
+"<expirationDt>2011-12-12</expirationDt>"
+"<location>NMXSD</location>"
+"<pharmacist>John Terry</pharmacist>"
+"<hoverTexts/>"
+"<hoverTexts>0093-4160-78 (PH_FDA_DRLS)</hoverTexts>"
+"</facts>"
+"<facts/>"
+"<successStatus>true</successStatus>"
+"</patientDataFact>"
;
        //SERIALIZE XML TO JSON
        XMLSerializer xmlser = new XMLSerializer();
        xmlser.setTypeHintsEnabled(true);

        JSON jsonOut = xmlser.read(text);
        String jsonRsp = jsonOut.toString(2);

        System.out.println("\nJSON=\n" + jsonRsp);
    }

    //@Test
    public void testxmlNamespace() throws Exception {
/*
        //READ IN XML FILE
        String filename = "/home/nhin/Properties/facts/data/getPatientData-Medications_List.json";
        System.out.println("PULLING list STATIC DATA:  "+filename);
        String text = new Scanner( new File(filename) ).useDelimiter("\\A").next();
*/
String text =
 "<kmr:patientDataFact>"
+"<kmr:factType>Medications</kmr:factType>"
+"<kmr:trxnType>detail</kmr:trxnType>"
+"<kmr:visibleGridHeaders>true</kmr:visibleGridHeaders>"
+"<kmr:maxcolumns>8</kmr:maxcolumns>"
+"<kmr:itemId>10</kmr:itemId>"
+"<kmr:status>Active</kmr:status>"
+"<kmr:detailTabs>"
+"<kmr:label>Dispensations</kmr:label>"
+"<kmr:type>grid</kmr:type>"
+"<kmr:responseType>detail</kmr:responseType>"
+"<kmr:sectionId>dispensations</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:detailTabs>"
+"<kmr:label>Description</kmr:label>"
+"<kmr:type>text</kmr:type>"
+"<kmr:responseType>ecs</kmr:responseType>"
+"<kmr:sectionId>4</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"<kmr:filters>sectionId</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:detailTabs>"
+"<kmr:label>Instructions</kmr:label>"
+"<kmr:type>text</kmr:type>"
+"<kmr:responseType>ecs</kmr:responseType>"
+"<kmr:sectionId>102</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"<kmr:filters>sectionId</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:detailTabs>"
+"<kmr:label>Indications</kmr:label>"
+"<kmr:type>text</kmr:type>"
+"<kmr:responseType>ecs</kmr:responseType>"
+"<kmr:sectionId>104</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"<kmr:filters>sectionId</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:detailTabs>"
+"<kmr:label>Side-Effects</kmr:label>"
+"<kmr:type>text</kmr:type>"
+"<kmr:responseType>ecs</kmr:responseType>"
+"<kmr:sectionId>105</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"<kmr:filters>sectionId</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:detailTabs>"
+"<kmr:label>Potential Interactions</kmr:label>"
+"<kmr:type>text</kmr:type>"
+"<kmr:responseType>ecs</kmr:responseType>"
+"<kmr:sectionId>106</kmr:sectionId>"
+"<kmr:filters>code</kmr:filters>"
+"<kmr:filters>codeSystemCode</kmr:filters>"
+"<kmr:filters>sectionId</kmr:filters>"
+"</kmr:detailTabs>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>fillDt</kmr:columnId>"
+"<kmr:value>Fill D/T</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>dispenseDt</kmr:columnId>"
+"<kmr:value>Dispense D/T</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>drugDispensed</kmr:columnId>"
+"<kmr:value>Drug Dispensed</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>manufacturer</kmr:columnId>"
+"<kmr:value>Manufacturer</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>dispAmount</kmr:columnId>"
+"<kmr:value>Disp Amount</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>4</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>expirationDt</kmr:columnId>"
+"<kmr:value>Expiration Date</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>location</kmr:columnId>"
+"<kmr:value>Loaction</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:gridHeaders>"
+"<kmr:columnId>pharmacist</kmr:columnId>"
+"<kmr:value>Pharmacist</kmr:value>"
+"<kmr:description>A column header.</kmr:description>"
+"<kmr:width>10</kmr:width>"
+"</kmr:gridHeaders>"
+"<kmr:facts>"
+"<kmr:fact>"
+"<kmr:fillDt>1989-01-15</kmr:fillDt>"
+"<kmr:dispenseDt>2011-12-11</kmr:dispenseDt>"
+"<kmr:drugDispensed>Amoxicillin</kmr:drugDispensed>"
+"<kmr:code>0093-4160-78</kmr:code>"
+"<kmr:codeSystemName>NDC</kmr:codeSystemName>"
+"<kmr:codeSystemCode>NDC</kmr:codeSystemCode>"
+"<kmr:manufacturer>Abott</kmr:manufacturer>"
+"<kmr:dispAmount>5</kmr:dispAmount>"
+"<kmr:expirationDt>2011-12-12</kmr:expirationDt>"
+"<kmr:location>NMXSD</kmr:location>"
+"<kmr:pharmacist>John Terry</kmr:pharmacist>"
+"<kmr:hoverTexts/>"
+"<kmr:hoverTexts/>"
+"<kmr:hoverTexts>0093-4160-78 (PH_FDA_DRLS)</kmr:hoverTexts>"
+"<kmr:fact>"
+"</kmr:facts>"
+"<kmr:successStatus>true</kmr:successStatus>"
+"</kmr:patientDataFact>"
;
        //SERIALIZE XML TO JSON
        XMLSerializer xmlser = new XMLSerializer();


        System.out.println("\nXML: " + text);

        xmlser.setSkipNamespaces(true);
        xmlser.setRemoveNamespacePrefixFromElements(true);
        JSON jsonOut = xmlser.read(text);
        String jsonRsp = jsonOut.toString(2);        
    }








    //@Test
    public void testXmlToJson() throws Exception {
        String xml = "<CATALOG>\n"
                + "<CD>\n"
                + "<TITLE>Empire Burlesque</TITLE>\n"
                + "<ARTIST>Bob Dylan</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>Columbia</COMPANY>\n"
                + "<PRICE>10.90</PRICE>\n"
                + "<YEAR>1985</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Hide your heart</TITLE>\n"
                + "<ARTIST>Bonnie Tyler</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>CBS Records</COMPANY>\n"
                + "<PRICE>9.90</PRICE>\n"
                + "<YEAR>1988</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Greatest Hits</TITLE>\n"
                + "<ARTIST>Dolly Parton</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>RCA</COMPANY>\n"
                + "<PRICE>9.90</PRICE>\n"
                + "<YEAR>1982</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Still got the blues</TITLE>\n"
                + "<ARTIST>Gary Moore</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Virgin records</COMPANY>\n"
                + "<PRICE>10.20</PRICE>\n"
                + "<YEAR>1990</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Eros</TITLE>\n"
                + "<ARTIST>Eros Ramazzotti</ARTIST>\n"
                + "<COUNTRY>EU</COUNTRY>\n"
                + "<COMPANY>BMG</COMPANY>\n"
                + "<PRICE>9.90</PRICE>\n"
                + "<YEAR>1997</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>One night only</TITLE>\n"
                + "<ARTIST>Bee Gees</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Polydor</COMPANY>\n"
                + "<PRICE>10.90</PRICE>\n"
                + "<YEAR>1998</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Sylvias Mother</TITLE>\n"
                + "<ARTIST>Dr.Hook</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>CBS</COMPANY>\n"
                + "<PRICE>8.10</PRICE>\n"
                + "<YEAR>1973</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Maggie May</TITLE>\n"
                + "<ARTIST>Rod Stewart</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Pickwick</COMPANY>\n"
                + "<PRICE>8.50</PRICE>\n"
                + "<YEAR>1990</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Romanza</TITLE>\n"
                + "<ARTIST>Andrea Bocelli</ARTIST>\n"
                + "<COUNTRY>EU</COUNTRY>\n"
                + "<COMPANY>Polydor</COMPANY>\n"
                + "<PRICE>10.80</PRICE>\n"
                + "<YEAR>1996</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>When a man loves a woman</TITLE>\n"
                + "<ARTIST>Percy Sledge</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>Atlantic</COMPANY>\n"
                + "<PRICE>8.70</PRICE>\n"
                + "<YEAR>1987</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Black angel</TITLE>\n"
                + "<ARTIST>Savage Rose</ARTIST>\n"
                + "<COUNTRY>EU</COUNTRY>\n"
                + "<COMPANY>Mega</COMPANY>\n"
                + "<PRICE>10.90</PRICE>\n"
                + "<YEAR>1995</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>1999 Grammy Nominees</TITLE>\n"
                + "<ARTIST>Many</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>Grammy</COMPANY>\n"
                + "<PRICE>10.20</PRICE>\n"
                + "<YEAR>1999</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>For the good times</TITLE>\n"
                + "<ARTIST>Kenny Rogers</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Mucik Master</COMPANY>\n"
                + "<PRICE>8.70</PRICE>\n"
                + "<YEAR>1995</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Big Willie style</TITLE>\n"
                + "<ARTIST>Will Smith</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>Columbia</COMPANY>\n"
                + "<PRICE>9.90</PRICE>\n"
                + "<YEAR>1997</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Tupelo Honey</TITLE>\n"
                + "<ARTIST>Van Morrison</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Polydor</COMPANY>\n"
                + "<PRICE>8.20</PRICE>\n"
                + "<YEAR>1971</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Soulsville</TITLE>\n"
                + "<ARTIST>Jorn Hoel</ARTIST>\n"
                + "<COUNTRY>Norway</COUNTRY>\n"
                + "<COMPANY>WEA</COMPANY>\n"
                + "<PRICE>7.90</PRICE>\n"
                + "<YEAR>1996</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>The very best of</TITLE>\n"
                + "<ARTIST>Cat Stevens</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Island</COMPANY>\n"
                + "<PRICE>8.90</PRICE>\n"
                + "<YEAR>1990</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Stop</TITLE>\n"
                + "<ARTIST>Sam Brown</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>A and M</COMPANY>\n"
                + "<PRICE>8.90</PRICE>\n"
                + "<YEAR>1988</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Bridge of Spies</TITLE>\n"
                + "<ARTIST>T'Pau</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Siren</COMPANY>\n"
                + "<PRICE>7.90</PRICE>\n"
                + "<YEAR>1987</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Private Dancer</TITLE>\n"
                + "<ARTIST>Tina Turner</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>Capitol</COMPANY>\n"
                + "<PRICE>8.90</PRICE>\n"
                + "<YEAR>1983</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Midt om natten</TITLE>\n"
                + "<ARTIST>Kim Larsen</ARTIST>\n"
                + "<COUNTRY>EU</COUNTRY>\n"
                + "<COMPANY>Medley</COMPANY>\n"
                + "<PRICE>7.80</PRICE>\n"
                + "<YEAR>1983</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Pavarotti Gala Concert</TITLE>\n"
                + "<ARTIST>Luciano Pavarotti</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>DECCA</COMPANY>\n"
                + "<PRICE>9.90</PRICE>\n"
                + "<YEAR>1991</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>The dock of the bay</TITLE>\n"
                + "<ARTIST>Otis Redding</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>Atlantic</COMPANY>\n"
                + "<PRICE>7.90</PRICE>\n"
                + "<YEAR>1987</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Picture book</TITLE>\n"
                + "<ARTIST>Simply Red</ARTIST>\n"
                + "<COUNTRY>EU</COUNTRY>\n"
                + "<COMPANY>Elektra</COMPANY>\n"
                + "<PRICE>7.20</PRICE>\n"
                + "<YEAR>1985</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Red</TITLE>\n"
                + "<ARTIST>The Communards</ARTIST>\n"
                + "<COUNTRY>UK</COUNTRY>\n"
                + "<COMPANY>London</COMPANY>\n"
                + "<PRICE>7.80</PRICE>\n"
                + "<YEAR>1987</YEAR>\n"
                + "</CD>\n"
                + "<CD>\n"
                + "<TITLE>Unchain my heart</TITLE>\n"
                + "<ARTIST>Joe Cocker</ARTIST>\n"
                + "<COUNTRY>USA</COUNTRY>\n"
                + "<COMPANY>EMI</COMPANY>\n"
                + "<PRICE>8.20</PRICE>\n"
                + "<YEAR>1987</YEAR>\n"
                + "</CD>\n"
                + "</CATALOG>";
        XMLSerializer xmlser = new XMLSerializer();
        JSON json = xmlser.read(xml);
        System.out.println(json.toString(2));
        assertTrue(true);
    }

    //@Test
    public void testXmlToJsonFile() throws Exception {
        String fileLocation = "/Users/nhin/vitalsSOAPResponse.xml";
        System.out.println("Expected location for vitalsSOAPResponse.xml is: " + fileLocation);
        String xml = readFileAsString(fileLocation);
        XMLSerializer xmlser = new XMLSerializer();
        JSON json = xmlser.read(xml);
        System.out.println(json.toString(2));
        assertTrue(true);

    }

    private String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
