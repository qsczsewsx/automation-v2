package com.tcbs.automation.tcinvest;

import com.tcbs.automation.tcinvest.ifund.entity.InvOrder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.config.coman.ComanKey.BOND_STATIC_ID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "INV_BOND_STATIC")
public class InvBondStatic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOND_STATIC_ID")
  private Integer bondStaticId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "CODE")
  private String code;

  @Column(name = "PAR")
  private Integer par;

  @Column(name = "CURRENCY")
  private String currency;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Column(name = "EXPIRED_DATE")
  private Date expiredDate;

  @Column(name = "NOTE_PAYMENT_INTEREST")
  private String notePaymentInterest;

  @Column(name = "NOTE_TYPE")
  private String noteType;

  @Column(name = "NOTE_INTEREST")
  private String noteInterest;

  @Column(name = "BASE_INTEREST_TOP")
  private String baseInterestTop;

  @Column(name = "BASE_INTEREST_BOTTOM")
  private String baseInterestBottom;

  @Column(name = "BOND_ISSUER_ID")
  private Integer bondIssuerId;

  @Column(name = "SERIES_CONTRACT_CODE")
  private String seriesContractCode;

  @Column(name = "SERIES_BOND_CODE")
  private String seriesBondCode;

  @Column(name = "IS_COUPON")
  private Integer isCoupon;

  @Column(name = "COUPON_PAYMENT_END_TERM")
  private Integer couponPaymentEndTerm;

  @Column(name = "COUPON_RATE")
  private Integer couponRate;

  @Column(name = "COUPON_FREQ")
  private String couponFreq;

  @Column(name = "COUPON_DATE_PAYMENT")
  private Date couponDatePayment;

  @Column(name = "TRUST_ASSET")
  private String trustAsset;

  @Column(name = "TRUST_ASSET_VALUE")
  private String trustAssetValue;

  @Column(name = "GUARANTEE")
  private String guarantee;

  @Column(name = "GUARANTEE_NAME")
  private String guaranteeName;

  @Column(name = "FIRST_DATE_PERIOD_PL")
  private Date firstDatePeriodPl;

  @Column(name = "LAST_DATE_PERIOD_PL")
  private Date lastDatePeriodPl;

  @Column(name = "FLOAT_BAND_INTEREST")
  private Float floatBandInterest;

  @Column(name = "REFERENCE_RATE_COUPON")
  private Float referenceRateCoupon;

  @Column(name = "BUILD_BOOK")
  private Integer buildBook;

  @Column(name = "LISTED_STATUS")
  private Integer listedStatus;

  @Column(name = "ACTIVE")
  private Integer active;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;

  @Column(name = "LISTED_CODE")
  private String listedCode;

  @Column(name = "FROZEN_STATUS")
  private Integer frozenStatus;

  @Column(name = "PAYMENT_TERM")
  private String paymentTerm;

  @Column(name = "HNX_CODE")
  private String hnxCode;

  @Column(name = "PO_PP")
  private String poPp;

  @Column(name = "TRUST_ASSET_MANAGER_NAME")
  private String trustAssetManagerName;

  @Column(name = "TERM_OF_REDEMPTION")
  private String termOfRedemption;

  @Column(name = "CLOSE_REGISTER_VSD_DATE")
  private Date closeRegisterVSDDate;

  @Column(name = "REGISTER_VSD_DATE")
  private Date registerVSDDate;

  @Column(name = "LISTED_DATE")
  private Date listedDate;

  @Column(name = "START_TRADING_DATE")
  private Date startTradingDate;

  @Column(name = "ISSUER_TCBSID")
  private String issuerTcbsId;

  @Column(name = "DELISTING_DATE")
  private Date delistingDate;

  @Column(name = "EXPIRED_DATE_OC")
  private Date expiredDateOC;

  @Column(name = "CLOSE_DEPOSITORY_DATE")
  private Date closeDepositoryDate;

  public static void deleteByCodes(List<String> codes) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM InvBondStatic ib WHERE ib.code in :codes"
    );
    query.setParameter("codes", codes);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM InvBondStatic ib WHERE ib.code = :code"
    );
    query.setParameter("code", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteById(Integer id) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM InvBondStatic ib WHERE ib.bondStaticId = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByIds(List<Integer> ids) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM InvBondStatic ib WHERE ib.bondStaticId in :ids"
    );
    query.setParameter("ids", ids);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void insert(InvBondStatic invBondStatic) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(invBondStatic);
    trans.commit();
  }

  @Step
  public static List<InvBondStatic> getListBondStatic(Integer bondStaticId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic a where a.bondStaticId=:bondStaticId", InvBondStatic.class);
    query.setParameter(BOND_STATIC_ID, bondStaticId);

    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticByBondIssuerId(Integer bondIssuerId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic a where a.bondIssuerId=:bondIssuerId", InvBondStatic.class);
    query.setParameter("bondIssuerId", bondIssuerId);

    return query.getResultList();
  }

  public static InvBondStatic getByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic where code =: code");
    query.setParameter("code", code);
    List<InvBondStatic> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void updateExpiredDateByCode(String code, String expiredDate) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery("UPDATE InvBondStatic SET expiredDate = to_date(:expiredDate, 'yyyy-MM-dd')  WHERE code =: code");
    query.setParameter("code", code);
    query.setParameter("expiredDate", expiredDate);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static List<InvBondStatic> getListBondStatic() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic where 1=1", InvBondStatic.class);

    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticWithListedCode(String listedCode) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic where listedCode =: listedCode", InvBondStatic.class);
    query.setParameter("listedCode", listedCode);
    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticWithIssuerTcbsId(String issuerTcbsId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondStatic> query = session.createQuery("from InvBondStatic where issuerTcbsId =: issuerTcbsId", InvBondStatic.class);
    query.setParameter("issuerTcbsId", issuerTcbsId);
    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticWithIssueDate(String issueDate) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder();
    querySql.append(String.format("SELECT * FROM INV_BOND_STATIC WHERE ISSUE_DATE = to_date('%s','YYYY-MM-DD')", issueDate));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticWithExpiredDate(String expiredDate) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder();
    querySql.append(String.format("SELECT * FROM INV_BOND_STATIC WHERE EXPIRED_DATE = to_date('%s','YYYY-MM-DD')", expiredDate));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  @Step
  public static List<InvBondStatic> getListBondStaticWithExpiredDateOc(String expiredDateOc) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder();
    querySql.append(String.format("SELECT * FROM INV_BOND_STATIC WHERE EXPIRED_DATE_OC = to_date('%s','YYYY-MM-DD')", expiredDateOc));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  public static void deleteIssuerId() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "UPDATE InvBondStatic set issuerTcbsId = null where 1 = 1"
    );
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteCouponFreq() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "UPDATE InvBondStatic set couponFreq = null where 1 = 1"
    );
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateListedStatusOtc(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery("UPDATE InvBondStatic SET listedStatus = 0 WHERE code =: code");
    query.setParameter("code", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateListedStatusListed(String code, Date closeRegisterVSDDate) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery("UPDATE InvBondStatic SET listedStatus = 1,closeRegisterVSDDate =:closeRegisterVSDDate  WHERE code =: code");
    query.setParameter("code", code);
    query.setParameter("closeRegisterVSDDate", closeRegisterVSDDate);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
