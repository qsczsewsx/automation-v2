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
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "DYNAMIC_MARGIN_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DynamicMarginTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "LOAN_CODE")
  private String loanCode;
  @Column(name = "NORMAL_RATE")
  private String normalRate;
  @Column(name = "PREFERENTIAL_INTEREST_RATE")
  private String preferentialInterestRate;
  @Column(name = "PREFERENTIAL_INTEREST")
  private String preferentialInterest;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "POOL_ID")
  private String poolId;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DynamicMarginTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByCode105C(List<String> code105C) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DynamicMarginTransaction s where s.code105C in :code105C");
    query.setParameter("code105C", code105C);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsIdAndLoanCode(String tcbsId, String loanCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DynamicMarginTransaction s where s.tcbsId =:tcbsId and s.loanCode=:loanCode");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("loanCode", loanCode);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<DynamicMarginTransaction> findDynamicMarginTransactionByTcbsIdAndCampaignId(String tcbsId, String loanCode) {
    Query<DynamicMarginTransaction> query = ixuDbConnection.getSession().createQuery(
      "from DynamicMarginTransaction a where a.tcbsId=:tcbsId and a.loanCode=:loanCode order by a.id desc ");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("loanCode", loanCode);
    List<DynamicMarginTransaction> marginTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return marginTransactionList;
  }

  @Step
  public static List<DynamicMarginTransaction> findAllDynamicMarginTransaction() {
    Query<DynamicMarginTransaction> query = ixuDbConnection.getSession().createQuery(
      "from DynamicMarginTransaction a where a.id is not null ");
    List<DynamicMarginTransaction> marginTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return marginTransactionList;
  }

  @Step
  public List<HashMap<String, String>> searchDynamicMarginTransactionBy105C(Map<String, Object> request) {
    String sqlQuery = "SELECT gr.TOTAL, gr.POOL_ID, trunc(oth.END_DATE), oth.PREFERENTIAL_INTEREST_RATE, oth.LOAN_CODE, gr.MAX_AMOUNT, gr.MIN_AMOUNT\n" +
      "   FROM (SELECT FLOOR(SUM(dyn.POINT))          as TOTAL,\n" +
      "             MAX(dyn.PREFERENTIAL_INTEREST) as MAX_AMOUNT,\n" +
      "             MIN(dyn.PREFERENTIAL_INTEREST) as MIN_AMOUNT,\n" +
      "             dyn.POOL_ID, dyn.LOAN_CODE\n" +
      "       FROM DYNAMIC_MARGIN_TRANSACTION dyn WHERE";

    sqlQuery += String.format(" dyn.CODE105C = '%s' ", request.get("custodycd"));

    if (request.get("loan_code") != null) {
      sqlQuery += " AND dyn.LOAN_CODE IN (:loanCode) ";
    }

    sqlQuery += "GROUP BY dyn.POOL_ID, dyn.LOAN_CODE) gr\n" +
      "       LEFT JOIN (select sub1.POOL_ID, sub1.LOAN_CODE, sub1.END_DATE, sub1.PREFERENTIAL_INTEREST_RATE\n" +
      "                  from DYNAMIC_MARGIN_TRANSACTION sub1 LEFT JOIN DYNAMIC_MARGIN_TRANSACTION sub2\n" +
      "                                                                 ON (sub1.LOAN_CODE = sub2.LOAN_CODE\n" +
      "                                                                   AND sub1.CODE105C = sub2.CODE105C\n" +
      "                                                                   AND sub1.POOL_ID = sub2.POOL_ID\n" +
      "                                                                   AND TRUNC(sub1.ISSUED_DATE) < TRUNC(sub2.ISSUED_DATE))" +
      "                   where sub2.ID is null";

    if (request.get("loan_code") != null) {
      sqlQuery += " AND sub1.LOAN_CODE IN (:loanCode) ";
    }


    sqlQuery += String.format(" AND sub1.CODE105C = '%s') oth\n", request.get("custodycd")) +
      " ON gr.POOL_ID = oth.POOL_ID AND gr.LOAN_CODE = oth.LOAN_CODE\n" +
      "       ORDER BY oth.LOAN_CODE DESC";

    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    NativeQuery query = session.createSQLQuery(sqlQuery);
    if (request.get("loan_code") != null) {
      query.setParameter("loanCode", request.get("loan_code"));
    }
    List<Object[]> results = query.getResultList();
    String[] responseField = {"total", "pool_id", "end_date", "pRate", "loan_code", "max_uamount", "min_uamount",};

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
}
