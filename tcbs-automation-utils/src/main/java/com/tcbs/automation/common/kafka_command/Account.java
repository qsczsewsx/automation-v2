package com.tcbs.automation.common.kafka_command;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Account implements Serializable {
  private Long id;
  private Long accountID;
  private String tcbsID;
  private String custodyID;
  private String accountNo;

  public Account() {
  }

  public Account(Long id, long accountId, String tcbsID, String custodyID, String accountNo) {
    this.id = id;
    this.accountID = accountId;
    this.tcbsID = tcbsID;
    this.custodyID = custodyID;
    this.accountNo = accountNo;
  }
}
