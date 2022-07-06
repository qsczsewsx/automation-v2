package com.tcbs.automation.tca.industry;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "stx_cpf_Organization", schema = "dbo")
public class StoxTbCompany {
  private String ticker;
  private String shortName;
  private Short typeId;
  private String englishName;
  private String name;
  private String companyTypeBeforeListed;
  private String industryParent;
  private String industryId;
  private String primaryProduct;
  private Short exchangeId;
  private String shareType;
  private Double shareCirculate;
  private Double freefloat;
  private Timestamp firstListingDate;
  private Double shareIssue;
  private Double capitalFund;
  private Double parValue;
  private Double priceListed;
  private String ctckid;

  @Id
  @Column(name = "OrganCode")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "OrganName", insertable = false, updatable = false)
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @Basic
  @Column(name = "ComTypeCode")
  public Short getTypeId() {
    return typeId;
  }

  public void setTypeId(Short typeId) {
    this.typeId = typeId;
  }

  @Basic
  @Column(name = "en_OrganName")
  public String getEnglishName() {
    return englishName;
  }

  public void setEnglishName(String englishName) {
    this.englishName = englishName;
  }

  @Basic
  @Column(name = "OrganName", insertable = false, updatable = false)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "BusinessTypeCode")
  public String getCompanyTypeBeforeListed() {
    return companyTypeBeforeListed;
  }

  public void setCompanyTypeBeforeListed(String companyTypeBeforeListed) {
    this.companyTypeBeforeListed = companyTypeBeforeListed;
  }

  @Basic
  @Column(name = "MarginStatusCode")
  public String getIndustryParent() {
    return industryParent;
  }

  public void setIndustryParent(String industryParent) {
    this.industryParent = industryParent;
  }

  @Basic
  @Column(name = "IcbCode")
  public String getIndustryId() {
    return industryId;
  }

  public void setIndustryId(String industryId) {
    this.industryId = industryId;
  }

  @Basic
  @Column(name = "PrimaryProduct")
  public String getPrimaryProduct() {
    return primaryProduct;
  }

  public void setPrimaryProduct(String primaryProduct) {
    this.primaryProduct = primaryProduct;
  }

  @Basic
  @Column(name = "ComGroupCode")
  public Short getExchangeId() {
    return exchangeId;
  }

  public void setExchangeId(Short exchangeId) {
    this.exchangeId = exchangeId;
  }

  @Basic
  @Column(name = "OutstandingShare")
  public Double getShareCirculate() {
    return shareCirculate;
  }

  public void setShareCirculate(Double shareCirculate) {
    this.shareCirculate = shareCirculate;
  }

  @Basic
  @Column(name = "Freefloat")
  public Double getFreefloat() {
    return freefloat;
  }

  public void setFreefloat(Double freefloat) {
    this.freefloat = freefloat;
  }

  @Basic
  @Column(name = "ListingDate")
  public Timestamp getFirstListingDate() {
    return firstListingDate;
  }

  public void setFirstListingDate(Timestamp firstListingDate) {
    this.firstListingDate = firstListingDate;
  }

  @Basic
  @Column(name = "IssueShare")
  public Double getShareIssue() {
    return shareIssue;
  }

  public void setShareIssue(Double shareIssue) {
    this.shareIssue = shareIssue;
  }

  @Basic
  @Column(name = "CharterCapital")
  public Double getCapitalFund() {
    return capitalFund;
  }

  public void setCapitalFund(Double capitalFund) {
    this.capitalFund = capitalFund;
  }


  @Basic
  @Column(name = "PriceListed")
  public Double getPriceListed() {
    return priceListed;
  }

  public void setPriceListed(Double priceListed) {
    this.priceListed = priceListed;
  }

  @Basic
  @Column(name = "SecurityOrganCode")
  public String getCtckid() {
    return ctckid;
  }

  public void setCtckid(String ctckid) {
    this.ctckid = ctckid;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StoxTbCompany that = (StoxTbCompany) o;
    return
      Objects.equals(ticker, that.ticker) &&
        Objects.equals(shortName, that.shortName) &&
        Objects.equals(typeId, that.typeId) &&
        Objects.equals(englishName, that.englishName) &&
        Objects.equals(name, that.name) &&
        Objects.equals(companyTypeBeforeListed, that.companyTypeBeforeListed) &&
        Objects.equals(industryParent, that.industryParent) &&
        Objects.equals(industryId, that.industryId) &&
        Objects.equals(primaryProduct, that.primaryProduct) &&
        Objects.equals(exchangeId, that.exchangeId) &&
        Objects.equals(shareType, that.shareType) &&
        Objects.equals(shareCirculate, that.shareCirculate) &&
        Objects.equals(freefloat, that.freefloat) &&
        Objects.equals(firstListingDate, that.firstListingDate) &&
        Objects.equals(shareIssue, that.shareIssue) &&
        Objects.equals(capitalFund, that.capitalFund) &&
        Objects.equals(parValue, that.parValue) &&
        Objects.equals(priceListed, that.priceListed) &&
        Objects.equals(ctckid, that.ctckid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ticker, shortName, typeId, englishName, name, companyTypeBeforeListed, industryParent, industryId, primaryProduct, exchangeId, shareType, shareCirculate,
      freefloat, firstListingDate, shareIssue, capitalFund, parValue, priceListed, ctckid
    );
  }
}
