package com.tcbs.automation.dwh.ixu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.io.IOException;
import java.util.*;

public class GetCustomerTcbsEkycEntity {

  @Step("Delete Data test")
  public static void delDataEtlCurDate(String etlCurDate) {
    String date = etlCurDate.replace("-", "");
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM smy_dwh_dwhtcb_cust_ekyc_campaign WHERE etlcurdate = " + date);
    executeQuery(queryBuilder);
  }

  @Step("Delete Data test")
  public static void delDataTest(String t24Cus) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM smy_dwh_dwhtcb_cust_ekyc_campaign WHERE t24_cust = " + t24Cus);
    executeQuery(queryBuilder);
  }

  @Step("Get max etlCurDate")
  public static List<HashMap<String, Object>> getData(String ekycDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT tcbsid FROM smy_dwh_dwhtcb_cust_ekyc_campaign ");
    queryBuilder.append(" where etlcurdate = (select max(etlcurdate) from smy_dwh_dwhtcb_cust_ekyc_campaign) ");
    queryBuilder.append(" and ekyc_date = :ekycDate ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ekycDate", ekycDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get max etlCurDate")
  public static List<HashMap<String, Object>> maxEtlCurDate() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT max(etlcurdate) as etlcurdate FROM smy_dwh_dwhtcb_cust_ekyc_campaign");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static String getMaxEtlCurDate() {
    List<HashMap<String, Object>> maxEtlCurDate = maxEtlCurDate();
    Iterator<HashMap<String, Object>> iterator = maxEtlCurDate.iterator();
    HashMap<String, Object> result = iterator.next();
    return result.get("etlcurdate").toString();
  }


  @Step("Insert Data Test")
  public static void insertDataTest(String tblName, String column, String values) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("INSERT INTO " + tblName);
    queryBuilder.append(" (" + column + ") VALUES ");
    queryBuilder.append("(" + values + ")");
    executeQuery(queryBuilder);
  }


  public static void insertData(String tblName, String data) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<HashMap<String, Object>> listentity = mapper.readValue(data, new TypeReference<List<HashMap<String, Object>>>() {
    });
    for (HashMap<String, Object> item : listentity) {
      String value = new String();
      String column = new String();
      for (Map.Entry<String, Object> map : item.entrySet()) {
        String key = map.getKey();
        Object val = map.getValue();
        column = column + key + ",";
        value = value + val + ",";
      }
      insertDataTest(tblName, column.substring(0, column.length() - 1), value.substring(0, value.length() - 1));
    }
  }


  public static void executeQuery(StringBuilder sql) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      Dwh.dwhDbConnection.closeSession();
    } catch (Exception e) {
      Dwh.dwhDbConnection.closeSession();
      throw e;
    }
  }
}
