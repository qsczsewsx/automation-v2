package com.tcbs.automation.models;

import com.tcbs.automation.tools.CompareUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserInfoDto {

  private String systemUserType;

  private String systemUser;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    SystemUserInfoDto other = (SystemUserInfoDto) obj;
    if (systemUser == null) {
      if (other.systemUser != null)
        return false;
    } else if (!systemUser.equals(other.systemUser))
      return false;
    if (systemUserType == null) {
      if (other.systemUserType != null)
        return false;
    } else if (!systemUserType.equals(other.systemUserType))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((systemUser == null) ? 0 : systemUser.hashCode());
    result = prime * result + ((systemUserType == null) ? 0 : systemUserType.hashCode());
    return result;
  }

}
