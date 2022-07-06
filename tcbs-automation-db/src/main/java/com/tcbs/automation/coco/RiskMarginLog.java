package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "RISK_MARGIN_LOG")
public class RiskMarginLog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ERROR")
  private String error;
  @Column(name = "WARNING")
  private String warning;
  @Column(name = "CURRENT_PRICE")
  private String currentPrice;
  @Column(name = "CURRENT_RATIO")
  private String currentRatio;
  @Column(name = "ROOM_REMAIN")
  private String roomRemain;
  @Column(name = "ROOM_USED")
  private String roomUsed;
  @Column(name = "RATIO")
  private Double ratio;
  @Column(name = "VAR")
  private Double var;
  @Column(name = "AUTO_ROOM")
  private String autoRoom;
  @Column(name = "DEBT")
  private String debt;
  @Column(name = "EXC_ROOM")
  private String excRoom;
  @Column(name = "FINAL_ROOM")
  private String finalRoom;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "ID")
  private String id;

}
