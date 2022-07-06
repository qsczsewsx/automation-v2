package com.tcbs.automation.stockmarket.transinfo;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransInfoTradingEntity {
  private final static Logger LOG = LoggerFactory.getLogger(TransInfoTradingEntity.class);

  public static List<HashMap<String, Object>> getTransInfoTrading(String ticker, long fromTime, long toTime) {
    LOG.info("fromTime = {}, toTime={}", fromTime, toTime);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT *\n" +
      "FROM \n" +
      "(\n" +
      "    SELECT DISTINCT(seq_time) AS seq_time,\n" +
      "        ticker AS ticker ,\n" +
      "        over_bought AS overBt,\n" +
      "        over_sold AS overSd,\n" +
      "        over_bought /(over_bought + over_sold) as overBtPct \n" +
      "    FROM bid_ask_by_1min babm \n" +
      "    WHERE ticker = :p_ticker\n" +
      "    AND seq_time >= :p_from_time \n" +
      "    AND seq_time < :p_to_time\n" +
      "    ORDER BY seq_time DESC\n" +
      "    LIMIT 1\n" +
      ") bisask\t\n" +
      "\n" +
      "LEFT JOIN\n" +
      "(\n" +
      "    SELECT\n" +
      "        i_acc_volume AS accVol\n" +
      "    FROM intraday_by_1min ibm \n" +
      "    WHERE t_ticker = :p_ticker\n" +
      "    AND i_seq_time >= :p_from_time\n" +
      "    AND i_seq_time < :p_to_time\n" +
      "    ORDER BY i_seq_time DESC \n" +
      "    LIMIT 1\n" +
      ") accVol ON 1 = 1\n" +
      "\n" +
      "LEFT JOIN\n" +
      "(\n" +
      "    SELECT \n" +
      "        acc_trade AS netForeignVol\n" +
      "    FROM foreigntrade_by_1min fbm \n" +
      "    WHERE ticker = :p_ticker\n" +
      "    AND seq_time >= :p_from_time \n" +
      "    AND seq_time < :p_to_time\t\n" +
      "    ORDER BY seq_time DESC\t\t\n" +
      "    LIMIT 1\n" +
      ") netForeign ON 1 = 1\n" +
      "\n" +
      "\n" +
      "LEFT JOIN\n" +
      "(\n" +
      "    SELECT \n" +
      "        total_bu_vol_acc / (total_bu_vol_acc + total_sd_vol_acc) AS buPct\n" +
      "    FROM buysellactive_acc_by_1min babm \n" +
      "    WHERE ticker = :p_ticker\n" +
      "    AND seq_time >= :p_from_time \n" +
      "    AND seq_time < :p_to_time\t\n" +
      "    ORDER BY seq_time DESC\t\t\n" +
      "    LIMIT 1\n" +
      ") bsa ON 1 = 1\n" +
      "\n" +
      "LEFT JOIN \n" +
      "(\n" +
      "    SELECT shark_bu_acc AS sharkBuAcc,\n" +
      "        shark_sd_acc AS sharkSdAcc,\n" +
      "        shark_bu_acc / (shark_bu_acc + wolf_bu_acc + sheep_bu_acc) AS sharkBuAccPct,\n" +
      "        shark_sd_acc / (shark_sd_acc + wolf_sd_acc + sheep_sd_acc) AS sharkSdAccPct\n" +
      "    FROM investor_classifier_by_1min icbm \n" +
      "    WHERE ticker  = :p_ticker\n" +
      "    AND seq_time >= :p_from_time \n" +
      "    AND seq_time < :p_to_time\n" +
      "    ORDER BY seq_time DESC\n" +
      "    LIMIT 1\n" +
      ") sharkBsa ON 1 = 1\n" +
      "\n" +
      "LEFT JOIN \n" +
      "(\n" +
      "    SELECT\n" +
      "        rsi rsi ,\n" +
      "        macd / 1000 macd \n" +
      "    FROM indicators_by_1min ibm \n" +
      "    WHERE ticker = :p_ticker\n" +
      "    ORDER BY seq_time DESC\t\t\n" +
      "    LIMIT 1\n" +
      ") ta ON 1 = 1\n" +
      ";");

    try {
      List<HashMap<String, Object>> lst = Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setParameter("p_from_time", fromTime)
        .setParameter("p_to_time", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return lst;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      Stockmarket.stockMarketConnection.closeSession();
    }
    return new ArrayList<>();
  }
}
