package com.tcbs.automation.otp_wt;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "OTP_USER_MAP")
public class OtpUserMapWT {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "TYPE_NAME")
  private String typeName;
  @Column(name = "IS_ENABLED")
  private String isEnabled;
  @Column(name = "IS_DEFAULT")
  private String isDefault;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;


}
