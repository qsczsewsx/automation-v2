package com.tcbs.automation.evoting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoteRequest {
  private String url;
  private String name;
  private String campaignCode;
  private String description;
  private String startDate;
  private String endDate;
}
