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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "REFERRAL_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferralTransaction {
  public static final String ORDER_TYPE = "orderType";
  public static final String TCBS_ID = "tcbsId";
  public static final String TOTAL_POINT = "totalPoint";
  private static Logger logger = LoggerFactory.getLogger(ReferralTransaction.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "ORDER_TYPE")
  private String orderType;

  @Column(name = "ORDER_ID")
  private String orderId;

  @Column(name = "TRADING_CODE")
  private String tradingCode;

  @Column(name = "TRADING_VALUE")
  private BigDecimal tradingValue;

  @Column(name = "CUSTOMER_TYPE")
  private String customerType;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "NET_VALUE")
  private BigDecimal netValue;

  @Column(name = "REF_ID")
  private String refId;

  @Column(name = "AWARD_TYPE")
  private String awardType;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "NOTIFY_MESSAGE")
  private String notifyMessage;

  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "POINT")
  private BigDecimal point;

  @Step
  public static List<Map<String, Object>> getListReferralTransactionByTcbsIdAndOrderType(String tcbsId,
                                                                                         String orderType) {

    String queryString = "Select ref.ORDER_ID, ref.POINT, ref2.TCBS_ID as REF_TCBS_ID from REFERRAL_TRANSACTION ref " +
      " inner join GENERAL_TRANSACTION gen " +
      " ON ref.ID = gen.REFERENCE_ID AND gen.STATUS = 'SUCCESS' AND gen.REFERENCE_LOCATION = 'REFERRAL_TRANSACTION' " +
      " inner join REFERRAL_TRANSACTION ref2 " +
      " ON ref.ID <> ref2.ID AND ref.ORDER_ID = ref2.ORDER_ID AND ref.ORDER_TYPE = ref2.ORDER_TYPE " +
      " AND ref.CUSTOMER_TYPE = 'REFERRER'" +
      " AND ref2.CUSTOMER_TYPE <> ref.CUSTOMER_TYPE" +
      " WHERE ref.TCBS_ID = :tcbsId " +
      " AND ref.ORDER_TYPE = :orderType " +
      " ORDER BY ref.ISSUE_DATE DESC ";
    NativeQuery query = ixuDbConnection.getSession().createSQLQuery(queryString);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(ORDER_TYPE, orderType);
    List<Object[]> results = query.getResultList();

    String[] responseField = {"order_id", "iXu_amount", "referee_id"};
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

  @Step
  public static BigDecimal getTotalPointByTcbsIdAndOrderType(String tcbsId, String orderType) {

    String queryString = "select SUM(ref.POINT) FROM REFERRAL_TRANSACTION ref\n" +
      "INNER JOIN GENERAL_TRANSACTION gen\n" +
      "ON gen.REFERENCE_ID = ref.ID\n" +
      "WHERE ref.TCBS_ID  = :tcbsId AND ref.ORDER_TYPE =:orderType\n" +
      "AND (ref.CUSTOMER_TYPE = 'REFERRER' OR ref.CUSTOMER_TYPE IS NULL )\n" +
      "AND gen.REFERENCE_LOCATION = 'REFERRAL_TRANSACTION' AND  gen.STATUS = 'SUCCESS'\n" +
      "ORDER BY ref.ISSUE_DATE DESC ";

    NativeQuery query = ixuDbConnection.getSession().createSQLQuery(queryString);

    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(ORDER_TYPE, orderType);

    BigDecimal result = (BigDecimal) query.getSingleResult();
    if (result != null) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return BigDecimal.ZERO;
    }
  }

  @Step
  public static List<Map<String, Object>> getTopByOrderTypeAndTopNumber(String orderType, int topNumber) {
    String queryString = "select * from TOP_REFERRAL where ORDER_TYPE = :orderType order by POINT desc fetch next :topNumber row only ";
    NativeQuery query = ixuDbConnection.getSession().createSQLQuery(queryString);

    query.setParameter(ORDER_TYPE, orderType);
    query.setParameter("topNumber", topNumber);

    List<Object[]> results = query.getResultList();
    List<Map<String, Object>> response = Lists.newArrayList();

    results.forEach(result -> {
      Map<String, Object> r = new HashMap<>();
      r.put(TCBS_ID, result[1]);
      r.put(TOTAL_POINT, result[2]);
      response.add(r);
    });
    ixuDbConnection.closeSession();
    return response;
  }

  @Step
  public static void create(ReferralTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(entity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }

    org.hibernate.query.Query query = ixuDbConnection.getSession().createNativeQuery(
      "DELETE FROM REFERRAL_TRANSACTION where TCBS_ID =:tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static ReferralTransaction findReferralTransactionById(Long id) {
    ixuDbConnection.getSession().clear();
    Query<ReferralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ReferralTransaction where id=:id ",
      ReferralTransaction.class);
    query.setParameter("id", id);
    List<ReferralTransaction> result = query.getResultList();
    if (!result.isEmpty()) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  public static ReferralTransaction findReferralTransactionByTcbsIdAndOrderType(String tcbsId, String orderType) {
    ixuDbConnection.getSession().clear();
    Query<ReferralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ReferralTransaction where tcbsId=:tcbsId and orderType=:orderType",
      ReferralTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter(ORDER_TYPE, orderType);
    List<ReferralTransaction> result = query.getResultList();
    if (!result.isEmpty()) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  public static ReferralTransaction getByTcbsIdAndOrderId(String tcbsId, String orderId) {
    Session session = ixuDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    ReferralTransaction result = new ReferralTransaction();
    Query<ReferralTransaction> query = session.createQuery(
      "from ReferralTransaction a where a.orderId = :orderId and tcbsId=:tcbsId", ReferralTransaction.class
    );
    query.setParameter("orderId", orderId);
    query.setParameter(TCBS_ID, tcbsId);
    query.setMaxResults(1);
    try {
      ixuDbConnection.closeSession();
      result = query.getSingleResult();
    } catch (Exception e) {
      ixuDbConnection.closeSession();
      logger.info(e.getMessage());
    }
    ixuDbConnection.closeSession();
    return result;
  }

  public void dmlPrepareData(String queryString) {
    Session session = ixuDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
