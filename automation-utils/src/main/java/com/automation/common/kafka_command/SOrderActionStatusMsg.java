package com.automation.common.kafka_command;

import com.automation.constants.coco.Constants;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
public class SOrderActionStatusMsg implements Serializable {
  private String action;
  private String commandId;
  private String status;
  private Constants.CopierAction commandFlow;
  private String accountNo;
  private String symbol;
  private Long quantity;
  private Long executedQty;
  private Double matchPrice;
  private String cmdTraceId;
  private String hftOrderId;
  private String hftTxDate;

  public SOrderActionStatusMsg() {
  }
}
