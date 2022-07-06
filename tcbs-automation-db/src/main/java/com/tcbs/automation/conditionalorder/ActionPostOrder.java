package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ACTION_POST_TO_FLEX")
public class ActionPostOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private int id;
  @NotNull
  @Column(name = "ACTION_ID")
  private int actionId;
  @NotBlank
  @Column(name = "SYMBOL")
  private String symbol;
  @NotNull
  @Column(name = "ORDER_VOLUME")
  private int orderVolume;
  @NotBlank
  @Column(name = "PRICE_TYPE")
  private String priceType;
  @NotBlank
  @Column(name = "ORDER_PRICE")
  private String orderPrice;
  @NotBlank
  @Column(name = "EXEC_TYPE")
  private String execType;

  @Step
  public static ActionPostOrder getActionPostOrderByActionId(int actionId) {
    Query<ActionPostOrder> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ActionPostOrder a where a.actionId=:actionId", ActionPostOrder.class);
    query.setParameter("actionId", actionId);

    return query.getSingleResult();
  }
}
