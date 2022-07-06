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
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "LIMIT_CAMPAIGN_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LimitCampaignTransaction {

  private static final String ISSUE_DATE = "issueDate";
  private static final String CAMPAIGN_ID = "campaignId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "BASE_REDEEMABLE")
  private Double baseRedeemable;
  @Column(name = "QUOTA_LIMIT_REDEEMABLE")
  private Double quotaLimitRedeemable;
  @Column(name = "QUOTA_WARNING_REDEEMABLE")
  private Double quotaWarningRedeemable;
  @Column(name = "POINT_REDEEMABLE")
  private Double pointRedeemable;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "BASE_RANKING")
  private Double baseRanking;
  @Column(name = "QUOTA_LIMIT_RANKING")
  private Double quotaLimitRanking;
  @Column(name = "QUOTA_WARNING_RANKING")
  private Double quotaWarningRanking;
  @Column(name = "POINT_RANKING")
  private Double pointRanking;
  @Column(name = "TOTAL_POINT_REDEEMABLE")
  private Double totalPointRedeemable;
  @Column(name = "TOTAL_POINT_RANKING")
  private Double totalPointRanking;

  @Step
  public static void insertLimitCampaignTransaction(LimitCampaignTransaction limitCampaignTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(limitCampaignTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteLimitCampaignTransaction(String date) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete LimitCampaignTransaction a where trunc(a.issuedDate) = to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter(ISSUE_DATE, date);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteLimitCampaignTransactionWithDateAndCampaignId(String date, Long campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete LimitCampaignTransaction a where trunc(a.issuedDate) = to_date( :issueDate, 'yyyy-MM-dd') and a.campaignId = :campaignId");
    query.setParameter(ISSUE_DATE, date);
    query.setParameter(CAMPAIGN_ID, campaignId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<LimitCampaignTransaction> findLimitCampaignTransactionByCampaignIdAndIssueDate(Long campaignId, String date) {
    ixuDbConnection.getSession().clear();
    Query<LimitCampaignTransaction> query = ixuDbConnection.getSession().createQuery(
      "from LimitCampaignTransaction a where a.campaignId=:campaignId and trunc(a.issuedDate) = to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter(CAMPAIGN_ID, campaignId);
    query.setParameter(ISSUE_DATE, date);
    List<LimitCampaignTransaction> limitCampaignTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitCampaignTransactionList;
  }

  @Step
  public static LimitCampaignTransaction getLimitCampaignTransactionByCampaignIdAndIssueDate(Long campaignId, String date) {
    ixuDbConnection.getSession().clear();
    Query<LimitCampaignTransaction> query = ixuDbConnection.getSession().createQuery(
      "from LimitCampaignTransaction a where a.campaignId=:campaignId and trunc(a.issuedDate) = to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter(CAMPAIGN_ID, campaignId);
    query.setParameter(ISSUE_DATE, date);
    List<LimitCampaignTransaction> rs = query.getResultList();
    if (rs != null && rs.size() > 0) {
      ixuDbConnection.closeSession();
      return rs.get(0);
    }
    ixuDbConnection.closeSession();
    return null;
  }

  @Step
  public static List<LimitCampaignTransaction> findLimitCampaignTransactionByIssueDate(String date) {
    ixuDbConnection.getSession().clear();
    Query<LimitCampaignTransaction> query = ixuDbConnection.getSession().createQuery(
      "from LimitCampaignTransaction a where trunc(a.issuedDate) = to_date( :issueDate, 'yyyy-MM-dd')  ");
    query.setParameter(ISSUE_DATE, date);
    List<LimitCampaignTransaction> limitCampaignTransactions = query.getResultList();
    ixuDbConnection.closeSession();
    return limitCampaignTransactions;
  }

  @Step
  public static List<LimitCampaignTransaction> search(HashMap<String, Object> params) {
    ixuDbConnection.getSession().clear();
    String[] stringParams = new String[] {"campaign_id", "status", "time_from", "time_to", "page_index", "page_size"};
    String sqlStr = "select * from ( select tb.*, row_number() over (ORDER BY ISSUED_DATE desc ) line, count(tb.ID) over () total " +
      " from LIMIT_CAMPAIGN_TRANSACTION tb where s1 ) where line BETWEEN s2 and s3 order by line";
    Integer from = ((Integer) params.get(stringParams[4])) * ((Integer) params.get(stringParams[5])) + 1;
    Integer to = ((Integer) params.get(stringParams[4]) + 1) * ((Integer) params.get(stringParams[5]));
    String sqlSub = "";
    String and = " and ";
    if (params.get(stringParams[0]) != null && (Integer) params.get(stringParams[0]) > 0) {
      sqlSub += "CAMPAIGN_ID = " + params.get(stringParams[0]);
    }
    if (params.get(stringParams[1]) != null) {
      if (sqlSub.length() > 0) {
        sqlSub += and;
      }
      sqlSub += "STATUS = '" + params.get(stringParams[1]) + "'";
    }
    if (params.get(stringParams[2]) != null) {
      if (sqlSub.length() > 0) {
        sqlSub += and;
      }
      sqlSub += "TRUNC(ISSUED_DATE) >= TO_DATE('" + params.get(stringParams[2]) + "', 'YYYY-MM-DD')";
    }
    if (params.get(stringParams[3]) != null) {
      if (sqlSub.length() > 0) {
        sqlSub += and;
      }
      sqlSub += "TRUNC(ISSUED_DATE) <= TO_DATE('" + params.get(stringParams[3]) + "', 'YYYY-MM-DD')";
    }
    sqlStr = sqlStr.replace("s1", sqlSub);
    sqlStr = sqlStr.replace("s2", from.toString());
    sqlStr = sqlStr.replace("s3", to.toString());
    Query<LimitCampaignTransaction> query = ixuDbConnection.getSession().createNativeQuery(sqlStr, LimitCampaignTransaction.class);
    List<LimitCampaignTransaction> rs = query.getResultList();
    ixuDbConnection.closeSession();
    return rs;
  }
}
