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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicesByUser {

  @ColumnIndex(name = "USER_ID", index = 0, ignoreIfNull = true)
  private String userId;
  @ColumnIndex(name = "EOMONTH", index = 1, ignoreIfNull = true)
  private Date eoMonth;
  @ColumnIndex(name = "PORTF_INDEX", index = 2, ignoreIfNull = true)
  private Double index = 0.0;
  @ColumnIndex(name = "PORTF_MONRET_FACTOR_LN", index = 3, ignoreIfNull = true)
  private Double portfMonretReturns = 0.0;
  @ColumnIndex(name = "VALUE_AT_RICK", index = 4, ignoreIfNull = true)
  private Double valueAtRick = 0.0;

  public static List<IndicesByUser> getIndicesByUser(String userId, String benchmark) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<IndicesByUser> indicesByUsers = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_INDICES_BY_USER");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(userId);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(benchmark);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    indicesByUsers = ReflexData.convertResultSetToObj(resultSets, IndicesByUser.class);


    return indicesByUsers;
  }
}
