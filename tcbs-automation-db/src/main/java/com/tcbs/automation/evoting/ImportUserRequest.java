package com.tcbs.automation.evoting;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ImportUserRequest {
  private String campaignCode;
  private List<UserInfo> content;
}
