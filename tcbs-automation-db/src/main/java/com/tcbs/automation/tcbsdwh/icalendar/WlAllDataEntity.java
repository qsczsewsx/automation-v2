package com.tcbs.automation.tcbsdwh.icalendar;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WlAllDataEntity {
  @Step("Get data from proc by custody")
  public static List<HashMap<String, Object>> getWatchlistEventByCustody(String custody) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from api.icalendar_wl_alldata ");
    queryBuilder.append("where custodycd = :custody");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get watchlist data by custody")
  public static List<HashMap<String, Object>> getWatchlistTickerByCustody(String custody) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with wl as ( ");
    queryBuilder.append("   select tcbsid, listagg(distinct list_stocks_code, ', ') within group (order by list_stocks_code) ");
    queryBuilder.append("   from staging.stg_poseidon_category ");
    queryBuilder.append("   where etlcurdate = (select max(etlcurdate) from staging.stg_poseidon_category) ");
    queryBuilder.append(" and category_type = 'WL' and list_stocks_code is not null ");
    queryBuilder.append(" group by tcbsid ) ");
    queryBuilder.append(" select wl.tcbsid, wl.listagg, alluser.custodycd ");
    queryBuilder.append(" from wl ");
    queryBuilder.append(" left join dwh.smy_dwh_cas_alluserview alluser ON wl.tcbsid = alluser.tcbsid ");
    queryBuilder.append(" WHERE alluser.custodycd IS NOT NULL AND alluser.etlcurdate = (select max(etlcurdate) from dwh.smy_dwh_cas_alluserview) ");
    queryBuilder.append(" and alluser.custodycd = :custody ");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("Get list event by list ticker")
  public static List<HashMap<String, Object>> getListEventsByTickers(List<String> tickers) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select stock.stock_symbol as objid, ");
    queryBuilder.append("   (case  when stock.is_additional = 0 then 'icalendar.wl.stock.firstlistedDate' else 'icalendar.wl.stock.additionalListedDate' end)  as eventType, ");
    queryBuilder.append(" first_trading_date as eventDate, ");
    queryBuilder.append("   1 as eventindex, ");
    queryBuilder.append(" null as stock_code, ");
    queryBuilder.append(" null as stock_ratio ");
    queryBuilder.append(" from staging.VSD_STOCK_ACCOUNTING_DATE stock ");
    queryBuilder.append(" where stock_symbol in :tickers");
    queryBuilder.append(" union all ");
    queryBuilder.append(" select event.symbol as objid, ");
    queryBuilder.append("   event.event as eventType, ");
    queryBuilder.append(" event.reportdate as eventdate, ");
    queryBuilder.append("   2 as eventindex, ");
    queryBuilder.append(" event.catype as stock_code, ");
    queryBuilder.append("   event.ical_stock_ratio as stock_ratio ");
    queryBuilder.append(" from dwh.SMY_DWH_FLX_STOCKEVENT event ");
    queryBuilder.append(" where etlcurdate = (select max(etlcurdate) from dwh.SMY_DWH_FLX_STOCKEVENT) ");
    queryBuilder.append(" and event.symbol in :tickers ");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
