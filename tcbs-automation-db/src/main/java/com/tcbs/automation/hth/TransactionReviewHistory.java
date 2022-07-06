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
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "TRANSACTION_REVIEW_HISTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReviewHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TYPE")
  private String type;
  @NotNull
  @Column(name = "TRANS_ID")
  private BigDecimal transId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "BEFORE")
  private String before;
  @Column(name = "AFTER")
  private String after;
  @Column(name = "MAKER")
  private String maker;
  @Column(name = "CHECKER")
  private String checker;
  @Column(name = "MAKER_NOTE")
  private String makerNote;
  @Column(name = "CHECKER_NOTE")
  private String checkerNote;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "APPROVE_NUMBER")
  private String approveNumber;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  /**
   * Author Lybtk
   */
  public static TransactionReviewHistory getTransId(BigDecimal transId) {
    Query<TransactionReviewHistory> query = HthDb.h2hConnection.getSession().createQuery(
      "from TransactionReviewHistory where transId=:transId",
      TransactionReviewHistory.class
    );
    query.setParameter("transId", transId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static List<TransactionReviewHistory> getListTransactionReviewByParam(String transactionId, String custodyCode, String custodyName, String maker, String action, String checker, String status, String amount, String from, String to) {
    try {
      h2hConnection.getSession().clear();
      String queryStr = "SELECT * FROM (SELECT * FROM TRANSACTION_REVIEW_HISTORY ORDER BY CREATED_TIME desc) TRH " +
        "join COLLECTION_TRANS CT on TRH.TRANS_ID = CT.ID " +
        "where TRH.TYPE = 'COBO'" +
        (StringUtils.isNotEmpty(action) ? "and TRH.ACTION =:action " : "") +
        (StringUtils.isNotEmpty(maker) ? "and TRH.MAKER =:maker " : "") +
        (StringUtils.isNotEmpty(from) ? "and TRH.CREATED_TIME >= :from " : "") +
        (StringUtils.isNotEmpty(to) ? "and TRH.CREATED_TIME <= :to " : "") +
        ((StringUtils.isNotEmpty(status) && !status.equals("WAITING_APPROVE")) ? "and TRH.STATUS =:status " : "") +
        ((StringUtils.isNotEmpty(status) && status.equals("WAITING_APPROVE")) ? "and TRH.STATUS ='WAITING_APPROVE' " : "") +
        (StringUtils.isEmpty(status) ? "and TRH.STATUS !='WAITING_APPROVE' " : "") +
        (StringUtils.isNotEmpty(checker) ? "and TRH.CHECKER =:checker " : "") +
        (StringUtils.isNotEmpty(transactionId) ? "and CT.TRANSACTION_ID  LIKE :transactionId " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and CT.STOCK_ACCOUNT_NUMBER =:custodyCode " : "") +
        (StringUtils.isNotEmpty(custodyName) ? "and CT.STOCK_ACCOUNT_NAME =:custodyName " : "") +
        (StringUtils.isNotEmpty(amount) ? "and CT.AMOUNT =:amount " : "") +
        "and ROWNUM<=20 and TRH.ACTION !='NOTE' and TRH.STATUS != 'CANCELED' ";

      Query<TransactionReviewHistory> query = h2hConnection.getSession().createNativeQuery(
        queryStr, TransactionReviewHistory.class
      );
      if (StringUtils.isNotEmpty(status) && !status.equals("WAITING_APPROVE")) {
        query.setParameter("status", status);
      }
      if (StringUtils.isNotEmpty(maker)) {
        query.setParameter("maker", maker);
      }
      if (StringUtils.isNotEmpty(from)) {
        query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
      }
      if (StringUtils.isNotEmpty(to)) {
        query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
      }
      if (StringUtils.isNotEmpty(action)) {
        query.setParameter("action", action);
      }
      if (StringUtils.isNotEmpty(transactionId)) {
        query.setParameter("transactionId", transactionId);
      }
      if (StringUtils.isNotEmpty(custodyCode)) {
        query.setParameter("custodyCode", custodyCode);
      }
      if (StringUtils.isNotEmpty(custodyName)) {
        query.setParameter("custodyName", custodyName);
      }
      if (StringUtils.isNotEmpty(checker)) {
        query.setParameter("checker", checker);
      }
      if (StringUtils.isNotEmpty(amount)) {
        query.setParameter("amount", amount);
      }
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("error : " + e.getMessage());
      return null;
    }
  }

  /**
   * Author Thompt
   */
  public static TransactionReviewHistory getLastestRowWithTransId(BigDecimal transId) {
    h2hConnection.getSession().clear();
    Query<TransactionReviewHistory> query = HthDb.h2hConnection.getSession().createQuery(
      "from TransactionReviewHistory where transId=:transId order by id desc",
      TransactionReviewHistory.class
    );
    query.setParameter("transId", transId);
    query.setMaxResults(1).setFirstResult(0);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static void deleteByTransIdAndStatus(BigDecimal transId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    javax.persistence.Query query = session.createQuery("delete from TransactionReviewHistory where transId=:transId and status=:status");
    query.setParameter("transId", transId)
      .setParameter("status", status)
      .executeUpdate();
    session.getTransaction().commit();
  }


  public static List<TransactionReviewHistory> getListByStatus(String status) {
    try {
      h2hConnection.getSession().clear();
      String queryStr = "SELECT * FROM TRANSACTION_REVIEW_HISTORY TRH " +
        "join COLLECTION_TRANS CT on TRH.TRANS_ID = CT.ID " +
        "where TRH.STATUS =:status " +
        "order by TRH.CREATED_TIME desc";

      Query<TransactionReviewHistory> query = h2hConnection.getSession().createNativeQuery(
        queryStr, TransactionReviewHistory.class
      );
      if (StringUtils.isNotEmpty(status)) {
        query.setParameter("status", status);
      }
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("error : " + e.getMessage());
      return null;
    }

  }


  public static void updateStatus(String status, BigDecimal transId) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    javax.persistence.Query query = session.createQuery(
      "update TransactionReviewHistory set status=:status where transId=:transId"
    );
    query.setParameter("status", status)
      .setParameter("transId", transId)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static List<TransactionReviewHistory> getListByTransId(BigDecimal transId, String status) {
    h2hConnection.getSession().clear();
    Query<TransactionReviewHistory> query;
    query = h2hConnection.getSession().createQuery(
      "FROM TransactionReviewHistory WHERE transId=:transId " +
        (StringUtils.isNotEmpty(status) ? "and STATUS =:status " : "") +
        "ORDER BY createdTime desc",
      TransactionReviewHistory.class);

    query.setParameter("transId", transId);

    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    return query.getResultList();

  }

  public static TransactionReviewHistory getTransactionReviewById(BigDecimal id) {
    h2hConnection.getSession().clear();
    Query<TransactionReviewHistory> query = HthDb.h2hConnection.getSession().createQuery(
      "from TransactionReviewHistory where id=:id",
      TransactionReviewHistory.class
    );
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static List<TransactionReviewHistory> getListTransactionInvestByParam(String transactionId, String stockAccountNumber, String stockAccountName,
                                                                               String maker, String action, String checker, String status, String from, String to) {
    try {
      h2hConnection.getSession().clear();
      String queryStr = "SELECT * FROM TRANSACTION_REVIEW_HISTORY TRH " +
        "join COLLECTION_TRANS CT on TRH.TRANS_ID = CT.ID " +
        "where TRH.TYPE = 'COBO'" +
        (StringUtils.isNotEmpty(status) ? "and TRH.STATUS =:status " : "") +
        (StringUtils.isNotEmpty(maker) ? "and TRH.MAKER like :maker " : "") +
        (StringUtils.isNotEmpty(from) ? "and TRH.CREATED_TIME >= :from " : "") +
        (StringUtils.isNotEmpty(to) ? "and TRH.CREATED_TIME <= :to " : "") +
        (StringUtils.isNotEmpty(action) ? "and TRH.ACTION =:action " : "") +
        (StringUtils.isNotEmpty(checker) ? "and TRH.CHECKER =:checker " : "") +
        (StringUtils.isNotEmpty(transactionId) ? "and CT.TRANSACTION_ID like :transactionId " : "") +
        (StringUtils.isNotEmpty(stockAccountNumber) ? "and CT.STOCK_ACCOUNT_NUMBER like :stockAccountNumber " : "") +
        (StringUtils.isNotEmpty(stockAccountName) ? "and CT.STOCK_ACCOUNT_NAME like :stockAccountName " : "") +
        "order by TRH.CREATED_TIME desc";

      Query<TransactionReviewHistory> query = h2hConnection.getSession().createNativeQuery(
        queryStr, TransactionReviewHistory.class
      );

      if (StringUtils.isNotEmpty(status)) {
        query.setParameter("status", status);
      }
      if (StringUtils.isNotEmpty(maker)) {
        query.setParameter("maker", "%" + maker + "%");
      }
      if (StringUtils.isNotEmpty(from)) {
        query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
      }
      if (StringUtils.isNotEmpty(to)) {
        query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
      }
      if (StringUtils.isNotEmpty(action)) {
        query.setParameter("action", action);
      }
      if (StringUtils.isNotEmpty(transactionId)) {
        query.setParameter("transactionId", "%" + transactionId + "%");
      }
      if (StringUtils.isNotEmpty(stockAccountNumber)) {
        query.setParameter("stockAccountNumber", "%" + stockAccountNumber + "%");
      }
      if (StringUtils.isNotEmpty(stockAccountName)) {
        query.setParameter("stockAccountName", "%" + stockAccountName + "%");
      }
      if (StringUtils.isNotEmpty(checker)) {
        query.setParameter("checker", checker);
      }
      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println("error : " + e.getMessage());
      return null;
    }

  }


}
