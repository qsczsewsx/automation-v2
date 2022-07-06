package com.tcbs.automation.dwh.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBondAttrRequest {
  private Long id;
  private String name;
  private String value;
  private String caption;
  private Integer bondStaticId;
  private String timestamp;
  private String applyType;
}
