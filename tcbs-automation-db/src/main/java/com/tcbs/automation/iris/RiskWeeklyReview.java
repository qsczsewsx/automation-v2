package com.tcbs.automation.iris;

import com.tcbs.automation.staging.AwsStagingDwh;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RISK_ANALYST_MARGIN_REVIEWED")
public class RiskWeeklyReview {

  @Step("get all risk reviewed")
  public static List<HashMap<String, Object>> getAllRiskReviewed() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT TICKER , CLOSE_PRICE_ADJUSTED from ( ");
    query.append(" SELECT TICKER , CLOSE_PRICE_ADJUSTED, ma_5 FROM RISK_ANALYST_MARGIN_REVIEWED_FULL ");
    query.append(" where LOAN_TYPE = 'LOAN'  ) ");
    query.append("  where ma_5 > = 1.1 *CLOSE_PRICE_ADJUSTED or ma_5 <= 0.9 *CLOSE_PRICE_ADJUSTED ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    AwsIRis.AwsIRisDbConnection.getSession().close();
    return new ArrayList();
  }

  @Step("get all risk reviewed")
  public static List<HashMap<String, Object>> getWhitelist() {
    StringBuilder query = new StringBuilder();

    query.append("SELECT distinct SYMBOL as TICKER FROM Stg_flx_020201 ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    AwsStagingDwh.awsStagingDwhDbConnection.getSession().close();
    return new ArrayList();
  }
}
