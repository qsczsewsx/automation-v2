package com.tcbs.automation.iris;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetLogEntity {
  @Id
  private String ticker;
  private String loanType;
  private String priceType;
  private Double loanPrice;
  private String reason;
  private String note;
  private String analyst;
  private Timestamp updatedDate;
  private Double loanRatio;
  private Double roomFinal;
  private Double closePriceAdjusted;
  private Double estimatedLoan;
  private Timestamp reviewedDate;
  private String createdBy;
  private String statusTicker;
  private Timestamp editedTime;
  private String bpmReviewTicker;

  @Step("Get lastest data")
  public static List<HashMap<String, Object>> getLog(String reviewType) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT *  ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL_LOG ");
    queryBuilder.append(" WHERE BPM_REVIEW_TYPE = :reviewType ");

    try {
      Query query = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setParameter("reviewType", reviewType);

      return query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }


    return new ArrayList<>();
  }


}
