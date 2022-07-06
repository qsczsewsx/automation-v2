package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class BalanceSheetEntity {
  private static final String YEARLY_STR = "yearly";
  private static final String TICKER_STR = "ticker";
  private String ticker;

  private Long quarter;

  private Long year;

  private Double shortAsset;

  private Double cash;

  private Double shortInvest;

  private Double shortReceivable;

  private Double inventory;

  private Double longAsset;

  private Double longReceivable;

  private Double fixedAsset;

  private Double asset;

  private Double debt;

  private Double shortDebt;

  private Double longDebt;

  private Double equity;

  private Double capital;

  private Double centralBankDeposit;

  private Double otherBankDeposit;

  private Double otherBankLoan;

  private Double stockInvest;

  private Double customerLoan;

  private Double netCustomerLoan;

  private Double badLoan;

  private Double provision;

  private Double otherAsset;

  private Double otherBankCredit;

  private Double oweOtherBank;

  private Double oweCentralBank;

  private Double valuablePaper;

  private Double payableInterest;

  private Double receivableInterest;

  private Double deposit;

  private Double otherDebt;

  private Double fund;

  private Double unDistributedIncome;

  private Double minorShareHolderProfit;

  private Double payable;

  @Step
  public List<Map<String, Object>> getByStockCode(String stockCode, Integer yearly, Boolean isAll) {

    try {
      if (TickerBasic.getTickerBasic(stockCode).getComTypeCode().equals("NH")) {
        return getFromBank(stockCode, yearly, isAll);
      } else {
        return getFromCompany(stockCode, yearly, isAll);
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }

  public List<Map<String, Object>> getFromBank(String stockCode, Integer yearly, Boolean isAll) {
    Integer quantity = (yearly == 1) ? 5 : 10;
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM ( ");
    queryBuilder.append("     SELECT top(:quantity)  Ticker, YearReport as [Nam], LengthReport as [Quy] ");
    queryBuilder.append("     , cast(BSA53 as float) / 1000000000 as [asset] ");
    queryBuilder.append("     , cast(BSA2 as float) / 1000000000 as [Cash] ");
    queryBuilder.append("     , cast(BSB97 as float) / 1000000000 as [centralBankDeposit] ");
    queryBuilder.append("     , cast(BSB98 as float) / 1000000000 as [ci_balances_loans] ");
    queryBuilder.append("     , cast(BSB258 as float) / 1000000000 as  [otherBankDeposit] ");
    queryBuilder.append("     , cast(BSB259 as float) / 1000000000 as [otherBankLoan] ");
    queryBuilder.append("     , cast(BSB260 as float) / 1000000000 as [ci_allowance] ");
    queryBuilder.append("     , cast(BSB99 + BSB106 + BSA43 as float) / 1000000000 as [stockInvest] ");
    queryBuilder.append("     , cast(BSB100 as float) / 1000000000 as [trading_securities] ");
    queryBuilder.append("     , cast(BSB101 as float) / 1000000000 as [trading_securities_allowance] ");
    queryBuilder.append("     , cast(BSB102 as float) / 1000000000 as [other_financial_assets] ");
    queryBuilder.append("     , cast(BSB103 as float) / 1000000000 as [netCustomerLoan] ");
    queryBuilder.append("     , cast(BSB104 as float) / 1000000000 as [customerLoan] ");
    queryBuilder.append("     , cast(BSB105 as float) / 1000000000 as [provision] ");
    queryBuilder.append("     , cast(BSB106 as float) / 1000000000 as [investment_securities] ");
    queryBuilder.append("     , cast(BSB107 as float) / 1000000000 as [afs_securities] ");
    queryBuilder.append("     , cast(BSB108 as float) / 1000000000 as [htm_securities] ");
    queryBuilder.append("     , cast(BSB109 as float) / 1000000000 as [investment_securities_allowance] ");
    queryBuilder.append("     , cast(BSA43 as float) / 1000000000 as [lt_investments] ");
    queryBuilder.append("     , cast(BSA44 as float) / 1000000000 as [investments_subsidiaries] ");
    queryBuilder.append("     , cast(BSA45 as float) / 1000000000 as [investments_associates] ");
    queryBuilder.append("     , cast(BSA46 as float) / 1000000000 as [other_lt_investments] ");
    queryBuilder.append("     , cast(BSA47 as float) / 1000000000 as [lt_investments_allowance] ");
    queryBuilder.append("     , cast(BSA29 as float) / 1000000000 as [fixedAsset] ");
    queryBuilder.append("     , cast(BSA30 as float) / 1000000000 as [tangible_fixed_assets] ");
    queryBuilder.append("     , cast(BSB110 as float) / 1000000000 as [otherAsset] ");
    queryBuilder.append("     , cast(BSB264 as float) / 1000000000 as [other_receivables] ");
    queryBuilder.append("     , cast(BSB265 as float) / 1000000000 as [receivableInterest] ");
    queryBuilder.append("     , cast(BSA96 as float) / 1000000000 as [liabilities_equity] ");
    queryBuilder.append("     , cast(BSA54 as float) / 1000000000 as [liability] ");
    queryBuilder.append("     , cast(BSB111 as float) / 1000000000 as [oweCentralBank] ");
    queryBuilder.append("     , cast(BSB112 as float) / 1000000000 as [ci_deposits_borrowings] ");
    queryBuilder.append("     , cast(BSB270 as float) / 1000000000 as [otherBankCredit] ");
    queryBuilder.append("     , cast(BSB271 as float) / 1000000000 as  [oweOtherBank] ");
    queryBuilder.append("     , cast(BSB272 as float) / 1000000000 as [payableInterest] ");
    queryBuilder.append("     , cast(BSB113 as float) / 1000000000 as [customerCredit] ");
    queryBuilder.append("     , cast(BSB116 as float) / 1000000000 as [valuablePaper] ");
    queryBuilder.append("     , cast(BSB116 as float) / 1000000000 as [otherPayable] ");
    queryBuilder.append("     , cast(BSA78 as float) / 1000000000 as [equity] ");
    queryBuilder.append("     , cast(BSB118 as float) / 1000000000 as [capitals] ");
    queryBuilder.append("     , cast(BSA80 as float) / 1000000000 as [capital] ");
    queryBuilder.append("     , cast(BSB119 as float) / 1000000000 as [construction_capital] ");
    queryBuilder.append("     , cast(BSA81 as float) / 1000000000 as [capital_surplus] ");
    queryBuilder.append("     , cast(BSA83 as float) / 1000000000 as [treasury_stock] ");
    queryBuilder.append("     , cast(BSB120 as float) / 1000000000 as [preferred_shares] ");
    queryBuilder.append("     , cast(BSB121 as float) / 1000000000 as [fund] ");
    queryBuilder.append("     , cast(BSA90 as float) / 1000000000 as [undistributedIncome] ");
    queryBuilder.append("     , cast(BSA210 as float) / 1000000000 as [minorShareHolderProfit] ");
    queryBuilder.append("   FROM	STX_FSC_BALANCESHEET  ");
    queryBuilder.append("   WHERE ticker = :ticker  ");
    queryBuilder.append("     AND ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ) )   "); 
    queryBuilder.append("   ORDER BY yearreport desc, LengthReport desc  ");
    queryBuilder.append(" ) BS  ");
    queryBuilder.append(" left join  ");
    queryBuilder.append(" ( ");
    queryBuilder.append("   SELECT top(:quantity) YearReport as [YearReport_BK], LengthReport  as [LengthReport_BK],  ");
    queryBuilder.append("     (NOB42 + NOB43 + NOB44) / 1000000000 as [badDebt] ");
    queryBuilder.append("   FROM	STX_FSC_NOTEBANK  ");
    queryBuilder.append("   WHERE	ticker = :ticker ");
    queryBuilder.append("     AND  ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ) )  ");
    queryBuilder.append("   ORDER BY yearreport desc, LengthReport desc  ");
    queryBuilder.append(" ) BK   ");
    queryBuilder.append(" ON BS.Nam = BK.YearReport_BK and BS.Quy = BK.LengthReport_BK ");
    try {
      if(isAll == true){
        return TcAnalysis.tcaDbConnection
          .getSession()
          .createNativeQuery(queryBuilder.toString().replace("top(:quantity)","").replace("ORDER BY yearreport desc, LengthReport desc",""))
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {

        return TcAnalysis.tcaDbConnection
          .getSession()
          .createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setParameter("quantity", quantity)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public List<Map<String, Object>> getFromCompany(String stockCode, Integer yearly, Boolean isAll) {
    Integer quantity = (yearly == 1) ? 5 : 10;
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT top(:quantity) Ticker, YearReport  as [Nam], LengthReport  as [Quy] ");
    queryBuilder.append("     , cast(BSA1 as float) / 1000000000 as [shortAsset] ");
    queryBuilder.append("     , cast(BSA2 as float) / 1000000000 as [Cash] ");
    queryBuilder.append("     , cast(BSA3 as float) / 1000000000 as [cashs] ");
    queryBuilder.append("     , cast(BSA4 as float) / 1000000000 as [cash_equivalent] ");
    queryBuilder.append("     , cast(BSA5 as float) / 1000000000 as [shortInvest] ");
    queryBuilder.append("     , cast(BSA7 as float) / 1000000000 as [provision_st_inv] ");
    queryBuilder.append("     , cast(BSA8 as float) / 1000000000 as [shortReceivable] ");
    queryBuilder.append("     , cast(BSA9 as float) / 1000000000 as [accounts_receivables] ");
    queryBuilder.append("     , cast(BSA10 as float) / 1000000000 as [prepayment_to_suppliers] ");
    queryBuilder.append("     , cast(BSA11 as float) / 1000000000 as [st_internal_receivables] ");
    queryBuilder.append("     , cast(BSA13 as float) / 1000000000 as [other_receivables] ");
    queryBuilder.append("     , cast(BSA14 as float) / 1000000000 as [provision_for_st_receivables] ");
    queryBuilder.append("     , cast(BSA15 as float) / 1000000000 as [inventory] ");
    queryBuilder.append("     , cast(BSA16 as float) / 1000000000 as [inventories] ");
    queryBuilder.append("     , cast(BSA17 as float) / 1000000000 as [provision_for_inventories] ");
    queryBuilder.append("     , cast(BSA18 as float) / 1000000000 as [total_other_current_assets] ");
    queryBuilder.append("     , cast(BSA19 as float) / 1000000000 as [st_prepaid_expenses] ");
    queryBuilder.append("     , cast(BSA20 as float) / 1000000000 as [input_vat] ");
    queryBuilder.append("     , cast(BSA22 as float) / 1000000000 as [other_current_assets] ");
    queryBuilder.append("     , cast(BSA23 as float) / 1000000000 as [longAsset] ");
    queryBuilder.append("     , cast(BSA24 as float) / 1000000000 as [longReceivable] ");
    queryBuilder.append("     , cast(BSA25 as float) / 1000000000 as [lt_accounts_receivables] ");
    queryBuilder.append("     , cast(BSA26 as float) / 1000000000 as [lt_internal_receivables] ");
    queryBuilder.append("     , cast(BSA27 as float) / 1000000000 as [other_lt_receivables] ");
    queryBuilder.append("     , cast(BSA28 as float) / 1000000000 as [provision_for_lt_receivables] ");
    queryBuilder.append("     , cast(BSA29 as float) / 1000000000 as [fixedAsset] ");
    queryBuilder.append("     , cast(BSA30 as float) / 1000000000 as [tangible_fixed_assets] ");
    queryBuilder.append("     , cast(BSA31 as float) / 1000000000 as [gross_tangible_fixed_assets] ");
    queryBuilder.append("     , cast(BSA32 as float) / 1000000000 as [tangible_fixed_assets_depreciation] ");
    queryBuilder.append("     , cast(BSA33 as float) / 1000000000 as [fixed_financial_leases] ");
    queryBuilder.append("     , cast(BSA34 as float) / 1000000000 as [gross_fixed_financial_leases] ");
    queryBuilder.append("     , cast(BSA35 as float) / 1000000000 as [fixed_financial_leases_depreciation] ");
    queryBuilder.append("     , cast(BSA36 as float) / 1000000000 as [intangible_fixed_assets] ");
    queryBuilder.append("     , cast(BSA37 as float) / 1000000000 as [gross_intangible_fixed_assets] ");
    queryBuilder.append("     , cast(BSA38 as float) / 1000000000 as [intangible_fixed_assets_depreciation] ");
    queryBuilder.append("     , cast(BSA39 as float) / 1000000000 as [cip_before_2015] ");
    queryBuilder.append("     , cast(BSA40 as float) / 1000000000 as [real_estate_investments] ");
    queryBuilder.append("     , cast(BSA41 as float) / 1000000000 as [gross_real_estate_investments] ");
    queryBuilder.append("     , cast(BSA42 as float) / 1000000000 as [real_estate_investments_depreciation] ");
    queryBuilder.append("     , cast(BSA43 as float) / 1000000000 as [lt_investments] ");
    queryBuilder.append("     , cast(BSA44 as float) / 1000000000 as [inv_in_sucast(BSidiary] ");
    queryBuilder.append("     , cast(BSA45 as float) / 1000000000 as [inv_in_associates] ");
    queryBuilder.append("     , cast(BSA46 as float) / 1000000000 as [other_lt_inv] ");
    queryBuilder.append("     , cast(BSA47 as float) / 1000000000 as [provision_for_lt_investment] ");
    queryBuilder.append("     , cast(BSA48 as float) / 1000000000 as [goodwill_before_2015] ");
    queryBuilder.append("     , cast(BSA49 as float) / 1000000000 as [total_other_lt_assets] ");
    queryBuilder.append("     , cast(BSA50 as float) / 1000000000 as [lt_prepaid_expenses] ");
    queryBuilder.append("     , cast(BSA52 as float) / 1000000000 as [other_lt_assets] ");
    queryBuilder.append("     , cast(BSA53 as float) / 1000000000 as [asset] ");
    queryBuilder.append("     , cast(BSA54 as float) / 1000000000 as [liability] ");
    queryBuilder.append("     , cast(BSA55 as float) / 1000000000 as [current_liabilities] ");
    queryBuilder.append("     , cast(BSA56 as float) / 1000000000 as [shortDebt] ");
    queryBuilder.append("     , cast(BSA57 as float) / 1000000000 as [st_account_payables] ");
    queryBuilder.append("     , cast(BSA58 as float) / 1000000000 as [st_advances_from_customers] ");
    queryBuilder.append("     , cast(BSA59 as float) / 1000000000 as [taxes_and_obligations_to_state_budget] ");
    queryBuilder.append("     , cast(BSA60 as float) / 1000000000 as [payables_to_employees] ");
    queryBuilder.append("     , cast(BSA61 as float) / 1000000000 as [st_accruals] ");
    queryBuilder.append("     , cast(BSA62 as float) / 1000000000 as [interco_payables] ");
    queryBuilder.append("     , cast(BSA64 as float) / 1000000000 as [otherPayable] ");
    queryBuilder.append("     , cast(BSA65 as float) / 1000000000 as [provisions_st_payables] ");
    queryBuilder.append("     , cast(BSA67 as float) / 1000000000 as [lt_liabilities] ");
    queryBuilder.append("     , cast(BSA68 as float) / 1000000000 as [lt_account_payables] ");
    queryBuilder.append("     , cast(BSA76 as float) / 1000000000 as [lt_unearned_rev] ");
    queryBuilder.append("     , cast(BSA70 as float) / 1000000000 as [other_lt_payables] ");
    queryBuilder.append("     , cast(BSA71 as float) / 1000000000 as [longDebt] ");
    queryBuilder.append("     , cast(BSA72 as float) / 1000000000 as [deferred_tax] ");
    queryBuilder.append("     , cast(BSA74 as float) / 1000000000 as [provisions_lt_payables] ");
    queryBuilder.append("     , cast(BSA78 as float) / 1000000000 as [equity] ");
    queryBuilder.append("     , cast(BSA79 as float) / 1000000000 as [equity_and_funds] ");
    queryBuilder.append("     , cast(BSA80 as float) / 1000000000 as [capital] ");
    queryBuilder.append("     , cast(BSA81 as float) / 1000000000 as [capital_surplus] ");
    queryBuilder.append("     , cast(BSA83 as float) / 1000000000 as [treasury_stock] ");
    queryBuilder.append("     , cast(BSA84 as float) / 1000000000 as [differences_upon_asset_revaluation] ");
    queryBuilder.append("     , cast(BSA85 as float) / 1000000000 as [exchange_rate_difference] ");
    queryBuilder.append("     , cast(BSA86 as float) / 1000000000 as [development_investment_fund] ");
    queryBuilder.append("     , cast(BSA91 as float) / 1000000000 as [corporation_arrangement_support_fund] ");
    queryBuilder.append("     , cast(BSA87 as float) / 1000000000 as [financial_reserves] ");
    queryBuilder.append("     , cast(BSA90 as float) / 1000000000 as [other_funds] ");
    queryBuilder.append("     , cast(BSA278 as float) / 1000000000 as [undistributedIncome] ");
    queryBuilder.append("     , cast(BSA210 as float) / 1000000000 as [minorShareHolderProfit] ");
    queryBuilder.append("     , cast(BSA92 as float) / 1000000000 as [budget_resources_and_funds] ");
    queryBuilder.append("     , cast(BSA95 as float) / 1000000000 as [minority_equity_before_2015] ");
    queryBuilder.append("     , cast(BSA96 as float) / 1000000000 as [total_resources] ");
    queryBuilder.append(" FROM	STX_FSC_BALANCESHEET  ");
    queryBuilder.append(" WHERE	ticker = :ticker ");
    queryBuilder.append("   AND  ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ) )  ");
    queryBuilder.append(" ORDER BY yearreport desc, LengthReport desc ");
    try {
      if(isAll == true){
        return TcAnalysis.tcaDbConnection
          .getSession()
          .createNativeQuery(queryBuilder.toString().replace("top(:quantity)","").replace("ORDER BY yearreport desc, LengthReport desc",""))
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {

        return TcAnalysis.tcaDbConnection
          .getSession()
          .createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setParameter("quantity", quantity)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}