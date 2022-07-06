package com.tcbs.automation.tcbsdwh.icalendar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockAssetEntity {

  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Call data from proc")
  public static void callProc(String proc, String param) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL " + proc + "(" + param + ")");
    StockAssetEntity.executeQuery(queryBuilder);
  }

  @Step("Get data through proc")
  public static List<HashMap<String, Object>> getAssetStockFromProc(String type) {
    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("past")) {
      queryBuilder.append("SELECT * FROM API.ICALENDAR_ASSET_STOCK_PAST");
    } else if (type.equalsIgnoreCase("future")) {
      queryBuilder.append("SELECT * FROM API.ICALENDAR_ASSET_STOCK_FUTURE");
    } else {
      queryBuilder.append("SELECT * FROM API.ICALENDAR_ASSET_STOCK_ALL");
    }
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from db")
  public static List<HashMap<String, Object>> getAssetStockFromSql(String type, String maxTime) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with stock as ( ");
    queryBuilder.append("   select a.*, CASE  ");
    queryBuilder.append("   WHEN symbol like '%_WFT%' THEN LEFT(symbol, 3)  ");
    queryBuilder.append("   else symbol  ");
    queryBuilder.append("   END AS ticker  ");
    queryBuilder.append("   from dwh.dailyport_stockbal a  ");
    queryBuilder.append("   where datereport = (select max(datereport) from dwh.dailyport_stockbal)) ");
    queryBuilder.append(" SELECT * FROM ");
    queryBuilder.append("   ( select sum(quantity) as qtt, datereport, custodycd, ticker, etlcurdate, etlrundatetime ");
    queryBuilder.append("     from stock ");
    queryBuilder.append("     group by custodycd, ticker, datereport, etlcurdate, etlrundatetime) mystock ");
    queryBuilder.append(" JOIN staging.VSD_STOCK_ACCOUNTING_DATE events  ON mystock.ticker = events.stock_symbol ");

    if (type.equalsIgnoreCase("past")) {
      queryBuilder.append(" WHERE to_date(events.first_trading_date, 'YYYY-MM-DD') <= :maxTime ");
    } else if (type.equalsIgnoreCase("future")) {
      queryBuilder.append(" WHERE to_date(events.first_trading_date, 'YYYY-MM-DD') > :maxTime ");
    } else {
      queryBuilder.append(" WHERE :maxTime = :maxTime ");
    }
    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("maxTime", maxTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    } catch (Exception e) {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
      throw e;
    }
  }

  @Step("Insert Data Test")
  public static void insertDataTest(String tblName, String column, String values) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("INSERT INTO " + tblName);
    queryBuilder.append(" (" + column + ") VALUES ");
    queryBuilder.append("(" + values + ")");
    StockAssetEntity.executeQuery(queryBuilder);
  }


  public static void insertData(String tblName, String assetStockFuture) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    List<HashMap<String, Object>> listentity = mapper.readValue(assetStockFuture, new TypeReference<List<HashMap<String, Object>>>() {
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
      StockAssetEntity.insertDataTest(tblName, column.substring(0, column.length() - 1), value.substring(0, value.length() - 1));
    }
  }

  @Step("Delete Data test")
  public static void delDataAssetStockTest(String tcbsid) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM DWH.SMY_DWH_FLX_ALLSTOCKTXN WHERE TCBSID = " + tcbsid);
    StockAssetEntity.executeQuery(queryBuilder);
  }

  @Step("Delete Data test")
  public static void deleteVsdStockAccountData(String id) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM staging.VSD_STOCK_ACCOUNTING_DATE WHERE id = " + id);
    StockAssetEntity.executeQuery(queryBuilder);
  }

  @Step("Truncate table before test")
  public static void truncateTable(String tblName) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("TRUNCATE TABLE " + tblName);
    StockAssetEntity.executeQuery(queryBuilder);
  }

}

