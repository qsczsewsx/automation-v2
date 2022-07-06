package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "documents_transaction")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "DOCUMENT_TITLE")
  private String documentTitle;

  @Column(name = "DOCUMENT_ID")
  private String documentId;

  @Column(name = "TRANSACTION_ID")
  private String transactionId;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "OBJECT_STORE_NAME")
  private String objectStoreName;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "CLASS_DEFINITION")
  private String classDefinition;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "PATH_FOLDER")
  private String pathFolder;

  @Column(name = "FILE_NAME")
  private String fileName;

  @Step
  public static DocumentEntity getDocumentEntityBy(String status, String title) {
    Query<DocumentEntity> query = ixuDbConnection.getSession().createQuery(
      "from DocumentEntity where documentTitle like '%" + title + "%' and status = :status order by createdDate desc", DocumentEntity.class);
    query.setParameter("status", status);
    List<DocumentEntity> rs = query.getResultList();
    if (rs != null) {
      ixuDbConnection.closeSession();
      return rs.get(0);
    }
    ixuDbConnection.closeSession();
    return null;
  }

  @Step
  public static List<DocumentEntity> getDocumentEntityByTransactionId(String transactionId) {
    Query<DocumentEntity> query = ixuDbConnection.getSession().createQuery(
      "from DocumentEntity where transactionId = :transactionId order by createdDate desc", DocumentEntity.class);
    query.setParameter("transactionId", transactionId);
    List<DocumentEntity> rs = query.getResultList();
    if (rs != null) {
      ixuDbConnection.closeSession();
      return rs;
    }
    ixuDbConnection.closeSession();
    return null;
  }

  @Step
  public static void clearDocumentWithTransactionId(String transactionId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DocumentEntity where transactionId = :transactionId"
    );
    query.setParameter("transactionId", transactionId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertDocument(DocumentEntity entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

}
