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
@Table(name = "HOLDING_FUND_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HoldingFundTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "COGS_VALUE")
  private Double cogsValue;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "NOTIFY_MESSAGE")
  private String notifyMessage;
  @Column(name = "CUSTOMER_TYPE")
  private String customerType;
  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingFundTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingFundTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListOrderId(List<String> orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingFundTransaction s where s.orderId in :orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createHoldingFundTransaction(HoldingFundTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<HoldingFundTransaction> findHoldingFundTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<HoldingFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingFundTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<HoldingFundTransaction> holdingFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingFundTransactionList;
  }

  @Step
  public static List<HoldingFundTransaction> findHoldingFundTransactionByTcbsIdOrderIdAndCustomerType(String tcbsId, String customerType, String orderId, String transactionType) {
    ixuDbConnection.getSession().clear();
    Query<HoldingFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingFundTransaction a where a.tcbsId=:tcbsId and a.customerType=:customerType and a.orderId=:orderId and a.transactionType=:transactionType");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("customerType", customerType);
    query.setParameter("orderId", orderId);
    query.setParameter("transactionType", transactionType);
    List<HoldingFundTransaction> holdingFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingFundTransactionList;
  }

  @Step
  public static List<HoldingFundTransaction> findHoldingFundTransactionByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<HoldingFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingFundTransaction a where a.tcbsId in :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<HoldingFundTransaction> holdingFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingFundTransactionList;
  }

  @Step
  public static List<HoldingFundTransaction> findHoldingFundTransactionByListOrderId(List<String> orderId) {
    ixuDbConnection.getSession().clear();
    Query<HoldingFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingFundTransaction a where a.orderId in :orderId ");
    query.setParameter("orderId", orderId);
    List<HoldingFundTransaction> holdingFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingFundTransactionList;
  }
}
