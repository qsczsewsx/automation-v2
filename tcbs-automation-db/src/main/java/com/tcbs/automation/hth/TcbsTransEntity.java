package com.tcbs.automation.hth;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "TCBS_TRANS")
public class TcbsTransEntity {
  private int id;
  private String transactionId;
  private String batchId;
  private String clientId;
  private String userName;
  private String sourceAccountNumber;
  private String destinationAccountNumber;
  private long amount;
  private String description;
  private Timestamp transactionDate;
  private String type;
  private String status;
  private Timestamp createdTime;
  private Timestamp updatedTime;

  /**
   * Author Lybtk
   */
  public static Long getTotalInquiryTransactionForFlowExtenalFutureAndCapitalMarket(String txnId, String clientId,
                                                                                    String custodyCode, String sourceAccountNumber, String destinationAccountNumber, String status,
                                                                                    String from, String to) {
    Query<Long> query = HthDb.h2hConnection.getSession().createQuery(
      "select count (*) from TcbsTransEntity where transactionId!=null " +
        (StringUtils.isNotEmpty(txnId) ? "and transactionId =:txnId " : " ") +
        (StringUtils.isNotEmpty(clientId) ? "and clientId =:clientId " : "") +
        (StringUtils.isNotEmpty(custodyCode) ? "and userName =:custodyCode " : "") +
        (StringUtils.isNotEmpty(sourceAccountNumber) ? "and sourceAccountNumber =:sourceAccountNumber " : "") +
        (StringUtils.isNotEmpty(status) ? "and status =:status " : "") +
        (StringUtils.isNotEmpty(destinationAccountNumber) ? "and destinationAccountNumber =:destinationAccountNumber " : "") +
        (StringUtils.isNotEmpty(from) ? "and transactionDate >=:from " : "") +
        (StringUtils.isNotEmpty(to) ? "and transactionDate <=:to " : "")
    );
    setParameter(query, txnId, clientId, custodyCode, sourceAccountNumber, destinationAccountNumber, status,
      from, to);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static void setParameter(Query<Long> query, String txnId, String clientId, String custodyCode,
                                  String sourceAccountNumber, String destinationAccountNumber,
                                  String status, String from, String to) {

    if (StringUtils.isNotEmpty(txnId)) {
      query.setParameter("txnId", txnId);
    }
    if (StringUtils.isNotEmpty(clientId)) {
      query.setParameter("clientId", clientId);
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      query.setParameter("custodyCode", custodyCode);
    }
    if (StringUtils.isNotEmpty(sourceAccountNumber)) {
      query.setParameter("sourceAccountNumber", sourceAccountNumber);
    }
    if (StringUtils.isNotEmpty(destinationAccountNumber)) {
      query.setParameter("destinationAccountNumber", destinationAccountNumber);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
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
  public static TcbsTransEntity getFromTxnId(String clientId, String txnId) {
    try {
      h2hConnection.getSession().clear();
      Query<TcbsTransEntity> query = h2hConnection.getSession().createQuery(
        "from TcbsTransEntity where clientId=:clientId and transactionId=:transactionId",
        TcbsTransEntity.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("transactionId", txnId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "TRANSACTION_ID")
  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }

  @Basic
  @Column(name = "BATCH_ID")
  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  @Basic
  @Column(name = "CLIENT_ID")
  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  @Basic
  @Column(name = "USER_NAME")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Basic
  @Column(name = "SOURCE_ACCOUNT_NUMBER")
  public String getSourceAccountNumber() {
    return sourceAccountNumber;
  }

  public void setSourceAccountNumber(String sourceAccountNumber) {
    this.sourceAccountNumber = sourceAccountNumber;
  }

  @Basic
  @Column(name = "DESTINATION_ACCOUNT_NUMBER")
  public String getDestinationAccountNumber() {
    return destinationAccountNumber;
  }

  public void setDestinationAccountNumber(String destinationAccountNumber) {
    this.destinationAccountNumber = destinationAccountNumber;
  }

  @Basic
  @Column(name = "AMOUNT")
  public long getAmount() {
    return amount;
  }

  public void setAmount(long amount) {
    this.amount = amount;
  }

  @Basic
  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "TRANSACTION_DATE")
  public Timestamp getTransactionDate() {
    return transactionDate;
  }

  public void setTransactionDate(Timestamp transactionDate) {
    this.transactionDate = transactionDate;
  }

  @Basic
  @Column(name = "TYPE")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "CREATED_TIME")
  public Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Timestamp createdTime) {
    this.createdTime = createdTime;
  }

  @Basic
  @Column(name = "UPDATED_TIME")
  public Timestamp getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(Timestamp updatedTime) {
    this.updatedTime = updatedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TcbsTransEntity that = (TcbsTransEntity) o;
    return id == that.id &&
      amount == that.amount &&
      Objects.equals(transactionId, that.transactionId) &&
      Objects.equals(batchId, that.batchId) &&
      Objects.equals(clientId, that.clientId) &&
      Objects.equals(userName, that.userName) &&
      Objects.equals(sourceAccountNumber, that.sourceAccountNumber) &&
      Objects.equals(destinationAccountNumber, that.destinationAccountNumber) &&
      Objects.equals(description, that.description) &&
      Objects.equals(transactionDate, that.transactionDate) &&
      Objects.equals(type, that.type) &&
      Objects.equals(status, that.status) &&
      Objects.equals(createdTime, that.createdTime) &&
      Objects.equals(updatedTime, that.updatedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, transactionId, batchId, clientId, userName, sourceAccountNumber, destinationAccountNumber, amount, description, transactionDate, type, status, createdTime, updatedTime);
  }
}
