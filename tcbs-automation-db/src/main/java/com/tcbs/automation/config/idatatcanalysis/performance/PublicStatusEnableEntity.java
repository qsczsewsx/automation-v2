package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PublicStatusEnableEntity {

  @Step("get data from database")
  public static List<HashMap<String, Object>> getDataEnable(String custody){
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM ACCOUNT_PUBLIC_INFO WHERE CUSTODY_ID = :custody ");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
