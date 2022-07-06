package com.tcbs.automation.intermana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDto {
  @Id
  private Integer id;
  private String fullName;
  private String identityNumber;
  private String identityDate;
  private String tcbsId;
  private String custodyId;
  private String createdAt;
  private String updatedAt;
}
