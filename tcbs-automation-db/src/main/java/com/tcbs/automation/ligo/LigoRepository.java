package com.tcbs.automation.ligo;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class LigoRepository {
  static HibernateEdition ligoDbConnection;

  static {
    try {
      ligoDbConnection = Database.LIGO.getConnection();
    } catch (Exception e) {
      System.out.println("Exception: " + e.toString());
    }
  }
}
