package com.tcbs.automation.tca.datacharts;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Smy_dwh_stox_MarketPrices")
public class SmyDwhStoxMarketPricesEntity {
  private Long id;
  private Timestamp tradingDate;
  private String code;
  private Integer basicPrice;
  private Integer closePrice;
  private String bourse;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;
  private Integer openPriceAdjusted;
  private Integer closePriceAdjusted;
  private Integer highestAdjusted;
  private Integer lowestAdjusted;
  private Integer totalTradingQtty;
  private Integer nmTotalTradedQtty;
  private Integer totalValue;


  public static List<HashMap<String, Object>> getByTicker(String ticker, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("    WITH foreign_trade AS ( ");
    queryStringBuilder.append("  	SELECT tradingdate as TradingDate, ticker as Ticker,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED as ForeignBuy,  ");
    queryStringBuilder.append("  	-FOREIGNSELLVOLUMEMATCHED as ForeignSell,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED - FOREIGNSELLVOLUMEMATCHED as NetForeignVol   ");
    queryStringBuilder.append("  	FROM stx_mrk_HoseStock   ");
    queryStringBuilder.append("  	WHERE tradingdate BETWEEN :fromDate AND :toDate ");
    queryStringBuilder.append("  		AND ticker = :ticker  ");
    queryStringBuilder.append("  	 ");
    queryStringBuilder.append("  	UNION  ");
    queryStringBuilder.append("  	SELECT tradingdate as TradingDate, ticker as Ticker,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED as ForeignBuy,  ");
    queryStringBuilder.append("  	-FOREIGNSELLVOLUMEMATCHED as ForeignSell,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED - FOREIGNSELLVOLUMEMATCHED as NetForeignVol ");
    queryStringBuilder.append("  	FROM stx_mrk_hNXStock  ");
    queryStringBuilder.append("  	WHERE tradingdate BETWEEN :fromDate AND :toDate ");
    queryStringBuilder.append("  		AND TICKER = :ticker ");
    queryStringBuilder.append("  	 ");
    queryStringBuilder.append("  	UNION ");
    queryStringBuilder.append("  	SELECT tradingdate as TradingDate, ticker as Ticker,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED as ForeignBuy,  ");
    queryStringBuilder.append("  	-FOREIGNSELLVOLUMEMATCHED as ForeignSell,  ");
    queryStringBuilder.append("  	FOREIGNBUYVOLUMEMATCHED - FOREIGNSELLVOLUMEMATCHED as NetForeignVol  ");
    queryStringBuilder.append("  	FROM stx_mrk_UpcomStock   ");
    queryStringBuilder.append("  	WHERE tradingdate BETWEEN :fromDate AND :toDate ");
    queryStringBuilder.append("  		AND TICKER = :ticker ");
    queryStringBuilder.append("  ) ");
    queryStringBuilder.append("  SELECT  ");
    queryStringBuilder.append("  sdsmp.tradingDate AS dateReport, sdsmp.ticker ,  ");
    queryStringBuilder.append("  ISNULL(foreign_trade.ForeignBuy, 0) AS foreignBuy,  ");
    queryStringBuilder.append("  ISNULL(foreign_trade.ForeignSell, 0) AS foreignSell,  ");
    queryStringBuilder.append("  ISNULL(foreign_trade.NetForeignVol, 0) AS netForeignVol, ");
    queryStringBuilder.append("  sdsmp.TotalMatchVolume AS totalVolume, ");
    queryStringBuilder.append("  ISNULL(srr.RS_Rank_1M, 0) AS rsRank ");
    queryStringBuilder.append("  FROM Smy_dwh_stox_MarketPrices sdsmp ");
    queryStringBuilder.append("  LEFT JOIN foreign_trade ON sdsmp.tradingDate = foreign_trade.TradingDate AND sdsmp.ticker = foreign_trade.Ticker  ");
    queryStringBuilder.append("  LEFT JOIN Stock_RSRating_Refining srr ON sdsmp.tradingDate = srr.DateReport AND sdsmp.ticker = srr.Ticker  ");
    queryStringBuilder.append("  WHERE sdsmp.ticker = :ticker ");
    queryStringBuilder.append("  AND sdsmp.tradingDate BETWEEN :fromDate AND :toDate ");
    queryStringBuilder.append(" ORDER BY sdsmp.tradingDate ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Id
  @GeneratedValue
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "trading_Date")
  public Timestamp getTradingDate() {
    return tradingDate;
  }

  public void setTradingDate(Timestamp tradingDate) {
    this.tradingDate = tradingDate;
  }

  @Basic
  @Column(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "basic_price")
  public Integer getBasicPrice() {
    return basicPrice;
  }

  public void setBasicPrice(Integer basicPrice) {
    this.basicPrice = basicPrice;
  }

  @Basic
  @Column(name = "close_price")
  public Integer getClosePrice() {
    return closePrice;
  }

  public void setClosePrice(Integer closePrice) {
    this.closePrice = closePrice;
  }

  @Basic
  @Column(name = "bourse")
  public String getBourse() {
    return bourse;
  }

  public void setBourse(String bourse) {
    this.bourse = bourse;
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

  @Basic
  @Column(name = "OpenPrice_Adjusted")
  public Integer getOpenPriceAdjusted() {
    return openPriceAdjusted;
  }

  public void setOpenPriceAdjusted(Integer openPriceAdjusted) {
    this.openPriceAdjusted = openPriceAdjusted;
  }

  @Basic
  @Column(name = "ClosePrice_Adjusted")
  public Integer getClosePriceAdjusted() {
    return closePriceAdjusted;
  }

  public void setClosePriceAdjusted(Integer closePriceAdjusted) {
    this.closePriceAdjusted = closePriceAdjusted;
  }

  @Basic
  @Column(name = "Highest_Adjusted")
  public Integer getHighestAdjusted() {
    return highestAdjusted;
  }

  public void setHighestAdjusted(Integer highestAdjusted) {
    this.highestAdjusted = highestAdjusted;
  }

  @Basic
  @Column(name = "Lowest_Adjusted")
  public Integer getLowestAdjusted() {
    return lowestAdjusted;
  }

  public void setLowestAdjusted(Integer lowestAdjusted) {
    this.lowestAdjusted = lowestAdjusted;
  }

  @Basic
  @Column(name = "total_trading_qtty")
  public Integer getTotalTradingQtty() {
    return totalTradingQtty;
  }

  public void setTotalTradingQtty(Integer totalTradingQtty) {
    this.totalTradingQtty = totalTradingQtty;
  }

  @Basic
  @Column(name = "nm_total_traded_qtty")
  public Integer getNmTotalTradedQtty() {
    return nmTotalTradedQtty;
  }

  public void setNmTotalTradedQtty(Integer nmTotalTradedQtty) {
    this.nmTotalTradedQtty = nmTotalTradedQtty;
  }

  @Basic
  @Column(name = "Total_Value")
  public Integer getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(Integer totalValue) {
    this.totalValue = totalValue;
  }
}
