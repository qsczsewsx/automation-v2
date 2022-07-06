package com.tcbs.automation.fundstation.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@Table(name = "PRODUCT_GLOBAL")
public class ProductGlobal {
  public static Session session;
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  private static ObjectMapper obm = new ObjectMapper();
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "LISTED_NAME")
  private String listedName;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "TYPE_CODE")
  private String typeCode;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "EXCHANGE")
  private Integer exchange;

  @Column(name = "PAR")
  private Double par;

  @Column(name = "ISSUE_TIMESTAMP")
  private Date issueTimestamp;

  @Column(name = "MATURITY_TIMESTAMP")
  private Date maturityTimestamp;

  @Column(name = "LISTED_TIMESTAMP")
  private Date listedTimestamp;

  @Column(name = "DELISTED_TIMESTAMP")
  private Date delistedTimestamp;

  @Column(name = "COUPON_PAYMENT_TYPE")
  private Integer couponPaymentType;

  @Column(name = "CONVENTION")
  private Integer convention;

  @Column(name = "COMPANY")
  private Integer company;

  @Column(name = "COMPANY_GROUP")
  private Integer companyGroup;

  @Column(name = "INPUT_SOURCE")
  private String inputSource;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "INDUSTRY")
  private String industry;

  @Column(name = "EARLY_WITHDRAWN_INTEREST")
  private String earlyWithdrawnInterest;

  @Column(name = "AUTO_ROLLOVER")
  private String autoRollover;

  @Column(name = "AUTO_ROLLOVER_INTEREST")
  private String autoRolloverInterest;

  @Column(name = "INTEREST_PERIOD_EXCLUDING")
  private String interestPeriodExcluding;

  @Column(name = "HOLIDAY_INTEREST")
  private String holidayInterest;

  public static List<ProductGlobal> getAllProductGlobal() {
    Query<ProductGlobal> query = session.createQuery("from ProductGlobal");
    List<ProductGlobal> result = query.getResultList();
    return result;
  }

  public static ProductGlobal getProductGlobalByTicker(String code) {
    Query<ProductGlobal> query = session.createQuery("from ProductGlobal where code =:code");
    query.setParameter("code", code);
    List<ProductGlobal> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}
