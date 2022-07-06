package com.tcbs.automation.hfcdata.icalendar.entity;

import com.tcbs.automation.Database;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class WidgetEntity {
  private String tcbsid;

  private static final String STARTDATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
  private static final String STARTMMDD = new SimpleDateFormat("MM-dd").format(new Date());
  private static final String ENDDATE = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.addMonths(new Date(), 1));
  private static final String ENDMMDD = new SimpleDateFormat("MM-dd").format(DateUtils.addMonths(new Date(), 1));
  private static final String DATE1 = "start_date";
  private static final String DATE2 = "end_date";
  private static final String TCBSID_P = "p_tcbsid";
  private static final String LANG_P = "lang";

  public WidgetEntity(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  public static List<HashMap<String, Object>> getListEventWidgetBondRM(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) as EVENT_DATE, ICAL_EV_MARKET.DEF_TYPE, Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end  as EVENT_NAME,ICAL_AS_BOND_SNAPSHOT.TCBSID,ICAL_AS_BOND_SNAPSHOT.COUPONRATE, ");
    queryBuilder.append(
      "  FLOOR(ICAL_AS_BOND_SNAPSHOT.COUPONPAYMENTAMTAFTERTAX) AS COUPONPAYMENTAMTAFTERTAX  , ICAL_AS_BOND_SNAPSHOT.OBALQUANT, ICAL_AS_BOND_SNAPSHOT.OBALPARVALUE, ICAL_AS_BOND_SNAPSHOT.BONDCODE AS MARKET_CODE, ICAL_EVM_EXT_BOND.PAR, ");
    queryBuilder.append("   TO_CHAR( ICAL_EVM_EXT_BOND.ISSUE_DATE , 'DD/MM/YYYY' ) AS ISSUE_DATE , TO_CHAR( ICAL_EVM_EXT_BOND.MATURITY_DATE  , 'DD/MM/YYYY' ) AS MATURITY_DATE,");
    queryBuilder.append(" ROUND(ICAL_EVM_EXT_BOND.REFERENCE_RATE_COUPON,4) AS REFERENCE_RATE_COUPON, ICAL_EVM_EXT_BOND.COUPONPERUNIT ");
    queryBuilder.append(" FROM ICAL_EV_MARKET ");
    queryBuilder.append(" INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_MARKET.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE ");
    queryBuilder.append(
      " LEFT JOIN ICAL_AS_BOND_SNAPSHOT ON ICAL_EV_MARKET.EVENT_DATE  = ICAL_AS_BOND_SNAPSHOT.EVENTDATE  AND ICAL_EV_MARKET.MARKET_CODE  = ICAL_AS_BOND_SNAPSHOT.BONDCODE  AND ICAL_EV_MARKET.EVENT_CODE  = ICAL_AS_BOND_SNAPSHOT.EVENTCODE ");
    queryBuilder.append(
      " LEFT JOIN  ICAL_EVM_EXT_BOND  ON ICAL_AS_BOND_SNAPSHOT.BONDCODE  = ICAL_EVM_EXT_BOND.BOND_CODE AND ICAL_AS_BOND_SNAPSHOT.EVENTDATE = ICAL_EVM_EXT_BOND.EXRIGHT_DATE AND ICAL_AS_BOND_SNAPSHOT.EVENTCODE  = ICAL_EVM_EXT_BOND.EVENT_CODE");
    queryBuilder.append(" WHERE ( TO_CHAR ( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) BETWEEN :start_date AND :end_date ) ");
    queryBuilder.append(" AND  ICAL_EV_MARKET.EVENT_TYPE = 'BOND'  AND (ICAL_AS_BOND_SNAPSHOT.TCBSID = :p_tcbsid OR ICAL_AS_BOND_SNAPSHOT.RM_TCBSID = :p_tcbsid) ");
    queryBuilder.append(" AND ICAL_EV_MARKET.MARKET_CODE  IN ");
    queryBuilder.append(
      " ( SELECT  BONDCODE From ICAL_AS_BOND_SNAPSHOT iabs where (TO_CHAR(EVENTDATE,'yyyy-MM-dd') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid OR  RM_TCBSID  = :p_tcbsid) ) ");
    queryBuilder.append(" AND ICAL_EV_MARKET.EVENT_CODE  IN ");
    queryBuilder.append(
      " ( SELECT  EVENTCODE  From ICAL_AS_BOND_SNAPSHOT iabs where (TO_CHAR(EVENTDATE,'yyyy-MM-dd') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid OR  RM_TCBSID  = :p_tcbsid) ) ");

    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListEventWidgetBondProRM(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( EVENTDATE, 'YYYY-MM-DD' ) AS EVENT_DATE ,'icalendar.bond.pro.expiry' AS DEF_TYPE,Case When :lang ='vi' then 'Ngày đến hạn hợp đồng Trái phiếu Pro' else 'Pro sellback date' end  as EVENT_NAME,TCBSID, TRADINGCODE, FLOOR(RECEIVEAMTPROEXPIRED) AS RECEIVEAMTPROEXPIRED , ROUND(INVESTMENTRATEPROEXPIRED, 4) AS INVESTMENTRATEPROEXPIRED , FLOOR(RECEIVEAMTHTM) as RECEIVEAMTHTM, ROUND(INVESTMENTRATEHTM, 4) AS INVESTMENTRATEHTM, BONDCODE AS MARKET_CODE, TO_CHAR(MATURITYDATE,'DD/MM/YYYY') AS MATURITY_DATE\n" +
        "FROM ICAL_AS_BOND_PRO_SNAPSHOT WHERE (TO_CHAR (EVENTDATE, 'YYYY-MM-DD') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid OR  RM_TCBSID  = :p_tcbsid) ");


    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListEventWidgetBondCus(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) as EVENT_DATE, ICAL_EV_MARKET.DEF_TYPE, Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end  as EVENT_NAME,ICAL_AS_BOND_SNAPSHOT.TCBSID,ICAL_AS_BOND_SNAPSHOT.COUPONRATE, ");
    queryBuilder.append("  FLOOR(ICAL_AS_BOND_SNAPSHOT.COUPONPAYMENTAMTAFTERTAX) AS COUPONPAYMENTAMTAFTERTAX  , ICAL_AS_BOND_SNAPSHOT.OBALQUANT, ICAL_AS_BOND_SNAPSHOT.OBALPARVALUE, ICAL_AS_BOND_SNAPSHOT.BONDCODE AS MARKET_CODE , ICAL_EVM_EXT_BOND.PAR, ");
    queryBuilder.append("   TO_CHAR( ICAL_EVM_EXT_BOND.ISSUE_DATE , 'DD/MM/YYYY' ) AS ISSUE_DATE , TO_CHAR( ICAL_EVM_EXT_BOND.MATURITY_DATE  , 'DD/MM/YYYY' ) AS MATURITY_DATE,");
    queryBuilder.append(" ROUND(ICAL_EVM_EXT_BOND.REFERENCE_RATE_COUPON,4) AS REFERENCE_RATE_COUPON, ICAL_EVM_EXT_BOND.COUPONPERUNIT ");
    queryBuilder.append(" FROM ICAL_EV_MARKET ");
    queryBuilder.append(" INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_MARKET.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE ");
    queryBuilder.append(" LEFT JOIN ICAL_AS_BOND_SNAPSHOT ON ICAL_EV_MARKET.EVENT_DATE  = ICAL_AS_BOND_SNAPSHOT.EVENTDATE  AND ICAL_EV_MARKET.MARKET_CODE  = ICAL_AS_BOND_SNAPSHOT.BONDCODE  AND ICAL_EV_MARKET.EVENT_CODE  = ICAL_AS_BOND_SNAPSHOT.EVENTCODE ");
    queryBuilder.append(" LEFT JOIN  ICAL_EVM_EXT_BOND  ON ICAL_AS_BOND_SNAPSHOT.BONDCODE  = ICAL_EVM_EXT_BOND.BOND_CODE AND ICAL_AS_BOND_SNAPSHOT.EVENTDATE = ICAL_EVM_EXT_BOND.EXRIGHT_DATE AND ICAL_AS_BOND_SNAPSHOT.EVENTCODE  = ICAL_EVM_EXT_BOND.EVENT_CODE");
    queryBuilder.append(" WHERE ( TO_CHAR ( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) BETWEEN :start_date AND :end_date ) ");
    queryBuilder.append(" AND  ICAL_EV_MARKET.EVENT_TYPE = 'BOND'  AND (ICAL_AS_BOND_SNAPSHOT.TCBSID = :p_tcbsid) ");
    queryBuilder.append(" AND ICAL_EV_MARKET.MARKET_CODE  IN ");
    queryBuilder.append(" ( SELECT  BONDCODE From ICAL_AS_BOND_SNAPSHOT iabs where (TO_CHAR(EVENTDATE,'yyyy-MM-dd') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid) ) ");
    queryBuilder.append(" AND ICAL_EV_MARKET.EVENT_CODE  IN ");
    queryBuilder.append(" ( SELECT  EVENTCODE  From ICAL_AS_BOND_SNAPSHOT iabs where (TO_CHAR(EVENTDATE,'yyyy-MM-dd') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid) ) ");

    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListEventWidgetBondProCus(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( EVENTDATE, 'YYYY-MM-DD' ) AS EVENT_DATE ,'icalendar.bond.pro.expiry' AS DEF_TYPE," +
        "Case When :lang ='vi' then 'Ngày đến hạn hợp đồng Trái phiếu Pro' else 'Pro sellback date' end  as EVENT_NAME,TCBSID, " +
        "TRADINGCODE, FLOOR(RECEIVEAMTPROEXPIRED) AS RECEIVEAMTPROEXPIRED , ROUND(INVESTMENTRATEPROEXPIRED, 4) AS INVESTMENTRATEPROEXPIRED , FLOOR(RECEIVEAMTHTM) as RECEIVEAMTHTM, " +
        "ROUND(INVESTMENTRATEHTM, 4) AS INVESTMENTRATEHTM, BONDCODE AS MARKET_CODE , TO_CHAR(MATURITYDATE,'DD/MM/YYYY') AS MATURITY_DATE\n" +
        "FROM ICAL_AS_BOND_PRO_SNAPSHOT WHERE (TO_CHAR (EVENTDATE, 'YYYY-MM-DD') BETWEEN :start_date AND :end_date) AND ( TCBSID  = :p_tcbsid) ");
    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListEventWidgetStock(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) AS EVENT_DATE, ICAL_AS_STOCK_LATEST.TCBSID , ICAL_AS_STOCK_LATEST.QUANTITY , ICAL_AS_STOCK_LATEST.MKTAMT, \n" +
        "Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end  as EVENT_NAME, ICAL_EV_MARKET.DEF_TYPE , \n" +
        "ICAL_EV_MARKET.MARKET_CODE, TO_CHAR( ICAL_EVM_EXT_STOCK.EXRIGHT_DATE , 'DD/MM/YYYY' ) AS EXRIGHT_DATE, TO_CHAR( ICAL_EVM_EXT_STOCK.RECORD_DATE , 'DD/MM/YYYY' ) AS RECORD_DATE\n" +
        ",TO_CHAR( ICAL_EVM_EXT_STOCK.ISSUE_DATE , 'DD/MM/YYYY' ) AS STOCK_ISSUE_DATE, ICAL_EVM_EXT_STOCK.ISSUE_YEAR, ICAL_EVM_EXT_STOCK.RATIO ,ICAL_EVM_EXT_STOCK.DIVIDENDYEAR , ICAL_EVM_EXT_STOCK.DIVIDEND_STAGE_CODE ,ICAL_EVM_EXT_STOCK.PLANVOLUMN \n" +
        "FROM ICAL_EV_MARKET \n" +
        "INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_MARKET.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE\n" +
        "INNER JOIN ICAL_AS_STOCK_LATEST ON ICAL_EV_MARKET.MARKET_CODE  = ICAL_AS_STOCK_LATEST.TICKER\n" +
        "LEFT JOIN ICAL_EVM_EXT_STOCK  ON ICAL_EV_MARKET.MARKET_CODE  = ICAL_EVM_EXT_STOCK.TICKER AND ICAL_EV_MARKET.EVENT_CODE  = ICAL_EVM_EXT_STOCK.EVENT_CODE AND ICAL_EV_MARKET.EVENT_DATE  = ICAL_EVM_EXT_STOCK.EXRIGHT_DATE \n" +
        "WHERE ( TO_CHAR ( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) BETWEEN :start_date AND :end_date )  \n" +
        "AND  ICAL_EV_MARKET.EVENT_TYPE = 'STOCK'\n" +
        "AND ICAL_AS_STOCK_LATEST.TCBSID =:p_tcbsid \n" +
        "AND ICAL_EV_MARKET.MARKET_CODE  IN  ( SELECT TICKER From ICAL_AS_STOCK_LATEST WHERE TCBSID  = :p_tcbsid )   ");
    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getListEventWidgetCW(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      " SELECT TO_CHAR( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) AS EVENT_DATE , \n" +
        "Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end  as EVENT_NAME,\n" +
        "ICAL_EV_MARKET.DEF_TYPE, ICAL_AS_CW_LATEST.TCBSID, ICAL_AS_CW_LATEST.QUANTITY AS CW_QUANTITY, ICAL_EV_MARKET.MARKET_CODE FROM ICAL_EV_MARKET\n" +
        "INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_MARKET.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE \n" +
        "INNER JOIN ICAL_AS_CW_LATEST ON ICAL_AS_CW_LATEST.TICKER = ICAL_EV_MARKET.MARKET_CODE \n" +
        "WHERE ICAL_EV_MARKET.EVENT_TYPE = 'COVWR' AND ICAL_AS_CW_LATEST.TCBSID =:p_tcbsid AND ( TO_CHAR ( ICAL_EV_MARKET.EVENT_DATE, 'YYYY-MM-DD' ) BETWEEN :start_date AND :end_date )\n ");
    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTDATE)
        .setParameter(DATE2, ENDDATE)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getPerRoleRM(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT EXTRACT (YEAR FROM sysdate)||'-'||TO_CHAR (EVENT_DATE, 'MM-DD') AS EVENTDATE, ICAL_EV_PERSONAL.DEF_TYPE,\n" +
      "Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end as EVENT_NAME , ICAL_EV_PERSONAL.TCBSID, EXTRACT (YEAR FROM sysdate)- EXTRACT (YEAR FROM EVENT_DATE) AS MILESTONE\n" +
      "FROM ICAL_EV_PERSONAL\n" +
      "INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_PERSONAL.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE \n" +
      "WHERE (RM_TCBSID = :p_tcbsid OR TCBSID = :p_tcbsid) AND (TO_CHAR(EVENT_DATE,'MM-dd') BETWEEN :start_date AND :end_date) ");
    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTMMDD)
        .setParameter(DATE2, ENDMMDD)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getPerRoleCus(String tcbsId, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT EXTRACT (YEAR FROM sysdate)||'-'||TO_CHAR (EVENT_DATE, 'MM-DD') AS EVENTDATE, ICAL_EV_PERSONAL.DEF_TYPE,\n" +
      "Case When :lang ='vi' then ICAL_EV_CONFIG.EVENT_NAME_VI else ICAL_EV_CONFIG.EVENT_NAME_EN end as EVENT_NAME , ICAL_EV_PERSONAL.TCBSID, EXTRACT (YEAR FROM sysdate)- EXTRACT (YEAR FROM EVENT_DATE) AS milestone\n" +
      "FROM ICAL_EV_PERSONAL\n" +
      "INNER JOIN ICAL_EV_CONFIG ON ICAL_EV_PERSONAL.DEF_TYPE = ICAL_EV_CONFIG.DEF_TYPE \n" +
      "WHERE (TCBSID = :p_tcbsid) AND (TO_CHAR(EVENT_DATE,'MM-dd') BETWEEN :start_date AND :end_date)  ");
    try {
      return Database.HFC_DATA.getConnection().getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(DATE1, STARTMMDD)
        .setParameter(DATE2, ENDMMDD)
        .setParameter(TCBSID_P, tcbsId)
        .setParameter(LANG_P, lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}



