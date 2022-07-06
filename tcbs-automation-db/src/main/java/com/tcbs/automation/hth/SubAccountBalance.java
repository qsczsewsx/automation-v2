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
@Table(name = "SUB_ACCOUNT_BALANCE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "SUB_ACCOUNT_NO")
  private String subAccountNo;
  @NotNull
  @Column(name = "SUB_ACCOUNT_TYPE")
  private String subAccountType;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "BALANCE")
  private BigDecimal balance;
  @Column(name = "AVAILABLE")
  private BigDecimal available;
  @Column(name = "HELD")
  private BigDecimal held;
  @Column(name = "LOAN")
  private BigDecimal loan;
  @Column(name = "PLANNING_BALANCE")
  private BigDecimal planningBalance;
  @Column(name = "PLANNING_AVAILABLE")
  private BigDecimal planningAvailable;
  @Column(name = "PLANNING_HELD")
  private BigDecimal planningHeld;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  /**
   * Author Lybtk
   */
  public static SubAccountBalance getBySubAcc(String subAccountNo) {
    HthDb.h2hConnection.getSession().clear();
    org.hibernate.query.Query<SubAccountBalance> query = HthDb.h2hConnection.getSession().createQuery(
        "from SubAccountBalance where subAccountNo=:subAccountNo",
      SubAccountBalance.class
    );
    query.setParameter("subAccountNo", subAccountNo);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Author Lybtk
   */
  public static void deleteBySubAcc(String subAccountNo) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "delete from SubAccountBalance where subAccountNo=:subAccountNo");
    query
      .setParameter("subAccountNo", subAccountNo)
      .executeUpdate();
    session.getTransaction().commit();
  }

  /**
   * Author Lybtk
   */
  public static SubAccountBalance getFromSubAccountNo(String subAccountNo) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBalance> query = h2hConnection.getSession().createQuery(
        "from SubAccountBalance where subAccountNo=:subAccountNo ",
        SubAccountBalance.class
      );
      query.setParameter("subAccountNo", subAccountNo);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }
}
