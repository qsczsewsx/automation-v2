package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
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
public class TblIdataFinanceRatioBank {

  private String ticker;
  private Short yearReport;
  private Short lengthReport;
  private Double loan;
  private Double asset;
  private Double equity;
  private Double credit;
  private Double provision;
  private Double liability;
  private Double liquidAsset;
  private Double badDebt;
  private Double preProvision;
  private Double toi;
  private Double postTax;
  private Double provisionExpense;
  private Double pE;
  private Double pB;
  private Double earnAsset;
  private Double roe;
  private Double capitalize;
  private Double nim;
  private Double cof;
  private Double roa;
  private Double npl;
  private Double nonInterestOnToi;
  private Double quityOnLoan;
  private Double cti;
  private Double proNpl;
  private Double eps;
  private Double bvps;
  private Double dividend;
  private Double ebitda;
  private Double preProvisionOnToi;
  private Double postTaxOnToi;
  private Double cancelDebt;
  private Double assetOnEquity;
  private Double loanOnEarnAsset;
  private Double loanOnAsset;
  private Double loanOnDeposit;
  private Double depositOnEarnAsset;
  private Double badDebtOnAsset;
  private Double liquidityOnLiability;
  private Double equityOnTotalAsset;
  private Double payableOnEquity;
  private Double epsChange;
  private Double bookValuePerShare;
  private Double creditGrowth;
  // For rating
  private Double priceToSales;

