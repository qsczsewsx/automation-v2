package com.tcbs.automation.tca.entity;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllStatisticsEntity {

  private static final String TICKER = "ticker";
  @Step("get data")
  public static List<HashMap<String, Object>> getListTicker(String tradingDate, Integer pageNumber, Integer pageSize) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select (c.ticker)     ticker, ")
      .append(" CASE ")
      .append(" WHEN c.comGroupCode = 'HNXIndex' THEN CAST(1 AS SMALLINT) ")
      .append(" WHEN c.comGroupCode = 'UpcomIndex' THEN CAST(3  AS SMALLINT) ")
      .append(" WHEN c.comGroupCode = 'VNINDEX' THEN CAST(0  AS SMALLINT) ")
      .append(" ELSE NULL ")
      .append(" END AS exchange_id ")
      .append(" from stx_cpf_Organization c ")
      .append(" where c.comGroupCode IN ('HNXIndex','UpcomIndex','VNINDEX') ")
      .append(" union ")
      .append(" select (m.ticker) ticker,  null as exchange_id ")
      .append(" from Smy_dwh_stox_MarketPrices m ")
      .append(" where CONVERT(varchar, m.tradingDate, 20) = :tradingDate and len(ticker) = 8 and ticker like 'C%' ")
      .append(" order by ticker ")
      .append(" offset :pageNumber rows fetch next :pageSize rows only ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tradingDate", tradingDate.concat(" 00:00:00"))
        .setParameter("pageNumber", pageNumber)
        .setParameter("pageSize", pageSize)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get latest report")
  public static List<HashMap<String, Object>> getLatestReportQuarter(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT Ticker ")
      .append(" FROM stx_fsc_BalanceSheet sfbs ")
      .append(" WHERE YearReport * 10 + LengthReport = (SELECT MAX(YearReport * 10 + LengthReport) FROM stx_fsc_BalanceSheet WHERE LengthReport < 5 AND Status = 1) ")
      .append(" and ticker in :ticker ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get net profit after mi")
  public static List<HashMap<String, Object>> getNetProfitAfterMi(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT ticker, net_profit_after_mi AS isa22, cash_adjusted_on_marcap AS cash_on_marcap, cash_adjusted_on_asset  AS cash_on_asset ")
      .append(" FROM tcdata_ratio_bank t1 ")
      .append(" WHERE (0 = (SELECT COUNT(1) FROM tcdata_ratio_bank t2 WHERE t1.ticker = t2.ticker ")
      .append(" AND ( t2.yearReport > t1.yearReport OR (t2.yearReport = t1.yearReport AND t2.lengthReport > t1.lengthReport)))) AND ticker in :ticker ")
      .append(" UNION ")
      .append(" SELECT ticker, net_profit_after_mi AS isa22, cash_adjusted_on_marcap AS cash_on_marcap, cash_adjusted_on_asset  AS cash_on_asset ")
      .append(" FROM tcdata_ratio_company t1 ")
      .append(" WHERE (0 = (SELECT COUNT(1) FROM tcdata_ratio_company t2 WHERE t1.ticker = t2.ticker ")
      .append(" AND (t2.yearReport > t1.yearReport OR (t2.yearReport = t1.yearReport AND t2.lengthReport > t1.lengthReport)))) AND ticker in :ticker ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data grown profit")
  public static List<HashMap<String, Object>> getDataProfitGrown(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE  ")
      .append("  @tickER dwl_tickerTable;   ")
      .append("INSERT   ")
      .append("	INTO  ")
      .append("	@TICKER(TICKER)   ")
      .append("SELECT	tICKER FROM stx_cpf_Organization  ")
      .append("WHERE	stx_cpf_Organization.Ticker IN :ticker ;  ")
      .append("DECLARE @maxIncomeStatementQuarter INT;  ")
      .append("SET  ")
      .append("@maxIncomeStatementQuarter = (   ")
      .append("SELECT   ")
      .append("	MAX(YearReport * 10 + LengthReport)   ")
      .append("FROM   ")
      .append("	stx_fsc_IncomeStatement   ")
      .append("WHERE  ")
      .append("	LengthReport < 5);  ")
      .append("SELECT   ")
      .append("	Ticker , ")
      .append("	quarter,   ")
      .append("	(CASE 	WHEN GRW.quarter = @maxIncomeStatementQuarter   ")
      .append("  THEN GRW.rev_growth_lastestquarter   ")
      .append("		ELSE NULL END) AS rev_growth_lastestquarter ,   ")
      .append("	(CASE WHEN GRW.quarter = @maxIncomeStatementQuarter ")
      .append("  THEN GRW.rev_growth_2nd_lastestquarter   ")
      .append("		ELSE NULL END) AS rev_growth_2nd_lastestquarter ,   ")
      .append("	(CASE WHEN GRW.quarter = @maxIncomeStatementQuarter  ")
      .append("  THEN GRW.qEps_growth_lastestquarter ELSE NULL   ")
      .append("	END) AS qEps_growth_lastestquarter ,   ")
      .append("	(CASE	WHEN GRW.quarter = @maxIncomeStatementQuarter   ")
      .append("  THEN GRW.qEps_growth_2nd_lastestquarter  ")
      .append("		ELSE NULL END) AS qEps_growth_2nd_lastestquarter ,   ")
      .append("	(CASE WHEN GRW.quarter = @maxIncomeStatementQuarter   ")
      .append("  THEN GRW.profit_growth_lastestquarter  ")
      .append("		ELSE NULL END) AS profit_growth_lastestquarter,   ")
      .append("	(CASE 	WHEN GRW.quarter = @maxIncomeStatementQuarter   ")
      .append("  THEN GRW.profit_growth_2nd_lastestquarter 	ELSE NULL   ")
      .append("	END) AS profit_growth_2nd_lastestquarter  ")
      .append("FROM   ")
      .append("	(   ")
      .append("	SELECT  ")
      .append("		Ticker,   ")
      .append("		MAX(quarter) AS quarter,  ")
      .append("		MAX(CASE WHEN RN = 1 THEN rev_growth_lastestquarter END) AS rev_growth_lastestquarter,  ")
      .append("		MAX(CASE WHEN RN = 2 THEN rev_growth_lastestquarter END) AS rev_growth_2nd_lastestquarter,  ")
      .append("		MAX(CASE WHEN RN = 1 THEN qEps_growth_lastestquarter END) AS qEps_growth_lastestquarter,  ")
      .append("		MAX(CASE WHEN RN = 2 THEN qEps_growth_lastestquarter END) AS qEps_growth_2nd_lastestquarter,")
      .append("		MAX(CASE WHEN RN = 1 THEN profit_growth_lastestquarter END) AS profit_growth_lastestquarter,")
      .append("		MAX(CASE WHEN RN = 2 THEN profit_growth_lastestquarter END) AS profit_growth_2nd_lastestquarter ")
      .append("	FROM  ")
      .append("		(   ")
      .append("		SELECT  ")
      .append("			ticker,   ")
      .append("			quarter  ,   ")
      .append("			CASE WHEN (net_sales >= 0) THEN (net_sales - NULLIF(Prev1yNetSales,0)) / ABS(NULLIF(Prev1yNetSales, 0))   ")
      .append("				ELSE NULL END AS [rev_growth_lastestquarter] ,   ")
      .append("			CASE WHEN (Prev1yQuarterBasicEps >= 0)   ")
      .append("   THEN (quarter_basic_eps - NULLIF(Prev1yQuarterBasicEps,0)) / ABS(NULLIF(Prev1yQuarterBasicEps, 0))  ")
      .append("				ELSE NULL END AS [qEps_growth_lastestquarter] ,   ")
      .append("			CASE 	WHEN (net_profit_after_mi >= 0)   ")
      .append("   THEN (net_profit_after_mi - NULLIF(Prev1yQuarterProfit, 0)) / ABS(NULLIF(Prev1yQuarterProfit, 0))  ")
      .append("				ELSE NULL END AS [profit_growth_lastestquarter],   ")
      .append("			RN  ")
      .append("		FROM  ")
      .append("			(   ")
      .append("			SELECT  ")
      .append("				Ticker,   ")
      .append("				quarter,  ")
      .append("				net_sales,  ")
      .append("				LAG(net_sales, 4, 	NULL)   ")
      .append("   OVER (PARTITION BY ticker ORDER BY quarter) AS Prev1yNetSales ,  ")
      .append("				quarter_basic_eps,  ")
      .append("				LAG(quarter_basic_eps,	4, 	NULL)   ")
      .append("   OVER (PARTITION BY ticker ORDER BY quarter) AS Prev1yQuarterBasicEps  ,  ")
      .append("				net_profit_after_mi,  ")
      .append("				LAG(net_profit_after_mi, 4,  NULL)   ")
      .append("   OVER (PARTITION BY ticker ORDER BY quarter) AS Prev1yQuarterProfit,  ")
      .append("				ROW_NUMBER() OVER (PARTITION BY Ticker  ")
      .append("			ORDER BY  ")
      .append("				quarter DESC) AS rn   ")
      .append("			FROM  ")
      .append("				(   ")
      .append("				SELECT  ")
      .append("					AA.Ticker,  ")
      .append("					AA.quarter,   ")
      .append("					net_sales,  ")
      .append("					quarter_basic_eps,  ")
      .append("					net_profit_after_mi   ")
      .append("				FROM  ")
      .append("					(   ")
      .append("					SELECT 	* FROM (   ")
      .append("						SELECT  ")
      .append("							tICKER  ")
      .append("						FROM  ")
      .append("							@tickER) A1   ")
      .append("					CROSS JOIN  ")
      .append("   (   ")
      .append("						SELECT  ")
      .append("							*   ")
      .append("						FROM  ")
      .append("							idata_quarter   ")
      .append("						WHERE   ")
      .append("							quarter <= @maxIncomeStatementQuarter   ")
      .append("							AND length < 5) A2) AA  ")
      .append("				LEFT JOIN (   ")
      .append("					SELECT  ")
      .append("						Ticker,   ")
      .append("						YearReport * 10 + LengthReport AS quarter,  ")
      .append("						net_sales,  ")
      .append("						quarter_basic_eps,  ")
      .append("						net_profit_after_mi   ")
      .append("					FROM  ")
      .append("						tcdata_ratio_company  ")
      .append("					WHERE   ")
      .append("						lengthreport < 5  ")
      .append("						AND YearReport > YEAR(GETDATE()) - 7  ")
      .append("				UNION   ")
      .append("					SELECT  ")
      .append("						Ticker,   ")
      .append("						YearReport * 10 + LengthReport AS quarter,  ")
      .append("						net_sales,  ")
      .append("						quarter_basic_eps,  ")
      .append("						net_profit_after_mi   ")
      .append("					FROM  ")
      .append("						tcdata_ratio_bank   ")
      .append("					WHERE   ")
      .append("						lengthreport < 5  ")
      .append("						AND YearReport > YEAR(GETDATE()) - 7) BB  ")
      .append("   ON  ")
      .append("					AA.ticker = BB.Ticker   ")
      .append("						AND AA.quarter = BB.quarter) S) Q   ")
      .append("		WHERE   ")
      .append("			RN <= 2) TTT  ")
      .append("	GROUP BY  ")
      .append("		Ticker) GRW;  ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get price")
  public static List<HashMap<String, Object>> getPriceInfo(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT Ticker, PriceLowest, PriceHighest FROM tbl_idata_price_summary WHERE Ticker in :ticker ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get sm100")
  public static List<HashMap<String, Object>> getSm100(List<String> ticker, String tradingDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" declare @closestDateReport datetime ")
      .append(" set @closestDateReport = (select max(DateReport) from idata_indicators_daily where DateReport <= :tradingDate ) ")
      .append(" select ticker, i.sma100  ")
      .append(" from idata_indicators_daily i ")
      .append(" where DateReport = @closestDateReport ")
      .append(" and ticker in :ticker ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setParameter("tradingDate", tradingDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get free float rate")
  public static List<HashMap<String, Object>> freeFloatRate(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT O.FreeFloatRate, Ticker FROM stx_cpf_Organization O INNER JOIN view_idata_industry I ON O.IcbCode = I.IdLevel4 WHERE ticker IN :ticker ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter(TICKER, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
