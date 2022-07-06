package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "iCalendar_BondProExpried")
public class ICalendarBondProExpriedEntity {
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
  private String tradingCode;
  private BigDecimal oBalQ;
  private BigDecimal receivableAmountProExpired;
  private BigDecimal investmentRateProExpired;
  private Timestamp maturityDate;
  private BigDecimal receivableAmountHtm;
  private BigDecimal investmentRateHtm;
  private String rmCustodyCd;
  private String rmTcbsid;
  private int etlCurDate;
  private Timestamp etlRunDatetime;
  private Integer id;

  @Step("Get bond pro expired static data")
  public static List<ICalendarBondProExpriedEntity> byCondition(String parentCus, List<String> childCus, String startDate, String endDate, List<String> eventStatus) {
    try {
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      Date fromDate = df.parse(startDate);
      Date toDate = df.parse(endDate);
      Query<ICalendarBondProExpriedEntity> query;
      Session session = Dwh.dwhDbConnection.getSession();
      session.clear();
      beginTransaction(session);
      if (!parentCus.equals("") && childCus.get(0).equals("")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondProExpriedEntity a where a.tcbsId=:tcbsId and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate", ICalendarBondProExpriedEntity.class);
        query.setParameter("tcbsId", parentCus);
      } else if (!parentCus.equals("") && childCus.get(0).equals("all")) {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondProExpriedEntity a where a.rmTcbsid=:rmTcbsid and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate", ICalendarBondProExpriedEntity.class);
        query.setParameter("rmTcbsid", parentCus);
      } else {
        query = Dwh.dwhDbConnection.getSession().createQuery(
          "from ICalendarBondProExpriedEntity a where a.rmTcbsid=:rmTcbsid and a.custodyCd in :custodyCD and a.eventStatus in :eventStatus and a.eventDueDate between :startDate and :endDate",
          ICalendarBondProExpriedEntity.class);
        query.setParameter("rmTcbsid", parentCus);
        query.setParameter("custodyCD", childCus);
      }
      query.setParameter("startDate", fromDate);
      query.setParameter("endDate", toDate);
      query.setParameter("eventStatus", eventStatus);

      List<ICalendarBondProExpriedEntity> result = query.getResultList();
      return result;
    } catch (ParseException ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return null;
    }
  }

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
  @Column(name = "TradingCode")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
  }

  @Basic
  @Column(name = "OBalQ")
  public BigDecimal getoBalQ() {
    return oBalQ;
  }

  public void setoBalQ(BigDecimal oBalQ) {
    this.oBalQ = oBalQ;
  }

  @Basic
  @Column(name = "receivableAmountProExpired")
  public BigDecimal getReceivableAmountProExpired() {
    return receivableAmountProExpired;
  }

  public void setReceivableAmountProExpired(BigDecimal receivableAmountProExpired) {
    this.receivableAmountProExpired = receivableAmountProExpired;
  }

  @Basic
  @Column(name = "investmentRateProExpired")
  public BigDecimal getInvestmentRateProExpired() {
    return investmentRateProExpired;
  }

  public void setInvestmentRateProExpired(BigDecimal investmentRateProExpired) {
    this.investmentRateProExpired = investmentRateProExpired;
  }

  @Basic
  @Column(name = "maturityDate")
  public Timestamp getMaturityDate() {
    return maturityDate;
  }

  public void setMaturityDate(Timestamp maturityDate) {
    this.maturityDate = maturityDate;
  }

  @Basic
  @Column(name = "receivableAmountHTM")
  public BigDecimal getReceivableAmountHtm() {
    return receivableAmountHtm;
  }

  public void setReceivableAmountHtm(BigDecimal receivableAmountHtm) {
    this.receivableAmountHtm = receivableAmountHtm;
  }

  @Basic
  @Column(name = "investmentRateHTM")
  public BigDecimal getInvestmentRateHtm() {
    return investmentRateHtm;
  }

  public void setInvestmentRateHtm(BigDecimal investmentRateHtm) {
    this.investmentRateHtm = investmentRateHtm;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    ICalendarBondProExpriedEntity that = (ICalendarBondProExpriedEntity) o;
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
      Objects.equals(tradingCode, that.tradingCode) &&
      Objects.equals(oBalQ, that.oBalQ) &&
      Objects.equals(receivableAmountProExpired, that.receivableAmountProExpired) &&
      Objects.equals(investmentRateProExpired, that.investmentRateProExpired) &&
      Objects.equals(maturityDate, that.maturityDate) &&
      Objects.equals(receivableAmountHtm, that.receivableAmountHtm) &&
      Objects.equals(investmentRateHtm, that.investmentRateHtm) &&
      Objects.equals(rmCustodyCd, that.rmCustodyCd) &&
      Objects.equals(rmTcbsid, that.rmTcbsid) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productEventType, eventType, defId, defType, objId, data, eventId, eventDueDate, eventStatus, eventIndex, custodyCd, tcbsId, tradingCode, oBalQ, receivableAmountProExpired,
      investmentRateProExpired, maturityDate, receivableAmountHtm, investmentRateHtm, rmCustodyCd, rmTcbsid, etlCurDate, etlRunDatetime, id);
  }

  @Step("insert data")
  public boolean saveBondProExpired(ICalendarBondProExpriedEntity bondProExpiredEntity) {
    try {
      Session session = Dwh.dwhDbConnection.getSession();
      session.clear();
      beginTransaction(session);
      Integer id = (Integer) session.save(bondProExpiredEntity);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return true;
  }

  @Step("delete data")
  public boolean deleteBondProExpired(ICalendarBondProExpriedEntity proExpriedEntity) {
    try {
      Session session = Dwh.dwhDbConnection.getSession();
      session.clear();
      beginTransaction(session);
      session.delete(proExpriedEntity);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return true;
  }

  @Step("delete data by key")
  public void deleteByTradingCode(String code) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<ICalendarBondProExpriedEntity> query = session.createQuery(
      "DELETE FROM ICalendarBondProExpriedEntity i WHERE i.tradingCode=:tradingCode"
    );
    query.setParameter("tradingCode", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
