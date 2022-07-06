package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import java.util.*;

public class MarginPostCrmExchangeEntity {
  private static final String TCBSID = "TCBSID";

  public static List<HashMap<String, Object>> getListCustomers() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from iris_smy_get_tcbsid_custype ");

    List<HashMap<String, Object>> rs = new ArrayList<>();
    Set<String> ids = new HashSet<>();
    try {
      List<HashMap<String, Object>> result = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
          if (!ids.contains((String) object.get(TCBSID))) {
            HashMap<String, Object> item = new HashMap<>();
            item.put(TCBSID, object.get(TCBSID));
            item.put("isVip", object.get("CusType"));
            rs.add(item);
            ids.add((String) item.get(TCBSID));
          }
        });
        return rs;
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}