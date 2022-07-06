package com.tcbs.automation.cas;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "TCBS_USER_ACCOUNT")
public class TcbsUserAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "BANK_ACCOUNT_NO")
  private String bankAccountNo;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "BANK_ACCOUNT_NAME")
  private String bankAccountName;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BANK_BRANCH")
  private String bankBranch;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "ID_NAME")
  private String idName;
  @Column(name = "ID_DATE")
  private Date idDate;
  @Column(name = "ID_PLACE")
  private String idPlace;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "MAX_LIMIT")
  private String maxLimit;
  @Column(name = "CURRENT_LIMIT")
  private String currentLimit;
  @Column(name = "CASH_ACCOUNT_TYPE")
  private String cashAccountType;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getBankAccountNo() {
    return bankAccountNo;
  }

  public void setBankAccountNo(String bankAccountNo) {
    this.bankAccountNo = bankAccountNo;
  }


  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }


  public String getBankAccountName() {
    return bankAccountName;
  }

  public void setBankAccountName(String bankAccountName) {
    this.bankAccountName = bankAccountName;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }


  public String getBankBranch() {
    return bankBranch;
  }

  public void setBankBranch(String bankBranch) {
    this.bankBranch = bankBranch;
  }


  public String getBankprovince() {
    return bankprovince;
  }

  public void setBankprovince(String bankprovince) {
    this.bankprovince = bankprovince;
  }


  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }


  public String getIdName() {
    return idName;
  }

  public void setIdName(String idName) {
    this.idName = idName;
  }


  public Date getIdDate() {
    return idDate;
  }

  public void setIdDate(Date idDate) {
    this.idDate = idDate;
  }


  public String getIdPlace() {
    return idPlace;
  }

  public void setIdPlace(String idPlace) {
    this.idPlace = idPlace;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getMaxLimit() {
    return maxLimit;
  }

  public void setMaxLimit(String maxLimit) {
    this.maxLimit = maxLimit;
  }


  public String getCurrentLimit() {
    return currentLimit;
  }

  public void setCurrentLimit(String currentLimit) {
    this.currentLimit = currentLimit;
  }


  public String getCashAccountType() {
    return cashAccountType;
  }

  public void setCashAccountType(String cashAccountType) {
    this.cashAccountType = cashAccountType;
  }

  @Step
  public TcbsUserAccount getByTcbsAccount(String userId) {
    Query<TcbsUserAccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserAccount a where a.userId=:userId", TcbsUserAccount.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }
}
