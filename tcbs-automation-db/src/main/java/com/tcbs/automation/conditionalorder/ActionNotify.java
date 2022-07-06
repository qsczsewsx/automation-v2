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
@Table(name = "ACTION_NOTIFY")
public class ActionNotify {
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

  @Step
  public static ActionNotify getActionNotifyByActionId(int actionId) {
    Query<ActionNotify> query = TheConditionalOrder.anattaDbConnection.getSession()
      .createQuery("from ActionNotify a where a.actionId=:actionId", ActionNotify.class);
    query.setParameter("actionId", actionId);

    return query.getSingleResult();
  }
}
