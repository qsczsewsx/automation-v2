package com.tcbs.automation.tca.finance;

import com.beust.jcommander.internal.Nullable;
import com.sun.istack.NotNull;
import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class FinanceRatioEntity {
  private static final String TICKER_STR = "ticker";
  private String ticker;
  private Long quarter;
  private Long year;
  private Double capitalize;
  private Double priceToEarning;
  private Double priceToBook;
  private Double valueBeforeEbitda;
  @NumberFormat
  private Double dividend;
  @NumberFormat
  private Double roe;
  @NumberFormat
  private Double roa;
  @Nullable
  private Double daysReceivable;
  @Nullable
  private Double daysInventory;
  @Nullable
  private Double daysPayable;
  private Double ebitOnInterest;
  private Double revenue;
  private Double gaExpense;
  private Double interestExpense;
  @Nullable
  private Double earningPerShare;
  private Double asset;
  private Double liability;
  private Double equity;
  @Nullable
  private Double bookValuePerShare;
  @NumberFormat
  private Double interestMargin;
  @NumberFormat
  private Double nonInterestOnToi;
  private Double operatingProfit;
  private Double postTax;
  private Double preTax;
  @NumberFormat
  private Double badDebtPercentage;
  @NumberFormat
  private Double provisionOnBadDebt;
  private Double deposit;
  private Double earnAsset;
  @NumberFormat
  private Double costOfFinancing;
  private Double equityOnTotalAsset;
  @NumberFormat
  private Double equityOnLoan;
  @NumberFormat
  private Double costToIncome;
  private Double equityOnLiability;
  private Double badDebt;
  private Double preProvision;
  private Double toi;
  private Double provisionExpense;
  private Double provision;
  private Double loan;
  private Double liquidAsset;
  private Double currentPayment;
  private Double quickPayment;
  private Double ebit;
  private Double ebitda;
  private Double publicStock;
  private Double grossProfit;
  private Double cash;
  private Double shortDebt;
  private Double longDebt;
  private Double fixedAsset;
  private Double debt;
  private Double shortLiability;
  private Double capex;
  @NumberFormat
  private Double epsChange;
  @Nullable
  private Double ebitdaOnStock;
  @NumberFormat
  private Double grossProfitMargin;
  @NumberFormat
  private Double operatingProfitMargin;
  @NumberFormat
  private Double postTaxMargin;
  private Double debtOnEquity;
  private Double debtOnAsset;
  private Double debtOnEbitda;
  private Double shortOnLongDebt;
  private Double assetOnEquity;
  @Nullable
  private Double capitalBalance;
  @NumberFormat
  private Double cashOnEquity;
  @NumberFormat
  private Double cashOnCapitalize;
  @Nullable
  private Double cashCirculation;
  private Double workCapital;
  private Double revenueOnWorkCapital;
  @NumberFormat
  private Double capexOnFixedAsset;
  private Double revenueOnAsset;
  private Double postTaxOnPreTax;
  @NumberFormat
  private Double ebitOnRevenue;
  private Double preTaxOnEbit;
  @NumberFormat
  private Double preProvisionOnToi;
  @NumberFormat
  private Double postTaxOnToi;
  @NumberFormat
  private Double loanOnEarnAsset;
  @NumberFormat
  private Double loanOnAsset;
  @NumberFormat
  private Double loanOnDeposit;
  @NumberFormat
  private Double depositOnEarnAsset;
  @NumberFormat
  private Double badDebtOnAsset;
  @NumberFormat
  private Double liquidityOnLiability;
  private Double payableOnEquity;
  @NumberFormat
  private Double cancelDebt;
  @NumberFormat
  private Double ebitdaOnStockChange;
  @NumberFormat
  private Double bookValuePerShareChange;
  @NumberFormat
  private Double creditGrowth;
  private Double noi;
  private Double nonInterest;
  private Double operatingExpense;
  private Double cashDividend;

  @Step
  public static List<Map<String, Object>> getByStockCode(String stockCode, int yearly, Boolean isAll) {
    try {
      if (TickerBasic.getTickerBasic(stockCode).getComTypeCode().equals("NH")) {
        return getFromBank(stockCode, yearly, isAll);
      } else {
        return getFromCompany(stockCode, LocalDateTime.now().getYear() - 6, yearly, isAll);
      }
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<Map<String, Object>> getFromBank(String stockCode, int yearly, Boolean isAll) {
    StringBuilder fromRatio = new StringBuilder();

    String quantity1 = "";
    String oderBy1 = "";
    String quantity = "";
    String oderBy = "";

    if (isAll == true) {
      quantity1 = " SELECT Ticker, YearReport, LengthReport ";
      oderBy1 = "";
      quantity = " SELECT r2.*  ";
    } else {
      quantity1 = " SELECT top(:quantity+1) Ticker, YearReport, LengthReport ";
      oderBy1 = " ORDER BY YearReport desc, LengthReport desc  ";
      quantity = " SELECT TOP(:quantity) r2.*  ";
    }

    if (yearly == 0) {
      fromRatio.append("; with raw as (");
      fromRatio.append(quantity1);
      fromRatio.append("    , pe as priceToEarning");
      fromRatio.append("    , pb as priceToBook");
      fromRatio.append("    , dividend");
      fromRatio.append("    , bvps as bvps");
      fromRatio.append("    , basic_eps as basic_eps");
      fromRatio.append("    , roe");
      fromRatio.append("    , roa");
      fromRatio.append("    , nim as  interestMargin");
      fromRatio.append("    , 1 - nii_on_toi as nonInterestOnToi");
      fromRatio.append("    , npl_ratio as badDebtPercentage");
      fromRatio.append("    , reserve_on_npl as provisionOnBadDebt");
      fromRatio.append("    , cost_of_financing as costOfFinancing");
      fromRatio.append("    , equity_on_asset as equityOnTotalAsset");
      fromRatio.append("    , equity_on_loan as equityOnLoan");
      fromRatio.append("    , cir as costToIncome");
      fromRatio.append("    , equity_on_liab as equityOnLiability");
      fromRatio.append("    , 1 / nullif( equity_on_asset, 0)  as assetOnEquity");
      fromRatio.append("    , preprovision_profit_on_toi as preProvisionOnToi");
      fromRatio.append("    , net_profit_on_toi as postTaxOnToi");
      fromRatio.append("    , loan_on_earning_assets as loanOnEarnAsset");
      fromRatio.append("    , equity_on_asset / nullif( equity_on_loan, 0)  as loanOnAsset");
      fromRatio.append("    , ldr as loanOnDeposit");
      fromRatio.append("    , deposit_on_earning_assets as depositOnEarnAsset ");
      fromRatio.append("    , npl_on_asset as badDebtOnAsset");
      fromRatio.append("    , liquid_on_liab as liquidityOnLiability");
      fromRatio.append("    , 1 / nullif( equity_on_liab, 0)  as payableOnEquity");
      fromRatio.append("    , loan_growth as creditGrowth");
      fromRatio.append("    , cancelled_debt as cancelDebt");
      fromRatio.append("  FROM  tcdata_ratio_bank");
      fromRatio.append("  WHERE  ticker = :ticker");
      fromRatio.append("  AND LengthReport <> 5");
      fromRatio.append(oderBy1);
      fromRatio.append(" ) ");
      fromRatio.append(quantity);
      fromRatio.append("  , floor(r2.bvps) as bookValuePerShare");
      fromRatio.append("  , case when r2.basic_eps < 0 then round(r2.basic_eps,-1) else floor(r2.basic_eps) end as  earningPerShare");
      fromRatio.append("   , r2.bvps/ nullif( r1.bvps ,0)-1 as [bookValuePerShareChange]");
      fromRatio.append("   , r2.basic_eps/ nullif( r1.basic_eps ,0)-1 as [epsChange]");
      fromRatio.append(" FROM raw r2");
      fromRatio.append(" INNER JOIN raw r1");
      fromRatio.append(" ON (r1.yearreport = r2.yearreport");
      fromRatio.append("   AND r1.lengthreport + 1 = r2.lengthreport)");
      fromRatio.append(" OR (r1.yearreport + 1 = r2.yearreport");
      fromRatio.append("   AND r1.lengthreport  = r2.lengthreport + 3)");
      fromRatio.append(" ORDER BY r2.YearReport desc, r2.LengthReport desc");
    } else {
      fromRatio.append("; with raw as (");
      fromRatio.append(quantity1);
      fromRatio.append("    , pe as priceToEarning");
      fromRatio.append("    , pb as priceToBook");
      fromRatio.append("    , dividend");
      fromRatio.append("    , bvps  as bvps");
      fromRatio.append("    , basic_eps as basic_eps");
      fromRatio.append("    , roe");
      fromRatio.append("    , roa");
      fromRatio.append("    , nim as  interestMargin");
      fromRatio.append("    , 1 - nii_on_toi as nonInterestOnToi");
      fromRatio.append("    , npl_ratio as badDebtPercentage");
      fromRatio.append("    , reserve_on_npl as provisionOnBadDebt");
      fromRatio.append("    , cost_of_financing as costOfFinancing");
      fromRatio.append("    , equity_on_asset as equityOnTotalAsset");
      fromRatio.append("    , equity_on_loan as equityOnLoan");
      fromRatio.append("    , cir as costToIncome");
      fromRatio.append("    , equity_on_liab as equityOnLiability");
      fromRatio.append("    , 1 / nullif( equity_on_asset, 0)  as assetOnEquity");
      fromRatio.append("    , preprovision_profit_on_toi as preProvisionOnToi");
      fromRatio.append("    , net_profit_on_toi as postTaxOnToi");
      fromRatio.append("    , loan_on_earning_assets as loanOnEarnAsset");
      fromRatio.append("    , equity_on_asset / nullif( equity_on_loan, 0)  as loanOnAsset");
      fromRatio.append("    , ldr as loanOnDeposit");
      fromRatio.append("    , deposit_on_earning_assets as depositOnEarnAsset ");
      fromRatio.append("    , npl_on_asset as badDebtOnAsset");
      fromRatio.append("    , liquid_on_liab as liquidityOnLiability");
      fromRatio.append("    , 1 / nullif( equity_on_liab, 0)  as payableOnEquity");
      fromRatio.append("    , loan_growth as creditGrowth");
      fromRatio.append("    , cancelled_debt as cancelDebt");
      fromRatio.append("  FROM  tcdata_ratio_bank");
      fromRatio.append("  WHERE  ticker = :ticker");
      fromRatio.append("  AND LengthReport = 5");
      fromRatio.append(oderBy1);
      fromRatio.append("  ) ");
      fromRatio.append(quantity);
      fromRatio.append("  , floor(r2.bvps) as bookValuePerShare");
      fromRatio.append("  , case when r2.basic_eps < 0 then round(r2.basic_eps,-1) else floor(r2.basic_eps) end as   earningPerShare");
      fromRatio.append("    , r2.bvps/ nullif( r1.bvps ,0)-1 as [bookValuePerShareChange]");
      fromRatio.append("    , r2.basic_eps/ nullif( r1.basic_eps ,0)-1 as [epsChange]");
      fromRatio.append("  FROM raw r2");
      fromRatio.append("  INNER JOIN raw r1");
      fromRatio.append("  ON (r1.yearreport+1 = r2.yearreport");
      fromRatio.append("    AND r1.lengthreport = r2.lengthreport)");
      fromRatio.append(" ORDER BY r2.YearReport desc, r2.LengthReport desc");
    }
    return getFinanceRatioEntities(stockCode, yearly == 0 ? 10 : 5, fromRatio.toString(), isAll);
  }

  public static List<Map<String, Object>> getFromCompany(String stockCode, Integer endYear, int yearly, Boolean isAll){
    String quantity1 = "";
    String oderBy1 = "";
    String quantity = "";
    String oderBy = "";

    if (isAll == true) {
      quantity1 = " SELECT Ticker, YearReport, LengthReport ";
      oderBy1 = "";
      quantity = " SELECT r2.*  ";
    } else {
      quantity1 = " SELECT top(:quantity+1) Ticker, YearReport, LengthReport ";
      oderBy1 = " ORDER BY YearReport desc, LengthReport desc  ";
      quantity = " SELECT TOP(:quantity) r2.*  ";
    }

    StringBuilder fromRatio = new StringBuilder();
    if (yearly == 0) {
      fromRatio.append("; with raw as (                       ");
      fromRatio.append(quantity1);
      fromRatio.append("    , pe as        priceToEarning");
      fromRatio.append("    , pb as       priceToBook");
      fromRatio.append("    , dividend       ");
      fromRatio.append("    , bvps          as bvps");
      fromRatio.append("    , basic_eps  as basic_eps        ");
      fromRatio.append("    , roe         ");
      fromRatio.append("    , roa      ");
      fromRatio.append("    , receivable_days as daysReceivable");
      fromRatio.append("    , inventory_days as daysInventory");
      fromRatio.append("    , payable_days as daysPayable");
      fromRatio.append("    , interest_coverage as ebitOnInterest");
      fromRatio.append("    , equity_on_asset as equityOnTotalAsset               ");
      fromRatio.append("    , 1 / nullif( liab_on_equity, 0)  as equityOnLiability");
      fromRatio.append("    , current_ratio as currentPayment");
      fromRatio.append("    , quick_ratio as quickPayment");
      fromRatio.append("    , quick_ratio as workcapital");
      fromRatio.append("    , ebitda_per_share as ebitdaOnStock");
      fromRatio.append("    , gross_profit_margin as grossProfitMargin");
      fromRatio.append("    , ebit_margin as operatingProfitMargin");
      fromRatio.append("    , ebit_margin as ebitOnRevenue");
      fromRatio.append("    , net_profit_after_mi_margin as postTaxMargin");
      fromRatio.append("    , debt_on_equity as debtOnEquity");
      fromRatio.append("    , debt_on_asset as debtOnAsset");
      fromRatio.append("    , debt_on_ebitda as debtOnEbitda");
      fromRatio.append("    , st_on_lt_debt as shortOnLongDebt");
      fromRatio.append("    , 1 / nullif( equity_on_asset, 0) as assetOnEquity       ");
      fromRatio.append("    , capital_balance as capitalBalance");
      fromRatio.append("    , cash_on_equity as cashOnEquity");
      fromRatio.append("    , cash_on_marcap as cashOnCapitalize");
      fromRatio.append("    , receivable_days + inventory_days - payable_days as cashCirculation");
      fromRatio.append("    , receivable_turnover as revenueOnWorkCapital");
      fromRatio.append("    , capex_on_fixed_asset as capexOnFixedAsset");
      fromRatio.append("    , net_sales_on_asset as revenueOnAsset");
      fromRatio.append("    , net_on_pretax_profit as postTaxOnPreTax");
      fromRatio.append("    , pretax_on_ebit as preTaxOnEbit");
      fromRatio.append("    , liab_on_equity as payableOnEquity");
      fromRatio.append("    , ev_on_ebitda as valueBeforeEbitda");
      fromRatio.append("  FROM  tcdata_ratio_company   ");
      fromRatio.append("  WHERE  ticker = :ticker     ");
      fromRatio.append("  AND LengthReport <> 5");
      fromRatio.append(oderBy1);
      fromRatio.append("  ) ");
      fromRatio.append(quantity);
      fromRatio.append("  , floor(r2.bvps) as bookValuePerShare     ");
      fromRatio.append("  , case when r2.basic_eps < 0 then round(r2.basic_eps,-1) else floor(r2.basic_eps) end as  earningPerShare       ");
      fromRatio.append("    , r2.bvps/ nullif( r1.bvps ,0)-1 as [bookValuePerShareChange]     ");
      fromRatio.append("    , r2.basic_eps/ nullif( r1.basic_eps ,0)-1 as [epsChange]       ");
      fromRatio.append("    , r2.ebitdaOnStock ");
      fromRatio.append("      / nullif( r1.ebitdaOnStock ,0)-1 as [ebitdaOnStockChange]        ");
      fromRatio.append("  FROM raw r2");
      fromRatio.append("  INNER JOIN raw r1");
      fromRatio.append("  ON (r1.yearreport = r2.yearreport");
      fromRatio.append("    AND r1.lengthreport + 1 = r2.lengthreport)");
      fromRatio.append("  OR (r1.yearreport + 1 = r2.yearreport");
      fromRatio.append("    AND r1.lengthreport  = r2.lengthreport + 3)");
      fromRatio.append("  ORDER BY r2.YearReport desc, r2.LengthReport desc   ");
    } else {
      fromRatio.append("; with raw as    (  ");
      fromRatio.append(quantity1);
      fromRatio.append("    , pe as priceToEarning  ");
      fromRatio.append("    , pb as    priceToBook");
      fromRatio.append("    , dividend   ");
      fromRatio.append("    , bvps as bvps   ");
      fromRatio.append("    , basic_eps as basic_eps     ");
      fromRatio.append("    , roe  ");
      fromRatio.append("    , roa           ");
      fromRatio.append("    , receivable_days as daysReceivable");
      fromRatio.append("    , inventory_days as daysInventory");
      fromRatio.append("    , payable_days as daysPayable");
      fromRatio.append("    , interest_coverage as ebitOnInterest");
      fromRatio.append("    , equity_on_asset as equityOnTotalAsset     ");
      fromRatio.append("    , 1 / nullif( liab_on_equity, 0)  as equityOnLiability");
      fromRatio.append("    , current_ratio as currentPayment");
      fromRatio.append("    , quick_ratio as quickPayment");
      fromRatio.append("    , quick_ratio as workcapital");
      fromRatio.append("    , ebitda_per_share as ebitdaOnStock");
      fromRatio.append("    , gross_profit_margin as grossProfitMargin");
      fromRatio.append("    , ebit_margin as ebitOnRevenue");
      fromRatio.append("    , ebit_margin as operatingProfitMargin");
      fromRatio.append("    , net_profit_after_mi_margin as postTaxMargin");
      fromRatio.append("    , debt_on_equity as debtOnEquity");
      fromRatio.append("    , debt_on_asset as debtOnAsset");
      fromRatio.append("    , debt_on_ebitda as debtOnEbitda");
      fromRatio.append("    , st_on_lt_debt as shortOnLongDebt");
      fromRatio.append("    , 1 / nullif( equity_on_asset, 0)  as assetOnEquity     ");
      fromRatio.append("    , capital_balance as capitalBalance");
      fromRatio.append("    , cash_on_equity as cashOnEquity");
      fromRatio.append("    , cash_on_marcap as cashOnCapitalize");
      fromRatio.append("    , receivable_days + inventory_days - payable_days as cashCirculation");
      fromRatio.append("    , receivable_turnover as revenueOnWorkCapital");
      fromRatio.append("    , capex_on_fixed_asset as capexOnFixedAsset");
      fromRatio.append("    , net_sales_on_asset as revenueOnAsset");
      fromRatio.append("    , net_on_pretax_profit as postTaxOnPreTax");
      fromRatio.append("    , pretax_on_ebit as preTaxOnEbit");
      fromRatio.append("    , liab_on_equity as payableOnEquity");
      fromRatio.append("    , ev_on_ebitda as valueBeforeEbitda");
      fromRatio.append("  FROM  tcdata_ratio_company");
      fromRatio.append("  WHERE  ticker = :ticker   ");
      fromRatio.append("  AND LengthReport = 5");
      fromRatio.append(oderBy1);
      fromRatio.append("  ) ");
      fromRatio.append(quantity);
      fromRatio.append("  , floor(r2.bvps) as bookValuePerShare    ");
      fromRatio.append("  , case when r2.basic_eps < 0 then round(r2.basic_eps,-1) else floor(r2.basic_eps) end as earningPerShare    ");
      fromRatio.append("    , r2.bvps/ nullif( r1.bvps ,0)-1 as [bookValuePerShareChange]		");
      fromRatio.append("    , r2.basic_eps/ nullif( r1.basic_eps ,0)-1 as [epsChange]     ");
      fromRatio.append("    , r2.ebitdaOnStock");
      fromRatio.append("      / nullif( r1.ebitdaOnStock ,0)-1 as [ebitdaOnStockChange]");
      fromRatio.append("  FROM raw r2	");
      fromRatio.append("  INNER JOIN raw r1	  ");
      fromRatio.append("  ON (r1.yearreport+1 = r2.yearreport");
      fromRatio.append("    AND r1.lengthreport = r2.lengthreport)");
      fromRatio.append("  ORDER BY r2.YearReport desc, r2.LengthReport desc ");
    }
    return getFinanceRatioEntities(stockCode, yearly == 0 ? 10 : 5, fromRatio.toString(), isAll);
  }

  @NotNull
  private static List<Map<String, Object>> getFinanceRatioEntities(String stockCode, Integer endYear, String fromRatio, Boolean isAll) {
    try {
      if(isAll == true){
        return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(fromRatio).setParameter(TICKER_STR, stockCode)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {
        return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(fromRatio).setParameter(TICKER_STR, stockCode)
          .setParameter("quantity", endYear).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      ex.printStackTrace(System.out);
    }
    return new ArrayList<>();
  }
}