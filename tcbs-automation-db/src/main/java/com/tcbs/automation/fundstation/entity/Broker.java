package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "BROKER")
public class Broker {
  public static Session session;
  private static Map<Object, Broker> mapBroker = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static void clearCacheBroker() {
    mapBroker.clear();
  }

  public static Broker getBroker(Object codeOrId) {
    if (mapBroker.isEmpty()) {
      for (Broker broker : getAllBrokerFromDb()) {
        mapBroker.put(broker.getId(), broker);
        mapBroker.put(broker.getCode(), broker);
      }
    }

    if (codeOrId != null && mapBroker.containsKey(codeOrId)) {
      return mapBroker.get(codeOrId);
    } else {
      return new Broker();
    }
  }

  public static List<Broker> getAllBrokerFromDb() {
    Query<Broker> query = session.createQuery("from Broker order by code asc");
    return query.getResultList();
  }

  public static void deleteListBroker(List<String> listBroker) {
    if (!listBroker.isEmpty()) {
      Query<Broker> query = session.createQuery("delete Broker where  code in :listBroker");
      query.setParameter("listBroker", listBroker);
      query.executeUpdate();
    }
  }
}
