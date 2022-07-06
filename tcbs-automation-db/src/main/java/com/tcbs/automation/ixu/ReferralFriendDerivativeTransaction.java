package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "REFERRAL_FRIEND_DERIVATIVE_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferralFriendDerivativeTransaction {

  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "TOTAL_FEE")
  private String totalFee;
  @Column(name = "TOTAL_AMOUNT")
  private String totalAmount;
  @Column(name = "MAX_AMOUNT")
  private String maxAmount;
  @Column(name = "POINT")
  private String point;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED")
  private Timestamp lastUpdated;

  public ReferralFriendDerivativeTransaction(ReferralFriendDerivativeTransaction that) {
    this.tcbsId = that.tcbsId;
    this.issuedDate = that.issuedDate;
    this.point = that.point;
    this.code105C = that.code105C;
    this.amount = that.amount;
    this.totalFee = that.totalFee;
    this.totalAmount = that.totalAmount;
    this.maxAmount = that.maxAmount;
    this.rawData = that.rawData;
    this.tradingDate = that.tradingDate;
    this.createdDate = that.createdDate;
    this.lastUpdated = that.lastUpdated;
  }

  @Step
  public static ReferralFriendDerivativeTransaction insert(ReferralFriendDerivativeTransaction entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete ReferralFriendDerivativeTransaction a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(List<String> tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete ReferralFriendDerivativeTransaction a where a.tcbsId in :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ReferralFriendDerivativeTransaction> findAllByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<ReferralFriendDerivativeTransaction> query = ixuDbConnection.getSession().createQuery("" +
      "from ReferralFriendDerivativeTransaction a where a.tcbsId = :tcbsId order " +
      "by a.createdDate "
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<ReferralFriendDerivativeTransaction> referralFriendDerivativeTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return referralFriendDerivativeTransactionList;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ReferralFriendDerivativeTransaction"
    );
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


}
