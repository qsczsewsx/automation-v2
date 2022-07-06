package com.tcbs.automation.tcinvest.ifund.entity;

import com.tcbs.automation.tcinvest.InvGlobalAttr;
import com.tcbs.automation.tcinvest.TcInvest;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_ORDER")
public class InvOrder {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "ACTION_ID")
  private String actionId;
  @Column(name = "PRODUCT_ID")
  private String productId;
  @Column(name = "VOLUME")
  private Double volume;
  @Column(name = "PRICE")
  private Double price;
  @Column(name = "TOTAL_VALUE")
  private Double totalValue;
  @Column(name = "TRANSACTION_VALUE")
  private Double transactionValue;
  @Column(name = "PURCHASER_ID")
  private String purchaserId;
  @Column(name = "CPTY_ID")
  private String cptyId;
  @Column(name = "AGREEMENT_ID")
  private String agreementId;
  @Column(name = "BOOK_ID")
  private String bookId;
  @Column(name = "ORDER_TIMESTAMP")
  private Date orderTimestamp;
  @Column(name = "VALUATION_TIMESTAMP")
  private Date valuationTimestamp;
  @Column(name = "WARNING_MESSAGE")
  private String warningMessage;
  @Column(name = "GROUP_ORDER_ID")
  private String groupOrderId;
  @Column(name = "CONDITION_ID")
  private String conditionId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ORDER_CODE")
  private String orderCode;
  @Column(name = "SUBMITTED_TIMESTAMP")
  private Date submitTimestamps;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "OPERATOR_ID")
  private String operatorId;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "CUSTYPE")
  private String cusType;
  @Column(name = "PLAN_CODE")
  private String planCode;
  @Column(name = "PLAN_INS_ID")
  private String planInsId;
  @Column(name = "PROGRAM_CODE")
  private String programCode;
  @Column(name = "SUB_MATCH_MECHANISM")
  private String subMatchMechanism;
  @Column(name = "TRANSACTION_VOLUME")
  private String transactionVolume;
  @Column(name = "UNIT_PRICE")
  private String unitPrice;
  @Column(name = "RATE")
  private String rate;
  @Column(name = "REMAIN_VOLUME")
  private String remainVolume;
  @Column(name = "PRINCIPAL")
  private String principal;
  @Column(name = "BASE_STATUS")
  private String baseStatus;
  @Column(name = "MARKET_TYPE")
  private String marketType;
  @Column(name = "TRADING_CHANNEL")
  private String tradingChanel;
  @Column(name = "TRADING_SOURCE")
  private String tradingSource;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
  @Column(name = "TRADING_START_TIMESTAMP")
  private Date tradingStartTimestamp;
  @Column(name = "TRADING_END_TIMESTAMP")
  private Date tradingEndTimestamp;
  @Column(name = "PAID_TIMESTAMP")
  private Date paidTimestamp;
  @Column(name = "MATCHED_TIMESTAMP")
  private Date matchedTimestamp;
  @Column(name = "TRANSFERRED_TIMESTAMP")
  private Date transferredTimestamp;

  public static List<InvOrder> getListOrderByAccountId(String accountId) {
    Query<InvOrder> query = session.createQuery("from InvOrder where productId in ('TCBF','TCEF','TCFF') and accountId=:accountId ORDER BY orderId DESC");
    query.setParameter("accountId", accountId);
    return query.getResultList();
  }

  public static List<InvOrder> getListOrderByPurchaserId(String purchaserId) {
    Query<InvOrder> query = session.createQuery("from InvOrder where productId in ('TCBF','TCEF','TCFF') and purchaserId=:purchaserId ORDER BY orderId DESC");
    query.setParameter("purchaserId", purchaserId);
    return query.getResultList();
  }

  public static void deleteByOrderId(String orderId) {
//    startTransaction();
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery("DELETE InvOrder WHERE orderId =: orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static InvOrder getOrderByOrderId(String orderId) {
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvOrder> query = session.createQuery("from InvOrder where orderId =: orderId");
    query.setParameter("orderId", orderId);
    List<InvOrder> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void startTransaction() {
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
  }

  public static void updateGroupOrderIdByOrderId(String orderId, String groupOrderId) {
    startTransaction();
    Query<InvGlobalAttr> query = session.createQuery("UPDATE InvOrder SET groupOrderId = : groupOrderId  WHERE orderId =: orderId");
    query.setParameter("groupOrderId", groupOrderId);
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public String insert() {
    startTransaction();
    session.save(this);
    session.getTransaction().commit();
    return session.save(this).toString();
  }

  public void dmlPrepareData(String queryString) {
    startTransaction();
    Query query = session.createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvOrder> getByProductId(String productId, String valuationTimestamp) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvOrder> query = session.createQuery("from InvOrder ib where ib.productId =: productId and ib.status = 'DONE' and ib.valuationTimestamp = to_date(:valuationTimestamp, 'yyyy-mm-dd') order by ib.orderId desc");
    query.setParameter("productId", productId);
    query.setParameter("valuationTimestamp", valuationTimestamp);
    List<InvOrder> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static List<InvOrder> getByProductIdCptyId(String productId, String cptyId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    String queryString = "from InvOrder ib where ib.productId =: productId and ib.cptyId = :cptyId order by ib.orderId desc";
    Query<InvOrder> query = session.createQuery(queryString);
    query.setParameter("productId", productId);
    query.setParameter("cptyId", cptyId);

    List<InvOrder> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static List<InvOrder> getByOrderCode(String orderCode) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    String queryString = "from InvOrder ib where ib.orderCode =: orderCode order by ib.orderId desc";
    Query<InvOrder> query = session.createQuery(queryString);
    query.setParameter("orderCode", orderCode);
    List<InvOrder> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
