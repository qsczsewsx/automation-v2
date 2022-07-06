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
public class EnterprisePersonInfo {

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

}
