package com.tcbs.automation.tcinvest.ifund.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.tcbs.automation.tools.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;
import static com.tcbs.automation.tcinvest.TcInvest.tcInvestDbConnection;

@Entity
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_PLAN_DETAIL")
public class InvFundPlanDetail {
  public static Session session;
  @Id
  @SerializedName("ID")
  @SequenceGenerator(name = "INV_FUND_PLAN_DETAIL_GENERATOR", sequenceName = "INV_FUND_PLAN_DETAIL_SEQ",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INV_FUND_PLAN_DETAIL_GENERATOR")
  private Long id;

  @Column(name = "PLAN_ID")
  private Long planId;

  @Column(name = "FUND_ORDER_ID")
  private Long fundOrderId;

  @Column(name = "ACTION")
  @Enumerated(EnumType.STRING)
  private InvFundPlanDetailAction action;

  @Column(name = "TRANSACTION_VALUE")
  private Double transactionValue;

  @Column(name = "COUNT_FUTURES")
  private Long countFutures;

  @Column(name = "TRANSACTION_ID")
  private String transactionId;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private InvFundPlanDetailStatus status;

  @Column(name = "FEE")
  private Double fee;

  @Column(name = "TRADING_DATE")
  private Date tradingDate;

  @Column(name = "REF_ID")
  private Long refId;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "BATCH_ID")
  private String batchId;

  @Column(name = "PURCHASE_ID")
  private String purchaseId;

  @Column(name = "FUTURE_CONTRACT")
  private String futureContract;

  @Column(name = "FUTURE_CONTRACT_POSITION")
  private String futureContractPosition;

  @Column(name = "RETRY_TIME")
  private Integer retryTime;

  @Column(name = "IA_STATUS")
  private String iaStatus;

  @Column(name = "CREATED_TIME")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = DateUtils.FORMAT_FLOAT_NUMBER_REGEX)
  private Instant createdTime;

  @Column(name = "MODIFIED_TIME")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = DateUtils.FORMAT_FLOAT_NUMBER_REGEX)
  private Instant modifiedTime;

  @Transient
  @JsonProperty
  private String accountId;

  @Transient
  @JsonProperty
  private Long minId;

  public InvFundPlanDetail(Long planId, InvFundPlanDetailAction action, Double transactionValue,
                           String transactionId, InvFundPlanDetailStatus status, Double fee, Date tradingDate, Long refId,
                           String description, String batchId, String purchaseId) {
    this.planId = planId;
    this.action = action;
    this.transactionValue = transactionValue;
    this.transactionId = transactionId;
    this.status = status;
    this.fee = fee;
    this.tradingDate = tradingDate;
    this.refId = refId;
    this.description = description;
    this.batchId = batchId;
    this.purchaseId = purchaseId;
  }

  public InvFundPlanDetail(Long planId, InvFundPlanDetailAction action, Double transactionValue,
                           String transactionId, InvFundPlanDetailStatus status, Double fee, Date tradingDate, Long refId,
                           String description, String batchId, String purchaseId, Instant modifiedTime) {
    this.planId = planId;
    this.action = action;
    this.transactionValue = transactionValue;
    this.transactionId = transactionId;
    this.status = status;
    this.fee = fee;
    this.tradingDate = tradingDate;
    this.refId = refId;
    this.description = description;
    this.batchId = batchId;
    this.purchaseId = purchaseId;
    this.modifiedTime = modifiedTime;
  }

  public static void insertPlanDetail(List<InvFundPlanDetail> planDetails) {
    try {
      if (session == null) {
        session = tcInvestDbConnection.getSession();
      }
      session.clear();
      beginTransaction(session);
      planDetails.forEach(session::save);
      session.getTransaction().commit();
    } catch (Exception ex) {
      log.error("[insertPlanDetail]__Insert fund plan detail EXCEPTION >>>>> {}", ex.getMessage());
    }
  }
}
