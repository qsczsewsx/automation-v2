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
@Table(name = "MonthlyPort_CashTxn")
public class MonthlyPortCashTxnEntity {
  private Date txDate;
  @Id
  private String custodyCd;
  private Double ciCreditAmt;
  private Double ciDebitAmt;
  private Double ciBeginBal;
  private Double ciReceivingBal;
  private Double ciEmkAmtBal;
  private Double ciDfdebtamtBal;
  private Double odBuySecu;
  private String txDesc;
  private int etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getCashTxn(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, CONVERT(varchar, txDate, 23) as txDate , txDesc, ci_credit_amt, ci_debit_amt, ci_end_amt  ");
    queryStringBuilder.append(" from MonthlyPort_CashTxn   ");
    queryStringBuilder.append(" where CustodyCD = :custodyCd  ");

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
  @Column(name = "TxDate")
  public Date getTxDate() {
    return txDate;
  }

  public void setTxDate(Date txDate) {
    this.txDate = txDate;
  }

  @Basic
  @Column(name = "CustodyCD")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "CI_Credit_Amt")
  public Double getCiCreditAmt() {
    return ciCreditAmt;
  }

  public void setCiCreditAmt(Double ciCreditAmt) {
    this.ciCreditAmt = ciCreditAmt;
  }

  @Basic
  @Column(name = "CI_Debit_Amt")
  public Double getCiDebitAmt() {
    return ciDebitAmt;
  }

  public void setCiDebitAmt(Double ciDebitAmt) {
    this.ciDebitAmt = ciDebitAmt;
  }

  @Basic
  @Column(name = "CI_Begin_Bal")
  public Double getCiBeginBal() {
    return ciBeginBal;
  }

  public void setCiBeginBal(Double ciBeginBal) {
    this.ciBeginBal = ciBeginBal;
  }

  @Basic
  @Column(name = "CI_Receiving_Bal")
  public Double getCiReceivingBal() {
    return ciReceivingBal;
  }

  public void setCiReceivingBal(Double ciReceivingBal) {
    this.ciReceivingBal = ciReceivingBal;
  }

  @Basic
  @Column(name = "CI_EmkAmt_Bal")
  public Double getCiEmkAmtBal() {
    return ciEmkAmtBal;
  }

  public void setCiEmkAmtBal(Double ciEmkAmtBal) {
    this.ciEmkAmtBal = ciEmkAmtBal;
  }

  @Basic
  @Column(name = "CI_DFDEBTAMT_BAL")
  public Double getCiDfdebtamtBal() {
    return ciDfdebtamtBal;
  }

  public void setCiDfdebtamtBal(Double ciDfdebtamtBal) {
    this.ciDfdebtamtBal = ciDfdebtamtBal;
  }

  @Basic
  @Column(name = "OD_BUY_SECU")
  public Double getOdBuySecu() {
    return odBuySecu;
  }

  public void setOdBuySecu(Double odBuySecu) {
    this.odBuySecu = odBuySecu;
  }

  @Basic
  @Column(name = "TxDesc")
  public String getTxDesc() {
    return txDesc;
  }

  public void setTxDesc(String txDesc) {
    this.txDesc = txDesc;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public int getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(int etlCurDate) {
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
    MonthlyPortCashTxnEntity that = (MonthlyPortCashTxnEntity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(txDate, that.txDate) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(ciCreditAmt, that.ciCreditAmt) &&
      Objects.equals(ciDebitAmt, that.ciDebitAmt) &&
      Objects.equals(ciBeginBal, that.ciBeginBal) &&
      Objects.equals(ciReceivingBal, that.ciReceivingBal) &&
      Objects.equals(ciEmkAmtBal, that.ciEmkAmtBal) &&
      Objects.equals(ciDfdebtamtBal, that.ciDfdebtamtBal) &&
      Objects.equals(odBuySecu, that.odBuySecu) &&
      Objects.equals(txDesc, that.txDesc) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txDate, custodyCd, ciCreditAmt, ciDebitAmt, ciBeginBal, ciReceivingBal, ciEmkAmtBal, ciDfdebtamtBal, odBuySecu, txDesc, etlCurDate, etlRunDateTime);
  }

}
