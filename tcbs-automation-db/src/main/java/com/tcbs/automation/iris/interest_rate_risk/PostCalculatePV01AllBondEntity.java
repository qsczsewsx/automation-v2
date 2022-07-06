package com.tcbs.automation.iris.interest_rate_risk;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.staging.AwsTcbsDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCalculatePV01AllBondEntity {

  public static final String PAYMENT_DATE = "payment_date";
  public static final String DISCOUNT_RATE = "discount_rate";
  public static final String PV100 = "pv100";
  public static final String PV200 = "pv200";

  @Step("Get data from PV01 detail table")
  public static List<HashMap<String, Object>> getDataFromPV01DetailInDB() {
    StringBuilder queryBuilder = new StringBuilder();
    List<HashMap<String, Object>> result = new ArrayList<>();
    queryBuilder.append(" select d.* from iris_pv01_summary s join iris_pv01_Detail d on s.id = d.id where s.date_report = (select max(date_report) from iris_pv01_summary); ");
    try {
      List<HashMap<String, Object>> data = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(
        AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      data.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("code", v.get("code"));
        hashMap.put(PAYMENT_DATE, v.get(PAYMENT_DATE));
        hashMap.put("cash_flow", Double.parseDouble(v.get("cash_flow").toString()));
        hashMap.put("proximity", Double.parseDouble(v.get("proximity").toString()));
        hashMap.put(DISCOUNT_RATE, v.get(DISCOUNT_RATE) == null ? null : Double.parseDouble(v.get(DISCOUNT_RATE).toString()));
        hashMap.put("pv01", Double.parseDouble(v.get("pv01").toString()));
        hashMap.put(PV100, Double.parseDouble(v.get(PV100).toString()));
        hashMap.put(PV200, Double.parseDouble(v.get(PV200).toString()));
        hashMap.put("principal_payment", Double.parseDouble(v.get("principal_payment").toString()));
        hashMap.put("coupon_payment", Double.parseDouble(v.get("coupon_payment").toString()));
        hashMap.put("note", v.get("note"));
        result.add(hashMap);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return result;
  }

  @Step("Get data from PV01 detail table")
  public static List<HashMap<String, Object>> fromDbSummary() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select summary.id, summary.code, sum(summary.pv01) pv01, sum(summary.pv100) pv100, sum(summary.pv200) pv200, summary.note from (select d.* from iris_pv01_summary s join  ");
    queryBuilder.append(" iris_pv01_Detail d on s.id = d.id where s.date_report = (select max(date_report) from iris_pv01_summary)) as summary group by summary.id, summary.code, summary.note   ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get list bond code detail table")
  public static List<String> getListBondCodeFromDetail() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select DISTINCT summary.code from (select d.* from iris_pv01_summary s join iris_pv01_Detail d on s.id = d.id where s.date_report =  ");
    queryBuilder.append(" (select max(date_report) from iris_pv01_summary)) as summary  ");
    try {
      return (List<String>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(
        AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from PV01 detail table")
  public static List<HashMap<String, Object>> getDataFromPV01SummaryInDB() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from iris_pv01_summary where date_report = (select max(date_report) from iris_pv01_summary) ");
    List<HashMap<String, Object>> result = new ArrayList<>();
    try {
      List<HashMap<String, Object>> data = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(
        AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      data.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", Double.parseDouble(v.get("id").toString()));
        hashMap.put("request", v.get("request"));
        hashMap.put("code", v.get("code"));
        hashMap.put("pv01", Double.parseDouble(v.get("pv01").toString()));
        hashMap.put(PV100, Double.parseDouble(v.get(PV100).toString()));
        hashMap.put(PV200, Double.parseDouble(v.get(PV200).toString()));
        hashMap.put("except_flag", v.get("except_flag"));
        hashMap.put("note", v.get("note").toString().equals("") ? null : v.get("note"));
        result.add(hashMap);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return result;
  }

  @Step("Get data from from coupon payment table")
  public static List<HashMap<String, Object>> getDataFromCouponPayment() {
    StringBuilder queryBuilder = new StringBuilder();
    String reviewDate = LocalDateTime.now().toString().substring(0, 10);
    queryBuilder.append(" select code, start_date, end_date, payment_date, real_rate, expected_rate, define_date, rate_type from iris_bond_coupon_payment where update_date =  ");
    queryBuilder.append(" (select max(update_date) from iris_bond_coupon_payment)  and payment_date >= '" + reviewDate + "'");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get discount from iris_zero_curve table")
  public static Double getDiscount(Double diffDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select sob from iris_zero_curve where updated_date = (select max (updated_date) from iris_zero_curve) and [date] = :date  ");
    try {
      List<HashMap<String, Object>> listHashMap = (List<HashMap<String, Object>>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("date", diffDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (listHashMap.size() == 0) {
        return null;
      } else {
        return Double.parseDouble(listHashMap.get(0).get("sob").toString());
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static Double getProximityDate(HashMap<String, Object> data) {
    Double diffDate = null;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dateTime1 = LocalDate.parse(data.get(PAYMENT_DATE).toString().substring(0, 10), formatter);
    LocalDate dateTime2 = LocalDate.parse(LocalDateTime.now().toString().substring(0, 10), formatter);
    diffDate = Double.valueOf(ChronoUnit.DAYS.between(dateTime2, dateTime1));
    return diffDate;
  }

}
