package com.tcbs.automation.tcpoints;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Tcpoints {
  public static final HibernateEdition tcpointsDbConnection = Database.TCPOINTS.getConnection();
}