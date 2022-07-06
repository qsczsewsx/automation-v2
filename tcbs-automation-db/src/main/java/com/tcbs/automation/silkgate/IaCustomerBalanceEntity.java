package com.tcbs.automation.silkgate;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "IA_CUSTOMER_BALANCE")
public class IaCustomerBalanceEntity {
  private int id;
  private String custodyCode;
  private String bankAccount;
  private Float workingBalance;
  private Long availableBalance;
  private Long totalHold;
  private Short accountType;
  private Short accountStatus;
  private Timestamp createdTime;
  private Timestamp updatedTime;

  //by Lybtk
  public static void deleteData() {
    Session session = SilkgateDb.silkgateDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      " delete from IaCustomerBalanceEntity");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  //by Lybtk
  public static Long selectCountTotalAccount() {
    Query query = SilkgateDb.silkgateDbConnection.getSession().createQuery(
      "select count (*) from IaCustomerBalanceEntity");
    return (Long) query.uniqueResult();
  }

  //by Lybtk
  public static Float getAmountFromCustodyCodeAndBankAccount(String custodyCode, String bankAccount) {
    try {
      SilkgateDb.silkgateDbConnection.getSession().clear();
      Query<Float> query = SilkgateDb.silkgateDbConnection.getSession().createQuery(
        "select workingBalance from IaCustomerBalanceEntity where custodyCode=:custodyCode and bankAccount=:bankAccount"
      );
      query.setParameter("custodyCode", custodyCode);
      query.setParameter("bankAccount", bankAccount);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  //by Lybtk
  public static IaCustomerBalanceEntity getFromCustodyCodeAndBankAccount(String custodyCode, String bankAccount) {
    try {
      SilkgateDb.silkgateDbConnection.getSession().clear();
      Query<IaCustomerBalanceEntity> query = SilkgateDb.silkgateDbConnection.getSession().createQuery(
        " from IaCustomerBalanceEntity where custodyCode=:custodyCode and bankAccount=:bankAccount"
        , IaCustomerBalanceEntity.class);
      query.setParameter("custodyCode", custodyCode);
      query.setParameter("bankAccount", bankAccount);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "CUSTODY_CODE")
  public String getCustodyCode() {
    return custodyCode;
  }

  public void setCustodyCode(String custodyCode) {
    this.custodyCode = custodyCode;
  }

  @Basic
  @Column(name = "BANK_ACCOUNT")
  public String getBankAccount() {
    return bankAccount;
  }

  public void setBankAccount(String bankAccount) {
    this.bankAccount = bankAccount;
  }

  @Basic
  @Column(name = "WORKING_BALANCE")
  public Float getWorkingBalance() {
    return workingBalance;
  }

  public void setWorkingBalance(Float workingBalance) {
    this.workingBalance = workingBalance;
  }

  @Basic
  @Column(name = "AVAILABLE_BALANCE")
  public Long getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Long availableBalance) {
    this.availableBalance = availableBalance;
  }

  @Basic
  @Column(name = "TOTAL_HOLD")
  public Long getTotalHold() {
    return totalHold;
  }

  public void setTotalHold(Long totalHold) {
    this.totalHold = totalHold;
  }

  @Basic
  @Column(name = "ACCOUNT_TYPE")
  public Short getAccountType() {
    return accountType;
  }

  public void setAccountType(Short accountType) {
    this.accountType = accountType;
  }

  @Basic
  @Column(name = "ACCOUNT_STATUS")
  public Short getAccountStatus() {
    return accountStatus;
  }

  public void setAccountStatus(Short accountStatus) {
    this.accountStatus = accountStatus;
  }

  @Basic
  @Column(name = "CREATED_TIME")
  public Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Timestamp createdTime) {
    this.createdTime = createdTime;
  }

  @Basic
  @Column(name = "UPDATED_TIME")
  public Timestamp getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(Timestamp updatedTime) {
    this.updatedTime = updatedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IaCustomerBalanceEntity that = (IaCustomerBalanceEntity) o;
    return id == that.id &&
      Objects.equals(custodyCode, that.custodyCode) &&
      Objects.equals(bankAccount, that.bankAccount) &&
      Objects.equals(workingBalance, that.workingBalance) &&
      Objects.equals(availableBalance, that.availableBalance) &&
      Objects.equals(totalHold, that.totalHold) &&
      Objects.equals(accountType, that.accountType) &&
      Objects.equals(accountStatus, that.accountStatus) &&
      Objects.equals(createdTime, that.createdTime) &&
      Objects.equals(updatedTime, that.updatedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, custodyCode, bankAccount, workingBalance, availableBalance, totalHold, accountType, accountStatus, createdTime, updatedTime);
  }
}
