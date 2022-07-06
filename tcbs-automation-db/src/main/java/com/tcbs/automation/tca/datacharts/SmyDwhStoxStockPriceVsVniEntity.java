package com.tcbs.automation.tca.datacharts;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Smy_dwh_stox_StockPrice_vs_VNI")
public class SmyDwhStoxStockPriceVsVniEntity {
  private Long id;
  private String ticker;
  private BigDecimal tickerVni1W;
  private BigDecimal tickerVni1M;
  private BigDecimal tickerVni3M;
  private BigDecimal tickerVni6M;
  private BigDecimal tickerVni1Y;
  private Timestamp reportDate;

  public static List<HashMap<String, Object>> getByTicker(String ticker, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("  SELECT ReportDate as dateReport, Ticker as ticker,[Ticker/VNI1W] as pricePerIndex");
    queryStringBuilder.append("  FROM Smy_dwh_stox_StockPrice_vs_VNI sdsspvv ");
    queryStringBuilder.append("  WHERE Ticker = :ticker and ReportDate >= :fromDate and ReportDate <= :toDate ");
    queryStringBuilder.append("  ORDER BY ReportDate DESC ");

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

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "Ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "Ticker/VNI1W")
  public BigDecimal getTickerVni1W() {
    return tickerVni1W;
  }

  public void setTickerVni1W(BigDecimal tickerVni1W) {
    this.tickerVni1W = tickerVni1W;
  }

  @Basic
  @Column(name = "Ticker/VNI1M")
  public BigDecimal getTickerVni1M() {
    return tickerVni1M;
  }

  public void setTickerVni1M(BigDecimal tickerVni1M) {
    this.tickerVni1M = tickerVni1M;
  }

  @Basic
  @Column(name = "Ticker/VNI3M")
  public BigDecimal getTickerVni3M() {
    return tickerVni3M;
  }

  public void setTickerVni3M(BigDecimal tickerVni3M) {
    this.tickerVni3M = tickerVni3M;
  }

  @Basic
  @Column(name = "Ticker/VNI6M")
  public BigDecimal getTickerVni6M() {
    return tickerVni6M;
  }

  public void setTickerVni6M(BigDecimal tickerVni6M) {
    this.tickerVni6M = tickerVni6M;
  }

  @Basic
  @Column(name = "Ticker/VNI1Y")
  public BigDecimal getTickerVni1Y() {
    return tickerVni1Y;
  }

  public void setTickerVni1Y(BigDecimal tickerVni1Y) {
    this.tickerVni1Y = tickerVni1Y;
  }

  @Basic
  @Column(name = "ReportDate")
  public Timestamp getReportDate() {
    return reportDate;
  }

  public void setReportDate(Timestamp reportDate) {
    this.reportDate = reportDate;
  }
}
