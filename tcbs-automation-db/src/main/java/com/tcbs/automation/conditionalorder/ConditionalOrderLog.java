package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CONDITIONAL_ORDER_LOG")
public class ConditionalOrderLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "ORDER_ID")
  private String orderId;
  @NotNull
  @Column(name = "ACTION")
  private String action;
  @NotNull
  @Column(name = "OBJECT")
  private String object;
  @NotNull
  @Column(name = "DESCRIPTION")
  private String description;
  @NotNull
  @Column(name = "ACTOR")
  private String actor;
  @NotNull
  @Column(name = "CREATED_ON")
  private String createdOn;

  @Step
  public List<ConditionalOrderLog> getOrderLogUpdatedByOrderIdAndAction(String orderId, String action) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    Query<ConditionalOrderLog> query = session.createQuery("from ConditionalOrderLog a where a.orderId=:orderId and a.action=:action");

    query.setParameter("orderId", orderId);
    query.setParameter("action", action);

    return query.getResultList();
  }
}
