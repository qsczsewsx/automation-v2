package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "TCBF_REFUND_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TcbfRefundTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "ORDER_CODE")
  private String orderCode;
  @Column(name = "MATCHED_DATE")
  private String matchedDate;
  @Column(name = "TRANSACTION_VALUE")
  private Double transactionValue;
  @Column(name = "VIP_TYPE")
  private String vipType;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_TIME")
  private Timestamp lastUpdatedTime;

  @Step
  public static void deleteByOrderCode(List<String> orderCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from TcbfRefundTransaction s where s.orderCode in :orderCode");
    query.setParameter("orderCode", orderCode);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<TcbfRefundTransaction> findTcbfRefundTransactionByOrderCode(String orderCode) {
    Query<TcbfRefundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TcbfRefundTransaction a where a.orderCode=:orderCode order by a.id desc ");
    query.setParameter("orderCode", orderCode);
    List<TcbfRefundTransaction> tcbfRefundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return tcbfRefundTransactionList;
  }

  @Step
  public static List<TcbfRefundTransaction> findAllTcbfRefundTransaction() {
    Query<TcbfRefundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TcbfRefundTransaction a where a.id is not null ");
    List<TcbfRefundTransaction> tcbfRefundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return tcbfRefundTransactionList;
  }
}
