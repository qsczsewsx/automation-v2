package com.tcbs.automation.dwh.dwhservice;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "DailyPort_StockTxn")
public class DailyPortStockTxnEntity {
  @Id
  private Timestamp busDate;
  private String custodyCd;
  private String symbol;
  private String field;
  private Long quantity;
  private BigDecimal buyPrice;
  private Long buyAmt;
  private BigDecimal sellPrice;
  private Long sellAmt;
  private Long pnL;
  private BigDecimal pcPnL;

  @Step("update ETL data")
  public static void updateEtlDate(String txDate) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("UPDATE DailyPort_StockTxn " +
      " SET BusDate= ? " +
      " WHERE BusDate = (SELECT MAX(BusDate) FROM DailyPort_StockTxn) ");
    query.setParameter(1, txDate);
    query.executeUpdate();
    trans.commit();
  }

  @Step("Get latest etlDate")
  public static Date getLatestEtlDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT MAX(BusDate) ");
    queryStringBuilder.append(" FROM DailyPort_StockTxn   ");

    try {
      List<Date> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();
      Date latestDate = result.get(0);
      return latestDate;
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Basic
  @Column(name = "BusDate")
  public Timestamp getBusDate() {
    return busDate;
  }

  public void setBusDate(Timestamp busDate) {
    this.busDate = busDate;
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
  @Column(name = "Symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "Field")
  public String getField() {
    return field;
  }

  public void setField(String field) {
    this.field = field;
  }

  @Basic
  @Column(name = "Quantity")
  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "BuyPrice")
  public BigDecimal getBuyPrice() {
    return buyPrice;
  }

  public void setBuyPrice(BigDecimal buyPrice) {
    this.buyPrice = buyPrice;
  }

  @Basic
  @Column(name = "BuyAmt")
  public Long getBuyAmt() {
    return buyAmt;
  }

  public void setBuyAmt(Long buyAmt) {
    this.buyAmt = buyAmt;
  }

  @Basic
  @Column(name = "SellPrice")
  public BigDecimal getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(BigDecimal sellPrice) {
    this.sellPrice = sellPrice;
  }

  @Basic
  @Column(name = "SellAmt")
  public Long getSellAmt() {
    return sellAmt;
  }

  public void setSellAmt(Long sellAmt) {
    this.sellAmt = sellAmt;
  }

  @Basic
  @Column(name = "PnL")
  public Long getPnL() {
    return pnL;
  }

  public void setPnL(Long pnL) {
    this.pnL = pnL;
  }

  @Basic
  @Column(name = "PCPnL")
  public BigDecimal getPcPnL() {
    return pcPnL;
  }

  public void setPcPnL(BigDecimal pcPnL) {
    this.pcPnL = pcPnL;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyPortStockTxnEntity that = (DailyPortStockTxnEntity) o;
    return Objects.equals(busDate, that.busDate) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(field, that.field) &&
      Objects.equals(quantity, that.quantity) &&
      Objects.equals(buyPrice, that.buyPrice) &&
      Objects.equals(buyAmt, that.buyAmt) &&
      Objects.equals(sellPrice, that.sellPrice) &&
      Objects.equals(sellAmt, that.sellAmt) &&
      Objects.equals(pnL, that.pnL) &&
      Objects.equals(pcPnL, that.pcPnL);
  }

  @Override
  public int hashCode() {
    return Objects.hash(busDate, custodyCd, symbol, field, quantity, buyPrice, buyAmt, sellPrice, sellAmt, pnL, pcPnL);
  }
}
