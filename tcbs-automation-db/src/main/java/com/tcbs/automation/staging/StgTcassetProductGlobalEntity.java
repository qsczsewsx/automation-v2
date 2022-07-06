package com.tcbs.automation.staging;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "Stg_tcasset_PRODUCT_GLOBAL")
public class StgTcassetProductGlobalEntity {
  private double id;
  private String code;
  private String description;
  private String ticker;
  private String listedName;
  private Double type;
  private String typeCode;
  private Double status;
  private Double exchange;
  private Double par;
  private Date issueTimestamp;
  private Date maturityTimestamp;
  private Date listedTimestamp;
  private Date delistedTimestamp;
  private String couponPaymentType;
  private String convention;
  private Double company;
  private Double companyGroup;
  private String inputSource;
  private Date createdTimestamp;
  private Date updatedTimestamp;
  private String industry;
  private BigDecimal earlyWithdrawnInterest;
  private Short autoRollover;
  private BigDecimal autoRolloverInterest;
  private Short interestPeriodExcluding;
  private BigDecimal holidayInterest;
  private String couponPaymentMethod;
  private String name;
  private String nameEn;
  private String metaData;
  private Integer etlCurDate;
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
  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "TICKER")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "LISTED_NAME")
  public String getListedName() {
    return listedName;
  }

  public void setListedName(String listedName) {
    this.listedName = listedName;
  }

  @Basic
  @Column(name = "TYPE")
  public Double getType() {
    return type;
  }

  public void setType(Double type) {
    this.type = type;
  }

  @Basic
  @Column(name = "TYPE_CODE")
  public String getTypeCode() {
    return typeCode;
  }

  public void setTypeCode(String typeCode) {
    this.typeCode = typeCode;
  }

  @Basic
  @Column(name = "STATUS")
  public Double getStatus() {
    return status;
  }

  public void setStatus(Double status) {
    this.status = status;
  }

  @Basic
  @Column(name = "EXCHANGE")
  public Double getExchange() {
    return exchange;
  }

  public void setExchange(Double exchange) {
    this.exchange = exchange;
  }

  @Basic
  @Column(name = "PAR")
  public Double getPar() {
    return par;
  }

  public void setPar(Double par) {
    this.par = par;
  }

  @Basic
  @Column(name = "ISSUE_TIMESTAMP")
  public Date getIssueTimestamp() {
    return issueTimestamp;
  }

  public void setIssueTimestamp(Date issueTimestamp) {
    this.issueTimestamp = issueTimestamp;
  }

  @Basic
  @Column(name = "MATURITY_TIMESTAMP")
  public Date getMaturityTimestamp() {
    return maturityTimestamp;
  }

  public void setMaturityTimestamp(Date maturityTimestamp) {
    this.maturityTimestamp = maturityTimestamp;
  }

  @Basic
  @Column(name = "LISTED_TIMESTAMP")
  public Date getListedTimestamp() {
    return listedTimestamp;
  }

  public void setListedTimestamp(Date listedTimestamp) {
    this.listedTimestamp = listedTimestamp;
  }

  @Basic
  @Column(name = "DELISTED_TIMESTAMP")
  public Date getDelistedTimestamp() {
    return delistedTimestamp;
  }

  public void setDelistedTimestamp(Date delistedTimestamp) {
    this.delistedTimestamp = delistedTimestamp;
  }

  @Basic
  @Column(name = "COUPON_PAYMENT_TYPE")
  public String getCouponPaymentType() {
    return couponPaymentType;
  }

  public void setCouponPaymentType(String couponPaymentType) {
    this.couponPaymentType = couponPaymentType;
  }

  @Basic
  @Column(name = "CONVENTION")
  public String getConvention() {
    return convention;
  }

  public void setConvention(String convention) {
    this.convention = convention;
  }

  @Basic
  @Column(name = "COMPANY")
  public Double getCompany() {
    return company;
  }

  public void setCompany(Double company) {
    this.company = company;
  }

  @Basic
  @Column(name = "COMPANY_GROUP")
  public Double getCompanyGroup() {
    return companyGroup;
  }

  public void setCompanyGroup(Double companyGroup) {
    this.companyGroup = companyGroup;
  }

  @Basic
  @Column(name = "INPUT_SOURCE")
  public String getInputSource() {
    return inputSource;
  }

  public void setInputSource(String inputSource) {
    this.inputSource = inputSource;
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
  @Column(name = "INDUSTRY")
  public String getIndustry() {
    return industry;
  }

  public void setIndustry(String industry) {
    this.industry = industry;
  }

  @Basic
  @Column(name = "EARLY_WITHDRAWN_INTEREST")
  public BigDecimal getEarlyWithdrawnInterest() {
    return earlyWithdrawnInterest;
  }

  public void setEarlyWithdrawnInterest(BigDecimal earlyWithdrawnInterest) {
    this.earlyWithdrawnInterest = earlyWithdrawnInterest;
  }

  @Basic
  @Column(name = "AUTO_ROLLOVER")
  public Short getAutoRollover() {
    return autoRollover;
  }

  public void setAutoRollover(Short autoRollover) {
    this.autoRollover = autoRollover;
  }

  @Basic
  @Column(name = "AUTO_ROLLOVER_INTEREST")
  public BigDecimal getAutoRolloverInterest() {
    return autoRolloverInterest;
  }

  public void setAutoRolloverInterest(BigDecimal autoRolloverInterest) {
    this.autoRolloverInterest = autoRolloverInterest;
  }

  @Basic
  @Column(name = "INTEREST_PERIOD_EXCLUDING")
  public Short getInterestPeriodExcluding() {
    return interestPeriodExcluding;
  }

  public void setInterestPeriodExcluding(Short interestPeriodExcluding) {
    this.interestPeriodExcluding = interestPeriodExcluding;
  }

  @Basic
  @Column(name = "HOLIDAY_INTEREST")
  public BigDecimal getHolidayInterest() {
    return holidayInterest;
  }

  public void setHolidayInterest(BigDecimal holidayInterest) {
    this.holidayInterest = holidayInterest;
  }

  @Basic
  @Column(name = "COUPON_PAYMENT_METHOD")
  public String getCouponPaymentMethod() {
    return couponPaymentMethod;
  }

  public void setCouponPaymentMethod(String couponPaymentMethod) {
    this.couponPaymentMethod = couponPaymentMethod;
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
  @Column(name = "NAME_EN")
  public String getNameEn() {
    return nameEn;
  }

  public void setNameEn(String nameEn) {
    this.nameEn = nameEn;
  }

  @Basic
  @Column(name = "META_DATA")
  public String getMetaData() {
    return metaData;
  }

  public void setMetaData(String metaData) {
    this.metaData = metaData;
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
    StgTcassetProductGlobalEntity that = (StgTcassetProductGlobalEntity) o;
    return Double.compare(that.id, id) == 0 &&
      Objects.equals(code, that.code) &&
      Objects.equals(description, that.description) &&
      Objects.equals(ticker, that.ticker) &&
      Objects.equals(listedName, that.listedName) &&
      Objects.equals(type, that.type) &&
      Objects.equals(typeCode, that.typeCode) &&
      Objects.equals(status, that.status) &&
      Objects.equals(exchange, that.exchange) &&
      Objects.equals(par, that.par) &&
      Objects.equals(issueTimestamp, that.issueTimestamp) &&
      Objects.equals(maturityTimestamp, that.maturityTimestamp) &&
      Objects.equals(listedTimestamp, that.listedTimestamp) &&
      Objects.equals(delistedTimestamp, that.delistedTimestamp) &&
      Objects.equals(couponPaymentType, that.couponPaymentType) &&
      Objects.equals(convention, that.convention) &&
      Objects.equals(company, that.company) &&
      Objects.equals(companyGroup, that.companyGroup) &&
      Objects.equals(inputSource, that.inputSource) &&
      Objects.equals(createdTimestamp, that.createdTimestamp) &&
      Objects.equals(updatedTimestamp, that.updatedTimestamp) &&
      Objects.equals(industry, that.industry) &&
      Objects.equals(earlyWithdrawnInterest, that.earlyWithdrawnInterest) &&
      Objects.equals(autoRollover, that.autoRollover) &&
      Objects.equals(autoRolloverInterest, that.autoRolloverInterest) &&
      Objects.equals(interestPeriodExcluding, that.interestPeriodExcluding) &&
      Objects.equals(holidayInterest, that.holidayInterest) &&
      Objects.equals(couponPaymentMethod, that.couponPaymentMethod) &&
      Objects.equals(name, that.name) &&
      Objects.equals(nameEn, that.nameEn) &&
      Objects.equals(metaData, that.metaData) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, code, description, ticker, listedName, type, typeCode, status, exchange, par, issueTimestamp, maturityTimestamp, listedTimestamp, delistedTimestamp, couponPaymentType,
      convention, company, companyGroup, inputSource, createdTimestamp, updatedTimestamp, industry, earlyWithdrawnInterest, autoRollover, autoRolloverInterest, interestPeriodExcluding,
      holidayInterest,
      couponPaymentMethod, name, nameEn, metaData, etlCurDate, etlRunDateTime);
  }
}
