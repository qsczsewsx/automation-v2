package com.tcbs.automation.clients.flex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlexAccountInfoItemDto {
  @JsonProperty("accountNo")
  private String accountNo;

  @JsonProperty("aftype")
  private String afType;

  @JsonProperty("accountType")
  private String accountType;

  private String bankName;

  @JsonProperty("marginAccount")
  private String marginAccount;

  private String fullName;

  public boolean isMarginAccount() {
    return "Y".equals(marginAccount);
  }
}