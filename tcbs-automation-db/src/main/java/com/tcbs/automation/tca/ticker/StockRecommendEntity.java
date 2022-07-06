package com.tcbs.automation.tca.ticker;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class StockRecommendEntity {

  @Step
  public static List<HashMap<String, Object>> stockRecommendByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    List<HashMap<String, Object>> resultList;
    queryStringBuilder.append("	SELECT sr.recommendations        as ticker	");
    queryStringBuilder.append("	, round(sc2.correlation, 3)     correlation	");
    queryStringBuilder.append("	, round(sc.beta, 3)            beta	");
    queryStringBuilder.append("	, round(lt.pe, 3)              pe	");
    queryStringBuilder.append("	, round(vni.vni1m, 3)          vni1m	");
    queryStringBuilder.append("	, round(vni.vni3m, 3)          vni3m	");
    queryStringBuilder.append("	, round(vni.vni6m, 3)          vni6m	");
    queryStringBuilder.append("	, sh.Count_Holding          as countHolding	");
    queryStringBuilder.append("	, round(sh.[Percentage], 3) as percentHoling	");
    queryStringBuilder.append("	, ci.longName	");
    queryStringBuilder.append("	, ci.englishLongName	");
    queryStringBuilder.append("	, ci.namelv2	");
    queryStringBuilder.append("	, ci.nameEnlv2	");
    queryStringBuilder.append("	, ci.exchangeName	");
    queryStringBuilder.append("	, round(ci.stock_rating, 3)    stockRating	");
    queryStringBuilder.append("	FROM StockRecommend sr	");
    queryStringBuilder.append("	LEFT JOIN StockCorrelation sc2 on sr.Ticker = sc2.Ticker and sr.Recommendations = sc2.[Other Ticker]	");
    queryStringBuilder.append("	LEFT JOIN Prc_Stock_CAPM sc on sr.recommendations = sc.ticker	");
    queryStringBuilder.append("	LEFT JOIN Smy_dwh_stox_VNI_StockPrice_Change vni on sr.recommendations = vni.ticker	");
    queryStringBuilder.append("	LEFT JOIN StockHoldCount sh on sr.recommendations = sh.Stock_Ticker	");
    queryStringBuilder.append("	LEFT JOIN Smy_dwh_stox_CompanyIndustryRate ci on sr.recommendations = ci.ticker	");
    queryStringBuilder.append("	LEFT JOIN tca_ratio_latest lt ON sr.recommendations = lt.Ticker ");
    queryStringBuilder.append("	WHERE sr.ticker = :ticker	");
    queryStringBuilder.append("	AND vni.etlcurdate = (SELECT max(Etlcurdate) FROM Smy_dwh_stox_VNI_StockPrice_Change)	");
    queryStringBuilder.append("	AND sc.datereport = (SELECT max(datereport) FROM Prc_Stock_CAPM)	");

    resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", ticker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    TcAnalysis.tcaDbConnection.closeSession();
    return resultList;
  }

  @Step
  public static List<HashMap<String, Object>> stockSameIndustryByTicker(String ticker, String lang) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (lang == null || lang.trim().isEmpty()) {
      lang = "vi";
    }
    List<HashMap<String, Object>> resultList;
    queryStringBuilder.append("	SELECT com.ticker	");
    queryStringBuilder.append("	, round(sr.Correlation,3) correlation	");
    queryStringBuilder.append("	, round(sc.beta, 3) beta	");
    queryStringBuilder.append("	, round(lt.pe, 3) pe	");
    queryStringBuilder.append("	, round(vni.vni1m, 3) vni1m	");
    queryStringBuilder.append("	, round(vni.vni3m, 3) vni3m	");
    queryStringBuilder.append("	, round(vni.vni6m, 3) vni6m	");
    queryStringBuilder.append("	, sh.Count_Holding as countHolding	");
    queryStringBuilder.append("	, round(sh.[Percentage], 3) as percentHoling	");
    queryStringBuilder.append("	, CASE WHEN :lang = 'vi' THEN longName	");
    queryStringBuilder.append("	ELSE englishLongName	");
    queryStringBuilder.append("	END AS companyName	");
    queryStringBuilder.append("	, CASE WHEN :lang = 'vi' THEN namelv2	");
    queryStringBuilder.append("	ELSE nameEnlv2	");
    queryStringBuilder.append("	END AS industryName	");
    queryStringBuilder.append("	, com.exchangeName	");
    queryStringBuilder.append("	, round(com.Stock_Rating, 3) stockRating	");
    queryStringBuilder.append("	FROM	");
    queryStringBuilder.append("	(	");
    queryStringBuilder.append("	SELECT TOP(10) * FROM Smy_dwh_stox_CompanyIndustryRate	");
    queryStringBuilder.append("	WHERE IdLevel2 =  (SELECT IdLevel2 FROM Smy_dwh_stox_CompanyIndustryRate WHERE ticker = :ticker)	");
    queryStringBuilder.append("	AND exchangeId IN (0, 1, 3)	");
    queryStringBuilder.append("	ORDER BY Stock_Rating DESC	");
    queryStringBuilder.append("	) com	");
    queryStringBuilder.append("	LEFT JOIN StockCorrelation sr on com.ticker = sr.[Other Ticker]	");
    queryStringBuilder.append("	LEFT JOIN Prc_Stock_CAPM sc on com.ticker = sc.ticker	");
    queryStringBuilder.append("	LEFT JOIN Smy_dwh_stox_VNI_StockPrice_Change vni on com.ticker = vni.ticker	");
    queryStringBuilder.append("	LEFT JOIN StockHoldCount sh on com.ticker = sh.Stock_Ticker	");
    queryStringBuilder.append("	LEFT JOIN tca_ratio_latest lt ON com.ticker = lt.Ticker	");
    queryStringBuilder.append("	WHERE sr.Ticker = :ticker	");
    queryStringBuilder.append("	AND vni.etlcurdate = (SELECT max(Etlcurdate) FROM Smy_dwh_stox_VNI_StockPrice_Change)	");
    queryStringBuilder.append("	AND sc.datereport = (SELECT max(datereport) FROM Prc_Stock_CAPM)	");

    resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", ticker)
      .setParameter("lang", lang)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    TcAnalysis.tcaDbConnection.closeSession();
    return resultList;
  }
}
