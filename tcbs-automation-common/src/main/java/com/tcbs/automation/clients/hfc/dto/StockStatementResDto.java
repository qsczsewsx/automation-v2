package com.tcbs.automation.clients.hfc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcbs.automation.tools.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockStatementResDto {
  @Builder.Default
  private String object = "stockStatement";
  private String accountNo;
  private String custodyId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_WITH_TIMEZONE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  private Date transactionDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_WITH_TIMEZONE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  private Date businessDate;
  private String transactionNum;
  private String transactionCode;
  private String transactionName;
  private String descriptions;
  private String symbol;
  private Double creditQuantity;
  private Double creditAmount;
  private Double debitQuantity;
  private Double debitAmount;
  private Double sellAmount;
  private Double feeTaxAmount;
}
