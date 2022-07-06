package com.tcbs.automation.common.kafka_command.future;

import com.tcbs.automation.common.kafka_command.BaseCmd;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SOrderStatusMsg extends BaseCmd {
  private String commandFlow;
  private String custody;
  private String accountNo;
  private String side;
  private String symbol;
  private Long quantity;
  private Long executedQty;
  private Double matchPrice;
  private String status;
  private String fsPkOrderId;
  private String fsDate;
}
