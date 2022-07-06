package com.tcbs.automation.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalInfo {

  private String makerUser;

  private String checkerUser;

  private String instanceId;

  private String updateSource;

  private List<Long> bankAccountIds;

  private List<Long> personEnterpriseIds;

  private String activationLink;
}
