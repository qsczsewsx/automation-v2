package com.tcbs.automation.hfcdata.icalendar.market;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tools.DateUtils;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class MarketEventEntity implements Serializable {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();
  public static final String WHERE = " WHERE ";
  public static final String WHERE_TCBSID_TCBS_ID = " WHERE TCBSID = :tcbsId ";
  public static final String WHERE_EVENT_DATE_BETWEEN_TO_DATE_START_YYYY_MM_DD_AND_TO_DATE_END_YYYY_MM_DD = "  WHERE EVENT_DATE BETWEEN TO_DATE(:start, 'yyyy-MM-dd') AND TO_DATE(:end, 'yyyy-MM-dd') ";
  public static final String WHERE_EVENT_DATE_BETWEEN_TO_DATE_FROM_DATE_YYYY_MM_DD_AND_TO_DATE_TO_DATE_YYYY_MM_DD = " WHERE EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ";
  public static final String UNION = " UNION ";
  public static final String FROM_DATE = "fromDate";
  public static final String TO_DATE = "toDate";
  public static final String EVENT_DATE = "EVENT_DATE";
  public static final String NULL_AS_CW_NAME_NULL_AS_STOCK_CODE_NULL_AS_STOCK_COMPANY_NAME = "  NULL AS CW_NAME, NULL AS STOCK_CODE, NULL AS STOCK_COMPANY_NAME, ";
  public static final String NULL_AS_ISSUER_NAME_NULL_AS_STRIKE_PRICE_NULL_AS_TERM_NULL_AS_CONVERSION_RATIO = "  NULL AS ISSUER_NAME, NULL AS STRIKE_PRICE, NULL AS TERM, NULL AS CONVERSION_RATIO, ";
  public static final String NULL_AS_REGISTRATION_VOLUME_NULL_AS_LISTED_DATE_NULL_AS_FIRST_TRADING_DATE = "  NULL AS REGISTRATION_VOLUME, NULL AS LISTED_DATE, NULL AS FIRST_TRADING_DATE, ";
  public static final String NULL_AS_MATURITY_DATE_NULL_AS_LAST_TRADING_DATE = "  NULL AS MATURITY_DATE, NULL AS LAST_TRADING_DATE ";
  public static final String FROM_EVENT = "  FROM event ";
  public static final String CASE = ", CASE";
  public static final String P_FROM_DATE = "p_fromDate";
  public static final String P_TO_DATE = "p_toDate";
  public static final String RECORD_DATE = "RECORD_DATE";
  public static final String EXRIGHT_DATE = "EXRIGHT_DATE";
  public static final String ISSUE_DATE = "ISSUE_DATE";
  public static final String ISSUE_YEAR = "ISSUE_YEAR";
  public static final String RATIO = "RATIO";
  public static final String VALUE = "VALUE";
  public static final String CW_CODE = "CW_CODE";
  public static final String CW_NAME = "CW_NAME";
  public static final String STOCK_CODE = "STOCK_CODE";
  public static final String STRIKE_PRICE = "STRIKE_PRICE";
  public static final String TERM = "TERM";
  public static final String CONVERSION_RATIO = "CONVERSION_RATIO";
  public static final String REGISTRATION_VOLUME = "REGISTRATION_VOLUME";
  public static final String MARKET_CODE = "MARKET_CODE";
  private String tcbsid;
  private String listStocksCode;

  public MarketEventEntity(String tcbsid, String listStocksCode) {
    this.tcbsid = tcbsid;
    this.listStocksCode = listStocksCode;
  }

  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  public String getListStocksCode() {
    return listStocksCode;
  }

  public void setListStocksCode(String listStocksCode) {
    this.listStocksCode = listStocksCode;
  }

  public static final HibernateEdition REDSHIFT = Database.REDSHIFT_STAGING.getConnection();

  public static List<String> getListTickerInWatchList(String tcbsId) {
    List<String> result = new ArrayList<>();
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT LIST_TICKER_FOCUS FROM HFC_DATA_SIT.ICAL_EVM_FIL_WATCHLIST_INFO WHERE TCBSID = :p_tcbsId");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_tcbsId", tcbsId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (resultList == null) {
        return new ArrayList<>();
      }
      for (HashMap<String, Object> item : resultList) {
        String listTickerFocus = (String) item.get("LIST_TICKER_FOCUS");
        String[] listTicker = listTickerFocus.split(",");
        for (String ticker : listTicker) {
          result.add(ticker);
        }
      }
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data short watch list")
  public static List<HashMap<String, Object>> getEventShortFromDB(String tcbsId, String src,
                                                                  String fromDate, String toDate) {

    List<String> listTickerFocus = getListTickerInWatchList(tcbsId);

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TO_CHAR(EVENT_DATE, 'YYYY-MM-DD') AS EVENT_DATE, DEF_TYPE, MARKET_CODE ");
    queryBuilder.append(" FROM ICAL_EV_MARKET ");
    queryBuilder.append(WHERE_EVENT_DATE_BETWEEN_TO_DATE_FROM_DATE_YYYY_MM_DD_AND_TO_DATE_TO_DATE_YYYY_MM_DD);
    if (src.equalsIgnoreCase("ALL")) {

      queryBuilder.append(UNION);
      queryBuilder.append(" SELECT TO_CHAR(EVENT_DATE, 'YYYY-MM-DD') AS EVENT_DATE , DEF_TYPE , 'NULL' AS MARKET_CODE ");
      queryBuilder.append(" FROM ICAL_EV_HOLIDAY ");
      queryBuilder.append(" WHERE EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')   ");
    } else if (src.equalsIgnoreCase("WL")) {
      queryBuilder.append(" AND MARKET_CODE IN :tickers ");
    }

    try {
      if (src.equalsIgnoreCase("ALL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else if (src.equalsIgnoreCase("WL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameterList("tickers", listTickerFocus)
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data short watch list")
  public static List<HashMap<String, Object>> getEventFullFromDB(String tcbsId, String src, String lang,
                                                                 String fromDate, String toDate) {

    List<String> listTickerFocus = getListTickerInWatchList(tcbsId);

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TO_CHAR(EVENT_DATE, 'YYYY-MM-DD') AS EVENT_DATE, EMK.DEF_TYPE, MARKET_CODE,CONF.EVENT_CODE");
    queryBuilder.append(" , CASE WHEN :lang = 'EN' THEN EVENT_NAME_EN ELSE EVENT_NAME_VI END AS EVENT_NAME ");
//    queryBuilder.append(" , CASE WHEN :lang = 'EN' THEN EVENT_DESC_EN ELSE EVENT_DESC_VI END AS EVENT_DESC ");
    queryBuilder.append(" FROM ICAL_EV_MARKET EMK ");
    queryBuilder.append(" INNER JOIN ICAL_EV_CONFIG CONF ");
    queryBuilder.append(" ON CONF.ID = EMK.CONF_ID AND ACTIVE = 1 ");
    queryBuilder.append(WHERE_EVENT_DATE_BETWEEN_TO_DATE_FROM_DATE_YYYY_MM_DD_AND_TO_DATE_TO_DATE_YYYY_MM_DD);
    if (src.equalsIgnoreCase("ALL")) {

      queryBuilder.append(UNION);
      queryBuilder.append(" SELECT TO_CHAR(EVENT_DATE, 'YYYY-MM-DD') AS EVENT_DATE , DEF_TYPE , 'NULL' AS MARKET_CODE,'NULL' AS EVENT_CODE,  ");
      queryBuilder.append(" CASE WHEN :lang = 'EN' THEN 'Holiday' ELSE 'Ngày nghỉ lễ' END AS EVENT_NAME ");
//      queryBuilder.append(" CASE WHEN :lang = 'EN' THEN 'Holiday' ELSE 'Ngày nghỉ lễ' END AS EVENT_DESC ");
      queryBuilder.append(" FROM ICAL_EV_HOLIDAY ");
      queryBuilder.append(" WHERE EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')   ");
    } else if (src.equalsIgnoreCase("WL")) {
      queryBuilder.append(" AND MARKET_CODE IN :tickers ");
    }

    try {
      if (src.equalsIgnoreCase("ALL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setParameter("lang", lang)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else if (src.equalsIgnoreCase("WL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameterList("tickers", listTickerFocus)
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setParameter("lang", lang)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data StockInfor")
  public static List<HashMap<String, Object>> getStockInfor(String fromDate, String toDate, String lang) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TO_CHAR(EXRIGHT_DATE , 'YYYY-MM-DD') AS EXRIGHT_DATE,TICKER , EVENT_CODE ,RECORD_DATE ,ISSUE_DATE ,ISSUE_YEAR");
    queryBuilder.append(",DIVIDEND_STAGE_CODE ,ISSUEPRICE ,PLANVOLUMN ,DIVIDENDYEAR , CASE WHEN :lang = 'en' THEN EN_EVENTTITLE " +
      "ELSE EVENTTITLE END AS EVENTTITLE , UPPER(COMGROUPCODENEW) AS COMGROUPCODENEW, UPPER(COMGROUPCODEPREVIOUS) AS COMGROUPCODEPREVIOUS  from ICAL_EVM_EXT_STOCK ");
    queryBuilder.append(" WHERE EXRIGHT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");

    try {
      return hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(FROM_DATE, fromDate)
        .setParameter(TO_DATE, toDate)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data CWInfor")
  public static List<HashMap<String, Object>> getCwInfor(String fromDate, String toDate) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT CW_CODE,CW_NAME,TO_CHAR(MATURITY_DATE , 'YYYY-MM-DD') AS MATURITY_DATE "
      + ",STOCK_CODE , STOCK_COMPANY_NAME , ISSUER_NAME, STRIKE_PRICE, TERM,CONVERSION_RATIO,REGISTRATION_VOLUME"
      + ",TO_CHAR(ISSUED_DATE , 'YYYY-MM-DD') AS ISSUED_DATE "
      + ",TO_CHAR(LISTED_DATE , 'YYYY-MM-DD') AS LISTED_DATE "
      + ",TO_CHAR(ADDITIONAL_ISSUED_DATE , 'YYYY-MM-DD') AS ADDITIONAL_ISSUED_DATE "
      + ",TO_CHAR(FIRST_TRADING_DATE , 'YYYY-MM-DD') AS FIRST_TRADING_DATE "
      + ",TO_CHAR(LAST_TRADING_DATE , 'YYYY-MM-DD') AS LAST_TRADING_DATE "
      + " FROM ICAL_EVM_EXT_CW ");
    queryBuilder.append(" WHERE CW_CODE IN (SELECT DISTINCT MARKET_CODE FROM ICAL_EV_MARKET WHERE EVENT_TYPE = 'COVWR' AND EVENT_DATE ");
    queryBuilder.append(" BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd'))");
    try {
      return hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(FROM_DATE, fromDate)
        .setParameter(TO_DATE, toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data all event")
  public static List<HashMap<String, Object>> getEventDetaiALLEventFromDB(String tcbsId,
                                                                          String fromDate,
                                                                          String toDate,
                                                                          String timeFrame,
                                                                          String lang) {
    StringBuilder startDay = new StringBuilder(fromDate);
    StringBuilder endDay = new StringBuilder(toDate);
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT EMK.DEF_TYPE " +
      ", EMK.MARKET_CODE" +
      ", EMK.EVENT_DATE  " +
      ", CONF.EVENT_TYPE" +
      ", CONF.EVENT_CODE" +
      CASE +
      "     WHEN :p_lang = 'EN' THEN EVENT_NAME_EN " +
      "     ELSE EVENT_NAME_VI" +
      "  END AS EVENT_NAME" +
      CASE +
      "     WHEN :p_lang = 'EN' THEN EVENT_DESC_EN " +
      "     ELSE EVENT_DESC_VI" +
      "  END AS EVENT_DESC " +
      "FROM ICAL_EV_MARKET EMK" +
      " INNER JOIN ICAL_EV_CONFIG CONF" +
      " ON CONF.ID = EMK.CONF_ID AND ACTIVE = 1" +
      " WHERE EVENT_DATE BETWEEN TO_DATE(:p_fromDate, 'yyyy-MM-dd') AND TO_DATE(:p_toDate, 'yyyy-MM-dd')");


    StringBuilder stockInfor = new StringBuilder();
    stockInfor.append("SELECT TICKER" +
      ", EVENT_CODE" +
      ", RECORD_DATE" +
      ", EXRIGHT_DATE" +
      ", ISSUE_DATE" +
      ", ISSUE_YEAR" +
      ", RATIO" +
      ", VALUE" +
      " FROM ICAL_EVM_EXT_STOCK " +
      " WHERE RECORD_DATE BETWEEN TO_DATE(:p_fromDate, 'yyyy-MM-dd') AND TO_DATE(:p_toDate, 'yyyy-MM-dd')");


    StringBuilder cwInfor = new StringBuilder();
    cwInfor.append("SELECT * FROM ICAL_EVM_EXT_CW " +
      "WHERE LISTED_DATE BETWEEN TO_DATE(:p_fromDate, 'yyyy-MM-dd') AND TO_DATE(:p_toDate, 'yyyy-MM-dd') " +
      "OR ADDITIONAL_ISSUED_DATE BETWEEN TO_DATE(:p_fromDate, 'yyyy-MM-dd') AND TO_DATE(:p_toDate, 'yyyy-MM-dd')");


    StringBuilder holidayQueryString = new StringBuilder();
    holidayQueryString.append("SELECT CONF.DEF_TYPE" +
      ", EVENT_TYPE" +
      ", EVENT_DATE" +
      CASE +
      "     WHEN :p_lang = 'EN' THEN EVENT_NAME_EN " +
      "     ELSE EVENT_NAME_VI" +
      "  END AS EVENT_NAME" +
      CASE +
      "     WHEN :p_lang = 'EN' THEN EVENT_DESC_EN " +
      "     ELSE EVENT_DESC_VI" +
      "  END AS EVENT_DESC " +
      " FROM ICAL_EV_HOLIDAY HO " +
      " INNER JOIN ICAL_EV_CONFIG CONF" +
      " ON HO.CONF_ID = CONF.ID " +
      " WHERE EVENT_DATE BETWEEN TO_DATE(:p_fromDate, 'yyyy-MM-dd') AND TO_DATE(:p_toDate, 'yyyy-MM-dd')" +
      " AND ACTIVE = 1 " +
      " ORDER BY EVENT_DATE DESC OFFSET 0 ROWS FETCH NEXT 999 ROWS ONLY");
    try {
      List<HashMap<String, Object>> allEvent = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_lang", lang)
        .setParameter(P_FROM_DATE, startDay.toString())
        .setParameter(P_TO_DATE, endDay.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();


      List<HashMap<String, Object>> stockInforList = hfc.getSession().createNativeQuery(stockInfor.toString())
        .setParameter(P_FROM_DATE, startDay.toString())
        .setParameter(P_TO_DATE, endDay.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      List<HashMap<String, Object>> cwInforList = hfc.getSession().createNativeQuery(cwInfor.toString())
        .setParameter(P_FROM_DATE, startDay.toString())
        .setParameter(P_TO_DATE, endDay.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      List<HashMap<String, Object>> holidayList = hfc.getSession().createNativeQuery(holidayQueryString.toString())
        .setParameter("p_lang", lang)
        .setParameter(P_FROM_DATE, startDay.toString())
        .setParameter(P_TO_DATE, endDay.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return convert(allEvent, stockInforList, cwInforList, holidayList);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HashMap<String, Object>> convert(List<HashMap<String, Object>> allEvents,
                                                       List<HashMap<String, Object>> stockInfor,
                                                       List<HashMap<String, Object>> cwInfor,
                                                       List<HashMap<String, Object>> listHoliday) {
    List<HashMap<String, Object>> result = new ArrayList<>();

    HashMap<String, List<HashMap<String, Object>>> mapStockInfor = map(stockInfor);
    HashMap<String, List<HashMap<String, Object>>> mapCwInfor = mapCWList(cwInfor);
    for (HashMap<String, Object> item : listHoliday) {
      String eventDate = DateUtils.toISOStringWithoutTime((Date) item.get(EVENT_DATE), "");
      item.put(EVENT_DATE, eventDate);
      item.put(RECORD_DATE, null);
      item.put(EXRIGHT_DATE, null);
      item.put(ISSUE_DATE, null);
      item.put(ISSUE_YEAR, null);
      item.put(RATIO, null);
      item.put(VALUE, null);
      item.put(CW_CODE, null);
      item.put(CW_NAME, null);
      item.put(STOCK_CODE, null);
      item.put(STRIKE_PRICE, null);
      item.put(TERM, null);
      item.put(CONVERSION_RATIO, null);
      item.put(REGISTRATION_VOLUME, null);
      item.put(MARKET_CODE, "HOLIDAY");
      item.remove("EVENT_TYPE");
      item.remove("EVENT_DESC");
      result.add(item);
    }

    for (HashMap<String, Object> item : allEvents) {
      String keyMapEventsStock = item.get("EVENT_CODE").toString().concat(item.get(EVENT_DATE).toString()).concat(item.get(MARKET_CODE).toString());
      List<HashMap<String, Object>> mapStock = mapStockInfor.get(keyMapEventsStock);
      if (mapStock == null) {
        item.put(RECORD_DATE, null);
        item.put(EXRIGHT_DATE, null);
        item.put(ISSUE_DATE, null);
        item.put(ISSUE_YEAR, null);
        item.put(RATIO, null);
        item.put(VALUE, null);
      } else {
        item.put(RECORD_DATE, mapStock.get(0).get(RECORD_DATE));
        item.put(EXRIGHT_DATE, mapStock.get(0).get(EXRIGHT_DATE));
        item.put(ISSUE_DATE, mapStock.get(0).get(ISSUE_DATE));
        item.put(ISSUE_YEAR, mapStock.get(0).get(ISSUE_YEAR));
        item.put(RATIO, mapStock.get(0).get(RATIO));
        item.put(VALUE, mapStock.get(0).get(VALUE));
      }


      String keyMapEventsCW = item.get(MARKET_CODE).toString();
      List<HashMap<String, Object>> mapCw = mapCwInfor.get(keyMapEventsCW);
      if (mapCw == null) {
        item.put(CW_CODE, null);
        item.put(CW_NAME, null);
        item.put(STOCK_CODE, null);
        item.put(STRIKE_PRICE, null);
        item.put(TERM, null);
        item.put(CONVERSION_RATIO, null);
        item.put(REGISTRATION_VOLUME, null);
      } else {
        item.put(CW_CODE, mapCw.get(0).get(CW_CODE));
        item.put(CW_NAME, mapCw.get(0).get(CW_NAME));
        item.put(STOCK_CODE, mapCw.get(0).get(STOCK_CODE));
        item.put(STRIKE_PRICE, mapCw.get(0).get(STRIKE_PRICE));
        item.put(TERM, mapCw.get(0).get(TERM));
        item.put(CONVERSION_RATIO, mapCw.get(0).get(CONVERSION_RATIO));
        item.put(REGISTRATION_VOLUME, mapCw.get(0).get(REGISTRATION_VOLUME));
      }
      item.remove("EVENT_TYPE");
      item.remove("EVENT_DESC");
      String eventDate = DateUtils.toISOStringWithoutTime((Date) item.get(EVENT_DATE), "");
      item.put(EVENT_DATE, eventDate);
      result.add(item);
    }
    return result;
  }

  private static HashMap<String, List<HashMap<String, Object>>> map(List<HashMap<String, Object>> stockListInfor) {
    HashMap<String, List<HashMap<String, Object>>> result = new HashMap<>();
    for (HashMap<String, Object> item : stockListInfor) {
      String keyMapEvents = item.get("EVENT_CODE").toString().concat(item.get(RECORD_DATE).toString()).concat(item.get("TICKER").toString());
      if (result.get(keyMapEvents) == null) {
        List<HashMap<String, Object>> listItem = new ArrayList<>();
        listItem.add(item);
        result.put(keyMapEvents, listItem);
      } else {
        result.get(keyMapEvents).add(item);
      }
    }
    return result;
  }

  private static HashMap<String, List<HashMap<String, Object>>> mapCWList(List<HashMap<String, Object>> cwInforList) {
    HashMap<String, List<HashMap<String, Object>>> result = new HashMap<>();
    for (HashMap<String, Object> item : cwInforList) {
      String keyMapEvents = item.get(CW_CODE).toString();
      if (result.get(keyMapEvents) == null) {
        List<HashMap<String, Object>> listItem = new ArrayList<>();
        listItem.add(item);
        result.put(keyMapEvents, listItem);
      } else {
        result.get(keyMapEvents).add(item);
      }
    }
    return result;
  }

  @Step("Get event date ")
  public static List<HashMap<String, Object>> getEventDate(List<String> tickers, String src, String fromDate, String toDate,
                                                           Integer page, Integer size) {
    StringBuilder queryBuilder = new StringBuilder();
    String allQuery = " GROUP BY EVENT_DATE "
      + UNION
      + " SELECT EVENT_DATE FROM ICAL_EV_HOLIDAY iem "
      + WHERE_EVENT_DATE_BETWEEN_TO_DATE_FROM_DATE_YYYY_MM_DD_AND_TO_DATE_TO_DATE_YYYY_MM_DD;
    String wlQuery = " and MARKET_CODE IN :watchList ";
    queryBuilder.append(" SELECT TO_CHAR(EVENT_DATE, 'YYYY-MM-DD') as EVENT_DATE ");
    queryBuilder.append("     FROM ( SELECT EVENT_DATE FROM ICAL_EV_MARKET iem ");
    queryBuilder.append("     WHERE EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    if (src.equalsIgnoreCase("ALL")) {
      queryBuilder.append(allQuery);
    } else if (src.equalsIgnoreCase("WL")) {
      queryBuilder.append(wlQuery);
    }
    queryBuilder.append(" GROUP BY EVENT_DATE ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ORDER BY EVENT_DATE  ");
    queryBuilder.append(" OFFSET :page ROWS FETCH NEXT :size ROWS ONLY ");
    try {
      if (src.equalsIgnoreCase("WL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setParameter("page", page * size)
          .setParameter("size", size)
          .setParameter("watchList", tickers)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else if (src.equalsIgnoreCase("ALL")) {
        return hfc.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(FROM_DATE, fromDate)
          .setParameter(TO_DATE, toDate)
          .setParameter("page", page * size)
          .setParameter("size", size)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get event date ")
  public static List<HashMap<String, Object>> getAllEventData(String tcbsId, String src, String fromDate, String toDate,
                                                              Integer page, Integer size, String lang) {
    List<String> listTickerFocus = getListTickerInWatchList(tcbsId);
    List<HashMap<String, Object>> listEventDate = MarketEventEntity.getEventDate(listTickerFocus, src, fromDate, toDate, page, size);
    if (listEventDate.size() > 0) {
      String start = listEventDate.get(0).get(EVENT_DATE).toString();
      String end = listEventDate.get(listEventDate.size() - 1).get(EVENT_DATE).toString();

      StringBuilder queryBuilder = new StringBuilder();

      queryBuilder.append("  WITH event AS ( ");
      queryBuilder.append("    SELECT EMK.DEF_TYPE, EMK.MARKET_CODE, EMK.EVENT_DATE, CONF.EVENT_TYPE, CONF.EVENT_CODE ");
      queryBuilder.append("    , CASE WHEN :lang = 'EN' THEN EVENT_NAME_EN ELSE EVENT_NAME_VI END AS EVENT_NAME ");
      queryBuilder.append("    , CASE WHEN :lang = 'EN' THEN EVENT_DESC_EN ELSE EVENT_DESC_VI END AS EVENT_DESC ");
      queryBuilder.append("  FROM ICAL_EV_MARKET EMK ");
      queryBuilder.append("  INNER JOIN ICAL_EV_CONFIG CONF ");
      queryBuilder.append("  ON CONF.ID = EMK.CONF_ID AND ACTIVE = 1 ");
      queryBuilder.append(WHERE_EVENT_DATE_BETWEEN_TO_DATE_START_YYYY_MM_DD_AND_TO_DATE_END_YYYY_MM_DD);
      queryBuilder.append("  GROUP BY EMK.DEF_TYPE, EMK.MARKET_CODE, EMK.EVENT_DATE, CONF.EVENT_TYPE, CONF.EVENT_CODE, EVENT_NAME_EN , EVENT_NAME_VI, EVENT_DESC_EN, EVENT_DESC_VI ) ");
      queryBuilder.append("  SELECT event.*,");
      queryBuilder.append("  EXRIGHT_DATE, ISSUE_DATE AS ISSUED_DATE, ISSUE_YEAR, RATIO, NULL as VALUE, ");
      queryBuilder.append(NULL_AS_CW_NAME_NULL_AS_STOCK_CODE_NULL_AS_STOCK_COMPANY_NAME);
      queryBuilder.append(NULL_AS_ISSUER_NAME_NULL_AS_STRIKE_PRICE_NULL_AS_TERM_NULL_AS_CONVERSION_RATIO);
      queryBuilder.append(NULL_AS_REGISTRATION_VOLUME_NULL_AS_LISTED_DATE_NULL_AS_FIRST_TRADING_DATE);
      queryBuilder.append(NULL_AS_MATURITY_DATE_NULL_AS_LAST_TRADING_DATE);
//Start
      queryBuilder.append(FROM_EVENT);
//End
      queryBuilder.append("  INNER JOIN ICAL_EVM_EXT_STOCK stock ");
      queryBuilder.append("  ON event.MARKET_CODE = stock.TICKER AND event.EVENT_DATE = stock.EXRIGHT_DATE AND stock.EVENT_CODE = event.EVENT_CODE ");
      if (src.equalsIgnoreCase("WL")) {
        queryBuilder.append("  WHERE MARKET_CODE IN :watchList ");
      } else if (src.equalsIgnoreCase("ALL")) {
        queryBuilder.append("  UNION SELECT event.*, ");
        queryBuilder.append("  NULL AS EXRIGHT_DATE, ISSUED_DATE, NULL AS ISSUE_YEAR, NULL AS RATIO, NULL AS VALUE, ");
        queryBuilder.append("    CW_NAME, STOCK_CODE, STOCK_COMPANY_NAME, ");
        queryBuilder.append("  CASE WHEN :lang = 'EN' THEN ISSUER_NAME ELSE ISSUER_NAME_EN END AS ISSUER_NAME, ");
        queryBuilder.append("    STRIKE_PRICE, TERM, CONVERSION_RATIO, REGISTRATION_VOLUME, ");
        queryBuilder.append("    LISTED_DATE, FIRST_TRADING_DATE, MATURITY_DATE, LAST_TRADING_DATE ");
        queryBuilder.append(FROM_EVENT);
        queryBuilder.append("  INNER JOIN ICAL_EVM_EXT_CW cw ");
        queryBuilder.append("  ON event.MARKET_CODE = cw.CW_CODE ");
        queryBuilder.append("  UNION SELECT DEF_TYPE, 'HOLIDAY' AS MARKET_CODE, EVENT_DATE, ");
        queryBuilder.append("  'HOLIDAY' AS EVENT_TYPE, 'HOLIDAY' AS EVENT_CODE, ");
        queryBuilder.append("  CASE WHEN :lang = 'EN' THEN 'Holiday' ELSE 'Ngày nghỉ lễ' END AS EVENT_NAME, ");
        queryBuilder.append("  CASE WHEN :lang = 'EN' THEN 'Holiday' ELSE 'Ngày nghỉ lễ' END AS EVENT_DESC, ");
        queryBuilder.append("  NULL AS EXRIGHT_DATE, NULL AS ISSUED_DATE, NULL AS ISSUE_YEAR, NULL AS RATIO, NULL AS VALUE, ");
        queryBuilder.append(NULL_AS_CW_NAME_NULL_AS_STOCK_CODE_NULL_AS_STOCK_COMPANY_NAME);
        queryBuilder.append(NULL_AS_ISSUER_NAME_NULL_AS_STRIKE_PRICE_NULL_AS_TERM_NULL_AS_CONVERSION_RATIO);
        queryBuilder.append(NULL_AS_REGISTRATION_VOLUME_NULL_AS_LISTED_DATE_NULL_AS_FIRST_TRADING_DATE);
        queryBuilder.append(NULL_AS_MATURITY_DATE_NULL_AS_LAST_TRADING_DATE);
        queryBuilder.append("  FROM ICAL_EV_HOLIDAY ");
        queryBuilder.append(WHERE_EVENT_DATE_BETWEEN_TO_DATE_START_YYYY_MM_DD_AND_TO_DATE_END_YYYY_MM_DD);
        queryBuilder.append("  UNION SELECT event.* ");
        queryBuilder.append(", NULL AS EXRIGHT_DATE, NULL AS ISSUED_DATE, NULL AS ISSUE_YEAR, NULL AS RATIO, NULL AS VALUE, ");
        queryBuilder.append(NULL_AS_CW_NAME_NULL_AS_STOCK_CODE_NULL_AS_STOCK_COMPANY_NAME);
        queryBuilder.append(NULL_AS_ISSUER_NAME_NULL_AS_STRIKE_PRICE_NULL_AS_TERM_NULL_AS_CONVERSION_RATIO);
        queryBuilder.append(NULL_AS_REGISTRATION_VOLUME_NULL_AS_LISTED_DATE_NULL_AS_FIRST_TRADING_DATE);
        queryBuilder.append(NULL_AS_MATURITY_DATE_NULL_AS_LAST_TRADING_DATE);
        queryBuilder.append(FROM_EVENT);
        queryBuilder.append("  WHERE EVENT_TYPE = 'FTURE' ");
        queryBuilder.append("  UNION SELECT DEF_TYPE, MARKET_CODE, EVENT_DATE, NULL AS EVENT_TYPE, NULL AS EVENT_CODE, ");
        queryBuilder.append("  NULL AS EVENT_NAME, NULL AS EVENT_DESC, NULL AS EXRIGHT_DATE, NULL AS ISSUED_DATE, ");
        queryBuilder.append("  NULL AS ISSUE_YEAR, NULL AS RATIO, NULL AS VALUE, NULL AS CW_NAME, NULL AS STOCK_CODE, ");
        queryBuilder.append("  NULL AS STOCK_COMPANY_NAME, NULL AS ISSUER_NAME, NULL AS STRIKE_PRICE, NULL AS TERM, ");
        queryBuilder.append("  NULL AS CONVERSION_RATIO, NULL AS REGISTRATION_VOLUME, NULL AS LISTED_DATE, ");
        queryBuilder.append("  NULL AS FIRST_TRADING_DATE,   NULL AS MATURITY_DATE, NULL AS LAST_TRADING_DATE ");
        queryBuilder.append("  FROM ICAL_EV_MARKET ");
        queryBuilder.append(WHERE_EVENT_DATE_BETWEEN_TO_DATE_START_YYYY_MM_DD_AND_TO_DATE_END_YYYY_MM_DD);
        queryBuilder.append("  AND DEF_TYPE LIKE '%icalendar.bond%' ");
      }
      try {
        if (src.equalsIgnoreCase("ALL")) {
          return hfc.getSession().createNativeQuery(queryBuilder.toString())
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("lang", lang)
            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
            .getResultList();
        } else if (src.equalsIgnoreCase("WL")) {
          return hfc.getSession().createNativeQuery(queryBuilder.toString())
            .setParameter("start", start)
            .setParameter("end", end)
            .setParameter("lang", lang)
            .setParameter("watchList", listTickerFocus)
            .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
            .getResultList();
        }

      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
      return new ArrayList<>();
    } else {
      return new ArrayList<>();
    }
  }

  public static List<HashMap<String, Object>> getEventAssetFromDB(String tcbsId, String lang, String fromDate, String toDate, String src,
                                                                  Integer page, Integer size) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("  WITH ASSET_STOCK_PAST AS ( ");
    queryBuilder.append("  SELECT TICKER, QUANTITY, MKTAMT, EVENT_DATE ");
    queryBuilder.append("  FROM ICAL_AS_STOCK_SNAPSHOT  ");
    queryBuilder.append("  WHERE TCBSID = :tcbsId ");
    queryBuilder.append("  AND EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')  ");
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_CW_PAST AS ( ");
    queryBuilder.append(" SELECT TICKER, QUANTITY, EVENT_DATE  ");
    queryBuilder.append(" FROM ICAL_AS_CW_SNAPSHOT   ");
    queryBuilder.append(" WHERE TCBSID = :tcbsId   ");
    queryBuilder.append(" AND EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')    ");
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_BOND AS ( ");
    queryBuilder.append(" SELECT BONDCODE AS TICKER ");
    queryBuilder.append(" , EVENTCODE ");
    queryBuilder.append(" , EVENTDATE AS EVENT_DATE ");
    queryBuilder.append(" , COUPONRATE ");
    queryBuilder.append(" , COUPONPAYMENTAMTAFTERTAX ");
    queryBuilder.append(" , OBALQUANT ");
    queryBuilder.append(" , OBALPARVALUE  ");

    queryBuilder.append(" FROM ICAL_AS_BOND_SNAPSHOT  ");
    queryBuilder.append(WHERE_TCBSID_TCBS_ID);
    queryBuilder.append(" AND EVENTDATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd')  ");
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_BOND_PRO AS ( ");
    queryBuilder.append(" SELECT TRADINGCODE ");
    queryBuilder.append(" , BONDCODE AS TICKER ");
    queryBuilder.append(" , EVENTDATE AS EVENT_DATE ");
    queryBuilder.append(" , RECEIVEAMTPROEXPIRED ");
    queryBuilder.append(" , INVESTMENTRATEPROEXPIRED");
    queryBuilder.append(" , RECEIVEAMTHTM");
    queryBuilder.append(" , INVESTMENTRATEHTM");
    queryBuilder.append(" , MATURITYDATE");
    queryBuilder.append(" FROM ICAL_AS_BOND_PRO_SNAPSHOT ");
    queryBuilder.append(" WHERE TCBSID = :tcbsId");
    queryBuilder.append(" AND EVENTDATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_STOCK_LATEST AS (  ");
    queryBuilder.append(" SELECT TICKER, QUANTITY, MKTAMT  ");
    queryBuilder.append(" FROM ICAL_AS_STOCK_LATEST  ");
    queryBuilder.append(WHERE_TCBSID_TCBS_ID);
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_CW_LATEST AS ( ");
    queryBuilder.append(" SELECT TICKER, QUANTITY  ");
    queryBuilder.append(" FROM ICAL_AS_CW_LATEST  ");
    queryBuilder.append(WHERE_TCBSID_TCBS_ID);
    queryBuilder.append(" )  ");

    queryBuilder.append(" , ASSET_CW_LATEST AS ( ");
    queryBuilder.append(" SELECT TICKER, QUANTITY  ");
    queryBuilder.append(" FROM ICAL_AS_CW_LATEST  ");
    queryBuilder.append(WHERE_TCBSID_TCBS_ID);
    queryBuilder.append(" )  ");

    queryBuilder.append(" , EVENT_STOCK_ALL AS (");
    queryBuilder.append(" SELECT EV.DEF_TYPE, EV.MARKET_CODE AS TICKER, EV.EVENT_CODE, EV.EVENT_DATE, EV.CONF_ID, ASS.QUANTITY, ASS.MKTAMT  ");
    queryBuilder.append(" FROM ICAL_EV_MARKET EV  ");
    queryBuilder.append(" INNER JOIN ASSET_STOCK_PAST ASS ");
    queryBuilder.append(" ON EV.MARKET_CODE = ASS.TICKER AND EV.EVENT_DATE = ASS.EVENT_DATE ");

    queryBuilder.append(" UNION SELECT EV.DEF_TYPE, EV.MARKET_CODE AS TICKER, EV.EVENT_CODE, EV.EVENT_DATE, EV.CONF_ID, ASS.QUANTITY, ASS.MKTAMT ");
    queryBuilder.append(" FROM ICAL_EV_MARKET EV LEFT JOIN ASSET_STOCK_LATEST ASS ON EV.MARKET_CODE = ASS.TICKER ");

    queryBuilder.append(" WHERE EVENT_TYPE = 'STOCK' AND TICKER IS NOT NULL AND EV.EVENT_DATE BETWEEN TRUNC(SYSDATE) AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    queryBuilder.append(" )  ");

    queryBuilder.append(" , EVENT_CW_ALL AS ( ");
    queryBuilder.append(" SELECT EV.DEF_TYPE , EV.MARKET_CODE AS TICKER, EV.EVENT_CODE, EV.EVENT_DATE, EV.CONF_ID , ASS.QUANTITY ");
    queryBuilder.append(" FROM ICAL_EV_MARKET EV INNER JOIN ASSET_CW_PAST ASS ON EV.MARKET_CODE = ASS.TICKER AND EV.EVENT_DATE = ASS.EVENT_DATE ");
    queryBuilder.append(" UNION SELECT EV.DEF_TYPE , EV.MARKET_CODE AS TICKER, EV.EVENT_CODE, EV.EVENT_DATE, EV.CONF_ID , ASS.QUANTITY ");

    queryBuilder.append(
      " FROM ICAL_EV_MARKET EV LEFT JOIN ASSET_CW_LATEST ASS ON EV.MARKET_CODE = ASS.TICKER WHERE EVENT_TYPE = 'COVWR' AND TICKER IS NOT NULL AND EV.EVENT_DATE BETWEEN TRUNC(SYSDATE)  AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    queryBuilder.append(" ) ");

    queryBuilder.append(" , EVENT_BOND_ALL AS ( ");
    queryBuilder.append(" SELECT EV.DEF_TYPE , EV.MARKET_CODE AS TICKER, EV.EVENT_DATE, EV.CONF_ID, EVENTCODE AS EVENT_CODE, COUPONRATE, COUPONPAYMENTAMTAFTERTAX, OBALQUANT, OBALPARVALUE ");
    queryBuilder.append(
      " FROM ICAL_EV_MARKET EV INNER JOIN ASSET_BOND ASS ON EV.MARKET_CODE = ASS.TICKER AND EV.EVENT_DATE = ASS.EVENT_DATE AND EV.EVENT_CODE = ASS.EVENTCODE WHERE EV.EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ");
    queryBuilder.append(" ) ");

    queryBuilder.append(" , EVENT_BOND_PRO_ALL AS ( ");
    queryBuilder.append(
      " SELECT CNF.DEF_TYPE, TICKER, EVENT_DATE, CNF.ID AS CONF_ID, CNF.EVENT_CODE, TRADINGCODE, RECEIVEAMTPROEXPIRED, INVESTMENTRATEPROEXPIRED, RECEIVEAMTHTM, INVESTMENTRATEHTM, MATURITYDATE ");

    queryBuilder.append(" FROM ASSET_BOND_PRO LEFT JOIN (SELECT * FROM ICAL_EV_CONFIG WHERE ID = 30) CNF ON 1 = 1 ");
    queryBuilder.append(" ) ");

    queryBuilder.append(" , EVENT_ASSET_ALL AS ( ");
    queryBuilder.append(
      " SELECT COALESCE(ST.CONF_ID, CW.CONF_ID, BO.CONF_ID, PR.CONF_ID) AS CONF_ID, COALESCE(ST.DEF_TYPE, CW.DEF_TYPE, BO.DEF_TYPE, PR.DEF_TYPE) AS DEF_TYPE, COALESCE(ST.TICKER, CW.TICKER, BO.TICKER, PR.TICKER) AS TICKER, ST.MKTAMT, COALESCE(ST.QUANTITY, CW.QUANTITY) AS QUANTITY, COALESCE(ST.EVENT_DATE, CW.EVENT_DATE, BO.EVENT_DATE, PR.EVENT_DATE) AS EVENT_DATE, COALESCE(ST.EVENT_CODE, CW.EVENT_CODE, BO.EVENT_CODE, PR.EVENT_CODE) AS EVENT_CODE, COUPONRATE");
    queryBuilder.append(" , COUPONPAYMENTAMTAFTERTAX, OBALQUANT, OBALPARVALUE, TRADINGCODE, RECEIVEAMTPROEXPIRED, INVESTMENTRATEPROEXPIRED, RECEIVEAMTHTM, INVESTMENTRATEHTM, MATURITYDATE ");
    queryBuilder.append(
      " FROM EVENT_STOCK_ALL ST FULL OUTER JOIN EVENT_CW_ALL CW ON ST.TICKER = CW.TICKER FULL OUTER JOIN EVENT_BOND_ALL BO ON ST.TICKER = BO.TICKER FULL OUTER JOIN EVENT_BOND_PRO_ALL PR ON BO.TICKER = PR.TICKER AND BO.EVENT_CODE = PR.EVENT_CODE ");

    queryBuilder.append(" ) ");
    queryBuilder.append(" , T_RANGE_DATE AS ( ");
    queryBuilder.append(
      " SELECT MIN(EVENT_DATE) AS MIN_EVENT_DATE, MAX(EVENT_DATE) AS MAX_EVENT_DATE FROM(SELECT DISTINCT EVENT_DATE FROM EVENT_ASSET_ALL ORDER BY EVENT_DATE OFFSET (:page * :size) ROWS FETCH NEXT :size ROWS ONLY) TBL  ");
    queryBuilder.append(" ) ");

    queryBuilder.append(
      " SELECT :tcbsId AS TCBSID, ASA.DEF_TYPE, ASA.TICKER AS MARKET_CODE, ASA.MKTAMT, ASA.QUANTITY, ASA.EVENT_DATE, ASA.EVENT_CODE, ASA.COUPONRATE, ASA.COUPONPAYMENTAMTAFTERTAX, ASA.OBALQUANT, ASA.OBALPARVALUE , ASA.TRADINGCODE, ASA.RECEIVEAMTPROEXPIRED, ASA.INVESTMENTRATEPROEXPIRED, ASA.RECEIVEAMTHTM, ASA.INVESTMENTRATEHTM ");
    queryBuilder.append(
      " , ST.RECORD_DATE, ST.EXRIGHT_DATE, ST.ISSUE_DATE, ST.ISSUE_YEAR, ST.RATIO, ST.VALUE"
        + ", ST.DIVIDEND_STAGE_CODE, ST.ADDRESS, ST.ISSUEPRICE, ST.SUBSCRIPTIONPERIODSTART, ST.SUBSCRIPTIONPERIODEND, ST.PAYOUTDATE, ST.ORGANNAME, ST.PLANVOLUMN, ST.DIVIDENDYEAR" +
        ",  CASE WHEN :lang = 'EN' THEN EN_EVENTTITLE ELSE EVENTTITLE END AS EVENTTITLE"
        + ", CW.CW_NAME, CW.STOCK_CODE, CW.STOCK_COMPANY_NAME, CW.ISSUER_NAME, CW.STRIKE_PRICE, CW.TERM, CW.CONVERSION_RATIO, CW.REGISTRATION_VOLUME, CW.ISSUED_DATE, CW.LISTED_DATE, CW.ADDITIONAL_ISSUED_DATE, CW.FIRST_TRADING_DATE, CW.MATURITY_DATE, CW.LAST_TRADING_DATE ");
    queryBuilder.append(
      " , BO.PAR, BO.REFERENCE_RATE_COUPON, BO.COUPONPERUNIT, BO.ISSUE_DATE AS BOND_ISSUE_DATE, COALESCE(BO.MATURITY_DATE,ASA.MATURITYDATE) AS BOND_MATURITY_DATE, CONF.EVENT_TYPE, CASE WHEN :lang = 'EN' THEN EVENT_NAME_EN ELSE EVENT_NAME_VI END AS EVENT_NAME, CASE WHEN :lang = 'EN' THEN EVENT_DESC_EN ELSE EVENT_DESC_VI END AS EVENT_DESC  ");
    queryBuilder.append(
      " FROM EVENT_ASSET_ALL ASA LEFT JOIN ICAL_EVM_EXT_STOCK ST ON ASA.TICKER = ST.TICKER AND ASA.EVENT_CODE = ST.EVENT_CODE AND ASA.EVENT_DATE = ST.EXRIGHT_DATE LEFT JOIN ICAL_EVM_EXT_CW CW ON ASA.TICKER = CW.CW_CODE AND (ASA.EVENT_DATE = CW.LISTED_DATE OR ASA.EVENT_DATE = CW.ADDITIONAL_ISSUED_DATE OR ASA.EVENT_DATE = CW.MATURITY_DATE OR ASA.EVENT_DATE = CW.LAST_TRADING_DATE)");

    queryBuilder.append(
      " LEFT JOIN ICAL_EVM_EXT_BOND BO ON ASA.TICKER = BO.BOND_CODE AND ASA.EVENT_CODE = BO.EVENT_CODE AND ASA.EVENT_DATE = BO.EXRIGHT_DATE LEFT JOIN ICAL_EV_CONFIG CONF ON ACTIVE = 1 AND CONF.EVENT_TYPE IN ('STOCK', 'COVWR', 'BOND') AND ASA.CONF_ID = CONF.ID WHERE EVENT_DATE BETWEEN (SELECT MIN_EVENT_DATE FROM T_RANGE_DATE) AND (SELECT MAX_EVENT_DATE FROM T_RANGE_DATE) AND EVENT_DATE BETWEEN TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ORDER BY ASA.EVENT_DATE ASC");
    try {
      return hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter("lang", lang)
        .setParameter(FROM_DATE, fromDate)
        .setParameter(TO_DATE, toDate)
        //.setParameter("src", src)
        .setParameter("page", page)
        .setParameter("size", size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getEventShortAssetFromDB(String tcbsId, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    String strBetween = " TO_DATE(:fromDate, 'yyyy-MM-dd') AND TO_DATE(:toDate, 'yyyy-MM-dd') ";
    String strToDate = " TO_DATE(:toDate, 'yyyy-MM-dd') ";
    String strWhereID = " TCBSID = :tcbsId ";
    String strCondition = WHERE + strWhereID + " AND EVENT_DATE BETWEEN " + strBetween;
    String strConditionEvDate = WHERE + strWhereID + " AND EVENTDATE BETWEEN " + strBetween;
    queryBuilder.append(" WITH EVENT_PAST AS ( ");
    queryBuilder.append(" SELECT TICKER, EVENT_DATE FROM ICAL_AS_STOCK_SNAPSHOT  ");
    queryBuilder.append(strCondition);
    queryBuilder.append(" UNION ALL SELECT TICKER, EVENT_DATE FROM ICAL_AS_CW_SNAPSHOT ");
    queryBuilder.append(strCondition);
    queryBuilder.append(" ) ");
    queryBuilder.append(" , CURRENT_ASSET AS ( ");
    queryBuilder.append(" SELECT DISTINCT TICKER FROM ICAL_AS_STOCK_LATEST ");
    queryBuilder.append(WHERE);
    queryBuilder.append(strWhereID + " AND DATEREPORT BETWEEN " + strBetween);
    queryBuilder.append(" UNION ALL SELECT DISTINCT TICKER FROM ICAL_AS_CW_LATEST   ");
    queryBuilder.append(WHERE);
    queryBuilder.append(strWhereID + " AND DATEREPORT BETWEEN " + strBetween);
    queryBuilder.append(" ) ");
    queryBuilder.append(" , EVENT_FUTURE AS ( ");
    queryBuilder.append(" SELECT MARKET_CODE AS TICKER, EVENT_DATE FROM ICAL_EV_MARKET  ");
    queryBuilder.append(" WHERE MARKET_CODE IN (SELECT TICKER FROM CURRENT_ASSET) ");
    queryBuilder.append(" AND EVENT_DATE BETWEEN TRUNC(SYSDATE)  AND " + strToDate);
    queryBuilder.append(" ) ");
    queryBuilder.append(" , EVENT_BOND_ALL AS ( ");
    queryBuilder.append(" SELECT BONDCODE, EVENTDATE FROM ICAL_AS_BOND_SNAPSHOT   ");
    queryBuilder.append(strConditionEvDate);

    queryBuilder.append(" ) ");
    queryBuilder.append(" , EVENT_BOND_PRO AS( ");
    queryBuilder.append(" SELECT BONDCODE , CNF.DEF_TYPE, EVENTDATE FROM ICAL_AS_BOND_PRO_SNAPSHOT BO ");
    queryBuilder.append(" LEFT JOIN (SELECT * FROM ICAL_EV_CONFIG WHERE ID = 30) CNF ON 1 = 1 ");
    queryBuilder.append(strConditionEvDate);
    queryBuilder.append(" ) ");

    queryBuilder.append(" , EVENT_ALL AS ( ");
    queryBuilder.append(" SELECT * FROM EVENT_PAST UNION ALL SELECT * FROM EVENT_FUTURE UNION ALL SELECT * FROM EVENT_BOND_ALL ");
    queryBuilder.append("  ) ");

    queryBuilder.append(" , EVENT_ASSET AS ( ");
    queryBuilder.append(" SELECT MARKET_CODE, MK.DEF_TYPE, MK.EVENT_DATE FROM ICAL_EV_MARKET MK ");
    queryBuilder.append(" INNER JOIN EVENT_ALL EV ON MK.MARKET_CODE = EV.TICKER AND MK.EVENT_DATE = EV.EVENT_DATE LEFT JOIN ICAL_EV_CONFIG CONF ON CONF.ID = MK.CONF_ID WHERE CONF.ACTIVE = 1 ");
    queryBuilder.append(" UNION ALL SELECT BONDCODE,DEF_TYPE,EVENTDATE FROM EVENT_BOND_PRO ORDER BY EVENT_DATE ASC ");
    queryBuilder.append(" ) SELECT MARKET_CODE,DEF_TYPE,EVENT_DATE FROM EVENT_ASSET GROUP BY MARKET_CODE,DEF_TYPE,EVENT_DATE");
    try {
      return hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter(FROM_DATE, fromDate)
        .setParameter(TO_DATE, toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
