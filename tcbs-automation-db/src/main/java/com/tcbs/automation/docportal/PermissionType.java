package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERMISSION_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionType extends BaseEntity {

  @Id
  @Column(name = "PERMISSION_TYPE", columnDefinition = "VARCHAR(50)", length = 50)
  private String permissionType;

  @Column(name = "PERMISSION_NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String permissionName;

}
