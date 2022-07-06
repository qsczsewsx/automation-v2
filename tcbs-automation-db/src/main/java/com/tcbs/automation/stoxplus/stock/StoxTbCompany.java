package com.tcbs.automation.stoxplus.stock;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "stox_tb_Company", schema = "dbo")
public class StoxTbCompany {
  private int id;
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
  private Timestamp createDate;
  private String index;
  private String shareType;
  private Double shareCirculate;
  private Double freefloat;
  private Timestamp firstListingDate;
  private Double shareIssue;
  private Double capitalFund;
  private Double parValue;
  private Double priceListed;
  private String ctckid;
  private Double numberOfCdpt;
  private Double numberOfEmployees;
  private String websiteHomepage;
  private String websiteCorporateAction;
  private String websiteAboutCompany;
  private String websiteInvesterRelation;
  private String websiteProductServices;
  private String websiteJobHunter;
  private String registedOffice;
  private String districtId;
  private String cityId;
  private String telephoneCc;
  private String telephoneAc;
  private String telephoneNumber;
  private String faxCc;
  private String faxAc;
  private String faxNumber;
  private String numberOfAtm;
  private String numberOfSub1;
  private String numberOfTradeStation;
  private String historyDev;
  private String businessStrategies;
  private String keyDevelopments;
  private String posCompany;
  private String companyProfile;
  private String largeInvestProject;
  private String companyPromise;
  private String businessRisk;
  private String ePrimaryProduct;
  private String eRegistedOffice;
  private String eDistrictId;
  private String eCityId;
  private String eHistoryDev;
  private String eBusinessStrategies;
  private String eKeyDevelopments;
  private String ePosCompany;
  private String eCompanyProfile;
  private String eLargeInvestProject;
  private String eCompanyPromise;
  private String eBusinessRisk;
  private String shortEnglishName;
  private Double f274;
  private Integer marketCapIndex;
  private Double firstVolumn;
  private String jpRegistedOffice;
  private String jpDistrictId;
  private String jpCityId;
  private String jpHistoryDev;
  private String jpBusinessStrategies;
  private String jpKeyDevelopments;
  private String jpPosCompany;
  private String jpCompanyProfile;
  private String jpLargeInvestProject;
  private String jpCompanyPromise;
  private String jpBusinessRisk;
  private Integer marketCapIndexNew;
  private String stoxCode;
  private String jpShortName;
  private String jpName;

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "Ticker")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Basic
  @Column(name = "ShortName")
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @Basic
  @Column(name = "TypeID")
  public Short getTypeId() {
    return typeId;
  }

  public void setTypeId(Short typeId) {
    this.typeId = typeId;
  }

  @Basic
  @Column(name = "EnglishName")
  public String getEnglishName() {
    return englishName;
  }

  public void setEnglishName(String englishName) {
    this.englishName = englishName;
  }

  @Basic
  @Column(name = "Name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "CompanyTypeBeforeListed")
  public String getCompanyTypeBeforeListed() {
    return companyTypeBeforeListed;
  }

  public void setCompanyTypeBeforeListed(String companyTypeBeforeListed) {
    this.companyTypeBeforeListed = companyTypeBeforeListed;
  }

  @Basic
  @Column(name = "IndustryParent")
  public String getIndustryParent() {
    return industryParent;
  }

  public void setIndustryParent(String industryParent) {
    this.industryParent = industryParent;
  }

  @Basic
  @Column(name = "IndustryID")
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
  @Column(name = "ExchangeID")
  public Short getExchangeId() {
    return exchangeId;
  }

  public void setExchangeId(Short exchangeId) {
    this.exchangeId = exchangeId;
  }

  @Basic
  @Column(name = "CreateDate")
  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  @Basic
  @Column(name = "Index")
  public String getIndex() {
    return index;
  }

  public void setIndex(String index) {
    this.index = index;
  }

  @Basic
  @Column(name = "ShareType")
  public String getShareType() {
    return shareType;
  }

  public void setShareType(String shareType) {
    this.shareType = shareType;
  }

  @Basic
  @Column(name = "ShareCirculate")
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
  @Column(name = "FirstListingDate")
  public Timestamp getFirstListingDate() {
    return firstListingDate;
  }

  public void setFirstListingDate(Timestamp firstListingDate) {
    this.firstListingDate = firstListingDate;
  }

  @Basic
  @Column(name = "ShareIssue")
  public Double getShareIssue() {
    return shareIssue;
  }

  public void setShareIssue(Double shareIssue) {
    this.shareIssue = shareIssue;
  }

  @Basic
  @Column(name = "CapitalFund")
  public Double getCapitalFund() {
    return capitalFund;
  }

  public void setCapitalFund(Double capitalFund) {
    this.capitalFund = capitalFund;
  }

  @Basic
  @Column(name = "ParValue")
  public Double getParValue() {
    return parValue;
  }

  public void setParValue(Double parValue) {
    this.parValue = parValue;
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
  @Column(name = "CTCKID")
  public String getCtckid() {
    return ctckid;
  }

  public void setCtckid(String ctckid) {
    this.ctckid = ctckid;
  }

  @Basic
  @Column(name = "NumberOfCDPT")
  public Double getNumberOfCdpt() {
    return numberOfCdpt;
  }

  public void setNumberOfCdpt(Double numberOfCdpt) {
    this.numberOfCdpt = numberOfCdpt;
  }

  @Basic
  @Column(name = "NumberOfEmployees")
  public Double getNumberOfEmployees() {
    return numberOfEmployees;
  }

  public void setNumberOfEmployees(Double numberOfEmployees) {
    this.numberOfEmployees = numberOfEmployees;
  }

  @Basic
  @Column(name = "WebsiteHomepage")
  public String getWebsiteHomepage() {
    return websiteHomepage;
  }

  public void setWebsiteHomepage(String websiteHomepage) {
    this.websiteHomepage = websiteHomepage;
  }

  @Basic
  @Column(name = "WebsiteCorporateAction")
  public String getWebsiteCorporateAction() {
    return websiteCorporateAction;
  }

  public void setWebsiteCorporateAction(String websiteCorporateAction) {
    this.websiteCorporateAction = websiteCorporateAction;
  }

  @Basic
  @Column(name = "WebsiteAboutCompany")
  public String getWebsiteAboutCompany() {
    return websiteAboutCompany;
  }

  public void setWebsiteAboutCompany(String websiteAboutCompany) {
    this.websiteAboutCompany = websiteAboutCompany;
  }

  @Basic
  @Column(name = "WebsiteInvesterRelation")
  public String getWebsiteInvesterRelation() {
    return websiteInvesterRelation;
  }

  public void setWebsiteInvesterRelation(String websiteInvesterRelation) {
    this.websiteInvesterRelation = websiteInvesterRelation;
  }

  @Basic
  @Column(name = "WebsiteProductServices")
  public String getWebsiteProductServices() {
    return websiteProductServices;
  }

  public void setWebsiteProductServices(String websiteProductServices) {
    this.websiteProductServices = websiteProductServices;
  }

  @Basic
  @Column(name = "WebsiteJobHunter")
  public String getWebsiteJobHunter() {
    return websiteJobHunter;
  }

  public void setWebsiteJobHunter(String websiteJobHunter) {
    this.websiteJobHunter = websiteJobHunter;
  }

  @Basic
  @Column(name = "RegistedOffice")
  public String getRegistedOffice() {
    return registedOffice;
  }

  public void setRegistedOffice(String registedOffice) {
    this.registedOffice = registedOffice;
  }

  @Basic
  @Column(name = "DistrictID")
  public String getDistrictId() {
    return districtId;
  }

  public void setDistrictId(String districtId) {
    this.districtId = districtId;
  }

  @Basic
  @Column(name = "CityID")
  public String getCityId() {
    return cityId;
  }

  public void setCityId(String cityId) {
    this.cityId = cityId;
  }

  @Basic
  @Column(name = "Telephone_CC")
  public String getTelephoneCc() {
    return telephoneCc;
  }

  public void setTelephoneCc(String telephoneCc) {
    this.telephoneCc = telephoneCc;
  }

  @Basic
  @Column(name = "Telephone_AC")
  public String getTelephoneAc() {
    return telephoneAc;
  }

  public void setTelephoneAc(String telephoneAc) {
    this.telephoneAc = telephoneAc;
  }

  @Basic
  @Column(name = "Telephone_Number")
  public String getTelephoneNumber() {
    return telephoneNumber;
  }

  public void setTelephoneNumber(String telephoneNumber) {
    this.telephoneNumber = telephoneNumber;
  }

  @Basic
  @Column(name = "Fax_CC")
  public String getFaxCc() {
    return faxCc;
  }

  public void setFaxCc(String faxCc) {
    this.faxCc = faxCc;
  }

  @Basic
  @Column(name = "Fax_AC")
  public String getFaxAc() {
    return faxAc;
  }

  public void setFaxAc(String faxAc) {
    this.faxAc = faxAc;
  }

  @Basic
  @Column(name = "Fax_Number")
  public String getFaxNumber() {
    return faxNumber;
  }

  public void setFaxNumber(String faxNumber) {
    this.faxNumber = faxNumber;
  }

  @Basic
  @Column(name = "NumberOfATM")
  public String getNumberOfAtm() {
    return numberOfAtm;
  }

  public void setNumberOfAtm(String numberOfAtm) {
    this.numberOfAtm = numberOfAtm;
  }

  @Basic
  @Column(name = "NumberOfSub1")
  public String getNumberOfSub1() {
    return numberOfSub1;
  }

  public void setNumberOfSub1(String numberOfSub1) {
    this.numberOfSub1 = numberOfSub1;
  }

  @Basic
  @Column(name = "NumberOfTradeStation")
  public String getNumberOfTradeStation() {
    return numberOfTradeStation;
  }

  public void setNumberOfTradeStation(String numberOfTradeStation) {
    this.numberOfTradeStation = numberOfTradeStation;
  }

  @Basic
  @Column(name = "HistoryDev")
  public String getHistoryDev() {
    return historyDev;
  }

  public void setHistoryDev(String historyDev) {
    this.historyDev = historyDev;
  }

  @Basic
  @Column(name = "BusinessStrategies")
  public String getBusinessStrategies() {
    return businessStrategies;
  }

  public void setBusinessStrategies(String businessStrategies) {
    this.businessStrategies = businessStrategies;
  }

  @Basic
  @Column(name = "KeyDevelopments")
  public String getKeyDevelopments() {
    return keyDevelopments;
  }

  public void setKeyDevelopments(String keyDevelopments) {
    this.keyDevelopments = keyDevelopments;
  }

  @Basic
  @Column(name = "PosCompany")
  public String getPosCompany() {
    return posCompany;
  }

  public void setPosCompany(String posCompany) {
    this.posCompany = posCompany;
  }

  @Basic
  @Column(name = "CompanyProfile")
  public String getCompanyProfile() {
    return companyProfile;
  }

  public void setCompanyProfile(String companyProfile) {
    this.companyProfile = companyProfile;
  }

  @Basic
  @Column(name = "LargeInvestProject")
  public String getLargeInvestProject() {
    return largeInvestProject;
  }

  public void setLargeInvestProject(String largeInvestProject) {
    this.largeInvestProject = largeInvestProject;
  }

  @Basic
  @Column(name = "CompanyPromise")
  public String getCompanyPromise() {
    return companyPromise;
  }

  public void setCompanyPromise(String companyPromise) {
    this.companyPromise = companyPromise;
  }

  @Basic
  @Column(name = "BusinessRisk")
  public String getBusinessRisk() {
    return businessRisk;
  }

  public void setBusinessRisk(String businessRisk) {
    this.businessRisk = businessRisk;
  }

  @Basic
  @Column(name = "ePrimaryProduct")
  public String getePrimaryProduct() {
    return ePrimaryProduct;
  }

  public void setePrimaryProduct(String ePrimaryProduct) {
    this.ePrimaryProduct = ePrimaryProduct;
  }

  @Basic
  @Column(name = "eRegistedOffice")
  public String geteRegistedOffice() {
    return eRegistedOffice;
  }

  public void seteRegistedOffice(String eRegistedOffice) {
    this.eRegistedOffice = eRegistedOffice;
  }

  @Basic
  @Column(name = "eDistrictID")
  public String geteDistrictId() {
    return eDistrictId;
  }

  public void seteDistrictId(String eDistrictId) {
    this.eDistrictId = eDistrictId;
  }

  @Basic
  @Column(name = "eCityID")
  public String geteCityId() {
    return eCityId;
  }

  public void seteCityId(String eCityId) {
    this.eCityId = eCityId;
  }

  @Basic
  @Column(name = "eHistoryDev")
  public String geteHistoryDev() {
    return eHistoryDev;
  }

  public void seteHistoryDev(String eHistoryDev) {
    this.eHistoryDev = eHistoryDev;
  }

  @Basic
  @Column(name = "eBusinessStrategies")
  public String geteBusinessStrategies() {
    return eBusinessStrategies;
  }

  public void seteBusinessStrategies(String eBusinessStrategies) {
    this.eBusinessStrategies = eBusinessStrategies;
  }

  @Basic
  @Column(name = "eKeyDevelopments")
  public String geteKeyDevelopments() {
    return eKeyDevelopments;
  }

  public void seteKeyDevelopments(String eKeyDevelopments) {
    this.eKeyDevelopments = eKeyDevelopments;
  }

  @Basic
  @Column(name = "ePosCompany")
  public String getePosCompany() {
    return ePosCompany;
  }

  public void setePosCompany(String ePosCompany) {
    this.ePosCompany = ePosCompany;
  }

  @Basic
  @Column(name = "eCompanyProfile")
  public String geteCompanyProfile() {
    return eCompanyProfile;
  }

  public void seteCompanyProfile(String eCompanyProfile) {
    this.eCompanyProfile = eCompanyProfile;
  }

  @Basic
  @Column(name = "eLargeInvestProject")
  public String geteLargeInvestProject() {
    return eLargeInvestProject;
  }

  public void seteLargeInvestProject(String eLargeInvestProject) {
    this.eLargeInvestProject = eLargeInvestProject;
  }

  @Basic
  @Column(name = "eCompanyPromise")
  public String geteCompanyPromise() {
    return eCompanyPromise;
  }

  public void seteCompanyPromise(String eCompanyPromise) {
    this.eCompanyPromise = eCompanyPromise;
  }

  @Basic
  @Column(name = "eBusinessRisk")
  public String geteBusinessRisk() {
    return eBusinessRisk;
  }

  public void seteBusinessRisk(String eBusinessRisk) {
    this.eBusinessRisk = eBusinessRisk;
  }

  @Basic
  @Column(name = "ShortEnglishName")
  public String getShortEnglishName() {
    return shortEnglishName;
  }

  public void setShortEnglishName(String shortEnglishName) {
    this.shortEnglishName = shortEnglishName;
  }

  @Basic
  @Column(name = "F2_74")
  public Double getF274() {
    return f274;
  }

  public void setF274(Double f274) {
    this.f274 = f274;
  }

  @Basic
  @Column(name = "MarketCapIndex")
  public Integer getMarketCapIndex() {
    return marketCapIndex;
  }

  public void setMarketCapIndex(Integer marketCapIndex) {
    this.marketCapIndex = marketCapIndex;
  }

  @Basic
  @Column(name = "FirstVolumn")
  public Double getFirstVolumn() {
    return firstVolumn;
  }

  public void setFirstVolumn(Double firstVolumn) {
    this.firstVolumn = firstVolumn;
  }

  @Basic
  @Column(name = "jpRegistedOffice")
  public String getJpRegistedOffice() {
    return jpRegistedOffice;
  }

  public void setJpRegistedOffice(String jpRegistedOffice) {
    this.jpRegistedOffice = jpRegistedOffice;
  }

  @Basic
  @Column(name = "jpDistrictID")
  public String getJpDistrictId() {
    return jpDistrictId;
  }

  public void setJpDistrictId(String jpDistrictId) {
    this.jpDistrictId = jpDistrictId;
  }

  @Basic
  @Column(name = "jpCityID")
  public String getJpCityId() {
    return jpCityId;
  }

  public void setJpCityId(String jpCityId) {
    this.jpCityId = jpCityId;
  }

  @Basic
  @Column(name = "jpHistoryDev")
  public String getJpHistoryDev() {
    return jpHistoryDev;
  }

  public void setJpHistoryDev(String jpHistoryDev) {
    this.jpHistoryDev = jpHistoryDev;
  }

  @Basic
  @Column(name = "jpBusinessStrategies")
  public String getJpBusinessStrategies() {
    return jpBusinessStrategies;
  }

  public void setJpBusinessStrategies(String jpBusinessStrategies) {
    this.jpBusinessStrategies = jpBusinessStrategies;
  }

  @Basic
  @Column(name = "jpKeyDevelopments")
  public String getJpKeyDevelopments() {
    return jpKeyDevelopments;
  }

  public void setJpKeyDevelopments(String jpKeyDevelopments) {
    this.jpKeyDevelopments = jpKeyDevelopments;
  }

  @Basic
  @Column(name = "jpPosCompany")
  public String getJpPosCompany() {
    return jpPosCompany;
  }

  public void setJpPosCompany(String jpPosCompany) {
    this.jpPosCompany = jpPosCompany;
  }

  @Basic
  @Column(name = "jpCompanyProfile")
  public String getJpCompanyProfile() {
    return jpCompanyProfile;
  }

  public void setJpCompanyProfile(String jpCompanyProfile) {
    this.jpCompanyProfile = jpCompanyProfile;
  }

  @Basic
  @Column(name = "jpLargeInvestProject")
  public String getJpLargeInvestProject() {
    return jpLargeInvestProject;
  }

  public void setJpLargeInvestProject(String jpLargeInvestProject) {
    this.jpLargeInvestProject = jpLargeInvestProject;
  }

  @Basic
  @Column(name = "jpCompanyPromise")
  public String getJpCompanyPromise() {
    return jpCompanyPromise;
  }

  public void setJpCompanyPromise(String jpCompanyPromise) {
    this.jpCompanyPromise = jpCompanyPromise;
  }

  @Basic
  @Column(name = "jpBusinessRisk")
  public String getJpBusinessRisk() {
    return jpBusinessRisk;
  }

  public void setJpBusinessRisk(String jpBusinessRisk) {
    this.jpBusinessRisk = jpBusinessRisk;
  }

  @Basic
  @Column(name = "MarketCapIndex_new")
  public Integer getMarketCapIndexNew() {
    return marketCapIndexNew;
  }

  public void setMarketCapIndexNew(Integer marketCapIndexNew) {
    this.marketCapIndexNew = marketCapIndexNew;
  }

  @Basic
  @Column(name = "StoxCode")
  public String getStoxCode() {
    return stoxCode;
  }

  public void setStoxCode(String stoxCode) {
    this.stoxCode = stoxCode;
  }

  @Basic
  @Column(name = "jpShortName")
  public String getJpShortName() {
    return jpShortName;
  }

  public void setJpShortName(String jpShortName) {
    this.jpShortName = jpShortName;
  }

  @Basic
  @Column(name = "jpName")
  public String getJpName() {
    return jpName;
  }

  public void setJpName(String jpName) {
    this.jpName = jpName;
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
    return id == that.id &&
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
      Objects.equals(createDate, that.createDate) &&
      Objects.equals(index, that.index) &&
      Objects.equals(shareType, that.shareType) &&
      Objects.equals(shareCirculate, that.shareCirculate) &&
      Objects.equals(freefloat, that.freefloat) &&
      Objects.equals(firstListingDate, that.firstListingDate) &&
      Objects.equals(shareIssue, that.shareIssue) &&
      Objects.equals(capitalFund, that.capitalFund) &&
      Objects.equals(parValue, that.parValue) &&
      Objects.equals(priceListed, that.priceListed) &&
      Objects.equals(ctckid, that.ctckid) &&
      Objects.equals(numberOfCdpt, that.numberOfCdpt) &&
      Objects.equals(numberOfEmployees, that.numberOfEmployees) &&
      Objects.equals(websiteHomepage, that.websiteHomepage) &&
      Objects.equals(websiteCorporateAction, that.websiteCorporateAction) &&
      Objects.equals(websiteAboutCompany, that.websiteAboutCompany) &&
      Objects.equals(websiteInvesterRelation, that.websiteInvesterRelation) &&
      Objects.equals(websiteProductServices, that.websiteProductServices) &&
      Objects.equals(websiteJobHunter, that.websiteJobHunter) &&
      Objects.equals(registedOffice, that.registedOffice) &&
      Objects.equals(districtId, that.districtId) &&
      Objects.equals(cityId, that.cityId) &&
      Objects.equals(telephoneCc, that.telephoneCc) &&
      Objects.equals(telephoneAc, that.telephoneAc) &&
      Objects.equals(telephoneNumber, that.telephoneNumber) &&
      Objects.equals(faxCc, that.faxCc) &&
      Objects.equals(faxAc, that.faxAc) &&
      Objects.equals(faxNumber, that.faxNumber) &&
      Objects.equals(numberOfAtm, that.numberOfAtm) &&
      Objects.equals(numberOfSub1, that.numberOfSub1) &&
      Objects.equals(numberOfTradeStation, that.numberOfTradeStation) &&
      Objects.equals(historyDev, that.historyDev) &&
      Objects.equals(businessStrategies, that.businessStrategies) &&
      Objects.equals(keyDevelopments, that.keyDevelopments) &&
      Objects.equals(posCompany, that.posCompany) &&
      Objects.equals(companyProfile, that.companyProfile) &&
      Objects.equals(largeInvestProject, that.largeInvestProject) &&
      Objects.equals(companyPromise, that.companyPromise) &&
      Objects.equals(businessRisk, that.businessRisk) &&
      Objects.equals(ePrimaryProduct, that.ePrimaryProduct) &&
      Objects.equals(eRegistedOffice, that.eRegistedOffice) &&
      Objects.equals(eDistrictId, that.eDistrictId) &&
      Objects.equals(eCityId, that.eCityId) &&
      Objects.equals(eHistoryDev, that.eHistoryDev) &&
      Objects.equals(eBusinessStrategies, that.eBusinessStrategies) &&
      Objects.equals(eKeyDevelopments, that.eKeyDevelopments) &&
      Objects.equals(ePosCompany, that.ePosCompany) &&
      Objects.equals(eCompanyProfile, that.eCompanyProfile) &&
      Objects.equals(eLargeInvestProject, that.eLargeInvestProject) &&
      Objects.equals(eCompanyPromise, that.eCompanyPromise) &&
      Objects.equals(eBusinessRisk, that.eBusinessRisk) &&
      Objects.equals(shortEnglishName, that.shortEnglishName) &&
      Objects.equals(f274, that.f274) &&
      Objects.equals(marketCapIndex, that.marketCapIndex) &&
      Objects.equals(firstVolumn, that.firstVolumn) &&
      Objects.equals(jpRegistedOffice, that.jpRegistedOffice) &&
      Objects.equals(jpDistrictId, that.jpDistrictId) &&
      Objects.equals(jpCityId, that.jpCityId) &&
      Objects.equals(jpHistoryDev, that.jpHistoryDev) &&
      Objects.equals(jpBusinessStrategies, that.jpBusinessStrategies) &&
      Objects.equals(jpKeyDevelopments, that.jpKeyDevelopments) &&
      Objects.equals(jpPosCompany, that.jpPosCompany) &&
      Objects.equals(jpCompanyProfile, that.jpCompanyProfile) &&
      Objects.equals(jpLargeInvestProject, that.jpLargeInvestProject) &&
      Objects.equals(jpCompanyPromise, that.jpCompanyPromise) &&
      Objects.equals(jpBusinessRisk, that.jpBusinessRisk) &&
      Objects.equals(marketCapIndexNew, that.marketCapIndexNew) &&
      Objects.equals(stoxCode, that.stoxCode) &&
      Objects.equals(jpShortName, that.jpShortName) &&
      Objects.equals(jpName, that.jpName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, ticker, shortName, typeId, englishName, name, companyTypeBeforeListed, industryParent, industryId, primaryProduct, exchangeId, createDate, index, shareType, shareCirculate,
      freefloat, firstListingDate, shareIssue, capitalFund, parValue, priceListed, ctckid, numberOfCdpt, numberOfEmployees, websiteHomepage, websiteCorporateAction, websiteAboutCompany,
      websiteInvesterRelation, websiteProductServices, websiteJobHunter, registedOffice, districtId, cityId, telephoneCc, telephoneAc, telephoneNumber, faxCc, faxAc, faxNumber, numberOfAtm,
      numberOfSub1, numberOfTradeStation, historyDev, businessStrategies, keyDevelopments, posCompany, companyProfile, largeInvestProject, companyPromise, businessRisk, ePrimaryProduct,
      eRegistedOffice,
      eDistrictId, eCityId, eHistoryDev, eBusinessStrategies, eKeyDevelopments, ePosCompany, eCompanyProfile, eLargeInvestProject, eCompanyPromise, eBusinessRisk, shortEnglishName, f274,
      marketCapIndex,
      firstVolumn, jpRegistedOffice, jpDistrictId, jpCityId, jpHistoryDev, jpBusinessStrategies, jpKeyDevelopments, jpPosCompany, jpCompanyProfile, jpLargeInvestProject, jpCompanyPromise,
      jpBusinessRisk, marketCapIndexNew, stoxCode, jpShortName, jpName);
  }
}
