package com.tcbs.automation.docportal;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "METADATA_TYPE")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MetadataType extends BaseEntity {

  @Id
  @Column(name = "METADATA_TYPE_ID", columnDefinition = "NUMBER(9)")
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer metadataTypeId;

  @Column(name = "NAME", columnDefinition = "NVARCHAR2(200)", length = 200, nullable = false)
  private String name;

  @Column(name = "METADATA_KEY", columnDefinition = "VARCHAR(50)", length = 50, nullable = false, unique = true)
  private String metadataKey;

  @Column(name = "M_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String type;

  @Column(name = "VALUE", columnDefinition = "VARCHAR(50) NULL", length = 50, nullable = true)
  private String value;

  @Column(name = "OBJECT_TYPE", columnDefinition = "VARCHAR(50)", length = 50, nullable = false)
  private String objectType;

  @Column(name = "GROUP_KEY", columnDefinition = "VARCHAR(50) NULL", length = 50, nullable = true)
  private String groupKey;

  @Column(name = "GROUP_NAME", columnDefinition = "VARCHAR(200) NULL", length = 200, nullable = true)
  private String groupName;

  @Column(name = "M_LEVEL", columnDefinition = "VARCHAR(50) NULL", length = 50, nullable = true)
  private String level;

  @Column(name = "ORDER_NO", columnDefinition = "NUMBER(3) DEFAULT 0", nullable = true)
  private Integer orderNo;

  @Column(name = "METADATA_REF", columnDefinition = "NUMBER(15) NULL", nullable = true)
  private Integer metadataTypeRef;

  @Column(name = "STATUS", columnDefinition = "VARCHAR(100)", length = 200, nullable = false)
  private String status;

  public static void deleteByMetadataTypeId(Integer metadataTypeId) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM MetadataType f WHERE f.metadataTypeId = :metadataTypeId"
    );
    query.setParameter("metadataTypeId", metadataTypeId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static Integer save(MetadataType metadataType) {
    Session session = DocPortal.CONNECTION.getSession();
    session.clear();
    org.hibernate.Transaction trans = session.beginTransaction();
    Integer generatedId = (Integer) session.save(metadataType);
    trans.commit();
    return generatedId;
  }

}
