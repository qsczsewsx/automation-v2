package com.tcbs.automation.hth;

import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Data
@Entity
@Table(name = "H2H_TRANS")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HthTrans {

  private static final String QUERY_CLIENT_BATCH_ID = "and clientBatchId =:batchId ";
  private static final String CLIENT_BATCH_ID = "clientBatchId";
  private static final String BATCH_ID = "batchId";
  private static final String QUERY_CLIENT_TXN_ID = "and clientTxnId =:txnId ";
  private static final String TXN_ID = "txnId";
  private static final String DES_ACCOUNT_NO = "desAccountNo";
  @Id
  @NotNull
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "H2H_TRANS_GEN")
  @SequenceGenerator(sequenceName = "ISEQ$$_200022", allocationSize = 1, name = "H2H_TRANS_GEN")
  private BigDecimal id;
  @Column(name = "CLIENT_TXN_ID")
  private String clientTxnId;
  @Column(name = "GW_TXN_ID")
  private String gwTxnId;
  @Column(name = "GW_BATCH_ID")
  private String gwBatchId;
  @Column(name = "SOURCE_ACCOUNT_NUMBER")
  private String sourceAccountNumber;
  @Column(name = "SOURCE_ACCOUNT_NAME")
  private String sourceAccountName;
  @Column(name = "DESTINATION_ACCOUNT_NUMBER")
  private String destinationAccountNumber;
  @Column(name = "DESTINATION_ACCOUNT_NAME")
  private String destinationAccountName;
  @Column(name = "CITAD")
  private String citad;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "TRANSACTION_TYPE")
  private String transactionType;
  @Column(name = "DEBIT_AMOUNT")
  private BigDecimal debitAmount;
  @Column(name = "CREDIT_AMOUNT")
  private BigDecimal creditAmount;
  @Column(name = "CCY")
  private String ccy;
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CLIENT_BATCH_ID")
  private String clientBatchId;
  @Column(name = "CLIENT_ID")
  private String clientId;
  @Column(name = "TCB_TXN_ID")
  private String tcbTxnId;
  @Column(name = "INQUIRY_DESCRIPTION")
  private String inquiryDescription;
  @Column(name = "MAKER")
  private String maker;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "TRANSFER_RESPONSE_MSG")
  private String transferResponseMsg;
  @Column(name = "TRANSFER_TYPE")
  private String transferType;
  @Column(name = "CLIENT_REQUEST")
  private String clientRequest;
  @Column(name = "TRANSFER_SPEED")
  private String transferSpeed;
  @Column(name = "MARK")
  private String mark;
  @Column(name = "MQ")
  private String mq;
  @Column(name = "CLASSIFY")
  private String classify;
  @Column(name = "CODES")
  private String codes;

  public static HthTrans getFromClientAndTxnId(String clientId, String txnId) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTrans> query = h2hConnection.getSession().createQuery(
        "from HthTrans where clientId=:clientId and clientTxnId=:clientTxnId and mark='ACTIVE'",
        HthTrans.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("clientTxnId", txnId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
  /**
   * Author Lybtk
   */
  public static List<HthTrans> getFromClientAndStatus(String clientId, String status, String maker) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTrans> query = h2hConnection.getSession().createQuery(
        "from HthTrans where clientId=:clientId and status=:status and maker=:maker and mark='ACTIVE'",
        HthTrans.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("maker", maker)
        .setParameter("status", status);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }
  /**
   * Author Lybtk
   */
  public static List<HthTrans> getListFromBatchID(String clientId, String clientBatchId) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTrans> query = h2hConnection.getSession().createQuery(
        "from HthTrans where clientId=:clientId and clientBatchId=:clientBatchId and mark='ACTIVE' order by id desc",
        HthTrans.class
      );
      query.setParameter("clientId", clientId)
        .setParameter(CLIENT_BATCH_ID, clientBatchId);
      return query.getResultList();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static HthTrans getFromGwTxnId(String gwTxnId) {
    h2hConnection.getSession().clear();
    Query<HthTrans> query = h2hConnection.getSession().createQuery(
      "from HthTrans where gwTxnId=:gwTxnId",
      HthTrans.class
    );
    query.setParameter("gwTxnId", gwTxnId);
    return query.getSingleResult();
  }

  public static List<HthTrans> getListTransFromBatchId(String gwBatchId) {
    h2hConnection.getSession().clear();
    Query<HthTrans> query = h2hConnection.getSession().createQuery(
      "from HthTrans where gwBatchId=:gwBatchId",
      HthTrans.class
    );
    query.setParameter("gwBatchId", gwBatchId);
    return query.getResultList();
  }

  public static List<HthTrans> getListTransFromClientTxnId(String clientTxnId) {
    h2hConnection.getSession().clear();
    Query<HthTrans> query = h2hConnection.getSession().createQuery(
      "from HthTrans where clientTxnId=:clientTxnId",
      HthTrans.class
    );
    query.setParameter("clientTxnId", clientTxnId);
    return query.getResultList();
  }

  public static List<HthTrans> getListTransactionErrorByParam(String sourceNumber, String clientId, String clientBatchId,
                                                              String fromDate, String toDate) {
    Query<HthTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTrans where sourceAccountNumber=:sourceNumber and clientId=:clientId " +
        (StringUtils.isNotEmpty(clientBatchId) ? "and clientBatchId=:clientBatchId " : "") +
        (StringUtils.isNotEmpty(fromDate) ? "and transactionDate>=:fromDate " : "") +
        (StringUtils.isNotEmpty(toDate) ? "and transactionDate<=:toDate " : "") +
        " and (status like '%ERROR%' or status like '%INVALID%')" +
        " and mark='ACTIVE'" +
        " order by updatedTime desc, id desc"
      , HthTrans.class
    );
    query.setParameter("sourceNumber", sourceNumber);
    query.setParameter("clientId", clientId);
    if (StringUtils.isNotEmpty(clientBatchId)) {
      query.setParameter(CLIENT_BATCH_ID, clientBatchId);
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

  //bylybtk
  public static List<HthTrans> getTotalListTransactionByParam(String batchId, String txnId, String transactionType,
                                                              String customerType, String status, String bondCode, String couponPeriod,
                                                              String desAccountNo, String custodyCd, String fromDate, String toDate) {
    Query<HthTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTrans where  sourceAccountNumber ='13833336666016' " +
        (StringUtils.isNotEmpty(batchId) ? QUERY_CLIENT_BATCH_ID : "") +
        (StringUtils.isNotEmpty(txnId) ? QUERY_CLIENT_TXN_ID : "") +
        (StringUtils.isNotEmpty(transactionType) ? "and transactionType=:transactionType " : "") +
        (StringUtils.isNotEmpty(customerType) ? "and JSON_VALUE(clientRequest,'$.creditAccountInfo.customerType')=:customerType " : "") +
        (StringUtils.isNotEmpty(status) ? "and status=:status " : "") +
        (StringUtils.isNotEmpty(bondCode) ? "and JSON_VALUE(clientRequest, '$.attrs.bondCode') =:bondCode " : "") +
        (StringUtils.isNotEmpty(couponPeriod) ? "and JSON_VALUE(clientRequest, '$.attrs.couponPeriod') =:couponPeriod " : "") +
        (StringUtils.isNotEmpty(custodyCd) ? "and JSON_VALUE(clientRequest, '$.creditAccountInfo.custodyCd') =:custodyCd " : "") +
        (StringUtils.isNotEmpty(desAccountNo) ? "and destinationAccountNumber=:desAccountNo " : "") +
        (StringUtils.isNotEmpty(fromDate) ? "and transactionDate>=:fromDate " : "") +
        (StringUtils.isNotEmpty(toDate) ? "and transactionDate<=:toDate " : "") +
        "and MARK='ACTIVE'" +
        " order by updatedTime desc, id desc"
      , HthTrans.class
    );
    setParameter1(query, batchId, txnId, transactionType, customerType, status, bondCode,
      couponPeriod, desAccountNo, custodyCd, fromDate, toDate);
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  private static void setParameter1(Query<HthTrans> query, String batchId, String txnId, String transactionType,
                                    String customerType, String status, String bondCode, String couponPeriod,
                                    String desAccountNo, String custodyCd, String fromDate, String toDate) {
    if (StringUtils.isNotEmpty(batchId)) {
      query.setParameter(BATCH_ID, batchId);
    }
    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter(TXN_ID, txnId);
    }
    if (StringUtils.isNotEmpty(transactionType)) {
      query.setParameter("transactionType", transactionType);
    }
    if (StringUtils.isNotEmpty(customerType)) {
      query.setParameter("customerType", customerType);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(bondCode)) {
      query.setParameter("bondCode", bondCode);
    }
    if (StringUtils.isNotEmpty(couponPeriod)) {
      query.setParameter("couponPeriod", couponPeriod);
    }
    if (StringUtils.isNotEmpty(custodyCd)) {
      query.setParameter("custodyCd", custodyCd);
    }
    if (StringUtils.isNotEmpty(desAccountNo)) {
      query.setParameter(DES_ACCOUNT_NO, desAccountNo);
    }
    if (StringUtils.isNotEmpty(fromDate)) {
      query.setParameter("fromDate", Timestamp.valueOf(fromDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toDate)) {
      query.setParameter("toDate", Timestamp.valueOf(toDate + " 23:59:59"));
    }
  }

  //bylybtk
  public static List<HthTrans> getTotalListMoneyTransferSellToCustomers(String batchId, String txnId, String sourceAccountNumber,
                                                                        String status, String clientId, String desAccountNo, String custodyCode,
                                                                        String contractCode, String fromPaymentDate, String toPaymentDate) {
    Query<HthTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTrans where " + " MARK='ACTIVE' " +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? "and sourceAccountNumber =:sourceAccountNumber " : " ") +
        (StringUtils.isNotEmpty(batchId) ? QUERY_CLIENT_BATCH_ID : "") +
        (StringUtils.isNotEmpty(txnId) ? QUERY_CLIENT_TXN_ID : "") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(contractCode) ? "and JSON_VALUE(clientRequest, '$.attrs.contractCode') =:contractCode " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and JSON_VALUE(clientRequest, '$.creditAccountInfo.custodyCd') =:custodyCode " : "") +
        (StringUtils.isNotEmpty(desAccountNo) ? "and destinationAccountNumber =:desAccountNo " : "") +
        (StringUtils.isNotEmpty(fromPaymentDate) ? "and transactionDate >=:fromPaymentDate " : "") +
        (StringUtils.isNotEmpty(toPaymentDate) ? "and transactionDate <=:toPaymentDate " : "")
      , HthTrans.class
    );
    setParameter2(query, batchId, txnId, sourceAccountNumber, status, clientId,
      desAccountNo, custodyCode, contractCode, fromPaymentDate, toPaymentDate);
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public static Long getTotalInquiryTransactionForBondOTC(String batchId, String txnId, String sourceAccountNumber,
                                                          String status, String clientId, String desAccountNo, String custodyCode,
                                                          String contractCode, String fromPaymentDate, String toPaymentDate) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from HthTrans where mark='ACTIVE' " +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? "and sourceAccountNumber =:sourceAccountNumber " : " ") +
        (StringUtils.isNotEmpty(batchId) ? QUERY_CLIENT_BATCH_ID : "") +
        (StringUtils.isNotEmpty(txnId) ? QUERY_CLIENT_TXN_ID : "") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(contractCode) ? "and JSON_VALUE(clientRequest, '$.attrs.contractCode') =:contractCode " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and JSON_VALUE(clientRequest, '$.creditAccountInfo.custodyCd') =:custodyCode " : "") +
        (StringUtils.isNotEmpty(desAccountNo) ? "and destinationAccountNumber =:desAccountNo " : "") +
        (StringUtils.isNotEmpty(fromPaymentDate) ? "and transactionDate >=:fromPaymentDate " : "") +
        (StringUtils.isNotEmpty(toPaymentDate) ? "and transactionDate <=:toPaymentDate " : "")

    );
    setParameter3(query, batchId, txnId, sourceAccountNumber, status, clientId,
      desAccountNo, custodyCode, contractCode, fromPaymentDate, toPaymentDate);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  private static void setParameter2(Query<HthTrans> query, String batchId, String txnId, String sourceAccountNumber,
                                    String status, String clientId, String desAccountNo, String custodyCode,
                                    String contractCode, String fromPaymentDate, String toPaymentDate) {
    if (StringUtils.isNotEmpty(batchId)) {
      query.setParameter(BATCH_ID, batchId);
    }
    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter(TXN_ID, txnId);
    }
    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(contractCode)) {
      query.setParameter("contractCode", contractCode);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(desAccountNo)) {
      query.setParameter(DES_ACCOUNT_NO, desAccountNo);
    }
    if (StringUtils.isNotEmpty(fromPaymentDate)) {
      query.setParameter("fromPaymentDate", Timestamp.valueOf(fromPaymentDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toPaymentDate)) {
      query.setParameter("toPaymentDate", Timestamp.valueOf(toPaymentDate + " 23:59:59"));
    }
  }

  private static void setParameter3(Query<Long> query, String batchId, String txnId, String sourceAccountNumber,
                                    String status, String clientId, String desAccountNo, String custodyCode,
                                    String contractCode, String fromPaymentDate, String toPaymentDate) {
    if (StringUtils.isNotEmpty(batchId)) {
      query.setParameter(BATCH_ID, batchId);
    }
    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter(TXN_ID, txnId);
    }
    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(contractCode)) {
      query.setParameter("contractCode", contractCode);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(desAccountNo)) {
      query.setParameter(DES_ACCOUNT_NO, desAccountNo);
    }
    if (StringUtils.isNotEmpty(fromPaymentDate)) {
      query.setParameter("fromPaymentDate", Timestamp.valueOf(fromPaymentDate + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(toPaymentDate)) {
      query.setParameter("toPaymentDate", Timestamp.valueOf(toPaymentDate + " 23:59:59"));
    }
  }

  //bylybtk
  public static List<HthTrans> getListTransactionByParam(String sourceNumber, String clientId, String clientBatchId,
                                                         String fromDate, String toDate, String status) {
    Query<HthTrans> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthTrans where sourceAccountNumber=:sourceNumber " +
        (StringUtils.isNotEmpty(clientId) ? "and clientId=:clientId " : "") +
        (StringUtils.isNotEmpty(clientBatchId) ? "and clientBatchId=:clientBatchId " : "") +
        (StringUtils.isNotEmpty(fromDate) ? "and transactionDate>=:fromDate " : "") +
        (StringUtils.isNotEmpty(toDate) ? "and transactionDate<=:toDate " : "") +
        (StringUtils.isNotEmpty(status) ? " and status=:status " : "") +
        " and mark='ACTIVE'" +
        " order by updatedTime desc, id desc"
      , HthTrans.class
    );
    query.setParameter("sourceNumber", sourceNumber);
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(clientBatchId)) {
      query.setParameter(CLIENT_BATCH_ID, clientBatchId);
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

  //bylybtk
  public static void updateTransactionStatus(String clientId, String clientTxnId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update HthTrans set status=:status where clientId=:clientId and clientTxnId=:clientTxnId");
    query.setParameter("status", status);
    query.setParameter("clientTxnId", clientTxnId);
    query.setParameter("clientId", clientId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  //by lybtk: count transaction
  public static Long selectTransactionSummary(String transactionDate) {
    Query query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from HthTrans where clientId= 'iBondHold' and  mark='ACTIVE' " +
        (StringUtils.isNotEmpty(transactionDate) ? "and transactionDate >=:from and transactionDate <= :to " : ""));
    if (StringUtils.isNotEmpty(transactionDate)) {
      query.setParameter("from", Timestamp.valueOf(transactionDate + " 00:00:00"));
      query.setParameter("to", Timestamp.valueOf(transactionDate + " 23:59:59"));
    }
    return (Long) query.uniqueResult();
  }

  @Step("get list txn by clientTxnId")
  public static List<HashMap<String, Object>> getByListTxn(List<String> tnxIds) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT *   ");
    queryStringBuilder.append("FROM H2H_trans  ");
    queryStringBuilder.append("WHERE CLIENT_TXN_ID IN :tnxIds  ");
    try {
      return HthDb.h2hConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tnxIds", tnxIds)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get list txn by clientTxnId")
  public static List<HashMap<String, Object>> getByListTxnSuccess(List<String> tnxIds) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT *   ");
    queryStringBuilder.append("FROM H2H_trans  ");
    queryStringBuilder.append("WHERE CLIENT_TXN_ID IN :tnxIds AND STATUS NOT IN ('INVALID', 'ERROR')");
    try {
      return HthDb.h2hConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tnxIds", tnxIds)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get bank info ")
  public static List<HashMap<String, Object>> getBankInfoByBankSys(String bankSys) {
    Session session = h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT * FROM H2H_BANK_INFO   ");
    queryStringBuilder.append("WHERE BANK_CODE = :bankSys ");
    try {
      return HthDb.h2hConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("bankSys", bankSys)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("delete data by object")
  public static void deleteData(String tnxId) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(" DELETE from H2H_TRANS " +
      " where CLIENT_TXN_ID = ?");
    query.setParameter(1, tnxId);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("insert data")
  public static void insertData(HthTrans entity) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery("INSERT INTO H2H_TRANS  " +
      "(ID, CLIENT_TXN_ID, GW_TXN_ID, GW_BATCH_ID, SOURCE_ACCOUNT_NUMBER, SOURCE_ACCOUNT_NAME, DESTINATION_ACCOUNT_NUMBER, DESTINATION_ACCOUNT_NAME, CITAD, " +
      "BANK_NAME, TRANSACTION_TYPE, DEBIT_AMOUNT, CREDIT_AMOUNT, CCY, TRANSACTION_DATE, STATUS, DESCRIPTION, CLIENT_BATCH_ID, CLIENT_ID, TCB_TXN_ID, " +
      "INQUIRY_DESCRIPTION, MAKER, UPDATED_TIME, CREATED_TIME, TRANSFER_RESPONSE_MSG, TRANSFER_TYPE, CLIENT_REQUEST, TRANSFER_SPEED, MARK, MQ, CLASSIFY, CODES) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, " +
      "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getClientTxnId());
    query.setParameter(3, entity.getGwTxnId());
    query.setParameter(4, entity.getGwBatchId());
    query.setParameter(5, entity.getSourceAccountNumber());
    query.setParameter(6, entity.getSourceAccountName());
    query.setParameter(7, entity.getDestinationAccountName());
    query.setParameter(8, entity.getDestinationAccountNumber());
    query.setParameter(9, entity.getCitad());
    query.setParameter(10, entity.getBankName());
    query.setParameter(11, entity.getTransactionType());
    query.setParameter(12, entity.getDebitAmount());
    query.setParameter(13, entity.getCreditAmount());
    query.setParameter(14, entity.getCcy());
    query.setParameter(15, entity.getTransactionDate());
    query.setParameter(16, entity.getStatus());
    query.setParameter(17, entity.getDescription());
    query.setParameter(18, entity.getClientBatchId());
    query.setParameter(19, entity.getClientId());
    query.setParameter(20, entity.getTcbTxnId());
    query.setParameter(21, entity.getInquiryDescription());
    query.setParameter(22, entity.getMaker());
    query.setParameter(23, entity.getUpdatedTime());
    query.setParameter(24, entity.getCreatedTime());
    query.setParameter(25, entity.getTransferResponseMsg());
    query.setParameter(26, entity.getTransferType());
    query.setParameter(27, entity.getClientRequest());
    query.setParameter(28, entity.getTransferSpeed());
    query.setParameter(29, entity.getMark());
    query.setParameter(30, entity.getMq());
    query.setParameter(31, entity.getClassify());
    query.setParameter(32, entity.getCodes());

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteH2HTransByDesAccountName(String desAccountName) {
    Session session = h2hConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createNativeQuery(" DELETE from H2H_TRANS " +
      " where DESTINATION_ACCOUNT_NAME = ?");
    query.setParameter(1, desAccountName);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  /****
   *** Author Thuynt53
   **/
  public static List<HthTrans> getListIsave(String sourceAccountNumber, String clientId, String clientBatchId,
                                            String status, String clientTxnId, String from, String to) {
    Query<HthTrans> query;
    query = h2hConnection.getSession().createQuery(

      "FROM HthTrans WHERE clientId =:clientId " +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? " and sourceAccountNumber =:sourceAccountNumber " : "") +
        (StringUtils.isNotEmpty(clientBatchId) ? " and clientBatchId =:clientBatchId " : "") +
        (StringUtils.isNotEmpty(status) ? " and status =:status " : "") +
        (StringUtils.isNotEmpty(clientTxnId) ? " and clientTxnId=:clientTxnId " : "") +
        (StringUtils.isNotEmpty(from) ? " and transactionDate >= :from " : "") +
        (StringUtils.isNotEmpty(to) ? " and transactionDate <= :to " : "") +
        " AND mark ='ACTIVE' ORDER BY createdTime ASC,clientTxnId ASC ",
      HthTrans.class
    );
    setParameter(query, sourceAccountNumber, clientId, clientBatchId, status, clientTxnId, from, to);
    return query.getResultList();
  }

  private static void setParameter(Query query, String sourceAccountNumber, String clientId, String clientBatchId,
                                   String status, String clientTxnId, String from, String to) {

    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(clientBatchId)) {
      query.setParameter("clientBatchId", clientBatchId);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(clientTxnId)) {
      query.setParameter("clientTxnId", clientTxnId);
    }
    if (StringUtils.isNotEmpty(from)) {
      query.setParameter("from", Timestamp.valueOf(from + " 00:00:00"));
    }
    if (StringUtils.isNotEmpty(to)) {
      query.setParameter("to", Timestamp.valueOf(to + " 23:59:59"));
    }
  }

  /**
   * Author Lybtk
   */
  public static void updateTransactionStatusForInquiryReconcile(String clientTxnId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update HthTrans set status=:status where clientTxnId=:clientTxnId");
    query.setParameter("status", status);
    query.setParameter("clientTxnId", clientTxnId);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

  public static HthTrans getClientIdandStatus(String clientId, String status) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTrans> query = h2hConnection.getSession().createQuery(
              " FROM HthTrans where clientId=:clientId and status=:status and mark='ACTIVE' ORDER BY createdTime DESC ",
              HthTrans.class
      );
      query.setParameter("clientId", clientId)
              .setParameter("status", status);
      query.setMaxResults(1).setFirstResult(0);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static HthTrans getId(BigDecimal id) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTrans> query = h2hConnection.getSession().createQuery(
              " FROM HthTrans where id=:id ORDER BY createdTime DESC ",
              HthTrans.class
      );
      query.setParameter("id", id);
      query.setMaxResults(1).setFirstResult(0);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  public static void insertTCBTxnId(BigDecimal id) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    javax.persistence.Query query = session.createQuery(
            " UPDATE HthTrans SET tcbTxnId = 'thuytest123' WHERE id=:id "
    );
    query.setParameter("id", id)
            .executeUpdate();
    session.getTransaction().commit();
  }
}
