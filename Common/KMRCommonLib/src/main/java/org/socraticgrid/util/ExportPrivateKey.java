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
 * Utility to export a private key from the Java Keystore for a given
 * alias.
 */

package org.socraticgrid.util;

import org.socraticgrid.properties.PropertyAccessException;
import org.socraticgrid.properties.PropertyAccessor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.util.logging.Level;

import sun.misc.BASE64Encoder;

/**
 * ExportPrivateKey class allows the exporting of private keys from the Java
 * keystore, keystore.jks. When new AES encryption keys are created, this can
 * be used to obtain the exported.key file containing the private key.
 * 
 * @author jharby
 */

public class ExportPrivateKey {
       private File keystoreFile;
       private String keyStoreType;
       private char[] password;
       private String alias;
       private File exportedFile;
       private File publicKeyFile;
       private static String PROPERTY_FILE = "KMRCommonUtil";

       public static KeyPair getPrivateKey(KeyStore keystore, String alias, char[] password) {
               try {
                       Key key=keystore.getKey(alias,password);
                       if(key instanceof PrivateKey) {
                               Certificate cert=keystore.getCertificate(alias);
                               PublicKey publicKey=cert.getPublicKey();
                               return new KeyPair(publicKey,(PrivateKey)key);
                       }
               } 
               catch (UnrecoverableKeyException e) {
                   e.printStackTrace();
               }
               catch (NoSuchAlgorithmException e) {
                   e.printStackTrace();
               }
               catch (KeyStoreException e) {
                   e.printStackTrace();
               }
               return null;
       }

       public void export() throws Exception{
               KeyStore keystore=KeyStore.getInstance(keyStoreType);
               BASE64Encoder encoder=new BASE64Encoder();
               keystore.load(new FileInputStream(keystoreFile),password);
               KeyPair keyPair=getPrivateKey(keystore,alias,password);
               PrivateKey privateKey=keyPair.getPrivate();
               String encoded=encoder.encode(privateKey.getEncoded());
               FileWriter fw=new FileWriter(exportedFile);
               fw.write("---BEGIN PRIVATE KEY---\n");
               fw.write(encoded);
               fw.write("\n");
               fw.write("---END PRIVATE KEY---");
               fw.close();
       }

        private void setFiles() {
        try {
            String kmrDir = System.getProperty("KMR_PROPERTIES_DIR");
            String privFile = PropertyAccessor.getProperty(PROPERTY_FILE, "privateKeyFile");
            String pubFile = PropertyAccessor.getProperty(PROPERTY_FILE, "publicKeyFile");
            String keyFile = PropertyAccessor.getProperty(PROPERTY_FILE, "keystoreFile");
            exportedFile = new File(kmrDir + "/" + privFile);
            publicKeyFile = new File(kmrDir + "/" + pubFile);
            keystoreFile = new File(kmrDir + "/" + keyFile);
        } catch (PropertyAccessException pae) {
            pae.printStackTrace();
        }
    }

       public static void main(String args[]) throws Exception {
               String keystoreFile = "/home/nhin/keystore.jks";
               String keystoreType = "JKS";
               String password = "nhinpass";
               String alias = "kmr2key";
               String exportedFile = "/home/nhin/exported.key";
               ExportPrivateKey export=new ExportPrivateKey();
               export.keystoreFile=new File(keystoreFile);
               export.keyStoreType=keystoreType;
               export.password=password.toCharArray();
               export.alias=alias;
               export.exportedFile=new File(exportedFile);
               export.export();
       }
}
