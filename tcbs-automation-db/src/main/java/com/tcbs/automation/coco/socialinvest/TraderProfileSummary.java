package com.tcbs.automation.coco.socialinvest;

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
import org.springframework.util.CollectionUtils;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraderProfileSummary {
  @ColumnIndex(name = "TRADER_ID", index = 0, ignoreIfNull = true)
  private Long traderId;
  @ColumnIndex(name = "TCBSID", index = 1, ignoreIfNull = true)
  private String tcbsId;
  @ColumnIndex(name = "REPORT_DATE", index = 2, ignoreIfNull = true)
  private Date reportDate;
  @ColumnIndex(name = "AVG_AUM", index = 3, ignoreIfNull = true)
  private Double avgAUM = 0.0;
  @ColumnIndex(name = "AVG_COPIER_TIME", index = 4, ignoreIfNull = true)
  private Double avgCopierTime = 0.0;
  @ColumnIndex(name = "AVG_PNL", index = 5, ignoreIfNull = true)
  private Double avgPnl = 0.0;
  @ColumnIndex(name = "SUCCESS_COPIED_ORDER_RATE", index = 6, ignoreIfNull = true)
  private Double successCopiedOrderRate = 0.0;
  @ColumnIndex(name = "MATCHED_PRICE_DELTA", index = 7, ignoreIfNull = true)
  private Double matchedPriceDelta = 0.0;
  @ColumnIndex(name = "CHANGE_AUM_30DAYS", index = 8, ignoreIfNull = true)
  private Double aumChange30Days = 0.0;
  @ColumnIndex(name = "AVG_AUM_30DAYS", index = 9, ignoreIfNull = true)
  private Double avgAUM30Days = 0.0;

  public static TraderProfileSummary getTraderProfileSummary(Long id) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<TraderProfileSummary> traderProfileSummaryList = new ArrayList<>();
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_TRADER_PROFILE_SUMMARY_BY_TRADER_IDS");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(StringUtils.join(Collections.singletonList(id), ","));
    call.registerParameter(2, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();

    traderProfileSummaryList = ReflexData.convertResultSetToObj(resultSets, TraderProfileSummary.class);

    if (CollectionUtils.isEmpty(traderProfileSummaryList)) {
      return new TraderProfileSummary();
    } else {
      return traderProfileSummaryList.get(0);
    }
  }
}