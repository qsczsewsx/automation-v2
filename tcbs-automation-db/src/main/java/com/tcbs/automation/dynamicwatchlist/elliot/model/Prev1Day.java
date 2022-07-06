package com.tcbs.automation.dynamicwatchlist.elliot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Property;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Prev1Day {
  @Property(value = "sma5")
  Double sma5;

  @Property(value = "sma10")
  Double sma10;

  @Property(value = "sma20")
  Double sma20;

  @Property(value = "resistance")
  Double maxClose;

  @Property(value = "support")
  Double minClose;

  @Property(value = "closePrice")
  Double closePrice;

  @Property(value = "avgTradingQuantityShortTerm")
  Double avgTradingQuantityShortTerm;

  @Property(value = "avgTradingQuantityShortTerm1Minute")
  Double avgTradingQuantityShortTermPerMin;

  @Property(value = "avgTradingQuantityMediumTerm")
  Double avgTradingQuantityMediumTerm;

  @Property(value = "marketCap")
  Double marketCap;

  @Property(value = "roe")
  Double roe;

  @Property(value = "pe")
  Double pe;

  @Property(value = "pb")
  Double pb;

  @Property(value = "evEbitda")
  Double evEbitda;

  @Property(value = "stockRating")
  Double stockRating;

  @Property(value = "financialHealth")
  Double financialHealth;

  @Property(value = "businessModel")
  Double businessModel;

  @Property(value = "businessOperation")
  Double businessOperation;

  @Property(value = "alpha")
  Double alpha;

  @Property(value = "beta")
  Double beta;

  @Property(value = "relativeStrength")
  Double relativeStrength;

  @Property(value = "dividendYield")
  Double dividendYield;

  @Property(value = "revenueGrowth1Year")
  Double revenueGrowth1Year;

  @Property(value = "revenueGrowth5Year")
  Double revenueGrowth5Year;

  @Property(value = "epsGrowth1Year")
  Double epsGrowth1Year;

  @Property(value = "epsGrowth5Year")
  Double epsGrowth5Year;

  @Property(value = "grossMargin")
  Double grossMargin;

  @Property(value = "netMargin")
  Double netMargin;

  @Property(value = "doe")
  Double doe;

  @Property(value = "avgTradingValue5Day")
  Double avgTradingValue5Day;

  @Property(value = "avgTradingValue10Day")
  Double avgTradingValue10Day;

  @Property(value = "avgTradingValue20Day")
  Double avgTradingValue20Day;

  @Property(value = "relativeStrength3Day")
  Double relativeStrength3Day;

  @Property(value = "relativeStrength1Month")
  Double relativeStrength1Month;

  @Property(value = "relativeStrength3Month")
  Double relativeStrength3Month;

  @Property(value = "relativeStrength1Year")
  Double relativeStrength1Year;

  @Property(value = "foreignVolumePercent")
  Double foreignVolumePercent;

  @Property(value = "prev5DayClose")
  Double prev5DayClose;

  @Property(value = "prev20DayClose")
  Double prev20DayClose;

  @Property(value = "tcRS")
  Double tcRS;

  //phase 3
  @Property(value = "tcbsRecommend")
  String tcbsRecommend;

  @Property(value = "foreignBuySell20Session")
  Double foreignBuySell20Session;

  @Property(value = "numContinuousDay")
  Double numContinuousDay;

  @Property(value = "eps")
  Double eps;

  @Property(value = "sma50")
  Double sma50;

  @Property(value = "lowestPrice52Week")
  Double lowestPrice52Week;

  @Property(value = "highestPrice52Week")
  Double highestPrice52Week;

  @Property(value = "sar")
  Double sar;

  @Property(value = "macdHist")
  Double macdHist;

  @Property(value = "minusDi")
  Double minusDi;

  @Property(value = "plusDi")
  Double plusDi;

  @Property(value = "rsi")
  Double rsi;

  @Property(value = "isLatestReportQuarter")
  Boolean isLatestReportQuarter;

  @Property(value = "freeFloatRate")
  Double freeFloatRate;

  @Property(value = "cashOnMarketCap")
  Double cashOnMarketCap;

  @Property(value = "cashOnAsset")
  Double cashOnAsset;

  @Property(value = "isa22")
  Double isa22;

  @Property(value = "revGrowthLatestQuarter")
  Double revGrowthLatestQuarter;

  @Property(value = "revGrowth2NdLatestQuarter")
  Double revGrowth2NdLatestQuarter;

  @Property(value = "profitGrowthLatestQuarter")
  Double profitGrowthLatestQuarter;

  @Property(value = "profitGrowth2NdLatestQuarter")
  Double profitGrowth2NdLatestQuarter;

  @Property(value = "highestPriceFrom2015")
  Double highestPriceFrom2015;

  @Property(value = "lowestPriceFrom2015")
  Double lowestPriceFrom2015;

  @Property(value = "sma100")
  Double sma100;
}
