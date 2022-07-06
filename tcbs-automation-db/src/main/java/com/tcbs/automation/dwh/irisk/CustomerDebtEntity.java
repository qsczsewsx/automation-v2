package com.tcbs.automation.dwh.irisk;

import com.tcbs.automation.staging.AwsStagingDwh;
import lombok.*;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerDebtEntity {
  @Id
  private String tcbsId;
  private Boolean isVip;

  public static List<CustomerDebtEntity> getList() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select * from iris_smy_get_tcbsid_custype ");
    List<CustomerDebtEntity> rs = new ArrayList<>();
    Set<String> ids = new HashSet<>();
    try {
      List<HashMap<String, Object>> result = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
          if (!ids.contains((String) object.get("TCBSID"))) {
            CustomerDebtEntity entity = CustomerDebtEntity.builder()
              .tcbsId((String) object.get("TCBSID"))
              .isVip(((String) object.get("CusType")).endsWith("VIP"))
              .build();
            rs.add(entity);
            ids.add(entity.getTcbsId());
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
