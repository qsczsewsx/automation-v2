package com.tcbs.automation.iris;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class IRis {
  public static final HibernateEdition iRisDbConnection = Database.IRIS.getConnection();
}
