package com.tcbs.automation.tcinvest;

import com.tcbs.automation.enumerable.bond.ApplyType;
import com.tcbs.automation.enumerable.bond.AttributeName;
import com.tcbs.automation.enumerable.bond.AttributeValue;
import lombok.SneakyThrows;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

import static com.tcbs.automation.functions.PublicConstant.dateTimeFormat;

@Entity
@Table(name = "INV_BOND_ATTR")
public class InvBondAttr {

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
  @Column(name = "BOND_STATIC_ID")
  private Long bondStaticId;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "APPLYTYPE")
  private String applytype;


  public InvBondAttr() {
  }

  @SneakyThrows
  public InvBondAttr(String name, String value, String caption, Long bondStaticId, Date timestamp, String applytype) {
    this.name = name;
    this.value = value;
    this.caption = caption;
    this.bondStaticId = bondStaticId;
    this.timestamp = timestamp;
    this.applytype = applytype;
  }

  public Long getId() {
    return id;
  }

  public InvBondAttr setId(Long id) {
    this.id = id;
    return this;
  }

  public String getName() {
    return name;
  }

  public InvBondAttr setName(String name) {
    this.name = name;
    return this;
  }

  public InvBondAttr setName(AttributeName name) {
    this.name = name.getValue();
    return this;
  }

  public String getValue() {
    return value;
  }

  public InvBondAttr setValue(String value) {
    this.value = value;
    return this;
  }

  public InvBondAttr setValue(AttributeValue value) {
    this.value = value.getValue();
    return this;
  }

  public String getCaption() {
    return caption;
  }

  public InvBondAttr setCaption(String caption) {
    this.caption = caption;
    return this;
  }

  public Long getBondStaticId() {
    return bondStaticId;
  }

  public InvBondAttr setBondStaticId(Long bondStaticId) {
    this.bondStaticId = bondStaticId;
    return this;
  }

  public Date getTimestamp() {
    return timestamp;
  }

  @SneakyThrows
  public InvBondAttr setTimestamp(String timestamp) {
    this.timestamp = dateTimeFormat.parse(timestamp);
    return this;
  }

  public InvBondAttr setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  public String getApplytype() {
    return applytype;
  }

  public InvBondAttr setApplytype(String applytype) {
    this.applytype = applytype;
    return this;
  }

  public InvBondAttr setApplytype(ApplyType applytype) {
    this.applytype = applytype.getValue();
    return this;
  }

  @Override
  public String toString() {
    return "InvBondAttr{" +
      "name='" + name + '\'' +
      ", value='" + value + '\'' +
      ", caption='" + caption + '\'' +
      ", bondStaticId=" + bondStaticId +
      ", timestamp='" + timestamp + '\'' +
      ", applytype='" + applytype + '\'' +
      '}';
  }

  @SuppressWarnings("unchecked")
  public void updateAttribute_value_byName(String name, String value, Long bondStaticId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    Transaction trans = session.beginTransaction();

    Query<InvGlobalAttr> query = session.createQuery(
      "UPDATE InvBondAttr i\n" +
        "    SET i.value=:value\n" +
        "    where i.name=:name AND i.bondStaticId=:bondStaticId"
    );

    query.setParameter("value", value);
    query.setParameter("name", name);
    query.setParameter("bondStaticId", bondStaticId);

    query.executeUpdate();
    trans.commit();
  }

  @SuppressWarnings("unchecked")
  public void delete() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvBondAttr WHERE name=:name AND bondStaticId = :bondStaticId"
    );
    query.setParameter("name", name);
    query.setParameter("bondStaticId", bondStaticId);
    query.executeUpdate();
    trans.commit();
  }

  public void insert() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public void delete_by_id() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvBondAttr WHERE id=:id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void deleteByStatus(Long bondId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery(
      "DELETE FROM InvBondAttr WHERE bondStaticId=:id and name='status'"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
