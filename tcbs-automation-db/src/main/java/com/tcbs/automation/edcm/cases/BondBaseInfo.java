package com.tcbs.automation.edcm.cases;

import com.tcbs.automation.edcm.EdcmConnection;
import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "BOND_BASE_INFO")
public class BondBaseInfo implements Comparator<BondBaseInfo> {
  @Id
  @NotNull
  @Column(name = "BOND_ID")
  private String bondId;
  @Column(name = "BOND_NAME")
  private String bondName;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "ISSUE_DATE")
  private Date issueDate;
  @Column(name = "MATURITY_DATE")
  private Date maturityDate;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "UPDATED_BY")
  private String updatedBy;
  @Column(name = "IS_ACTIVE")
  private String isActive;
  @Column(name = "ISSUE_EXPECTED_DATE")
  private Timestamp issueExpectedDate;
  @Column(name = "ORDER_BOND")
  private String orderBond;
  @Column(name = "BOND_TEMP_ID")
  private String bondTempId;
  @Column(name = "HNX_CODE")
  private String hnxCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "IS_CLOSE_OFFERING")
  private String isCloseOffering;
  @Column(name = "MAXIMUM_AMOUNT")
  private String maximumAmount;
  @Column(name = "PRE_STATE")
  private String preState;
  @Column(name = "IS_ACTIVATE_LISTING")
  private String isActivateListing;
  @Column(name = "PRE_STATE_CASE")
  private String preStateCase;
  @Column(name = "TRADING_STATUS")
  private String tradingStatus;
  @Column(name = "IS_MIGRATE")
  private String isMigrate;

  @Step
  public static List<BondBaseInfo> getListBond(String caseId) {
    Session session = EdcmConnection.connection.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery(
      "select ca.case_id , bond.* from bond_base_info bond" +
        " join bond_temp_base_info tmp on bond.bond_temp_id = tmp.bond_temp_id" +
        " join case_info ca on ca.case_id = tmp.case_id" +
        " where ca.case_id = :caseId and bond.is_active =1", BondBaseInfo.class);
    query.setParameter("caseId", caseId);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }
    return results;
  }

  public String getBondId() {
    return bondId;
  }

  public void setBondId(String bondId) {
    this.bondId = bondId;
  }

  public String getBondName() {
    return bondName;
  }

  public void setBondName(String bondName) {
    this.bondName = bondName;
  }

  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  public Date getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(Date issueDate) {
    this.issueDate = issueDate;
  }

  public Date getMaturityDate() {
    return maturityDate;
  }

  public void setMaturityDate(Date maturityDate) {
    this.maturityDate = maturityDate;
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

  public String getIsActive() {
    return isActive;
  }

  public void setIsActive(String isActive) {
    this.isActive = isActive;
  }

  public String getIssueExpectedDate() {
    return issueExpectedDate == null ? null : PublicConstant.dateTimeFormat.format(issueExpectedDate);
  }

  public void setIssueExpectedDate(String issueExpectedDate) throws ParseException {
    this.issueExpectedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(issueExpectedDate).getTime());
  }

  public String getOrderBond() {
    return orderBond;
  }

  public void setOrderBond(String orderBond) {
    this.orderBond = orderBond;
  }

  public String getBondTempId() {
    return bondTempId;
  }

  public void setBondTempId(String bondTempId) {
    this.bondTempId = bondTempId;
  }

  public String getHnxCode() {
    return hnxCode;
  }

  public void setHnxCode(String hnxCode) {
    this.hnxCode = hnxCode;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIsCloseOffering() {
    return isCloseOffering;
  }

  public void setIsCloseOffering(String isCloseOffering) {
    this.isCloseOffering = isCloseOffering;
  }

  public String getMaximumAmount() {
    return maximumAmount;
  }

  public void setMaximumAmount(String maximumAmount) {
    this.maximumAmount = maximumAmount;
  }

  public String getPreState() {
    return preState;
  }

  public void setPreState(String preState) {
    this.preState = preState;
  }

  public String getIsActivateListing() {
    return isActivateListing;
  }

  public void setIsActivateListing(String isActivateListing) {
    this.isActivateListing = isActivateListing;
  }

  public String getPreStateCase() {
    return preStateCase;
  }

  public void setPreStateCase(String preStateCase) {
    this.preStateCase = preStateCase;
  }

  public String getTradingStatus() {
    return tradingStatus;
  }

  public void setTradingStatus(String tradingStatus) {
    this.tradingStatus = tradingStatus;
  }

  public String getIsMigrate() {
    return isMigrate;
  }

  public void setIsMigrate(String isMigrate) {
    this.isMigrate = isMigrate;
  }

  @Step
  public List<BondBaseInfo> getListData(String bondTempId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondBaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "from BondBaseInfo a WHERE a.bondTempId =:bondTempId", BondBaseInfo.class);
    query.setParameter("bondTempId", bondTempId);
    return query.getResultList();
  }

  @Step
  public BondBaseInfo getBondData(String bondId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondBaseInfo> query = EdcmConnection.connection.getSession().createQuery(
      "from BondBaseInfo a WHERE a.bondId =:bondId", BondBaseInfo.class);
    query.setParameter("bondId", bondId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new BondBaseInfo();
    }
  }

  @Step
  public List<BondBaseInfo> getListDataByCaseId(Long caseId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondBaseInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT bbi.* FROM BOND_BASE_INFO bbi  " +
        "LEFT JOIN BOND_LISTING_INFO bli ON bli.BOND_ID = bbi.BOND_ID " +
        "INNER JOIN BOND_TEMP_BASE_INFO btbi ON btbi.BOND_TEMP_ID = bbi.BOND_TEMP_ID " +
        "INNER JOIN CASE_INFO ci ON ci.CASE_ID = btbi.CASE_ID  " +
        "WHERE ci.CASE_ID = :caseId AND bbi.STATUS = 349", BondBaseInfo.class);
    query.setParameter("caseId", caseId);
    return query.getResultList();
  }

  @Step
  public List<BondBaseInfo> getListDataByBondTempId(Long bondTempId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondBaseInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT * FROM BOND_BASE_INFO a  " +
        "LEFT JOIN BOND_LISTING_INFO bli ON bli.BOND_ID = a.BOND_ID " +
        "WHERE a.BOND_TEMP_ID = :bondTempId AND a.STATUS = 349 ", BondBaseInfo.class);
    query.setParameter("bondTempId", bondTempId);
    return query.getResultList();
  }

  @Step
  public BondBaseInfo getListDataByBondId(Integer bondId) {
    EdcmConnection.connection.getSession().clear();
    Query<BondBaseInfo> query = EdcmConnection.connection.getSession().createNativeQuery(
      "SELECT * FROM BOND_BASE_INFO a WHERE BOND_ID =:bondId   ", BondBaseInfo.class);
    query.setParameter("bondId", bondId);
    return query.getSingleResult();
  }

  @Override
  public int compare(BondBaseInfo o1, BondBaseInfo o2) {
    return Integer.parseInt(o1.getBondId()) - Integer.parseInt(o2.getBondId());
  }

}
