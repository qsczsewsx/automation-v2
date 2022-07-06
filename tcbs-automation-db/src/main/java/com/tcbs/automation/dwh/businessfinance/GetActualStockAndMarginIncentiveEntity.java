package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActualStockAndMarginIncentiveEntity {
  @Step("Get stock total value")
  public static List<HashMap<String, Object>> getStockTotalVal(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT 'Stock' as product, 'Commission iwp' as feeType, round(a.Actual_Incentive / TotalValue,6) as rate, a.ReportDate as reportDate, dateadd(day, 15, a.ReportDate) as ActualDate, TotalValue as totalValue, Actual_Incentive as actualIncentive FROM ( ");
    queryBuilder.append(" select ReportDate, sum(Total_Incentive) as Actual_Incentive from tcpoints.dbo.iWP_Incentive_iHao ");
    queryBuilder.append(" where INSTRUMENT_TYPE = 'Stock' and ReportDate between :from and :to group by ReportDate ) a ");
    queryBuilder.append(" LEFT JOIN  ");
    queryBuilder.append(" (select eomonth(ReportDate) as ReportDate, sum(value_based) as TotalValue from tcpoints.dbo.iwp_incentive ");
    queryBuilder.append(" where INSTRUMENT_TYPE = 'Stock' and Incentive > 0 and ReportDate between :from and :to group by eomonth(ReportDate) ");
    queryBuilder.append(" ) b ON a.ReportDate = b.ReportDate ");
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

  @Step(" Get margin total value")
  public static List<HashMap<String, Object>> getMarginTotalVal(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT 'Margin' as product,'Commission iwp' as feeType, a.ReportDate as reportDate, dateadd(day, 15, a.ReportDate) as ActualDate, Actual_Incentive as actualIncentive, round(Actual_Incentive / TotalValue,6) as rate, TotalValue as totalValue FROM ( ");
    queryBuilder.append(" select 'Margin' as Product,'Commission iwp' as FeeType, eomonth(ReportDate) as ReportDate, sum(value_based) as TotalValue ");
    queryBuilder.append(" from tcpoints.dbo.iwp_incentive ");
    queryBuilder.append(" where INSTRUMENT_TYPE = 'Margin' and Incentive > 0 and ReportDate between :from and :to group by eomonth(ReportDate) ) a ");
    queryBuilder.append(" LEFT JOIN (select 'Margin' as Product,'Commission iwp' as FeeType, ReportDate, sum(Total_Incentive) as Actual_Incentive ");
    queryBuilder.append(" from tcpoints.dbo.iWP_Incentive_iHao where INSTRUMENT_TYPE = 'Margin' and ReportDate between :from and :to ");
    queryBuilder.append(" group by ReportDate) b ON a.ReportDate = b.ReportDate ");
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

  @Step(" Get margin cb total value")
  public static List<HashMap<String, Object>> getCbActualMargin (String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT product, FeeType as feeType, round(a.Actual_Incentive / TotalValue,6) as rate, a.ReportDate as reportDate, dateadd(day, 15, a.ReportDate) as ActualDate, TotalValue as totalValue, Actual_Incentive as actualIncentive FROM ( ");
    queryBuilder.append(" select 'Margin' as Product, 'Cashback iwp' as FeeType, ReportDate, sum(Total_Cashback) as Actual_Incentive ");
    queryBuilder.append(" from tcpoints.dbo.iwp_cashback_ihao ");
    queryBuilder.append(" where  ReportDate between :from and :to group by ReportDate) a  ");
    queryBuilder.append(" LEFT JOIN (select ReportDate, sum(Interest_Differ) as TotalValue from tcpoints.dbo.iwp_cashback ");
    queryBuilder.append(" where  ReportDate between :from and :to ");
    queryBuilder.append(" group by ReportDate) b ON a.ReportDate = b.ReportDate ");
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
