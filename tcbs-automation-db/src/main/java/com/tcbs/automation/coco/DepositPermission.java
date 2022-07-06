package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "DEPOSIT_PERMISSION")
public class DepositPermission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "TCBSID")
  private String tcbsid;
  @Column(name = "CUSTODYID")
  private String custodyid;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "TD_PERMISSION")
  private String tdPermission;
  @Column(name = "UPDATE_TIME")
  private Timestamp updateTime;

}
