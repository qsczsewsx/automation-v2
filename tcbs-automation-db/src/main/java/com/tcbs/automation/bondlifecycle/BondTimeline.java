package com.tcbs.automation.bondlifecycle;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "BOND_TIMELINE")
public class BondTimeline extends BondTimelineBase {
  @Id
  @Column(name = "TIMELINE_ID", updatable = false, nullable = false)
  private Integer timelineId;

  public BondTimeline() {
    super(null, null);
  }

  public BondTimeline(Integer groupId, RuleBaseEntity ruleBase) {
    super(groupId, ruleBase);
  }

  @Step
  public static void deleteById(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TIMELINE a where a.TIMELINE_ID = :timelineId");
    query.setParameter("timelineId", timelineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TIMELINE a where a.BOND_TIMELINE_GROUP_ID = :groupId");
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondTimeline bondTimeline) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(bondTimeline);
    trans.commit();
  }

  @Step
  public static void insert(List<BondTimeline> bondTimelines) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    for (BondTimeline timeline : bondTimelines) {
      session.save(timeline);
    }
    trans.commit();
  }

  @Step
  public static BondTimeline getById(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimeline> query = session.createNativeQuery("select * from BOND_TIMELINE where TIMELINE_ID = :timelineId", BondTimeline.class);
    query.setParameter("timelineId", timelineId);
    List<BondTimeline> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondTimeline> getByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimeline> query = session.createNativeQuery("select * from BOND_TIMELINE where BOND_TIMELINE_GROUP_ID = :groupId", BondTimeline.class);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  @Step
  public static List<Business> getUsedBusinessListByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT * ");
    stringBuilder.append("FROM BUSINESS b, (SELECT DISTINCT bt2.BUSINESS_ID FROM BOND_TIMELINE bt2 WHERE bt2.BOND_TIMELINE_GROUP_ID = :groupId) bi ");
    stringBuilder.append("WHERE b.ID = bi.BUSINESS_ID");
    Query<Business> query = session.createNativeQuery(stringBuilder.toString(), Business.class);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  public static List<Participant> getUsedParticipantListByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT * ");
    stringBuilder.append("FROM PARTICIPANT b, (SELECT DISTINCT bt2.PARTICIPANT_ID FROM BOND_TIMELINE bt2 WHERE bt2.BOND_TIMELINE_GROUP_ID = :groupId) bi ");
    stringBuilder.append("WHERE b.ID = bi.PARTICIPANT_ID");
    Query<Participant> query = session.createNativeQuery(stringBuilder.toString(), Participant.class);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

  public Integer getTimelineId() {
    return timelineId;
  }

  public void setTimelineId(Integer timelineId) {
    this.timelineId = timelineId;
  }
}
