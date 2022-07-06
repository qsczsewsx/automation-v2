package com.tcbs.automation.bondlifecycle.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusinessDto extends ApprovalDto {

  private Integer id;

  private String name;

  private String nameNoAccent;

  private ReferenceDto bondTypeCodeRef;

  private Boolean paramQueryApproval;
}
