package com.tcbs.automation.iris;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@Table(name = "RISK_CONFIG")
public class RiskConfigEntity {

  static final Logger logger = LoggerFactory.getLogger(RiskConfigEntity.class);
  @Id
  @Column(name = "NAME")
  private String name;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "ID")
  private Integer id;

  @Column(name = "UPDATE_TIME")
  private Long updateTime;

  @Step("get num of config")
  public static Integer countRiskConfig() {
    StringBuilder query = new StringBuilder();
    query.append("select * from RISK_CONFIG");
    try {
      return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().size();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    IData.idataDbConnection.getSession().close();
    return 0;
  }

  @Step("get risk config by name")
  public static List<HashMap<String, Object>> getDetailRiskConfig(String name) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM (SELECT * FROM RISK_CONFIG WHERE NAME = :name ORDER BY ID DESC) WHERE rownum = 1 ");
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

  @Step("get all config")
  public static List<HashMap<String, Object>> getAllConfig(String timeStamp) {
    StringBuilder query = new StringBuilder();
    if (timeStamp != null && !timeStamp.isEmpty() && timeStamp != "null") {
      query.append("SELECT * FROM RISK_CONFIG WHERE UPDATE_TIME = :updateTime ");
      try {
        return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
          .setParameter("updateTime", timeStamp)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
    } else {
      query.append("SELECT * FROM ( SELECT * FROM RISK_CONFIG ORDER BY ID DESC ) WHERE rownum <= 7 ");
      try {
        return IData.idataDbConnection.getSession().createNativeQuery(query.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } catch (Exception ex) {
        StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      }
    }
    return new ArrayList();
  }

  @Step("delete risk config after update")
  public static void deleteRiskConfig() {
    Session session = IData.idataDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<RiskConfigEntity> query = session.createQuery(
      " DELETE FROM RiskConfigEntity WHERE ID <> 1 "
    );
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("get risk config id = 1")
  public static List<HashMap<String, Object>> getRiskConfigById1WithoutName(String name) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM RISK_CONFIG WHERE ID = 1 and NAME not in :name");
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
  public static List<HashMap<String, Object>> getAllConfigWithoutName(String fieldName) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM ( SELECT * FROM ( SELECT * FROM RISK_CONFIG ORDER BY ID DESC ) WHERE rownum <= 7 ) WHERE NAME NOT IN :name  ");
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
