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
@Table(name = "IXU_IFUND_TRANSACTIONS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuIfundTransactions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "AMOUNT")
  private Double amount;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "ORDER_CODE")
  private String orderCode;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "RULE_OPS")
  private String ruleOps;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "MEMBERSHIPTYPE")
  private String membershiptype;
  @Column(name = "FUND_CODE")
  private String fundCode;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;

  @Step
  public static void deleteIfundTransactionByOrderId(String orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuIfundTransactions a where a.orderId=:orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteIfundTransactionByTcbsId(String tcbsid) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuIfundTransactions a where a.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsid);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static List<IxuIfundTransactions> findIfundTransaction(String orderId, String awardType) {
    ixuDbConnection.getSession().clear();
    Query<IxuIfundTransactions> query = ixuDbConnection.getSession().createQuery(
      "from IxuIfundTransactions a where a.orderId=:orderId and a.awardType=:awardType");
    query.setParameter("orderId", orderId);
    query.setParameter("awardType", awardType);
    List<IxuIfundTransactions> ixuIfundTransactionsList = query.getResultList();
    ixuDbConnection.closeSession();
    return ixuIfundTransactionsList;
  }

  @Step
  public static void insertIxuIfundTransactions(IxuIfundTransactions ixuIfundTransactions) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(ixuIfundTransactions);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

}
