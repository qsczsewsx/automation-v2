package com.tcbs.automation.bondlifecycle;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "VIEW_BOND_EVENT_DETAIL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewBondEventDetail {
  @Id
  @Column(name = "EVENT_ENGINE_INSTANCE_ID", updatable = false, nullable = false)
  private String eventEngineInstanceId;

  @Column(name = "TIMELINE_ENGINE_INSTANCE_ID")
  private String timelineEngineInstanceId;

  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "BOND_NAME")
  private String bondName;

  @Column(name = "EVENT_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date eventDateDB = null;

  @Column(name = "EVENT_NAME")
  private String eventName;

  @Column(name = "EVENT_NAME_NO_ACCENT")
  private String eventNameNoAccent;

  @Column(name = "EVENT_DESC")
  private String eventDesc;

  @Column(name = "EVENT_DESC_NO_ACCENT")
  private String eventDescNoAccent;

  @Column(name = "BUSINESS_ID")
  private String businessId;

  @Column(name = "BUSINESS_NAME")
  private String businessName;

  @Column(name = "PARTICIPANT_ID")
  private String participantId;

  @Column(name = "PARTICIPANT_NAME")
  private String participantName;

  @Column(name = "CONTRACT_REFS")
  private String contractRefs;

  @Column(name = "EVENT_STATUS")
  private String eventStatus;

  @Column(name = "MAKER")
  private String maker;

  @Column(name = "CHECKER")
  private String checker;

  @Column(name = "EVENT_NOTE")
  private String eventNote;

  @Column(name = "PUBLICITY")
  private String publicity;

  @Column(name = "EXTENSION_SERIAL")
  private String extensionSerial;

  @Step
  public static ViewBondEventDetail getById(String eventId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<ViewBondEventDetail> query = session.createNativeQuery("select * from VIEW_BOND_EVENT_DETAIL where EVENT_ENGINE_INSTANCE_ID = :eventId", ViewBondEventDetail.class);
    query.setParameter("eventId", eventId);
    List<ViewBondEventDetail> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }
}
