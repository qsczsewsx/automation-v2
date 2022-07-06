package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "RISK_CONDITION")
public class RiskCondition {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "NAME")
  private String name;
  @Column(name = "OPERATOR")
  private String operator;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "REQUIRE")
  private String require;
  @Column(name = "ID")
  private String id;
  @Column(name = "UPDATE_TIME")
  private String updateTime;

}
