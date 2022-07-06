package com.tcbs.automation.iris.backup;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBackupDataEntity {

  @Step("Get data from IRISK")
  public static List<HashMap<String, Object>> getBackUpCus() {
    StringBuilder query = new StringBuilder();
    query.append(
      "select * from RISK_CUSTOMER_DAILY_TRACKING_BACKUP ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from IRISK")
  public static List<HashMap<String, Object>> getRowBackUpCus(String tradingdate) {
    StringBuilder query = new StringBuilder();
    query.append(
      "select * from RISK_CUSTOMER_DAILY_TRACKING where ETL_UPDATE_DATE <= :tradingdate ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tradingdate", tradingdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}
