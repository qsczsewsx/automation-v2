package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_BALANCE_FLUCTUATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountBalanceFluctuation {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "BEFORE_BALANCE")
  private BigDecimal beforeBalance;
  @Column(name = "BEFORE_AVAILABLE")
  private BigDecimal beforeAvailable;
  @Column(name = "BEFORE_HELD")
  private BigDecimal beforeHeld;
  @Column(name = "BEFORE_LOAN")
  private BigDecimal beforeLoan;
  @Column(name = "BEFORE_PLANNING_BALANCE")
  private BigDecimal beforePlanningBalance;
  @Column(name = "BEFORE_PLANNING_AVAILABLE")
  private BigDecimal beforePlanningAvailable;
  @Column(name = "BEFORE_PLANNING_HELD")
  private BigDecimal beforePlanningHeld;
  @Column(name = "AFTER_BALANCE")
  private BigDecimal afterBalance;
  @Column(name = "AFTER_AVAILABLE")
  private BigDecimal afterAvailable;
  @Column(name = "AFTER_HELD")
  private BigDecimal afterHeld;
  @Column(name = "AFTER_LOAN")
  private BigDecimal afterLoan;
  @Column(name = "AFTER_PLANNING_BALANCE")
  private BigDecimal afterPlanningBalance;
  @Column(name = "AFTER_PLANNING_AVAILABLE")
  private BigDecimal afterPlanningAvailable;
  @Column(name = "AFTER_PLANNING_HELD")
  private BigDecimal afterPlanningHeld;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "REFERENCE_ID")
  private String referenceId;

  public static SubAccountBalanceFluctuation getByTransactionId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountBalanceFluctuation> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalanceFluctuation where referenceId=:referenceId ",
        SubAccountBalanceFluctuation.class
      );
      query.setParameter("referenceId", referenceId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static SubAccountBalanceFluctuation getByTransactionIdAndSubAccountNo(String referenceId, String subAccountNo) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountBalanceFluctuation> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalanceFluctuation where referenceId=:referenceId and subAccountNo=:subAccountNo",
        SubAccountBalanceFluctuation.class
      );
      query.setParameter("referenceId", referenceId);
      query.setParameter("subAccountNo", subAccountNo);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountBalanceFluctuation> getListFromTransactionIdAndSubAccountNo(String referenceId,String subAccountNo) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBalanceFluctuation> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalanceFluctuation where referenceId=:referenceId and subAccountNo=:subAccountNo order by id desc",
        SubAccountBalanceFluctuation.class
      );
      query.setParameter("referenceId", referenceId);
      query.setParameter("subAccountNo", subAccountNo);

      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountBalanceFluctuation> getListFromSubTransactionId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBalanceFluctuation> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalanceFluctuation where referenceId=:referenceId order by id desc",
        SubAccountBalanceFluctuation.class
      );
      query.setParameter("referenceId", referenceId);

      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */

  public static SubAccountBalanceFluctuation getByReferenceId(String referenceId) {
    try {
      h2hConnection.getSession().clear();
      org.hibernate.query.Query<SubAccountBalanceFluctuation> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalanceFluctuation where referenceId=:referenceId ",
        SubAccountBalanceFluctuation.class
      );
      query.setParameter("referenceId", referenceId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
