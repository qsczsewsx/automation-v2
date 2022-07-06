package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExDividendStock {

  @Step("Get data")
  public static List<HashMap<String, Object>> getExDividend(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select sdscir.ticker as symbol, ex.EXDATE as exdividendDate, ex.event, sdscir.exchangeName as exchangeName, ");
    queryBuilder.append(" Stock_Rating as Stock_Rating , sdscir.NameLv2 as NameLv2, sdscir.NameEnLv2 as NameEnLv2, ex.stockRatio  ");
    queryBuilder.append(" from SMY_DWH_STOX_COMPANYINDUSTRYRATE sdscir  ");
    queryBuilder.append(" left join ( SELECT EXDATE, SYMBOL, LISTAGG(CATYPE,',') as event,  LISTAGG(ICAL_STOCK_RATIO, ';') as stockRatio  ");
    queryBuilder.append("             FROM SMY_DWH_FLX_STOCKEVENT ");
    queryBuilder.append("             WHERE EXDATE  BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD') ");
    queryBuilder.append("             AND EtlCurDate = (SELECT MAX(EtlCurDate) FROM SMY_DWH_FLX_STOCKEVENT) ");
    queryBuilder.append("             group by SYMBOL, EXDATE ) ex on ex.SYMBOL = sdscir.ticker");
    queryBuilder.append(" where sdscir.exchangeId in (0,1,3)  ");
    queryBuilder.append(" group by ticker, EXDATE, exchangeName, Stock_Rating, NameLv2, NameEnLv2, event, stockRatio ");
    queryBuilder.append("  order by sdscir.ticker ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getExDividendLean(String fromDate, String toDate, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT s, TO_CHAR(exDate,'DD/MM/YY')  exDate, ex, sr, ind ");
    queryBuilder.append(" 		, CASE WHEN exDate IS NOT NULL ");
    queryBuilder.append(" 			THEN ");
    queryBuilder.append(" 				CONCAT( ");
    queryBuilder.append(" 					CONCAT(TO_CHAR(exDate,'DD/MM/YY') , ' : ')  ");
    queryBuilder.append(" 			 		, LISTAGG( CONCAT(e, CONCAT(' ', ratio)), '| ')  ");
    queryBuilder.append(" 						WITHIN GROUP (ORDER BY exDate) ");
    queryBuilder.append(" 			  	)  ");
    queryBuilder.append(" 		  	ELSE NULL END AS e ");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append(" 	SELECT C.TICKER AS s ");
    queryBuilder.append(" 			, C.EXCHANGENAME AS ex ");
    queryBuilder.append(" 			, C.ICBCODELV2 AS ind ");
    queryBuilder.append(" 			, ROUND(C.STOCK_RATING ,1) AS sr ");
    queryBuilder.append(" 			, EX.EXRIGHT_DATE AS exDate ");
    queryBuilder.append(" 			, EX.EVENT_NAME AS e ");
    queryBuilder.append(" 			, EX.RAW_RATIO  AS ratio ");
    queryBuilder.append(" 	FROM SMY_DWH_STX_COMPANY_INDUSTRY_RATE C ");
    queryBuilder.append(" 	LEFT JOIN ( ");
    queryBuilder.append(" 		SELECT EM.MARKET_CODE AS TICKER  ");
    queryBuilder.append(" 		 	, EM.EVENT_DATE AS EXRIGHT_DATE ");
    queryBuilder.append(" 		 	, CASE WHEN :lang = 'en' OR :lang = 'EN' THEN C.EVENT_NAME_EN ");
    queryBuilder.append(" 				ELSE  C.EVENT_NAME_VI  ");
    queryBuilder.append(" 				END  AS EVENT_NAME ");
    queryBuilder.append(" 			, CASE WHEN C.EVENT_CODE ='DIV010' ");
    queryBuilder.append(" 				THEN CONCAT(ES.RATIO * 100, '%') ");
    queryBuilder.append(" 				WHEN ES.RAW_RATIO IS NULL THEN NULL ELSE REPLACE(CONCAT('(', CONCAT(ES.RAW_RATIO, ')')), '/' , ':') ");
    queryBuilder.append(" 			END AS RAW_RATIO ");
    queryBuilder.append(" 		FROM ICAL_EV_MARKET EM ");
    queryBuilder.append(" 		INNER JOIN ICAL_EV_CONFIG C ");
    queryBuilder.append(" 			ON EM.CONF_ID = C.ID ");
    queryBuilder.append(" 		LEFT JOIN ICAL_EVM_EXT_STOCK ES  ");
    queryBuilder.append(" 		 	ON EM.MARKET_CODE = ES.TICKER  ");
    queryBuilder.append(" 		 	AND EM.EVENT_DATE = ES.EXRIGHT_DATE  ");
    queryBuilder.append(" 		 	AND EM.EVENT_CODE = ES.EVENT_CODE ");
    queryBuilder.append(" 		WHERE C.EVENT_TYPE = 'STOCK' AND C.PARENT_ID = 1 AND C.ACTIVE = 1 ");
    queryBuilder.append(" 		AND EM.EVENT_DATE BETWEEN TO_DATE(:fromDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD') ");
    queryBuilder.append(" 	) EX ON C.TICKER = EX.TICKER ");
    queryBuilder.append(" 	WHERE C.TICKER IS NOT NULL AND C.EXCHANGENAME IN ('HOSE', 'HNX', 'UpCom') ");
    queryBuilder.append(" ) TBL ");
    queryBuilder.append(" GROUP BY s, exDate, ex, sr, ind ");
    queryBuilder.append(" ORDER BY s ASC ");
    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
