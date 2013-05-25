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

package org.socraticgrid.addrbook.service;

import org.socraticgrid.addrbook.dao.AddressItemDao;
import org.socraticgrid.addrbook.model.AddressItem;
import org.socraticgrid.addrbook.model.AddressQueryParams;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cmatser
 */
public class AddrBookService {

    private static Log log = LogFactory.getLog(AddrBookService.class);

    /**
     * Save address record.
     *
     * @param addr Address object to save.
     */
    public void saveAddress(AddressItem addr)
    {
        log.debug("Saving a address");

        if (addr != null)
        {
            if (addr.getAddressId() != null)
            {
                log.debug("Performing an update for address: " + addr.getAddressId());
            }
            else
            {
                log.debug("Performing an insert");
            }

        }

        AddressItemDao dao = new AddressItemDao();
        dao.save(addr);
    }

    /**
     * Delete a address
     *
     * @param addr Address to delete
     * @throws DBServiceException
     */
    public void deleteAdress(AddressItem addr) throws DBServiceException
    {
        log.debug("Deleting a address");
        AddressItemDao dao = new AddressItemDao();

        if (addr == null)
        {
            throw new DBServiceException("Address to delete was null");
        }

        dao.delete(addr);
    }

    /**
     * Retrieve a address by identifier
     *
     * @param addrId Address identifier
     * @return Retrieved address
     */
    public AddressItem getAddress(Long addrid)
    {
        AddressItemDao dao = new AddressItemDao();
        return dao.findById(addrid);
    }

    /**
     * Retrieves all addresses
     *
     * @return All address records
     */
    public List<AddressItem> getAllAddresses()
    {
        AddressItemDao dao = new AddressItemDao();
        return dao.findAll();
    }

    /**
     * Address query
     *
     * @param params Address query parameters
     * @return Query results
     */
    public List<AddressItem> addressQuery(AddressQueryParams params)
    {
        AddressItemDao dao = new AddressItemDao();
        return dao.findAddresses(params);
    }

}
