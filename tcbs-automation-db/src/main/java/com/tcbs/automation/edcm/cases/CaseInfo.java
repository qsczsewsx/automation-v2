package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "CASE_INFO")
public class CaseInfo {
  @Id
  @NotNull
  @Column(name = "CASE_ID")
  private String caseId;
  @NotNull
  @Column(name = "CASE_NAME")
  private String caseName;
  @NotNull
  @Column(name = "ISSUER_ID")
  private String issuerId;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "STATUS_ID")
  private String statusId;
  @Column(name = "IS_ACTIVE")
  private String isActive;
  @Column(name = "QUANTITY")
  private String quantity;
  @Column(name = "PAR_VALUE")
  private String parValue;
  @Column(name = "CURRENCY_TYPE_ID")
  private String currencyTypeId;
  @Column(name = "ISSUANCE_FORM_ID")
  private String issuanceFormId;
  @Column(name = "ISSUANCE_METHOD_ID")
  private String issuanceMethodId;
  @Column(name = "SENIORITY_ID")
  private String seniorityId;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "IS_MIGRATE")
  private String isMigrate;
  @Column(name = "IS_EXPOSE_TO_FS")
  private String isExposeToFs;
  @Column(name = "PRE_STATE_EXPOSE_FS")
  private String preStateExposeFs;


  public String getCaseId() {
    return caseId;
  }

  public void setCaseId(String caseId) {
    this.caseId = caseId;
  }


  public String getCaseName() {
    return caseName;
  }

  public void setCaseName(String caseName) {
    this.caseName = caseName;
  }


  public String getIssuerId() {
    return issuerId;
  }

  public void setIssuerId(String issuerId) {
    this.issuerId = issuerId;
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


  public String getStatusId() {
    return statusId;
  }

  public void setStatusId(String statusId) {
    this.statusId = statusId;
  }


  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }


  public String getQuantity() {
    return quantity;
  }

  public void setQuantity(String quantity) {
    this.quantity = quantity;
  }


  public String getParValue() {
    return parValue;
  }

  public void setParValue(String parValue) {
    this.parValue = parValue;
  }


  public String getCurrencyTypeId() {
    return currencyTypeId;
  }

  public void setCurrencyTypeId(String currencyTypeId) {
    this.currencyTypeId = currencyTypeId;
  }


  public String getIssuanceFormId() {
    return issuanceFormId;
  }

  public void setIssuanceFormId(String issuanceFormId) {
    this.issuanceFormId = issuanceFormId;
  }


  public String getIssuanceMethodId() {
    return issuanceMethodId;
  }

  public void setIssuanceMethodId(String issuanceMethodId) {
    this.issuanceMethodId = issuanceMethodId;
  }


  public String getSeniorityId() {
    return seniorityId;
  }

  public void setSeniorityId(String seniorityId) {
    this.seniorityId = seniorityId;
  }


  public String getAmount() {
    return amount;
  }

  public void setAmount(String amount) {
    this.amount = amount;
  }


  public String getIsMigrate() {
    return isMigrate;
  }

  public void setIsMigrate(String isMigrate) {
    this.isMigrate = isMigrate;
  }


  public String getIsExposeToFs() {
    return isExposeToFs;
  }

  public void setIsExposeToFs(String isExposeToFs) {
    this.isExposeToFs = isExposeToFs;
  }


  public String getPreStateExposeFs() {
    return preStateExposeFs;
  }

  public void setPreStateExposeFs(String preStateExposeFs) {
    this.preStateExposeFs = preStateExposeFs;
  }

  @Step
  public CaseInfo getDataById(String caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "from CaseInfo a WHERE a.caseId= :caseId", CaseInfo.class);
    query.setParameter("caseId", caseId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new CaseInfo();
    }
  }

  @Step
  public List<CaseInfo> getListData(int page, int size, String dealSupervisor, String dealOwner, String statusId) {
    EdcmConnection.connection.getSession().clear();
    Query<CaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "select a from CaseInfo a inner join CaseTeamMember t on a.caseId = t.caseId WHERE t.dealSupervisor=:dealSupervisor AND t.dealOwner=:dealOwner AND a.statusId=:statusId ", CaseInfo.class);
    query.setParameter("dealSupervisor", dealSupervisor);
    query.setParameter("dealOwner", dealOwner);
    query.setParameter("statusId", statusId);
    query.setFirstResult((page - 1) * size);
    query.setMaxResults(size);
    List<CaseInfo> result = query.list();
    return result;
  }

  @Step
  public List<CaseInfo> getListData() {
    EdcmConnection.connection.getSession().clear();
    Query<CaseInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT distinct ci.CASE_ID  ,ci.* FROM CASE_INFO ci  left join BOND_TO_SELL bts on BTS.CASE_ID = ci.CASE_ID where bts.IS_EXPOSE = 1  ", CaseInfo.class);
    return query.getResultList();
  }
}