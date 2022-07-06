package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PRODUCT_DETAIL")
public class ProductDetail {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "PORTFOLIO_ID")
  private String portfolioId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "EXCHANGE")
  private String exchange;

  @Column(name = "PAR")
  private String par;

  @Column(name = "ISSUE_TIMESTAMP")
  private Date issueTimestamp;

  @Column(name = "MATURITY_TIMESTAMP")
  private Date maturityTimestamp;

  @Column(name = "LISTED_TIMESTAMP")
  private Date listedTimestamp;

  @Column(name = "PURCHASING_TIMESTAMP")
  private Date purchasingTimestamp;

  @Column(name = "DELISTED_TIMESTAMP")
  private Date delistedTimestamp;

  @Column(name = "RATE")
  private String rate;

  @Column(name = "RATE_TYPE")
  private String rateType;

  @Column(name = "CONVENTION")
  private String convention;

  @Column(name = "COMPANY")
  private String company;

  @Column(name = "COMPANY_GROUP")
  private String companyGroup;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "TYPE_CODE")
  private String typeCode;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
}
