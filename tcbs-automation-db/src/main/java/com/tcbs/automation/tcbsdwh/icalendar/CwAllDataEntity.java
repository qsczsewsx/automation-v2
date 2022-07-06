package com.tcbs.automation.tcbsdwh.icalendar;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CwAllDataEntity {
  public static final String FUTURESTR = "future";

  @Step("Get data from db after call proc")
  public static List<HashMap<String, Object>> cwDataFromProc(String type) {
    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("past")) {
      queryBuilder.append("SELECT * FROM api.icalendar_cw_past");
    } else if (type.equalsIgnoreCase(FUTURESTR)) {
      queryBuilder.append("SELECT * FROM api.icalendar_cw_future");
    } else {
      queryBuilder.append("SELECT * FROM api.icalendar_cw_alldata ");
    }

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from db")
  public static List<HashMap<String, Object>> getCwAssetFromSql(String type, String etlDate) {
    String fieldColumns = " symbol as objid, custodycd, cw_name as name_cw, qtt, strike_price, strike_adjustment_price,sectype, " +
      "underlying_symbol, underlying_company_name, issuer_name, term,conversion_ratio, conversion_adjustment_ratio, issuer_name_en, " +
      "last_trading_date, maturity_date, registration_volume ";
    String fromTable = " FROM event ";
    String operator = " union all ";
    StringBuilder queryBuilder = new StringBuilder();
    String condition = "";
    if (type.equalsIgnoreCase("past")) {
      condition = "<= \'" + etlDate + "\'";
    } else if (type.equalsIgnoreCase(FUTURESTR)) {
      condition = "> \'" + etlDate + "\'";
    } else {
      condition = "> \'1970-01-01\'";
    }

    queryBuilder.append(" with cw as ( ");
    queryBuilder.append(" select datereport, custodycd , st.symbol, sum(quantity) as qtt, sectype ");
    queryBuilder.append(" from dwh.dailyport_stockbal st ");
    queryBuilder.append(" left join dwh.Prc_flx_SecuritiesType sect on sect.symbol = st.SYMBOL ");
    queryBuilder.append(" where length(st.SYMBOL) = 8 and st.SYMBOL like 'C%' and sect.sectype_lvl2 = 'CW' ");
    queryBuilder.append(" and datereport = (select max(datereport) from dwh.dailyport_stockbal) ");
    queryBuilder.append(" group by datereport, custodycd, st.symbol, sectype), ");
    queryBuilder.append(" event as ( ");
    queryBuilder.append("   select * from cw ");
    queryBuilder.append(" join dwh.smy_crawl_cw_events event on cw.symbol = event.CW_SYMBOL) ");
    if (type.equalsIgnoreCase(FUTURESTR)) {
      queryBuilder.append(" SELECT 'icalendar.myasset.cw.listedDate' as eventtype, hsx_issued_date as eventdate, ");
      queryBuilder.append(fieldColumns);
      queryBuilder.append(fromTable);
      queryBuilder.append(" where to_date(hsx_issued_date, 'yyyy-MM-dd') ");
      queryBuilder.append(condition);
    } else if (type.equalsIgnoreCase("past")) {
      queryBuilder.append(" SELECT 'icalendar.myasset.cw.issuedDate' as eventtype,  ");
      queryBuilder.append(" (case when vietstock_issued_date is not null then vietstock_issued_date else hsx_issued_date END) as eventdate, ");
      queryBuilder.append(fieldColumns);
      queryBuilder.append(fromTable);
      queryBuilder.append(" where ( to_date(vietstock_issued_date, 'yyyy-MM-dd') ");
      queryBuilder.append(condition);
      queryBuilder.append(" or to_date(hsx_issued_date, 'yyyy-MM-dd')  ");
      queryBuilder.append(condition);
      queryBuilder.append("  )  ");
    } else {
      queryBuilder.append(" SELECT 'icalendar.myasset.cw.issuedDate' as eventtype,  ");
      queryBuilder.append(" (case when vietstock_issued_date is not null then vietstock_issued_date else hsx_issued_date END) as eventdate, ");
      queryBuilder.append(fieldColumns);
      queryBuilder.append(fromTable);
    }
    queryBuilder.append(operator);
    queryBuilder.append(" SELECT 'icalendar.myasset.cw.additionalIssuedDate' as eventtype, additional_issued_date as eventdate, ");
    queryBuilder.append(fieldColumns);
    queryBuilder.append(fromTable);
    queryBuilder.append(" where to_date(additional_issued_date, 'yyyy-MM-dd')  ");
    queryBuilder.append(condition);
    queryBuilder.append(operator);
    queryBuilder.append(" SELECT 'icalendar.myasset.cw.lastTradingDate' as eventtype, last_trading_date as eventdate, ");
    queryBuilder.append(fieldColumns);
    queryBuilder.append(fromTable);
    queryBuilder.append(" where to_date(last_trading_date, 'yyyy-MM-dd')  ");
    queryBuilder.append(condition);
    queryBuilder.append(operator);
    queryBuilder.append(" SELECT 'icalendar.myasset.cw.expiredDate' as eventtype, maturity_date as eventdate, ");
    queryBuilder.append(fieldColumns);
    queryBuilder.append(fromTable);
    queryBuilder.append(" where to_date(maturity_date, 'yyyy-MM-dd') ");
    queryBuilder.append(condition);


    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get list cw event")
  public static List<HashMap<String, Object>> getCwEventFromSql() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with cw as ( ");
    queryBuilder.append("     select vsd.cw_symbol , hsx.cw_name, hsx.cw_type, vsd.underlying_symbol, vsd.underlying_company_name, hsx.issuer, ");
    queryBuilder.append("     vietstock_en.TOCHUCPHATHANH_CW, vsd.settlement_method, vsd.excercise_style, vsd.term, ");
    queryBuilder.append("     vsd.conversion_ratio, vietstock.tilechuyendoi, vietstock.tlcd_dieuchinh, ");
    queryBuilder.append("     vietstock.giathuchien, vietstock.giath_dieuchinh, vsd.strike_price, ");
    queryBuilder.append("     vietstock.giaphathanh, hsx.registration_volume, vietstock.kl_niemyet, vietstock.kl_luuhanh, ");
    queryBuilder.append("     vsd.registration_mode, to_timestamp(hsx.MATURITY_DATE, 'YYYY-MM-DD 00:00:00.0') as expiry_date, vsd.publish_time , vietstock.ngayphathanh as ngayphathanh_vietstock, ");
    queryBuilder.append("     vietstock.ngayniemyet, vietstock.ngaygd_dautien, vietstock.ngaygd_cuoicung, vietstock.ngaydaohan, ");
    queryBuilder.append("     hsx.is_vsd_updated, hsx.ref_link as hsx_ref_link, ");
    queryBuilder.append("     vsd.is_vietstock_updated, vsd.ref_link as vsd_ref_link, ");
    queryBuilder.append("     vietstock.url_caobachphathanh ");
    queryBuilder.append("     FROM staging.stg_hfc_hsx_cw as hsx ");
    queryBuilder.append("     inner join staging.stg_hfc_VSD_CW_ARTICLE as vsd on hsx.cw_symbol = vsd.cw_symbol ");
    queryBuilder.append("     inner join staging.stg_hfc_vietstock_cw_info as vietstock on vsd.cw_symbol =vietstock.cw_symbol ");
    queryBuilder.append("     inner join staging.stg_hfc_vietstock_cw_info_eng as vietstock_en on vsd.cw_symbol=vietstock_en.cw_symbol ");
    queryBuilder.append("     order by vsd.cw_symbol ), ");
    queryBuilder.append(" temp as ( ");
    queryBuilder.append("     select 	hca.symbol as symbol, ");
    queryBuilder.append("     MIN(to_timestamp(hca.ngay_phat_hanh_lan_dau, 'YYYY-MM-DD 00:00:00.0')) as ngayphathanh_hsx, ");
    queryBuilder.append("     MAX(hca.ngay_phat_hanh_bo_sung) as  ngay_phat_hanh_bo_sung, ");
    queryBuilder.append("     MIN(hca.ngay_dk_chao_ban) as ngay_dk_chao_ban, ");
    queryBuilder.append("     MAX(hca.ngay_dk_chao_ban_bs) as ngay_dk_chao_ban_bs ");
    queryBuilder.append("     from staging.stg_hfc_hsx_cw_article hca ");
    queryBuilder.append("     group by hca.symbol) ");
    queryBuilder.append(" select * from cw ");
    queryBuilder.append(" inner join temp on cw.cw_symbol = temp.symbol  ");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get list cw event from Proc")
  public static List<HashMap<String, Object>> getCwEventFromProc() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from dwh.smy_crawl_cw_events");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
