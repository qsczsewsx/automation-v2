package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.coco.ColumnIndex;
import com.tcbs.automation.coco.ReflexData;
import com.tcbs.automation.constants.coco.Constants;
import com.tcbs.automation.tools.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.procedure.ProcedureCall;

import javax.persistence.ParameterMode;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Data
@Builder
public class TraderOrderHistory {
  private List<TraderOrderHistoryItem> orderHistories;
  private Long count;

  public static TraderOrderHistory getTraderOrderHistories(TraderOrderHistoryParam param) throws IllegalAccessException, ParseException, NoSuchMethodException, InstantiationException, InvocationTargetException {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    ProcedureCall call = session.createStoredProcedureCall("PROC_BE_GET_TRADER_ORDER_HISTORY");

    call.registerParameter(1, Long.class, ParameterMode.IN).bindValue(param.getTraderId());
    call.registerParameter(2, String.class, ParameterMode.IN).bindValue(param.getSymbol());
    call.registerParameter(3, String.class, ParameterMode.IN).bindValue(Constants.OrderSide.stringValue(param.getOrderSide()));
    call.registerParameter(4, String.class, ParameterMode.IN).bindValue(param.getPriceType());
    call.registerParameter(5, String.class, ParameterMode.IN).bindValue(DateUtils.toString(param.getFromDate(), DateUtils.ISO_DATE_FORMAT));
    call.registerParameter(6, String.class, ParameterMode.IN).bindValue(DateUtils.toString(param.getToDate(), DateUtils.ISO_DATE_FORMAT));
    call.registerParameter(7, int.class, ParameterMode.IN).bindValue(param.getSize() * param.getPage());
    call.registerParameter(8, int.class, ParameterMode.IN).bindValue(param.getSize());
    call.registerParameter(9, Long.class, ParameterMode.OUT);
    call.registerParameter(10, void.class, ParameterMode.REF_CURSOR);

    List<Object> resultSets = call.getResultList();
    Long count = (Long) call.getOutputParameterValue(9);

    CocoConnBridge.socialInvestConnection.closeSession();

    return TraderOrderHistory.builder()
      .count(count)
      .orderHistories(ReflexData.convertResultSetToObj(resultSets, TraderOrderHistoryItem.class))
      .build();
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TraderOrderHistoryItem {
    @ColumnIndex(name = "TRADER_ID", index = 0, ignoreIfNull = true)
    private Long traderId;
    @ColumnIndex(name = "COMMAND_ID", index = 1, ignoreIfNull = true)
    private String commandId;
    @ColumnIndex(name = "TICKER", index = 2, ignoreIfNull = true)
    private String ticker;
    @ColumnIndex(name = "SUBTYPECD", index = 3, ignoreIfNull = true)
    private String subtypeCd;
    @ColumnIndex(name = "CREATED_TIME", fmtIncaseDatetime = DateUtils.ORACLE_TIMESTAMP_FORMAT, index = 4, ignoreIfNull = true)
    private Date createdTime;
    @ColumnIndex(name = "QUOTE_QUANTITY", index = 5, ignoreIfNull = true)
    private Long quoteQuantity;
    @ColumnIndex(name = "MATCHING_VOLUME", index = 6, ignoreIfNull = true)
    private Long matchingVolume;
    @ColumnIndex(name = "PROPORTION", index = 7, ignoreIfNull = true)
    private Double proportion;
    @ColumnIndex(name = "PRICE", index = 8, ignoreIfNull = true)
    private Double price;
    @ColumnIndex(name = "ORDER_SIDE", index = 9, ignoreIfNull = true)
    private String orderSide;
  }
}
