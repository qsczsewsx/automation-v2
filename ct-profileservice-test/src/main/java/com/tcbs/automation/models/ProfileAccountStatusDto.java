package com.tcbs.automation.models;

import java.util.Date;

import com.tcbs.automation.tools.CompareUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileAccountStatusDto {

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

  private OnboardingStatusDto onboardingStatus;

  private Boolean hasPermBondPt;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfileAccountStatusDto other = (ProfileAccountStatusDto) obj;
    if (confirmed105CStatus == null) {
      if (other.confirmed105CStatus != null)
        return false;
    } else if (!confirmed105CStatus.equals(other.confirmed105CStatus))
      return false;
    if (docusignStatus == null) {
      if (other.docusignStatus != null)
        return false;
    } else if (!docusignStatus.equals(other.docusignStatus))
      return false;
    if (flexAccount == null) {
      if (other.flexAccount != null)
        return false;
    } else if (!flexAccount.equals(other.flexAccount))
      return false;
    if (flexActivationDate == null) {
      if (other.flexActivationDate != null)
        return false;
    } else if (!flexActivationDate.equals(other.flexActivationDate))
      return false;
    if (flexActivationStatus == null) {
      if (other.flexActivationStatus != null)
        return false;
    } else if (!flexActivationStatus.equals(other.flexActivationStatus))
      return false;
    if (fundAccount == null) {
      if (other.fundAccount != null)
        return false;
    } else if (!fundAccount.equals(other.fundAccount))
      return false;
    if (fundActivationDate == null) {
      if (other.fundActivationDate != null)
        return false;
    } else if (!fundActivationDate.equals(other.fundActivationDate))
      return false;
    if (fundActivationStatus == null) {
      if (other.fundActivationStatus != null)
        return false;
    } else if (!fundActivationStatus.equals(other.fundActivationStatus))
      return false;
    if (hasPermBondPt == null) {
      if (other.hasPermBondPt != null)
        return false;
    } else if (!hasPermBondPt.equals(other.hasPermBondPt))
      return false;
    if (onboardingStatus == null) {
      if (other.onboardingStatus != null)
        return false;
    } else if (!onboardingStatus.equals(other.onboardingStatus))
      return false;
    if (tcBondAccount == null) {
      if (other.tcBondAccount != null)
        return false;
    } else if (!tcBondAccount.equals(other.tcBondAccount))
      return false;
    if (transferStatus == null) {
      if (other.transferStatus != null)
        return false;
    } else if (!transferStatus.equals(other.transferStatus))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((confirmed105CStatus == null) ? 0 : confirmed105CStatus.hashCode());
    result = prime * result + ((docusignStatus == null) ? 0 : docusignStatus.hashCode());
    result = prime * result + ((flexAccount == null) ? 0 : flexAccount.hashCode());
    result = prime * result
        + ((flexActivationDate == null) ? 0 : flexActivationDate.hashCode());
    result = prime * result
        + ((flexActivationStatus == null) ? 0 : flexActivationStatus.hashCode());
    result = prime * result + ((fundAccount == null) ? 0 : fundAccount.hashCode());
    result = prime * result
        + ((fundActivationDate == null) ? 0 : fundActivationDate.hashCode());
    result = prime * result
        + ((fundActivationStatus == null) ? 0 : fundActivationStatus.hashCode());
    result = prime * result + ((hasPermBondPt == null) ? 0 : hasPermBondPt.hashCode());
    result = prime * result + ((onboardingStatus == null) ? 0 : onboardingStatus.hashCode());
    result = prime * result + ((tcBondAccount == null) ? 0 : tcBondAccount.hashCode());
    result = prime * result + ((transferStatus == null) ? 0 : transferStatus.hashCode());
    return result;
  }

}
