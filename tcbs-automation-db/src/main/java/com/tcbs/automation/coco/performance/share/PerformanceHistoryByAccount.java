package com.tcbs.automation.coco.performance.share;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceHistoryByAccount {
  @ColumnIndex(name = "ACCOUNT_NO", index = 0, ignoreIfNull = true)
  private String accountNo;
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

  public static List<PerformanceHistoryByAccount> gePerformanceHistoryByAccounts(String accountNo, String reportDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PerformanceHistoryByAccount> performanceHistoryByAccounts = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_PORTFOLIO_HISTORY_BY_ACCOUNT");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(reportDate);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    performanceHistoryByAccounts = ReflexData.convertResultSetToObj(resultSets, PerformanceHistoryByAccount.class);


    return performanceHistoryByAccounts;
  }
}
