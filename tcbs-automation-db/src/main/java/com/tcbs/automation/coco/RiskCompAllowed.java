package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "RISK_COMP_ALLOWED")
public class RiskCompAllowed {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "NCOMALLOW")
  private String ncomallow;

}
