package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_GROUP_ORDER")
public class InvGroupOrder {
  public static Session session;
  @Id
  @Column(name = "GROUP_ORDER_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String groupOrderId;
  @Column(name = "GROUP_ORDER_NAME")
  private String groupOrderName;
  @Column(name = "GROUP_ORDER_TYPE")
  private String groupOrderType;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  public static InvGroupOrder getInvGroupOrderByGroupOrderId(String groupOrderId) {
    Query<InvGroupOrder> query = session.createQuery("from InvGroupOrder where groupOrderId=:groupOrderId");
    query.setParameter("groupOrderId", groupOrderId);
    List<InvGroupOrder> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void deleteByGroupOrderId(String groupOrderId) {
    startTransaction();
    Query<InvGroupOrder> query = session.createQuery("DELETE InvGroupOrder WHERE groupOrderId =: groupOrderId");
    query.setParameter("groupOrderId", groupOrderId);
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
