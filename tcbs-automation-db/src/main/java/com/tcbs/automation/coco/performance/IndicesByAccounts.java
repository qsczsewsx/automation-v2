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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicesByAccounts {
  @ColumnIndex(name = "ACCOUNT_NO", index = 0, ignoreIfNull = true)
  private String accountNo;
  @ColumnIndex(name = "REPORT_DATE", index = 1, ignoreIfNull = true)
  private Date eoMonth;
  @ColumnIndex(name = "PORTF_INDEX", index = 2, ignoreIfNull = true)
  private Double index = 0.0;

  public static List<IndicesByAccounts> getIndicesByAccounts(List<String> accountNos, String fromDate, String toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<IndicesByAccounts> indices = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_INDICES_BY_ACCOUNTS");

    String accountNoList = StringUtils.join(accountNos, ",");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNoList);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    indices = ReflexData.convertResultSetToObj(resultSets, IndicesByAccounts.class);

    return indices;
  }
}
