package com.tcbs.automation.tca.datacharts;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Technical_Indicator_byDate")
public class TechnicalIndicatorByDateEntity {
  private Long id;
  private Date dateReport;
  private String ticker;
  private Double close;
  private Double high;
  private Double low;
  private Double basicPrice;
  private Double rsi;
  private Double stochk;
  private Double stochd;
  private Double macd;
  private Double macdema;
  private Double macdhist;
  private Double adx;
  private Double sma5;
  private Double sma10;
  private Double sma20;
  private Double sma20Std;
  private Double bbUpper;
  private Double bbLower;
  private Double totalValue;
  private Double sma60;
  private Double sma120;
  private Double sma240;
  private Double sar;
  private Double vSma20;

  public static List<HashMap<String, Object>> getByTicker(String ticker, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("  SELECT DateReport as dateReport, Ticker as ticker, [Close] as closePrice , sma5, sma20,");
    queryStringBuilder.append("         BB_upper as upper , BB_lower as lower, macd, macdema, macdhist, stochk, stochd, rsi, adx ");
    queryStringBuilder.append("  FROM Technical_Indicator_byDate tibd ");
    queryStringBuilder.append("  WHERE Ticker = :ticker and DateReport >= :fromDate and DateReport <= :toDate ");
    queryStringBuilder.append("  ORDER BY DateReport ASC ");

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
  @Column(name = "DateReport")
  public Date getDateReport() {
    return dateReport;
  }

  public void setDateReport(Date dateReport) {
    this.dateReport = dateReport;
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
  @Column(name = "Close")
  public Double getClose() {
    return close;
  }

  public void setClose(Double close) {
    this.close = close;
  }

  @Basic
  @Column(name = "High")
  public Double getHigh() {
    return high;
  }

  public void setHigh(Double high) {
    this.high = high;
  }

  @Basic
  @Column(name = "Low")
  public Double getLow() {
    return low;
  }

  public void setLow(Double low) {
    this.low = low;
  }

  @Basic
  @Column(name = "Basic_Price")
  public Double getBasicPrice() {
    return basicPrice;
  }

  public void setBasicPrice(Double basicPrice) {
    this.basicPrice = basicPrice;
  }

  @Basic
  @Column(name = "rsi")
  public Double getRsi() {
    return rsi;
  }

  public void setRsi(Double rsi) {
    this.rsi = rsi;
  }

  @Basic
  @Column(name = "stochk")
  public Double getStochk() {
    return stochk;
  }

  public void setStochk(Double stochk) {
    this.stochk = stochk;
  }

  @Basic
  @Column(name = "stochd")
  public Double getStochd() {
    return stochd;
  }

  public void setStochd(Double stochd) {
    this.stochd = stochd;
  }

  @Basic
  @Column(name = "macd")
  public Double getMacd() {
    return macd;
  }

  public void setMacd(Double macd) {
    this.macd = macd;
  }

  @Basic
  @Column(name = "macdema")
  public Double getMacdema() {
    return macdema;
  }

  public void setMacdema(Double macdema) {
    this.macdema = macdema;
  }

  @Basic
  @Column(name = "macdhist")
  public Double getMacdhist() {
    return macdhist;
  }

  public void setMacdhist(Double macdhist) {
    this.macdhist = macdhist;
  }

  @Basic
  @Column(name = "adx")
  public Double getAdx() {
    return adx;
  }

  public void setAdx(Double adx) {
    this.adx = adx;
  }

  @Basic
  @Column(name = "sma5")
  public Double getSma5() {
    return sma5;
  }

  public void setSma5(Double sma5) {
    this.sma5 = sma5;
  }

  @Basic
  @Column(name = "sma10")
  public Double getSma10() {
    return sma10;
  }

  public void setSma10(Double sma10) {
    this.sma10 = sma10;
  }

  @Basic
  @Column(name = "sma20")
  public Double getSma20() {
    return sma20;
  }

  public void setSma20(Double sma20) {
    this.sma20 = sma20;
  }

  @Basic
  @Column(name = "sma20_std")
  public Double getSma20Std() {
    return sma20Std;
  }

  public void setSma20Std(Double sma20Std) {
    this.sma20Std = sma20Std;
  }

  @Basic
  @Column(name = "BB_upper")
  public Double getBbUpper() {
    return bbUpper;
  }

  public void setBbUpper(Double bbUpper) {
    this.bbUpper = bbUpper;
  }

  @Basic
  @Column(name = "BB_lower")
  public Double getBbLower() {
    return bbLower;
  }

  public void setBbLower(Double bbLower) {
    this.bbLower = bbLower;
  }

  @Basic
  @Column(name = "TotalValue")
  public Double getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(Double totalValue) {
    this.totalValue = totalValue;
  }

  @Basic
  @Column(name = "sma60")
  public Double getSma60() {
    return sma60;
  }

  public void setSma60(Double sma60) {
    this.sma60 = sma60;
  }

  @Basic
  @Column(name = "sma120")
  public Double getSma120() {
    return sma120;
  }

  public void setSma120(Double sma120) {
    this.sma120 = sma120;
  }

  @Basic
  @Column(name = "sma240")
  public Double getSma240() {
    return sma240;
  }

  public void setSma240(Double sma240) {
    this.sma240 = sma240;
  }

  @Basic
  @Column(name = "SAR")
  public Double getSar() {
    return sar;
  }

  public void setSar(Double sar) {
    this.sar = sar;
  }

  @Basic
  @Column(name = "V_sma20")
  public Double getvSma20() {
    return vSma20;
  }

  public void setvSma20(Double vSma20) {
    this.vSma20 = vSma20;
  }
}
