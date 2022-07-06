package com.tcbs.automation.intermana;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "ACTION")
public class Action {
  static final Logger logger = LoggerFactory.getLogger(Action.class);
  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "ACTION_CODE")
  private String actionCode;
  @Column(name = "ACTION_NAME")
  private String actionName;

  @Step("get actions")
  public List<Action> getListAction(Integer id) {
    Session session = IntermanaService.intermanaConnection.getSession();
    StringBuilder sql = new StringBuilder("select ac.id, ac.action_code, ac.action_name from related_list_action ra");
    sql.append(" left join action ac on ra.action_id = ac.id where ra.related_list_id = :related_list_id");
    Query query = session.createSQLQuery(sql.toString());
    query.setParameter("related_list_id", id);
    List<Object[]> objList = query.getResultList();
    List<Action> actions = new ArrayList<>();
    for (Object[] obj : objList) {
      Action action = new Action();
      action.setId(((BigDecimal) obj[0]).intValue());
      action.setActionCode((String) obj[1]);
      action.setActionName((String) obj[2]);
      actions.add(action);
    }
    return actions;
  }

}
