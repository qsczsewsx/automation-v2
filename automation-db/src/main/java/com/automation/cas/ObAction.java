package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "OB_ACTION")
@Getter
@Setter
public class ObAction {
  private static Logger logger = LoggerFactory.getLogger(ObAction.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "GROUP_ACTION")
  private String groupAction;

  @Step
  public static ObAction getByActionId(String id) {
    CAS.casConnection.getSession().clear();
    Query<ObAction> query = CAS.casConnection.getSession().createQuery(
      "from ObAction a where a.id=:id", ObAction.class);
    query.setParameter("id", new BigDecimal(id));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObAction();
    }
  }

}
