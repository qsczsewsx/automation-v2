package com.tcbs.automation.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Table(name = "OTP_USER_DEVICES")
@Entity
public class OtpUserDevice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "DEVICE_ID")
  private String deviceId;
  @Column(name = "DEVICE_INFO")
  private String deviceInfo;
  @Column(name = "ENABLE")
  private Integer enable;
  @Column(name = "CREATED")
  private Timestamp created;
  @Column(name = "LAST_MODIFIED")
  private Timestamp lastModified;

  @Step("get device active")
  public OtpUserDevice getActiveByUserId(String id) {
    Query<OtpUserDevice> query = OtpRepository.otpDbConnection.getSession().createQuery("from OtpUserDevice where tcbsId=:id and enable = true", OtpUserDevice.class);
    query.setParameter("id", id);
    val rs = query.getResultStream().findFirst().orElse(null);
    return rs;
  }

  @Step("get devices active")
  public List<OtpUserDevice> getDevicesActiveByUserId(String id) {
    Query<OtpUserDevice> query = OtpRepository.otpDbConnection.getSession().createQuery("from OtpUserDevice where tcbsId=:id and enable = true", OtpUserDevice.class);
    query.setParameter("id", id);
    val rs = query.getResultStream().collect(Collectors.toList());
    return rs;
  }

  @Step("get devices active by tcbs id")
  public static OtpUserDevice getDevicesActiveByTcbsId(String id) {
    Query<OtpUserDevice> query = OtpRepository.otpDbConnection.getSession().createQuery("from OtpUserDevice where tcbsId=:id and enable = 1", OtpUserDevice.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

}
