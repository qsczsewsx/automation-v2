package com.tcbs.automation.cas;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Data
@Getter
@Setter
@Entity
@Table(name = "TCBS_ICONNECT_ORDER")
public class TcbsIconnectOrder {

  private static final Logger logger = LoggerFactory.getLogger(TcbsIconnectOrder.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "QUANTITY")
  private String quantity;
  @Column(name = "PRICE")
  private String price;
  @Column(name = "TOTAL_VALUE")
  private String totalValue;
  @Column(name = "MATCHED_DATE")
  private Date matchedDate;
  @Column(name = "ORDER_PAYLOAD")
  private String orderPayload;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public TcbsIconnectOrder getByOrderIdAndType(String orderId, String type) {
    Query<TcbsIconnectOrder> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIconnectOrder a where a.orderId=:orderId and a.status=:status", TcbsIconnectOrder.class);
    query.setParameter("orderId", orderId);
    query.setParameter("status", type);
    try {
//      query.setMaxResults(1);
      return query.getResultList().get(query.getResultList().size() - 1);
//      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }


  public TcbsIconnectOrder getByOrderId(String orderId) {
    Query<TcbsIconnectOrder> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIconnectOrder a where a.orderId=:orderId", TcbsIconnectOrder.class);
    query.setParameter("orderId", orderId);
    try {
      query.setMaxResults(1);
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public void deleteByOrderId(String orderId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsIconnectOrder WHERE orderId=:orderId");
      query.setParameter("orderId", orderId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }


  public TcbsIconnectOrder getTcbsReferralByDataReferralId(String orderId) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<TcbsIconnectOrder> query = session.createQuery(
      "from TcbsIconnectOrder WHERE orderId=:orderId", TcbsIconnectOrder.class);
    TcbsIconnectOrder result = new TcbsIconnectOrder();
    query.setParameter("orderId", orderId);
    query.setMaxResults(1);
    try {
      result = query.getSingleResult();
      session.refresh(result);
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
    return result;
  }
}
