package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ROLE_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleType extends BaseEntity {

  @Id
  @Column(name = "ROLE_TYPE", columnDefinition = "VARCHAR(50)", length = 50)
  private String roleType;

  @Column(name = "ROLE_NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String roleName;

}
