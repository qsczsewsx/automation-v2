package com.tcbs.automation.bondlifecycle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOND_BASE_INFO")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class BondBaseInfo extends BaseEntity {

  @Id
  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "BOND_NAME")
  private String bondName;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Column(name = "MATURITY_DATE")
  private Date maturityDate;

  @Column(name = "LISTED_CODE")
  private String listedCode;

  @Column(name = "COUPON_FREQ")
  private String couponFreq;

  @Column(name = "BOND_STATUS_TYPE")
  private Integer bondStatusType;

  @Column(name = "MAKER")
  private String maker;

  @Column(name = "CHECKER")
  private String checker;

  @Column(name = "STATUS")
  private String status;

  @Transient
  private String issueDateStr;

  @Transient
  private String maturityDateStr;

  public BondBaseInfo(String bondId) {
    if (bondId != null) {
      this.bondId = Integer.valueOf(bondId);
    }
  }

  @Step
  public static void insert(BondBaseInfo bondBaseInfo) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondBaseInfo);
    trans.commit();
  }

  @Step
  public static void deleteByIds(List<Integer> ids) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO  where BOND_ID in :ids");
    query.setParameter("ids", ids);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByCodes(List<String> codes) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO  where BOND_CODE in :codes");
    query.setParameter("codes", codes);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO where BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByCode(String bondCode) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO where BOND_CODE = :bondCode");
    query.setParameter("bondCode", bondCode);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static BondBaseInfo getById(Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery("select * from BOND_BASE_INFO where BOND_ID = :bondId", BondBaseInfo.class);
    query.setParameter("bondId", bondId);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static BondBaseInfo getByCode(String bondCode) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery("select * from BOND_BASE_INFO where BOND_CODE = :bondCode", BondBaseInfo.class);
    query.setParameter("bondCode", bondCode);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondBaseInfo> getByCodes(List<String> bondCodes) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createQuery("select bbi from BondBaseInfo bbi where bondCode in :bondCodes", BondBaseInfo.class);
    query.setParameter("bondCodes", bondCodes);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static List<BondBaseInfo> findAllByGroupId(Integer groupId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery("SELECT bbi.* " +
      "FROM BOND_BASE_INFO bbi, BOND_TIMELINE_GROUP_MAPPING btgm, BOND_TIMELINE_GROUP btg  " +
      "WHERE btg.GROUP_ID = :groupId " +
      "and bbi.bond_id = btgm.bond_id " +
      "and btgm.BOND_TIMELINE_GROUP_ID = btg.GROUP_ID", BondBaseInfo.class);
    query.setParameter("groupId", groupId);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static List<BondBaseInfo> getByIds(List<Integer> ids) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createQuery("select bbi from BondBaseInfo bbi where bondId in :ids", BondBaseInfo.class);
    query.setParameter("ids", ids);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static List<BondBaseInfo> getUsedBondBaseInfos() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery(
      "select * from bond_base_info b where bond_id in (select bond_id from bond_timeline_group_mapping btgm, bond_timeline_group btg where btgm.bond_timeline_group_id = btg.group_id and btg.status <> 'INACTIVE')",
      BondBaseInfo.class);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static List<BondBaseInfo> findBondByKey(String findKey) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondBaseInfo> query = session.createNativeQuery(
      "SELECT * FROM BOND_BASE_INFO WHERE UPPER(NVL(bond_Code, '')) LIKE '%' || :findKey  || '%' AND status <> 'INACTIVE' ORDER BY bond_Id",
      BondBaseInfo.class);
    query.setParameter("findKey", findKey);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  public Integer getBondId() {
    return bondId;
  }

  public void setBondId(Integer bondId) {
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

  public String getListedCode() {
    return listedCode;
  }

  public void setListedCode(String listedCode) {
    this.listedCode = listedCode;
  }

  public String getCouponFreq() {
    return couponFreq;
  }

  public void setCouponFreq(String couponFreq) {
    this.couponFreq = couponFreq;
  }

  public Integer getBondStatusType() {
    return bondStatusType;
  }

  public void setBondStatusType(Integer bondStatusType) {
    this.bondStatusType = bondStatusType;
  }

  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  public String getChecker() {
    return checker;
  }

  public void setChecker(String checker) {
    this.checker = checker;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getIssueDateStr() {
    if (issueDateStr == null && this.issueDate != null) {
      this.issueDateStr = DateFormatUtils.format(this.issueDate, "yyyy-MM-dd");
    }

    return issueDateStr;
  }

  public void setIssueDateStr(String issueDateStr) {
    this.issueDateStr = issueDateStr;
  }

  public String getMaturityDateStr() {
    if (maturityDateStr == null && this.maturityDate != null) {
      this.maturityDateStr = DateFormatUtils.format(this.maturityDate, "yyyy-MM-dd");
    }
    return maturityDateStr;
  }

  public void setMaturityDateStr(String maturityDateStr) {
    this.maturityDateStr = maturityDateStr;
  }
}
