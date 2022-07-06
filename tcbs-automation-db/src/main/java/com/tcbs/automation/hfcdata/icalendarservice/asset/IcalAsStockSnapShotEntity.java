package com.tcbs.automation.hfcdata.icalendarservice.asset;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IcalAsStockSnapShotEntity {
  public static final String CUSTODYCD = "custodycd";
  public static final String SYSBOL = "symbol";
  public static final String QUANTITY = "quantity";
  public static final String MKTAMT = "mktamt";
  public static final HibernateEdition REDSHIFT = Database.TCBS_DWH.getConnection();

  @Step("get data from api.ical_as_stock_snapshot table")
  public static List<HashMap<String, Object>> stockSnapShotTable() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM api.ical_as_stock_snapshot");
    try {
      return REDSHIFT.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Truncate table")
  public static void truncateTable() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("truncate table ical_as_cw_snapshot");
    executeQuery(queryBuilder);
  }

  @Step(" Call proc ")
  public static void callProc(String procedureName, int etl) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL ").append(procedureName).append("(").append(etl).append(");");
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = REDSHIFT.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createSQLQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      throw e;
    } finally {
      REDSHIFT.closeSession();

    }
  }

  @Step("Get asset stock of customer")
  public static List<HashMap<String, Object>> getAssetStockCus() {

    List<HashMap<String, String>> listuser = null;
    List<HashMap<String, Object>> listAssetNonGroup = null;

    StringBuilder listAllUser = new StringBuilder();
    listAllUser.append(" select tcbsid,custodycd from dwh.Smy_dwh_cas_AllUserView ");
    listAllUser.append("where etlcurdate = (select max(etlcurdate) from dwh.Smy_dwh_cas_AllUserView)");


    StringBuilder listAssetUser = new StringBuilder();
    listAssetUser.append(
      "select" +
        " custodycd," +
        " (case when right(symbol,4) = '_WFT' then left(symbol,3) else symbol end) as symbol," +
        " quantity," +
        " (case when mktamt is null then 0 else mktamt end) as mktamt" +
        " from dwh.dailyport_stockbal " +
        " where trunc(datereport) = trunc(staging.f_dateadd_working_day(getdate(), -2)) " +
        " and ( (len(symbol) = 3) or (right(symbol,4) = '_WFT')) order by custodycd,symbol");

    try {
      listuser = REDSHIFT.getSession()
        .createNativeQuery(listAllUser.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();


      listAssetNonGroup = REDSHIFT.getSession()
        .createNativeQuery(listAssetUser.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      listAssetNonGroup = calculateQuantity(listAssetNonGroup);

      Map<String, String> hashMap = convert(listuser);

      return merge(listAssetNonGroup, hashMap);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return new ArrayList<>();
    }
  }

  public static List<HashMap<String, Object>> calculateQuantity(List<HashMap<String, Object>> listAssetNonGroup) {
    List<HashMap<String, Object>> result = null;
    HashMap<String, HashMap<String, Object>> mapping = new HashMap<>();
    for (HashMap<String, Object> item : listAssetNonGroup) {
      String custodycd = (String) item.get(CUSTODYCD);
      String symbol = (String) item.get(SYSBOL);
      String key = custodycd.concat(symbol);
      double quantity = (double) item.get(QUANTITY);
      double mktamt = (double) item.get(MKTAMT);
      if (!mapping.containsKey(key)) {
        mapping.put(key, new HashMap<>());
      }
      mapping.get(key).put(CUSTODYCD, custodycd);
      mapping.get(key).put("ticker", symbol);
      double temp_quantity = mapping.get(key).containsKey(QUANTITY) ? (double) mapping.get(key).get(QUANTITY) : 0;
      mapping.get(key).put(QUANTITY, temp_quantity + quantity);

      double temp_mktamt = mapping.get(key).containsKey(MKTAMT) ? (double) mapping.get(key).get(MKTAMT) : 0;
      mapping.get(key).put(MKTAMT, temp_mktamt + mktamt);
    }
    result = new ArrayList<>();
    for (Map.Entry<String, HashMap<String, Object>> entry : mapping.entrySet()) {
      HashMap value = entry.getValue();
      result.add(value);
    }
    return result;
  }

  public static Map<String, String> convert(List<HashMap<String, String>> listUser) {
    Map<String, String> map = new HashMap<>();
    for (HashMap<String, String> item : listUser) {
      String custodycd = item.get(CUSTODYCD);
      String tcbsid = item.get("tcbsid");
      map.put(custodycd, tcbsid);
    }
    return map;
  }

  public static List<HashMap<String, Object>> merge(List<HashMap<String, Object>> listAssetGroup, Map<String, String> mappingTcbsId) {

    List<HashMap<String, Object>> result = new ArrayList<>();
    for (HashMap<String, Object> item : listAssetGroup) {
      String custodycd = (String) item.get(CUSTODYCD);
      if (mappingTcbsId.containsKey(custodycd)) {
        item.put("tcbsid", mappingTcbsId.get(custodycd));
        result.add(item);
      }
    }
    return result;
  }
}
