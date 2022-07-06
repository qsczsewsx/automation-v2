package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_PLANNING")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountPlanning {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "PLANNING_KEY")
  private String planningKey;
  @Column(name = "REFERENCE_ID")
  private String referenceId;
  @Column(name = "REFERENCE_TYPE")
  private String referenceType;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "AVAILABLE")
  private BigDecimal available;
  @Column(name = "ACTUAL")
  private BigDecimal actual;
  @Column(name = "HELD")
  private BigDecimal held;
  @Column(name = "ADVANCE")
  private BigDecimal advance;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static void updateByReferenceId(String referenceId) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update SubAccountPlanning SET status ='PLANNING', available=10000, held=10000, actual=10000 where referenceId=:referenceId");
    query.setParameter("referenceId", referenceId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static SubAccountPlanning getByPlanningKey(String planningKey) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountPlanning> query = h2hConnection.getSession().createQuery(
        "from SubAccountPlanning where planningKey=:planningKey",
        SubAccountPlanning.class
      );
      query.setParameter("planningKey", planningKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
