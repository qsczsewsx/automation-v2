package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnualSigmaEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getBySymbol(String symbol) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT * FROM ( ");
    queryBuilder.append("   select a.*, row_number() OVER( PARTITION BY UNDERLYINGSYMBOL  ORDER BY LAST_TRADINGDATE DESC) AS rn ");
    queryBuilder.append("   from ANNUALIZEDSIGMA a ");
    queryBuilder.append("   WHERE UNDERLYINGSYMBOL = :symbol ) b ");
    queryBuilder.append(" WHERE b.rn = 1  ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("symbol", symbol)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
