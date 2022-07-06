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
public class PeSellTransHistoryByAccount {
//  @ColumnIndex(name = "USER_ID", index = 0)
//  private String userId;

  @ColumnIndex(name = "ACCOUNT_ID", index = 0)
  private String accountId;

  @ColumnIndex(name = "TICKER", index = 1, ignoreIfNull = true)
  private String ticker;

  @ColumnIndex(name = "SELLING_VALUE", index = 2, ignoreIfNull = true)
  private Double sellingValue = 0.0;

  @ColumnIndex(name = "SELLING_VOLUME", index = 3, ignoreIfNull = true)
  private Double sellingVolume = 0.0;

  @ColumnIndex(name = "COGS_PER_UNIT_SELL", index = 4, ignoreIfNull = true)
  private Double cogsPerUnitSell = 0.0;

  @ColumnIndex(name = "TRADING_DATE", index = 5, fmtIncaseDatetime = "yyyy-MM-dd HH:mm:ss.S")
  private Date tradingDate;

  @ColumnIndex(name = "sellPrice", index = 6, ignoreIfNull = true)
  private Double sellPrice = 0.0;

  @ColumnIndex(name = "winLoss", index = 7, ignoreIfNull = true)
  private Double avgWinLoss = 0.0;

  public static List<PeSellTransHistoryByAccount> getPeSellTransHistoryByAccounts(String accountNo, String fromDate, String toDate) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    List<PeSellTransHistoryByAccount> peTransHistorySummaryByAccounts = new ArrayList<>();
    Session session = CocoConnBridge.cocoConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_PE_GET_SELL_TRANSACTION_HISTORY_BY_ACCOUNT");

    call.registerParameter(1, String.class, ParameterMode.IN).bindValue(accountNo);
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(fromDate);
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(toDate);
    call.registerParameter(4, void.class, ParameterMode.REF_CURSOR);

    Output output = call.getOutputs().getCurrent();

    List<Object> resultSets = ((ResultSetOutput) output).getResultList();
    CocoConnBridge.cocoConnection.closeSession();

    peTransHistorySummaryByAccounts = ReflexData.convertResultSetToObj(resultSets, PeSellTransHistoryByAccount.class);


    return peTransHistorySummaryByAccounts;
  }
}
