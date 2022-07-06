package com.tcbs.automation.tcbsdbapi1;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "Stg_tcb_Customer")
public class StgTcbCustomer {
  private Integer customerId;
  private String customerName;
  private String identityCard;
  private String identityBy;
  private Timestamp identityDate;
  private String address;
  private String phone;
  private String fax;
  private String email;
  private String bankCode;
  private Integer agencyId;
  private String representative;
  private String authorizationDocument;
  private Timestamp createdDate;
  private String customerCode;
  private Timestamp birthday;
  private Byte active;
  private Integer grouponId;
  private String title;
  private String password;
  private String branchCode;
  private String branchName;
  private String bankName;
  private String bankCity;
  private String custodyCode;
  private Byte gender;
  private String addressCity;
  private String tcbsid;
  private String loanBankAccount;
  private String loanBankAgency;
  private Double customerIncomePerMonth;
  private String escrowAccount;
  private String registrationBookAddress;
  private Integer spouse;
  private String spouseName;
  private String spouseIdNo;
  private String spouseIssueBy;
  private Double spouseIncomePerMonth;
  private Timestamp spouseIssueDate;
  private Integer representativeId;
  private String customerCodeFromTcb;
  private Timestamp startDateEb;
  private String cusBranchCd;
  private String saleId;
  private Integer apType;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Id
  @Basic
  @Column(name = "CustomerId")
  public Integer getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  @Basic
  @Column(name = "CustomerName")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  @Basic
  @Column(name = "IdentityCard")
  public String getIdentityCard() {
    return identityCard;
  }

  public void setIdentityCard(String identityCard) {
    this.identityCard = identityCard;
  }

  @Basic
  @Column(name = "IdentityBy")
  public String getIdentityBy() {
    return identityBy;
  }

  public void setIdentityBy(String identityBy) {
    this.identityBy = identityBy;
  }

  @Basic
  @Column(name = "IdentityDate")
  public Timestamp getIdentityDate() {
    return identityDate;
  }

  public void setIdentityDate(Timestamp identityDate) {
    this.identityDate = identityDate;
  }

