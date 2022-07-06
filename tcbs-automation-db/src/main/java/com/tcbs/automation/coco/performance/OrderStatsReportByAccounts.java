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
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatsReportByAccounts {
  @ColumnIndex(name = "ACCOUNT_ID", index = 0, ignoreIfNull = true)
  private String accountNo;
  @ColumnIndex(name = "TRADE_WIN_COUNT", index = 1, ignoreIfNull = true)
  private Integer tradeWinCount = 0;
  @ColumnIndex(name = "WIN_PERCENT", index = 2, ignoreIfNull = true)
  private Double winPercent = 0.0;
  @ColumnIndex(name = "TRADE_COUNT", index = 3, ignoreIfNull = true)
  private Integer tradeCount = 0;
  @ColumnIndex(name = "AVG_PROFIT", index = 4, ignoreIfNull = true)
  private Double avgWin = 0.0;
  @ColumnIndex(name = "AVG_LOSS", index = 5, ignoreIfNull = true)
  private Double avgLoss = 0.0;
  @ColumnIndex(name = "AVG_HOLDING_PERIOD", index = 6, ignoreIfNull = true)
  private Integer avgHoldingPeriod = 0;
  @ColumnIndex(name = "MAX_NO_TICKER", index = 7, ignoreIfNull = true)
  private Integer maxNoTicker = 0;

  public static List<OrderStatsReportByAccounts> getStatistic(List<String> userIds, String fromDate, String toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<OrderStatsReportByAccounts> stats;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRANS_SUMMARY_BY_TRADER_ACCOUNTS");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(String.join(",", userIds));
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    stats = ReflexData.convertResultSetToObj(resultSets, OrderStatsReportByAccounts.class);

    return stats;
  }
}
