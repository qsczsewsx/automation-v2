package com.tcbs.automation.tca.ticker;

import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.stoxplus.stock.FormatUtils;

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

import static com.tcbs.automation.stoxplus.stock.EntityUtils.toMapUseQuarterAndYear;
import static com.tcbs.automation.stoxplus.stock.FormatUtils.formatNumber;
import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseDouble;

@Getter
@RequiredArgsConstructor
@Setter
public class QuarterFinancialInfo {
  private String ticker;
  private Long sale;
  private Long netIncome;
  private Long quarter;
  private Long year;
  private Double payableOnEquity;
  private Double payableOnEbitda;
  private Long operatingProfit;
  @NumberFormat
  private Double badDebtPercentage;
  @NumberFormat
  private Double profitMargin;
  private Double ebitda;
  private Double debt;

  @Step
  public List<QuarterFinancialInfo> getByStockCode(String stockCode) {
    try {
      if (TickerBasic.getTickerBasic(stockCode).getComTypeCode().equals("NH")) {
        return (getFromBank(stockCode));
      } else {
        return (getFromCompany(stockCode));
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public List<QuarterFinancialInfo> getFromBank(String stockCode) {
    String query = "SELECT  i.Ticker,"
                  + "   i.YearReport, "
                  + "   i.LengthReport,"
                  + "   i.OperatingProfit,"
                  + "   i.NetIncome ,"
                  + "   cf.NIM,"
                  + "   cf.NPL"
                  + " FROM ( "
                  + "   SELECT top(10) NetIncome,  Ticker, OperatingProfit, LengthReport , YearReport "
                  + "   FROM ("
                  + "     SELECT ticker, YearReport, LengthReport"
                  + "       , cast(ISB38 as float)/1000000000 as [OperatingProfit]"
                  + "       , cast(ISA22 as float)/1000000000 as [NetIncome]"
                  + "       , row_number() over (partition by Ticker, YearReport, LengthReport  order by updatedate desc) as rn"
                  + "     FROM stx_fsc_IncomeStatement"
                  + "     WHERE lengthreport < 5"
                  + "       AND ticker =  :stockCode "
                  + "   ) i "
                  + "   WHERE i.rn = 1 order by YearReport desc, LengthReport desc"
                  + " ) i"
                  + " LEFT JOIN ("
                  + "   SELECT top(14)  NIM, npl_ratio AS NPL, Ticker, LengthReport , YearReport "
                  + "   FROM tcdata_ratio_bank "
                  + "   WHERE ticker =  :stockCode"
                  + "   ORDER BY YearReport desc , lengthreport desc"
                  + " ) cf "
                  + " ON	i.lengthreport = cf.lengthreport"
                  + "   AND i.yearreport = cf.yearreport ";
    List<Map<String, Object>> result = new ArrayList<>();
    List<QuarterFinancialInfo> fromDb = new ArrayList<>();

    try {
      result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query).setParameter("stockCode", stockCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      for (Map<String, Object> quarterFinance : result) {
        QuarterFinancialInfo info = new QuarterFinancialInfo();
        info.setTicker(quarterFinance.get("Ticker").toString());
        info.setNetIncome((quarterFinance.get("NetIncome") == null) ? null
          : (long) (Double.parseDouble(quarterFinance.get("NetIncome").toString())));
        info.setOperatingProfit((quarterFinance.get("OperatingProfit") == null) ? null
          : (long) (Double.parseDouble(quarterFinance.get("OperatingProfit").toString())));
        info.setQuarter((quarterFinance.get("LengthReport") == null) ? null
          : Long.parseLong(quarterFinance.get("LengthReport").toString()));
        info.setYear((quarterFinance.get("YearReport") == null) ? null
          : Long.parseLong(quarterFinance.get("YearReport").toString()));
        info.setBadDebtPercentage(
          (quarterFinance.get("NPL") == null) ? null : (Double.parseDouble(quarterFinance.get("NPL").toString())));
        info.setProfitMargin(
          (quarterFinance.get("NIM") == null) ? null : (Double.parseDouble(quarterFinance.get("NIM").toString())));
        fromDb.add((QuarterFinancialInfo) formatNumber(info));
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return fromDb;
  }

  public List<QuarterFinancialInfo> getFromCompany(String stockCode) {
    String query = "SELECT i.ticker,"
                  + "     i.yearreport, "
                  + "     i.lengthreport,"
                  + "     i.sales,"
                  + "     i.netincome,"
                  + "     bs.Debt - bs.Cash as NetDebt,"
                  + "     bs.equity,"
                  + "     bs.debt,"
                  + "     i.GrossProfit + i.SellingExpense + i.GAExpense + cf.Depreciation as [EBITDA]"
                  + " FROM"
                  + " ( "
                  + "   SELECT top(14) * "
                  + "   FROM"
                  + "   ("
                  + "     SELECT ticker, YearReport, LengthReport"
                  + "       , cast(ISA3 as float)/1000000000 as [Sales]"
                  + "       , cast(ISA5 as float)/1000000000 as [GrossProfit]"
                  + "       , cast(ISA22 as float)/1000000000 as [NetIncome]"
                  + "       , cast(ISA8 as float)/1000000000 as [InterestExpense]"
                  + "       , cast(ISA9 as float)/1000000000 as [SellingExpense]"
                  + "       , cast(ISA10 as float)/1000000000 as [GAExpense]"
                  + "       , cast(ISA16 as float)/1000000000 as [preTax]"
                  + "       , row_number() over (partition by Ticker, YearReport , lengthreport order by UpdateDATE desc) as rn"
                  + "     FROM stx_fsc_IncomeStatement"
                  + "     WHERE ticker =  :stockCode and lengthreport < 5"
                  + "   ) i"
                  + "   WHERE i.rn = 1 "
                  + "   ORDER BY YearReport desc, LengthReport desc"
                  + " ) i"
                  + " LEFT   JOIN"
                  + " ( "
                  + "   SELECT top(14) * "
                  + "   FROM "
                  + "   (   "
                  + "     SELECT ticker, YearReport, LengthReport"
                  + "       , cast(BSA2 as float) / 1000000000 as[Cash]"
                  + "           , cast(BSA54 as float) / 1000000000 as [Liability]"
                  + "             , cast(BSA78 as float) / 1000000000 as [Equity]"
                  + "             , cast(BSA53 as float) / 1000000000 as Asset"
                  + "             , (cast(BSA56 as float) + cast(BSA71 as float)) / 1000000000 as [Debt]"
                  + "       , row_number() over (partition by Ticker, YearReport , lengthreport order by  UpdateDate desc) as rn"
                  + "     FROM stx_fsc_BalanceSheet"
                  + "     WHERE lengthreport < 5 and ticker =  :stockCode"
                  + "   ) bs    "
                  + "   WHERE bs.rn = 1 "
                  + "   ORDER BY YearReport desc, LengthReport desc"
                  + " ) bs "
                  + " ON i.yearreport = bs.yearreport and i.lengthreport = bs.lengthreport"
                  + " LEFT JOIN     "
                  + "       ( "
                  + "   SELECT top(14) * "
                  + "   FROM "
                  + "   ("
                  + "     SELECT	Ticker, YearReport, LengthReport, cast(CFA2 as float)/1000000000 as [Depreciation],"
                  + "       row_number() over (partition by Ticker, YearReport , lengthreport order by  UpdateDate desc) as rn"
                  + "     FROM	STX_FSC_CASHFLOW"
                  + "     WHERE lengthreport < 5 and ticker = :stockCode"
                  + "   ) cf"
                  + "   WHERE cf.rn = 1 "
                  + "   ORDER BY YearReport desc, LengthReport desc"
                  + " ) cf "
                  + " ON i.yearreport = cf.yearreport and i.lengthreport = cf.lengthreport"
                  + " ORDER BY i.YearReport desc, i.LengthReport desc";
    List<Map<String, Object>> result = new ArrayList<>();
    List<QuarterFinancialInfo> fromDb = new ArrayList<>();

    try {
      result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query).setParameter("stockCode", stockCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      for (Map<String, Object> quarterFinance : result) {
        QuarterFinancialInfo info = new QuarterFinancialInfo();
        info.setTicker(quarterFinance.get("ticker").toString());
        info.setNetIncome((quarterFinance.get("netincome") == null) ? null
          : ((long) Double.parseDouble(quarterFinance.get("netincome").toString())));
        info.setSale((quarterFinance.get("sales") == null) ? null
          : (long) (Double.parseDouble(quarterFinance.get("sales").toString())));
        Double payableOnEquity = calculatePayableOnEquity(Double.parseDouble(quarterFinance.get("NetDebt").toString()),
          0d,
          Double.parseDouble(quarterFinance.get("equity").toString()));
        info.setDebt(parseDouble(quarterFinance.get("debt")));
        info.setPayableOnEquity((payableOnEquity == null) ? null : Double.parseDouble(payableOnEquity.toString()));
        info.setEbitda(parseDouble(quarterFinance.get("EBITDA")));
        info.setQuarter((quarterFinance.get("lengthreport") == null) ? null
          : Long.parseLong(quarterFinance.get("lengthreport").toString()));
        info.setYear((quarterFinance.get("yearreport") == null) ? null
          : Long.parseLong(quarterFinance.get("yearreport").toString()));
        fromDb.add(info);
      }
      if (fromDb == null || fromDb.size() == 0) {
        return fromDb;
      }
      Map<Long, Object> mapInfo = toMapUseQuarterAndYear(fromDb);
      for (QuarterFinancialInfo info : fromDb) {
        Double payableOnEbitda = calculateDebtOnEbitda(info, mapInfo);
        info.setPayableOnEbitda(payableOnEbitda);
        info = (QuarterFinancialInfo) FormatUtils.formatNumber(info);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    // return fromDb.subList(0, fromDb.size() - 4);
    return new ArrayList<>(fromDb.subList(0, (fromDb.size() <= 4 ?
      fromDb.size() : fromDb.size() - 4)));
  }

  public Double calculatePayableOnEquity(Double debt, Double cash, Double equity) {

    return (equity != 0) ? ((debt - cash) / (equity)) : (null);
  }

  public Double calculateEbitda(Double gross, Double sellExpense, Double gaExpense, Double depreciation) {
    try {
      return gross + sellExpense + gaExpense + depreciation;
    } catch (Exception e) {
      return null;
    }
  }

  public Double calculateDebtOnEbitda(QuarterFinancialInfo info, Map<Long, Object> mapInfo) {
    try {
      QuarterFinancialInfo sameQuarterLastYear = (QuarterFinancialInfo) mapInfo
        .get(info.getQuarter() * (info.getYear() - 1));
      return ((info.getDebt() + sameQuarterLastYear.getDebt()) / 2) / cumulateEbitda(info, mapInfo, (long) 4);
    } catch (Exception e) {
      return null;
    }

  }

  public Double cumulateEbitda(QuarterFinancialInfo fr, Map<Long, Object> map, Long count) {
    try {
      if (count == 1) {
        return fr.getEbitda();
      }
      Long quarter = (fr.getQuarter() == 1) ? (4) : (fr.getQuarter() - 1);
      Long year = (fr.getQuarter() == 1) ? (fr.getYear() - 1) : (fr.getYear());
      return fr.getEbitda() + cumulateEbitda((QuarterFinancialInfo) map.get(quarter * year), map, count - 1);
    } catch (Exception e) {
      return null;
    }
  }

}