package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.Null;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "H2H_TXN_RECONCILE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HthTxnReconcile {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private BigDecimal id;
  @Column(name = "BOOKING_DATE")
  private Timestamp bookingDate;
  @Column(name = "DETAILS")
  private String details;
  @Column(name = "TXN_ID")
  private String txnId;
  @Column(name = "SOURCE_ACCOUNT_NUMBER")
  private String sourceAccountNumber;
  @Column(name = "SOURCE_ACCOUNT_NAME")
  private String sourceAccountName;
  @Column(name = "SOURCE_ACCOUNT_BALANCE")
  private BigDecimal sourceAccountBalance;
  @Column(name = "DESTINATION_ACCOUNT_NUMBER")
  private String destinationAccountNumber;
  @Column(name = "DESTINATION_ACCOUNT_NAME")
  private String destinationAccountName;
  @Column(name = "DEBIT_AMOUNT")
  private BigDecimal debitAmount;
  @Column(name = "CREDIT_AMOUNT")
  private BigDecimal creditAmount;
  @Column(name = "CURRENCY")
  private String currency;
  @Column(name = "DATE_TIME")
  private Timestamp dateTime;
  @Column(name = "TCBS_REF")
  private String tcbsRef;
  @Column(name = "SERVICE_ID")
  private String serviceId;
  @Column(name = "ORDERING_BANK")
  private String orderingBank;
  @Column(name = "BENEFICIAL_BANK")
  private String beneficialBank;
  @Column(name = "INPUT_SOURCE")
  private String inputSource;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static List<HthTxnReconcile> getListReconcileByParam(String sourceNumber, String destinationNumber,
                                                              String fromDate, String toDate) {
    Query<HthTxnReconcile> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTxnReconcile where sourceAccountNumber=:sourceNumber " +
        (StringUtils.isNotEmpty(destinationNumber) ? "and destinationAccountNumber=:destinationNumber " : "") +
        (StringUtils.isNotEmpty(fromDate) ? "and bookingDate>=:fromDate " : "") +
        (StringUtils.isNotEmpty(toDate) ? "and bookingDate<=:toDate " : "") +
        " order by updatedTime desc, id desc"
      , HthTxnReconcile.class
    );
    query.setParameter("sourceNumber", sourceNumber);
    if (StringUtils.isNotEmpty(destinationNumber)) {
      query.setParameter("destinationNumber", destinationNumber);
    }
    if (StringUtils.isNotEmpty(fromDate)) {
      query.setParameter("fromDate", Timestamp.valueOf(fromDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toDate)) {
      query.setParameter("toDate", Timestamp.valueOf(toDate + " 23:59:59"));
    }
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public static List<HthTxnReconcile> getListReconcileByParamStock(String sourceNumber, String destinationNumber, String fromDate,
                                                                   String toDate, String fromId, String toId, String paymentType) {
    Query<HthTxnReconcile> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTxnReconcile where sourceAccountNumber=:sourceNumber " +
        (StringUtils.isNotEmpty(destinationNumber) ? "and destinationAccountNumber=:destinationNumber " : "") +
        (StringUtils.isNotEmpty(fromDate) ? "and bookingDate>=:fromDate " : "") +
        (StringUtils.isNotEmpty(toDate) ? "and bookingDate<=:toDate " : "") +
        (StringUtils.isNotEmpty(fromId) ? "and id>=:fromId " : "") +
        (StringUtils.isNotEmpty(toId) ? "and id<=:toId " : "") +
        (StringUtils.isNotEmpty(paymentType) && paymentType.equalsIgnoreCase("CASHIN") ?
          "and creditAmount is not null " : "") +
        (StringUtils.isNotEmpty(paymentType) && paymentType.equalsIgnoreCase("CASHOUT") ?
          "and debitAmount is not null " : "") +
        " order by updatedTime desc, id desc"
      , HthTxnReconcile.class
    );
    query.setParameter("sourceNumber", sourceNumber);
    if (StringUtils.isNotEmpty(destinationNumber)) {
      query.setParameter("destinationNumber", destinationNumber);
    }
    if (StringUtils.isNotEmpty(fromDate)) {
      query.setParameter("fromDate", Timestamp.valueOf(fromDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toDate)) {
      query.setParameter("toDate", Timestamp.valueOf(toDate + " 23:59:59"));
    }
    if (StringUtils.isNotEmpty(fromId)) {
      query.setParameter("fromId", Integer.parseInt(fromId));
    }
    if (StringUtils.isNotEmpty(toId)) {
      query.setParameter("toId", Integer.parseInt(toId));
    }
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  //by Lybtk
  public static Long selectReconcileSummary(String date) {
    Query query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from HthTxnReconcile where txnId=:txnId ");
    return (Long) query.uniqueResult();
  }

  /****
   *** Author Thuynt53
   **/

  public static HthTxnReconcile getSubAccBalance(String sourceAccountNumber) {
    h2hConnection.getSession().clear();
    Query<HthTxnReconcile> query = HthDb.h2hConnection.getSession().createQuery(
      " FROM HthTxnReconcile WHERE sourceAccountNumber =:sourceAccountNumber ORDER BY dateTime DESC ",
      HthTxnReconcile.class
    );
    query.setParameter("sourceAccountNumber", sourceAccountNumber);
    query.setMaxResults(1).setFirstResult(0);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public void insert() {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public static List<HthTxnReconcile> listLookUpMoneyFromTCBAcc (String sourceAccountNumber,String destinationAccountNumber,String txnId,BigDecimal creditAmount,String details,String from, String to){
    org.hibernate.query.Query<HthTxnReconcile> query;
    query = h2hConnection.getSession().createQuery(
            " FROM HthTxnReconcile WHERE creditAmount is not null" +
                    (StringUtils.isNotEmpty(sourceAccountNumber) ? " AND sourceAccountNumber=:sourceAccountNumber " : " ") +
                    (StringUtils.isNotEmpty(details) ? " AND details  LIKE :details " : " ") +
                    (StringUtils.isNotEmpty(destinationAccountNumber) ? "AND destinationAccountNumber=:destinationAccountNumber " : " ") +
                    (StringUtils.isNotEmpty(txnId) ? " AND txnId=:txnId " : " ") +
                    (creditAmount != null ? " AND creditAmount=:creditAmount " : " ") +
                    (StringUtils.isNotEmpty(from) ? " AND dateTime >= :from " : "") +
                    (StringUtils.isNotEmpty(to) ? " AND dateTime <= :to " : "") +
                    " ORDER BY id DESC, createdTime DESC ",
            HthTxnReconcile.class
    );
    System.out.println(query.getQueryString());
    query.setParameter("sourceAccountNumber", sourceAccountNumber);
    if (StringUtils.isNotEmpty(details)){
      query.setParameter("details", "%" + details + "%");
    }
    if (StringUtils.isNotEmpty(destinationAccountNumber)) {
      query.setParameter("destinationAccountNumber", destinationAccountNumber);
    }
    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter("txnId", txnId);
    }
    if (creditAmount != null) {
      query.setParameter("creditAmount", creditAmount);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }
}
