package com.tcbs.automation.projection.tcanalysis;

import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectionData {

  private Double revenue;
  @NumberFormat
  private Double yearRevenueGrowth;

  private Double costOfGoodSold;

  private Double grossProfit;

  private Double operationExpense;

  private Double operationProfit;
  @NumberFormat
  private Double yearOperationProfitGrowth;

  private Double interestExpense;

  private Double preTaxProfit;

  private Double postTaxProfit;

  private String ticker;

  private Long year;

  private Double shareHolderIncome;
  @NumberFormat
  private Double yearShareHolderIncomeGrowth;

  private Double netInterestIncome;

  private Double investProfit;

  private Double otherBankCredit;

  private Double oweOtherBank;

  private Double serviceProfit;

  private Double otherProfit;

  private Double otherBankDeposit;

  private Double otherBankLoan;

  private Double provision;

  private Double operationIncome;

  private Double shortAsset;

  private Double cash;

  private Double shortInvest;

  private Double shortReceivable;

  private Double inventory;

  private Double longAsset;

  private Double fixedAsset;

  private Double asset;

  private Double debt;

  private Double shortDebt;

  private Double longDebt;

  private Double equity;

  private Double capital;

  private Double centralBankDeposit;

  private Double stockInvest;

  private Double customerLoan;

  private Double netCustomerLoan;

  private Double fromSale;

  private Double ebitda;

  private Double badLoan;

  private Double provisionExpense;

  private Double otherAsset;

  private Double oweCentralBank;

  private Double valuablePaper;

  private Double payableInterest;

  private Double receivableInterest;

  private Double deposit;

  private Double otherDebt;

  private Double fund;

  private Double unDistributedIncome;

  private Double minorShareHolderProfit;

  private Double investCost;

  private Double fromInvest;

  private Double fromFinancial;

  private Double freeCashFlow;

  private Double payable;


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
    return null;
  }

  public List<Map<String, Object>> getFromBank(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT t.*, t1.badDebt  * t.asset as [badLoan]  ");
      queryBuilder.append(" 	from (select ");
      queryBuilder
        .append("         max(case when t.ItemCode = '6' then cast(t.Value as float) end) as [revenue], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '7' then cast(t.Value as float) end) as [serviceProfit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '8' then cast(t.Value as float) end) as [investProfit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '9' then cast(t.Value as float) end) as [otherProfit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '10' then cast(t.Value as float) end) as [operationProfit], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '11' then cast(t.Value as float) end) as [provisionExpense], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '12' then cast(t.Value as float) end) as [operationIncome], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '13' then cast(t.Value as float) end) as [operationExpense], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '14' then cast(t.Value as float) end) as [preTaxProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '14' then cast(t.Value as float) end) -  ");
      queryBuilder
        .append("         max(case when t.ItemCode = '15' then cast(t.Value as float) end) as [postTaxProfit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '18' then 0-cast(t.Value as float) end) + ");
      queryBuilder
        .append("         max(case when t.ItemCode = '19' then cast(t.Value as float) end) as [shareHolderIncome], ");
      queryBuilder.append("         max(case when t.ItemCode = '21' then cast(t.Value as float) end) as [cash], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '22' then cast(t.Value as float) end) as [centralBankDeposit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '23' then cast(t.Value as float) end) as [otherBankDeposit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '24' then cast(t.Value as float) end) as [otherBankLoan], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '25' then cast(t.Value as float) end) as [stockInvest], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '27' then cast(t.Value as float) end) as [customerLoan], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '28' then  0 - cast(t.Value as float) end) as [provision], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '29' then cast(t.Value as float) end) as [netCustomerLoan], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '30' then cast(t.Value as float) end) as [fixedAsset], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '31' then cast(t.Value as float) end) as [receivableInterest], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '32' then cast(t.Value as float) end) as [otherAsset], ");
      queryBuilder.append("         max(case when t.ItemCode = '33' then cast(t.Value as float) end) as [asset], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '34' then cast(t.Value as float) end) as [otherBankCredit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '35' then cast(t.Value as float) end) as [oweOtherBank], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '36' then cast(t.Value as float) end) as [oweCentralBank], ");
      queryBuilder.append("         max(case when t.ItemCode = '37' then cast(t.Value as float) end) as [deposit], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '38' then cast(t.Value as float) end) as [valuablePaper], ");
      queryBuilder
        .append("         max(case when t.ItemCode = '40' then cast(t.Value as float) end) as [payableInterest], ");
      queryBuilder.append("         max(case when t.ItemCode = '41' then cast(t.Value as float) end) as [otherDebt], ");
      queryBuilder.append("         max(case when t.ItemCode = '43' then cast(t.Value as float) end) as [payable], ");
      queryBuilder.append("         max(case when t.ItemCode = '44' then cast(t.Value as float) end) as [capital], ");
      queryBuilder.append("         max(case when t.ItemCode = '46' then cast(t.Value as float) end) as [fund], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '47' then cast(t.Value as float) end) as [unDistributedIncome], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '48' then cast(t.Value as float) end) as [equity], ");
      queryBuilder.append(
        "         max(case when t.ItemCode = '49' then cast(t.Value as float) end) as [minorShareHolderProfit], ");
      queryBuilder.append("         year(getDate()) +  ASCII(t.YearCode) - ASCII('I') as year, t.ticker, t.yearcode ");
      queryBuilder.append("     	from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("         from db_owner.stox_tb_financials ");
      queryBuilder.append("         where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 			) t ");
      queryBuilder.append("    	 where   t.rn = 1 ");
      queryBuilder.append("    	 group by t.YearCode, t.year, t.ticker) t ");
      queryBuilder.append("  left join  ");
      queryBuilder.append(" 	(select ");
      queryBuilder
        .append("             max(case when t1.ItemCode = '29' then cast(t1.Value as float)/100 end) as [badDebt], ");
      queryBuilder.append(" 			t1.yearcode ");
      queryBuilder.append(" 	from    (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 							substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 						row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 						cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append(" 			from db_owner.stox_tb_ratios	 ");
      queryBuilder.append(" 			where ticker = :ticker ");
      queryBuilder.append(" 					and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 					and year = year(getDate()) - 1  ");
      queryBuilder.append(" 			) t1 ");
      queryBuilder.append(" 	where   t1.rn = 1 ");
      queryBuilder.append(" 	group by t1.YearCode, t1.year, t1.ticker) t1 ");
      queryBuilder.append(" on t1.yearCode = t.yearCode ");
      queryBuilder.append(" 	order by t.yearcode desc ");
      return Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public List<Map<String, Object>> getFromCompany(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append(" SELECT t.*, t1.sale, t1.invest, t1.finance ");
      queryBuilder.append(" from (select ");
      queryBuilder.append("         max(case when t.ItemCode = '6' then cast(t.Value as float) end) as [revenue], ");
      queryBuilder.append("         max(case when t.ItemCode = '7' then 0-cast(t.Value as float) end) as [costOfGoodSold], ");
      queryBuilder.append("         max(case when t.ItemCode = '8' then cast(t.Value as float) end) as [grossProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '12' then 0-cast(t.Value as float) end) as [operationExpense], ");
      queryBuilder.append("         max(case when t.ItemCode = '13' then cast(t.Value as float) end) as [operationProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '15' then 0-cast(t.Value as float) end) as [interestExpense], ");
      queryBuilder.append("         max(case when t.ItemCode = '21' then cast(t.Value as float) end) as [preTaxProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '23' then cast(t.Value as float) end) as [postTaxProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '25' then cast(t.Value as float) end) as [shareHolderIncome], ");
      queryBuilder.append("         max(case when t.ItemCode = '30' then cast(t.Value as float) end) as [cash], ");
      queryBuilder.append("         max(case when t.ItemCode = '31' then cast(t.Value as float) end) as [shortInvest], ");
      queryBuilder.append("         max(case when t.ItemCode = '32' then cast(t.Value as float) end) as [shortReceivable], ");
      queryBuilder.append("         max(case when t.ItemCode = '33' then cast(t.Value as float) end) as [inventory], ");
      queryBuilder.append("         max(case when t.ItemCode = '35' then cast(t.Value as float) end) as [shortAsset], ");
      queryBuilder.append("         max(case when t.ItemCode = '36' then cast(t.Value as float) end) as [longInvest], ");
      queryBuilder.append("         max(case when t.ItemCode = '37' then cast(t.Value as float) end) as [fixedAsset], ");
      queryBuilder.append("         max(case when t.ItemCode = '38' then cast(t.Value as float) end) as [longAsset], ");
      queryBuilder.append("         max(case when t.ItemCode = '39' then cast(t.Value as float) end) as [asset], ");
      queryBuilder.append("         max(case when t.ItemCode = '41' then cast(t.Value as float) end) as [shortDebt], ");
      queryBuilder.append("         max(case when t.ItemCode = '44' then cast(t.Value as float) end) as [longDebt], ");
      queryBuilder.append("         max(case when t.ItemCode = '41' then cast(t.Value as float) end) + ");
      queryBuilder.append("         max(case when t.ItemCode = '44' then cast(t.Value as float) end) as [debt], ");
      queryBuilder.append("         max(case when t.ItemCode = '57' then cast(t.Value as float) end) as [ebitda], ");
      queryBuilder.append("         max(case when t.ItemCode = '56' then cast(t.Value as float) end) as [freeCashFlow], ");
      queryBuilder.append("         max(case when t.ItemCode = '46' then cast(t.Value as float) end) as [payable], ");
      queryBuilder.append("         max(case when t.ItemCode = '47' then cast(t.Value as float) end) as [minorShareHolderProfit], ");
      queryBuilder.append("         max(case when t.ItemCode = '48' then cast(t.Value as float) end) as [capital], ");
      queryBuilder.append("         max(case when t.ItemCode = '50' then cast(t.Value as float) end) as [unDistributedIncome], ");
      queryBuilder.append("         max(case when t.ItemCode = '52' then cast(t.Value as float) end) as [equity], ");
      queryBuilder.append("         max(case when t.ItemCode = '55' then 0-cast(t.Value as float) end) as [investCost], ");
      queryBuilder.append("         year(getDate()) +  ASCII(t.YearCode) - ASCII('I') as year, t.ticker, t.yearcode ");
      queryBuilder.append("     from (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 						substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 					row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 					cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append("         from db_owner.stox_tb_financials ");
      queryBuilder.append("         where ticker = :ticker ");
      queryBuilder.append(" 				and substring(cid, 1, 1)   in ('h','i', 'j', 'k','l','m') ");
      queryBuilder.append(" 				and year = year(getDate()) - 1  ");
      queryBuilder.append(" 			) t ");
      queryBuilder.append("     where   t.rn = 1 ");
      queryBuilder.append("     group by t.YearCode, t.year, t.ticker) t ");
      queryBuilder.append(" left join  ");
      queryBuilder.append(" (select ");
      queryBuilder.append("           max(case when t1.ItemCode = '23' then cast(t1.Value as float) end) as [sale], ");
      queryBuilder.append("           max(case when t1.ItemCode = '24' then cast(t1.Value as float) end) as [invest], ");
      queryBuilder.append("           max(case when t1.ItemCode = '25' then cast(t1.Value as float) end) as [finance], ");
      queryBuilder.append(" 		t1.yearcode ");
      queryBuilder.append(" from    (select * , cast(substring(cid,2,3) as int) as ItemCode, ");
      queryBuilder.append(" 						substring(cid, 1, 1) as YearCode , ");
      queryBuilder.append(" 					row_number() over ( partition by upper(ticker), ");
      queryBuilder.append(" 					cid order by last_date desc, update_no desc, year desc ) as rn ");
      queryBuilder.append(" 		from db_owner.stox_tb_manual_input	 ");
      queryBuilder.append(" 		where ticker = :ticker ");
      queryBuilder.append(" 				and substring(cid, 1, 1)   in ('c','d', 'e', 'f','g') ");
      queryBuilder.append(" 				and year = year(getDate()) - 1  ");
      queryBuilder.append(" 		) t1 ");
      queryBuilder.append(" where   t1.rn = 1 ");
      queryBuilder.append(" group by t1.YearCode, t1.year, t1.ticker) t1 ");
      queryBuilder.append(" on ASCII(t1.YearCode) = ASCII(t1.YearCode) - 6 ");
      queryBuilder.append(" order by t.yearcode desc ");
      return Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}