package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SMY_DWH_CAS_PARENTUSER")
public class SmyDwhCasParentuserEntity {
  @Id
  private String cusTcbsid;
  private String rmTcbsid;
  private String cusCustodycode;
  private String rmCustodycode;
  private String rmUsername;
  private String cusName;
  private String rmName;
  private String rmEmail;
  private String agencycode;
  private String agencyname;
  private String agencyEmail;
  private String agencyEmail2;
  private Long etlcurdate;
  private Time etlrundatetime;
  private Long customerid;
  private String permission;
  private String zone;
  private String rmCode;
  private String branchCus;
  private String aumVip;
  private String customercode;

  @Step("get data from db")
  public static List<HashMap<String, Object>> byCondition(String parentCustody) {
    StringBuilder query = new StringBuilder();

    query.append(" SELECT pu.CUS_CUSTODYCODE AS custody, pu.CUS_TCBSID AS tcbsId, pu.CUS_NAME customerName  ");
    query.append(" FROM SMY_DWH_CAS_PARENTUSER pu   ");
    query.append(" WHERE pu.ETLCURDATE = (select max(ETLCURDATE) from SMY_DWH_CAS_PARENTUSER)  ");
    query.append(" AND pu.RM_TCBSID is not null and pu.CUS_CUSTODYCODE is not null and RM_CUSTODYCODE = :parentCustody  ");

    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("parentCustody", parentCustody)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

  }


  @Step("Insert data")
  public static void insertData(SmyDwhCasParentuserEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("INSERT INTO SMY_DWH_CAS_PARENTUSER");
    queryStringBuilder.append("(CUS_TCBSID,RM_TCBSID,CUS_CUSTODYCODE,RM_CUSTODYCODE, ETLCURDATE)");
    queryStringBuilder.append((" VALUES( "));
    queryStringBuilder.append("'" + entity.getCusTcbsid() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getRmTcbsid() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getCusCustodycode() + "'" + ", ");
    queryStringBuilder.append("'" + entity.getRmCustodycode() + "'" + ", ");
    queryStringBuilder.append("(select MAX(ETLCURDATE) FROM SMY_DWH_CAS_PARENTUSER)) ");
    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.executeUpdate();
    trans.commit();
  }

  @Step("Delete data by key")
  public static void deleteData(SmyDwhCasParentuserEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;
    query = session.createNativeQuery("DELETE FROM SMY_DWH_CAS_PARENTUSER WHERE RM_CUSTODYCODE = :rmCustodyCode");
    query.setParameter("rmCustodyCode", entity.getRmCustodycode());
    query.executeUpdate();
    trans.commit();

  }

  @Step("get Cus_CustodyCode data from db")
  public static List<HashMap<String, Object>> getCusCustodyCode(String rmCustodycode) {
    StringBuilder query = new StringBuilder();

    query.append(" select distinct CUS_TCBSID, CUS_CUSTODYCODE, b.FULLNAME, b.IDNUMBER, TO_CHAR(b.BIRTHDAY, 'YYYY-MM-DD') BIRTHDAY, b.CUSTYPE");
    query.append(" FROM SMY_DWH_CAS_PARENTUSER a ");
    query.append(" LEFT JOIN SMY_DWH_CAS_ALLUSERVIEW b ON a.CUS_TCBSID = b.TCBSID");
    query.append(" WHERE a.ETLCURDATE = (select max(ETLCURDATE) from SMY_DWH_CAS_PARENTUSER)");
    query.append(" AND b.ETLCURDATE = (select max(ETLCURDATE) from SMY_DWH_CAS_ALLUSERVIEW)");
    query.append(" AND RM_CUSTODYCODE = :rmCustodycode ");
    query.append(" order by CUS_TCBSID");

    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("rmCustodycode", rmCustodycode)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

  }

  @Basic
  @Column(name = "CUS_TCBSID")
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
  @Column(name = "CUS_CUSTODYCODE")
  public String getCusCustodycode() {
    return cusCustodycode;
  }

  public void setCusCustodycode(String cusCustodycode) {
    this.cusCustodycode = cusCustodycode;
  }

  @Basic
  @Column(name = "RM_CUSTODYCODE")
  public String getRmCustodycode() {
    return rmCustodycode;
  }

  public void setRmCustodycode(String rmCustodycode) {
    this.rmCustodycode = rmCustodycode;
  }

  @Basic
  @Column(name = "RM_USERNAME")
  public String getRmUsername() {
    return rmUsername;
  }

  public void setRmUsername(String rmUsername) {
    this.rmUsername = rmUsername;
  }

  @Basic
  @Column(name = "CUS_NAME")
  public String getCusName() {
    return cusName;
  }

  public void setCusName(String cusName) {
    this.cusName = cusName;
  }

