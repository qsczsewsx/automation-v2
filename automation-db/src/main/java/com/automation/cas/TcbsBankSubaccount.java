package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.automation.cas.CAS.casConnection;

@Entity
@Table(name = "xxxx_BANK_SUBACCOUNT")
@Getter
@Setter
public class xxxxBankSubaccount {
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
  public static xxxxBankSubaccount getBank(String userId) {
    Query<xxxxBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankSubaccount a where a.userId=:userId", xxxxBankSubaccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static List<xxxxBankSubaccount> getBankByAccountNoAndUserId(String accountNo, String userId) {
    Query<xxxxBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankSubaccount a where a.accountNo=:accountNo and a.userId=:userId", xxxxBankSubaccount.class);
    query.setParameter("accountNo", accountNo);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  public static List<xxxxBankSubaccount> getListBank(String userId) {
    Query<xxxxBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankSubaccount a where a.userId=:userId order by id desc", xxxxBankSubaccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  @Step
  public static xxxxBankSubaccount getSubAccountByUserIdAndAccountType(String userId, String accountType) {
    Query<xxxxBankSubaccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankSubaccount a where a.userId=:userId and a.accountType=:accountType", xxxxBankSubaccount.class);
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
      "Delete from xxxxBankSubaccount a where a.accountNo=:accountNo");
    query.setParameter("accountNo", accountNo);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }
}
