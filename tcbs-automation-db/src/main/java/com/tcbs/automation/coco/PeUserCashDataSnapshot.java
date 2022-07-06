package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "PE_USER_CASH_DATA_SNAPSHOT")
public class PeUserCashDataSnapshot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "CASH_CREDIT")
  private Double cashCredit;
  @Column(name = "CASH_DEBIT")
  private Double cashDebit;
  @Column(name = "CASH_BALANCE")
  private Double cashBalance;
  @Column(name = "CASH_BALANCE_PREV")
  private Double cashBalancePrev;

}
