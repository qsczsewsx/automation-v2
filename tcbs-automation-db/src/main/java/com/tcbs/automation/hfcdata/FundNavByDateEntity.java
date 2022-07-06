package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class FundNavByDateEntity {
  @Step("get NAV data from db")
  public static List<HashMap<String, Object>> byCondition(String fromDate, String toDate, String product) {
    StringBuilder query = new StringBuilder();

    query.append(" SELECT \"date\" AS matchedDate, PFNB.TOTALNAV AS total, PFNB.NAVPERCCQ AS navCurrent, PFNB.PRODUCT AS fundCode  ");
    query.append(" FROM PRC_FUND_NAV_BYDATE pfnb  ");
    query.append(" WHERE PFNB.\"date\" > TO_DATE(:fromDate, 'YYYY-MM-DD') AND PFNB.\"date\" <= TO_DATE(:toDate, 'YYYY-MM-DD')  ");
    query.append(" AND PFNB.PRODUCT = :product ORDER BY PFNB.\"date\" ASC   ");
    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("fromDate", fromDate)
      .setParameter("toDate", toDate)
      .setParameter("product", product)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

  }
}
