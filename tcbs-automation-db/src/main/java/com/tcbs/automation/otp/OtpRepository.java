package com.tcbs.automation.otp;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class OtpRepository {
  static HibernateEdition otpDbConnection;

  static {
    try {
      otpDbConnection = Database.OTP.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
