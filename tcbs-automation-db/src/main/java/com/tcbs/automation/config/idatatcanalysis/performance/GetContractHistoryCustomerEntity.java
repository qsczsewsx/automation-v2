package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetContractHistoryCustomerEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> dataContractHistory(String cusId, String fromDate, String toDate){
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT USER_ID, REPORT_DATE, SUM(case when IS_TRANS <> 10 AND MT_SIDE = 'B' then MT_VOLUME ELSE 0 end) AS TXN_B ")
      .append(" , SUM(case when IS_TRANS <> 10 AND MT_SIDE = 'S' then MT_VOLUME ELSE 0 end) AS TXN_S ")
      .append(" , SUM(case when IS_TRANS = 10 AND OPEN_SIDE = 'B' then OPEN_BALANCE ELSE 0 end) AS BALANCE_B ")
      .append(" , SUM(case when IS_TRANS = 10 AND OPEN_SIDE = 'S' then OPEN_BALANCE ELSE 0 end) AS BALANCE_S ")
      .append(" FROM PE_FS_TRANS_FIFO_MATCHED_LOG WHERE USER_ID = :cusId ")
      .append(" AND TO_CHAR(REPORT_DATE,'yyyy-mm-dd') BETWEEN :fromDate AND :toDate GROUP BY USER_ID, REPORT_DATE ");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("cusId", cusId)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
