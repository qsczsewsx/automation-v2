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

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "H2H_TXN_REVIEW")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HthTxnReview {

  @Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_CODE")
  private String userCode;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "CLIENT_TXN_ID")
  private String clientTxnId;
  @Column(name = "CLIENT_ID")
  private String clientId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "BUSINESS_TYPE")
  private String businessType;
  @Column(name = "NOTE")
  private String note;

  /**
   * Author Lybtk
   */
  public static HthTxnReview getFromClientAndTxnId(String clientId, String txnId) {
    try {
      h2hConnection.getSession().clear();
      Query<HthTxnReview> query = h2hConnection.getSession().createQuery(
        "from HthTxnReview where clientId=:clientId and clientTxnId=:clientTxnId",
        HthTxnReview.class
      );
      query.setParameter("clientId", clientId)
        .setParameter("clientTxnId", txnId);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
