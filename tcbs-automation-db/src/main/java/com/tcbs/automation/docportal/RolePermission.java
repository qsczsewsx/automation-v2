package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ROLE_PERMISSION")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermission extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "ROLE_TYPE", columnDefinition = "VARCHAR(50)", length = 50, updatable = false)
  private String roleType;

  @Column(name = "PERMISSION_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String permissionType;

}
