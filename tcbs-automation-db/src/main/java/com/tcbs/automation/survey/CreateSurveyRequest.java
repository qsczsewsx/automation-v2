package com.tcbs.automation.survey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSurveyRequest {
  private String campaignCode;
  private String url;
  private String name;
  private boolean isPublic;
  private boolean isSecured;
}
