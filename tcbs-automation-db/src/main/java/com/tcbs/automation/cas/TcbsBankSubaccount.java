package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_BANK_SUBACCOUNT")
@Getter
@Setter
public class TcbsBankSubaccount {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "ACCOUNT_NAME")
  private String accountName;
  @NotNull
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BANK_BRANCH")
  private String bankBranch;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @NotNull
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "USER_TYPE")
  private String userType;

  @Step
  public static TcbsBankSubaccount getBank(String userId) {
    Query<TcbsBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankSubaccount a where a.userId=:userId", TcbsBankSubaccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static List<TcbsBankSubaccount> getBankByAccountNoAndUserId(String accountNo, String userId) {
    Query<TcbsBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankSubaccount a where a.accountNo=:accountNo and a.userId=:userId", TcbsBankSubaccount.class);
    query.setParameter("accountNo", accountNo);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  public static List<TcbsBankSubaccount> getListBank(String userId) {
    Query<TcbsBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankSubaccount a where a.userId=:userId order by id desc", TcbsBankSubaccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  @Step
  public static TcbsBankSubaccount getSubAccountByUserIdAndAccountType(String userId, String accountType) {
    Query<TcbsBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankSubaccount a where a.userId=:userId and a.accountType=:accountType", TcbsBankSubaccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("accountType", accountType);
    return query.getSingleResult();
  }

  @Step
  public static void deleteByAccountNo(String accountNo) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Delete from TcbsBankSubaccount a where a.accountNo=:accountNo");
    query.setParameter("accountNo", accountNo);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }
}
