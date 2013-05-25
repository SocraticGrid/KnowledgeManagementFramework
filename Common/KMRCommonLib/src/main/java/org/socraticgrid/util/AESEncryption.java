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

package org.socraticgrid.util;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.xml.bind.DatatypeConverter;

/**
 * This class provides an AES encryption. The aesEncrypt method takes a String
 * and returns a byte array of the AES encrypted value of the String. The
 * aesDecrypt method takes a byte array as input and decrypts that byte array
 * then returns the original String.
 * 
 */
public class AESEncryption {

    Logger logger = Logger.getLogger("org.socraticgrid.util.AESEncryption");

    /**
     * Turns bytes of bytes into hex string
     *
     * @param bytes	Array of bytes to convert to hex string
     * @return	Generated hex string
     */    
    public static String toHexString(byte[] bytes) {
        return DatatypeConverter.printHexBinary(bytes);
    }

    /**
     * Turns hex string into array of bytes
     *
     * @param s	hex string to be converted
     * @return	Generated byte array
     */
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }


    /**
     * Encrypts the input string using the AES algorithm. The key is obtained
     * from the persistent Java keystore.
     *
     * @param the string to encrypt
     * 
     * @return byte array containing the encrypted string
     *
     * @throws IOException
     * @throws GeneralSecurityException
     *
     */
    public byte[] aesEncrypt(String toEncrypt) throws IOException, GeneralSecurityException {
        SecretKeySpec skeySpec = getKeySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] toEncryptBytes = toEncrypt.getBytes("UTF-8");
        byte[] encrypted = cipher.doFinal(toEncryptBytes);
        logger.log(Level.INFO, "aesEncrypt - encrypted string: " + encrypted);
        return encrypted;
    }

    /**
     * Decrypts the input byte array using the AES algorithm. The key is obtained
     * from the persistent Java keystore.
     *
     * @param the byte array to decreypt
     *
     * @return the decrypted original string 
     *
     * @throws IOException
     * @throws GeneralSecurityException
     *
     */
    public String aesDecrypt(byte[] toDecrypt) throws IOException, GeneralSecurityException {
        SecretKeySpec skeySpec = getKeySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, cipher.getParameters());
        byte[] original = cipher.doFinal(toDecrypt);
        String decrypted = new String(original, "UTF-8");
        logger.log(Level.INFO, "aesDecrypt - decrypted string: " + decrypted);
        return new String(original, "UTF-8");
    }

    /**
     * Retrieves and loads the key spec used for encryption and decryption from the
     * Java keystore using the KMRKeyStore class.
     *
     * @return the SecretKeySpec from KMRKeyStore
     *
     * @throws IOException
     * @throws GeneralSecurityException
     *
     */
    private SecretKeySpec getKeySpec() throws GeneralSecurityException, IOException {
        KMRKeyStore kmrKeyStore = new KMRKeyStore();
        kmrKeyStore.loadKey();
        return kmrKeyStore.getKeySpec();
    }
}
