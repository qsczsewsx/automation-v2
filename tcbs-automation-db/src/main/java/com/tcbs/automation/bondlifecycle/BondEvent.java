package com.tcbs.automation.bondlifecycle;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOND_EVENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BondEvent extends BaseEntity {
  @Id
  @Column(name = "EVENT_ENGINE_INSTANCE_ID")
  private String eventEngineInstanceId;

  @Column(name = "EVENT_ENGINE_STATUS")
  private String eventEngineStatus;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOND_TIMELINE_ID")
  private BondTimelineEvent bondTimelineEvent;

  @Column(name = "BPM_INSTANCE_ID")
  private String bpmInstanceId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BOND_ID", insertable = true, nullable = true)
  private BondBaseInfo bondBaseInfo;

  @Column(name = "EVENT_DATE")
  private Date eventDateDB;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "MAKER")
  private String maker;

  @Column(name = "CHECKER")
  private String checker;

  @Step
  public static void deleteById(String eventId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_EVENT a where EVENT_ENGINE_INSTANCE_ID =:eventId");
    query.setParameter("eventId", eventId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByTimelineId(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_EVENT a where BOND_TIMELINE_ID =:timelineId");
    query.setParameter("timelineId", timelineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondEvent bondEvent) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(bondEvent);
    trans.commit();
  }

  @Step
  public static void insertNativeSql(BondEvent bondEvent) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into BOND_EVENT " +
      "(EVENT_ENGINE_INSTANCE_ID, EVENT_ENGINE_STATUS, BOND_TIMELINE_ID, BOND_ID, EVENT_DATE, STATUS, NOTE, CREATED_AT, CREATED_BY, MAKER, CHECKER ) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, bondEvent.getEventEngineInstanceId());
    query.setParameter(2, bondEvent.getEventEngineStatus());
    query.setParameter(3, bondEvent.getBondTimelineEvent().getTimelineId());
    query.setParameter(4, bondEvent.getBondBaseInfo().getBondId());
    query.setParameter(5, bondEvent.getEventDateDB(), TemporalType.DATE);
    query.setParameter(6, bondEvent.getStatus());
    query.setParameter(7, bondEvent.getNote());
    query.setParameter(8, bondEvent.getCreatedAtDate(), TemporalType.DATE);
    query.setParameter(9, bondEvent.getCreatedBy());
    query.setParameter(10, bondEvent.getMaker());
    query.setParameter(11, bondEvent.getChecker());

    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static BondEvent getById(String eventId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondEvent> query = session.createNativeQuery("select * from BOND_EVENT where EVENT_ENGINE_INSTANCE_ID = :eventId", BondEvent.class);
    query.setParameter("eventId", eventId);
    List<BondEvent> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @SuppressWarnings({"unchecked"})
  @Step
  public static List<BondEvent> getByIdList(List<String> eventEngineInstanceIdList) {
    List<BondEvent> bondEvents = new ArrayList<>();
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Object[]> query = session.createNativeQuery(
      "select e.EVENT_ENGINE_INSTANCE_ID, e.EVENT_ENGINE_STATUS, e.BOND_TIMELINE_ID, e.BOND_ID, e.EVENT_DATE, e.STATUS, e.NOTE, e.MAKER, e.CHECKER"
        + " from BOND_EVENT e "
        + " where EVENT_ENGINE_INSTANCE_ID in :eventEngineInstanceIdList");
    query.setParameter("eventEngineInstanceIdList", eventEngineInstanceIdList);
    List<Object[]> results = query.getResultList();
    for (Object[] row : results) {
      BondEvent bondEvent = new BondEvent();
      bondEvent.setEventEngineInstanceId((String) row[0]);
      bondEvent.setEventEngineStatus((String) row[1]);

      BondTimelineEvent bondTimelineEvent = new BondTimelineEvent();
      bondTimelineEvent.setTimelineId(((BigDecimal) row[2]).intValue());
      bondEvent.setBondTimelineEvent(bondTimelineEvent);

      BondBaseInfo bondBaseInfo = new BondBaseInfo();
      bondBaseInfo.setBondId(((BigDecimal) row[3]).intValue());
      bondEvent.setBondBaseInfo(bondBaseInfo);

      bondEvent.setEventDateDB(new Date(((Timestamp) row[4]).getTime()));
      bondEvent.setStatus((String) row[5]);
      bondEvent.setNote((String) row[6]);
      bondEvent.setMaker((String) row[7]);
      bondEvent.setChecker((String) row[8]);

      bondEvents.add(bondEvent);
    }

    return bondEvents;
  }

  @Step
  public static List<BondEvent> getAllByTimelineId(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondEvent> query = session.createNativeQuery("select * from BOND_EVENT where BOND_TIMELINE_ID = :timelineId", BondEvent.class);
    query.setParameter("timelineId", timelineId);
    return query.getResultList();
  }


  @Step
  public static List<String> getCheckerList() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<String> query = session.createNativeQuery("select CHECKER from BOND_EVENT where CHECKER is not null group by CHECKER");
    return query.getResultList();
  }

  @Step
  public static List<String> getMakerList() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<String> query = session.createNativeQuery("select MAKER from BOND_EVENT where MAKER is not null group by MAKER");
    return query.getResultList();
  }
}
