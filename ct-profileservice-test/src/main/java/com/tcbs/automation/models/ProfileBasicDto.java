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
public class ProfileBasicDto {

  private String tcbsId;

  private String code105C;

  private String status;

  private String type;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfileBasicDto other = (ProfileBasicDto) obj;
    if (code105C == null) {
      if (other.code105C != null)
        return false;
    } else if (!code105C.equals(other.code105C))
      return false;
    if (status == null) {
      if (other.status != null)
        return false;
    } else if (!status.equals(other.status))
      return false;
    if (tcbsId == null) {
      if (other.tcbsId != null)
        return false;
    } else if (!tcbsId.equals(other.tcbsId))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((code105C == null) ? 0 : code105C.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + ((tcbsId == null) ? 0 : tcbsId.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }
}
