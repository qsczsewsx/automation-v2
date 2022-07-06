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
@Table(name = "VINID_OPEN_ACCOUNT_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VinidOpenAccountTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "VINID_CODE")
  private String vinidCode;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static void insert(VinidOpenAccountTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from VinidOpenAccountTransaction a where a.tcbsId =:tcbsid");
    query.setParameter("tcbsid", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<VinidOpenAccountTransaction> find(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<VinidOpenAccountTransaction> query = ixuDbConnection.getSession().createQuery(
      "from VinidOpenAccountTransaction a where a.tcbsId = :tcbsid ");
    query.setParameter("tcbsid", tcbsId);
    List<VinidOpenAccountTransaction> vinidOpenAccountTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return vinidOpenAccountTransactionList;
  }

  @Step
  public static List<VinidOpenAccountTransaction> findVinidOpenAccountByOrderId(String orderId) {
    ixuDbConnection.getSession().clear();
    Query<VinidOpenAccountTransaction> query = ixuDbConnection.getSession().createQuery(
      "from VinidOpenAccountTransaction a where a.orderId = :orderId ");
    query.setParameter("orderId", orderId);
    List<VinidOpenAccountTransaction> vinidOpenAccountTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return vinidOpenAccountTransactionList;
  }
}
