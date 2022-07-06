package com.tcbs.automation.moksha;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class MoksaRepository {
  static HibernateEdition moksaDbConnection;

  static {
    try {
      moksaDbConnection = Database.MOKSHA.getConnection();
    } catch (Exception e) {
      System.out.println("Exception: " + e.toString());
    }
  }
}
