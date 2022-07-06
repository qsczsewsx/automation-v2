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

public class GetMa5Entity {
  @Id
  private String ticker;
  private Double ma5;


  @Step("Get list ticker")
  public static List<HashMap<String, Object>> getFullList() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  TICKER,  MA_5  ");
    queryBuilder.append(" FROM  RISK_ANALYST_MARGIN_REVIEWED_FULL ");

    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}