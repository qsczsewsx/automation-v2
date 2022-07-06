package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "REFERENCE_RATE_DRAFT")
public class ReferenceRateDraft {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "VERSION")
  private String version;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "STATUS")
  private String status;

  public static List<ReferenceRateDraft> getReferenceRateDraft(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM ReferenceRateDraft WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    try {
      Query<ReferenceRateDraft> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), ReferenceRateDraft.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      return new ArrayList<>();
    }
  }

  @Step("get version")
  public static Integer getVersion(Date fromDate, Date toDate) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySQL = new StringBuilder("select version from reference_rate_draft where id in ");
    querySQL.append(" (select max(id) from reference_rate_draft where created_at >= :fromDate and created_at <= :toDate)");
    try {
      Query query = session.createSQLQuery(querySQL.toString());
      query.setParameter("fromDate", fromDate);
      query.setParameter("toDate", toDate);
      Object obj = query.getSingleResult();
      return ((BigDecimal) obj).intValue();
    } catch (Exception ex) {
      return 0;
    }
  }

  public static void deleteDraft(String name) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "DELETE FROM ReferenceRateDraft a WHERE a.name = :name"
    );
    query.setParameter("name", name);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
