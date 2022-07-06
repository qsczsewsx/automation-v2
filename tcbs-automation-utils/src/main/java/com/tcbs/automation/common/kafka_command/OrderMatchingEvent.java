package com.tcbs.automation.common.kafka_command;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class OrderMatchingEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  private String orderId;
  private String accountNumber;
  private String custodyId;
  private String symbol;
  private Double quotePrice;
  private Double quoteQuantity;
  private Double executedQuantity;
  private Double matchPrice;
  private Double matchingQuantity;
  private String orderSide;
  private String sessionEx;
  private String typeCd;
  private String subtypeCd;
  private String board;
  private Date createdDate;
}
