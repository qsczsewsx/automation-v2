package com.tcbs.automation.evoting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class VoteResponse {
  @Id
  private Integer id;
  private String url;
  private String name;
  private String campaignCode;
  private String description;
  private String startDate;
  private String endDate;
}
