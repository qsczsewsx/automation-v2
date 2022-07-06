package com.tcbs.automation.tcbond;

import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "INV_ORDER_ATTR")
public class InvOrderAttr {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CAPTION")
  private String caption;
  @Column(name = "ORDER_ID")
  private Long orderId;
  @Column(name = "TIMESTAMP")
  private Timestamp timestamp;


  public InvOrderAttr(Long id, String name, String value, String caption, Long orderId, Timestamp timestamp) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.caption = caption;
    this.orderId = orderId;
    this.timestamp = timestamp;
  }

  public InvOrderAttr() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getTimestamp() {
    return timestamp == null ? null : PublicConstant.dateTimeFormat.format(timestamp);
  }

  public void setTimestamp(String timestamp) throws ParseException {
    this.timestamp = new Timestamp(PublicConstant.dateTimeFormat.parse(timestamp).getTime());
  }

  @Step
  public InvOrderAttr getDataByNameAndValue(String name, String value) throws Exception {
    InvOrderAttr result = null;
    Query<InvOrderAttr> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from InvOrderAttr where name=:name and value=:value", InvOrderAttr.class);
    query.setParameter("name", name);
    query.setParameter("value", value);
    List<InvOrderAttr> listResult = query.getResultList();
    if (listResult.size() == 1) {
      result = listResult.get(0);
    }
    return result;
  }

}
