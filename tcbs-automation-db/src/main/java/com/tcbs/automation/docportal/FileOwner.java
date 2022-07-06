package com.tcbs.automation.docportal;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FILE_OWNER")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileOwner extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer fileId;

  @Column(name = "OWNER_CODE", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String ownerCode;

  public static FileOwner findById(Long id) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    FileOwner fileOwner = session.find(FileOwner.class, id);

    return fileOwner;
  }

  public static List<FileOwner> findByIdIn(List<Long> ids) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileOwner> query = session.createQuery("select f from FileOwner f where f.id in :ids",
      FileOwner.class);
    query.setParameter("ids", ids);
    List<FileOwner> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static List<FileOwner> findByFileId(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileOwner> query = session.createQuery("select f from FileOwner f where f.fileId =:fileId",
      FileOwner.class);
    query.setParameter("fileId", fileId);
    List<FileOwner> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static Long save(FileOwner fileOwner) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Long generatedId = (Long) session.save(fileOwner);
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
      "DELETE FROM FileOwner f WHERE f.id = :id"
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
      "DELETE FROM FileOwner f WHERE f.fileId = :fileId"
    );
    query.setParameter("fileId", fileId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
