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
@Table(name = "EXCHANGE")
public class ExChange {
  public static Session session;
  static Map<Object, ExChange> mapExchange = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;
  @Column(name = "CURRENCY_ID")
  private String currencyId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static ExChange getExchangeInfo(Object idOrCode) {
    if (mapExchange.isEmpty()) {
      Query<ExChange> query = session.createQuery("from ExChange");
      List<ExChange> listExchange = query.getResultList();

      for (ExChange exChange : listExchange) {
        mapExchange.put(exChange.getId(), exChange);
        mapExchange.put(exChange.getCode(), exChange);
      }
    }

    return mapExchange.get(idOrCode);
  }
}