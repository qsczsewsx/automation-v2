package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Stg_tci_INV_BOND")
public class StgTciInvBondEntity {
  private Integer bondId;
  private Integer bondCategoryId;
  private Integer bondStaticId;
  private String code;
  private Double unitMinimum;
  private String description;
  private Byte active;
  private Double ltv;
  private Double pricingPerUnit;
  private Timestamp createdDate;
  private Timestamp updatedDate;
  private Integer period;
  private String periodType;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Column(name = "BOND_ID")
  public Integer getBondId() {
    return bondId;
  }

  public void setBondId(Integer bondId) {
    this.bondId = bondId;
  }

  @Basic
  @Column(name = "BOND_CATEGORY_ID")
  public Integer getBondCategoryId() {
    return bondCategoryId;
  }

  public void setBondCategoryId(Integer bondCategoryId) {
    this.bondCategoryId = bondCategoryId;
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
  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "UNIT_MINIMUM")
  public Double getUnitMinimum() {
    return unitMinimum;
  }

  public void setUnitMinimum(Double unitMinimum) {
    this.unitMinimum = unitMinimum;
  }

  @Basic
  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "ACTIVE")
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
  }

  @Basic
  @Column(name = "LTV")
  public Double getLtv() {
    return ltv;
  }

  public void setLtv(Double ltv) {
    this.ltv = ltv;
  }

  @Basic
  @Column(name = "PRICING_PER_UNIT")
  public Double getPricingPerUnit() {
    return pricingPerUnit;
  }

  public void setPricingPerUnit(Double pricingPerUnit) {
    this.pricingPerUnit = pricingPerUnit;
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
  @Column(name = "UPDATED_DATE")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Basic
  @Column(name = "PERIOD")
  public Integer getPeriod() {
    return period;
  }

  public void setPeriod(Integer period) {
    this.period = period;
  }

  @Basic
  @Column(name = "PERIOD_TYPE")
  public String getPeriodType() {
    return periodType;
  }

  public void setPeriodType(String periodType) {
    this.periodType = periodType;
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
    StgTciInvBondEntity that = (StgTciInvBondEntity) o;
    return bondId == that.bondId &&
      bondCategoryId == that.bondCategoryId &&
      bondStaticId == that.bondStaticId &&
      Objects.equals(code, that.code) &&
      Objects.equals(unitMinimum, that.unitMinimum) &&
      Objects.equals(description, that.description) &&
      Objects.equals(active, that.active) &&
      Objects.equals(ltv, that.ltv) &&
      Objects.equals(pricingPerUnit, that.pricingPerUnit) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(updatedDate, that.updatedDate) &&
      Objects.equals(period, that.period) &&
      Objects.equals(periodType, that.periodType) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bondId, bondCategoryId, bondStaticId, code, unitMinimum, description, active, ltv, pricingPerUnit, createdDate, updatedDate, period, periodType, etlCurDate, etlRunDateTime);
  }

  @Step("insert data")
  public boolean saveBondInfo(StgTciInvBondEntity bondEntity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.beginTransaction();
    Integer id = (Integer) session.save(bondEntity);
    session.getTransaction().commit();
    return true;
  }

  @Step("delete data by key")
  public void deleteByBondCode(String bondCode) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTciInvBondEntity> query = session.createQuery(
      "DELETE FROM StgTciInvBondEntity i WHERE i.code=:code"
    );
    query.setParameter("code", bondCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