  @Basic
  @Column(name = "RM_NAME")
  public String getRmName() {
    return rmName;
  }

  public void setRmName(String rmName) {
    this.rmName = rmName;
  }

  @Basic
  @Column(name = "RM_EMAIL")
  public String getRmEmail() {
    return rmEmail;
  }

  public void setRmEmail(String rmEmail) {
    this.rmEmail = rmEmail;
  }

  @Basic
  @Column(name = "AGENCYCODE")
  public String getAgencycode() {
    return agencycode;
  }

  public void setAgencycode(String agencycode) {
    this.agencycode = agencycode;
  }

  @Basic
  @Column(name = "AGENCYNAME")
  public String getAgencyname() {
    return agencyname;
  }

  public void setAgencyname(String agencyname) {
    this.agencyname = agencyname;
  }

  @Basic
  @Column(name = "AGENCY_EMAIL")
  public String getAgencyEmail() {
    return agencyEmail;
  }

  public void setAgencyEmail(String agencyEmail) {
    this.agencyEmail = agencyEmail;
  }

  @Basic
  @Column(name = "AGENCY_EMAIL2")
  public String getAgencyEmail2() {
    return agencyEmail2;
  }

  public void setAgencyEmail2(String agencyEmail2) {
    this.agencyEmail2 = agencyEmail2;
  }

  @Basic
  @Column(name = "ETLCURDATE")
  public Long getEtlcurdate() {
    return etlcurdate;
  }

  public void setEtlcurdate(Long etlcurdate) {
    this.etlcurdate = etlcurdate;
  }

  @Basic
  @Column(name = "ETLRUNDATETIME")
  public Time getEtlrundatetime() {
    return etlrundatetime;
  }

  public void setEtlrundatetime(Time etlrundatetime) {
    this.etlrundatetime = etlrundatetime;
  }

  @Basic
  @Column(name = "CUSTOMERID")
  public Long getCustomerid() {
    return customerid;
  }

  public void setCustomerid(Long customerid) {
    this.customerid = customerid;
  }

  @Basic
  @Column(name = "PERMISSION")
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  @Basic
  @Column(name = "ZONE")
  public String getZone() {
    return zone;
  }

  public void setZone(String zone) {
    this.zone = zone;
  }

  @Basic
  @Column(name = "RM_CODE")
  public String getRmCode() {
    return rmCode;
  }

  public void setRmCode(String rmCode) {
    this.rmCode = rmCode;
  }

  @Basic
  @Column(name = "BRANCH_CUS")
  public String getBranchCus() {
    return branchCus;
  }

  public void setBranchCus(String branchCus) {
    this.branchCus = branchCus;
  }

  @Basic
  @Column(name = "AUM_VIP")
  public String getAumVip() {
    return aumVip;
  }

  public void setAumVip(String aumVip) {
    this.aumVip = aumVip;
  }

  @Basic
  @Column(name = "CUSTOMERCODE")
  public String getCustomercode() {
    return customercode;
  }

  public void setCustomercode(String customercode) {
    this.customercode = customercode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhCasParentuserEntity that = (SmyDwhCasParentuserEntity) o;
    return Objects.equals(cusTcbsid, that.cusTcbsid) &&
      Objects.equals(rmTcbsid, that.rmTcbsid) &&
      Objects.equals(cusCustodycode, that.cusCustodycode) &&
      Objects.equals(rmCustodycode, that.rmCustodycode) &&
      Objects.equals(rmUsername, that.rmUsername) &&
      Objects.equals(cusName, that.cusName) &&
      Objects.equals(rmName, that.rmName) &&
      Objects.equals(rmEmail, that.rmEmail) &&
      Objects.equals(agencycode, that.agencycode) &&
      Objects.equals(agencyname, that.agencyname) &&
      Objects.equals(agencyEmail, that.agencyEmail) &&
      Objects.equals(agencyEmail2, that.agencyEmail2) &&
      Objects.equals(etlcurdate, that.etlcurdate) &&
      Objects.equals(etlrundatetime, that.etlrundatetime) &&
      Objects.equals(customerid, that.customerid) &&
      Objects.equals(permission, that.permission) &&
      Objects.equals(zone, that.zone) &&
      Objects.equals(rmCode, that.rmCode) &&
      Objects.equals(branchCus, that.branchCus) &&
      Objects.equals(aumVip, that.aumVip) &&
      Objects.equals(customercode, that.customercode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cusTcbsid, rmTcbsid, cusCustodycode, rmCustodycode, rmUsername, cusName, rmName, rmEmail, agencycode, agencyname, agencyEmail, agencyEmail2, etlcurdate, etlrundatetime,
      customerid, permission, zone, rmCode, branchCus, aumVip, customercode);
  }
}
