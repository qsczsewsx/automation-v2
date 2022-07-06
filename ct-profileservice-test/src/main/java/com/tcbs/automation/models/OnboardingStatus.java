package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStatus {

  private StatusInfo activatedStatus;

  private StatusInfo preferActivationChannelStatus;

  private StatusInfo idStatus;

  private StatusInfo bankInfoStatus;

  private StatusInfo eKycStatus;

  private StatusInfo eContractStatus;

  private StatusInfo counterKycStatus;

  private StatusInfo documentStatus;

  private StatusInfo accountPermissionStatus;

}
