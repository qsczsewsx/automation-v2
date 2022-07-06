package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
import com.tcbs.automation.SqlUtils;
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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.tcbs.automation.ixu.ManualTransactionConstant.ACTION_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.ACTION_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.AMOUNT_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.APPROVED_DATE_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.APPROVED_DATE_FROM_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.APPROVED_DATE_TO_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.APPROVED_USER_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.APPROVED_USER_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CAMPAIGN_ID_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CAMPAIGN_ID_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CODE105C_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CODE_105C_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CREATED_DATE_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CREATED_DATE_FROM_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CREATED_DATE_TO_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CREATED_USER_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CREATED_USER_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CUSTOMER_FULL_NAME_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.CUSTOMER_FULL_NAME_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.EFFECTIVE_DATE_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.EFFECTIVE_DATE_FROM_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.EFFECTIVE_DATE_TO_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.ID_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.ID_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.MEMBERSHIP_NAME_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.MEMBERSHIP_NAME_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.ORIGINAL_TRANSACTION_ID_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.ORIGINAL_TRANSACTION_ID_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.POINT_FROM_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.POINT_TO_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.POINT_TYPE_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.POINT_TYPE_SEARCH_KEY;
import static com.tcbs.automation.ixu.ManualTransactionConstant.STATE_COLUMN_NAME;
import static com.tcbs.automation.ixu.ManualTransactionConstant.STATE_SEARCH_KEY;
import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "manual_transaction")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManualTransactionEntity {

  private static String stateString = "state";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "TEMPLATE_CODE")
  private String templateCode;

  @Column(name = "ACTION")
  private String action;

  @Column(name = "AMOUNT")
  private Long amount;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CODE105C")
  private String code105c;

  @Column(name = "CUSTOMER_FULL_NAME")
  private String customerFullName;

  @Column(name = "ORIGINAL_TRANSACTION_ID")
  private String originalTransactionId;

  @Column(name = "EFFECTIVE_DATE")
  private Timestamp effectiveDate;

  @Column(name = "STATE")
  private String state;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "APPROVED_USER")
  private String approvedUser;

  @Column(name = "APPROVED_DATE")
  private Timestamp approvedDate;

  @Column(name = "TAX_TRANS")
  private String taxTrans;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "GL_DESCRIPTION")
  private String glDescription;

  @Column(name = "NOTIFY_MESSAGE")
  private String notifyMessage;

  @Column(name = "EXT_TRANSACTION_ID")
  private String extTransactionId;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;

  @Column(name = "POINT_TYPE")
  private String pointType;

  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;

  @Column(name = "CAMPAIGN_NAME")
  private String campaignName;

  @Column(name = "TRANSACTION_ID")
  private String transactionId;

  @Column(name = "PAYMENT_PERIOD")
  private String paymentPeriod;


  @Step
  public static List<ManualTransactionEntity> findManualTransactionByFilter(Map<String, String> filters) throws ParseException {
    CriteriaBuilder cb = ixuDbConnection.getSession().getCriteriaBuilder();
    CriteriaQuery<ManualTransactionEntity> cr = cb.createQuery(ManualTransactionEntity.class);
    Root<ManualTransactionEntity> root = cr.from(ManualTransactionEntity.class);
    List<Predicate> predicates = new ArrayList<>();

    for (String key : filters.keySet()) {
      String value = filters.get(key);
      String columnName = getColumnName(key);

      switch (key) {
        case ID_SEARCH_KEY:
          String wildcardMask = String.format("%%%s%%", value);
          predicates.add(cb.like(cb.function("TO_CHAR", String.class, root.get(columnName), cb.literal("FM9999999999")), cb.literal(wildcardMask)));
          break;
        case CODE_105C_SEARCH_KEY:
        case CUSTOMER_FULL_NAME_SEARCH_KEY:
        case CREATED_USER_SEARCH_KEY:
        case APPROVED_USER_SEARCH_KEY:
          wildcardMask = String.format("%%%s%%", value);
          predicates.add(cb.like(root.get(columnName), wildcardMask));
          break;
        case CAMPAIGN_ID_SEARCH_KEY:
        case ACTION_SEARCH_KEY:
        case POINT_TYPE_SEARCH_KEY:
        case STATE_SEARCH_KEY:
        case MEMBERSHIP_NAME_SEARCH_KEY:
        case ORIGINAL_TRANSACTION_ID_SEARCH_KEY:
          predicates.add(cb.equal(root.get(columnName), value));
          break;
        case CREATED_DATE_FROM_SEARCH_KEY:
        case EFFECTIVE_DATE_FROM_SEARCH_KEY:
        case APPROVED_DATE_FROM_SEARCH_KEY:
          Date fromDate = SqlUtils.convertStringToDate(value, "dd/MM/yyyy");
          predicates.add(cb.greaterThanOrEqualTo(root.get(columnName), fromDate));
          break;
        case CREATED_DATE_TO_SEARCH_KEY:
        case EFFECTIVE_DATE_TO_SEARCH_KEY:
        case APPROVED_DATE_TO_SEARCH_KEY:
          Date dateValue = SqlUtils.convertStringToDate(value, "dd/MM/yyyy");
          Date endOfDay = new Date(dateValue.getTime() + TimeUnit.DAYS.toMillis(1) - 1);
          predicates.add(cb.lessThanOrEqualTo(root.get(columnName), endOfDay));
          break;
        case POINT_FROM_SEARCH_KEY:
          predicates.add(cb.greaterThanOrEqualTo(root.get(columnName), value));
          break;
        case POINT_TO_SEARCH_KEY:
          predicates.add(cb.lessThanOrEqualTo(root.get(columnName), value));
          break;
        default:
          break;
      }
    }

    cr.select(root).where(cb.and(predicates.toArray(new Predicate[0])));
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(cr);
    List<ManualTransactionEntity> manualTransactionEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return manualTransactionEntityList;
  }

  @Step
  public static void insertManualTransaction(ManualTransactionEntity manualTransactionEntity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(manualTransactionEntity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static Long create(ManualTransactionEntity manualTransactionEntity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Long id = (Long) ixuDbConnection.getSession().save(manualTransactionEntity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return id;
  }

  public static void deleteByTcbsId(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ManualTransactionEntity a where a.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsIds);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static void deleteByPaymentPeriod(String paymentPeriod) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ManualTransactionEntity a where a.paymentPeriod = :paymentPeriod");
    query.setParameter("paymentPeriod", paymentPeriod);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTransactionId(String transactionId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ManualTransactionEntity a where a.transactionId = :transactionId");
    query.setParameter("transactionId", transactionId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateStateById(Long id, String state) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "update ManualTransactionEntity i set i.state=:state  where i.id=:id");
    query.setParameter("id", id);
    query.setParameter(stateString, state);
    int numberRowDeleted = query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateEffectiveDateAndStatusById(Long id, String state, Date effectiveDate) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "update ManualTransactionEntity i set i.state=:state, i.effectiveDate=:effectiveDate where i.id=:id"
    );
    query.setParameter("id", id);
    query.setParameter(stateString, state);
    query.setParameter("effectiveDate", new Timestamp(effectiveDate.getTime()));
    int numberRowDeleted = query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateEffectiveDateAndStatusById(Long id, String state, Date effectiveDate, Date approvedDate) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "update ManualTransactionEntity i set i.state=:state, i.effectiveDate=:effectiveDate, i.approvedDate=:approvedDate where i.id=:id"
    );
    query.setParameter("id", id);
    query.setParameter(stateString, state);
    query.setParameter("effectiveDate", new Timestamp(effectiveDate.getTime()));
    query.setParameter("approvedDate", new Timestamp(approvedDate.getTime()));
    int numberRowDeleted = query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ManualTransactionEntity> findManualTranction(String description, List<String> code105c, List<Long> campaignId, List<Long> amount) {
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from ManualTransactionEntity a where a.glDescription=:description and a.code105c IN (:code105c) " +
        "and a.campaignId IN (:campaignId) and amount IN (:amount)"
    );
    query.setParameter("description", description);
    query.setParameter("code105c", code105c);
    query.setParameter("campaignId", campaignId);
    query.setParameter("amount", amount);
    List<ManualTransactionEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<ManualTransactionEntity> getManualTranctionById(Long id) {
    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from ManualTransactionEntity a where a.id =:id"
    );

    query.setParameter("id", id);
    List<ManualTransactionEntity> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static void clearManualTransaction(Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ManualTransactionEntity a where a.id=:id");
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static String getDataType(String searchKey) {
    switch (searchKey) {
      case CREATED_DATE_FROM_SEARCH_KEY:
      case EFFECTIVE_DATE_FROM_SEARCH_KEY:
      case APPROVED_DATE_FROM_SEARCH_KEY:
      case CREATED_DATE_TO_SEARCH_KEY:
      case EFFECTIVE_DATE_TO_SEARCH_KEY:
      case APPROVED_DATE_TO_SEARCH_KEY:
        return "date";
      case POINT_FROM_SEARCH_KEY:
      case POINT_TO_SEARCH_KEY:
      case CAMPAIGN_ID_SEARCH_KEY:
      case ID_SEARCH_KEY:
        return "long";
      default:
        return "string";
    }
  }

  public static String getOperator(String searchKey) {
    switch (searchKey) {
      case CREATED_DATE_FROM_SEARCH_KEY:
      case EFFECTIVE_DATE_FROM_SEARCH_KEY:
      case APPROVED_DATE_FROM_SEARCH_KEY:
      case POINT_FROM_SEARCH_KEY:
        return "greater";
      case CREATED_DATE_TO_SEARCH_KEY:
      case EFFECTIVE_DATE_TO_SEARCH_KEY:
      case APPROVED_DATE_TO_SEARCH_KEY:
      case POINT_TO_SEARCH_KEY:
        return "less";
      case CODE_105C_SEARCH_KEY:
      case CUSTOMER_FULL_NAME_SEARCH_KEY:
      case ID_SEARCH_KEY:
      case CREATED_USER_SEARCH_KEY:
      case APPROVED_USER_SEARCH_KEY:
        return "contain";
      default:
        return "equal";
    }
  }

  public static String getColumnName(String searchKey) {
    switch (searchKey) {
      case CREATED_DATE_FROM_SEARCH_KEY:
      case CREATED_DATE_TO_SEARCH_KEY:
        return CREATED_DATE_COLUMN_NAME;
      case EFFECTIVE_DATE_FROM_SEARCH_KEY:
      case EFFECTIVE_DATE_TO_SEARCH_KEY:
        return EFFECTIVE_DATE_COLUMN_NAME;
      case APPROVED_DATE_FROM_SEARCH_KEY:
      case APPROVED_DATE_TO_SEARCH_KEY:
        return APPROVED_DATE_COLUMN_NAME;
      case POINT_FROM_SEARCH_KEY:
      case POINT_TO_SEARCH_KEY:
        return AMOUNT_COLUMN_NAME;
      case ID_SEARCH_KEY:
        return ID_COLUMN_NAME;
      case CODE_105C_SEARCH_KEY:
        return CODE105C_COLUMN_NAME;
      case CUSTOMER_FULL_NAME_SEARCH_KEY:
        return CUSTOMER_FULL_NAME_COLUMN_NAME;
      case CAMPAIGN_ID_SEARCH_KEY:
        return CAMPAIGN_ID_COLUMN_NAME;
      case ACTION_SEARCH_KEY:
        return ACTION_COLUMN_NAME;
      case POINT_TYPE_SEARCH_KEY:
        return POINT_TYPE_COLUMN_NAME;
      case STATE_SEARCH_KEY:
        return STATE_COLUMN_NAME;
      case CREATED_USER_SEARCH_KEY:
        return CREATED_USER_COLUMN_NAME;
      case APPROVED_USER_SEARCH_KEY:
        return APPROVED_USER_COLUMN_NAME;
      case MEMBERSHIP_NAME_SEARCH_KEY:
        return MEMBERSHIP_NAME_COLUMN_NAME;
      case ORIGINAL_TRANSACTION_ID_SEARCH_KEY:
        return ORIGINAL_TRANSACTION_ID_COLUMN_NAME;
      default:
        return null;
    }
  }

  public static List<ManualTransactionEntity> findManualTransactionsByPaymentPeriodOrderByAsc(String paymentPeriod) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query<ManualTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from ManualTransactionEntity a where a.paymentPeriod = :paymentPeriod order by a.id asc", ManualTransactionEntity.class);
    query.setParameter("paymentPeriod", paymentPeriod);
    List<ManualTransactionEntity> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }
}