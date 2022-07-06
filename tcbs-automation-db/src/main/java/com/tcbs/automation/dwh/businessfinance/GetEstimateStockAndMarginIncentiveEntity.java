package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetEstimateStockAndMarginIncentiveEntity {

  @Step("get stock product info")
  public static List<HashMap<String, Object>> getEstimatedStock(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select 'Stock' as product,'Commission iwp' as feeType, CAST(ReportDate as date) as reportDate, sum(value_based) as totalValue, sum(incentive) as estimatedIncentive, sum(incentive) /sum(value_based) as rate ");
    queryBuilder.append(" from tcpoints.dbo.iwp_incentive where INSTRUMENT_TYPE = 'Stock' and Incentive > 0 ");
    queryBuilder.append(" and CAST(ReportDate as date) between :from and :to group by ReportDate ");
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

  @Step("get margin product info")
  public static List<HashMap<String, Object>> getMarginIncentive(String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select 'Margin' as product,'Commission iwp' as feeType, CAST(ReportDate as date) as reportDate, sum(value_based) as totalValue, sum(incentive) as estimatedIncentive, sum(incentive) /sum(value_based) as rate ");
    queryBuilder.append(" from tcpoints.dbo.iwp_incentive where INSTRUMENT_TYPE = 'Margin' and Incentive > 0 ");
    queryBuilder.append(" and CAST(ReportDate as date) between :from and :to group by ReportDate ");
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
