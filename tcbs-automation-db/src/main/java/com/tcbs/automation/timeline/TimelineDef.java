package com.tcbs.automation.timeline;


import com.tcbs.automation.functions.PublicConstant;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "timeline_def")
public class TimelineDef {
  @Id
  @Column(name = "id")
  private String id;
  @Column(name = "type")
  private String type;
  @Column(name = "obj_id")
  private String objId;
  @Column(name = "data")
  private String data;
  @Column(name = "start_date")
  private Timestamp startDate;
  @Column(name = "end_date")
  private Timestamp endDate;
  @Column(name = "freq")
  private String freq;
  @Column(name = "status")
  private String status;
  @Column(name = "total")
  private Integer total;
  @Column(name = "created_at")
  private Timestamp createAt;
  @Column(name = "updated_at")
  private Timestamp updateAt;

  public TimelineDef() {
  }

  public TimelineDef(String id, String type, String objId, String data, Timestamp startDate,
                     Timestamp endDate, String freq, String status, Integer total, Timestamp createAt, Timestamp updateAt) {
    this.id = id;
    this.type = type;
    this.objId = objId;
    this.data = data;
    this.startDate = startDate;
    this.endDate = endDate;
    this.freq = freq;
    this.status = status;
    this.total = total;
    this.createAt = createAt;
    this.updateAt = updateAt;
  }

  public TimelineDef(String id) {
    this.id = id;
  }

  @Step
  public static List<String> getBondCodeTimelineFromTypeAndDueDate(String dueDate, String status, String type) throws Exception {
    Timestamp timestamp = new Timestamp(PublicConstant.dateTimeFormat.parse(dueDate).getTime());
    String sql =
      " select obj_id from timeline_def where id in (select def_id from timeline_event where due_date=:dueDate and status=:status) and type like :type ";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("dueDate", timestamp)
      .setParameter("status", status)
      .setParameter("type", "%" + type + "%")
      .getResultList();
    if (result.size() > 0) {
      return null;
    } else {
      return null;
    }
  }

  @Step
  public static TimelineDef getTimelineFromTypeAndObjId(String type, String objId) {

    Session session = Timeline.timelineDbConnection.getSession();
    session.clear();
    Query<TimelineDef> query = session.createQuery(
      "from TimelineDef a where a.type =:type and a.objId=:objId ", TimelineDef.class);
    query.setParameter("type", type);
    query.setParameter("objId", objId);
    return query.getSingleResult();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  public String getStartDate() {
    return PublicConstant.dateTimeFormat.format(startDate);
  }

  public void setStartDate(String startDate) {
    this.startDate = Timestamp.valueOf(startDate);
  }

  public String getEndDate() {
    return PublicConstant.dateTimeFormat.format(endDate);
  }

  public void setEndDate(String endDate) {
    this.endDate = Timestamp.valueOf(endDate);
  }

  public String getFreq() {
    return freq;
  }

  public void setFreq(String freq) {
    this.freq = freq;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public String getCreateAt() {
    return PublicConstant.dateTimeFormat.format(createAt);
  }

  public void setCreateAt(String createAt) {
    this.createAt = Timestamp.valueOf(createAt);
  }

  public String getUpdateAt() {
    return PublicConstant.dateTimeFormat.format(updateAt);
  }

  public void setUpdateAt(String updateAt) {
    this.updateAt = Timestamp.valueOf(updateAt);
  }

  @SuppressWarnings({"unchecked", "deprecation"})
  @Step
  public List<Map<String, Object>> getTimelineFromPlanIdAndType(String planId, String type) throws Exception {
    String sql =
      "SELECT id, type, obj_id, data, start_date, end_date,\n"
        + "freq, status, total, created_at, updated_at "
        + "FROM timeline_def \n"
        + "WHERE data like :planId and type = :type";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("planId", "%" + planId + "%")
      .setParameter("type", type)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    return result;
  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public List<Map<String, Object>> getTimelineFromId(String id) {
    String sql = "select * from timeline_def where id = :id";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("id", id)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    return result;
  }

  @Step
  public TimelineDef getTimelineFromIdNew(String id) {
    String sql = "from TimelineDef where id = :id";
    return Timeline.timelineDbConnection.getSession().createQuery(sql, TimelineDef.class)
      .setParameter("id", id)
      .getSingleResult();
  }

  @Step
  public List<TimelineDef> getTimelineFromObjId(String objId) {
    Query<TimelineDef> query = Timeline.timelineDbConnection.getSession().createQuery(
      "from TimelineDef a where a.objId=:objId order by a.type asc", TimelineDef.class);
    query.setParameter("objId", objId);
    List<TimelineDef> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public List<TimelineDef> getTimelineFromObjIdAndType(String objId, String type) {
    Query<TimelineDef> query = Timeline.timelineDbConnection.getSession().createQuery(
      "from TimelineDef a where a.objId=:objId and a.type=:type", TimelineDef.class);
    query.setParameter("objId", objId);
    query.setParameter("type", type);
    List<TimelineDef> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<TimelineDef> getTimelineFromType(String type) {
    Query<TimelineDef> query = Timeline.timelineDbConnection.getSession().createQuery(
      "from TimelineDef a where a.type like :type", TimelineDef.class);
    query.setParameter("type", "%" + type + "%");
    List<TimelineDef> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step("delete timeline def")
  public void deleteTimelineDef(String objId) {
    final Logger logger = LoggerFactory.getLogger(TimelineDef.class);
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("DELETE FROM timeline_def "
      + " WHERE obj_id = '%s'; \r\n", objId));

    try {
      Timeline.timelineDbConnection.getSession().beginTransaction();
      Timeline.timelineDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).executeUpdate();
      Timeline.timelineDbConnection.getSession().getTransaction().commit();
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex.getStackTrace());
    }
  }
}
