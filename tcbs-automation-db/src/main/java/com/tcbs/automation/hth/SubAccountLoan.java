package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_LOAN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountLoan {

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
  @Column(name = "LOAN_KEY")
  private String loanKey;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "USED_AMOUNT")
  private BigDecimal usedAmount;
  @Column(name = "PAID_BACK_AMOUNT")
  private BigDecimal paidBackAmount;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @Column(name = "PAID_BACK_DATE")
  private Timestamp paidBackDate;
  @Column(name = "PAID_BACK_ACCOUNT_NUMBER")
  private String paidBackAccountNumber;
  @Column(name = "PAID_BACK_ACCOUNT_NAME")
  private String paidBackAccountName;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "CURRENCY")
  private String currency;

  /**
   * Author Lybtk
   */
  public static List<SubAccountLoan> getListInquiryHoldingLoan(String custodyCode, String subAccountNo) {
    Query<SubAccountLoan> query = HthDb.h2hConnection.getSession().createQuery(
      "from SubAccountLoan where  (( status = 'HOLD_FOR_BUY' and expiredDate >= sysdate) or status = 'USED_LOAN') and type = 'BOND_LD' " +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : " ") +
        (StringUtils.isNotEmpty(subAccountNo) ? "and subAccountNo =:subAccountNo " : "")
      , SubAccountLoan.class
    );
    setParameter(query, custodyCode, subAccountNo);
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private static void setParameter(Query<SubAccountLoan> query, String custodyCode,
                                   String subAccountNo) {
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(subAccountNo)) {
      query.setParameter("subAccountNo", subAccountNo);
    }
  }

  public static SubAccountLoan getByLoanKey(String loanKey) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountLoan> query = h2hConnection.getSession().createQuery(
        "from SubAccountLoan where loanKey=:loanKey",
        SubAccountLoan.class
      );
      query.setParameter("loanKey", loanKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<SubAccountLoan> getToWaitPayment() {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountLoan> query = h2hConnection.getSession().createQuery(
        "from SubAccountLoan where status = 'WAIT_PAYMENT' and paidBackDate <= sysdate",
        SubAccountLoan.class
      );
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static List<SubAccountLoan> getToPaidLoan() {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountLoan> query = h2hConnection.getSession().createQuery(
        "from SubAccountLoan where status = 'PAID_LOAN'",
        SubAccountLoan.class
      );
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static void updateByLoanKey(String loanKey) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = h2hConnection.getSession().createQuery(
      "UPDATE SubAccountLoan SET status = 'HOLD_FOR_BUY', usedAmount = '' WHERE loanKey=:loanKey"
    );
    query.setParameter("loanKey", loanKey);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateStatusByLoanKey(String status, String loanKey) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = h2hConnection.getSession().createQuery(
      "UPDATE SubAccountLoan SET status =:status WHERE loanKey=:loanKey"
    );
    query.setParameter("status", status);
    query.setParameter("loanKey", loanKey);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountLoan> getListLoan(String custodyCode, String loanKey, String status,
                                                 String stockSubAccountName, String fromTxnDate, String toTxnDate) {
    Query<SubAccountLoan> query = HthDb.h2hConnection.getSession().createQuery(
      "from SubAccountLoan where " + " CURRENCY='VND' " +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : " ") +
        (StringUtils.isNotEmpty(loanKey) ? "and loanKey =: loanKey " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(stockSubAccountName) ? "and stockSubAccountName =:stockSubAccountName " : "") +
        (StringUtils.isNotEmpty(fromTxnDate) ? "and fromTxnDate >=:fromTxnDate " : "") +
        (StringUtils.isNotEmpty(toTxnDate) ? "and toTxnDate <=:toTxnDate " : "")
      , SubAccountLoan.class
    );
    setParameter2(query, custodyCode, loanKey, status, stockSubAccountName,
      fromTxnDate, toTxnDate);
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private static void setParameter2(Query<SubAccountLoan> query, String custodyCode, String loanKey, String status,
                                    String stockSubAccountName, String fromTxnDate, String toTxnDate) {
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(loanKey)) {
      query.setParameter("loanKey", loanKey);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(stockSubAccountName)) {
      query.setParameter("stockSubAccountName", stockSubAccountName);
    }
    if (StringUtils.isNotEmpty(fromTxnDate)) {
      query.setParameter("fromTxnDate", Timestamp.valueOf(fromTxnDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toTxnDate)) {
      query.setParameter("toTxnDate", Timestamp.valueOf(toTxnDate + " 23:59:59"));
    }
  }

  /**
   * Author Lybtk
   */
  public static Long getTotalListLoan(String custodyCode, String loanKey, String status,
                                      String stockSubAccountName, String fromTxnDate, String toTxnDate) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from SubAccountLoan where  type ='BOND_LD' " +
        (StringUtils.isNotEmpty(custodyCode) ? "and custodyCode =:custodyCode " : " ") +
        (StringUtils.isNotEmpty(loanKey) ? "and loanKey =: loanKey " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(stockSubAccountName) ? "and paidBackAccountName =:stockSubAccountName " : "") +
        (StringUtils.isNotEmpty(fromTxnDate) ? "and createdTime >=:fromTxnDate " : "") +
        (StringUtils.isNotEmpty(toTxnDate) ? "and createdTime <=:toTxnDate " : "")
    );
    setParameter3(query, custodyCode, loanKey, status, stockSubAccountName,
      fromTxnDate, toTxnDate);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter3(Query<Long> query, String custodyCode, String loanKey, String status,
                                    String stockSubAccountName, String fromTxnDate, String toTxnDate) {
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(loanKey)) {
      query.setParameter("loanKey", loanKey);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(stockSubAccountName)) {
      query.setParameter("stockSubAccountName", stockSubAccountName);
    }
    if (StringUtils.isNotEmpty(fromTxnDate)) {
      query.setParameter("fromTxnDate", Timestamp.valueOf(fromTxnDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toTxnDate)) {
      query.setParameter("toTxnDate", Timestamp.valueOf(toTxnDate + " 23:59:59"));
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountLoan> getListLoan(String status, String expiredDate) {
    Query<SubAccountLoan> query = HthDb.h2hConnection.getSession().createQuery(
      "from SubAccountLoan where  status=:status and expiredDate <=:expiredDate order by id desc ",
      SubAccountLoan.class
    );
    query.setParameter("status", status)
      .setParameter("expiredDate", Timestamp.valueOf(expiredDate + " 23:59:59"));
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  /**
   * Author Lybtk
   */
  public static List<SubAccountLoan> getBySubAccountNo(String subAccountNo) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountLoan> query = h2hConnection.getSession().createQuery(
        "from SubAccountLoan where subAccountNo=:subAccountNo",
        SubAccountLoan.class
      );
      query.setParameter("subAccountNo", subAccountNo);

      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }


}
