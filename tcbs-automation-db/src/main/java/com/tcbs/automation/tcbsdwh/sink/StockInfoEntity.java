package com.tcbs.automation.tcbsdwh.sink;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockInfoEntity {
  public static SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");

  @Step("Get data from db after saving")
  public static List<HashMap<String, Object>> select() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM stockmarket_data.stock_info_price");

    try {
      List<HashMap<String, Object>> resultList = HfcData.redshiftStockMarket.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return convertField(resultList);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> convertField(List<HashMap<String, Object>> resultList) {
    List<HashMap<String, Object>> result = new ArrayList<>();
    for (HashMap<String, Object> item : resultList) {
      item.put("Symbol", item.get("symbol"));
      item.put("Tradingdate", item.get("tradingdate") == null ? "" : formattedDate.format(item.get("tradingdate")));
      item.put("MaturityDate", item.get("maturitydate") == null ? "" : formattedDate.format(item.get("maturitydate")));
      item.put("FirstTradingDate", item.get("firsttradingdate") == null ? "" : formattedDate.format(item.get("firsttradingdate")));
      item.put("LastTradingDate", item.get("lasttradingdate") == null ? "" : formattedDate.format(item.get("lasttradingdate")));
      item.put("bidprice1", item.get("bidprice1") == null ? "" : item.get("bidprice1"));
      item.put("bidprice2", item.get("bidprice2") == null ? "" : item.get("bidprice2"));
      item.put("bidprice3", item.get("bidprice3") == null ? "" : item.get("bidprice3"));
      result.add(item);
    }
    return result;
  }


  @Step
  public static void truncateData() {
    Session session = HfcData.redshiftStockMarket.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table stockmarket_data.stock_info_price");
    query.executeUpdate();
    trans.commit();
  }
}
