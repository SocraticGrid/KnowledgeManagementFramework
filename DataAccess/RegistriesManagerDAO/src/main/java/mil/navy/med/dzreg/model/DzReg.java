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
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author kim
 */
@Entity
@Table(name = "DZ_REG")
@NamedQueries({
  @NamedQuery(name = "DzReg.findAll", query = "SELECT d FROM DzReg d"),
  @NamedQuery(name = "DzReg.findByPatid", query = "SELECT d FROM DzReg d WHERE d.dzRegPK.patid = :patid"),
  @NamedQuery(name = "DzReg.findByDiseasetypeId", query = "SELECT d FROM DzReg d WHERE d.dzRegPK.diseasetypeId = :diseasetypeId"),
  @NamedQuery(name = "DzReg.findByRegisteredDt", query = "SELECT d FROM DzReg d WHERE d.registeredDt = :registeredDt"),
  @NamedQuery(name = "DzReg.findByActive", query = "SELECT d FROM DzReg d WHERE d.active = :active")
//  @NamedQuery(name = "DzReg.findByCmdFlagDt", query = "SELECT d FROM DzReg d WHERE d.cmdFlagDt = :cmdFlagDt"),
//  @NamedQuery(name = "DzReg.findByInsertedDt", query = "SELECT d FROM DzReg d WHERE d.insertedDt = :insertedDt"),
//  @NamedQuery(name = "DzReg.findByDataSource", query = "SELECT d FROM DzReg d WHERE d.dataSource = :dataSource"),
//  @NamedQuery(name = "DzReg.findByHedis", query = "SELECT d FROM DzReg d WHERE d.hedis = :hedis"),
//  @NamedQuery(name = "DzReg.findByCycle1Scenario", query = "SELECT d FROM DzReg d WHERE d.cycle1Scenario = :cycle1Scenario"),
//  @NamedQuery(name = "DzReg.findByFlag", query = "SELECT d FROM DzReg d WHERE d.flag = :flag")
})
public class DzReg implements Serializable {
  private static final long serialVersionUID = 1L;
  @EmbeddedId
  protected DzRegPK dzRegPK;
  @Column(name = "REGISTERED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp registeredDt;
  @Basic(optional = false)
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "CMD_FLAG_DT")
  @Temporal(TemporalType.DATE)
  private Date cmdFlagDt;
  @Column(name = "INSERTED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp insertedDt;
  @Basic(optional = false)
  @Column(name = "DATA_SOURCE")
  private String dataSource;
  @Column(name = "HEDIS")
  private Integer hedis;
  @Column(name = "CYCLE1_SCENARIO")
  private Integer cycle1Scenario;
  @Column(name = "FLAG")
  private Integer flag;
  @JoinColumn(name = "PATID", referencedColumnName = "PATID", insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private DzPatients dzPatients;
  @JoinColumn(name = "DISEASETYPE_ID", referencedColumnName = "DZTYPE_ID", insertable = false, updatable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private DzType dzType;

  public DzReg() {
  }

  public DzReg(DzRegPK dzRegPK) {
    this.dzRegPK = dzRegPK;
  }

  public DzReg(DzRegPK dzRegPK, Integer active, String dataSource) {
    this.dzRegPK = dzRegPK;
    this.active = active;
    this.dataSource = dataSource;
  }

  public DzReg(Long patid, Integer diseasetypeId) {
    this.dzRegPK = new DzRegPK(patid, diseasetypeId);
  }

  public DzRegPK getDzRegPK() {
    return dzRegPK;
  }

  public void setDzRegPK(DzRegPK dzRegPK) {
    this.dzRegPK = dzRegPK;
  }

  public Timestamp getRegisteredDt() {
    return registeredDt;
  }

  public void setRegisteredDt(Timestamp registeredDt) {
    this.registeredDt = registeredDt;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Date getCmdFlagDt() {
    return cmdFlagDt;
  }

  public void setCmdFlagDt(Date cmdFlagDt) {
    this.cmdFlagDt = cmdFlagDt;
  }

  public Timestamp getInsertedDt() {
    return insertedDt;
  }

  public void setInsertedDt(Timestamp insertedDt) {
    this.insertedDt = insertedDt;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public Integer getHedis() {
    return hedis;
  }

  public void setHedis(Integer hedis) {
    this.hedis = hedis;
  }

  public Integer getCycle1Scenario() {
    return cycle1Scenario;
  }

  public void setCycle1Scenario(Integer cycle1Scenario) {
    this.cycle1Scenario = cycle1Scenario;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

  public DzPatients getDzPatients() {
    return dzPatients;
  }

  public void setDzPatients(DzPatients dzPatients) {
    this.dzPatients = dzPatients;
  }

  public DzType getDzType() {
    return dzType;
  }

  public void setDzType(DzType dzType) {
    this.dzType = dzType;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (dzRegPK != null ? dzRegPK.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof DzReg)) {
      return false;
    }
    DzReg other = (DzReg) object;
    if ((this.dzRegPK == null && other.dzRegPK != null) || (this.dzRegPK != null && !this.dzRegPK.equals(other.dzRegPK))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DzReg[dzRegPK=" + dzRegPK + "]";
  }

}
