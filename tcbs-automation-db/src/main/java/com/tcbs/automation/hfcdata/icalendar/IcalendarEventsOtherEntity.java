package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IcalendarEventsOtherEntity {
  public static final HibernateEdition redshift = Database.TCBS_DWH.getConnection();

  @Step("Get data from staging.stg_stoxmarket_future_by_1d")
  public static List<HashMap<String, Object>> getOtherEventFuture() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(
      "select distinct ticker, TO_CHAR(maturity_date, 'yyyy-MM-dd') as eventdate from staging.stg_stoxmarket_future_by_1d where ticker like '%VN%' and maturity_date is not null and future_name is not null");
    return redshift.getSession().createNativeQuery(queryBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
  }

  @Step("Get Data from Db")
  public static List<HashMap<String, Object>> getEventsOther(String productType, String fromDate) {
    StringBuilder cwQuery = new StringBuilder();
    cwQuery.append(" with cw_event as ( ");
    cwQuery.append(" SELECT cw_symbol as symbol, cw_name, cw_type, 'Stock' as secType, ");
    cwQuery.append(" (case when strike_adjustment_price is not null then strike_adjustment_price else strike_price END) as strike_price, ");
    cwQuery.append("  underlying_symbol, term, issuer_name, issuer_name_en, ");
    cwQuery.append(" (case when conversion_adjustment_ratio is not null then conversion_adjustment_ratio else conversion_ratio END) as conversion_ratio, ");
    cwQuery.append(" TO_CHAR(last_trading_date, 'yyyy-MM-dd') as last_trading_date, TO_CHAR(maturity_date, 'yyyy-MM-dd') as maturity_date, ");
    cwQuery.append(" TO_CHAR(additional_issued_date, 'yyyy-MM-dd') as additional_issued_date, registration_volume, ");
    cwQuery.append(" (case when hsx_issued_date is not null then TO_CHAR(hsx_issued_date, 'yyyy-MM-dd') ");
    cwQuery.append(" else TO_CHAR(vietstock_issued_date, 'yyyy-MM-dd') END) as issued_date ");
    cwQuery.append(" FROM dwh.smy_crawl_cw_events) ");
    cwQuery.append(" select 'icalendar.cw.issuedDate' as defType, cw_event.*, issued_date as eventDate ");
    cwQuery.append(" from cw_event where issued_date >= :fromDate ");
    cwQuery.append(" union select 'icalendar.cw.additionalIssuedDate' as defType, cw_event.*, additional_issued_date as eventDate ");
    cwQuery.append(" from cw_event where additional_issued_date >= :fromDate ");
    cwQuery.append(" union select 'icalendar.cw.lastTradingDate' as defType, cw_event.*, last_trading_date as eventDate ");
    cwQuery.append(" from cw_event where last_trading_date >= :fromDate ");
    cwQuery.append(" union select 'icalendar.cw.expiredDate' as defType, cw_event.*, maturity_date as eventDate ");
    cwQuery.append(" from cw_event ");
    cwQuery.append(" where maturity_date >= :fromDate ");

    StringBuilder futureQuery = new StringBuilder();
    futureQuery.append(" select 'icalendar.future.maturityDate' as defType, ticker as symbol, null as cw_name, null as cw_type, null as secType, ");
    futureQuery.append(" null as strike_price, null as underlying_symbol, null as term, null as conversion_ratio, null as issuer_name, ");
    futureQuery.append(" null as issuer_name_en, null as last_trading_date, null as maturity_date, null as additional_issued_date, ");
    futureQuery.append(" null as registration_volume, null as issued_date, TO_CHAR(maturity_date, 'yyyy-MM-dd') as eventDate ");
    futureQuery.append(" from staging.stg_stoxmarket_future_by_1d fbd ");
    futureQuery.append(" where seq_time = (select max(seq_time) from staging.stg_stoxmarket_future_by_1d)  ");
    futureQuery.append(" and :fromDate = :fromDate ");

    StringBuilder queryBuilder = new StringBuilder();
    if (productType.equalsIgnoreCase("cw")) {
      queryBuilder.append(cwQuery);
    } else if (productType.equalsIgnoreCase("future")) {
      queryBuilder.append(futureQuery);
    } else {
      queryBuilder.append(cwQuery);
      queryBuilder.append(" union ");
      queryBuilder.append(futureQuery);
    }
    try {
      List<HashMap<String, Object>> resultList = redshift.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
