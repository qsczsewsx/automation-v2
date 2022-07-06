package com.tcbs.automation.docportal;

import com.tcbs.automation.VNCharacterUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "FILE_ROLE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FileRole extends BaseEntity {

  @Id
  @Column(name = "ID", columnDefinition = "NUMBER(15)")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "FILE_ID", columnDefinition = "NUMBER(15)", nullable = false)
  private Integer fileId;

  @Column(name = "ROLE_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String roleType;

  @Column(name = "ACTOR_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String actorType;

  @Column(name = "ACTOR_ID", columnDefinition = "VARCHAR(100)", length = 50, nullable = false)
  private String actorId;

  @Column(name = "ACTOR_NAME", columnDefinition = "NVARCHAR2(255)", length = 50, nullable = false)
  private String actorName;

  @Column(name = "ACTOR_NAME_NO_ACCENT", columnDefinition = "VARCHAR(255)", length = 50, nullable = false)
  @Setter(AccessLevel.NONE)
  private String actorNameNoAccent;

  public static FileRole findById(Long id) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    FileRole fileRole = session.find(FileRole.class, id);

    return fileRole;
  }

  public static List<FileRole> findByIdIn(List<Long> ids) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileRole> query = session.createQuery("select f from FileRole f where f.id in :ids",
      FileRole.class);
    query.setParameter("ids", ids);
    List<FileRole> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static List<FileRole> findByFileId(Integer fileId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FileRole> query = session.createQuery("select f from FileRole f where f.fileId =:fileId",
      FileRole.class);
    query.setParameter("fileId", fileId);
    List<FileRole> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }

  public static Long save(FileRole fileRole) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Long generatedId = (Long) session.save(fileRole);
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
      "DELETE FROM FileRole f WHERE f.id = :id"
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
      "DELETE FROM FileRole f WHERE f.fileId = :fileId"
    );
    query.setParameter("fileId", fileId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setActorName(String actorName) {
    this.actorName = actorName;
    if (StringUtils.isNotBlank(this.actorName)) {
      this.actorNameNoAccent = VNCharacterUtils.removeAccent(this.actorName).toLowerCase();
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FileRole fileRole = (FileRole) o;

    return new EqualsBuilder()
      .append(fileId, fileRole.fileId)
      .append(roleType, fileRole.roleType)
      .append(actorType, fileRole.actorType)
      .append(actorId, fileRole.actorId)
      .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
      .append(fileId)
      .append(roleType)
      .append(actorType)
      .append(actorId)
      .toHashCode();
  }
}
