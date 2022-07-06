package com.tcbs.automation.models;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileAccountStatus {

  private String fundActivationStatus;

  private Date fundActivationDate;

  private String fundAccount;

  private String flexActivationStatus;

  private Date flexActivationDate;

  private String flexAccount;

  private String tcBondAccount;

  private String transferStatus;

  private String confirmed105CStatus;

  private String docusignStatus;

  private OnboardingStatus onboardingStatus;

  private Boolean hasPermBondPt;

}
