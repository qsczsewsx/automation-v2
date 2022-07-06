package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "RT_tcb_TRADING_ATTR")
public class RtTcbTradingAttrEntity {
  private int id;
  private Integer orderId;
  private String name;
  private String value;
  private String caption;
  private Byte type;
  private Timestamp createdDate;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("insert data")
  public static void insertRtTradingAttr(RtTcbTradingAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Date createdDate = entity.getCreatedDate() == null ? new Date() : entity.getCreatedDate();
    Query<?> query = session.createNativeQuery("insert into RT_tcb_TRADING_ATTR " +
      "(ID, ORDER_ID, NAME, VALUE, CAPTION, [TYPE], CREATED_DATE, EtlCurDate, EtlRunDateTime) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getOrderId());
    query.setParameter(3, entity.getName());
    query.setParameter(4, entity.getValue());
    query.setParameter(5, entity.getCaption());
    query.setParameter(6, entity.getType());
    query.setParameter(7, createdDate);
    query.setParameter(8, Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date())));
    query.setParameter(9, new Date());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteTradingAttrByOrderId(RtTcbTradingAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tcb_TRADING_ATTR WHERE ORDER_ID = :orderId ");
    query.setParameter("orderId", entity.getOrderId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "ORDER_ID")
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  @Basic
  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "VALUE")
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Basic
  @Column(name = "CAPTION")
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  @Basic
  @Column(name = "TYPE")
  public Byte getType() {
    return type;
  }

  public void setType(Byte type) {
    this.type = type;
  }

  @Basic
  @Column(name = "CREATED_DATE")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
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
  @Column(name = "EtlRunDateTime")
  public Timestamp getEtlRunDateTime() {
    return etlRunDateTime;
  }

  public void setEtlRunDateTime(Timestamp etlRunDateTime) {
    this.etlRunDateTime = etlRunDateTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTcbTradingAttrEntity that = (RtTcbTradingAttrEntity) o;
    return id == that.id &&
      Objects.equals(orderId, that.orderId) &&
      Objects.equals(name, that.name) &&
      Objects.equals(value, that.value) &&
      Objects.equals(caption, that.caption) &&
      Objects.equals(type, that.type) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, orderId, name, value, caption, type, createdDate, etlCurDate, etlRunDateTime);
  }
}
