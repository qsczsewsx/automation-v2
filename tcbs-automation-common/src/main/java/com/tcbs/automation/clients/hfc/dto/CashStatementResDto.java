package com.tcbs.automation.clients.hfc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CashStatementResDto {
  @Builder.Default
  private String object = "cashStatement";
  private String accountNo;
  @JsonProperty("custodyID") // typo of flex
  private String custodyId;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_WITH_TIMEZONE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  @JsonProperty("transationDate") // typo of flex
  private Date transactionDate;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtils.DATE_WITH_TIMEZONE_FORMAT, timezone = DateUtils.TIMEZONE_VN)
  private Date businessDate;
  private String transactionNum;
  private String transactionCode;
  private String transactionName;
  private String descriptions;
  private Double creditAmount;
  private Double debitAmount;
}
