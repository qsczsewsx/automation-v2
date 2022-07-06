package com.tcbs.automation.tca.ticker;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.tca.TcAnalysis;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class TickerRatio {
  final static Logger log = LoggerFactory.getLogger(TickerRatio.class);
  private String ticker;
  @Nullable
  private Double capitalize;
  private Double tradeValuePerDay;
  private Double priceToEarning;
  private Double priceToBook;
  private Double valueBeforeEbitda;
  @NumberFormat
  private Double dividend;
  @NumberFormat
  private Double roe;
  @NumberFormat
  private Double profitGrowthAvarage;
  private Double ageOfReceivable;
  private Double ageOfInventory;
  private Double payableOnEquity;
  private Double payableOnEbitda;
  private Double ebitOnInterest;
  private Double shortOnLongTermPayable;
  @Nullable
  private Double revenue;
  @Nullable
  private Double operationProfit;
  @Nullable
  private Double netProfit;
  private Double earningPerShare;
  @Nullable
  private Double asset;
  @Nullable
  private Double liability;
  @Nullable
  private Double equity;
  @Nullable
  private Double bookValuePerShare;
  @NumberFormat
  private Double profitMargin;
  @NumberFormat
  private Double nonInterestOnToi;
  @NumberFormat
  private Double loanOnDeposit;
  @NumberFormat
  private Double creditGrowth;
  @NumberFormat
  private Double badDebtPercentage;
  @NumberFormat
  private Double provisionOnBadDebt;
  @Nullable
  private Double customerCredit;
  private Double betaIndex;
  private Double price;
  private Double noStock;
  private Double payment;

  @Step
  public static List<Map<String, Object>> getByStockCode(String stockCode) {
    try {
      if (TickerBasic.getTickerBasic(stockCode).getComTypeCode().equals("NH")) {
        return getFromBank(stockCode);
      } else {
        return getFromCompany(stockCode);
      }
    } catch(Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }

  public static List<Map<String, Object>> getFromBank(String stockCode) {
    StringBuilder fromRatio = new StringBuilder();
    fromRatio.append(" WITH mp as (");
    fromRatio.append("  	SELECT TOP(1) ClosePriceAdjusted  AS close_price, TotalMatchVolume AS nm_total_traded_qtty ");
    fromRatio.append("  	FROM  Smy_dwh_stox_MarketPrices ");
    fromRatio.append("  	WHERE Ticker = :ticker ");
    fromRatio.append("  	ORDER BY tradingDate DESC");
    fromRatio.append("  )");
    fromRatio.append(" select RQ.Ticker as ticker,");
    fromRatio.append(" 	mp.nm_total_traded_qtty as tradeValuePerDay,");
    fromRatio.append(" 	DPS.dividendPerShare as dividend,");
    fromRatio.append(" 	BS_Q.Asset as asset,");
    fromRatio.append(" 	BS_Q.Liability as liability,");
    fromRatio.append(" 	BS_Q.Equity as equity,");
    fromRatio.append(" 	IS_Q.OperatingProfit as operationProfit,");
    fromRatio.append(" 	IS_Q.Provision as provision,");
    fromRatio.append(" 	IS_Q.NetIncome as  netProfit,");
    fromRatio.append(" 	IS_Q.Sales as revenue,");
    fromRatio.append(" 	RQ.income5year as profitGrowthAvarage ,");
    fromRatio.append(" 	BS_Q.Credit as customerCredit ,");
    fromRatio.append(" 	RQ.loan_growth as creditGrowth,");
    fromRatio.append(" 	RQ.marcap  as capitalize,");
    fromRatio.append(" 	RQ.pe as priceToEarning,");
    fromRatio.append(" 	RQ.pb as priceToBook,");
    fromRatio.append(" 	RQ.roe as roe,");
    fromRatio.append(" 	RQ.bvps as bookValuePerShare,");
    fromRatio.append(" 	RQ.basic_eps as earningPerShare,");
    fromRatio.append(" 	RQ.NIM as profitMargin,");
    fromRatio.append(" 	RQ.[Pro/NPL] as provisionOnBadDebt,");
    fromRatio.append(" 	RQ.NPL as badDebtPercentage,");
    fromRatio.append(" 	RQ.[Loan/Deposit] as loanOnDeposit,");
    fromRatio.append(" 	RQ.OutInterest as nonInterestOnToi ");
    fromRatio.append("  from mp");
    fromRatio.append("  left join	");
    fromRatio.append("  (");
    fromRatio.append("  		SELECT top(1) cast(BSA2 as float) / 1000000000 as[Cash]");
    fromRatio.append("  			, cast(BSA54 as float) / 1000000000 as [Liability]");
    fromRatio.append("  			, cast(BSA78 as float) / 1000000000 as [Equity]");
    fromRatio.append("  			, cast(BSA53 as float) / 1000000000 as [Asset]");
    fromRatio.append("  			, cast(BSB103 as float) / 1000000000 as [Credit]");
    fromRatio.append("  		FROM	stx_fsc_BalanceSheet ");
    fromRatio.append("  		WHERE	lengthreport < 5 and ticker = :ticker");
    fromRatio.append("  		order by yearreport desc, lengthreport desc , UpdateDate desc ");
    fromRatio.append("  ) BS_Q on 1 = 1");
    fromRatio.append("  left join	");
    fromRatio.append("  (");
    fromRatio.append("  	SELECT	sum(OperatingProfit) / 1000000000 as OperatingProfit, ");
    fromRatio.append("  			sum(NetIncome) / 1000000000 as NetIncome,");
    fromRatio.append("  			sum(Provision) / 1000000000 as Provision, ");
    fromRatio.append("  			sum(Sales) / 1000000000 as Sales ");
    fromRatio.append("  	FROM	");
    fromRatio.append("  	(");
    fromRatio.append("  			SELECT top(4) cast(ISB27 as float) as [Sales]");
    fromRatio.append("  				, cast(ISB38 as float) as [OperatingProfit]");
    fromRatio.append("  				, cast(ISA22 as float) as [NetIncome]");
    fromRatio.append("  				, cast(ISB41 as float) as [Provision]");
    fromRatio.append("  			FROM	stx_fsc_IncomeStatement");
    fromRatio.append("  			WHERE	lengthreport < 5 and  ticker = :ticker");
    fromRatio.append("  			order by yearreport desc, lengthreport desc ,UpdateDate desc");
    fromRatio.append("  	) IS_Q");
    fromRatio.append("  ) IS_Q on 1 = 1 ");
    fromRatio.append("  left join	");
    fromRatio.append("  ( ");
    fromRatio.append("  	SELECT Ticker");
    fromRatio.append("  			,  nim as [NIM]");
    fromRatio.append("  			, 1 - nii_on_toi as [OutInterest]");
    fromRatio.append("  			, ldr as [Loan/Deposit]");
    fromRatio.append("  			, npl_ratio  as [NPL]");
    fromRatio.append("  			, reserve_on_npl as [Pro/NPL]");
    fromRatio.append("  			, loan_growth");
    fromRatio.append("  			, marcap");
    fromRatio.append("  			, pe");
    fromRatio.append("  			, pb");
    fromRatio.append("  			, roe");
    fromRatio.append("  			, bvps");
    fromRatio.append("  			, basic_eps");
    fromRatio.append("  			, income5year");
    fromRatio.append("  	FROM	tca_ratio_latest ");
    fromRatio.append("  	WHERE	ticker = :ticker ");
    fromRatio.append("  ) RQ on 1 = 1");
    fromRatio.append("  left join ");
    fromRatio.append("  (");
    fromRatio.append("  	SELECT  SUM(CAST(ExerciseRate AS FLOAT)) AS dividendPerShare");
    fromRatio.append("  	FROM stx_cpa_CashDividendPayout ");
    fromRatio.append("  	WHERE OrganCode = :ticker");
    fromRatio.append("  	AND DividendYear > YEAR(GETDATE()) - 1");
    fromRatio.append("  ) DPS ON 1 = 1		");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(fromRatio.toString())
        .setParameter("ticker", stockCode).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<Map<String, Object>> getFromCompany(String stockCode) {
    StringBuilder fromRatio = new StringBuilder();
    fromRatio.append(" 		WITH mp as (");
    fromRatio.append(" 	SELECT TOP(1) ClosePriceAdjusted  AS close_price, TotalMatchVolume AS nm_total_traded_qtty ");
    fromRatio.append(" 	FROM  Smy_dwh_stox_MarketPrices ");
    fromRatio.append(" 	WHERE Ticker = :ticker ");
    fromRatio.append(" 	ORDER BY tradingDate DESC");
    fromRatio.append(" )");
    fromRatio.append(" SELECT r.Ticker as ticker,");
    fromRatio.append(" 		mp.nm_total_traded_qtty as tradeValuePerDay,");
    fromRatio.append(" 		DPS.dividendPerShare  as dividend,");
    fromRatio.append(" 		BS_Q.STDebt ");
    fromRatio.append(" 		/ nullif(BS_Q.LTDebt,0) as shortOnLongTermPayable,");
    fromRatio.append(" 		BS_Q.Liability ");
    fromRatio.append(" 		/ nullif(BS_Q.Equity,0) as payableOnEquity,");
    fromRatio.append(" 		r.interest_coverage as ebitOnInterest,");
    fromRatio.append(" 		r.debt_on_ebitda as payableOnEbitda,");
    fromRatio.append(" 		BS_Q.Asset as asset,");
    fromRatio.append(" 		BS_Q.liability,");
    fromRatio.append(" 		BS_Q.equity,");
    fromRatio.append(" 		IS_Q.Sales as revenue,");
    fromRatio.append(" 		IS_Q.OperatingProfit as operationProfit,");
    fromRatio.append(" 		IS_Q.GrossProfit as grossProfit,");
    fromRatio.append(" 		IS_Q.NetIncome as netProfit ,");
    fromRatio.append(" 		r.receivable_days as ageOfReceivable,");
    fromRatio.append(" 		r.inventory_days as ageOfInventory,");
    fromRatio.append(" 		r.bvps bookValuePerShare,");
    fromRatio.append(" 		r.basic_eps earningPerShare,");
    fromRatio.append(" 		r.marcap  as capitalize,");
    fromRatio.append(" 		r.pe as priceToEarning,");
    fromRatio.append(" 		r.pb as priceToBook,");
    fromRatio.append(" 		r.roe as roe,");
    fromRatio.append(" 		r.ev_on_ebitda as valueBeforeEbitda,");
    fromRatio.append(" 		r.income5year as profitGrowthAvarage");
    fromRatio.append(" FROM ");
    fromRatio.append(" (");
    fromRatio.append(" 	SELECT  Ticker");
    fromRatio.append(" 		, receivable_days");
    fromRatio.append(" 		, inventory_days");
    fromRatio.append(" 		, bvps");
    fromRatio.append(" 		, basic_eps");
    fromRatio.append(" 		, marcap");
    fromRatio.append(" 		, pe");
    fromRatio.append(" 		, pb");
    fromRatio.append(" 		, roe ");
    fromRatio.append(" 		, ev_on_ebitda");
    fromRatio.append(" 		, interest_coverage");
    fromRatio.append(" 		, income5year");
    fromRatio.append(" 		, st_on_lt_debt");
    fromRatio.append(" 		, debt_on_ebitda");
    fromRatio.append(" 	FROM	tca_ratio_latest ");
    fromRatio.append(" 	WHERE	ticker = :ticker ");
    fromRatio.append(" ) r ");
    fromRatio.append(" left join	");
    fromRatio.append(" (");
    fromRatio.append(" 	SELECT top(1) cast(BSA2 as float) / 1000000000 as[Cash]");
    fromRatio.append(" 	        , cast(BSA54 as float) / 1000000000 as [Liability]");
    fromRatio.append("             , cast(BSA78 as float) / 1000000000 as [Equity]");
    fromRatio.append("             , cast(BSA53 as float) / 1000000000 as Asset");
    fromRatio.append("             , (cast(BSA56 as float) + cast(BSA71 as float)) / 1000000000 as [Vay]");
    fromRatio.append("             , cast(BSA56 as float) / 1000000000 AS [STDEBT]");
    fromRatio.append("             , cast(BSA71 as float) / 1000000000 AS [LTDEBT]");
    fromRatio.append(" 	FROM	stx_fsc_BalanceSheet ");
    fromRatio.append(" 	WHERE	lengthreport < 5 and ticker = :ticker");
    fromRatio.append(" 	ORDER BY YearReport desc, lengthreport desc, UpdateDate desc");
    fromRatio.append(" ) BS_Q on 1 = 1");
    fromRatio.append(" left join	");
    fromRatio.append(" (");
    fromRatio.append(" 	SELECT	 sum(Sales) / 1000000000 as Sales");
    fromRatio.append(" 			, sum(OperatingProfit) / 1000000000 as OperatingProfit");
    fromRatio.append(" 			, sum(GrossProfit) / 1000000000 as GrossProfit");
    fromRatio.append(" 			, sum(NetIncome) / 1000000000 as NetIncome");
    fromRatio.append(" 			, sum(InterestExpense) / 1000000000 as InterestExpense");
    fromRatio.append(" 			, sum(SellingExpense) / 1000000000 as SellingExpense");
    fromRatio.append(" 			, sum(GAExpense) / 1000000000 as GAExpense");
    fromRatio.append(" 			, sum(preTax) / 1000000000 as preTax");
    fromRatio.append(" 	FROM	");
    fromRatio.append(" 	( SELECT top(4) cast(ISA3 as float) as [Sales]");
    fromRatio.append(" 			, cast(ISA5 as float) + cast(ISA9 as float) + cast(ISA10 as float) as [OperatingProfit]");
    fromRatio.append(" 			, cast(ISA5 as float) as [GrossProfit]");
    fromRatio.append(" 			, cast(ISA22 as float) as [NetIncome]");
    fromRatio.append(" 			, cast(ISA8 as float) as [InterestExpense]");
    fromRatio.append(" 			, cast(ISA9 as float) as [SellingExpense]");
    fromRatio.append(" 			, cast(ISA10 as float) as [GAExpense]");
    fromRatio.append(" 			, cast(ISA16 as float) as [preTax]");
    fromRatio.append(" 			FROM	stx_fsc_IncomeStatement");
    fromRatio.append(" 			WHERE	ticker = :ticker and lengthreport < 5");
    fromRatio.append(" 			ORDER BY YearReport desc, lengthreport desc, UpdateDate desc");
    fromRatio.append(" 	) IS_Q");
    fromRatio.append(" ) IS_Q on 1 = 1 ");
    fromRatio.append(" left join ");
    fromRatio.append(" (");
    fromRatio.append(" 	SELECT  SUM(CAST(ExerciseRate AS FLOAT)) AS dividendPerShare");
    fromRatio.append(" 	FROM stx_cpa_CashDividendPayout ");
    fromRatio.append(" 	WHERE OrganCode = :ticker");
    fromRatio.append(" 	AND DividendYear > YEAR(GETDATE()) - 1");
    fromRatio.append(" ) DPS ON 1 = 1	");
    fromRatio.append(" left join mp on 1 = 1    ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(fromRatio.toString())
        .setParameter("ticker", stockCode).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      System.out.println(ex);
    }
    return new ArrayList<>();
  }

}