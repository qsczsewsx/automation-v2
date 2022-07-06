package com.tcbs.automation.cas;

import com.tcbs.automation.functions.PublicConstant;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "INV_ACCOUNT_PRO_USER")
public class InvAccountProUser {

  private static final String KEY_TCBS_ID = "tcbsId";
  private static final Logger logger = LoggerFactory.getLogger(InvAccountProUser.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "PRO_CODE")
  private String proCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "APPLIED_DATE")
  private Timestamp appliedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "REFERRER_ID")
  private String referrerId;
  @Column(name = "PURCHASER_ID")
  private String purchaserId;
  @Column(name = "PRO_EXPIRED_DATE")
  private Timestamp proExpiredDate;
  @Column(name = "TRIAL_EXPIRED_DATE")
  private Timestamp trialExpiredDate;
  @Column(name = "PAYMENT_DATE")
  private Timestamp paymentDate;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "CHANNEL")
  private String channel;
  @Column(name = "ACTIVED_DATE")
  private Timestamp activedDate;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "ACCNO_FLEX")
  private String accnoFlex;
  @Column(name = "AFTYPE_STATUS")
  private String aftypeStatus;
  @Column(name = "AFTYPE_STR_ERROR")
  private String aftypeStrError;
  @Column(name = "CASH_STATUS")
  private String cashStatus;
  @Column(name = "CASH_ERROR_MESSAGE")
  private String cashErrorMessage;
  @Column(name = "BANK_REF_ID")
  private String bankRefId;
  @Column(name = "AFTYPE_CODE_ERROR")
  private String aftypeCodeError;
  @Column(name = "CASH_TRANSACTION_END")
  private String cashTransactionEnd;
  @Column(name = "VSD_STATUS")
  private String vsdStatus;
  @Column(name = "IWP_CONFIRM")
  private String iwpConfirm;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  public String getProCode() {
    return proCode;
  }

  public void setProCode(String proCode) {
    this.proCode = proCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAppliedDate() {
    return appliedDate == null ? null : PublicConstant.dateTimeFormat.format(appliedDate);
  }

  public void setAppliedDate(String appliedDate) throws ParseException {
    this.appliedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(appliedDate).getTime());
  }

  public String getCreatedDate() {
    return createdDate == null ? null : PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }

  public String getUpdatedDate() {
    return updatedDate == null ? null : PublicConstant.dateTimeFormat.format(updatedDate);
  }

  public void setUpdatedDate(String updatedDate) throws ParseException {
    this.updatedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedDate).getTime());
  }

  public String getReferrerId() {
    return referrerId;
  }

  public void setReferrerId(String referrerId) {
    this.referrerId = referrerId;
  }

  public String getPurchaserId() {
    return purchaserId;
  }

  public void setPurchaserId(String purchaserId) {
    this.purchaserId = purchaserId;
  }

  public String getProExpiredDate() {
    return proExpiredDate == null ? null : PublicConstant.dateTimeFormat.format(proExpiredDate);
  }

  public void setProExpiredDate(String proExpiredDate) throws ParseException {
    this.proExpiredDate = new Timestamp(PublicConstant.dateTimeFormat.parse(proExpiredDate).getTime());
  }

  public String getTrialExpiredDate() {
    return trialExpiredDate == null ? null : PublicConstant.dateTimeFormat.format(trialExpiredDate);
  }

  public void setTrialExpiredDate(String trialExpiredDate) throws ParseException {
    this.trialExpiredDate = new Timestamp(PublicConstant.dateTimeFormat.parse(trialExpiredDate).getTime());
  }

  public String getPaymentDate() {
    return paymentDate == null ? null : PublicConstant.dateTimeFormat.format(paymentDate);
  }

  public void setPaymentDate(String paymentDate) throws ParseException {
    this.paymentDate = new Timestamp(PublicConstant.dateTimeFormat.parse(paymentDate).getTime());
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  public String getActivedDate() {
    return activedDate == null ? null : PublicConstant.dateTimeFormat.format(activedDate);
  }

  public void setActivedDate(String activedDate) throws ParseException {
    this.activedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(activedDate).getTime());
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getAccnoFlex() {
    return accnoFlex;
  }

  public void setAccnoFlex(String accnoFlex) {
    this.accnoFlex = accnoFlex;
  }

  public String getAftypeStatus() {
    return aftypeStatus;
  }

  public void setAftypeStatus(String aftypeStatus) {
    this.aftypeStatus = aftypeStatus;
  }

  public String getAftypeStrError() {
    return aftypeStrError;
  }

  public void setAftypeStrError(String aftypeStrError) {
    this.aftypeStrError = aftypeStrError;
  }

  public String getCashStatus() {
    return cashStatus;
  }

  public void setCashStatus(String cashStatus) {
    this.cashStatus = cashStatus;
  }

  public String getCashErrorMessage() {
    return cashErrorMessage;
  }

  public void setCashErrorMessage(String cashErrorMessage) {
    this.cashErrorMessage = cashErrorMessage;
  }

  public String getBankRefId() {
    return bankRefId;
  }

  public void setBankRefId(String bankRefId) {
    this.bankRefId = bankRefId;
  }

  public String getAftypeCodeError() {
    return aftypeCodeError;
  }

  public void setAftypeCodeError(String aftypeCodeError) {
    this.aftypeCodeError = aftypeCodeError;
  }

  public String getCashTransactionEnd() {
    return cashTransactionEnd;
  }

  public void setCashTransactionEnd(String cashTransactionEnd) {
    this.cashTransactionEnd = cashTransactionEnd;
  }

  public String getVsdStatus() {
    return vsdStatus;
  }

  public void setVsdStatus(String vsdStatus) {
    this.vsdStatus = vsdStatus;
  }

  public String getIwpConfirm() {
    return iwpConfirm;
  }

  public void setIwpConfirm(String iwpConfirm) {
    this.iwpConfirm = iwpConfirm;
  }

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void updateIwpConfirmByTcbsId(String iwpConfirm, String tcbsId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvAccountProUser> query = session.createQuery("update InvAccountProUser set iwpConfirm=:iwpConfirm where tcbsid=:tcbsId");
    query.setParameter("iwpConfirm", iwpConfirm);
    query.setParameter(KEY_TCBS_ID, tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void updateStatusByTcbsId(String status, String tcbsId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvAccountProUser> query = session.createQuery("update InvAccountProUser set status=:status where tcbsid=:tcbsId");
    query.setParameter("status", status);
    query.setParameter(KEY_TCBS_ID, tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void updateStatusAndAftypeByTcbsId(String tcbsId, String status, String aftype) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvAccountProUser> query = session.createQuery(
      "update InvAccountProUser set status=:status, aftypeStatus=:aftype where " +
        "id=(select max(id) from InvAccountProUser where tcbsid=:tcbsId)");
    query.setParameter("status", status);
    query.setParameter(KEY_TCBS_ID, tcbsId);
    query.setParameter("aftype", aftype);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public InvAccountProUser getNewestDataByTcbsId(String tcbsId) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvAccountProUser> query = session.createQuery("from InvAccountProUser where id=(select max(id) from InvAccountProUser where tcbsid=:tcbsId)", InvAccountProUser.class);
    InvAccountProUser result = new InvAccountProUser();
    query.setParameter(KEY_TCBS_ID, tcbsId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }

}
