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
public class ProfileDto {

//    private Profile profile;

  public ProfileBasicDto basicInfo;

  public ProfilePersonalDto personalInfo;

  public List<ProfileBankAccountDto> bankAccounts;

  public ProfileAccountStatusDto accountStatus;

  public List<ProfileBookDto> books;

  public List<ProfileUploadedDocumentDto> uploadedDocuments;

  public ProfileOtpChannelDto otp;

  public SystemUserInfoDto systemUserInfo;

  public EnterpriseInfoDto enterpriseInfo;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ProfileDto other = (ProfileDto) obj;
    if (accountStatus == null) {
      if (other.accountStatus != null)
        return false;
    } else if (!accountStatus.equals(other.accountStatus))
      return false;
    if (bankAccounts == null) {
      if (other.bankAccounts != null && !other.bankAccounts.isEmpty())
        return false;
    } else if (!bankAccounts.equals(other.bankAccounts))
      return false;
    if (basicInfo == null) {
      if (other.basicInfo != null)
        return false;
    } else if (!basicInfo.equals(other.basicInfo))
      return false;
    if (books == null) {
      if (other.books != null && !other.books.isEmpty())
        return false;
    } else if (!books.equals(other.books))
      return false;
    if (enterpriseInfo == null) {
      if (other.enterpriseInfo != null) {
        if ((null == other.enterpriseInfo.getAuthorizedPersons()
            || other.enterpriseInfo.getAuthorizedPersons().isEmpty())
            && null == other.enterpriseInfo.getChiefAccountantInfo()
            && (null == other.enterpriseInfo.getContactPersons()
            || other.enterpriseInfo.getContactPersons().isEmpty())
            && (null == other.enterpriseInfo.getRepresentativePersons()
            || other.enterpriseInfo.getRepresentativePersons().isEmpty()))
          return true;
        return false;
      }
    } else if (!enterpriseInfo.equals(other.enterpriseInfo))
      return false;
    if (otp == null) {
      if (other.otp != null)
        return false;
    } else if (!otp.equals(other.otp))
      return false;
    if (personalInfo == null) {
      if (other.personalInfo != null)
        return false;
    } else if (!personalInfo.equals(other.personalInfo))
      return false;
    if (systemUserInfo == null) {
      if (other.systemUserInfo != null)
        return false;
    } else if (!systemUserInfo.equals(other.systemUserInfo))
      return false;
    if (uploadedDocuments == null) {
      if (other.uploadedDocuments != null && !other.uploadedDocuments.isEmpty())
        return false;
    } else if (!uploadedDocuments.equals(other.uploadedDocuments))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accountStatus == null) ? 0 : accountStatus.hashCode());
    result = prime * result + ((bankAccounts == null) ? 0 : bankAccounts.hashCode());
    result = prime * result + ((basicInfo == null) ? 0 : basicInfo.hashCode());
    result = prime * result + ((books == null) ? 0 : books.hashCode());
    result = prime * result + ((enterpriseInfo == null) ? 0 : enterpriseInfo.hashCode());
    result = prime * result + ((otp == null) ? 0 : otp.hashCode());
    result = prime * result + ((personalInfo == null) ? 0 : personalInfo.hashCode());
    result = prime * result + ((systemUserInfo == null) ? 0 : systemUserInfo.hashCode());
    result = prime * result + ((uploadedDocuments == null) ? 0 : uploadedDocuments.hashCode());
    return result;
  }

}
