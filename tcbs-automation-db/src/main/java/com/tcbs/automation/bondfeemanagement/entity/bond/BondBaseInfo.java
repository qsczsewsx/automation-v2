package com.tcbs.automation.bondfeemanagement.entity.bond;

import com.tcbs.automation.bondfeemanagement.BaseEntity;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOND_BASE_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondBaseInfo extends BaseEntity {

  @Id
  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "BOND_NAME")
  private String bondName;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "TOTAL_VOLUME")
  private Integer totalVolume;

  @Column(name = "ISSUER_ID")
  private Integer issuerId;

  @Column(name = "ISSUER_NAME")
  private String issuerName;

  @Column(name = "ISSUER_NAME_NO_ACCENT")
  private String issuerNameNoAccent;

  @Column(name = "ISSUE_DATE")
  private Date issueDateDB;

  @Column(name = "MATURITY_DATE")
  private Date maturityDateDB;

  @Column(name = "CLOSED_DATE")
  private Date closedDateDB;

  @Column(name = "DISCLOSED_OC_PP_DATE")
  private Date disclosedOCPPDateDB;

  @Column(name = "DISCLOSED_OC_PO_DATE")
  private Date disclosedOCPODateDB;

  @Column(name = "ACTUAL_OFFICIAL_LISTING_APPROVAL_DATE")
  private Date actualOfficialListingApprovalDateDB;

  @Column(name = "ACTUAL_FIRST_TRADING_DATE")
  private Date actualFirstTradingDateDB;

  //Ngày nộp hồ sơ đăng ký niêm yết
  @Column(name = "EXCHANGE_SUBMISSION_DATE")
  private Date exchangeSubmissionDateDB;

  @Column(name = "STATUS")
  private String status;

  public BondBaseInfo(String bondId) {
    if (bondId != null) {
      this.bondId = Integer.valueOf(bondId);
    }
  }

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table BOND_BASE_INFO");
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void insert(BondBaseInfo bondBaseInfo) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondBaseInfo);
    trans.commit();
  }

  @Step
  public static void deleteByIds(List<Integer> ids) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO  where BOND_ID in :ids");
    query.setParameter("ids", ids);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByCodes(List<String> codes) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO  where BOND_CODE in :codes");
    query.setParameter("codes", codes);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteByGroupId(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO where bond_id in (select bond_id from BOND_GROUP_MAPPING where group_id = :groupId)");
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO where BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByCode(String bondCode) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_BASE_INFO where BOND_CODE = :bondCode");
    query.setParameter("bondCode", bondCode);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static BondBaseInfo getById(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createNativeQuery("select * from BOND_BASE_INFO where BOND_ID = :bondId", BondBaseInfo.class);
    query.setParameter("bondId", bondId);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static BondBaseInfo getByCode(String bondCode) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createNativeQuery("select * from BOND_BASE_INFO where BOND_CODE = :bondCode", BondBaseInfo.class);
    query.setParameter("bondCode", bondCode);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondBaseInfo> getByCodes(List<String> bondCodes) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createQuery("select bbi from BondBaseInfo bbi where bbi.bondCode in :bondCodes", BondBaseInfo.class);
    query.setParameter("bondCodes", bondCodes);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();

    }
    return results;
  }

  @Step
  public static List<BondBaseInfo> findAllByGroupId(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createNativeQuery("SELECT bbi.* " +
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
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createQuery("select bbi from BondBaseInfo bbi where bondId in :ids", BondBaseInfo.class);
    query.setParameter("ids", ids);
    List<BondBaseInfo> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static List<BondBaseInfo> getUsedBondBaseInfos() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondBaseInfo> query = session.createNativeQuery(
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
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
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
}
