package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CONDITIONAL_ORDER_ST_DETAIL")
public class ConditionalOrderStDetail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "ORDER_ID")
  private String orderId;
  @NotNull
  @Column(name = "CODE")
  private String code;
  @NotNull
  @Column(name = "VALUE")
  private String value;
  @NotNull
  @Column(name = "VALUE_TYPE")
  private String valueType;
  @NotNull
  @Column(name = "UUID")
  private String uuid;
  @NotNull
  @Column(name = "COST_PRICE")
  private String costPrice;
  @Column(name = "ORDER_PRICE")
  private String orderPrice;
  @Column(name = "PRICE_TYPE")
  private String priceType;
  @NotNull
  @Column(name = "TYPE")
  private String type;
  @NotNull
  @Column(name = "MATCHED")
  private String matched;
  @NotNull
  @Column(name = "ACTION")
  private String action;

  @Step
  public static ConditionalOrderStDetail getConditionalOrderDetailsById(String orderId) {
    Query<ConditionalOrderStDetail> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ConditionalOrderStDetail a where a.orderId=:orderId", ConditionalOrderStDetail.class);
    query.setParameter("orderId", orderId);

    return query.getSingleResult();
  }

  public static List<ConditionalOrderStDetail> getListConditionalOrderDetailsById(String orderId) {
    Query<ConditionalOrderStDetail> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ConditionalOrderStDetail a where a.orderId=:orderId", ConditionalOrderStDetail.class);
    query.setParameter("orderId", orderId);

    return query.getResultList();
  }

  public static void updateDetailsStatus(String orderId, String value) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    Query<ConditionalOrderStDetail> query = session.createQuery("UPDATE ConditionalOrderStDetail i\n" +
      "    SET i.matched=:value\n" +
      "    where i.orderId=:orderId");

    query.setParameter("orderId", orderId);
    query.setParameter("value", value);

    query.executeUpdate();
    trans.commit();
  }

}
