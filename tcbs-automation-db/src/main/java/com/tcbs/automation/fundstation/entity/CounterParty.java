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
@Table(name = "COUNTER_PARTY")
public class CounterParty {
  public static Session session;
  private static Map<Object, CounterParty> mapCounterParty = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static void clearCacheCounterParty() {
    mapCounterParty.clear();
  }

  public static CounterParty getCounterParty(Object codeOrId) {
    if (mapCounterParty.isEmpty()) {
      for (CounterParty broker : getAllCounterPartyFromDb()) {
        mapCounterParty.put(broker.getId(), broker);
        mapCounterParty.put(broker.getCode(), broker);
      }
    }

    if (codeOrId != null && mapCounterParty.containsKey(codeOrId)) {
      return mapCounterParty.get(codeOrId);
    } else {
      return new CounterParty();
    }
  }

  public static List<CounterParty> getAllCounterPartyFromDb() {
    Query<CounterParty> query = session.createQuery("from CounterParty order by code asc");
    return query.getResultList();
  }

  public static void deleteListCounterParty(List<String> listCounterParty) {
    if (!listCounterParty.isEmpty()) {
      Query<CounterParty> query = session.createQuery("delete CounterParty where  code in :listCounterParty");
      query.setParameter("listCounterParty", listCounterParty);
      query.executeUpdate();
    }
  }
}
