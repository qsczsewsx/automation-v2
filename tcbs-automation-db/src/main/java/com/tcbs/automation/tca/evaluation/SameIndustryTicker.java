package com.tcbs.automation.tca.evaluation;

import com.tcbs.automation.tca.TcAnalysis;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseDouble;

@Getter
@Setter
@Builder
public class SameIndustryTicker {
  private String ticker;
  private Double priceToBook;
  private Double priceToEarning;
  private Double valueBeforeEbitda;

  @Step
  public static List<SameIndustryTicker> getByIndustryL2Id(String ticker) {
    String query = "SELECT EP.Peer AS ticker"
                + "   , marcap AS [MarketCap]"
                + "   , pe AS [PoE] "
                + "   , pb AS [PoB]"
                + "   , ev_on_ebitda AS [EVoEBITDA]"
                + " FROM evaluation_peer EP"
                + " LEFT JOIN tca_ratio_latest RL"
                + "   ON EP.Peer = RL.TICKER"
                + " WHERE EP.Ticker = :TICKER"
                + " ORDER BY marcap DESC";

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession()
          .createNativeQuery(query)
          .setParameter("TICKER", ticker)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();

      List<SameIndustryTicker> listTickers = new ArrayList<>();
      for (Map<String, Object> result : resultList) {
        listTickers.add(
          SameIndustryTicker.builder()
            .ticker(result.get("ticker").toString())
            .priceToEarning(parseDouble(result.get("PoE")))
            .priceToBook(parseDouble(result.get("PoB")))
            .valueBeforeEbitda(parseDouble(result.get("EVoEBITDA")))
            .build()
        );
      }

      return listTickers;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
