package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class IncomeStatementEntity {
  private static final String YEARLY_STR = "yearly";
  private static final String TICKER_STR = "ticker";
  private String ticker;

  private Long quarter;

  private Long year;

  private Double revenue;

  @NumberFormat
  private Double yearRevenueGrowth;

  @NumberFormat
  private Double quarterRevenueGrowth;

  private Double costOfGoodSold;

  private Double grossProfit;

  private Double operationExpense;

  private Double operationProfit;

  @NumberFormat
  private Double yearOperationProfitGrowth;

  @NumberFormat
  private Double quarterOperationProfitGrowth;

  private Double interestExpense;

  private Double preTaxProfit;

  private Double postTaxProfit;

  private Double shareHolderIncome;

  @NumberFormat
  private Double yearShareHolderIncomeGrowth;

  @NumberFormat
  private Double quarterShareHolderIncomeGrowth;

  private Double investProfit;

  private Double serviceProfit;

  private Double otherProfit;

  private Double provisionExpense;

  private Double operationIncome;

  private Double ebitda;

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

  @Step
  public List<Map<String, Object>> getFromBank(String stockCode, Integer yearly, Boolean isAll) {
    Integer quantity = (yearly == 1) ? 6 : 14;
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT top(:quantity) Ticker, LengthReport as [Quy], 	YearReport as [Nam] ");
    queryBuilder.append("   , cast(ISB25 as float )/ 1000000000 as	[interest_income] ");
    queryBuilder.append("   , cast(ISB26 as float )/ 1000000000 as	[interest_expenses] ");
    queryBuilder.append("   , cast(ISB27 as float )/ 1000000000 as	[Revenue] ");
    queryBuilder.append("   , cast(ISB28 as float )/ 1000000000 as	[fee_income] ");
    queryBuilder.append("   , cast(ISB29 as float )/ 1000000000 as	[fee_expenses] ");
    queryBuilder.append("   , cast(ISB30 as float )/ 1000000000 as	[ServiceProfit] ");
    queryBuilder.append("   , cast(ISB31 + ISB32 + ISB33 + ISB37 as float )/1000000000 as	[InvestProfit] ");
    queryBuilder.append("   , cast(ISB34 as float )/ 1000000000 as	[other_income] ");
    queryBuilder.append("   , cast(ISB35 as float )/ 1000000000 as	[other_expenses] ");
    queryBuilder.append("   , cast(ISB36 as float )/ 1000000000 as	[OtherProfit] ");
    queryBuilder.append("   , cast(ISB37 as float )/ 1000000000 as	[equity_investments_income] ");
    queryBuilder.append("   , cast(ISB38 as float )/ 1000000000 as	[OperationProfit] ");
    queryBuilder.append("   , cast(ISB39 as float )/ 1000000000 as	[OperationExpense] ");
    queryBuilder.append("   , cast(ISB40 as float )/ 1000000000 as	[OperationIncome] ");
    queryBuilder.append("   , cast(ISB41 as float )/ 1000000000 as	[Provision] ");
    queryBuilder.append("   , cast(ISA16 as float )/ 1000000000 as	[PreTaxIncome] ");
    queryBuilder.append("   , cast(ISA17 as float )/ 1000000000 as	[current_income_tax] ");
    queryBuilder.append("   , cast(ISA18 as float )/ 1000000000 as	[deferred_income_tax] ");
    queryBuilder.append("   , cast(ISA16  + ISA19 as float )/ 1000000000 as	[PostTaxIncome] ");
    queryBuilder.append("   , cast(ISA21 as float )/ 1000000000 as	[minority_interest] ");
    queryBuilder.append("   , cast(ISA22 as float )/ 1000000000 as	[ShareHolderIncome] ");
    queryBuilder.append(" FROM STX_FSC_INCOMESTATEMENT  ");
    queryBuilder.append(" WHERE  Ticker = :ticker  ");
    queryBuilder.append(" AND ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ) )  ");
    queryBuilder.append(" ORDER BY YearReport desc, LengthReport desc ");
    try {
      if(isAll == true){
        // String query = fromIncome.replace("top( :quantity )","").replace("order by Nam desc, Quy desc","");
        return TcAnalysis.tcaDbConnection
          .getSession()
          .createNativeQuery(queryBuilder.toString().replace("top(:quantity)","").replace("ORDER BY YearReport desc, LengthReport desc",""))
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

  @Step
  public List<Map<String, Object>> getFromCompany(String stockCode, Integer yearly, Boolean isAll) {
    Integer quantity = (yearly == 1) ? 6 : 14;
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT t3.* ");
    queryBuilder.append("     , t3.[sales_expenses] + t3.[administration_expenses] as [OperationExpense] ");
    queryBuilder.append("     , (t3.GrossProfit + t3.sales_expenses + t3.administration_expenses) as [OperationProfit] ");
    queryBuilder.append("   , (t3.GrossProfit + t3.sales_expenses + t3.administration_expenses + t2.depreciation) as [ebitda]  ");
    queryBuilder.append("   FROM  ");
    queryBuilder.append("   (  ");
    queryBuilder.append("     SELECT top(:quantity) Ticker, YearReport as [Nam], LengthReport as [Quy] ");
    queryBuilder.append("     , cast(ISA3 as float) / 1000000000 as [Revenue] ");
    queryBuilder.append("     , cast(ISA4 as float) / 1000000000 as [COGS] ");
    queryBuilder.append("     , cast(ISA5 as float) / 1000000000 as [GrossProfit] ");
    queryBuilder.append("     , cast(ISA6 as float) / 1000000000 as [revenue_from_financial_activities] ");
    queryBuilder.append("     , cast(ISA7 as float) / 1000000000 as [expenses_from_finacial_activities] ");
    queryBuilder.append("     , cast(ISA8 as float) / 1000000000 as [InterestExpense] ");
    queryBuilder.append("     , cast(ISA102 as float) / 1000000000 as [associates_income] ");
    queryBuilder.append("     , cast(ISA9 as float) / 1000000000 as [sales_expenses] ");
    queryBuilder.append("     , cast(ISA10 as float) / 1000000000 as [administration_expenses] ");
    queryBuilder.append("     , cast(ISA11 as float) / 1000000000 as [operating_profit] ");
    queryBuilder.append("     , cast(ISA12 as float) / 1000000000 as [other_incomes] ");
    queryBuilder.append("     , cast(ISA13 as float) / 1000000000 as [other_expenses] ");
    queryBuilder.append("     , cast(ISA14 as float) / 1000000000 as [other_non_operating_income] ");
    queryBuilder.append("     , cast(ISA15 as float) / 1000000000 as [investment_income] ");
    queryBuilder.append("     , cast(ISA16 as float) / 1000000000 as [PreTaxIncome] ");
    queryBuilder.append("     , cast(ISA19 as float) / 1000000000 as [current_income_tax] ");
    queryBuilder.append("     , cast(ISA17 as float) / 1000000000 as [deferred_income_tax] ");
    queryBuilder.append("     , cast(ISA18 as float) / 1000000000 as [income_tax] ");
    queryBuilder.append("     , cast(ISA20 as float) / 1000000000 as [PostTaxIncome] ");
    queryBuilder.append("     , cast(ISA21 as float) / 1000000000 as [minority_interest] ");
    queryBuilder.append("     , cast(ISA22 as float) / 1000000000 as [ShareHolderIncome] ");
    queryBuilder.append("      ");
    queryBuilder.append("    FROM STX_FSC_INCOMESTATEMENT  ");
    queryBuilder.append("    WHERE  Ticker = :ticker   ");
    queryBuilder.append("      AND ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ) )  ");
    queryBuilder.append("    ORDER BY yearreport desc, LengthReport desc ");
    queryBuilder.append(" ) t3 ");
    queryBuilder.append(" LEFT JOIN  ");
    queryBuilder.append(" ( ");
    queryBuilder.append("   SELECT top(:quantity) Ticker, LengthReport, YearReport,  ");
    queryBuilder.append("      cast(CFA2 as float) / 1000000000  as [depreciation] ");
    queryBuilder.append("   FROM STX_FSC_CASHFLOW  ");
    queryBuilder.append("   WHERE  Ticker = :ticker  ");
    queryBuilder.append("     AND ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 )) ");
    queryBuilder.append("   ORDER BY yearreport desc, LengthReport desc ");
    queryBuilder.append(" ) t2 ");
    queryBuilder.append("   ON t2.LengthReport = t3.Quy and t3.Nam = t2.YearReport ");
    queryBuilder.append("     ORDER BY yearreport desc, LengthReport desc ");
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