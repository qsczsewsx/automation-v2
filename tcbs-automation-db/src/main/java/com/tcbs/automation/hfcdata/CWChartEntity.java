package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CWChartEntity {
  private static Logger logger = LoggerFactory.getLogger(CWChartEntity.class);

  @Step("Get data")
  public static List<HashMap<String, Object>> getStrikePriceAndConversionRatio(String symbol) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(
      " SELECT CW_SYMBOL, CK_COSO AS BASE_SYMBOL, CAST(trim(regexp_substr(RATIO , '[^:]+', 1)) AS FLOAT) AS RATIO, STRIKE_PRICE, CREATEDATE, NGAYDAOHAN AS MATURITY_DATE, NGAYGD_DAUTIEN  AS FIRST_TRADING_DAY FROM ( ");
    queryBuilder.append(" SELECT CW_SYMBOL, CK_COSO,  ");
    queryBuilder.append(" CASE WHEN TLCD_DIEUCHINH IS NULL THEN TILECHUYENDOI ELSE TLCD_DIEUCHINH END AS RATIO, ");
    queryBuilder.append(" CASE WHEN GIATH_DIEUCHINH IS NULL THEN GIATHUCHIEN ELSE GIATH_DIEUCHINH END AS STRIKE_PRICE, CREATEDATE, NGAYDAOHAN, NGAYGD_DAUTIEN   ");
    queryBuilder.append(" FROM VIETSTOCK_CW_INFO ");
    queryBuilder.append(" WHERE CW_SYMBOL = :p_cw_symbol ) TBL  ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_cw_symbol", symbol)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getCWLatestSigma(String baseTicker, String cwSymbol) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT * FROM ANNUALIZEDSIGMA  ");
    queryBuilder.append(" WHERE UNDERLYINGSYMBOL = :p_base_ticker  ");
    queryBuilder.append(" AND LAST_TRADINGDATE = (SELECT MAX(LAST_TRADINGDATE) FROM ANNUALIZEDSIGMA WHERE UNDERLYINGSYMBOL = :p_cw_symbol) ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_base_ticker", baseTicker)
        .setParameter("p_cw_symbol", cwSymbol)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
