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
public class ProfilePersonalDto {
  private String fullName;

  private String firstName;

  private String lastName;

  private String email;

  private String phoneNumber;

  private String gender;

  private String contactAddress;

  private String permanentAddress;

  private String job;

  private String nationality;

  private Integer referer;

  private Long userId;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null) return CompareUtils.isObjEmpty(this);
    if (getClass() != obj.getClass())
      return false;
    ProfilePersonalDto other = (ProfilePersonalDto) obj;
    if (contactAddress == null) {
      if (other.contactAddress != null)
        return false;
    } else if (!contactAddress.equals(other.contactAddress))
      return false;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (firstName == null) {
      if (other.firstName != null)
        return false;
    } else if (!firstName.equals(other.firstName))
      return false;
    if (fullName == null) {
      if (other.fullName != null)
        return false;
    } else if (!fullName.equals(other.fullName))
      return false;
    if (gender == null) {
      if (other.gender != null)
        return false;
    } else if (!gender.equals(other.gender))
      return false;
    if (job == null) {
      if (other.job != null)
        return false;
    } else if (!job.equals(other.job))
      return false;
    if (lastName == null) {
      if (other.lastName != null)
        return false;
    } else if (!lastName.equals(other.lastName))
      return false;
    if (nationality == null) {
      if (other.nationality != null)
        return false;
    } else if (!nationality.equals(other.nationality))
      return false;
    if (permanentAddress == null) {
      if (other.permanentAddress != null)
        return false;
    } else if (!permanentAddress.equals(other.permanentAddress))
      return false;
    if (phoneNumber == null) {
      if (other.phoneNumber != null)
        return false;
    } else if (!phoneNumber.equals(other.phoneNumber))
      return false;
    if (referer == null) {
      if (other.referer != null)
        return false;
    } else if (!referer.equals(other.referer))
      return false;
    if (userId == null) {
      if (other.userId != null)
        return false;
    } else if (!userId.equals(other.userId))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((contactAddress == null) ? 0 : contactAddress.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
    result = prime * result + ((gender == null) ? 0 : gender.hashCode());
    result = prime * result + ((job == null) ? 0 : job.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((nationality == null) ? 0 : nationality.hashCode());
    result = prime * result + ((permanentAddress == null) ? 0 : permanentAddress.hashCode());
    result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
    result = prime * result + ((referer == null) ? 0 : referer.hashCode());
    result = prime * result + ((userId == null) ? 0 : userId.hashCode());
    return result;
  }
}
