package com.tcbs.automation.cas;

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
@Table(name = "TCBS_BANK_ACCOUNT")
@Getter
@Setter
public class TcbsBankAccount {
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
  public static List<TcbsBankAccount> getListBanks(String userId) {
    Query<TcbsBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankAccount a where a.userId=:userId", TcbsBankAccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }

  public static TcbsBankAccount getBank(String userId) {
    Query<TcbsBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankAccount a where a.userId=:userId", TcbsBankAccount.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static List<TcbsBankAccount> getListByBankBranch(String bankBranch) {
    Query<TcbsBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankAccount a where a.bankBranch=:bankBranch", TcbsBankAccount.class);
    query.setParameter("bankBranch", bankBranch);
    return query.getResultList();
  }

  public static TcbsBankAccount getUserIdByAccountNo(String bankAccountNo) {
    Query<TcbsBankAccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankAccount a where a.bankAccountNo=:bankAccountNo", TcbsBankAccount.class);
    query.setParameter("bankAccountNo", bankAccountNo);
    return query.getSingleResult();
  }
}
