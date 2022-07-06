package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import com.tcbs.automation.tools.DateUtils;
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
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Views {
  @ColumnIndex(name = "REPORT_DATE", index = 0, fmtIncaseDatetime = DateUtils.ORACLE_TIMESTAMP_FORMAT)
  private Date reportDate;

  @ColumnIndex(name = "TCBSID", index = 1)
  private String tcbsId;

  @ColumnIndex(name = "TOTAL_VIEWS", index = 2)
  private Long totalViews;

  public static List<Views> getTraderViewsByTcbsId(String tcbsId, Date currentDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRADER_MONTHLY_VIEW");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(DateUtils.toString(currentDate, DateUtils.ISO_DATE_FORMAT));
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(tcbsId);
    call.registerParameter(3, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();

    List<Views> viewsList = ReflexData.convertResultSetToObj(resultSets, Views.class);

    return viewsList;
  }

  public static List<Views> getTraderViewsByTraderId(Long traderId, Date fromDate, Date toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRADER_MONTHLY_VIEW_BY_TRADER_ID");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(DateUtils.toString(fromDate, DateUtils.ISO_DATE_FORMAT));
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(DateUtils.toString(toDate, DateUtils.ISO_DATE_FORMAT));
    call.registerParameter(3, Long.class, ParameterMode.IN).bindValue(traderId);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();

    List<Views> viewsList = ReflexData.convertResultSetToObj(resultSets, Views.class);

    return viewsList;
  }
}
