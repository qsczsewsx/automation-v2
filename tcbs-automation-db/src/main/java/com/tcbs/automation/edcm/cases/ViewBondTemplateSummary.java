package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "VIEW_BOND_TEMPLATE_SUMMARY")
public class ViewBondTemplateSummary implements Comparator<ViewBondTemplateSummary> {
  @Id
  @NotNull
  @Column(name = "BOND_TEMP_ID")
  private String bondTempId;
  @Column(name = "BOND_TEMP_CODE")
  private String bondTempCode;
  @Column(name = "CASE_ID")
  private String caseId;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "IS_ACTIVE")
  private String isActive;
  @Column(name = "IS_ACTIVE_CASE")
  private String isActiveCase;
  @Column(name = "TENOR_TIME")
  private String tenorTime;
  @Column(name = "COUPON")
  private String coupon;
  @Column(name = "LISTING")
  private String listing;
  @Column(name = "UOPS")
  private String uops;
  @Column(name = "SECURED_ASSET")
  private String securedAsset;


  public String getBondTempId() {
    return bondTempId;
  }

  public void setBondTempId(String bondTempId) {
    this.bondTempId = bondTempId;
  }


  public String getBondTempCode() {
    return bondTempCode;
  }

  public void setBondTempCode(String bondTempCode) {
    this.bondTempCode = bondTempCode;
  }


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }


  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public String getIsActiveCase() {
    return isActiveCase;
  }

  public void setIsActiveCase(String isActiveCase) {
    this.isActiveCase = isActiveCase;
  }


  public String getTenorTime() {
    return tenorTime;
  }

  public void setTenorTime(String tenorTime) {
    this.tenorTime = tenorTime;
  }


  public String getCoupon() {
    return coupon;
  }

  public void setCoupon(String coupon) {
    this.coupon = coupon;
  }


  public String getListing() {
    return listing;
  }

  public void setListing(String listing) {
    this.listing = listing;
  }


  public String getUops() {
    return uops;
  }

  public void setUops(String uops) {
    this.uops = uops;
  }


  public String getSecuredAsset() {
    return securedAsset;
  }

  public void setSecuredAsset(String securedAsset) {
    this.securedAsset = securedAsset;
  }


  @Step
  public List<ViewBondTemplateSummary> getData(String caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<ViewBondTemplateSummary> query = EdcmConnection.connection.getSession().createQuery(
      "from ViewBondTemplateSummary a WHERE a.caseId=:caseId", ViewBondTemplateSummary.class);
    query.setParameter("caseId", caseId);
    List<ViewBondTemplateSummary> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }

  }

  @Override
  public int compare(ViewBondTemplateSummary o1, ViewBondTemplateSummary o2) {
    return Integer.parseInt(o1.getBondTempId()) - Integer.parseInt(o2.getBondTempId());
  }
}
