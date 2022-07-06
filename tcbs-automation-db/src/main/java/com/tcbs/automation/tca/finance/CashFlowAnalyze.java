package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CashFlowAnalyze {

  private String ticker;

  private Long quarter;

  private Long year;

  private Double endLiquid = 0.0;

  private Double initLiquid = 0.0;

  private Double deltaLiquid = 0.0;

  private Double deltaLiquidDebt = 0.0;

  private Double deltaOtherLiquid = 0.0;

  private Double receivable = 0.0;

  private Double deltaReceivable = 0.0;

  private Double inventory = 0.0;

  private Double deltaInventory = 0.0;

  private Double payable = 0.0;

  private Double deltaPayble = 0.0;

  private Double endDebt = 0.0;

  private Double initDebt = 0.0;

  private Double deltaDebt = 0.0;

  private Double capex = 0.0;

  private Double capexDebt = 0.0;

  private Double dividend = 0.0;

  private Double ebitda = 0.0;

  private Double tax = 0.0;

  private Double preTax = 0.0;

  private Double initCash = 0.0;

  private Double endCash = 0.0;

  private Double interestExpense = 0.0;

  private Double shortReceivable = 0.0;

  private Double deltaShortReceivable = 0.0;

  private Double financialInvest = 0.0;

  private Double raiseCapital = 0.0;

  private Double dividendPayment = 0.0;

  private Double longAndShortDebt = 0.0;

  private Double payBack = 0.0;

  private Double depreciation = 0.0;

  @Step
  public static List<Map<String, Object>> getByTickerAndPeriod(String ticker, Integer period, Integer quantity) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      if (period == 1) {
        queryBuilder.append(" select * from (SELECT CB.Nam, CB.Quy, CB.Ticker,");
        queryBuilder.append(" CB.shortReceivable + CB.otherLiquid + CB.inventory ");
        queryBuilder.append(" - CB.shortLiability + CB.shortDebt + CB.shortUnEarn as endLiquid,");
        queryBuilder.append(" CB.shortReceivable + CB.otherLiquid as receivable,");
        queryBuilder.append(" CB.inventory,");
        queryBuilder.append(" CB.shortLiability - CB.shortDebt - CB.shortUnEarn as payable,");
        queryBuilder.append(" CB.shortDebt + CB.longDebt + CB.bond + - CB.cash as endDebt,");
        queryBuilder.append(" CC.capex as capex,");
        queryBuilder.append(" - CC.dividend as dividend,");
        queryBuilder.append(" - CI.preTax + CI.interestExpense - CC.depreciation as ebitda,");
        queryBuilder.append(" - CC.tax as tax,");
        queryBuilder.append(" CI.preTax, ");
        queryBuilder.append(" CC.depreciation,");
        queryBuilder.append(" CB.shortReceivable,");
        queryBuilder.append(" CC.financialInvest,");
        queryBuilder.append(" CI.interestExpense,");
        queryBuilder.append(" CC.raiseCapital,");
        queryBuilder.append(" CC.dividendPayment,");
        queryBuilder.append(" CC.longAndShortDebt,");
        queryBuilder.append(" CC.payBack,");
        queryBuilder.append(" CC.initCash,");
        queryBuilder.append(" CC.endCash");
        queryBuilder.append(" FROM ( ");
        queryBuilder.append(" 	SELECT TOP(1) Ticker,	");         
        queryBuilder.append("			    cast(BSA2 as float)  as[cash], 	");
        queryBuilder.append("	       cast(BSA18 as float) as [otherLiquid],	");
        queryBuilder.append("	       cast(BSA56 as float)  as [shortDebt], 	");
        queryBuilder.append("	       cast(BSA71 as float) as [longDebt], 	");
        queryBuilder.append("	       cast(BSA173 as float) as [bond],	");
        queryBuilder.append("	       cast(BSA55 as float) as [shortLiability],	"); 
        queryBuilder.append("	       cast(BSA8 as float) as [shortReceivable],	");
        queryBuilder.append("	       cast(BSA15 as float) as [inventory], 	");
        queryBuilder.append("	       cast(BSA167 as float) as [shortUnEarn],	");
        queryBuilder.append(" 			YearReport as [Nam], LengthReport  as [Quy], ");
        queryBuilder.append(" 			row_number() over (partition by Ticker,  YearReport ,");
        queryBuilder.append(" 			LengthReport order by UpdateDate desc) as rn	");
        queryBuilder.append(" 	FROM	STX_FSC_BALANCESHEET");
        queryBuilder.append(" 	WHERE	ticker = :ticker and YearReport = year(getDate()) and LengthReport < 5");
        queryBuilder.append(" 	ORDER BY  YearReport desc, LengthReport desc");
        queryBuilder.append(" )  CB  ");
        queryBuilder.append(" LEFT JOIN ");
        queryBuilder.append(" (SELECT sum(preTax) as [preTax], sum(interestExpense) as [interestExpense]");
        queryBuilder.append(" FROM (select *, ");
        queryBuilder.append(" 	row_number() over (partition by Ticker order by  YearReport desc ,LengthReport  desc) as rn2");
        queryBuilder.append(" 		FROM ");
        queryBuilder.append(" 		(");
        queryBuilder.append(" 			SELECT cast(ISA16 as float) as [preTax], Ticker,");
        queryBuilder.append(" 				YearReport , LengthReport  , cast(ISA8 as float) as [interestExpense], ");
        queryBuilder.append(" 				row_number() over (partition by Ticker,  YearReport ,");
        queryBuilder.append(" 				LengthReport order by UpdateDate desc) as rn");
        queryBuilder.append(" 			FROM	STX_FSC_INCOMESTATEMENT ");
        queryBuilder.append(" 			WHERE	ticker = :ticker  ");
        queryBuilder.append(" 		) CI ");
        queryBuilder.append(" 		WHERE CI.rn = 1 and YearReport = year(getDate()) and LengthReport < 5");
        queryBuilder.append(" )CI WHERE	CI.rn2 <= 4)  CI  ON  1 = 1 ");
        queryBuilder.append(" LEFT JOIN");
        queryBuilder.append(" (SELECT sum(capex) as [capex],  sum(dividend) as [dividend],  sum(dividendPayment) as [dividendPayment], ");
        queryBuilder.append(" sum( case when LengthReport = 1 then initCash else 0 end) as [initCash],  sum(financialInvest) as [financialInvest], ");
        queryBuilder.append("  sum(depreciation) as [depreciation],sum(tax) as [tax], ");
        queryBuilder.append(" sum(raiseCapital) as [raiseCapital], sum(longAndShortDebt) as [longAndShortDebt], sum(payBack) as [payBack], ");
        queryBuilder.append(" sum( case when LengthReport = Quy then endCash else 0 end) as [endCash]");
        queryBuilder.append(" 	FROM ");
        queryBuilder.append(" 	(");
        queryBuilder.append(" 		SELECT cast(CFA19  + CFA20 as float)  as [capex],  ");
        queryBuilder.append("         cast(CFA32  + CFA28 as float) as [dividend], ");
        queryBuilder.append("         cast(CFA15 as float) as [tax],  ");
        queryBuilder.append("         cast(CFA32 as float) as [dividendPayment],  ");
        queryBuilder.append("         cast(CFA36 as float) as [initCash], ");
        queryBuilder.append("         cast(CFA21  + CFA22 + CFA23 + CFA24 + CFA25 as float) as [financialInvest], ");
        queryBuilder.append("         cast(CFA2 as float) as [depreciation], ");
        queryBuilder.append("         cast(CFA27 + CFA28 as float) as [raiseCapital],  ");
        queryBuilder.append("         cast(CFA29 as float) as [longAndShortDebt], ");
        queryBuilder.append("         cast(CFA30 + CFA31 as float) as [payBack],  ");
        queryBuilder.append("         cast(CFA38 as float) as [endCash],");
        queryBuilder.append(" 			(select max(LengthReport) from STX_FSC_CASHFLOW ");
        queryBuilder.append(" WHERE	ticker = :ticker and YearReport = year(getDate())  and LengthReport < 5)	as Quy ,");
        queryBuilder.append(" 			Ticker, YearReport , LengthReport ,");
        queryBuilder.append(" 			row_number() over (partition by Ticker,  YearReport ,	");
        queryBuilder.append(" 			LengthReport order by UpdateDate desc) as rn");
        queryBuilder.append(" 		FROM	STX_FSC_CASHFLOW");
        queryBuilder.append(" 		WHERE	ticker = :ticker	and YearReport = year(getDate()) and LengthReport < 5 ");
        queryBuilder.append(" 	) CC WHERE CC.rn = 1  ");
        queryBuilder.append(" ) CC  ON  1 = 1) YTD   ");
        queryBuilder.append("      UNION ALL	");
        queryBuilder.append("select * from (SELECT CB.YearReport as [Nam], CB.LengthReport as [Quy], CB.Ticker,	");
        queryBuilder.append("		CB.shortReceivable + CB.otherLiquid + CB.inventory 	");
        queryBuilder.append("		- CB.shortLiability + CB.shortDebt + CB.shortUnEarn as endLiquid,	");
        queryBuilder.append("		CB.shortReceivable + CB.otherLiquid as receivable,	");
        queryBuilder.append("		CB.inventory,	");
        queryBuilder.append("		CB.shortLiability - CB.shortDebt - CB.shortUnEarn as payable,	");
        queryBuilder.append("		CB.shortDebt + CB.longDebt + CB.bond + - CB.cash as endDebt,	");
        queryBuilder.append("		CC.capex as capex,	");
        queryBuilder.append("		- CC.dividend as dividend,	");
        queryBuilder.append("		- CI.preTax + CI.interestExpense - CC.depreciation as ebitda,	");
        queryBuilder.append("		- CC.tax as tax,	");
        queryBuilder.append("		CI.preTax, 	");
        queryBuilder.append("		CC.depreciation,	");
        queryBuilder.append("		CB.shortReceivable,	");
        queryBuilder.append("		CC.financialInvest,	");
        queryBuilder.append("		CI.interestExpense,	");
        queryBuilder.append("		CC.raiseCapital,	");
        queryBuilder.append("		CC.dividendPayment,	");
        queryBuilder.append("		CC.longAndShortDebt,	");
        queryBuilder.append("		CC.payback,	");
        queryBuilder.append("		CC.initCash,	");
        queryBuilder.append("		CC.endCash	");
        queryBuilder.append("	FROM	");
        queryBuilder.append("	(	");
        queryBuilder.append("		SELECT *	");
        queryBuilder.append("		FROM 	");
        queryBuilder.append("		(	");
        queryBuilder.append("			SELECT Ticker,	");
        queryBuilder.append("			     cast(BSA2 as float)  as[cash], 	");
        queryBuilder.append("	        cast(BSA18 as float) as [otherLiquid],	");
        queryBuilder.append("	        cast(BSA56 as float)  as [shortDebt], 	");
        queryBuilder.append("	        cast(BSA71 as float) as [longDebt], 	");
        queryBuilder.append("	        cast(BSA173 as float) as [bond],	");
        queryBuilder.append("	        cast(BSA55 as float) as [shortLiability],	"); 
        queryBuilder.append("	        cast(BSA8 as float) as [shortReceivable],	");
        queryBuilder.append("	        cast(BSA15 as float) as [inventory], 	");
        queryBuilder.append("	        cast(BSA167 as float) as [shortUnEarn],	");
        queryBuilder.append("					YearReport , LengthReport  , 	");
        queryBuilder.append("					row_number() over (partition by Ticker,  YearReport ,	");
        queryBuilder.append("					LengthReport order by UpdateDate desc) as rn		");
        queryBuilder.append("			FROM	STX_FSC_BALANCESHEET	");
        queryBuilder.append("			WHERE	ticker = :ticker 	");
        queryBuilder.append("						");
        queryBuilder.append("		) CB 	");
        queryBuilder.append("	WHERE rn = 1 and YearReport >= :endYear and YearReport < year(getDate()) and LengthReport = 5	");
        queryBuilder.append("	) CB  	");
        queryBuilder.append("	LEFT JOIN	");
        queryBuilder.append("	(	");
        queryBuilder.append("		SELECT * 	");
        queryBuilder.append("		FROM 	");
        queryBuilder.append("		(	");
        queryBuilder.append("			SELECT cast(ISA16 as float) as [preTax],	");
        queryBuilder.append("				YearReport , LengthReport  , cast(ISA8 as float) as [interestExpense], 	");
        queryBuilder.append("				row_number() over (partition by Ticker,  YearReport ,	");
        queryBuilder.append("				LengthReport order by UpdateDate desc) as rn	");
        queryBuilder.append("			FROM	STX_FSC_INCOMESTATEMENT 	");
        queryBuilder.append("			WHERE	ticker = :ticker 	");
        queryBuilder.append("						");
        queryBuilder.append("		) CI 	");
        queryBuilder.append("		WHERE rn = 1 and YearReport >= :endYear and YearReport < year(getDate()) and LengthReport = 5	");
        queryBuilder.append("	) CI  	");
        queryBuilder.append("	ON CB.YearReport = CI.YearReport and CB.LengthReport = CI.LengthReport	");
        queryBuilder.append("		");
        queryBuilder.append("	LEFT JOIN	");
        queryBuilder.append("	(	");
        queryBuilder.append("		SELECT * 	");
        queryBuilder.append("		FROM 	");
        queryBuilder.append("		(	");
        queryBuilder.append(" 		SELECT cast(CFA19  + CFA20 as float)  as [capex],  ");
        queryBuilder.append("         cast(CFA32  + CFA28 as float) as [dividend], ");
        queryBuilder.append("         cast(CFA15 as float) as [tax],  ");
        queryBuilder.append("         cast(CFA32 as float) as [dividendPayment],  ");
        queryBuilder.append("         cast(CFA36 as float) as [initCash], ");
        queryBuilder.append("         cast(CFA21  + CFA22 + CFA23 + CFA24 + CFA25 as float) as [financialInvest], ");
        queryBuilder.append("         cast(CFA2 as float) as [depreciation], ");
        queryBuilder.append("         cast(CFA27 + CFA28 as float) as [raiseCapital],  ");
        queryBuilder.append("         cast(CFA29 as float) as [longAndShortDebt], ");
        queryBuilder.append("         cast(CFA30 + CFA31 as float) as [payBack],  ");
        queryBuilder.append("         cast(CFA38 as float) as [endCash], YearReport, LengthReport ,");
        queryBuilder.append("				row_number() over (partition by Ticker,  YearReport ,		");
        queryBuilder.append("				LengthReport order by UpdateDate desc) as rn	");
        queryBuilder.append("			FROM	STX_FSC_CASHFLOW	");
        queryBuilder.append("			WHERE	ticker = :ticker	");
        queryBuilder.append("						");
        queryBuilder.append("		) CC 	");
        queryBuilder.append("		WHERE rn = 1 and YearReport >= :endYear and YearReport <  year(getDate()) and LengthReport = 5	");
        queryBuilder.append("	) CC  	");
        queryBuilder.append("	ON CB.YearReport = CC.YearReport and CB.LengthReport = CC.LengthReport	");
        queryBuilder.append("	) YY order by Nam desc, Quy desc	");
        return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("ticker", ticker)
          .setParameter("endYear", LocalDateTime.now().getYear() - quantity)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}