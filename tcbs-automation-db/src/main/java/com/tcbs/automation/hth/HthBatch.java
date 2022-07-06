package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "H2H_BATCH")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HthBatch {

  @Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CLIENT_BATCH_ID")
  private String clientBatchId;
  @Column(name = "GW_BATCH_ID")
  private String gwBatchId;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "NUM_TXN")
  private BigDecimal numTxn;
  @Column(name = "TOTAL_AMOUNT")
  private BigDecimal totalAmount;
  @Column(name = "CLIENT_ID")
  private String clientId;
  @Column(name = "DESCRIPTION")
  private String description;
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
  @Column(name = "PARENT_ID")
  private String parentId;
  @Column(name = "CLIENT_REQUEST")
  private String clientRequest;
//  @Column(name = "RESPONSE_NO")
//  private String responseNo;
//  @Column(name = "RETRY_NO")
//  private String retryNo;
//  @Column(name = "CLASSIFY")
//  private String classify;

  public static List<HthBatch> getListBatchFromClientBatchId(String clientBatchId) {
    Query<HthBatch> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthBatch where clientBatchId=:clientBatchId",
      HthBatch.class
    );
    return query.setParameter("clientBatchId", clientBatchId)
      .getResultList();
  }

  public static HthBatch getBatchFromGwBatchId(String gwBatchId) {
    Query<HthBatch> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthBatch where gwBatchId=:gwBatchId",
      HthBatch.class
    );
    return query.setParameter("gwBatchId", gwBatchId)
      .getSingleResult();
  }

  public static HthBatch getBatchFromClassify(String classify) {
    Query<HthBatch> query = HthDb.h2hConnection.getSession().createQuery(
      "from HthBatch where classify=:classify",
      HthBatch.class
    );
    try {
      return query.setParameter("classify", classify)
        .getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }
}
