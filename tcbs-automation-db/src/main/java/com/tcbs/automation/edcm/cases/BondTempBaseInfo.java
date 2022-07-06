package com.tcbs.automation.edcm.cases;

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
@Table(name = "BOND_TEMP_BASE_INFO")
public class BondTempBaseInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "BOND_TEMP_ID")
  private String bondTempId;
  @Column(name = "CASE_ID")
  private String caseId;
  @Column(name = "BOND_TEMP_NAME")
  private String bondTempName;
  @Column(name = "BOND_TEMP_CODE")
  private String bondTempCode;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "TENOR_YEAR")
  private String tenorYear;
  @Column(name = "TENOR_MONTH")
  private String tenorMonth;
  @Column(name = "TENOR_DAY")
  private String tenorDay;
  @Column(name = "LISTING_ID")
  private String listingId;
  @Column(name = "IS_ACTIVE")
  private String isActive;
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
  public List<BondTempBaseInfo> getListData() {
    EdcmConnection.connection.getSession().clear();
    Query<BondTempBaseInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "select btbi.* from BOND_TEMP_BASE_INFO btbi left join BOND_TO_SELL bts on BTS.BOND_ID = btbi.BOND_TEMP_ID where BTS.IS_EXPOSE = 1 ", BondTempBaseInfo.class);
    return query.getResultList();
  }


  public String getBondTempId() {
    return bondTempId;
  }

  public void setBondTempId(String bondTempId) {
    this.bondTempId = bondTempId;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getBondTempName() {
    return bondTempName;
  }

  public void setBondTempName(String bondTempName) {
    this.bondTempName = bondTempName;
  }


  public String getBondTempCode() {
    return bondTempCode;
  }

  public void setBondTempCode(String bondTempCode) {
    this.bondTempCode = bondTempCode;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }


  public String getTenorYear() {
    return tenorYear;
  }

  public void setTenorYear(String tenorYear) {
    this.tenorYear = tenorYear;
  }


  public String getTenorMonth() {
    return tenorMonth;
  }

  public void setTenorMonth(String tenorMonth) {
    this.tenorMonth = tenorMonth;
  }


  public String getTenorDay() {
    return tenorDay;
  }

  public void setTenorDay(String tenorDay) {
    this.tenorDay = tenorDay;
  }


  public String getListingId() {
    return listingId;
  }

  public void setListingId(String listingId) {
    this.listingId = listingId;
  }


  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
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


  @Step
  public BondTempBaseInfo getData(String bondTempId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondTempBaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "from BondTempBaseInfo a WHERE a.bondTempId=:bondTempId", BondTempBaseInfo.class);
    query.setParameter("bondTempId", bondTempId);

    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new BondTempBaseInfo();
    }

  }

}
