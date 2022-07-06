package com.tcbs.automation.dynamicwatchlist.elliot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity("stock_statistics")
public class Analyses {
  @Property(value = "ticker")
  String ticker;

  @Property(value = "exchangeName")
  String exchangeName;

  @Property(value = "companyName")
  String companyName;

  @Property(value = "industryCode")
  Integer industryCode;

  @Property(value = "industryName")
  String industryName;

  @Property(value = "prev1MonthClose")
  Double previous1MonthClose;

  @Property(value = "prev1YearClose")
  Double previous1YearClose;

  @Property(value = "prev5YearClose")
  Double previous5YearClose;

  @Embedded(value = "prev1Day")
  Prev1Day prev1Day;

  @Embedded(value = "prev1Min")
  CombinedData prev1Min;

  @Embedded(value = "prev15Min")
  CombinedData prev15Min;

  @Embedded(value = "uptrend")
  RecordBoolean uptrend;

  @Embedded(value = "bullishBreakout")
  RecordBoolean bullishBreakout;

  @Embedded(value = "bearishBreakout")
  RecordBoolean bearishBreakout;

  @Embedded(value = "activeBuyPercentage")
  RecordNumber activeBuyPercentage;

  @Embedded(value = "strongBuyPercentage")
  RecordNumber strongBuyPercentage;

  @Embedded(value = "suddenlyHighVolumeMatching")
  RecordNumber suddenlyHighVolumeMatching;

  @Embedded(value = "forecastVolumeRatio")
  RecordNumber forecastVolumeRatio;

  @Embedded(value = "priceVsSma5")
  RecordString priceVsSma5;

  @Embedded(value = "priceVsSma20")
  RecordString priceVsSma20;

  @Embedded(value = "priceGrowth1Month")
  RecordNumber priceGrowth1Month;

  @Embedded(value = "priceGrowth1Week")
  RecordNumber priceGrowth1Week;

  @Embedded(value = "foreignTransaction")
  RecordNumber foreignTransaction;

  @Embedded(value = "totalTradingValue")
  RecordNumber totalTradingValue;

  @Embedded(value = "priceNearRealtime")
  RecordNumber priceNearRealtime;

  @Embedded(value = "rsi14NearRealtime")
  RecordNumber rsi14NearRealtime;

  /*
  phase3
   */
  @Embedded(value = "tcbsBuySellSignal")
  RecordString tcbsBuySellSignal;

  @Embedded(value = "macdHist")
  RecordNumber macdHist;

  @Embedded(value = "macdHistVsPrev")
  RecordNumber macdHistVsPrev;

  @Embedded(value = "volumeVsVSma5")
  RecordNumber volumeVsVSma5;

  @Embedded(value = "volumeVsVSma10")
  RecordNumber volumeVsVSma10;

  @Embedded(value = "volumeVsVSma20")
  RecordNumber volumeVsVSma20;

  @Embedded(value = "volumeVsVSma50")
  RecordNumber volumeVsVSma50;

  @Embedded(value = "priceVsSma10")
  RecordString priceVsSma10;

  @Embedded(value = "priceVsSma50")
  RecordString priceVsSma50;

  @Embedded(value = "priceVs52Week")
  RecordString priceVs52Week;

  @Embedded(value = "sarVsMacdHist")
  RecordString sarVsMacdHist;

  @Embedded(value = "openBollingerBand")
  RecordString openBollingerBand;

  @Embedded(value = "dmiSignal")
  RecordString dmiSignal;

  @Embedded(value = "rsiVsOverBoughtRegion")
  RecordString rsiVsOverBoughtRegion;

  @Embedded(value = "rsiVsOverSoldRegion")
  RecordString rsiVsOverSoldRegion;

  //phase 5
  @Embedded(value = "percent1YearFromPeak")
  RecordNumber percent1YearFromPeak;

  public Boolean getUptrendValue() {
    return this.getUptrend() != null ? this.getUptrend().getValue() : null;
  }

  public Boolean getBullishBreakoutValue() {
    return this.getBullishBreakout() != null ? this.getBullishBreakout().getValue() : null;
  }

  public String getPriceVsSma5Value() {
    return this.getPriceVsSma5() != null ? this.getPriceVsSma5().getValue() : null;
  }

