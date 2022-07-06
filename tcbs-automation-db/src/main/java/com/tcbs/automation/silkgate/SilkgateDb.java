package com.tcbs.automation.silkgate;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class SilkgateDb {
  public static final HibernateEdition silkgateDbConnection = Database.SILKGATE.getConnection();

}
