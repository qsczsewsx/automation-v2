package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "RISK_CONFIG")
public class RiskConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "NAME")
  private String name;
  @Column(name = "VALUE")
  private Double value;
  @Column(name = "ID")
  private String id;
  @Column(name = "UPDATE_TIME")
  private String updateTime;

}