  public String getPriceVsSma20Value() {
    return this.getPriceVsSma20() != null ? this.getPriceVsSma20().getValue() : null;
  }

  public Double getActiveBuyPercentageValue() {
    return this.getActiveBuyPercentage() != null ? this.getActiveBuyPercentage().getValue() : null;
  }

  public Double getStrongBuyPercentageValue() {
    return this.getStrongBuyPercentage() != null ? this.getStrongBuyPercentage().getValue() : null;
  }

  public Double getSuddenlyHighVolumeMatchingValue() {
    return this.getSuddenlyHighVolumeMatching() != null ? this.getSuddenlyHighVolumeMatching().getValue() : null;
  }

  public Double getForecastVolumeRatioValue() {
    return this.getForecastVolumeRatio() != null ? this.getForecastVolumeRatio().getValue() : null;
  }

  public Double getPriceGrowth1MonthValue() {
    return this.getPriceGrowth1Month() != null ? this.getPriceGrowth1Month().getValue() : null;
  }

  public Double getPriceGrowth1WeekValue() {
    return this.getPriceGrowth1Week() != null ? this.getPriceGrowth1Week().getValue() : null;
  }

  public Double getForeignTransactionValue() {
    return this.getForeignTransaction() != null ? this.getForeignTransaction().getValue() : null;
  }

  public Double getTotalTradingValueValue() {
    return this.getTotalTradingValue() != null ? this.getTotalTradingValue().getValue() : null;
  }

  public Double getPriceNearRealtimeValue() {
    return this.getPriceNearRealtime() != null ? this.getPriceNearRealtime().getValue() : null;
  }

  public Double getRsi14NearRealtimeValue() {
    return this.getRsi14NearRealtime() != null ? this.getRsi14NearRealtime().getValue() : null;
  }

  public String getTcbsBuySellSignalValue() {
    return this.getTcbsBuySellSignal() != null ? this.getTcbsBuySellSignal().getValue() : null;
  }

  public Double getMacdHistValue() {
    return this.getMacdHist() != null ? this.getMacdHist().getValue() : null;
  }

  public Double getMacdHistVsPrevValue() {
    return this.getMacdHistVsPrev() != null ? this.getMacdHistVsPrev().getValue() : null;
  }

  public Double getVolumeVsVSma5Value() {
    return this.getVolumeVsVSma5() != null ? this.getVolumeVsVSma5().getValue() : null;
  }

  public Double getVolumeVsVSma10Value() {
    return this.getVolumeVsVSma10() != null ? this.getVolumeVsVSma10().getValue() : null;
  }

  public Double getVolumeVsVSma20Value() {
    return this.getVolumeVsVSma20() != null ? this.getVolumeVsVSma20().getValue() : null;
  }

  public Double getVolumeVsVSma50Value() {
    return this.getVolumeVsVSma50() != null ? this.getVolumeVsVSma50().getValue() : null;
  }

  public String getPriceVsSma10Value() {
    return this.getPriceVsSma10() != null ? this.getPriceVsSma10().getValue() : null;
  }

  public String getPriceVsSma50Value() {
    return this.getPriceVsSma50() != null ? this.getPriceVsSma50().getValue() : null;
  }

  public String getPriceVs52WeekValue() {
    return this.getPriceVs52Week() != null ? this.getPriceVs52Week().getValue() : null;
  }

  public String getSarVsMacdHistValue() {
    return this.getSarVsMacdHist() != null ? this.getSarVsMacdHist().getValue() : null;
  }

  public String getOpenBollingerBandValue() {
    return this.getOpenBollingerBand() != null ? this.getOpenBollingerBand().getValue() : null;
  }

  public String getDmiSignalValue() {
    return this.getDmiSignal() != null ? this.getDmiSignal().getValue() : null;
  }

  public String getRsiVsOverBoughtRegionValue() {
    return this.getRsiVsOverBoughtRegion() != null ? this.getRsiVsOverBoughtRegion().getValue() : null;
  }

  public String getRsiVsOverSoldRegionValue() {
    return this.getRsiVsOverSoldRegion() != null ? this.getRsiVsOverSoldRegion().getValue() : null;
  }

  //phase 5
  public Double getPercent1YearFromPeakValue() {
    return this.getPercent1YearFromPeak() != null ? this.getPercent1YearFromPeak().getValue() : null;
  }
}