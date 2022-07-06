package com.tcbs.automation.dwh;


import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "iCalendar_TL_Noti")
public class ICalendarTLNotiInfo {
  private int id;
  private int iCalendarTimelineId;
  private String notiType;
  private String notiRole;
  private Timestamp createdDate;
  private String note;

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "iCalendar_Timeline_ID")
  public int getiCalendarTimelineId() {
    return iCalendarTimelineId;
  }

  public void setiCalendarTimelineId(int iCalendarTimelineId) {
    this.iCalendarTimelineId = iCalendarTimelineId;
  }

  @Basic
  @Column(name = "NotiType")
  public String getNotiType() {
    return notiType;
  }

  public void setNotiType(String notiType) {
    this.notiType = notiType;
  }

  @Basic
  @Column(name = "NotiRole")
  public String getNotiRole() {
    return notiRole;
  }

  public void setNotiRole(String notiRole) {
    this.notiRole = notiRole;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "Note")
  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  @Step("delete iCalendar Timeline Noti data")
  public void deleteTLNoti(Integer timelineId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ICalendarTLNotiInfo> query = session.createQuery(
      "DELETE FROM ICalendarTLNotiInfo i WHERE i.iCalendarTimelineId=:iCalendarTimelineId"
    );
    query.setParameter("iCalendarTimelineId", timelineId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
