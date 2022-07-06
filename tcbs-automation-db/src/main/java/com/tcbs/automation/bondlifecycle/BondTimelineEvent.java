package com.tcbs.automation.bondlifecycle;

import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOND_TIMELINE_EVENT")
@ToString
public class BondTimelineEvent extends BondTimelineBase {
  @Id
  @Column(name = "TIMELINE_ID", updatable = false, nullable = false)
  private Integer timelineId;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "bondTimelineEvent")
  private List<BondEvent> bondEvents = new ArrayList<>();

  public BondTimelineEvent() {
    super(null, null);
  }

  public BondTimelineEvent(Integer groupId, RuleBaseEntity ruleBase) {
    super(groupId, ruleBase);
  }

  @Step
  public static void deleteById(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_EVENT a where a.TIMELINE_ID =:timelineId");
    query.setParameter("timelineId", timelineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondTimelineEvent bondTimelineEvent) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondTimelineEvent);
    trans.commit();
  }

  @Step
  public static BondTimelineEvent getById(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineEvent> query = session.createNativeQuery("select * from BOND_TIMELINE_EVENT where TIMELINE_ID = :timelineId", BondTimelineEvent.class);
    query.setParameter("timelineId", timelineId);
    List<BondTimelineEvent> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BigDecimal> getBusinessIdList() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query query = session.createNativeQuery(
      "select distinct bte.business_Id from Bond_Timeline_Event bte join bond_event be on be.bond_timeline_id = bte.timeline_id join business bus on bte.business_id = bus.id");
    return (List<BigDecimal>) query.getResultList();
  }

  @Step
  public static List<BigDecimal> getParticipantIdList() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query query = session.createNativeQuery(
      "select distinct bte.participant_Id from Bond_Timeline_Event bte join bond_event be on be.bond_timeline_id = bte.timeline_id join participant bus on bte.participant_Id = bus.id");
    return (List<BigDecimal>) query.getResultList();
  }

  public Integer getTimelineId() {
    return timelineId;
  }

  public void setTimelineId(Integer timelineId) {
    this.timelineId = timelineId;
  }

  public List<BondEvent> getBondEvents() {
    return bondEvents;
  }

  public void setBondEvents(List<BondEvent> bondEvents) {
    this.bondEvents = bondEvents;
  }
}
