package com.tcbs.automation.iris;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Getter
@Setter
@Entity
@Table(name = "RISK_CONDITION")
public class RiskConditionEnity {
  @Column(name = "VALUE")
  private String value;

  @Id
  @Column(name = "NAME")
  private String name;

  @Column(name = "ID")
  private Integer id;

  @Column(name = "UPDATE_TIME")
  private Long updateTime;

  @Column(name = "OPERATOR")
  private String operator;

  @Column(name = "REQUIRE")
  private boolean require;

  @Step("get all conditions")
  public static List<HashMap<String, Object>> getAllConfConditons(String timeStamp) {
    StringBuilder query = new StringBuilder();
    if (timeStamp != null && !timeStamp.isEmpty() && timeStamp != "null") {
      query.append("SELECT * FROM RISK_CONDITION WHERE UPDATE_TIME = :updateTime ");
      try {
        return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
          .setParameter("updateTime", timeStamp)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
    } else {
      query.append("SELECT * FROM ( SELECT * FROM RISK_CONDITION ORDER BY ID DESC ) WHERE rownum <= 14 ");
      try {
        return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
    }
    return new ArrayList();
  }

  @Step("get num of conditions")
  public static Integer countRiskConfig() {
    StringBuilder query = new StringBuilder();
    query.append("select * from RISK_CONDITION");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().size();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    IData.idataDbConnection.getSession().close();
    return 0;
  }

  @Step("get risk condition by name")
  public static List<HashMap<String, Object>> getRiskConditionByName(String name) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM (SELECT * FROM RISK_CONDITION WHERE NAME = :name ORDER BY ID DESC) WHERE rownum = 1 ");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("name", name)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    IData.idataDbConnection.getSession().close();
    return new ArrayList();
  }

  @Step("delete risk config after update")
  public static void deleteRiskCondition() {
    Session session = IData.idataDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<RiskConfigEntity> query = session.createQuery(
      " DELETE FROM RiskConditionEnity WHERE ID <> 1  "
    );
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("get risk config id = 1")
  public static List<HashMap<String, Object>> getRiskConditionById1WithoutName(String name) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM RISK_CONDITION WHERE ID = 1 and NAME not in :name");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("name", Arrays.asList(name.split(",")))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get all config without field name")
  public static List<HashMap<String, Object>> getAllRiskConditionWithoutName(String fieldName) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM ( SELECT * FROM RISK_CONDITION WHERE NAME NOT IN :name  ORDER BY ID DESC  ) WHERE rownum < 14 ");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("name", Arrays.asList(fieldName.split(",")))
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
