package com.tcbs.automation.dwh;


import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class StockRecommendEntity {

  @Step("Get Stock recommend from DB")
  public static List<HashMap<String, Object>> stockRecommendByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    List<HashMap<String, Object>> resultList;
    queryStringBuilder.append(" SELECT ci.ticker, ci.longName, ci.englishLongName, ci.namelv2, ci.nameEnlv2, ci.stock_rating, ci.exchangeName ");
    queryStringBuilder.append("   , round(sc2.correlation,3) correlation, sc.beta, sc.pe, vni.vni1m, vni.vni3m, vni.vni6m  ");
    queryStringBuilder.append("   , sh.Count_Holding, sh.[Percentage] as percentHoling ");
    queryStringBuilder.append(" FROM StockRecommend sr ");
    queryStringBuilder.append(" Left join StockCorrelation sc2 on sr.Ticker = sc2.Ticker and sr.Recommendations = sc2.[Other Ticker]  ");
    queryStringBuilder.append(" LEFT JOIN Prc_Stock_CAPM sc on sr.recommendations = sc.ticker ");
    queryStringBuilder.append(" LEFT JOIN Smy_dwh_stox_VNI_StockPrice_Change vni on sr.recommendations = vni.ticker ");
    queryStringBuilder.append(" LEFT JOIN StockHoldCount sh on sr.recommendations = sh.Stock_Ticker ");
    queryStringBuilder.append(" LEFT JOIN Smy_dwh_stox_CompanyIndustryRate ci on sr.recommendations = ci.ticker ");
    queryStringBuilder.append(" WHERE sr.ticker = :ticker ");
    queryStringBuilder.append(" AND vni.etlcurdate = (SELECT max(Etlcurdate) FROM Smy_dwh_stox_VNI_StockPrice_Change) ");
    queryStringBuilder.append(" AND sc.datereport = (SELECT max(datereport) FROM Prc_Stock_CAPM); ");

    resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", ticker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    Dwh.dwhDbConnection.closeSession();
    return resultList;
  }

  @Step("Get Stock same industry from DB")
  public static List<HashMap<String, Object>> stockSameIndustryByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    List<HashMap<String, Object>> resultList;
    queryStringBuilder.append(" SELECT com.ticker, com.longName, com.englishLongName, com.namelv2, com.nameEnlv2, com.stock_rating, com.exchangeName  ");
    queryStringBuilder.append("   , round(sr.Correlation,3) as correlation  ");
    queryStringBuilder.append("   , sc.beta, sc.pe, vni.vni1m, vni.vni3m, vni.vni6m, sh.Count_Holding, sh.[Percentage] as percentHoling, com.Stock_Rating  ");
    queryStringBuilder.append(" FROM (  ");
    queryStringBuilder.append("   SELECT TOP(10) * FROM Smy_dwh_stox_CompanyIndustryRate  ");
    queryStringBuilder.append("   WHERE IdLevel2 = ( SELECT IdLevel2 FROM Smy_dwh_stox_CompanyIndustryRate WHERE ticker = :ticker )  ");
    queryStringBuilder.append("   AND exchangeId IN (0, 1, 3)  ");
    queryStringBuilder.append("   ORDER BY marketcap DESC  ");
    queryStringBuilder.append(" ) com  ");
    queryStringBuilder.append(" LEFT JOIN StockCorrelation sr on com.ticker = sr.[Other Ticker]  ");
    queryStringBuilder.append(" LEFT JOIN Prc_Stock_CAPM sc on com.ticker = sc.ticker  ");
    queryStringBuilder.append(" LEFT JOIN Smy_dwh_stox_VNI_StockPrice_Change vni on com.ticker = vni.ticker  ");
    queryStringBuilder.append(" LEFT JOIN StockHoldCount sh on com.ticker = sh.Stock_Ticker  ");
    queryStringBuilder.append(" WHERE sr.Ticker = :ticker  ");
    queryStringBuilder.append(" AND vni.etlcurdate = (SELECT max(Etlcurdate) FROM Smy_dwh_stox_VNI_StockPrice_Change)  ");
    queryStringBuilder.append(" AND sc.datereport = (SELECT max(datereport) FROM Prc_Stock_CAPM) order by com.marketcap desc ;  ");

    resultList = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", ticker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    Dwh.dwhDbConnection.closeSession();
    return resultList;
  }
}
