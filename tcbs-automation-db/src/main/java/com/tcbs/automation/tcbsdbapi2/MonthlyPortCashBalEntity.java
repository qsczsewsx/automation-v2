package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_CashBal")
public class MonthlyPortCashBalEntity {
  private Date dateReport;
  @Id
  private String custodyCd;
  private Double cashCashEquivalent;
  private Double borrow;
  private Double ciBeginAmt;
  private Double ciTotalCreditAmt;
  private Double ciTotalDebitAmt;
  private Double ciEndAmt;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getCashAndBorrowing(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT custodyCd, cash_cashEquivalent, borrow FROM MonthlyPort_CashBal  ");
    queryStringBuilder.append(" where custodyCd = :custodyCd  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      Dwh.dwhDbConnection.closeSession();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getCashBal(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT custodyCd , ci_begin_amt, ci_total_credit_amt, ci_total_debit_amt, ci_end_amt  ");
    queryStringBuilder.append(" FROM MonthlyPort_CashBal  ");
    queryStringBuilder.append(" where custodyCd = :custodyCd  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      Dwh.dwhDbConnection.closeSession();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Column(name = "DateReport")
  public Date getDateReport() {
    return dateReport;
  }

  public void setDateReport(Date dateReport) {
    this.dateReport = dateReport;
  }

  @Basic
  @Column(name = "custodyCd")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "cash_cashEquivalent")
  public Double getCashCashEquivalent() {
    return cashCashEquivalent;
  }

  public void setCashCashEquivalent(Double cashCashEquivalent) {
    this.cashCashEquivalent = cashCashEquivalent;
  }

  @Basic
  @Column(name = "borrow")
  public Double getBorrow() {
    return borrow;
  }

  public void setBorrow(Double borrow) {
    this.borrow = borrow;
  }

  @Basic
  @Column(name = "ci_begin_amt")
  public Double getCiBeginAmt() {
    return ciBeginAmt;
  }

  public void setCiBeginAmt(Double ciBeginAmt) {
    this.ciBeginAmt = ciBeginAmt;
  }

  @Basic
  @Column(name = "ci_total_credit_amt")
  public Double getCiTotalCreditAmt() {
    return ciTotalCreditAmt;
  }

  public void setCiTotalCreditAmt(Double ciTotalCreditAmt) {
    this.ciTotalCreditAmt = ciTotalCreditAmt;
  }

  @Basic
  @Column(name = "ci_total_debit_amt")
  public Double getCiTotalDebitAmt() {
    return ciTotalDebitAmt;
  }

  public void setCiTotalDebitAmt(Double ciTotalDebitAmt) {
    this.ciTotalDebitAmt = ciTotalDebitAmt;
  }

  @Basic
  @Column(name = "ci_end_amt")
  public Double getCiEndAmt() {
    return ciEndAmt;
  }

  public void setCiEndAmt(Double ciEndAmt) {
    this.ciEndAmt = ciEndAmt;
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
    MonthlyPortCashBalEntity that = (MonthlyPortCashBalEntity) o;
    return Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(cashCashEquivalent, that.cashCashEquivalent) &&
      Objects.equals(borrow, that.borrow) &&
      Objects.equals(ciBeginAmt, that.ciBeginAmt) &&
      Objects.equals(ciTotalCreditAmt, that.ciTotalCreditAmt) &&
      Objects.equals(ciTotalDebitAmt, that.ciTotalDebitAmt) &&
      Objects.equals(ciEndAmt, that.ciEndAmt) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dateReport, custodyCd, cashCashEquivalent, borrow, ciBeginAmt, ciTotalCreditAmt, ciTotalDebitAmt, ciEndAmt, etlCurDate, etlRunDateTime);
  }
}
