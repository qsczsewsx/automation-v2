package com.automation.common.kafka_command.future;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlacedOrder implements Serializable {
  private String symbol;
  private String orderType;
  private Double matchingPrice;
  private String side;
  private String board;
  private String orderId;
  private boolean inAtc;
}
