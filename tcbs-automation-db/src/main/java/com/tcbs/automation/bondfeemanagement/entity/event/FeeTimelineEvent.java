package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BaseEntity;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FEE_TIMELINE_EVENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeTimelineEvent extends BaseEntity {
  @Id
  @Column(name = "EVENT_ENGINE_INSTANCE_ID")
  private String eventEngineInstanceId;

  @Column(name = "TIMELINE_ENGINE_INSTANCE_ID")
  private String feeTimelineEngineInstance;

  @Column(name = "EVENT_ENGINE_STATUS")
  private String eventEngineStatus;

  @Column(name = "EVENT_DATE")
  private Date eventDateDB;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "OTHER_STATUS")
  private String otherStatus;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "FEE_PERCENT")
  private Float feePercent;

  @Column(name = "FEE_AMOUNT")
  private Long feeAmount;

  @Column(name = "FEE_AMOUNT_VAT")
  private Long feeAmountVAT;

  @Column(name = "FEE_AMOUNT_FINAL")
  private Long feeAmountFinal;

  @Column(name = "ACTUAL_FEE_AMOUNT")
  private Long actualFeeAmount;

  @Column(name = "BILL_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date billDateDB;

  @Column(name = "PAYMENT_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date paymentDateDB;

  @Column(name = "PAYMENT_EXPIRED_DAY")
  private Integer paymentExpiredDay;

  @Column(name = "BPM_TASK_ID")
  private Integer bpmTaskId;

  @Column(name = "MIS_RETAIL")
  private Boolean misRetail;

  @Column(name = "FEE_TYPE_LEVEL1_CODE")
  private String feeTypeLevel1Code;

  @Column(name = "FEE_TYPE_LEVEL2_CODE")
  private String feeTypeLevel2Code;

  @Column(name = "REVENUE_RECOGNITION_DATE_CODE")
  private String revenueRecognitionCode;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ENGINE_INSTANCE_ID")
  private List<FeeEventExtend> eventExtends = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "EVENT_ENGINE_INSTANCE_ID")
  private List<FeeEventPayment> eventPayments = new ArrayList<>();

  @Step
  public static void deleteById(String eventId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE_EVENT where EVENT_ENGINE_INSTANCE_ID = :eventId");
    query.setParameter("eventId", eventId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void truncateTable() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table FEE_TIMELINE_EVENT");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(FeeTimelineEvent feeTimelineEvent) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(feeTimelineEvent);
    trans.commit();
  }

  @Step
  public static FeeTimelineEvent getById(String id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineEvent> query = session.createNativeQuery("select * from FEE_TIMELINE_EVENT where EVENT_ENGINE_INSTANCE_ID = :id", FeeTimelineEvent.class);
    query.setParameter("id", id);
    List<FeeTimelineEvent> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static List<FeeTimelineEvent> getByListId(List<String> ids) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineEvent> query = session.createNativeQuery("select * from FEE_TIMELINE_EVENT where EVENT_ENGINE_INSTANCE_ID IN :ids order by EVENT_ENGINE_INSTANCE_ID",
      FeeTimelineEvent.class);
    query.setParameter("ids", ids);
    List<FeeTimelineEvent> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }
}
