package com.tcbs.automation.iris.interest_rate_risk;

import com.tcbs.automation.staging.AwsStagingDwh;
import io.restassured.response.Response;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.tcbs.automation.config.risk.RiskConfig.GET_CURVE_URL;
import static com.tcbs.automation.config.risk.RiskConfig.RISK_DOMAIN_IRIS_BOND_V1_URL;
import static net.serenitybdd.rest.RestRequests.given;

public class PostCalculateCashFlowEntity {

  @Step("Get data from coupon payment table")
  public static List<HashMap<String, Object>> getDataFromCouponPayment(String code) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from iris_bond_coupon_payment where code = :code and update_date = (select max(update_date) from iris_bond_coupon_payment) ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("code", code)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get last payment date for bond code from coupon payment table")
  public static String getLastPaymentDateFromCouponPayment(String code) {
    String lastPaymentDate = "";
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select top(1) payment_date from iris_bond_coupon_payment where update_date = (select max(update_date) from iris_bond_coupon_payment) ");
    queryBuilder.append(" and code = :code order by end_date desc ");
    try {
      HashMap<String, Object> lastPaymentDateHashMap = (HashMap<String, Object>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("code", code)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      lastPaymentDate = lastPaymentDateHashMap.get("payment_date").toString();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return lastPaymentDate;
  }

  public static void executeQuery(String sql, String type, String table) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    session.clear();
    List<String> resultSql = new ArrayList();
    try {
      String[] sqlRows = sql.split("\r\n");
      if (type.equals("Delete")) {
        for (String item : sqlRows) {
          if (item.contains("DELETE")) {
            resultSql.add(item);
          }
        }
      } else {
        // replace get max update date
        LocalDateTime maxDate = getMaxDate(table);
        if (maxDate != null && table.equals("iris_bond_coupon_payment")) {
          sql = sql.replace("update_date", "'" + maxDate.toString().substring(0, 10) + "'");
        } else {
          sql = sql.replace("update_date", "'" + maxDate + "'");
        }
        resultSql = Arrays.asList(sql.split("\r\n"));
      }
      Transaction transaction = session.getTransaction();
      if (transaction != null && transaction.isActive() && transaction.getStatus().canRollback()) {
        transaction.rollback();
      }
      transaction = session.beginTransaction();
      for (String s : resultSql) {
        session.createNativeQuery(s).executeUpdate();
      }
      transaction.commit();
    } catch (Exception e) {
      throw e;
    } finally {
      AwsStagingDwh.awsStagingDwhDbConnection.closeSession();
    }
  }

  @Step("get max date data in a table")
  public static LocalDateTime getMaxDate(String table) {
    StringBuilder query = new StringBuilder();
    LocalDateTime maxDate = null;
    if (table.equals("iris_bond_coupon_payment")) {
      query.append(" select DISTINCT (update_date) from iris_bond_coupon_payment where update_date = (select max(update_date) from iris_bond_coupon_payment) ");
    } else {
      query.append(" select DISTINCT (Updated_Date) from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance) ");
    }
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    try {
      List<Date> date = session.createNativeQuery(query.toString()).getResultList();
      if (date != null && !date.isEmpty()) {
        String dateTime = date.get(0).toString().replace(".0", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        maxDate = LocalDateTime.parse(dateTime, formatter);
      } else {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        maxDate = LocalDateTime.parse(now.format(formatter), formatter);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    session.clear();
    return maxDate;
  }

}
