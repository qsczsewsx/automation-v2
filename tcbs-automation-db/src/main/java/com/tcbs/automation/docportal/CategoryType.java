package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CATEGORY_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryType extends BaseEntity {
  @Id
  @Column(name = "CATEGORY_TYPE")
  private String cateType;

  @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String name;

  @Column(name = "NAME_NO_ACCENT", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String nameNoAccent;

  @Column(name = "IS_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 1", nullable = true)
  private Integer isActiveDB;

}
