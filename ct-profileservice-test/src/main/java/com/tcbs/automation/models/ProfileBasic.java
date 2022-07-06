package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileBasic {

  private String tcbsId;

  private String code105C;

  private String status;

  private String type;
}
