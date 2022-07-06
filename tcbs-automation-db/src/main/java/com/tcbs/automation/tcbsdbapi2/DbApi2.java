package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class DbApi2 {
  public static final HibernateEdition dbApiDb2Connection = Database.DBAPI2.getConnection();
}
