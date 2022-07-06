package com.tcbs.automation.hfcdata.icalendar.entity;

import com.tcbs.automation.hfcdata.HfcData;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventsMarketEntity {

  @Step("Get cw events")
  public static List<HashMap<String, Object>> getCwEvents() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT DISTINCT cw_symbol , cw_name , underlying_symbol , underlying_company_name, issuer_name  ");
    queryBuilder.append(" , issuer_name_en  , strike_adjustment_price , strike_price , term ");
    queryBuilder.append(" , conversion_adjustment_ratio, conversion_ratio, registration_volume  ");
    queryBuilder.append(" , TO_DATE(hsx_issued_date, 'YYYY-MM-DD') as hsx_issued_date, TO_DATE(vietstock_issued_date, 'YYYY-MM-DD') as vietstock_issued_date ");
    queryBuilder.append(" , TO_DATE(listed_date, 'YYYY-MM-DD') as listed_date , TO_DATE(additional_issued_date, 'YYYY-MM-DD') as additional_issued_date  ");
    queryBuilder.append(" , TO_DATE(first_trading_date, 'YYYY-MM-DD') as first_trading_date , TO_DATE(maturity_date, 'YYYY-MM-DD') as maturity_date ");
    queryBuilder.append(" , TO_DATE(last_trading_date, 'YYYY-MM-DD') as last_trading_date ");
    queryBuilder.append(" FROM (SELECT * FROM dwh.smy_crawl_cw_events WHERE etlcurdate  = (SELECT max(etlcurdate) FROM dwh.smy_crawl_cw_events) ) ");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get all data event market")
  public static List<HashMap<String, Object>> getAllData() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM api.ical_ev_market WHERE event_type IN ('STOCK', 'FTURE', 'COVWR') ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get config")
  public static List<HashMap<String, Object>> getConfig() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ical_ev_config WHERE event_type = 'STOCK' and active = 1 ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get future events")
  public static List<HashMap<String, Object>> getFutureEvents() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT def_type, event_code, ticker, TO_DATE(maturity_date, 'YYYY-MM-DD') as maturity_date, event_type, id  FROM ( SELECT distinct ticker, maturity_date ");
    queryBuilder.append(
      " FROM staging.stg_stoxmarket_future_by_1d WHERE maturity_date IS NOT NULL AND ticker LIKE 'VN30%'AND seq_time  = (SELECT max(seq_time) FROM staging.stg_stoxmarket_future_by_1d)) future ");
    queryBuilder.append(" LEFT JOIN ");
    queryBuilder.append(" (SELECT id, event_code, event_type, def_type FROM api.ical_ev_config WHERE def_type = 'icalendar.future.maturityDate') conf ON 1 = 1 ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get stock events")
  public static List<HashMap<String, Object>> getStockEvents() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with share_issue AS ( ")
      .append(" SELECT  ROW_NUMBER() over(partition by OrganCode, IssueMethodCode, ExrightDate order by shareissueid DESC ) as rn ")
      .append(" , OrganCode , CASE WHEN IssueMethodCode = 'DIV' THEN CONCAT(IssueMethodCode, '011') ELSE UPPER(IssueMethodCode) END AS IssueMethodCode ")
      .append(" , RecordDate , ExrightDate , IssueDate, issueyear , dividend_stagecode ")
      .append(" , CASE WHEN ExerciseRatio = 0 THEN null ELSE ExerciseRatio END AS ExerciseRatio ")
      .append(" , CASE WHEN exerciseratioown = 0 THEN null  ELSE concat(cast(exerciseratioown AS float4), concat('/', cast(exerciseratioearn AS float4))) END AS RawRatio ")
      .append(" ,IssuePrice ")
      .append(" ,( case when UPPER(IssueMethodCode) = 'RIGHTS' then ")
      .append("  ( case when TRUNC(SubscriptionPeriodStart) < TO_DATE('1900-01-01', 'YYYY-MM-DD') then null  else SubscriptionPeriodStart end  ) ")
      .append(" else SubscriptionPeriodStart end  ) as SubscriptionPeriodStart ")
      .append("  ,( case when UPPER(IssueMethodCode) = 'RIGHTS' then ")
      .append(" ( case when TRUNC(SubscriptionPeriodEnd) < TO_DATE('1900-01-01', 'YYYY-MM-DD') then null   else SubscriptionPeriodEnd end ) ")
      .append(" else SubscriptionPeriodEnd   end ) as SubscriptionPeriodEnd ")
      .append(" ,PlanVolumn ,dividend_year  ,PublicDate ")
      .append(" FROM staging.stg_tcs_stx_cpa_shareissue ")
      .append(" WHERE trunc(ExrightDate) > TO_DATE('1900-01-01', 'YYYY-MM-DD') ")
      .append(" AND UPPER(IssueMethodCode) IN (  SELECT event_code FROM api.ical_ev_config WHERE event_type = 'STOCK'  union  select 'DIV'  ) ")
      .append(" AND status = 1 ")
      .append(" ), ")
      .append(" cash_dividend_payout AS ( ")
      .append(" SELECT  ROW_NUMBER() over(partition by OrganCode, ExrightDate order by cashdividendpayoutid DESC ) as rn ")
      .append(" , OrganCode  , CONCAT('DIV', '010') AS EventCode  , RecordDate, ExrightDate, PayoutDate AS IssueDate ")
      .append(" , dividendyear , dividendstagecode , ExerciseRate AS Ratio ")
      .append(" , cast(valuepershare AS VARCHAR) AS RawRatio  , payoutdate  , PublicDate ")
      .append(" FROM staging.stg_tcs_stx_cpa_cashdividendpayout ")
      .append(" WHERE TRUNC(ExrightDate) > TO_DATE('1900-01-01', 'YYYY-MM-DD')   AND status = 1 ")
      .append(" ), ")
      .append(" event AS ( ")
      .append(" SELECT ROW_NUMBER() over(partition by OrganCode, eventlistcode, ExrightDate order by eventid DESC ) as rn ")
      .append(" , OrganCode , eventlistcode   , RecordDate, ExrightDate, IssueDate  , issueyear ")
      .append(" , address , EventTitle  , en_EventTitle , ComGroupCodeNew  , ComGroupCodePrevious , PublicDate ")
      .append(" FROM staging.stg_tcs_stx_cpa_event ")
      .append(" WHERE TRUNC(ExrightDate) > TO_DATE('1900-01-01', 'YYYY-MM-DD') ")
      .append(" AND eventlistcode in ( ")
      .append(" SELECT event_code FROM api.ical_ev_config WHERE event_type = 'STOCK' ")
      .append(" and event_code not in ('BOME','AGMR','BCHA','NLIS','RETU','AIS','MOVE','SUSP') ")
      .append(" ) AND eventlistcode <> 'DIV' AND status = 1 ")
      .append(" union all ")
      .append(" SELECT ROW_NUMBER() over(partition by OrganCode, eventlistcode, ExrightDate order by eventid DESC ) as rn ")
      .append(" , OrganCode , eventlistcode  , RecordDate  , IssueDate as ExrightDate , IssueDate , issueyear ")
      .append(" , address , EventTitle , en_EventTitle  , ComGroupCodeNew , ComGroupCodePrevious  , PublicDate ")
      .append(" FROM staging.stg_tcs_stx_cpa_event ")
      .append(" WHERE TRUNC(issuedate) > TO_DATE('1900-01-01', 'YYYY-MM-DD') ")
      .append(" AND eventlistcode in ('BOME','AGMR','BCHA','NLIS','RETU','AIS','MOVE','SUSP') AND status = 1 ")
      .append(" ), ")
      .append(" list_event as ( ")
      .append(" SELECT org.Ticker, IssueMethodCode,  RecordDate, ExrightDate, IssueDate,  issueyear, ")
      .append(" dividend_stagecode,  ExerciseRatio,  RawRatio, null as address, IssuePrice, ")
      .append(" SubscriptionPeriodStart, SubscriptionPeriodEnd,  null as payoutdate, org.organname, ")
      .append(" PlanVolumn, dividend_year as DividendYear, null as EventTitle,  null as en_EventTitle, ")
      .append(" null as ComGroupCodeNew, null as ComGroupCodePrevious, org.ComGroupCode,  PublicDate ")
      .append(" FROM share_issue sha LEFT JOIN staging.stg_tcs_stx_cpf_organization org  ")
      .append(" ON sha.OrganCode = org.OrganCode ")
      .append(" WHERE rn = 1   AND LENGTH(org.ticker) = 3  AND org.comgroupcode in ('HNXIndex','VNINDEX','UpcomIndex') ")
      .append(" UNION ALL ")
      .append(" SELECT org.Ticker, EventCode, RecordDate, ExrightDate, IssueDate,  ")
      .append(" dividendyear, dividendstagecode,   Ratio,   RawRatio, ")
      .append(" null as address,  null as IssuePrice, null as SubscriptionPeriodStart, ")
      .append(" null as SubscriptionPeriodEnd,  payoutdate, org.organname, null as PlanVolumn, ")
      .append(" DividendYear, null as EventTitle, null as en_EventTitle, ")
      .append(" null as ComGroupCodeNew, null as ComGroupCodePrevious, ")
      .append(" org.ComGroupCode,   PublicDate ")
      .append(" FROM cash_dividend_payout cash ")
      .append(" LEFT JOIN staging.stg_tcs_stx_cpf_organization org  ")
      .append(" ON cash.OrganCode = org.OrganCode ")
      .append(" WHERE rn = 1   ")
      .append(" AND LENGTH(org.ticker) = 3 ")
      .append(" AND org.comgroupcode in ('HNXIndex','VNINDEX','UpcomIndex') ")
      .append(" UNION ALL ")
      .append(" SELECT org.Ticker, eventlistcode, RecordDate, ExrightDate, IssueDate,  ")
      .append(" issueyear, NULL AS stage_code, NULL AS ratio,  NULL AS RawRatio, ")
      .append(" address,  null as IssuePrice, null as SubscriptionPeriodStart, ")
      .append(" null as SubscriptionPeriodEnd, null as payoutdate, org.organname, ")
      .append(" null as PlanVolumn, null as DividendYear, EventTitle,  en_EventTitle, ")
      .append(" ComGroupCodeNew, comGroupCodePrevious, org.ComGroupCode, PublicDate ")
      .append(" FROM event ev ")
      .append(" LEFT JOIN staging.stg_tcs_stx_cpf_organization org  ")
      .append(" ON ev.OrganCode = org.OrganCode  ")
      .append(" WHERE rn = 1   ")
      .append(" AND LENGTH(org.ticker) = 3 ")
      .append(" AND org.comgroupcode in ('HNXIndex','VNINDEX','UpcomIndex') ")
      .append(" ) ")
      .append(" SELECT distinct  ticker , IssueMethodCode as event_code")
      .append(" , cast (RecordDate as date) as Record_Date ")
      .append(" , CASE WHEN TRUNC(ExrightDate) < TO_DATE('1990-01-01', 'YYYY-MM-DD') THEN NULL ")
      .append(" ELSE cast(ExrightDate as date) ")
      .append(" END AS Exright_Date ")
      .append(" , CASE WHEN TRUNC(IssueDate) < TO_DATE('1990-01-01', 'YYYY-MM-DD') THEN NULL ")
      .append(" ELSE cast(IssueDate as date) ")
      .append(" END AS Issue_Date ")
      .append(" , CASE WHEN issueyear < 1990 THEN NULL ")
      .append(" ELSE issueyear ")
      .append(" END AS issueyear ")
      .append(" , dividend_stagecode ")
      .append(" , ExerciseRatio ")
      .append(" , 0 as value ")
      .append(" , RawRatio ")
      .append(" ,address ")
      .append(" ,IssuePrice ")
      .append(" ,SubscriptionPeriodStart ")
      .append(" ,SubscriptionPeriodEnd ")
      .append(" ,payoutdate ")
      .append(" ,organname ")
      .append(" ,PlanVolumn ")
      .append(" ,DividendYear ")
      .append(" ,EventTitle ")
      .append(" ,en_EventTitle ")
      .append(" ,ComGroupCodeNew ")
      .append(" ,ComGroupCodePrevious ")
      .append(" ,ComGroupCode ")
      .append(" ,PublicDate ")
      .append(" FROM list_event aaa  ")
      .append(" JOIN ical_ev_config conf ON aaa.issuemethodcode = conf.event_code AND conf.active = 1  ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" get cw from proc")
  public static List<HashMap<String, Object>> getCwEventFromProc() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM api.ical_evm_ext_cw ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" get stock from proc ")
  public static List<HashMap<String, Object>> getStockEventFromProc() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM api.ical_evm_ext_stock ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" get future event from proc")
  public static List<HashMap<String, Object>> getFutureEventFromProc() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM api.ical_ev_market iem WHERE event_type = 'FTURE' ");
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Call data from proc")
  public static void callProc(String proc, String param) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL " + proc + "(" + param + ")");
    executeQuery(queryBuilder);
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = HfcData.tcbsDwhWriteDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
//      session.getTransaction().commit();
      HfcData.tcbsDwhWriteDbConnection.closeSession();
    } catch (Exception e) {
      HfcData.tcbsDwhWriteDbConnection.closeSession();
      throw e;
    }
  }
}
