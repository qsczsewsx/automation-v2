package com.tcbs.automation.coman;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Connection {
  public static final HibernateEdition comanDbConnection = Database.COMAN.getConnection();
}
