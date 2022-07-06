package com.tcbs.automation.coco.performance.share;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PeTransHistorySummaryByAccount {
  @ColumnIndex(name = "TRADE_COUNT", index = 0)
  private Integer orderCount;
  @ColumnIndex(name = "TRADE_WIN_COUNT", index = 1, ignoreIfNull = true)
  private Integer winOrderCount;
  @ColumnIndex(name = "WIN_PERCENT", index = 2, ignoreIfNull = true)
  private Double winOrderPercent = 0.0;
  @ColumnIndex(name = "WIN_LOSS", index = 3, ignoreIfNull = true)
  private Double avgWinLoss = 0.0;

  public static PeTransHistorySummaryByAccount getPeTransHistorySummaryByAccounts(String accountNo, String fromDate, String toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PeTransHistorySummaryByAccount> peTransHistorySummaryByAccounts = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRANS_HISTORY_SUMMARY_REPORT_BY_ACCOUNT");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    peTransHistorySummaryByAccounts = ReflexData.convertResultSetToObj(resultSets, PeTransHistorySummaryByAccount.class);

    if (CollectionUtils.isEmpty(peTransHistorySummaryByAccounts)) {
      return null;
    }

    return peTransHistorySummaryByAccounts.get(0);
  }
}
