package com.tcbs.automation.config.iotp;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IOTPConfig {
  private static final Config conf = new ConfigImpl("iotp").getConf();

  // IOTP
  public static final String IOTP_DOMAIN = conf.getString("iotp.domain");
  public static final String IOTP_REGISTER = conf.getString("iotp.register");
  public static final String IOTP_VERIFY = conf.getString("iotp.verify");
  public static final String IOTP_CONFIRM = conf.getString("iotp.confirm");
  public static final String IOTP_UNREGISTER = conf.getString("iotp.unregister");
  public static final String IOTP_DOMAIN_NEW = conf.getString("iotp.domainnew");
  public static final String IOTP_EXCEPTION = conf.getString("iotp.smsexception");
  public static final String IOTP_SCAN_EXCEPTION = conf.getString("iotp.scansmsexception");
  public static final String IOTP_DOMAIN_BACK = conf.getString("iotp.domainback");
  public static final String IOTP_OTP_DETAIL = conf.getString("iotp.otpdetail");
  public static final String IOTP_OTP_DOMAIN_EXT = conf.getString("iotp.domainext");
  public static final String IOTP_OTP_TYPES = conf.getString("iotp.otptypes");
  public static final String IOTP_AUTHEN = conf.getString("iotp.authen");
  public static final String IOTP_DATAPOWER_DOMAIN = conf.getString("iotp.domaindatapowerext");
  public static final String IOTP_GET_PSK_BACK_END = conf.getString("iotp.getpsk");
  public static final String IOTP_SECURE = conf.getString("iotp.secure");
  public static final String IOTP_DOMAIN_EXT_OTP = conf.getString("iotp.domainextotp");
  public static final String IOTP_GET_SMS_BACK_END = conf.getString("iotp.getsmslog");
  public static final String IOTP_COMMON = conf.getString("iotp.common");
  public static final String IOTP_CACHE = conf.getString("iotp.cache");
  public static final String IOTP_FOTP = conf.getString("iotp.fotp");
  public static final String IOTP_AUTH = conf.getString("iotp.auth");
  public static final String IOTP_DOMAIN_DEVMANA = conf.getString("iotp.domaindevmana");
  public static final String IOTP_MESSAGE = conf.getString("iotp.message");
  public static final String IOTP_UNICAST_DEVICE = conf.getString("iotp.unicastdevice");
  public static final String IOTP_IOTP_IOTP = conf.getString("iotp.iotp");
  public static final String IOTP_IOTP_CANCEL_TICKET = conf.getString("iotp.canceliotpticket");
  public static final String IOTP_WT_DOMAIN = conf.getString("iotp_wt.domain");
  public static final String IOTP_AUTHEN_SOTP = conf.getString("iotp.authen_otp");
  public static final String IOTP_AUTHEN_STEPUP_OTP = conf.getString("iotp.authen_stepup_otp");


}
