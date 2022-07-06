package com.tcbs.automation.bondlifecycle;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "RULE_TEMPLATE")
public class RuleTemplate extends RuleBaseEntity {
  @Id
  @Column(name = "RULE_ID", updatable = false, nullable = false)
  private Integer ruleId;

  public RuleTemplate() {
    super(null);
  }

  public RuleTemplate(RuleBaseEntity ruleBase) {
    super(ruleBase);
  }

  public static String getTableName() {
    Table table = RuleTemplate.class.getAnnotation(Table.class);
    return table.name();
  }

  @Step
  public static void deleteRuleById(Integer ruleId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete RULE_TEMPLATE a where a.RULE_ID =:ruleId");
    query.setParameter("ruleId", ruleId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByBusinessId(Integer businessId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete RULE_TEMPLATE a where a.BUSINESS_ID =:businessId");
    query.setParameter("businessId", businessId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static RuleTemplate getRuleById(Integer ruleId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<RuleTemplate> query = session.createNativeQuery("select * from RULE_TEMPLATE where RULE_ID = :ruleId", RuleTemplate.class);
    query.setParameter("ruleId", ruleId);
    List<RuleTemplate> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<RuleTemplate> findAllActive() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<RuleTemplate> query = session.createNativeQuery("select * from RULE_TEMPLATE where STATUS = 'ACTIVE'", RuleTemplate.class);
    return query.getResultList();
  }

  @Step
  public static List<RuleTemplate> findAllByBondTypes(List<String> bondTypeCodes) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<RuleTemplate> query = session.createNativeQuery("SELECT * FROM RULE_TEMPLATE WHERE STATUS = 'ACTIVE' and BUSINESS_ID in (select ID from BUSINESS where BOND_TYPE_CODE in :bondTypeCodes)",
      RuleTemplate.class);
    query.setParameter("bondTypeCodes", bondTypeCodes);
    return query.getResultList();
  }

  public Integer getRuleId() {
    return ruleId;
  }

  public void setRuleId(Integer ruleId) {
    this.ruleId = ruleId;
  }

  @Step
  public void insert() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(this);
    trans.commit();
  }
}
