package com.tcbs.automation.projection.tcanalysis.evaluation;

import com.tcbs.automation.projection.Projection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseDouble;
import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseInt;


@Getter
@RequiredArgsConstructor
@Setter
public class CashFlow {
  private Integer year;

  private Double freeCashFlow;

  @Step
  public static List<CashFlow> getByTicker(String ticker) {
    String indexQuerySql = "select t.ticker, year + 1 +  ASCII(t.YearCode) - ASCII('I') as year, t.YearCode,"
      + " max(case when t.ItemCode = '56' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as freeCashFlow"
      + " from (select *, cast(substring(cid,2,3) as int) as ItemCode, substring(cid, 1, 1) as YearCode, "
      + " row_number() over ( partition by upper(ticker), cid order by last_date desc, update_no desc, year desc ) as rn "
      + " from db_owner.stox_tb_financials"
      + " where ticker = :ticker and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m')"
      + " and year = (select max(year) from  db_owner.stox_tb_financials where ticker = :ticker)) t "
      + " where t.rn = 1 and len(ticker) < 4"
      + " group by t.YearCode, t.year, t.ticker"
      + " order by t.YearCode";
    try {
      List<Map<String, Object>> resultList = Projection.projectionDbConnection.getSession().createNativeQuery(indexQuerySql)
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList.size() == 0) {
        return new ArrayList<>();
      }
      List<CashFlow> cashFlows = new ArrayList<>();
      for (Map<String, Object> result : resultList) {
        CashFlow cf = new CashFlow();
        cf.setYear(parseInt(result.get("year")));
        cf.setFreeCashFlow(parseDouble(result.get("freeCashFlow")));
        cashFlows.add(cf);
      }

      return cashFlows;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    Projection.projectionDbConnection.closeSession();

    return new ArrayList<>();
  }
}
