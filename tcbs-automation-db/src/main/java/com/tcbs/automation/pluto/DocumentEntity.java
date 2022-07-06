package com.tcbs.automation.pluto;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.ops.OPS.opsConnection;

@Entity
@Table(name = "SVR_DOCUMENT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "VALUATION_ID")
  private String valuationID;

  @Column(name = "ECM_ID")
  private String ecmID;

  @Column(name = "DOC_NAME")
  private String docName;

  @Column(name = "DOC_TYPE")
  private String docType;

  @Column(name = "UPLOADER")
  private String uploader;

  public static void removeByValuationID(String valuationID) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query query = opsConnection.getSession().createQuery("DELETE FROM DocumentEntity d WHERE d.valuationID = :valuationID");
    query.setParameter("valuationID", valuationID);
    query.executeUpdate();

    opsConnection.getSession().getTransaction().commit();
  }

  public static List<DocumentEntity> getListDocuments(Integer valuationID) {
    opsConnection.getSession().clear();
    opsConnection.getSession().beginTransaction();

    Query query = opsConnection.getSession().createQuery(
      "select c from DocumentEntity c where c.valuationID = :valuationID",
      DocumentEntity.class
    );
    query.setParameter("valuationID", String.valueOf(valuationID));
    List<DocumentEntity> res = query.getResultList();
    opsConnection.getSession().getTransaction().commit();
    return res;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocumentEntity that = (DocumentEntity) o;
    return valuationID.equals(that.valuationID) && ecmID.equals(that.ecmID) && docName.equals(that.docName) && docType.equals(that.docType) && uploader.equals(that.uploader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valuationID, ecmID, docName, docType, uploader);
  }
}
