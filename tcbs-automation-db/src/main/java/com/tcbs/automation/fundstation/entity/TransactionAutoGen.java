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
@Table(name = "TRANSACTION_AUTO_GEN")
public class TransactionAutoGen {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "TRADING_DATE")
  private Date tradingDate;

  @Column(name = "TRANSACTION_ACTION")
  private Integer transactionAction;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "UNDERLYING_TYPE_CODE")
  private String underlyingTypeCode;

  @Column(name = "VOLUME")
  private Double volume;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "BROKER_CODE")
  private String brokerCode;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;

  @Column(name = "EX_DATE")
  private Date exDate;

  @Column(name = "MATURITY_DATE")
  private Date maturityDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
}
