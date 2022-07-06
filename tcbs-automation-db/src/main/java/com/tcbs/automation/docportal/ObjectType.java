package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OBJECT_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectType extends BaseEntity {

  @Id
  @Column(name = "OBJECT_TYPE", columnDefinition = "VARCHAR(50)", length = 50)
  private String objectType;

  @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String name;

  @Column(name = "STATUS", columnDefinition = "NUMBER(1)", length = 200, nullable = false)
  private boolean status;

}
