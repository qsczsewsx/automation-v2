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
@Table(name = "MonthlyPort_StockTxn")
public class MonthlyPortStockTxnEntity {
  private Date txDate;
  @Id
  private String custodycd;
  private String field;
  private String symbol;
  private Long quantity;
  private BigDecimal price;
  private Long amount;
  private Integer tradingCode;
  private int etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("Get data")
  public static List<HashMap<String, Object>> getStockTxn(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select custodyCd, CONVERT(varchar, txdate, 23) as txDate , field as action , SYMBOL as underlying, quantity, Price, amount  ");
    queryStringBuilder.append(" from MonthlyPort_StockTxn   ");
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
  @Column(name = "custodycd")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodycd) {
    this.custodycd = custodycd;
  }

  @Basic
  @Column(name = "field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Basic
  @Column(name = "SYMBOL")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "quantity")
  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "Price")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Basic
  @Column(name = "Amount")
  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  @Basic
  @Column(name = "TradingCode")
  public Integer getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(Integer tradingCode) {
    this.tradingCode = tradingCode;
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
    MonthlyPortStockTxnEntity that = (MonthlyPortStockTxnEntity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(txDate, that.txDate) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(field, that.field) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(price, that.price) &&
      Objects.equals(amount, that.amount) &&
      Objects.equals(tradingCode, that.tradingCode) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txDate, custodycd, field, symbol, quantity, price, amount, tradingCode, etlCurDate, etlRunDateTime);
  }

}
