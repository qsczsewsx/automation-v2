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
@Table(name = "VINID_STOCK_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VinIdStockTransaction {

  private static final String TCBSID = "tcbsId";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "VINID_CODE")
  private String vinIdCode;
  @Column(name = "TOTAL_AMOUNT")
  private String totalAmount;
  @Column(name = "TOTAL_FEE")
  private String totalFee;
  @Column(name = "POINT")
  private String point;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from VinIdStockTransaction s where s.tcbsId =:tcbsId");
    query.setParameter(TCBSID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from VinIdStockTransaction s where s.tcbsId in :tcbsId");
    query.setParameter(TCBSID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createStockTransaction(VinIdStockTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<VinIdStockTransaction> findVinIdStockTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<VinIdStockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from VinIdStockTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter(TCBSID, tcbsId);
    List<VinIdStockTransaction> stockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockTransactionList;
  }
}
