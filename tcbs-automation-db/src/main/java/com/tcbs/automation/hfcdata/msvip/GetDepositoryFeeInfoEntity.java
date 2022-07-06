package com.tcbs.automation.hfcdata.msvip;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDepositoryFeeInfoEntity {

  @Step("Get depository fee info ms vip from db")
  public static List<HashMap<String, Object>> getDepositoryFeeInfo(String custody, String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM api.iwealth_monthlyport_ci0032 imc WHERE custodycd = :custody and datereport BETWEEN :from AND :to ");
    try {
      return HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
