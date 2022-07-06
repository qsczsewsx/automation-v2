package com.tcbs.automation.coco.performance;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ReflexData;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.result.Output;
import org.hibernate.result.ResultSetOutput;

import javax.persistence.ParameterMode;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPerfSumMetrics {
  private List<CustomerPerfRisk> risks;

  private List<CustomerPerfReturn> returns;

  public static CustomerPerfSumMetrics getSummaryMetrics(String custodyId) throws Exception {
    CustomerPerfSumMetrics res = new CustomerPerfSumMetrics();
    res.setRisks(callProcedure(Constants.Procedure.PROC_PE_GET_LATEST_RISK_SCORE_BY_USER, CustomerPerfRisk.class, custodyId));
    res.setReturns(callProcedure(Constants.Procedure.PROC_PE_GET_LATEST_RETURN_BY_USER, CustomerPerfReturn.class, custodyId));
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
