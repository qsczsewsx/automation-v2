package com.tcbs.automation.ixu;

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

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "CAMPAIGN_TEMPLATE_CODE")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CampaignTemplateCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "TEMPLATE_CODE")
  private String templateCode;

  @Step
  public static void create(CampaignTemplateCode entity) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    session.save(entity);
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByCampaignId(Long campaignId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from CampaignTemplateCode a where a.campaignId=:campaignId");
    query.setParameter("campaignId", campaignId);
    query.executeUpdate();

    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByCampaignIdAndType(Long campaignId, String type) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from CampaignTemplateCode a where a.campaignId=:campaignId and a.type=:type");
    query.setParameter("campaignId", campaignId);
    query.setParameter("type", type);
    query.executeUpdate();

    transaction.commit();
    ixuDbConnection.closeSession();
  }

}
