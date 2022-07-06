package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author minhnv8
 * @date 02/07/2021 11:03
 */


public class RiskGetAllTickersEntity {
  @Step("get all tickers")
  public static List<HashMap<String, String>> getRiskGetAllTickers() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT Ticker FROM stx_cpf_Organization where ComGroupCode in ('VNINDEX','HNXIndex') and len(ticker) = 3 ");
    try {
      List<HashMap<String, String>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
