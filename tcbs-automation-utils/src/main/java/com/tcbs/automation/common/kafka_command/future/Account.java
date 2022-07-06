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
public class Account implements Serializable {
  private Long id;
  private String tcbsId;
  private String custodyId;
  private String subAccount;
}
