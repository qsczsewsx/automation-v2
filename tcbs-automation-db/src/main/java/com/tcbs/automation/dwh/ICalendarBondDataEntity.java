package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "iCalendar_BondData")
public class ICalendarBondDataEntity {
  private String productEventType;
  private String eventType;
  private String defId;
  private String defType;
  private String objId;
  private String data;
  private String eventId;
  private Timestamp eventDueDate;
  private String eventStatus;
  private Integer eventIndex;
  private String custodyCd;
  private String tcbsId;
  private Double oBalQ;
  private Double oBalParValue;
  private Double rmOBalQ;
  private Double rmOBalParValue;
  private String rmCustodyCd;
  private String rmTcbsid;
  private int etlCurDate;
  private Timestamp etlRunDatetime;
  private Integer id;

  @Basic
  @Column(name = "productEventType")
  public String getProductEventType() {
    return productEventType;
  }

  public void setProductEventType(String productEventType) {
    this.productEventType = productEventType;
  }

  @Basic
  @Column(name = "eventType")
  public String getEventType() {
    return eventType;
  }

  public void setEventType(String eventType) {
    this.eventType = eventType;
  }

  @Basic
  @Column(name = "defId")
  public String getDefId() {
    return defId;
  }

  public void setDefId(String defId) {
    this.defId = defId;
  }

  @Basic
  @Column(name = "defType")
  public String getDefType() {
    return defType;
  }

  public void setDefType(String defType) {
    this.defType = defType;
  }

  @Basic
  @Column(name = "objId")
  public String getObjId() {
    return objId;
  }

  public void setObjId(String objId) {
    this.objId = objId;
  }

  @Basic
  @Column(name = "data")
  public String getData() {
    return data;
  }

  public void setData(String data) {
    this.data = data;
  }

  @Basic
  @Column(name = "eventId")
  public String getEventId() {
    return eventId;
  }

  public void setEventId(String eventId) {
    this.eventId = eventId;
  }

  @Basic
  @Column(name = "eventDueDate")
  public Timestamp getEventDueDate() {
    return eventDueDate;
  }

  public void setEventDueDate(Timestamp eventDueDate) {
    this.eventDueDate = eventDueDate;
  }

  @Basic
  @Column(name = "eventStatus")
  public String getEventStatus() {
    return eventStatus;
  }

  public void setEventStatus(String eventStatus) {
    this.eventStatus = eventStatus;
  }

  @Basic
  @Column(name = "eventIndex")
  public Integer getEventIndex() {
    return eventIndex;
  }

  public void setEventIndex(Integer eventIndex) {
    this.eventIndex = eventIndex;
  }

