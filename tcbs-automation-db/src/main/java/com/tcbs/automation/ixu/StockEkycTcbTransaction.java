package com.tcbs.automation.ixu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
@Table(name = "STOCK_EKYCTCB_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockEkycTcbTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @JsonProperty("id")
  private Long id;

  @JsonProperty(TCBS_ID)
  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CODE_105C")
  @JsonProperty("code105c")
  private String code105c;

  @Column(name = "POINT")
  @JsonProperty("point")
  private String point;

  @Column(name = "SUM_AMOUNT")
  private String sumAmount;

  @Column(name = "TOTAL_AMOUNT")
  private String totalAmount;

  @Column(name = "TOTAL_FEE")
  private String totalFee;

  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsIdAndIssueDate(List<String> tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockEkycTcbTransaction a where a.tcbsId in :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static StockEkycTcbTransaction findTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<StockEkycTcbTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockEkycTcbTransaction a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    StockEkycTcbTransaction stockEkycTcbTransaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    if (stockEkycTcbTransaction == null) {
      return null;
    }
    return stockEkycTcbTransaction;
  }

  @Step
  public static List<StockEkycTcbTransaction> findByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<StockEkycTcbTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockEkycTcbTransaction a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<StockEkycTcbTransaction> ekycTcbTransactions = query.getResultList();
    ixuDbConnection.closeSession();
    return ekycTcbTransactions;
  }

}