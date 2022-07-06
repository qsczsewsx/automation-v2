package com.tcbs.automation.ops;

import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "CPS_BONDHOLDER_COUPON_PAYMENT")
public class CpsBondholderCouponPayment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "BOND_NAME")
  private String bondName;
  @Column(name = "LISTED_CODE")
  private String listedCode;
  @Column(name = "INTEREST_PERIOD")
  private String interestPeriod;
  @Column(name = "REDEMPTION")
  private String redemption;
  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;
  @Column(name = "ACTUAL_PAYMENT_DATE")
  private Date actualPaymentDate;
  @Column(name = "BONDHOLDER_NAME")
  private String bondholderName;
  @Column(name = "BONDHOLDER_ID")
  private String bondholderId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "NONDEPO_AMOUNT")
  private String nondepoAmount;
  @Column(name = "DEPO_AMOUNT")
  private String depoAmount;
  @Column(name = "TOTAL_AMOUNT")
  private String totalAmount;
  @Column(name = "TOTAL_CBF_AMOUNT")
  private String totalCbfAmount;
  @Column(name = "NONDEPO_INTEREST_BEFORETAX")
  private String nondepoInterestBeforetax;
  @Column(name = "NONDEPO_TAXS")
  private String nondepoTaxs;
  @Column(name = "NONDEPO_INTEREST_AFTERTAX")
  private String nondepoInterestAftertax;
  @Column(name = "DEPO_INTEREST_BEFORETAX")
  private String depoInterestBeforetax;
  @Column(name = "DEPO_TAXS")
  private String depoTaxs;
  @Column(name = "DEPO_INTEREST_AFTERTAX")
  private String depoInterestAftertax;
  @Column(name = "TOTAL_INTEREST_BEFORETAX")
  private String totalInterestBeforetax;
  @Column(name = "TOTAL_TAXS")
  private String totalTaxs;
  @Column(name = "TOTAL_INTEREST_AFTERTAX")
  private String totalInterestAftertax;
  @Column(name = "TOTAL_PARVALUE")
  private String totalParvalue;
  @Column(name = "TOTAL_PAYMENT")
  private String totalPayment;
  @Column(name = "INFO_RECEIVING_ACCOUNT")
  private String infoReceivingAccount;
  @Column(name = "INFO_RECEIVING_BANK")
  private String infoReceivingBank;
  @Column(name = "INFO_RECEIVING_BANKBRANCH")
  private String infoReceivingBankbranch;
  @Column(name = "STATUS")
  private String status;
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_AT")
  private java.util.Date createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @NotNull
  @Column(name = "PROCESS_INSTANCE_ID")
  private String processInstanceId;
  @Column(name = "MAKER")
  private String maker;
  @Column(name = "MAKER_APPROVE_DATE")
  private Timestamp makerApproveDate;
  @Column(name = "BOM")
  private String bom;
  @Column(name = "BOM_APPROVE_DATE")
  private Timestamp bomApproveDate;
  @Column(name = "ACCOUNTANT")
  private String accountant;
  @Column(name = "ACCOUNTANT_DATE")
  private Timestamp accountantDate;

  @Step
  public static int deleteByTcbsIdAndCreateAt(String tcbsId, java.util.Date createdAt) {
    Session session = OPS.opsConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("delete from CPS_BONDHOLDER_COUPON_PAYMENT where TCBS_ID=:tcbsId and CREATED_AT=:createdAt");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("createdAt", createdAt);
    int res = query.executeUpdate();
    trans.commit();
    return res;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  public String getBondName() {
    return bondName;
  }

  public void setBondName(String bondName) {
    this.bondName = bondName;
  }

  public String getListedCode() {
    return listedCode;
  }

  public void setListedCode(String listedCode) {
    this.listedCode = listedCode;
  }

  public String getInterestPeriod() {
    return interestPeriod;
  }

  public void setInterestPeriod(String interestPeriod) {
    this.interestPeriod = interestPeriod;
  }

  public String getRedemption() {
    return redemption;
  }

  public void setRedemption(String redemption) {
    this.redemption = redemption;
  }

  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
  }

  public Date getActualPaymentDate() {
    return actualPaymentDate;
  }

  public void setActualPaymentDate(Date actualPaymentDate) {
    this.actualPaymentDate = actualPaymentDate;
  }

  public String getBondholderName() {
    return bondholderName;
  }

  public void setBondholderName(String bondholderName) {
    this.bondholderName = bondholderName;
  }

  public String getBondholderId() {
    return bondholderId;
  }

  public void setBondholderId(String bondholderId) {
    this.bondholderId = bondholderId;
  }

  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }

  public String getNondepoAmount() {
    return nondepoAmount;
  }

  public void setNondepoAmount(String nondepoAmount) {
    this.nondepoAmount = nondepoAmount;
  }

  public String getDepoAmount() {
    return depoAmount;
  }

  public void setDepoAmount(String depoAmount) {
    this.depoAmount = depoAmount;
  }

  public String getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(String totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTotalCbfAmount() {
    return totalCbfAmount;
  }

  public void setTotalCbfAmount(String totalCbfAmount) {
    this.totalCbfAmount = totalCbfAmount;
  }

  public String getNondepoInterestBeforetax() {
    return nondepoInterestBeforetax;
  }

  public void setNondepoInterestBeforetax(String nondepoInterestBeforetax) {
    this.nondepoInterestBeforetax = nondepoInterestBeforetax;
  }

  public String getNondepoTaxs() {
    return nondepoTaxs;
  }

  public void setNondepoTaxs(String nondepoTaxs) {
    this.nondepoTaxs = nondepoTaxs;
  }

  public String getNondepoInterestAftertax() {
    return nondepoInterestAftertax;
  }

  public void setNondepoInterestAftertax(String nondepoInterestAftertax) {
    this.nondepoInterestAftertax = nondepoInterestAftertax;
  }

  public String getDepoInterestBeforetax() {
    return depoInterestBeforetax;
  }

  public void setDepoInterestBeforetax(String depoInterestBeforetax) {
    this.depoInterestBeforetax = depoInterestBeforetax;
  }

  public String getDepoTaxs() {
    return depoTaxs;
  }

  public void setDepoTaxs(String depoTaxs) {
    this.depoTaxs = depoTaxs;
  }

  public String getDepoInterestAftertax() {
    return depoInterestAftertax;
  }

  public void setDepoInterestAftertax(String depoInterestAftertax) {
    this.depoInterestAftertax = depoInterestAftertax;
  }

  public String getTotalInterestBeforetax() {
    return totalInterestBeforetax;
  }

  public void setTotalInterestBeforetax(String totalInterestBeforetax) {
    this.totalInterestBeforetax = totalInterestBeforetax;
  }

  public String getTotalTaxs() {
    return totalTaxs;
  }

  public void setTotalTaxs(String totalTaxs) {
    this.totalTaxs = totalTaxs;
  }

  public String getTotalInterestAftertax() {
    return totalInterestAftertax;
  }

  public void setTotalInterestAftertax(String totalInterestAftertax) {
    this.totalInterestAftertax = totalInterestAftertax;
  }

  public String getTotalParvalue() {
    return totalParvalue;
  }

  public void setTotalParvalue(String totalParvalue) {
    this.totalParvalue = totalParvalue;
  }

  public String getTotalPayment() {
    return totalPayment;
  }

  public void setTotalPayment(String totalPayment) {
    this.totalPayment = totalPayment;
  }

  public String getInfoReceivingAccount() {
    return infoReceivingAccount;
  }

  public void setInfoReceivingAccount(String infoReceivingAccount) {
    this.infoReceivingAccount = infoReceivingAccount;
  }

  public String getInfoReceivingBank() {
    return infoReceivingBank;
  }

  public void setInfoReceivingBank(String infoReceivingBank) {
    this.infoReceivingBank = infoReceivingBank;
  }

  public String getInfoReceivingBankbranch() {
    return infoReceivingBankbranch;
  }

  public void setInfoReceivingBankbranch(String infoReceivingBankbranch) {
    this.infoReceivingBankbranch = infoReceivingBankbranch;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getUpdatedAt() {
    return updatedAt == null ? null : PublicConstant.dateTimeFormat.format(updatedAt);
  }

  public void setUpdatedAt(String updatedAt) throws ParseException {
    this.updatedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedAt).getTime());
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  public String getMakerApproveDate() {
    return makerApproveDate == null ? null : PublicConstant.dateTimeFormat.format(makerApproveDate);
  }

  public void setMakerApproveDate(String makerApproveDate) throws ParseException {
    this.makerApproveDate = new Timestamp(PublicConstant.dateTimeFormat.parse(makerApproveDate).getTime());
  }

  public String getBom() {
    return bom;
  }

  public void setBom(String bom) {
    this.bom = bom;
  }

  public String getBomApproveDate() {
    return bomApproveDate == null ? null : PublicConstant.dateTimeFormat.format(bomApproveDate);
  }

  public void setBomApproveDate(String bomApproveDate) throws ParseException {
    this.bomApproveDate = new Timestamp(PublicConstant.dateTimeFormat.parse(bomApproveDate).getTime());
  }

  public String getAccountant() {
    return accountant;
  }

  public void setAccountant(String accountant) {
    this.accountant = accountant;
  }

  public String getAccountantDate() {
    return accountantDate == null ? null : PublicConstant.dateTimeFormat.format(accountantDate);
  }

  public void setAccountantDate(String accountantDate) throws ParseException {
    this.accountantDate = new Timestamp(PublicConstant.dateTimeFormat.parse(accountantDate).getTime());
  }

  @Step
  public CpsBondholderCouponPayment getByTcbsIdAndCreateAt(String tcbsId, java.util.Date createdAt) {
    Query<CpsBondholderCouponPayment> query = OPS.opsConnection.getSession().createQuery(
      "from CpsBondholderCouponPayment a where a.tcbsId=:tcbsId and a.createdAt=:createdAt", CpsBondholderCouponPayment.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("createdAt", createdAt);
    return query.getSingleResult();
  }

}
