package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import net.thucydides.core.annotations.Step;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "DEFINITION_RULE")
public class DefinitionRule {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "TEMPLATE_ID")
  private String templateId;
  @Column(name = "VERSION")
  private String version;
  @Column(name = "FILENAME")
  private String filename;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "IS_DEFAULT")
  private Integer isDefault;
  @Column(name = "CONTENT_TYPE")
  private String contentType;

  @Step("get definition rule")
  public static List<DefinitionRule> getDefinitionRule(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM DefinitionRule WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    querySQL.append(" order by id asc");
    try {
      Query<DefinitionRule> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), DefinitionRule.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      return new ArrayList<>();
    }
  }

  public static List<DefinitionRule> getAllDefinitionRule() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<DefinitionRule> query = session.createQuery("FROM DefinitionRule order by id asc ", DefinitionRule.class);
    return query.getResultList();
  }

  public static DefinitionRule getDefinitionRule(Integer id) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<DefinitionRule> query = session.createQuery("FROM DefinitionRule a WHERE a.id = :id", DefinitionRule.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
}
