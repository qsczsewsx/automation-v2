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
@Table(name = "BOND_TRANSACTION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BondTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "ORDER_ID")
  private String orderId;

  @Column(name = "TRADING_ID")
  private String tradingId;

  @Column(name = "COUNTER_ID")
  private String counterId;

  @Column(name = "MATCHED_DATE")
  private Timestamp matchedDate;

  @Column(name = "MATCHED_AMOUNT")
  private BigDecimal matchedAmount;

  @Column(name = "CUSTODY_CODE")
  private String custodyCode;

  @Column(name = "HOLD_KEY")
  private String holdKey;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;

  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;

  @Column(name = "LOAN_KEY")
  private String loanKey;

  /**
   * Author Lybtk
   */
  public static BondTransaction getFromTradingId(String tradingId) {
    h2hConnection.getSession().clear();
    Query<BondTransaction> query = h2hConnection.getSession().createQuery(
      "from BondTransaction where tradingId=:tradingId",
      BondTransaction.class
    );
    query.setParameter("tradingId", tradingId);
    return query.getSingleResult();
  }

  public static void updateStatusByTradingId(String tradingId, String status) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update BondTransaction set status =:status where tradingId =:tradingId");
    query.setParameter("tradingId", tradingId);
    query.setParameter("status", status);

    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<BondTransaction> getLastIdFromType(String type) {
    h2hConnection.getSession().clear();
    Query<BondTransaction> query = h2hConnection.getSession().createQuery(
      "from BondTransaction where type=:type order by id desc",
      BondTransaction.class
    );
    query.setParameter("type", type);
    return query.getResultList();
  }

  /****
   *** Author Thuynt53
   **/
  public static List<BondTransaction> getListBondTransactionByHoldKey(String holdKey) {
    org.hibernate.query.Query<BondTransaction> query;
    query = h2hConnection.getSession().createQuery(
      "FROM BondTransaction WHERE holdKey =: holdKey ORDER BY id DESC ",
      BondTransaction.class
    );
    query.setParameter("holdKey", holdKey);
    System.out.println(query.getQueryString());
    return query.getResultList();
  }
}
