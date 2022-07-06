package com.tcbs.automation.evoting;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Vote {
  public static final HibernateEdition voteDbConnection = Database.EVOTING.getConnection();
}
