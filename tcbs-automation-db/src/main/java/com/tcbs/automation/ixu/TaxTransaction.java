package com.tcbs.automation.ixu;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.assertj.core.util.Lists;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "TAX_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TaxTransaction {

  private static final String TCBS_ID = "tcbs_id";
  private static final String STATUS = "status";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "TAX_AMOUNT")
  private BigDecimal taxAmount;
  @Column(name = "TOTAL_AMOUNT")
  private BigDecimal totalAmount;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "PAID_TRANSACTION_ID")
  private BigDecimal paidTransactionId;
  @Column(name = "REFUND_TRANSACTION_ID")
  private BigDecimal refundTransactionId;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from TaxTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteById(String id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from TaxTransaction s where s.id =:id");
    query.setParameter("id", id);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from TaxTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createTaxTransaction(TaxTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<TaxTransaction> findTaxTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<TaxTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TaxTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<TaxTransaction> taxTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return taxTransactionList;
  }

  @Step
  public static List<TaxTransaction> findTaxTransactionByTcbsIdAndPaidTransactionId(String tcbsId, String paidTransactionId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<TaxTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TaxTransaction a where a.tcbsId=:tcbsId and a.paidTransactionId=:paidTransactionId");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("paidTransactionId", new BigDecimal(paidTransactionId));
    List<TaxTransaction> taxTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return taxTransactionList;
  }

  @Step
  public static List<TaxTransaction> findTaxTransactionByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<TaxTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TaxTransaction a where a.tcbsId in :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<TaxTransaction> taxTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return taxTransactionList;
  }

  @Step
  public static List<TaxTransaction> findTaxTransactionByListTcbsId(List<String> tcbsId, String timeStart, String timeEnd, String status) {
    ixuDbConnection.getSession().clear();
    Query<TaxTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TaxTransaction a where a.tcbsId in :tcbsId and a.status = :status and TRUNC(a.createdDate) between TO_DATE(:timeStart, \'YYYY-MM-DD\') and TO_DATE(:timeEnd, \'YYYY-MM-DD\')");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter(STATUS, status);
    query.setParameter("timeStart", timeStart);
    query.setParameter("timeEnd", timeEnd);
    List<TaxTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static List<TaxTransaction> getTaxTransaction(String timeStart, String timeEnd, String status) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Query<TaxTransaction> query = ixuDbConnection.getSession().createQuery(
      "from TaxTransaction a " +
        " where a.status = :status and TRUNC(a.createdDate) between TO_DATE(:timeStart, \'YYYY-MM-DD\') and TO_DATE(:timeEnd, \'YYYY-MM-DD\')",
      TaxTransaction.class);
    query.setParameter(STATUS, status);
    query.setParameter("timeStart", timeStart);
    query.setParameter("timeEnd", timeEnd);
    List<TaxTransaction> rs = query.getResultList();
    ixuDbConnection.closeSession();
    return rs;
  }

  @Step
  public static List<HashMap<String, String>> searchTransactionByCondition(Map<String, Object> request) {
    Session session = ixuDbConnection.getSession();
    session.clear();

    StringBuilder queryStr = new StringBuilder("SELECT v.ID ,v.TCBS_ID," +
      " v.TAX_AMOUNT, ROUND(v.TOTAL_AMOUNT , 4), v.STATUS, coalesce(v.PAID_TRANSACTION_ID, 0), coalesce(v.REFUND_TRANSACTION_ID, 0) FROM TAX_TRANSACTION v ");

    StringBuilder condition = new StringBuilder();

    createConditionRequest(request, condition);

    if (condition.length() != 0) {
      queryStr.append(" where  ").append(condition.toString());
    }

    queryStr.append(" order by v.ID desc ");

    int page = 1;
    int size = 20;
    if (request.get("page") != null && Integer.valueOf(request.get("page").toString()) > 0) {
      page = Integer.valueOf(request.get("page").toString());
    }
    if (request.get("record_per_page") != null && Integer.valueOf(request.get("record_per_page").toString()) > 0) {
      size = Integer.valueOf(request.get("record_per_page").toString());
    }
    int offset = (page - 1) * size;
    queryStr.append(String.format(" offset %s rows fetch next %s rows only ", offset, size));

    Query query = session.createSQLQuery(queryStr.toString());

    List<Object[]> results = query.getResultList();

    String[] responseField = {"id", TCBS_ID, "tax_amount", "total_amount", STATUS, "paid_transaction_id", "refund_transaction_id"};

    List<HashMap<String, String>> response = Lists.newArrayList();

    if (!results.isEmpty()) {
      results.forEach(result -> {
        HashMap<String, String> map = Maps.newHashMap();
        for (int i = 0; i < responseField.length; i++) {
          map.put(responseField[i], result[i].toString());
        }
        response.add(map);
      });
    }
    ixuDbConnection.closeSession();
    return response;
  }

  @Step
  public static Long countTransactionByCondition(Map<String, Object> request) {
    Session session = ixuDbConnection.getSession();
    StringBuilder queryString = new StringBuilder("select count(*) as count from TAX_TRANSACTION v ");
    StringBuilder condition = new StringBuilder();

    createConditionRequest(request, condition);

    if (condition.length() != 0) {
      queryString.append(" where  ").append(condition.toString());
    }
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

  private static void createConditionRequest(Map<String, Object> request, StringBuilder condition) {
    if (request.containsKey(TCBS_ID)) {
      buildQueryWithCondition(condition);
      List<String> sts = Lists.newArrayList();
      for (String st : (List<String>) request.get(TCBS_ID)) {
        sts.add(String.format("'%s'", st));
      }
      condition.append(String.format(" v.TCBS_ID IN (%s)", String.join(",", sts)));
    }

    if (request.containsKey("paidId")) {
      buildQueryWithCondition(condition);
      condition.append(String.format("v.PAID_TRANSACTION_ID = '%s' ", request.get("paidId")));
    }

    if (request.containsKey(STATUS)) {
      buildQueryWithCondition(condition);
      condition.append(String.format("v.STATUS = '%s' ", request.get(STATUS)));
    }

    if (request.containsKey("created_date_from")) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" trunc(v.CREATED_DATE) >= to_date('%s', 'dd/MM/yyyy')", request.get("created_date_from")));
    }

    if (request.containsKey("created_date_to")) {
      buildQueryWithCondition(condition);
      condition.append(String.format(" trunc(v.CREATED_DATE) <= to_date('%s', 'dd/MM/yyyy')", request.get("created_date_to")));
    }
  }


  private static void buildQueryWithCondition(StringBuilder condition) {
    if (condition.length() != 0) {
      condition.append(" AND  ");
    }
  }
}
