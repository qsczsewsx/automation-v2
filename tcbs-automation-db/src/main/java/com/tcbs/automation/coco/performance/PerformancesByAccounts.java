package com.tcbs.automation.coco.performance;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformancesByAccounts {
  @ColumnIndex(name = "ACCOUNT_NO", index = 0, ignoreIfNull = true)
  private String accountNo;
  @ColumnIndex(name = "PNL_30DAYS_REPORT_DATE", index = 1, ignoreIfNull = true)
  private Date pnl30DaysReportDate;
  @ColumnIndex(name = "PNL_30DAYS_RETURN_VALUE", index = 2, ignoreIfNull = true)
  private Double pnl30DaysReturnValue = 0.0;
  @ColumnIndex(name = "PNL_30DAYS_RETURN_PCT", index = 3, ignoreIfNull = true)
  private Double pnl30DaysReturnPct = 0.0;
  @ColumnIndex(name = "PNL_12MONTHS_REPORT_DATE", index = 4, ignoreIfNull = true)
  private Date pnl12MonthsReportDate;
  @ColumnIndex(name = "PNL_12MONTHS_RETURN_VALUE", index = 5, ignoreIfNull = true)
  private Double pnl12MonthsReturnValue = 0.0;
  @ColumnIndex(name = "PNL_12MONTHS_RETURN_PCT", index = 6, ignoreIfNull = true)
  private Double pnl12MonthsReturnPct = 0.0;
  @ColumnIndex(name = "ALL_TIME_REPORT_DATE", index = 7, ignoreIfNull = true)
  private Date pnlAllTimeReportDate;
  @ColumnIndex(name = "ALL_TIME_RETURN_VALUE", index = 8, ignoreIfNull = true)
  private Double pnlAllTImeReturnValue = 0.0;
  @ColumnIndex(name = "ALL_TIME_RETURN_PCT", index = 9, ignoreIfNull = true)
  private Double pnlAllTimeReturnPct = 0.0;
  @ColumnIndex(name = "LASTED_RISK_DATE", index = 10, ignoreIfNull = true)
  private Date lastedRickDate;
  @ColumnIndex(name = "RISK_SCORE", index = 11, ignoreIfNull = true)
  private Double rickScore;


  public static List<PerformancesByAccounts> getPerformanceReportByAccounts(List<String> accountNos, String currentDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PerformancesByAccounts> performancesByAccounts;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_PERFORMANCE_REPORT_BY_ACCOUNTS");

    String accountNoList = StringUtils.join(accountNos, ",");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNoList);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(currentDate);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    performancesByAccounts = ReflexData.convertResultSetToObj(resultSets, PerformancesByAccounts.class);

    return performancesByAccounts;
  }
}
