package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Basic;
import javax.persistence.Column;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblIdataFinanceRatioComp {

  private String ticker;
  private Short yearReport;
  private Short lengthReport;
  private Double cash;
  private Double liability;
  private Double equity;
  private Double shortDebt;
  private Double longDebt;
  private Double fixedAsset;
  private Double debt;
  private Double asset;
  private Double shortLiability;
  private Double capitalBalance;
  private Double workCapital;
  private Double grossProfit;
  private Double revenue;
  private Double operatingProfit;
  private Double postTax;
  private Double preTax;
  private Double interestExpense;
  private Double capex;
  private Double pE;
  private Double pB;
  private Double publicStock;
  private Double roe;
  private Double capitalize;
  private Double evEbitda;
  private Double currentPayment;
  private Double roa;
  private Double daysPayable;
  private Double ebitda;
  private Double quickPayment;
  private Double eps;
  private Double bvps;
  private Double dividend;
  private Double ebit;
  private Double daysReceivable;
  private Double daysInventory;
  private Double cashCirculation;
  private Double ebitdaOnStock;
  private Double ebitOnInterest;
  private Double revenueOnWorkCapital;
  private Double revenueOnAsset;
  private Double ebitOnRevenue;
  private Double postTaxOnPreTax;
  private Double capexOnFixedAsset;
  private Double grossProfitMargin;
  private Double operatingProfitMargin;
  private Double postTaxMargin;
  private Double debtOnEbitda;
  private Double preTaxOnEbit;
  private Double debtOnEquity;
  private Double debtOnAsset;
  private Double shortOnLongDebt;
  private Double assetOnEquity;
  private Double cashOnEquity;
  private Double cashOnCapitalize;
  private Double equityOnTotalAsset;
  private Double payableOnEquity;
  private Double epsChange;
  private Double ebitdaOnStockChange;
  private Double bookValuePerShare;
  // For rating
  private Double priceToSales;
  private Double netDebtOnEbitda;
  private Double netDebtOnEquity;

  public static Map<String, Object> getTop200() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT pe AS [P/E]  ");
    queryBuilder.append(" 	, ev_on_ebitda AS [EV/EBITDA] ");
    queryBuilder.append(" 	, pb AS [P/B] ");
    queryBuilder.append(" 	, dividend AS [dividend] ");
    queryBuilder.append(" 	, roe AS [ROE] ");
    queryBuilder.append(" 	, roa AS [ROA] ");
    queryBuilder.append(" 	, gross_profit_margin AS [GrossProfitMargin] ");
    queryBuilder.append(" 	, ebit_margin AS [OperatingProfitMargin] ");
    queryBuilder.append(" 	, net_profit_after_mi_margin AS [PostTaxMargin] ");
    queryBuilder.append(" 	, liab_on_asset / NULLIF (equity_on_asset, 0)  AS [DebtOnEquity] ");
    queryBuilder.append(" 	, liab_on_asset AS [DebtOnAsset] ");
    queryBuilder.append(" 	, debt_on_ebitda AS [DebtOnEbitda] ");
    queryBuilder.append(" 	, st_on_lt_debt AS [ShortOnLongDebt] ");
    queryBuilder.append(" 	, interest_coverage AS [EbitOnInterest] ");
    queryBuilder.append(" 	, 1 / NULLIF (equity_on_asset, 0) AS [AssetOnEquity] ");
    queryBuilder.append(" 	, cash_on_equity AS [CashOnEquity] ");
    queryBuilder.append(" 	, cash_on_marcap AS [CashOnCapitalize] ");
    queryBuilder.append(" 	, current_ratio AS [CurrentPayment] ");
    queryBuilder.append(" 	, quick_ratio AS [QuickPayment] ");
    queryBuilder.append(" 	, receivable_days AS [DaysReceivable] ");
    queryBuilder.append(" 	, inventory_days AS [DaysInventory] ");
    queryBuilder.append(" 	, payable_days AS [DaysPayable] ");
    queryBuilder.append(" 	, cash_ratio AS [CashCirculation] ");
    queryBuilder.append(" 	, receivable_turnover AS [RevenueOnWorkCapital] ");
    queryBuilder.append(" 	, asset_turnover AS [RevenueOnAsset] ");
    queryBuilder.append(" 	, ebit_margin AS [EbitOnRevenue] ");
    queryBuilder.append(" 	, debt_on_ebitda AS [NetDebtOnEbitda] ");
    queryBuilder.append(" 	, debt_on_equity AS [NetDebtOnEquity] ");
    queryBuilder.append(" 	, ps AS [P/S] ");
    queryBuilder.append(" FROM tca_ratio_latest_special_portfolio ");
    queryBuilder.append(" WHERE portfolio = 'TOP200' ");

    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return result.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      TcAnalysis.tcaDbConnection.closeSession();
    }
    return null;
  }


  public static Map<String, Object> getAvgIndustry(String idLevel2) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" 	SELECT pe AS [P/E]  ");
    queryBuilder.append(" 		, ev_on_ebitda AS [EV/EBITDA] ");
    queryBuilder.append(" 		, pb AS [P/B] ");
    queryBuilder.append(" 		, dividend AS [dividend] ");
    queryBuilder.append(" 		, roe AS [ROE] ");
    queryBuilder.append(" 		, roa AS [ROA] ");
    queryBuilder.append(" 		, gross_profit_margin AS [GrossProfitMargin] ");
    queryBuilder.append(" 		, ebit_margin AS [OperatingProfitMargin] ");
    queryBuilder.append(" 		, net_profit_after_mi_margin AS [PostTaxMargin] ");
    queryBuilder.append(" 		, liab_on_asset / NULLIF (equity_on_asset, 0)  AS [DebtOnEquity] ");
    queryBuilder.append(" 		, liab_on_asset AS [DebtOnAsset] ");
    queryBuilder.append(" 		, debt_on_ebitda AS [DebtOnEbitda] ");
    queryBuilder.append(" 		, st_on_lt_debt AS [ShortOnLongDebt] ");
    queryBuilder.append(" 		, interest_coverage AS [EbitOnInterest] ");
    queryBuilder.append(" 		, 1 / NULLIF (equity_on_asset, 0) AS [AssetOnEquity] ");
    queryBuilder.append(" 		, cash_on_equity AS [CashOnEquity] ");
    queryBuilder.append(" 		, cash_on_marcap AS [CashOnCapitalize] ");
    queryBuilder.append(" 		, current_ratio AS [CurrentPayment] ");
    queryBuilder.append(" 		, quick_ratio AS [QuickPayment] ");
    queryBuilder.append(" 		, receivable_days AS [DaysReceivable] ");
    queryBuilder.append(" 		, inventory_days AS [DaysInventory] ");
    queryBuilder.append(" 		, payable_days AS [DaysPayable] ");
    queryBuilder.append(" 		, cash_ratio AS [CashCirculation] ");
    queryBuilder.append(" 		, receivable_turnover AS [RevenueOnWorkCapital] ");
    queryBuilder.append(" 		, asset_turnover AS [RevenueOnAsset] ");
    queryBuilder.append(" 		, ebit_margin AS [EbitOnRevenue] ");
    queryBuilder.append(" 		, debt_on_ebitda AS [NetDebtOnEbitda] ");
    queryBuilder.append(" 		, debt_on_equity AS [NetDebtOnEquity] ");
    queryBuilder.append(" 		, ps AS [P/S] ");
    queryBuilder.append(" 	FROM tca_ratio_latest_industry ");
    queryBuilder.append(" WHERE IdLevel2 = :idLevel2  ");

    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("idLevel2", idLevel2)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return result.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      TcAnalysis.tcaDbConnection.closeSession();
    }
    return null;
  }

  @Basic
  @Column(name = "Ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "YearReport")
  public Short getYearReport() {
    return yearReport;
  }

  public void setYearReport(Short yearReport) {
    this.yearReport = yearReport;
  }

  @Basic
  @Column(name = "LengthReport")
  public Short getLengthReport() {
    return lengthReport;
  }

  public void setLengthReport(Short lengthReport) {
    this.lengthReport = lengthReport;
  }

  @Basic
  @Column(name = "Cash")
  public Double getCash() {
    return cash;
  }

  public void setCash(Double cash) {
    this.cash = cash;
  }

  @Basic
  @Column(name = "Liability")
  public Double getLiability() {
    return liability;
  }

  public void setLiability(Double liability) {
    this.liability = liability;
  }

  @Basic
  @Column(name = "Equity")
  public Double getEquity() {
    return equity;
  }

  public void setEquity(Double equity) {
    this.equity = equity;
  }

  @Basic
  @Column(name = "ShortDebt")
  public Double getShortDebt() {
    return shortDebt;
  }

  public void setShortDebt(Double shortDebt) {
    this.shortDebt = shortDebt;
  }

  @Basic
  @Column(name = "LongDebt")
  public Double getLongDebt() {
    return longDebt;
  }

  public void setLongDebt(Double longDebt) {
    this.longDebt = longDebt;
  }

  @Basic
  @Column(name = "FixedAsset")
  public Double getFixedAsset() {
    return fixedAsset;
  }

  public void setFixedAsset(Double fixedAsset) {
    this.fixedAsset = fixedAsset;
  }

  @Basic
  @Column(name = "Debt")
  public Double getDebt() {
    return debt;
  }

  public void setDebt(Double debt) {
    this.debt = debt;
  }

  @Basic
  @Column(name = "Asset")
  public Double getAsset() {
    return asset;
  }

  public void setAsset(Double asset) {
    this.asset = asset;
  }

  @Basic
  @Column(name = "ShortLiability")
  public Double getShortLiability() {
    return shortLiability;
  }

  public void setShortLiability(Double shortLiability) {
    this.shortLiability = shortLiability;
  }

  @Basic
  @Column(name = "CapitalBalance")
  public Double getCapitalBalance() {
    return capitalBalance;
  }

  public void setCapitalBalance(Double capitalBalance) {
    this.capitalBalance = capitalBalance;
  }

  @Basic
  @Column(name = "WorkCapital")
  public Double getWorkCapital() {
    return workCapital;
  }

  public void setWorkCapital(Double workCapital) {
    this.workCapital = workCapital;
  }

  @Basic
  @Column(name = "GrossProfit")
  public Double getGrossProfit() {
    return grossProfit;
  }

  public void setGrossProfit(Double grossProfit) {
    this.grossProfit = grossProfit;
  }

  @Basic
  @Column(name = "Revenue")
  public Double getRevenue() {
    return revenue;
  }

  public void setRevenue(Double revenue) {
    this.revenue = revenue;
  }

  @Basic
  @Column(name = "OperatingProfit")
  public Double getOperatingProfit() {
    return operatingProfit;
  }

  public void setOperatingProfit(Double operatingProfit) {
    this.operatingProfit = operatingProfit;
  }

  @Basic
  @Column(name = "PostTax")
  public Double getPostTax() {
    return postTax;
  }

  public void setPostTax(Double postTax) {
    this.postTax = postTax;
  }

  @Basic
  @Column(name = "PreTax")
  public Double getPreTax() {
    return preTax;
  }

  public void setPreTax(Double preTax) {
    this.preTax = preTax;
  }

  @Basic
  @Column(name = "InterestExpense")
  public Double getInterestExpense() {
    return interestExpense;
  }

  public void setInterestExpense(Double interestExpense) {
    this.interestExpense = interestExpense;
  }

  @Basic
  @Column(name = "Capex")
  public Double getCapex() {
    return capex;
  }

  public void setCapex(Double capex) {
    this.capex = capex;
  }

  @Basic
  @Column(name = "P/E")
  public Double getpE() {
    return pE;
  }

  public void setpE(Double pE) {
    this.pE = pE;
  }

  @Basic
  @Column(name = "P/B")
  public Double getpB() {
    return pB;
  }

  public void setpB(Double pB) {
    this.pB = pB;
  }

  @Basic
  @Column(name = "PublicStock")
  public Double getPublicStock() {
    return publicStock;
  }

  public void setPublicStock(Double publicStock) {
    this.publicStock = publicStock;
  }

  @Basic
  @Column(name = "ROE")
  public Double getRoe() {
    return roe;
  }

  public void setRoe(Double roe) {
    this.roe = roe;
  }

  @Basic
  @Column(name = "Capitalize")
  public Double getCapitalize() {
    return capitalize;
  }

  public void setCapitalize(Double capitalize) {
    this.capitalize = capitalize;
  }

  @Basic
  @Column(name = "EV/EBITDA")
  public Double getEvEbitda() {
    return evEbitda;
  }

  public void setEvEbitda(Double evEbitda) {
    this.evEbitda = evEbitda;
  }

  @Basic
  @Column(name = "CurrentPayment")
  public Double getCurrentPayment() {
    return currentPayment;
  }

  public void setCurrentPayment(Double currentPayment) {
    this.currentPayment = currentPayment;
  }

  @Basic
  @Column(name = "ROA")
  public Double getRoa() {
    return roa;
  }

  public void setRoa(Double roa) {
    this.roa = roa;
  }

  @Basic
  @Column(name = "DaysPayable")
  public Double getDaysPayable() {
    return daysPayable;
  }

  public void setDaysPayable(Double daysPayable) {
    this.daysPayable = daysPayable;
  }

  @Basic
  @Column(name = "EBITDA")
  public Double getEbitda() {
    return ebitda;
  }

  public void setEbitda(Double ebitda) {
    this.ebitda = ebitda;
  }

  @Basic
  @Column(name = "QuickPayment")
  public Double getQuickPayment() {
    return quickPayment;
  }

  public void setQuickPayment(Double quickPayment) {
    this.quickPayment = quickPayment;
  }

  @Basic
  @Column(name = "EPS")
  public Double getEps() {
    return eps;
  }

  public void setEps(Double eps) {
    this.eps = eps;
  }

  @Basic
  @Column(name = "BVPS")
  public Double getBvps() {
    return bvps;
  }

  public void setBvps(Double bvps) {
    this.bvps = bvps;
  }

  @Basic
  @Column(name = "Dividend")
  public Double getDividend() {
    return dividend;
  }

  public void setDividend(Double dividend) {
    this.dividend = dividend;
  }

  @Basic
  @Column(name = "EBIT")
  public Double getEbit() {
    return ebit;
  }

  public void setEbit(Double ebit) {
    this.ebit = ebit;
  }

  @Basic
  @Column(name = "DaysReceivable")
  public Double getDaysReceivable() {
    return daysReceivable;
  }

  public void setDaysReceivable(Double daysReceivable) {
    this.daysReceivable = daysReceivable;
  }

  @Basic
  @Column(name = "DaysInventory")
  public Double getDaysInventory() {
    return daysInventory;
  }

  public void setDaysInventory(Double daysInventory) {
    this.daysInventory = daysInventory;
  }

  @Basic
  @Column(name = "CashCirculation")
  public Double getCashCirculation() {
    return cashCirculation;
  }

  public void setCashCirculation(Double cashCirculation) {
    this.cashCirculation = cashCirculation;
  }

  @Basic
  @Column(name = "EbitdaOnStock")
  public Double getEbitdaOnStock() {
    return ebitdaOnStock;
  }

  public void setEbitdaOnStock(Double ebitdaOnStock) {
    this.ebitdaOnStock = ebitdaOnStock;
  }

  @Basic
  @Column(name = "EbitOnInterest")
  public Double getEbitOnInterest() {
    return ebitOnInterest;
  }

  public void setEbitOnInterest(Double ebitOnInterest) {
    this.ebitOnInterest = ebitOnInterest;
  }

  @Basic
  @Column(name = "RevenueOnWorkCapital")
  public Double getRevenueOnWorkCapital() {
    return revenueOnWorkCapital;
  }

  public void setRevenueOnWorkCapital(Double revenueOnWorkCapital) {
    this.revenueOnWorkCapital = revenueOnWorkCapital;
  }

  @Basic
  @Column(name = "RevenueOnAsset")
  public Double getRevenueOnAsset() {
    return revenueOnAsset;
  }

  public void setRevenueOnAsset(Double revenueOnAsset) {
    this.revenueOnAsset = revenueOnAsset;
  }

  @Basic
  @Column(name = "EbitOnRevenue")
  public Double getEbitOnRevenue() {
    return ebitOnRevenue;
  }

  public void setEbitOnRevenue(Double ebitOnRevenue) {
    this.ebitOnRevenue = ebitOnRevenue;
  }

  @Basic
  @Column(name = "PostTaxOnPreTax")
  public Double getPostTaxOnPreTax() {
    return postTaxOnPreTax;
  }

  public void setPostTaxOnPreTax(Double postTaxOnPreTax) {
    this.postTaxOnPreTax = postTaxOnPreTax;
  }

  @Basic
  @Column(name = "CapexOnFixedAsset")
  public Double getCapexOnFixedAsset() {
    return capexOnFixedAsset;
  }

  public void setCapexOnFixedAsset(Double capexOnFixedAsset) {
    this.capexOnFixedAsset = capexOnFixedAsset;
  }

  @Basic
  @Column(name = "GrossProfitMargin")
  public Double getGrossProfitMargin() {
    return grossProfitMargin;
  }

  public void setGrossProfitMargin(Double grossProfitMargin) {
    this.grossProfitMargin = grossProfitMargin;
  }

  @Basic
  @Column(name = "OperatingProfitMargin")
  public Double getOperatingProfitMargin() {
    return operatingProfitMargin;
  }

  public void setOperatingProfitMargin(Double operatingProfitMargin) {
    this.operatingProfitMargin = operatingProfitMargin;
  }

  @Basic
  @Column(name = "PostTaxMargin")
  public Double getPostTaxMargin() {
    return postTaxMargin;
  }

  public void setPostTaxMargin(Double postTaxMargin) {
    this.postTaxMargin = postTaxMargin;
  }

  @Basic
  @Column(name = "DebtOnEbitda")
  public Double getDebtOnEbitda() {
    return debtOnEbitda;
  }

  public void setDebtOnEbitda(Double debtOnEbitda) {
    this.debtOnEbitda = debtOnEbitda;
  }

  @Basic
  @Column(name = "PreTaxOnEbit")
  public Double getPreTaxOnEbit() {
    return preTaxOnEbit;
  }

  public void setPreTaxOnEbit(Double preTaxOnEbit) {
    this.preTaxOnEbit = preTaxOnEbit;
  }

  @Basic
  @Column(name = "DebtOnEquity")
  public Double getDebtOnEquity() {
    return debtOnEquity;
  }

  public void setDebtOnEquity(Double debtOnEquity) {
    this.debtOnEquity = debtOnEquity;
  }

  @Basic
  @Column(name = "DebtOnAsset")
  public Double getDebtOnAsset() {
    return debtOnAsset;
  }

  public void setDebtOnAsset(Double debtOnAsset) {
    this.debtOnAsset = debtOnAsset;
  }

  @Basic
  @Column(name = "ShortOnLongDebt")
  public Double getShortOnLongDebt() {
    return shortOnLongDebt;
  }

  public void setShortOnLongDebt(Double shortOnLongDebt) {
    this.shortOnLongDebt = shortOnLongDebt;
  }

  @Basic
  @Column(name = "AssetOnEquity")
  public Double getAssetOnEquity() {
    return assetOnEquity;
  }

  public void setAssetOnEquity(Double assetOnEquity) {
    this.assetOnEquity = assetOnEquity;
  }

  @Basic
  @Column(name = "CashOnEquity")
  public Double getCashOnEquity() {
    return cashOnEquity;
  }

  public void setCashOnEquity(Double cashOnEquity) {
    this.cashOnEquity = cashOnEquity;
  }

  @Basic
  @Column(name = "CashOnCapitalize")
  public Double getCashOnCapitalize() {
    return cashOnCapitalize;
  }

  public void setCashOnCapitalize(Double cashOnCapitalize) {
    this.cashOnCapitalize = cashOnCapitalize;
  }

  @Basic
  @Column(name = "EquityOnTotalAsset")
  public Double getEquityOnTotalAsset() {
    return equityOnTotalAsset;
  }

  public void setEquityOnTotalAsset(Double equityOnTotalAsset) {
    this.equityOnTotalAsset = equityOnTotalAsset;
  }

  @Basic
  @Column(name = "PayableOnEquity")
  public Double getPayableOnEquity() {
    return payableOnEquity;
  }

  public void setPayableOnEquity(Double payableOnEquity) {
    this.payableOnEquity = payableOnEquity;
  }

  @Basic
  @Column(name = "EpsChange")
  public Double getEpsChange() {
    return epsChange;
  }

  public void setEpsChange(Double epsChange) {
    this.epsChange = epsChange;
  }

  @Basic
  @Column(name = "EbitdaOnStockChange")
  public Double getEbitdaOnStockChange() {
    return ebitdaOnStockChange;
  }

  public void setEbitdaOnStockChange(Double ebitdaOnStockChange) {
    this.ebitdaOnStockChange = ebitdaOnStockChange;
  }

  @Basic
  @Column(name = "BookValuePerShare")
  public Double getBookValuePerShare() {
    return bookValuePerShare;
  }

  public void setBookValuePerShare(Double bookValuePerShare) {
    this.bookValuePerShare = bookValuePerShare;
  }


  @Basic
  @Column(name = "PriceToSales")
  public Double getPriceToSales() {
    return priceToSales;
  }

  public void setPriceToSales(Double priceToSales) {
    this.priceToSales = priceToSales;
  }

  @Basic
  @Column(name = "NetDebtOnEbitda")
  public Double getNetDebtOnEbitda() {
    return netDebtOnEbitda;
  }

  public void setNetDebtOnEbitda(Double netDebtOnEbitda) {
    this.netDebtOnEbitda = netDebtOnEbitda;
  }

  @Basic
  @Column(name = "NetDebtOnEquity")
  public Double getNetDebtOnEquity() {
    return netDebtOnEquity;
  }

  public void setNetDebtOnEquity(Double netDebtOnEquity) {
    this.netDebtOnEquity = netDebtOnEquity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TblIdataFinanceRatioComp that = (TblIdataFinanceRatioComp) o;
    return Objects.equals(ticker, that.ticker) &&
      Objects.equals(yearReport, that.yearReport) &&
      Objects.equals(lengthReport, that.lengthReport) &&
      Objects.equals(cash, that.cash) &&
      Objects.equals(liability, that.liability) &&
      Objects.equals(equity, that.equity) &&
      Objects.equals(shortDebt, that.shortDebt) &&
      Objects.equals(longDebt, that.longDebt) &&
      Objects.equals(fixedAsset, that.fixedAsset) &&
      Objects.equals(debt, that.debt) &&
      Objects.equals(asset, that.asset) &&
      Objects.equals(shortLiability, that.shortLiability) &&
      Objects.equals(capitalBalance, that.capitalBalance) &&
      Objects.equals(workCapital, that.workCapital) &&
      Objects.equals(grossProfit, that.grossProfit) &&
      Objects.equals(revenue, that.revenue) &&
      Objects.equals(operatingProfit, that.operatingProfit) &&
      Objects.equals(postTax, that.postTax) &&
      Objects.equals(preTax, that.preTax) &&
      Objects.equals(interestExpense, that.interestExpense) &&
      Objects.equals(capex, that.capex) &&
      Objects.equals(pE, that.pE) &&
      Objects.equals(pB, that.pB) &&
      Objects.equals(publicStock, that.publicStock) &&
      Objects.equals(roe, that.roe) &&
      Objects.equals(capitalize, that.capitalize) &&
      Objects.equals(evEbitda, that.evEbitda) &&
      Objects.equals(currentPayment, that.currentPayment) &&
      Objects.equals(roa, that.roa) &&
      Objects.equals(daysPayable, that.daysPayable) &&
      Objects.equals(ebitda, that.ebitda) &&
      Objects.equals(quickPayment, that.quickPayment) &&
      Objects.equals(eps, that.eps) &&
      Objects.equals(bvps, that.bvps) &&
      Objects.equals(dividend, that.dividend) &&
      Objects.equals(ebit, that.ebit) &&
      Objects.equals(daysReceivable, that.daysReceivable) &&
      Objects.equals(daysInventory, that.daysInventory) &&
      Objects.equals(cashCirculation, that.cashCirculation) &&
      Objects.equals(ebitdaOnStock, that.ebitdaOnStock) &&
      Objects.equals(ebitOnInterest, that.ebitOnInterest) &&
      Objects.equals(revenueOnWorkCapital, that.revenueOnWorkCapital) &&
      Objects.equals(revenueOnAsset, that.revenueOnAsset) &&
      Objects.equals(ebitOnRevenue, that.ebitOnRevenue) &&
      Objects.equals(postTaxOnPreTax, that.postTaxOnPreTax) &&
      Objects.equals(capexOnFixedAsset, that.capexOnFixedAsset) &&
      Objects.equals(grossProfitMargin, that.grossProfitMargin) &&
      Objects.equals(operatingProfitMargin, that.operatingProfitMargin) &&
      Objects.equals(postTaxMargin, that.postTaxMargin) &&
      Objects.equals(debtOnEbitda, that.debtOnEbitda) &&
      Objects.equals(preTaxOnEbit, that.preTaxOnEbit) &&
      Objects.equals(debtOnEquity, that.debtOnEquity) &&
      Objects.equals(debtOnAsset, that.debtOnAsset) &&
      Objects.equals(shortOnLongDebt, that.shortOnLongDebt) &&
      Objects.equals(assetOnEquity, that.assetOnEquity) &&
      Objects.equals(cashOnEquity, that.cashOnEquity) &&
      Objects.equals(cashOnCapitalize, that.cashOnCapitalize) &&
      Objects.equals(equityOnTotalAsset, that.equityOnTotalAsset) &&
      Objects.equals(payableOnEquity, that.payableOnEquity) &&
      Objects.equals(epsChange, that.epsChange) &&
      Objects.equals(ebitdaOnStockChange, that.ebitdaOnStockChange) &&
      Objects.equals(bookValuePerShare, that.bookValuePerShare) &&
      Objects.equals(priceToSales, that.priceToSales) &&
      Objects.equals(netDebtOnEbitda, that.netDebtOnEbitda) &&
      Objects.equals(netDebtOnEquity, that.netDebtOnEquity);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, yearReport, lengthReport, cash, liability, equity, shortDebt, longDebt, fixedAsset, debt, asset, shortLiability, capitalBalance, workCapital, grossProfit, revenue,
      operatingProfit, postTax, preTax, interestExpense, capex, pE, pB, publicStock, roe, capitalize, evEbitda, currentPayment, roa, daysPayable, ebitda, quickPayment, eps, bvps, dividend, ebit,
      daysReceivable, daysInventory, cashCirculation, ebitdaOnStock, ebitOnInterest, revenueOnWorkCapital, revenueOnAsset, ebitOnRevenue, postTaxOnPreTax, capexOnFixedAsset, grossProfitMargin,
      operatingProfitMargin, postTaxMargin, debtOnEbitda, preTaxOnEbit, debtOnEquity, debtOnAsset, shortOnLongDebt, assetOnEquity, cashOnEquity, cashOnCapitalize, equityOnTotalAsset, payableOnEquity,
      epsChange, ebitdaOnStockChange, bookValuePerShare, priceToSales, netDebtOnEbitda, netDebtOnEquity);
  }
}
