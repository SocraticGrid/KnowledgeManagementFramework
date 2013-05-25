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

package org.socraticgrid.presentationservices.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
// add this import if you need soapaction
//import javax.xml.soap.MimeHeaders;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.Source;

import javax.xml.transform.SourceLocator;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
/**
 *
 * @author markpitman
 */
public class TestResource extends BaseResource {


    private String patientId;
    
    public TestResource(Context context, Request request, Response response) {
        super(context, request, response);

       patientId = request.getResourceRef().getQueryAsForm().getFirstValue("patientId");




        // This representation has only one type of representation.
        getVariants().add(new Variant(MediaType.APPLICATION_JSON));
        context.getClientDispatcher();
    }

    /**
     * Returns a full representation for a given variant.
     */
    @Override
    public Representation represent(Variant variant) throws ResourceException {
        //testing parameters
//        patientId = (String) this.getRequest().getAttributes().get("patientId");
//
//        System.out.println("patientId = "+patientId);

       // String result = makeSOAPCall();
        String result = "boo";

//        System.out.println("endPoint: "+ getProperty("SOAPEndpoint"));

        Representation representation = new StringRepresentation(result, MediaType.APPLICATION_JSON);
        return representation;
    }
   @Override
   public boolean allowPost() {
      return true;
   }

    @Override
    public void acceptRepresentation(Representation entity)
            throws ResourceException {

        try {
            if (entity.getText().contains("method=PUT"))
                this.storeRepresentation(entity);
            else{
                System.out.println("in POST: "+ entity.getText());
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void storeRepresentation(Representation entity) throws ResourceException {
        try {
            System.out.println("in PUT: "+ entity.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String makeSOAPCall(){

        String retVal = "";
        //Context.getClientDispatcher();
        //File file = new File(url.toString());
//        if (file.canRead())
//           System.out.println("success");
//        else
//            System.out.println("failure");

        //fis.read();

        try {
            // Create the connection
            //this.getClass().getClassLoader().getResourceAsStream("/WEB-INF/web.xml");

           getContext().getClientDispatcher();
           InputStream is = getContext().getClass().getResourceAsStream("/WEB-INF/web.xml");
           is.read();

//        File file  = new File(getContext().getClass().getResource("war:///WEB-INF/web.xml").toString());
//        if (file.canRead())
//           System.out.println("success");
//        else
//            System.out.println("failure");
        //URL url = this.getClass().getResource("war:///WEB-INF/web.xml");
        //System.out.println("file: "+ url.toString());
       // is.read();

//        catch (FileNotFoundException e) { retVal = "fileNotFound \n" + e.getMessage();
//                    }
     } catch (Exception e) {
            e.printStackTrace();
            retVal =e.getMessage();
        }

        return retVal;







    }

    //TODO: refactor abobe calll into one that makes call and one that trnasforms
//    private String transformToJSON(String s){
//
//    }
}
