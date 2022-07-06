package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "OBJECT_ATTRIBUTE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectAttribute extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(6)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "OBJECT_TYPE", columnDefinition = "VARCHAR(100)", nullable = false)
  private String objectType;

  @Column(name = "DATA_TYPE", columnDefinition = "VARCHAR(100)", nullable = false)
  private String searchDataType;

  @Column(name = "UI_TYPE", columnDefinition = "VARCHAR(100)", nullable = false)
  private String uiType;

  @Column(name = "KEY", columnDefinition = "VARCHAR(100)", length = 100, nullable = false)
  private String key;

  @Column(name = "NAME", columnDefinition = "VARCHAR(100)", length = 100, nullable = false)
  private String name;

  @Column(name = "SEARCH_ATTRIBUTE", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String searchAttr;

  @Column(name = "LIST_ATTRIBUTE", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String listAttr;

  @Column(name = "OPERATORS", columnDefinition = "VARCHAR(255)", length = 200, nullable = false)
  private String supportedOperators;

  @Column(name = "ORDER_NO", columnDefinition = "NUMBER(6)", nullable = false)
  private Integer orderNumber;


}
