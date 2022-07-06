package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStatusDto {

  private StatusInfoDto activatedStatus;

  private StatusInfoDto preferActivationChannelStatus;

  private StatusInfoDto idStatus;

  private StatusInfoDto bankInfoStatus;

  private StatusInfoDto eKycStatus;

  private StatusInfoDto eContractStatus;

  private StatusInfoDto counterKycStatus;

  private StatusInfoDto documentStatus;

  private StatusInfoDto accountPermissionStatus;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    OnboardingStatusDto other = (OnboardingStatusDto) obj;
    if (accountPermissionStatus == null) {
      if (other.accountPermissionStatus != null)
        return false;
    } else if (!accountPermissionStatus.equals(other.accountPermissionStatus))
      return false;
    if (activatedStatus == null) {
      if (other.activatedStatus != null)
        return false;
    } else if (!activatedStatus.equals(other.activatedStatus))
      return false;
    if (bankInfoStatus == null) {
      if (other.bankInfoStatus != null)
        return false;
    } else if (!bankInfoStatus.equals(other.bankInfoStatus))
      return false;
    if (counterKycStatus == null) {
      if (other.counterKycStatus != null)
        return false;
    } else if (!counterKycStatus.equals(other.counterKycStatus))
      return false;
    if (documentStatus == null) {
      if (other.documentStatus != null)
        return false;
    } else if (!documentStatus.equals(other.documentStatus))
      return false;
    if (eContractStatus == null) {
      if (other.eContractStatus != null)
        return false;
    } else if (!eContractStatus.equals(other.eContractStatus))
      return false;
    if (eKycStatus == null) {
      if (other.eKycStatus != null)
        return false;
    } else if (!eKycStatus.equals(other.eKycStatus))
      return false;
    if (idStatus == null) {
      if (other.idStatus != null)
        return false;
    } else if (!idStatus.equals(other.idStatus))
      return false;
    if (preferActivationChannelStatus == null) {
      if (other.preferActivationChannelStatus != null)
        return false;
    } else if (!preferActivationChannelStatus.equals(other.preferActivationChannelStatus))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((accountPermissionStatus == null) ? 0 : accountPermissionStatus.hashCode());
    result = prime * result + ((activatedStatus == null) ? 0 : activatedStatus.hashCode());
    result = prime * result + ((bankInfoStatus == null) ? 0 : bankInfoStatus.hashCode());
    result = prime * result + ((counterKycStatus == null) ? 0 : counterKycStatus.hashCode());
    result = prime * result + ((documentStatus == null) ? 0 : documentStatus.hashCode());
    result = prime * result + ((eContractStatus == null) ? 0 : eContractStatus.hashCode());
    result = prime * result + ((eKycStatus == null) ? 0 : eKycStatus.hashCode());
    result = prime * result + ((idStatus == null) ? 0 : idStatus.hashCode());
    result = prime * result + ((preferActivationChannelStatus == null) ? 0
        : preferActivationChannelStatus.hashCode());
    return result;
  }

}
