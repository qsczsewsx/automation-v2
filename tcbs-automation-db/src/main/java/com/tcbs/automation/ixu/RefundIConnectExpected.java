package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class RefundIConnectExpected {

  private String refundDate;
  private int totalRefund;
  private int totalOrderDetails;
}
