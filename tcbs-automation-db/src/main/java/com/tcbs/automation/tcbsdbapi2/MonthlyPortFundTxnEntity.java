package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_FundTxn")
public class MonthlyPortFundTxnEntity {
  private Date txDate;
  @Id
  private String custodyCd;
  private String action;
  private String underlying;
  private Double quantity;
  private BigDecimal unitPrice;
  private Double amount;
  private String tradingCode;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getFundTxn(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, CONVERT(varchar, txdate, 23) as txDate, action, underlying, quantity, amount   ");
    queryStringBuilder.append(" from MonthlyPort_FundTxn   ");
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
  @Column(name = "Action")
  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  @Basic
  @Column(name = "Underlying")
  public String getUnderlying() {
    return underlying;
  }

  public void setUnderlying(String underlying) {
    this.underlying = underlying;
  }

  @Basic
  @Column(name = "Quantity")
  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "UnitPrice")
  public BigDecimal getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  @Basic
  @Column(name = "Amount")
  public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  @Basic
  @Column(name = "TradingCode")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
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
    MonthlyPortFundTxnEntity that = (MonthlyPortFundTxnEntity) o;
    return Objects.equals(txDate, that.txDate) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(action, that.action) &&
      Objects.equals(underlying, that.underlying) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(unitPrice, that.unitPrice) &&
      Objects.equals(amount, that.amount) &&
      Objects.equals(tradingCode, that.tradingCode) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txDate, custodyCd, action, underlying, quantity, unitPrice, amount, tradingCode, etlCurDate, etlRunDateTime);
  }

}
