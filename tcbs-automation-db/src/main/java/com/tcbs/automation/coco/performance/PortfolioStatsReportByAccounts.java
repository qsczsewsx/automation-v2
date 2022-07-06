package com.tcbs.automation.coco.performance;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioStatsReportByAccounts {

  private int minSector;
  private int maxSector;
  private Date actualReportDate;
  private List<SectorData> data;

  public static List<PortfolioStatsReportByAccounts.StatsItem> getStatistic(List<String> userIds, String reportDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PortfolioStatsReportByAccounts.StatsItem> stats;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_PORTFOLIO_BY_TRADER_ACCOUNTS");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(String.join(",", userIds));
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(reportDate);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    stats = ReflexData.convertResultSetToObj(resultSets, PortfolioStatsReportByAccounts.StatsItem.class);

    return stats;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StatsItem {
    @ColumnIndex(name = "ACCOUNT_ID", index = 0, ignoreIfNull = true)
    private String accountNo;
    @ColumnIndex(name = "REPORT_DATE", index = 1, fmtIncaseDatetime = "yyyy-MM-dd HH:mm:ss.S")
    private Date reportDate;
    @ColumnIndex(name = "TICKER", index = 2, ignoreIfNull = true)
    private String ticker;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Data
  public static class SectorData {
    private String accountNo;
    private int noSector;
  }

}
