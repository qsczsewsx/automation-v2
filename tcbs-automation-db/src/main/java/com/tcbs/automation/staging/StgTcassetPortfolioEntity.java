package com.tcbs.automation.staging;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Stg_tcasset_PORTFOLIO")
public class StgTcassetPortfolioEntity {
  private double id;
  private String code;
  private String name;
  private String portfolioTypeCode;
  private String portfolioClassId;
  private Date createdTimestamp;
  private Date updatedTimestamp;
  private String status;
  private String permission;
  private String nameEn;
  private Double ownerId;
  private int etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Column(name = "ID")
  public double getId() {
    return id;
  }

  public void setId(double id) {
    this.id = id;
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
  @Column(name = "NAME")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "PORTFOLIO_TYPE_CODE")
  public String getPortfolioTypeCode() {
    return portfolioTypeCode;
  }

  public void setPortfolioTypeCode(String portfolioTypeCode) {
    this.portfolioTypeCode = portfolioTypeCode;
  }

  @Basic
  @Column(name = "PORTFOLIO_CLASS_ID")
  public String getPortfolioClassId() {
    return portfolioClassId;
  }

  public void setPortfolioClassId(String portfolioClassId) {
    this.portfolioClassId = portfolioClassId;
  }

  @Basic
  @Column(name = "CREATED_TIMESTAMP")
  public Date getCreatedTimestamp() {
    return createdTimestamp;
  }

  public void setCreatedTimestamp(Date createdTimestamp) {
    this.createdTimestamp = createdTimestamp;
  }

  @Basic
  @Column(name = "UPDATED_TIMESTAMP")
  public Date getUpdatedTimestamp() {
    return updatedTimestamp;
  }

  public void setUpdatedTimestamp(Date updatedTimestamp) {
    this.updatedTimestamp = updatedTimestamp;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "PERMISSION")
  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  @Basic
  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Basic
  @Column(name = "OWNER_ID")
  public Double getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Double ownerId) {
    this.ownerId = ownerId;
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
    StgTcassetPortfolioEntity that = (StgTcassetPortfolioEntity) o;
    return Double.compare(that.id, id) == 0 &&
      etlCurDate == that.etlCurDate &&
      Objects.equals(code, that.code) &&
      Objects.equals(name, that.name) &&
      Objects.equals(portfolioTypeCode, that.portfolioTypeCode) &&
      Objects.equals(portfolioClassId, that.portfolioClassId) &&
      Objects.equals(createdTimestamp, that.createdTimestamp) &&
      Objects.equals(updatedTimestamp, that.updatedTimestamp) &&
      Objects.equals(status, that.status) &&
      Objects.equals(permission, that.permission) &&
      Objects.equals(nameEn, that.nameEn) &&
      Objects.equals(ownerId, that.ownerId) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, name, portfolioTypeCode, portfolioClassId, createdTimestamp, updatedTimestamp, status, permission, nameEn, ownerId, etlCurDate, etlRunDateTime);
  }
}
