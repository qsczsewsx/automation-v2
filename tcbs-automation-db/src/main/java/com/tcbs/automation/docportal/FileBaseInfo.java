package com.tcbs.automation.docportal;

import com.tcbs.automation.VNCharacterUtils;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FILE_BASE_INFO")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SqlResultSetMapping(
  name = "FilePermissionTypeDtoMapping",
  classes = {
    @ConstructorResult(
      targetClass = FilePermissionTypeDtoMapping.class,
      columns = {
        @ColumnResult(name = "FILE_ID", type = Integer.class),
        @ColumnResult(name = "PERMISSION_TYPE", type = String.class),
        @ColumnResult(name = "PERMISSION_NAME", type = String.class),
      })
  })
@ToString
public class FileBaseInfo extends BaseEntity {

  @Id
  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)")
//	@GeneratedValue(strategy = GenerationType.AUTO)
  private Integer fileId;

  @Column(name = "FILE_NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String fileName;

  @Column(name = "DOC_NO", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String docNo;

  @Column(name = "FILE_NAME_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String fileNameNoAccent;

  @Column(name = "DOC_NO_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String docNoNoAccent;

  @Column(name = "ECM_FILE_ID", columnDefinition = "VARCHAR(200)", length = 200, nullable = false)
  private String ecmFileId;

  @Column(name = "DOC_TYPE", columnDefinition = "VARCHAR(20)", length = 20, nullable = true)
  private String docType;

  @Column(name = "DOC_TYPE_NO_ACCENT", columnDefinition = "VARCHAR(200)", length = 20, nullable = true)
  private String docTypeNoAccent;

  @Column(name = "DOC_STATUS", columnDefinition = "VARCHAR(20)", length = 20, nullable = true)
  private String docStatus;

  @Column(name = "DOC_STATUS_NO_ACCENT", columnDefinition = "VARCHAR(200)", length = 20, nullable = true)
  private String docStatusNoAccent;

  @Column(name = "DOC_DATE", columnDefinition = "TIMESTAMP", nullable = true)
  private Date docDate;

  @Column(name = "DOC_DESC", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  private String docDesc;

  @Column(name = "DOC_DESC_NO_ACCENT", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = true)
  @Setter(AccessLevel.NONE)
  private String docDescNoAccent;

  @Column(name = "STATUS", columnDefinition = "VARCHAR(100)", length = 200, nullable = false)
  private String status;

  @Column(name = "OBJECT_TYPE", columnDefinition = "VARCHAR(50)", length = 200, nullable = false)
  private String objectType;

  @Column(name = "ACTIVE_VERSION", columnDefinition = "NUMBER(15)", nullable = true)
  private Integer activeVersion;

  @Column(name = "ECM_PATH", columnDefinition = "VARCHAR(500)", length = 500, nullable = true)
  private String ecmPath;

  @Column(name = "FILE_TYPE", columnDefinition = "VARCHAR(500)", length = 500, nullable = true)
  private String fileType;

  public static FileBaseInfo findById(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    FileBaseInfo fileBaseInfo = session.find(FileBaseInfo.class, fileId);

    return fileBaseInfo;
  }

  public static List<FileBaseInfo> findByIdIn(List<Integer> fileIds) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    Query<FileBaseInfo> query = session.createQuery("select f from FileBaseInfo f where f.fileId in :ids",
      FileBaseInfo.class);
    query.setParameter("ids", fileIds);
    List<FileBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static Integer save(FileBaseInfo fileBaseInfo) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Integer generatedId = (Integer) session.save(fileBaseInfo);
    trans.commit();

    return generatedId;
  }

  public static void deleteById(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM FileBaseInfo f WHERE f.fileId = :id"
    );
    query.setParameter("id", fileId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByFileId(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM FileBaseInfo f WHERE f.fileId = :fileId"
    );
    query.setParameter("fileId", fileId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<FileBaseInfo> findByEcmFileIdIn(List<String> ecmFileIds) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    Query<FileBaseInfo> query = session.createQuery("select f from FileBaseInfo f where f.ecmFileId in :ecmFileIds",
      FileBaseInfo.class);
    query.setParameter("ecmFileIds", ecmFileIds);
    List<FileBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static FileBaseInfo findByEcmFileId(String ecmFileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    Query<FileBaseInfo> query = session.createQuery("select f from FileBaseInfo f where f.ecmFileId =:ecmFileId",
      FileBaseInfo.class);
    query.setParameter("ecmFileId", ecmFileId);
    List<FileBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  public void increaseActiveVersion() {
    this.activeVersion = this.activeVersion + 1;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
    if (this.fileName != null) {
      this.fileNameNoAccent = VNCharacterUtils.removeAccent(this.fileName).toLowerCase();
    }
  }

  public void setDocDesc(String docDesc) {
    this.docDesc = docDesc;
    if (this.docDesc != null) {
      this.docDescNoAccent = VNCharacterUtils.removeAccent(this.docDesc).toLowerCase();
    }
  }

  public void setDocNo(String docNo) {
    this.docNo = docNo;
    if (this.docNo != null) {
      this.docNoNoAccent = VNCharacterUtils.removeAccent(this.docNo).toLowerCase();
    }
  }

  public boolean isActive() {
    return this.status != null && this.status.equalsIgnoreCase("ACTIVE");
  }


}
