package com.tcbs.automation.tcbsdbapi1;

import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "smy_tcbsshare_cus_tcb_identification")
public class SmyTcbsshareCusTcbIdentificationEntity {
  private Integer customerid;
  private String tcbsid;
  private String custodycd;
  private Integer customercodeTcb;
  private String cstBdgSegNm;
  private String branchCus;
  private String rmSaleid;
  private Date effFmDate;
  private Date endToDate;
  private String aumVip;
  private Timestamp reportdate;
  private Timestamp createddate;

  @Id
  @Column(name = "CUSTOMERID")
  public Integer getCustomerid() {
    return customerid;
  }

  public void setCustomerid(Integer customerid) {
    this.customerid = customerid;
  }

  @Basic
  @Column(name = "TCBSID")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "CUSTODYCD")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodycd) {
    this.custodycd = custodycd;
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
  @Column(name = "RM_SALEID")
  public String getRmSaleid() {
    return rmSaleid;
  }

  public void setRmSaleid(String rmSaleid) {
    this.rmSaleid = rmSaleid;
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
  @Column(name = "AUM_VIP")
  public String getAumVip() {
    return aumVip;
  }

  public void setAumVip(String aumVip) {
    this.aumVip = aumVip;
  }

  @Basic
  @Column(name = "REPORTDATE")
  public Timestamp getReportdate() {
    return reportdate;
  }

  public void setReportdate(Timestamp reportdate) {
    this.reportdate = reportdate;
  }

  @Basic
  @Column(name = "CREATEDDATE")
  public Timestamp getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Timestamp createddate) {
    this.createddate = createddate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyTcbsshareCusTcbIdentificationEntity that = (SmyTcbsshareCusTcbIdentificationEntity) o;
    return Objects.equals(customerid, that.customerid) &&
      Objects.equals(tcbsid, that.tcbsid) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(customercodeTcb, that.customercodeTcb) &&
      Objects.equals(cstBdgSegNm, that.cstBdgSegNm) &&
      Objects.equals(branchCus, that.branchCus) &&
      Objects.equals(rmSaleid, that.rmSaleid) &&
      Objects.equals(effFmDate, that.effFmDate) &&
      Objects.equals(endToDate, that.endToDate) &&
      Objects.equals(aumVip, that.aumVip) &&
      Objects.equals(reportdate, that.reportdate) &&
      Objects.equals(createddate, that.createddate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerid, tcbsid, custodycd, customercodeTcb, cstBdgSegNm, branchCus, rmSaleid, effFmDate, endToDate, aumVip, reportdate, createddate);
  }

//  @Step("insert or update data")
//  public boolean saveOrUpdateTcbsShare(SmyTcbsshareCusTcbIdentificationEntity customer) {
//    try {
//      Session session = DbApi1.dbApiDbConnection.getSession();
//      session.beginTransaction();
//      session.save(customer);
//      session.getTransaction().commit();
//    } catch (Exception ex) {
//      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
//      return false;
//    }
//    return true;
//  }


  @Step("insert data")
  public void saveOrUpdateTcbsShare(SmyTcbsshareCusTcbIdentificationEntity entity) {
    Session session = DbApi1.dbApiDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("INSERT INTO smy_tcbsshare_cus_tcb_identification " +
      "(CUSTOMERID, TCBSID, CUSTODYCD, CUSTOMERCODE_TCB, CST_BDG_SEG_NM, BRANCH_CUS, RM_SALEID, EFF_FM_DATE, END_TO_DATE, AUM_VIP, REPORTDATE, CREATEDDATE)  " +
      " VALUES(?, ?, ?, ?, ?, ?, ?, CONVERT(DATE, ?), CONVERT(DATE, ?), ?, ?, ?)");
    query.setParameter(1, entity.getCustomerid());
    query.setParameter(2, entity.getTcbsid());
    query.setParameter(3, entity.getCustodycd());
    query.setParameter(4, entity.getCustomercodeTcb());
    query.setParameter(5, entity.getCstBdgSegNm());
    query.setParameter(6, entity.getBranchCus());
    query.setParameter(7, entity.getRmSaleid());
    query.setParameter(8, entity.getEffFmDate());
    query.setParameter(9, entity.getEndToDate());
    query.setParameter(10, entity.getAumVip());
    query.setParameter(11, entity.getReportdate());
    query.setParameter(12, entity.getCreateddate());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by key")
  public void deleteByCustomerId(Integer customerId) {
    Session session = DbApi1.dbApiDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<SmyTcbsshareCusTcbIdentificationEntity> query = session.createQuery(
      "DELETE FROM SmyTcbsshareCusTcbIdentificationEntity i WHERE i.customerid=:customerId"
    );
    query.setParameter("customerId", customerId);
    query.executeUpdate();
    session.getTransaction().commit();
    DbApi1.dbApiDbConnection.closeSession();
  }

  @Step("get data by tcbsId")
  public List<SmyTcbsshareCusTcbIdentificationEntity> findByKey(List<String> listTcbsId, List<String> listCustodyCd) {
    Query<SmyTcbsshareCusTcbIdentificationEntity> query;
    if (CollectionUtils.isEmpty(listTcbsId) || listTcbsId.get(0).equals("")) {
      query = DbApi1.dbApiDbConnection.getSession().createQuery(
        "from SmyTcbsshareCusTcbIdentificationEntity as cus "
          + " where cus.custodycd in :listCustodyCd "
          + " and cus.reportdate =(select max(cus1.reportdate) from SmyTcbsshareCusTcbIdentificationEntity as cus1) "
        , SmyTcbsshareCusTcbIdentificationEntity.class
      );
      query.setParameter("listCustodyCd", listCustodyCd);
    } else {
      query = DbApi1.dbApiDbConnection.getSession().createQuery(
        "from SmyTcbsshareCusTcbIdentificationEntity as cus "
          + " where cus.tcbsid in :listTcbsId "
          + " and cus.reportdate =(select max(cus1.reportdate) from SmyTcbsshareCusTcbIdentificationEntity as cus1) "
        , SmyTcbsshareCusTcbIdentificationEntity.class
      );
      query.setParameter("listTcbsId", listTcbsId);
    }

    List<SmyTcbsshareCusTcbIdentificationEntity> results = query.getResultList();
    DbApi1.dbApiDbConnection.closeSession();
    return results;
  }

}
