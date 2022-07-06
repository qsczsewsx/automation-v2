package com.tcbs.automation.hfcdata.isave;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.*;

public class IsaveInterestMonthlyAccountEntity {
  @Step("Get list customer isave with tip money")
  public static List<HashMap<String, Object>> getListCustomer(String period, String listTcbsId) {
    String[] listTcbsIdString = listTcbsId.split(",");
    Set<String> mySetString = new HashSet<>(Arrays.asList(listTcbsIdString));
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select record_month as MonthYear, tcbs_id, interest_before_tax, updated_at" +
      " from staging.stg_isave_monthly_user_isave_interest" +
      " where to_char(to_date(record_month, 'MM/yyyy'), 'yyyyMM') = :period" +
      " and tcbs_id IN (:p_listTcbId)");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("period", period)
        .setParameter("p_listTcbId", mySetString)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
