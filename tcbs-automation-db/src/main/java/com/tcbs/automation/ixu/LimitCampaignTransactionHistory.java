package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "LIMIT_CAMPAIGN_TRANSACTION_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LimitCampaignTransactionHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "DOER")
  private String doer;

  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "LIMIT_CAMPAIGN_TRANSACTION_ID")
  private Long limitCampaignTransactionId;

  @Column(name = "ACTION")
  private String action;


  public static void insert(LimitCampaignTransactionHistory limitCampaignTransactionHistory) {
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(limitCampaignTransactionHistory);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static void delete(LimitCampaignTransactionHistory limitCampaignTransactionHistory) {
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().delete(limitCampaignTransactionHistory);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static List<LimitCampaignTransactionHistory> findLimitCampaignHistory(Long id) {
    Query query = ixuDbConnection.getSession().createQuery(
      "from LimitCampaignTransactionHistory where limitCampaignTransactionId = :id order by createdDate desc"
    );
    query.setParameter("id", id);
    List<LimitCampaignTransactionHistory> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

}
