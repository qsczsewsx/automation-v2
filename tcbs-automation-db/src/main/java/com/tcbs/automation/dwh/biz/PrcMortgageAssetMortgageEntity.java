package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Data
@Table(name = "Prc_Mortgage_Asset_Mortgage")
public class PrcMortgageAssetMortgageEntity {
  @Id
  private String tradingCode;
  private String accountName;
  private String accountNumber;
  private String accountCity;
  private String accountBank;
  private String accountBankBranch;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;


  @Step("insert data")
  public static void insertData(PrcMortgageAssetMortgageEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into Prc_Mortgage_Asset_Mortgage " +
      "(Trading_Code, ACCOUNT_NAME, ACCOUNT_NUMBER, ACCOUNT_CITY, ACCOUNT_BANK, ACCOUNT_BANK_BRANCH, EtlCurDate, EtlRunDateTime) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getTradingCode());
    query.setParameter(2, entity.getAccountName());
    query.setParameter(3, entity.getAccountNumber());
    query.setParameter(4, entity.getAccountCity());
    query.setParameter(5, entity.getAccountBank());
    query.setParameter(6, entity.getAccountBankBranch());
    query.setParameter(7, entity.getEtlCurDate());
    query.setParameter(8, entity.getEtlRunDateTime());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(PrcMortgageAssetMortgageEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery(" DELETE from Prc_Mortgage_Asset_Mortgage " +
      " where Trading_Code = ?");
    query.setParameter(1, entity.getTradingCode());

    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "Trading_Code")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
  }

  @Basic
  @Column(name = "ACCOUNT_NAME")
  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  @Basic
  @Column(name = "ACCOUNT_NUMBER")
  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Basic
  @Column(name = "ACCOUNT_CITY")
  public String getAccountCity() {
    return accountCity;
  }

  public void setAccountCity(String accountCity) {
    this.accountCity = accountCity;
  }

  @Basic
  @Column(name = "ACCOUNT_BANK")
  public String getAccountBank() {
    return accountBank;
  }

  public void setAccountBank(String accountBank) {
    this.accountBank = accountBank;
  }

  @Basic
  @Column(name = "ACCOUNT_BANK_BRANCH")
  public String getAccountBankBranch() {
    return accountBankBranch;
  }

  public void setAccountBankBranch(String accountBankBranch) {
    this.accountBankBranch = accountBankBranch;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrcMortgageAssetMortgageEntity that = (PrcMortgageAssetMortgageEntity) o;
    return Objects.equals(tradingCode, that.tradingCode) && Objects.equals(accountName, that.accountName) && Objects.equals(accountNumber, that.accountNumber) && Objects.equals(accountCity,
      that.accountCity) && Objects.equals(accountBank, that.accountBank) && Objects.equals(accountBankBranch, that.accountBankBranch) && Objects.equals(etlCurDate, that.etlCurDate) && Objects.equals(
      etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tradingCode, accountName, accountNumber, accountCity, accountBank, accountBankBranch, etlCurDate, etlRunDateTime);
  }
}
