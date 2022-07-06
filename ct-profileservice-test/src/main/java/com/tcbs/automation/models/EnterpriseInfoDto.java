package com.tcbs.automation.models;

import java.util.List;

import com.tcbs.automation.tools.CompareUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnterpriseInfoDto {

  private EnterprisePersonInfoDto chiefAccountantInfo;

  private List<EnterprisePersonInfoDto> representativePersons;

  private List<EnterprisePersonInfoDto> contactPersons;

  private List<EnterprisePersonInfoDto> authorizedPersons;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    EnterpriseInfoDto other = (EnterpriseInfoDto) obj;
    if (authorizedPersons == null) {
      if (other.authorizedPersons != null)
        return false;
    } else if (!authorizedPersons.equals(other.authorizedPersons))
      return false;
    if (chiefAccountantInfo == null) {
      if (other.chiefAccountantInfo != null)
        return false;
    } else if (!chiefAccountantInfo.equals(other.chiefAccountantInfo))
      return false;
    if (contactPersons == null) {
      if (other.contactPersons != null)
        return false;
    } else if (!contactPersons.equals(other.contactPersons))
      return false;
    if (representativePersons == null) {
      if (other.representativePersons != null)
        return false;
    } else if (!representativePersons.equals(other.representativePersons))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((authorizedPersons == null) ? 0 : authorizedPersons.hashCode());
    result = prime * result
        + ((chiefAccountantInfo == null) ? 0 : chiefAccountantInfo.hashCode());
    result = prime * result + ((contactPersons == null) ? 0 : contactPersons.hashCode());
    result = prime * result
        + ((representativePersons == null) ? 0 : representativePersons.hashCode());
    return result;
  }

}
