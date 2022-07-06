package com.tcbs.automation.common.kafka_command;

import com.tcbs.automation.constants.coco.Constants;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
public class CopierActionStatusMsg implements Serializable {
  public Account copier;
  public Constants.CopierCompleteActionType action;
  public Constants.CopierCompleteActionStatus status;
  private List<StockItem> stocks;
  public Double cash;
  public String traceId;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StockItem implements Serializable {
    private String symbol;
    private Long volume;
    private Double costPrice;
    private Double currentPrice;
    private String orderId;
    private String hftTxDate;
  }

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Account implements Serializable {
    private Long id;
    private Long accountID;
    private String tcbsID;
    private String custodyID;
    private String accountNo;
  }
}