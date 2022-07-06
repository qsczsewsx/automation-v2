package com.tcbs.automation.docportal;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "METADATA_HISTORY")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetadataHistory extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "VERSION", columnDefinition = "NUMBER(15)", length = 50, nullable = false)
  private Integer version;

  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer fileId;

  @Column(name = "METADATA_TYPE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer metadataTypeId;

  @Column(name = "METADATA_VALUE", columnDefinition = "NVARCHAR2(500)", nullable = true)
  private String metadataValue;

}
