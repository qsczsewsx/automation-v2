package com.tcbs.automation.docportal;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FILE_METADATA")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileMetadata extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "METADATA_TYPE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer metadataTypeId;

  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer fileId;

  @Column(name = "METADATA_VALUE", columnDefinition = "NVARCHAR2(500)", length = 500, nullable = true)
  private String metadataValue;

  @Column(name = "METADATA_VALUE_NO_ACCENT", columnDefinition = "VARCHAR(500)", nullable = true)
  private String metadataValueNoAccent;

  public static FileMetadata findById(Long id) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    FileMetadata fileMetadata = session.find(FileMetadata.class, id);

    return fileMetadata;
  }

  public static List<FileMetadata> findByIdIn(List<Long> ids) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileMetadata> query = session.createQuery("select f from FileMetadata f where f.id in :ids",
      FileMetadata.class);
    query.setParameter("ids", ids);
    List<FileMetadata> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static List<FileMetadata> findByFileId(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileMetadata> query = session.createQuery("select f from FileMetadata f where f.fileId =:fileId",
      FileMetadata.class);
    query.setParameter("fileId", fileId);
    List<FileMetadata> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static Long save(FileMetadata fileMetadata) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Long generatedId = (Long) session.save(fileMetadata);
    trans.commit();

    return generatedId;
  }

  public static void deleteById(Long id) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM FileMetadata f WHERE f.id = :id"
    );
    query.setParameter("id", id);
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
      "DELETE FROM FileMetadata f WHERE f.fileId = :fileId"
    );
    query.setParameter("fileId", fileId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
