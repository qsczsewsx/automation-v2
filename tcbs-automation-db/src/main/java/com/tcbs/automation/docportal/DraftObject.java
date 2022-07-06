package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "DRAFT_OBJECT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DraftObject extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(9)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "OBJECT_TYPE", columnDefinition = "VARCHAR(50)", length = 50)
  private String objectType;

  @Column(name = "CREATOR", columnDefinition = "NVARCHAR2(100)", length = 50)
  private String creator;

  @Column(name = "VALUE", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String value;

  public DraftObject(String objectType, String creator, String value) {
    this.objectType = objectType;
    this.creator = creator;
    this.value = value;
  }
}
