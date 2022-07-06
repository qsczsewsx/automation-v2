package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REFERENCE_DATA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReferenceData extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "CODE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String code;

  @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String name;

  @Column(name = "NAME_NO_ACCENT", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String nameNoAccent;

  @Column(name = "CATEGORY_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String categoryType;

  @Column(name = "VALUE", columnDefinition = "NVARCHAR2(200) NULL", length = 200, nullable = true)
  private String value;

  @Column(name = "VALUE1", columnDefinition = "NVARCHAR2(200) NULL", length = 200, nullable = true)
  private String value1;

  @Column(name = "PARENTS", columnDefinition = "VARCHAR2(500) NULL", length = 500, nullable = true)
  private String parents;

  @Column(name = "ORDER_NUMBER", columnDefinition = "NUMBER(3) NULL", nullable = true)
  private String orderNumber;

  @Column(name = "IS_ACTIVE", columnDefinition = "NUMBER(1) DEFAULT 1", nullable = true)
  private Integer isActiveDB;

}
