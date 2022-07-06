package com.tcbs.automation.stockmarket.intradaymarket;

import com.tcbs.automation.stockmarket.Stockmarket;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
public class StockValueEntity {
  private static final Logger logger = LoggerFactory.getLogger(StockValueEntity.class);

  @Step("get accVal and forecast")
  public static List<HashMap<String, Object>> getForecastValue(String exchange, String idt2, long fromTime) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * " +
      "FROM intraday_forecast_val_15min mvm " +
      "WHERE exchange = :exchange AND icbCodeL2 = :idt2 " +
      "AND updated_time >= (SELECT MAX(updated_time) FROM intraday_forecast_val_15min where exchange = :exchange AND icbCodeL2 = :idt2) " +
      "AND updated_time >= :from_time");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("exchange", exchange)
        .setParameter("idt2", idt2)
        .setParameter("from_time", fromTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
