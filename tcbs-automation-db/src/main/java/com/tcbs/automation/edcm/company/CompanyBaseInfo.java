package com.tcbs.automation.edcm.company;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "COMPANY_BASE_INFO")
public class CompanyBaseInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "COMPANY_ID")
  private String companyId;
  @NotNull
  @Column(name = "NAME")
  private String name;
  @Column(name = "ENG_NAME")
  private String engName;
  @Column(name = "SHORT_NAME")
  private String shortName;
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "IS_ACTIVE")
  private Integer isActive;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;

  @Step
  public List<CompanyBaseInfo> getListData() {
    EdcmConnection.connection.getSession().clear();
    Query<CompanyBaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "from CompanyBaseInfo a WHERE a.isActive =:isActive", CompanyBaseInfo.class);
    query.setParameter("isActive", 1);
    return query.getResultList();
  }

  public String getCompanyId() {
    return companyId;
  }

  public void setCompanyId(String companyId) {
    this.companyId = companyId;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getEngName() {
    return engName;
  }

  public void setEngName(String engName) {
    this.engName = engName;
  }


  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }


  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }


  public Integer getIsActive() {
    return isActive;
  }

  public void setIsActive(Integer isActive) {
    this.isActive = isActive;
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

}
