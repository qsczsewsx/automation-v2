package com.tcbs.automation.caservice;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class CAService {
  public static HibernateEdition caserviceConnection;

  static {
    try {
      caserviceConnection = Database.CASERVICE.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
