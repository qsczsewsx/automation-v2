package com.tcbs.automation.tca.pricenews;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//
public class TickerPriceInfo {
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

  private String ticker;
  private String atDate;
  private Double price;
  private Double adjClose;

  /**
   * @param listTicker: list Contain ticker, date
   * @return list contain ticker, date, adjclose
   */
  @Step
  public List<TickerPriceInfo> getPriceInfo(List<TickerPriceInfo> listTicker) {
    // make condition
    String conditionTmp2 = "(ticker = '%s' AND CAST(tradingdate as date) = CAST('%s' as date))";
    String conditionStr2 = listTicker.stream().map(
      item -> {
        return String.format(conditionTmp2, item.ticker, item.atDate);
      }
    ).collect(Collectors.joining(" OR "));

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT Ticker, CAST(tradingdate as date) AS AtDate, closePriceAdjusted as AdjClose ");
    queryBuilder.append("FROM Smy_dwh_stox_MarketPrices_AllData ");
    queryBuilder.append("WHERE @condition2 ");
    String query = queryBuilder.toString();
    query = query.replaceAll("@condition2", conditionStr2);

    List<TickerPriceInfo> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isEmpty(listResult)) {
        result.forEach(map -> {
            TickerPriceInfo ticker = TickerPriceInfo.builder()
              .ticker((String) map.get("Ticker"))
              .atDate(sdf.format(map.get("AtDate")))
              .adjClose(map.get("AdjClose") == null ? null : (Double.valueOf(map.get("AdjClose").toString())))
              .build();
            listResult.add(ticker);
          }
        );

        return listResult;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public Map<String, TickerPriceInfo> getMapPriceInfo(List<TickerPriceInfo> listTicker) {
    List<TickerPriceInfo> listResult = getPriceInfo(listTicker);
    if (CollectionUtils.isEmpty(listResult)) {
      return new HashMap<>();
    }
    return listResult.stream().collect(Collectors.toMap(item -> (item.getTicker().trim() + item.getAtDate().trim()), item -> item));
  }
}
