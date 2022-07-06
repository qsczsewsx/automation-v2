package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "PE_INDICES_SNAPSHOT")
public class PeIndicesSnapshot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "EOMONTH")
  private Date eomonth;
  @Column(name = "PORTF_INDEX")
  private Double portfIndex;
  @Column(name = "PORTF_MONRET_FACTOR")
  private Double portfMonretFactor;
  @Column(name = "PORTF_MONRET_FACTOR_LN")
  private Double portfMonretFactorLn;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "VALUE_AT_RISK")
  private Double valueAtRisk;
  @Column(name = "USER_ID")
  private String userId;

}
