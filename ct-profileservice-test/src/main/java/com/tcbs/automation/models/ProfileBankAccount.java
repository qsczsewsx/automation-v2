package com.tcbs.automation.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileBankAccount {

  private Long bankAccountId;

  private String accountNo;

  private String accountName;

  private String bankCode;

  private String bankName;

  private String bankProvince;

  private String branchCode;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ProfileBankAccount other = (ProfileBankAccount) obj;
    if (accountName == null) {
      if (other.accountName != null)
        return false;
    } else if (!accountName.equals(other.accountName))
      return false;
    if (accountNo == null) {
      if (other.accountNo != null)
        return false;
    } else if (!accountNo.equals(other.accountNo))
      return false;
    if (bankAccountId == null) {
      if (other.bankAccountId != null)
        return false;
    } else if (!bankAccountId.equals(other.bankAccountId))
      return false;
    if (bankCode == null) {
      if (other.bankCode != null)
        return false;
    } else if (!bankCode.equals(other.bankCode))
      return false;
    if (bankName == null) {
      if (other.bankName != null)
        return false;
    } else if (!bankName.equals(other.bankName))
      return false;
    if (bankProvince == null) {
      if (other.bankProvince != null)
        return false;
    } else if (!bankProvince.equals(other.bankProvince))
      return false;
    if (branchCode == null) {
      if (other.branchCode != null)
        return false;
    } else if (!branchCode.equals(other.branchCode))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
    result = prime * result + ((accountNo == null) ? 0 : accountNo.hashCode());
    result = prime * result + ((bankAccountId == null) ? 0 : bankAccountId.hashCode());
    result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
    result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
    result = prime * result + ((bankProvince == null) ? 0 : bankProvince.hashCode());
    result = prime * result + ((branchCode == null) ? 0 : branchCode.hashCode());
    return result;
  }

}
