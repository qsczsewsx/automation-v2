package com.tcbs.automation.tcbond;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Customer")
public class Customer {
  @Id
  @Column(name = "CustomerId")
  private Integer customerId;
  @Column(name = "CustomerName")
  private String customerName;
  @Column(name = "IdentityCard")
  private String identityCard;
  @Column(name = "IdentityBy")
  private String identityBy;
  @Column(name = "IdentityDate")
  private String identityDate;
  @Column(name = "Address")
  private String address;
  @Column(name = "Phone")
  private String phone;
  @Column(name = "Fax")
  private String fax;
  @Column(name = "Email")
  private String email;
  @Column(name = "BankCode")
  private String bankCode;
  @Column(name = "AgencyId")
  private Integer agencyId;
  @Column(name = "Representative")
  private String representative;
  @Column(name = "AuthorizationDocument")
  private String authorizationDocument;
  @Column(name = "CreatedDate")
  private String createdDate;
  @Column(name = "CustomerCode")
  private String customerCode;
  @Column(name = "Birthday")
  private String birthday;
  @Column(name = "Active")
  private Integer active;
  @Column(name = "GrouponId")
  private Long grouponId;
  @Column(name = "Title")
  private String title;
  @Column(name = "Password")
  private String password;
  @Column(name = "BranchCode")
  private String branchCode;
  @Column(name = "BranchName")
  private String branchName;
  @Column(name = "BankName")
  private String bankName;
  @Column(name = "BankCity")
  private String bankCity;
  @Column(name = "CustodyCode")
  private String custodyCode;
  @Column(name = "Gender")
  private Long gender;
  @Column(name = "AddressCity")
  private String addressCity;
  @Column(name = "Tcbsid")
  private String tcbsid;
  @Column(name = "LoanBankAccount")
  private String loanBankAccount;
  @Column(name = "LoanBankAgency")
  private String loanBankAgency;
  @Column(name = "CustomerIncomePerMonth")
  private Double customerIncomePerMonth;
  @Column(name = "EscrowAccount")
  private String escrowAccount;
  @Column(name = "RegistrationBookAddress")
  private String registrationBookAddress;
  @Column(name = "Spouse")
  private Long spouse;
  @Column(name = "SpouseName")
  private String spouseName;
  @Column(name = "SpouseIDNo")
  private String spouseIdNo;
  @Column(name = "SpouseIssueBy")
  private String spouseIssueBy;
  @Column(name = "SpouseIncomePerMonth")
  private Double spouseIncomePerMonth;
  @Column(name = "SpouseIssueDate")
  private String spouseIssueDate;
  @Column(name = "RepresentativeID")
  private Long representativeId;
  @Column(name = "CustomerCode_fromTCB")
  private String customerCodeFromTcb;
  @Column(name = "START_DATE_EB")
  private String startDateEb;
  @Column(name = "CUS_BRANCH_CD")
  private String cusBranchCd;
  @Column(name = "Sale_ID")
  private String saleId;
  @Column(name = "APType")
  private Long apType;
  @Column(name = "Optlock")
  private Integer optlock;
  @Column(name = "LockedStatus")
  private Integer lockedStatus;
  @Column(name = "Normalized_Status")
  private Integer normalizedStatus;

  @Step
  public String getTcbsIdFromCustomerId(String customerId) {
    Query<Customer> query = TcBond.tcBondDbConnection.getSession().createQuery("from Customer where customerId=:customerId", Customer.class);
    query.setParameter("customerId", Long.valueOf(customerId));
    List<Customer> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0).getTcbsid();
    } else {
      return null;
    }
  }

  @Step("Get customer info by tcbsId {0}")
  public Customer getCustomerFromTcbsId(String tcbsId) {
    Query<Customer> query = TcBond.tcBondDbConnection.getSession()
      .createQuery("from Customer where tcbsId=:tcbsId", Customer.class);
    query.setParameter("tcbsId", tcbsId);
    List<Customer> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}

















