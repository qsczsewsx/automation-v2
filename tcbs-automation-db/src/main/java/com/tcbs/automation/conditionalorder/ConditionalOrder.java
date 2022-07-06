package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CONDITIONAL_ORDER")
public class ConditionalOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TCBSID")
  private String tcbsid;
  @NotNull
  @Column(name = "CODE105C")
  private String code105C;
  @NotNull
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @NotNull
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @NotNull
  @Column(name = "SYMBOL")
  private String symbol;
  @NotNull
  @Column(name = "VOLUME")
  private String volume;
  @NotNull
  @Column(name = "ORDER_TYPE")
  private String orderType;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @NotNull
  @Column(name = "START_DATE")
  private String startDate;
  @NotNull
  @Column(name = "END_DATE")
  private String endDate;
  @NotNull
  @Column(name = "EXEC_TYPE")
  private String execType;
  @Column(name = "FLEX_ORDER_ID")
  private String flexOrderId;
  @NotNull
  @Column(name = "CONDITION_ENGINE_ID")
  private String conditionEngineId;
  @Column(name = "TRIGGER_PRICE")
  private String triggerPrice;
  @Column(name = "TRIGGER_DATE")
  private String triggerDate;
  @Column(name = "FLEX_ORDER_PRICE")
  private String flexOrderPrice;
  @Column(name = "MATCHED_VOLUME")
  private String matchedVolume;
  @NotNull
  @Column(name = "CREATED_ON")
  private String createdOn;
  @Column(name = "MODIFIED_ON")
  private String modifiedOn;
  @Column(name = "FLEX_STATUS")
  private String flexStatus;
  @Column(name = "NOTE")
  private String note;

  @Step
  public static ConditionalOrder getConditionalOrderById(String id) {
    Query<ConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ConditionalOrder a where a.id=:id", ConditionalOrder.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }

  public static void updateStatusAndFlexStatus(String id, String status, String flexStatus) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<ConditionalOrder> query = session.createQuery(
      "UPDATE ConditionalOrder i\n" +
        "    SET i.status=:status\n" +
        "        ,i.flexStatus=:flexStatus\n" +
        "    where i.id=:id"
    );

    query.setParameter("id", id);
    query.setParameter("status", status);
    query.setParameter("flexStatus", flexStatus);

    query.executeUpdate();
    trans.commit();
  }

  public static List<Map<String, Object>> getListAdmin(int start, int offset) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT ID, TCBSID, CODE105C, ACCOUNT_NO, ACCOUNT_TYPE, SYMBOL, VOLUME, ORDER_TYPE, STATUS, FLEX_STATUS, START_DATE, END_DATE, EXEC_TYPE, FLEX_ORDER_ID, \r\n");
    queryStringBuilder.append("CONDITION_ENGINE_ID, TRIGGER_PRICE, TRIGGER_DATE, FLEX_ORDER_PRICE, MATCHED_VOLUME, CREATED_ON, MODIFIED_ON \r\n");
    queryStringBuilder.append("FROM (SELECT ID, TCBSID, CODE105C, ACCOUNT_NO, ACCOUNT_TYPE, SYMBOL, VOLUME, ORDER_TYPE, STATUS, FLEX_STATUS, START_DATE, END_DATE, EXEC_TYPE, \r\n");
    queryStringBuilder.append("FLEX_ORDER_ID, CONDITION_ENGINE_ID, TRIGGER_PRICE, TRIGGER_DATE, FLEX_ORDER_PRICE, MATCHED_VOLUME, \r\n");
    queryStringBuilder.append("CREATED_ON, MODIFIED_ON, ROW_NUMBER() OVER (ORDER BY ID DESC) AS ROW_NUM FROM CONDITIONAL_ORDER ) TB \r\n");
    queryStringBuilder.append(String.format("WHERE TB.ROW_NUM >=%2d and TB.ROW_NUM <=%2d", start, offset));

    List result = new ArrayList<>();
    result = TheConditionalOrder.anattaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    return result;
  }

  public static List<ConditionalOrder> getListFilter(HashMap hashMap) {
    StringBuilder queryStr = new StringBuilder("from ConditionalOrder a where a.tcbsid=:tcbsid ");
    queryStr.append(buildQueryStr(hashMap));
    Query<ConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery(queryStr.toString(), ConditionalOrder.class);
    query.setParameter("tcbsid", hashMap.get("tcbsid"));
    if (hashMap.get("accountType") != null && !hashMap.get("accountType").toString().isEmpty()) {
      query.setParameter("accountType", hashMap.get("accountType"));
    }
    if (hashMap.get("orderType") != null && !hashMap.get("orderType").toString().isEmpty()) {
      query.setParameter("orderType", hashMap.get("orderType"));
    }
    if (hashMap.get("symbol") != null && !hashMap.get("symbol").toString().isEmpty()) {
      query.setParameter("symbol", hashMap.get("symbol"));
    }
    if (hashMap.get("status") != null && !hashMap.get("status").toString().isEmpty()) {
      query.setParameter("status", hashMap.get("status"));
    }
    return query.getResultList();
  }

  private static StringBuilder buildQueryStr(HashMap hashMap) {
    StringBuilder queryStr = new StringBuilder();
    if (hashMap.get("accountType") != null && !hashMap.get("accountType").toString().isEmpty()) {
      queryStr.append(" AND accountType = :accountType");
    }
    if (hashMap.get("orderType") != null && !hashMap.get("orderType").toString().isEmpty()) {
      queryStr.append(" AND orderType = :orderType");
    }
    if (hashMap.get("symbol") != null && !hashMap.get("symbol").toString().isEmpty()) {
      queryStr.append(" AND symbol = :symbol");
    }
    if (hashMap.get("status") != null && !hashMap.get("status").toString().isEmpty()) {
      queryStr.append(" AND ").append(mapStatus(hashMap.get("status").toString()));
    }
    queryStr.append(buildFilterQuery(hashMap));
    return queryStr;
  }

  private static StringBuilder buildFilterQuery(HashMap hashMap) {
    StringBuilder queryStr = new StringBuilder();
    if (hashMap.get("filters") != null && !hashMap.get("filters").toString().isEmpty()) {
      String s = hashMap.get("filters").toString();
      s = s.substring(8, s.length() - 1);
      String[] lstStatus = s.split(";");
      for (int i = 0; i < lstStatus.length; i++) {
        if (i == 0) {
          queryStr.append(" AND ( ");
        }
        String str = mapStatus(lstStatus[i]);
        queryStr.append(str);
        if (i != lstStatus.length - 1) {
          queryStr.append(" OR ");
        }
        if (i == lstStatus.length - 1) {
          queryStr.append(" ) ");
        }
      }
    }
    return queryStr;
  }

  public static String mapStatus(String status) {
    switch (status) {
      case "PENDING_TRIGGER":
        return " (status = 'PENDING_TRIGGER') ";
      case "TRIGGERED":
        return " (status = 'TRIGGERED' AND flex_status IS NULL) ";
      case "FLEX_PLACED":
        return " (status = 'TRIGGERED' AND flex_status ='FLEX_PLACED') ";
      case "FAIL":
        return " (status = 'TRIGGERED' AND flex_status ='FAIL') ";
      case "EXCHANGE_PLACING":
        return " (status = 'TRIGGERED' AND flex_status ='EXCHANGE_PLACING') ";
      case "PENDING_FILL":
        return " (status = 'TRIGGERED' AND flex_status ='PENDING_FILL') ";
      case "PARTIALLY_FILLED":
        return " (status = 'TRIGGERED' AND flex_status ='PARTIALLY_FILLED') ";
      case "FILLED":
        return " (status = 'TRIGGERED' AND flex_status ='FILLED') ";
      case "COMPLETED":
        return " (status = 'TRIGGERED' AND flex_status ='COMPLETED') ";
      case "EXPIRED":
        return " ((status = 'EXPIRED') or (status = 'TRIGGERED' AND flex_status ='EXPIRED')) ";
      case "CANCELLED":
        return " ((status = 'CANCELLED') or (status = 'TRIGGERED' AND flex_status ='CANCELLED')) ";
      case "ACCEPT_CANCEL":
        return " (status = 'TRIGGERED' AND flex_status ='ACCEPT_CANCEL') ";
      case "CANCELLING":
        return " (status = 'TRIGGERED' AND flex_status ='CANCELLING') ";
      default:
        return "";
    }
  }

  public static List<ConditionalOrder> getConditionalOrderByAccNo(String accountNo) {
    Query<ConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ConditionalOrder a where a.accountNo=:accountNo order by id desc", ConditionalOrder.class);
    query.setParameter("accountNo", accountNo);

    return query.getResultList();
  }
  public static List<ConditionalOrder> getConditionalOrderByTcbsId(String tcbsid) {
    Query<ConditionalOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ConditionalOrder a where a.tcbsid=:tcbsid order by id desc", ConditionalOrder.class);
    query.setParameter("tcbsid", tcbsid);

    return query.getResultList();
  }
}
