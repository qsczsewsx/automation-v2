package com.tcbs.automation.dwh.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBondStaticRequest {
  private Integer bondStaticId;
  private String name;
  private String code;
  private Long par;
  private String currency;
  private String issueDate;
  private String expiredDate;
  private Integer bondIssuerId;
}
