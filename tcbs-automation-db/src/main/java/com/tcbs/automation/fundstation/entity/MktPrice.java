package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "MKT_PRICE")
public class MktPrice {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "PRICE_DATE")
  @Temporal(TemporalType.DATE)
  private Date priceDate;

  @Column(name = "CLEAN_PRICE")
  private Double cleanPrice;

  @Column(name = "MKT_PRICE")
  private Double mktPrice;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "TRADING_VOLUME")
  private Double tradingVolume;

  @Column(name = "TRADING_VALUE")
  private Double tradingValue;

  public static List<MktPrice> getAllMktPriceByStatus(String status) {
    session.clear();
    Query<MktPrice> query = session.createQuery(buildQueryMktPriceByStatus(status));
    if (!"ALL".equals(status)) {
      query.setParameter("status", status);
    }
    return query.getResultList();
  }

  static String buildQueryMktPriceByStatus(String status) {
    StringBuilder sql = new StringBuilder("from MktPrice ");
    if (!"ALL".equals(status)) {
      sql.append("where status = :status ");
    }
    sql.append("order by priceDate desc, updatedTimestamp desc, id desc ");
    return sql.toString();
  }
}
