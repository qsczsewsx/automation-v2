package com.tcbs.automation.coco.performance.share;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ReflexData;
import com.tcbs.automation.coco.performance.Constants;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AccountPerfSumMetrics {
  private List<AccountPerfRick> risks;

  private List<AccountPerfReturn> returns;

  public static AccountPerfSumMetrics getSummaryMetrics(String accountNo) throws Exception {
    AccountPerfSumMetrics res = new AccountPerfSumMetrics();
    res.setRisks(callProcedure(Constants.Procedure.PROC_PE_GET_LATEST_RISK_SCORE_BY_ACCOUNT, AccountPerfRick.class, accountNo));
    res.setReturns(callProcedure(Constants.Procedure.PROC_PE_GET_LATEST_RETURN_BY_ACCOUNT, AccountPerfReturn.class, accountNo));
    return res;
  }


  private static <T> List<T> callProcedure(String procedureName, Class<T> clazz, String custodyId) throws Exception {
    List<T> customerPerfSumMetrics;
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall(procedureName);

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(custodyId);
    call.registerParameter(2, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    customerPerfSumMetrics = ReflexData.convertResultSetToObj(resultSets, clazz);


    return customerPerfSumMetrics;
  }
}
