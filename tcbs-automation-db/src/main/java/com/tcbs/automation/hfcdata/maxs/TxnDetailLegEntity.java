package com.tcbs.automation.hfcdata.maxs;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TxnDetailLegEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getByCondition(String custody, String cptyCustody, String bondCode) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT * FROM ( ");
    queryBuilder.append("   select a.*, row_number() OVER( PARTITION BY UNDERLYINGSYMBOL  ORDER BY LAST_TRADINGDATE DESC) AS rn ");
    queryBuilder.append("   from ANNUALIZEDSIGMA a ");
    queryBuilder.append("   WHERE UNDERLYINGSYMBOL = :symbol ) b ");
    queryBuilder.append(" WHERE b.rn = 1  ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("custody", custody)
        .setParameter("cptyCustody", cptyCustody)
        .setParameter("bondCode", bondCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
