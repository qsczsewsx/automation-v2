package com.tcbs.automation.edcm.bond;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "BOND_PRIMARY_DISTRIBUTION")
public class BondPrimaryDistribution {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "BOND_ID")
  private String bondId;
  @NotNull
  @Column(name = "INVESTORS_TYPE")
  private String investorsType;
  @NotNull
  @Column(name = "INVESTORS")
  private String investors;
  @Column(name = "INDENTURE_NUMBER")
  private String indentureNumber;
  @Column(name = "QUANTITY")
  private String quantity;
  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @NotNull
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "MANAGEMENT_REPRESENTATIVE_ID")
  private String managementRepresentativeId;
  @Column(name = "BOND_ORDER_PRICE")
  private String bondOrderPrice;
  @Column(name = "ACCOUNT_ID")
  private String accountId;

  @Step
  public List<BondPrimaryDistribution> getListDataByBondId(Integer bondId, String investorType) {
    EdcmConnection.connection.getSession().clear();
    Query<BondPrimaryDistribution> query = EdcmConnection.connection.getSession().createNativeQuery(
      "select * from BOND_PRIMARY_DISTRIBUTION b WHERE b.BOND_ID = :bondId and b.INVESTORS_TYPE = :investorType order by CREATED_AT ASC  ", BondPrimaryDistribution.class);
    query.setParameter("bondId", bondId);
    query.setParameter("investorType", investorType);
    return query.getResultList();
  }


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getBondId() {
    return bondId;
  }

  public void setBondId(String bondId) {
    this.bondId = bondId;
  }


  public String getInvestorsType() {
    return investorsType;
  }

  public void setInvestorsType(String investorsType) {
    this.investorsType = investorsType;
  }


  public String getInvestors() {
    return investors;
  }

  public void setInvestors(String investors) {
    this.investors = investors;
  }


  public String getIndentureNumber() {
    return indentureNumber;
  }

  public void setIndentureNumber(String indentureNumber) {
    this.indentureNumber = indentureNumber;
  }


  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }


  public Date getPaymentDate() {
    return paymentDate;
  }

  public void setPaymentDate(Date paymentDate) {
    this.paymentDate = paymentDate;
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


  public String getManagementRepresentativeId() {
    return managementRepresentativeId;
  }

  public void setManagementRepresentativeId(String managementRepresentativeId) {
    this.managementRepresentativeId = managementRepresentativeId;
  }


  public String getBondOrderPrice() {
    return bondOrderPrice;
  }

  public void setBondOrderPrice(String bondOrderPrice) {
    this.bondOrderPrice = bondOrderPrice;
  }


  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }

}
