package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
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

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "LIMIT_TOTAL_AMOUNT")
public class LimitTotalAmount {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "ACCUM_TOTAL_AMOUNT")
  private Double accumTotalAmount;

  @Column(name = "MAX_AMOUNT")
  private Double maxAmount;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastTimeUpdated;

  @Step
  public static LimitTotalAmount insert(LimitTotalAmount limitTotalAmount) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(limitTotalAmount);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return limitTotalAmount;
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery("delete from LimitTotalAmount a where a.tcbsId = :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  public static LimitTotalAmount findLimitTotalAmountByTcbsIdAndCampaign(String tcbsId, Long campaignId) {
    Query<LimitTotalAmount> query = ixuDbConnection.getSession().createQuery(
      "from LimitTotalAmount where tcbsId = :tcbsId and campaignId = :campaignId", LimitTotalAmount.class
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", campaignId);
    LimitTotalAmount limitTotalAmountList = query.getSingleResult();
    ixuDbConnection.closeSession();
    return limitTotalAmountList;
  }
}
