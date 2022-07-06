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
@Table(name = "rb_campaign")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CampaignEntity {

  private static final String CATEGORY = "category";
  private static final String CAMPAIGN_ID = "campaignId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CAMPAIGNNAME")
  private String campaignName;

  @Column(name = "RULEAPPCODE")
  private String ruleAppCode;

  @Column(name = "RULEOPSCODE")
  private String ruleOPSCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CAMPAIGNMODE")
  private String campaignMode;

  @Column(name = "CREATEDDATE")
  private Timestamp createdDate;

  @Column(name = "CAMPAIGNID")
  private Long campaignId;

  @Column(name = "TEMPLATECODE")
  private String templateCode;

  @Column(name = "DELAY_TIME")
  private Long delayTime;

  @Column(name = "QUOTA_LIMIT")
  private String quotaLimit;

  @Column(name = "END_OF_DAY_PROCESSING")
  private String endOfDayProcessing;

  @Column(name = "REFUND_CALCULATED_POINT")
  private String refundCalculatedPoint;

  @Column(name = "PRIORITY")
  private Long priority;

  @Column(name = "CATEGORY")
  private String category;

  @Column(name = "STATUS")
  private String status;


  @Step
  public static List<CampaignEntity> getAllCampaign() {
    Query<CampaignEntity> query = ixuDbConnection.getSession().createQuery(
      "from CampaignEntity", CampaignEntity.class);
    List<CampaignEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<CampaignEntity> getCampaignWithTemplateCode(Long campaignId) {
    Query<CampaignEntity> query = ixuDbConnection.getSession().createQuery(
      "from CampaignEntity where campaignId =:campaignId", CampaignEntity.class);
    query.setParameter(CAMPAIGN_ID, campaignId);
    List<CampaignEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<CampaignEntity> getAllCampaignWithGroupIsNotNull() {
    Query<CampaignEntity> query = ixuDbConnection.getSession().createNativeQuery("select * from rb_campaign", CampaignEntity.class);
    List<CampaignEntity> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static List<CampaignEntity> getCampaignWithTemplateCode(List<Long> campaignId) {
    Query<CampaignEntity> query = ixuDbConnection.getSession().createQuery(
      "from CampaignEntity where campaignId IN (:campaignId)", CampaignEntity.class);
    query.setParameter(CAMPAIGN_ID, campaignId);
    List<CampaignEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void deleteByRuleOPSCode(String ruleOPSCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from CampaignEntity a where a.ruleOPSCode=:ruleOPSCode");
    query.setParameter("ruleOPSCode", ruleOPSCode);
    int number = query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByCampaignId(Long campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from CampaignEntity a where a.campaignId=:campaignId");
    query.setParameter(CAMPAIGN_ID, campaignId);
    int number = query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(CampaignEntity entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(entity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void saveOrUpdate(CampaignEntity entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGroupExclude(String category, Long campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update CampaignEntity a set a.category = null where a.category = :category and a.campaignId <> :campaignId");
    query.setParameter(CAMPAIGN_ID, campaignId);
    query.setParameter(CATEGORY, category);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void reUpdateGroupExclude(String category, List<Long> campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update CampaignEntity a set a.category = :category where a.campaignId in :campaignId");
    query.setParameter(CAMPAIGN_ID, campaignId);
    query.setParameter(CATEGORY, category);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<CampaignEntity> getAllCampaignsWithGroupDifferSystem() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<CampaignEntity> query = ixuDbConnection.getSession().createNativeQuery("SELECT * FROM rb_campaign where \"GROUP\" != 'System' or \"GROUP\" is null order by CAMPAIGNNAME", CampaignEntity.class);
    List<CampaignEntity> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }
}
