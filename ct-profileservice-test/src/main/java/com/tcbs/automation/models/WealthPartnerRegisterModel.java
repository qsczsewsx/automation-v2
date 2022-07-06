package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WealthPartnerRegisterModel {
  private String custodyCode;
  private String tcbsId;
  private String iwealthPartnerCode;
  private String iwealthPartnerChannel;
}
