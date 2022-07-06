package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
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
import java.time.ZonedDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "RB_RULEOPS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RbRuleOpsEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "RULEOPSCODE")
  private String ruleOpsCode;

  @Column(name = "APPVERSION")
  private String appVersion;

  @Column(name = "OPSVERSION")
  private String opsVersion;

  @Column(name = "INPUTS")
  private String inputs;

  @Column(name = "STARTTIME")
  private ZonedDateTime startTime;

  @Column(name = "ENDTIME")
  private ZonedDateTime endTime;

  @Column(name = "CREATEDDATE")
  private ZonedDateTime createdDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CAMPAIGN_ID")
  private String campaignId;

  @Column(name = "MAINTABLE")
  private String mainTable;

  @Step
  public static List<RbRuleOpsEntity> findRbRuleOpsEntityByRuleOpsCode(String ruleOpsCode) {
    ixuDbConnection.getSession().clear();
    Query<RbRuleOpsEntity> query = ixuDbConnection.getSession().createQuery(
      "from RbRuleOpsEntity a where a.ruleOpsCode=:ruleOpsCode order by a.opsVersion desc");
    query.setParameter("ruleOpsCode", ruleOpsCode);
    List<RbRuleOpsEntity> rbRuleOpsEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return rbRuleOpsEntityList;
  }

  @Step
  public static void clearRuleOps(long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from RbRuleOpsEntity ro where ro.id=:id");
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearRuleOpsByRuleOpsCode(String ruleOpsCode) {
    ixuDbConnection.getSession().clear();

    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from RbRuleOpsEntity ro where ro.ruleOpsCode=:ruleOpsCode");
    query.setParameter("ruleOpsCode", ruleOpsCode);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertRuleOps(RbRuleOpsEntity ruleOpsEntity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(ruleOpsEntity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByCampaignId(long campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from RbRuleOpsEntity ro where ro.campaignId=:campaignId");
    query.setParameter("campaignId", String.valueOf(campaignId));
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
