package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import net.thucydides.core.annotations.Step;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.coman.ComanKey.BOND_CODE_KEY;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "REFERENCE_BASE")
public class ReferenceBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "TYPE")
  private String type;

  public static List<ReferenceBase> getAllReferenceBase() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceBase> query = session.createQuery("FROM ReferenceBase WHERE type is not null order by id asc ", ReferenceBase.class);
    return query.getResultList();
  }

  @Step("get reference base")
  public static List<ReferenceBase> getReferenceBase(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM ReferenceBase WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    querySQL.append(" order by id asc");
    try {
      Query<ReferenceBase> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), ReferenceBase.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      return new ArrayList<>();
    }
  }

  public static ReferenceBase getReferenceBase(Integer id) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceBase> query = session.createQuery("FROM ReferenceBase a WHERE a.id = :id", ReferenceBase.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public static ReferenceBase getReferenceBaseByBaseNam(String name) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceBase> query = session.createQuery("FROM ReferenceBase a WHERE a.name = :name", ReferenceBase.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }
}
