package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Stg_tci_INV_BOND_ATTR")
public class StgTciInvBondAttrEntity {
  private Integer id;
  private String name;
  private String value;
  private String caption;
  private Integer bondStaticId;
  private Timestamp timestamp;
  private String applytype;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
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
  @Column(name = "BOND_STATIC_ID")
  public Integer getBondStaticId() {
    return bondStaticId;
  }

  public void setBondStaticId(Integer bondStaticId) {
    this.bondStaticId = bondStaticId;
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
  @Column(name = "APPLYTYPE")
  public String getApplytype() {
    return applytype;
  }

  public void setApplytype(String applytype) {
    this.applytype = applytype;
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
    StgTciInvBondAttrEntity that = (StgTciInvBondAttrEntity) o;
    return id == that.id &&
      Objects.equals(name, that.name) &&
      Objects.equals(value, that.value) &&
      Objects.equals(caption, that.caption) &&
      Objects.equals(bondStaticId, that.bondStaticId) &&
      Objects.equals(timestamp, that.timestamp) &&
      Objects.equals(applytype, that.applytype) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, value, caption, bondStaticId, timestamp, applytype, etlCurDate, etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveBonAttr(StgTciInvBondAttrEntity bondInfo) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(bondInfo);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByBondStaticId(Integer bondStaticId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTciInvBondAttrEntity> query = session.createQuery(
      "DELETE FROM StgTciInvBondAttrEntity i WHERE i.bondStaticId=:bondStaticId"
    );
    query.setParameter("bondStaticId", bondStaticId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("Get data by key")
  public List<StgTciInvBondAttrEntity> getByBondStaticIdAndValue(Integer bondStaticId, String value) {
    Query<StgTciInvBondAttrEntity> query = Dwh.dwhDbConnection.getSession().createQuery(
      "from StgTciInvBondAttrEntity a where a.bondStaticId=:bondStaticId and a.value=:value", StgTciInvBondAttrEntity.class);
    query.setParameter("bondStaticId", bondStaticId);
    query.setParameter("value", value);
    List<StgTciInvBondAttrEntity> result = query.getResultList();
    return result;
  }
}
