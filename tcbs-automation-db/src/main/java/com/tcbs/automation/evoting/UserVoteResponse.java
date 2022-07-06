package com.tcbs.automation.evoting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Id;

@Getter
@Setter
@NoArgsConstructor
public class UserVoteResponse {
  @Id
  private Integer id;
  private String campaignCode;
  private String name;
  private String custodycd;
  private String fundAmount;
  private String fullName;
  private String status;
  private String createdAt;
  private String updatedAt;
}
