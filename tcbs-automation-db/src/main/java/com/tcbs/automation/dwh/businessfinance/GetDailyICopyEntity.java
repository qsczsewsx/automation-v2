package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDailyICopyEntity {

  @Step("Get trader info")
  public static List<HashMap<String, Object>> getTraderInfo (String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select cast(report_date as date) as reportDate, count(distinct tcbsid) as quantity, sum(total_aum) as aum ");
    queryBuilder.append(" , sum(isnull(daily_fee,0) + isnull(totalincentive,0)) as fee, rank_code as rank from stg_icopy_trader_daily_summary t ");
    queryBuilder.append(" left join (select trader_id, sum(isnull(AUM_FEE_INCOME,0) + isnull(AUM_FEE_IC_LATCHED,0) + isnull(PNL_FEE_IC_LATCHED,0)) as daily_fee, cast(report_date as date) as reportDate ");
    queryBuilder.append(" from Stg_icopy_trader_DAILY_FEE group by cast(report_date as date), trader_id ");
    queryBuilder.append(" ) tf on tf.reportDate = t.report_date and tf.trader_id = t.trader_id ");
    queryBuilder.append(" left join (select distinct trader_id , cast(report_date as date) as reportDate , sum(isnull(pnl_incentive,0)) as totalincentive ");
    queryBuilder.append("  from stg_icopy_TRADER_PERFORMANCE_INCENTIVE group by trader_id, cast(report_date as date)) t1 ");
    queryBuilder.append("  on t1.reportDate = t.REPORT_DATE and t1.TRADER_ID = t.TRADER_ID ");
    queryBuilder.append(" where cast(report_date as date) >= :from and cast(report_date as date) <= :to group by report_date, rank_code ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getCopierInfo (String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select d.Date as reportDate, count(distinct tcbsid) as quantity, sum(aum) as aum, copierFee as fee ");
    queryBuilder.append(" from Dtm_dwh_Date_Dim d ");
    queryBuilder.append(" left join stg_icopy_copier_daily_aum c on cast(c.report_date as date) = d.Date ");
    queryBuilder.append(" left join (select sum(daily_fee) as copierFee, cast(calculated_date as date) as reportDate ");
    queryBuilder.append(" from Stg_icopy_copier_DAILY_FEE group by cast(calculated_date as date)) cf on cf.reportDate = d.Date ");
    queryBuilder.append(" where d.Date >= :from and d.Date <= :to group by d.Date, copierFee ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
