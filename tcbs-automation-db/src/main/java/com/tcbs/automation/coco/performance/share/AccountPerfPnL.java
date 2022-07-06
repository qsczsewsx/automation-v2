package com.tcbs.automation.coco.performance.share;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import com.tcbs.automation.coco.performance.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountPerfPnL {
  private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @ColumnIndex(name = "ACCOUNT_NO", index = 0, ignoreIfNull = true)
  private String accountNo;

  @ColumnIndex(name = "REPORT_TYPE", index = 1, ignoreIfNull = true)
  private String reportType;

  @ColumnIndex(name = "REPORT_DATE", index = 2, ignoreIfNull = true)
  private Date actualDate;

  @ColumnIndex(name = "AVG_PROFIT", index = 3, ignoreIfNull = true)
  private Double totalEstimatedRevenue;

  @ColumnIndex(name = "RETURN_VALUE", index = 4, ignoreIfNull = true)
  private Double returnValue;

  @ColumnIndex(name = "RETURN_PCT", index = 5, ignoreIfNull = true)
  private Double returnPct;

  public static List<AccountPerfPnL> getPerformancePnL(String accountNo) throws Exception {
    List<AccountPerfPnL> transSummaryReportByUsers;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall(Constants.Procedure.PROC_PE_GET_PERFORMANCE_BY_ACCOUNT);

    int index = 1;
    LocalDate currentDate = LocalDate.now();
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusWeeks(1)));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusMonths(1)));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusMonths(3)));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusMonths(6)));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusYears(1)));
    call.registerParameter(index++, String.class, ParameterMode.IN).bindValue(formatter.format(currentDate.minusYears(3)));
    call.registerParameter(index, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    transSummaryReportByUsers = ReflexData.convertResultSetToObj(resultSets, AccountPerfPnL.class);


    return transSummaryReportByUsers;

  }
}
