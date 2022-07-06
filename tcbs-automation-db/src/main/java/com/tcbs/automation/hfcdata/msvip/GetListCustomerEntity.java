package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetListCustomerEntity {

  @Step("Get list customer ms vip from db")
  public static List<HashMap<String, Object>> getListCustomer(String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM api.iwealth_monthlyport_customer imc WHERE datereport BETWEEN :from AND :to order by custodycd");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
