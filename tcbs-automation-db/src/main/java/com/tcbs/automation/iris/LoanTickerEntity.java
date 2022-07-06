package com.tcbs.automation.iris;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
public class LoanTickerEntity {
  @Id
  private String ticker;

  @Step("get loan tickers")
  public static List<String> getList() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT TICKER FROM RISK_ANALYST_MARGIN_REVIEWED_FULL WHERE LOAN_TYPE = 'LOAN'");
    List<String> listResult = new ArrayList<>();
    try {
      List<HashMap<String, Object>> result = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        result.forEach(object -> {
            String ticker = (String) object.get("TICKER");
            listResult.add(ticker);
          }
        );
        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
