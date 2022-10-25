package com.automation.common.kafka_command;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Account implements Serializable {
  private Long id;
  private Long accountID;
  private String xxxxID;
  private String custodyID;
  private String accountNo;

  public Account() {
  }

  public Account(Long id, long accountId, String xxxxID, String custodyID, String accountNo) {
    this.id = id;
    this.accountID = accountId;
    this.xxxxID = xxxxID;
    this.custodyID = custodyID;
    this.accountNo = accountNo;
  }
}
