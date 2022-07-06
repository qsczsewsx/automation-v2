package com.tcbs.automation.stockmarket.bondorion;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NegotiateTradingEntity {
  @Step("Get data hose")
  public static List<HashMap<String, Object>> getDataFromDb(String currentDate, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();

    switch (type) {
      case "HOSE":
        queryStringBuilder.append(" SELECT BondCode, cast((cast(TradingDate as time)) as char) as TradingDate , Volume, Value, AveragePrice FROM TCS_HOSE_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y-%m-%d') = '")
          .append(currentDate)
          .append("' ");
        break;
      case "HNX":
        queryStringBuilder.append(" SELECT BondCode, cast((cast(TradingDate as time)) as char) as TradingDate, Volume, Value, AveragePrice FROM TCS_HNX_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y-%m-%d') = '")
          .append(currentDate)
          .append("' ");
        break;
      case "derivative":
        queryStringBuilder.append("SELECT ticker as BondCode, cast((cast(TradingDate as time)) as char) as TradingDate, volume as Volume, value as Value, price as AveragePrice FROM derivative_negotiate_trading WHERE DATE_FORMAT(TradingDate, '%Y-%m-%d') = '")
          .append(currentDate)
          .append("' ");
        break;
      default:
        break;
    }

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  public static Long convertStringDateToTimeStamp(String dateString) throws ParseException {
    Date date = new SimpleDateFormat("yyyyMMdd").parse(dateString);
    return date.getTime();
  }

  @Step
  public static List<HashMap<String, Object>> fromDB(String floor, String from
    , String to, String bondCodes) {
    StringBuilder queryStringBuilder = new StringBuilder();
    String table = "";
    if ("HOSE".equalsIgnoreCase(floor)) {
      table = "TCS_HOSE_Negotiate_Trading";
    } else if ("HNX".equalsIgnoreCase(floor)) {
      table = "TCS_HNX_Negotiate_Trading";
    }

    Long fromLong = null;
    Long toLong = null;
    try {
      fromLong = convertStringDateToTimeStamp(from);
      toLong = convertStringDateToTimeStamp(to);
      toLong = toLong + 86399000L;
    } catch (ParseException e) {
      e.printStackTrace();
    }

    String pattern = "yyyy-MM-dd HH:mm:ss";
    DateFormat df = new SimpleDateFormat(pattern);

    Date fromDate = new Date(fromLong);
    Date toDate = new Date(toLong);

    String fromStr = df.format(fromDate);
    String toStr = df.format(toDate);


    queryStringBuilder.append("SELECT " +
      "BondCode," +
      "cast((cast(TradingDate as date)) as char) as TradingDate," +
      "Volume," +
      "Value," +
      "AveragePrice " +
      " FROM " + table + " WHERE TradingDate between STR_TO_DATE(p_fromStr,'%Y-%m-%d %H:%i:%s')".replace("p_fromStr", "'" + fromStr + "'") +
      " and STR_TO_DATE(p_toStr,'%Y-%m-%d %H:%i:%s')".replace("p_toStr", "'" + toStr + "'"));

    queryStringBuilder.append(" and value > 0 and volume > 0");


    if (!bondCodes.isEmpty()) {
      String[] split = bondCodes.split(",");
      String listBondCode = "";
      for (int i = 0; i < split.length; i++) {
        String ticker = split[i];
        listBondCode = listBondCode + "'" + ticker + "'" + ",";
      }
      listBondCode = listBondCode.substring(0, listBondCode.length() - 1);

      queryStringBuilder.append(" and BondCode in (listBondCode)".replace("listBondCode", listBondCode));
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    }
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Delete Data for testing")
  public static void delDataTest(String tblName, String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM " + tblName + " WHERE DATE_FORMAT(TradingDate, '%Y-%m-%d') = '" + date + "'");
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = Stockmarket.stockMarketConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      Stockmarket.stockMarketConnection.closeSession();
    } catch (Exception e) {
      Stockmarket.stockMarketConnection.closeSession();
      throw e;
    }
  }
}
