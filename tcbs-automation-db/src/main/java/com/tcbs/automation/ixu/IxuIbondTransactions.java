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
@Table(name = "IXU_IBOND_TRANSACTIONS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuIbondTransactions {
  private static String orderIdProperty = "orderId";

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
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private String createdDate;
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
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "BOND_CODE")
  private String bondCode;


  @Step
  public static void deleteByOrderId(String orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuIbondTransactions a where a.orderId=:orderId");
    query.setParameter(orderIdProperty, orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String tcbsid) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuIbondTransactions a where a.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsid);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IxuIbondTransactions> findIBondTransaction(String orderId, String awardType) {
    Query<IxuIbondTransactions> query = ixuDbConnection.getSession().createQuery(
      "from IxuIbondTransactions a where a.orderId=:orderId and a.awardType=:awardType");
    query.setParameter(orderIdProperty, orderId);
    query.setParameter("awardType", awardType);
    List<IxuIbondTransactions> ixuIbondTransactions = query.getResultList();
    ixuDbConnection.closeSession();
    return ixuIbondTransactions;
  }

  @Step
  public static List<IxuIbondTransactions> findIBondTransaction(String orderId) {
    Query<IxuIbondTransactions> query = ixuDbConnection.getSession().createQuery(
      "from IxuIbondTransactions a where a.orderId=:orderId");
    query.setParameter(orderIdProperty, orderId);
    List<IxuIbondTransactions> ixuIbondTransactions = query.getResultList();
    ixuDbConnection.closeSession();
    return ixuIbondTransactions;
  }

  @Step
  public static void insertIxuIbondTransaction(IxuIbondTransactions ixuIbondTransactions) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(ixuIbondTransactions);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
