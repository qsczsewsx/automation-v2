package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "PE_TRANSACTION_DETAIL")
public class PeTransactionDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ORDERID")
  private String orderid;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "EXECTYPE")
  private String exectype;
  @Column(name = "EXECQTTY")
  private String execqtty;
  @Column(name = "FEEACR")
  private String feeacr;
  @Column(name = "MATCHPRICE")
  private String matchprice;
  @Column(name = "TAXSELLAMOUNT")
  private String taxsellamount;
  @Column(name = "COSTPRICE")
  private String costprice;
  @Column(name = "TXDATE")
  private Date txdate;
  @Column(name = "TXTIME")
  private String txtime;

}
