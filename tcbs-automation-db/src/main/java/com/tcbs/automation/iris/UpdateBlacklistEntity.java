package com.tcbs.automation.iris;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor


public class UpdateBlacklistEntity {
  @Id
  private String ticker;
  private String loanType;
  private Double loanPrice;
  private Double loanRatio;
  private Double roomFinal;
  private String updatedBy;


  @Step("Get data from IRISK")
  public static List<HashMap<String, Object>> getTicker(List<String> listTicker) {
    StringBuilder query = new StringBuilder();
    query.append("select * from RISK_ANALYST_MARGIN_REVIEWED_FULL where ticker in :listTicker ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("listTicker", listTicker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
