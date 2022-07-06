package com.tcbs.automation.stoxplus.future;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PrcStockCapmVn30FEntity {
  @Step
  public static List<HashMap<String, Object>> getBetaData(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    if (ticker.get(0).equalsIgnoreCase("")) {
      queryBuilder.append(" SELECT RTRIM(C.Ticker) ticker, Beta, Beta30F ");
      queryBuilder.append(" FROM stx_cpf_Organization C ");
      queryBuilder.append(" LEFT JOIN Prc_Stock_CAPM_VN30 F ");
      queryBuilder.append(" ON C.TICKER = F.TICKER ");
      queryBuilder.append(" AND C.ComGroupCode IN ('HNXIndex','UpcomIndex','VNINDEX') AND :ticker is not null");
      queryBuilder.append(" WHERE F.DateReport = (Select max(DateReport) from Prc_Stock_CAPM_VN30 ); ");
    } else {
      queryBuilder.append("  SELECT RTRIM(Ticker) ticker, Beta, Beta30F ");
      queryBuilder.append("  FROM Prc_Stock_CAPM_VN30 ");
      queryBuilder.append("  WHERE Ticker in :ticker");
      queryBuilder.append("  AND DateReport = (Select max(DateReport) from Prc_Stock_CAPM_VN30 ) ");
    }

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
