package com.tcbs.automation.coco.performance;

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
public class PeTransHistorySummaryByUser {
  @ColumnIndex(name = "TRADE_COUNT", index = 0)
  private Integer orderCount;
  @ColumnIndex(name = "TRADE_WIN_COUNT", index = 1, ignoreIfNull = true)
  private Integer winOrderCount;
  @ColumnIndex(name = "WIN_PERCENT", index = 2, ignoreIfNull = true)
  private Double winOrderPercent = 0.0;
  @ColumnIndex(name = "WIN_LOSS", index = 3, ignoreIfNull = true)
  private Double avgWinLoss = 0.0;

  public static PeTransHistorySummaryByUser getPeTransHistorySummaryByUsers(String userId, String fromDate, String toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PeTransHistorySummaryByUser> peTransHistorySummaryByUsers = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRANS_HISTORY_SUMMARY_REPORT_BY_USER");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    peTransHistorySummaryByUsers = ReflexData.convertResultSetToObj(resultSets, PeTransHistorySummaryByUser.class);

    if (CollectionUtils.isEmpty(peTransHistorySummaryByUsers)) {
      return null;
    }

    return peTransHistorySummaryByUsers.get(0);
  }
}
