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
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "SUB_ACCOUNT_BLOCKADE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountBlockade {

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
  @Column(name = "HOLD_KEY")
  private String holdKey;
  @Column(name = "LOAN_KEY")
  private String loanKey;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "BLOCKADE_STATUS")
  private String blockadeStatus;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "PLANNING_KEY")
  private String planningKey;
  @Column(name = "BLOCKADE_TYPE")
  private String blockadeType;

  public static SubAccountBlockade getByLoanKey(String loanKey) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBlockade> query = h2hConnection.getSession().createQuery(
        "from SubAccountBlockade where loanKey=:loanKey",
        SubAccountBlockade.class
      );
      query.setParameter("loanKey", loanKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static SubAccountBlockade getByHoldKey(String holdKey) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBlockade> query = h2hConnection.getSession().createQuery(
        "from SubAccountBlockade where holdKey=:holdKey",
        SubAccountBlockade.class
      );
      query.setParameter("holdKey", holdKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public static void updateTypeByHoldKey(String holdKey) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "update SubAccountBlockade SET type='PLANNING' where holdKey =:holdKey"
    );
    query.setParameter("holdKey", holdKey);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<SubAccountBlockade> getBySubAccountNumber(String subAccountNumber) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBlockade> query = h2hConnection.getSession().createQuery(
        "from SubAccountBlockade where subAccountNo=:subAccountNo order by id desc",
        SubAccountBlockade.class
      );
      query.setParameter("subAccountNo", subAccountNumber);

      return query.getResultList();
    } catch (NoResultException e) {
      System.out.println(e.getMessage());
      return new ArrayList<>();
    }
  }

  /**
   * Author Lybtk
   */
  public static void updateTypeBySubAcc(String holdKey, String type) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "update SubAccountBlockade set blockadeType =:type where holdKey =:holdKey");
    query.setParameter("type", type);
    query.setParameter("holdKey", holdKey);

    query.executeUpdate();
    session.getTransaction().commit();

  }

  public static SubAccountBlockade getByLoanKeyPaidLoan(String loanKey) {
    try {
      h2hConnection.getSession().clear();
      Query<SubAccountBlockade> query = h2hConnection.getSession().createQuery(
        "from SubAccountBlockade where loanKey=:loanKey and blockadeType = 'SELL_BOND_LD' ",
        SubAccountBlockade.class
      );
      query.setParameter("loanKey", loanKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

}
