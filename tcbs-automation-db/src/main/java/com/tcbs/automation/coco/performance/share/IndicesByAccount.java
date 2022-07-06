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
public class IndicesByAccount {
  @ColumnIndex(name = "ACCOUNT_NO", index = 0, ignoreIfNull = true)
  private String accountNo;
  @ColumnIndex(name = "EOMONTH", index = 1, ignoreIfNull = true)
  private Date eoMonth;
  @ColumnIndex(name = "PORTF_INDEX", index = 2, ignoreIfNull = true)
  private Double index = 0.0;
  @ColumnIndex(name = "PORTF_MONRET_FACTOR_LN", index = 3, ignoreIfNull = true)
  private Double portfMonretReturns = 0.0;
  @ColumnIndex(name = "VALUE_AT_RICK", index = 4, ignoreIfNull = true)
  private Double valueAtRick = 0.0;

  public static List<IndicesByAccount> getIndicesByAccounts(String accountNo, String benchmark) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<IndicesByAccount> indices = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_INDICES_BY_ACCOUNT");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(benchmark);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    indices = ReflexData.convertResultSetToObj(resultSets, IndicesByAccount.class);

    return indices;
  }
}
