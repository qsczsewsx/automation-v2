package com.tcbs.automation.otp_wt;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Setter
@Getter
@Table(name = "TOTP")
public class TotpWT {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "SECRET")
  private String secret;


}
