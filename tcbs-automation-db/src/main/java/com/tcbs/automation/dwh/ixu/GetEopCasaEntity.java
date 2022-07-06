package com.tcbs.automation.dwh.ixu;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetEopCasaEntity {

  @Step("Get eop casa")
  public static List<HashMap<String, Object>> getEopCasa(String datereport) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT  tcbsid , datereport , eop_bal , customer_code , aum_vip , rmcode , rmemail  ");
    queryStringBuilder.append(" FROM smy_dwh_dwhtcb_tcbs_casa");
    queryStringBuilder.append(" WHERE datereport = :datereport");
    queryStringBuilder.append(" AND tcbsid IS NOT NULL ");
    queryStringBuilder.append(" ORDER BY tcbsid asc; ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("datereport", datereport)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
