package com.tcbs.automation.stockmarket.future;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HedgingArbitrageEntity {

  @Step("Get Data from db")
  public static List<HashMap<String, Object>> getData(){

    StringBuilder query = new StringBuilder();
    query.append("SELECT d.close_price as VN30F1M , t.ticker as TICKER ");
    query.append("    FROM future_by_1D d ");
    query.append("    cross join (");
    query.append("        select min(f.ticker) as ticker ");
    query.append("        from ( ");
    query.append("            select ticker , maturity_date ");
    query.append("            FROM future_by_1min ");
    query.append("            where maturity_date >= date(now()) ");
    query.append("            and ticker like 'VN30F%' ");
    query.append("        group by ticker , maturity_date ");
    query.append("    ) f ");
    query.append(") t ");
    query.append("WHERE d.future_name = 'VN30F1M'");
    query.append("order by d.seq_time desc limit 1 offset 0");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get beta 30F")
  public static List<HashMap<String, Object>> getBeta30F(){
    StringBuilder query = new StringBuilder();
    query.append(" SELECT * FROM Prc_Stock_CAPM_VN30 WHERE Ticker = 'E1VFVN30' and DateReport = (select max(DateReport) from Prc_Stock_CAPM_VN30) ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
