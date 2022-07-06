package com.tcbs.automation.tcbond;

import lombok.Getter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Table(name = "TRADING_SELL_ICONNECT")
public class TradingSellIconnect {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  public Integer id;
  @NotNull
  @Column(name = "TRADING_ID")
  public Integer tradingId;
  @Column(name = "ORDER_MARKET_ID")
  public Integer orderMarketId;
  @Column(name = "TRADING_CODE")
  public String tradingCode;
  @Column(name = "MATCHING_VOLUME")
  public Integer matchingVolume;
  @Column(name = "HOLD_VOLUME")
  public Integer holdVolume;
  @Column(name = "TRANSFERRED_VOLUME")
  public Integer transferredVolume;
  @Column(name = "MATCHED_VOLUME")
  public Integer matchedVolume;
  @Column(name = "TRACE_LOG")
  public String traceLog;
  @Column(name = "COUNTER_ORDERMARKET_ID")
  public Integer counterOrdermarketId;
  @Column(name = "OPTLOCK")
  public Integer optlock;


  public TradingSellIconnect setId(Integer id) {
    this.id = id;
    return this;
  }


  public TradingSellIconnect setTradingId(Integer tradingId) {
    this.tradingId = tradingId;
    return this;
  }


  public TradingSellIconnect setOrderMarketId(Integer orderMarketId) {
    this.orderMarketId = orderMarketId;
    return this;
  }


  public TradingSellIconnect setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
    return this;
  }


  public TradingSellIconnect setMatchingVolume(Integer matchingVolume) {
    this.matchingVolume = matchingVolume;
    return this;
  }


  public TradingSellIconnect setHoldVolume(Integer holdVolume) {
    this.holdVolume = holdVolume;
    return this;
  }


  public TradingSellIconnect setTransferredVolume(Integer transferredVolume) {
    this.transferredVolume = transferredVolume;
    return this;
  }


  public TradingSellIconnect setMatchedVolume(Integer matchedVolume) {
    this.matchedVolume = matchedVolume;
    return this;
  }


  public TradingSellIconnect setTraceLog(String traceLog) {
    this.traceLog = traceLog;
    return this;
  }


  public TradingSellIconnect setCounterOrdermarketId(Integer counterOrdermarketId) {
    this.counterOrdermarketId = counterOrdermarketId;
    return this;
  }


  public TradingSellIconnect setOptlock(Integer optlock) {
    this.optlock = optlock;
    return this;
  }

  @Step("Get trading distributation from TradingSellIconnect")
  public TradingSellIconnect getDistributationTradingByOrderId(Integer orderId) {
    TradingSellIconnect result = null;
    Query<TradingSellIconnect> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from TradingSellIconnect where orderMarketId =: orderMarketId");
    query.setParameter("orderMarketId", orderId);
    List<TradingSellIconnect> listResult = query.getResultList();
    if (listResult != null) {
      result = listResult.get(0);
    }
    return result;
  }
}
