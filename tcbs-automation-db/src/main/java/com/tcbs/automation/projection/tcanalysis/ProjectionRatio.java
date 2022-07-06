package com.tcbs.automation.projection.tcanalysis;

import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.jetbrains.annotations.Nullable;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionRatio {

  private String ticker;

  private Long year;

  private Double priceToEarning;

  private Double priceToBook;

  private Double valueBeforeEbitda;
  @NumberFormat
  private Double dividend;

  private Long earningPerShare;
  @NumberFormat
  private Double epsChange;

  private Double ebitdaOnStock;
  @NumberFormat
  private Double ebitdaOnStockChange;
  @NumberFormat
  private Double bookValuePerShareChange;

  private Long bookValuePerShare;
  @NumberFormat
  private Double roe;
  @NumberFormat
  private Double roa;
  @NumberFormat
  private Double grossProfitMargin;
  @NumberFormat
  private Double operatingProfitMargin;
  @NumberFormat
  private Double postTaxMargin;

  private Double debtOnEquity;

  private Double debtOnAsset;

  private Double debtOnEbitda;

  private Double daysReceivable;

  private Double daysInventory;

  private Double daysPayable;

  private Double currentPayment;

  private Double quickPayment;

  private Double ebitOnInterest;

  private Double assetOnEquity;
  @NumberFormat
  private Double cashOnEquity;

  private Double cashCirculation;

  private Double postTaxOnPreTax;

  private Double preTaxOnEbit;

  private Double revenueOnWorkCapital;

  private Double revenueOnAsset;
  @NumberFormat
  private Double ebitOnRevenue;
  @NumberFormat
  private Double capexOnFixedAsset;

  private Double capitalBalance;
  @NumberFormat
  private Double preProvisionOnToi;
  @NumberFormat
  private Double costToIncome;
  @NumberFormat
  private Double nonInterestOnToi;
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
  private Double costOfFinancing;
  @NumberFormat
  private Double badDebtPercentage;
  @NumberFormat
  private Double provisionOnBadDebt;
  @NumberFormat
  private Double equityOnLoan;

  private Double equityOnTotalAsset;
  @NumberFormat
  private Double interestMargin;

  private Double payableOnEquity;
  @NumberFormat
  private Double liquidityOnLiability;

  private Double cashOnCapitalize;

  private Double shortOnLongDebt;
  @NumberFormat
  private Double cancelDebt;
  @Nullable
  private Double loan;
  @NumberFormat
  private Double creditGrowth;

  @Step
  public List<Map<String, Object>> getByTicker(String ticker) {
    String sql = "SELECT top 1 typeId as type from stox_tb_Company where Ticker = :ticker";

    try {
      List<Map<String, Object>> typeResult = Projection.projectionDbConnection.getSession().createNativeQuery(sql)
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      int type = Integer.parseInt(typeResult.get(0).get("type").toString());
      if (type == 0) {
        return getFromBank(ticker);
      } else {
        return getFromCompany(ticker);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public List<Map<String, Object>> getFromBank(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT *, Round(t.interestMargin1, 4) as [interestMargin] from ");
      queryBuilder.append(" 	(select ");
      queryBuilder
        .append("             max(case when t1.ItemCode = '29' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [loan], ");
      queryBuilder.append(" 			t1.yearcode2 ");
      queryBuilder.append(" 	from    (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode2 , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append(" 			from db_owner.stox_tb_financials	 ");
      queryBuilder.append(" 			where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 			) t1 ");
      queryBuilder.append(" 	where   t1.rn = 1 ");
      queryBuilder.append(" 	group by t1.YearCode2, t1.year, t1.ticker) t2 ");
      queryBuilder.append("  left join  ");
      queryBuilder
        .append("   (select max(case when t.ItemCode = '7' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [priceToEarning], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '8' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [priceToBook], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '10' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [dividend], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '11' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [earningPerShare], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '12' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [epsChange], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '13' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [bookValuePerShare], ");
      queryBuilder.append("           max(case when t.ItemCode = '15' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [roe], ");
      queryBuilder.append("           max(case when t.ItemCode = '16' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [roa], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '17' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [preProvisionOnToi], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '19' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [postTaxOnToi], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '20' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [nonInterestOnToi], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '21' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [costToIncome], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '23' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [loanOnEarnAsset], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '24' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [loanOnAsset], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '26' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [loanOnDeposit], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '27' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [depositOnEarnAsset], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '28' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [badDebtPercentage], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '29' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [badDebtOnAsset], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '32' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [provisionOnBadDebt], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '34' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [costOfFinancing], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '35' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [interestMargin1], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '36' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [equityOnTotalAsset], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '37' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [payableOnEquity], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '38' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [liquidityOnLiability], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '39' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [equityOnLoan], ");
      queryBuilder
        .append("           year(getDate()) +  ASCII(t.YearCode) - ASCII('I') as year, t.ticker, t.yearcode ");
      queryBuilder.append("       from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("           from db_owner.stox_tb_ratios ");
      queryBuilder.append("           where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 				) t ");
      queryBuilder.append("       where   t.rn = 1 ");
      queryBuilder.append("       group by t.YearCode, t.year, t.ticker	) t ");
      queryBuilder.append(" on t2.yearCode2 = t.yearCode ");
      queryBuilder.append("   left join ");
      queryBuilder.append("       (select ");
      queryBuilder
        .append("           max(case when t1.ItemCode = '154' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [cancelDebt], ");
      queryBuilder.append("            t1.yearcode as yearcode1 ");
      queryBuilder.append("       from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("           from db_owner.stox_tb_financial_model ");
      queryBuilder.append("           where ticker = :ticker ");
      queryBuilder.append(" 			and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 			and year = year(getDate()) - 1  ");
      queryBuilder.append(" 				) t1 ");
      queryBuilder.append("       where   t1.rn = 1 ");
      queryBuilder.append("       group by t1.YearCode, t1.year, t1.ticker) t1 ");
      queryBuilder.append("       on t1.yearCode1 = t.yearCode ");
      queryBuilder.append("   order by t.yearcode desc ");
      return Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public List<Map<String, Object>> getFromCompany(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT t.*, t1.capex/t1.fixedAsset as [capexOnFixedAsset],  ");
      queryBuilder.append("   	ROUND(t1.capitalBalance, 0)	as capitalBalance, t1.cash/(select top(1) F5_7 from ");
      queryBuilder.append(" dbo.stox_tb_ratio where ticker = :ticker ");
      queryBuilder.append(" order by YearReport_Cal desc, LengthReport_Cal desc) as [cashOnCapitalize], ");
      queryBuilder.append(
        "   		t1.shortDebt/(case when t1.longDebt = 0 then null else t1.longDebt end) as [shortOnLongDebt] ");
      queryBuilder.append(
        "   from (select max(case when t.ItemCode = '6' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [priceToEarning], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '7' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [valueBeforeEbitda], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '8' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [priceToBook], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '12' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [dividend], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '13' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [earningPerShare], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '14' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [epsChange], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '21' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [ebitdaOnStock], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '22' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [ebitdaOnStockChange], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '23' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [bookValuePerShare], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '24' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [bookValuePerShareChange], ");
      queryBuilder.append("           max(case when t.ItemCode = '25' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [roe], ");
      queryBuilder.append("           max(case when t.ItemCode = '26' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [roa], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '27' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [grossProfitMargin], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '28' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [operatingProfitMargin], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '30' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [postTaxMargin], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '31' and ISNUMERIC(t.Value) = 1 then cast(t.Value as decimal(10,1)) end) as [debtOnEquity], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '32' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [debtOnAsset], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '33' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [debtOnEbitda], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '34' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [ebitOnInterest], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '35' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [assetOnEquity], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '36' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end)/100 as [cashOnEquity], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '37' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [currentPayment], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '38' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [quickPayment], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '40' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [daysReceivable], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '41' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [daysInventory], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '42' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [daysPayable], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '43' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [cashCirculation], ");
      queryBuilder.append(
        "           max(case when t.ItemCode = '45' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [revenueOnWorkCapital], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '46' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [revenueOnAsset], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '50' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [postTaxOnPreTax], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '51' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float) end) as [preTaxOnEbit], ");
      queryBuilder
        .append("           max(case when t.ItemCode = '52' and ISNUMERIC(t.Value) = 1 then cast(t.Value as float)/100 end) as [ebitOnRevenue], ");
      queryBuilder
        .append("           year(getDate()) +  ASCII(t.YearCode) - ASCII('I') as  year, t.ticker, t.yearcode ");
      queryBuilder.append("       from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("           from db_owner.stox_tb_ratios ");
      queryBuilder.append("           where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 				) t ");
      queryBuilder.append("       where   t.rn = 1 ");
      queryBuilder.append(" group by t.YearCode, t.year, t.ticker ");
      queryBuilder.append(" 		) t ");
      queryBuilder.append("   left join ");
      queryBuilder.append("       (select ");
      queryBuilder
        .append("           max(case when t1.ItemCode = '37' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [fixedAsset], ");
      queryBuilder.append("           max(case when t1.ItemCode = '55' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [capex], ");
      queryBuilder.append("           max(case when t1.ItemCode = '30' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [cash], ");
      queryBuilder.append("           max(case when t1.ItemCode = '35' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end)  - ");
      queryBuilder.append(
        "           max(case when t1.ItemCode = '43' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [capitalBalance], ");
      queryBuilder
        .append("           max(case when t1.ItemCode = '41' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [shortDebt], ");
      queryBuilder
        .append("           max(case when t1.ItemCode = '44' and ISNUMERIC(t1.Value) = 1 then cast(t1.Value as float) end) as [longDebt], ");
      queryBuilder.append("            t1.yearcode ");
      queryBuilder.append("       from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("           from db_owner.stox_tb_financials ");
      queryBuilder.append("           where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 				) t1 ");
      queryBuilder.append("       where   t1.rn = 1 ");
      queryBuilder.append("       group by t1.YearCode, t1.year, t1.ticker) t1 ");
      queryBuilder.append("       on t1.yearCode = t.yearCode ");
      queryBuilder.append("   order by t.yearcode desc ");
      return Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}