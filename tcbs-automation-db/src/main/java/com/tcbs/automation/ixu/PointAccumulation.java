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
@Table(name = "POINT_ACCUMULATION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PointAccumulation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_TIME")
  private Timestamp lastUpdatedTime;
  @Column(name = "AWARD_TYPE")
  private String awardType;

  public PointAccumulation(String awardType, String campaignId, Double point) {
    this.awardType = awardType;
    this.campaignId = campaignId;
    this.point = point;
  }


  @Step
  public static void deleteByTcbsId(String tcbsId, String campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from PointAccumulation s where s.tcbsId =:tcbsId and s.campaignId=:campaignId");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", campaignId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsIdList(List<String> tcbsId, int campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from PointAccumulation s where s.tcbsId in :tcbsId and s.campaignId=:campaignId");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", String.valueOf(campaignId));
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<PointAccumulation> findPointAccumulationByTcbsIdAndCampaignId(List<String> tcbsId, int campaignId, String awardType) {
    Query<PointAccumulation> query = ixuDbConnection.getSession().createQuery(
      "from PointAccumulation a where a.tcbsId in :tcbsId and a.campaignId=:campaignId and a.awardType = :awardType ");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", String.valueOf(campaignId));
    query.setParameter("awardType", awardType);
    List<PointAccumulation> accumulationList = query.getResultList();
    ixuDbConnection.closeSession();
    return accumulationList;
  }

  @Step
  public static List<PointAccumulation> findPointAccumulationByTcbsIdAndCampaignId(String tcbsId, String campaignId) {
    Query<PointAccumulation> query = ixuDbConnection.getSession().createQuery(
      "from PointAccumulation a where a.tcbsId=:tcbsId and a.campaignId=:campaignId ");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", campaignId);
    List<PointAccumulation> accumulationList = query.getResultList();
    ixuDbConnection.closeSession();
    return accumulationList;
  }

  @Step
  public static void deletePointAccumulationByListTcbsIdAndCampaignId(List<String> tcbsId, String campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from PointAccumulation s where s.tcbsId in :tcbsId and s.campaignId=:campaignId");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("campaignId", campaignId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
