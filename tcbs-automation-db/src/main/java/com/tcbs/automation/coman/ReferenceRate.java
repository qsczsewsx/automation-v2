package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "REFERENCE_RATE")
public class ReferenceRate {

  private final static Logger logger = LoggerFactory.getLogger(ReferenceRate.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "PERIOD")
  private Integer period;
  @Column(name = "STATUS")
  private Integer status;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "BASE_ID")
  private Integer baseId;
  @Column(name = "PERIOD_TYPE")
  private Integer periodType;
  @Column(name = "BANK_GROUP_ID")
  private Integer bankGroupId;
  @Column(name = "DEFINITION_RULE_ID")
  private Integer definitionRuleId;
  @Column(name = "FILE_ID")
  private String fileId;
  @Column(name = "PERIOD_UNIT")
  private String periodUnit;
  @Transient
  private String baseName;
  @Transient
  private String periodTypeStr;
  @Transient
  private String groupName;
  @Transient
  private String definitionRuleName;

  public static void deleteReferenceRateById(Integer referenceRateId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery(
      "DELETE FROM ReferenceRate a WHERE a.id = :referenceRateId"
    );
    query.setParameter("referenceRateId", referenceRateId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteReferenceRateByName(String referenceRateName) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery(
      "DELETE FROM ReferenceRate a WHERE a.name = :referenceRateName"
    );
    query.setParameter("referenceRateName", referenceRateName);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<ReferenceRate> getAllReferenceRate() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate order by id desc ", ReferenceRate.class);
    return query.getResultList();
  }

  public static ReferenceRate getReferenceRateDetail(Integer id) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.id = :id", ReferenceRate.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public static List<ReferenceRate> getReferenceRateByPeriod(Integer period) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.period = :period ", ReferenceRate.class);
    query.setParameter("period", period);
    return query.getResultList();
  }

  public static List<ReferenceRate> getReferenceRateByGroupBankId(Integer bankGroupId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.bankGroupId = :bankGroupId ", ReferenceRate.class);
    query.setParameter("bankGroupId", bankGroupId);
    return query.getResultList();
  }

  public static List<ReferenceRate> getReferenceRateByBaseId(Integer baseId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.baseId = :baseId ", ReferenceRate.class);
    query.setParameter("baseId", baseId);
    return query.getResultList();
  }

  public static List<ReferenceRate> getReferenceRateByDefinitionId(Integer definitionRuleId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.definitionRuleId = :definitionRuleId ", ReferenceRate.class);
    query.setParameter("definitionRuleId", definitionRuleId);
    return query.getResultList();
  }

  public static ReferenceRate getReferenceRateDetailByName(String name) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferenceRate> query = session.createQuery("FROM ReferenceRate a WHERE a.name = :name", ReferenceRate.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }

  @Step("get reference rate")
  public List<ReferenceRate> getReferenceRate(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM ReferenceRate WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    try {
      Query<ReferenceRate> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), ReferenceRate.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }
  }
}
