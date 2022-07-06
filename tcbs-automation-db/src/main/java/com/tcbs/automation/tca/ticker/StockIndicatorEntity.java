package com.tcbs.automation.tca.ticker;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class StockIndicatorEntity {
  @Id
  private String ticker;
  @NumberFormat
  private Double priceToEarning;

  public List<Map<String, Object>> getByStockList(String stockList) {
    String sql = " select sr.LengthReportCal, "
      + "        sr.YearReportCal,   "
      + "        sr.Ticker, "
      + "        sr.RTD21  as [P/E] "
      + " from stx_rto_RatioTTMDaily sr  "
      + " INNER JOIN ( SELECT value "
      + "   FROM STRING_SPLIT(:tickers, ',')) t  "
      + " ON sr.Ticker = t.value "
      + " INNER JOIN stx_cpf_Organization c "
      + " ON sr.Ticker = C.Ticker "
      + " where c.ComGroupCode in ('HNXIndex','UpcomIndex','VNINDEX') "
      + " and sr.tradingDate = (select max(tradingDate) from stx_rto_RatioTTMDaily) ";
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql).setParameter("tickers", stockList)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public List<Map<String, Object>> getAllTicker() {
    String sql = "    SELECT *  "
      + "    FROM ( 	select sr.LengthReportCal, "
      + "                    sr.YearReportCal,   "
      + "                    sr.Ticker, "
      + "                    sr.RTD21  as [P/E] "
      + "            from stx_rto_RatioTTMDaily sr  "
      + "            INNER JOIN stx_cpf_Organization c "
      + "          ON SR.Ticker = C.Ticker "
      + "          where c.ComGroupCode in ('HNXIndex','UpcomIndex','VNINDEX') "
      + "          and sr.tradingDate = (select max(tradingDate) from stx_rto_RatioTTMDaily) "
      + "  ) SR ";
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}