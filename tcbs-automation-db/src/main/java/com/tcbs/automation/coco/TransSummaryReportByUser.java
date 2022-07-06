package com.tcbs.automation.coco;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransSummaryReportByUser {

  @ColumnIndex(name = "TRADE_WIN_COUNT", index = 0, ignoreIfNull = true)
  private Integer tradeWinCount;
  @ColumnIndex(name = "WIN_PERCENT", index = 1, ignoreIfNull = true)
  private Double winPercent;
  @ColumnIndex(name = "TRADE_COUNT", index = 2, ignoreIfNull = true)
  private Integer tradeCount;
  @ColumnIndex(name = "AVG_PROFIT", index = 3, ignoreIfNull = true)
  private Double avgProfit;
  @ColumnIndex(name = "AVG_LOSS", index = 4, ignoreIfNull = true)
  private Double avgLoss;
  @ColumnIndex(name = "AVG_HOLDING_PERIOD", index = 5, ignoreIfNull = true)
  private Integer avgHoldingPeriod;

  public static List<TransSummaryReportByUser> getUserPortfolioPresent(String userId, String fromDate, String toDate, String type) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<TransSummaryReportByUser> transSummaryReportByUsers = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRANS_SUMMARY_REPORT_BY_USER");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, String.class, ParameterMode.IN).bindValue(type);
    call.registerParameter(5, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    transSummaryReportByUsers = ReflexData.convertResultSetToObj(resultSets, TransSummaryReportByUser.class);


    return transSummaryReportByUsers;
  }
}
