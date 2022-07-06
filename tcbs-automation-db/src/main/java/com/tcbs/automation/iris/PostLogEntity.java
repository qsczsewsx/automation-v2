package com.tcbs.automation.iris;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor


public class PostLogEntity {
  @Id
  private String ticker;
  private String loanType;
  private String priceType;
  private Double loanPrice;
  private String reason;
  private String note;
  private String analyst;
  private Double loanRatio;
  private Double roomFinal;
  private Double closePriceAdjusted;
  private Double estimatedLoan;
  private String reviewedDate;
  private String createdBy;
  private String statusTicker;

  @Step("Get lastest data")
  public static List<HashMap<String, Object>> getLastestData() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  TICKER, LOAN_TYPE, PRICE_TYPE, LOAN_PRICE, REASON, NOTE, ANALYST, LOAN_RATIO, ROOM_FINAL, ");
    queryBuilder.append("  CLOSE_PRICE_ADJUSTED, ESTIMATED_LOAN, REVIEWED_DATE, CREATED_BY, STATUS_TICKER ");
    queryBuilder.append(" FROM RISK_ANALYST_MARGIN_REVIEWED_FULL_LOG ");
    queryBuilder.append(" WHERE UPDATED_DATE = (select max(UPDATED_DATE) from RISK_ANALYST_MARGIN_REVIEWED_FULL_LOG) ");

    try {
      Query query = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString());

      return query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }


    return new ArrayList<>();
  }

  @Step("Delete data by processId")
  public static void deleteData(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();


    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("DELETE FROM RISK_ANALYST_MARGIN_REVIEWED_FULL_LOG where ticker = :ticker ");
    Query query = session.createNativeQuery(queryBuilder.toString()).setParameter("ticker", ticker);
    query.executeUpdate();
    session.getTransaction().commit();


  }


}