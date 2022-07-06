package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
@Table(name = "INVEST_FUND_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvestFundTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "FUND_ORDER_ID")
  private Double fundOrderId;
  @Column(name = "CASH_ID")
  private Double cashId;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;
  @Column(name = "TRANSACTION_CODE")
  private String transactionCode;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "AMOUNT")
  private Double amount;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ORDER_STATUS")
  private String orderStatus;
  @Column(name = "CONFIG_ID")
  private String configId;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;


  @Step
  public static InvestFundTransaction insert(InvestFundTransaction entity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(List<String> tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from InvestFundTransaction where tcbsId in :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<InvestFundTransaction> findByUniqueCondition(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<InvestFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from InvestFundTransaction a where tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    List<InvestFundTransaction> investFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return investFundTransactionList;
  }

  public static void updateTcbsIdForRetry(String id, String retryTcbsId, String productCode) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update InvestFundTransaction set tcbsId = :tcbsId, productCode = :productCode where id = :id");
    query.setParameter(TCBS_ID, retryTcbsId);
    query.setParameter("id", id);
    query.setParameter("productCode", productCode);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByOrderId(Long id) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from InvestFundTransaction a where a.fundOrderId = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String id) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from InvestFundTransaction a where a.tcbsId = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<InvestFundTransaction> findRecordByOrderId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<InvestFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from InvestFundTransaction tb where tb.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<InvestFundTransaction> investFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return investFundTransactionList;
  }
}
