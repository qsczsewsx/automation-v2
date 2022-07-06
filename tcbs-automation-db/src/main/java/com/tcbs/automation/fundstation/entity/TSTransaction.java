package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "TS_PORTFOLIO_HISTORY")
public class TSTransaction {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "SOURCE")
  private String source;

  @Column(name = "DESTINATION")
  private String destination;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "UNIT")
  private Double unit;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "PRODUCT_TYPE")
  private String productType;

  @Column(name = "REPO_DATE")
  private Date repoDate;

  @Column(name = "REF_ID")
  private Integer refId;

  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

}
