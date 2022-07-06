package com.tcbs.automation.tca.rating;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingDetail {

  @Id
  @Column(name = "Ticker")
  private String ticker;

  @Column(name = "marcap")
  @Nullable
  private Double marcap;

  @Column(name = "price")
  @Nullable
  private Double price;

  @Column(name = "Numberofdays")
  @Nullable
  private Double numberOfDays;

  @Column(name = "pe")
  private Double priceToEarning;

  @Column(name = "peg")
  private Double peg;

  @Column(name = "pb")
  private Double priceToBook;

  @Column(name = "ev_on_ebitda")
  private Double valueBeforeEbitda;

  @Column(name = "dividend")
  @NumberFormat
  private Double dividend;

  @Column(name = "roe")
  @NumberFormat
  private Double roe;

  @Column(name = "roa")
  @NumberFormat
  private Double roa;

  @Column(name = "interest_coverage")
  private Double ebitOnInterest;

  @Column(name = "nim")
  @NumberFormat
  private Double interestMargin;

  @Column(name = "npl_ratio")
  @NumberFormat
  private Double badDebtPercentage;

  @Column(name = "current_ratio")
  private Double currentPayment;

  @Column(name = "quick_ratio")
  private Double quickPayment;

  @Column(name = "gross_profit_margin")
  @NumberFormat
  private Double grossProfitMargin;

  @Column(name = "net_profit_after_mi_margin")
  @NumberFormat
  private Double postTaxMargin;

  @Column(name = "debt_on_equity")
  private Double debtOnEquity;

  @Column(name = "income5year")
  @NumberFormat
  private Double income5year;

  @Column(name = "sale5year")
  @NumberFormat
  private Double sale5year;

  @Column(name = "income1quarter")
  @NumberFormat
  private Double income1quarter;

  @Column(name = "sale1quarter")
  @NumberFormat
  private Double sale1quarter;

  @Column(name = "nextIncome")
  @NumberFormat
  private Double nextIncome;

  @Column(name = "nextSale")
  @NumberFormat
  private Double nextSale;

  @Column(name = "RSI")
  private Double rsi;

  @Column(name = "TC_RS")
  private Double rs;


  public static List<RatingDetail> getRatingDetail(String tickers, String fType) {
    switch(fType) {
      case "TICKER":
        return getByTicker(tickers);
      case "TOP":
        return getTop200();
      case "INDUSTRY":
        return getByIndustry(tickers);
      case "INDUSTRIES":
        return getSameIndustry(tickers);
      case "TICKERS":
        return getByListTicker(tickers);
      default:
        return new ArrayList<>();
    }
  }


  public static List<RatingDetail> getByTicker(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  WITH UP_DOWN AS ( ");
    queryBuilder.append("    SELECT s.Numberofdays, s.code , I.RSI , R.TC_RS  ");
    queryBuilder.append("    FROM Technical_Indicator_byDate I   ");
    queryBuilder.append("    INNER JOIN stockDirection s ");
    queryBuilder.append("      ON I.TICKER =  s.code ");
    queryBuilder.append("      AND S.trading_Date = I.DATEREPORT ");
    queryBuilder.append("    INNER JOIN Stock_RSRating_REFINING R ");
    queryBuilder.append("      ON I.TICKER =  R.Ticker ");
    queryBuilder.append("      AND R.DATEREPORT = I.DATEREPORT ");
    queryBuilder.append("    WHERE i.Ticker = :ticker ");
    queryBuilder.append("      AND I.DateReport = (SELECT MAX(DATEREPORT) FROM Technical_Indicator_byDate) ");
    queryBuilder.append("  )	SELECT *  ");
    queryBuilder.append("  FROM tca_ratio_latest R   ");
    queryBuilder.append("  INNER JOIN UP_DOWN D ");
    queryBuilder.append("    ON R.Ticker = D.CODE ");

    try {
      List<Map<String,Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.stream().map(RatingDetail::build).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static List<RatingDetail> getTop200() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * , portfolio as Ticker  ");
    queryBuilder.append(" 			FROM tca_ratio_latest_special_portfolio ");
    queryBuilder.append(" 			WHERE portfolio = 'TOP200'  ");

    try {
      List<Map<String,Object>> listTickers = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listTickers.stream().map(RatingDetail::build).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static List<RatingDetail> getSameIndustry(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" ; WITH stocks AS ( ");
    queryBuilder.append(" SELECT TOP(20) S.Ticker ");
    queryBuilder.append(" FROM stx_cpf_Organization S ");
    queryBuilder.append(" WHERE IcbCode  = (SELECT IcbCode  ");
    queryBuilder.append(" FROM stx_cpf_Organization S ");
    queryBuilder.append(" WHERE S.Ticker = :ticker) ");
    queryBuilder.append(" AND S.ComGroupCode IN ('HNXIndex','UpcomIndex','VNINDEX') ");
    queryBuilder.append(" ORDER BY CharterCapital DESC ), ");
    queryBuilder.append(" UP_DOWN AS ( ");
    queryBuilder.append(" SELECT s.Numberofdays, s.code , I.RSI, R.TC_RS  ");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append("  SELECT *  ");
    queryBuilder.append("  FROM Technical_Indicator_byDate ");
    queryBuilder.append("  WHERE DateReport = ( select MAX(DateReport) FROM Technical_Indicator_byDate ) ");
    queryBuilder.append("  	AND Ticker IN (SELECT Ticker FROM stocks) ");
    queryBuilder.append(" ) I LEFT JOIN stockDirection s ");
    queryBuilder.append(" ON I.TICKER =  s.code ");
    queryBuilder.append(" AND S.trading_Date = I.DateReport ");
    queryBuilder.append(" LEFT JOIN Stock_RSRating_REFINING R ");
    queryBuilder.append(" ON I.TICKER =  R.Ticker  ");
    queryBuilder.append(" AND R.DateReport = I.DateReport ");
//    queryBuilder.append(" WHERE I.DateReport = (SELECT MAX(trading_Date) FROM stockDirection) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" SELECT *  ");
    queryBuilder.append(" FROM tca_ratio_latest R  ");
    queryBuilder.append(" INNER JOIN UP_DOWN D ");
    queryBuilder.append(" ON R.Ticker = D.CODE ");
    queryBuilder.append(" ORDER BY R.marcap DESC  ");

    try {
      List<Map<String,Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.stream().map(RatingDetail::build).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<RatingDetail> getByIndustry(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT *, :ticker AS Ticker  ");
    queryBuilder.append(" FROM tca_ratio_latest_industry  ");
    queryBuilder.append(" WHERE IDLEVEL2 = (SELECT V.IdLevel2 AS ID  ");
    queryBuilder.append(" 				FROM stx_cpf_Organization S  ");
    queryBuilder.append(" 				INNER JOIN view_idata_industry v  ");
    queryBuilder.append(" 					ON S.IcbCode = V.IdLevel4  ");
    queryBuilder.append(" 					AND S.Ticker  = :ticker)  ");
    try {
      List<Map<String,Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.stream().map(RatingDetail::build).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static List<RatingDetail> getByListTicker(String tickers) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" WITH stocks AS ( ");
    queryBuilder.append("   SELECT value AS TICKER FROM STRING_SPLIT(:tickers,',') ");
    queryBuilder.append(" )     , ");
    queryBuilder.append(" UP_DOWN AS ( ");
    queryBuilder.append("   SELECT s.Numberofdays, s.code , I.RSI , R.TC_RS  ");
    queryBuilder.append("   FROM Technical_Indicator_byDate I ");
    queryBuilder.append("   INNER JOIN stocks T ");
    queryBuilder.append("     ON T.TICKER = I.Ticker				   ");
    queryBuilder.append("   INNER JOIN stockDirection s ");
    queryBuilder.append("     ON T.TICKER =  s.code ");
    queryBuilder.append("     AND S.trading_Date = I.DateReport ");
    queryBuilder.append("   INNER JOIN Stock_RSRating_REFINING R ");
    queryBuilder.append("     ON T.TICKER =  R.Ticker  ");
    queryBuilder.append("     AND R.DateReport = I.DateReport ");
    queryBuilder.append("   WHERE I.DateReport = (SELECT MAX(DATEREPORT) FROM Technical_Indicator_byDate) ");
    queryBuilder.append(" )	    SELECT *  ");
    queryBuilder.append(" FROM tca_ratio_latest R  ");
    queryBuilder.append(" INNER JOIN UP_DOWN D ");
    queryBuilder.append("   ON R.Ticker = D.CODE ");
    try {
      List<Map<String,Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.stream().map(RatingDetail::build).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static RatingDetail build(Map<String,Object> rs) {
    return RatingDetail.builder()
      .ticker(rs.get("Ticker").toString())
      .marcap(tryDouble(rs.get("marcap")))
      .price(tryDouble(rs.get("price")))
      .numberOfDays(tryDouble(rs.get("Numberofdays")))
      .priceToEarning(tryDouble(rs.get("pe")))
      .peg(tryDouble(rs.get("peg")))
      .priceToBook(tryDouble(rs.get("pb")))
      .valueBeforeEbitda(tryDouble(rs.get("ev_on_ebitda")))
      .dividend(tryDouble(rs.get("dividend")))
      .roe(tryDouble(rs.get("roe")))
      .roa(tryDouble(rs.get("roa")))
      .ebitOnInterest(tryDouble(rs.get("interest_coverage")))
      .interestMargin(tryDouble(rs.get("nim")))
      .badDebtPercentage(tryDouble(rs.get("npl_ratio")))
      .currentPayment(tryDouble(rs.get("current_ratio")))
      .quickPayment(tryDouble(rs.get("quick_ratio")))
      .grossProfitMargin(tryDouble(rs.get("gross_profit_margin")))
      .postTaxMargin(tryDouble(rs.get("net_profit_after_mi_margin")))
      .debtOnEquity(tryDouble(rs.get("debt_on_equity")))
      .income5year(tryDouble(rs.get("income5year")))
      .sale5year(tryDouble(rs.get("sale5year")))
      .income1quarter(tryDouble(rs.get("income1quarter")))
      .sale1quarter(tryDouble(rs.get("sale1quarter")))
      .rsi(tryDouble(rs.get("RSI")))
      .rs(tryDouble(rs.get("TC_RS")))
      .build();
  }

  public static Double tryDouble(Object x) {
    if(x == null) {
      return null;
    } else {
      return Double.parseDouble(x.toString());
    }
  }
}
