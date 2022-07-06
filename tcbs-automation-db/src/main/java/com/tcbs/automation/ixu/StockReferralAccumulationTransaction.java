package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "STOCK_REFERRAL_ACCUMULATION_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StockReferralAccumulationTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "POINT")
  private BigDecimal point;

  @Column(name = "AWARD_TYPE")
  private String awardType;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CAMPAIGN_ID")
  private String campaignId;

  @Column(name = "REF_ID")
  private String refId;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockReferralAccumulationTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(StockReferralAccumulationTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<StockReferralAccumulationTransaction> findStockRefAccumulationTransactionByTcbsId(String tcbsId) {
    Query<StockReferralAccumulationTransaction> query = ixuDbConnection.getSession().createQuery(
      "from StockReferralAccumulationTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<StockReferralAccumulationTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static Long countTotalTransaction() {
    Session session = ixuDbConnection.getSession();
    Query query = session.createNativeQuery(
      "select count(*) as count from STOCK_REFERRAL_ACCUMULATION_TRANSACTION"
    );
    BigDecimal count = (BigDecimal) query.getResultList().get(0);
    ixuDbConnection.closeSession();
    return count.longValue();
  }

}
