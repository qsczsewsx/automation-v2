package com.tcbs.automation.staging;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "[Stg_tcbs-share_R_TBL_CU_0015_IB_CST_FOR_TCB]")
public class StgTcbsShareRTblCu0015IbCstForTcb {
  private String customerid;
  private String customercode;
  private String customername;
  private Date birthday;
  private String identitycard;
  private Date identitydate;
  private String bankname;
  private String bankcode;
  private String customerbank;
  private Integer customercodeTcb;
  private String cstBdgSegNm;
  private String branchCus;
  private String zonNm;
  private String sector;
  private Integer rmSaleid;
  private String cstTpGrp;
  private String branchIa;
  private Date effFmDate;
  private Date endToDate;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;
  private String aumVip;
  private Timestamp reportDate;

  @Basic
  @Column(name = "CUSTOMERID")
  public String getCustomerid() {
    return customerid;
  }

  public void setCustomerid(String customerid) {
    this.customerid = customerid;
  }

  @Basic
  @Column(name = "CUSTOMERCODE")
  public String getCustomercode() {
    return customercode;
  }

  public void setCustomercode(String customercode) {
    this.customercode = customercode;
  }

  @Basic
  @Column(name = "CUSTOMERNAME")
  public String getCustomername() {
    return customername;
  }

  public void setCustomername(String customername) {
    this.customername = customername;
  }

  @Basic
  @Column(name = "BIRTHDAY")
  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  @Id
  @Column(name = "IDENTITYCARD")
  public String getIdentitycard() {
    return identitycard;
  }

  public void setIdentitycard(String identitycard) {
    this.identitycard = identitycard;
  }

  @Basic
  @Column(name = "IDENTITYDATE")
  public Date getIdentitydate() {
    return identitydate;
  }

  public void setIdentitydate(Date identitydate) {
    this.identitydate = identitydate;
  }

  @Basic
  @Column(name = "BANKNAME")
  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  @Basic
  @Column(name = "BANKCODE")
  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  @Basic
  @Column(name = "CUSTOMERBANK")
  public String getCustomerbank() {
    return customerbank;
  }

  public void setCustomerbank(String customerbank) {
    this.customerbank = customerbank;
  }

  @Basic
  @Column(name = "CUSTOMERCODE_TCB")
  public Integer getCustomercodeTcb() {
    return customercodeTcb;
  }

  public void setCustomercodeTcb(Integer customercodeTcb) {
    this.customercodeTcb = customercodeTcb;
  }

  @Basic
  @Column(name = "CST_BDG_SEG_NM")
  public String getCstBdgSegNm() {
    return cstBdgSegNm;
  }

  public void setCstBdgSegNm(String cstBdgSegNm) {
    this.cstBdgSegNm = cstBdgSegNm;
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
  @Column(name = "ZON_NM")
  public String getZonNm() {
    return zonNm;
  }

  public void setZonNm(String zonNm) {
    this.zonNm = zonNm;
  }

  @Basic
  @Column(name = "SECTOR")
  public String getSector() {
    return sector;
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  @Basic
  @Column(name = "RM_SALEID")
  public Integer getRmSaleid() {
    return rmSaleid;
  }

  public void setRmSaleid(Integer rmSaleid) {
    this.rmSaleid = rmSaleid;
  }

  @Basic
  @Column(name = "CST_TP_GRP")
  public String getCstTpGrp() {
    return cstTpGrp;
  }

  public void setCstTpGrp(String cstTpGrp) {
    this.cstTpGrp = cstTpGrp;
  }

  @Basic
  @Column(name = "BRANCH_IA")
  public String getBranchIa() {
    return branchIa;
  }

  public void setBranchIa(String branchIa) {
    this.branchIa = branchIa;
  }

  @Basic
  @Column(name = "EFF_FM_DATE")
  public Date getEffFmDate() {
    return effFmDate;
  }

  public void setEffFmDate(Date effFmDate) {
    this.effFmDate = effFmDate;
  }

  @Basic
  @Column(name = "END_TO_DATE")
  public Date getEndToDate() {
    return endToDate;
  }

  public void setEndToDate(Date endToDate) {
    this.endToDate = endToDate;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
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
  @Column(name = "AUM_VIP")
  public String getAumVip() {
    return aumVip;
  }

  public void setAumVip(String aumVip) {
    this.aumVip = aumVip;
  }

  @Basic
  @Column(name = "ReportDate")
  public Timestamp getReportDate() {
    return reportDate;
  }

  public void setReportDate(Timestamp reportDate) {
    this.reportDate = reportDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StgTcbsShareRTblCu0015IbCstForTcb that = (StgTcbsShareRTblCu0015IbCstForTcb) o;
    return Objects.equals(customerid, that.customerid) &&
      Objects.equals(customercode, that.customercode) &&
      Objects.equals(customername, that.customername) &&
      Objects.equals(birthday, that.birthday) &&
      Objects.equals(identitycard, that.identitycard) &&
      Objects.equals(identitydate, that.identitydate) &&
      Objects.equals(bankname, that.bankname) &&
      Objects.equals(bankcode, that.bankcode) &&
      Objects.equals(customerbank, that.customerbank) &&
      Objects.equals(customercodeTcb, that.customercodeTcb) &&
      Objects.equals(cstBdgSegNm, that.cstBdgSegNm) &&
      Objects.equals(branchCus, that.branchCus) &&
      Objects.equals(zonNm, that.zonNm) &&
      Objects.equals(sector, that.sector) &&
      Objects.equals(rmSaleid, that.rmSaleid) &&
      Objects.equals(cstTpGrp, that.cstTpGrp) &&
      Objects.equals(branchIa, that.branchIa) &&
      Objects.equals(effFmDate, that.effFmDate) &&
      Objects.equals(endToDate, that.endToDate) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime) &&
      Objects.equals(aumVip, that.aumVip) &&
      Objects.equals(reportDate, that.reportDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerid, customercode, customername, birthday, identitycard, identitydate, bankname, bankcode, customerbank, customercodeTcb, cstBdgSegNm, branchCus, zonNm, sector,
      rmSaleid,
      cstTpGrp, branchIa, effFmDate, endToDate, etlCurDate, etlRunDateTime, aumVip, reportDate);
  }

  @Step("insert or update data")
  public boolean saveOrUpdateBankShareCustomer(StgTcbsShareRTblCu0015IbCstForTcb bankShareCustomer) {
    try {
      Session session = Staging.stagingDbConnection.getSession();
      session.beginTransaction();
      session.save(bankShareCustomer);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return true;

  }

  @Step("delete data by key")
  public void deleteByIdentityCard(String identityCard) {
    Session session = Staging.stagingDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTcbsShareRTblCu0015IbCstForTcb> query = session.createQuery(
      "DELETE FROM StgTcbsShareRTblCu0015IbCstForTcb i WHERE i.identitycard=:identityCard"
    );
    query.setParameter("identityCard", identityCard);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
