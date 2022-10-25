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
public class OrderEventDto implements Serializable {
  private String group;
  private String originAccountCode;
  private Integer orderNo;
  private String accountCode;
  private String side;
  private String symbol;
  private Integer volume;
  private String price;
  private Integer matchVolume;
  private String matchValue;
  private String status;
  private String channel;
  private String orderTime;
  private String msgType;
  private String market;
  private String shareStatus;
  private String type;
  private String pkOrderNo;
  private String authorisedAccountCode;
  private String permission;
  private String hnxConfirmID;
  private String branch;
  private String subBranch;
  private String marketingID;
  private String restrictionList;
  private Integer userLevel;
  private String accountType;
  private String quotes;
  private String cancelTime;
  private String info;
  private String orderType;
  private String autoType;
  private String productType;
  private String changeTime;
  private Integer orderVolume;
  private String traderCode;
  private String confirmNumber;
  /**
   * the net of {@param accountCode} with the {@param symbol}
   */
  private Double net;
  /**
   * matched price in the deal
   */
  private Double matchDealPrice;
  /**
   * matched volume in the deal
   */
  private Integer matchDealVolume;
}