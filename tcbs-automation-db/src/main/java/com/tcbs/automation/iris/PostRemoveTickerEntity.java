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


public class PostRemoveTickerEntity {
  @Id
  private String ticker;
  private String loanType;
  private Integer loanRatio;
  private Integer roomFinal;
  private Integer loanPrice;


  @Step("Update blacklist in full list")
  public static List<HashMap<String, Object>> updateBlacklist(List<String> listTicker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  * ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL where ticker in :listTicker ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("listTicker", listTicker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getlistTickerRemove() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  * FROM RISK_LIST_TICKER_REMOVE where updated_date = (select max(updated_date) from RISK_LIST_TICKER_REMOVE) ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}