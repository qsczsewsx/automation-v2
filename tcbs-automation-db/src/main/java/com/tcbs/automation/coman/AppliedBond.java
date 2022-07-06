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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "APPLIED_BOND")
public class AppliedBond {

  private final static Logger logger = LoggerFactory.getLogger(AppliedBond.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "REFERENCE_RATE_ID")
  private Integer referenceRateId;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "APPLY_TIME")
  private String applyTime;

  public static List<AppliedBond> getListAppliedBond(Integer referenceRateId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
//    if (!session.getTransaction().isActive()) {
//      session.beginTransaction();
//    }
    Query<AppliedBond> query = session
      .createQuery("from AppliedBond a where a.referenceRateId=:referenceRateId");
    query.setParameter("referenceRateId", referenceRateId);
    List<AppliedBond> list = query.getResultList();
    return list;

  }

  public static void deleteAppliedBondByRefRateId(Integer referenceRateId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<AppliedBond> query = session.createQuery(
      "DELETE FROM AppliedBond a WHERE a.referenceRateId = :referenceRateId"
    );
    query.setParameter("referenceRateId", referenceRateId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step("get applied bond")
  public static List<AppliedBond> getAppliedBond(HashMap<String, Object> params) {
    StringBuilder querySQL = new StringBuilder("FROM AppliedBond WHERE 1 = 1");
    for (Map.Entry<String, Object> entry : params.entrySet()) {
      querySQL.append(" and ");
      querySQL.append(entry.getKey() + " = :" + entry.getKey());
    }
    try {
      Query<AppliedBond> query = Connection.comanDbConnection.getSession().createQuery(querySQL.toString(), AppliedBond.class);
      for (Map.Entry<String, Object> entry : params.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }
  }

  public static void main(String[] args) {
    HashMap b = new HashMap();
    b.put("paymentDate", "2022-02-28");
    List<AppliedBond> a = AppliedBond.getAppliedBond(b);
    System.out.println(a);
  }
}
