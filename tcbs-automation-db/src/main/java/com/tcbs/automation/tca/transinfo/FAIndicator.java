package com.tcbs.automation.tca.transinfo;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FAIndicator {
  private static final Logger LOG = LoggerFactory.getLogger(FAIndicator.class);
  private static final String TICKER_STR = "ticker";
  private String ticker;
  private Double eps;
  private Double bvps;
  private Double roe;
  private Double pe;
  private Double pb;
  private Double dividend;
  private Double rs;

  @Step
  public static FAIndicator getByTicker(String ticker) {
    String query = " select :ticker AS ticker,"
                + "    FA.pe as PE,"
                + "    FA.pb as PB,"
                + "    FA.roe as ROE,"
                + "    FA.bvps as BVPS,"
                + "    FA.basic_eps as EPS,"
                + "    FA.dividend AS dividend,"
                + "    RS.TC_RS AS rs"
                + " from ( "
                + "    SELECT Ticker , basic_eps , pe , pb , roe , bvps, dividend"
                + "    from tca_ratio_latest trb "
                + "    where Ticker = :ticker"
                + " ) FA  LEFT JOIN ("
                + "    SELECT TOP 1 TC_RS AS TC_RS"
                + "    FROM Stock_RSRating_Refining srr "
                + "    WHERE Ticker = :ticker"
                + "    ORDER BY DateReport DESC"
                + " ) RS ON 1 = 1";
    try {
      List<Map<String, Object>> lstData = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setParameter(TICKER_STR, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      FAIndicator faIndicator = new FAIndicator();
      faIndicator.setTicker(ticker);
      if (CollectionUtils.isNotEmpty(lstData)) {
        Map<String, Object> mData = lstData.get(0);
        faIndicator.setEps((Double) mData.get("EPS"));
        faIndicator.setBvps((Double) mData.get("BVPS"));
        faIndicator.setPb((Double) mData.get("PB"));
        faIndicator.setPe((Double) mData.get("PE"));
        faIndicator.setRoe((Double) mData.get("ROE"));
        faIndicator.setRs((Double) mData.get("rs"));
        faIndicator.setDividend((Double) mData.get("dividend"));
      }
      return faIndicator;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new FAIndicator();

  }
}
