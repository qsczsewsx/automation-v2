package com.tcbs.automation.ixu;

import com.google.common.collect.Maps;
import com.tcbs.automation.SqlUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "GENERAL_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GeneralTransaction {
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneralTransaction.class);

  private static final String STATUS_TITLE = "status";
  private static final String SOURCE = "source";
  private static final String CAMPAIGN_ID_PROPERTY = "campaignId";
  private static final String LAST_UPDATE_TIME_FROM = "last_updated_time_from";
  private static final String LAST_UPDATE_TIME_TO = "last_updated_time_to";
  private static final String DESCRIPTION_OBJECT = "description";

  private static final String ACTION_COLUMN_NAME = "ACTION";
  private static final String GT_KEY = "gt";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "GL_ID")
  private String glId;
  @Column(name = "POINT")
  private BigDecimal point;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "REFERENCE_LOCATION")
  private String referenceLocation;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ERROR_CODE")
  private String errorCode;
  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "LAST_TIME_UPDATED")
  private Timestamp lastTimeUpdated;
  @Column(name = "BLOCK_ID")
  private Integer blockId;
  @Column(name = "EXECUTED_DATE")
  private Timestamp executedDate;
  @Column(name = "DESCRIPTION")
  @Lob
  private String description;
  @Column(name = "PARAM_NOTIFY")
  private String paramNotify;
  @Column(name = "TEMPLATE_CODE")
  private String templateCode;
  @Column(name = "OUTSTANDING")
  private Double outstanding;
  @Column(name = "REFERENCE_DATE")
  private Timestamp referenceDate;
  @Column(name = "HAVE_TAX")
  private String haveTax;
  @Column(name = "REFERENCE_POINT")
  private BigDecimal referencePoint;
  @Column(name = "REFUND_POINT_STATUS")
  private String refundPointStatus;
  @Column(name = "ACTUAL_PAY_DATE")
  private Timestamp actualPayDate;

  private static final String TCBS_ID = "tcbsId";

  public GeneralTransaction(String id, String campaignId, String glId, BigDecimal point, Timestamp issueDate, String status, String awardType, String referenceId, String referenceLocation, Timestamp createdDate, String tcbsId, String errorCode, String errorMessage, String action, Timestamp lastTimeUpdated) {
    this.id = id;
    this.campaignId = campaignId;
    this.glId = glId;
    this.point = point;
    this.issueDate = issueDate;
    this.status = status;
    this.awardType = awardType;
    this.referenceId = referenceId;
    this.referenceLocation = referenceLocation;
    this.createdDate = createdDate;
    this.tcbsId = tcbsId;
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
    this.action = action;
    this.lastTimeUpdated = lastTimeUpdated;
  }

  public GeneralTransaction(GeneralTransaction that) {
    this.campaignId = that.campaignId;
    this.glId = that.glId;
    this.point = that.point;
    this.issueDate = that.issueDate;
    this.status = that.status;
    this.awardType = that.awardType;
    this.referenceId = that.referenceId;
    this.referenceLocation = that.referenceLocation;
    this.createdDate = that.createdDate;
    this.tcbsId = that.tcbsId;
    this.errorCode = that.errorCode;
    this.errorMessage = that.errorMessage;
    this.action = that.action;
    this.lastTimeUpdated = that.lastTimeUpdated;
    this.haveTax = that.haveTax;
    this.referenceDate = that.referenceDate;
    this.paramNotify = that.paramNotify;
  }

  @Step
  public static void clearGL(List<String> tcbsId, String tableName) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    stringBuilder.append("delete from GENERAL_TRANSACTION a where (");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBS_ID"));
    stringBuilder.append(") AND REFERENCE_LOCATION = '" + tableName + "'");
    Query query = session.createNativeQuery(stringBuilder.toString());
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<GeneralTransaction> getTransactionID(List<String> tcbsId) {
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("select * from GENERAL_TRANSACTION a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBS_ID"));
    Query<GeneralTransaction> query = session.createNativeQuery(stringBuilder.toString(), GeneralTransaction.class);
    List<GeneralTransaction> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByReferenceIdAndReferenceLocation(String referenceId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.referenceId=:referenceId and a.referenceLocation=:referenceLocation",
      GeneralTransaction.class);
    query.setParameter("referenceId", referenceId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static void clearGeneralTransactionByCampaignIdAndDate(String campaignId, String date) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete GeneralTransaction a where a.campaignId=:campaignId and trunc(a.issueDate) > to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter(CAMPAIGN_ID_PROPERTY, campaignId);
    query.setParameter("issueDate", date);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGeneralTransactionByCampaignIdAndDate1(String campaignId, String date) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete GeneralTransaction a where a.campaignId=:campaignId and trunc(a.issueDate) = to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter(CAMPAIGN_ID_PROPERTY, campaignId);
    query.setParameter("issueDate", date);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocation(List<String> tcbsId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId in :tcbsId and a.referenceLocation=:referenceLocation",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocationOrderById(List<String> tcbsId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId in :tcbsId and a.referenceLocation=:referenceLocation order by a.id desc",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocationOrderByTcbsId(List<String> tcbsId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId in :tcbsId and a.referenceLocation=:referenceLocation order by a.tcbsId desc",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocationOrderById(String tcbsId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId = :tcbsId and a.referenceLocation=:referenceLocation order by a.id desc",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocationAndAction(List<String> tcbsId, String referenceLocation, String action) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId in :tcbsId and a.referenceLocation=:referenceLocation and a.action=:action",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    query.setParameter("action", action);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static List<GeneralTransaction> findGeneralTransactionByTcbsIdAndReferenceLocationAndCampaignId(List<String> tcbsId, String referenceLocation, String campaignId) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId in :tcbsId and a.referenceLocation=:referenceLocation and a.campaignId = :campaignId",
      GeneralTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    query.setParameter(CAMPAIGN_ID_PROPERTY, campaignId);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static void insertGeneralTransaction(GeneralTransaction generalTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(generalTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static GeneralTransaction insert(GeneralTransaction generalTransaction) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(generalTransaction);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return generalTransaction;
  }

  @Step
  public static GeneralTransaction saveOrUpdate(GeneralTransaction generalTransaction) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    GeneralTransaction entity = session.get(GeneralTransaction.class, generalTransaction.getId());
    entity.setPoint(generalTransaction.getPoint());
    entity.setGlId(generalTransaction.getGlId());
    entity.setStatus(generalTransaction.getStatus());
    entity.setExecutedDate(generalTransaction.getExecutedDate());
    session.saveOrUpdate(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return generalTransaction;
  }

  @Step
  public static void updateGeneralTransaction(String status, String id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update GeneralTransaction a set a.status=:status where a.id=:id");
    query.setParameter(STATUS_TITLE, status);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateAndInsertCalculator(String time, String campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update GeneralTransaction a " +
        "set a.status=:status " +
        "where a.campaignId=:campaignId and " +
        "TRUNC(a.issueDate) = TO_DATE(:time, 'YYYY-MM-DD') and a.status = 'SUCCESS'" +
        " and a.awardType = :awardType");
    query.setParameter("status", "OVER_LIMIT");
    query.setParameter("campaignId", campaignId);
    query.setParameter("time", time);
    query.setParameter("awardType", "Redeemable");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateGeneralTransactionWithStatusAndGlId(String status, String glId, String id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update GeneralTransaction a set a.status=:status, a.glId=:glId where a.id=:id");
    query.setParameter(STATUS_TITLE, status);
    query.setParameter("glId", glId);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<GeneralTransaction> getTransactionById(Long id) {
    Session session = ixuDbConnection.getSession();
    Query query = session.createQuery(
      "from GeneralTransaction a where a.id = :id"
    );
    query.setParameter("id", id.toString());
    List<GeneralTransaction> list = query.getResultList();
    session.clear();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static void clearGTById(Long id) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from GeneralTransaction a where a.id = :id"
    );
    query.setParameter("id", id.toString());
    query.executeUpdate();
    session.getTransaction().commit();
    session.clear();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGTByTcbsIdAndReferenceLocation(String tcbsId, String referenceLocation) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from GeneralTransaction a where a.tcbsId =:tcbsId and a.referenceLocation =:referenceLocation"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    query.executeUpdate();
    session.getTransaction().commit();
    session.clear();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGTWithCreatedDateIsNull() {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from GeneralTransaction a where a.createdDate is null"
    );
    query.executeUpdate();
    session.getTransaction().commit();
    session.clear();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearByReferenceIdAndTableCampaign(String referenceId, String tableName) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createNativeQuery(
      "delete from " + tableName + "  a where a.Id = :id"
    );
    query.setParameter("id", referenceId);
    query.executeUpdate();
    Query querySQL = session.createQuery(
      "delete from GeneralTransaction a where a.referenceId = :id and a.referenceLocation = :tableName"
    );
    querySQL.setParameter("id", referenceId);
    querySQL.setParameter("tableName", tableName);
    querySQL.executeUpdate();
    session.getTransaction().commit();
    session.clear();
    ixuDbConnection.closeSession();
  }

  @Step
  public static Long countTotalTransaction() {
    Session session = ixuDbConnection.getSession();
    Query query = session.createNativeQuery(
      "select count(*) as count from GENERAL_TRANSACTION"
    );
    BigDecimal count = (BigDecimal) query.getResultList().get(0);
    return count.longValue();
  }

  public static void updateTcbsIdForRetry(Long generalId, String retryTcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update GeneralTransaction a set a.tcbsId=:tcbsId where a.id=:id");
    query.setParameter("tcbsId", retryTcbsId);
    query.setParameter("id", generalId.toString());
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public List<HashMap<String, Object>> searchCustomerTransactionRequest(HashMap<String, Object> request, String tcbsId) {
    StringBuilder queryString = new StringBuilder(" select gt.id, rc.CAMPAIGNNAME as CAMPAIGN_NAME, rc.CAMPAIGNID as CAMPAIGN_ID, gt.ACTION, gt.POINT, gt.OUTSTANDING, gt.STATUS, ");
    queryString.append(" NVL(gt.ACTUAL_PAY_DATE, gt.EXECUTED_DATE) as EXECUTED_DATE_TIME, gt.DESCRIPTION, count(*) over (  ) as TOTAL_RECORDS from GENERAL_TRANSACTION gt left join ");
    queryString.append(" RB_CAMPAIGN rc on gt.CAMPAIGN_ID = rc.CAMPAIGNID ");
    queryString.append(String.format("where gt.TCBS_ID = '%s'", tcbsId));
    queryString.append(" and gt.AWARD_TYPE = 'Redeemable'");

    if (request.get(STATUS_TITLE) != null) {
      List<String> listStatus = Lists.newArrayList((List<String>) request.get(STATUS_TITLE));
      if (listStatus.contains("FAIL")) {
        listStatus.add("PROCESSING");
        listStatus.add("PENDING");
      }
      queryString.append(" and (");
      queryString.append(SqlUtils.getTcbsidListForSqlWithKey(listStatus, "STATUS", "gt"));
      queryString.append(")");
    } else {
      queryString.append(" and (");
      queryString.append(SqlUtils.getTcbsidListForSqlWithKey(Arrays.asList("SUCCESS", "PROCESSING", "FAIL", "PENDING"), "STATUS", GT_KEY));
      queryString.append(")");
    }

    if (request.get("campaign_id") != null) {
      queryString.append(" and (");
      queryString.append(SqlUtils.getTcbsidListForSqlWithKey((List<String>) request.get("campaign_id"), "CAMPAIGN_ID", GT_KEY));
      queryString.append(")");
    }

    if (request.get("action") != null) {
      List<String> listActions = (List<String>) request.get("action");
      String joinedListActions = listActions.stream().collect(Collectors.joining("', '", "'", "'"));
      queryString.append(" and (");
      queryString.append(" (" + SqlUtils.getTcbsidListForSqlWithKey((List<String>) request.get("action"), ACTION_COLUMN_NAME, GT_KEY));
      queryString.append(" and gt.CAMPAIGN_ID != 71)");
      queryString.append(" OR (" + SqlUtils.getTcbsidListForSqlWithKey((List<String>) request.get("action"), ACTION_COLUMN_NAME, GT_KEY));
      queryString.append(String.format(" and gt.CAMPAIGN_ID = 71 and 'sent' IN (%s))", joinedListActions));
      queryString.append(" OR (" + SqlUtils.getTcbsidListForSqlWithKey((List<String>) request.get("action"), ACTION_COLUMN_NAME, GT_KEY));
      queryString.append(String.format(" and gt.CAMPAIGN_ID = 71 and 'receive' IN (%s))", joinedListActions));
      queryString.append(")");
    }

    queryString.append(String.format(" and trunc(gt.LAST_TIME_UPDATED) >= to_date('%s', 'dd/MM/yyyy')", request.get(LAST_UPDATE_TIME_FROM)));
    queryString.append(String.format(" and trunc(gt.LAST_TIME_UPDATED) <= to_date('%s', 'dd/MM/yyyy')", request.get(LAST_UPDATE_TIME_TO)));

    StringBuilder finalQuery = new StringBuilder();

    finalQuery.append("Select id, CAMPAIGN_NAME, CAMPAIGN_ID, ACTION, POINT, OUTSTANDING, STATUS, " + " trunc(EXECUTED_DATE_TIME) as EXECUTED_DATE, DESCRIPTION, TOTAL_RECORDS from (").append(
      queryString.toString()).append(") order by EXECUTED_DATE_TIME desc ");

    int page = 0;
    int size = 10;
    if (request.get("page") != null && Integer.valueOf(request.get("page").toString()) > 0) {
      page = Integer.valueOf(request.get("page").toString());
    }
    if (request.get("size") != null && Integer.valueOf(request.get("size").toString()) > 0) {
      size = Integer.valueOf(request.get("size").toString());
    }
    int offset = page * size;
    finalQuery.append(String.format(" offset %s rows fetch next %s rows only ", offset, size));

    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    NativeQuery query = session.createSQLQuery(finalQuery.toString());

    List<Object[]> results = query.getResultList();
    String[] responseField = {"id", "campaign_name", "campaign_id",
      "action", "point", "outstanding", STATUS_TITLE, "last_updated_time",
      DESCRIPTION_OBJECT, "total_elements"};

    ixuDbConnection.closeSession();
    return mapResponseFromDb(results, responseField);
  }

  public List<HashMap<String, Object>> mapResponseFromDb(List<Object[]> results, String[] responseField) {
    List<HashMap<String, Object>> response = Lists.newArrayList();
    if (!results.isEmpty()) {
      results.forEach(result -> {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("showLink", false);
        for (int i = 0; i < responseField.length; i++) {
          if (result[i] == null && (responseField[i].equals(DESCRIPTION_OBJECT) || responseField[i].equals("campaign_name"))) {
            map.put(responseField[i], "");
          } else if (result[i] == null && (responseField[i].equals("outstanding") || responseField[i].equals("campaign_id"))) {
            map.put(responseField[i], "0");
          } else {
            fixClobToString(responseField[i], result[i], map);
          }
          if (result[2].toString().equals("71")) {
            if (result[3].toString().equals("Credit")) {
              map.put("showLink", true);
              map.put("action", "Receive");
            } else {
              map.put("action", "Sent");
            }
          }
        }
        response.add(map);
      });
    }
    ixuDbConnection.closeSession();
    return response;
  }

  public void fixClobToString(String responseField, Object result, HashMap<String, Object> mapData) {
    if (result != null) {
      if (!responseField.equals(DESCRIPTION_OBJECT)) {
        mapData.put(responseField, result.toString());
      } else {
        Clob clob = (Clob) result;
        String value = null;
        try {
          value = clob.getSubString(1, (int) clob.length());
        } catch (SQLException throwables) {
          LOGGER.info(throwables.getMessage());
        }
        mapData.put(responseField, value);
      }
    }
  }

  @Step
  public List<GeneralTransaction> searchGeneralTransactionRequest(Map<String, Object> request) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    StringBuilder queryString = new StringBuilder("select v.*, c.CAMPAIGNNAME as CAMPAIGN_NAME, count(*) OVER (  ) as TOTAL_RECORDS FROM GENERAL_TRANSACTION v " +
      "LEFT JOIN RB_CAMPAIGN c ON c.CAMPAIGNID = v.CAMPAIGN_ID");

    StringBuilder condition = new StringBuilder();

    generalConditionCommonRequest(request, condition);

    if (condition.length() != 0) {
      queryString.append(" where  ").append(condition.toString());
    }
    queryString.append(" order by v.ISSUE_DATE desc, v.ID desc ");

    int page = 1;
    int size = 200;
    if (request.get("page") != null && Integer.valueOf(request.get("page").toString()) > 0) {
      page = Integer.valueOf(request.get("page").toString());
    }
    if (request.get("size") != null && Integer.valueOf(request.get("size").toString()) > 0) {
      size = Integer.valueOf(request.get("size").toString());
    }
    int offset = (page - 1) * size;
    queryString.append(String.format(" offset %s rows fetch next %s rows only ", offset, size));

    Query query = session.createNativeQuery(queryString.toString()).addEntity(GeneralTransaction.class);

    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  private void buildSpecialContionRequestQuery(Map<String, Object> request, StringBuilder condition) {
    if (request.get("transaction_id") != null) {
      buildQueryWithCondition(condition);
      List<String> transactionIds = Lists.newArrayList();
      for (Integer transactionId : (Integer[]) request.get("transaction_id")) {
        transactionIds.add(String.valueOf(transactionId.intValue()));
      }
      condition.append(String.format(" v.ID IN (%s)", String.join(",", transactionIds)));
    }

    if (request.get(STATUS_TITLE) != null) {
      buildQueryWithCondition(condition);
      List<String> sts = Lists.newArrayList();
      for (String st : (List<String>) request.get(STATUS_TITLE)) {
        sts.add(String.format("'%s'", st));
      }
      condition.append(String.format(" v.STATUS IN (%s)", String.join(",", sts)));
    } else {
      buildQueryWithCondition(condition);
      condition.append(" (v.STATUS = 'PROCESSING' OR v.STATUS = 'FAIL' OR v.STATUS = 'REVERT' OR v.STATUS = 'SUCCESS' OR\n" +
        "                v.STATUS = 'TIMEOUT' OR v.STATUS = 'PENDING') ");
    }

    if (request.get("campaign_ids") != null) {
      buildQueryWithCondition(condition);
      List<String> campaign = Lists.newArrayList();
      for (Integer campaignValue : (Integer[]) request.get("campaign_ids")) {
        campaign.add(String.valueOf(campaignValue.intValue()));
      }
      condition.append(String.format(" v.CAMPAIGN_ID IN (%s)", String.join(",", campaign)));
    }
  }

  private void generalConditionCommonRequest(Map<String, Object> request, StringBuilder condition) {
    if (request.get("action") != null) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.ACTION = '%s'", request.get("action")));
    }

    if (request.get("tcbs_id") != null) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.TCBS_ID= '%s'", request.get("tcbs_id")));
    }

    if (request.get("min_point") != null) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.POINT >= %s", request.get("min_point")));
    }

    if (request.get("max_point") != null) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.POINT <= %s", request.get("max_point")));
    }

    if (request.get("created_time_from") != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(v.ISSUE_DATE) >= to_date('%s', 'dd/MM/yyyy') ",
        request.get("created_time_from").toString()));
    }

    if (request.get("created_time_to") != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(v.ISSUE_DATE) <= to_date('%s', 'dd/MM/yyyy') ",
        request.get("created_time_to").toString()));
    }

    if (request.get(LAST_UPDATE_TIME_FROM) != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(nvl(v.ACTUAL_PAY_DATE, v.EXECUTED_DATE)) >= to_date('%s', 'dd/MM/yyyy') ",
        request.get(LAST_UPDATE_TIME_FROM).toString()));
    }

    if (request.get(LAST_UPDATE_TIME_TO) != null) {
      buildQueryWithCondition(condition);

      condition.append(String.format(" trunc(nvl(v.ACTUAL_PAY_DATE, v.EXECUTED_DATE)) <= to_date('%s', 'dd/MM/yyyy') ",
        request.get(LAST_UPDATE_TIME_TO).toString()));
    }
    if (request.get(SOURCE) != null) {
      buildQueryWithCondition(condition);
      if ("MANUAL".equals(String.valueOf(request.get(SOURCE)))) {
        condition.append(" v.REFERENCE_LOCATION = 'MANUAL_TRANSACTION'");
      }
      if ("AUTO".equals(String.valueOf(request.get(SOURCE)))) {
        condition.append(" v.REFERENCE_LOCATION <> 'MANUAL_TRANSACTION'");
      }
    }
    if (request.get("point_type") != null) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" v.AWARD_TYPE = '%s'", request.get("point_type").toString()));
    }

    buildSpecialContionRequestQuery(request, condition);
  }

  private void buildQueryWithCondition(StringBuilder condition) {
    if (condition.length() != 0) {
      condition.append(" AND  ");
    }
  }

  @Step
  public Long countTotalGeneralTransactionRequest(Map<String, Object> request) {

    Session session = ixuDbConnection.getSession();
    session.clear();
    StringBuilder queryString = new StringBuilder("select count(*) FROM GENERAL_TRANSACTION v " +
      "LEFT JOIN RB_CAMPAIGN c ON c.CAMPAIGNID = v.CAMPAIGN_ID");

    StringBuilder condition = new StringBuilder();

    generalConditionCommonRequest(request, condition);

    if (condition.length() != 0) {
      queryString.append(" where  ").append(condition.toString());
    }
    queryString.append(" order by v.CREATED_DATE desc ");

    Query query = session.createNativeQuery(queryString.toString());

    BigDecimal result = (BigDecimal) query.getSingleResult();
    if (result != null) {
      ixuDbConnection.closeSession();
      return result.longValue();
    } else {
      ixuDbConnection.closeSession();
      return BigDecimal.ZERO.longValue();
    }
  }

  @Step
  public static List<GeneralTransaction> getGeneralTransactionByTcbsIdAndReferenceLocationOrderByDesc(String tcbsId, String referenceLocation) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId=:tcbsId and a.referenceLocation=:referenceLocation order by a.id desc", GeneralTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("referenceLocation", referenceLocation);
    List<GeneralTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<GeneralTransaction> getGeneralTransactionByTcbsIdAndCampaignIdAndAwardTypeAndAction(String tcbsId, String campaignId, String awardType, String action) {
    ixuDbConnection.getSession().clear();
    Query<GeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransaction a where a.tcbsId = :tcbsId and a.campaignId = :campaignId and a.awardType = :awardType and a.action = :action",
      GeneralTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(CAMPAIGN_ID_PROPERTY, campaignId);
    query.setParameter("awardType", awardType);
    query.setParameter("action", action);
    List<GeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static void clearGeneralTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete GeneralTransaction a where a.tcbsId = :tcbsId ");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGeneralTransactionByTcbsIdsAndCampaignIds(List<String> tcbsIds, List<String> campaignIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete GeneralTransaction a where a.tcbsId in :tcbsIds and a.campaignId in :campaignIds");
    query.setParameter("tcbsIds", tcbsIds);
    query.setParameter("campaignIds", campaignIds);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGTByCampaignId(String campaignId) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from GeneralTransaction a where a.campaignId =:campaignId"
    );
    query.setParameter("campaignId", campaignId);
    query.executeUpdate();
    session.getTransaction().commit();
    session.clear();
    ixuDbConnection.closeSession();
  }
}
