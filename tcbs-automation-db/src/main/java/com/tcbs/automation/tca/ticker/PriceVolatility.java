package com.tcbs.automation.tca.ticker;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceVolatility {
  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  @Id
  @Column(name = "Ticker")
  private String ticker;

  @Column(name = "TradingDate")
  private Date tradingDate;

  @Column(name = "AdjClose")
  private Double adjClose;

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public List<PriceVolatility> getByTicker(String ticker, Date preWorkingDay) {

    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT TICKER as Ticker, TRADINGDATE AS TradingDate, CAST(closePriceAdjusted AS FLOAT) as AdjClose");
    queryBuilder.append(" FROM stx_mrk_HoseStock  ");
    queryBuilder.append(" WHERE TICKER = :ticker AND TRADINGDATE >= :MinDate AND closePriceAdjusted >  0");
    queryBuilder.append(" UNION ");
    queryBuilder.append(" SELECT TICKER, TRADINGDATE, CAST(closePriceAdjusted AS FLOAT) ");
    queryBuilder.append(" FROM stx_mrk_HnxStock ");
    queryBuilder.append(" WHERE TICKER = :ticker AND TRADINGDATE >= :MinDate AND closePriceAdjusted >  0");
    queryBuilder.append(" UNION");
    queryBuilder.append(" SELECT TICKER, TRADINGDATE, CAST(closePriceAdjusted AS FLOAT) ");
    queryBuilder.append(" FROM stx_mrk_UpcomStock  ");
    queryBuilder.append(" WHERE TICKER = :ticker AND TRADINGDATE >= :MinDate AND closePriceAdjusted >  0");

    String query = queryBuilder.toString();

    List<PriceVolatility> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setParameter("ticker", ticker)
        .setParameter("MinDate", preWorkingDay)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(result)) {
        for (Map<String, Object> item : result) {
          PriceVolatility priceVolatility = PriceVolatility.builder()
            .ticker(String.valueOf(item.get("Ticker")).trim())
            .tradingDate((Date) item.get("TradingDate"))
            .adjClose((Double) item.get("AdjClose"))
            .build();
          listResult.add(priceVolatility);
        }
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listResult;
  }

}
