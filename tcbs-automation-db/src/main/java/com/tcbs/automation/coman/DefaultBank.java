package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "DEFAULT_BANK")
public class DefaultBank {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "CITAD")
  private String citad;
  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Step("get default bank")
  public static List<DefaultBank> getDefaultBank(Integer groupId) {
    StringBuilder querySQL = new StringBuilder("FROM DefaultBank WHERE groupId = :groupId order by id asc");
    try {
      Query<DefaultBank> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), DefaultBank.class);
      query.setParameter("groupId", groupId);
      return query.getResultList();
    } catch (Exception ex) {
      return new ArrayList<>();
    }
  }

  public static List<DefaultBank> getDefaultBankList(Integer groupId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<DefaultBank> query = session.createQuery("FROM DefaultBank a WHERE a.groupId = :groupId order by id asc ", DefaultBank.class);
    query.setParameter("groupId", groupId);
    return query.getResultList();
  }

}
