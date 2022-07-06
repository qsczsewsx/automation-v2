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
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "ZERO_BALANCE_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ZeroBalanceTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "ORDER_SOURCE")
  private String orderSource;
  @Column(name = "VOLUME_ORDER")
  private String volumeOrder;
  @Column(name = "ISSUED_DATE")
  private Date issuedDate;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULEOPS_INFO")
  private String ruleopsInfo;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "IXU")
  private Long ixu;
  @Column(name = "TXINFO")
  private String txinfo;
  @Column(name = "CREATED_DATE")
  private Date createdDate = new Date();

  @Step
  public static void deleteZeroBalanceTransactionByTcbsId(String tcbsid) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from ZeroBalanceTransaction a where a.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsid);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ZeroBalanceTransaction> findZeroBalanceTransaction(String tcbsid) {
    ixuDbConnection.getSession().clear();
    Query<ZeroBalanceTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ZeroBalanceTransaction a where a.tcbsid=:tcbsid ");
    query.setParameter("tcbsid", tcbsid);
    List<ZeroBalanceTransaction> zeroBalanceTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return zeroBalanceTransactionList;
  }

  @Step
  public static List<ZeroBalanceTransaction> findZeroBalanceTransactionByOrderId(String orderId) {
    ixuDbConnection.getSession().clear();
    Query<ZeroBalanceTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ZeroBalanceTransaction a where a.orderId=:orderId order by a.id desc");
    query.setParameter("orderId", orderId);
    List<ZeroBalanceTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static void insertZeroBalanceTransaction(ZeroBalanceTransaction zeroBalanceTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(zeroBalanceTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();

  }
}
