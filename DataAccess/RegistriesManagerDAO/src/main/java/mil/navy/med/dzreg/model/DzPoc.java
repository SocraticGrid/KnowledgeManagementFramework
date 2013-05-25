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

package mil.navy.med.dzreg.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "DZ_POC")
@NamedQueries({
  @NamedQuery(name = "DzPoc.findAll", query = "SELECT d FROM DzPoc d"),
  @NamedQuery(name = "DzPoc.findByPocid", query = "SELECT d FROM DzPoc d WHERE d.pocid = :pocid"),
  @NamedQuery(name = "DzPoc.findByPoc", query = "SELECT d FROM DzPoc d WHERE d.poc = :poc")
//  @NamedQuery(name = "DzPoc.findByInsertedDt", query = "SELECT d FROM DzPoc d WHERE d.insertedDt = :insertedDt"),
//  @NamedQuery(name = "DzPoc.findByDataSource", query = "SELECT d FROM DzPoc d WHERE d.dataSource = :dataSource"),
//  @NamedQuery(name = "DzPoc.findByHospitalLocationInactiveDt", query = "SELECT d FROM DzPoc d WHERE d.hospitalLocationInactiveDt = :hospitalLocationInactiveDt"),
//  @NamedQuery(name = "DzPoc.findByDivisionId", query = "SELECT d FROM DzPoc d WHERE d.divisionId = :divisionId"),
//  @NamedQuery(name = "DzPoc.findByDivisionDmis", query = "SELECT d FROM DzPoc d WHERE d.divisionDmis = :divisionDmis"),
//  @NamedQuery(name = "DzPoc.findByFlag", query = "SELECT d FROM DzPoc d WHERE d.flag = :flag")
})
public class DzPoc implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "POCID")
  private Long pocid;
  @Column(name = "POC")
  private String poc;
  @Column(name = "INSERTED_DT")
  @Temporal(TemporalType.DATE)
  private Date insertedDt;
  @Basic(optional = false)
  @Column(name = "DATA_SOURCE")
  private String dataSource;
  @Column(name = "HOSPITAL_LOCATION_INACTIVE_DT")
  @Temporal(TemporalType.DATE)
  private Date hospitalLocationInactiveDt;
  @Column(name = "DIVISION_ID")
  private Integer divisionId;
  @Column(name = "DIVISION_DMIS")
  private String divisionDmis;
  @Column(name = "FLAG")
  private Integer flag;
  //@OneToMany(mappedBy = "pocid", fetch = FetchType.EAGER)
  //private Collection<DzAppt> dzApptCollection;
  //@OneToMany(mappedBy = "pocid", fetch = FetchType.EAGER)
  //private Collection<DzPatients> dzPatientsCollection;

  public DzPoc() {
  }

  public DzPoc(Long pocid) {
    this.pocid = pocid;
  }

  public DzPoc(Long pocid, String dataSource) {
    this.pocid = pocid;
    this.dataSource = dataSource;
  }

  public Long getPocid() {
    return pocid;
  }

  public void setPocid(Long pocid) {
    this.pocid = pocid;
  }

  public String getPoc() {
    return poc;
  }

  public void setPoc(String poc) {
    this.poc = poc;
  }

  public Date getInsertedDt() {
    return insertedDt;
  }

  public void setInsertedDt(Date insertedDt) {
    this.insertedDt = insertedDt;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public Date getHospitalLocationInactiveDt() {
    return hospitalLocationInactiveDt;
  }

  public void setHospitalLocationInactiveDt(Date hospitalLocationInactiveDt) {
    this.hospitalLocationInactiveDt = hospitalLocationInactiveDt;
  }

  public Integer getDivisionId() {
    return divisionId;
  }

  public void setDivisionId(Integer divisionId) {
    this.divisionId = divisionId;
  }

  public String getDivisionDmis() {
    return divisionDmis;
  }

  public void setDivisionDmis(String divisionDmis) {
    this.divisionDmis = divisionDmis;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

//  public Collection<DzAppt> getDzApptCollection() {
//    return dzApptCollection;
//  }
//
//  public void setDzApptCollection(Collection<DzAppt> dzApptCollection) {
//    this.dzApptCollection = dzApptCollection;
//  }

//  public Collection<DzPatients> getDzPatientsCollection() {
//    return dzPatientsCollection;
//  }
//
//  public void setDzPatientsCollection(Collection<DzPatients> dzPatientsCollection) {
//    this.dzPatientsCollection = dzPatientsCollection;
//  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (pocid != null ? pocid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof DzPoc)) {
      return false;
    }
    DzPoc other = (DzPoc) object;
    if ((this.pocid == null && other.pocid != null) || (this.pocid != null && !this.pocid.equals(other.pocid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "mil.navy.med.dzreg.jpa.DzPoc[pocid=" + pocid + "]";
  }

}
