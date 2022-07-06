package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ACTION")
public class Action {
  @Id
  @NotNull
  @Column(name = "ID")
  private int id;
  @NotNull
  @Column(name = "TYPE")
  private String type;
  @NotNull
  @Column(name = "ORDER_ID")
  private int orderId;

  @Step
  public static Action getActionByOrderId(String orderId) {
    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append("SELECT * FROM ACTION \r\n");
//    queryStringBuilder.append(String.format("WHERE ORDER_ID = %2d", Integer.parseInt(orderId)));
//
//    Query<Action> query = TheConditionalOrder.anattaDbConnection.getSession().createSQLQuery(queryStringBuilder.toString());
    Query<Action> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from Action a where a.orderId=:orderId", Action.class);
    query.setParameter("orderId", Integer.parseInt(orderId));
//    val a = query.getSingleResult();

    return query.getSingleResult();
  }
}

