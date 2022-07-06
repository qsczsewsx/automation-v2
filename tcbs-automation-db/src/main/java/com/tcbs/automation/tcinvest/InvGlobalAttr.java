package com.tcbs.automation.tcinvest;

import com.tcbs.automation.enumerable.bond.ApplyType;
import com.tcbs.automation.enumerable.bond.AttributeName;
import lombok.SneakyThrows;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;
import static com.tcbs.automation.functions.PublicConstant.dateTimeFormat;

@Entity
@Table(name = "INV_GLOBAL_ATTR")
public class InvGlobalAttr {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CAPTION")
  private String caption;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "APPLYTYPE")
  private String applytype;

  public InvGlobalAttr() {
  }

  @SneakyThrows
  public InvGlobalAttr(String name, String value, String caption, Date timestamp, String applytype) {
    this.name = name;
    this.value = value;
    this.caption = caption;
    this.timestamp = timestamp;
    this.applytype = applytype;
  }

  public Long getId() {
    return id;
  }

  public InvGlobalAttr setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public InvGlobalAttr setName(String name) {
    this.name = name;
    return this;
  }

  public InvGlobalAttr setName(AttributeName name) {
    this.name = name.getValue();
    return this;
  }

  public String getValue() {
    return value;
  }

  public InvGlobalAttr setValue(String value) {
    this.value = value;
    return this;
  }

  public String getCaption() {
    return caption;
  }

  public InvGlobalAttr setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @SneakyThrows
  public InvGlobalAttr setTimestamp(String timestamp) {
    this.timestamp = dateTimeFormat.parse(timestamp);
    return this;
  }

  public InvGlobalAttr setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getApplytype() {
    return applytype;
  }

  public InvGlobalAttr setApplytype(String applytype) {
    this.applytype = applytype;
    return this;
  }

  public InvGlobalAttr setApplytype(ApplyType applytype) {
    this.applytype = applytype.getValue();
    return this;
  }


  @SuppressWarnings("unchecked")
  public void updateAttribute_value_byName(String name, String value) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    Transaction trans = beginTransaction(session);

    Query<InvGlobalAttr> query = session.createQuery(
      "UPDATE InvGlobalAttr i\n" +
        "    SET i.value=:value\n" +
        "    where i.name=:name"
    );

    query.setParameter("value", value);
    query.setParameter("name", name);

    query.executeUpdate();
    trans.commit();
  }

  public void insert() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvGlobalAttr WHERE name=:name"
    );
    query.setParameter("name", name);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void delete_by_id() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    Transaction trans = beginTransaction(session);
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvGlobalAttr WHERE id=:id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }

  @Step("Lay data theo truong name trong bang inv_global_attr")
  public InvGlobalAttr getDataByName(String name) {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<InvGlobalAttr> query = TcInvest.tcInvestDbConnection.getSession().createQuery(
      "from InvGlobalAttr where name=:name", InvGlobalAttr.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }

  @Override
  public String toString() {
    return "InvGlobalAttr{" +
      "name='" + name + '\'' +
      ", value='" + value + '\'' +
      ", caption='" + caption + '\'' +
      ", timestamp='" + timestamp + '\'' +
      ", applytype='" + applytype + '\'' +
      '}';
  }
}
