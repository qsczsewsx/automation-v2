package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Smy_dwh_cas_ParentUser")
public class ParentUserEntity {
  private String cusTcbsid;
  private String rmTcbsid;
  private String cusCustodyCode;
  private String rmCustodyCode;
  private String rmUsername;
  private String cusName;
  private String rmName;
  private String rmEmail;
  private String agencyCode;
  private String agencyName;
  private String agencyEmail;
  private String agencyEmail2;
  private int etlCurDate;
  private Timestamp etlRunDateTime;
  private Integer customerId;

  @Id
  @Column(name = "Cus_TCBSID")
  public String getCusTcbsid() {
    return cusTcbsid;
  }

  public void setCusTcbsid(String cusTcbsid) {
    this.cusTcbsid = cusTcbsid;
  }

  @Basic
  @Column(name = "RM_TCBSID")
  public String getRmTcbsid() {
    return rmTcbsid;
  }

  public void setRmTcbsid(String rmTcbsid) {
    this.rmTcbsid = rmTcbsid;
  }

  @Basic
  @Column(name = "Cus_CustodyCode")
  public String getCusCustodyCode() {
    return cusCustodyCode;
  }

  public void setCusCustodyCode(String cusCustodyCode) {
    this.cusCustodyCode = cusCustodyCode;
  }

  @Basic
  @Column(name = "RM_CustodyCode")
  public String getRmCustodyCode() {
    return rmCustodyCode;
  }

  public void setRmCustodyCode(String rmCustodyCode) {
    this.rmCustodyCode = rmCustodyCode;
  }

  @Basic
  @Column(name = "RM_Username")
  public String getRmUsername() {
    return rmUsername;
  }

  public void setRmUsername(String rmUsername) {
    this.rmUsername = rmUsername;
  }

  @Basic
  @Column(name = "Cus_Name")
  public String getCusName() {
    return cusName;
  }

  public void setCusName(String cusName) {
    this.cusName = cusName;
  }

  @Basic
  @Column(name = "RM_Name")
  public String getRmName() {
    return rmName;
  }

  public void setRmName(String rmName) {
    this.rmName = rmName;
  }

  @Basic
  @Column(name = "RM_Email")
  public String getRmEmail() {
    return rmEmail;
  }

  public void setRmEmail(String rmEmail) {
    this.rmEmail = rmEmail;
  }

  @Basic
  @Column(name = "AgencyCode")
  public String getAgencyCode() {
    return agencyCode;
  }

  public void setAgencyCode(String agencyCode) {
    this.agencyCode = agencyCode;
  }

  @Basic
  @Column(name = "AgencyName")
  public String getAgencyName() {
    return agencyName;
  }

  public void setAgencyName(String agencyName) {
    this.agencyName = agencyName;
  }

  @Basic
  @Column(name = "Agency_Email")
  public String getAgencyEmail() {
    return agencyEmail;
  }

  public void setAgencyEmail(String agencyEmail) {
    this.agencyEmail = agencyEmail;
  }

  @Basic
  @Column(name = "Agency_Email2")
  public String getAgencyEmail2() {
    return agencyEmail2;
  }

  public void setAgencyEmail2(String agencyEmail2) {
    this.agencyEmail2 = agencyEmail2;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public int getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(int etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDateTime")
  public Timestamp getEtlRunDateTime() {
    return etlRunDateTime;
  }

  public void setEtlRunDateTime(Timestamp etlRunDateTime) {
    this.etlRunDateTime = etlRunDateTime;
  }

  @Basic
  @Column(name = "CustomerId")
  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParentUserEntity that = (ParentUserEntity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(cusTcbsid, that.cusTcbsid) &&
      Objects.equals(rmTcbsid, that.rmTcbsid) &&
      Objects.equals(cusCustodyCode, that.cusCustodyCode) &&
      Objects.equals(rmCustodyCode, that.rmCustodyCode) &&
      Objects.equals(rmUsername, that.rmUsername) &&
      Objects.equals(cusName, that.cusName) &&
      Objects.equals(rmName, that.rmName) &&
      Objects.equals(rmEmail, that.rmEmail) &&
      Objects.equals(agencyCode, that.agencyCode) &&
      Objects.equals(agencyName, that.agencyName) &&
      Objects.equals(agencyEmail, that.agencyEmail) &&
      Objects.equals(agencyEmail2, that.agencyEmail2) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime) &&
      Objects.equals(customerId, that.customerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cusTcbsid, rmTcbsid, cusCustodyCode, rmCustodyCode, rmUsername, cusName, rmName, rmEmail, agencyCode, agencyName, agencyEmail, agencyEmail2, etlCurDate, etlRunDateTime,
      customerId);
  }

  @Step("insert data")
  public boolean saveParentUser(ParentUserEntity userEntity) {
    try {
      Session session = Dwh.dwhDbConnection.getSession();
      session.clear();
      beginTransaction(session);
      session.save(userEntity);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return true;
  }

  @Step("delete data by key")
  public void deleteByCusName(String cusName) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<ParentUserEntity> query = session.createQuery(
      "DELETE FROM ParentUserEntity i WHERE i.cusName=:cusName"
    );
    query.setParameter("cusName", cusName);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
