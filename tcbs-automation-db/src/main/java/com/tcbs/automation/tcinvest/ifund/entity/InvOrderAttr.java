package com.tcbs.automation.tcinvest.ifund.entity;

import com.tcbs.automation.tcinvest.InvGlobalAttr;
import com.tcbs.automation.tcinvest.TcInvest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_ORDER_ATTR")
public class InvOrderAttr {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CAPTION")
  private String caption;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TIMESTAMP")
  private Date timestamp;
  @Column(name = "UPDATED_TIME")
  private Date updateTime;

  public InvOrderAttr(String name, String value, String caption, String orderId) {
    this.name = name;
    this.value = value;
    this.caption = caption;
    this.orderId = orderId;
    this.timestamp = new Date();
  }

  public static List<String> getListOrderByTradingDate(String tradingDate) {
    Query<InvOrderAttr> query = session.createQuery("from InvOrderAttr where name='TRADING_TIMESTAMP' and value =: tradingDate");
    query.setParameter("tradingDate", tradingDate);
    List<InvOrderAttr> listOrder = query.getResultList();
    List<String> listOrderId = new ArrayList<>();
    for (InvOrderAttr order : listOrder) {
      listOrderId.add(order.getOrderId());
    }
    return listOrderId;
  }

  public static void deleteByOrderId(String orderId) {
//    startTransaction();
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvGlobalAttr> query = session.createQuery("DELETE InvOrderAttr WHERE orderId =: orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static InvOrderAttr getListOrderByName(String name, String orderId) {
    Query<InvOrderAttr> query = session.createQuery("from InvOrderAttr where name=: name and orderId=: orderId");
    query.setParameter("name", name);
    query.setParameter("orderId", orderId);
    List<InvOrderAttr> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static List<String> getListOrderByAllocationId(String allocationId) {
    Query<InvOrderAttr> query = session.createQuery("from InvOrderAttr where name='ALLOCATION_ID' and value =: allocationId");
    query.setParameter("allocationId", allocationId);
    List<InvOrderAttr> listOrder = query.getResultList();
    List<String> listOrderId = new ArrayList<>();
    for (InvOrderAttr order : listOrder) {
      listOrderId.add(order.getOrderId());
    }
    return listOrderId;
  }

  public static void startTransaction() {
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
  }

  public void insert() {
    startTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public static List<InvOrderAttr> getListOrderByOrderId(String orderId) {
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvOrderAttr> query = session.createQuery("from InvOrderAttr where orderId=: orderId");
    query.setParameter("orderId", orderId);
    List<InvOrderAttr> result = query.getResultList();
    return result;
  }

  public static void deleteByOrderIdAndNameContact(String orderId){
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvOrderAttr> query = session.createQuery("DELETE InvOrderAttr WHERE orderId =: orderId and (name = 'CONTACT_NAME' or name = 'CONTACT_PHONE' or name = 'CONTACT_POSITION' or name = 'CONTACT_EMAIL' or name = 'CONTACT_ADDRESS')");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByOrderIdAndNamePayment(String orderId){
    session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvOrderAttr> query = session.createQuery("DELETE InvOrderAttr WHERE orderId =: orderId and (name = 'SETTLEMENT_ACCOUNT_NAME' or name = 'SETTLEMENT_ACCOUNT_NUMBER' or name = 'SETTLEMENT_BANK' or name = 'SETTLEMENT_BRANCH' or name = 'SETTLEMENT_CITY' or name = 'SETTLEMENT_BKCD'or name = 'SETTLEMENT_CITAD')");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
