package com.tcbs.automation.docportal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilePermissionTypeDtoMapping extends PermissionTypeDto {

  @JsonIgnore
  private Integer fileId;

  public FilePermissionTypeDtoMapping(Integer fileId, String permissionType, String permissionName) {
    super(permissionType, permissionName);
    this.fileId = fileId;
  }
}
