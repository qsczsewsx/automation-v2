package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RSIndicator {
  private String ticker;
  private String tradingDate;
  private Double value;

  @Step
  public static List<Map<String, Object>> getByTicker(String ticker, Long from, Long to, String type) {
    String fromDate = getDateTime(from);
    String toDate = getDateTime(to);
    String query;
    if (type.equalsIgnoreCase("3D")) {
      query = "SELECT \n" +
        "CONVERT(varchar, DateReport , 120) AS tradingDate , \n" +
        "Ticker AS ticker, \n" +
        "RS_Rank_3D AS value \n";
    } else if (type.equalsIgnoreCase("1M")) {
      query = "SELECT \n" +
        "CONVERT(varchar, DateReport , 120) AS tradingDate , \n" +
        "Ticker AS ticker , \n" +
        "RS_Rank_1M AS value \n";
    } else if (type.equalsIgnoreCase("3M")) {
      query = "SELECT \n" +
        "CONVERT(varchar, DateReport , 120) AS tradingDate , \n" +
        "Ticker AS ticker, \n" +
        "RS_Rank_3M AS value \n";
    } else if (type.equalsIgnoreCase("1Y")) {
      query = "SELECT \n" +
        "CONVERT(varchar, DateReport , 120) AS tradingDate , \n" +
        "Ticker AS ticker, \n" +
        "RS_Rank_1Y AS value \n";
    } else {
      query = "SELECT \n" +
        "CONVERT(varchar, DateReport , 120) AS tradingDate , \n" +
        "Ticker AS ticker, \n" +
        "TC_RS AS value \n";
    }
    query = query + " FROM Stock_RSRating_Refining \n" +
      "WHERE Ticker = :ticker \n" +
      "AND DateReport >= :fromDate\n" +
      "AND DateReport  <= :toDate\n" +
      "ORDER BY DateReport DESC";
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setParameter("ticker", ticker)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static String getDateTime(long t) {
    Timestamp ts = new Timestamp(t * 1000);
    Date d = new Date(ts.getTime());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
    return sdf.format(d);
  }

}
