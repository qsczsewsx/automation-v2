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

public class MarketInfoEntity {
  public static SimpleDateFormat formattedDate = new SimpleDateFormat("dd/MM/yyyy");

  @Step("Get data from db after saving")
  public static List<HashMap<String, Object>> select() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM stockmarket_data.market_info_price where sequencemsg > 0 ");

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
      item.put("sequenceMsg", item.get("sequencemsg"));
      item.put("tradingdate", formattedDate.format(item.get("tradingdate")));
      result.add(item);
    }
    return result;
  }

  @Step
  public static void truncateData() {
    Session session = HfcData.redshiftStockMarket.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table stockmarket_data.market_info_price");
    query.executeUpdate();
    trans.commit();
  }
}
