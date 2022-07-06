package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "RISK_IN_OUT_LOG")
public class RiskInOutLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "CUSTOM_CONDITION")
  private String customCondition;
  @Column(name = "CUSTOM_CONFIG")
  private String customConfig;
  @Column(name = "FLAG")
  private String flag;
  @Column(name = "YEAR_REPORT")
  private String yearReport;
  @Column(name = "LENGTH_REPORT")
  private String lengthReport;
  @Column(name = "CONDITION_ID")
  private String conditionId;
  @Column(name = "CONFIG_ID")
  private String configId;
  @Column(name = "UPDATE_TIME")
  private String updateTime;
  @NotNull
  @Column(name = "ID")
  private String id;

}
