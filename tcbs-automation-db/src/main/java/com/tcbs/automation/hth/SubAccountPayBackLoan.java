package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "SUB_ACCOUNT_PAY_BACK_LOAN")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubAccountPayBackLoan {

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
  @Column(name = "LOAN_KEY")
  private String loanKey;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "TRANSACTION_DATE")
  private Timestamp transactionDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

}
