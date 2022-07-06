package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.List;
import java.util.Map;

public class StockInfoEntity {
  public static String getExchangeId(String ticker) {
    StringBuilder stringBuilder = new StringBuilder();
//    stringBuilder.append("SELECT id, \n" +
//      "ticker, \n" +
//      "CASE WHEN ExchangeID = 0 THEN 'HOSE'\n" +
//      "WHEN ExchangeID = 1 THEN 'HNX'\n" +
//      "WHEN ExchangeID = 2 THEN 'OTC'\n" +
//      "WHEN ExchangeID = 3 THEN 'UpCom'\n" +
//      "ELSE 'HALT'\n" +
//      "END AS ExchangeID\n" +
//      "FROM stox_tb_Company\n" +
//      "WHERE ticker = :ticker\n");

    stringBuilder.append("SELECT Ticker, \n" +
      "CASE WHEN ComGroupCode = 'VNINDEX' THEN 'HOSE'\n" +
      "WHEN ComGroupCode = 'HNXIndex' THEN 'HNX'\n" +
      "WHEN ComGroupCode = 'OTC' THEN 'OTC'\n" +
      "WHEN ComGroupCode = 'UpcomIndex' THEN 'UpCom'\n" +
      "ELSE 'HALT'\n" +
      "END AS ExchangeID\n" +
      "FROM stx_cpf_Organization\n" +
      "WHERE Ticker = :ticker\n");

    try {
      List<Map<String, Object>> lm = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      String exchangeId = "";
      if (CollectionUtils.isNotEmpty(lm)) {
        exchangeId = (String) lm.get(0).get("ExchangeID");
      }
      return exchangeId;
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      TcAnalysis.tcaDbConnection.closeSession();
    }

    return "";
  }
}
