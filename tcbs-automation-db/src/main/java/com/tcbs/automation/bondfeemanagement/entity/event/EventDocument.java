package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import com.tcbs.automation.paymentprocess.CRUEntity;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "EVENT_DOCUMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDocument extends CRUEntity {
  @Id
  @Column(name = "DOC_ID")
  private Integer docId;

  @Column(name = "EVENT_ENGINE_INSTANCE_ID")
  private String eventEngineInstanceId;

  @Column(name = "ECM_ID")
  private String ecmId;

  @Column(name = "DOC_TYPE_CODE")
  private String docTypeCodeRef;

  @Column(name = "DOC_NAME")
  private String docName;

  @Column(name = "DOC_STATUS")
  private String docStatusRef;

  @Column(name = "IS_ACTIVE")
  private Integer isActive;

  @Step
  public static void truncateTable() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table EVENT_DOCUMENT");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByEventId(String eventId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete EVENT_DOCUMENT where EVENT_ENGINE_INSTANCE_ID = :eventId");
    query.setParameter("eventId", eventId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(EventDocument eventDocument) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(eventDocument);
    trans.commit();
  }

  @Step
  public static List<EventDocument> getByEventId(String id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<EventDocument> query = session.createNativeQuery("select * from EVENT_DOCUMENT where EVENT_ENGINE_INSTANCE_ID = :id", EventDocument.class);
    query.setParameter("id", id);
    List<EventDocument> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return new ArrayList<>();
    }
    return results;
  }
}

