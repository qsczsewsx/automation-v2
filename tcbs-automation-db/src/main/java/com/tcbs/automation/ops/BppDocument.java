package com.tcbs.automation.ops;

import com.tcbs.automation.ops.converter.DocStatusConverter;
import com.tcbs.automation.ops.converter.DocTypeConverter;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "BPP_DOCUMENT")
public class BppDocument {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BPP_DOCUMENT_SEQ")
  @SequenceGenerator(sequenceName = "BPP_DOCUMENT_SEQ", allocationSize = 1, name = "BPP_DOCUMENT_SEQ")
  private Integer id;

  @Column(name = "DOC_TYPE")
  @Convert(converter = DocTypeConverter.class)
  private DocType docType;

  @Column(name = "PLEDGE_ID")
  private Integer pledgeId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "ECM_ID")
  private String ecmId;

  @Column(name = "UPLOADED_USER")
  private String uploadedUser;

  @Column(name = "UPLOADED_DATE")
  @Temporal(TemporalType.DATE)
  private Date uploadedDate;

  @Column(name = "DOC_STATUS")
  @Convert(converter = DocStatusConverter.class)
  private DocStatus docStatus;

  public static Integer save(BppDocument bppDocument) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Integer generatedId = (Integer) session.save(bppDocument);
    session.getTransaction().commit();

    return generatedId;
  }

  public static void removeByPledgeId(Integer pledgeId) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    Query<BppDocument> getContractsById = session.createQuery("SELECT c FROM BppDocument c WHERE c.pledgeId = :pledgeId", BppDocument.class);
    getContractsById.setParameter("pledgeId", pledgeId);
    List<BppDocument> contract = getContractsById.getResultList();
    if (contract.size() > 0) {
      contract.forEach(bppContract -> {
        session.remove(bppContract);
      });
    }

    session.getTransaction().commit();
  }

  public static List<BppDocument> getDocumentByPledgeID(Integer pledgeID) {
    OPS.opsConnection.getSession().clear();

    Query<BppDocument> query = OPS.opsConnection.getSession().createQuery(
      "select c from BppDocument c WHERE c.pledgeId = :pledgeId",
      BppDocument.class
    );
    query.setParameter("pledgeId", pledgeID);
    List<BppDocument> res = query.getResultList();
    return res;
  }

  public static List<Integer> saveAll(List<BppDocument> dtos) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    List<Integer> idDocs = new ArrayList<>();
    dtos.stream().forEach(docs -> {
      Integer id = (Integer) session.save(docs);
      idDocs.add(id);
    });
    session.getTransaction().commit();
    return idDocs;
  }

  public static List<BppDocument> getDocumentById(List<Integer> ids) {
    Session session = OPS.opsConnection.getSession();
    session.clear();

    List<BppDocument> bppDocuments = new ArrayList<>();
    ids.stream().forEach(id -> {
      BppDocument doc = session.get(BppDocument.class, id);
      bppDocuments.add(doc);
    });

    return bppDocuments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BppDocument that = (BppDocument) o;
    return Objects.equals(id, that.id)
      && docType == that.docType
      && Objects.equals(pledgeId, that.pledgeId)
      && Objects.equals(name, that.name)
      && Objects.equals(ecmId, that.ecmId)
      && Objects.equals(uploadedUser, that.uploadedUser)
      && Objects.equals(uploadedDate, that.uploadedDate)
      && docStatus == that.docStatus;
  }

  @Override
  public int hashCode() {
    return 31;
  }
}
