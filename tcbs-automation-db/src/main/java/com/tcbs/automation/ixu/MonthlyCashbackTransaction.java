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
@Table(name = "MONTHLY_CASHBACK_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyCashbackTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "GROSS_VALUE")
  private Double grossValue;
  @Column(name = "TAX")
  private String tax;
  @Column(name = "PERIOD")
  private Timestamp period;
  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;
  @Column(name = "REF_CODE")
  private String refCode;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  private static final String TCBS_ID = "tcbsId";

  @Step
  public static List<MonthlyCashbackTransaction> getAllByTcbsIdAndCampaignId(String tcbsId, Long campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<MonthlyCashbackTransaction> query = ixuDbConnection.getSession().createQuery(
      "from MonthlyCashbackTransaction a where a.tcbsId =:tcbsId and a.campaignId =:campaignId order by a.id desc", MonthlyCashbackTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("campaignId", campaignId);
    List<MonthlyCashbackTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<MonthlyCashbackTransaction> getAllByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<MonthlyCashbackTransaction> query = ixuDbConnection.getSession().createQuery(
      "from MonthlyCashbackTransaction a where a.tcbsId =:tcbsId order by a.id desc", MonthlyCashbackTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<MonthlyCashbackTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<MonthlyCashbackTransaction> getAllByNullTcbsId() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<MonthlyCashbackTransaction> query = ixuDbConnection.getSession().createQuery(
      "from MonthlyCashbackTransaction a where a.tcbsId is null order by a.id desc", MonthlyCashbackTransaction.class);
    List<MonthlyCashbackTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from MonthlyCashbackTransaction a where a.tcbsId =:tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByRefCode(String refCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from MonthlyCashbackTransaction a where a.refCode =:refCode");
    query.setParameter("refCode", refCode);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static MonthlyCashbackTransaction insertOrSave(MonthlyCashbackTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }
}