  @Basic
  @Column(name = "Address")
  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @Basic
  @Column(name = "Phone")
  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Basic
  @Column(name = "Fax")
  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  @Basic
  @Column(name = "Email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "BankCode")
  public String getBankCode() {
    return bankCode;
  }

  public void setBankCode(String bankCode) {
    this.bankCode = bankCode;
  }

  @Basic
  @Column(name = "AgencyId")
  public Integer getAgencyId() {
    return agencyId;
  }

  public void setAgencyId(Integer agencyId) {
    this.agencyId = agencyId;
  }

  @Basic
  @Column(name = "Representative")
  public String getRepresentative() {
    return representative;
  }

  public void setRepresentative(String representative) {
    this.representative = representative;
  }

  @Basic
  @Column(name = "AuthorizationDocument")
  public String getAuthorizationDocument() {
    return authorizationDocument;
  }

  public void setAuthorizationDocument(String authorizationDocument) {
    this.authorizationDocument = authorizationDocument;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "CustomerCode")
  public String getCustomerCode() {
    return customerCode;
  }

  public void setCustomerCode(String customerCode) {
    this.customerCode = customerCode;
  }

  @Basic
  @Column(name = "Birthday")
  public Timestamp getBirthday() {
    return birthday;
  }

  public void setBirthday(Timestamp birthday) {
    this.birthday = birthday;
  }

  @Basic
  @Column(name = "Active")
  public Byte getActive() {
    return active;
  }

  public void setActive(Byte active) {
    this.active = active;
  }

  @Basic
  @Column(name = "GrouponId")
  public Integer getGrouponId() {
    return grouponId;
  }

  public void setGrouponId(Integer grouponId) {
    this.grouponId = grouponId;
  }

  @Basic
  @Column(name = "Title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "Password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Basic
  @Column(name = "BranchCode")
  public String getBranchCode() {
    return branchCode;
  }

  public void setBranchCode(String branchCode) {
    this.branchCode = branchCode;
  }

  @Basic
  @Column(name = "BranchName")
  public String getBranchName() {
    return branchName;
  }

  public void setBranchName(String branchName) {
    this.branchName = branchName;
  }

  @Basic
  @Column(name = "BankName")
  public String getBankName() {
    return bankName;
  }

  public void setBankName(String bankName) {
    this.bankName = bankName;
  }

  @Basic
  @Column(name = "BankCity")
  public String getBankCity() {
    return bankCity;
  }

  public void setBankCity(String bankCity) {
    this.bankCity = bankCity;
  }

  @Basic
  @Column(name = "CustodyCode")
  public String getCustodyCode() {
    return custodyCode;
  }

  public void setCustodyCode(String custodyCode) {
    this.custodyCode = custodyCode;
  }

  @Basic
  @Column(name = "Gender")
  public Byte getGender() {
    return gender;
  }

  public void setGender(Byte gender) {
    this.gender = gender;
  }

  @Basic
  @Column(name = "AddressCity")
  public String getAddressCity() {
    return addressCity;
  }

  public void setAddressCity(String addressCity) {
    this.addressCity = addressCity;
  }

  @Basic
  @Column(name = "Tcbsid")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "LoanBankAccount")
  public String getLoanBankAccount() {
    return loanBankAccount;
  }

  public void setLoanBankAccount(String loanBankAccount) {
    this.loanBankAccount = loanBankAccount;
  }

  @Basic
  @Column(name = "LoanBankAgency")
  public String getLoanBankAgency() {
    return loanBankAgency;
  }

  public void setLoanBankAgency(String loanBankAgency) {
    this.loanBankAgency = loanBankAgency;
  }

  @Basic
  @Column(name = "CustomerIncomePerMonth")
  public Double getCustomerIncomePerMonth() {
    return customerIncomePerMonth;
  }

  public void setCustomerIncomePerMonth(Double customerIncomePerMonth) {
    this.customerIncomePerMonth = customerIncomePerMonth;
  }

  @Basic
  @Column(name = "EscrowAccount")
  public String getEscrowAccount() {
    return escrowAccount;
  }

  public void setEscrowAccount(String escrowAccount) {
    this.escrowAccount = escrowAccount;
  }

  @Basic
  @Column(name = "RegistrationBookAddress")
  public String getRegistrationBookAddress() {
    return registrationBookAddress;
  }

  public void setRegistrationBookAddress(String registrationBookAddress) {
    this.registrationBookAddress = registrationBookAddress;
  }

  @Basic
  @Column(name = "Spouse")
  public Integer getSpouse() {
    return spouse;
  }

  public void setSpouse(Integer spouse) {
    this.spouse = spouse;
  }

  @Basic
  @Column(name = "SpouseName")
  public String getSpouseName() {
    return spouseName;
  }

  public void setSpouseName(String spouseName) {
    this.spouseName = spouseName;
  }

  @Basic
  @Column(name = "SpouseIDNo")
  public String getSpouseIdNo() {
    return spouseIdNo;
  }

  public void setSpouseIdNo(String spouseIdNo) {
    this.spouseIdNo = spouseIdNo;
  }

  @Basic
  @Column(name = "SpouseIssueBy")
  public String getSpouseIssueBy() {
    return spouseIssueBy;
  }

  public void setSpouseIssueBy(String spouseIssueBy) {
    this.spouseIssueBy = spouseIssueBy;
  }

  @Basic
  @Column(name = "SpouseIncomePerMonth")
  public Double getSpouseIncomePerMonth() {
    return spouseIncomePerMonth;
  }

  public void setSpouseIncomePerMonth(Double spouseIncomePerMonth) {
    this.spouseIncomePerMonth = spouseIncomePerMonth;
  }

  @Basic
  @Column(name = "SpouseIssueDate")
  public Timestamp getSpouseIssueDate() {
    return spouseIssueDate;
  }

  public void setSpouseIssueDate(Timestamp spouseIssueDate) {
    this.spouseIssueDate = spouseIssueDate;
  }

  @Basic
  @Column(name = "RepresentativeID")
  public Integer getRepresentativeId() {
    return representativeId;
  }

  public void setRepresentativeId(Integer representativeId) {
    this.representativeId = representativeId;
  }

  @Basic
  @Column(name = "CustomerCode_fromTCB")
  public String getCustomerCodeFromTcb() {
    return customerCodeFromTcb;
  }

  public void setCustomerCodeFromTcb(String customerCodeFromTcb) {
    this.customerCodeFromTcb = customerCodeFromTcb;
  }

  @Basic
  @Column(name = "START_DATE_EB")
  public Timestamp getStartDateEb() {
    return startDateEb;
  }

  public void setStartDateEb(Timestamp startDateEb) {
    this.startDateEb = startDateEb;
  }

  @Basic
  @Column(name = "CUS_BRANCH_CD")
  public String getCusBranchCd() {
    return cusBranchCd;
  }

  public void setCusBranchCd(String cusBranchCd) {
    this.cusBranchCd = cusBranchCd;
  }

  @Basic
  @Column(name = "Sale_ID")
  public String getSaleId() {
    return saleId;
  }

  public void setSaleId(String saleId) {
    this.saleId = saleId;
  }

  @Basic
  @Column(name = "APType")
  public Integer getApType() {
    return apType;
  }

  public void setApType(Integer apType) {
    this.apType = apType;
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
    com.tcbs.automation.tcbsdbapi1.StgTcbCustomer that = (com.tcbs.automation.tcbsdbapi1.StgTcbCustomer) o;
    return customerId == that.customerId &&
      Objects.equals(customerName, that.customerName) &&
      Objects.equals(identityCard, that.identityCard) &&
      Objects.equals(identityBy, that.identityBy) &&
      Objects.equals(identityDate, that.identityDate) &&
      Objects.equals(address, that.address) &&
      Objects.equals(phone, that.phone) &&
      Objects.equals(fax, that.fax) &&
      Objects.equals(email, that.email) &&
      Objects.equals(bankCode, that.bankCode) &&
      Objects.equals(agencyId, that.agencyId) &&
      Objects.equals(representative, that.representative) &&
      Objects.equals(authorizationDocument, that.authorizationDocument) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(customerCode, that.customerCode) &&
      Objects.equals(birthday, that.birthday) &&
      Objects.equals(active, that.active) &&
      Objects.equals(grouponId, that.grouponId) &&
      Objects.equals(title, that.title) &&
      Objects.equals(password, that.password) &&
      Objects.equals(branchCode, that.branchCode) &&
      Objects.equals(branchName, that.branchName) &&
      Objects.equals(bankName, that.bankName) &&
      Objects.equals(bankCity, that.bankCity) &&
      Objects.equals(custodyCode, that.custodyCode) &&
      Objects.equals(gender, that.gender) &&
      Objects.equals(addressCity, that.addressCity) &&
      Objects.equals(tcbsid, that.tcbsid) &&
      Objects.equals(loanBankAccount, that.loanBankAccount) &&
      Objects.equals(loanBankAgency, that.loanBankAgency) &&
      Objects.equals(customerIncomePerMonth, that.customerIncomePerMonth) &&
      Objects.equals(escrowAccount, that.escrowAccount) &&
      Objects.equals(registrationBookAddress, that.registrationBookAddress) &&
      Objects.equals(spouse, that.spouse) &&
      Objects.equals(spouseName, that.spouseName) &&
      Objects.equals(spouseIdNo, that.spouseIdNo) &&
      Objects.equals(spouseIssueBy, that.spouseIssueBy) &&
      Objects.equals(spouseIncomePerMonth, that.spouseIncomePerMonth) &&
      Objects.equals(spouseIssueDate, that.spouseIssueDate) &&
      Objects.equals(representativeId, that.representativeId) &&
      Objects.equals(customerCodeFromTcb, that.customerCodeFromTcb) &&
      Objects.equals(startDateEb, that.startDateEb) &&
      Objects.equals(cusBranchCd, that.cusBranchCd) &&
      Objects.equals(saleId, that.saleId) &&
      Objects.equals(apType, that.apType) &&
      Objects.equals(etlCurDate, that.etlCurDate) &&
      Objects.equals(etlRunDateTime, that.etlRunDateTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, customerName, identityCard, identityBy, identityDate, address, phone, fax, email, bankCode, agencyId, representative, authorizationDocument, createdDate,
      customerCode, birthday, active, grouponId, title, password, branchCode, branchName, bankName, bankCity, custodyCode, gender, addressCity, tcbsid, loanBankAccount, loanBankAgency,
      customerIncomePerMonth, escrowAccount, registrationBookAddress, spouse, spouseName, spouseIdNo, spouseIssueBy, spouseIncomePerMonth, spouseIssueDate, representativeId, customerCodeFromTcb,
      startDateEb, cusBranchCd, saleId, apType, etlCurDate, etlRunDateTime);
  }

  @Step("insert or update data")
  public boolean saveOrUpdateCustomer(com.tcbs.automation.tcbsdbapi1.StgTcbCustomer customer) {
    try {
      Session session = DbApi1.dbApiDbConnection.getSession();
      session.beginTransaction();
      session.save(customer);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return false;
    }
    return true;
  }


  @Step("delete data by key")
  public void deleteByCustomerId(Integer customerId) {
    Session session = DbApi1.dbApiDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTcbCustomer> query = session.createQuery(
      "DELETE FROM StgTcbCustomer i WHERE i.customerId=:customerId"
    );
    query.setParameter("customerId", customerId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public List<StgTcbCustomer> findByTcbsId(List<String> listTcbsId) {
    if (CollectionUtils.isEmpty(listTcbsId)) {
      return new ArrayList<>();
    }
    Query<StgTcbCustomer> query = DbApi1.dbApiDbConnection.getSession().createQuery(
      "from StgTcbCustomer as cus "
        + " where cus.tcbsid in :listTcbsId "
        + " AND cus.etlCurDate = (SELECT MAX(b.etlCurDate) FROM StgTcbCustomer as b) "
      , StgTcbCustomer.class
    );
    query.setParameter("listTcbsId", listTcbsId);
    List<StgTcbCustomer> results = query.getResultList();
    DbApi1.dbApiDbConnection.closeSession();
    return results;
  }
}
