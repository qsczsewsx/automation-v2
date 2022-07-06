package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "RT_tcb_BONDPRODUCT")
public class RtTcbBondProductEntity {
  @Id
  private Integer id;
  private Integer categoryid;
  private Integer bondid;
  private String code;
  private Integer unitminimum;
  private String description;
  private Timestamp createddate;
  private Timestamp updateddate;
  private Integer active;
  private Integer groupon;
  private Integer ltv;
  private Integer pricingperunit;
  private String groupcode;
  private String bundle;

  public static void insertRtBondProduct(RtTcbBondProductEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    entity.setCreateddate(entity.getCreateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getCreateddate());
    entity.setUpdateddate(entity.getUpdateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getUpdateddate());
    Query<?> query = session.createNativeQuery("insert into RT_tcb_BONDPRODUCT " +
      "(ID, CATEGORYID, BONDID, CODE, UNITMINIMUM, DESCRIPTION, CREATEDDATE, UPDATEDDATE, ACTIVE, GROUPON, LTV, " +
      "PRICINGPERUNIT, GROUPCODE) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getCategoryid());
    query.setParameter(3, entity.getBondid());
    query.setParameter(4, entity.getCode());
    query.setParameter(5, entity.getUnitminimum());
    query.setParameter(6, entity.getDescription());
    query.setParameter(7, entity.getCreateddate());
    query.setParameter(8, entity.getUpdateddate());
    query.setParameter(9, entity.getActive());
    query.setParameter(10, entity.getGroupon());
    query.setParameter(11, entity.getLtv());
    query.setParameter(12, entity.getPricingperunit());
    query.setParameter(13, entity.getGroupcode());

    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();

  }

  public static void deleteData(RtTcbBondProductEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tcb_BONDPRODUCT WHERE ID = :id ");
    query.setParameter("id", entity.getId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getCategoryid() {
    return categoryid;
  }

  public void setCategoryid(Integer categoryid) {
    this.categoryid = categoryid;
  }

  public Integer getBondid() {
    return bondid;
  }

  public void setBondid(Integer bondid) {
    this.bondid = bondid;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Integer getUnitminimum() {
    return unitminimum;
  }

  public void setUnitminimum(Integer unitminimum) {
    this.unitminimum = unitminimum;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Timestamp createddate) {
    this.createddate = createddate;
  }

  public Timestamp getUpdateddate() {
    return updateddate;
  }

  public void setUpdateddate(Timestamp updateddate) {
    this.updateddate = updateddate;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Integer getGroupon() {
    return groupon;
  }

  public void setGroupon(Integer groupon) {
    this.groupon = groupon;
  }

  public Integer getLtv() {
    return ltv;
  }

  public void setLtv(Integer ltv) {
    this.ltv = ltv;
  }

  public Integer getPricingperunit() {
    return pricingperunit;
  }

  public void setPricingperunit(Integer pricingperunit) {
    this.pricingperunit = pricingperunit;
  }

  public String getGroupcode() {
    return groupcode;
  }

  public void setGroupcode(String groupcode) {
    this.groupcode = groupcode;
  }

  public String getBundle() {
    return bundle;
  }

  public void setBundle(String bundle) {
    this.bundle = bundle;
  }
}
