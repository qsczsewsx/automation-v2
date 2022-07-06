package com.tcbs.automation.tcinvest.ifund.entity;

import com.tcbs.automation.tcinvest.InvGlobalAttr;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_ORDER_MATCHED")
public class InvOrderMatched {
  public static Session session;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "MATCHED_PRICE")
  private Double matchedPrice;
  @Column(name = "MATCHED_VOLUME")
  private Double matchedVolume;
  @Column(name = "MATCHED_TIMESTAMP")
  private Timestamp matchedTimestamp;
  @Id
  @Column(name = "MATCHED_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String matchedId;

  public InvOrderMatched(String orderId, Double matchedPrice, Double matchedVolume, Timestamp matchedTimestamp) {
    this.orderId = orderId;
    this.matchedPrice = matchedPrice;
    this.matchedVolume = matchedVolume;
    this.matchedTimestamp = matchedTimestamp;
  }

  public static void deleteByOrderId(String orderId) {
    startTransaction();
    Query<InvGlobalAttr> query = session.createQuery("DELETE InvOrderMatched WHERE orderId =: orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void startTransaction() {
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
  }

  public String insert() {
    startTransaction();
    session.save(this);
    session.getTransaction().commit();
    return session.save(this).toString();
  }
}
