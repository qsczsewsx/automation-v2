package com.tcbs.automation.devmana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterInfoDto {
  private String topic;
  private String tcbsId;
  private String groupName;
  @Id
  private String device_uuid;
  private String deviceToken;
}
