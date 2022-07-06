package com.tcbs.automation.ixu;

import com.google.common.collect.Maps;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.assertj.core.util.Lists;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;


@Entity
@Table(name = "IXU_TRANSACTION_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuTransactionHistory {

  public static final String REF_ID = "referenceId";
  public static final String TCBS_ID = "tcbsId";

  @Id
  @NotNull
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "ROLE")
  private String role;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "REFERENCE_LOCATION")
  private String referenceLocation;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "OUTSTANDING")
  private Double outstanding;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "ISSUED_DATE")
  private Date issuedDate;
  @Column(name = "EXPIRED_DATE")
  private Date expiredDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "HISTORY_KEY")
  private String historyKey;
  @Column(name = "CHECKSUM")
  private String checksum;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static IxuTransactionHistory insert(IxuTransactionHistory historyIxu) {
    Session session = ixuDbConnection.getSession();
    session.save(historyIxu);
    ixuDbConnection.closeSession();
    return historyIxu;
  }

  @Step
  public static IxuTransactionHistory getTransactionHistoryByHistoryKey(String historyKey) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.historyKey=:HISTORY_KEY ",
      IxuTransactionHistory.class);
    query.setParameter("HISTORY_KEY", historyKey);
    List<IxuTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static IxuTransactionHistory getTransactionHistoryById(String id) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.id=:id ",
      IxuTransactionHistory.class);
    query.setParameter("id", Long.valueOf(id));
    List<IxuTransactionHistory> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result.get(0);
  }

  @Step
  public static IxuTransactionHistory getTransactionHistoryByTcbsIdAndAction(String tcbsId, String action) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.tcbsid=:tcbsId and a.action=:action order by a.id desc",
      IxuTransactionHistory.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("action", action);
    List<IxuTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void updateTransactionHistory(String expiredDate, Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update IxuTransactionHistory a set a.expiredDate = to_date(:expiredDate, 'yyyy-MM-dd') where a.id=:id");
    query.setParameter("expiredDate", expiredDate);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IxuTransactionHistory> getTransactionHistoryByExpiredDateAndAction(String expiredDate, String action) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.expiredDate <= to_date(:expiredDate, 'yyyy-MM-dd') and a.action=:action order by a.id desc",
      IxuTransactionHistory.class);
    query.setParameter("expiredDate", expiredDate);
    query.setParameter("action", action);
    List<IxuTransactionHistory> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static List<IxuTransactionHistory> getTransactionHistoryByRefIdAndActionAndRefLocation(String refId, String action, String refLoc) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.referenceId=:referenceId and a.action =:action and a.referenceLocation =:referenceLocation order by a.id desc",
      IxuTransactionHistory.class);
    query.setParameter(REF_ID, refId);
    query.setParameter("action", action);
    query.setParameter("referenceLocation", refLoc);
    List<IxuTransactionHistory> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }


  @Step
  public static List<IxuTransactionHistory> getTransactionHistoryPagination(int page, int size, String awardType, String tcbsid) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.tcbsid=:tcbsId and a.awardType=:AWARD_TYPE order by a.createdDate desc, a.id desc",
      IxuTransactionHistory.class);

    query.setParameter("AWARD_TYPE", awardType);
    query.setParameter(TCBS_ID, tcbsid);
    query.setFirstResult((page - 1) * size);
    query.setMaxResults(size);
    List<IxuTransactionHistory> result = query.list();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static void clearHistory(String tcbsId) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuTransactionHistory his where his.tcbsid=:tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearHistoryByReferenceIdAndReferenceLocation(String referenceId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuTransactionHistory his where his.referenceId =:referenceId and his.referenceLocation =:referenceLocation");
    query.setParameter(REF_ID, referenceId);
    query.setParameter("referenceLocation", referenceLocation);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearHistoryByHistoryKey(String historyKey) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuTransactionHistory his where his.historyKey=:historyKey");
    query.setParameter("historyKey", historyKey);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertIxuTransactionHistory(IxuTransactionHistory entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void saveOrUpdateIxuTransactionHistory(IxuTransactionHistory entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static List<Map<String, Object>> getListSumaryCampaignByTcbsIdAndAction(String tcbsId,
                                                                                 String action) {

    String queryStrings = "select rc.CAMPAIGNNAME as CAMPAIGN_NAME, rs.TOTAL_POINT, rs.NUMBER_RECORD\n" +
      "  from (select ith.CAMPAIGN_ID CAMPAIGN_ID, count(*) as NUMBER_RECORD, sum(POINT) TOTAL_POINT\n" +
      "  from IXU_TRANSACTION_HISTORY ith\n" +
      "  where ith.TCBSID = :tcbsId\n" +
      "  and ith.ACTION = :action and ith.AWARD_TYPE = 'Redeemable'\n" +
      "  group by ith.CAMPAIGN_ID\n" +
      "  order by TOTAL_POINT desc) rs\n" +
      "  inner join RB_CAMPAIGN rc\n" +
      "  on rs.CAMPAIGN_ID = rc.CAMPAIGNID";
    NativeQuery query = ixuDbConnection.getSession().createSQLQuery(queryStrings);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("action", action);
    List<Object[]> results = query.getResultList();

    String[] responseField = {"campaignName", "pointSum", "transactionCount"};
    List<Map<String, Object>> response = Lists.newArrayList();

    if (!results.isEmpty()) {
      results.forEach(result -> {
        Map<String, Object> map = Maps.newHashMap();
        for (int i = 0; i < responseField.length; i++) {
          map.put(responseField[i], result[i]);
        }
        response.add(map);
      });
    }
    ixuDbConnection.closeSession();
    return response;
  }

  public static void clearGeneralLedgerHistory(String referralLocation, List<String> referenceId, String action, List<String> source) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery("delete from IxuTransactionHistory a where " +
      "a.referenceLocation = :referralLocation and " +
      "a.action = :action and a.referenceId in :referenceId and a.source in :source");
    query.setParameter("referralLocation", referralLocation);
    query.setParameter("action", action);
    query.setParameter(REF_ID, referenceId);
    query.setParameter("source", source);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static void updateOrSave(IxuTransactionHistory ixuTransactionHistory) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.saveOrUpdate(ixuTransactionHistory);
    session.getTransaction().commit();
  }

  @Step
  public static List<IxuTransactionHistory> getTransactionHistoryByTcbsIdAndActionAndAwardType(String tcbsId, String action, String awardType) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.tcbsid=:tcbsId and a.action=:action and a.awardType=:awardType order by a.id desc",
      IxuTransactionHistory.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("action", action);
    query.setParameter("awardType", awardType);
    List<IxuTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<IxuTransactionHistory> getTransactionHistoryByTcbsId(String tcbsId) {
    Query<IxuTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from IxuTransactionHistory a where a.tcbsid=:tcbsId order by a.id desc",
      IxuTransactionHistory.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<IxuTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }
}
