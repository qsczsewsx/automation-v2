package com.tcbs.automation.tcbsdbapi1;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class DbApi1 {
  public static final HibernateEdition dbApiDbConnection = Database.DBAPI1.getConnection();
}
