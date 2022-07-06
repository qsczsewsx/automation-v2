package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "CAMPAIGN_CATEGORY_POINT_LIMIT")
public class CampaignCategoryPointLimit {
  private static final Logger LOGGER = LoggerFactory.getLogger(CampaignCategoryPointLimit.class);

  private static final String STATUS_TITLE = "status";
  private static final String SOURCE = "source";
  private static final String CAMPAIGN_ID_PROPERTY = "campaignId";
  private static final String LAST_UPDATE_TIME_FROM = "last_updated_time_from";
  private static final String LAST_UPDATE_TIME_TO = "last_updated_time_to";
  private static final String DESCRIPTION_OBJECT = "description";
  private static final String TCBS_ID = "tcbsId";
  private static final String CATEGORY = "category";
  private static final String ISSUED_DATE = "issuedDate";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CATEGORY")
  private String category;

  @Column(name = "MAX_POINT")
  private BigDecimal maxPoint;

  @Column(name = "REMAIN_POINT")
  private BigDecimal remainPoint;

  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastTimeUpdated;

  @Step
  public static CampaignCategoryPointLimit insert(CampaignCategoryPointLimit generalTransaction) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(generalTransaction);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return generalTransaction;
  }

  @Step
  public static CampaignCategoryPointLimit saveOrUpdate(
    CampaignCategoryPointLimit categoryPointLimit) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();

    CampaignCategoryPointLimit entity =
      session.get(CampaignCategoryPointLimit.class, categoryPointLimit.getId());
    entity.setMaxPoint(categoryPointLimit.getMaxPoint());
    entity.setRemainPoint(categoryPointLimit.getRemainPoint());
    entity.setCategory(categoryPointLimit.getCategory());

    session.saveOrUpdate(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return categoryPointLimit;
  }

  @Step
  public static List<CampaignCategoryPointLimit> findPointLimitByTcbsIdAndCategory(String tcbsId, String category, Timestamp issueDate) {
    ixuDbConnection.getSession().clear();
    Query<CampaignCategoryPointLimit> query = ixuDbConnection.getSession().createQuery(
      "from CampaignCategoryPointLimit a where a.tcbsId = :tcbsId and a.category = :category and a.issuedDate = :issuedDate", CampaignCategoryPointLimit.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(ISSUED_DATE, issueDate);
    query.setParameter(CATEGORY, category);
    List<CampaignCategoryPointLimit> limitList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitList;
  }

  @Step
  public static List<CampaignCategoryPointLimit> findByTcbsIdAndCategoryFromDateToDate(String tcbsId, String category, String fromDate, String toDate) {
    ixuDbConnection.getSession().clear();
    Query<CampaignCategoryPointLimit> query =
      ixuDbConnection
        .getSession()
        .createQuery(
          "from CampaignCategoryPointLimit a where a.tcbsId = :tcbsId and a.category = :category " +
            "and trunc(a.issuedDate) >= to_date(:fromDate, 'yyyy-mm-dd') and trunc(a.issuedDate) <= to_date(:toDate, 'yyyy-mm-dd')",
          CampaignCategoryPointLimit.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(CATEGORY, category);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    List<CampaignCategoryPointLimit> limitList = query.getResultList();
    ixuDbConnection.closeSession();
    return limitList;
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query =
      ixuDbConnection
        .getSession()
        .createQuery("delete from CampaignCategoryPointLimit a where a.tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsIdAndCategoryAndIssuedDate(String tcbsId, String category, String issuedDate) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query =
      ixuDbConnection
        .getSession()
        .createQuery("delete from CampaignCategoryPointLimit a where a.tcbsId = :tcbsId " +
          "and a.category = :category and trunc(a.issuedDate) = TO_DATE(:issuedDate, 'YYYY-MM-DD')");
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(CATEGORY, category);
    query.setParameter(ISSUED_DATE, issuedDate);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static void deleteCategoryAndIssuedDate(String category, String issuedDate) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query =
      ixuDbConnection
        .getSession()
        .createQuery("delete from CampaignCategoryPointLimit a where a.category = :category and trunc(a.issuedDate) = TO_DATE(:issuedDate, 'YYYY-MM-DD')");
    query.setParameter(CATEGORY, category);
    query.setParameter(ISSUED_DATE, issuedDate);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static List<CampaignCategoryPointLimit> findPointLimitCategory(
    List<String> tcbsId, String category, String issueDate) {
    ixuDbConnection.getSession().clear();
    Query<CampaignCategoryPointLimit> query =
      ixuDbConnection
        .getSession()
        .createQuery(
          "from CampaignCategoryPointLimit a where a.category = :category " +
            "and trunc(a.issuedDate) = TO_DATE(:issuedDate, 'YYYY-MM-DD') and a.tcbsId in :tcbsId",
          CampaignCategoryPointLimit.class);
    query.setParameter(ISSUED_DATE, issueDate);
    query.setParameter(CATEGORY, category);
    query.setParameter(TCBS_ID, tcbsId);
    List<CampaignCategoryPointLimit> campaignCategoryPointLimits = query.getResultList();
    ixuDbConnection.closeSession();
    return campaignCategoryPointLimits;
  }
}
