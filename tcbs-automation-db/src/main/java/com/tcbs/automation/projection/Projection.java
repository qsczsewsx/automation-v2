package com.tcbs.automation.projection;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Projection {
  public static final HibernateEdition projectionDbConnection = Database.PROJECTION.getConnection();
}