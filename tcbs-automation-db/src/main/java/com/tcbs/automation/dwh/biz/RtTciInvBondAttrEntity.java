package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "RT_tci_INV_BOND_ATTR")
public class RtTciInvBondAttrEntity {
  private Integer id;
  private String name;
  private String value;
  private String caption;
  private Integer bondStaticId;
  private Timestamp timestamp;
  private String applytype;


  @Step("insert data")
  public static void insertData(RtTciInvBondAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into RT_tci_INV_BOND_ATTR " +
      "(ID, NAME, VALUE, CAPTION, BOND_STATIC_ID, [TIMESTAMP], APPLYTYPE) " +
      "values (?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getName());
    query.setParameter(3, entity.getValue());
    query.setParameter(4, entity.getCaption());
    query.setParameter(5, entity.getBondStaticId());
    query.setParameter(6, entity.getTimestamp());
    query.setParameter(7, entity.getApplytype());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteById(RtTciInvBondAttrEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tci_INV_BOND_ATTR WHERE ID = :id ");
    query.setParameter("id", entity.getId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  @Basic
  @Column(name = "ID", nullable = true, precision = 0)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "NAME", nullable = true, length = 100)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "VALUE", nullable = true, length = -1)
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Basic
  @Column(name = "CAPTION", nullable = true, length = 1024)
  public String getCaption() {
    return caption;
  }

  public void setCaption(String caption) {
    this.caption = caption;
  }

  @Basic
  @Column(name = "BOND_STATIC_ID", nullable = true, precision = 0)
  public Integer getBondStaticId() {
    return bondStaticId;
  }

  public void setBondStaticId(Integer bondStaticId) {
    this.bondStaticId = bondStaticId;
  }

  @Basic
  @Column(name = "TIMESTAMP", nullable = true)
  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  @Basic
  @Column(name = "APPLYTYPE", nullable = true, length = 100)
  public String getApplytype() {
    return applytype;
  }

  public void setApplytype(String applytype) {
    this.applytype = applytype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTciInvBondAttrEntity that = (RtTciInvBondAttrEntity) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(value, that.value) &&
      Objects.equals(caption, that.caption) &&
      Objects.equals(bondStaticId, that.bondStaticId) &&
      Objects.equals(timestamp, that.timestamp) &&
      Objects.equals(applytype, that.applytype);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, value, caption, bondStaticId, timestamp, applytype);
  }
}
