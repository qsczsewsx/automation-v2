package com.tcbs.automation.otp_wt;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class OtpWTRepositoryWT {
  static HibernateEdition otpWTDbConnection;

  static {
    try {
      otpWTDbConnection = Database.OTP_WT.getConnection();
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}
