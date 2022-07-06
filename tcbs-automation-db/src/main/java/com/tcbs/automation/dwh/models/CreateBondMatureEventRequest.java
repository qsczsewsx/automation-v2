package com.tcbs.automation.dwh.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBondMatureEventRequest {
  private Long id;
  private String name;
  private String code;
  private Long price;
  private String publicDate;
  private String expiredDate;
  private String seriesBondCode;
  private Integer issuerId;
}
