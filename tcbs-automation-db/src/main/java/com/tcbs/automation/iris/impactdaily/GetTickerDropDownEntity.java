package com.tcbs.automation.iris.impactdaily;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTickerDropDownEntity {
  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getTickerDropDown(String fromDate, String toDate, Integer rate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" select ");
    queryBuilder.append(" CONVERT_TZ(from_unixtime(i_seq_time), '+00:00', '+07:00') time, ");
    queryBuilder.append(" ticker                                                 , ");
    queryBuilder.append(" f_close_price closePrice, ");
    queryBuilder.append(" f_open_price - f_open_price_change basicPrice ");
    queryBuilder.append(" from intraday_by_1min a, ");
    queryBuilder.append(" (select t_ticker ticker, max(i_seq_time) max ");
    queryBuilder.append(" from intraday_by_1min ");
    queryBuilder.append(" where LENGTH(t_ticker) = 3 AND t_ticker != 'TCB' ");
    queryBuilder.append(" and  CONVERT_TZ(from_unixtime(i_seq_time), '+00:00', '+07:00') >= :fromDate ");
    queryBuilder.append(" and CONVERT_TZ(from_unixtime(i_seq_time), '+00:00', '+07:00') < :toDate ");
    queryBuilder.append(" group by t_ticker) b where a.t_ticker = b.ticker and a.i_seq_time = b.max) t where (closePrice-basicPrice)/basicPrice*100<=(:rate) ");
    queryBuilder.append(" order by ticker ");
    try {

      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("rate", rate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
