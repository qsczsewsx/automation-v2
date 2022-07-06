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
public class EnterprisePersonInfoDto {

  private Long id;

  private String fullName;

  private Date birthday;

  private String position;

  private String email;

  private String phone;

  private String idNumber;

  private String idPlace;

  private Date idDate;

  private String address;

  private String citizenShip;

  private Long idType;

  private String typePerson;// 0-Nguoi dai dien, 1-ke toan truong, 2- Nguoi lien he, 3- Nguoi uy
  // quyen

  private Date permissFromDate;

  private Date permissToDate;

  private String authorities;

  private String note;

  private String groupAuthorities;

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    EnterprisePersonInfoDto other = (EnterprisePersonInfoDto) obj;
    if (address == null) {
      if (other.address != null)
        return false;
    } else if (!address.equals(other.address))
      return false;
    if (authorities == null) {
      if (other.authorities != null)
        return false;
    } else if (!authorities.equals(other.authorities))
      return false;
    if (birthday == null) {
      if (other.birthday != null)
        return false;
    } else if (!birthday.equals(other.birthday))
      return false;
    if (citizenShip == null) {
      if (other.citizenShip != null)
        return false;
    } else if (!citizenShip.equals(other.citizenShip))
      return false;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    if (fullName == null) {
      if (other.fullName != null)
        return false;
    } else if (!fullName.equals(other.fullName))
      return false;
    if (groupAuthorities == null) {
      if (other.groupAuthorities != null)
        return false;
    } else if (!groupAuthorities.equals(other.groupAuthorities))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (idDate == null) {
      if (other.idDate != null)
        return false;
    } else if (!idDate.equals(other.idDate))
      return false;
    if (idNumber == null) {
      if (other.idNumber != null)
        return false;
    } else if (!idNumber.equals(other.idNumber))
      return false;
    if (idPlace == null) {
      if (other.idPlace != null)
        return false;
    } else if (!idPlace.equals(other.idPlace))
      return false;
    if (idType == null) {
      if (other.idType != null)
        return false;
    } else if (!idType.equals(other.idType))
      return false;
    if (note == null) {
      if (other.note != null)
        return false;
    } else if (!note.equals(other.note))
      return false;
    if (permissFromDate == null) {
      if (other.permissFromDate != null)
        return false;
    } else if (!permissFromDate.equals(other.permissFromDate))
      return false;
    if (permissToDate == null) {
      if (other.permissToDate != null)
        return false;
    } else if (!permissToDate.equals(other.permissToDate))
      return false;
    if (phone == null) {
      if (other.phone != null)
        return false;
    } else if (!phone.equals(other.phone))
      return false;
    if (position == null) {
      if (other.position != null)
        return false;
    } else if (!position.equals(other.position))
      return false;
    if (typePerson == null) {
      if (other.typePerson != null)
        return false;
    } else if (!typePerson.equals(other.typePerson))
      return false;
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((address == null) ? 0 : address.hashCode());
    result = prime * result + ((authorities == null) ? 0 : authorities.hashCode());
    result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
    result = prime * result + ((citizenShip == null) ? 0 : citizenShip.hashCode());
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
    result = prime * result + ((groupAuthorities == null) ? 0 : groupAuthorities.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((idDate == null) ? 0 : idDate.hashCode());
    result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
    result = prime * result + ((idPlace == null) ? 0 : idPlace.hashCode());
    result = prime * result + ((idType == null) ? 0 : idType.hashCode());
    result = prime * result + ((note == null) ? 0 : note.hashCode());
    result = prime * result + ((permissFromDate == null) ? 0 : permissFromDate.hashCode());
    result = prime * result + ((permissToDate == null) ? 0 : permissToDate.hashCode());
    result = prime * result + ((phone == null) ? 0 : phone.hashCode());
    result = prime * result + ((position == null) ? 0 : position.hashCode());
    result = prime * result + ((typePerson == null) ? 0 : typePerson.hashCode());
    return result;
  }

}
