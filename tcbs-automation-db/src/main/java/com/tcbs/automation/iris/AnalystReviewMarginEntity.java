package com.tcbs.automation.iris;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RISK_CONDITION")
public class AnalystReviewMarginEntity {
  @Id
  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "LOAN_TYPE")
  private String loanType;

  @Column(name = "PRICE_TYPE")
  private double priceType;

  @Column(name = "LOAN_PRICE_FINAL")
  private Double loanPriceFinal;

  @Column(name = "LOAN_PRICE")
  private Double loanPrice;

  @Column(name = "REASON")
  private String reason;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "ANALYST")
  private String analyst;

  @Column(name = "BPM_PROCESS_ID")
  private String bpmProcessId;

  @Column(name = "LOAN_RATIO")
  private Double loanRatio;

  @Column(name = "ROOM_FINAL")
  private Double roomFinal;

  @Column(name = "CREATED_DATE")
  private LocalDate createdDate;

  @Step("get review margin by ticker")
  public static HashMap<String, Object> getReviewMarginByTicker(String ticker, String reviewDate) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT TICKER, LOAN_TYPE, LOAN_PRICE, PRICE_TYPE , LOAN_RATIO, ROOM_FINAL, ROOM_USED \n");
    query.append("FROM RISK_ANALYST_MARGIN_REVIEWED_FULL WHERE TICKER = :ticker ");
    query.append("AND TO_CHAR(REVIEWED_DATE , 'YYYY-MM-DD') <= :reviewDate ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
        .setParameter("reviewDate", reviewDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }
}
