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
@Table(name = "STOCK_VIP_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockVipTransaction {

  private static final String TCBSID = "tcbsId";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE105C")
  private String code105C;
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
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "VIP_TYPE")
  private String vipType;
  @Column(name = "VIP_DATE")
  private String vipDate;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockVipTransaction s where s.tcbsId =:tcbsId");
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
      "delete from StockVipTransaction s where s.tcbsId in :tcbsId");
    query.setParameter(TCBSID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createStockTransaction(StockVipTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<StockVipTransaction> findStockTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<StockVipTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockVipTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter(TCBSID, tcbsId);
    List<StockVipTransaction> stockVipTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockVipTransactionList;
  }
}
