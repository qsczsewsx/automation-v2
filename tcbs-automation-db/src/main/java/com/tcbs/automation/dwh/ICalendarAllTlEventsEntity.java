package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "iCalendar_AllTLEvents")
public class ICalendarAllTlEventsEntity {
  private String defId;
  private String defType;
  private String objId;
  private String defData;
  private String eventId;
  private Timestamp eventDueDate;
  private String eventStatus;
  private Integer eventIndex;
  private Timestamp defCreatedAt;
  private Integer etlCurDate;
  private Timestamp etlRunDatetime;

  @Id
  @Column(name = "def_id")
  public String getDefId() {
    return defId;
  }

  public void setDefId(String defId) {
    this.defId = defId;
  }

  @Basic
  @Column(name = "def_Type")
  public String getDefType() {
    return defType;
  }

  public void setDefType(String defType) {
    this.defType = defType;
  }

  @Basic
  @Column(name = "obj_id")
  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  @Basic
  @Column(name = "def_data")
  public String getDefData() {
    return defData;
  }

  public void setDefData(String defData) {
    this.defData = defData;
  }

  @Basic
  @Column(name = "event_id")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  @Basic
  @Column(name = "event_due_date")
  public Timestamp getEventDueDate() {
    return eventDueDate;
  }

  public void setEventDueDate(Timestamp eventDueDate) {
    this.eventDueDate = eventDueDate;
  }

  @Basic
  @Column(name = "event_status")
  public String getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(String eventStatus) {
    this.eventStatus = eventStatus;
  }

  @Basic
  @Column(name = "event_index")
  public Integer getEventIndex() {
    return eventIndex;
  }

  public void setEventIndex(Integer eventIndex) {
    this.eventIndex = eventIndex;
  }

  @Basic
  @Column(name = "def_created_at")
  public Timestamp getDefCreatedAt() {
    return defCreatedAt;
  }

  public void setDefCreatedAt(Timestamp defCreatedAt) {
    this.defCreatedAt = defCreatedAt;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDatetime")
  public Timestamp getEtlRunDatetime() {
    return etlRunDatetime;
  }

  public void setEtlRunDatetime(Timestamp etlRunDatetime) {
    this.etlRunDatetime = etlRunDatetime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ICalendarAllTlEventsEntity that = (ICalendarAllTlEventsEntity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(defId, that.defId) &&
      Objects.equals(defType, that.defType) &&
      Objects.equals(objId, that.objId) &&
      Objects.equals(defData, that.defData) &&
      Objects.equals(eventId, that.eventId) &&
      Objects.equals(eventDueDate, that.eventDueDate) &&
      Objects.equals(eventStatus, that.eventStatus) &&
      Objects.equals(eventIndex, that.eventIndex) &&
      Objects.equals(defCreatedAt, that.defCreatedAt) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(defId, defType, objId, defData, eventId, eventDueDate, eventStatus, eventIndex, defCreatedAt, etlCurDate, etlRunDatetime);
  }

  @Step("insert data")
  public boolean saveTLEvent(ICalendarAllTlEventsEntity event) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.save(event);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data")
  public boolean deleteTLEvent(ICalendarAllTlEventsEntity event) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    session.delete(event);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by defId")
  public void deleteTLEventByDefId(String defId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ICalendarAllTlEventsEntity> query = session.createQuery(
      "DELETE FROM ICalendarAllTlEventsEntity i WHERE i.defId=:defId"
    );
    query.setParameter("defId", defId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
