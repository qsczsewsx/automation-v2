package com.tcbs.automation.tcinvest.cogs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INV_ORDER")
public class InvOrder implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ORDER_ID")
  private Long id;

  @Column(name = "ACCOUNT_ID")
  private String partyId;

  @Column(name = "ACTION_ID")
  private Long actionId;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "VOLUME")
  private Double volume;

  @Column(name = "TRANSACTION_VALUE")
  private Double transactionValue;

  @Column(name = "TOTAL_VALUE")
  private Double totalValue;

  @Column(name = "PURCHASER_ID")
  private String purchaserId;

  @Column(name = "CPTY_ID")
  private String counterPartyId;

  @Column(name = "AGREEMENT_ID")
  private String agreementId;

  @Column(name = "BOOK_ID")
  private String bookId;

  @Column(name = "STATUS")
  private String orderStatus;

  @Column(name = "ORDER_CODE")
  private String orderCode;

  @Column(name = "ORDER_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdAt;

  @Column(name = "VALUATION_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date valuationTime;

  @Column(name = "SUBMITTED_TIMESTAMP")
  @Temporal(TemporalType.TIMESTAMP)
  private Date submittedTime;

  @Column(name = "GROUP_ORDER_ID")
  private Long groupOrderId;

  @Column(name = "WARNING_MESSAGE")
  private String description;
}
