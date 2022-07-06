package com.tcbs.automation.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  private ProfileBasic profileBasic;

  private ProfilePersonal profilePersonal;

  private List<ProfileBankAccount> profileBankAccounts;

  private ProfileAccountStatus profileAccountStatus;

  private List<ProfileBook> profileBooks;

  private List<ProfileUploadedDocument> profileUploadedDocuments;

  private ProfileOtpChannel profileOtpChannel;

  private SystemUserInfo systemUserInfo;

  private AdditionalInfo additionalInfo;

  private WealthPartnerRegisterModel wealthPartnerInfo;

  private EnterpriseInfo enterpriseInfo;

}
