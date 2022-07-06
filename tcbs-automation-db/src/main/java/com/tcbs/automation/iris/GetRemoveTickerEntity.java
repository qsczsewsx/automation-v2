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

public class GetRemoveTickerEntity {
  @Id
  private String ticker;
  private String loanType;
  private String priceType;
  private Double loanPrice;
  private String reason;
  private String note;
  private Double loanRatio;
  private Double roomFinal;
  private Boolean isClean0012;
  private String statusTicker;
  private String updatedBy;
  private String typeTicker;


  @Step("Get list ticker remove")
  public static List<HashMap<String, Object>> getRemoveTicker() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  *  ");
    queryBuilder.append(" FROM  RISK_LIST_TICKER_REMOVE WHERE STATUS_TICKER = 'SYNCING' AND TYPE_TICKER = 'EXCHANGE_REMOVE'  ");

    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}
