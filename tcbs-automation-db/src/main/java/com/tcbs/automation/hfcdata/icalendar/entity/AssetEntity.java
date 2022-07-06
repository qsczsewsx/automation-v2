package com.tcbs.automation.hfcdata.icalendar.entity;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssetEntity {

  @Step("get Data")
  public static List<HashMap<String, Object>> getDataFromDb() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" WITH alluser AS ( SELECT tcbsid, custodycd FROM dwh.Smy_dwh_cas_AllUserView ")
      .append(" WHERE etlcurdate = (SELECT max(etlcurdate) FROM dwh.Smy_dwh_cas_AllUserView) ), ")
      .append(" events AS ( SELECT market_code, event_code, event_date FROM api.ical_ev_market WHERE TO_CHAR(event_date,'yyyy-mm-dd') = '2021-08-27' ")
      .append(" AND event_type = 'STOCK'), ")
      .append(" asset_data AS ( select custodycd, (case when right(symbol,4) = '_WFT' then left(symbol,3) else symbol end) as symbol, ")
      .append(" quantity, mktamt FROM dwh.dailyport_stockbal  ")
      .append(" WHERE TO_CHAR(datereport,'yyyy-mm-dd') = '2021-08-26' ")
      .append(" and ((len(symbol) = 3) or ((right(symbol,4) = '_WFT') and len(symbol) = 7))), ")
      .append(" group_data_asset AS ( SELECT custodycd, symbol, sum(quantity) as quantity, sum(mktamt) as mktamt FROM asset_data ")
      .append(" WHERE symbol in (SELECT market_code FROM events) group by custodycd, symbol ) ")
      .append(" SELECT distinct a.tcbsid, a.custodycd, symbol as ticker, quantity, mktamt, trunc(staging.f_dateadd_working_day(getdate(), -2)) as datereport   ")
      .append(" , e.event_date FROM group_data_asset g inner join events e on g.symbol = e.market_code inner join alluser a on g.custodycd = a.custodycd ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getDataTbl() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ical_as_stock_snapshot where datereport = '2021-08-26' and event_date = '2021-08-27'  ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getLatestAssetFromProc() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with alluser as( ")
      .append("    select distinct tcbsid, custodycd ")
      .append("     from dwh.Smy_dwh_cas_AllUserView ")
      .append("     where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_AllUserView) ")
      .append("   ) ")
      .append("   , asset_data as ( ")
      .append("       SELECT    ")
      .append("     datereport ")
      .append("       , custodycd ")
      .append("       , (case when right(symbol,4) = '_WFT' then left(symbol,3) else symbol end) as symbol ")
      .append("       , quantity ")
      .append("       , mktamt ")
      .append("     from dwh.dailyport_stockbal ")
      .append("     where trunc(datereport) = (select max(datereport) from dwh.dailyport_stockbal) ")
      .append("     and ((len(symbol) = 3) or ((right(symbol,4) = '_WFT') and len(symbol) = 7)) ")
      .append("   ) ")
      .append("   , group_data_asset as ( ")
      .append("       SELECT ")
      .append("     custodycd ")
      .append("       , symbol ")
      .append("       , sum(quantity) as quantity ")
      .append("         , sum(mktamt) as mktamt ")
      .append("     from asset_data ")
      .append("     group by custodycd,symbol ")
      .append("   ) ")
      .append("     select distinct ")
      .append("     a.tcbsid ")
      .append("       , g.custodycd ")
      .append("       , symbol as ticker ")
      .append("       , quantity ")
      .append("       , mktamt ")
      .append("       , SYSDATE as etlrundatetime ")
      .append("     from group_data_asset g ")
      .append("     inner join alluser a ")
      .append("     on g.custodycd = a.custodycd ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> latestAssetFromTbl(){

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ical_as_stock_latest  ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get cw asset data")
  public static List<HashMap<String, Object>> getCwAsset(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with alluser as( ")
      .append(" select tcbsid, custodycd ")
      .append(" from dwh.Smy_dwh_cas_AllUserView ")
      .append(" where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_AllUserView) ")
      .append(" ) ")
      .append(" , events as ( ")
      .append(" select ")
      .append(" market_code ")
      .append(" ,event_code ")
      .append(" ,event_date ")
      .append(" from api.ical_ev_market ")
      .append(" where to_char(event_date,'yyyy-mm-dd') = '2022-05-26' ")
      .append(" AND event_type = 'COVWR' ")
      .append(" ) ")
      .append(" , asset_data AS ( ")
      .append(" select ")
      .append(" custodycd, ")
      .append(" st.symbol, ")
      .append(" qtttype, ")
      .append(" quantity ")
      .append(" from dwh.dailyport_stockbal st ")
      .append(" left join staging.vw_flx_securitiestype sect ")
      .append(" on sect.symbol = st.symbol ")
      .append(" where to_char(datereport,'yyyy-mm-dd') = '2022-05-25' ")
      .append(" AND sect.sectype_lvl2 = 'CW' ")
      .append(" and LEN(sect.symbol) = 8 ")
      .append(" ) ")
      .append(" , group_data_asset as ( ")
      .append(" select ")
      .append(" custodycd ")
      .append(" , symbol ")
      .append(" , sum(quantity) as quantity ")
      .append(" from asset_data ")
      .append(" WHERE symbol in (SELECT market_code FROM events) ")
      .append(" group by custodycd, symbol ")
      .append(" ) ")
      .append(" select ")
      .append(" a.tcbsid ")
      .append(" , a.custodycd ")
      .append(" , symbol as ticker ")
      .append(" , quantity ")
      .append(" FROM group_data_asset g ")
      .append(" inner join events e ")
      .append(" on g.symbol = e.market_code ")
      .append(" inner join alluser a ")
      .append(" on g.custodycd = a.custodycd ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return  new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> cwAssetFromTbl(){

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ical_as_cw_snapshot where to_char(datereport,'yyyy-mm-dd') = '2022-05-25'  ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get cw asset data")
  public static List<HashMap<String, Object>> getCwAssetLatest(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with alluser as( ")
      .append(" select tcbsid, custodycd ")
      .append(" from dwh.Smy_dwh_cas_AllUserView ")
      .append(" where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_AllUserView) ")
      .append(" ) ")
      .append(" , asset_data AS ( ")
      .append(" select ")
      .append(" custodycd, ")
      .append(" st.symbol, ")
      .append(" qtttype, ")
      .append(" quantity ")
      .append(" from dwh.dailyport_stockbal st ")
      .append(" left join staging.vw_flx_securitiestype sect ")
      .append(" on sect.symbol = st.symbol ")
      .append(" where to_char(datereport,'yyyy-mm-dd') = '2022-05-25'  ")
      .append(" AND sect.sectype_lvl2 = 'CW' ")
      .append(" and LEN(sect.symbol) = 8 ")
      .append(" ) ")
      .append(" , group_data_asset as ( ")
      .append(" select ")
      .append(" custodycd ")
      .append(" , symbol ")
      .append(" , sum(quantity) as quantity ")
      .append(" from asset_data ")
      .append(" group by custodycd, symbol ")
      .append(" ) ")
      .append(" select distinct ")
      .append(" a.tcbsid ")
      .append(" , g.custodycd ")
      .append(" , symbol ")
      .append(" , quantity ")
      .append(" from group_data_asset g ")
      .append(" inner join alluser a ")
      .append(" on g.custodycd = a.custodycd ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return  new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> cwAssetLatestFromTbl(){

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ical_as_cw_latest where to_char(datereport,'yyyy-mm-dd') = '2022-05-25'  ");

    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
