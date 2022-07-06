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
public class ProfilePersonal {

  private String fullName;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private String gender;

  private Date birthday;

  private String contactAddress;

  private String permanentAddress;

  private String job;

  private String nationality;

  private Integer referer;

  private IdentityCard identityCard;

  private Original original;

  private String taxIdNumber;

  private String acronym;

  private String avatarData;

  private String province;

  private String typeAccount;

  private Long userId;

  private Integer relationship;

  private Date createdDate;

  private Date updatedDate;

  private String profilePicture;

  private Integer honorific;

  private String rmValue;

  private Integer flowOpenAccount;

  private String envelopeId;

  private String checkHardCopy;

  private Boolean editable;

  private String referralCode;

  private Integer statusProcess;

  private String branchCode;

  private String branchName;

  private String homePhone;

  private String fax;

  private String openAccountData;

  private VideoKyc videoKyc;

  private String contractPayload;

  private String password;

  private String refId;

}
