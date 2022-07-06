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
@Table(name = "STOCK_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "POINT")
  private String point;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "MEMBERSHIP_TYPE")
  private String membershipType;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;


  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createStockTransaction(StockTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<StockTransaction> findStockTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<StockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<StockTransaction> stockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockTransactionList;
  }

  @Step
  public static List<StockTransaction> findStockTransactionByTcbsIdAndAwardType(String tcbsId, String awardType) {
    ixuDbConnection.getSession().clear();
    Query<StockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockTransaction a where a.tcbsId=:tcbsId and a.awardType=:awardType");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("awardType", awardType);
    List<StockTransaction> stockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockTransactionList;
  }

  @Step
  public static List<StockTransaction> findStockTransactionByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<StockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockTransaction a where a.tcbsId in :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<StockTransaction> stockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockTransactionList;
  }
}
