package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "EXCHANGE_ATTR")
public class ExchangeAttr {
  public static Session session;
  public static List<ExchangeAttr> listSettlement = new ArrayList<>();
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;
  @Column(name = "EXCHANGE_ID")
  private Integer exchangeId;
  @Column(name = "UNDERLYING_TYPE_ID")
  private Integer underlyingTypeId;
  @Column(name = "TRANSACTION_ACTION_ID")
  private Integer transactionActionId;
  @Column(name = "PRODUCT_GLOBAL_STATUS_ID")
  private Integer productGlobalStatusId;
  @Column(name = "VALUE")
  private Integer value;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<ExchangeAttr> getAllSetlement() {
    if (listSettlement == null || listSettlement.size() == 0) {
      Query<ExchangeAttr> query = session.createQuery("from ExchangeAttr ");
      listSettlement = query.getResultList();
    }
    return listSettlement;
  }
}
