package com.tcbs.automation.otp_wt;

import lombok.Getter;
import lombok.Setter;

import java.sql.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "OTP_USER_DEVICES")
public class OtpUserDevicesWT {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "DEVICE_ID")
  private String deviceId;
  @Column(name = "DEVICE_INFO")
  private String deviceInfo;
  @Column(name = "ENABLE")
  private String enable;
  @Column(name = "CREATED")
  private Timestamp created;
  @Column(name = "LAST_MODIFIED")
  private Timestamp lastModified;


}
