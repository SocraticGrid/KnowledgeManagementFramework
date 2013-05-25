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
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "DZ_PATIENTS")
@NamedQueries({
  @NamedQuery(name = "DzPatients.findAll", query = "SELECT d FROM DzPatients d"),
  @NamedQuery(name = "DzPatients.findByPatid", query = "SELECT d FROM DzPatients d WHERE d.patid = :patid"),
  @NamedQuery(name = "DzPatients.findByFmpssn", query = "SELECT d FROM DzPatients d WHERE d.fmpssn = :fmpssn"),
  @NamedQuery(name = "DzPatients.findByName", query = "SELECT d FROM DzPatients d WHERE d.name = :name"),
  @NamedQuery(name = "DzPatients.findByDob", query = "SELECT d FROM DzPatients d WHERE d.dob = :dob")
//  @NamedQuery(name = "DzPatients.findByAcv", query = "SELECT d FROM DzPatients d WHERE d.acv = :acv"),
//  @NamedQuery(name = "DzPatients.findByDmis", query = "SELECT d FROM DzPatients d WHERE d.dmis = :dmis"),
//  @NamedQuery(name = "DzPatients.findByUpdated", query = "SELECT d FROM DzPatients d WHERE d.updated = :updated"),
//  @NamedQuery(name = "DzPatients.findByPhone", query = "SELECT d FROM DzPatients d WHERE d.phone = :phone"),
//  @NamedQuery(name = "DzPatients.findByAddress", query = "SELECT d FROM DzPatients d WHERE d.address = :address"),
//  @NamedQuery(name = "DzPatients.findByCity", query = "SELECT d FROM DzPatients d WHERE d.city = :city"),
//  @NamedQuery(name = "DzPatients.findByState", query = "SELECT d FROM DzPatients d WHERE d.state = :state"),
//  @NamedQuery(name = "DzPatients.findByZip", query = "SELECT d FROM DzPatients d WHERE d.zip = :zip"),
//  @NamedQuery(name = "DzPatients.findByEnrolled", query = "SELECT d FROM DzPatients d WHERE d.enrolled = :enrolled"),
//  @NamedQuery(name = "DzPatients.findByEndEnrolledDt", query = "SELECT d FROM DzPatients d WHERE d.endEnrolledDt = :endEnrolledDt"),
//  @NamedQuery(name = "DzPatients.findByInactiveDt", query = "SELECT d FROM DzPatients d WHERE d.inactiveDt = :inactiveDt"),
//  @NamedQuery(name = "DzPatients.findByDeceasedDt", query = "SELECT d FROM DzPatients d WHERE d.deceasedDt = :deceasedDt"),
//  @NamedQuery(name = "DzPatients.findByLastApptDt", query = "SELECT d FROM DzPatients d WHERE d.lastApptDt = :lastApptDt"),
//  @NamedQuery(name = "DzPatients.findByPrevDmis", query = "SELECT d FROM DzPatients d WHERE d.prevDmis = :prevDmis"),
//  @NamedQuery(name = "DzPatients.findByPrevPcmid", query = "SELECT d FROM DzPatients d WHERE d.prevPcmid = :prevPcmid"),
//  @NamedQuery(name = "DzPatients.findByPrevPocid", query = "SELECT d FROM DzPatients d WHERE d.prevPocid = :prevPocid"),
//  @NamedQuery(name = "DzPatients.findByPrevEnrolled", query = "SELECT d FROM DzPatients d WHERE d.prevEnrolled = :prevEnrolled"),
//  @NamedQuery(name = "DzPatients.findByInsertedDt", query = "SELECT d FROM DzPatients d WHERE d.insertedDt = :insertedDt"),
//  @NamedQuery(name = "DzPatients.findByPrevEnrolledDt", query = "SELECT d FROM DzPatients d WHERE d.prevEnrolledDt = :prevEnrolledDt"),
//  @NamedQuery(name = "DzPatients.findByDataSource", query = "SELECT d FROM DzPatients d WHERE d.dataSource = :dataSource"),
//  @NamedQuery(name = "DzPatients.findByPatientDeersIdentifier", query = "SELECT d FROM DzPatients d WHERE d.patientDeersIdentifier = :patientDeersIdentifier"),
//  @NamedQuery(name = "DzPatients.findByOfficePhone", query = "SELECT d FROM DzPatients d WHERE d.officePhone = :officePhone"),
//  @NamedQuery(name = "DzPatients.findByStreet2", query = "SELECT d FROM DzPatients d WHERE d.street2 = :street2"),
//  @NamedQuery(name = "DzPatients.findByStartEnrolledDt", query = "SELECT d FROM DzPatients d WHERE d.startEnrolledDt = :startEnrolledDt"),
//  @NamedQuery(name = "DzPatients.findByUpdatedDt", query = "SELECT d FROM DzPatients d WHERE d.updatedDt = :updatedDt"),
//  @NamedQuery(name = "DzPatients.findByPatientDob", query = "SELECT d FROM DzPatients d WHERE d.patientDob = :patientDob"),
//  @NamedQuery(name = "DzPatients.findByPatientCategoryStatus", query = "SELECT d FROM DzPatients d WHERE d.patientCategoryStatus = :patientCategoryStatus"),
//  @NamedQuery(name = "DzPatients.findByNedHcdpCode", query = "SELECT d FROM DzPatients d WHERE d.nedHcdpCode = :nedHcdpCode"),
//  @NamedQuery(name = "DzPatients.findByNedHcdpText", query = "SELECT d FROM DzPatients d WHERE d.nedHcdpText = :nedHcdpText"),
//  @NamedQuery(name = "DzPatients.findByUnitShipId", query = "SELECT d FROM DzPatients d WHERE d.unitShipId = :unitShipId"),
//  @NamedQuery(name = "DzPatients.findByUnitShipName", query = "SELECT d FROM DzPatients d WHERE d.unitShipName = :unitShipName"),
//  @NamedQuery(name = "DzPatients.findByMcp", query = "SELECT d FROM DzPatients d WHERE d.mcp = :mcp"),
//  @NamedQuery(name = "DzPatients.findByMcpid", query = "SELECT d FROM DzPatients d WHERE d.mcpid = :mcpid"),
//  @NamedQuery(name = "DzPatients.findByFlag", query = "SELECT d FROM DzPatients d WHERE d.flag = :flag")
})
public class DzPatients implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id
  @Basic(optional = false)
  @Column(name = "PATID")
  private Long patid;
  @Basic(optional = false)
  @Column(name = "FMPSSN")
  private String fmpssn;
  @Column(name = "NAME")
  private String name;
  @Column(name = "DOB")
  private String dob;
  @Column(name = "ACV")
  private String acv;
  @Column(name = "DMIS")
  private String dmis;
  @Column(name = "UPDATED")
  private String updated;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "CITY")
  private String city;
  @Column(name = "STATE")
  private String state;
  @Column(name = "ZIP")
  private String zip;
  @Column(name = "ENROLLED")
  private Character enrolled;
  @Column(name = "END_ENROLLED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp endEnrolledDt;
  @Column(name = "INACTIVE_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp inactiveDt;
  @Column(name = "DECEASED_DT")
  @Temporal(TemporalType.DATE)
  private Date deceasedDt;
  @Column(name = "LAST_APPT_DT")
  @Temporal(TemporalType.DATE)
  private Date lastApptDt;
  @Column(name = "PREV_DMIS")
  private String prevDmis;
  @Column(name = "PREV_PCMID")
  private Long prevPcmid;
  @Column(name = "PREV_POCID")
  private Long prevPocid;
  @Column(name = "PREV_ENROLLED")
  private Character prevEnrolled;
  @Column(name = "INSERTED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp insertedDt;
  @Column(name = "PREV_ENROLLED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp prevEnrolledDt;
  @Basic(optional = false)
  @Column(name = "DATA_SOURCE")
  private String dataSource;
  @Column(name = "PATIENT_DEERS_IDENTIFIER")
  private Long patientDeersIdentifier;
  @Column(name = "OFFICE_PHONE")
  private String officePhone;
  @Column(name = "STREET2")
  private String street2;
  @Column(name = "START_ENROLLED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp startEnrolledDt;
  @Column(name = "UPDATED_DT")
  //@Temporal(TemporalType.DATE)
  private Timestamp updatedDt;
  @Column(name = "PATIENT_DOB")
  //@Temporal(TemporalType.DATE)
  private Timestamp patientDob;
  @Column(name = "PATIENT_CATEGORY_STATUS")
  private String patientCategoryStatus;
  @Column(name = "NED_HCDP_CODE")
  private String nedHcdpCode;
  @Column(name = "NED_HCDP_TEXT")
  private String nedHcdpText;
  @Column(name = "UNIT_SHIP_ID")
  private Integer unitShipId;
  @Column(name = "UNIT_SHIP_NAME")
  private String unitShipName;
  @Column(name = "MCP")
  private String mcp;
  @Column(name = "MCPID")
  private Long mcpid;
  @Column(name = "FLAG")
  private Integer flag;
  //@OneToOne(cascade = CascadeType.ALL, mappedBy = "dzPatients", fetch = FetchType.LAZY)
  //private DzAppt dzAppt;
  //@JoinColumn(name = "PCMID", referencedColumnName = "PCMID")
  //@ManyToOne(fetch = FetchType.LAZY)
  //private DzPcm pcmid;
  private Long pcmid;
  //@JoinColumn(name = "POCID", referencedColumnName = "POCID")
  //@ManyToOne(fetch = FetchType.LAZY)
  //private DzPoc pocid;
  private Long pocid;
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "dzPatients", fetch = FetchType.LAZY)
  private Collection<DzReg> dzRegCollection;

  public DzPatients() {
  }

  public DzPatients(Long patid) {
    this.patid = patid;
  }

  public DzPatients(Long patid, String fmpssn, String dataSource) {
    this.patid = patid;
    this.fmpssn = fmpssn;
    this.dataSource = dataSource;
  }

  public Long getPatid() {
    return patid;
  }

  public void setPatid(Long patid) {
    this.patid = patid;
  }

  public String getFmpssn() {
    return fmpssn;
  }

  public void setFmpssn(String fmpssn) {
    this.fmpssn = fmpssn;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
  }

  public String getAcv() {
    return acv;
  }

  public void setAcv(String acv) {
    this.acv = acv;
  }

  public String getDmis() {
    return dmis;
  }

  public void setDmis(String dmis) {
    this.dmis = dmis;
  }

  public String getUpdated() {
    return updated;
  }

  public void setUpdated(String updated) {
    this.updated = updated;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public Character getEnrolled() {
    return enrolled;
  }

  public void setEnrolled(Character enrolled) {
    this.enrolled = enrolled;
  }

  public Timestamp getEndEnrolledDt() {
    return endEnrolledDt;
  }

  public void setEndEnrolledDt(Timestamp endEnrolledDt) {
    this.endEnrolledDt = endEnrolledDt;
  }

  public Timestamp getInactiveDt() {
    return inactiveDt;
  }

  public void setInactiveDt(Timestamp inactiveDt) {
    this.inactiveDt = inactiveDt;
  }

  public Date getDeceasedDt() {
    return deceasedDt;
  }

  public void setDeceasedDt(Date deceasedDt) {
    this.deceasedDt = deceasedDt;
  }

  public Date getLastApptDt() {
    return lastApptDt;
  }

  public void setLastApptDt(Date lastApptDt) {
    this.lastApptDt = lastApptDt;
  }

  public String getPrevDmis() {
    return prevDmis;
  }

  public void setPrevDmis(String prevDmis) {
    this.prevDmis = prevDmis;
  }

  public Long getPrevPcmid() {
    return prevPcmid;
  }

  public void setPrevPcmid(Long prevPcmid) {
    this.prevPcmid = prevPcmid;
  }

  public Long getPrevPocid() {
    return prevPocid;
  }

  public void setPrevPocid(Long prevPocid) {
    this.prevPocid = prevPocid;
  }

  public Character getPrevEnrolled() {
    return prevEnrolled;
  }

  public void setPrevEnrolled(Character prevEnrolled) {
    this.prevEnrolled = prevEnrolled;
  }

  public Timestamp getInsertedDt() {
    return insertedDt;
  }

  public void setInsertedDt(Timestamp insertedDt) {
    this.insertedDt = insertedDt;
  }

  public Timestamp getPrevEnrolledDt() {
    return prevEnrolledDt;
  }

  public void setPrevEnrolledDt(Timestamp prevEnrolledDt) {
    this.prevEnrolledDt = prevEnrolledDt;
  }

  public String getDataSource() {
    return dataSource;
  }

  public void setDataSource(String dataSource) {
    this.dataSource = dataSource;
  }

  public Long getPatientDeersIdentifier() {
    return patientDeersIdentifier;
  }

  public void setPatientDeersIdentifier(Long patientDeersIdentifier) {
    this.patientDeersIdentifier = patientDeersIdentifier;
  }

  public String getOfficePhone() {
    return officePhone;
  }

  public void setOfficePhone(String officePhone) {
    this.officePhone = officePhone;
  }

  public String getStreet2() {
    return street2;
  }

  public void setStreet2(String street2) {
    this.street2 = street2;
  }

  public Timestamp getStartEnrolledDt() {
    return startEnrolledDt;
  }

  public void setStartEnrolledDt(Timestamp startEnrolledDt) {
    this.startEnrolledDt = startEnrolledDt;
  }

  public Timestamp getUpdatedDt() {
    return updatedDt;
  }

  public void setUpdatedDt(Timestamp updatedDt) {
    this.updatedDt = updatedDt;
  }

  public Timestamp getPatientDob() {
    return patientDob;
  }

  public void setPatientDob(Timestamp patientDob) {
    this.patientDob = patientDob;
  }

  public String getPatientCategoryStatus() {
    return patientCategoryStatus;
  }

  public void setPatientCategoryStatus(String patientCategoryStatus) {
    this.patientCategoryStatus = patientCategoryStatus;
  }

  public String getNedHcdpCode() {
    return nedHcdpCode;
  }

  public void setNedHcdpCode(String nedHcdpCode) {
    this.nedHcdpCode = nedHcdpCode;
  }

  public String getNedHcdpText() {
    return nedHcdpText;
  }

  public void setNedHcdpText(String nedHcdpText) {
    this.nedHcdpText = nedHcdpText;
  }

  public Integer getUnitShipId() {
    return unitShipId;
  }

  public void setUnitShipId(Integer unitShipId) {
    this.unitShipId = unitShipId;
  }

  public String getUnitShipName() {
    return unitShipName;
  }

  public void setUnitShipName(String unitShipName) {
    this.unitShipName = unitShipName;
  }

  public String getMcp() {
    return mcp;
  }

  public void setMcp(String mcp) {
    this.mcp = mcp;
  }

  public Long getMcpid() {
    return mcpid;
  }

  public void setMcpid(Long mcpid) {
    this.mcpid = mcpid;
  }

  public Integer getFlag() {
    return flag;
  }

  public void setFlag(Integer flag) {
    this.flag = flag;
  }

//  public DzPcm getPcmid() {
//    return pcmid;
//  }
//
//  public void setPcmid(DzPcm pcmid) {
//    this.pcmid = pcmid;
//  }

  public Long getPcmid() {
    return pcmid;
  }

  public void setPcmid(Long pcmid) {
    this.pcmid = pcmid;
  }

//  public DzPoc getPocid() {
//    return pocid;
//  }
//
//  public void setPocid(DzPoc pocid) {
//    this.pocid = pocid;
//  }

  public Long getPocid() {
    return pocid;
  }

  public void setPocid(Long pocid) {
    this.pocid = pocid;
  }

  public Collection<DzReg> getDzRegCollection() {
    return dzRegCollection;
  }

  public void setDzRegCollection(Collection<DzReg> dzRegCollection) {
    this.dzRegCollection = dzRegCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (patid != null ? patid.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof DzPatients)) {
      return false;
    }
    DzPatients other = (DzPatients) object;
    if ((this.patid == null && other.patid != null) || (this.patid != null && !this.patid.equals(other.patid))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "DzPatients[patid=" + patid + "]";
  }
}
