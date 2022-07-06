package com.tcbs.automation.tcbond;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class TcBond {
  static HibernateEdition tcBondDbConnection;

  static {
    try {
      tcBondDbConnection = Database.TCBOND.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
