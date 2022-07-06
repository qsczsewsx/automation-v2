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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.coco.performance.Constants.Procedure.PROC_PE_GET_PORTFOLIO_HISTORY_BY_USER;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceHistoryByUser {
  @ColumnIndex(name = "USER_ID", index = 0, ignoreIfNull = true)
  private String userId;
  @ColumnIndex(name = "REPORT_DATE", index = 1, ignoreIfNull = true)
  private Date repostDate;
  @ColumnIndex(name = "DENOMINATOR", index = 2, ignoreIfNull = true)
  private Double denominator = 0.0;
  @ColumnIndex(name = "NUMERATOR", index = 3, ignoreIfNull = true)
  private Double numerator = 0.0;
  @ColumnIndex(name = "PORTF_INDEX", index = 4, ignoreIfNull = true)
  private Double portfIndex = 0.0;
  @ColumnIndex(name = "DAILY_PROFIT", index = 5, ignoreIfNull = true)
  private Double dailyProfit = 0.0;
  @ColumnIndex(name = "DAILY_SELL_VALUE", index = 6, ignoreIfNull = true)
  private Double dailySellValue = 0.0;
  @ColumnIndex(name = "DAILY_BUY_VALUE", index = 7, ignoreIfNull = true)
  private Double dailyBuyValue = 0.0;
  @ColumnIndex(name = "NET_VALUE", index = 8, ignoreIfNull = true)
  private Double netValue = 0.0;
  @ColumnIndex(name = "TOTAL_VALUE", index = 9, ignoreIfNull = true)
  private Double totalValue = 0.0;

  public static List<PerformanceHistoryByUser> getPerformanceHistoryByUser(String userId, String reportDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PerformanceHistoryByUser> transSummaryReportByUsers = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall(PROC_PE_GET_PORTFOLIO_HISTORY_BY_USER);

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(reportDate);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    transSummaryReportByUsers = ReflexData.convertResultSetToObj(resultSets, PerformanceHistoryByUser.class);


    return transSummaryReportByUsers;
  }
}
