package com.tcbs.automation.dwh.fund;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetiBondBuyBackEntity {

  @Step("Get list iBond Buy Back from db")
  public static List<HashMap<String, Object>> getListiBondBuyBack(String bondCode, String counterParty, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select nextRollDate, bondCode, productCode ");
    queryBuilder.append(" , case when CounterParty = '3926' then 'TCS' when CounterParty = '378112' then 'HBI' else null end as firm ");
    queryBuilder.append(" , sum(round(quantity,0)) as quantity ");
    queryBuilder.append(" , round(ParValue/Quantity,0) as parUnitPrice ");
    queryBuilder.append(" , round(sellback_RecAmt/Quantity,4) as dirtyUnitPrice ");
    queryBuilder.append(" , sum(cast(sellback_RecAmt as bigint)) as value ");
    queryBuilder.append(" , cast(sum(parValue) as bigint) as parValue ");
    queryBuilder.append(" from DailyPort_risk_Risk_iBondBuyback_NextRollDateProjection ");
    queryBuilder.append(" where nextRollDate >= :fromDate and nextRollDate <= :toDate ");
    if (!counterParty.equalsIgnoreCase("")) {
      queryBuilder.append(" and counterParty = :counterParty ");
    } else {
      queryBuilder.append(" and :counterParty is not null ");
    }
    if (!bondCode.equalsIgnoreCase("")) {
      queryBuilder.append(" and bondCode = :bondCode ");
    } else {
      queryBuilder.append(" and :bondCode is not null ");
    }

    queryBuilder.append(" group by nextRollDate, bondCode, productCode, round(ParValue/Quantity,0), round(sellback_RecAmt/Quantity,4), CounterParty ");
    queryBuilder.append(" order by nextRollDate ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("bondCode", bondCode)
        .setParameter("counterParty", counterParty)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
