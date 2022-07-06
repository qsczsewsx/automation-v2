package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_ORDER_DATA_MATCHED")
public class InvOrderDataMatched {
  public static Session session;

  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Id
  @Column(name = "ORDER_ID")
  private String orderId;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "PURCHASER_ID")
  private String purchaserId;

  @Column(name = "ACTION_ID")
  private String actionId;

  @Column(name = "VOLUME")
  private String volume;

  @Column(name = "PRICE")
  private String price;

  @Column(name = "TRANSACTION_VALUE")
  private String transactionValue;

  @Column(name = "TRADING_TIMESTAMP")
  private Date tradingTimestamp;

  @Column(name = "MATCHED_DATE")
  private Date matchedDate;

  @Column(name = "ROW_NO")
  private String rowNo;

  @Column(name = "TOTAL_BUY")
  private String totalBuy;

  @Column(name = "TOTAL_SELL")
  private String totalSell;

  @Column(name = "BALANCE")
  private String balance;

  @Column(name = "PARAMS")
  private String params;

  public static void deleteByAccountID(String accountId) {
    startTransaction();
    Query<InvOrderDataMatched> query = session.createQuery("DELETE InvOrderDataMatched WHERE accountId =: accountId");
    query.setParameter("accountId", accountId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByPurchaserId(String purchaserId) {
    startTransaction();
    Query<InvOrderDataMatched> query = session.createQuery("DELETE InvOrderDataMatched WHERE purchaserId =: purchaserId");
    query.setParameter("purchaserId", purchaserId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void startTransaction() {
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
  }

}
