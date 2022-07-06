package com.tcbs.automation.docportal;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FILE_HISTORY")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileHistory extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "VERSION", columnDefinition = "NUMBER(15)", length = 50, nullable = false)
  private Integer version;

  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer fileId;

  @Column(name = "FILE_NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String fileName;

  @Column(name = "DOC_NO", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String docNo;

  @Column(name = "ECM_FILE_ID", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String ecmFileId;

  @Column(name = "DOC_TYPE", columnDefinition = "VARCHAR(20)", length = 20, nullable = false)
  private String docType;

  @Column(name = "DOC_STATUS", columnDefinition = "VARCHAR(20)", length = 20, nullable = true)
  private String docStatus;

  @Column(name = "DOC_DATE", columnDefinition = "TIMESTAMP", nullable = true)
  private Date docDate;

  @Column(name = "DOC_DESC", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String docDesc;

  @Column(name = "ACTION", columnDefinition = "VARCHAR(20)", length = 20, nullable = false)
  private String action;

  @Column(name = "ACTOR", columnDefinition = "NVARCHAR2(100)", length = 100, nullable = false)
  private String actor;

  @Column(name = "NOTE", columnDefinition = "NVARCHAR2(500)", length = 500, nullable = true)
  private String note;

  @Column(name = "ACTION_TIME", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  private Date actionTime;

}
