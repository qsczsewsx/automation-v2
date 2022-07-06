package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PRE_ORDER")
public class PreOrder {

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
  @Column(name = "ORDER_TYPE")
  private String orderType;
  @NotNull
  @Column(name = "SYMBOL")
  private String symbol;
  @NotNull
  @Column(name = "VOLUME")
  private String volume;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @Column(name = "FLEX_STATUS")
  private String flexStatus;
  @NotNull
  @Column(name = "START_DATE")
  private String startDate;
  @NotNull
  @Column(name = "END_DATE")
  private String endDate;
  @NotNull
  @Column(name = "EXEC_TYPE")
  private String execType;
  @Column(name = "PRICE_TYPE")
  private String priceType;
  @Column(name = "ORDER_PRICE")
  private String orderPrice;
  @Column(name = "FLEX_ORDER_ID")
  private String flexOrderId;
  @Column(name = "MATCHED_VOLUME")
  private String matchedVolume;
  @Column(name = "FLEX_ORDER_PRICE")
  private String flexOrderPrice;
  @Column(name = "TRIGGER_DATE")
  private String triggerDate;
  @Column(name = "FLEX_ERR_CODE")
  private String flexErrCode;
  @NotNull
  @Column(name = "CREATED_ON")
  private String createdOn;
  @Column(name = "MODIFIED_ON")
  private String modifiedOn;
  @Column(name = "DETAIL_REASON")
  private String detailReason;

  public static void deleteByOrderId(String id) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<PreOrder> query = session.createQuery(
      "DELETE FROM PreOrder  WHERE id = :id");

    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static PreOrder getByOrderId(String id) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    Query<PreOrder> query = session.createQuery("from PreOrder ib where ib.id =:id");
    query.setParameter("id", id);
    PreOrder result = query.getSingleResult();
    return result;
  }

  public static void updateStatusByOrderId(String id, String status) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    Transaction trans = session.beginTransaction();

    Query<PreOrder> query = session.createSQLQuery(
      "UPDATE PRE_ORDER set status=:status  where id =:id"
    );
    query.setParameter("status", status);
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }

  public static List<PreOrder> getConditionalOrderByAccNo(String accountNo) {
    Query<PreOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from PreOrder a where a.accountNo=:accountNo order by id desc", PreOrder.class);
    query.setParameter("accountNo", accountNo);

    return query.getResultList();
  }
  public static List<PreOrder> getConditionalOrderByTcbsId(String tcbsid) {
    Query<PreOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from PreOrder a where a.tcbsid=:tcbsid order by id desc", PreOrder.class);
    query.setParameter("tcbsid", tcbsid);

    return query.getResultList();
  }
}
