package com.tcbs.automation.bondlifecycle.dto;

public class ParticipantDto extends ApprovalDto {
  private Integer id;

  private String name;

  private String nameNoAccent;

  private Boolean paramQueryApproval;

  public Boolean getParamQueryApproval() {
    return paramQueryApproval;
  }

  public void setParamQueryApproval(Boolean paramQueryApproval) {
    this.paramQueryApproval = paramQueryApproval;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameNoAccent() {
    return nameNoAccent;
  }


}
