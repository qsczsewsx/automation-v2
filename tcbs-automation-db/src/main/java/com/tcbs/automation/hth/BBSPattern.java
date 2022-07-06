package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "BBS_PATTERN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BBSPattern {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "PATTERN")
  private String pattern;
  @NotNull
  @Column(name = "START_DATE")
  private Date startDate;
  @NotNull
  @Column(name = "END_DATE")
  private Date endDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "PATTERN_ID")
  private String patternId;

  public static void deleteByPatternId(String patternId) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "delete from BBSPattern where patternId=:patternId");
    query
      .setParameter("patternId", patternId)
      .executeUpdate();

    Query query1 = session.createNativeQuery(
      "delete from BBS_RECONCILE where PATTERN_ID=:patternId");
    query1
      .setParameter("patternId", patternId)
      .executeUpdate();
    session.getTransaction().commit();
  }
}
