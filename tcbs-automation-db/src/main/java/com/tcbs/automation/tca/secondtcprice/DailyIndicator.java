package com.tcbs.automation.tca.secondtcprice;

import com.tcbs.automation.projection.Projection;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyIndicator {

  @Step("get data")
  public static Map<String, Object> getDailyIndicatorsByTicker(String ticker, String dateStr, int numberTDays) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DECLARE @day1Ago AS DATETIME; ");
    queryBuilder.append("SET @day1Ago = dbo.businessDaysAdd(:number, CAST(:dateStr AS DATE)); ");
    queryBuilder.append(" SELECT tbl1.Ticker, ROUND(pe, 1) AS pe, ROUND(pb, 1) AS pb, ROUND(roe, 1) AS roe ");
    queryBuilder.append(" , rs3D, rs1M, rs3M, rs1Y, rsAvg, sessionNumber ");
    queryBuilder.append(" FROM (SELECT Ticker, pe, pb, roe FROM tca_ratio_latest WHERE Ticker = :ticker) tbl1 ");
    queryBuilder.append(" LEFT JOIN ( ");
    queryBuilder.append(" SELECT srr.Ticker, srr.RS_Rank_3D as rs3D, srr.RS_Rank_1M AS rs1M ");
    queryBuilder.append(" , srr.RS_Rank_3M AS rs3M, srr.RS_Rank_1Y AS rs1Y, srr.TC_RS as rsAvg ");
    queryBuilder.append(" FROM Stock_RSRating_Refining srr ");
    queryBuilder.append(" WHERE srr.DateReport = @day1Ago AND Ticker = :ticker ");
    queryBuilder.append(" ) tbl2 ON tbl1.Ticker = tbl2.Ticker ");
    queryBuilder.append(" LEFT JOIN ( ");
    queryBuilder.append(" SELECT code AS Ticker, Numberofdays AS sessionNumber ");
    queryBuilder.append(" FROM Stockdirection WHERE trading_Date = @day1Ago AND code = :ticker ");
    queryBuilder.append(" ) tbl3 ON tbl1.Ticker = tbl3.Ticker ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("number", numberTDays)
        .setParameter("dateStr", dateStr)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
  }

  @Step("get data")
  public static Map<String, Object> getRatingOverall(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select *  from db_owner.tbl_idata_RatingIndex WHERE Ticker =:ticker and RatingKeyID = 0 order by UpdateTime desc");

    try {
      List<Map<String, Object>> resultList = Projection.projectionDbConnection
        .getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
  }

  @Step("get data")
  public static Map<String, Object> getVniRatio(String tradingDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select [P/E] pe, [P/B] pb, ReportDate from tbl_idata_evaluation_index_ratio where ReportDate < :toTime  order by ReportDate desc ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection
        .getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("toTime", tradingDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (!resultList.isEmpty()) {
        return resultList.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
  }
}
