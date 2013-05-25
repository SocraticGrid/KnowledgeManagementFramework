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
package org.socraticgrid.kmrolib;

import org.xml.sax.SAXException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;
import javax.xml.validation.Schema;
import javax.xml.transform.Source;
import javax.xml.validation.SchemaFactory;
import javax.xml.XMLConstants;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
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
public class TestTransform {

    public TestTransform() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }


    @Test
    public void testTransform() throws Exception {

        String inXML = "/home/nhin/test/deep_Vitals.xml";
        String inXSL = "/home/nhin/test/toKMR2Format.xslt";
        String outTXT = "/home/nhin/test/output.xml";

        try {
            transform(inXML, inXSL, outTXT);
        } catch (TransformerConfigurationException e) {
            System.err.println("Invalid factory configuration");
            System.err.println(e);
        } catch (TransformerException e) {
            System.err.println("Error during transformation");
            System.err.println(e);
        }

    }


    public void transform (String inXML,String inXSL,String outTXT)
    throws TransformerConfigurationException,
           TransformerException
    {
    	TransformerFactory factory = TransformerFactory.newInstance();

        	StreamSource xslStream = new StreamSource(inXSL);
        	Transformer transformer = factory.newTransformer(xslStream);
        	transformer.setErrorListener(new MyErrorListener2());

        	StreamSource in = new StreamSource(inXML);
        	StreamResult out = new StreamResult(outTXT);
        	transformer.transform(in,out);
        	System.out.println("The generated XML file is:" + outTXT);

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
class MyErrorListener2 implements ErrorListener {
    public void warning(TransformerException e)
                throws TransformerException {
        show("Warning",e);
        throw(e);
    }
    public void error(TransformerException e)
                throws TransformerException {
        show("Error",e);
        throw(e);
    }
    public void fatalError(TransformerException e)
                throws TransformerException {
        show("Fatal Error",e);
        throw(e);
    }
    private void show(String type,TransformerException e) {
        System.out.println(type + ": " + e.getMessage());
        if(e.getLocationAsString() != null)
            System.out.println(e.getLocationAsString());

    }
}