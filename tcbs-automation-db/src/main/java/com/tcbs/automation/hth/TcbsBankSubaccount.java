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
@Table(name = "TCBS_BANK_SUBACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsBankSubaccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @Column(name = "STATUS")
  private String status;

}
