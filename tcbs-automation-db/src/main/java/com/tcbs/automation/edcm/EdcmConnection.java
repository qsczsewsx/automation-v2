package com.tcbs.automation.edcm;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class EdcmConnection {
  public static HibernateEdition connection;

  static {
    try {
      connection = Database.EDCM.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
