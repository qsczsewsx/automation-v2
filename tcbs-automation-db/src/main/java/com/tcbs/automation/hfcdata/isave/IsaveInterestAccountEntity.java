package com.tcbs.automation.hfcdata.isave;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.*;

public class IsaveInterestAccountEntity {
  @Step("Get list customer isave with tip money")
  public static List<HashMap<String, Object>> getListCustomer(String date, String listTcbsId) {
    String[] listTcbsIdString = listTcbsId.split(",");
    Set<String> mySetString = new HashSet<>(Arrays.asList(listTcbsIdString));
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from staging.stg_isave_daily_user_isave_interest" +
      " where to_char(record_date, 'YYYY-MM-DD') = :p_date" +
      " and tcbs_id IN (:p_listTcbId)");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_date", date)
        .setParameter("p_listTcbId", mySetString)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
