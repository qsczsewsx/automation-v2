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
public class StatusInfoDto {

  private String value;

  private String rejectReason;

  private String rejectContent;

  private String rejectPerson;

  private Date updatedDate;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    StatusInfoDto other = (StatusInfoDto) obj;
    if (rejectContent == null) {
      if (other.rejectContent != null)
        return false;
    } else if (!rejectContent.equals(other.rejectContent))
      return false;
    if (rejectPerson == null) {
      if (other.rejectPerson != null)
        return false;
    } else if (!rejectPerson.equals(other.rejectPerson))
      return false;
    if (rejectReason == null) {
      if (other.rejectReason != null)
        return false;
    } else if (!rejectReason.equals(other.rejectReason))
      return false;
    if (updatedDate == null) {
      if (other.updatedDate != null)
        return false;
    } else if (!updatedDate.equals(other.updatedDate))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((rejectContent == null) ? 0 : rejectContent.hashCode());
    result = prime * result + ((rejectPerson == null) ? 0 : rejectPerson.hashCode());
    result = prime * result + ((rejectReason == null) ? 0 : rejectReason.hashCode());
    result = prime * result + ((updatedDate == null) ? 0 : updatedDate.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

}
