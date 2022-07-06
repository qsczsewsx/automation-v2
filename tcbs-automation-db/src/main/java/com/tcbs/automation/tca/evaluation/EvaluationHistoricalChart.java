package com.tcbs.automation.tca.evaluation;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.*;

@Getter
@RequiredArgsConstructor
@Setter
public class EvaluationHistoricalChart {
  private String ticker;
  @Nullable
  private  Double pe;
  @Nullable
  private  Double pb;
  @Nullable
  private Double industryPe;
  @Nullable
  private Double industryPb;
  @Nullable
  private Double indexPe;
  @Nullable
  private Double indexPb;
  @Nullable
  private LocalDate reportDate;

  @Step
  public static List<EvaluationHistoricalChart> getHistoricalChart(String ticker, LocalDate fromDate) {

    try {
      List<Map<String, Object>> resultList;
      if (TickerBasic.getTickerBasic(ticker).getComTypeCode().equals("NH")) {
        resultList = TcAnalysis.tcaDbConnection.getSession()
          .createNativeQuery(buildQuery("tcdata_ratio_bank"))
          .setParameter("anchor", getCurrentQuarter("tcdata_ratio_bank", ticker))
          .setParameter("NameLevel2", TickerBasic.getIndustryName(ticker, "vi"))
          .setParameter("IdLevel2", TickerBasic.getIndustryLv2(ticker))
          .setParameter("reportDate", fromDate)
          .setParameter("ticker", ticker)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {
        resultList = TcAnalysis.tcaDbConnection.getSession()
          .createNativeQuery(buildQuery("tcdata_ratio_company"))
          .setParameter("anchor", getCurrentQuarter("tcdata_ratio_company", ticker))
          .setParameter("NameLevel2", TickerBasic.getIndustryName(ticker, "vi"))
          .setParameter("IdLevel2", TickerBasic.getIndustryLv2(ticker))
          .setParameter("reportDate", fromDate)
          .setParameter("ticker", ticker)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }

      List<EvaluationHistoricalChart> evaluationHistoricalChartList = new ArrayList<>();
      for (Map<String, Object> result : resultList) {
        EvaluationHistoricalChart evaluationHistoricalChart = new EvaluationHistoricalChart();
        evaluationHistoricalChart.setPe(parseDouble(result.get("PE")));
        evaluationHistoricalChart.setPb(parseDouble(result.get("PB")));
        evaluationHistoricalChart.setIndustryPe(parseDouble(result.get("IndustryPE")));
        evaluationHistoricalChart.setIndustryPb(parseDouble(result.get("IndustryPB")));
        evaluationHistoricalChart.setIndexPe(parseDouble(result.get("IndexPE")));
        evaluationHistoricalChart.setIndexPb(parseDouble(result.get("IndexPB")));
        evaluationHistoricalChart.setReportDate(LocalDate.parse(result.get("ReportDate").toString()));
        evaluationHistoricalChartList.add(evaluationHistoricalChart);
      }

      return evaluationHistoricalChartList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static Object getCurrentQuarter(String table, String ticker) {
    String query = "(SELECT MAX(YearReport*10+LengthReport) as currentQuarter "
                + " FROM  " + table
                + " WHERE Ticker = :ticker AND LengthReport  < 5)";
    return ((Map<String, Object>) (TcAnalysis.tcaDbConnection.getSession()
      .createNativeQuery(query)
      .setParameter("ticker", ticker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList().get(0))).get("currentQuarter");
  }


  public static String buildQuery(String table) {
    StringBuilder query = new StringBuilder();
    query.append("		;WITH tmp AS ( ");
    query.append("			SELECT P.TradingDate AS UpdateDate ");
    query.append("				, ClosePriceAdjusted / basic_eps AS [PE] ");
    query.append("				, ClosePriceAdjusted / bvps AS [PB] ");
    query.append("			FROM  Smy_dwh_stox_MarketPrices P ");
    query.append("			LEFT JOIN ");
    query.append(table);
    query.append("          T ");
    query.append("				ON P.Ticker = T.Ticker  ");
    query.append("				AND (YEAR(P.TradingDate)*10+DATEPART(Quarter, P.TradingDate) = T.YearReport*10+T.LengthReport ");
    query.append("					OR (YEAR(P.TradingDate)*10+DATEPART(Quarter, P.TradingDate) > :anchor  ");
    query.append("						AND T.YearReport*10+T.LengthReport = :anchor)) ");
    query.append("			WHERE P.TICKER = :ticker  ");
    query.append("				AND P.tradingDate >= :reportDate ");
    query.append("		) ");
    query.append("		SELECT ");
    query.append("			tmp.pe  AS [PE], ");
    query.append("			tmp.pb AS [PB], ");
    query.append("			index_ratio.[IndexPB], ");
    query.append("			index_ratio.[IndexPE],  ");
    query.append("			industry_ratio.[IndustryPE], ");
    query.append("			industry_ratio.[IndustryPB], ");
    query.append("			:NameLevel2 AS Industry, ");
    query.append("			CAST(TMP.UpdateDate AS DATE) AS ReportDate ");
    query.append("		FROM tmp  ");
    query.append("		INNER JOIN ( ");
    query.append("			SELECT ReportDate, ");
    query.append("				[P/B] AS [IndexPB],  ");
    query.append("				[P/E] AS [IndexPE] ");
    query.append("			FROM tbl_idata_evaluation_index_ratio  ");
    query.append("			WHERE ReportDate >= :reportDate ");
    query.append("		) index_ratio ");
    query.append("			ON CAST(index_ratio.ReportDate AS DATE) = CAST(tmp.UpdateDate AS DATE) ");
    query.append("		INNER JOIN ( ");
    query.append("			SELECT ReportDate, ");
    query.append("				[P/B] AS [IndustryPB],  ");
    query.append("				[P/E] AS [IndustryPE] ");
    query.append("			FROM tbl_idata_evaluation_industry_ratio  ");
    query.append("			WHERE IndustryId = :IdLevel2 ");
    query.append("				AND ReportDate >= :reportDate ");
    query.append("		) industry_ratio  ");
    query.append("			ON CAST(industry_ratio.ReportDate as date) = CAST(index_ratio.ReportDate AS DATE)  ");
    query.append("		ORDER BY TMP.UpdateDate ASC ");
    return query.toString();
  }
}