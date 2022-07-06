package com.tcbs.automation.tcbsdwh.ani.fin5;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

public class Fin5DailyCopyEntity {

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getFeeCopierTraderFromSql(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select ");
    queryBuilder.append(" c.reportDate4 as reportDate");
    queryBuilder.append(" ,c.copierQuantity ");
    queryBuilder.append(" ,c.copierAum");
    queryBuilder.append(" ,t.traderQuantity ");
    queryBuilder.append(" ,t.traderAum ");
    queryBuilder.append(" ,t.traderRank ");
    queryBuilder.append(" ,c.copierFee ");
    queryBuilder.append(" ,cast(t.traderFee as float) traderFee");
    queryBuilder.append(" ");
    queryBuilder.append(" from (select ");
    queryBuilder.append(" t4.date as reportDate4");
    queryBuilder.append(" ,t5.copierQuantity");
    queryBuilder.append(" ,t5.copierAum");
    queryBuilder.append(" ,isnull(t6.copierAumFee,0) + isnull(t7.copierPnl,0) as copierFee");
    queryBuilder.append(" from (select distinct date from dwh.smy_dwh_date_dim) t4");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select cast(report_date as date) as reportDate5");
    queryBuilder.append(" , count(distinct tcbsid) as copierQuantity");
    queryBuilder.append(" , sum(isnull(aum,0)) as copierAum");
    queryBuilder.append(" from staging.stg_icopy_copier_daily_aum");
    queryBuilder.append(" group by cast(report_date as date)");
    queryBuilder.append(" )t5 on t5.reportDate5 = t4.date");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select cast(calculated_date as date) as reportDate6");
    queryBuilder.append(" , sum(isnull(daily_fee,0)) as copierAumFee");
    queryBuilder.append(" from staging.Stg_icopy_copier_DAILY_FEE");
    queryBuilder.append(" group by cast(calculated_date as date)");
    queryBuilder.append(" )t6 on t6.reportDate6 = t4.date");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select cast(last_day(month) as date) as reportDate7");
    queryBuilder.append(" , sum(isnull(pnl_fee_payable,0)) as copierPnl");
    queryBuilder.append(" from staging.Stg_icopy_COPIER_MONTH_FEE");
    queryBuilder.append(" group by cast(last_day(month) as date)");
    queryBuilder.append(" )t7 on t7.reportDate7 = t4.date ");
    queryBuilder.append(" )c ");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select ");
    queryBuilder.append(" t1.reportdate1");
    queryBuilder.append(" ,count(distinct tcbsid) as traderQuantity");
    queryBuilder.append(" , sum(isnull(total_aum,0)) as traderAum");
    queryBuilder.append(" , sum(isnull(daily_fee,0) + isnull(totalincentive,0)) as traderFee");
    queryBuilder.append(" , rank_code as traderRank");
    queryBuilder.append(" from (select *,cast(report_date as date) as reportDate1 from staging.stg_icopy_trader_daily_summary) t1");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select distinct trader_id, cast(report_date as date) as reportDate2, ");
    queryBuilder.append(" sum(isnull(AUM_FEE_INCOME,0) + isnull(AUM_FEE_IC_LATCHED,0) + isnull(PNL_FEE_IC_LATCHED,0)) as daily_fee");
    queryBuilder.append(" from staging.Stg_icopy_trader_DAILY_FEE ");
    queryBuilder.append(" group by trader_id, cast(report_date as date)");
    queryBuilder.append(" )t2 on t1.reportDate1 = t2.reportdate2 and t1.trader_id = t2.trader_id");
    queryBuilder.append(" ");
    queryBuilder.append(" left join (select distinct trader_id, cast(report_date as date) as reportDate3, ");
    queryBuilder.append(" sum(isnull(pnl_incentive,0)) as totalincentive");
    queryBuilder.append(" from staging.stg_icopy_TRADER_PERFORMANCE_INCENTIVE");
    queryBuilder.append(" group by trader_id, cast(report_date as date)");
    queryBuilder.append(" )t3 on t1.reportDate1 = t3.reportdate3 and t1.trader_id = t3.trader_id");
    queryBuilder.append(" ");
    queryBuilder.append(" group by t1.reportDate1 ,t1.rank_code ");
    queryBuilder.append(" )t on c.reportDate4 = t.reportDate1");
    queryBuilder.append(" where c.reportDate4 >= :fromdate and c.reportDate4 <= :todate");
    queryBuilder.append(" AND(copierquantity is not null or copieraum is not null or traderquantity is not null ");
    queryBuilder.append(" or traderaum is not null or traderrank is not null)");
    queryBuilder.append(" order by c.reportDate4  desc");


    try {

      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromdate", fromDate)
        .setParameter("todate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}

