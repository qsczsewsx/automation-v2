package com.tcbs.automation.common.kafka_command.future;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PositionProportion implements Serializable {
  private String side;
  private Double proportion;
  private Long quantity;
}
