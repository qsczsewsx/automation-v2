package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.functions.PublicConstant;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "COMPANY_MANAGEMENT")
public class CompanyManagement {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "COMPANY_ID")
  private String companyId;
  @Column(name = "REGION_ID")
  private String regionId;
  @Column(name = "SEGMENT_ID")
  private String segmentId;
  @Column(name = "SECTOR_L1_ID")
  private String sectorL1Id;
  @Column(name = "SECTOR_L2_ID")
  private String sectorL2Id;
  @Column(name = "PIC_SENIOR")
  private String picSenior;
  @Column(name = "PIC_DCM")
  private String picDcm;
  @Column(name = "PIC_ANALYST")
  private String picAnalyst;
  @Column(name = "CST_REP_IB")
  private String cstRepIb;
  @Column(name = "TCB_RM")
  private String tcbRm;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "COMPANY_GROUP_TYPE_ID")
  private String companyGroupTypeId;
  @Column(name = "COMPANY_PARENT_ID")
  private String companyParentId;
  @Column(name = "PARENT_OWNERSHIP_RATE")
  private Double parentOwnershipRate;
  @Column(name = "FI_SALE")
  private String fiSale;


  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }


  public String getRegionId() {
    return regionId;
  }

  public void setRegionId(String regionId) {
    this.regionId = regionId;
  }


  public String getSegmentId() {
    return segmentId;
  }

  public void setSegmentId(String segmentId) {
    this.segmentId = segmentId;
  }


  public String getSectorL1Id() {
    return sectorL1Id;
  }

  public void setSectorL1Id(String sectorL1Id) {
    this.sectorL1Id = sectorL1Id;
  }


  public String getSectorL2Id() {
    return sectorL2Id;
  }

  public void setSectorL2Id(String sectorL2Id) {
    this.sectorL2Id = sectorL2Id;
  }


  public String getPicSenior() {
    return picSenior;
  }

  public void setPicSenior(String picSenior) {
    this.picSenior = picSenior;
  }


  public String getPicDcm() {
    return picDcm;
  }

  public void setPicDcm(String picDcm) {
    this.picDcm = picDcm;
  }


  public String getPicAnalyst() {
    return picAnalyst;
  }

  public void setPicAnalyst(String picAnalyst) {
    this.picAnalyst = picAnalyst;
  }


  public String getCstRepIb() {
    return cstRepIb;
  }

  public void setCstRepIb(String cstRepIb) {
    this.cstRepIb = cstRepIb;
  }


  public String getTcbRm() {
    return tcbRm;
  }

  public void setTcbRm(String tcbRm) {
    this.tcbRm = tcbRm;
  }


  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getUpdatedAt() {
    return updatedAt == null ? null : PublicConstant.dateTimeFormat.format(updatedAt);
  }

  public void setUpdatedAt(String updatedAt) throws ParseException {
    this.updatedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedAt).getTime());
  }


  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }


  public String getCompanyGroupTypeId() {
    return companyGroupTypeId;
  }

  public void setCompanyGroupTypeId(String companyGroupTypeId) {
    this.companyGroupTypeId = companyGroupTypeId;
  }


  public String getCompanyParentId() {
    return companyParentId;
  }

  public void setCompanyParentId(String companyParentId) {
    this.companyParentId = companyParentId;
  }


  public Double getParentOwnershipRate() {
    return parentOwnershipRate;
  }

  public void setParentOwnershipRate(Double parentOwnershipRate) {
    this.parentOwnershipRate = parentOwnershipRate;
  }


  public String getFiSale() {
    return fiSale;
  }

  public void setFiSale(String fiSale) {
    this.fiSale = fiSale;
  }

}
