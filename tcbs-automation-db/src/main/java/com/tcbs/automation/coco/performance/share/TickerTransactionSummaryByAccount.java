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
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerTransactionSummaryByAccount {
  @ColumnIndex(name = "TICKER", index = 0, ignoreIfNull = true)
  private String ticker;
  @ColumnIndex(name = "TRANSACTION_COUNT", index = 1, ignoreIfNull = true)
  private Integer transactionCount;

  public static List<TickerTransactionSummaryByAccount> getTickerTransactionSummaryByAccount(String accountNo, String fromDate, String toDate, String type) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<TickerTransactionSummaryByAccount> transactionSummaryByAccounts = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TICKER_TRANSACTION_SUMMARY_BY_ACCOUNT");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, String.class, ParameterMode.IN).bindValue(type);
    call.registerParameter(5, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    transactionSummaryByAccounts = ReflexData.convertResultSetToObj(resultSets, TickerTransactionSummaryByAccount.class);


    return transactionSummaryByAccounts;
  }
}
