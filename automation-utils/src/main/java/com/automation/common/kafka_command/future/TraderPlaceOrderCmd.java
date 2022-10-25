package com.automation.common.kafka_command.future;

import com.automation.common.kafka_command.BaseCmd;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
public class TraderPlaceOrderCmd extends BaseCmd {
  private Account trader;
  private PlacedOrder order;
  private PositionProportion opened;
  private PositionProportion closed;
}
