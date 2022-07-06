package com.tcbs.automation.iris.impactdaily;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTickerDividendEntity {
  @Step("Get ticker change")
  public static List<HashMap<String, Object>> getTickerDividend(String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select ISNULL(a.OrganCode, c.Ticker) as ticker,  ");
    queryBuilder.append(" ISNULL(b.CashExerciseRate*10000, 0) as CashExerciseRate,  ");
    queryBuilder.append(" ISNULL(a.DivExerciseRatio, 0) as DivExerciseRatio,  ");
    queryBuilder.append(" ISNULL(a.RightsExerciseRatio, 0) as RightExerciseRatio,  ");
    queryBuilder.append(" ISNULL(a.IssuePrice, 0) as IssuePrice,  ");
    queryBuilder.append(" ISNULL(a.BonusExerciseRatio, 0) as BonusExerciseRatio,  ");
    queryBuilder.append(" ISNULL(a.ExrightDate, b.ExrightDate) as ExrightDate, c.comGroupCode as exchange  ");
    queryBuilder.append(" from  ");
    queryBuilder.append(
      " (select OrganCode, ExrightDate, IssuePrice, sum(BonusExerciseRatio) BonusExerciseRatio,sum(RightsExerciseRatio) RightsExerciseRatio, sum(DIVExerciseRatio) DIVExerciseRatio  ");
    queryBuilder.append(" from (  ");
    queryBuilder.append(" SELECT OrganCode,  ");
    queryBuilder.append(" ExrightDate,  ");
    queryBuilder.append(" IssuePrice,  ");
    queryBuilder.append(" case when IssueMethodCode = 'Bonus' then ExerciseRatio end  BonusExerciseRatio,  ");
    queryBuilder.append(" case when IssueMethodCode = 'Rights' then ExerciseRatio end RightsExerciseRatio,  ");
    queryBuilder.append(" case when IssueMethodCode = 'DIV' then ExerciseRatio end as DIVExerciseRatio  ");
    queryBuilder.append(" FROM stx_cpa_ShareIssue  ");
    queryBuilder.append(" WHERE STATUS = 1  ");
    queryBuilder.append(" AND IssueMethodCode in ('Bonus', 'Rights', 'DIV')  ");
    queryBuilder.append(" AND ExrightDate >= :fromDate  ");
    queryBuilder.append(" AND ExrightDate <= :toDate  ");
    queryBuilder.append(" AND ExerciseRatio IS NOT NULL  ");
    queryBuilder.append(" and len(OrganCode) = 3) as temp  ");
    queryBuilder.append("   ");
    queryBuilder.append(" group by  OrganCode, ExrightDate, IssuePrice) as a  ");
    queryBuilder.append("   ");
    queryBuilder.append(" full outer join (  ");
    queryBuilder.append(" SELECT OrganCode TICKER ,ExrightDate , ExerciseRate  cashExerciseRate  ");
    queryBuilder.append(" FROM stx_cpa_CashDividendPayout  ");
    queryBuilder.append(" WHERE STATUS = 1  ");
    queryBuilder.append(" AND ExrightDate >= :fromDate  ");
    queryBuilder.append(" AND ExrightDate <= :toDate  ");
    queryBuilder.append(" AND ExerciseRate IS NOT NULL  ");
    queryBuilder.append("   ");
    queryBuilder.append(" ) as b on a.OrganCode = b.TICKER and a.ExrightDate = b.ExrightDate  ");
    queryBuilder.append(" LEFT JOIN (SELECT OrganCode CodeCompany, Ticker, comGroupCode FROM stx_cpf_Organization) c  ");
    queryBuilder.append(" ON b.TICKER = c.CodeCompany  ");
    try {

      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get close price")
  public static List<HashMap<String, Object>> getClosePrice(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT CONVERT_TZ(from_unixtime(i_seq_time), '+00:00', '+07:00') as time,t_ticker ticker, f_close_price FROM intraday_by_1min  ");
    queryBuilder.append(" WHERE t_ticker  = :ticker  ");
    queryBuilder.append(" and i_seq_time = (select max(i_seq_time) from intraday_by_1min where t_ticker = :ticker)  ");
    try {

      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}