package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Data
@Table(name = "RT_tcb_INV_ORDER_ATTR")
public class RtTcbInvOrderAttrEntity {
  private Long id;
  private String name;
  private String value;
  private String caption;
  private Integer orderId;
  private Timestamp timestamp;
  private String channel;

  @Step("insert data")
  public static boolean insertData(RtTcbInvOrderAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    entity.setTimestamp(entity.getTimestamp() == null ? new Timestamp(System.currentTimeMillis()) : entity.getTimestamp());
    session.save(entity);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by object")
  public static void deleteData(RtTcbInvOrderAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query query = session.createQuery("DELETE RtTcbInvOrderAttrEntity WHERE orderId = :orderId ");
    query.setParameter("orderId", entity.getOrderId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  @Basic
  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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
  @Column(name = "ORDER_ID")
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  @Basic
  @Column(name = "TIMESTAMP")
  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Basic
  @Column(name = "CHANNEL")
  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTcbInvOrderAttrEntity that = (RtTcbInvOrderAttrEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(value, that.value) && Objects.equals(caption, that.caption) && Objects.equals(orderId,
      that.orderId) && Objects.equals(timestamp, that.timestamp) && Objects.equals(channel, that.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, value, caption, orderId, timestamp, channel);
  }
}
