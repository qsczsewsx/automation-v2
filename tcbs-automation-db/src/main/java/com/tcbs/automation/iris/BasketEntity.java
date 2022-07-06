package com.tcbs.automation.iris;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasketEntity {


  @Step(" get list ticker risk")
  public static List<HashMap<String, Object>> getListTickerRisk() {
    StringBuilder query = new StringBuilder();
    query.append(
      " SELECT* FROM RISK_MARGIN_BASKET WHERE UPDATED_DATE = ( SELECT MAX(UPDATED_DATE) FROM RISK_MARGIN_BASKET ) AND TICKER IN (SELECT TICKER FROM RISK_ANALYST_MARGIN_REVIEWED_FULL  where LOAN_TYPE = 'LOAN') ");

    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }

  @Step("get all basket")
  public static List<HashMap<String, Object>> getAllBasket() {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT t1.BASKET_CODE , t1.BASKET_NAME , t1.NOTE , t1.CREATED_BY , TO_CHAR(t1.UPDATED_DATE, 'DD/MM/YYYY HH24:MI:SS') UPDATED_DATE , ");
    query.append("	t3.TICKER, t3.LOAN_RATIO AS RATE_RATIO, t3.LOAN_RATIO , t3.LOAN_PRICE as MARGIN_PRICE, t3.LOAN_PRICE  , t3.DESCRIPTION , t3.CREATE_TICKER, t3.UPDATE_TICKER ");
    query.append("	FROM RISK_BASKET_INFO t1  ");
    query.append("	LEFT JOIN ");
    query.append("	 ( ");
    query.append(
      "	 	SELECT t2.BASKET_CODE, t2.TICKER, t4.LOAN_RATIO, t4.LOAN_PRICE , t2.DESCRIPTION , t2.CREATE_BY AS CREATE_TICKER, TO_CHAR(t2.UPDATED_DATE, 'DD/MM/YYYY HH24:MI:SS') AS UPDATE_TICKER ");
    query.append("	 	FROM RISK_MARGIN_BASKET t2  ");
    query.append("	 	INNER JOIN RISK_ANALYST_MARGIN_REVIEWED_FULL t4  ");
    query.append("	 	ON t2.TICKER = t4.TICKER  ");
    query.append("	 	WHERE t2.UPDATED_DATE = (SELECT MAX(UPDATED_DATE) FROM RISK_MARGIN_BASKET) ");
    query.append("	 	AND t4.LOAN_TYPE  != 'BLACK_LIST' ");
    query.append("	 	ORDER BY t2.BASKET_CODE, t2.TICKER asc ");
    query.append("	 ) t3 ");
    query.append("	ON t1.BASKET_CODE  = t3.BASKET_CODE ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get all basket")
  public static List<HashMap<String, Object>> getBaskets() {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT *  ");
    query.append("	FROM RISK_BASKET_INFO ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get all basket")
  public static List<HashMap<String, Object>> getBasketWithCondition(List<String> basketCodes) {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT *  ");
    query.append("	FROM RISK_BASKET_INFO   ");
    query.append("	WHERE BASKET_CODE in :basketCodes  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("basketCodes", basketCodes)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get all basket")
  public static List<HashMap<String, Object>> getBasketDetailWithCondition(String basketCodes, List<String> tickers) {
    if (tickers == null || tickers.isEmpty()) {
      return new ArrayList<>();
    }
    StringBuilder query = new StringBuilder();
    query.append("	SELECT *  FROM RISK_MARGIN_BASKET t2  ");
    query.append("	WHERE BASKET_CODE in :basketCode and  TICKER in :tickers ");
    query.append(" AND UPDATED_DATE = (SELECT MAX(UPDATED_DATE) FROM RISK_MARGIN_BASKET WHERE BASKET_CODE = t2.BASKET_CODE)  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("basketCode", basketCodes)
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get all basket config ")
  public static List<HashMap<String, Object>> getBasketConfigWithCondition(List<String> tickers, List<String> listCreated) {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT *  FROM RISK_MARGIN_BASKET_CONFIG   ");
    query.append("	WHERE TICKER in :ticker and  CREATED_BY in :created  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", tickers)
        .setParameter("created", listCreated)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("delete data")
  public static void deleteData(String basketCode) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("delete from RISK_BASKET_INFO where BASKET_CODE = '" + basketCode + "' ");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.executeUpdate();
    session.getTransaction().commit();
  }


  @Step("delete multiple data")
  public static void deleteMultipleData(List<String> basketCode) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("delete from RISK_BASKET_INFO where BASKET_CODE IN :baskets ");
    Query query = session.createNativeQuery(queryBuilder.toString())
      .setParameter("baskets", basketCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("delete data")
  public static void deleteDataDetailBasket(String basketCode, List<String> ticker) {
    if (ticker != null && !ticker.isEmpty()) {
      StringBuilder queryBuilder = new StringBuilder();
      Session session = AwsIRis.AwsIRisDbConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      queryBuilder.append("delete from RISK_MARGIN_BASKET where BASKET_CODE = :basketCode and TICKER IN :ticker ");
      Query query = session.createNativeQuery(queryBuilder.toString())
        .setParameter("basketCode", basketCode)
        .setParameter("ticker", ticker);
      query.executeUpdate();
      session.getTransaction().commit();
    }
  }

  @Step("get all basket config")
  public static List<HashMap<String, Object>> getAllBasketConfig() {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT ID, TICKER, CREATED_BY ");
    query.append("	FROM RISK_MARGIN_BASKET_CONFIG  ");
    query.append("	WHERE CREATED_DATE = (SELECT MAX(CREATED_DATE) FROM RISK_MARGIN_BASKET_CONFIG) ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("delete basket config")
  public static void deleteBasketConfig(String createdBy, String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("delete from RISK_MARGIN_BASKET_CONFIG where CREATED_BY = '" + createdBy + "' and TICKER = '" + ticker + "'");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("get all basket")
  public static List<HashMap<String, Object>> verifyBasket(List<String> listBasketCodes) {
    StringBuilder query = new StringBuilder();
    query.append("	SELECT t1.BASKET_CODE , t1.BASKET_NAME , t1.NOTE , t1.CREATED_BY , TO_CHAR(t1.UPDATED_DATE, 'DD/MM/YYYY HH24:MI:SS') UPDATED_DATE , ");
    query.append("	t3.TICKER, t3.RATE_RATIO, t3.LOAN_RATIO , t3.MARGIN_PRICE, t3.LOAN_PRICE , t3.DESCRIPTION , t3.CREATE_TICKER, t3.UPDATE_TICKER ");
    query.append("	FROM RISK_BASKET_INFO t1  ");
    query.append("	LEFT JOIN ");
    query.append("	 ( ");
    query.append(
      "	 	SELECT t2.BASKET_CODE, t2.TICKER, t2.RATE_RATIO, t2.LOAN_RATIO, t2.MARGIN_PRICE, t2.LOAN_PRICE , t2.DESCRIPTION , t2.CREATE_BY AS CREATE_TICKER, TO_CHAR(t2.UPDATED_DATE, 'DD/MM/YYYY HH24:MI:SS') AS UPDATE_TICKER ");
    query.append("	 	FROM RISK_MARGIN_BASKET t2  ");
    query.append("	 	WHERE t2.UPDATED_DATE = (SELECT MAX(UPDATED_DATE) FROM RISK_MARGIN_BASKET WHERE BASKET_CODE = t2.BASKET_CODE) ");
    query.append("	 	ORDER BY t2.BASKET_CODE, t2.TICKER asc ");
    query.append("	 ) t3 ");
    query.append("	ON t1.BASKET_CODE  = t3.BASKET_CODE ");
    if (listBasketCodes != null && !listBasketCodes.isEmpty()) {
      query.append("	WHERE t1.BASKET_CODE IN :list ");
    }
    try {
      NativeQuery sql = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString());
      if (listBasketCodes != null && !listBasketCodes.isEmpty()) {
        sql.setParameter("list", listBasketCodes);
      }
      return sql
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}