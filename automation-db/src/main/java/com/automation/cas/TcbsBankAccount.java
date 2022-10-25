package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "xxxx_BANK_ACCOUNT")
@Getter
@Setter
public class xxxxBankAccount {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "BANK_ACCOUNT_NO")
  private String bankAccountNo;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "BANK_ACCOUNT_NAME")
  private String bankAccountName;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BANK_BRANCH")
  private String bankBranch;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "ID_NAME")
  private String idName;
  @Column(name = "ID_DATE")
  private Date idDate;
  @Column(name = "ID_PLACE")
  private String idPlace;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "MAX_LIMIT")
  private BigDecimal maxLimit;
  @Column(name = "CURRENT_LIMIT")
  private BigDecimal currentLimit;
  @Column(name = "CASH_ACCOUNT_TYPE")
  private BigDecimal cashAccountType;

  @Step
  public static List<xxxxBankAccount> getListBanks(String userId) {
    Query<xxxxBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankAccount a where a.userId=:userId", xxxxBankAccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  public static xxxxBankAccount getBank(String userId) {
    Query<xxxxBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankAccount a where a.userId=:userId", xxxxBankAccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static List<xxxxBankAccount> getListByBankBranch(String bankBranch) {
    Query<xxxxBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankAccount a where a.bankBranch=:bankBranch", xxxxBankAccount.class);
    query.setParameter("bankBranch", bankBranch);
    return query.getResultList();
  }

  public static xxxxBankAccount getUserIdByAccountNo(String bankAccountNo) {
    Query<xxxxBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from xxxxBankAccount a where a.bankAccountNo=:bankAccountNo", xxxxBankAccount.class);
    query.setParameter("bankAccountNo", bankAccountNo);
    return query.getSingleResult();
  }
}
