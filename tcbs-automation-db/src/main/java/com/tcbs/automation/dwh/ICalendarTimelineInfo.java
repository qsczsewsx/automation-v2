package com.tcbs.automation.dwh;


import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "iCalendar_Timeline")
public class ICalendarTimelineInfo {
  @Id
  @Column(name = "iCalendar_Timeline")
  private Integer iCalendarTimeline;
  @Column(name = "Timeline_ObjId")
  private String timelineObjId;
  @Column(name = "Timeline_DefId")
  private String timelineDefId;
  @Column(name = "Timeline_Type")
  private String timelineType;
  @Column(name = "Note")
  private String note;
  @Column(name = "Post_Rel")
  private String postRel;
  @Column(name = "Post_Rel_Date")
  private Date postRelDate;

  public Integer getiCalendarTimeline() {
    return iCalendarTimeline;
  }

  public void setiCalendarTimeline(Integer iCalendarTimeline) {
    this.iCalendarTimeline = iCalendarTimeline;
  }

  public String getTimelineObjId() {
    return timelineObjId;
  }

  public void setTimelineObjId(String timelineObjId) {
    this.timelineObjId = timelineObjId;
  }

  public String getTimelineDefId() {
    return timelineDefId;
  }

  public void setTimelineDefId(String timelineDefId) {
    this.timelineDefId = timelineDefId;
  }

  public String getTimelineType() {
    return timelineType;
  }

  public void setTimelineType(String timelineType) {
    this.timelineType = timelineType;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getPostRel() {
    return postRel;
  }

  public void setPostRel(String postRel) {
    this.postRel = postRel;
  }

  public Date getPostRelDate() {
    return postRelDate;
  }

  public void setPostRelDate(Date postRelDate) {
    this.postRelDate = postRelDate;
  }

  @Step("Select event by objId")
  public List<ICalendarTimelineInfo> byObjIdAndType(String objId, String type) {
    Query<ICalendarTimelineInfo> query = Dwh.dwhDbConnection.getSession().createQuery(
      "from ICalendarTimelineInfo where timelineObjId=:timelineObjId and timelineType=:timelineType", ICalendarTimelineInfo.class);
    query.setParameter("timelineObjId", objId);
    query.setParameter("timelineType", type);
    List<ICalendarTimelineInfo> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step("delete timeline event")
  public void deleteDwhTimelineEvent(String objId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ICalendarTimelineInfo> query = session.createQuery(
      "DELETE FROM ICalendarTimelineInfo i WHERE i.timelineObjId=:timelineObjId"
    );
    query.setParameter("timelineObjId", objId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