  @Basic
  @Column(name = "CustodyCD")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "TcbsId")
  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }

  @Basic
  @Column(name = "OBalQ")
  public Double getoBalQ() {
    return oBalQ;
  }

  public void setoBalQ(Double oBalQ) {
    this.oBalQ = oBalQ;
  }

  @Basic
  @Column(name = "OBalParValue")
  public Double getoBalParValue() {
    return oBalParValue;
  }

  public void setoBalParValue(Double oBalParValue) {
    this.oBalParValue = oBalParValue;
  }

  @Basic
  @Column(name = "RM_OBalQ")
  public Double getRmOBalQ() {
    return rmOBalQ;
  }

  public void setRmOBalQ(Double rmOBalQ) {
    this.rmOBalQ = rmOBalQ;
  }

  @Basic
  @Column(name = "RM_OBalParValue")
  public Double getRmOBalParValue() {
    return rmOBalParValue;
  }

  public void setRmOBalParValue(Double rmOBalParValue) {
    this.rmOBalParValue = rmOBalParValue;
  }

  @Basic
  @Column(name = "RM_CustodyCD")
  public String getRmCustodyCd() {
    return rmCustodyCd;
  }

  public void setRmCustodyCd(String rmCustodyCd) {
    this.rmCustodyCd = rmCustodyCd;
  }

  @Basic
  @Column(name = "RM_Tcbsid")
  public String getRmTcbsid() {
    return rmTcbsid;
  }

  public void setRmTcbsid(String rmTcbsid) {
    this.rmTcbsid = rmTcbsid;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public int getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(int etlCurDate) {
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

  @Id
  @Column(name = "Id")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ICalendarBondDataEntity that = (ICalendarBondDataEntity) o;
    return etlCurDate == that.etlCurDate &&
      id == that.id &&
      Objects.equals(productEventType, that.productEventType) &&
      Objects.equals(eventType, that.eventType) &&
      Objects.equals(defId, that.defId) &&
      Objects.equals(defType, that.defType) &&
      Objects.equals(objId, that.objId) &&
      Objects.equals(data, that.data) &&
      Objects.equals(eventId, that.eventId) &&
      Objects.equals(eventDueDate, that.eventDueDate) &&
      Objects.equals(eventStatus, that.eventStatus) &&
      Objects.equals(eventIndex, that.eventIndex) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(tcbsId, that.tcbsId) &&
      Objects.equals(oBalQ, that.oBalQ) &&
      Objects.equals(oBalParValue, that.oBalParValue) &&
      Objects.equals(rmOBalQ, that.rmOBalQ) &&
      Objects.equals(rmOBalParValue, that.rmOBalParValue) &&
      Objects.equals(rmCustodyCd, that.rmCustodyCd) &&
      Objects.equals(rmTcbsid, that.rmTcbsid) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productEventType, eventType, defId, defType, objId, data, eventId, eventDueDate, eventStatus, eventIndex, custodyCd, tcbsId, oBalQ, oBalParValue, rmOBalQ, rmOBalParValue,
      rmCustodyCd, rmTcbsid, etlCurDate, etlRunDatetime, id);
  }


  @Step("Get bond static data")
  public List<ICalendarBondDataEntity> byCondition(String parentCus, List<String> childCus, String productType, List<String> eventType, String startDate, String endDate, List<String> eventStatus) {
    try {
      // validation
      if (productType.equals("")) {
        productType = "Bond";
      }
      if (eventType.get(0).equals("")) {
        eventType = Arrays.asList("LISTED", "MATURED", "FROZEN");
      }
      if (eventStatus.get(0).equals("")) {
        eventStatus = Arrays.asList("HAPPENED", "ACTIVE");
      }
      // process
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date fromDate = df.parse(startDate);
      Date toDate = df.parse(endDate);
      Query<ICalendarBondDataEntity> query;
      if (!parentCus.equals("") && childCus.get(0).equals("")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondDataEntity a where a.tcbsId=:tcbsId and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productEventType=:productEventType and a.eventType in :eventType and a.oBalQ>0 and a.oBalQ is not null",
          ICalendarBondDataEntity.class);
        query.setParameter("tcbsId", parentCus);
      } else if (!parentCus.equals("") && childCus.get(0).equals("all")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondDataEntity a where (a.rmTcbsid=:rmTcbsid or a.tcbsId = :rmTcbsid ) and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productEventType=:productEventType and a.eventType in :eventType and a.oBalQ>0 and a.oBalQ is not null and a.rmOBalQ>0 and a.rmOBalQ is not null",
          ICalendarBondDataEntity.class);
        query.setParameter("rmTcbsid", parentCus);
      } else {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondDataEntity a where a.rmTcbsid=:rmTcbsid and a.custodyCd in :custodyCD and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate and a.productEventType=:productEventType and a.eventType in :eventType and a.oBalQ>0 and a.oBalQ is not null and a.rmOBalQ>0 and a.rmOBalQ is not null",
          ICalendarBondDataEntity.class);
        query.setParameter("rmTcbsid", parentCus);
        query.setParameter("custodyCD", childCus);
      }
      query.setParameter("startDate", fromDate);
      query.setParameter("endDate", toDate);
      query.setParameter("eventStatus", eventStatus);
      query.setParameter("productEventType", productType);
      query.setParameter("eventType", eventType);

      List<ICalendarBondDataEntity> result = query.getResultList();
      return result;
    } catch (ParseException ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return null;
    }
  }
}