  public static Map<String, Object> getAvgIndustry(String idLevel2) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" 	SELECT cost_of_financing AS [CostOfFinancing] ");
    queryBuilder.append("		, nim AS [InterestMargin] ");
    queryBuilder.append("		, 1 - nii_on_toi AS NonInterestOnToi  ");
    queryBuilder.append("		, cir AS CostToIncome  ");
    queryBuilder.append("		, preprovision_profit_on_toi AS PreProvisionOnToi  ");
    queryBuilder.append("		, net_profit_on_toi AS PostTaxOnToi  ");
    queryBuilder.append("		, deposit_on_earning_assets AS DepositOnEarnAsset  ");
    queryBuilder.append("		, cancelled_debt AS CancelDebt  ");
    queryBuilder.append("		, npl_ratio AS BadDebtPercentage  ");
    queryBuilder.append("		, reserve_on_npl AS ProvisionOnBadDebt  ");
    queryBuilder.append("		, ldr AS LoanOnDeposit  ");
    queryBuilder.append("		, equity_on_asset AS EquityOnTotalAsset  ");
    queryBuilder.append("		, npl_on_asset AS BadDebtOnAsset  ");
    queryBuilder.append(" 	FROM tca_ratio_latest_industry");
    queryBuilder.append(" 	WHERE IdLevel2 = :idLevel2 ");

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
  @Column(name = "Loan")
  public Double getLoan() {
    return loan;
  }

  public void setLoan(Double loan) {
    this.loan = loan;
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
  @Column(name = "Equity")
  public Double getEquity() {
    return equity;
  }

  public void setEquity(Double equity) {
    this.equity = equity;
  }

  @Basic
  @Column(name = "Credit")
  public Double getCredit() {
    return credit;
  }

  public void setCredit(Double credit) {
    this.credit = credit;
  }

  @Basic
  @Column(name = "Provision")
  public Double getProvision() {
    return provision;
  }

  public void setProvision(Double provision) {
    this.provision = provision;
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
  @Column(name = "LiquidAsset")
  public Double getLiquidAsset() {
    return liquidAsset;
  }

  public void setLiquidAsset(Double liquidAsset) {
    this.liquidAsset = liquidAsset;
  }

  @Basic
  @Column(name = "BadDebt")
  public Double getBadDebt() {
    return badDebt;
  }

  public void setBadDebt(Double badDebt) {
    this.badDebt = badDebt;
  }

  @Basic
  @Column(name = "PreProvision")
  public Double getPreProvision() {
    return preProvision;
  }

  public void setPreProvision(Double preProvision) {
    this.preProvision = preProvision;
  }

  @Basic
  @Column(name = "TOI")
  public Double getToi() {
    return toi;
  }

  public void setToi(Double toi) {
    this.toi = toi;
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
  @Column(name = "ProvisionExpense")
  public Double getProvisionExpense() {
    return provisionExpense;
  }

  public void setProvisionExpense(Double provisionExpense) {
    this.provisionExpense = provisionExpense;
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
  @Column(name = "EarnAsset")
  public Double getEarnAsset() {
    return earnAsset;
  }

  public void setEarnAsset(Double earnAsset) {
    this.earnAsset = earnAsset;
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
  @Column(name = "NIM")
  public Double getNim() {
    return nim;
  }

  public void setNim(Double nim) {
    this.nim = nim;
  }

  @Basic
  @Column(name = "COF")
  public Double getCof() {
    return cof;
  }

  public void setCof(Double cof) {
    this.cof = cof;
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
  @Column(name = "NPL")
  public Double getNpl() {
    return npl;
  }

  public void setNpl(Double npl) {
    this.npl = npl;
  }

  @Basic
  @Column(name = "NonInterestOnToi")
  public Double getNonInterestOnToi() {
    return nonInterestOnToi;
  }

  public void setNonInterestOnToi(Double nonInterestOnToi) {
    this.nonInterestOnToi = nonInterestOnToi;
  }

  @Basic
  @Column(name = "QuityOnLoan")
  public Double getQuityOnLoan() {
    return quityOnLoan;
  }

  public void setQuityOnLoan(Double quityOnLoan) {
    this.quityOnLoan = quityOnLoan;
  }

  @Basic
  @Column(name = "CTI")
  public Double getCti() {
    return cti;
  }

  public void setCti(Double cti) {
    this.cti = cti;
  }

  @Basic
  @Column(name = "Pro/NPL")
  public Double getProNpl() {
    return proNpl;
  }

  public void setProNpl(Double proNpl) {
    this.proNpl = proNpl;
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
  @Column(name = "EBITDA")
  public Double getEbitda() {
    return ebitda;
  }

  public void setEbitda(Double ebitda) {
    this.ebitda = ebitda;
  }

  @Basic
  @Column(name = "PreProvisionOnToi")
  public Double getPreProvisionOnToi() {
    return preProvisionOnToi;
  }

  public void setPreProvisionOnToi(Double preProvisionOnToi) {
    this.preProvisionOnToi = preProvisionOnToi;
  }

  @Basic
  @Column(name = "PostTaxOnToi")
  public Double getPostTaxOnToi() {
    return postTaxOnToi;
  }

  public void setPostTaxOnToi(Double postTaxOnToi) {
    this.postTaxOnToi = postTaxOnToi;
  }

  @Basic
  @Column(name = "CancelDebt")
  public Double getCancelDebt() {
    return cancelDebt;
  }

  public void setCancelDebt(Double cancelDebt) {
    this.cancelDebt = cancelDebt;
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
  @Column(name = "LoanOnEarnAsset")
  public Double getLoanOnEarnAsset() {
    return loanOnEarnAsset;
  }

  public void setLoanOnEarnAsset(Double loanOnEarnAsset) {
    this.loanOnEarnAsset = loanOnEarnAsset;
  }

  @Basic
  @Column(name = "LoanOnAsset")
  public Double getLoanOnAsset() {
    return loanOnAsset;
  }

  public void setLoanOnAsset(Double loanOnAsset) {
    this.loanOnAsset = loanOnAsset;
  }

  @Basic
  @Column(name = "LoanOnDeposit")
  public Double getLoanOnDeposit() {
    return loanOnDeposit;
  }

  public void setLoanOnDeposit(Double loanOnDeposit) {
    this.loanOnDeposit = loanOnDeposit;
  }

  @Basic
  @Column(name = "DepositOnEarnAsset")
  public Double getDepositOnEarnAsset() {
    return depositOnEarnAsset;
  }

  public void setDepositOnEarnAsset(Double depositOnEarnAsset) {
    this.depositOnEarnAsset = depositOnEarnAsset;
  }

  @Basic
  @Column(name = "BadDebtOnAsset")
  public Double getBadDebtOnAsset() {
    return badDebtOnAsset;
  }

  public void setBadDebtOnAsset(Double badDebtOnAsset) {
    this.badDebtOnAsset = badDebtOnAsset;
  }

  @Basic
  @Column(name = "LiquidityOnLiability")
  public Double getLiquidityOnLiability() {
    return liquidityOnLiability;
  }

  public void setLiquidityOnLiability(Double liquidityOnLiability) {
    this.liquidityOnLiability = liquidityOnLiability;
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
  @Column(name = "BookValuePerShare")
  public Double getBookValuePerShare() {
    return bookValuePerShare;
  }

  public void setBookValuePerShare(Double bookValuePerShare) {
    this.bookValuePerShare = bookValuePerShare;
  }

  @Basic
  @Column(name = "CreditGrowth")
  public Double getCreditGrowth() {
    return creditGrowth;
  }

  public void setCreditGrowth(Double creditGrowth) {
    this.creditGrowth = creditGrowth;
  }

  @Basic
  @Column(name = "P/S")
  public Double getPriceToSales() {
    return priceToSales;
  }

  public void setPriceToSales(Double priceToSales) {
    this.priceToSales = priceToSales;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TblIdataFinanceRatioBank that = (TblIdataFinanceRatioBank) o;
    return Objects.equals(ticker, that.ticker) &&
      Objects.equals(yearReport, that.yearReport) &&
      Objects.equals(lengthReport, that.lengthReport) &&
      Objects.equals(loan, that.loan) &&
      Objects.equals(asset, that.asset) &&
      Objects.equals(equity, that.equity) &&
      Objects.equals(credit, that.credit) &&
      Objects.equals(provision, that.provision) &&
      Objects.equals(liability, that.liability) &&
      Objects.equals(liquidAsset, that.liquidAsset) &&
      Objects.equals(badDebt, that.badDebt) &&
      Objects.equals(preProvision, that.preProvision) &&
      Objects.equals(toi, that.toi) &&
      Objects.equals(postTax, that.postTax) &&
      Objects.equals(provisionExpense, that.provisionExpense) &&
      Objects.equals(pE, that.pE) &&
      Objects.equals(pB, that.pB) &&
      Objects.equals(earnAsset, that.earnAsset) &&
      Objects.equals(roe, that.roe) &&
      Objects.equals(capitalize, that.capitalize) &&
      Objects.equals(nim, that.nim) &&
      Objects.equals(cof, that.cof) &&
      Objects.equals(roa, that.roa) &&
      Objects.equals(npl, that.npl) &&
      Objects.equals(nonInterestOnToi, that.nonInterestOnToi) &&
      Objects.equals(quityOnLoan, that.quityOnLoan) &&
      Objects.equals(cti, that.cti) &&
      Objects.equals(proNpl, that.proNpl) &&
      Objects.equals(eps, that.eps) &&
      Objects.equals(bvps, that.bvps) &&
      Objects.equals(dividend, that.dividend) &&
      Objects.equals(ebitda, that.ebitda) &&
      Objects.equals(preProvisionOnToi, that.preProvisionOnToi) &&
      Objects.equals(postTaxOnToi, that.postTaxOnToi) &&
      Objects.equals(cancelDebt, that.cancelDebt) &&
      Objects.equals(assetOnEquity, that.assetOnEquity) &&
      Objects.equals(loanOnEarnAsset, that.loanOnEarnAsset) &&
      Objects.equals(loanOnAsset, that.loanOnAsset) &&
      Objects.equals(loanOnDeposit, that.loanOnDeposit) &&
      Objects.equals(depositOnEarnAsset, that.depositOnEarnAsset) &&
      Objects.equals(badDebtOnAsset, that.badDebtOnAsset) &&
      Objects.equals(liquidityOnLiability, that.liquidityOnLiability) &&
      Objects.equals(equityOnTotalAsset, that.equityOnTotalAsset) &&
      Objects.equals(payableOnEquity, that.payableOnEquity) &&
      Objects.equals(epsChange, that.epsChange) &&
      Objects.equals(bookValuePerShare, that.bookValuePerShare) &&
      Objects.equals(creditGrowth, that.creditGrowth) &&
      Objects.equals(priceToSales, that.priceToSales);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, yearReport, lengthReport, loan, asset, equity, credit, provision, liability, liquidAsset, badDebt, preProvision, toi, postTax, provisionExpense, pE, pB, earnAsset, roe,
      capitalize, nim, cof, roa, npl, nonInterestOnToi, quityOnLoan, cti, proNpl, eps, bvps, dividend, ebitda, preProvisionOnToi, postTaxOnToi, cancelDebt, assetOnEquity, loanOnEarnAsset, loanOnAsset,
      loanOnDeposit, depositOnEarnAsset, badDebtOnAsset, liquidityOnLiability, equityOnTotalAsset, payableOnEquity, epsChange, bookValuePerShare, creditGrowth, priceToSales);
  }
}
