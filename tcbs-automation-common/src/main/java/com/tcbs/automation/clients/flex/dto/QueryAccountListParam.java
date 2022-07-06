package com.tcbs.automation.clients.flex.dto;

import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryAccountListParam {
  private String custodyId;
  private String accountNo;
  private Boolean isMargin;
  private Constants.FlexAccType type;

  private Boolean iCopyAccount;
  private Boolean excludeStopped;

  public Map<String, Object> toFlexQueryParam() {
    Map<String, Object> params = new HashMap<>();
    params.put("custodyID", custodyId);
    if (accountNo != null) {
      params.put("accountNo", accountNo);
    }
    if (isMargin != null) {
      params.put("marginAccount", isMargin ? "Y" : "N");
    }
    if (type != null) {
      params.put("accountType", type.getStringValue());
    }

    return params;
  }

  public Map<String, Object> toICopyQueryParam() {
    Map<String, Object> params = new HashMap<>();
    params.put("custodyId", custodyId);
    if (accountNo != null) {
      params.put("accountNo", accountNo);
    }
    if (type != null) {
      params.put("accountType", type.getStringValue());
    }
    if (iCopyAccount != null) {
      params.put("icopyAccount", iCopyAccount);
    }
    if (excludeStopped != null) {
      params.put("excludeStopped", excludeStopped);
    }

    return params;
  }
}
