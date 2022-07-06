package com.tcbs.automation.staging;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Staging {
  public static final HibernateEdition stagingDbConnection = Database.STAGING.getConnection();
}