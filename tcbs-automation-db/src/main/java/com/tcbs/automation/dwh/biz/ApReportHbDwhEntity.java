package com.tcbs.automation.dwh.biz;


import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApReportHbDwhEntity {
  public static List<HashMap<String, Object>> getByBondHbReconcile() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * from dbmart_ap.dbo.HB_AP_Report_DWH ");

    try {
      return DwhAP.dwhAPDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getByFeeTaxReconcile() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" EXEC proc_tax_getAPReport ");

    try {
      return DwhAP.dwhAPDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}

