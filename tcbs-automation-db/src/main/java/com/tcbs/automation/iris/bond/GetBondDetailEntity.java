package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetBondDetailEntity {

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getIssuer(String issuerId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_CONFIG_LIMIT where ISSUER_GA_ID = :issuerId  ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("issuerId", issuerId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Insert data by processId")
  public static void insertData(String issuerGaId) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("INSERT INTO RISK_CONFIG_LIMIT values (RISK_CONFIG_LIMIT_SEQ.nextval,:issuerGaId,'testauto','testauto','ISSUER',:issuerGaId,null,null,'1000000000','ACTIVE',sysdate) ");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.setParameter("issuerGaId", issuerGaId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("Get data from iRis")
  public static void delData(String issuerGaId) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append(" delete  from RISK_CONFIG_LIMIT where ISSUER_GA_ID = :issuerGaId ");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.setParameter("issuerGaId", issuerGaId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
