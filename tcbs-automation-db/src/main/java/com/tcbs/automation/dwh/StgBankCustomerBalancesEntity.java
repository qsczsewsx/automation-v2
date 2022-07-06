package com.tcbs.automation.dwh;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;


@Entity
@Table(name = "Stg_bank_CustomerBalances")
public class StgBankCustomerBalancesEntity {
  private int id;
  private String custodyCd;
  private String bankAccountNumber;
  private Double workingBalance;
  private Double availableBalance;
  private Double totalHeld;
  private String acctAcctRelStatus;
  private String acctRelType;
  private Timestamp createdDate;

  @Basic
  @Id
  @Column(name = "Id")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "CustodyCd")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "BankAccountNumber")
  public String getBankAccountNumber() {
    return bankAccountNumber;
  }

  public void setBankAccountNumber(String bankAccountNumber) {
    this.bankAccountNumber = bankAccountNumber;
  }

  @Basic
  @Column(name = "WorkingBalance")
  public Double getWorkingBalance() {
    return workingBalance;
  }

  public void setWorkingBalance(Double workingBalance) {
    this.workingBalance = workingBalance;
  }

  @Basic
  @Column(name = "AvailableBalance")
  public Double getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Double availableBalance) {
    this.availableBalance = availableBalance;
  }

  @Basic
  @Column(name = "TotalHeld")
  public Double getTotalHeld() {
    return totalHeld;
  }

  public void setTotalHeld(Double totalHeld) {
    this.totalHeld = totalHeld;
  }

  @Basic
  @Column(name = "AcctAcctRelStatus")
  public String getAcctAcctRelStatus() {
    return acctAcctRelStatus;
  }

  public void setAcctAcctRelStatus(String acctAcctRelStatus) {
    this.acctAcctRelStatus = acctAcctRelStatus;
  }

  @Basic
  @Column(name = "AcctRelType")
  public String getAcctRelType() {
    return acctRelType;
  }

  public void setAcctRelType(String acctRelType) {
    this.acctRelType = acctRelType;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StgBankCustomerBalancesEntity that = (StgBankCustomerBalancesEntity) o;
    return id == that.id &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(bankAccountNumber, that.bankAccountNumber) &&
      Objects.equals(workingBalance, that.workingBalance) &&
      Objects.equals(availableBalance, that.availableBalance) &&
      Objects.equals(totalHeld, that.totalHeld) &&
      Objects.equals(acctAcctRelStatus, that.acctAcctRelStatus) &&
      Objects.equals(acctRelType, that.acctRelType) &&
      Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, custodyCd, bankAccountNumber, workingBalance, availableBalance, totalHeld, acctAcctRelStatus, acctRelType, createdDate);
  }

  //by Lybtk
  public static void updateAccountAmount(String custodyCd, String bankAccountNumber, Float workingBalance) {
    Session session = Dwh.dwhDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update StgBankCustomerBalancesEntity set workingBalance=:workingBalance where custodyCd=:custodyCd and bankAccountNumber=:bankAccountNumber ");
    query.setParameter("workingBalance", workingBalance.doubleValue());
    query.setParameter("custodyCd", custodyCd);
    query.setParameter("bankAccountNumber", bankAccountNumber);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  //by Lybtk
  public static Float getAmountFromCustudyandBankAcount(String custodyCd, String bankAccountNumber) {
    try {
      Dwh.dwhDbConnection.getSession().clear();
      Query<Double> query = Dwh.dwhDbConnection.getSession().createQuery(
        "select workingBalance from StgBankCustomerBalancesEntity where custodyCd=:custodyCd and bankAccountNumber=:bankAccountNumber order by id desc"
        , Double.class);
      query.setParameter("custodyCd", custodyCd);
      query.setParameter("bankAccountNumber", bankAccountNumber);
      return query.setMaxResults(1).getSingleResult().floatValue();
    } catch (NoResultException e) {
      return null;
    }
  }

}

