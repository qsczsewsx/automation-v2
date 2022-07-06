package com.tcbs.automation.tcinvest.ifund.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.gson.annotations.SerializedName;
import com.tcbs.automation.tools.DateUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;
import static com.tcbs.automation.tcinvest.TcInvest.tcInvestDbConnection;

@Entity
@Getter
@Setter
@Slf4j
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_PLAN")
public class InvFundPlan {
  public static Session session;
  @Id
  @SerializedName("ID")
  @SequenceGenerator(name = "INV_FUND_PLAN_GENERATOR", sequenceName = "INV_FUND_PLAN_SEQ",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INV_FUND_PLAN_GENERATOR")
  private Long id;

  @Column(name = "PRODUCT_REF")
  private String productRef;

  @Column(name = "CUSTODY_CODE")
  private String custodyCode;

  @Column(name = "ACCOUNT_ID")
  private String accountId;

  @Column(name = "PURCHASE_ID")
  private String purchaseId;

  @Column(name = "TYPE")
  @Enumerated(EnumType.STRING)
  private InvFundPlanType type;

  @Column(name = "FUND_INVEST_VALUE")
  private Double fundInvestValue;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private InvFundPlanStatus status;

  @Column(name = "PROGRAM_CODE")
  @Enumerated(EnumType.STRING)
  private ProgramCode programCode;

  @Column(name = "START_TIME")
  private Timestamp startTime;

  @Column(name = "CREATED_TIME")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = DateUtils.FORMAT_FLOAT_NUMBER_REGEX)
  private Instant createdTime;

  @Column(name = "MODIFIED_TIME")
  @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = DateUtils.FORMAT_FLOAT_NUMBER_REGEX)
  private Instant modifiedTime;

  @Transient
  private List<InvFundPlanDetail> planDetails;

  public InvFundPlan(String productRef, String custodyCode, String accountId, String purchaseId,
                     InvFundPlanType type, Double fundInvestValue, InvFundPlanStatus status, ProgramCode programCode,
                     Instant createdTime, Timestamp startTime) {
    this.productRef = productRef;
    this.custodyCode = custodyCode;
    this.accountId = accountId;
    this.purchaseId = purchaseId;
    this.type = type;
    this.fundInvestValue = fundInvestValue;
    this.status = status;
    this.programCode = programCode;
    this.startTime = startTime;
    this.setCreatedTime(createdTime);
  }

  public InvFundPlan(String productRef, String custodyCode, String accountId, String purchaseId,
                     InvFundPlanType type, Double fundInvestValue, InvFundPlanStatus status, ProgramCode programCode,
                     Instant createdTime, Timestamp startTime, Instant modifiedTime) {
    this.productRef = productRef;
    this.custodyCode = custodyCode;
    this.accountId = accountId;
    this.purchaseId = purchaseId;
    this.type = type;
    this.fundInvestValue = fundInvestValue;
    this.status = status;
    this.programCode = programCode;
    this.startTime = startTime;
    this.setCreatedTime(createdTime);
    this.setModifiedTime(modifiedTime);
  }

  public static List<InvFundPlan> getAllFundPlanInfo() {
    if (session == null) {
      session = tcInvestDbConnection.getSession();
    }
    Query<InvFundPlan> query = session.createQuery("from InvFundPlan");
    List<InvFundPlan> result = query.getResultList();
    return result;
  }

  public static InvFundPlan getPlanById(Long id) {
    if (session == null) {
      session = tcInvestDbConnection.getSession();
    }
    Query<InvFundPlan> query = session.createQuery("from InvFundPlan where id=:id");
    query.setParameter("id", id);
    List<InvFundPlan> result = query.getResultList();
    return CollectionUtils.isEmpty(result) ? null : result.get(0);
  }

  public static void insertPlan(InvFundPlan fundPlan) {
    try {
      if (session == null) {
        session = tcInvestDbConnection.getSession();
      }
      session.clear();
      beginTransaction(session);
      session.save(fundPlan);
      session.getTransaction().commit();
    } catch (Exception ex) {
      log.error("[insertPlan]__Insert fund plan EXCEPTION >>>>> {}", ex.getMessage());
    }
  }

  public static void deleteFundPlan(InvFundPlan plan, List<InvFundPlanDetail> planDetails) {
    try {
      if (session == null) {
        session = tcInvestDbConnection.getSession();
      }
      session.clear();
      beginTransaction(session);
      session.delete(plan);
      if (org.apache.commons.collections.CollectionUtils.isNotEmpty(planDetails)) {
        planDetails.forEach(session::delete);
      }
      session.getTransaction().commit();
    } catch (Exception ex) {
      log.error("[deleteFundPlan]__Delete fund plan EXCEPTION >>>>> {}", ex.getMessage());
    }
  }

  public static Long getLastPlan() {
    if (session == null) {
      session = tcInvestDbConnection.getSession();
    }
    session.clear();
    beginTransaction(session);

    String queryPlan = "SELECT MAX(FP.ID) FROM INV_FUND_PLAN FP";
    Query query = session.createNativeQuery(queryPlan);
    List<Object> planResult = query.getResultList();
    if (!org.apache.commons.collections.CollectionUtils.isEmpty(planResult)) {
      try {
        return Long.parseLong(String.valueOf(planResult.get(0)));
      } catch (Exception e) {
        return 0L;
      }
    } else {
      return 0L;
    }
  }
}
