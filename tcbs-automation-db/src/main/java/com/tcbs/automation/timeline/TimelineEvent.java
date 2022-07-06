package com.tcbs.automation.timeline;

import com.tcbs.automation.functions.PublicConstant;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Getter
@Setter
@Entity
@Table(name = "timeline_event")
public class TimelineEvent {
  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "def_id")
  private String defId;
  @Column(name = "due_date")
  private Timestamp dueDate;
  @Column(name = "status")
  private String status;
  @Column(name = "root_id")
  private String rootId;
  @Column(name = "index")
  private Integer index;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  public TimelineEvent() {
  }

  public TimelineEvent(String id, String defId, Timestamp dueDate, String status, String rootId, Integer index, Timestamp createdAt, Timestamp updatedAt) {
    this.id = id;
    this.defId = defId;
    this.dueDate = dueDate;
    this.status = status;
    this.rootId = rootId;
    this.index = index;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDefId() {
    return defId;
  }

  public void setDefId(String defId) {
    this.defId = defId;
  }

  public String getDueDate() {
    return PublicConstant.dateTimeFormat.format(dueDate);
  }

  public void setDueDate(String dueDate) {
    this.dueDate = Timestamp.valueOf(dueDate);
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getRootId() {
    return rootId;
  }

  public void setRootId(String rootId) {
    this.rootId = rootId;
  }

  public Integer getIndex() {
    return index;
  }

  public void setIndex(Integer index) {
    this.index = index;
  }

  public String getCreatedAt() {
    return PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = Timestamp.valueOf(createdAt);
  }

  public String getUpdatedAt() {
    return PublicConstant.dateTimeFormat.format(updatedAt);
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = Timestamp.valueOf(updatedAt);
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public List<Map<String, Object>> getTimelineFromPlanId(String planId, String couponFixDate) throws Exception {
    String sql =
      "select * from timeline_event where def_id IN (select id from timeline_def where data like :planId) "
        + " and due_date = :dueDate";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("planId", "%\"planInstanceId\":\"" + planId + "\"%")
      .setParameter("dueDate", Timestamp.valueOf(couponFixDate))
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Step
  public List<String> getTimelineByPlanInstanceId(String planceInstanceId, Integer goalId) {

    String sql = " select id from timeline_event where def_id in (select id from timeline_def where data like :planceInstanceId and data like :goalId and type = :type)";
    return Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("planceInstanceId", "%" + planceInstanceId + "%")
      .setParameter("goalId", "%" + goalId + "%")
      .setParameter("type", "tcwealth.perform.date")
      .getResultList();
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public String getRootIdFromEventId(String eventId, String defId) throws Exception {
    String sql =
      "select id from timeline_event where index = (select index from timeline_event where id = :eventId) and def_id = :defId ";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("eventId", eventId)
      .setParameter("defId", defId)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result.get(0).get("id").toString();
    } else {
      return null;
    }
  }

  @Step
  public TimelineEvent getTimelineFromDefIdAndDueDate(String defId, String dueDate) {
    Query<TimelineEvent> query = Timeline.timelineDbConnection.getSession().createQuery(
      "from TimelineEvent a where a.defId=:defId and a.dueDate=:dueDate", TimelineEvent.class);
    query.setParameter("defId", defId);
    try {
      query.setParameter("dueDate", new Timestamp(PublicConstant.dateTimeFormat.parse(dueDate).getTime()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    List<TimelineEvent> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }


  @Step
  public List<TimelineEvent> getListTimelineFromDefId(String defId) {
    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    Query<TimelineEvent> query = session.createQuery(
      "from TimelineEvent a where a.defId=:defId and status<>:status order by dueDate desc", TimelineEvent.class);
    query.setParameter("defId", defId);
    query.setParameter("status", "CANCELED");
    List<TimelineEvent> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<TimelineEvent> getTimelineEventFromTypeOfTimelineDef(String type) {
    String sql =
      "select * from timeline_event where def_id in (select id from timeline_def where type like :type) ";
    List<TimelineEvent> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql, TimelineEvent.class)
      .setParameter("type", "%" + type + "%")
      .getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step("delete timeline event")
  public void deleteTimelineEvent(String defId) {
    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<TimelineEvent> query = session.createQuery(
      "DELETE FROM TimelineEvent i WHERE i.defId=:defId"
    );
    query.setParameter("defId", defId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static List<TimelineEvent> getListTimelineByStatus(String status, String id) {
    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    Query<TimelineEvent> query = session.createQuery(
      "from TimelineEvent a where a.status =:status and id = :id");
    query.setParameter("status", status);
    query.setParameter("id", id);
    List<TimelineEvent> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static void updateStatusTimelineEvents(String status, String id) {
    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(
      String.format("UPDATE timeline_event SET status = '%s' where status = 'HAPPENED' and id = '%s'", status, id));
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static TimelineEvent getEvent(String id) {
    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    Query<TimelineEvent> query = session.createQuery(
      "from TimelineEvent a where a.id =:id", TimelineEvent.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
}
