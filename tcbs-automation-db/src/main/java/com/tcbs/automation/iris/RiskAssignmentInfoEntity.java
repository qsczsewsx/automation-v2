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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author minhnv8
 * @date 01/07/2021 17:50
 */

@Getter
@Setter
@Entity
@Table(name = "RISK_ASSIGNMENT_INFO")
public class RiskAssignmentInfoEntity {
  @Id
  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "ANALYST_ID")
  private String analystId;

  @Step("get all assignment info latest")
  public static List<HashMap<String, Object>> getRiskAssignmentInfo() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM RISK_ASSIGNMENT_INFO  WHERE CREATE_DATE=(SELECT MAX(CREATE_DATE)FROM RISK_ASSIGNMENT_INFO)");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getAssignmentNull() {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM RISK_ASSIGNMENT_INFO  WHERE CREATE_DATE=(SELECT MAX(CREATE_DATE)FROM RISK_ASSIGNMENT_INFO) AND (TICKER IS NULL OR ANALYST_ID IS NULL OR TICKER='' OR ANALYST_ID='')");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
